package br.com.yallandev.potepvp.commands.register;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import br.com.yallandev.potepvp.commands.BukkitCommandFramework.Command;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.CommandArgs;
import br.com.yallandev.potepvp.manager.CommandClass;
import br.com.yallandev.potepvp.permissions.group.Group;

public class TeleportCommand extends CommandClass {
	
	@Command(name = "teleportall", aliases = { "tpall" }, groupToUse = Group.MOD, onlyPlayers = true)
	public void onTeleportall(CommandArgs cmdArgs) {
		Player p = cmdArgs.getPlayer();
		String[] a = cmdArgs.getArgs();
		
		if (a.length == 0) {
			for (Player players : Bukkit.getOnlinePlayers()) {
				if (players.getUniqueId().equals(p.getUniqueId()))
					continue;
				
				players.teleport(p);
			}
			
			send(p, "Todos os jogadores foram teletransportados para você!");
			broadcast("Todos os jogadores foram teletransportados para o §a" + p.getName() + "§f.", Group.MOD);
			broadcast("Todos os jogadores foram teletransportados!");
			return;
		}
		
		Player target = Bukkit.getPlayer(a[0]);
		
		if (target == null) {
			send(p, "O jogador §c\"" + a[0] + "\"§f não está online!");
			return;
		}
		
		for (Player players : Bukkit.getOnlinePlayers()) {
			if (players.getUniqueId().equals(target.getUniqueId()))
				continue;
			
			target.teleport(p);
		}
		
		send(p, "Todos os jogadores foram teletransportados para o §a" + target.getName() + "§f.");
		broadcast("Todos os jogadores foram forçados a ser teletransportados para o §a" + target.getName() + "§f pelo §a" + p.getName() + "§f.", Group.MOD);
		broadcast("Todos os jogadores foram teletransportados!");
	}

}
