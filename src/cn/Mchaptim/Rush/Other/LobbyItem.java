package cn.Mchaptim.Rush.Other;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import cn.Mchaptim.Core.PluginUtils.Color;

public class LobbyItem {
	public ItemStack GetInventoryItemStack() {
		ItemStack item = new ItemStack(Material.SLIME_BALL);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Color.To("&7·µ»Ø´óÌü"));
		item.setItemMeta(meta);
		return item;
	}
}
