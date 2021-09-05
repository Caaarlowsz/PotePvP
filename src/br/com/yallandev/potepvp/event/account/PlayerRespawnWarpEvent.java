package br.com.yallandev.potepvp.event.account;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import br.com.yallandev.potepvp.BukkitMain;
import br.com.yallandev.potepvp.account.Account;
import br.com.yallandev.potepvp.event.PlayerCancellableEvent;
import br.com.yallandev.potepvp.manager.WarpManager.Warp;

public class PlayerRespawnWarpEvent extends PlayerCancellableEvent {

	private Warp warp;
	private Location respawnLocation;

	public PlayerRespawnWarpEvent(Player player, Warp warp) {
		super(player);
		this.warp = warp;
		this.respawnLocation = warp.getWarpLocation();
	}

	public Location getRespawnLocation() {
		return respawnLocation;
	}

	public void setRespawnLocation(Location respawnLocation) {
		this.respawnLocation = respawnLocation;
	}

	public Warp getWarp() {
		return warp;
	}

	public Account getAccount() {
		return BukkitMain.getAccountCommon().getAccount(this.player.getUniqueId());
	}

}
