package br.com.yallandev.potepvp.listener;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.yallandev.potepvp.BukkitMain;
import br.com.yallandev.potepvp.BukkitMain.Configuration;
import br.com.yallandev.potepvp.event.account.PlayerJoinWarpEvent;
import br.com.yallandev.potepvp.event.account.PlayerRespawnWarpEvent;
import br.com.yallandev.potepvp.listener.umvum.Warp1v1;
import br.com.yallandev.potepvp.listener.umvum.WarpSumo;
import br.com.yallandev.potepvp.manager.WarpManager.Warp;
import br.com.yallandev.potepvp.scoreboard.Scoreboarding;
import br.com.yallandev.potepvp.utils.ItemManager;
import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.IChatBaseComponent;
import net.minecraft.server.v1_7_R4.PacketPlayOutChat;

public class WarpListener implements Listener {
	
	private BukkitMain main;
	
	public WarpListener(BukkitMain main) {
		this.main = main;
	}
	
	public void defaultItem(Player p) {
		p.getInventory().clear();
		p.getInventory().setArmorContents(new ItemStack[4]);
		
		for (PotionEffect pot : p.getActivePotionEffects())
			p.removePotionEffect(pot.getType());
		
		if (Configuration.FULLIRON.isActive()) {
			p.getInventory().setItem(0, new ItemManager(Material.DIAMOND_SWORD, "§aEspada de diamante!").addEnchantment(Enchantment.DAMAGE_ALL, 1).build());
		
			p.getInventory().setHelmet(new ItemManager(Material.IRON_HELMET, "§aCapacete de diamante!").build());
			p.getInventory().setChestplate(new ItemManager(Material.IRON_CHESTPLATE, "§aPeitoral de diamante!").build());
			p.getInventory().setLeggings(new ItemManager(Material.IRON_LEGGINGS, "§aCalça de diamante!").build());
			p.getInventory().setBoots(new ItemManager(Material.IRON_BOOTS, "§aBotas de diamante!").build());
		} else {
			p.getInventory().setItem(0, new ItemManager(Material.STONE_SWORD, "§aEspada de pedra!").addEnchantment(Enchantment.DAMAGE_ALL, 1).build());
		}
		
		for (int x = 0; x < 36; x++)
			p.getInventory().addItem(new ItemStack(Material.MUSHROOM_SOUP));
		
		p.getInventory().setItem(13, new ItemStack(Material.BOWL, 64));
		p.getInventory().setItem(14, new ItemStack(Material.RED_MUSHROOM, 64));
		p.getInventory().setItem(15, new ItemStack(Material.BROWN_MUSHROOM, 64));
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinWarpEvent e) {
		if (e.getNewWarp().getWarpName().equalsIgnoreCase("Spawn"))
			return;
		
		Player p = e.getPlayer();
		sendAction(p, "Você §aentrou§f na warp §e" + e.getNewWarp().getWarpName());
		
		if (e.getNewWarp().getWarpName().equalsIgnoreCase("1v1")) {
			Warp1v1.set1v1(p);
			return;
		}
		
		if (e.getNewWarp().getWarpName().equalsIgnoreCase("sumo")) {
			WarpSumo.set1v1(p);
			return;
		}
		
		if (e.getNewWarp().getWarpName().equalsIgnoreCase("Simulator")) {
			p.getInventory().clear();
			p.getInventory().setArmorContents(new ItemStack[4]);
			
			for (PotionEffect pot : p.getActivePotionEffects())
				p.removePotionEffect(pot.getType());
			
			p.getInventory().setItem(0, new ItemManager(Material.STONE_SWORD, "§aEspada de pedra!").build());
			p.getInventory().setItem(1, new ItemManager(Material.COMPASS, "§aBússola!").build());
			return;
		}
		
		if (e.getNewWarp().getWarpName().equalsIgnoreCase("Lava")) {
			p.getInventory().clear();
			p.getInventory().setArmorContents(new ItemStack[4]);
			
			for (PotionEffect pot : p.getActivePotionEffects())
				p.removePotionEffect(pot.getType());
			
			p.getInventory().setItem(0, new ItemManager(Material.STONE_SWORD, "§aEspada de pedra!").addEnchantment(Enchantment.DAMAGE_ALL, 1).build());
			
			for (int x = 0; x < 36; x++)
				p.getInventory().addItem(new ItemStack(Material.MUSHROOM_SOUP));
			
			p.getInventory().setItem(13, new ItemStack(Material.BOWL, 64));
			p.getInventory().setItem(14, new ItemStack(Material.RED_MUSHROOM, 64));
			p.getInventory().setItem(15, new ItemStack(Material.BROWN_MUSHROOM, 64));
			main.getPlayerManager().setProtection(p.getUniqueId(), true);
			return;
		}
		
		if (e.getNewWarp().getWarpName().equalsIgnoreCase("Main")) {
			p.getInventory().clear();
			p.getInventory().setArmorContents(new ItemStack[4]);
			
			for (PotionEffect pot : p.getActivePotionEffects())
				p.removePotionEffect(pot.getType());
			
			p.getInventory().setItem(0, new ItemManager(Material.DIAMOND_SWORD, "§aEspada de diamante!").addEnchantment(Enchantment.DAMAGE_ALL, 1).build());
			
			p.getInventory().setHelmet(new ItemManager(Material.IRON_HELMET, "§aCapacete de diamante!").build());
			p.getInventory().setChestplate(new ItemManager(Material.IRON_CHESTPLATE, "§aPeitoral de diamante!").build());
			p.getInventory().setLeggings(new ItemManager(Material.IRON_LEGGINGS, "§aCalça de diamante!").build());
			p.getInventory().setBoots(new ItemManager(Material.IRON_BOOTS, "§aBotas de diamante!").build());
			
			for (int x = 0; x < 36; x++)
				p.getInventory().addItem(new ItemStack(Material.MUSHROOM_SOUP));
			
			p.getInventory().setItem(13, new ItemStack(Material.BOWL, 64));
			p.getInventory().setItem(14, new ItemStack(Material.RED_MUSHROOM, 64));
			p.getInventory().setItem(15, new ItemStack(Material.BROWN_MUSHROOM, 64));
			
			p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*9999, 0));
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*9999, 0));
			return;
		}
		
		if (e.getNewWarp().getWarpName().equalsIgnoreCase("Potion")) {
			p.getInventory().clear();
			p.getInventory().setArmorContents(new ItemStack[4]);
			
			for (PotionEffect pot : p.getActivePotionEffects())
				p.removePotionEffect(pot.getType());
			
			p.getInventory().setItem(0, new ItemManager(Material.DIAMOND_SWORD, "§aEspada de diamante!").addEnchantment(Enchantment.DAMAGE_ALL, 1).build());

			p.getInventory().setHelmet(new ItemManager(Material.IRON_HELMET, "§aCapacete de diamante!").build());
			p.getInventory().setChestplate(new ItemManager(Material.IRON_CHESTPLATE, "§aPeitoral de diamante!").build());
			p.getInventory().setLeggings(new ItemManager(Material.IRON_LEGGINGS, "§aCalça de diamante!").build());
			p.getInventory().setBoots(new ItemManager(Material.IRON_BOOTS, "§aBotas de diamante!").build());
			
			for (int x = 0; x < 36; x++)
				p.getInventory().addItem(new ItemStack(Material.getMaterial(373), 1, (short)16421));
			
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*9999, 1));
			return;
		}
		
		if (e.getNewWarp().getWarpName().equalsIgnoreCase("Fisherman")) {
			p.getInventory().clear();
			p.getInventory().setArmorContents(new ItemStack[4]);
			
			for (PotionEffect pot : p.getActivePotionEffects())
				p.removePotionEffect(pot.getType());
			
			p.getInventory().setItem(0, new ItemManager(Material.FISHING_ROD, "§aVara de pesca!").build());
			return;
		}
		
		defaultItem(p);
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnWarpEvent e) {
		Scoreboarding.setScoreboard(e.getPlayer());
		
		Player p = e.getPlayer();
		
		BukkitMain.getInstance().getPlayerManager().addProtection(p.getUniqueId());
		
		if (e.getWarp().getWarpName().equalsIgnoreCase("1v1")) {
			Warp1v1.set1v1(p);
			e.setRespawnLocation(e.getWarp().getWarpLocation());
			return;
		}
		
		if (e.getWarp().getWarpName().equalsIgnoreCase("sumo")) {
			WarpSumo.set1v1(p);
			e.setRespawnLocation(e.getWarp().getWarpLocation());
			return;
		}

		if (e.getWarp().getWarpName().equalsIgnoreCase("Simulator")) {
			p.getInventory().clear();
			p.getInventory().setArmorContents(new ItemStack[4]);

			for (PotionEffect pot : p.getActivePotionEffects())
				p.removePotionEffect(pot.getType());

			p.getInventory().setItem(0, new ItemManager(Material.STONE_SWORD, "§aEspada de pedra!").build());
			p.getInventory().setItem(1, new ItemManager(Material.COMPASS, "§aBússola!").build());

			e.setRespawnLocation(e.getWarp().getWarpLocation());
			p.updateInventory();
			return;
		}

		if (e.getWarp().getWarpName().equalsIgnoreCase("Lava")) {
			p.getInventory().clear();
			p.getInventory().setArmorContents(new ItemStack[4]);

			for (PotionEffect pot : p.getActivePotionEffects())
				p.removePotionEffect(pot.getType());

			p.getInventory().setItem(0, new ItemManager(Material.STONE_SWORD, "§aEspada de pedra!")
					.addEnchantment(Enchantment.DAMAGE_ALL, 1).build());

			for (int x = 0; x < 36; x++)
				p.getInventory().addItem(new ItemStack(Material.MUSHROOM_SOUP));

			p.getInventory().setItem(13, new ItemStack(Material.BOWL, 64));
			p.getInventory().setItem(14, new ItemStack(Material.RED_MUSHROOM, 64));
			p.getInventory().setItem(15, new ItemStack(Material.BROWN_MUSHROOM, 64));
			main.getPlayerManager().setProtection(p.getUniqueId(), true);

			e.setRespawnLocation(e.getWarp().getWarpLocation());
			p.updateInventory();
			return;
		}

		if (e.getWarp().getWarpName().equalsIgnoreCase("Main")) {
			p.getInventory().clear();
			p.getInventory().setArmorContents(new ItemStack[4]);

			for (PotionEffect pot : p.getActivePotionEffects())
				p.removePotionEffect(pot.getType());

			p.getInventory().setItem(0, new ItemManager(Material.DIAMOND_SWORD, "§aEspada de diamante!")
					.addEnchantment(Enchantment.DAMAGE_ALL, 1).build());

			p.getInventory().setHelmet(new ItemManager(Material.IRON_HELMET, "§aCapacete de diamante!").build());
			p.getInventory()
					.setChestplate(new ItemManager(Material.IRON_CHESTPLATE, "§aPeitoral de diamante!").build());
			p.getInventory().setLeggings(new ItemManager(Material.IRON_LEGGINGS, "§aCalça de diamante!").build());
			p.getInventory().setBoots(new ItemManager(Material.IRON_BOOTS, "§aBotas de diamante!").build());

			for (int x = 0; x < 36; x++)
				p.getInventory().addItem(new ItemStack(Material.MUSHROOM_SOUP));

			p.getInventory().setItem(13, new ItemStack(Material.BOWL, 64));
			p.getInventory().setItem(14, new ItemStack(Material.RED_MUSHROOM, 64));
			p.getInventory().setItem(15, new ItemStack(Material.BROWN_MUSHROOM, 64));
			
			new BukkitRunnable() {
				
				@Override
				public void run() {
					p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 9999, 0));
					p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 9999, 0));
				}
			}.runTaskLater(BukkitMain.getInstance(), 20);

			e.setRespawnLocation(e.getWarp().getWarpLocation());
			p.updateInventory();
			return;
		}

		if (e.getWarp().getWarpName().equalsIgnoreCase("Potion")) {
			p.getInventory().clear();
			p.getInventory().setArmorContents(new ItemStack[4]);

			for (PotionEffect pot : p.getActivePotionEffects())
				p.removePotionEffect(pot.getType());

			p.getInventory().setItem(0, new ItemManager(Material.DIAMOND_SWORD, "§aEspada de diamante!")
					.addEnchantment(Enchantment.DAMAGE_ALL, 1).build());

			p.getInventory().setHelmet(new ItemManager(Material.IRON_HELMET, "§aCapacete de diamante!").build());
			p.getInventory()
					.setChestplate(new ItemManager(Material.IRON_CHESTPLATE, "§aPeitoral de diamante!").build());
			p.getInventory().setLeggings(new ItemManager(Material.IRON_LEGGINGS, "§aCalça de diamante!").build());
			p.getInventory().setBoots(new ItemManager(Material.IRON_BOOTS, "§aBotas de diamante!").build());

			for (int x = 0; x < 36; x++)
				p.getInventory().addItem(new ItemStack(Material.getMaterial(373), 1, (short) 16421));

			new BukkitRunnable() {
				
				@Override
				public void run() {
					p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 9999, 1));
				}
			}.runTaskLater(BukkitMain.getInstance(), 20);
			p.updateInventory();
			return;
		}

		if (e.getWarp().getWarpName().equalsIgnoreCase("Fisherman")) {
			p.getInventory().clear();
			p.getInventory().setArmorContents(new ItemStack[4]);

			for (PotionEffect pot : p.getActivePotionEffects())
				p.removePotionEffect(pot.getType());

			p.getInventory().setItem(0, new ItemManager(Material.FISHING_ROD, "§aVara de pesca!").build());
			p.updateInventory();
			return;
		}

		defaultItem(p);
				
		e.setRespawnLocation(e.getWarp().getWarpLocation());
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
		if (!(e.getEntity() instanceof Player))
			return;
		
		Player p = (Player) e.getEntity();
		Warp warp = main.getPlayerManager().getWarp(p.getUniqueId());
		
		if (warp.getWarpName().equalsIgnoreCase("Fisherman"))
			if (e.getCause() == DamageCause.VOID)
				e.setDamage(3000D);
			else
				e.setDamage(0.0D);
		
		if (warp.getWarpName().equalsIgnoreCase("Sumo"))
			if (e.getCause() == DamageCause.FALL)
				e.setDamage(3000D);
			else
				e.setDamage(0.0D);
	}
	
	@EventHandler
	public void onFish(PlayerFishEvent e) {
		Player p = e.getPlayer();
		Warp warp = main.getPlayerManager().getWarp(p.getUniqueId());
		
		if (e.getCaught() instanceof Player && warp.getWarpName().equalsIgnoreCase("Fisherman")) {
			e.getCaught().teleport(e.getPlayer().getLocation());
			e.getPlayer().getItemInHand().setDurability((short) 0);
			
			sendAction(p, "§aVocê puxou o " + ((Player) e.getCaught()).getName() + ".");
			sendAction((Player) e.getCaught(), "§cVocê foi puxado pelo " + e.getPlayer().getName() + ".");
		}
	}
	
	public void sendAction(Player player, String action) {
		if (((CraftPlayer)player).getHandle().playerConnection.networkManager.getVersion() < 47) {
			return;
		}
        IChatBaseComponent comp = ChatSerializer.a("{\"text\":\"" + action + " \"}");
        PacketPlayOutChat bar = new PacketPlayOutChat(comp, 2);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(bar);
	}

}
