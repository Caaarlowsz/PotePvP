package br.com.yallandev.potepvp.kit.register;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import br.com.yallandev.potepvp.kit.Kit;

public class AntiTower extends Kit {
	
	public AntiTower() {
		super("AntiTower", Material.DIAMOND_BOOTS, 12000, false, Arrays.asList("Use seu antitower para", "não ser pisoteado pelos", "stompers!"));
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	private void onEntityDamage(EntityDamageEvent e) {
		if (!(e.getEntity() instanceof Player))
			return;

		Player player = (Player) e.getEntity();

		if (e.getCause() != DamageCause.FALL)
			return;

		if (hasKit(player)) {
			e.setCancelled(true);
		}
	}

}
