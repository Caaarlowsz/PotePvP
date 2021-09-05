package br.com.yallandev.potepvp.kit.register;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import br.com.yallandev.potepvp.BukkitMain;
import br.com.yallandev.potepvp.kit.Kit;

public class Stomper extends Kit {
	
	public boolean hasPlayer(Player p) {
		return true;
	}
	
	public Stomper() {
		super("Stomper", Material.IRON_BOOTS, 36000, false, Arrays.asList("Use seu stomper para", "pisotear alguém perto de você."));
	}

	@EventHandler(priority = EventPriority.HIGH)
	private void onPlayerFallStomper(EntityDamageEvent e) {
		if (!(e.getEntity() instanceof Player))
			return;

		Player player = (Player) e.getEntity();

		if (e.getCause() != DamageCause.FALL)
			return;

		if (hasKit(player)) {
			for (Entity et : player.getNearbyEntities(2.0D, 4.0D, 2.0D)) {
				if (et instanceof Player) {
					Player d = (Player) et;

					if (d == player)
						continue;
					Bukkit.getServer().getWorld("world").playSound(player.getLocation(), Sound.ANVIL_BREAK, 5.0F, 5.0F);
					if (hasPlayer(d)) {
						if (d.isSneaking() || hasKit(d, "AntiTower")) {
							d.damage(0.1D, player);
							d.damage(3.9D);
							d.damage(0.1D, player);
						} else {
							d.damage(0.1D, player);
							d.damage(player.getFallDistance() - 8.1F);
							d.damage(0.1D, player);
						}
					}
				}
			}

			Location player_location = player.getLocation();

			int radius = 2;

			for (int i = 0; i < 2; i++) {
				for (double x = -radius; x <= radius; x = x + 1.0D) {
					for (double z = -radius; z <= radius; z = z + 1.0D) {
						Location effect_location = new Location(player_location.getWorld(), player_location.getX() + x,
								player_location.getY(), player_location.getZ() + z);

						effect_location.getWorld().playEffect(effect_location, Effect.MOBSPAWNER_FLAMES, 500);
					}
				}
			}

//			if (player.isDead()) {
//				getPlayer(player).getStatus().addKills(1);
//			}

			if (e.getDamage() > 4.0D) {
				e.setDamage(4.0D);
			}
		}
	}
}
