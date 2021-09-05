package br.com.yallandev.potepvp.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;

import br.com.yallandev.potepvp.BukkitMain;
import br.com.yallandev.potepvp.BukkitMain.Configuration;
import br.com.yallandev.potepvp.manager.WarpManager.Warp;
import br.com.yallandev.potepvp.status.Status;

public class PlayerManager {

	private BukkitMain main;
	private HashMap<UUID, Warp> playerWarp;
//	private HashMap<String, Kit> playerKit;
	private HashMap<UUID, Status> playerStatus;
	private HashMap<UUID, Long> playerCombatLog;
	private HashMap<UUID, Boolean> playerProtection;
	private List<UUID> staffchat;
	private HashMap<UUID, UUID> screenshare;

	public PlayerManager() {
		this.main = BukkitMain.getInstance();
		this.playerWarp = new HashMap<>();
		this.playerStatus = new HashMap<>();
		this.playerCombatLog = new HashMap<>();
		this.playerProtection = new HashMap<>();
		this.staffchat = new ArrayList<>();
		this.screenshare = new HashMap<>();
	}

	public HashMap<UUID, UUID> getScreenshare() {
		return screenshare;
	}

	public void removeScreenshare(UUID uuid) {
		screenshare.remove(uuid);
	}

	public boolean isScreenshare(UUID uuid) {
		return screenshare.containsKey(uuid);
	}

	public UUID getScreenshareModerator(UUID uuid) {
		return screenshare.get(uuid);
	}

	public List<UUID> getStaffchat() {
		return staffchat;
	}

	public boolean isSomeWarp(UUID uuid) {
		if (!playerWarp.containsKey(uuid))
			playerWarp.put(uuid, main.getWarpManager().getWarp("Spawn"));

		return !playerWarp.get(uuid).getWarpName().equalsIgnoreCase("Spawn");
	}

	public boolean isInWarp(UUID uuid, String warpName) {
		if (!playerWarp.containsKey(uuid))
			playerWarp.put(uuid, main.getWarpManager().getWarp("Spawn"));

		return playerWarp.get(uuid).getWarpName().equalsIgnoreCase(warpName);
	}

	public Warp getWarp(UUID uuid) {
		if (!playerWarp.containsKey(uuid))
			playerWarp.put(uuid, main.getWarpManager().getWarp("Spawn"));

		return playerWarp.get(uuid);
	}

	public void setWarp(UUID uuid, Warp warp) {
		playerWarp.put(uuid, warp);
		main.debug("Mudando a warp do player " + uuid + " para " + warp.getWarpName());
	}

	public void setWarp(UUID uuid, String warpName) {
		playerWarp.put(uuid, main.getWarpManager().getWarp(warpName));

		main.debug("Mudando a warp do player " + uuid + " para " + warpName);
	}

	public Status loadStatus(UUID uuid) {
		if (!BukkitMain.getConnection().isConnected()) {
			BukkitMain.getConnection().tryConnection();
		}
		try {
			Connection connection = BukkitMain.getConnection().getConnection();
			PreparedStatement stmt = connection
					.prepareStatement("SELECT * FROM `kitpvp_status` WHERE `Uuid`='" + uuid.toString() + "';");
			ResultSet result = stmt.executeQuery();
			if (result.next()) {
				Status player = new Status(uuid, result.getInt("kills"), result.getInt("deaths"),
						result.getInt("killstreak"), result.getInt("money"), result.getInt("xp"),
						result.getInt("highestKS"));
				loadStatus(uuid, player);
			} else {
				loadStatus(uuid, new Status(uuid, 0, 0, 0, 0, 0, 0));
			}
			result.close();
			result = null;
			stmt = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return getStatus(uuid);
	}

	public Status loadStatus(UUID uuid, boolean value) {
		Status status = null;

		try {
			Connection connection = BukkitMain.getConnection().getConnection();
			PreparedStatement stmt = connection
					.prepareStatement("SELECT * FROM `kitpvp_status` WHERE `Uuid`='" + uuid.toString() + "';");
			ResultSet result = stmt.executeQuery();

			if (result.next()) {
				status = new Status(uuid, result.getInt("kills"), result.getInt("deaths"), result.getInt("killstreak"),
						result.getInt("money"), result.getInt("xp"), result.getInt("highestKS"));
			}

			result.close();
			result = null;
			stmt = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return status;
	}

	public Status loadStatus(UUID uuid, Status status) {
		if (this.playerStatus.containsKey(uuid))
			return this.playerStatus.get(uuid);

		return this.playerStatus.put(uuid, status);
	}

	public Status getStatus(UUID uuid) {
		if (this.playerStatus.containsKey(uuid))
			return this.playerStatus.get(uuid);

		return null;
	}

	public void saveStatus(UUID uuid, Status status) {
		if (!BukkitMain.getConnection().isConnected()) {
			BukkitMain.getConnection().tryConnection();
		}

		try {
			PreparedStatement stmt = BukkitMain.getConnection().prepareStatment(
					"INSERT INTO `kitpvp_status`(`Uuid`, `kills`, `deaths`, `killstreak`, `money`, `xp`, `highestKS`) VALUES (?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE `kills`=VALUES(`kills`), `deaths`=VALUES(`deaths`), `killstreak`=VALUES(`killstreak`), `money`=VALUES(`money`), `xp`=VALUES(`xp`), `highestKS`=VALUES(`highestKS`);");
			stmt.setString(1, uuid.toString());
			stmt.setInt(2, status.getKills());
			stmt.setInt(3, status.getDeaths());
			stmt.setInt(4, status.getKillstreak());
			stmt.setInt(5, status.getMoney());
			stmt.setInt(6, status.getXp());
			stmt.setInt(7, status.getHighestKillstreak());
			stmt.executeUpdate();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public void removePlayer(UUID uuid) {
		playerWarp.remove(uuid);
		playerCombatLog.remove(uuid);
		playerProtection.remove(uuid);
		playerStatus.remove(uuid);
		main.getKitManager().removeAbility(uuid);

		main.debug("Removendo o jogador " + uuid);
	}

	public boolean isCombateLog(UUID uuid) {
		if (playerCombatLog.containsKey(uuid))
			return playerCombatLog.get(uuid) > System.currentTimeMillis();

		return false;
	}

	public void setCombatLog(UUID uuid, int time) {
		setCombatLog(uuid, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(time));
	}

	public void setCombatLog(UUID uuid, long time) {
		if (isCombateLog(uuid))
			return;

		playerCombatLog.put(uuid, time);
	}

	public long getCombatLog(UUID uuid) {
		return playerCombatLog.containsKey(uuid) ? playerCombatLog.get(uuid) : -1l;
	}

	public void removeCombatLog(UUID uuid) {
		playerCombatLog.remove(uuid);
	}

	public boolean isProtected(UUID uuid) {
		if (playerProtection.containsKey(uuid))
			return playerProtection.get(uuid);

		playerProtection.put(uuid, true);
		return true;
	}

	public void setProtection(UUID uuid, Boolean value) {
		playerProtection.put(uuid, value);

		if (Bukkit.getPlayer(uuid) != null) {
			if (value)
				Bukkit.getPlayer(uuid)
						.sendMessage(Configuration.PREFIX.getMessage() + "Voc� recebeu a prote��o do spawn!");
			else
				Bukkit.getPlayer(uuid).sendMessage(Configuration.PREFIX.getMessage() + "Sua prote��o foi retirada!");
		}
	}

	public void removeProtection(UUID uniqueId) {
		setProtection(uniqueId, false);
	}

	public void addProtection(UUID uniqueId) {
		setProtection(uniqueId, true);
	}

}
