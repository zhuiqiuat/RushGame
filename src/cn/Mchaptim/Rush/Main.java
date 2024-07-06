package cn.Mchaptim.Rush;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import cn.Mchaptim.Rush.Config.MainConfig;
import cn.Mchaptim.Rush.Game.Game;
import cn.Mchaptim.Rush.Listener.Blocks;
import cn.Mchaptim.Rush.Listener.Entitys;
import cn.Mchaptim.Rush.Listener.Inventorys;
import cn.Mchaptim.Rush.Listener.Players;
import cn.Mchaptim.Rush.Listener.Servers;
import cn.Mchaptim.Rush.Other.PlayerData;

public class Main extends JavaPlugin {
	public static Main m;
	public static Map<Player, PlayerData> PlayerDatas = new HashMap<>();
	public static List<Game> Games = new ArrayList<>();

	public static Game GetGame(Player p) {
		Iterator<Game> it = Games.iterator();
		while (it.hasNext()) {
			Game game = it.next();
			if (game.GamePlayers.contains(p))
				return game;
			if (game.SpectatorPlayers.contains(p)) {
				return game;
			}
		}
		return null;
	}

	@Override
	public void onEnable() {
		m = this;
		saveDefaultConfig();
		MainConfig.LoadConfig();
		getCommand("rushgame").setExecutor(new Commannds());
		Bukkit.getPluginManager().registerEvents(new Players(), this);
		Bukkit.getPluginManager().registerEvents(new Servers(), this);
		Bukkit.getPluginManager().registerEvents(new Entitys(), this);
		Bukkit.getPluginManager().registerEvents(new Blocks(), this);
		Bukkit.getPluginManager().registerEvents(new Inventorys(), this);
	}

	@Override
	public void onDisable() {
		Iterator<Game> it = Games.iterator();
		while (it.hasNext()) {
			Game game = it.next();
			game.ClearBlock();
		}
//		Iterator<? extends Player> it2 = Bukkit.getOnlinePlayers().iterator();
//		while (it2.hasNext()) {
//			Player p = it2.next();
//			PlayerDatas.get(p).UpdatePlayerDataToMySQL();
//		}
	}

}
