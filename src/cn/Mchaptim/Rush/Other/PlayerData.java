package cn.Mchaptim.Rush.Other;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;

import cn.Mchaptim.Rush.Utils.MySQL;

public class PlayerData {
	PreparedStatement ps = null;
	Player p;
	public int BreakBed = 0;
	public int Wins = 0;
	public int Xps = 0;
	public int Levels = 1;
	public int StickSlot = 0;
	public int PickaxeSlot = 1;
	public int BlockSlot = 2;
	public int BlockSkin = -1;
	public int StickSkin = -1;

	public PlayerData(Player p) {
		try {
			ps = MySQL.con.prepareStatement("select * from RushPlayerData where UUID = ?");
			ps.setString(1, p.getUniqueId().toString());
			ResultSet rs = ps.executeQuery();
			rs.next();
			this.p = p;
			this.BreakBed = rs.getInt("BreakBed");
			this.Wins = rs.getInt("Wins");
			this.Xps = rs.getInt("Xps");
			this.Levels = rs.getInt("Levels");
			this.StickSlot = rs.getInt("StickSlot");
			this.PickaxeSlot = rs.getInt("PickaxeSlot");
			this.BlockSlot = rs.getInt("BlockSlot");
			this.BlockSkin = rs.getInt("BlockSkin");
			this.StickSkin = rs.getInt("StickSkin");

		} catch (Exception e) {
			if (e.getMessage().equals("Illegal operation on empty result set.")) {
				try {
					ps = MySQL.con.prepareStatement(
							"insert into RushPlayerData(UUID,BreakBed,Wins,Xps,Levels,StickSlot,PickaxeSlot,BlockSlot,BlockSkin,StickSkin)"
									+ "values(?,0,0,0,1,0,1,2,-1,-1);");
					ps.setString(1, p.getUniqueId().toString());
					ps.execute();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			} else {
				e.printStackTrace();
			}

		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void UpdatePlayerDataToMySQL() {
		try {
			ps = MySQL.con.prepareStatement("update RushPlayerData set BreakBed=" + BreakBed + ", Wins=" + Wins
					+ ", Xps=" + Xps + ", Levels=" + Levels + " where UUID =?;");
			ps.setString(1, p.getUniqueId().toString());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
