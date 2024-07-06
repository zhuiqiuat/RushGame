package cn.Mchaptim.Rush.Game;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import cn.Mchaptim.Core.PluginUtils.Color;
import cn.Mchaptim.Rush.Config.MainConfig;

public class ScoreBoard extends BukkitRunnable {
	Game game;
	Player p;
	List<Team> tl = new ArrayList<>();
	ScoreboardManager sm = Bukkit.getScoreboardManager();
	Scoreboard sb = sm.getNewScoreboard();
	Objective ob = sb.registerNewObjective("RushGame计分板", "dummy");
	Date date = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	int winscore = MainConfig.WinScores;

	public ScoreBoard(Game game, Player p) {
		this.game = game;
		this.p = p;
		ob.setDisplayName(Color.To(MainConfig.Sb_Title));
		ob.setDisplaySlot(DisplaySlot.SIDEBAR);
		p.setScoreboard(sb);
	}

	@Override
	public void run() {
		List<String> line = new ArrayList<>();
		switch (game.GetStat()) {
		case WAITING:
			line.add(" ");
			line.add(Color.To("&f地图: &7") + game.GameName);
			line.add(Color.To("&f人数: &7") + game.GamePlayers.size() + "/" + MainConfig.MaxPlayers);
			line.add(Color.To("&f模式: " + MainConfig.Sb_Mode));
			line.add(Color.To("&f版本: " + MainConfig.Sb_Version));
			line.add(" ");
			line.add(Color.To("&f匹配玩家中"));
			line.add(" ");
			line.add(Color.To("&7#") + sdf.format(date));
			line.add(Color.To(MainConfig.Sb_LastLine));
			break;
		case FULL:
			line.add(" ");
			line.add(Color.To("&f地图: &7") + game.GameName);
			line.add(Color.To("&f人数: &7") + game.GamePlayers.size() + "/" + MainConfig.MaxPlayers);
			line.add(Color.To("&f模式: " + MainConfig.Sb_Mode));
			line.add(Color.To("&f版本: " + MainConfig.Sb_Version));
			line.add(" ");
			line.add(Color.To("&f将在 &7" + game.t.Second + " &f后开始"));
			line.add(" ");
			line.add(Color.To("&7#") + sdf.format(date));
			line.add(Color.To(MainConfig.Sb_LastLine));
			break;
		case GAMING:
			line.add(" ");
			if (game.isSpectator(p)) {
				line.add(Color.To("&c&l观战中"));
				line.add(" ");
			}
			line.add(Color.To("&f当前分数:"));
			line.add(Color.To("&c红队&f: &7" + game.GetScore(Teams.RED) + "/&f" + winscore));
			line.add(Color.To("&9蓝队&f: &7" + game.GetScore(Teams.BLUE) + "/&f" + winscore));
			line.add(" ");
			line.add(Color.To("&7#") + sdf.format(date));
			line.add(Color.To(MainConfig.Sb_LastLine));
			break;
			
		default:
			break;
		}
		if (tl.size() > line.size()) {
			for (int i = tl.size() - 1; i > line.size() - 1; i--) {
				Team t = tl.get(i);
				StringBuilder strb = new StringBuilder("§" + i);
				if (strb.length() > 2) {
					strb.insert(2, "§");
					strb.setCharAt(1, '9');
				}
				sb.resetScores(strb.toString());
				t.removeEntry(strb.toString());
				t.unregister();
				tl.remove(t);
			}
		}
		if (line.size() > tl.size()) {
			for (int i = tl.size(); i < line.size(); i++) {
				StringBuilder strb = new StringBuilder("§" + i);
				if (strb.length() > 2) {
					strb.insert(2, "§");
					strb.setCharAt(1, '9');
				}
				Team t = sb.registerNewTeam(strb.toString());
				t.addEntry(strb.toString());
				ob.getScore(strb.toString()).setScore(0);
				tl.add(t);
				String tmp = line.get(i);
				if (tmp.length() > 16) {
					t.setPrefix(tmp.substring(0, 16));
					int last = tmp.lastIndexOf("§", 17);
					t.setSuffix(tmp.substring(last, last + 2) + tmp.substring(16, tmp.length()));
					continue;
				}
				t.setSuffix(tmp);
			}
		}
		if (line.size() == tl.size()) {
			for (int i = 0; i < line.size(); i++) {
				Team t = tl.get(i);
				String tmp = line.get(i);
				if (tmp.length() > 16) {
					t.setPrefix(tmp.substring(0, 16));
					int last = tmp.lastIndexOf("§", 17);
					t.setSuffix(tmp.substring(last, last + 2) + tmp.substring(16, tmp.length()));
					continue;
				}
				if (t.getPrefix() != "") {
					t.setPrefix("");
				}
				t.setSuffix(tmp);
			}
		}
	}
}
