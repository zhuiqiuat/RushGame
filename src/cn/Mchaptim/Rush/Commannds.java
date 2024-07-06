package cn.Mchaptim.Rush;

import java.util.Random;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cn.Mchaptim.Core.PluginUtils.BungeeCord;
import cn.Mchaptim.Core.PluginUtils.Color;
import cn.Mchaptim.Rush.Config.MainConfig;
import cn.Mchaptim.Rush.Config.MapConfig;

public class Commannds implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String labers, String[] args) {
		if (args.length == 1 && args[0].equalsIgnoreCase("reload") && sender.hasPermission("rg.admin")) {
			MainConfig.LoadConfig();
			sender.sendMessage(Color.To("&a�������سɹ�"));
			return true;
		}
		if (!(sender instanceof Player)) {
			sender.sendMessage(Color.To("&c�������һ�����"));
			return true;
		}
		Player p = (Player) sender;
		if (args.length == 1 && args[0].equalsIgnoreCase("leave")) {
			Random r = new Random();
			String lobby = MainConfig.Lobbys.get(r.nextInt(MainConfig.Lobbys.size()));
			BungeeCord.ConnectTo(p, lobby);
			return true;
		}
		if (args.length > 1 && args[0].equalsIgnoreCase("mapset") && !MainConfig.GameMode
				&& sender.hasPermission("rg.admin")) {
			return MapConfig.SetMap(p, args);
		}
		if (args.length == 1 && args[0].equalsIgnoreCase("mapset") && !MainConfig.GameMode
				&& sender.hasPermission("rg.admin")) {
			sender.sendMessage(Color.To("&7/RushGame mapset create [��Ϸ��] &f����һ����Ϸ"));
			sender.sendMessage(Color.To("&7/RushGame mapset setlobby [��Ϸ��] &f������Ϸ����"));
			sender.sendMessage(Color.To("&7/RushGame mapset setredspawn [��Ϸ��] &f���ú�ӳ�����"));
			sender.sendMessage(Color.To("&7/RushGame mapset setbluespawn [��Ϸ��] &f�������ӳ�����"));
			sender.sendMessage(Color.To("&7/RushGame mapset setredbed [��Ϸ��] &f���ú�Ӵ�"));
			sender.sendMessage(Color.To("&7/RushGame mapset setbluebed [��Ϸ��] &f�������Ӵ�"));
			sender.sendMessage(Color.To("&7/RushGame mapset setspec [��Ϸ��] &f���ù۲��߳�����"));
			sender.sendMessage(Color.To("&7/RushGame mapset setloc1 [��Ϸ��] &f���������1"));
			sender.sendMessage(Color.To("&7/RushGame mapset setloc2 [��Ϸ��] &f���������2"));
			sender.sendMessage(Color.To("&7/RushGame mapset save [��Ϸ��] &f������Ϸ"));
			return true;
		}
		sender.sendMessage(Color.To("&7RushGame &f�������"));
		sender.sendMessage(Color.To("&7/RushGame leave &f�뿪��Ϸ"));
		if (sender.hasPermission("rg.admin")) {
			sender.sendMessage(Color.To("&7/RushGame reload &f��������"));
			if (!MainConfig.GameMode) {
				sender.sendMessage(Color.To("&7/RushGame mapset &f��ͼ����"));
			}
		}
		return true;
	}

}
