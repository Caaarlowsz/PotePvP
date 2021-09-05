package br.com.yallandev.potepvp.listener;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import br.com.yallandev.potepvp.BukkitMain;
import br.com.yallandev.potepvp.BukkitMain.Configuration;
import br.com.yallandev.potepvp.account.Account;
import br.com.yallandev.potepvp.ban.constructor.Ban;
import br.com.yallandev.potepvp.permissions.group.Group;

public class ScreenshareListener implements Listener {
	
	private BukkitMain main;
	
	public ScreenshareListener() {
		this.main = BukkitMain.getInstance();
	}
	
	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		UUID uuid = p.getUniqueId();
		
		if (!main.getPlayerManager().isScreenshare(uuid))
			return;
		
		if (e.getMessage().startsWith("/warp ")) {
			p.sendMessage(Configuration.PREFIX.getMessage() + "Esse comando foi bloqueado no screenshare.");
			e.setCancelled(true);
			return;
		}
		
		if (e.getMessage().startsWith("/kill")) {
			p.sendMessage(Configuration.PREFIX.getMessage() + "Esse comando foi bloqueado no screenshare.");
			e.setCancelled(true);
			return;
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		UUID uuid = p.getUniqueId();
		Account player = BukkitMain.getAccountCommon().getAccount(uuid);
		
		if (player.getPunishmentHistory().getActualBan() != null)
			return;
		
		if (main.getPlayerManager().isScreenshare(p.getUniqueId())) {
			Account moderator = BukkitMain.getAccountCommon().getAccount(main.getPlayerManager().getScreenshareModerator(uuid));
			
			Ban ban = new Ban(moderator.getUserName(), "ScreenShare leave!");
			
			moderator.sendMessage("O jogador §a\"" + player.getUserName() + "\"§f foi banido automaticamente pois §csaiu do screenshare§f.");
			main.getBanManager().ban(player, ban);
			main.getPlayerManager().removeScreenshare(uuid);
			return;
		}
		
		if (player.hasServerGroup(Group.MODGC)) {
			for (UUID uuids : main.getPlayerManager().getScreenshare().keySet()) {
				if (Bukkit.getPlayer(uuids) == null)
					continue;
				
				if (!main.getPlayerManager().getScreenshare().get(uuids).equals(uuid))
					continue;
				
				Account ssPlayer = BukkitMain.getAccountCommon().getAccount(uuids);
				
				if (ssPlayer == null) {
					Bukkit.getPlayer(uuids).kickPlayer(Configuration.KICK_PREFIX.getMessage() + "\nVocê saiu do screenshare!");
					break;
				}
				
				ssPlayer.sendMessage("Você foi removido do screenshare, pois o §cmoderador saiu§f!");
				main.getPlayerManager().removeScreenshare(uuids);
			}
		}
	}

}
