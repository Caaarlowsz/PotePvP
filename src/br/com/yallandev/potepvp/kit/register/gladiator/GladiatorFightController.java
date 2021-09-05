package br.com.yallandev.potepvp.kit.register.gladiator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class GladiatorFightController {

	private HashMap<UUID, String> playersInFight;
	private List<Block> fightsBlocks;

	public GladiatorFightController() {
		this.playersInFight = new HashMap<>();
		this.fightsBlocks = new ArrayList<>();
	}

	public void stop() {
		for (Block b : this.fightsBlocks) {
			b.setType(Material.AIR);
		}
	}

	public boolean isInFight(Player p) {
		return this.playersInFight.containsKey(p.getUniqueId());
	}

	public void removePlayerFromFight(UUID id) {
		this.playersInFight.remove(id);
	}

	public void addPlayerToFights(UUID id, String type) {
		this.playersInFight.put(id, type);
	}

	public String getType(UUID id) {
		return this.playersInFight.get(id);
	}

	public void removeBlock(Block b) {
		this.fightsBlocks.remove(b);
	}

	public void addBlock(Block b) {
		this.fightsBlocks.add(b);
	}

	public boolean isFightBlock(Block b) {
		return this.fightsBlocks.contains(b);
	}
}
