package br.com.yallandev.potepvp.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SignListener implements Listener {

	@EventHandler
	public void onSignChange(SignChangeEvent e) {
		if (e.getLine(0).equalsIgnoreCase("sopas")) {
			e.setLine(0, "§6-=-=====-=-");
			e.setLine(1, "  §6Placa");
			e.setLine(2, "    §8Sopas");
			e.setLine(3, "§6-=-=(*)=-=-");
			return;
		} else if (e.getLine(0).equalsIgnoreCase("recraft")) {
			e.setLine(0, "§6-=-=====-=-");
			e.setLine(1, "  §6Placa");
			e.setLine(2, "    §8Recraft");
			e.setLine(3, "§6-=-=(*)=-=-");
			return;
		} else if (e.getLine(0).equalsIgnoreCase("potion")) {
			e.setLine(0, "§6-=-=====-=-");
			e.setLine(1, "  §6Placa");
			e.setLine(2, "  §8Potions");
			e.setLine(3, "§6-=-=====-=-");
			return;
		}
		
		if (e.getLine(0).contains("&")) {
			e.setLine(0, e.getLine(0).replace("&", "§"));
		}
		if (e.getLine(1).contains("&")) {
			e.setLine(1, e.getLine(1).replace("&", "§"));
		}
		if (e.getLine(2).contains("&")) {
			e.setLine(2, e.getLine(2).replace("&", "§"));
		}
		if (e.getLine(3).contains("&")) {
			e.setLine(3, e.getLine(3).replace("&", "§"));
		}
	}

	@EventHandler
	public void onPlayerInteractSoup(PlayerInteractEvent e) {
		Player p = e.getPlayer();

		ItemStack sopas = new ItemStack(Material.MUSHROOM_SOUP);

		Inventory inv = Bukkit.getServer().createInventory(p, 54, "§8PotePvP - Sopas");

		for (int i = 0; i < 54; i++){
			inv.setItem(i, sopas);
		}
		
		if ((e.getAction() == Action.RIGHT_CLICK_BLOCK) && (e.getClickedBlock() != null)
				&& ((e.getClickedBlock().getType() == Material.WALL_SIGN)
						|| (e.getClickedBlock().getType() == Material.SIGN_POST))) {
			Sign s = (Sign) e.getClickedBlock().getState();
			String[] lines = s.getLines();
			if (lines.length >= 4 && lines[2].contains("Sopas")) {
				p.openInventory(inv);
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteractPotion(PlayerInteractEvent e) {
		Player p = e.getPlayer();

		ItemStack sopas = new ItemStack(Material.getMaterial(373), 1, (short)16421);

		Inventory inve = Bukkit.getServer().createInventory(p, 54, "§8PotePvP - Potions");

		for (int i = 0; i < 54; i++){
			inve.setItem(i, sopas);
		}
		if ((e.getAction() == Action.RIGHT_CLICK_BLOCK) && (e.getClickedBlock() != null)
				&& ((e.getClickedBlock().getType() == Material.WALL_SIGN)
						|| (e.getClickedBlock().getType() == Material.SIGN_POST))) {
			Sign s = (Sign) e.getClickedBlock().getState();
			String[] lines = s.getLines();
			if (lines.length >= 4 && lines[2].contains("Potions")) {
				p.openInventory(inve);
			}
		}
	}

	@EventHandler
	public void onPlayerInteractRecraft(PlayerInteractEvent e) {
		Player p = e.getPlayer();

		ItemStack sopas = new ItemStack(Material.BOWL, 64);
		ItemStack cogur = new ItemStack(Material.RED_MUSHROOM, 64);
		ItemStack cogur1 = new ItemStack(Material.BROWN_MUSHROOM, 64);

		Inventory inve = Bukkit.getServer().createInventory(p, 9, "§8PotePvP - Recraft");

		inve.setItem(0, sopas);
		inve.setItem(1, cogur);
		inve.setItem(2, cogur1);
		inve.setItem(3, sopas);
		inve.setItem(4, cogur);
		inve.setItem(5, cogur1);
		inve.setItem(6, sopas);
		inve.setItem(7, cogur);
		inve.setItem(8, cogur1);

		if ((e.getAction() == Action.RIGHT_CLICK_BLOCK) && (e.getClickedBlock() != null)
				&& ((e.getClickedBlock().getType() == Material.WALL_SIGN)
						|| (e.getClickedBlock().getType() == Material.SIGN_POST))) {
			Sign s = (Sign) e.getClickedBlock().getState();
			String[] lines = s.getLines();
			if (lines.length >= 4 && lines[2].contains("Recraft")) {
				p.openInventory(inve);
			}
		}
	}
}
