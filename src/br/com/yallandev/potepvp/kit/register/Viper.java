package br.com.yallandev.potepvp.kit.register;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import br.com.yallandev.potepvp.kit.Kit;

public class Viper extends Kit {

	public Viper() {
		super("Viper", Material.FERMENTED_SPIDER_EYE, 18000, true,
				Arrays.asList("Use seu viper para", "ter 33% de deixar algu�m", "com veneno."));
	}

	@EventHandler
	public void onViper(EntityDamageByEntityEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			return;
		}
		if (!(e.getDamager() instanceof Player)) {
			return;
		}
		Player p = (Player) e.getEntity();
		Player d = (Player) e.getDamager();
		if (hasKit(d)) {
			if ((Math.random() > 0.4D) && (Math.random() > 0.1D)) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 80, 0));
			}
		}
	}

}
