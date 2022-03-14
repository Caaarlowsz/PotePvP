package br.com.yallandev.potepvp.event.account;

import org.bukkit.entity.Player;

import com.github.caaarlowsz.potemc.kitpvp.PotePvP;
import br.com.yallandev.potepvp.account.Account;
import br.com.yallandev.potepvp.event.PlayerCancellableEvent;
import br.com.yallandev.potepvp.manager.WarpManager.Warp;

public class PlayerDeathInWarpEvent extends PlayerCancellableEvent {

	private Warp warp;

	public PlayerDeathInWarpEvent(Player player, Warp warp) {
		super(player);
		this.warp = warp;
	}

	public Warp getWarp() {
		return warp;
	}

	public Account getAccount() {
		return PotePvP.getAccountCommon().getAccount(this.player.getUniqueId());
	}
}
