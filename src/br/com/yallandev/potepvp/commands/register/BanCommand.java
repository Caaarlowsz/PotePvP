package br.com.yallandev.potepvp.commands.register;

import java.util.UUID;

import org.bukkit.command.CommandSender;

import br.com.yallandev.potepvp.account.Account;
import br.com.yallandev.potepvp.ban.constructor.Ban;
import br.com.yallandev.potepvp.ban.constructor.Mute;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.Command;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.CommandArgs;
import br.com.yallandev.potepvp.manager.CommandClass;
import br.com.yallandev.potepvp.permissions.group.Group;
import br.com.yallandev.potepvp.utils.DateUtils;

public class BanCommand extends CommandClass {

	@Command(name = "ban", groupToUse = Group.TRIAL, onlyPlayers = true)
	public void onBan(CommandArgs cmdArgs) {
		CommandSender s = cmdArgs.getSender();
		String[] a = cmdArgs.getArgs();

		if (a.length == 0) {
			send(s, "Use �a/ban [Jogador] [Motivo]�f para banir permanentemente algu�m.");
			return;
		}

		if (getAccountCommon().getUUID(a[0]) == null) {
			send(s, "O jogador �c\"" + a[0] + "\" �fn�o existe!");
			return;
		}

		UUID uuid = getAccountCommon().getUUID(a[0]);

		if (uuid == null) {
			send(s, "O jogador �c\"" + a[0] + "\" �fn�o existe no nosso banco de dados!");
			return;
		}

		Account player = getAccountCommon().getAccount(uuid);

		if (player == null) {
			player = getAccountCommon().loadAccount(uuid, false);

			if (player == null) {
				send(s, "O jogador �c\"" + a[0] + "\" �fn�o existe!");
				return;
			}
		}

		if (player.getGroup().ordinal() > getGroup(s).ordinal()) {
			send(s, "Voc� n�o pode banir alguem que tem um cargo maior que o seu!");
			return;
		}

		StringBuilder sb = new StringBuilder();

		if (a.length == 1) {
			sb.append("Sem motivo");
		} else {
			for (int i = 1; i < a.length; i++) {
				if (i >= a.length - 1) {
					sb.append(a[i]);
				} else {
					sb.append(a[i] + " ");
				}
			}
		}

		String reason = sb.toString().trim();

		if (player.getPunishmentHistory().getActualBan() != null
				&& player.getPunishmentHistory().getActualBan().isPermanent()) {
			send(s, "O jogador �a\"" + player.getUserName() + "\" �fj� est� banido!");
			return;
		}

		Ban ban = new Ban((cmdArgs.isPlayer() ? cmdArgs.getAccount().getUserName() : s.getName()), reason);

		send(s, "Voc� baniu o jogador �a\"" + player.getUserName() + "\" �f pelo motivo �a\"" + reason + "\"�f.");
		main.getBanManager().ban(player, ban);
	}

	@Command(name = "unban", aliases = { "pardon" }, groupToUse = Group.ADMIN)
	public void onUnban(CommandArgs cmdArgs) {
		CommandSender s = cmdArgs.getSender();
		String[] a = cmdArgs.getArgs();

		if (a.length == 0) {
			send(s, "Use �a/unban [Jogador]�f para desbanir um jogador!");
			return;
		}

		if (getAccountCommon().getUUID(a[0]) == null) {
			send(s, "O jogador �c\"" + a[0] + "\" �fn�o existe!");
			return;
		}

		Account player = getAccountCommon().getAccount(getAccountCommon().getUUID(a[0]));

		if (player == null) {
			player = getAccountCommon().loadAccount(getAccountCommon().getUUID(a[0]), false);

			if (player == null) {
				send(s, "O jogador �c\"" + a[0] + "\" �fn�o existe!");
				return;
			}
		}

		if (player.getPunishmentHistory().getActualBan() == null) {
			send(s, "O jogador �c\"" + a[0] + "\" �fn�o est� banido!");
			return;
		}

		send(s, "Voc� desbaniu o jogador �a\"" + a[0] + "\" �f!");
		main.getBanManager().unban(player, s.getName());
	}

