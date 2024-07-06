package cn.Mchaptim.Rush.Config;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import cn.Mchaptim.Core.PluginUtils.Color;
import cn.Mchaptim.Rush.Main;
import cn.Mchaptim.Rush.Game.Game;
import cn.Mchaptim.Rush.Game.GameStats;

public class MapConfig {
	static File MapFile = new File(Main.m.getDataFolder(), "Map.yml");
	static FileConfiguration MapConfig = YamlConfiguration.loadConfiguration(MapFile);
	static String TmpGameName;

	public static boolean SetMap(Player p, String[] args) {
		if (args.length == 3 && args[1].equalsIgnoreCase("create")) {
			TmpGameName = args[2];
			MapConfig.set("Games", TmpGameName);
			MapConfig.set("Games." + TmpGameName + ".GameName", args[2]);
			p.sendMessage(Color.To("&a创建成功"));
			return true;
		}
		if (args.length == 3 && args[1].equalsIgnoreCase("setlobby")) {
			if (TmpGameName != null && args[2].equalsIgnoreCase(TmpGameName)) {
				MapConfig.set("Games." + TmpGameName + ".Lobby", p.getLocation());
				p.sendMessage(Color.To("&a设置成功"));
				return true;
			}
		}
		if (args.length == 3 && args[1].equalsIgnoreCase("setredspawn")) {
			if (TmpGameName != null && args[2].equalsIgnoreCase(TmpGameName)) {
				MapConfig.set("Games." + TmpGameName + ".Team.Red.Spawn", p.getLocation());
				p.sendMessage(Color.To("&a设置成功"));
				return true;
			}
		}
		if (args.length == 3 && args[1].equalsIgnoreCase("setbluespawn")) {
			if (TmpGameName != null && args[2].equalsIgnoreCase(TmpGameName)) {
				MapConfig.set("Games." + TmpGameName + ".Team.Blue.Spawn", p.getLocation());
				p.sendMessage(Color.To("&a设置成功"));
				return true;
			}
		}
		if (args.length == 3 && args[1].equalsIgnoreCase("setredbed")) {
			Block downblock = p.getLocation().getBlock();
			if (TmpGameName != null && args[2].equalsIgnoreCase(TmpGameName)
					&& downblock.getType().equals(Material.BED_BLOCK)) {
				MapConfig.set("Games." + TmpGameName + ".Team.Red.Bed", downblock.getLocation());
				p.sendMessage(Color.To("&a设置成功"));
				return true;
			}
		}
		if (args.length == 3 && args[1].equalsIgnoreCase("setbluebed")) {
			Block downblock = p.getLocation().getBlock();
			if (TmpGameName != null && args[2].equalsIgnoreCase(TmpGameName)
					&& downblock.getType().equals(Material.BED_BLOCK)) {
				MapConfig.set("Games." + TmpGameName + ".Team.Blue.Bed", downblock.getLocation());
				p.sendMessage(Color.To("&a设置成功"));
				return true;
			}
		}
		if (args.length == 3 && args[1].equalsIgnoreCase("setspec")) {
			if (TmpGameName != null && args[2].equalsIgnoreCase(TmpGameName)) {
				MapConfig.set("Games." + TmpGameName + ".Spec", p.getLocation());
				p.sendMessage(Color.To("&a设置成功"));
				return true;
			}
		}
		if (args.length == 3 && args[1].equalsIgnoreCase("setloc1")) {
			if (TmpGameName != null && args[2].equalsIgnoreCase(TmpGameName)) {
				MapConfig.set("Games." + TmpGameName + ".Loc1", p.getLocation());
				p.sendMessage(Color.To("&a设置成功"));
				return true;
			}
		}
		if (args.length == 3 && args[1].equalsIgnoreCase("setloc2")) {
			if (TmpGameName != null && args[2].equalsIgnoreCase(TmpGameName)) {
				MapConfig.set("Games." + TmpGameName + ".Loc2", p.getLocation());
				p.sendMessage(Color.To("&a设置成功"));
				return true;
			}
		}
		if (args.length == 3 && args[1].equalsIgnoreCase("save")) {
			try {
				if (!MapFile.exists()) {
					MapFile.createNewFile();
				}
				MapConfig.save(MapFile);
				p.sendMessage(Color.To("&a保存成功"));
				LoadMap();
				return true;
			} catch (IOException e) {
				p.sendMessage(Color.To("&c保存失败"));
				e.printStackTrace();
				return true;
			}
		}
		if (args.length < 3 && args.length > 3) {
			p.sendMessage(Color.To("&c参数有误"));
			return true;
		}
		return true;
	}

	public static void LoadMap() {
		if (!MainConfig.GameMode) {
			return;
		}
		Set<String> keys = MapConfig.getConfigurationSection("Games").getKeys(true);
		Iterator<String> it = keys.iterator();
		while (it.hasNext()) {
			String tmp = it.next();
			if (tmp.lastIndexOf(".") != -1) {
				continue;
			}
			Game game = new Game();
			game.GameName = MapConfig.getString("Games." + tmp + ".GameName");
			game.LobbyLoc = (Location) MapConfig.get("Games." + game.GameName + ".Lobby");
			game.RedSpawn = (Location) MapConfig.get("Games." + game.GameName + ".Team.Red.Spawn");
			game.BlueSpawn = (Location) MapConfig.get("Games." + game.GameName + ".Team.Blue.Spawn");
			game.RedBed = (Location) MapConfig.get("Games." + game.GameName + ".Team.Red.Bed");
			game.BlueBed = (Location) MapConfig.get("Games." + game.GameName + ".Team.Blue.Bed");
			game.SpecSpawn = (Location) MapConfig.get("Games." + game.GameName + ".Spec");
			game.Min = Vector.getMinimum(((Location) MapConfig.get("Games." + game.GameName + ".Loc1")).toVector(),
					((Location) MapConfig.get("Games." + game.GameName + ".Loc2")).toVector());
			game.Max = Vector.getMaximum(((Location) MapConfig.get("Games." + game.GameName + ".Loc1")).toVector(),
					((Location) MapConfig.get("Games." + game.GameName + ".Loc2")).toVector());
			game.SetGameStat(GameStats.WAITING);
			Main.Games.add(game);

		}
	}
}
