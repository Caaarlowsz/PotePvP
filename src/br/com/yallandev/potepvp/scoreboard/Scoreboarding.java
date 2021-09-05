package br.com.yallandev.potepvp.scoreboard;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.spigotmc.ProtocolInjector;
import org.spigotmc.ProtocolInjector.PacketTabHeader;

import br.com.yallandev.potepvp.BukkitMain;
import br.com.yallandev.potepvp.BukkitMain.Configuration;
import br.com.yallandev.potepvp.account.Account;
import br.com.yallandev.potepvp.event.update.UpdateEvent;
import br.com.yallandev.potepvp.event.update.UpdateEvent.UpdateType;
import br.com.yallandev.potepvp.listener.umvum.Warp1v1;
import br.com.yallandev.potepvp.listener.umvum.WarpSumo;
import br.com.yallandev.potepvp.manager.WarpManager.Warp;
import br.com.yallandev.potepvp.permissions.group.Group;
import br.com.yallandev.potepvp.ranking.Ranking;
import br.com.yallandev.potepvp.tag.Tag;
import br.com.yallandev.potepvp.utils.Util;
import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.IChatBaseComponent;

public class Scoreboarding {
	
	private static void addRanking(ScoreboardManager scoreboardManager, Account player) {
		if (player.getRanking() == Ranking.POTTER) {
			String color = "§";
			
			if (new Random().nextBoolean()) {
				color += String.valueOf(colors[new Random().nextInt(colors.length)]);
			} else {
				color += new Random().nextInt(9) + 1;
			}
			
			scoreboardManager.addLine("§fRanking: ", color + player.getRanking().getIcon() + " " + player.getRanking().name());
		} else {
			scoreboardManager.addLine("§fRanking: ", player.getRanking().getColor() + player.getRanking().getIcon() + " " + player.getRanking().name());
		}
	}
	
