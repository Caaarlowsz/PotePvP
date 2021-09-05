package br.com.yallandev.potepvp.kit.register;

import java.util.Arrays;
import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import br.com.yallandev.potepvp.BukkitMain;
import br.com.yallandev.potepvp.kit.Kit;
import br.com.yallandev.potepvp.kit.register.gladiator.GladiatorFight;

public class Gladiator extends Kit {

	public Gladiator() {
		super("Gladiator", Material.IRON_FENCE, 25000, true, Arrays.asList("Use seu gladiator para", "puxar alguém para uma", "arena 1v1 onde vocês", "possam tirar pvp sem", "interferencia externa."));
	}

	@EventHandler
	public void onPlayerInteractEntityListener(PlayerInteractEntityEvent e) {
		Player player = e.getPlayer();
		if ((e.getPlayer().getItemInHand() != null) && (e.getPlayer().getItemInHand().getType() == Material.IRON_FENCE)
				&& (hasKit(player)) && ((e.getRightClicked() instanceof Player))) {
			Player t = (Player) e.getRightClicked();
			e.setCancelled(true);
			if (!BukkitMain.getInstance().getGladiatorFightController().isInFight(e.getPlayer())) {
				if (!BukkitMain.getInstance().getGladiatorFightController().isInFight(t)) {
					new GladiatorFight(e.getPlayer(), t, BukkitMain.getInstance());
				} else {
					e.getPlayer().sendMessage(getPrefix() + "§fEste jogador já está em §aBATALHA§f.");
				}
			} else {
				e.getPlayer().sendMessage(getPrefix() + "§fVocê já esta em §aBATALHA§f.");
			}
		}
	}

	@EventHandler
	public void onPlayerInteractListener(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		if ((e.getAction() != Action.PHYSICAL) && (hasKit(player)) && (e.getPlayer().getItemInHand() != null)
				&& (e.getPlayer().getItemInHand().getType() == Material.IRON_FENCE)) {
			e.getPlayer().updateInventory();
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent e) {
		if (BukkitMain.getInstance().getGladiatorFightController().isInFight(e.getPlayer())) {
			if (!e.getMessage().toLowerCase().startsWith("/report")) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onBlock(BlockDamageEvent event) {
		if (BukkitMain.getInstance().getGladiatorFightController().isFightBlock(event.getBlock())) {
			Block b = event.getBlock();
			if (b.getType() == Material.GLASS) {
				Player p = event.getPlayer();
				p.sendBlockChange(b.getLocation(), Material.BEDROCK, (byte) 0);
			}
		}
	}

	@EventHandler
	public void onExplode(EntityExplodeEvent event) {
		Iterator<Block> blockIt = event.blockList().iterator();
		while (blockIt.hasNext()) {
			Block b = (Block) blockIt.next();
			blockIt.remove();
		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if (BukkitMain.getInstance().getGladiatorFightController().isFightBlock(event.getBlock())) {
			if (event.getBlock().getType() == Material.GLASS) {
				event.setCancelled(true);
			}
		}
	}
}
