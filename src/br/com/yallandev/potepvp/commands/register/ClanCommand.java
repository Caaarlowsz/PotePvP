package br.com.yallandev.potepvp.commands.register;

import java.util.UUID;

import org.bukkit.command.CommandSender;

import br.com.yallandev.potepvp.BukkitMain;
import br.com.yallandev.potepvp.account.Account;
import br.com.yallandev.potepvp.clan.Clan;
import br.com.yallandev.potepvp.clan.ClanStatus;
import br.com.yallandev.potepvp.clan.MemberType;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.Command;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.CommandArgs;
import br.com.yallandev.potepvp.manager.CommandClass;
import br.com.yallandev.potepvp.permissions.group.Group;
import br.com.yallandev.potepvp.utils.string.CenterChat;

public class ClanCommand extends CommandClass {
	
	public void sendUsage(CommandSender s, Account player) {
		if (player.hasClan()) {
			send(s, "Use §a/clan info§f para ver o status do seu clã.");
			send(s, "Use §a/clan info [Tag]§f para ver o status de algum clã.");
			send(s, "Use §a/clan convidar [Player]§f para convidar um player para o seu clã.");
			
			if (player.getClan().hasAdmin(player.getUuid())) {
				send(s, "Use §a/clan expulsar [Player]§f para expulsar alguém do seu clã.");
				send(s, "Use §a/clan upgrade§f para dar upgrade em seu clã.");
			}
			
			if (player.getClan().isOwner(player.getUuid())) {
				send(s, "Use §a/clan disband§f para deletar seu clã.");
				send(s, "Use §a/clan setdescription§f para deletar seu clã.");
			}
		} else {
			send(s, "Use §a/clan info [Tag]§f para ver o status de algum clã.");
			send(s, "Use §a/clan create [Tag] [Nome do Clan]§f para criar um clã.");
			send(s, "Use §a/clan convidar [Player]§f para convidar um player para o seu clã.");
			send(s, "Use §a/clan aceitar [Player]§f para aceitar um convite para entrar em um clã.");
			send(s, "Use §a/clan rejeitar [Player]§f para rejeitar um convite para entrar em um clã.");
		}
	}
	
