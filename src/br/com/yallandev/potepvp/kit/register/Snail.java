package br.com.yallandev.potepvp.kit.register;

import java.util.Arrays;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import br.com.yallandev.potepvp.kit.Kit;

public class Snail extends Kit {

	public Snail() {
		super("Snail", Material.STRING, 18000, false,
				Arrays.asList("Use seu snail para", "ter 33% de deixar algu�m", "com lentid�o."));
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
			if ((new Random().nextInt(100) + 1) <= 33) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, 0));
			}
		}
	}

}
