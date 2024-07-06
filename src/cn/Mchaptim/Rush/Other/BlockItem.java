package cn.Mchaptim.Rush.Other;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import cn.Mchaptim.Core.PluginUtils.Color;

public class BlockItem {
	public ItemStack GetInventoryItemStack() {
		ItemStack item = new ItemStack(Material.SANDSTONE, 16, (short) 2);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Color.To("&7·½¿é"));
		item.setItemMeta(meta);
		return item;
	}
}
