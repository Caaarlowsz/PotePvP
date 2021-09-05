package br.com.yallandev.potepvp.kit.register;

import java.util.Arrays;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import br.com.yallandev.potepvp.kit.Kit;

public class Magma extends Kit {

	public Magma() {
		super("Magma", Material.MAGMA_CREAM, 18000, false, Arrays.asList("Use seu magma para",
				"ter 33% de deixar algu�m", "com fogo e n�o tome", "dano para fogo."));
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

		if ((fire == EntityDamageEvent.DamageCause.FIRE) || (fire == EntityDamageEvent.DamageCause.LAVA)
				|| (fire == EntityDamageEvent.DamageCause.FIRE_TICK)
				|| (fire == EntityDamageEvent.DamageCause.LIGHTNING)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onDamager(EntityDamageByEntityEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			return;
		}
		if (!(e.getDamager() instanceof Player)) {
			return;
		}
		Player p = (Player) e.getEntity();
		Player d = (Player) e.getDamager();
		if (!hasKit(p)) {
			return;
		}
		Random r = new Random();
		if (r.nextInt(5) == 0) {
			d.setFireTicks(100);
		}
	}
}
