package cn.Mchaptim.Rush.Listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import cn.Mchaptim.Core.PluginUtils.Color;
import cn.Mchaptim.Rush.Main;
import cn.Mchaptim.Rush.Game.Game;
import cn.Mchaptim.Rush.Game.Teams;
import cn.Mchaptim.Rush.Other.PlayerData;

public class Blocks implements Listener {

	@EventHandler
	void OnBreak(BlockBreakEvent e) {
		Game game = Main.GetGame(e.getPlayer());
		if (game == null) {
			return;
		}
		switch (game.GetStat()) {
		case GAMING:
			Block b = e.getBlock();
			e.setCancelled(true);
			if (game.BlockLoc.contains(b.getLocation())) {
				b.setType(Material.AIR);
			}
			if (b.getType().equals(Material.BED_BLOCK)) {
				Player p = e.getPlayer();
				Location bedloc = b.getLocation();
				Teams team = game.GetBedTeam(bedloc);
				if (team.equals(game.GetTeam(p))) {
					p.sendMessage(Color.To("&c你不能拆你自己的床"));
					return;
				}
				PlayerData pd = Main.PlayerDatas.get(p);
				pd.BreakBed++;
				game.ClearBlock();
				game.AddScore(game.GetTeam(p));
				game.SendAllToSpawn();
			}
			break;
		case STOPPTING:
			break;
		default:
			e.setCancelled(true);
			break;
		}
	}

	@EventHandler
	void OnPlace(BlockPlaceEvent e) {
		Game game = Main.GetGame(e.getPlayer());
		if (game == null) {
			return;
		}
		switch (game.GetStat()) {
		case GAMING:
			Block b = e.getBlock();
			if (!b.getLocation().toVector().isInAABB(game.Min, game.Max)) {
				e.setCancelled(true);
				e.getPlayer().sendMessage(Color.To("&7你在游戏区域外放方块"));
				return;
			}
			e.getItemInHand().setAmount(16);
			game.BlockLoc.add(b.getLocation());
			break;
		case STOPPTING:
			break;
		default:
			e.setCancelled(true);
			break;
		}
	}
}
