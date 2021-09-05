package br.com.yallandev.potepvp.kit.register;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import br.com.yallandev.potepvp.BukkitMain;
import br.com.yallandev.potepvp.kit.Kit;

public class Forcefield extends Kit {
	
	public Forcefield() {
		super("Forcefield", Material.FENCE, 18000, true, Arrays.asList("Use seu forcefield para", "gerar um compo de força", "que se alguem entrar", "irá tomar dano."));
	}
	
	@EventHandler
	public void asd(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		if (player.getItemInHand().getType() == Material.AIR) {
			return;
		}
		if (player.getItemInHand() == null) {
			return;
		}
		if (hasKit(player)) {
			if (player.getItemInHand().getType() == Material.FENCE) {
				e.setCancelled(true);
				
				if (isCooldown(player)) {
					cooldownMessage(player);
					return;
				}
				addCooldown(player, 45);
				player.getWorld().playSound(player.getLocation(), Sound.MAGMACUBE_WALK2, 5.0f, -5.0f);
				sendAction(player, "Você §a§lATIVOU§f seu forcefield.");
				
				new BukkitRunnable() {
					
					@Override
					public void run() {
						if (!hasKit(player)) {
							return;
						}
						for (Entity entity : player.getNearbyEntities(8.0, 8.0, 8.0)) {
							if (entity instanceof Player) {
								entity.setVelocity(new Vector(0.2, 0.0, 0.2));
								((Player) entity).damage(3.0);
							}
						}
					}
				}.runTaskLater(BukkitMain.getPlugin(), 0);
				
				new BukkitRunnable() {
					
					@Override
					public void run() {
						if (!hasKit(player)) {
							return;
						}
						for (Entity entity : player.getNearbyEntities(8.0, 8.0, 8.0)) {
							if (entity instanceof Player) {
								entity.setVelocity(new Vector(0.2, 0.0, 0.2));
								((Player) entity).damage(3.0);
							}
						}
					}
				}.runTaskLater(BukkitMain.getPlugin(), 10);
				
				new BukkitRunnable() {
					
					@Override
					public void run() {
						if (!hasKit(player)) {
							return;
						}
						for (Entity entity : player.getNearbyEntities(8.0, 8.0, 8.0)) {
							if (entity instanceof Player) {
								entity.setVelocity(new Vector(0.2, 0.0, 0.2));
								((Player) entity).damage(3.0);
							}
						}
					}
				}.runTaskLater(BukkitMain.getPlugin(), 20);
				
				new BukkitRunnable() {
					
					@Override
					public void run() {
						if (!hasKit(player)) {
							return;
						}
						for (Entity entity : player.getNearbyEntities(8.0, 8.0, 8.0)) {
							if (entity instanceof Player) {
								entity.setVelocity(new Vector(0.2, 0.0, 0.2));
								((Player) entity).damage(3.0);
							}
						}
					}
				}.runTaskLater(BukkitMain.getPlugin(), 30);
				
				new BukkitRunnable() {
					
					@Override
					public void run() {
						if (!hasKit(player)) {
							return;
						}
						for (Entity entity : player.getNearbyEntities(8.0, 8.0, 8.0)) {
							if (entity instanceof Player) {
								entity.setVelocity(new Vector(0.2, 0.0, 0.2));
								((Player) entity).damage(3.0);
							}
						}
					}
				}.runTaskLater(BukkitMain.getPlugin(), 40);
				
				new BukkitRunnable() {
					
					@Override
					public void run() {
						if (!hasKit(player)) {
							return;
						}
						for (Entity entity : player.getNearbyEntities(8.0, 8.0, 8.0)) {
							if (entity instanceof Player) {
								entity.setVelocity(new Vector(0.2, 0.0, 0.2));
								((Player) entity).damage(3.0);
							}
						}
					}
				}.runTaskLater(BukkitMain.getPlugin(), 50);
				
				new BukkitRunnable() {
					
					@Override
					public void run() {
						if (!hasKit(player)) {
							return;
						}
						for (Entity entity : player.getNearbyEntities(8.0, 8.0, 8.0)) {
							if (entity instanceof Player) {
								entity.setVelocity(new Vector(0.2, 0.0, 0.2));
								((Player) entity).damage(3.0);
							}
						}
					}
				}.runTaskLater(BukkitMain.getPlugin(), 60);
				
				new BukkitRunnable() {
					
					@Override
					public void run() {
						if (!hasKit(player)) {
							return;
						}
						for (Entity entity : player.getNearbyEntities(8.0, 8.0, 8.0)) {
							if (entity instanceof Player) {
								entity.setVelocity(new Vector(0.2, 0.0, 0.2));
								((Player) entity).damage(3.0);
							}
						}
					}
				}.runTaskLater(BukkitMain.getPlugin(), 70);
				
				new BukkitRunnable() {
					
					@Override
					public void run() {
						if (!hasKit(player)) {
							return;
						}
						for (Entity entity : player.getNearbyEntities(8.0, 8.0, 8.0)) {
							if (entity instanceof Player) {
								entity.setVelocity(new Vector(0.2, 0.0, 0.2));
								((Player) entity).damage(3.0);
							}
						}
					}
				}.runTaskLater(BukkitMain.getPlugin(), 80);
				
				new BukkitRunnable() {
					
					@Override
					public void run() {
						if (!hasKit(player)) {
							return;
						}
						for (Entity entity : player.getNearbyEntities(8.0, 8.0, 8.0)) {
							if (entity instanceof Player) {
								entity.setVelocity(new Vector(0.2, 0.0, 0.2));
								((Player) entity).damage(3.0);
							}
						}
					}
				}.runTaskLater(BukkitMain.getPlugin(), 90);
				
				new BukkitRunnable() {
					
					@Override
					public void run() {
						if (!hasKit(player)) {
							return;
						}
						for (Entity entity : player.getNearbyEntities(8.0, 8.0, 8.0)) {
							if (entity instanceof Player) {
								entity.setVelocity(new Vector(0.2, 0.0, 0.2));
								((Player) entity).damage(3.0);
							}
						}
					}
				}.runTaskLater(BukkitMain.getPlugin(), 100);
			}
		}
	}

}
