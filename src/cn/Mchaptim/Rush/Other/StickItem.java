package cn.Mchaptim.Rush.Other;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import cn.Mchaptim.Core.PluginUtils.Color;

public class StickItem {
	public ItemStack GetInventoryItemStack() {
		ItemStack item = new ItemStack(Material.STICK);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Color.To("&7»÷ÍË°ô"));
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
		return item;
	}
}
