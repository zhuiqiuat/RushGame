package cn.Mchaptim.Rush.Listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import cn.Mchaptim.Rush.Main;
import cn.Mchaptim.Rush.Game.Game;

public class Entitys implements Listener {

	@EventHandler
	void OnEntityDamage(EntityDamageEvent e) {
		if (!e.getEntityType().equals(EntityType.PLAYER)) {
		}
		Player p = Bukkit.getPlayer(e.getEntity().getName());
		Game game = Main.GetGame(p);
		if (game == null) {
			return;
		}
		switch (game.GetStat()) {
		case WAITING:
		case FULL:
			switch (e.getCause()) {
			case VOID:
				p.teleport(game.LobbyLoc);
			default:
				e.setCancelled(true);
				break;
			}
			break;
		case GAMING:
			if (game.isSpectator(p)) {
				e.setCancelled(true);
			}
			switch (e.getCause()) {
			case ENTITY_ATTACK:
				e.setDamage(0);
				break;
			default:
				e.setCancelled(true);
				break;
			}
			break;
		default:
			e.setCancelled(true);
			break;
		}
	}

	@EventHandler
	void OnFoodLevelChange(FoodLevelChangeEvent e) {
		e.setFoodLevel(20);
	}
}
