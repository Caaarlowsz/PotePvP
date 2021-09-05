package br.com.yallandev.potepvp.commands.register;

import java.util.UUID;

import org.bukkit.command.CommandSender;

import br.com.yallandev.potepvp.BukkitMain;
import br.com.yallandev.potepvp.account.Account;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.Command;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.CommandArgs;
import br.com.yallandev.potepvp.manager.CommandClass;
import br.com.yallandev.potepvp.permissions.group.Group;
import br.com.yallandev.potepvp.permissions.group.PaymentGroup;
import br.com.yallandev.potepvp.tag.Tag;
import br.com.yallandev.potepvp.utils.DateUtils;

public class GroupCommand extends CommandClass {

	@Command(name = "groupset", aliases = { "groupset" }, groupToUse = Group.ADMIN)
	public void onGroupset(CommandArgs cmdArgs) {
		final CommandSender sender = cmdArgs.getSender();
		final String[] a = cmdArgs.getArgs();

		if (a.length != 2) {
			send(sender, "Use �a/groupset [Player] [Group]�f para setar o grupo de algum jogador!");
			return;
		}

		Group group = null;

		try {
			group = Group.valueOf(a[1].toUpperCase());
		} catch (Exception e) {
			send(sender, "Use um grupo valido!");

			if (hasServerGroup(sender, Group.DONO)) {
				String list = "Grupos disponiveis: ";

				for (int x = 0; x < Group.DEVELOPER.ordinal(); x++) {
					list += Group.values()[x].name() + (x == Group.DEVELOPER.ordinal() - 1 ? "." : ", ");
				}

				send(sender, list);
			} else if (hasServerGroup(sender, Group.GERENTE)) {
				String list = "Grupos disponiveis: ";

				for (int x = 0; x < (Group.GERENTE.ordinal() - 1); x++) {
					list += Group.values()[x].name() + (x == Group.GERENTE.ordinal() ? "." : ", ");
				}

				send(sender, list);
			}
			return;
		}

		if (group != Group.MEMBRO && group.ordinal() <= Group.HEIGHT.ordinal()) {
			send(sender, "Use �a/tempgroup�f para setar um grupo de pagantes.");
			return;
		}

		UUID uuid = getAccountCommon().getUUID(a[0]);

		if (uuid == null) {
			send(sender, "O jogador �c\"" + a[0] + "\" �fn�o existe no nosso banco de dados!");
			return;
		}

		Account player = getAccountCommon().getAccount(uuid);

		if (player == null) {
			player = getAccountCommon().loadAccount(uuid, false);

			if (player == null) {
				send(sender, "O jogador �c\"" + a[0] + "\" �fn�o existe!");
				return;
			}
		}

		if (player.getServerGroup().ordinal() >= Group.ADMIN.ordinal()) {
			if (!hasServerGroup(sender, Group.DONO)) {
				send(sender, "Voc� n�o pode manejar o grupo desse jogador!");
				return;
			}
		}

		if (player.getServerGroup().ordinal() >= Group.ADMIN.ordinal()) {
			if (!hasServerGroup(sender, Group.ADMIN)) {
				send(sender, "Voc� n�o pode manejar o grupo desse jogador!");
				return;
			}
		}

		if (group.ordinal() >= Group.GERENTE.ordinal()) {
			if (!hasServerGroup(sender, Group.DONO)) {
				send(sender, "Voc� n�o pode manejar esse grupo!");
				return;
			}
		}

		Group actualGroup = player.getGroup();

		if (actualGroup == group) {
			send(sender, "O jogador �a\"" + player.getUserName() + "\"�f j� est� no grupo �a�l" + group.name() + "�f.");
			return;
		}

		if (group == Group.MEMBRO) {
			player.setGroup(Group.MEMBRO);
			player.setTag(Tag.MEMBRO);
			player.sendMessage("O seu grupo foi retirado do servidor!");
		} else {
			player.setGroup(group);
			player.setTag(Tag.valueOf(group.name()));
			player.sendMessage(
					"Seu grupo foi alterado para �a" + (Tag.valueOf(group.name()) == Tag.MEMBRO ? "�f�lMEMBRO"
							: Tag.valueOf(group.name()).getPrefix().replace(" ", "")) + "�f.");
		}

		if (!player.isOnline()) {
			BukkitMain.getAccountCommon().saveAccount(player);
		} else {
			BukkitMain.getInstance().superms.removeAttachment(player.getPlayer());
			BukkitMain.getInstance().superms.updateAttachment(player.getPlayer(), player);
		}

		send(sender, "Voc� alterou o grupo do �a\"" + player.getUserName() + "\"�f para �a�l" + group.name() + "�f.");
		broadcast("O grupo do jogador �a" + player.getUserName() + "�f foi alterado para �a" + group.name() + "�f.",
				Group.MOD);
	}

