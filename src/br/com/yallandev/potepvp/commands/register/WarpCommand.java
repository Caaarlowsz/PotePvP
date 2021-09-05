package br.com.yallandev.potepvp.commands.register;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.yallandev.potepvp.BukkitMain;
import br.com.yallandev.potepvp.BukkitMain.Configuration;
import br.com.yallandev.potepvp.account.Account;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.Command;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.CommandArgs;
import br.com.yallandev.potepvp.event.account.PlayerJoinWarpEvent;
import br.com.yallandev.potepvp.listener.PlayerListener;
import br.com.yallandev.potepvp.manager.CommandClass;
import br.com.yallandev.potepvp.manager.WarpManager.Warp;
import br.com.yallandev.potepvp.manager.WarpManager.Warp1v1;
import br.com.yallandev.potepvp.manager.WarpManager.WarpSumo;
import br.com.yallandev.potepvp.permissions.group.Group;
import br.com.yallandev.potepvp.scoreboard.Scoreboarding;
import br.com.yallandev.potepvp.utils.DateUtils;

public class WarpCommand extends CommandClass {

	public static List<UUID> teleport = new ArrayList<>();

	@Command(name = "setprotection", onlyPlayers = true, groupToUse = Group.BUILDER)
	public void onSetprotection(CommandArgs cmdArgs) {
		CommandSender s = cmdArgs.getSender();
		String[] a = cmdArgs.getArgs();

		if (a.length < 2) {
			send(s, "Use �a/setprotection [Warp] [Prote��o]�f para setar a prote��o de uma warp!");
			return;
		}

		if (!isNumeric(a[1])) {
			send(s, "Use numero como prote��o!");
			return;
		}

		Warp warp = main.getWarpManager().getWarp(a[0]);
		Integer protection = Integer.valueOf(a[1]);

		warp.setProtection(protection);

		main.getWarpManager().getConfig().set("warps." + warp.getWarpName().toLowerCase() + ".Protection",
				warp.getProtection());
		main.getWarpManager().saveConfig();

		send(s, "Voc� setou a prote��o da warp �a\"" + warp.getWarpName() + "\"�f como �a" + protection + "�f.");
	}

	@Command(name = "setarena", onlyPlayers = true, groupToUse = Group.BUILDER)
	public void onSetarena(CommandArgs cmdArgs) {
		CommandSender s = cmdArgs.getSender();
		String[] a = cmdArgs.getArgs();

		if (!(hasServerGroup(s, Group.GERENTE) || isServerGroup(s, Group.BUILDER))) {
			s.sendMessage(Configuration.SEMPERM.getMessage());
			return;
		}

		if (a.length == 0) {
			send(s, "Use �a/setarena [Numero]�f para setar um arena!");
			return;
		}

		if (!isNumeric(a[0])) {
			send(s, "Use um numero valido!");
			return;
		}

		Integer arena = Integer.valueOf(a[0]);

		if (arena > 10 || arena < 0) {
			send(s, "Use um numero valido de 1 a 10!");
			return;
		}

		Location warpLocation = cmdArgs.getPlayer().getLocation();

		main.getWarpManager().getConfig().set("arenas.A" + arena + ".World", warpLocation.getWorld().getName());
		main.getWarpManager().getConfig().set("arenas.A" + arena + ".X", warpLocation.getX());
		main.getWarpManager().getConfig().set("arenas.A" + arena + ".Y", warpLocation.getY());
		main.getWarpManager().getConfig().set("arenas.A" + arena + ".Z", warpLocation.getZ());
		main.getWarpManager().getConfig().set("arenas.A" + arena + ".Yaw", warpLocation.getYaw());
		main.getWarpManager().getConfig().set("arenas.A" + arena + ".Pitch", warpLocation.getPitch());
		main.getWarpManager().saveConfig();

		send(s, "Voc� setou a arena �a" + arena + "�f.");

		main.getKitManager().getArenas().clear();
		main.getKitManager().loadArenas();
	}

