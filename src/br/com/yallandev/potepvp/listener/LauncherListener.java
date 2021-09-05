package br.com.yallandev.potepvp.listener;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class LauncherListener implements Listener {

	public ArrayList<UUID> noFall;

	public LauncherListener() {
		noFall = new ArrayList<>();
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();

		if (e.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.REDSTONE_BLOCK) {
			Location loc = e.getTo().getBlock().getLocation();
			Vector sponge = p.getLocation().getDirection().multiply(3).setY(1);
			p.setVelocity(sponge);
			p.playSound(loc, Sound.LEVEL_UP, 6.0F, 1.0F);
			p.playEffect(loc, Effect.FLAME, null);

			if (!noFall.contains(p.getUniqueId()))
				noFall.add(p.getUniqueId());
		} else if (e.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.DIAMOND_BLOCK) {
			Location loc = e.getTo().getBlock().getLocation();
			Vector sponge = p.getLocation().getDirection().multiply(0).setY(1);
			p.setVelocity(sponge);
			p.playSound(loc, Sound.LEVEL_UP, 6.0F, 1.0F);
			p.playEffect(loc, Effect.FLAME, null);

			if (!noFall.contains(p.getUniqueId()))
				noFall.add(p.getUniqueId());
		} else if (e.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.IRON_BLOCK) {
			Location loc = e.getTo().getBlock().getLocation();
			Vector sponge = p.getLocation().getDirection().multiply(0).setY(2);
			p.setVelocity(sponge);
			p.playSound(loc, Sound.LEVEL_UP, 6.0F, 1.0F);
			p.playEffect(loc, Effect.FLAME, null);

			if (!noFall.contains(p.getUniqueId()))
				noFall.add(p.getUniqueId());
		} else if (e.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SPONGE) {
			Location loc = e.getTo().getBlock().getLocation();
			Vector sponge = p.getLocation().getDirection().multiply(0).setY(3);
			p.setVelocity(sponge);
			p.playSound(loc, Sound.LEVEL_UP, 6.0F, 1.0F);
			p.playEffect(loc, Effect.FLAME, null);

			if (!noFall.contains(p.getUniqueId()))
				noFall.add(p.getUniqueId());
		}
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
		if ((e.getEntity() instanceof Player)) {
			Player p = (Player) e.getEntity();
			if ((e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) && (noFall.contains(p.getUniqueId()))) {
				e.setCancelled(true);

				while (noFall.contains(p.getUniqueId()))
					noFall.remove(p.getUniqueId());
			}
		}
	}

}
