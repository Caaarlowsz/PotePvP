package br.com.yallandev.potepvp.commands.register;

import org.bukkit.command.CommandSender;

import br.com.yallandev.potepvp.commands.BukkitCommandFramework.Command;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.CommandArgs;
import br.com.yallandev.potepvp.manager.CommandClass;
import br.com.yallandev.potepvp.manager.WarpManager.Warp;
import br.com.yallandev.potepvp.permissions.group.Group;

public class DamageCommand extends CommandClass {

	@Command(name = "damage", groupToUse = Group.MOD)
	public void onDamage(CommandArgs cmdArgs) {
		String[] a = cmdArgs.getArgs();
		CommandSender s = cmdArgs.getSender();

		if (a.length == 0) {
			send(s, "Use �a/damage [on/off]�f para ativar ou desativar o dano global.");
			send(s, "Use �a/damage [Warp] [on/off]�f para ativar ou desativar o dano de uma warp.");
		} else if (a.length == 1) {
			if (a[0].equalsIgnoreCase("on")) {
				if (main.getServerManager().isDamageAll()) {
					send(s, "O dano global j� est� "
							+ (main.getServerManager().isDamageAll() ? "�a�lATIVADO" : "�4�lDESATIVADO") + "�f.");
					return;
				}
				main.getServerManager().setDamageAll(true);
				send(s, "Voc� " + (main.getServerManager().isDamageAll() ? "�a�lATIVOU" : "�4�lDESATIVOU")
						+ " �fo pvp global.");
			} else if (a[0].equalsIgnoreCase("off")) {
				if (!main.getServerManager().isDamageAll()) {
					send(s, "O dano global j� est� "
							+ (main.getServerManager().isDamageAll() ? "�a�lATIVADO" : "�4�lDESATIVADO") + "�f.");
					return;
				}
				main.getServerManager().setDamageAll(false);
				send(s, "Voc� " + (main.getServerManager().isDamageAll() ? "�a�lATIVOU" : "�4�lDESATIVOU")
						+ " �fo pvp global.");
			} else {
				send(s, "Use �a/damage [on/off]�f para ativar ou desativar o dano global.");
				send(s, "Use �a/damage [Warp] [on/off]�f para ativar ou desativar o dano de uma warp.");
			}
		} else {
			Warp warp = main.getWarpManager().getWarp(a[0]);

			if (warp == null) {
				send(s, "Essa warp n�o existe.");
				return;
			}

			if (a[1].equalsIgnoreCase("on")) {
				if (main.getServerManager().isDamageEnable(warp)) {
					send(s, "O dano global j� est� "
							+ (main.getServerManager().isDamageEnable(warp) ? "�a�lATIVADO" : "�4�lDESATIVADO")
							+ "�f.");
					return;
				}
				main.getServerManager().setDamageEnable(warp, true);
				send(s, "Voc� " + (main.getServerManager().isDamageEnable(warp) ? "�a�lATIVOU" : "�4�lDESATIVOU")
						+ " �fo pvp global.");
			} else if (a[1].equalsIgnoreCase("off")) {
				if (!main.getServerManager().isDamageAll()) {
					send(s, "O dano global j� est� "
							+ (main.getServerManager().isDamageEnable(warp) ? "�a�lATIVADO" : "�4�lDESATIVADO")
							+ "�f.");
					return;
				}
				main.getServerManager().setDamageEnable(warp, false);
				send(s, "Voc� " + (main.getServerManager().isDamageEnable(warp) ? "�a�lATIVOU" : "�4�lDESATIVOU")
						+ " �fo pvp global.");
			} else {
				send(s, "Use �a/damage [on/off]�f para ativar ou desativar o dano global.");
				send(s, "Use �a/damage [Warp] [on/off]�f para ativar ou desativar o dano de uma warp.");
			}
		}
	}

}
