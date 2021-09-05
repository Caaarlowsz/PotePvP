package br.com.yallandev.potepvp.kit.register;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;

import br.com.yallandev.potepvp.kit.Kit;

public class Fireman extends Kit {
	
	public Fireman() {
		super("Fireman", Material.LAVA_BUCKET, 18000, false, Arrays.asList("Use seu fireman para", "não tome dano para o", "fogo."));
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onMagma(EntityDamageEvent event) {
		Entity entity = event.getEntity();
		if (!(entity instanceof Player)) {
			return;
		}
		Player p = (Player) entity;
		if (!hasKit(p))
			return;
		EntityDamageEvent.DamageCause fire = event.getCause();
		
		if ((fire == EntityDamageEvent.DamageCause.FIRE)
				|| (fire == EntityDamageEvent.DamageCause.LAVA)
				|| (fire == EntityDamageEvent.DamageCause.FIRE_TICK)
				|| (fire == EntityDamageEvent.DamageCause.LIGHTNING)) {
			event.setCancelled(true);
		}
	}

}
