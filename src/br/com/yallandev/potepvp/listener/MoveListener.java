package br.com.yallandev.potepvp.listener;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.yallandev.potepvp.BukkitMain;
import br.com.yallandev.potepvp.BukkitMain.Configuration;
import br.com.yallandev.potepvp.event.account.RealMoveEvent;
import br.com.yallandev.potepvp.manager.WarpManager;
import br.com.yallandev.potepvp.manager.WarpManager.Warp;
import br.com.yallandev.potepvp.utils.Util;

public class MoveListener implements Listener {
	
	private BukkitMain m;
	private WarpManager warpManager;
	private HashMap<UUID, Location> locations;

	public MoveListener() {
		this.m = BukkitMain.getInstance();
		this.warpManager = this.m.getWarpManager();
		this.locations = new HashMap();
		startUpdater();
	}

	public void startUpdater() {
		new BukkitRunnable() {
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (MoveListener.this.locations.containsKey(p.getUniqueId())) {
						Location from = (Location) MoveListener.this.locations.get(p.getUniqueId());
						if ((from.getX() != p.getLocation().getX()) || (from.getZ() != p.getLocation().getZ())
								|| (from.getY() != p.getLocation().getY())
								|| (from.distance(p.getLocation()) >= 1.0D)) {
							MoveListener.this.m.getServer().getPluginManager()
									.callEvent(new RealMoveEvent(p, from, p.getLocation()));
						}
					} else {
						MoveListener.this.locations.put(p.getUniqueId(), p.getLocation());
					}
				}
			}
		}.runTaskTimerAsynchronously(this.m, 0L, 0L);
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		this.locations.remove(event.getPlayer().getUniqueId());
	}

	@EventHandler
	public void onRealMove(RealMoveEvent event) {
		Player p = event.getPlayer();
		Warp warp = m.getPlayerManager().getWarp(p.getUniqueId());
		
		if (!m.getPlayerManager().isProtected(p.getUniqueId()))
			return;
		
		if (warp == null)
			return;
		
		if (warp.getProtection() <= 0.0D)
			return;
		
		if (event.getTo().getX() > warp.getWarpLocation().getX() + warp.getProtection()) {
			m.getPlayerManager().removeProtection(p.getUniqueId());
			this.m.getPlayerHideManager().showForAll(p);
			return;
		}
		if (event.getTo().getZ() > warp.getWarpLocation().getZ() + warp.getProtection()) {
			m.getPlayerManager().removeProtection(p.getUniqueId());
			this.m.getPlayerHideManager().showForAll(p);
			return;
		}
		if (event.getTo().getZ() < warp.getWarpLocation().getZ() - warp.getProtection()) {
			m.getPlayerManager().removeProtection(p.getUniqueId());
			this.m.getPlayerHideManager().showForAll(p);
			return;
		}
		if (event.getTo().getX() < warp.getWarpLocation().getX() - warp.getProtection()) {
			m.getPlayerManager().removeProtection(p.getUniqueId());
			this.m.getPlayerHideManager().showForAll(p);
			return;
		}
	}
}
