package br.com.yallandev.potepvp.permissions.regex.regexpermissions;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.Permissible;

import br.com.yallandev.potepvp.BukkitMain;
import br.com.yallandev.potepvp.permissions.regex.CraftBukkitInterface;
import br.com.yallandev.potepvp.utils.Util;

public class RegexPermissions {

	private final BukkitMain plugin;
	private PermissionList permsList;
	private PEXPermissionSubscriptionMap subscriptionHandler;

	public RegexPermissions(BukkitMain plugin) {
		this.plugin = plugin;
		this.subscriptionHandler = PEXPermissionSubscriptionMap.inject(plugin, plugin.getServer().getPluginManager());
		this.permsList = PermissionList.inject(plugin.getServer().getPluginManager());
		plugin.getServer().getPluginManager().registerEvents(new EventListener(), plugin);
		injectAllPermissibles();
	}

	public void onDisable() {
		this.subscriptionHandler.uninject();
		uninjectAllPermissibles();
	}

	public PermissionList getPermissionList() {
		return this.permsList;
	}

	public void injectPermissible(Player player) {
		try {
			PermissiblePEX permissible = new PermissiblePEX(player, this.plugin);
			PermissibleInjector injector = new PermissibleInjector.ClassPresencePermissibleInjector(
					CraftBukkitInterface.getCBClassName("entity.CraftHumanEntity"), "perm", true);
			boolean success = false;
			if (injector.isApplicable(player)) {
				Permissible oldPerm = injector.inject(player, permissible);
				if (oldPerm != null) {
					permissible.setPreviousPermissible(oldPerm);
					success = true;
				}
			}
			if (!success) {
				this.plugin.getLogger().warning("Unable to inject PEX's permissible for " + player.getName());
			}
			permissible.recalculatePermissions();
		} catch (Throwable e) {
			this.plugin.getLogger().log(Level.SEVERE, "Unable to inject permissible for " + player.getName(), e);
		}
	}

	private void injectAllPermissibles() {
		for (Player player : Util.getOnlinePlayers()) {
			injectPermissible(player);
		}
	}

	private void uninjectPermissible(Player player) {
		try {
			boolean success = false;
			PermissibleInjector injector = new PermissibleInjector.ClassPresencePermissibleInjector(
					CraftBukkitInterface.getCBClassName("entity.CraftHumanEntity"), "perm", true);
			if (injector.isApplicable(player)) {
				Permissible pexPerm = injector.getPermissible(player);
				if ((pexPerm instanceof PermissiblePEX)) {
					if (injector.inject(player, ((PermissiblePEX) pexPerm).getPreviousPermissible()) != null) {
						success = true;
					}
				} else {
					success = true;
				}
			}
			if (!success) {
				this.plugin.getLogger()
						.warning("No Permissible injector found for your server implementation (while uninjecting for "
								+ player.getName() + "!");
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private void uninjectAllPermissibles() {
		for (Player player : this.plugin.getServer().getOnlinePlayers()) {
			uninjectPermissible(player);
		}
	}

	private class EventListener implements Listener {
		private EventListener() {
		}

		@EventHandler(priority = EventPriority.LOWEST)
		public void onPlayerLogin(PlayerLoginEvent event) {
			RegexPermissions.this.injectPermissible(event.getPlayer());
		}

		@EventHandler(priority = EventPriority.MONITOR)
		public void onPlayerQuit(PlayerQuitEvent event) {
			RegexPermissions.this.uninjectPermissible(event.getPlayer());
		}

		@EventHandler(priority = EventPriority.MONITOR)
		public void onPlayerKick(PlayerKickEvent event) {
			RegexPermissions.this.uninjectPermissible(event.getPlayer());
		}
	}
}
