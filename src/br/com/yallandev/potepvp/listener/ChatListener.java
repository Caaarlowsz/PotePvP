package br.com.yallandev.potepvp.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import br.com.yallandev.potepvp.BukkitMain;
import br.com.yallandev.potepvp.BukkitMain.Configuration;
import br.com.yallandev.potepvp.account.Account;
import br.com.yallandev.potepvp.ban.constructor.Mute;
import br.com.yallandev.potepvp.permissions.group.Group;
import br.com.yallandev.potepvp.ranking.Ranking;
import br.com.yallandev.potepvp.tag.Tag;
import br.com.yallandev.potepvp.utils.DateUtils;

public class ChatListener implements Listener {

	private static Pattern urlFinderPattern = Pattern.compile("((https?):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)", Pattern.CASE_INSENSITIVE);
	private static HashMap<String, Long> chatCooldown = new HashMap<>();
	
	public static List<String> extractUrls(String text) {
		List<String> containedUrls = new ArrayList<String>();
		Matcher urlMatcher = urlFinderPattern.matcher(text);
		while (urlMatcher.find()) {
			containedUrls.add(urlMatcher.group(1));
		}
		return containedUrls;
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onChatEnabled(AsyncPlayerChatEvent event) {
		Player p = event.getPlayer();

		Account player = BukkitMain.getAccountCommon().getAccount(p.getUniqueId());

		if (player == null) {
			event.setCancelled(true);
			return;
		}
		
		if (!player.hasServerGroup(Group.TRIAL)) {
			if (chatCooldown.containsKey(player.getUserName())) {
				if (chatCooldown.get(player.getUserName()) > System.currentTimeMillis()) {
					player.sendMessage("Você deve esperar mais §a" + DateUtils.getTime(chatCooldown.get(player.getUserName())) + "§f para falar no chat!");
					event.setCancelled(true);
					return;
				}
			}
		}
		
		chatCooldown.put(player.getUserName(), (player.hasServerGroup(Group.PRO) ? TimeUnit.SECONDS.toMillis(3) : TimeUnit.SECONDS.toMillis(5)) + System.currentTimeMillis());
		
		switch (BukkitMain.getInstance().getChatAPI().getChatState()) {
		case DISABLED:
			if (!player.hasServerGroup(Group.BETA)) {
				event.setCancelled(true);
				break;
			}
			break;
		case PAYMENT:
			if (!player.hasServerGroup(Group.LIGHT)) {
				event.setCancelled(true);
				break;
			}
			break;
		default:
			break;
		}
		if (event.isCancelled()) {
			player.sendTimerMessage("O chat está §4§lDESATIVADO§f.");
		}
		p = null;
		player = null;
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onMute(AsyncPlayerChatEvent event) {
		Player p = event.getPlayer();
		Account player = BukkitMain.getAccountCommon().getAccount(p.getUniqueId());
		
		if (player == null) {
			event.setCancelled(true);
			return;
		}
		
		Mute mute = player.getPunishmentHistory().getActualMute();
		
		if (mute == null)
			return;
		
		p.sendMessage(mute.getMessage());
		event.setCancelled(true);
		mute = null;
		p = null;
		player = null;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onChat(AsyncPlayerChatEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		Account player = BukkitMain.getAccountCommon().getAccount(event.getPlayer().getUniqueId());
		
		if (player == null) {
			return;
		}
		
		if (BukkitMain.getInstance().getPlayerManager().getStaffchat().contains(player.getUuid())) {
			for (Player players : Bukkit.getOnlinePlayers()) {
				if (BukkitMain.getAccountCommon().hasGroup(players.getUniqueId(), Group.BUILDER)) {
					players.sendMessage("§e§l[STAFFCHAT] " + Tag.valueOf(player.getServerGroup().name()).getPrefix().replace(" ", "") + " " + player.getUserName() + " §6§l» §a" + event.getMessage().replace("", ""));
				}
			}
			event.setCancelled(true);
			return;
		}
		
		if (event.getMessage().contains("%")) {
			event.setCancelled(true);
			return;
		}
		
		event.setFormat((player.hasClan() && !player.hasFake() ? "§7[" + player.getClan().getClanTag().replace("&", "§") + "§7] " : "") + event.getPlayer().getDisplayName() +  (!player.hasFake() ? " §7(" + player.getRanking().getColor() + player.getRanking().getIcon() + "§7)" : " §7(" + Ranking.UNRANKED.getColor() + Ranking.UNRANKED.getIcon() + "§7)") + "§6§l» §f" + (player.hasServerGroup(Group.HEIGHT) ? event.getMessage().replace("&", "§") : event.getMessage()));
	}
}
