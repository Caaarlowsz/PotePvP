package br.com.yallandev.potepvp.kit.register;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import br.com.yallandev.potepvp.kit.Kit;
import br.com.yallandev.potepvp.utils.ItemManager;

public class Urgal extends Kit {

	public Urgal() {
		super("Urgal",
				new ItemManager(Material.POTION, "�aUrgal").setDurability(8201)
						.setLore(Arrays.asList("Use seu endermage para", "puxar os jogadores at�", "voc�!")).build(),
				Arrays.asList(new ItemManager(Material.COAL_BLOCK, "�aUrgal").build()), 14000);
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player player = e.getPlayer();

		if (!hasKit(player))
			return;

		if (e.getItem() == null)
			return;

		if (e.getItem().getType() != Material.COAL_BLOCK)
			return;

		if (isCooldown(player)) {
			cooldownMessage(player);
			return;
		}

		setCooldown(player, System.currentTimeMillis() + (1000l * 30));
		player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 5, 0));
		player.sendMessage("Voc� recebeu for�a por �a10 segundos�f.");
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		if (!(e.getEntity() instanceof Player))
			return;

		Player player = (Player) e.getEntity();

		if (!hasKit(player))
			return;

		boolean containsUrgal = false;

		for (PotionEffect pot : player.getActivePotionEffects())
			if (pot.getType() == PotionEffectType.INCREASE_DAMAGE)
				containsUrgal = true;

		if (!containsUrgal)
			return;

		e.setDamage(e.getDamage() - 4.5D);
	}
}
