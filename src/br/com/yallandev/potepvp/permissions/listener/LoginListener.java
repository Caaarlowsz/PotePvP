package br.com.yallandev.potepvp.permissions.listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.yallandev.potepvp.BukkitMain;
import br.com.yallandev.potepvp.account.Account;
import br.com.yallandev.potepvp.utils.Util;

public class LoginListener implements Listener {

	private BukkitMain main;
	private final Map<UUID, PermissionAttachment> attachments;

	public LoginListener(BukkitMain main) {
		this.attachments = new HashMap<UUID, PermissionAttachment>();
		this.main = main;
		new BukkitRunnable() {

			@Override
			public void run() {
				for (Player player : Util.getOnlinePlayers()) {
					Account account = BukkitMain.getAccountCommon().getAccount(player.getUniqueId());

					updateAttachment(player, account);
				}
			}
		}.runTaskLater(main, 10);
	}

	@EventHandler
	public void asd(PlayerJoinEvent e) {
		Player player = e.getPlayer();

		Account acc = BukkitMain.getAccountCommon().getAccount(player.getUniqueId());

		removeAttachment(player);
		updateAttachment(player, acc);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onMonitorLogin(PlayerLoginEvent event) {
		if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
			removeAttachment(event.getPlayer());
		}
	}

	public void updateAttachment(Player player, Account jogador) {
		PermissionAttachment attach = (PermissionAttachment) this.attachments.get(player.getUniqueId());
		Permission playerPerm = getCreateWrapper(player, player.getUniqueId().toString());

		if (attach == null) {
			attach = player.addAttachment(main);
			this.attachments.put(player.getUniqueId(), attach);
			attach.setPermission(playerPerm, true);
		}

		playerPerm.getChildren().clear();

		for (String perm : jogador.getGroup().getPermissions()) {
			if (!playerPerm.getChildren().containsKey(perm)) {
				playerPerm.getChildren().put(perm, Boolean.valueOf(true));
			}
		}

		for (String perm : jogador.getPermissions()) {
			if (!playerPerm.getChildren().containsKey(perm)) {
				playerPerm.getChildren().put(perm, Boolean.valueOf(true));
			}
		}

		player.recalculatePermissions();
	}

	private Permission getCreateWrapper(Player player, String name) {
		Permission perm = this.main.getServer().getPluginManager().getPermission(name);
		if (perm == null) {
			perm = new Permission(name, "Permissao interna", PermissionDefault.FALSE);
			this.main.getServer().getPluginManager().addPermission(perm);
		}

		return perm;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onQuit(PlayerQuitEvent event) {
		removeAttachment(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onKick(PlayerKickEvent event) {
		removeAttachment(event.getPlayer());
	}

	public void removeAttachment(Player player) {
		if (!this.attachments.containsKey(player.getUniqueId()))
			return;

		PermissionAttachment attach = (PermissionAttachment) this.attachments.remove(player.getUniqueId());

		if (attach != null) {
			attach.remove();
		}

		this.main.getServer().getPluginManager().removePermission(player.getUniqueId().toString());
	}

	public void onDisable() {
		for (PermissionAttachment attach : this.attachments.values()) {
			attach.remove();
		}
		this.attachments.clear();
	}
}
