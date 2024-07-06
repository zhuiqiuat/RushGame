package cn.Mchaptim.Rush.Config;

import java.io.File;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import cn.Mchaptim.Rush.Main;
import cn.Mchaptim.Rush.Utils.MySQL;

public class MainConfig {
	static String url;
	static int port;
	static String database;
	static String username;
	static String password;
	public static boolean BungeeMode;
	public static boolean GameMode;
	public static List<String> Lobbys;
	public static int StartSeconds;
	public static int TeamPlayers;
	public static int MaxPlayers;
	public static int WinScores;
	public static String WaitingMOTD;
	public static String GamingMOTD;
	public static String FullMOTD;
	public static String StoppingMOTD;
	public static String EndingMOTD;
	public static String Sb_Title;
	public static String Sb_Mode;
	public static String Sb_Version;
	public static String Sb_LastLine;

	public static void LoadConfig() {
		Main.m.reloadConfig();
		FileConfiguration config = Main.m.getConfig();
		url = config.getString("MySQL.url");
		port = config.getInt("MySQL.port");
		database = config.getString("MySQL.database");
		username = config.getString("MySQL.username");
		password = config.getString("MySQL.password");
		MySQL.ConnectToMySQL(url, port, database, username, password);
		MySQL.CreateTables();
		new BukkitRunnable() {
			@Override
			public void run() {
				MySQL.ConnectToMySQL(url, port, database, username, password);
			}
		}.runTaskTimer(Main.m, 72000L, 72000L);
		BungeeMode = config.getBoolean("BungeeMode");
		GameMode = config.getBoolean("GameMode");
		Lobbys = config.getStringList("Lobbys");
		StartSeconds = config.getInt("StartSeconds");
		TeamPlayers = config.getInt("TeamPlayers");
		MaxPlayers = config.getInt("MaxPlayers");
		WinScores = config.getInt("WinScores");
		WaitingMOTD = config.getString("MOTD.Waiting");
		GamingMOTD = config.getString("MOTD.Gaming");
		FullMOTD = config.getString("MOTD.Full");
		StoppingMOTD = config.getString("MOTD.Stopping");
		EndingMOTD = config.getString("MOTD.Ending");
		Sb_Title = config.getString("Scoreboard.Title");
		Sb_Mode = config.getString("Scoreboard.Mode");
		Sb_Version = config.getString("Scoreboard.Version");
		Sb_LastLine = config.getString("Scoreboard.LastLine");
		File f = new File(Main.m.getDataFolder(), "Map.yml");
		if (f.exists()) {
			MapConfig.LoadMap();
		}
	}
}
