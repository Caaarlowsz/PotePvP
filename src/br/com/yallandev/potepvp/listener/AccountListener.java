package br.com.yallandev.potepvp.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Team;

import br.com.yallandev.potepvp.BukkitMain;
import br.com.yallandev.potepvp.account.Account;
import br.com.yallandev.potepvp.event.account.PlayerChangeTagEvent;
import br.com.yallandev.potepvp.ranking.Ranking;
import br.com.yallandev.potepvp.tag.Tag;
import br.com.yallandev.potepvp.utils.Util;

public class AccountListener implements Listener {
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		
		for (Player players : Bukkit.getOnlinePlayers()) {
			if (player.getUniqueId() != players.getUniqueId()) {
				Account account = BukkitMain.getAccountCommon().getAccount(players.getUniqueId());
				if (account==null)
					continue;
				String id = getTeamname(account.getTag(), account.getRanking());
				
				joinTeam(createTeamIfNotExistsToPlayer(player, id, account.getTag().getPrefix(), " §7(" + account.getRanking().getColor() + account.getRanking().getIcon() + "§7)"), players.getName());
			}
		}
		
		Account account = BukkitMain.getAccountCommon().getAccount(player.getUniqueId());
		
		account.setTag(account.getTag());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onScoreboard(PlayerJoinEvent e) {
		e.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
	}
	
	@EventHandler
	public void onTag(PlayerChangeTagEvent e) {
		if (e.getPlayer() == null) {
			return;
		}
		
		Player player = e.getPlayer();
		
		Account acc = BukkitMain.getAccountCommon().getAccount(player.getUniqueId());
		
		if (acc == null) {
			return;
		}
		
		String id = getTeamname(e.getNewTag(), acc.getRanking());
		String tag = e.getNewTag().getPrefix();
		
		player.setDisplayName(tag + player.getName() + "§f");
		
		for (Player o : Bukkit.getOnlinePlayers()) {
			try {
				joinTeam(createTeamIfNotExistsToPlayer(o, id, tag, " §7(" + acc.getRanking().getColor() + acc.getRanking().getIcon() + "§7)"), player.getName());
			} catch (Exception ex) {}
		}
	}
	
	public Team getTeamName(Player player, String teamID) {
		if (teamID.length() > 16) {
			teamID = teamID.substring(0, 16);
		}
		return player.getScoreboard().getTeam(teamID);
	}
	
	public Team createTeamIfNotExistsToPlayer(Player player, String teamID, String teamPrefix, String teamSuffix) {
		Team team = getTeamName(player, teamID);
		if (team == null) {
			team = createTeamToPlayer(player, teamID, teamPrefix, teamSuffix);
		}
		player = null;
		teamSuffix = null;
		teamPrefix = null;
		teamID = null;
		return team;
	}
	
	public Team createTeamToPlayer(Player player, String teamID, String teamPrefix, String teamSuffix) {
		Team team = getTeamName(player, teamID);
		if (team == null) {
			if (teamID.length() > 16) {
				teamID = teamID.substring(0, 16);
			}
			if (teamPrefix.length() > 16) {
				teamPrefix = teamPrefix.substring(0, 16);
			}
			if (teamSuffix.length() > 16) {
				teamSuffix = teamSuffix.substring(0, 16);
			}
			team = player.getScoreboard().registerNewTeam(teamID);
		}
		team.setPrefix(teamPrefix);
		team.setSuffix(teamSuffix);
		player = null;
		teamID = null;
		teamPrefix = null;
		teamSuffix = null;
		return team;
	}
	
	public void joinTeam(Team team, String join) {
		if (team != null) {
			if (join.length() > 16) {
				join = join.substring(0, 16);
			}
			if (!team.getEntries().contains(join)) {
				team.addEntry(join);
			}
			team = null;
		}
		join = null;
	}
	
	private static char[] chars = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
	private static char[] charsOther = new char[] { 'z', 'y', 'x', 'w', 'v', 'u', 't', 's', 'r', 'q', 'p', 'o', 'n', 'm', 'l', 'k', 'j', 'i', 'h', 'g', 'f', 'e', 'd', 'c', 'b', 'a' };
	
	public String getTeamname(Tag tag, Ranking liga) {
		return chars[tag.ordinal()] + "-" + charsOther[liga.ordinal()];
	}
}
