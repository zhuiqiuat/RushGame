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
			sender.sendMessage(Color.To("&a配置重载成功"));
			return true;
		}
		if (!(sender instanceof Player)) {
			sender.sendMessage(Color.To("&c你必须是一个玩家"));
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
			sender.sendMessage(Color.To("&7/RushGame mapset create [游戏名] &f创建一个游戏"));
			sender.sendMessage(Color.To("&7/RushGame mapset setlobby [游戏名] &f设置游戏大厅"));
			sender.sendMessage(Color.To("&7/RushGame mapset setredspawn [游戏名] &f设置红队出生点"));
			sender.sendMessage(Color.To("&7/RushGame mapset setbluespawn [游戏名] &f设置蓝队出生点"));
			sender.sendMessage(Color.To("&7/RushGame mapset setredbed [游戏名] &f设置红队床"));
			sender.sendMessage(Color.To("&7/RushGame mapset setbluebed [游戏名] &f设置蓝队床"));
			sender.sendMessage(Color.To("&7/RushGame mapset setspec [游戏名] &f设置观察者出生点"));
			sender.sendMessage(Color.To("&7/RushGame mapset setloc1 [游戏名] &f设置区域点1"));
			sender.sendMessage(Color.To("&7/RushGame mapset setloc2 [游戏名] &f设置区域点2"));
			sender.sendMessage(Color.To("&7/RushGame mapset save [游戏名] &f保存游戏"));
			return true;
		}
		sender.sendMessage(Color.To("&7RushGame &f命令帮助"));
		sender.sendMessage(Color.To("&7/RushGame leave &f离开游戏"));
		if (sender.hasPermission("rg.admin")) {
			sender.sendMessage(Color.To("&7/RushGame reload &f重载配置"));
			if (!MainConfig.GameMode) {
				sender.sendMessage(Color.To("&7/RushGame mapset &f地图设置"));
			}
		}
		return true;
	}

}
