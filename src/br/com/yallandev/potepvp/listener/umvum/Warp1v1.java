package br.com.yallandev.potepvp.listener.umvum;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.yallandev.potepvp.BukkitMain;
import br.com.yallandev.potepvp.BukkitMain.Configuration;
import br.com.yallandev.potepvp.commands.register.WarpCommand;
import br.com.yallandev.potepvp.event.account.PlayerDeathInWarpEvent;
import br.com.yallandev.potepvp.event.account.PlayerJoinWarpEvent;
import br.com.yallandev.potepvp.scoreboard.Scoreboarding;
import br.com.yallandev.potepvp.utils.DateUtils;
import br.com.yallandev.potepvp.utils.ItemManager;

public class Warp1v1 implements Listener {
	
	private BukkitMain main;
	public static List<Player> playersIn1v1 = new ArrayList<>();
	public static HashMap<Player, String> duel = new HashMap<>();
	private HashMap<String, HashMap<ChanllengeType, HashMap<String, Desafio>>> playerDesafios;
	private List<Player> playersInQueue;
	private Location firstLoction;
	private Location secondLoction;

	public Warp1v1() {
		this.main = BukkitMain.getInstance();
		playersIn1v1 = new ArrayList();
		this.playerDesafios = new HashMap();
		this.playersInQueue = new ArrayList();
	}
	