	@Command(name = "warp", onlyPlayers = true)
	public void onWarp(CommandArgs cmdArgs) {
		String[] a = cmdArgs.getArgs();
		Player p = (Player) cmdArgs.getSender();

		Account player = BukkitMain.getAccountCommon().getAccount(p.getUniqueId());

		if (player == null)
			return;

		if (teleport.contains(player.getUuid())) {
			player.sendMessage("Voc� j� est� em um teleport!");
			return;
		}

		if (a.length == 0) {
			player.sendMessage("Use �a/warp [Nome da warp]�f para se teletransportar para uma warp.");
			return;
		}

		Warp warp = main.getWarpManager().getWarp(a[0]);

		if (warp == null) {
			player.sendMessage("A warp �a\"" + a[0] + "\"�f n�o existe!");
			return;
		}

		if (warp.getWarpName().equalsIgnoreCase("Spawn"))
			if (main.getPlayerManager().getWarp(player.getUuid()).getWarpName().equalsIgnoreCase("Spawn")
					&& main.getKitManager().hasAbility(player.getUuid(), "Nenhum")) {
				player.sendMessage("Voc� j� est� no spawn!");
				return;
			}

		if (main.getPlayerManager().isCombateLog(player.getUuid())) {
			player.sendMessage("Voc� est� em combate, espere mais �a"
					+ DateUtils.getTime(main.getPlayerManager().getCombatLog(player.getUuid()))
					+ " �fpara poder teletransportar-se.");
			return;
		}

		player.sendMessage("Voc� ser� teletransportado em �a3 segundos�f para a warp �a" + warp.getWarpName() + "�f.");
		teleport.add(player.getUuid());

		new BukkitRunnable() {

			@Override
			public void run() {
				if (teleport.contains(player.getUuid())) {
					p.closeInventory();
				}
			}
		}.runTaskLater(main, 20 * 3);

		new BukkitRunnable() {

			@Override
			public void run() {
				if (teleport.contains(player.getUuid())) {
					teleport.remove(player.getUuid());
					p.closeInventory();
					player.sendMessage("Voc� foi teletransportado para �a" + warp.getWarpName() + "�f.");

					if (warp.getWarpName().equalsIgnoreCase("Spawn")) {
						PlayerJoinWarpEvent event = new PlayerJoinWarpEvent(p,
								main.getPlayerManager().getWarp(player.getUuid()), warp, false);
						main.getServer().getPluginManager().callEvent(event);

						if (!event.isCancelled()) {
							p.teleport(main.getWarpManager().getWarp("Spawn").getWarpLocation());
							main.getPlayerManager().setWarp(player.getUuid(), warp);
							main.getPlayerManager().setProtection(player.getUuid(), true);
							main.getKitManager().removeAbility(player.getUuid());
							PlayerListener.setItem(player);
						}
					} else {
						PlayerJoinWarpEvent event = new PlayerJoinWarpEvent(p,
								main.getPlayerManager().getWarp(player.getUuid()), warp, false);
						main.getServer().getPluginManager().callEvent(event);

						if (!event.isCancelled()) {
							p.teleport(warp.getWarpLocation());
							main.getPlayerManager().setWarp(player.getUuid(), warp);
							main.getPlayerManager().setProtection(player.getUuid(), true);
							main.getKitManager().removeAbility(player.getUuid());
							Scoreboarding.setScoreboard(p);
						}
					}
				}
			}
		}.runTaskLater(main, 20 * 3);
	}

	@Command(name = "spawn", onlyPlayers = true)
	public void onSpawn(CommandArgs cmdArgs) {
		cmdArgs.getPlayer().performCommand("warp spawn");
	}

	@Command(name = "1v1", onlyPlayers = true)
	public void on1v1(CommandArgs cmdArgs) {
		cmdArgs.getPlayer().performCommand("warp 1v1");
	}

