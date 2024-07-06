package cn.Mchaptim.Rush.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.github.paperspigot.Title;

//import com.destroystokyo.paper.Title; 1.12.2 Paper

import cn.Mchaptim.Core.PluginUtils.Color;
import cn.Mchaptim.Rush.Main;
import cn.Mchaptim.Rush.SpectatorInventory;
import cn.Mchaptim.Rush.Config.MainConfig;
import cn.Mchaptim.Rush.Other.BlockItem;
import cn.Mchaptim.Rush.Other.LobbyItem;
import cn.Mchaptim.Rush.Other.PickaxeItem;
import cn.Mchaptim.Rush.Other.PlayerData;
import cn.Mchaptim.Rush.Other.SpectatorItem;
import cn.Mchaptim.Rush.Other.StickItem;
import cn.Mchaptim.Rush.Utils.Time;

public class Game {
	public String GameName;
	public Location LobbyLoc;
	public Location RedSpawn;
	public Location BlueSpawn;
	public Location RedBed;
	public Location BlueBed;
	public Location SpecSpawn;
	public Vector Min;
	public Vector Max;
	GameStats Stat;
	public Time t;
	public List<Player> GamePlayers = new ArrayList<>();
	public Set<Player> SpectatorPlayers = new HashSet<>();
	Map<Player, ScoreBoard> ScoreBoards = new HashMap<>();
	Map<Teams, TeamData> GameTeams = new HashMap<>();
	public List<Location> BlockLoc = new ArrayList<>();
	public SpectatorInventory SpecGui = new SpectatorInventory(this);

	public void StartGame() {
		t.cancel();
		Iterator<Player> it = GamePlayers.iterator();
		int tmp = 0;
		while (it.hasNext()) {
			Player p = it.next();
			if (HasTeam(p)) {
				continue;
			}
			Teams tmp2 = Teams.values()[tmp];
			if (GameTeams.containsKey(tmp2) && GameTeams.get(tmp2).TeamPlayers.size() == MainConfig.TeamPlayers) {
				if (tmp == Teams.values().length - 1) {
					AddSpectatorInGame(p);
					continue;
				}
				tmp2 = Teams.values()[++tmp];
			}
			SetTeam(p, tmp2);
		}
		SendAllToSpawn();
		Iterator<Player> tmp2 = SpectatorPlayers.iterator();
		while (tmp2.hasNext()) {
			Player tmp3 = tmp2.next();
			if (GamePlayers.contains(tmp3)) {
				GamePlayers.remove(tmp3);
			}
		}
		SpecGui.runTaskTimer(Main.m, 0L, 2L);
		SetGameStat(GameStats.GAMING);
	}

	public void JoinGame(Player p) {
		switch (GetStat()) {
		case FULL:
		case WAITING:
			if (!(p.getGameMode() == GameMode.SURVIVAL)) {
				p.setGameMode(GameMode.SURVIVAL);
			}
			p.getInventory().clear();
			GamePlayers.add(p);
			for (Player tmp : GamePlayers) {
				tmp.sendMessage(Color.To("&b" + p.getName() + "&f加入了游戏"));
			}
			p.teleport(LobbyLoc);
			p.getInventory().setItem(8, new LobbyItem().GetInventoryItemStack());
			if (GamePlayers.size() == MainConfig.MaxPlayers) {
				SetGameStat(GameStats.FULL);
				t = new Time(this);
				t.runTaskTimer(Main.m, 0L, 20L);
			}
			ScoreBoard sb = new ScoreBoard(this, p);
			sb.runTaskTimer(Main.m, 0L, 2L);
			ScoreBoards.put(p, sb);
			Main.PlayerDatas.put(p, new PlayerData(p));
			break;
		case GAMING:
			AddSpectatorInGame(p);
			break;
		default:
			break;
		}
	}

