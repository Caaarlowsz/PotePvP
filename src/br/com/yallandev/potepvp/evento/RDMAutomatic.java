package br.com.yallandev.potepvp.evento;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import br.com.yallandev.potepvp.BukkitMain;
import br.com.yallandev.potepvp.BukkitMain.Configuration;
import br.com.yallandev.potepvp.account.Account;
import br.com.yallandev.potepvp.event.update.UpdateEvent;
import br.com.yallandev.potepvp.event.update.UpdateEvent.UpdateType;
import br.com.yallandev.potepvp.listener.PlayerListener;
import br.com.yallandev.potepvp.manager.WarpManager.Warp;
import br.com.yallandev.potepvp.utils.ItemManager;

public class RDMAutomatic {

	private BukkitMain main;

	private int time;
	private GameType gameType;
	private Listener listener;
	private List<Player> players;
	private int maxPlayers;
	private boolean full;

	private boolean pvp;
	private List<Player> playersInPvp;

	private List<Player> specs;

	public RDMAutomatic() {
		this.main = BukkitMain.getInstance();
		this.time = 300;
		this.players = new ArrayList<>();
		this.gameType = GameType.STARTING;
		this.maxPlayers = 60;
		this.full = false;
		this.pvp = false;
		this.specs = new ArrayList<>();
		this.playersInPvp = new ArrayList<>();
		BukkitMain.broadcast("O evento �aRei da Mesa�f ir� iniciar em �a5 minutos�f.");
		BukkitMain.broadcast("Use �a/evento entrar�f para entrar no evento!");

		Bukkit.getPluginManager().registerEvents(listener = new Listener() {

			@EventHandler
			public void onUpdate(UpdateEvent e) {
				if (e.getType() != UpdateType.SECOND)
					return;

				if (getGameType() == GameType.STARTING) {
					if (time == 240) {
						BukkitMain.broadcast("O evento �aRei da Mesa�f ir� iniciar em �a4 minutos�f.");
						BukkitMain.broadcast("Use �a/evento entrar�f para entrar no evento!");
						BukkitMain.broadcast("�e" + players.size() + " jogadores de " + maxPlayers + " no evento.");
					}
					if (time == 180) {
						BukkitMain.broadcast("O evento �aRei da Mesa�f ir� iniciar em �a3 minutos�f.");
						BukkitMain.broadcast("Use �a/evento entrar�f para entrar no evento!");
						BukkitMain.broadcast("�e" + players.size() + " jogadores de " + maxPlayers + " no evento.");
					}
					if (time == 120) {
						BukkitMain.broadcast("O evento �aRei da Mesa�f ir� iniciar em �a2 minutos�f.");
						BukkitMain.broadcast("Use �a/evento entrar�f para entrar no evento!");
						BukkitMain.broadcast("�e" + players.size() + " jogadores de " + maxPlayers + " no evento.");
					}
					if (time == 90) {
						BukkitMain.broadcast("O evento �aRei da Mesa�f ir� iniciar em �a2 minutos e 30 segundos�f.");
						BukkitMain.broadcast("Use �a/evento entrar�f para entrar no evento!");
						BukkitMain.broadcast("�e" + players.size() + " jogadores de " + maxPlayers + " no evento.");
					}
					if (time == 60) {
						BukkitMain.broadcast("O evento �aRei da Mesa�f ir� iniciar em �a1 minuto�f.");
						BukkitMain.broadcast("Use �a/evento entrar�f para entrar no evento!");
						BukkitMain.broadcast("�e" + players.size() + " jogadores de " + maxPlayers + " no evento.");
					}
					if (time == 30) {
						BukkitMain.broadcast("O evento �aRei da Mesa�f ir� iniciar em �a30 segundos�f.");
						BukkitMain.broadcast("Use �a/evento entrar�f para entrar no evento!");
						BukkitMain.broadcast("�e" + players.size() + " jogadores de " + maxPlayers + " no evento.");
					}
					if (time == 15) {
						BukkitMain.broadcast("O evento �aRei da Mesa�f ir� iniciar em �a15 segundos�f.");
						BukkitMain.broadcast("Use �a/evento entrar�f para entrar no evento!");
						BukkitMain.broadcast("�e" + players.size() + " jogadores de " + maxPlayers + " no evento.");
					}

					if (time == 10) {
						BukkitMain.broadcast("O evento �aRei da Mesa�f ir� iniciar em �a10 segundos�f.");
						BukkitMain.broadcast("Use �a/evento entrar�f para entrar no evento!");
						BukkitMain.broadcast("�e" + players.size() + " jogadores de " + maxPlayers + " no evento.");
					}

					if (players.size() == (10 - maxPlayers) && time >= 50 && !full) {
						time = 30;
						BukkitMain.broadcast(
								"O tempo foi alterado para �a30 segundos�f, porque o evento est� quase cheio!");
						full = true;
					}

					if (time <= 0) {
						gameType = GameType.GAMIMG;
						BukkitMain.broadcast("O evento �aRei da Mesa�f iniciou!");
					}

					time--;
				} else {
					if (!pvp) {
						queuedPlayers();
					}
				}
			}

			@EventHandler
			public void onPlayerQuit(PlayerQuitEvent e) {
				if (players.contains(e.getPlayer())) {
					players.remove(e.getPlayer());

					if (playersInPvp.contains(e.getPlayer())) {
						e.getPlayer().damage(9999D);
						playersInPvp.remove(e.getPlayer());
						pvp = false;
						broadcast("O jogador �a" + e.getPlayer().getName()
								+ "�f foi eliminado do evento por combate log!");
						return;
					}

					if (getGameType() == GameType.GAMIMG) {
						broadcast("O jogador �a" + e.getPlayer().getName()
								+ "�f saiu do servidor e foi desclassificado do evento!");
					}
				}
			}

			@EventHandler
			public void onPlayerDeath(PlayerDeathEvent e) {
				if (!(e.getEntity() instanceof Player))
					return;

				if (e.getEntity().getKiller() == null)
					return;

				Player p = e.getEntity();
				Player d = e.getEntity().getKiller();

				if (players.contains(d) || players.contains(p)) {
					if (playersInPvp.contains(d) && playersInPvp.contains(p)) {
						playersInPvp.remove(p);
						players.remove(p);
						pvp = false;
						p.sendMessage(Configuration.PREFIX.getMessage() + "Voc� foi eliminado do evento pelo �a"
								+ d.getName() + "�f!");
						broadcast("O jogador �a" + p.getName() + "�f foi eliminado do evento pelo �a" + d.getName()
								+ "�f.");
						broadcast("�a" + players.size() + "�f jogadores restantes.");
						broadcast("Procurando proximo jogador...");
					}
				}
			}

			@EventHandler(priority = EventPriority.MONITOR)
			public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
				if (!(e.getDamager() instanceof Player))
					return;

				if (!isSpec((Player) e.getDamager()))
					return;

				e.setCancelled(true);
			}

			@EventHandler
			public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent e) {
				Player p = e.getPlayer();

				if (!isInEvent(p))
					return;

				if (isInPvP(p)) {
					e.setCancelled(true);
					return;
				}

				if (e.getMessage().toLowerCase().startsWith("/warp")) {
					e.setCancelled(true);
					p.sendMessage(Configuration.PREFIX.getMessage() + "Use �a/evento sair�f para sair do evento!");
					return;
				}

				if (e.getMessage().toLowerCase().startsWith("/kit")) {
					e.setCancelled(true);
					p.sendMessage(Configuration.PREFIX.getMessage() + "Use �a/evento sair�f para sair do evento!");
					return;
				}
			}

		}, main);
	}

	public boolean isInEvent(Player player) {
		return getPlayers().contains(player);
	}

	public void queuedPlayers() {
		Player firstPlayer = null;
		Player secondPlayer = null;

		if (players.size() == 1) {
			Player winner = players.get(0);
			this.playersInPvp.clear();
			BukkitMain.broadcast("O jogador �a" + winner.getName() + "�f ganhou o evento!");

			Account player = BukkitMain.getAccountCommon().getAccount(winner.getUniqueId());

			player.sendMessage("Voc� ganhou �a50 de xp�f!");
			player.sendMessage("Voc� ganhou �a1500 de coins�f!");

			player.getStatus().addXp(100);
			player.getStatus().addMoney(1500);
			player.saveStatus();

			destroy();
			return;
		}

		if (players.size() == 0) {
			BukkitMain.broadcast("N�o houve nenhum ganhador!");
			this.playersInPvp.clear();
			return;
		}

		for (Player players : this.players) {
			if (!players.isOnline())
				this.players.remove(players);
		}

		firstPlayer = null;
		secondPlayer = this.players.get(new Random().nextInt(this.players.size()));

		if (playersInPvp.isEmpty()) {
			firstPlayer = this.players.get(new Random().nextInt(this.players.size()));
		} else {
			firstPlayer = playersInPvp.get(0);
			playersInPvp.clear();
		}

		while (secondPlayer.getUniqueId().equals(firstPlayer.getUniqueId()))
			secondPlayer = this.players.get(new Random().nextInt(this.players.size()));

		firstPlayer.teleport(new Location(Bukkit.getWorld("world"), 0, 0, 0));
		secondPlayer.teleport(new Location(Bukkit.getWorld("world"), 0, 0, 0));

		firstPlayer.closeInventory();
		secondPlayer.closeInventory();

		send1v1(firstPlayer, secondPlayer);
	}

	public void broadcast(String message) {
		for (Player players : this.players) {
			players.sendMessage(Configuration.PREFIX.getMessage() + message);
		}

		for (Player players : this.specs) {
			players.sendMessage(Configuration.PREFIX.getMessage() + message);
		}
	}

	public void send1v1(Player firstPlayer, Player secondPlayer) {
		this.playersInPvp.clear();
		this.playersInPvp.add(firstPlayer);
		this.playersInPvp.add(secondPlayer);

		System.out.println("[EVENTO] " + firstPlayer.getName() + " VS " + secondPlayer.getName());

		broadcast("O jogador �a" + firstPlayer.getName() + "�f ir� lutar contra o �c" + secondPlayer.getName() + "�f.");

		firstPlayer.setHealth(20D);
		secondPlayer.setHealth(20D);

		main.getPlayerManager().removeProtection(firstPlayer.getUniqueId());
		main.getPlayerManager().removeProtection(secondPlayer.getUniqueId());

		firstPlayer.teleport(new Location(Bukkit.getWorld("world"), 3000.5, 62.5, 3018.5));
		firstPlayer.getEyeLocation().setYaw(180);

		secondPlayer.teleport(new Location(Bukkit.getWorld("world"), 3000.5, 62.5, 2982.5));
		secondPlayer.getEyeLocation().setYaw(0);

		firstPlayer.sendMessage(
				Configuration.PREFIX.getMessage() + "Voc� ir� batalhar contra o �a" + secondPlayer.getName() + "�f.");
		secondPlayer.sendMessage(
				Configuration.PREFIX.getMessage() + "Voc� ir� batalhar contra o �a" + firstPlayer.getName() + "�f.");

		for (PotionEffect pot : firstPlayer.getActivePotionEffects())
			firstPlayer.removePotionEffect(pot.getType());

		for (PotionEffect pot : secondPlayer.getActivePotionEffects())
			secondPlayer.removePotionEffect(pot.getType());

		firstPlayer.getInventory().clear();
		firstPlayer.getInventory().setArmorContents(new ItemStack[4]);
		firstPlayer.closeInventory();

		secondPlayer.getInventory().clear();
		secondPlayer.getInventory().setArmorContents(new ItemStack[4]);
		secondPlayer.closeInventory();

		firstPlayer.getInventory().setItem(0, new ItemManager(Material.DIAMOND_SWORD, "�a�nEspada de diamante!")
				.addEnchantment(Enchantment.DAMAGE_ALL, 1).build());
		secondPlayer.getInventory().setItem(0, new ItemManager(Material.DIAMOND_SWORD, "�a�nEspada de diamante!")
				.addEnchantment(Enchantment.DAMAGE_ALL, 1).build());

		firstPlayer.getInventory().setHelmet(new ItemManager(Material.IRON_HELMET, "�a�nCapacete de ferro!").build());
		firstPlayer.getInventory()
				.setChestplate(new ItemManager(Material.IRON_CHESTPLATE, "�a�nPeitoral de ferro!").build());
		firstPlayer.getInventory().setLeggings(new ItemManager(Material.IRON_LEGGINGS, "�a�nCal�a de ferro!").build());
		firstPlayer.getInventory().setBoots(new ItemManager(Material.IRON_BOOTS, "�a�nBota de ferro!").build());

		secondPlayer.getInventory().setHelmet(new ItemManager(Material.IRON_HELMET, "�a�nCapacete de ferro!").build());
		secondPlayer.getInventory()
				.setChestplate(new ItemManager(Material.IRON_CHESTPLATE, "�a�nPeitoral de ferro!").build());
		secondPlayer.getInventory().setLeggings(new ItemManager(Material.IRON_LEGGINGS, "�a�nCal�a de ferro!").build());
		secondPlayer.getInventory().setBoots(new ItemManager(Material.IRON_BOOTS, "�a�nBota de ferro!").build());

		for (int x = 0; x < 8; x++) {
			firstPlayer.getInventory().addItem(new ItemStack(Material.MUSHROOM_SOUP));
			secondPlayer.getInventory().addItem(new ItemStack(Material.MUSHROOM_SOUP));
		}

		this.pvp = true;
	}

	public GameType getGameType() {
		return gameType;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public List<Player> getPlayersInPvp() {
		return playersInPvp;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public void putInEvent(Player player) {
		if (players.contains(player)) {
			player.sendMessage(Configuration.PREFIX.getMessage() + "Voc� j� est� no evento!");
			return;
		}

		player.sendMessage(Configuration.PREFIX.getMessage() + "Voc� entrou no evento!");
		players.add(player);

		main.getPlayerManager().setWarp(player.getUniqueId(), main.getWarpManager().getWarp("Spawn"));
		main.getPlayerManager().addProtection(player.getUniqueId());
		main.getKitManager().removeAbility(player.getUniqueId());

		player.getInventory().clear();
		player.getInventory().setArmorContents(new ItemStack[4]);

		for (PotionEffect pot : player.getActivePotionEffects())
			player.removePotionEffect(pot.getType());

		player.teleport(new Location(Bukkit.getWorld("world"), 3000.5, 61.5, 3048.5));
	}

	public void leaveEvent(Player player) {
		if (!players.contains(player)) {
			player.sendMessage(Configuration.PREFIX.getMessage() + "Voc� n�o est� no evento!");
			return;
		}

		if (isInPvP(player)) {
			player.sendMessage("Voc� n�o pode sair do evento enquanto estiver no pvp!");
			return;
		}

		if (getGameType() == GameType.STARTING) {
			player.sendMessage(Configuration.PREFIX.getMessage() + "Voc� saiu do evento!");
		} else {
			player.sendMessage(Configuration.PREFIX.getMessage() + "Voc� foi eliminado do evento!");
		}

		Warp warp = main.getWarpManager().getWarp("Spawn");

		player.teleport(warp.getWarpLocation());
		main.getPlayerManager().setWarp(player.getUniqueId(), warp);
		main.getPlayerManager().setProtection(player.getUniqueId(), true);
		main.getKitManager().removeAbility(player.getUniqueId());
		PlayerListener.setItem(BukkitMain.getAccountCommon().getAccount(player.getUniqueId()));

		players.remove(player);
	}

	public boolean isInPvP(Player player) {
		return playersInPvp.contains(player) && getGameType() == GameType.GAMIMG;
	}

	public void destroy() {

		for (Player players : getSpecs()) {
			if (!players.isOnline())
				continue;

			Account player = BukkitMain.getAccountCommon().getAccount(players.getUniqueId());

			if (player == null)
				continue;

			player.desmakeVanish();

			Warp warp = main.getWarpManager().getWarp("Spawn");

			players.teleport(warp.getWarpLocation());
			main.getPlayerManager().setWarp(players.getUniqueId(), warp);
			main.getPlayerManager().setProtection(players.getUniqueId(), true);
			main.getKitManager().removeAbility(players.getUniqueId());
			PlayerListener.setItem(BukkitMain.getAccountCommon().getAccount(players.getUniqueId()));
		}

		HandlerList.unregisterAll(this.listener);
		BukkitMain.getInstance().getEventManager().setRdmAutomatic(null);
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getTime() {
		return time;
	}

	public List<Player> getSpecs() {
		return specs;
	}

	public boolean isSpec(Player p) {
		return specs.contains(p);
	}

	public void setSpec(Account player) {
		Player p = player.getPlayer();

		if (getSpecs().contains(p)) {
			player.sendMessage("Voc� j� � um espectador!");
			return;
		}

		if (getPlayers().contains(p)) {
			player.sendMessage("Voc� n�o pode ser um espectador jogando!");
			return;
		}

		p.teleport(new Location(Bukkit.getWorld("world"), 3000.5, 61.5, 3048.5));
		p.getInventory().clear();
		p.getInventory().setArmorContents(new ItemStack[4]);

		p.setAllowFlight(true);
		player.makeVanish();

		player.sendMessage("Voc� entrou no modo espectador!");
		player.sendMessage("Use �a/evento spec�f para sair do modo spec!");
		getSpecs().add(p);
	}

	public void removeSpec(Account player) {
		Player p = player.getPlayer();

		if (getSpecs().contains(p)) {
			player.sendMessage("Voc� j� � um espectador!");
			return;
		}

		getSpecs().remove(p);

		p.setAllowFlight(false);
		player.desmakeVanish();
		player.sendMessage("Voc� saiu do modo espectador!");

		Warp warp = main.getWarpManager().getWarp("Spawn");

		p.teleport(warp.getWarpLocation());
		main.getPlayerManager().setWarp(p.getUniqueId(), warp);
		main.getPlayerManager().setProtection(p.getUniqueId(), true);
		main.getKitManager().removeAbility(p.getUniqueId());
		PlayerListener.setItem(BukkitMain.getAccountCommon().getAccount(p.getUniqueId()));
	}

	public enum GameType {

		STARTING, GAMIMG;

	}

}
