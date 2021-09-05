package br.com.yallandev.potepvp.kit.register;

import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import br.com.yallandev.potepvp.kit.Kit;
import br.com.yallandev.potepvp.utils.ItemManager;

public class Switcher extends Kit {

	public Switcher() {
		super("Switcher", Material.SNOW_BALL, 18000, Arrays.asList(new ItemManager(Material.SNOW_BALL, "§aSwitcher").build()), Arrays.asList("§fUse seu avatar para", "§fpara controlar todos os", "§felementos ar, terra, agua e fogo."));
	}

	@EventHandler
	public void onLaunch(ProjectileLaunchEvent e) {
		if (!(e.getEntity() instanceof Snowball))
			return;
		
		if (!hasKit((Player) e.getEntity().getShooter()))
			return;
		
		if (e.getEntity().getShooter() instanceof Player) {
			Player p = (Player) e.getEntity().getShooter();
			
			e.setCancelled(true);
			
			if (isCooldown(p)) {
				p.getInventory().addItem(new ItemStack(Material.SNOW_BALL));
				cooldownMessage(p);
				return;
			}
			
			e.getEntity().setMetadata("switch", new FixedMetadataValue(main, p));
			e.getEntity().setVelocity(e.getEntity().getVelocity().multiply(1.5D));
			setCooldown(p, System.currentTimeMillis() + 3000l);
		}
	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && e.getDamager().hasMetadata("switch")) {
			Player p = (Player) e.getDamager().getMetadata("switch").get(0).value();
			Location loc = e.getEntity().getLocation().clone();
			e.getEntity().teleport(p.getLocation().clone());
			p.teleport(loc);
		}
	}
}
