package br.com.yallandev.potepvp.kit.register;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import br.com.yallandev.potepvp.BukkitMain;
import br.com.yallandev.potepvp.kit.Kit;
import br.com.yallandev.potepvp.kit.register.effect.Raios;

public class Waterbender extends Kit {

	public Waterbender() {
		super("Waterbender", Material.LAPIS_BLOCK, 18000, true, Arrays.asList("Use seu waterbender para",
				"prender alguem em uma", "bola de �gua e deixa-l�", "com veneno."));
	}

	public static ArrayList<String> wateratack = new ArrayList<String>();

	@EventHandler
	public void PlayerInteractEvt(PlayerInteractEntityEvent e) {
		if (!(e.getRightClicked() instanceof Player)) {
			return;
		}
		final Player p = e.getPlayer();
		final Player ent = (Player) e.getRightClicked();
		if (hasKit(p)) {
			if (p.getItemInHand().getType() == Material.LAPIS_BLOCK) {
				e.setCancelled(true);
				if (isCooldown(p)) {
					cooldownMessage(p);
					return;
				}
				wateratack.add(ent.getName());
				Raios.onWaterbender(ent.getLocation());
				addCooldown(p, 20);
				ent.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20 * 10, 200));
				Bukkit.getScheduler().scheduleAsyncDelayedTask(BukkitMain.getPlugin(), new Runnable() {
					@Override
					public void run() {
						wateratack.remove(ent.getName());
					}
				}, 20 * 2);
			}
		}
	}

	@EventHandler
	public void PlayerMov(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (wateratack.contains(p.getName())) {
			p.teleport(p);
		}
	}

}