	@Command(name = "clan", onlyPlayers = true)
	public void onClan(CommandArgs cmdArgs) {
		String[] a = cmdArgs.getArgs();
		CommandSender s = cmdArgs.getSender();
		Account player = cmdArgs.getAccount();
		
		if (a.length == 0) {
			sendUsage(s, player);
			return;
		}
		
		if (a.length == 1) {
			if (a[0].equalsIgnoreCase("info")) {
				if (player.hasClan()) {
					Clan clan = player.getClan();
					
					s.sendMessage(CenterChat.centered("§c§m---[-]---------------------------------------[-]---"));
					s.sendMessage("");
					s.sendMessage("§eNome do clã: §b" + clan.getClanName());
					s.sendMessage("§eTag do clã: §b" + clan.getClanTag().replace("&", "§"));
					s.sendMessage("");
					s.sendMessage("§4§lDONO: §f" + clan.getOwnerName());
					
					String admins = "§c§lADMINISTRADORES: §f";
					
					for (Account admin : clan.getAccountOfAdmins()) {
						admins += admin.getUserName() + ", ";
					}
					
					s.sendMessage(admins);
					
					String members = "§f§lMEMBROS: §f";
					
					for (Account member : clan.getAccountOfMembers()) {
						members += member.getUserName() + ", ";
					}
					
					s.sendMessage(members);
					s.sendMessage("§eTotal: §b" + clan.size() + "/" + clan.getMaxPlayers());
					s.sendMessage("");
					s.sendMessage("§7Kills: §b" + clan.getClanStatus().getKills());
					s.sendMessage("§7Deaths: §b" + clan.getClanStatus().getDeaths());
					s.sendMessage("§7Xp: §b" + clan.getClanStatus().getXp());
					s.sendMessage("§7Money: §b-50");
					s.sendMessage("");
					s.sendMessage(CenterChat.centered("§c§m---[-]---------------------------------------[-]---"));
				} else {
					player.sendMessage("Você não tem um clã!");
				}
			} else if (a[0].equalsIgnoreCase("disband")) {
				if (player.hasClan()) {
					s.sendMessage(CenterChat.centered("§c§m---[-]---------------------------------------[-]---"));
					s.sendMessage("");
					s.sendMessage("Você deletou o seu clã!");
					s.sendMessage("");
					s.sendMessage(CenterChat.centered("§c§m---[-]---------------------------------------[-]---"));
					
					main.getClanCommon().deleteClan(player.getClan());
					player.setClan(null);
				} else {
					player.sendMessage("Você não tem um clã!");
				}
			} else if (a[0].equalsIgnoreCase("upgrade")) {
				if (player.hasClan()) {
					s.sendMessage(CenterChat.centered("§c§m---[-]---------------------------------------[-]---"));
					s.sendMessage("");
					s.sendMessage("§fCalma, esse argumento ainda está sendo testado, liberaremos em breve!");
					s.sendMessage("");
					s.sendMessage(CenterChat.centered("§c§m---[-]---------------------------------------[-]---"));
				} else {
					player.sendMessage("Você não tem um clã!");
				}
			} else if (a[0].equalsIgnoreCase("sair")) {
				if (player.hasClan()) {
					Clan clan = player.getClan();
					
					if (clan.isOwner(player.getUuid())) {
						player.sendMessage("Você não pode sair do clã, use para §a/clan disband§f para desfazer o seu clã!");
						player.sendMessage("O dinheiro gasto para criar o clã não será reembolsado!");
						return;
					}
					
					clan.removeParticipant(player.getUuid());
					player.setClan(null);
					player.sendMessage("Você saiu do clã §a" + clan.getClanName() + " §f(" + clan.getClanTag().replace("&", "§") + "§f)");
					main.getClanCommon().broadcastClan(clan, "O jogador §a" +  player.getUserName() + "§f saiu do clã!");
					main.getClanCommon().saveClan(clan);
				} else {
					player.sendMessage("Você não tem um clã!");
				}
			} else {
				sendUsage(s, player);
			}
			return;
		}
		
		if (a.length == 2) {
			if (a[0].equalsIgnoreCase("info")) {
				String clanTag = a[1];
				
				Clan clan = main.getClanCommon().loadClanFromTag(clanTag);
				
				if (clan == null) {
					send(s, "Esse clã não existe!");
					return;
				}
				
				s.sendMessage(CenterChat.centered("§c§m---[-]---------------------------------------[-]---"));
				s.sendMessage("");
				s.sendMessage("§eNome do clã: §b" + clan.getClanName());
				s.sendMessage("§eTag do clã: §b" + clan.getClanTag().replace("&", "§"));
				s.sendMessage("");
				s.sendMessage("§4§lDONO: §f" + clan.getOwnerName());
				
				String admins = "§c§lADMINISTRADORES: §f";
				
				for (Account admin : clan.getAccountOfAdmins()) {
					admins += admin.getUserName() + ", ";
				}
				
				s.sendMessage(admins);
				
				String members = "§f§lMEMBROS: §f";
				
				for (Account member : clan.getAccountOfMembers()) {
					members += member.getUserName() + ", ";
				}
				
				s.sendMessage(members);
				s.sendMessage("§eTotal: §b" + clan.size() + "/" + clan.getMaxPlayers());
				s.sendMessage("");
				s.sendMessage("§7Kills: §b" + clan.getClanStatus().getKills());
				s.sendMessage("§7Deaths: §b" + clan.getClanStatus().getDeaths());
				s.sendMessage("§7Xp: §b" + clan.getClanStatus().getXp());
				s.sendMessage("§7Money: §b-50");
				s.sendMessage("");
				s.sendMessage(CenterChat.centered("§c§m---[-]---------------------------------------[-]---"));
			} else if (a[0].equalsIgnoreCase("convidar") || a[0].equalsIgnoreCase("invite")) {
				if (!player.hasClan()) {
					send(s, "Você não tem um clã!");
					return;
				}
				
				if (!player.getClan().hasAdmin(player.getUuid())) {
					player.sendMessage("Somente §4§lADMINISTRADORES§f do clã podem usar esse argumento!");
					return;
				}
				
				if (player.getClan().size() == player.getClan().getMaxPlayers()) {
					player.sendMessage("O seu clã já está cheio!");
					player.sendMessage("Use §a/clan upgrade§f para aumentar os slots do clã!");
					return;
				}
				
				UUID uuid = BukkitMain.getAccountCommon().getUUID(a[1]);
				
				if (uuid == null) {
					send(s, "O jogador §a" + a[0] + "§f não existe em nosso banco de dados!");
					return;
				}
				
				Account target = BukkitMain.getAccountCommon().getAccount(uuid);
				
				if (target == null) {
					send(s, "O jogador §a" + a[0] + "§f não está online!");
					return;
				}
				
				if (target.hasClan()) {
					send(s, "O jogador §a" + target.getUserName() + "§f já tem o clã.");
					return;
				}
				
				main.getClanCommon().createInvite(player, target, player.getClan());
				send(s, "Você convidou o jogador §a" + target.getUserName() + "§f.");
			} else if (a[0].equalsIgnoreCase("aceitar")) {
				if (player.hasClan()) {
					send(s, "Você já tem um clã!");
					return;
				}
				
				UUID uuid = BukkitMain.getAccountCommon().getUUID(a[1]);
				
				main.getClanCommon().acceptInvite(player, uuid);
			} else if (a[0].equalsIgnoreCase("expulsar")) {
				if (!player.hasClan()) {
					send(s, "Você não tem um clã!");
					return;
				}
				
				if (!player.getClan().hasAdmin(player.getUuid())) {
					player.sendMessage("Somente §c§lADMINISTRADORES§f do clã podem fazer isso!");
					return;
				}
				
				UUID uuid = BukkitMain.getAccountCommon().getUUID(a[1]);
				
				Account target = getAccountCommon().getAccount(uuid);
				
				if (!player.getClan().isParticipant(uuid)) {
					player.sendMessage("O jogador §a\"" + target.getUserName() + "\"§f não está no seu clã!");
					return;
				}
				
				if (player.getClan().isOwner(uuid)) {
					player.sendMessage("Você não pode expulsar o dono do clã!");
					return;
				}
				
				if (target == null) {
					target = getAccountCommon().loadAccount(uuid, false);
					
					if (target == null) {
						player.sendMessage("O jogador §a" + a[1] + "§f não existe!");
						return;
					}
				}
				
				if (target.isOnline()) {
					target.sendMessage("Você foi expulso do seu clã!");
				}
				
				main.getClanCommon().broadcastClan(player.getClan(), "O jogador §a" + target.getUserName() + "§f foi expulso do clã pelo §a" + player.getUserName() + "§f.");
				target.setClan(null);
				player.getClan().removeParticipant(target.getUuid());
				main.getClanCommon().saveClan(player.getClan());
				
				if (!target.isOnline())
					getAccountCommon().saveAccount(target);
				
			} else if (a[0].equalsIgnoreCase("rejeitar")) {
				if (player.hasClan()) {
					send(s, "Você já tem um clã!");
					return;
				}
				
				UUID uuid = BukkitMain.getAccountCommon().getUUID(a[1]);
				
				if (uuid == null) {
					player.sendMessage("Esse jogador não existe!");
					return;
				}
				
				main.getClanCommon().rejectInvite(player, uuid);
			} else {
				sendUsage(s, player);
			}
			return;
		}
		
		if (a[0].equalsIgnoreCase("create") || a[0].equalsIgnoreCase("criar")) {
			if (player.hasClan()) {
				send(s, "Você já tem um clã.");
				return;
			}
			
			String clanTag = a[1];
			
			StringBuilder sb = new StringBuilder();

			for (int i = 2; i < a.length; i++) {
				if (i >= a.length - 1) {
					sb.append(a[i]);
				} else {
					sb.append(a[i] + " ");
				}
			}
			
			if (player.getStatus().getMoney() < 15000) {
				player.sendMessage("Você precisa de §a15000§f para criar um clã!");
				return;
			}

			String clanName = sb.toString().trim();
			
			
			if (main.getClanCommon().loadClan(clanName, false) != null) {
				player.sendMessage("Esse clã já existe!");
				return;
			}
			
			if (main.getClanCommon().loadClanFromTag(clanTag) != null) {
				player.sendMessage("Esse clã já existe!");
				return;
			}
			
			if (clanTag.length() > 16) {
				send(s, "A tag do seu clã tem que conter menos de §a16 caracteres§f.");
				return;
			}
			
			if (clanTag.contains("&") && !player.hasServerGroup(Group.LIGHT)) {
				send(s, "Somente jogadores §a§lLIGHT§f ou superior podem usar cores no clã.");
				return;
			}
			
			if (clanName.length() > 32) {
				send(s, "O nome do seu clã tem que conter menos de §a32 caracteres§f.");
				return;
			}
			
			Clan clan = new Clan(clanName, clanTag, "", new ClanStatus(clanName, 0, 0, 0, 0), player.getUuid());
			
			send(s, "Você criou o clã §a" + clanName + " §f(" + clanTag.replace("&", "§") + "§f).");
			send(s, "Foram retirados §a15000 coins§f do seu status!");
			
			player.setClan(clan);
			player.getStatus().removeMoney(15000);
			
			main.getClanCommon().saveClan(clan);
		}
	}

}
