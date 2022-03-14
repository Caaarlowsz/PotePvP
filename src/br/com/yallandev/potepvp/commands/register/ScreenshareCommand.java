package br.com.yallandev.potepvp.commands.register;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.caaarlowsz.potemc.kitpvp.PotePvP;
import com.github.caaarlowsz.potemc.kitpvp.PotePvP.Configuration;
import br.com.yallandev.potepvp.account.Account;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.Command;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.CommandArgs;
import br.com.yallandev.potepvp.listener.PlayerListener;
import br.com.yallandev.potepvp.manager.CommandClass;
import br.com.yallandev.potepvp.permissions.group.Group;
import br.com.yallandev.potepvp.scoreboard.Scoreboarding;

public class ScreenshareCommand extends CommandClass {

	@Command(name = "screenshare", aliases = { "ss" }, groupToUse = Group.MODGC)
	public void onScreenshare(CommandArgs cmdArgs) {
		CommandSender s = cmdArgs.getSender();
		String[] a = cmdArgs.getArgs();
		Player p = cmdArgs.getPlayer();
		Account player = cmdArgs.getAccount();

		if (player == null)
			return;

		if (a.length == 0) {
			send(s, "Use �a/screenshare [Player]�f para mandar algu�m para o screenshare.");
			return;
		}

		Player t = Bukkit.getPlayer(a[0]);

		if (t == null) {
			send(s, "O jogador �a\"" + a[0] + "\"�f n�o est� online!");
			return;
		}

		UUID uuid = t.getUniqueId();
		Account target = PotePvP.getAccountCommon().getAccount(uuid);

		if (target == null) {
			send(s, "O jogador �a\"" + a[0] + "\"�f est� bugado!");
			t.kickPlayer(Configuration.KICK_PREFIX.getMessage()
					+ "\n�cAlgo de errado ocorreu em sua conta!\nRelogue para desbugar!");
			return;
		}

		if (main.getPlayerManager().isScreenshare(uuid)) {
			if (main.getPlayerManager().getScreenshareModerator(uuid).equals(player.getUuid())) {
				target.sendMessage("Voc� foi liberado do screenshare!");
				main.getPlayerManager().removeScreenshare(target.getUuid());
				send(s, "Voc� liberou o jogador �a" + target.getUserName() + "�f.");

				t.teleport(main.getWarpManager().getWarp("Spawn").getWarpLocation());
				main.getPlayerManager().setWarp(target.getUuid(), main.getWarpManager().getWarp("Spawn"));
				main.getPlayerManager().setProtection(target.getUuid(), true);
				main.getKitManager().removeAbility(target.getUuid());
				PlayerListener.setItem(target);

				p.teleport(main.getWarpManager().getWarp("Spawn").getWarpLocation());
				main.getPlayerManager().setWarp(player.getUuid(), main.getWarpManager().getWarp("Spawn"));
				main.getPlayerManager().setProtection(player.getUuid(), true);
				main.getKitManager().removeAbility(player.getUuid());
				PlayerListener.setItem(player);
			} else {
				player.sendMessage("Voc� n�o � o moderador que est� telando o jogador �a" + target.getUserName()
						+ "�f, e por isso n�o pode retirar ele da telagem.");
			}
			return;
		}

		target.sendMessage("Voc� foi enviado para a �aScreenShare�f pelo moderador �a" + player.getUserName() + "�f.");
		target.sendMessage("Siga as instru��es do moderador!");
		target.sendMessage("Se voc� deslogar ser� banido!");

		t.teleport(main.getWarpManager().getScreenshare().getWarpLocation());
		cmdArgs.getPlayer().teleport(main.getWarpManager().getScreenshare().getWarpLocation());

		Scoreboarding.setScoreboard(p);
		Scoreboarding.setScoreboard(t);

		main.getPlayerManager().getScreenshare().put(target.getUuid(), player.getUuid());
		broadcast("O jogador �a" + target.getUserName() + "�f foi puxado para o screenshare pelo �a"
				+ player.getUserName() + "�f.", Group.MODGC);
		send(s, "Voc� puxou o jogador �a\"" + target.getUserName() + "\"�f para screenshare.");
	}
}
