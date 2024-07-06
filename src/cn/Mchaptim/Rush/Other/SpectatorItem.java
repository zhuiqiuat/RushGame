package cn.Mchaptim.Rush.Other;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import cn.Mchaptim.Core.PluginUtils.Color;

public class SpectatorItem {
	public ItemStack GetInventoryItemStack() {
		ItemStack item = new ItemStack(Material.COMPASS);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Color.To("&7π€≤Ï’ﬂ≤Àµ•"));
		item.setItemMeta(meta);
		return item;
	}
}
