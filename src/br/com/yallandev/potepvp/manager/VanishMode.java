package br.com.yallandev.potepvp.manager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import br.com.yallandev.potepvp.BukkitMain;
import br.com.yallandev.potepvp.account.Account;
import br.com.yallandev.potepvp.permissions.group.Group;
import br.com.yallandev.potepvp.utils.ItemManager;

public class VanishMode {
	
	private Set<UUID> admin;
	
	private HashMap<UUID, ItemStack[]> armor;
	private HashMap<UUID, ItemStack[]> contents;
	
	public VanishMode() {
		this.admin = new HashSet<UUID>();
		
		this.armor = new HashMap<>();
		this.contents = new HashMap<>();
	}
	
	public boolean isAdmin(UUID uuid) {
		return admin.contains(uuid);
	}
	
	public Set<UUID> getAdmin() {
		return admin;
	}
	
	public void setAdmin(Player p) {
		if (!admin.contains(p.getUniqueId()))
			admin.add(p.getUniqueId());
		
		Account player = BukkitMain.getAccountCommon().getAccount(p.getUniqueId()); 
		
		player.makeVanish();
		player.updateVanished();
		player.sendMessage("§fVocê entrou no modo §4§lADMIN§f.");
		player.sendMessage("Você está invisivel para todos os jogadores§f.");
		if (player.isGroup(Group.TRIAL)) {
			p.setAllowFlight(true);
		} else {
			p.setGameMode(GameMode.CREATIVE);
		}
		setAdminItem(p);
	}
	
	public void setPlayer(Player p) {
		p.getInventory().clear();
		p.getInventory().setArmorContents(new ItemStack[4]);
		
		if (contents.containsKey(p.getUniqueId())) {
			System.out.println("itens");
			p.getInventory().setContents(contents.get(p.getUniqueId()));
			contents.remove(p.getUniqueId());
		}
		
		if (armor.containsKey(p.getUniqueId())) {
			p.getInventory().setArmorContents(armor.get(p.getUniqueId()));
			armor.remove(p.getUniqueId());
		}
		
		while (admin.contains(p.getUniqueId()))
			admin.remove(p.getUniqueId());
		
		Account player = BukkitMain.getAccountCommon().getAccount(p.getUniqueId()); 
		
		if (player == null)
			return;
		
		player.desmakeVanish();
		player.updateVanished();
		player.sendMessage("§fVocê entrou no modo §a§lJOGADOR§f.");
		player.sendMessage("Você está visivel para todos os jogadores§f.");
		p.setGameMode(GameMode.SURVIVAL);
		p.setAllowFlight(false);
	}
	
	public void setAdminItem(Player p) {
		armor.put(p.getUniqueId(), p.getInventory().getArmorContents());
		contents.put(p.getUniqueId(), p.getInventory().getContents());
		
		Account player = BukkitMain.getAccountCommon().getAccount(p.getUniqueId()); 
		
		if (player == null)
			return;
		
		p.getInventory().clear();
		p.getInventory().setArmorContents(new ItemStack[4]);
		
		p.getInventory().setHeldItemSlot(4);
		
		ItemManager item = new ItemManager(Material.MAGMA_CREAM, "§aFast admin §7(Clique)");
		
		item.addLore("");
		item.addLore("Use esse item para");
		item.addLore("entrar e sair rapidamente");
		item.addLore("do modo admin.");
		
		p.getInventory().setItem(4, item.build());
	}

}
