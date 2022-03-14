package br.com.yallandev.potepvp.commands.register;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.caaarlowsz.potemc.kitpvp.PotePvP;
import br.com.yallandev.potepvp.account.Account;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.Command;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.CommandArgs;
import br.com.yallandev.potepvp.manager.CommandClass;
import br.com.yallandev.potepvp.permissions.group.Group;

public class SkitCommand extends CommandClass {

	@Command(name = "skit", aliases = { "simplekit" }, groupToUse = Group.MOD)
	public void onSimplekit(CommandArgs cmdArgs) {
		CommandSender s = cmdArgs.getSender();
		String[] a = cmdArgs.getArgs();
		Player p = (Player) s;

		Account player = PotePvP.getAccountCommon().getAccount(p.getUniqueId());

		if (player == null)
			return;

		if (a.length == 0) {
			send(p, "Use �a/skit [criar/create] [Name]�f para criar um skit.");
			send(p, "Use �a/skit update [Name]�f para criar um skit.");
			send(p, "Use �a/skit [aplicar/apply] [Name] [Raio/All/Jogador/Warp]�f para aplicar um skit em um raio determinado, em todos do servidor ou em um player s�.");
			send(p, "Use �a/skit list�f para ver a lista de skits.");
		} else if (a.length == 1) {
			if (a[0].equalsIgnoreCase("list")) {
				List<String> list = new ArrayList<>();

				for (String l : PotePvP.getInstance().getSimpleKitManager().values()) {
					list.add(l);
				}

				if (list.size() == 0) {
					send(p, "N�o h� nenhum skit.");
				} else {
					String bgl = "";
					for (int j = 0; j < list.size(); j++) {
						if (j == (list.size() - 1)) {
							bgl += list.get(j) + ".";
						} else {
							bgl += list.get(j) + ", ";
						}
					}

					send(p, "Skit's disponiveis: " + bgl);
				}
			} else {
				send(p, "Use �a/skit [criar/create] [Name]�f para criar um skit.");
				send(p, "Use �a/skit [aplicar/apply] [Name] [Raio/All/Jogador/Warp]�f para aplicar um skit em um raio determinado, em todos do servidor ou em um player s�.");
				send(p, "Use �a/skit list�f para ver a lista de skits.");
			}
		} else if (a.length == 2) {
			if (a[0].equalsIgnoreCase("create") || a[0].equalsIgnoreCase("criar")) {
				PotePvP.getInstance().getSimpleKitManager().createSkit(p, a[1]);
			} else if (a[0].equalsIgnoreCase("update")) {
				PotePvP.getInstance().getSimpleKitManager().updateSkit(p, a[1]);
			} else {
				send(p, "Use �a/skit [criar/create] [Name]�f para criar um skit.");
				send(p, "Use �a/skit [aplicar/apply] [Name] [Raio/All/Jogador/Warp]�f para aplicar um skit em um raio determinado, em todos do servidor ou em um player s�.");
				send(p, "Use �a/skit list�f para ver a lista de skits.");
			}
		} else {
			if (a[0].equalsIgnoreCase("aplicar") || a[0].equalsIgnoreCase("apply")) {
				if (a[2].equalsIgnoreCase("all")) {
					PotePvP.getInstance().getSimpleKitManager().applyInAll(p, a[1]);
				} else if (PotePvP.getInstance().getWarpManager().getWarp(a[2]) != null) {
				} else if (Bukkit.getPlayer(a[2]) != null) {
					PotePvP.getInstance().getSimpleKitManager().applyInPlayer(p, Bukkit.getPlayer(a[2]), a[1]);
				} else {
					Integer raio = null;

					try {
						raio = Integer.valueOf(a[2]);
					} catch (Exception e) {
						send(p, "Use um formato de raio valido (Exemplo: 1999, 200, 500).");
						return;
					}

					PotePvP.getInstance().getSimpleKitManager().applySkit(p, raio, a[1]);
				}
			}
		}
	}

}
