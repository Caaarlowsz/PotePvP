package br.com.yallandev.potepvp.kit.register;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import br.com.yallandev.potepvp.kit.Kit;

public class Reaper extends Kit {

	public Reaper() {
		super("Reaper", Material.WOOD_HOE, 18000, true,
				Arrays.asList("Use seu reaper para", "deixar alguem com wither", "por 2 segundos!"));
	}

	@EventHandler
	public void asd(PlayerInteractEntityEvent e) {
		Player player = e.getPlayer();

		if (hasKit(player)) {
			if (e.getRightClicked() instanceof Player) {
				Player clicked = (Player) e.getRightClicked();
				ItemStack item = player.getItemInHand();
				if (item.getType() == Material.AIR) {
					return;
				}
				if (item.getType() == Material.WOOD_HOE) {
					if (isCooldown(player)) {
						cooldownMessage(player);
						return;
					}

					addCooldown(player, 4);
					clicked.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20 * 2, 2));
				}
			}
		}
	}

}