	public void LeaveGame(Player p) {
		switch (GetStat()) {
		case FULL:
			if (t != null && GamePlayers.size() < MainConfig.MaxPlayers) {
				t.cancel();
				t.Second = MainConfig.StartSeconds;
				if (!(GetStat() == GameStats.WAITING)) {
					SetGameStat(GameStats.WAITING);
				}
			}
		case WAITING:
			if (Main.PlayerDatas.containsKey(p)) {
				Main.PlayerDatas.remove(p);
			}
			if (ScoreBoards.containsKey(p)) {
				ScoreBoards.get(p).cancel();
				ScoreBoards.remove(p);
			}
			GamePlayers.remove(p);
			for (Player tmp : GamePlayers) {
				tmp.sendMessage(Color.To("&b" + p.getName() + "&c退出了游戏"));
			}
			break;
		case GAMING:
			if (Main.PlayerDatas.containsKey(p)) {
				PlayerData pd = Main.PlayerDatas.get(p);
				pd.UpdatePlayerDataToMySQL();
				Main.PlayerDatas.remove(p);
			}
			if (ScoreBoards.containsKey(p)) {
				ScoreBoards.get(p).cancel();
				ScoreBoards.remove(p);
			}
			if (isSpectator(p)) {
				p.setAllowFlight(false);
				Iterator<Player> tmp = GamePlayers.iterator();
				while (tmp.hasNext()) {
					Player tmp2 = tmp.next();
//					tmp2.showPlayer(Main.m, p); 1.12.2 Paper
					tmp2.showPlayer(p);
				}
				return;
			}
			GamePlayers.remove(p);
			Teams tmp = GetTeam(p);
			GameTeams.get(tmp).TeamPlayers.remove(p);
			if (GameTeams.get(tmp).TeamPlayers.size() == 0) {
				GameTeams.remove(tmp);
			}
			for (Player tmp2 : GamePlayers) {
				tmp2.sendMessage(Color.To("&b" + p.getName() + "&c退出了游戏"));
			}
			if (GameTeams.keySet().size() == 1) {
				WinGame(GameTeams.keySet().iterator().next());
				SetGameStat(GameStats.STOPPTING);
			}
			break;
		default:
			if (ScoreBoards.containsKey(p)) {
				ScoreBoards.get(p).cancel();
				ScoreBoards.remove(p);
			}
			if (isSpectator(p)) {
				p.setAllowFlight(false);
				Iterator<Player> tmp1 = GamePlayers.iterator();
				while (tmp1.hasNext()) {
					Player tmp2 = tmp1.next();
//					tmp2.showPlayer(Main.m, p); 1.12.2 Paper
					tmp2.showPlayer(p);
				}
				return;
			}
			break;
		}
	}

	public void WinGame(Teams team) {
		SetGameStat(GameStats.ENDING);
		List<Player> tmp = GameTeams.get(team).TeamPlayers;
		Iterator<Player> it = tmp.iterator();
		Title title;
		Player p;
		PlayerData pd;
		Iterator<Player> tmp2 = SpectatorPlayers.iterator();
		while (tmp2.hasNext()) {
			Player tmp3 = tmp2.next();
			title = new Title(Color.To("&c&l游戏结束"), Color.To("&7你没有获得本局游戏的胜利"));
			tmp3.sendTitle(title);
		}
		while (it.hasNext()) {
			p = it.next();
			pd = Main.PlayerDatas.get(p);
			pd.Wins++;
			pd.UpdatePlayerDataToMySQL();
			title = new Title(Color.To("&6&l你赢了"), " ");
			p.sendTitle(title);
			AddSpectatorInGame(p);
			GamePlayers.remove(p);
			Main.PlayerDatas.remove(p);
		}
		it = GamePlayers.iterator();
		while (it.hasNext()) {
			p = it.next();
			pd = Main.PlayerDatas.get(p);
			pd.UpdatePlayerDataToMySQL();
			Main.PlayerDatas.remove(p);
			title = new Title(Color.To("&c&l你输了"), " ");
			p.sendTitle(title);
			AddSpectatorInGame(p);
		}
		SpecGui.cancel();
		SpectatorPlayers.clear();
		GamePlayers.clear();
		new BukkitRunnable() {
			@Override
			public void run() {
				Iterator<? extends Player> tmp3 = Bukkit.getOnlinePlayers().iterator();
				while (tmp3.hasNext()) {
					tmp3.next().chat("/rushgame leave");
				}
			}
		}.runTaskLater(Main.m, 60L);
		new BukkitRunnable() {
			@Override
			public void run() {
				Bukkit.shutdown();
			}
		}.runTaskLater(Main.m, 100L);
	}

	public void SendAllToSpawn() {
		Iterator<Player> it = GamePlayers.iterator();
		while (it.hasNext()) {
			Player p = it.next();
			SendToSpawn(p);
		}
	}

