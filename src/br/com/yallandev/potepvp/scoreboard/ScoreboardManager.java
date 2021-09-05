package br.com.yallandev.potepvp.scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardManager {

	private String type, title;
	private List<String> types, values;

	public ScoreboardManager(String title, String type) {
		this.title = title;

		this.types = new ArrayList<>();
		this.values = new ArrayList<>();

		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public List<String> getTypes() {
		return types;
	}

	public List<String> getValues() {
		return types;
	}

	public void addLine(Object type, Object value) {
		types.add(String.valueOf(type));
		values.add(String.valueOf(value));
	}

	public void addLine(String text) {
		addLine(text.substring(0, text.length() / 2), text.substring(text.length() / 2));
	}
	
	public void blankLine() {
		addLine("§" + new Random().nextInt(10) + "§" + new Random().nextInt(10) + "§" + new Random().nextInt(10) + "§" + new Random().nextInt(10));
	}

	public void update(Player player) {
		if (player.getScoreboard().getObjective(DisplaySlot.SIDEBAR) == null || !player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getName().equalsIgnoreCase(type)) {
			Scoreboard board = player.getScoreboard();
			
			Objective obj = null;
			
			if (board.getObjective(type) == null) {
				obj = board.registerNewObjective(type, "dummy");
			} else {
				board.getObjective(type).unregister();
				obj = board.registerNewObjective(type, "dummy");
			}
			
			
			obj.setDisplayName(title);
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);
			int score = -1;
			for (int i = types.size() - 1; i >= 0; i--) {
				String s = types.get(i);
				
				FastOfflinePlayer fastOfflinePlayer = new FastOfflinePlayer(s);
				
				if (board.getTeam(type + i) == null)
					board.registerNewTeam(type + i).addPlayer(fastOfflinePlayer);
				
				score++;
				
				obj.getScore(fastOfflinePlayer.getName()).setScore(score);
			}
			player.setScoreboard(board);
		}
		
		Scoreboard board = player.getScoreboard();
		
		for (int i = types.size() - 1; i >= 0; i--) {
			String s = types.get(i);
			board.getTeam(type + i).setSuffix(values.get(i));
		}
	}
}
