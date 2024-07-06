package cn.Mchaptim.Rush.Listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import cn.Mchaptim.Core.PluginUtils.Color;

public class Inventorys implements Listener {
	@EventHandler
	void OnClick(InventoryClickEvent e) {
		Player p = Bukkit.getPlayer(e.getWhoClicked().getName());
		Inventory Gui = e.getInventory();
		ItemStack item = e.getCurrentItem();
		if (item == null || !item.hasItemMeta()) {
			return;
		}
		if (Gui.getTitle().equals("��Ϸ�����")) {
			e.setCancelled(true);
			Player tmp = Bukkit.getPlayer(item.getItemMeta().getDisplayName());
			p.teleport(tmp);
			p.closeInventory();
			p.sendMessage(Color.To("&7�ѽ������͵�&f" + tmp.getName()));
			return;
		}
	}
}
