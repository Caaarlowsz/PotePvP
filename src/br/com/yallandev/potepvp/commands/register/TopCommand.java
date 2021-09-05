package br.com.yallandev.potepvp.commands.register;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import br.com.yallandev.potepvp.BukkitMain;
import br.com.yallandev.potepvp.account.Account;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.Command;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.CommandArgs;
import br.com.yallandev.potepvp.manager.CommandClass;

public class TopCommand extends CommandClass {

	@Command(name = "top", onlyPlayers = true)
	public void onTop(CommandArgs cmdArgs) {
		Player p = cmdArgs.getPlayer();
		String[] a = cmdArgs.getArgs();

		if (a.length < 2) {
			send(p, "Use �a/top [Kills/Deaths/KillStreak] [Numero]�f para ordernar um top.");
			return;
		}

		if (!a[0].equalsIgnoreCase("kills") && !a[0].equalsIgnoreCase("deaths")
				&& !a[0].equalsIgnoreCase("killstreak")) {
			send(p, "Use �a/top [Kills/Deaths/KillStreak] [Numero]�f para ordernar um top.");
			return;
		}

		if (!isNumeric(a[1])) {
			send(p, "Use um numero como ordenador!");
			return;
		}

		String type = a[0];
		Integer order = Integer.valueOf(a[1]);

		if (order >= 200) {
			send(p, "O numero n�o pode ser maior que 200!");
			return;
		}

		if (order <= 0) {
			send(p, "O numero n�o pode ser menor do que 1!");
			return;
		}

		for (String toppers : getTop(type, order)) {
			p.sendMessage(toppers);
		}
	}

	public List<String> getTop(String type, int max) {
		List<String> tops = new ArrayList<String>();

		try {
			PreparedStatement stm = BukkitMain.getConnection().getConnection()
					.prepareStatement("SELECT * FROM `kitpvp_status` ORDER BY `" + type + "` DESC LIMIT 0," + max + "");
			ResultSet rs = stm.executeQuery();
			int i = 0;
			while (rs.next()) {
				UUID uuid = UUID.fromString(rs.getString("Uuid"));

				if (uuid == null)
					continue;

				Account player = getAccountCommon().getAccount(uuid);

				if (player == null) {
					player = getAccountCommon().loadAccount(uuid, false);

					if (player == null) {
						continue;
					}
				}

				i++;
				tops.add("�a" + i + "� �6" + player.getUserName() + " �8|�a " + rs.getInt(type) + " "
						+ type.substring(0, 1).toUpperCase() + type.substring(1, type.length()).toLowerCase());
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
		return tops;
	}

}
