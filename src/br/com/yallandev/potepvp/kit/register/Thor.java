package br.com.yallandev.potepvp.kit.register;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import br.com.yallandev.potepvp.kit.Kit;

public class Thor extends Kit {

	public static List<Location> explodir = new ArrayList<>();

	public Thor() {
		super("Thor", Material.WOOD_AXE, 18000, true, Arrays.asList("Use seu thor para", "lançar raios."));
	}

	@EventHandler
	public void Thorzao(PlayerInteractEvent e) {
		Player p = e.getPlayer();

		if (!(p.getItemInHand().getType() == Material.WOOD_AXE)) {
			return;
		}

		if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR)) {
			return;
		}

		if (hasKit(p)) {
			if (isCooldown(p)) {
				cooldownMessage(p);
				return;
			}
			Location loc = p.getTargetBlock(null, 7).getLocation();
			loc = loc.getWorld().getHighestBlockAt(loc).getLocation();

			p.getWorld().strikeLightning(loc);

			addCooldown(p, 7);
		}
	}

	@EventHandler
	public void EntityDamageMobs(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		Player player = (Player) event.getEntity();
		
		if ((event.getEntity() instanceof LightningStrike)) {
			if (hasKit(player)) {
				event.setDamage(0.0D);
			} else {
				event.setDamage(6.0D);
				event.getEntity().setFireTicks(200);
			}
		}
	}

}