	@Command(name = "tempban", groupToUse = Group.TRIAL)
	public void onTempban(CommandArgs cmdArgs) {
		CommandSender s = cmdArgs.getSender();
		String[] a = cmdArgs.getArgs();

		if (a.length == 0) {
			send(s, "Use �a/tempban [Jogador] [Tempo] [Motivo]�f para banir temporariamente um jogaor!");
			return;
		}

		if (a.length == 1) {
			send(s, "Use �a/tempban [Jogador] [Tempo] [Motivo]�f para banir temporariamente um jogaor!");
			return;
		}

		if (getAccountCommon().getUUID(a[0]) == null) {
			send(s, "O jogador �c\"" + a[0] + "\" �fn�o existe!");
			return;
		}

		Account player = getAccountCommon().getAccount(getAccountCommon().getUUID(a[0]));

		if (player == null) {
			player = getAccountCommon().loadAccount(getAccountCommon().getUUID(a[0]), false);

			if (player == null) {
				send(s, "O jogador �c\"" + a[0] + "\" �fn�o existe!");
				return;
			}
		}

		StringBuilder sb = new StringBuilder();

		if (a.length == 2) {
			sb.append("Sem motivo");
		} else {
			for (int i = 2; i < a.length; i++) {
				if (i >= a.length - 1) {
					sb.append(a[i]);
				} else {
					sb.append(a[i] + " ");
				}
			}
		}

		String reason = sb.toString().trim();
		Long expire = null;

		try {
			expire = DateUtils.getTime(a[1]);
		} catch (Exception e) {
			send(s, "Use um formato de tempo valido (Exemplo: 30s, 1m, 2h, 5d, 2mo, 1y)");
			return;
		}

		if (player.getPunishmentHistory().getActualBan() != null
				&& player.getPunishmentHistory().getActualBan().isPermanent()) {
			send(s, "O jogador �a\"" + player.getUserName() + "\" �fj� est� banido!");
			return;
		}

		Ban ban = new Ban((cmdArgs.isPlayer() ? cmdArgs.getAccount().getUserName() : s.getName()), reason, expire);

		send(s, "Voc� baniu o jogador �a\"" + player.getUserName() + "\" �f pelo motivo �a\"" + reason
				+ "\"�f pelo tempo �a�n" + DateUtils.getTime(expire) + "�f.");
		main.getBanManager().ban(player, ban);
	}

	@Command(name = "mute", groupToUse = Group.HELPER)
	public void onMute(CommandArgs cmdArgs) {
		CommandSender s = cmdArgs.getSender();
		String[] a = cmdArgs.getArgs();

		if (a.length == 0) {
			send(s, "Use �a/mute [Jogador] [Motivo]�f para mutar permanentemente um jogaor!");
			return;
		}

		if (getAccountCommon().getUUID(a[0]) == null) {
			send(s, "O jogador �c\"" + a[0] + "\" �fn�o existe!");
			return;
		}

		Account player = getAccountCommon().getAccount(getAccountCommon().getUUID(a[0]));

		if (player == null) {
			player = getAccountCommon().loadAccount(getAccountCommon().getUUID(a[0]), false);

			if (player == null) {
				send(s, "O jogador �c\"" + a[0] + "\" �fn�o existe!");
				return;
			}
		}

		StringBuilder sb = new StringBuilder();

		if (a.length == 1) {
			sb.append("Sem motivo");
		} else {
			for (int i = 1; i < a.length; i++) {
				if (i >= a.length - 1) {
					sb.append(a[i]);
				} else {
					sb.append(a[i] + " ");
				}
			}
		}

		String reason = sb.toString().trim();

		if (player.getPunishmentHistory().getActualMute() != null
				&& player.getPunishmentHistory().getActualMute().isPermanent()) {
			send(s, "O jogador �c\"" + a[0] + "\" �fj� est� mutado!");
			return;
		}

		Mute mute = new Mute((cmdArgs.isPlayer() ? cmdArgs.getAccount().getUserName() : s.getName()), reason);

		send(s, "Voc� mutou o jogador �a\"" + player.getUserName() + "\" �f pelo motivo �a\"" + reason + "\"�f.");
		main.getBanManager().mute(player, mute);
	}

	@Command(name = "unmute", groupToUse = Group.MODPLUS)
	public void onUnmute(CommandArgs cmdArgs) {
		CommandSender s = cmdArgs.getSender();
		String[] a = cmdArgs.getArgs();

		if (a.length == 0) {
			send(s, "Use �a/unmute [Jogador]�f para desmutar um jogador!");
			return;
		}

		if (getAccountCommon().getUUID(a[0]) == null) {
			send(s, "O jogador �c\"" + a[0] + "\" �fn�o existe!");
			return;
		}

		Account player = getAccountCommon().getAccount(getAccountCommon().getUUID(a[0]));

		if (player == null) {
			player = getAccountCommon().loadAccount(getAccountCommon().getUUID(a[0]), false);

			if (player == null) {
				send(s, "O jogador �c\"" + a[0] + "\" �fn�o existe!");
				return;
			}
		}

		if (player.getPunishmentHistory().getActualMute() == null) {
			send(s, "O jogador �c\"" + a[0] + "\" �fn�o est� mutado!");
			return;
		}

		send(s, "Voc� desmutou o jogador �a\"" + a[0] + "\" �f!");
		main.getBanManager().unmute(player, s.getName());
	}

