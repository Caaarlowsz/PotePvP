package br.com.yallandev.potepvp.event.account;

import org.bukkit.entity.Player;

import br.com.yallandev.potepvp.BukkitMain;
import br.com.yallandev.potepvp.account.Account;
import br.com.yallandev.potepvp.event.PlayerCancellableEvent;
import br.com.yallandev.potepvp.manager.WarpManager.Warp;

public class PlayerJoinWarpEvent extends PlayerCancellableEvent {

	private Warp oldWarp;
	private Warp newWarp;
	private boolean isForced;

	public PlayerJoinWarpEvent(Player player, Warp oldWarp, Warp newWarp, boolean isForced) {
		super(player);
		this.oldWarp = oldWarp;
		this.newWarp = newWarp;
		this.isForced = isForced;
	}

	public boolean isForced() {
		return isForced;
	}

	public void setForced(boolean isForced) {
		this.isForced = isForced;
	}

	public void setNewWarp(Warp newWarp) {
		this.newWarp = newWarp;
	}

	public void setOldWarp(Warp oldWarp) {
		this.oldWarp = oldWarp;
	}

	public Warp getNewWarp() {
		return newWarp;
	}

	public Warp getOldWarp() {
		return oldWarp;
	}

	public Account getAccount() {
		return BukkitMain.getAccountCommon().getAccount(this.player.getUniqueId());
	}
}
