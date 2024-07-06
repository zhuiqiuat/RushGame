package cn.Mchaptim.Rush.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import cn.Mchaptim.Core.PluginUtils.Color;
import cn.Mchaptim.Rush.Main;
import cn.Mchaptim.Rush.Config.MainConfig;
import cn.Mchaptim.Rush.Game.Game;
import cn.Mchaptim.Rush.Game.Teams;
import cn.Mchaptim.Rush.Other.LobbyItem;
import cn.Mchaptim.Rush.Other.SpectatorItem;

public class Players implements Listener {
	@EventHandler
	void OnChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		Game game = Main.GetGame(p);
		if (game == null) {
			e.setFormat(Color.To("&f" + p.getDisplayName() + "&f: ") + e.getMessage());
		}
		switch (game.GetStat()) {
		case ENDING:
		case WAITING:
		case FULL:
			e.setFormat(Color.To("&f" + p.getDisplayName() + "&f: ") + e.getMessage());
			break;
		case GAMING:
			e.setFormat(
					Color.To((game.GetTeam(p) == Teams.RED ? "&c[红队]" : "&9[蓝队]") + "&f " + p.getDisplayName() + "&f: ")
							+ e.getMessage());
		default:
			break;
		}
	}

	@EventHandler
	void OnDrop(PlayerDropItemEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	void OnInteract(PlayerInteractEvent e) {
		if (e.getItem() == null) {
			return;
		}
		Player p = e.getPlayer();
		if (e.getItem().equals(new LobbyItem().GetInventoryItemStack())) {
			p.sendMessage(Color.To("&7正在将你传送到大厅"));
			p.chat("/rushgame leave");
			return;
		}
		if (e.getItem().equals(new SpectatorItem().GetInventoryItemStack())) {
			Game tmp = Main.GetGame(p);
			if (tmp == null) {
				return;
			}
			if (tmp.isSpectator(p)) {
				tmp.SpecGui.OpenGui(p);
				return;
			}
		}
	}

	@EventHandler
	public void OnJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (MainConfig.BungeeMode) {
			e.setJoinMessage(null);
			if (Main.Games.size() == 0)
				return;
			Game game = Main.Games.get(0);
			game.JoinGame(p);
		}
	}

	@EventHandler
	void OnMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		Game game = Main.GetGame(p);
		if (game == null) {
			return;
		}
		switch (game.GetStat()) {
		case GAMING:
			if (e.getTo().getY() < game.Min.toLocation(p.getWorld()).getY()) {
				game.SendToSpawn(p);
			}
			break;
		default:
			break;
		}
	}

	@EventHandler
	void OnQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		Game game = Main.GetGame(p);
		if (game == null) {
			return;
		}
		game.LeaveGame(p);
		if (MainConfig.BungeeMode) {
			e.setQuitMessage(null);
			if (p.getInventory().contains(new LobbyItem().GetInventoryItemStack())) {
				p.getInventory().remove(new LobbyItem().GetInventoryItemStack());
			}
		}

	}
}
