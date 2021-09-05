package br.com.yallandev.potepvp.kit.register;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import br.com.yallandev.potepvp.kit.Kit;

public class Viking extends Kit {
	
	public Viking() {
		super("Viking", Material.STONE_AXE, 28000, false, Arrays.asList("Use seu viking para", "dar mais dano com seu", "machado viking."));
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if (!(e.getDamager() instanceof Player))
			return;
		
		Player player = (Player) e.getDamager();
		
		if (!hasKit(player))
			return;
		
		if (player.getItemInHand() == null)
			return;
		
		if (!player.getItemInHand().getType().name().contains("AXE"))
			return;
		
		e.setDamage(e.getDamage() + 1.0D);
		
		player.getItemInHand().setDurability((short) 0);
	}

}
