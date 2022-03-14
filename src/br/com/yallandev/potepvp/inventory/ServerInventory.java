package br.com.yallandev.potepvp.inventory;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import com.github.caaarlowsz.potemc.kitpvp.PotePvP;
import br.com.yallandev.potepvp.account.Account;
import br.com.yallandev.potepvp.kit.Kit;
import br.com.yallandev.potepvp.manager.WarpManager.Warp;
import br.com.yallandev.potepvp.utils.ItemManager;

public class ServerInventory {

	public static void openKit(Account player, int page) {
		Player p = player.getPlayer();

		Inventory inv = null;
		boolean dick = false;

		if (p.getOpenInventory().getTopInventory() != null
				&& p.getOpenInventory().getTopInventory().getTitle().contains("�8Kit Selector")) {
			inv = p.getOpenInventory().getTopInventory();
			inv.clear();
		} else {
			inv = Bukkit.createInventory(null, 54, "�8Kit Selector");
			dick = true;
		}

		ItemManager item = new ItemManager(Material.STAINED_GLASS_PANE, "�f");

		for (int x = 0; x < 9; x++)
			inv.setItem(x, item.build());

		item = new ItemManager(Material.STAINED_GLASS_PANE, "�a�nPagina anterior!");
		item.setDurability(5);

		if (page == 1) {
			item.setNome("�c�nPagina anterior!");
			item.setDurability(14);
		}

		inv.setItem(0, item.build());

		item = new ItemManager(Material.STAINED_GLASS_PANE, "�eVoc� est� na pagina " + page + ".");

		inv.setItem(4, item.build());

		item = new ItemManager(Material.STAINED_GLASS_PANE, "�a�nPagina posterior!");
		item.setDurability(5);

		if (page == 3) {
			item.setNome("�c�nPagina posterior!");
			item.setDurability(14);
		}

		inv.setItem(8, item.build());

		List<Kit> kits = new ArrayList<>();
		kits.add(PotePvP.getInstance().getKitManager().getKit("PvP"));

		for (Kit kit : PotePvP.getInstance().getKitManager().getKits()) {
			if (kit.getKitName().equalsIgnoreCase("PvP"))
				continue;

			if (kit.canUse(player)) {
				kits.add(kit);
			}
		}

		if (page == 1) {
			for (int l = 0; l < 45; l++) {
				if (isNull(kits, l)) {
					break;
				} else {
					inv.addItem(kits.get(l).getKitIcon());
				}
			}
		} else if (page == 2) {
			for (int l = 45; l < 90; l++) {
				if (isNull(kits, l)) {
					break;
				} else {
					inv.addItem(kits.get(l).getKitIcon());
				}
			}
		}

		if (inv.getItem(inv.getSize() - 1) == null) {
			item = new ItemManager(Material.STAINED_GLASS_PANE, "�a�nPagina posterior!");
			item.setNome("�c�nPagina posterior!");
			item.setDurability(14);
			inv.setItem(8, item.build());
		}

		for (int x = 0; x < inv.getSize(); x++) {
			if (inv.getItem(x) == null) {
				item = new ItemManager(Material.STAINED_GLASS_PANE, "");
				inv.setItem(x, item.build());
			}
		}

		if (dick)
			p.openInventory(inv);
	}

	public static void openShop(Account player) {
		Player p = player.getPlayer();

		Inventory inv = null;
		boolean dick = false;

		if (p.getOpenInventory().getTopInventory() != null
				&& p.getOpenInventory().getTopInventory().getTitle().contains("�8Loja de kits!")) {
			inv = p.getOpenInventory().getTopInventory();
			inv.clear();
		} else {
			inv = Bukkit.createInventory(null, InventoryType.HOPPER, "�8Loja de kits!");
			dick = true;
		}

		ItemManager item = new ItemManager(Material.EXP_BOTTLE, "�aLoja in-game!");

		item.addLore("");
		item.addLore("Compre kits usando");
		item.addLore("o dinheiro que voc�");
		item.addLore("conseguiu jogando no");
		item.addLore("servidor!");

		inv.setItem(1, item.build());

		item = new ItemManager(Material.EMERALD, "�aLoja out-game!");

		item.addLore("");
		item.addLore("Compre kits usando");
		item.addLore("o dinheiro que voc�");
		item.addLore("conseguiu jogando no");
		item.addLore("servidor!");

		inv.setItem(3, item.build());

		if (dick)
			p.openInventory(inv);

	}