	@Command(name = "tempgroup", aliases = { "givevip" }, groupToUse = Group.ADMIN)
	public void onTempgroup(CommandArgs cmdArgs) {
		String[] a = cmdArgs.getArgs();
		CommandSender sender = cmdArgs.getSender();

		if (a.length != 3) {
			send(sender, "Use �a/tempgroup [Player] [Tempo] [Group]�f para setar um grupo temporario para alguem!");
			return;
		}

		PaymentGroup group = null;
		Long expire = null;

		try {
			group = PaymentGroup.valueOf(a[2].toUpperCase());
		} catch (Exception e) {
			send(sender, "Use um grupo valido!");

			if (hasServerGroup(sender, Group.ADMIN)) {
				String list = "Grupos disponiveis: ";

				for (int x = 0; x < PaymentGroup.HEIGHT.ordinal(); x++) {
					list += Group.values()[x].name() + (x == Group.HEIGHT.ordinal() - 1 ? "." : ", ");
				}

				send(sender, list);
			}
			return;
		}

		try {
			expire = DateUtils.parseDateDiff(a[1], true);
		} catch (Exception e) {
			send(sender, "Use um formato de tempo valido! (Exemplo: 30s, 30m, 30d, 30w, 30mo, 30y)");
		}

		long newTime = expire - System.currentTimeMillis();

		UUID uuid = getAccountCommon().getUUID(a[0]);

		if (uuid == null) {
			send(sender, "O jogador �c\"" + a[0] + "\" �fn�o existe no nosso banco de dados!");
			return;
		}

		Account player = getAccountCommon().getAccount(uuid);

		if (player == null) {
			player = getAccountCommon().loadAccount(uuid, false);

			if (player == null) {
				send(sender, "O jogador �c\"" + a[0] + "\" �fn�o existe!");
				return;
			}
		}

		player.getPayment().put(group,
				player.getPayment().containsKey(group) ? player.getPayment().get(group) + newTime : expire);
		player.sendMessage("Voc� ganhou �a" + group.name() + "�f por �a" + DateUtils.getTime(expire) + "�f.");

		if (!player.isOnline()) {
			BukkitMain.getAccountCommon().saveAccount(player);
		}

		send(sender, "Voc� deu setou temporariamente o grupo �a" + group.name() + "�f para o jogador �a"
				+ player.getUserName() + "�f por �a" + DateUtils.getTime(expire) + "�f.");
		broadcast("O grupo �a" + group.name() + "�f foi temporariamente setado para o jogador �a" + player.getUserName()
				+ "�f por �a" + DateUtils.getTime(expire) + "�f.", Group.MOD);
	}

	@Command(name = "removervip", aliases = { "rvip" }, groupToUse = Group.ADMIN)
	public void onRemovervip(CommandArgs cmdArgs) {
		String[] a = cmdArgs.getArgs();
		CommandSender sender = cmdArgs.getSender();

		if (a.length != 2) {
			send(sender, "Use �a/removervip [Jogador] [Vip]�f para remover o vip de alguem!");
			return;
		}

		PaymentGroup group = null;

		try {
			group = PaymentGroup.valueOf(a[1].toUpperCase());
		} catch (Exception e) {
			send(sender, "Use um grupo valido!");

			if (hasServerGroup(sender, Group.ADMIN)) {
				String list = "Grupos disponiveis: ";

				for (int x = 0; x < PaymentGroup.HEIGHT.ordinal(); x++) {
					list += Group.values()[x].name() + (x == Group.HEIGHT.ordinal() - 1 ? "." : ", ");
				}

				send(sender, list);
			}
			return;
		}

		UUID uuid = getAccountCommon().getUUID(a[0]);

		if (uuid == null) {
			send(sender, "O jogador �c\"" + a[0] + "\" �fn�o existe no nosso banco de dados!");
			return;
		}

		Account player = getAccountCommon().getAccount(uuid);

		if (player == null) {
			player = getAccountCommon().loadAccount(uuid, false);

			if (player == null) {
				send(sender, "O jogador �c\"" + a[0] + "\" �fn�o existe!");
				return;
			}
		}

		if (!player.getPayment().containsKey(group)) {
			send(sender, "O jogador �a\"" + player.getUserName() + "\"�f n�o possui o grupo �a" + group.name() + "�f.");
			return;
		}

		player.getPayment().remove(group);
		player.sendMessage("O seu grupo �a" + Tag.valueOf(a[0]).getPrefix().replace(" ", "") + "�f foi retirado!");

		if (!player.isOnline())
			BukkitMain.getAccountCommon().saveAccount(player);

		send(sender, "O grupo " + Tag.valueOf(a[0]).getPrefix().replace(" ", "") + "�f do jogador �a"
				+ player.getUserName() + "�f foi retirado!");
		broadcast("O grupo �a" + group.name() + "�f foi retirado do jogador �a" + player.getUserName() + "�f.",
				Group.MOD);
	}

}
