package br.com.yallandev.potepvp.manager;

import java.util.AbstractMap;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;

import br.com.yallandev.potepvp.BukkitMain;
import br.com.yallandev.potepvp.BukkitMain.Configuration;
import br.com.yallandev.potepvp.account.Account;
import br.com.yallandev.potepvp.ban.constructor.Ban;
import br.com.yallandev.potepvp.ban.constructor.Mute;
import br.com.yallandev.potepvp.permissions.group.Group;
import br.com.yallandev.potepvp.twitter.TweetUtils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BanManager {
	
	private Cache<String, Entry<String, Ban>> banCache;

	public BanManager() {
		banCache = CacheBuilder.newBuilder().expireAfterWrite(30L, TimeUnit.MINUTES).build(new CacheLoader<String, Entry<String, Ban>>() {
			@Override
			public Entry<String, Ban> load(String name) throws Exception {
				return null;
			}
		});
	}

	public void ban(Account player, Ban ban) {
		player.getBanHistory().add(ban);

		for (Player pPlayer : Bukkit.getOnlinePlayers()) {
			Account account = BukkitMain.getAccountCommon().getAccount(pPlayer.getUniqueId());

			if (account == null)
				continue;

			String banMessage = "";

			if (account.hasGroup(Group.YOUTUBERPLUS)) {
				banMessage = "O jogador §a" + player.getUserName() + "§f foi banido "
						+ (ban.isPermanent() ? "permanentemente" : "temporariamente") + " pelo motivo §a\""
						+ ban.getReason() + "\"§f pelo jogador §a" + ban.getBannedBy() + "§f.";

				account.sendMessage(banMessage);
			} else if (account.hasGroup(Group.LIGHT)) {
				banMessage = "O jogador §a" + player.getUserName() + "§f foi banido "
						+ (ban.isPermanent() ? "permanentemente" : "temporariamente") + " pelo motivo §a\""
						+ ban.getReason() + "\"§f.";

				account.sendMessage(banMessage);
			} else {
				banMessage = "O jogador §a" + player.getUserName() + "§f foi banido "
						+ (ban.isPermanent() ? "permanentemente" : "temporariamente") + " do §6§lSERVIDOR§f.";

				account.sendMessage(banMessage);
			}
		}
		
		if (ban.isPermanent()) {
			TweetUtils.tweetBans("📢  Jogador banido: " + player.getUserName() + "\n" + "📢 Banido por: " + ban.getBannedBy() + "\n" + "📢 Motivo: " + ban.getReason());
			
			String ipAddress = player.getIpAddress();
			
			if (ipAddress == null)
				ipAddress = player.getLastIpAddress();
			
			if (ipAddress != null) {
				this.banCache.asMap().put(ipAddress, new AbstractMap.SimpleEntry(player.getUserName(), ban));
			}
		}

		if (!player.isOnline()) {
			BukkitMain.getAccountCommon().saveAccount(player);
		} else {
			player.getPlayer().kickPlayer(ban.getMessage());
		}
	}
	
	public void mute(Account player, Mute mute) {
		player.getMuteHistory().add(mute);

		for (Player pPlayer : Bukkit.getOnlinePlayers()) {
			Account account = BukkitMain.getAccountCommon().getAccount(pPlayer.getUniqueId());

			if (account == null)
				continue;

			String banMessage = "";

			if (account.hasGroup(Group.YOUTUBERPLUS)) {
				banMessage = "O jogador §a" + player.getUserName() + "§f foi mutado "
						+ (mute.isPermanent() ? "permanentemente" : "temporariamente") + " pelo motivo §a\""
						+ mute.getReason() + "\"§f pelo jogador §a" + mute.getMutedBy() + "§f.";

				account.sendMessage(banMessage);
			} else if (account.hasGroup(Group.MVP)) {
				banMessage = "O jogador §a" + player.getUserName() + "§f foi mutado "
						+ (mute.isPermanent() ? "permanentemente" : "temporariamente") + " pelo motivo §a\""
						+ mute.getReason() + "\"§f.";

				account.sendMessage(banMessage);
			} else {
				banMessage = "O jogador §a" + player.getUserName() + "§f foi mutado "
						+ (mute.isPermanent() ? "permanentemente" : "temporariamente") + " do §6§lSERVIDOR§f.";

				account.sendMessage(banMessage);
			}
		}

		if (!player.isOnline())
			BukkitMain.getAccountCommon().saveAccount(player);
	}

	public void unban(Account player, String commandSender) {
		player.getPunishmentHistory().getActualBan().unban(commandSender);
		
		for (Player pPlayer : Bukkit.getOnlinePlayers()) {
			Account account = BukkitMain.getAccountCommon().getAccount(pPlayer.getUniqueId());

			if (account == null)
				continue;

			String banMessage = "";

			if (account.hasGroup(Group.MOD)) {
				banMessage = "O jogador §a" + player.getUserName() + "§f foi desbanido pelo §a" + commandSender + "§f!";

				account.sendMessage(banMessage);
			}
		}
		
		String ipAddress = player.getLastIpAddress();
		
		if (ipAddress != null) {
			if (banCache.asMap().containsKey(ipAddress)) {
				banCache.asMap().remove(ipAddress);
			}
		}
		
		if (!player.isOnline())
			BukkitMain.getAccountCommon().saveAccount(player);
	}
	
	public void unmute(Account player, String commandSender) {
		player.getPunishmentHistory().getActualMute().unmute(commandSender);
		
		for (Player pPlayer : Bukkit.getOnlinePlayers()) {
			Account account = BukkitMain.getAccountCommon().getAccount(pPlayer.getUniqueId());

			if (account == null)
				continue;

			String banMessage = "";

			if (account.hasGroup(Group.YOUTUBERPLUS)) {
				banMessage = "O jogador §a" + player.getUserName() + "§f foi desbanido!";

				account.sendMessage(banMessage);
			}
		}
		
		if (!player.isOnline())
			BukkitMain.getAccountCommon().saveAccount(player);
	}

	public Entry<String, Ban> getIpBan(String address) {
		return this.banCache.asMap().get(address);
	}


}
