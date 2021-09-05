package br.com.yallandev.potepvp.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import br.com.yallandev.potepvp.BukkitMain;
import br.com.yallandev.potepvp.BukkitMain.Configuration;
import br.com.yallandev.potepvp.account.Account;
import br.com.yallandev.potepvp.inventory.ServerInventory;
import br.com.yallandev.potepvp.kit.Kit;
import br.com.yallandev.potepvp.status.Status;

public class InventoryListener implements Listener {
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (!(e.getWhoClicked() instanceof Player)) {
			return;
		}
		
		Player p = (Player) e.getWhoClicked();
		Account player = BukkitMain.getAccountCommon().getAccount(p.getUniqueId());
		
		if (player == null) {
			e.setCancelled(true);
			return;
		}
		
		Inventory inv = e.getInventory();
		ItemStack item = e.getCurrentItem();
		
		if (inv.getTitle().equalsIgnoreCase("§8Status") ) {
			if (item == null)
				return;
			
			e.setCancelled(true);
			return;
		}
		
		if (inv.getTitle().equalsIgnoreCase("§8Kit Selector")) {
			if (item == null)
				return;
			
			e.setCancelled(true);
			
			if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName())
				return;
			
			if (item.getItemMeta().getDisplayName().contains("§a")) {
				if (item.getItemMeta().getDisplayName().contains("§n")) {
					Integer page = null;
					
					if (inv.getItem(4).getItemMeta().getDisplayName().contains("§eVocê está na pagina ")) {
						page = Integer.valueOf(inv.getItem(4).getItemMeta().getDisplayName().replace("§eVocê está na pagina ", "").replace(".", ""));
					} else {
						p.sendMessage(BukkitMain.getPrefix() + "Não foi possivel verificar a sua sessão!"); 
						p.closeInventory();
						return;
					}
					
					ServerInventory.openKit(player, item.getItemMeta().getDisplayName().contains("anterior") ? page-1 : page+1);
					return;
				}
				
				String kitName = item.getItemMeta().getDisplayName().replace("§a", "");
				p.performCommand("kit " + kitName);
				p.closeInventory();
				return;
			}
			if (item.getItemMeta().getDisplayName().contains("§c§n")) {
				String page = item.getItemMeta().getDisplayName().replace("§c§nPagina ", "").replace("!", "");
				player.sendMessage("Não há uma página §a" + page.substring(0, 1).toUpperCase() + page.substring(1, page.length()) + "§f.");
				p.closeInventory();
				return;
			}
			return;
		}
		
		if (inv.getTitle().equalsIgnoreCase("§8Loja de kits!")) {
			if (item == null)
				return;
			
			e.setCancelled(true);
			
			if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName())
				return;
			
			if (item.getItemMeta().getDisplayName().contains("out-game")) {
				p.sendMessage("Entre em nosso site: §a§n" + Configuration.SITE.getMessage());
				p.closeInventory();
			} else
				ServerInventory.openShopOfKits(player, 1);
			return;
		}
		
		if (inv.getTitle().equalsIgnoreCase("§8Kits Store!")) {
			if (item == null)
				return;
			
			e.setCancelled(true);
			
			if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName())
				return;
			
			if (item.getItemMeta().getDisplayName().contains("§a")) {
				if (item.getItemMeta().getDisplayName().contains("§n")) {
					Integer page = null;
					
					if (inv.getItem(4).getItemMeta().getDisplayName().contains("§eVocê está na pagina ")) {
						page = Integer.valueOf(inv.getItem(4).getItemMeta().getDisplayName().replace("§eVocê está na pagina ", "").replace(".", ""));
					} else {
						p.sendMessage(BukkitMain.getPrefix() + "Não foi possivel verificar a sua sessão!"); 
						p.closeInventory();
						return;
					}
					
					ServerInventory.openKit(player, item.getItemMeta().getDisplayName().contains("anterior") ? page-1 : page+1);
					return;
				}
				
				String kitName = item.getItemMeta().getDisplayName().replace("§a", "");
				Kit kit = BukkitMain.getInstance().getKitManager().getKit(kitName);
				Status status = player.getStatus();
				
				if (kit.getPrice() > status.getMoney()) {
					player.sendMessage("Você não tem dinheiro §csuficiente§f para comprar esse kit.");
					player.sendAction("Você não tem dinheiro!");
				} else {
					player.sendMessage("Você comprou o kit §a" + kit.getKitName() + "§f.");
					player.addPermission("kit." + kitName.toLowerCase());
					status.removeMoney(kit.getPrice());
				}
				
				p.closeInventory();
				return;
			}
			if (item.getItemMeta().getDisplayName().contains("§c§n")) {
				String page = item.getItemMeta().getDisplayName().replace("§c§nPagina ", "").replace("!", "");
				player.sendMessage("Não há uma página §a" + page.substring(0, 1).toUpperCase() + page.substring(1, page.length()) + "§f.");
				p.closeInventory();
				return;
			}
			return;
		}
		
		if (inv.getTitle().equalsIgnoreCase("§8Warp Selector")) {
			if (item == null)
				return;
			
			e.setCancelled(true);
			
			if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName())
				return;
			
			if (item.getItemMeta().getDisplayName().contains("Rei")) {
				p.performCommand("evento entrar");
				return;
			}
			
			if (item.getItemMeta().getDisplayName().contains("§a")) {
				String warpName = item.getItemMeta().getDisplayName().replace("§a", "");
				
				p.performCommand("warp " + warpName);
				p.closeInventory();
				return;
			}
			
			return;
		}
	}
}
