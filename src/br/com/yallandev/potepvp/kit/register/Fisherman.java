package br.com.yallandev.potepvp.kit.register;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;

import br.com.yallandev.potepvp.kit.Kit;

public class Fisherman extends Kit {

	public Fisherman() {
		super("Fisherman", Material.FISHING_ROD, 12000, true,
				Arrays.asList("Use seu fisherman para", "puxar alguem at� voc�."));
	}

	@EventHandler
	public void onFish(PlayerFishEvent e) {
		if (e.getCaught() instanceof Player && hasKit(e.getPlayer())) {
			e.getCaught().teleport(e.getPlayer().getLocation());
			e.getPlayer().getItemInHand().setDurability((short) 0);

			sendAction(e.getPlayer(), "�aVoc� puxou o " + ((Player) e.getCaught()).getName() + ".");
			sendAction((Player) e.getCaught(), "�cVoc� foi puxado pelo " + e.getPlayer().getName() + ".");
		}
	}

}
