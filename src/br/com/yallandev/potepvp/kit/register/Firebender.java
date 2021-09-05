package br.com.yallandev.potepvp.kit.register;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import br.com.yallandev.potepvp.BukkitMain;
import br.com.yallandev.potepvp.kit.Kit;
import br.com.yallandev.potepvp.kit.register.effect.Raios;

public class Firebender extends Kit {

	public Firebender() {
		super("Firebender", Material.REDSTONE_BLOCK, 18000, true, Arrays.asList("Use seu firebender para",
				"prender alguem em uma", "bola de fogo e deixa-ló", "pegando fogo."));
	}

	public static ArrayList<String> fireattack = new ArrayList<String>();

	@EventHandler
	public void PlayerInteractEvt(PlayerInteractEntityEvent e) {
		if (!(e.getRightClicked() instanceof Player)) {
			return;
		}
		final Player p = e.getPlayer();
		final Player ent = (Player) e.getRightClicked();
		if (hasKit(p)) {
			if (p.getItemInHand().getType() == Material.REDSTONE_BLOCK) {
				e.setCancelled(true);
				if (isCooldown(p)) {
					cooldownMessage(p);
					return;
				}
				fireattack.add(ent.getName());
				Raios.onFirebender(ent.getLocation());
				addCooldown(p, 20);
				ent.setFireTicks(20 * 6);
				Bukkit.getScheduler().scheduleAsyncDelayedTask(BukkitMain.getPlugin(), new Runnable() {
					@Override
					public void run() {
						fireattack.remove(ent.getName());
					}
				}, 20 * 2);
			}
		}
	}

	@EventHandler
	public void PlayerMov(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (fireattack.contains(p.getName())) {
			p.teleport(p);
		}
	}

}
