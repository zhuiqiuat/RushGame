package cn.Mchaptim.Rush.Game;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class TeamData {
	Teams Team;
	int Score = 0;
	List<Player> TeamPlayers = new ArrayList<>();

	public TeamData(Teams team) {
		this.Team = team;
	}
}
