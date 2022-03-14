package br.com.yallandev.potepvp.kit.register.gladiator;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.caaarlowsz.potemc.kitpvp.PotePvP;

public class GladiatorFight {

	private PotePvP main;
	private Player gladiator;
	private Player target;
	private Location tpLocGladiator;
	private Location tpLocTarget;
	private BukkitRunnable witherEffect;
	private BukkitRunnable teleportBack;
	private List<Block> blocksToRemove;
	private Listener listener;
	private String type;
	private boolean ended;

	public GladiatorFight(final Player gladiator, final Player target, PotePvP bc) {
		this.main = bc;
		this.gladiator = gladiator;
		this.target = target;
		this.blocksToRemove = new ArrayList<>();
		send1v1("Gladiator");
		this.ended = false;
		this.listener = new Listener() {

			@EventHandler(priority = EventPriority.LOWEST)
			public void onDeath(PlayerDeathEvent e) {
				if (e.getEntity() instanceof Player) {
					if (e.getEntity().getKiller() instanceof Player) {
						if ((GladiatorFight.this.isIn1v1(e.getEntity())) && (!GladiatorFight.this.ended)) {
							GladiatorFight.this.ended = true;
							if (e.getEntity() == gladiator) {
								target.sendMessage(PotePvP.getPrefix() + "Voc� ganhou o gladiator.");
								target.addPotionEffect(
										new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100000));
								GladiatorFight.this.teleportBack(target, gladiator);
								return;
							}
							gladiator.sendMessage(PotePvP.getPrefix() + "Voc� ganhou o gladiator.");
							gladiator
									.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100000));
							GladiatorFight.this.teleportBack(gladiator, target);

							e.getDrops().clear();
							e.setDroppedExp(0);
						}
					}
				}
			}

			@EventHandler
			public void onQuit(PlayerQuitEvent e) {
				Player p = e.getPlayer();
				if (!GladiatorFight.this.isIn1v1(p)) {
					return;
				}
				if (e.getPlayer().isDead()) {
					return;
				}
				if (!GladiatorFight.this.ended) {
					GladiatorFight.this.ended = true;
					if (p == gladiator) {
						target.sendMessage(PotePvP.getPrefix() + "Voc� ganhou o gladiator.");
						target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100000));
						GladiatorFight.this.teleportBack(target, gladiator);
						return;
					}
					gladiator.sendMessage(PotePvP.getPrefix() + "Voc� ganhou o gladiator.");
					gladiator.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100000));
					GladiatorFight.this.teleportBack(gladiator, target);
				}
			}

			@EventHandler
			public void onKick(PlayerKickEvent event) {
				Player p = event.getPlayer();
				if (!GladiatorFight.this.isIn1v1(p)) {
					return;
				}
				if (event.getPlayer().isDead()) {
					return;
				}
				if (!GladiatorFight.this.ended) {
					GladiatorFight.this.ended = true;
					if (p == gladiator) {
						target.sendMessage(PotePvP.getPrefix() + "Voc� ganhou o gladiator.");
						target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100000));
						GladiatorFight.this.teleportBack(target, gladiator);
						return;
					}
					gladiator.sendMessage(PotePvP.getPrefix() + "Voc� ganhou o gladiator.");
					gladiator.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100000));
					GladiatorFight.this.teleportBack(gladiator, target);
				}
			}
		};
		this.main.getServer().getPluginManager().registerEvents(this.listener, this.main);
	}

	public GladiatorFight(final Player gladiator, final Player target, String type, PotePvP bc) {
		this.main = bc;
		this.type = type;
		this.gladiator = gladiator;
		this.target = target;
		this.blocksToRemove = new ArrayList<>();
		send1v1(type);
		this.ended = false;
		this.listener = new Listener() {

			@EventHandler(priority = EventPriority.LOWEST)
			public void onDeath(PlayerDeathEvent e) {
				if (e.getEntity() instanceof Player) {
					if (e.getEntity().getKiller() instanceof Player) {
						if ((GladiatorFight.this.isIn1v1(e.getEntity())) && (!GladiatorFight.this.ended)) {
							GladiatorFight.this.ended = true;
							if (e.getEntity() == gladiator) {
								target.sendMessage(PotePvP.getPrefix() + "Voc� ganhou o gladiator.");
								target.addPotionEffect(
										new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100000));
								GladiatorFight.this.teleportBack(target, gladiator);
								return;
							}
							gladiator.sendMessage(PotePvP.getPrefix() + "Voc� ganhou o gladiator.");
							gladiator
									.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100000));
							dropItems(e.getEntity(), GladiatorFight.this.tpLocTarget);
							GladiatorFight.this.teleportBack(gladiator, target);

							e.getDrops().clear();
							e.setDroppedExp(0);
						}
					}
				}
			}

			@EventHandler
			public void onQuit(PlayerQuitEvent e) {
				Player p = e.getPlayer();
				if (!GladiatorFight.this.isIn1v1(p)) {
					return;
				}
				if (e.getPlayer().isDead()) {
					return;
				}
				if (!GladiatorFight.this.ended) {
					GladiatorFight.this.ended = true;
					if (p == gladiator) {
						target.sendMessage(PotePvP.getPrefix() + "Voc� ganhou o gladiator.");
						target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100000));
						GladiatorFight.this.teleportBack(target, gladiator);
						return;
					}
					gladiator.sendMessage(PotePvP.getPrefix() + "Voc� ganhou o gladiator.");
					gladiator.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100000));
					dropItems(p, GladiatorFight.this.tpLocTarget);
					GladiatorFight.this.teleportBack(gladiator, target);
				}
			}

			@EventHandler
			public void onKick(PlayerKickEvent event) {
				Player p = event.getPlayer();
				if (!GladiatorFight.this.isIn1v1(p)) {
					return;
				}
				if (event.getPlayer().isDead()) {
					return;
				}
				if (!GladiatorFight.this.ended) {
					GladiatorFight.this.ended = true;
					if (p == gladiator) {
						target.sendMessage(PotePvP.getPrefix() + "Voc� ganhou o gladiator.");
						target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100000));
						GladiatorFight.this.teleportBack(target, gladiator);
						return;
					}
					gladiator.sendMessage(PotePvP.getPrefix() + "Voc� ganhou o gladiator.");
					gladiator.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100000));
					dropItems(p, GladiatorFight.this.tpLocTarget);
					GladiatorFight.this.teleportBack(gladiator, target);
				}
			}
		};
		this.main.getServer().getPluginManager().registerEvents(this.listener, this.main);
	}

	public String getType() {
		if (type == null)
			return "Gladiator";
		return type;
	}

	public boolean isIn1v1(Player player) {
		return (player == this.gladiator) || (player == this.target);
	}

	public void destroy() {
		HandlerList.unregisterAll(this.listener);
	}

	public void send1v1(String type) {
		Location loc = this.gladiator.getLocation();
		boolean hasGladi = true;
		while (hasGladi) {
			hasGladi = false;
			boolean stop = false;
			for (double x = -8.0D; x <= 8.0D; x += 1.0D) {
				for (double z = -8.0D; z <= 8.0D; z += 1.0D) {
					for (double y = 0.0D; y <= 10.0D; y += 1.0D) {
						Location l = new Location(loc.getWorld(), loc.getX() + x, 120.0D + y, loc.getZ() + z);
						if (l.getBlock().getType() != Material.AIR) {
							hasGladi = true;
							loc = new Location(loc.getWorld(), loc.getX() + 20.0D, loc.getY(), loc.getZ());
							stop = true;
						}
						if (stop) {
							break;
						}
					}
					if (stop) {
						break;
					}
				}
				if (stop) {
					break;
				}
			}
		}

		Block mainBlock = loc.getBlock();
		generateBlocks(mainBlock);
		this.tpLocGladiator = this.gladiator.getLocation().clone();
		this.tpLocTarget = this.target.getLocation().clone();
		this.gladiator.sendMessage(
				PotePvP.getPrefix() + "�fVoc� puxou �c" + this.target.getName() + " �fpara o gladiator!");
		this.gladiator.sendMessage(PotePvP.getPrefix() + "�fVoc� tem �a5 segundos �fde invencibilidade.");
		this.target.sendMessage(
				PotePvP.getPrefix() + "�fVoc� foi puxado �9�l" + this.gladiator.getName() + " �fpara o gladiator!");
		this.target.sendMessage(PotePvP.getPrefix() + "�fVoc� tem �a5 segundos �fde invencibilidade.");
		Location l2 = new Location(mainBlock.getWorld(), mainBlock.getX() + 6.5D, 121.0D, mainBlock.getZ() + 6.5D);
		l2.setYaw(135.0F);
		Location l3 = new Location(mainBlock.getWorld(), mainBlock.getX() - 5.5D, 121.0D, mainBlock.getZ() - 5.5D);
		l3.setYaw(315.0F);
		this.target.teleport(l2);
		this.gladiator.teleport(l3);
		this.main.getGladiatorFightController().addPlayerToFights(this.gladiator.getUniqueId(), getType());
		this.main.getGladiatorFightController().addPlayerToFights(this.target.getUniqueId(), getType());
		this.gladiator.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100000));
		this.target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100000));

		if (getType().equalsIgnoreCase("Whisted")) {
			int random = 1;

			if (this.target.getInventory().firstEmpty() == -1) {
				random = this.target.getInventory().first(Material.MUSHROOM_SOUP);
			} else {
				random = this.target.getInventory().firstEmpty();
			}

			this.target.getInventory().setItem(random, new ItemStack(Material.COBBLE_WALL, 16));

			random = 1;

			if (this.gladiator.getInventory().firstEmpty() == -1) {
				random = this.gladiator.getInventory().first(Material.MUSHROOM_SOUP);
			} else {
				random = this.gladiator.getInventory().firstEmpty();
			}

			this.gladiator.getInventory().setItem(random, new ItemStack(Material.COBBLE_WALL, 16));
		} else if (getType().equalsIgnoreCase("Infernor")) {
			this.gladiator.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 15 * 20, 0));
		}

		(this.witherEffect = new BukkitRunnable() {
			public void run() {
				GladiatorFight.this.gladiator.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 1200, 5));
				GladiatorFight.this.target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 1200, 5));
			}
		}).runTaskLater(this.main, 2400L);
		(this.teleportBack = new BukkitRunnable() {
			public void run() {
				GladiatorFight.this.teleportBack(GladiatorFight.this.tpLocGladiator, GladiatorFight.this.tpLocTarget);
			}
		}).runTaskLater(this.main, 3600L);
	}

	public void teleportBack(Location loc, Location loc1) {
		this.main.getGladiatorFightController().removePlayerFromFight(this.gladiator.getUniqueId());
		this.main.getGladiatorFightController().removePlayerFromFight(this.target.getUniqueId());
		this.gladiator.teleport(loc);
		this.target.teleport(loc1);
		removeBlocks();
		this.gladiator.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100000));
		this.target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100000));
		this.gladiator.removePotionEffect(PotionEffectType.WITHER);
		this.target.removePotionEffect(PotionEffectType.WITHER);
		stop();
		destroy();
	}

	public void teleportBack(Player winner, Player loser) {
		this.main.getGladiatorFightController().removePlayerFromFight(winner.getUniqueId());
		this.main.getGladiatorFightController().removePlayerFromFight(loser.getUniqueId());
		winner.teleport(this.tpLocGladiator);
		removeBlocks();
		winner.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100000));
		winner.removePotionEffect(PotionEffectType.WITHER);

		if (getType().equalsIgnoreCase("Whisted")) {
			winner.getInventory().removeItem(new ItemStack(Material.COBBLE_WALL));
		}

		stop();
		destroy();
	}

	public void generateBlocks(Block mainBlock) {
		for (double x = -8.0D; x <= 8.0D; x += 1.0D) {
			for (double z = -8.0D; z <= 8.0D; z += 1.0D) {
				for (double y = 0.0D; y <= 9.0D; y += 1.0D) {
					Location l = new Location(mainBlock.getWorld(), mainBlock.getX() + x, 120.0D + y,
							mainBlock.getZ() + z);
					l.getBlock().setType(Material.GLASS);
					this.main.getGladiatorFightController().addBlock(l.getBlock());
					this.blocksToRemove.add(l.getBlock());
				}
			}
		}
		for (double x = -7.0D; x <= 7.0D; x += 1.0D) {
			for (double z = -7.0D; z <= 7.0D; z += 1.0D) {
				for (double y = 1.0D; y <= 8.0D; y += 1.0D) {
					Location l = new Location(mainBlock.getWorld(), mainBlock.getX() + x, 120.0D + y,
							mainBlock.getZ() + z);
					l.getBlock().setType(Material.AIR);
				}
			}
		}
	}

	public void removeBlocks() {
		for (Block b : this.blocksToRemove) {
			if ((b.getType() != null) && (b.getType() != Material.AIR)) {
				b.setType(Material.AIR);
			}
			this.main.getGladiatorFightController().removeBlock(b);
		}
		this.blocksToRemove.clear();
	}

	public void stop() {
		this.witherEffect.cancel();
		this.teleportBack.cancel();
	}

	public void dropItems(final Player p, final Location l) {
		final ArrayList<ItemStack> itens = new ArrayList<ItemStack>();
		ItemStack[] contents;
		for (int length = (contents = p.getPlayer().getInventory().getContents()).length, i = 0; i < length; ++i) {
			final ItemStack item = contents[i];
			if (item != null && item.getType() != Material.AIR) {
				itens.add(item.clone());
			}
		}
		ItemStack[] armorContents;
		for (int length2 = (armorContents = p.getPlayer().getInventory().getArmorContents()).length,
				j = 0; j < length2; ++j) {
			final ItemStack item = armorContents[j];
			if (item != null && item.getType() != Material.AIR) {
				itens.add(item.clone());
			}
		}
		if (p.getPlayer().getItemOnCursor() != null && p.getPlayer().getItemOnCursor().getType() != Material.AIR) {
			itens.add(p.getPlayer().getItemOnCursor().clone());
		}
		this.dropItems(p, itens, l);
	}

	@SuppressWarnings({ "null" })
	public void dropItems(final Player p, final List<ItemStack> itens, final Location l) {
		final World world = l.getWorld();
		for (final ItemStack item : itens) {
			if (item != null || item.getType() != Material.AIR) {
				if (item.hasItemMeta()) {
					world.dropItemNaturally(l, item.clone()).getItemStack().setItemMeta(item.getItemMeta());
				} else {
					world.dropItemNaturally(l, item);
				}
			}

		}
		p.getPlayer().getInventory().setArmorContents(new ItemStack[4]);
		p.getPlayer().getInventory().clear();
		p.getPlayer().setItemOnCursor(new ItemStack(0));
		for (final PotionEffect pot : p.getActivePotionEffects()) {
			p.removePotionEffect(pot.getType());
		}
		itens.clear();
	}
}
