package br.com.yallandev.potepvp.commands.register;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.yallandev.potepvp.account.Account;
import br.com.yallandev.potepvp.ban.constructor.Mute;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.Command;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.CommandArgs;
import br.com.yallandev.potepvp.manager.CommandClass;

public class TellCommand extends CommandClass {

	public static ArrayList<Player> recieve = new ArrayList<Player>();

	@Command(name = "tell", aliases = { "msg", "w" })
	public void onTell(CommandArgs cmdArgs) {
		String[] a = cmdArgs.getArgs();
		Player p = cmdArgs.getPlayer();

		if (cmdArgs.isPlayer()) {
			Account player = cmdArgs.getAccount();

			Mute mute = player.getPunishmentHistory().getActualMute();

			if (mute != null) {
				p.sendMessage(mute.getMessage());
				return;
			}
		}

		if (a.length == 1) {
			if ((a[0].equalsIgnoreCase("on"))) {
				if (recieve.contains(p)) {
					send(p, "Voc� pode receber mensagem de tell!");
					recieve.remove(p);
					return;
				}
				send(p, "Voc� j� est� com o tell ativado!");
				return;
			}
			if ((a[0].equalsIgnoreCase("off"))) {
				if (!recieve.contains(p)) {
					send(p, "Voc� n�o pode receber mensagem de tell!");
					recieve.add(p);
					return;
				}
				send(p, "Voc� j� est� com o tell desativado!");
				return;
			}
			send(p, "Use �a/tell [on/off]�f para desativar ou ativar o tell!");
			send(p, "Use �a/tell [Player] [Mensagem]�f para mandar uma mensagem!");
			return;
		}
		if (a.length > 1) {
			CommandSender receptor = Bukkit.getServer().getPlayer(a[0]);
			if ((receptor == null) || (!(receptor instanceof Player))) {
				send(p, "O jogador �a" + a[0] + " �7nao foi encontrado!");
				return;
			}
			Player r = (Player) receptor;
			if (recieve.contains(r)) {
				send(p, "Voc� n�o pode mandar mensagem para esse jogador, porque ele est� com o tell desativado!");
				return;
			}
			String msg = "";
			for (int i = 1; i < a.length; i++) {
				msg = msg + a[i] + " ";
			}
			p.sendMessage("�7[Voc� -> " + r.getName() + "] " + msg);
			r.sendMessage("�7[" + p.getName() + " -> Voc�]" + msg);
			return;
		}
		send(p, "Use �a/tell [on/off]�f para desativar ou ativar o tell!");
		send(p, "Use �a/tell [Player] [Mensagem]�f para mandar uma mensagem!");
	}

}
