package br.com.yallandev.potepvp.event.account;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RealMoveEvent extends Event {
	
	public static HandlerList handlers = new HandlerList();
	private UUID playerUUID;
	private Location from;
	private Location to;

	public RealMoveEvent(Player player, Location from, Location to) {
		this.playerUUID = player.getUniqueId();
		this.from = from;
		this.to = to;
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(this.playerUUID);
	}

	public UUID getPlayerUUID() {
		return this.playerUUID;
	}

	public Location getFrom() {
		return this.from;
	}

	public Location getTo() {
		return this.to;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
