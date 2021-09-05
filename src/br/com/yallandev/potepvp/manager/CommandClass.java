package br.com.yallandev.potepvp.manager;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.yallandev.potepvp.BukkitMain;
import br.com.yallandev.potepvp.BukkitMain.Configuration;
import br.com.yallandev.potepvp.account.Account;
import br.com.yallandev.potepvp.permissions.group.Group;

public abstract class CommandClass {

	public BukkitMain main = BukkitMain.getInstance();

	public boolean isNumeric(String arg0) {
		try {
			Integer.valueOf(arg0);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void send(CommandSender sender, String message) {
		sender.sendMessage(Configuration.PREFIX.getMessage() + message);
	}

	public Group getGroup(CommandSender sender) {
		if (sender instanceof Player) {
			Account player = BukkitMain.getAccountCommon().getAccount(((Player) sender).getUniqueId());

			if (player == null)
				return Group.MEMBRO;

			return player.getServerGroup();
		} else {
			return Group.DEVELOPER;
		}
	}

	public Group getPosteriorGroup(CommandSender sender, int value) {
		if (sender instanceof Player) {
			Account player = BukkitMain.getAccountCommon().getAccount(((Player) sender).getUniqueId());

			if (player == null)
				return Group.MEMBRO;

			return Group.values()[player.getServerGroup().ordinal() + value] == null
					&& player.getServerGroup().ordinal() + value > Group.ADMIN.ordinal() ? Group.ADMIN
							: Group.values()[player.getServerGroup().ordinal() + value];
		} else {
			return Group.ADMIN;
		}
	}

	public boolean hasServerGroup(CommandSender sender, Group group) {
		return getGroup(sender).ordinal() >= group.ordinal();
	}

	public boolean isServerGroup(CommandSender sender, Group group) {
		return getGroup(sender).ordinal() == group.ordinal();
	}

	public AccountCommon getAccountCommon() {
		return BukkitMain.getAccountCommon();
	}

	public void broadcast(String message, Group group) {
		for (Player players : Bukkit.getOnlinePlayers()) {
			if (BukkitMain.getAccountCommon().hasGroup(players.getUniqueId(), group)) {
				players.sendMessage(Configuration.PREFIX.getMessage() + message);
			}
		}
	}

	public void broadcastD(String message, Group group) {
		for (Player players : Bukkit.getOnlinePlayers()) {
			if (BukkitMain.getAccountCommon().hasGroup(players.getUniqueId(), group)) {
				players.sendMessage(message);
			}
		}
	}

	public void broadcast(String message) {
		Bukkit.broadcastMessage(Configuration.PREFIX.getMessage() + message);
	}

}