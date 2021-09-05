package br.com.yallandev.potepvp.kit.register;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.yallandev.potepvp.BukkitMain;
import br.com.yallandev.potepvp.kit.Kit;

public class Timelord extends Kit {

	public Timelord() {
		super("Timelord", Material.WATCH, 18000, true,
				Arrays.asList("Use seu timelord para", "congelar todos os inimigos", "proximos de voc�."));
	}

	public static HashMap<UUID, UUID> freeze = new HashMap<>();

	@EventHandler
	public void asd(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		if (player.getItemInHand().getType() == Material.AIR) {
			return;
		}
		if (player.getItemInHand() == null) {
			return;
		}
		if (hasKit(player)) {
			if (player.getItemInHand().getType() == Material.WATCH) {
				if (isCooldown(player)) {
					cooldownMessage(player);
					return;
				}
				sendMessage(player, "Voc� usou o �aTimelord�f.");
				addCooldown(player, 25);
				for (Entity entity : player.getNearbyEntities(6.0, 7.0, 6.0)) {
					if (entity instanceof Player) {
						Player p = (Player) entity;
						freeze.put(p.getUniqueId(), player.getUniqueId());
						UUID uuid = p.getUniqueId();
						new BukkitRunnable() {

							@Override
							public void run() {
								if (freeze.containsKey(uuid)) {
									freeze.remove(uuid);
								}
							}
						}.runTaskLater(BukkitMain.getPlugin(), 20 * 5);
					}
				}
			}
		}
	}

	@EventHandler
	public void asd(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player) {
			if (e.getDamager() instanceof Player) {
				Player entity = (Player) e.getEntity();
				if (freeze.containsKey(entity.getUniqueId())) {
					Player damager = (Player) e.getDamager();
					if (freeze.get(entity.getUniqueId()) == damager.getUniqueId()) {
						freeze.remove(entity.getUniqueId());
					}
				}
			}
		}
	}

	@EventHandler
	public void asd(PlayerDeathEvent e) {
		if (e.getEntity() instanceof Player) {
			if (e.getEntity().getKiller() instanceof Player) {
				Player player = e.getEntity();
				if (freeze.containsKey(player.getUniqueId())) {
					freeze.remove(player.getUniqueId());
				}
			}
		}
	}

	@EventHandler
	public void asd(PlayerMoveEvent e) {
		Player player = e.getPlayer();
		if (freeze.containsKey(player.getUniqueId())) {
			player.teleport(player);
		}
	}

}
