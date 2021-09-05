package br.com.yallandev.potepvp.manager;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import br.com.yallandev.potepvp.BukkitMain;
import br.com.yallandev.potepvp.utils.Util;

public class PlayerHideManager {

	private BukkitMain main;
	private ArrayList<UUID> hideAllPlayers;

	public PlayerHideManager() {
		this.hideAllPlayers = new ArrayList<UUID>();
		this.main = BukkitMain.getInstance();
	}

	public void playerJoin(Player p) {
		for (UUID id : this.hideAllPlayers) {
			Player hide = Bukkit.getPlayer(id);
			if (hide != null) {
				hide.hidePlayer(p);
			}
		}
	}

	public void hideAllPlayers(Player p) {
		this.hideAllPlayers.add(p.getUniqueId());
		for (Player hide : Util.getOnlinePlayers()) {
			if (hide.getUniqueId() != p.getUniqueId()) {
				p.hidePlayer(hide);
			}
		}
	}

	public void showAllPlayers(Player p) {
		if (this.hideAllPlayers.contains(p.getUniqueId())) {
			this.hideAllPlayers.remove(p.getUniqueId());
		}
		for (Player show : Util.getOnlinePlayers()) {
			if ((!this.hideAllPlayers.contains(show.getUniqueId()))
					&& (!this.main.getVanishMode().isAdmin(show.getUniqueId()))
					&& (show.getUniqueId() != p.getUniqueId())) {
				p.showPlayer(show);
			}
		}
	}

	public void showForAll(Player p) {
	}

	public void hideForAll(Player p) {
	}

	public boolean isHiding(UUID id) {
		return this.hideAllPlayers.contains(id);
	}

	public boolean hideForAll(UUID id) {
		return false;
	}

	public void stop() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			for (Player show : Util.getOnlinePlayers()) {
				p.showPlayer(show);
			}
		}
		this.hideAllPlayers.clear();
	}

	public void remove(UUID id) {
		this.hideAllPlayers.remove(id);
	}
}