	@Command(name = "tempmute", groupToUse = Group.HELPER)
	public void onTempmute(CommandArgs cmdArgs) {
		CommandSender s = cmdArgs.getSender();
		String[] a = cmdArgs.getArgs();

		if (a.length <= 1) {
			send(s, "Use �a/tempmute [Jogador] [Tempo] [Motivo]�f para mutar temporariamente um jogaor!");
			return;
		}

		if (getAccountCommon().getUUID(a[0]) == null) {
			send(s, "O jogador �c\"" + a[0] + "\" �fn�o existe!");
			return;
		}

		Account player = getAccountCommon().getAccount(getAccountCommon().getUUID(a[0]));

		if (player == null) {
			player = getAccountCommon().loadAccount(getAccountCommon().getUUID(a[0]), false);

			if (player == null) {
				send(s, "O jogador �c\"" + a[0] + "\" �fn�o existe!");
				return;
			}
		}

		StringBuilder sb = new StringBuilder();

		if (a.length == 2) {
			sb.append("Sem motivo");
		} else {
			for (int i = 2; i < a.length; i++) {
				if (i >= a.length - 1) {
					sb.append(a[i]);
				} else {
					sb.append(a[i] + " ");
				}
			}
		}

		String reason = sb.toString().trim();
		Long expire = null;

		try {
			expire = DateUtils.getTime(a[1]);
		} catch (Exception e) {
			send(s, "Use um formato de tempo valido (Exemplo: 30s, 1m, 2h, 5d, 2mo, 1y)");
			return;
		}

		if (player.getPunishmentHistory().getActualMute() != null
				&& player.getPunishmentHistory().getActualMute().isPermanent()) {
			send(s, "O jogador �a\"" + player.getUserName() + "\" �fj� est� banido!");
			return;
		}

		Mute ban = new Mute((cmdArgs.isPlayer() ? cmdArgs.getAccount().getUserName() : s.getName()), reason, expire);

		send(s, "Voc� mutou o jogador �a\"" + player.getUserName() + "\" �f pelo motivo �a\"" + reason
				+ "\"�f pelo tempo �a�n" + DateUtils.getTime(expire) + "�f.");
		main.getBanManager().mute(player, ban);
	}

	@Command(name = "kick", groupToUse = Group.HELPER)
	public void onKick(CommandArgs cmdArgs) {
		CommandSender s = cmdArgs.getSender();
		String[] a = cmdArgs.getArgs();

		if (a.length == 0) {
			send(s, "Use �a/kick [Jogador] [Motivo]�f para kickar algu�m.");
			return;
		}

		if (getAccountCommon().getUUID(a[0]) == null) {
			send(s, "O jogador �c\"" + a[0] + "\" �fn�o existe!");
			return;
		}

		Account player = getAccountCommon().getAccount(getAccountCommon().getUUID(a[0]));

		if (player == null) {
			player = getAccountCommon().loadAccount(getAccountCommon().getUUID(a[0]), false);

			if (player == null) {
				send(s, "O jogador �c\"" + a[0] + "\" �fn�o existe!");
				return;
			}
		}

		if (!player.isOnline()) {
			send(s, "O jogador �a\"" + player.getUserName() + "\" �fn�o est� online!");
			return;
		}

		StringBuilder sb = new StringBuilder();

		if (a.length == 1) {
			sb.append("Sem motivo");
		} else {
			for (int i = 1; i < a.length; i++) {
				if (i >= a.length - 1) {
					sb.append(a[i]);
				} else {
					sb.append(a[i] + " ");
				}
			}
		}

		String reason = sb.toString().trim();

		player.getPlayer().kickPlayer("�cSua conta foi kickada do servidor!\nMotivo: " + reason);
		send(s, "Voc� kickou o jogador �a\"" + player.getUserName() + "\" �f pelo motivo �a\"" + reason + "\"�f.");
	}

}
