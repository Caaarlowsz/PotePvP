package br.com.yallandev.potepvp.clan.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.github.caaarlowsz.potemc.kitpvp.PotePvP;
import br.com.yallandev.potepvp.account.Account;

public class ClanListener implements Listener {

	private PotePvP main;

	public ClanListener() {
		this.main = PotePvP.getInstance();
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Account player = PotePvP.getAccountCommon().getAccount(e.getPlayer().getUniqueId());

		if (player == null)
			return;

		if (!player.hasClan())
			return;

		if (main.getClanCommon().getClan(player.getClan().getClanName()) == null) {
			if (main.getClanCommon().loadClan(player.getClan().getClanName()) == null) {
				player.setClan(null);
				player.sendMessage("O seu cl� foi deletado!");
				System.out.println("Clan do jogador " + player.getUserName() + " nao existe mais!");
			} else {
				player.setClan(main.getClanCommon().getClan(player.getClan().getClanName()));
				System.out.println("Clan do jogador " + player.getUserName() + " carregado!");
			}
		} else {
			System.out.println("O player " + player.getUserName() + " nao possui clan.");
		}
	}

}
