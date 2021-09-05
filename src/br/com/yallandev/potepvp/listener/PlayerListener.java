package br.com.yallandev.potepvp.listener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import br.com.yallandev.potepvp.BukkitMain;
import br.com.yallandev.potepvp.BukkitMain.Configuration;
import br.com.yallandev.potepvp.account.Account;
import br.com.yallandev.potepvp.ban.constructor.Ban;
import br.com.yallandev.potepvp.event.account.PlayerDeathInWarpEvent;
import br.com.yallandev.potepvp.event.account.PlayerRespawnWarpEvent;
import br.com.yallandev.potepvp.inventory.ServerInventory;
import br.com.yallandev.potepvp.listener.umvum.Warp1v1;
import br.com.yallandev.potepvp.manager.WarpManager.Warp;
import br.com.yallandev.potepvp.permissions.group.Group;
import br.com.yallandev.potepvp.scoreboard.Scoreboarding;
import br.com.yallandev.potepvp.server.ServerVersion;
import br.com.yallandev.potepvp.utils.ItemManager;
import br.com.yallandev.potepvp.utils.Util;
import br.com.yallandev.potepvp.utils.string.CenterChat;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerListener implements Listener {
	
	private BukkitMain main;
	
	public PlayerListener(BukkitMain main) {
		this.main = main;
	}
	
	public static void setItem(Account player) {
		Player p = player.getPlayer();
		
		if (p == null)
			return;
		
		p.setHealth(20D);
		p.setFoodLevel(20);
		
		p.getInventory().clear();
		p.getInventory().setArmorContents(new ItemStack[4]);
		
		for (PotionEffect pot : p.getActivePotionEffects())
			p.removePotionEffect(pot.getType());
		
		ItemManager item = new ItemManager(Material.BOOK, "§aSelecione seu kit §7(Clique)");
		
		item.addLore("");
		item.addLore("Use esse item para");
		item.addLore("selecionar um kit para");
		item.addLore("você poder ir pvp.");
		
		p.getInventory().setItem(0, item.build());
		
		item = new ItemManager(Material.COMPASS, "§aSelecione sua warp §7(Clique)");
		
		item.addLore("");
		item.addLore("Use esse item para");
		item.addLore("escolher uma warp para");
		item.addLore("você ir.");
		
		p.getInventory().setItem(1, item.build());	
		
		item = new ItemManager(Material.EMERALD, "§aLoja §7(Clique)");
		
		item.addLore("");
		item.addLore("Use esse item para");
		item.addLore("comprar um kit in-game");
		item.addLore("ou na nossa loja!");
		
		p.getInventory().setItem(8, item.build());		
		
		Scoreboarding.setScoreboard(p);
	}
	
	@EventHandler
	public void onWeatherChange(WeatherChangeEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onPluginEnable(PluginEnableEvent e) {
		for (Player players : Util.getOnlinePlayers()) {
			UUID uuid = players.getUniqueId();
			String userName = players.getName();
			
			if (BukkitMain.getAccountCommon().loadAccount(uuid) == null) {
				players.kickPlayer(Configuration.KICK_PREFIX.getMessage() + "\n§cNão foi possivel carregar sua conta!\nPor favor, relogue e tente novamente!");
			} else {
				System.out.print("A conta do jogador " + userName + " foi carregada com sucesso!");
			}
			
			if (BukkitMain.getInstance().getPlayerManager().loadStatus(uuid) == null) {
				players.kickPlayer(Configuration.KICK_PREFIX.getMessage() + "\n§cNão foi possivel carregar seu status!\nPor favor, relogue e tente novamente!");
			} else {
				System.out.print("A conta do jogador " + userName + " foi carregada com sucesso!");
			}
			
			Bukkit.getPluginManager().callEvent(new PlayerJoinEvent(players, null));
		}
	}
	
	@EventHandler
	public void onPluginDisable(PluginDisableEvent e) {
		for (Player players : Util.getOnlinePlayers()) {
			Account player = BukkitMain.getAccountCommon().getAccount(players.getUniqueId());
			
			if (player==null)
				continue;
			
			BukkitMain.getAccountCommon().saveAccount(player);
		}
	}
	
	
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent e) {
		String ipAddress = e.getAddress().getHostAddress();
		String userName = e.getPlayer().getName();
		UUID uuid = e.getPlayer().getUniqueId();
		
		System.out.println("Jogador com nick " + userName + " esta iniciando uma conexao usando o ip " + ipAddress + ".");
	
		if (BukkitMain.getAccountCommon().loadAccount(uuid) == null) {
			System.out.println("Criando uma nova conta para o jogador " + userName + ".");
			
			Account newAccount = new Account(uuid, userName, ipAddress);
			
			BukkitMain.getAccountCommon().loadAccount(uuid, newAccount);
			BukkitMain.getAccountCommon().saveAccount(newAccount);
		} else {
			System.out.print("A conta do jogador " + userName + " foi carregada com sucesso!");
		}
		
		BukkitMain.getInstance().getPlayerManager().loadStatus(uuid);
		
		Account player = BukkitMain.getAccountCommon().getAccount(e.getPlayer().getUniqueId());
		
		player.setUserName(e.getPlayer().getName());
		
		if (Bukkit.getPlayer(userName) != null) {
			if (Bukkit.getPlayer(userName).getAddress().getAddress().getHostAddress().equals(e.getAddress().getHostAddress())) {
				Bukkit.getPlayer(userName).kickPlayer("Você está conectando no servidor!");
			}
			
			e.disallow(Result.KICK_OTHER, Configuration.KICK_PREFIX.getMessage() + "\n§cEstá conta já está logada no servidor!");
			return;
		}
		
		if (player == null) {
			e.disallow(Result.KICK_OTHER, Configuration.KICK_PREFIX.getMessage() + "\n§cNão foi possivel carregar sua conta!\nTente novamente mais tarde!");
			return;
		}
		
		if (e.getResult() == Result.KICK_WHITELIST) {
			e.setKickMessage(Configuration.KICK_PREFIX.getMessage() + "\n§cO servidor está em manutenção!\nEstamos trabalhando para sua diversão!");
			if (player.hasServerGroup(Group.YOUTUBER)) {
				e.allow();
				return;
			}
		}
		
		if (e.getResult() == Result.KICK_FULL) {
			if (Util.getOnlinePlayers().size() >= Configuration.MAX_PLAYERS.getValue()) {
				if (player.hasServerGroup(Group.LIGHT)) {
					e.allow();
				} else {
					e.disallow(Result.KICK_FULL, Configuration.KICK_PREFIX.getMessage() + "\n§cO servidor está cheio no momento!\nCompre §a§lVIP §ce tenha sua vaga reservada!");
				}
			}
		}
		
		Ban ban = player.getPunishmentHistory().getActualBan();
		
		if (ban != null) {
			e.disallow(Result.KICK_OTHER, ban.getMessage());
		}
		
		if (e.getResult() == Result.ALLOWED) {
			if (ban == null) {
				Entry<String, Ban> ipBan = BukkitMain.getInstance().getBanManager().getIpBan(ipAddress);
				if (ipBan != null) {
					if (!ipBan.getKey().equalsIgnoreCase(userName))
						BukkitMain.getInstance().getBanManager().ban(player, new Ban(ipBan.getValue().getBannedBy(), (ipBan.getValue().getReason().contains(" [ALTERNATIVE ACCOUNT]") ? ipBan.getValue().getReason() : ipBan.getValue().getReason() + " [ALTERNATIVE ACCOUNT]")));
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
		e.setJoinMessage(null);
		
		Account player = BukkitMain.getAccountCommon().getAccount(p.getUniqueId());
		
		if (player == null)
			return;
		
		player.updateVanished();
		player.setFakeName(player.getUserName());
		player.setUserName(p.getName());
		main.getPlayerHideManager().playerJoin(p);
		
		player.setIpAddress(e.getPlayer().getAddress().getAddress().getHostAddress());
		
		for (int x = 0; x < 100; x++)
			p.sendMessage("");
		
		for (String list : Configuration.JOIN_MESSAGES.getStringList()) {
			String[] parsed = list.split(", ");
			
			p.sendMessage((parsed[0].equals("true") ? CenterChat.centered(parsed[1].replace("&", "§").replace("{site}", Configuration.SITE.getMessage())) : parsed[1].replace("&", "§").replace("{site}", Configuration.SITE.getMessage())));
		}
		
		setItem(player);
		p.teleport(main.getWarpManager().getWarp("Spawn").getWarpLocation());
		
		if (player.getServerVersion() == ServerVersion.NONE) {
			player.sendMessage("Por motivo de muitos bug's em nosso sistema de clã, tivemos que resetar!");
			player.sendMessage("Mesmo que você não tenha criado um clã, você recebeu §a15000§f de coins para criar um clã!");
			player.sendMessage("Nós, da staff, sentimos muito pelo ocorrido!");
			
			player.setServerVersion(ServerVersion.BETA);
			player.getStatus().addMoney(15000);
			player.sendMessage("§a§l+ §f15000");
		}
		
		main.getPlayerManager().setWarp(p.getUniqueId(), "Spawn");
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		
		Account player = BukkitMain.getAccountCommon().getAccount(p.getUniqueId());
		
		if (player == null) {
			return;
		}
		
		player.getStatus().setKillstreak(0);
		
		Warp warp = main.getPlayerManager().getWarp(player.getUuid());
		
		if (warp.getWarpName().equalsIgnoreCase("Spawn")) {
			e.setRespawnLocation(warp.getWarpLocation());
			new BukkitRunnable() {
				
				@Override
				public void run() {
					setItem(player);
					main.getKitManager().removeAbility(player.getUuid());
					main.getKitManager().getPlayerCooldown().remove(player.getUuid());
					main.getPlayerManager().setProtection(player.getUuid(), true);
				}
			}.runTaskLater(BukkitMain.getInstance(), 20);
			return;
		}
		
		PlayerRespawnWarpEvent event = new PlayerRespawnWarpEvent(p, warp);
		Bukkit.getPluginManager().callEvent(event);
		
		e.setRespawnLocation(event.getRespawnLocation());
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Account player = BukkitMain.getAccountCommon().getAccount(e.getPlayer().getUniqueId());
		
		e.setQuitMessage(null);
		
		if (player == null)
			return;
		
		if (!player.getUserName().equals(player.getFakeName())) {
			player.getPlayer().performCommand("tfake");
		}
		
		BukkitMain.getAccountCommon().saveAccount(player);
		player.saveStatus();
		
		main.getPlayerManager().removePlayer(player.getUuid());
		BukkitMain.getAccountCommon().unloadAccount(player.getUuid());
		
		while (main.getVanishMode().isAdmin(player.getUuid()))
			main.getVanishMode().setPlayer(e.getPlayer());
		
		while (main.getPlayerHideManager().isHiding(e.getPlayer().getUniqueId()))
			main.getPlayerHideManager().remove(e.getPlayer().getUniqueId());
	}
	
	@EventHandler
	public void onPlayerDamageByEntity(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		
		if (!(event.getDamager() instanceof Player)) {
			return;
		}
		
		Player e = (Player) event.getEntity();
		Player d = (Player) event.getDamager();
		
		Account entity = BukkitMain.getAccountCommon().getAccount(e.getUniqueId());
		Account damager = BukkitMain.getAccountCommon().getAccount(e.getUniqueId());
		
		if (entity == null || damager == null)
			return;
		
		if (BukkitMain.getInstance().getPlayerManager().isProtected(entity.getUuid())) {
			if (BukkitMain.getInstance().getPlayerManager().isProtected(damager.getUuid())) {
				event.setCancelled(true);
				return;
			}
			
			event.setCancelled(true);
			damager.sendMessage("Esse jogador está protegido!");
			damager.sendAction("§cEsse jogador está protegido!");
			return;
		} else if (BukkitMain.getInstance().getPlayerManager().isProtected(damager.getUuid())) {
			event.setCancelled(true);
			damager.sendMessage("Você está com proteção!");
			damager.sendAction("§cVocê está com proteção!");
			return;
		}
		
		main.getPlayerManager().setCombatLog(damager.getUuid(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(10));
		
		if (d.getItemInHand().getType().toString().contains("_SWORD")) {
			if (event.getDamage() >= 3.5) 
				event.setDamage(event.getDamage() - 3.5D);
			else 
				event.setDamage(event.getDamage() - 3.0D);
			
			d.getItemInHand().setDurability((short) 0);
			
			if (d.getInventory().getBoots() != null)
				d.getInventory().getBoots().setDurability((short) 0);
			
			if (d.getInventory().getLeggings() != null)
				d.getInventory().getLeggings().setDurability((short) 0);
			
			if (d.getInventory().getChestplate() != null)
				d.getInventory().getChestplate().setDurability((short) 0);
			
			if (d.getInventory().getHelmet() != null)
				d.getInventory().getHelmet().setDurability((short) 0);
		}
		
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
		if (!(e.getEntity() instanceof Player))
			return;
		
		if (!main.getServerManager().isDamageEnable(main.getPlayerManager().getWarp(((Player)e.getEntity()).getUniqueId())) ||
				!main.getServerManager().isDamageAll()) {
			e.setCancelled(true);
			return;
		}
		
		if (BukkitMain.getInstance().getPlayerManager().isProtected(((Player) e.getEntity()).getUniqueId()))
			if (e.getCause() == DamageCause.LAVA || e.getCause() == DamageCause.FIRE_TICK)
				if (main.getPlayerManager().getWarp(((Player)e.getEntity()).getUniqueId()).getWarpName().equalsIgnoreCase("Lava"))
					e.setCancelled(false);
				else
					e.setCancelled(true);
			else
				e.setCancelled(true);
		else
			e.setCancelled(false);
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e.getItem() == null)
			return;
		
		if (!e.getItem().hasItemMeta() || !e.getItem().getItemMeta().hasDisplayName())
			return;
		
		Account player = BukkitMain.getAccountCommon().getAccount(e.getPlayer().getUniqueId());
		
		if (player == null)
			return;
		
		ItemStack item = e.getItem();
		
		if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§aSelecione seu kit §7(Clique)")) {
			ServerInventory.openKit(player, 1);
			e.setCancelled(true);
			return;
		}
		
		if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§aSelecione sua warp §7(Clique)")) {
			ServerInventory.openWarp(player, 1);
			e.setCancelled(true);
			return;
		}
		
		if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§aLoja §7(Clique)")) {
			ServerInventory.openShop(player);
			e.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onPlayerPickup(PlayerPickupItemEvent e) {
		if (!e.getItem().hasMetadata("Simulator")) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		
		Account player = BukkitMain.getAccountCommon().getAccount(p.getUniqueId());
		
		if (player == null)
			return;
		
		if (main.getPlayerManager().getWarp(player.getUuid()).getWarpName().equalsIgnoreCase("Spawn")) {
			if (main.getKitManager().hasAbility(player.getUuid(), "Nenhum")) {
				e.setCancelled(true);
			} else {
				for (ItemStack item : main.getKitManager().getAbility(player.getUuid()).getKitItens()) {
					if (e.getItemDrop().getItemStack().getType() == item.getType()) {
						e.setCancelled(true);
						player.sendAction("§cVocê não pode dropar o item do seu kit!");
						return;
					}
				}
				
				e.setCancelled(false);
			}
		} else if (main.getPlayerManager().getWarp(player.getUuid()).getWarpName().equalsIgnoreCase("1v1")) {
			if (!Warp1v1.isIn1v1(p))
				e.setCancelled(true);
		}
		
		if (e.getItemDrop().getItemStack().getType().toString().contains("_SWORD")) {
			e.setCancelled(true);
			return;
		}
		
		if (e.getItemDrop().getItemStack().getType().toString().contains("_AXE")) {
			e.setCancelled(true);
			return;
		}
		
		if (e.getItemDrop().getItemStack().getType().toString().contains("IRON_")) {
			e.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if (e.getPlayer().getGameMode() == GameMode.CREATIVE && (BukkitMain.getAccountCommon().isGroup(e.getPlayer().getUniqueId(), Group.BUILDER) || BukkitMain.getAccountCommon().hasGroup(e.getPlayer().getUniqueId(), Group.ADMIN) || BukkitMain.getAccountCommon().hasPermission(e.getPlayer().getUniqueId(), "build.build")))
			e.setCancelled(false);
		else
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if (e.getPlayer().getGameMode() == GameMode.CREATIVE && (BukkitMain.getAccountCommon().isGroup(e.getPlayer().getUniqueId(), Group.BUILDER) || BukkitMain.getAccountCommon().hasGroup(e.getPlayer().getUniqueId(), Group.ADMIN) || BukkitMain.getAccountCommon().hasPermission(e.getPlayer().getUniqueId(), "build.build")))
			e.setCancelled(false);
		else
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerInteractSoup(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		ItemStack item = player.getItemInHand();
		
		if (item.getType() == Material.MUSHROOM_SOUP) {
			if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				e.setCancelled(true);
				
				Damageable health = player;
				int food = player.getFoodLevel();
				
				if (health.getHealth() < 20) {
					if (health.getHealth() + 7.0 <= 20) health.setHealth(health.getHealth() + 7.0);
					else health.setHealth(health.getMaxHealth());
					player.setSaturation(20.0F);
					player.setExhaustion(0.0F);
					
					player.setItemInHand(new ItemStack(Material.BOWL));
				} else if (food < 20) {
					if (food + 7 <= 20) player.setFoodLevel(food + 7);
					else player.setFoodLevel(20);
					player.setSaturation(20.0F);
					player.setExhaustion(0.0F);
					player.setItemInHand(new ItemStack(Material.BOWL));
				}
				
				player.updateInventory();
			}
		}
	}
	
	@EventHandler
	public void onItemSpawn(ItemSpawnEvent e) {
		if (!e.getEntity().hasMetadata("Simulator")) {
			new BukkitRunnable() {
				
				@Override
				public void run() {
					if (e.getEntity().isDead()) {
						cancel();
						return;
					}
					
					e.getLocation().getWorld().playEffect(e.getLocation(), Effect.NOTE, 2);
					e.getEntity().remove();
				}
			}.runTaskLater(main, 20);
		}
	}
	
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		
		Account player = BukkitMain.getAccountCommon().getAccount(p.getUniqueId());
		e.setDeathMessage(null);
		
		if (player == null)
			return;
		
		if (!main.getPlayerManager().getWarp(p.getUniqueId()).getWarpName().equalsIgnoreCase("Lava")) {
			player.getStatus().removeXp(3);
			player.getStatus().removeMoney(50);
			player.getStatus().setKillstreak(0);
			player.getStatus().addDeaths(1);
			player.saveStatus();
			player.sendMessage("Você perdeu §c3 xp§f.");
			player.sendMessage("Você perdeu §c50 coins§f.");
		}
		
		if (p.getKiller() != null) {
			Player k = p.getKiller();
			
			Account killer = BukkitMain.getAccountCommon().getAccount(k.getUniqueId());
			
			if (killer == null)
				return;
			
			int randomMoney = new Random().nextInt(50) + 1;
			int randomXp = new Random().nextInt(5) + 1;
			
			killer.getStatus().addMoney(randomMoney * (killer.multiplier()));
			killer.getStatus().addXp(randomXp * (killer.multiplier()));
			killer.getStatus().addKills(1);
			killer.getStatus().addKillstreak(1);
			killer.saveStatus();
			
			killer.sendMessage("Você matou o jogador §a" + p.getName() + "§f.");
			killer.sendMessage("Você recebeu §a" + randomXp * (killer.multiplier()) + " xp§f.");
			
			if (killer.multiplier() != 1) {
				killer.sendMessage("§fVocê ganhou §b§lXP§b em " + killer.multiplier() + "X§f.");
			}
			
			killer.sendMessage("Você recebeu §a" + randomMoney * (killer.multiplier()) + " coins§f.");
			
			if (killer.multiplier() != 1) {
				killer.sendMessage("§fVocê ganhou §b§lMONEY§b em " + killer.multiplier() + "X§f.");
			}
			
			if (killer.getStatus().getKillstreak() % 5 == 0) {
				BukkitMain.broadcast("O jogador §a" + k.getName() + "§f está com um §6§lKILLSTREAK§f de §6" + killer.getStatus().getKillstreak() + "§f.");
			}
			
			killer.checkRank();
		}
		
		if (!main.getPlayerManager().getWarp(p.getUniqueId()).getWarpName().equalsIgnoreCase("Spawn")) {
			PlayerDeathInWarpEvent event = new PlayerDeathInWarpEvent(p, main.getPlayerManager().getWarp(p.getUniqueId()));
			
			Bukkit.getPluginManager().callEvent(event);
		}
	}
	
	public static HashMap<UUID, HashMap<DamageCause, Long>> damageDelay = new HashMap<>();
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityDamageDelay(EntityDamageEvent e) {
		if (!(e.getEntity() instanceof Player))
			return;
		
		Player p = (Player) e.getEntity();
		
		if (damageDelay.containsKey(p.getUniqueId())) {
			if (damageDelay.get(p.getUniqueId()).containsKey(e.getCause())) {
				if (damageDelay.get(p.getUniqueId()).get(e.getCause()) > System.currentTimeMillis()) {
					e.setCancelled(true);
					return;
				}
			}
			
			HashMap<DamageCause, Long> damage = damageDelay.get(p.getUniqueId());
			
			damage.put(e.getCause(), 515l + System.currentTimeMillis());
		} else {
			HashMap<DamageCause, Long> damage = new HashMap<>();
			
			damage.put(e.getCause(), 515l + System.currentTimeMillis());
			damageDelay.put(p.getUniqueId(), damage);
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onCommand(PlayerCommandPreprocessEvent event) {
		if (event.getMessage().toLowerCase().startsWith("/me ")) {
			event.getPlayer().sendMessage(Configuration.PREFIX.getMessage() + "O comando §a\"me\" §ffoi desativado!");
			event.setCancelled(true);
			return;
		}
		if (event.getMessage().split(" ")[0].contains(":")) {
			event.getPlayer().sendMessage(Configuration.PREFIX.getMessage() + "Você não pode usar comando que tenham §a\":\"§f.");
			event.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onLeaveDecay(LeavesDecayEvent e) {
		e.setCancelled(true);
	}

}
