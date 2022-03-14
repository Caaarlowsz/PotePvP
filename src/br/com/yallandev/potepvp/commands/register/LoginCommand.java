package br.com.yallandev.potepvp.commands.register;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.caaarlowsz.potemc.kitpvp.PotePvP;
import br.com.yallandev.potepvp.check.Check;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.Command;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.CommandArgs;
import br.com.yallandev.potepvp.connection.SQLManager.Status;
import br.com.yallandev.potepvp.exception.InvalidCheckException;
import br.com.yallandev.potepvp.manager.CommandClass;
import br.com.yallandev.potepvp.utils.Util;

public class LoginCommand extends CommandClass {

	@Command(name = "login", onlyPlayers = true)
	public void onLogin(CommandArgs cmdArgs) {
		Player p = cmdArgs.getPlayer();
		CommandSender s = cmdArgs.getSender();
		String[] a = cmdArgs.getArgs();

		try {
			if (Check.fastCheck(p.getName())) {
				s.sendMessage("Voc� � um jogador premium.");
				return;
			}
		} catch (InvalidCheckException e) {
			e.printStackTrace();
		}

		if (!PotePvP.getSqlManager().hasOnDatabase(s.getName())) {
			s.sendMessage("Voc� n�o est� registrado!");
			return;
		}

		if (a.length == 0) {
			send(s, "Use �a/login [Senha]�f para se logar!");
		} else {
			if (Util.decode(PotePvP.getSqlManager().getPassword(s.getName())).equals(a[0])) {
				while (PotePvP.getStorage().needLogin(s.getName()))
					PotePvP.getStorage().removeNeedLogin(s.getName());

				send(s, "Logado com sucesso!");
			} else {
				send(s, "Senha incorreta!");
			}
		}
	}

	@Command(name = "register", onlyPlayers = true)
	public void onRegister(CommandArgs cmdArgs) {
		Player p = cmdArgs.getPlayer();
		CommandSender s = cmdArgs.getSender();
		String[] a = cmdArgs.getArgs();

		try {
			if (Check.fastCheck(p.getName())) {
				s.sendMessage("Voc� � um jogador premium.");
				return;
			}
		} catch (InvalidCheckException e) {
			e.printStackTrace();
		}

		if (PotePvP.getSqlManager().hasOnDatabase(s.getName())) {
			s.sendMessage("Voc� j� est� registrado!");
			return;
		}

		if (a.length == 0) {
			send(s, "Use �a/register [Senha] [Senha]�f para se registrar!");
		} else if (a.length == 1) {
			send(s, "Use �a/register [Senha] [Senha]�f para se registrar!");
		} else {
			if (!a[0].equals(a[1])) {
				send(s, "As senhas n�o colidem!");
				return;
			}

			while (PotePvP.getStorage().needLogin(s.getName()))
				PotePvP.getStorage().removeNeedLogin(s.getName());

			PotePvP.getSqlManager().setPasswordAndStatus(s.getName(), Status.CRACKED, new String(Util.encode(a[0])));
			send(s, "Voc� se registrou com sucesso!");
		}
	}
}