	@EventHandler
	public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		
		if (isOnWarp(p)) {
			if (isIn1v1(p)) {
				if (!e.getMessage().toLowerCase().startsWith("/report") || !e.getMessage().toLowerCase().startsWith("/tag") || !e.getMessage().toLowerCase().startsWith("/ban") || !e.getMessage().toLowerCase().startsWith("/kick") || !e.getMessage().toLowerCase().startsWith("/tell")) {
					e.setCancelled(true);
					p.sendMessage("§cVocê não pode executar comandos em pvp!");
					return;
				}
			}
		}
	}
	
	public boolean isOnWarp(Player p) {
		return this.getMain().getPlayerManager().getWarp(p.getUniqueId()).getWarpName().equalsIgnoreCase("1v1");
	}
	
	public static void set1v1(Player p) {
		p.getInventory().clear();
		p.getInventory().setArmorContents(new ItemStack[4]);
		
		for (PotionEffect pot : p.getActivePotionEffects())
			p.removePotionEffect(pot.getType());
		
		if (p.getGameMode() == GameMode.CREATIVE)
			p.sendMessage(Configuration.PREFIX.getMessage() + "Você está no modo §aCriativo§f!");
		
		ItemManager item = new ItemManager(Material.BLAZE_ROD, "§aDesafiar alguém §7(Clique)");
		
		item.addLore("");
		item.addLore("Use esse item para");
		item.addLore("desafiar alguém.");
		
		p.getInventory().setItem(3, item.build());
		
		item = new ItemManager(Material.INK_SACK, "§a1v1 rápido §7(Clique)");
		
		item.setDurability(8);
		item.addLore("");
		item.addLore("Use esse item para");
		item.addLore("ir para a fila de");
		item.addLore("1v1.");
		
		p.getInventory().setItem(5, item.build());
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		ItemStack item = event.getItem();
		
		if (!isOnWarp(p))
			return;
		
		if (!getMain().getKitManager().hasAbility(p.getUniqueId(), "Nenhum"))
			return;
		
		if (item == null)
			return;
		
		if (!event.getAction().toString().contains("RIGHT"))
			return;
		
		if (item.getType() != Material.INK_SACK)
			return;
		
		if (WarpCommand.teleport.contains(p.getUniqueId()))
			return;
		
		if (item.getDurability() == 8) {
			if (this.playersInQueue.contains(p)) {
				item.setDurability((short) 10);
				p.sendMessage(Configuration.PREFIX.getMessage() + "Você já está na lista de espera da 1v1.");
				return;
			}
			
			if (this.playersInQueue.size() > 0) {
				Player player = (Player) this.playersInQueue.get(0);
				if (player != null) {
					player.sendMessage(Configuration.PREFIX.getMessage() + "Você irá lutar contra o player §a" +  p.getName());
					p.sendMessage(Configuration.PREFIX.getMessage() + "Você irá lutar contra o player §a"+ ChatColor.YELLOW + player.getName());
					setIn1v1(new Desafio(player, p));
					return;
				}
			}
			
			item.setDurability((short) 10);
			p.sendMessage(Configuration.PREFIX.getMessage() + "Você entrou na fila de espera de 1v1 rapido!");
			this.playersInQueue.add(p);
		} else {
			if (!this.playersInQueue.contains(p)) {
				p.sendMessage(Configuration.PREFIX.getMessage() + "Você não está na lista de espera de 1v1 rapido!");
				item.setDurability((short) 8);
				return;
			}
			item.setDurability((short) 8);
			p.sendMessage(Configuration.PREFIX.getMessage() + "Você saiu da lista de espera de 1v1 rapido!");
			this.playersInQueue.remove(p);
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEntityEvent event) {
		Player p = event.getPlayer();
		Entity e = event.getRightClicked();
		ItemStack item = p.getItemInHand();
		
		if (!isOnWarp(p))
			return;
		
		if (!getMain().getKitManager().hasAbility(p.getUniqueId(), "Nenhum"))
			return;
		
		if (!(e instanceof Player))
			return;
		
		if (item == null)
			return;
		
		if ((item.getType() != Material.BLAZE_ROD) && (item.getType() != Material.IRON_FENCE))
			return;
		
		if (WarpCommand.teleport.contains(p.getUniqueId()))
			return;
		
		Player clicado = (Player) e;
		
		if (isIn1v1(clicado)) {
			p.sendMessage(Configuration.PREFIX.getMessage() + "O jogador §a\"" + clicado.getName() + "\"§f já está 1v1!");
			return;
		}
		
		ChanllengeType type = null;
		
		switch (item.getType()) {
		case FEATHER:
			type = ChanllengeType.CUSTOM;
			break;
		default:
			type = ChanllengeType.DEFAULT;
		}
		
		if (hasDesafio(p, clicado, type)) {
			Desafio desafio = getDesafio(p, clicado, type);
			if (type == ChanllengeType.CUSTOM) {
				if (!desafio.hasExpire()) {
					openAccept(p, desafio);
				}
			} else if (!desafio.hasExpire()) {
				p.sendMessage(Configuration.PREFIX.getMessage() + "Você aceitou o desafio de §a" + clicado.getName());
				clicado.sendMessage(Configuration.PREFIX.getMessage() + "O jogador" + p.getName() + ChatColor.GREEN + " aceitou seu desafio.");
				setIn1v1(desafio);
				return;
			}
		}
		
		if ((hasDesafio(clicado, p, type)) && (!getDesafio(clicado, p, type).hasExpire())) {
			p.sendMessage(Configuration.PREFIX.getMessage() + "Você já enviou um desafio para o jogador §a\"" + clicado.getName() +"\"§f, aguarde §a" + DateUtils.getTime(getDesafio(clicado, p, type).getExpire()) + "§f!");
			return;
		}
		
		if (type == ChanllengeType.DEFAULT) {
			HashMap<ChanllengeType, HashMap<String, Desafio>> hash;
			if (this.playerDesafios.containsKey(clicado.getName())) {
				hash = (HashMap) this.playerDesafios.get(clicado.getName());
			} else {
				hash = new HashMap();
			}
			HashMap<String, Desafio> hashDesa;
			if (hash.containsKey(type)) {
				hashDesa = (HashMap) hash.get(type);
			} else {
				hashDesa = new HashMap();
			}
			hashDesa.put(p.getName(), new Desafio(p, clicado));
			hash.put(type, hashDesa);
			this.playerDesafios.put(clicado.getName(), hash);
			p.sendMessage(Configuration.PREFIX.getMessage() + "Você enviou um desafio para 1v1 normal para §a" + clicado.getName() + "§f.");
			clicado.sendMessage(Configuration.PREFIX.getMessage() + "Você recebeu desafio para 1v1 normal de §a" + p.getName() + "§f.");
		} else {
			openChallange(p, clicado);
		}
	}

	@EventHandler
	public void onTeleport(PlayerTeleportEvent event) {
		if (this.playerDesafios.containsKey(event.getPlayer().getName())) {
			this.playerDesafios.remove(event.getPlayer().getName());
		}
		if (this.playersInQueue.contains(event.getPlayer())) {
			this.playersInQueue.remove(event.getPlayer());
		}
	}

	public boolean hasDesafio(Player desafiado, Player desafiante, ChanllengeType type) {
		return (this.playerDesafios.containsKey(desafiado.getName()))
				&& (((HashMap) this.playerDesafios.get(desafiado.getName())).containsKey(type))
				&& (((HashMap) ((HashMap) this.playerDesafios.get(desafiado.getName())).get(type))
						.containsKey(desafiante.getName()));
	}

	public Desafio getDesafio(Player desafiado, Player desafiante, ChanllengeType type) {
		return (Desafio) ((HashMap) ((HashMap) this.playerDesafios.get(desafiado.getName())).get(type))
				.get(desafiante.getName());
	}

	public static boolean isIn1v1(Player player) {
		return playersIn1v1.contains(player);
	}

	public static void openChallange(Player p, Player desafiador) {
		ItemStack nullitem = getItem(Material.STAINED_GLASS_PANE, " ", new String[0]);
		ItemStack enviar = getItem(new ItemStack(Material.WOOL, 1, (short) 5), "§aEnviar!",
				new String[0]);
		ItemStack sword = getItem(Material.WOOD_SWORD, ChatColor.GOLD + "Espada de Madeira", new String[] {
				ChatColor.DARK_AQUA + "Clique aqui para mudar", ChatColor.DARK_AQUA + "o tipo da espada!" });
		ItemStack armor = getItem(Material.GLASS, ChatColor.RED + "Sem Armadura", new String[] {
				ChatColor.DARK_AQUA + "Clique aqui para mudar", ChatColor.DARK_AQUA + "o tipo de armadura!" });
		ItemStack speed = getItem(Material.GLASS_BOTTLE, ChatColor.RED + "Sem Velocidade", new String[] {
				ChatColor.DARK_AQUA + "Clique aqui para usar", ChatColor.DARK_AQUA + "pocao de velocidade!" });
		ItemStack forca = getItem(Material.GLASS_BOTTLE, ChatColor.RED + "Sem Forca", new String[] {
				ChatColor.DARK_AQUA + "Clique aqui para usar", ChatColor.DARK_AQUA + "pocao de forca!" });
		ItemStack sopa = getItem(Material.BOWL, ChatColor.GREEN + "1 HotBar",
				new String[] { ChatColor.DARK_AQUA + "Clique aqui para usar", ChatColor.DARK_AQUA + "full sopa!" });
		ItemStack sharp = getItem(Material.BOOK, ChatColor.GREEN + "Sem Afiada",
				new String[] { ChatColor.DARK_AQUA + "Clique aqui para usar", ChatColor.DARK_AQUA + "Afiada I" });
		Inventory inventoty = Bukkit.createInventory(null, 54, ChatColor.RED + "1v1 contra " + desafiador.getName());
		for (int i = 0; i < 54; i++) {
			inventoty.setItem(i, nullitem);
		}
		inventoty.setItem(20, sword);
		inventoty.setItem(21, armor);
		inventoty.setItem(22, speed);
		inventoty.setItem(23, forca);
		inventoty.setItem(24, sopa);
		inventoty.setItem(29, sharp);
		inventoty.setItem(43, enviar);
		inventoty.setItem(44, enviar);
		inventoty.setItem(52, enviar);
		inventoty.setItem(53, enviar);
		p.openInventory(inventoty);
	}

	public static void openAccept(Player p, Desafio desafio) {
		Player desafiador = desafio.getDesafiante();
		ItemStack nullitem = new ItemStack(Material.STAINED_GLASS_PANE);
		ItemMeta nullmeta = nullitem.getItemMeta();
		nullmeta.setDisplayName(" ");
		nullitem.setItemMeta(nullmeta);
		ItemStack bar = new ItemStack(Material.IRON_FENCE);
		ItemMeta barmeta = bar.getItemMeta();
		barmeta.setDisplayName(" ");
		bar.setItemMeta(barmeta);
		ItemStack fire = new ItemStack(Material.FIRE);
		ItemMeta firemeta = fire.getItemMeta();
		firemeta.setDisplayName(String.valueOf(ChatColor.AQUA.toString()) + ChatColor.BOLD + "1v1 Customizado");
		fire.setItemMeta(firemeta);
		ItemStack enviar = new ItemStack(Material.WOOL, 1, (short) 5);
		ItemMeta sendMeta = enviar.getItemMeta();
		sendMeta.setDisplayName(ChatColor.GREEN + "Aceitar Desafio!");
		enviar.setItemMeta(sendMeta);
		ItemStack rejeitar = new ItemStack(Material.WOOL, 1, (short) 14);
		ItemMeta rejeitarMeta = rejeitar.getItemMeta();
		rejeitarMeta.setDisplayName(ChatColor.RED + "Rejeitar Desafio!");
		rejeitar.setItemMeta(rejeitarMeta);
		String espada = desafio.getSwordType();
		String armadura = desafio.getArmorType();
		boolean sopa = desafio.isRefill();
		boolean forca = desafio.isStreght();
		boolean speed = desafio.isSpeed();
		boolean sharpness = desafio.hasSharp();
		ItemStack sword = getItem(Material.valueOf(espada.toUpperCase()), new Name().getName(espada.toUpperCase()),
				new String[0]);
		ItemStack armor = getItem(Material.valueOf(armadura.toUpperCase()), new Name().getName(armadura.toUpperCase()),
				new String[0]);
		ItemStack soup = null;
		ItemStack velo = null;
		ItemStack strenght = null;
		ItemStack sharp = null;
		if (speed) {
			velo = getItem(new ItemStack(Material.POTION, 1, (short) 2), ChatColor.GREEN + "Com Velocidade",
					new String[0]);
		} else {
			velo = getItem(Material.GLASS_BOTTLE, ChatColor.RED + "Sem Velocidade", new String[0]);
		}
		if (forca) {
			strenght = getItem(new ItemStack(Material.POTION, 1, (short) 9), ChatColor.GREEN + "Com Forca",
					new String[0]);
		} else {
			strenght = getItem(Material.GLASS_BOTTLE, ChatColor.RED + "Sem Forca", new String[0]);
		}
		if (sopa) {
			soup = getItem(Material.MUSHROOM_SOUP, ChatColor.AQUA + "Full Sopa", new String[0]);
		} else {
			soup = getItem(Material.BOWL, ChatColor.GREEN + "1 HotBar", new String[0]);
		}
		if (sharpness) {
			sharp = getItem(Material.ENCHANTED_BOOK, ChatColor.AQUA + "Com Afiada I", new String[0]);
		} else {
			sharp = getItem(Material.BOOK, ChatColor.GRAY + "Sem Afiada I", new String[0]);
		}
		Inventory inventoty = Bukkit.createInventory(null, 54, ChatColor.RED + "1v1 contra " + desafiador.getName());
		for (int i = 0; i < 54; i++) {
			inventoty.setItem(i, nullitem);
		}
		inventoty.setItem(20, sword);
		inventoty.setItem(21, armor);
		inventoty.setItem(22, velo);
		inventoty.setItem(23, strenght);
		inventoty.setItem(24, soup);
		inventoty.setItem(29, sharp);
		inventoty.setItem(46, rejeitar);
		inventoty.setItem(45, rejeitar);
		inventoty.setItem(36, rejeitar);
		inventoty.setItem(37, rejeitar);
		inventoty.setItem(43, enviar);
		inventoty.setItem(44, enviar);
		inventoty.setItem(52, enviar);
		inventoty.setItem(53, enviar);
		p.openInventory(inventoty);
	}

	public void setIn1v1(Desafio desafio) {
		if (desafio == null) {
			return;
		}
		Player p = desafio.getDesafiante();
		Player desafiado = desafio.getDesafiado();
		if (this.playersInQueue.contains(p)) {
			this.playersInQueue.remove(p);
		}
		if (this.playersInQueue.contains(desafiado)) {
			this.playersInQueue.remove(desafiado);
		}
		p.getInventory().setArmorContents(new ItemStack[0]);
		p.getInventory().setContents(new ItemStack[0]);
		desafiado.getInventory().setArmorContents(new ItemStack[0]);
		desafiado.getInventory().setContents(new ItemStack[0]);
		String espada = desafio.getSwordType();
		String armadura = desafio.getArmorType();
		boolean sopa = desafio.isRefill();
		boolean forca = desafio.isStreght();
		boolean speed = desafio.isSpeed();
		boolean sharp = desafio.hasSharp();
		setSword(p, espada, sharp);
		setArmor(p, armadura);
		setSoup(p, sopa);
		addSpeed(p, speed);
		addStrengh(p, forca);
		setSword(desafiado, espada, sharp);
		setArmor(desafiado, armadura);
		setSoup(desafiado, sopa);
		addSpeed(desafiado, speed);
		addStrengh(desafiado, forca);
		playersIn1v1.add(p);
		playersIn1v1.add(desafiado);
		getMain().getPlayerManager().removeProtection(p.getUniqueId());
		getMain().getPlayerManager().removeProtection(desafiado.getUniqueId());
		this.main.getPlayerHideManager().hideAllPlayers(p);
		this.main.getPlayerHideManager().hideAllPlayers(desafiado);
		p.showPlayer(desafiado);
		desafiado.showPlayer(p);
		
		if (this.firstLoction == null)
			this.firstLoction = ((br.com.yallandev.potepvp.manager.WarpManager.Warp1v1) main.getWarpManager().getWarp("1v1")).getFirstLocation();
		
		if (this.secondLoction == null)
			this.secondLoction = ((br.com.yallandev.potepvp.manager.WarpManager.Warp1v1) main.getWarpManager().getWarp("1v1")).getSecondLocation();
		
		desafiado.teleport(this.secondLoction);
		p.teleport(this.firstLoction);
		
//		String dname1 = "";
//		String dname2 = "";
//		
//		dname1 = desafiado.getName();
//		
//		if (desafiado.getName().length() > 14) {
//			dname1 = desafiado.getName().substring(0, 14);
//			dname2 = desafiado.getName().substring(14, desafiado.getName().length());
//		}
		
//		String pname1 = "";
//		String pname2 = "";
//		pname1 = p.getName();
//		
//		if (p.getName().length() > 14) {
//			pname1 = p.getName().substring(0, 14);
//			pname2 = p.getName().substring(14, p.getName().length());
//		}
		
		duel.put(p, desafiado.getName());
		duel.put(desafiado, p.getName());
		
//		this.scoreboard.updateScoreName(p, "battleplayer", "§3" + dname1);
//		this.scoreboard.updateScoreValue(p, "battleplayer", "§3" + dname2);
//		this.scoreboard.updateScoreName(desafiado, "battleplayer", "§3" + pname1);
//		this.scoreboard.updateScoreValue(desafiado, "battleplayer", "§3" + pname2);
		new Fight(this, p, desafiado);
		
		Scoreboarding.setScoreboard(p);
		Scoreboarding.setScoreboard(desafiado);
	}

	private static ItemStack getItem(Material mat, String name, String... desc) {
		ItemStack item = new ItemStack(mat);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		itemMeta.setLore(Arrays.asList(desc));
		item.setItemMeta(itemMeta);
		return item;
	}

	private static ItemStack getItem(ItemStack item, String name, String... desc) {
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		itemMeta.setLore(Arrays.asList(desc));
		item.setItemMeta(itemMeta);
		return item;
	}

	public static void setSword(Player p, String material, boolean sharpness) {
		ItemStack item = new ItemStack(Material.valueOf(material));
		if (sharpness) {
			item.addEnchantment(Enchantment.DAMAGE_ALL, 1);
		}
		p.getInventory().setItem(0, item);
	}

	public static void setSoup(Player p, boolean full) {
		int b = 9;
		if (full) {
			b = 39;
		}
		for (int i = 1; i < b; i++) {
			p.getInventory().addItem(new ItemStack[] { new ItemStack(Material.MUSHROOM_SOUP) });
		}
	}

	public static void addStrengh(Player p, boolean forca) {
		if (forca) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2000000, 0));
		}
	}

	public static void addSpeed(Player p, boolean speed) {
		if (speed) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2000000, 0));
		}
	}

	public static void setArmor(Player p, String material) {
		if (material.contains("GLASS")) {
			return;
		}
		material = material.replace("_CHESTPLATE", "");
		p.getInventory().setHelmet(new ItemStack(Material.valueOf(String.valueOf(material) + "_HELMET")));
		p.getInventory().setChestplate(new ItemStack(Material.valueOf(String.valueOf(material) + "_CHESTPLATE")));
		p.getInventory().setLeggings(new ItemStack(Material.valueOf(String.valueOf(material) + "_LEGGINGS")));
		p.getInventory().setBoots(new ItemStack(Material.valueOf(String.valueOf(material) + "_BOOTS")));
	}

	@EventHandler
	public void on1v1(InventoryClickEvent event) {
		ItemStack item = event.getCurrentItem();
		
		if (!(event.getWhoClicked() instanceof Player))
			return;
		
		if (item == null)
			return;
		
		if (!item.hasItemMeta())
			return;
		
		if (!item.getItemMeta().hasDisplayName())
			return;
		
		Player p = (Player) event.getWhoClicked();
		String inv = p.getOpenInventory().getTitle();
		
		if (!isOnWarp(p))
			return;
		
		if (!inv.contains(ChatColor.RED + "1v1 contra "))
			return;
		
		event.setCancelled(true);
		p.setItemOnCursor(new ItemStack(0));
		p.updateInventory();
		
		if ((event.isShiftClick()) || (event.isRightClick()))
			return;
		
		if (item.getItemMeta().getDisplayName().contains("§aEnviar!")) {
			String sword = p.getOpenInventory().getItem(20).getType().toString();
			String armor = p.getOpenInventory().getItem(21).getType().toString();
			String b = p.getOpenInventory().getItem(22).getType().toString();
			String c = p.getOpenInventory().getItem(23).getType().toString();
			String d = p.getOpenInventory().getItem(24).getType().toString();
			String e = p.getOpenInventory().getItem(29).getType().toString();
			boolean speed = true;
			boolean forca = true;
			boolean sopa = true;
			boolean sharpness = true;
			if (b.equalsIgnoreCase(Material.GLASS_BOTTLE.toString())) {
				speed = false;
			}
			if (c.equalsIgnoreCase(Material.GLASS_BOTTLE.toString())) {
				forca = false;
			}
			if (d.equalsIgnoreCase(Material.BOWL.toString())) {
				sopa = false;
			}
			if (e.equalsIgnoreCase(Material.BOOK.toString())) {
				sharpness = false;
			}
			Player desafiado = Bukkit.getPlayer(inv.replace(ChatColor.RED + "1v1 contra ", ""));
			
			if (desafiado == null) {
				p.sendMessage(Configuration.PREFIX.getMessage() + "O jogador §ac\"" + inv.replace(ChatColor.RED + "1v1 contra ", "") + "\"§f não está mais online!");
				p.closeInventory();
				return;
			}
			
			if (isIn1v1(desafiado)) {
				p.sendMessage(Configuration.PREFIX.getMessage() + "O player já está em 1v1 com outra pessoa!");
				p.closeInventory();
				return;
			}
			
			Desafio desafio = new Desafio(p, desafiado, sword, armor, sopa, speed, forca, sharpness);
			HashMap<ChanllengeType, HashMap<String, Desafio>> hash;
			
			if (this.playerDesafios.containsKey(desafiado.getName())) {
				hash = (HashMap) this.playerDesafios.get(desafiado.getName());
			} else {
				hash = new HashMap();
			}
			
			HashMap<String, Desafio> hashDesa;
			
			if (hash.containsKey(ChanllengeType.CUSTOM)) {
				hashDesa = (HashMap) hash.get(ChanllengeType.CUSTOM);
			} else {
				hashDesa = new HashMap();
			}
			
			hashDesa.put(p.getName(), desafio);
			hash.put(ChanllengeType.CUSTOM, hashDesa);
			this.playerDesafios.put(desafiado.getName(), hash);
			p.sendMessage(Configuration.PREFIX.getMessage() + "Você convidou o §a" + desafiado.getName() + "§f para 1v1 custom!");
			desafiado.sendMessage(Configuration.PREFIX.getMessage() + "O jogador §a" + p.getName() + "§f convidou você para 1v1 custom.");
			p.closeInventory();
		} else if (item.getType().toString().contains("_SWORD")) {
			String swordName = String.valueOf(getNextSwordLevel(item.getType().toString().replace("_SWORD", "")))
					+ "_SWORD";
			ItemStack sword2 = getItem(Material.valueOf(swordName), new Name().getName(swordName), new String[] {
					ChatColor.DARK_AQUA + "Clique aqui para mudar", ChatColor.DARK_AQUA + "o tipo da espada!" });
			p.getOpenInventory().setItem(event.getSlot(), sword2);
		} else if ((item.getType().toString().equalsIgnoreCase("GLASS")) || (item.getType().toString().contains("_CHESTPLATE"))) {
			String materialName;
			
			if (item.getType().toString().equalsIgnoreCase("DIAMOND_CHESTPLATE")) {
				materialName = getNextArmorLevel(item.getType().toString().replace("_CHESTPLATE", ""));
			} else {
				materialName = String.valueOf(getNextArmorLevel(item.getType().toString().replace("_CHESTPLATE", ""))) + "_CHESTPLATE";
			}
			
			ItemStack armor2 = getItem(Material.valueOf(materialName), new Name().getName(materialName), new String[] { ChatColor.DARK_AQUA + "Clique aqui para mudar", ChatColor.DARK_AQUA + "o tipo de armadura!" });
			p.getOpenInventory().setItem(event.getSlot(), armor2);
		} else if ((event.getSlot() == 22) && (item.getType().toString().equalsIgnoreCase("GLASS_BOTTLE"))) {
			ItemStack speed2 = getItem(new ItemStack(Material.POTION, 1, (short) 2), ChatColor.GREEN + "Com Velocidade", new String[] { ChatColor.DARK_AQUA + "Clique aqui para remover", ChatColor.DARK_AQUA + "a pocao de velocidade!" });
			p.getOpenInventory().setItem(event.getSlot(), speed2);
		} else if ((event.getSlot() == 22) && (item.getType().toString().equalsIgnoreCase("POTION"))) {
			ItemStack speed2 = getItem(Material.GLASS_BOTTLE, ChatColor.RED + "Sem Velocidade", new String[] { ChatColor.DARK_AQUA + "Clique aqui para usar", ChatColor.DARK_AQUA + "pocao de velocidade!" });
			p.getOpenInventory().setItem(event.getSlot(), speed2);
		} else if ((event.getSlot() == 23) && (item.getType().toString().equalsIgnoreCase("GLASS_BOTTLE"))) {
			ItemStack speed2 = getItem(new ItemStack(Material.POTION, 1, (short) 9), ChatColor.GREEN + "Com Forca", new String[] { ChatColor.DARK_AQUA + "Clique aqui para remover", ChatColor.DARK_AQUA + "a pocao de forca!" });
			p.getOpenInventory().setItem(event.getSlot(), speed2);
		} else if ((event.getSlot() == 23) && (item.getType().toString().equalsIgnoreCase("POTION"))) {
			ItemStack speed2 = getItem(Material.GLASS_BOTTLE, ChatColor.RED + "Sem Forca", new String[] { ChatColor.DARK_AQUA + "Clique aqui para usar", ChatColor.DARK_AQUA + "pocao de forca!" });
			p.getOpenInventory().setItem(event.getSlot(), speed2);
		} else if (item.getType().toString().equalsIgnoreCase("BOWL")) {
			ItemStack sopa2 = getItem(Material.MUSHROOM_SOUP, ChatColor.AQUA + "Full Sopa", new String[] { ChatColor.DARK_AQUA + "Clique aqui para usar", ChatColor.DARK_AQUA + "1 HotBar!" });
			p.getOpenInventory().setItem(event.getSlot(), sopa2);
		} else if (item.getType().toString().equalsIgnoreCase("MUSHROOM_SOUP")) {
			ItemStack sopa2 = getItem(Material.BOWL, ChatColor.GREEN + "1 HotBar", new String[] { ChatColor.DARK_AQUA + "Clique aqui para usar", ChatColor.DARK_AQUA + "FullSopa!" });
			p.getOpenInventory().setItem(event.getSlot(), sopa2);
		} else if (item.getType().toString().equalsIgnoreCase("ENCHANTED_BOOK")) {
			ItemStack sharp = getItem(Material.BOOK, ChatColor.GRAY + "Sem Afiada", new String[] { ChatColor.DARK_AQUA + "Clique aqui para usar", ChatColor.DARK_AQUA + "Afiada I" });
			p.getOpenInventory().setItem(event.getSlot(), sharp);
		} else if (item.getType().toString().equalsIgnoreCase("BOOK")) {
			ItemStack sharp = getItem(Material.ENCHANTED_BOOK, ChatColor.AQUA + "Com Afiada", new String[] { ChatColor.DARK_AQUA + "Clique aqui para usar", ChatColor.DARK_AQUA + "sem Afiada I" });
			p.getOpenInventory().setItem(event.getSlot(), sharp);
		}
	}

	private String getNextSwordLevel(String str) {
		switch (str) {
		case "DIAMOND": {
			return "WOOD";
		}
		case "IRON": {
			return "DIAMOND";
		}
		case "WOOD": {
			return "STONE";
		}
		case "STONE": {
			return "IRON";
		}
		default: {
			return "";
		}
		}
	}

	private String getNextArmorLevel(String str) {
		switch (str) {
		case "DIAMOND": {
			return "GLASS";
		}
		case "CHAINMAIL": {
			return "IRON";
		}
		case "IRON": {
			return "DIAMOND";
		}
		case "GLASS": {
			return "LEATHER";
		}
		case "LEATHER": {
			return "CHAINMAIL";
		}
		default: {
			return "";
		}
		}
	}

	@EventHandler
	public void on1v1Accept(InventoryClickEvent event) {
		ItemStack item = event.getCurrentItem();
		
		if (!(event.getWhoClicked() instanceof Player))
			return;
		
		if (item == null)
			return;
		
		if (!item.hasItemMeta())
			return;
		
		if (!item.getItemMeta().hasDisplayName())
			return;
		
		Player p = (Player) event.getWhoClicked();
		String inv = p.getOpenInventory().getTitle();
		
		if (!isOnWarp(p))
			return;
		
		if (!inv.contains(ChatColor.RED + "1v1 contra "))
			return;
		
		event.setCancelled(true);
		p.setItemOnCursor(new ItemStack(0));
		p.updateInventory();
		
		if ((event.isShiftClick()) || (event.isRightClick()))
			return;
		
		Player desafiante = Bukkit.getPlayer(inv.replace(ChatColor.RED + "1v1 contra ", ""));
		Player desafiado = p;
		
		if (item.getItemMeta().getDisplayName().contains("Aceitar Desafio!")) {
			if (desafiante == null) {
				p.sendMessage(ChatColor.RED + "O player nao esta mais online!");
				p.closeInventory();
				return;
			}
			if (isIn1v1(desafiante)) {
				p.sendMessage(ChatColor.RED + "O player esta em 1v1 com outra pessa");
				p.closeInventory();
				return;
			}
			if (this.playerDesafios.containsKey(desafiado.getName())) {
				HashMap<ChanllengeType, HashMap<String, Desafio>> hash = (HashMap) this.playerDesafios
						.get(desafiado.getName());
				if (hash.containsKey(ChanllengeType.CUSTOM)) {
					HashMap<String, Desafio> hashDesa = (HashMap) hash.get(ChanllengeType.CUSTOM);
					if (hashDesa.containsKey(desafiante.getName())) {
						Desafio desafio = (Desafio) hashDesa.get(desafiante.getName());
						setIn1v1(desafio);
						return;
					}
				}
			}
			p.sendMessage(ChatColor.YELLOW + "Ocorreu algum erro, tente novamente!");
			p.closeInventory();
		} else if (item.getItemMeta().getDisplayName().contains(ChatColor.RED + "Rejeitar Desafio!")) {
			if (desafiante == null) {
				p.sendMessage(ChatColor.RED + "O player nao esta mais online!");
				p.closeInventory();
				return;
			}
			if (isIn1v1(desafiante)) {
				p.sendMessage(ChatColor.RED + "O player esta em 1v1 com outra pessa");
				p.closeInventory();
				return;
			}
			if (isIn1v1(desafiante)) {
				p.sendMessage(ChatColor.AQUA + "Parece que seu desafiante ja esta em 1v1");
				return;
			}
			if (this.playerDesafios.containsKey(desafiado.getName())) {
				HashMap<ChanllengeType, HashMap<String, Desafio>> hash = (HashMap) this.playerDesafios
						.get(desafiado.getName());
				if (hash.containsKey(ChanllengeType.CUSTOM)) {
					HashMap<String, Desafio> hashDesa = (HashMap) hash.get(ChanllengeType.CUSTOM);
					if (hashDesa.containsKey(desafiante.getName())) {
						hashDesa.remove(desafiante.getName());
					}
				}
			}
			desafiante.sendMessage(ChatColor.AQUA + desafiado.getName() + ChatColor.RED + " rejeitou seu desafio");
			desafiado
					.sendMessage(ChatColor.RED + "Voce rejeitou o desafio de " + ChatColor.AQUA + desafiante.getName());
			p.closeInventory();
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		handleQuit(event.getPlayer());
	}

	@EventHandler
	public void onKick(PlayerKickEvent event) {
		handleQuit(event.getPlayer());
	}

	public void handleQuit(Player p) {
		if (this.playerDesafios.containsKey(p.getName())) {
			this.playerDesafios.remove(p.getName());
		}
		if (this.playersInQueue.contains(p)) {
			this.playersInQueue.remove(p);
		}
	}

	public void teleport1v1(Player p) {
		getMain().getKitManager().removeAbility(p.getUniqueId());
		getMain().getPlayerManager().setProtection(p.getUniqueId(), true);
		p.teleport(getMain().getWarpManager().getWarp("1v1").getWarpLocation());
		
		for (PotionEffect potion : p.getActivePotionEffects())
			p.removePotionEffect(potion.getType());
		
		set1v1(p);
	}

	public static enum ChanllengeType {
		
		DEFAULT("DEFAULT", 0), CUSTOM("CUSTOM", 1), FAST("FAST", 2);

		private ChanllengeType(String s, int n) {}
		
	}

	private class Fight {
		private Listener listener;

		public Fight(final Warp1v1 l, final Player player1, final Player player2) {
			this.listener = new Listener() {
				@EventHandler(priority = EventPriority.LOWEST)
				public void onDeathStatus(PlayerDeathInWarpEvent e) {
					Player p = e.getPlayer();
					
					if (!isInPvP(p))
						return;
					
					Player killer = null;
					
					if (p == player1) 
						killer = player2;
					
					if (p == player2) 
						killer = player1;
					
					int i = 0;
					for (ItemStack sopa : killer.getInventory().getContents()) {
						if ((sopa != null) && (sopa.getType() != Material.AIR)
								&& (sopa.getType() == Material.MUSHROOM_SOUP)) {
							i += sopa.getAmount();
						}
					}
					
					p.getInventory().clear();
					p.getInventory().setArmorContents(null);
					killer.getInventory().clear();
					killer.getInventory().setArmorContents(null);
					
					DecimalFormat dm = new DecimalFormat("##.#");
					p.sendMessage(Configuration.PREFIX.getMessage() + "O jogador §a" + killer.getName() + "§f venceu o 1v1 com " + dm.format(((Damageable)killer).getHealth() / 2.0D) + " corações e com " + i + " sopas.");
					killer.sendMessage(Configuration.PREFIX.getMessage() + "Voce venceu o 1v1 contra " + p.getName() + " com " + dm.format(((Damageable)killer).getHealth() / 2.0D) + " corações e com " + i + " sopas.");
					
					Warp1v1.this.main.getPlayerHideManager().showAllPlayers(killer);
					Warp1v1.this.main.getPlayerHideManager().showAllPlayers(p);
					
					killer.setHealth(20.0D);
					killer.updateInventory();
					p.setHealth(20.0D);
					p.updateInventory();
					
					l.teleport1v1(killer);
					
					new BukkitRunnable() {
						
						@Override
						public void run() {
							l.teleport1v1(p);
						}
					}.runTaskLater(BukkitMain.getInstance(), 10);
					
					Warp1v1.playersIn1v1.remove(p);
					Warp1v1.playersIn1v1.remove(killer);
					
					Scoreboarding.setScoreboard(killer);
					Scoreboarding.setScoreboard(p);
					Warp1v1.Fight.this.destroy();
					
					BukkitMain.getAccountCommon().getAccount(killer.getUniqueId()).updateVanished();
					BukkitMain.getAccountCommon().getAccount(p.getUniqueId()).updateVanished();
				}

				@EventHandler
				public void onEntityDamage(EntityDamageByEntityEvent event) {
					if (((event.getEntity() instanceof Player)) && ((event.getDamager() instanceof Player))) {
						Player recebe = (Player) event.getEntity();
						Player faz = (Player) event.getDamager();
						
						if ((isInPvP(faz)) && (isInPvP(recebe)))
							return;
						
						if ((isInPvP(faz)) && (!isInPvP(recebe)))
							event.setCancelled(true);
						else if ((!isInPvP(faz)) && (isInPvP(recebe)))
							event.setCancelled(true);
					}
				}

				@EventHandler
				public void onQuit(PlayerQuitEvent event) {
					handleQuit(event.getPlayer());
				}

				@EventHandler
				public void onKick(PlayerKickEvent event) {
					handleQuit(event.getPlayer());
				}

				public void handleQuit(Player p) {
					if (!isInPvP(p))
						return;
					
					Player killer = null;
					
					if (p == player1)
						killer = player2;
					
					if (p == player2)
						killer = player1;
					
					killer.sendMessage(Configuration.PREFIX.getMessage() + "O jogador §c\"" + p.getName() + "\"§f §2§lDESISTIU§f e §4§lSAIU§f do servidor!");
					
					killer.setHealth(20.0D);
					killer.updateInventory();
					l.teleport1v1(killer);
					
					Warp1v1.playersIn1v1.remove(p);
					Warp1v1.playersIn1v1.remove(killer);
					
					Warp1v1.this.main.getPlayerHideManager().showAllPlayers(killer);
					Warp1v1.this.main.getPlayerHideManager().showAllPlayers(p);
					
					Scoreboarding.setScoreboard(p);
					
					p.damage(2000D, killer);
					Warp1v1.Fight.this.destroy();
					
					BukkitMain.getAccountCommon().getAccount(killer.getUniqueId()).updateVanished();
				}

				public boolean isInPvP(Player player) {
					return (player == player1) || (player == player2);
				}
				
			};
			Bukkit.getPluginManager().registerEvents(this.listener, l.getMain());
		}

		public void destroy() {
			HandlerList.unregisterAll(this.listener);
		}
	}
	
	public BukkitMain getMain() {
		return main;
	}

	public static class Name {
		
		private HashMap<String, String> NAMES;

		public Name() {
			(this.NAMES = new HashMap()).put("WOOD_SWORD", "§aEspada de madeira!");
			this.NAMES.put("STONE_SWORD", "§aEspada de pedra!");
			this.NAMES.put("IRON_SWORD", "§aEspada de ferro!");
			this.NAMES.put("DIAMOND_SWORD", "§aEspada de diamante!");
			this.NAMES.put("GLASS", "§aSem armadura!");
			this.NAMES.put("LEATHER_CHESTPLATE", "§aArmadura de couro!");
			this.NAMES.put("CHAINMAIL_CHESTPLATE", "§aArmadura de malha!");
			this.NAMES.put("IRON_CHESTPLATE", "§aArmadura de ferro!");
			this.NAMES.put("DIAMOND_CHESTPLATE", "§aArmadura de diamante!");
		}

		public String getEnchantName(Enchantment enchant) {
			return getName(enchant.getName());
		}

		public String getItemName(ItemStack item) {
			if (item == null)
				item = new ItemStack(0);
			
			if (this.NAMES.containsKey(item.getType().name()))
				return (String) this.NAMES.get(item.getType().name());
			
			return getName(item.getType().name());
		}

		public String getName(String string) {
			if (this.NAMES.containsValue(string))
				return (String) this.NAMES.get(string);
			
			return toReadable(string);
		}

		public String toReadable(String string) {
			String[] names = string.split("_");
			
			for (int i = 0; i < names.length; i++)
				names[i] = (String.valueOf(names[i].substring(0, 1)) + names[i].substring(1).toLowerCase());
			
			return StringUtils.join(names, " ");
		}
	}

}
