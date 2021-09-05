package br.com.yallandev.potepvp.clan.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import br.com.yallandev.potepvp.BukkitMain;
import br.com.yallandev.potepvp.account.Account;

public class ClanListener implements Listener {
	
	private BukkitMain main;
	
	public ClanListener() {
		this.main = BukkitMain.getInstance();
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Account player = BukkitMain.getAccountCommon().getAccount(e.getPlayer().getUniqueId());
		
		if (player == null)
			return;
		
		if (!player.hasClan())
			return;
		
		if (main.getClanCommon().getClan(player.getClan().getClanName()) == null) {
			if (main.getClanCommon().loadClan(player.getClan().getClanName()) == null) {
				player.setClan(null);
				player.sendMessage("O seu clã foi deletado!");
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