	@Command(name = "setposition", aliases = { "setpos" }, onlyPlayers = true, groupToUse = Group.BUILDER)
	public void onSetposition(CommandArgs cmdArgs) {
		String[] a = cmdArgs.getArgs();
		Player p = cmdArgs.getPlayer();

		Account player = BukkitMain.getAccountCommon().getAccount(p.getUniqueId());

		if (player == null)
			return;

		if (!(player.isGroup(Group.BUILDER) || player.hasServerGroup(Group.MODPLUS))) {
			p.sendMessage(Configuration.SEMPERM.getMessage());
			return;
		}

		if (a.length == 0) {
			send(p, "Use �a/setposition [1/2]�f para setar a posi��o da 1v1!");
			return;
		}

		if (!isNumeric(a[0])) {
			send(p, "Use 1 ou 2 como posi��o!");
			return;
		}

		Warp1v1 warp = (Warp1v1) main.getWarpManager().getWarp("1v1");

		Integer position = Integer.valueOf(a[0]);

		Location warpLocation = p.getLocation();

		if (position == 1)
			warp.setFirstLocation(warpLocation);
		else
			warp.setSecondLocation(warpLocation);

		main.getWarpManager().getConfig().set("1v1.Posicao" + position + ".World", warpLocation.getWorld().getName());
		main.getWarpManager().getConfig().set("1v1.Posicao" + position + ".X", warpLocation.getX());
		main.getWarpManager().getConfig().set("1v1.Posicao" + position + ".Y", warpLocation.getY());
		main.getWarpManager().getConfig().set("1v1.Posicao" + position + ".Z", warpLocation.getZ());
		main.getWarpManager().getConfig().set("1v1.Posicao" + position + ".Yaw", warpLocation.getYaw());
		main.getWarpManager().getConfig().set("1v1.Posicao" + position + ".Pitch", warpLocation.getPitch());
		main.getWarpManager().saveConfig();

		send(p, "Voc� setou a posi��o �a" + position + "�f da 1v1.");
	}

	@Command(name = "setsumo", onlyPlayers = true, groupToUse = Group.BUILDER)
	public void setSumo(CommandArgs cmdArgs) {
		String[] a = cmdArgs.getArgs();
		Player p = cmdArgs.getPlayer();

		Account player = BukkitMain.getAccountCommon().getAccount(p.getUniqueId());

		if (player == null)
			return;

		if (!(player.isGroup(Group.BUILDER) || player.hasServerGroup(Group.MODPLUS))) {
			p.sendMessage(Configuration.SEMPERM.getMessage());
			return;
		}

		if (a.length == 0) {
			send(p, "Use �a/setsumo [1/2]�f para setar a posi��o da 1v1!");
			return;
		}

		if (!isNumeric(a[0])) {
			send(p, "Use 1 ou 2 como posi��o!");
			return;
		}

		WarpSumo warp = (WarpSumo) main.getWarpManager().getWarp("Sumo");

		Integer position = Integer.valueOf(a[0]);

		Location warpLocation = p.getLocation();

		if (position == 1)
			warp.setFirstLocation(warpLocation);
		else
			warp.setSecondLocation(warpLocation);

		main.getWarpManager().getConfig().set("Sumo.Posicao" + position + ".World", warpLocation.getWorld().getName());
		main.getWarpManager().getConfig().set("Sumo.Posicao" + position + ".X", warpLocation.getX());
		main.getWarpManager().getConfig().set("Sumo.Posicao" + position + ".Y", warpLocation.getY());
		main.getWarpManager().getConfig().set("Sumo.Posicao" + position + ".Z", warpLocation.getZ());
		main.getWarpManager().getConfig().set("Sumo.Posicao" + position + ".Yaw", warpLocation.getYaw());
		main.getWarpManager().getConfig().set("Sumo.Posicao" + position + ".Pitch", warpLocation.getPitch());
		main.getWarpManager().saveConfig();

		send(p, "Voc� setou a posi��o �a" + position + "�f da Sumo.");
	}