	public Teams GetBedTeam(Location loc) {
		Location red = RedBed;
		Location blue = BlueBed;
		if (red.equals(loc)) {
			return Teams.RED;
		}
		if (red.getY() == loc.getY() && red.getZ() == loc.getZ()) {
			if (red.getX() + 1 == loc.getX() || red.getX() - 1 == loc.getX()) {
				return Teams.RED;
			}
		}
		if (red.getX() == loc.getX() && red.getY() == loc.getY()) {
			if (red.getZ() + 1 == loc.getZ() || red.getZ() - 1 == loc.getZ()) {
				return Teams.RED;
			}
		}
		if (blue.equals(loc)) {
			return Teams.BLUE;
		}
		if (blue.getY() == loc.getY() && blue.getZ() == loc.getZ()) {
			if (blue.getX() + 1 == loc.getX() || blue.getX() - 1 == loc.getX()) {
				return Teams.BLUE;
			}
		}
		if (blue.getX() == loc.getX() && blue.getY() == loc.getY()) {
			if (blue.getZ() + 1 == loc.getZ() || blue.getZ() - 1 == loc.getZ()) {
				return Teams.BLUE;
			}
		}
		return null;
	}

	public Teams GetTeam(Player p) {
		Set<Teams> tmp = GameTeams.keySet();
		Iterator<Teams> it = tmp.iterator();
		while (it.hasNext()) {
			Teams tmp2 = it.next();
			List<Player> tmp3 = GameTeams.get(tmp2).TeamPlayers;
			Iterator<Player> it2 = tmp3.iterator();
			while (it2.hasNext()) {
				if (it2.next().equals(p)) {
					return tmp2;
				}
			}
		}
		return null;
	}

	public boolean HasTeam(Player p) {
		return GetTeam(p) != null;
	}

	public void SetTeam(Player p, Teams team) {
		if (GameTeams.containsKey(team)) {
			GameTeams.get(team).TeamPlayers.add(p);
			return;
		}
		TeamData teamData = new TeamData(team);
		teamData.TeamPlayers = new ArrayList<>();
		teamData.TeamPlayers.add(p);
		GameTeams.put(team, teamData);
	}

	public void SendToSpawn(Player p) {
		SetItemSlot(p);
		p.teleport(GetSpawn(p));
	}

	public Location GetSpawn(Player p) {
		Teams tmp = GetTeam(p);
		switch (tmp) {
		case RED:
			return RedSpawn;
		case BLUE:
			return BlueSpawn;
		default:
			return null;
		}
	}

	public int GetScore(Teams team) {
		return GameTeams.get(team).Score;
	}

	public void AddScore(Teams team) {
		Title title;
		Iterator<Player> it = GamePlayers.iterator();
		GameTeams.get(team).Score += 1;
		if (GameTeams.get(team).Score == MainConfig.WinScores) {
			WinGame(team);
			return;
		}
		title = new Title(" ", Color.To((team == Teams.RED ? "&c红队" : "&9蓝队") + "获得一分"));
		while (it.hasNext()) {
			Player p = it.next();
			p.sendTitle(title);
		}
	}

	public void ClearBlock() {
		Iterator<Location> it = BlockLoc.iterator();
		while (it.hasNext()) {
			Location loc = it.next();
			loc.getBlock().setType(Material.AIR);
		}
		BlockLoc.clear();
	}

	public void SetItemSlot(Player p) {
		PlayerData pd = Main.PlayerDatas.get(p);
		Inventory inv = p.getInventory();
		inv.clear();
		inv.setItem(pd.StickSlot, new StickItem().GetInventoryItemStack());
		inv.setItem(pd.PickaxeSlot, new PickaxeItem().GetInventoryItemStack());
		inv.setItem(pd.BlockSlot, new BlockItem().GetInventoryItemStack());
	}

	public void AddSpectatorInGame(Player p) {
		SpectatorPlayers.add(p);
		p.getInventory().clear();
		p.teleport(SpecSpawn);
		Iterator<Player> tmp = GamePlayers.iterator();
		while (tmp.hasNext()) {
			Player tmp2 = tmp.next();
//			tmp2.hidePlayer(Main.m, p); 1.12.2 Paper
			tmp2.hidePlayer(p);
		}
		p.getInventory().setItem(0, new SpectatorItem().GetInventoryItemStack());
		p.getInventory().setItem(8, new LobbyItem().GetInventoryItemStack());
		p.setAllowFlight(true);
		p.setFlySpeed((float) 0.1);
		p.setFlying(true);
	}

	public boolean isSpectator(Player p) {
		Iterator<Player> tmp = SpectatorPlayers.iterator();
		while (tmp.hasNext()) {
			if (tmp.next().equals(p)) {
				return true;
			}
		}
		return false;
	}

	public GameStats GetStat() {
		return Stat;
	}

	public void SetGameStat(GameStats Stat) {
		this.Stat = Stat;
	}
}
