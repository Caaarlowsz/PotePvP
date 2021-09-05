package br.com.yallandev.potepvp.kit.register;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import br.com.yallandev.potepvp.kit.Kit;

public class Exploder extends Kit {

	public Exploder() {
		super("Exploder", Material.TNT, 18000, true, Arrays.asList("Use seu exploder para", "lançar uma tnt em", "qualquer lugar do mapa."));
	}

	@EventHandler
	public void onTntLaunch(PlayerInteractEvent e) {
		Player p = e.getPlayer();

		if (hasKit(p)) {
			if (p.getItemInHand().getType() == Material.TNT) {
				if ((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
					e.setCancelled(true);
					if (isCooldown(p)) {
						cooldownMessage(p);
						return;
					}
					Location loc = p.getLocation();
					loc.setY(loc.getY() + 1.0D);
					Entity tnt = Bukkit.getServer().getWorld(p.getLocation().getWorld().getName()).spawnEntity(loc,
							EntityType.PRIMED_TNT);
					tnt.setVelocity(p.getEyeLocation().getDirection().multiply(2.0D));
					addCooldown(p, 30);
				}
			}
		}
	}
}