	public static void setScoreboard(Player p) {
		Account player = BukkitMain.getAccountCommon().getAccount(p.getUniqueId());
		
		if (player == null)
			return;
		
		if (!player.isScoreboardEnable())
			return;
		
		Warp warp = BukkitMain.getInstance().getPlayerManager().getWarp(p.getUniqueId());
		
		if (BukkitMain.getInstance().getPlayerManager().isScreenshare(p.getUniqueId())) {
			ScoreboardManager scoreboard = new ScoreboardManager("§6§lSCREENSHARE", "ss");
			
			scoreboard.blankLine();
			scoreboard.addLine("§fGroup: ", player.getServerGroup() == Group.MEMBRO ? "§f§lMEMBRO" : Tag.valueOf(player.getServerGroup().name()).getPrefix().replace(" ", ""));
			scoreboard.addLine("§fXp: §7", "" + player.getStatus().getXp());
			addRanking(scoreboard, player);
			scoreboard.blankLine();
			scoreboard.addLine("§cSCREEN SHARE");
			scoreboard.blankLine();
			scoreboard.addLine("§e/score");
			scoreboard.addLine("§3§n" + Configuration.SCOREBOARD_SITE.getMessage());
			scoreboard.update(p);
			return;
		}
		
		if (BukkitMain.getInstance().getVanishMode().isAdmin(player.getUuid())) {
			ScoreboardManager scoreboard = new ScoreboardManager("§6§lPVP", "admin");
			
			scoreboard.blankLine();
			scoreboard.addLine("§fGroup: ", player.getServerGroup() == Group.MEMBRO ? "§f§lMEMBRO" : Tag.valueOf(player.getServerGroup().name()).getPrefix().replace(" ", ""));
			scoreboard.addLine("§fXp: §7", "" + player.getStatus().getXp());
			addRanking(scoreboard, player);
			scoreboard.blankLine();
			scoreboard.addLine("§cMODO VANISH");
			scoreboard.blankLine();
			scoreboard.addLine("§7/score");
			scoreboard.addLine("§3§n" + Configuration.SCOREBOARD_SITE.getMessage());
			scoreboard.update(p);
			return;
		}
		
		if (warp.getWarpName().equalsIgnoreCase("1v1")) {
			if (Warp1v1.isIn1v1(p)) {
				ScoreboardManager scoreboard = new ScoreboardManager("§6§l1v1", "1vs1");
				
				scoreboard.blankLine();
				scoreboard.addLine("§fGroup: ", player.getServerGroup() == Group.MEMBRO ? "§f§lMEMBRO" : Tag.valueOf(player.getServerGroup().name()).getPrefix().replace(" ", ""));
				scoreboard.addLine("§fXp: §7", "" + player.getStatus().getXp());
				scoreboard.addLine("§fRanking: §7", player.getRanking().name());
				scoreboard.blankLine();
				scoreboard.addLine("§fBatalhando contra: ");
				scoreboard.addLine("§3" + Warp1v1.duel.get(p));
				scoreboard.blankLine();
				scoreboard.addLine("§e/score");
				scoreboard.addLine("§3§n" + Configuration.SCOREBOARD_SITE.getMessage());
				scoreboard.update(p);
			} else {
				ScoreboardManager scoreboard = new ScoreboardManager("§6§l1v1", "1v1");
				
				scoreboard.blankLine();
				scoreboard.addLine("§fGroup: ", player.getServerGroup() == Group.MEMBRO ? "§7§lMEMBRO" : Tag.valueOf(player.getServerGroup().name()).getPrefix().replace(" ", ""));
				scoreboard.addLine("§fXp: §7", "" + player.getStatus().getXp());
				addRanking(scoreboard, player);
				scoreboard.blankLine();
				scoreboard.addLine("§fKills: §7", "" + player.getStatus().getKills());
				scoreboard.addLine("§fDeaths: §7", "" + player.getStatus().getDeaths());
				scoreboard.addLine("§fStreak: §7", "" + player.getStatus().getKillstreak());
				scoreboard.blankLine();
				scoreboard.addLine("§e/score");
				scoreboard.addLine("§3§n" + Configuration.SCOREBOARD_SITE.getMessage());
				scoreboard.update(p);
			}
			return;
		}
		
		if (warp.getWarpName().equalsIgnoreCase("Sumo")) {
			if (WarpSumo.isIn1v1(p)) {
				ScoreboardManager scoreboard = new ScoreboardManager("§6§lSUMO", "sumo");
				
				scoreboard.blankLine();
				scoreboard.addLine("§fGroup: ", player.getServerGroup() == Group.MEMBRO ? "§f§lMEMBRO" : Tag.valueOf(player.getServerGroup().name()).getPrefix().replace(" ", ""));
				scoreboard.addLine("§fXp: §7", "" + player.getStatus().getXp());
				addRanking(scoreboard, player);
				scoreboard.blankLine();
				scoreboard.addLine("§fBatalhando contra: ");
				scoreboard.addLine("§3" + WarpSumo.duel.get(p));
				scoreboard.blankLine();
				scoreboard.addLine("§e/score");
				scoreboard.addLine("§3§n" + Configuration.SCOREBOARD_SITE.getMessage());
				scoreboard.update(p);
			} else {
				ScoreboardManager scoreboard = new ScoreboardManager("§6§lSUMO", "sumo2");
				
				scoreboard.blankLine();
				scoreboard.addLine("§fGroup: ", player.getServerGroup() == Group.MEMBRO ? "§7§lMEMBRO" : Tag.valueOf(player.getServerGroup().name()).getPrefix().replace(" ", ""));
				scoreboard.addLine("§fXp: §7", "" + player.getStatus().getXp());
				addRanking(scoreboard, player);
				scoreboard.blankLine();
				scoreboard.addLine("§fKills: §7", "" + player.getStatus().getKills());
				scoreboard.addLine("§fDeaths: §7", "" + player.getStatus().getDeaths());
				scoreboard.addLine("§fStreak: §7", "" + player.getStatus().getKillstreak());
				scoreboard.blankLine();
				scoreboard.addLine("§e/score");
				scoreboard.addLine("§3§n" + Configuration.SCOREBOARD_SITE.getMessage());
				scoreboard.update(p);
			}
			return;
		}
		
		ScoreboardManager scoreboard = new ScoreboardManager("§6§lPVP", warp.getWarpName().equalsIgnoreCase("Spawn") ? "spawn" : "warp");
		
		scoreboard.blankLine();
		scoreboard.addLine("§fGroup: ", player.getServerGroup() == Group.MEMBRO ? "§7§lMEMBRO" : Tag.valueOf(player.getServerGroup().name()).getPrefix().replace(" ", ""));
		scoreboard.addLine("§fXp: §7", "" + player.getStatus().getXp());
		addRanking(scoreboard, player);
		scoreboard.blankLine();
		
		if (warp.getWarpName().equalsIgnoreCase("Spawn")) {
			scoreboard.addLine("§fKit: §a", BukkitMain.getInstance().getKitManager().getNameOfAbility(player.getUuid()));
		} else {
			scoreboard.addLine("§fWarp: §a", warp.getWarpName());
		}
		scoreboard.addLine("§fMoney: §7", "" + player.getStatus().getMoney());
		scoreboard.blankLine();
		scoreboard.addLine("§fKills: §7", "" + player.getStatus().getKills());
		scoreboard.addLine("§fDeaths: §7", "" + player.getStatus().getDeaths());
		scoreboard.addLine("§fStreak: §7", "" + player.getStatus().getKillstreak());
		scoreboard.blankLine();
		scoreboard.addLine("§e/score");
		scoreboard.addLine("§3§n" + Configuration.SCOREBOARD_SITE.getMessage());
		scoreboard.update(p);
	}
	
	private static void update(Player p) {
		Scoreboard sb = p.getScoreboard();
		
		Account player = BukkitMain.getAccountCommon().getAccount(p.getUniqueId());
		
		if (player == null)
			return;
		
		setScoreboard(p);
		sendTab(p, "§6Servidor: §fA1 §8| §6Kit: §f" + BukkitMain.getInstance().getKitManager().getNameOfAbility(p.getUniqueId()) + "  §8| §6Ping: §f" + ((CraftPlayer) p).getHandle().ping + "ms", "\n§bNick: §f" + player.getUserName() + " §8| §bXp: §f" + player.getStatus().getXp() + " §8| §bRanking: §f" + player.getRanking().name() + "\n§bMais informações: §f" + Configuration.SITE.getMessage()); 
	}
	
	public static char[] colors = { 'a', 'e', 'b', 'c', 'd' };
	
	public static void sendTab(Player p, String cima, String baixo) {
		if (((CraftPlayer) p).getHandle().playerConnection.networkManager.getVersion() < 46) {
			return;
		}
		
		IChatBaseComponent tabcima = ChatSerializer.a("{\"text\": \"" + cima + "\"}");
		IChatBaseComponent tabbaixo = ChatSerializer.a("{\"text\": \"" + baixo + "\"}");
		PacketTabHeader packet = new ProtocolInjector.PacketTabHeader(tabcima, tabbaixo);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}

	
	public static void startUpdater() {
		Bukkit.getPluginManager().registerEvents(new Listener() {
			
			@EventHandler
			public void onUpdate(UpdateEvent e) {
				if (e.getType() == UpdateType.SECOND) {
					for (Player p : Util.getOnlinePlayers()) {
						update(p);
					}
				}
			}
			
		}, BukkitMain.getPlugin());
	}

}
