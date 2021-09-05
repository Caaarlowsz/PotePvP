package br.com.yallandev.potepvp.kit.register;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import br.com.yallandev.potepvp.kit.Kit;

public class Sonic extends Kit {

	public Sonic() {
		super("Sonic", Material.LAPIS_BLOCK, 18000, true, Arrays.asList("Use seu sonic para", "dar um desh no ar",
				"e deixar todos os seus", "inimigos proximos com", "veneno."));
	}

	ArrayList<String> fall = new ArrayList<>();

	@EventHandler
	public void onDeshfireClick(PlayerInteractEvent event) {
		Player p = event.getPlayer();

		if (p.getItemInHand() == null) {
			return;
		}

		if (!(p.getItemInHand().getType() == Material.LAPIS_BLOCK)) {
			return;
		}

		if (hasKit(p)) {
			event.setCancelled(true);

			if (isCooldown(p)) {
				cooldownMessage(p);
				return;
			}

			addCooldown(p, 35);

			p.setLastDamageCause(new EntityDamageEvent(p, DamageCause.CUSTOM, 0));
			p.setVelocity(p.getEyeLocation().getDirection().multiply(6).add(new Vector(0, 0, 0)));
			fall.add(p.getName());

			sendAction(p, "Voc� usou seu �b�lSONIC�f.");

			new BukkitRunnable() {

				@Override
				public void run() {
					fall.remove(p.getName());
				}
			}.runTaskLater(getMain(), 15);
		}
	}

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player player = e.getPlayer();

		if (fall.contains(player.getName())) {
			for (Entity pertos : player.getNearbyEntities(12.0D, 13.0D, 12.0D)) {
				if ((pertos instanceof Player)) {
					Player perto = (Player) pertos;
					perto.damage(4.0D);
					perto.setVelocity(new Vector(0.1D, 0.0D, 0.1D));
					perto.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20 * 10, 1));
				}
			}
		}
	}
}
