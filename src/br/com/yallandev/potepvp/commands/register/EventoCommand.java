package br.com.yallandev.potepvp.commands.register;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.yallandev.potepvp.BukkitMain.Configuration;
import br.com.yallandev.potepvp.account.Account;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.Command;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.CommandArgs;
import br.com.yallandev.potepvp.evento.RDMAutomatic;
import br.com.yallandev.potepvp.evento.RDMAutomatic.GameType;
import br.com.yallandev.potepvp.manager.CommandClass;
import br.com.yallandev.potepvp.permissions.group.Group;

public class EventoCommand extends CommandClass {
	
	@Command(name = "evento")
	public void onEvento(CommandArgs cmdArgs) {
		String[] a = cmdArgs.getArgs();
		CommandSender s = cmdArgs.getSender();
		
		if (a.length == 0) {
			if (main.getEventManager().isRunningRDM()) {
				send(s, "Use §a/evento entrar§f para entrar em um evento!");
				send(s, "Use §a/evento sair§f para sair de um evento!");
				send(s, "Use §a/evento spec§f para espectar um evento!");
			} else {
				send(s, "Não há nenhum evento no momento!");
			}
			
			if (hasServerGroup(s, Group.MODPLUS)) {
				send(s, "Use §a/evento iniciar§f para iniciar um evento!");
				send(s, "Use §a/evento fechar§f para fechar um evento!");
				send(s, "Use §a/evento kick [Player]§f para kickar um jogador do evento!");
				send(s, "Use §a/evento time [Tempo]§f para mudar o tempo do evento!");
				send(s, "Use §a/evento setmax [Tempo]§f para mudar o tempo do evento!");
			}
		} else if (a.length == 1) {
			if (a[0].equalsIgnoreCase("entrar")) {
				if (cmdArgs.isPlayer()) {
					if (main.getEventManager().isRunningRDM()) {
						if (main.getEventManager().getRdmAutomatic().getGameType() == GameType.GAMIMG) {
							send(s, "O evento já iniciou!");
							return;
						}
						
						if (main.getEventManager().getRdmAutomatic().getPlayers().size() > main.getEventManager().getRdmAutomatic().getMaxPlayers()) {
							if (!hasServerGroup(s, Group.MVP)) {
								send(s, "O evento está cheio!");
								send(s, "Compre vip em §n" + Configuration.SITE.getMessage() + "§f e tenha o acesso liberado!");
								return;
							}
						}
						
						main.getEventManager().getRdmAutomatic().putInEvent((Player) s);
					} else {
						send(s, "Não há nenhum evento no momento!");
					}
				} else {
					send(s, "Somente jogadores podem executar esse comando!");
				}
			} if (a[0].equalsIgnoreCase("sair")) {
				if (cmdArgs.isPlayer()) {
					if (main.getEventManager().isRunningRDM()) {
						main.getEventManager().getRdmAutomatic().leaveEvent((Player) s);
					} else {
						send(s, "Não há nenhum evento no momento!");
					}
				} else {
					send(s, "Somente jogadores podem executar esse comando!");
				}
			} else if (a[0].equalsIgnoreCase("spec")) {
				if (hasServerGroup(s, Group.LIGHT)) {
					Account player = cmdArgs.getAccount();
					
					if (!main.getEventManager().getRdmAutomatic().isSpec((Player) s))
						main.getEventManager().getRdmAutomatic().setSpec(player);
					else
						main.getEventManager().getRdmAutomatic().removeSpec(player);
				} else {
					send(s, "Somente jogadores §aVIPS§f podem usar esse comando!");
				}
			} else if (a[0].equalsIgnoreCase("iniciar")) {
				if (hasServerGroup(s, Group.MODPLUS)) {
					if (main.getEventManager().isRunningRDM()) {
						send(s, "Já há um evento em andamento!");
					} else {
						main.getEventManager().setRdmAutomatic(new RDMAutomatic());
						send(s, "Você iniciou um evento rei da mesa!");
						broadcast("O jogador §a" + s.getName() + "§f iniciou um evento §aRei da Mesa§f.", Group.TRIAL);
					}
				}
			} else if (a[0].equalsIgnoreCase("fechar")) {
				if (hasServerGroup(s, Group.MODPLUS)) {
					if (main.getEventManager().isRunningRDM()) {
						main.getEventManager().getRdmAutomatic().destroy();
						send(s, "Você finalizou o evento §aRei da Mesa§f.");
						broadcast("O jogador §a" + s.getName() + "§f fechou um evento §aRei da Mesa§f.", Group.TRIAL);
					} else {
						send(s, "Não há nenhum evento em andamento no momento!");
					}
				}
			}
		} else {
			if (a[0].equalsIgnoreCase("time")) {
				if (hasServerGroup(s, Group.MODPLUS)) {
					if (!main.getEventManager().isRunningRDM()) {
						send(s, "Não há nenhum evento em andamento no momento!");
						return;
					}
					if (!isNumeric(a[1])) {
						send(s, "Use numeros como tempo!");
						return;
					}
					
					Integer time = Integer.valueOf(a[1]);
					
					if (time <= 0) {
						send(s, "O tempo tem que ser maior do que 0!");
						return;
					}
					
					main.getEventManager().getRdmAutomatic().setTime(time);
					send(s, "Você alterou o tempo do evento para §a" + time + "§f.");
					broadcast("O tempo do evento foi alterado pelo §a" + s.getName() + "§f para §a" + time + "§f.", Group.TRIAL);
				}
			} else if (a[0].equalsIgnoreCase("setmax")) {
				if (hasServerGroup(s, Group.MODPLUS)) {
					if (!main.getEventManager().isRunningRDM()) {
						send(s, "Não há nenhum evento em andamento no momento!");
						return;
					}
					if (!isNumeric(a[1])) {
						send(s, "Use numeros como tempo!");
						return;
					}
					
					Integer time = Integer.valueOf(a[1]);
					
					if (time <= 0) {
						send(s, "O tempo tem que ser maior do que 0!");
						return;
					}
					
					main.getEventManager().getRdmAutomatic().setMaxPlayers(time);
					send(s, "Você alterou o maximo de jogadores do evento para §a" + time + "§f.");
					broadcast("O maximo de jogadores do evento foi alterado pelo §a" + s.getName() + "§f para §a" + time + "§f.", Group.TRIAL);
				}
			}
		}
	}

}
