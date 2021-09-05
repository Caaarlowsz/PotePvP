package br.com.yallandev.potepvp.kit.register;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import br.com.yallandev.potepvp.kit.Kit;

public class Hulk extends Kit {

	public Hulk() {
		super("Hulk", Material.SADDLE, 18000, false,
				Arrays.asList("Use seu hulk para", "puxar alguem e deixa-lo", "em cima de sua cabeça."));
	}

	public static HashMap<String, Long> cooldown = new HashMap<>();

	@EventHandler
	public void pegar(PlayerInteractEntityEvent e) {
		Player p = e.getPlayer();
		if ((e.getRightClicked() instanceof Player)) {
			Player r = (Player) e.getRightClicked();
			if (p.getItemInHand().getType() == Material.AIR) {
				if (hasKit(p)) {
					if (isCooldown(p)) {
						cooldownMessage(p);
						return;
					}
					e.setCancelled(true);
					p.updateInventory();
					p.setPassenger(r);

					sendMessage(r, "Você foi pego pelo §a" + p.getName() + "§f.");
					sendMessage(p, "Você pegou o §a" + r.getName() + "§f.");
					sendAction(p, "Você pegou o §a" + r.getName() + "§f.");

					addCooldown(p, 18);
				}
			}
		}
	}
}
