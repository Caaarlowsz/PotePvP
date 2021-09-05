package br.com.yallandev.potepvp.hologram;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.yallandev.potepvp.BukkitMain;
import net.minecraft.util.io.netty.util.internal.ConcurrentSet;

public class HologramListener implements Listener {

	private static final ConcurrentSet<Hologram> holograms = new ConcurrentSet<>();
	public static final int HOLOGRAM_DISTANCE = Bukkit.getViewDistance() * 16;

	public static ConcurrentSet<Hologram> getHolograms() {
		return holograms;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Hologram hologram : holograms) {
					if (!hologram.isRegister())
						continue;

					if (hologram.getLocation().distance(event.getPlayer().getLocation()) < HOLOGRAM_DISTANCE) {
						if (!hologram.isVisible(event.getPlayer())) {
							hologram.show(event.getPlayer());
							hologram.lock(event.getPlayer(), 5);
						}
					} else {
						hologram.hide(event.getPlayer());
					}
				}

			}
		}.runTaskAsynchronously(BukkitMain.getInstance());
	}

	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if (event.isCancelled())
			return;
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Hologram hologram : holograms) {
					if (!hologram.isLocked(event.getPlayer()) || !hologram.isRegister())
						continue;

					if (hologram.getLocation().distance(event.getTo()) < HOLOGRAM_DISTANCE) {
						if (hologram.getLocation().distance(event.getFrom()) > HOLOGRAM_DISTANCE) {
							hologram.hide(event.getPlayer());
							hologram.show(event.getPlayer());
							hologram.lock(event.getPlayer(), 5);
						}
					} else {
						hologram.hide(event.getPlayer());
					}
				}
			}
		}.runTaskAsynchronously(BukkitMain.getInstance());
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Hologram hologram : holograms) {
					if (!hologram.isLocked(event.getPlayer()) || !hologram.isRegister())
						continue;

					if (hologram.getLocation().distance(event.getTo()) < HOLOGRAM_DISTANCE) {
						if (!hologram.isVisible(event.getPlayer())) {
							hologram.show(event.getPlayer());
							hologram.lock(event.getPlayer(), 5);
						}
					} else {
						hologram.hide(event.getPlayer());
					}
				}
			}
		}.runTaskAsynchronously(BukkitMain.getInstance());

	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Hologram hologram : holograms) {
					if (!hologram.isRegister())
						continue;

					if (hologram.getLocation().distance(event.getPlayer().getLocation()) < HOLOGRAM_DISTANCE) {
						hologram.hide(event.getPlayer());
						hologram.show(event.getPlayer());
					} else {
						hologram.hide(event.getPlayer());
					}
				}
			}
		}.runTaskAsynchronously(BukkitMain.getInstance());
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		for (Hologram hologram : holograms)
			hologram.hide(event.getPlayer());
	}
	
	@EventHandler
	public void onDisable(PluginDisableEvent e) {
		for (Hologram hologram : holograms)
			hologram.remove();
	}

}