	@Command(name = "setwarp", onlyPlayers = true, groupToUse = Group.BUILDER)
	public void onSetWarp(CommandArgs cmdArgs) {
		String[] a = cmdArgs.getArgs();
		Player p = cmdArgs.getPlayer();

		Account player = BukkitMain.getAccountCommon().getAccount(p.getUniqueId());

		if (player == null)
			return;

		if (!(player.isGroup(Group.BUILDER) || player.hasServerGroup(Group.MODPLUS))) {
			p.sendMessage(Configuration.SEMPERM.getMessage());
			return;
		}

		if (a.length == 0) {
			send(p, "Use �a/setwarp [Warp]�f para setar uma warp!");
			return;
		}

		if (a[0].equalsIgnoreCase("ss") || a[0].equalsIgnoreCase("screenshare")) {
			Warp warp = main.getWarpManager().getScreenshare();

			Location warpLocation = p.getLocation();

			warp.setWarpLocation(warpLocation);

			main.getWarpManager().getConfig().set("ss.World", warpLocation.getWorld().getName());
			main.getWarpManager().getConfig().set("ss.X", warpLocation.getX());
			main.getWarpManager().getConfig().set("ss.Y", warpLocation.getY());
			main.getWarpManager().getConfig().set("ss.Z", warpLocation.getZ());
			main.getWarpManager().saveConfig();

			player.sendMessage("Voc� alterou a localiza��o da warp �a" + warp.getWarpName() + "�f para �aX:"
					+ warp.getWarpLocation().getX() + "�f, �a" + warp.getWarpLocation().getY() + "�f, �a"
					+ warp.getWarpLocation().getZ() + "�f.");
			return;
		}

		Warp warp = main.getWarpManager().getWarp(a[0]);

		if (warp == null) {
			player.sendMessage("A warp �a\"" + a[0] + "\"�f n�o existe!");
			return;
		}

		Location warpLocation = p.getLocation();

		warp.setWarpLocation(warpLocation);

		if (warp.getWarpName().equalsIgnoreCase("1v1")) {
			main.getWarpManager().getConfig().set("1v1.Spawn.World", warpLocation.getWorld().getName());
			main.getWarpManager().getConfig().set("1v1.Spawn.X", warpLocation.getX());
			main.getWarpManager().getConfig().set("1v1.Spawn.Y", warpLocation.getY());
			main.getWarpManager().getConfig().set("1v1.Spawn.Z", warpLocation.getZ());
			main.getWarpManager().getConfig().set("1v1.Spawn.Yaw", warpLocation.getYaw());
			main.getWarpManager().getConfig().set("1v1.Spawn.Pitch", warpLocation.getPitch());
			main.getWarpManager().saveConfig();
		} else if (warp.getWarpName().equalsIgnoreCase("Sumo")) {
			main.getWarpManager().getConfig().set("Sumo.Spawn.World", warpLocation.getWorld().getName());
			main.getWarpManager().getConfig().set("Sumo.Spawn.X", warpLocation.getX());
			main.getWarpManager().getConfig().set("Sumo.Spawn.Y", warpLocation.getY());
			main.getWarpManager().getConfig().set("Sumo.Spawn.Z", warpLocation.getZ());
			main.getWarpManager().getConfig().set("Sumo.Spawn.Yaw", warpLocation.getYaw());
			main.getWarpManager().getConfig().set("Sumo.Spawn.Pitch", warpLocation.getPitch());
			main.getWarpManager().saveConfig();
		} else {
			main.getWarpManager().getConfig().set("warps." + warp.getWarpName().toLowerCase() + ".World",
					warpLocation.getWorld().getName());
			main.getWarpManager().getConfig().set("warps." + warp.getWarpName().toLowerCase() + ".X",
					warpLocation.getX());
			main.getWarpManager().getConfig().set("warps." + warp.getWarpName().toLowerCase() + ".Y",
					warpLocation.getY());
			main.getWarpManager().getConfig().set("warps." + warp.getWarpName().toLowerCase() + ".Z",
					warpLocation.getZ());
			main.getWarpManager().getConfig().set("warps." + warp.getWarpName().toLowerCase() + ".Yaw",
					warpLocation.getYaw());
			main.getWarpManager().getConfig().set("warps." + warp.getWarpName().toLowerCase() + ".Pitch",
					warpLocation.getPitch());
			main.getWarpManager().saveConfig();
		}

		player.sendMessage("Voc� alterou a localiza��o da warp �a" + warp.getWarpName() + "�f para �aX:"
				+ warp.getWarpLocation().getX() + "�f, �a" + warp.getWarpLocation().getY() + "�f, �a"
				+ warp.getWarpLocation().getZ() + "�f.");
	}

}
