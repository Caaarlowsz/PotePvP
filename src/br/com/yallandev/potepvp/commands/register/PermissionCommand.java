package br.com.yallandev.potepvp.commands.register;

import org.bukkit.command.CommandSender;

import br.com.yallandev.potepvp.account.Account;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.Command;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.CommandArgs;
import br.com.yallandev.potepvp.kit.Kit;
import br.com.yallandev.potepvp.manager.CommandClass;
import br.com.yallandev.potepvp.permissions.group.Group;

public class PermissionCommand extends CommandClass {

	@Command(name = "givekit", aliases = { "darkit" }, groupToUse = Group.ADMIN)
	public void onGivekit(CommandArgs cmdArgs) {
		String[] a = cmdArgs.getArgs();
		CommandSender sender = cmdArgs.getSender();

		if (a.length != 2) {
			send(sender, "Use �a/givekit [Jogador] [Kit]�f para dar um kit para algum jogador!");
			return;
		}

		Kit kit = main.getKitManager().getKit(a[1]);

		if (kit == null) {
			send(sender, "O kit �a\"" + a[1] + "\"�f n�o existe!");
			return;
		}

		Account player = getAccountCommon().getAccount(getAccountCommon().getUUID(a[0]));

		if (player == null) {
			player = getAccountCommon().loadAccount(getAccountCommon().getUUID(a[0]), false);

			if (player == null) {
				send(sender, "O jogador �c\"" + a[0] + "\" �fn�o existe!");
				return;
			}
		}

		player.addPermission("kit." + kit.getKitName());
		player.sendMessage("Voc� ganhou o kit �a\"" + kit.getKitName() + "\"�f.");
		send(sender, "Voc� deu o kit �a" + kit.getKitName() + "�f para �a" + player.getUserName() + "�f.");
		broadcast("O jogador �a" + sender.getName() + "�f deu o kit �a" + kit.getKitName() + "�f para o �a"
				+ player.getUserName() + "�f.", Group.MOD);
	}

	@Command(name = "givepermission", aliases = { "darpermission" }, groupToUse = Group.ADMIN)
	public void onGivepermission(CommandArgs cmdArgs) {
		String[] a = cmdArgs.getArgs();
		CommandSender sender = cmdArgs.getSender();

		if (a.length != 2) {
			send(sender, "Use �a/givepermission [Jogador] [Kit]�f para dar um kit para algum jogador!");
			return;
		}

		String permission = a[1];

		Account player = getAccountCommon().getAccount(getAccountCommon().getUUID(a[0]));

		if (player == null) {
			player = getAccountCommon().loadAccount(getAccountCommon().getUUID(a[0]), false);

			if (player == null) {
				send(sender, "O jogador �c\"" + a[0] + "\" �fn�o existe!");
				return;
			}
		}

		player.addPermission(permission.toLowerCase());
		player.sendMessage("Voc� ganhou a permiss�o �a\"" + permission.toLowerCase() + "\"�f.");
		send(sender,
				"Voc� deu a permiss�o �a" + permission.toLowerCase() + "�f para �a" + player.getUserName() + "�f.");
		broadcast("O jogador �a" + sender.getName() + "�f deu a permiss�o �a" + permission.toLowerCase()
				+ "�f para o �a" + player.getUserName() + "�f.", Group.MOD);
	}

}
