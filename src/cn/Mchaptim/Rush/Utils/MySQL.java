package cn.Mchaptim.Rush.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL {
	public static Connection con = null;

	public static void ConnectToMySQL(String url, int port, String database, String username, String password) {
		try {
			con = DriverManager.getConnection("jdbc:mysql://" + url + ":" + port + "/" + database + "?useSSL=false",
					username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void CreateTables() {
		try {
			Statement stmt = con.createStatement();
			stmt.execute(
					"create table if not exists `RushPlayerData`(`UUID` text,`BreakBed` int,`Wins` int,`Xps` int,"
							+ "`Levels` int,`StickSlot` int,`PickaxeSlot` int,`BlockSlot` int,`BlockSkin` int,`StickSkin` int);");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