	public static void openShopOfKits(Account player, int page) {
		Player p = player.getPlayer();

		Inventory inv = null;
		boolean dick = false;

		if (p.getOpenInventory().getTopInventory() != null
				&& p.getOpenInventory().getTopInventory().getTitle().contains("�8Kits Store!")) {
			inv = p.getOpenInventory().getTopInventory();
			inv.clear();
		} else {
			inv = Bukkit.createInventory(null, 54, "�8Kits Store!");
			dick = true;
		}

		ItemManager item = new ItemManager(Material.STAINED_GLASS_PANE, "�f");

		for (int x = 0; x < 9; x++)
			inv.setItem(x, item.build());

		item = new ItemManager(Material.STAINED_GLASS_PANE, "�a�nPagina anterior!");
		item.setDurability(5);

		if (page == 1) {
			item.setNome("�c�nPagina anterior!");
			item.setDurability(14);
		}

		inv.setItem(0, item.build());

		item = new ItemManager(Material.STAINED_GLASS_PANE, "�eVoc� est� na pagina " + page + ".");

		inv.setItem(4, item.build());

		item = new ItemManager(Material.STAINED_GLASS_PANE, "�a�nPagina posterior!");
		item.setDurability(5);

		if (page == 3) {
			item.setNome("�c�nPagina posterior!");
			item.setDurability(14);
		}

		inv.setItem(8, item.build());

		List<Kit> kits = new ArrayList<>();

		for (Kit kit : PotePvP.getInstance().getKitManager().getKits()) {
			if (kit.getKitName().equalsIgnoreCase("PvP"))
				continue;

			if (!kit.canUse(player)) {
				kits.add(kit);
			}
		}

		if (page == 1) {
			for (int l = 0; l < 45; l++) {
				if (isNull(kits, l)) {
					break;
				} else {
					inv.addItem(new ItemManager(kits.get(l).getKitIcon().getType(), "�a" + kits.get(l).getKitName())
							.setLore(kits.get(l).getKitIcon().getItemMeta().getLore()).addLore("�o")
							.addLore("Compre por �a" + kits.get(l).getPrice()).build());
				}
			}
		} else if (page == 2) {
			for (int l = 45; l < 90; l++) {
				if (isNull(kits, l)) {
					break;
				} else {
					inv.addItem(new ItemManager(kits.get(l).getKitIcon().getType(), "�a" + kits.get(l).getKitName())
							.setLore(kits.get(l).getKitIcon().getItemMeta().getLore()).addLore("�o")
							.addLore("Compre por �a" + kits.get(l).getPrice()).build());
				}
			}
		}

		if (inv.getItem(inv.getSize() - 1) == null) {
			item = new ItemManager(Material.STAINED_GLASS_PANE, "�a�nPagina posterior!");
			item.setNome("�c�nPagina posterior!");
			item.setDurability(14);
			inv.setItem(8, item.build());
		}

		for (int x = 0; x < inv.getSize(); x++) {
			if (inv.getItem(x) == null) {
				item = new ItemManager(Material.STAINED_GLASS_PANE, "");
				inv.setItem(x, item.build());
			}
		}

		if (dick)
			p.openInventory(inv);
	}

	public static int getPlayerInWarp(Warp warp) {
		int x = 0;

		for (Player players : Bukkit.getOnlinePlayers()) {
			if (PotePvP.getInstance().getPlayerManager().getWarp(players.getUniqueId()).getWarpName()
					.equalsIgnoreCase(warp.getWarpName()))
				x++;
		}

		return x;
	}

	public static void openWarp(Account player, int page) {
		Player p = player.getPlayer();

		Inventory inv = null;
		boolean dick = false;

		if (p.getOpenInventory().getTopInventory() != null
				&& p.getOpenInventory().getTopInventory().getTitle().contains("�8Warp Selector")) {
			inv = p.getOpenInventory().getTopInventory();
			inv.clear();
		} else {
			inv = Bukkit.createInventory(null, 36, "�8Warp Selector");
			dick = true;
		}

		ItemManager item = null;

		int a = 11;

		for (Warp warps : PotePvP.getInstance().getWarpManager().getWarps()) {
			if (warps.getWarpName().equalsIgnoreCase("Spawn"))
				continue;

			if (warps.getWarpName().equalsIgnoreCase("Arena"))
				continue;

			if (warps.getWarpName().equalsIgnoreCase("Sumo"))
				continue;

			item = new ItemManager(warps.getWarpIcon(), "�a" + warps.getWarpName());

			item.addLore("");
			item.addLore("�fJogadores na warp: �a" + getPlayerInWarp(warps));

			inv.setItem(a, item.build());
			a++;
		}

		item = new ItemManager(Material.CAKE, "�aRei da Mesa");
		inv.setItem(21, item.build());

		item = new ItemManager(Material.BEDROCK, "�aArena");

		item.addLore("");
		item.addLore("�fJogadores na warp: �a"
				+ getPlayerInWarp(PotePvP.getInstance().getWarpManager().getWarp("Arena")));

		inv.setItem(23, item.build());

		item = new ItemManager(Material.BRICK, "�aSumo");

		item.addLore("");
		item.addLore(
				"�fJogadores na warp: �a" + getPlayerInWarp(PotePvP.getInstance().getWarpManager().getWarp("Sumo")));

		inv.setItem(22, item.build());

		for (int x = 0; x < inv.getSize(); x++) {
			if (inv.getItem(x) == null) {
				item = new ItemManager(Material.STAINED_GLASS_PANE, "");
				inv.setItem(x, item.build());
			}
		}

		if (dick)
			p.openInventory(inv);
	}

	public static boolean isNull(List<?> kits, int l) {
		try {
			kits.get(l);
			return false;
		} catch (Exception e) {
			return true;
		}
	}
}
