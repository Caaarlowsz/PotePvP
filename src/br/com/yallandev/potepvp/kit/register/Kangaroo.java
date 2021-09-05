package br.com.yallandev.potepvp.kit.register;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import br.com.yallandev.potepvp.kit.Kit;
import br.com.yallandev.potepvp.utils.ItemManager;

public class Kangaroo extends Kit {
	
	public Kangaroo() {
		super("Kangaroo", Material.FIREWORK, 15000, Arrays.asList(new ItemManager(Material.FIREWORK, "§aKangaroo").build()), Arrays.asList("§fUse seu kangaroo para", "§fse movimentar mais rapidamente", "§fpelo mapa."));
		this.kanga = new ArrayList<>();
	}

	public ArrayList<Player> kanga;

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if (hasKit(p)) {
			if ((p.getItemInHand().getType() == Material.FIREWORK) && ((event.getAction() == Action.LEFT_CLICK_AIR)
					|| (event.getAction() == Action.LEFT_CLICK_BLOCK) || (event.getAction() == Action.RIGHT_CLICK_BLOCK)
					|| (event.getAction() == Action.RIGHT_CLICK_AIR)) && (isCooldown(p))) {
				event.setCancelled(true);
				cooldownMessage(p);
				return;
			}
			if ((p.getItemInHand().getType() == Material.FIREWORK) && ((event.getAction() == Action.LEFT_CLICK_AIR)
					|| (event.getAction() == Action.LEFT_CLICK_BLOCK) || (event.getAction() == Action.RIGHT_CLICK_BLOCK)
					|| (event.getAction() == Action.RIGHT_CLICK_AIR))) {
				event.setCancelled(true);
				if (!this.kanga.contains(p)) {
					p.setLastDamageCause(new EntityDamageEvent(p, EntityDamageEvent.DamageCause.FALL, 0.0D));
					if (!p.isSneaking()) {
						Vector vector = p.getEyeLocation().getDirection();
						vector.multiply(0.6F);
						vector.setY(1.0F);
						p.setVelocity(vector);
					} else {
						p.setVelocity(p.getLocation().getDirection().multiply(1.5D));
						p.setVelocity(new Vector(p.getVelocity().getX(), 0.5D, p.getVelocity().getZ()));
					}
					this.kanga.add(p);
				}
			}
		}
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player p = event.getPlayer();
		if (this.kanga.contains(p)) {
			Block b = p.getLocation().getBlock();
			if ((b.getType() != Material.AIR) || (b.getRelative(BlockFace.DOWN).getType() != Material.AIR)) {
				this.kanga.remove(p);
			}
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		Entity e = event.getEntity();
		if ((e instanceof Player)) {
			Player player = (Player) e;
			if ((hasKit(player)) && ((event.getEntity() instanceof Player))) {
				if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
					if (event.getDamage() > 7.0D) {
						event.setDamage(7.0D);
					}
				} else if ((event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) && (!isCooldown(player))) {
					setCooldown(player, Long.valueOf(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(6L)));
				}
			}
		}
	}
}
