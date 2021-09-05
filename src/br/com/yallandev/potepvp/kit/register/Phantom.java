package br.com.yallandev.potepvp.kit.register;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.yallandev.potepvp.kit.Kit;
import br.com.yallandev.potepvp.utils.ItemManager;

public class Phantom extends Kit {

	public Phantom() {
		super("Phantom", Material.FEATHER, 18000, Arrays.asList(new ItemManager(Material.FEATHER, "�aPhantom").build()),
				Arrays.asList("�fUse seu phantom para", "�fpara poder voar pelo", "�fc�u por 5 segundos."));
	}

	public static HashMap<UUID, ItemStack[]> armors = new HashMap<>();

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();

		if (hasKit(p)) {
			if (e.getCurrentItem() != null)
				if (e.getCurrentItem().getType().toString().contains("LEATHER"))
					e.setCancelled(true);
		}
	}

	@EventHandler
	public void onInteractPhantom(PlayerInteractEvent event) {
		ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
		LeatherArmorMeta chestp = (LeatherArmorMeta) chest.getItemMeta();
		chestp.setColor(Color.WHITE);
		chest.setItemMeta(chestp);

		ItemStack chest2 = new ItemStack(Material.LEATHER_HELMET);
		LeatherArmorMeta chestp2 = (LeatherArmorMeta) chest.getItemMeta();
		chestp2.setColor(Color.WHITE);
		chest2.setItemMeta(chestp2);

		ItemStack chest3 = new ItemStack(Material.LEATHER_LEGGINGS);
		LeatherArmorMeta chestp3 = (LeatherArmorMeta) chest.getItemMeta();
		chestp3.setColor(Color.WHITE);
		chest3.setItemMeta(chestp3);

		ItemStack chest4 = new ItemStack(Material.LEATHER_BOOTS);
		LeatherArmorMeta chestp4 = (LeatherArmorMeta) chest.getItemMeta();
		chestp4.setColor(Color.WHITE);
		chest4.setItemMeta(chestp4);

		Player player = event.getPlayer();

		if (player.getItemInHand() == null) {
			return;
		}

		if (player.getItemInHand().getType() != Material.FEATHER) {
			return;
		}

		if (hasKit(player)) {
			if (isCooldown(player)) {
				cooldownMessage(player);
				return;
			}

			addCooldown(player, 40);

			player.setAllowFlight(true);
			player.setFlying(true);

			armors.put(player.getUniqueId(), player.getInventory().getArmorContents());

			player.getInventory().setArmorContents(new ItemStack[4]);
			player.getInventory().setChestplate(chest);
			player.getInventory().setHelmet(chest2);
			player.getInventory().setLeggings(chest3);
			player.getInventory().setBoots(chest4);

			player.updateInventory();

			new BukkitRunnable() {

				@Override
				public void run() {
					sendMessage(player, "Seu �a�lPHANTOM�f acaba em �a3 segundos�f.");
					sendAction(player, "Seu �a�lPHANTOM�f acaba em �a3 segundos�f.");
				}
			}.runTaskLater(main, 20 * 2);

			new BukkitRunnable() {

				@Override
				public void run() {
					sendMessage(player, "Seu �6�lPHANTOM�f acaba em �62 segundos�f.");
					sendAction(player, "Seu �6�lPHANTOM�f acaba em �62 segundos�f.");
				}
			}.runTaskLater(main, 20 * 3);

			new BukkitRunnable() {

				@Override
				public void run() {
					sendMessage(player, "Seu �c�lPHANTOM�f acaba em �c1 segundos�f.");
					sendAction(player, "Seu �c�lPHANTOM�f acaba em �c1 segundos�f.");
				}
			}.runTaskLater(main, 20 * 4);

			new BukkitRunnable() {

				@Override
				public void run() {
					player.setFlying(false);
					player.setAllowFlight(false);
					player.updateInventory();
					sendAction(player, "Seu �4�lPHANTOM�f acabou!");
					sendMessage(player, "Seu �4�lPHANTOM�f acabou!");

					player.getInventory().setArmorContents(armors.get(player.getUniqueId()));

					armors.remove(player.getUniqueId());
				}
			}.runTaskLater(main, 20 * 5);
		}

	}

}
