package cn.Mchaptim.Rush.Other;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import cn.Mchaptim.Core.PluginUtils.Color;

public class PickaxeItem {
	public ItemStack GetInventoryItemStack() {
		ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Color.To("&7¸å×Ó"));
		item.setItemMeta(meta);
		return item;
	}
}
