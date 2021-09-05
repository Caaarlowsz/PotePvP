package br.com.yallandev.potepvp.commands.register;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import br.com.yallandev.potepvp.commands.BukkitCommandFramework.Command;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.CommandArgs;
import br.com.yallandev.potepvp.manager.CommandClass;

public class PingCommand extends CommandClass {

	@Command(name = "ping")
	public void onPing(CommandArgs args) {
		if (!args.isPlayer())
			return;

		Player player = args.getPlayer();
		String[] a = args.getArgs();

		if (a.length == 0) {
			send(player, "Voc� est� a �a" + ((CraftPlayer) player).getHandle().ping + " ms�f.");
		} else {
			Player target = Bukkit.getPlayer(a[0]);

			if (target == null) {
				send(player, "O jogador �a\"" + a[0] + "�f n�o est� online!");
				return;
			}

			send(player, "O jogador �a\"" + target.getName() + "\"�f est� a �a"
					+ ((CraftPlayer) target).getHandle().ping + " ms�f.");
		}
	}

}
