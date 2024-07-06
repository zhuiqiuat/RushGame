package cn.Mchaptim.Rush.Utils;

import java.util.Iterator;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.github.paperspigot.Title;

//import com.destroystokyo.paper.Title; 1.12.2 Paper

import cn.Mchaptim.Core.PluginUtils.Color;
import cn.Mchaptim.Rush.Config.MainConfig;
import cn.Mchaptim.Rush.Game.Game;

public class Time extends BukkitRunnable {
	Game game;
	List<Player> GamePlayers;
	public int Second = MainConfig.StartSeconds + 1;

	public Time(Game game) {
		this.game = game;
		this.GamePlayers = game.GamePlayers;
		Iterator<Player> it = GamePlayers.iterator();
		while (it.hasNext()) {
			Player p = it.next();
			Title title = new Title(Color.To("&7" + Second), "", 0, 30, 0);
			p.sendTitle(title);
		}
	}

	@Override
	public void run() {
		Iterator<Player> it = GamePlayers.iterator();
		if (Second > 1) {
			Second--;
			while (it.hasNext()) {
				Player p = it.next();
				Title title = new Title(Color.To("&7" + Second), "", 0, 30, 0);
				p.sendTitle(title);
			}
			return;
		}
		game.StartGame();
	}
}
