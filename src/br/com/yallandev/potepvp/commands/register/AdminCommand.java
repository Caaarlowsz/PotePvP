package br.com.yallandev.potepvp.commands.register;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import br.com.yallandev.potepvp.BukkitMain;
import br.com.yallandev.potepvp.BukkitMain.Configuration;
import br.com.yallandev.potepvp.account.Account;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.Command;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.CommandArgs;
import br.com.yallandev.potepvp.manager.CommandClass;
import br.com.yallandev.potepvp.permissions.group.Group;
import br.com.yallandev.potepvp.scoreboard.Scoreboarding;
import br.com.yallandev.potepvp.utils.DateUtils;
import br.com.yallandev.potepvp.utils.string.CenterChat;

public class AdminCommand extends CommandClass {

	private static HashMap<String, Long> reportCooldown = new HashMap<>();

	@Command(name = "aplicar")
	public void onAplicar(CommandArgs cmdArgs) {
		cmdArgs.getSender().sendMessage("");
		cmdArgs.getSender().sendMessage(
				CenterChat.centered("�7Formulario de �d�lTRIAL �8- �7" + Configuration.TRIAL.getMessage()));
		cmdArgs.getSender().sendMessage(
				CenterChat.centered("�7Formulario de �5�lMODGC �8- �7" + Configuration.MODGC.getMessage()));
		cmdArgs.getSender().sendMessage(
				CenterChat.centered("�7Formulario de �e�lHELPER �8- �7" + Configuration.HELPER.getMessage()));
		cmdArgs.getSender().sendMessage("");
	}

	@Command(name = "report", onlyPlayers = true)
	public void onReport(CommandArgs cmdArgs) {
		Player p = (Player) cmdArgs.getPlayer();
		String[] a = cmdArgs.getArgs();

		Account player = cmdArgs.getAccount();

		if (player == null)
			return;

		if (reportCooldown.containsKey(player.getUserName())) {
			if (reportCooldown.get(player.getUserName()) > System.currentTimeMillis()) {
				player.sendMessage("Voc� precisa esperar mais �a"
						+ DateUtils.getTime(reportCooldown.get(player.getUserName())) + "�f reportar novamente.");
				return;
			}
		}

		reportCooldown.put(player.getUserName(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(30));

		if (a.length < 2) {
			send(p, "Use �a/report [Nick] [Motivo]�f para reportar alguem.");
			return;
		}

		if (Bukkit.getPlayer(a[0]) == null) {
			send(p, "Voc� n�o pode reportar alguem que n�o esteja online!");
			return;
		}

		Account target = getAccountCommon().getAccount(Bukkit.getPlayer(a[0]).getUniqueId());

		if (target == null) {
			send(p, "Ocorreu um erro ao reportar esse jogador!");
			return;
		}

		String reason = "";

		for (int i = 1; i < a.length; i++) {
			reason = reason + a[i] + " ";
		}

		broadcastD("�c�l�m----�4�l REPORT �c�l�m----", Group.TRIAL);
		broadcastD("�fSuspeito: �a" + target.getUserName(), Group.TRIAL);
		broadcastD("�fPessoa que reportou: �a" + p.getName(), Group.TRIAL);
		broadcastD("�fMotivo: �c" + reason, Group.TRIAL);
		broadcastD("�c�l�m----�4�l REPORT �c�l�m----", Group.TRIAL);
	}

	@Command(name = "staffchat", aliases = { "sc" }, groupToUse = Group.BUILDER, onlyPlayers = true)
	public void onStaffchat(CommandArgs cmdArgs) {
		Player p = (Player) cmdArgs.getPlayer();

		Account player = cmdArgs.getAccount();

		if (player == null)
			return;

		boolean staffchat = !main.getPlayerManager().getStaffchat().contains(player.getUuid());

		send(p, "Voc� " + (staffchat ? "�a�lATIVOU" : "�4�lDESATIVOU") + "�f o staffchat.");

		if (staffchat)
			main.getPlayerManager().getStaffchat().add(player.getUuid());
		else
			main.getPlayerManager().getStaffchat().remove(player.getUuid());
	}

	@Command(name = "admin", groupToUse = Group.TRIAL, onlyPlayers = true)
	public void onAdmin(CommandArgs cmdArgs) {
		Player p = (Player) cmdArgs.getPlayer();

		if (BukkitMain.getInstance().getVanishMode().isAdmin(p.getUniqueId()))
			BukkitMain.getInstance().getVanishMode().setPlayer(p);
		else
			BukkitMain.getInstance().getVanishMode().setAdmin(p);

		Scoreboarding.setScoreboard(p);
	}
}
