package cn.Mchaptim.Rush.Listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import cn.Mchaptim.Rush.Main;
import cn.Mchaptim.Rush.Config.MainConfig;

public class Servers implements Listener {
	long LastActionTime;

	@EventHandler
	void OnPing(ServerListPingEvent e) {
		if (MainConfig.BungeeMode) {
			e.setMaxPlayers(MainConfig.MaxPlayers);
			if (Main.Games.size() == 0)
				return;
			switch (Main.Games.get(0).GetStat()) {
			case WAITING:
				e.setMotd(MainConfig.WaitingMOTD);
				break;
			case GAMING:
				e.setMotd(MainConfig.GamingMOTD);
				break;
			case FULL:
				e.setMotd(MainConfig.FullMOTD);
				break;
			case STOPPTING:
				e.setMotd(MainConfig.StoppingMOTD);
				break;
			case ENDING:
				e.setMotd(MainConfig.EndingMOTD);
				break;
			default:
				break;
			}
		}
	}

	@EventHandler
	void OnWeatherChange(WeatherChangeEvent e) {
		long tmp = System.currentTimeMillis();
		if (!(tmp - LastActionTime > 300000L)) {
			if (tmp - LastActionTime > 10000L)
				e.setCancelled(true);
			return;
		}
		LastActionTime = tmp;
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "weather clear");
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "time set day");
	}
}
