package cn.Mchaptim.Rush;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import cn.Mchaptim.Core.PluginUtils.Color;
import cn.Mchaptim.Rush.Game.Game;

public class SpectatorInventory extends BukkitRunnable {
	Game game;
	Inventory GUI;
	ItemStack item;
	SkullMeta meta;

	public SpectatorInventory(Game game) {
		this.game = game;
		GUI = Bukkit.createInventory(null, 3 * 9, "游戏中玩家");
		item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		meta = (SkullMeta) item.getItemMeta();
		List<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.add(Color.To("&3➤ 点击传送"));
		meta.setLore(lore);
	}

	@Override
	public void run() {
		Iterator<Player> tmp = game.GamePlayers.iterator();
		int tmp2 = 0;
		while (tmp.hasNext()) {
			Player tmp3 = tmp.next();
			meta.setOwner(tmp3.getName());
			meta.setDisplayName(tmp3.getDisplayName());
			item.setItemMeta(meta);
			GUI.setItem(tmp2++, item);
		}
	}

	public void OpenGui(Player p) {
		p.openInventory(GUI);
	}

}
