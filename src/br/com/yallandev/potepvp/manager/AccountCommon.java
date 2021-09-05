package br.com.yallandev.potepvp.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;

import br.com.yallandev.potepvp.BukkitMain;
import br.com.yallandev.potepvp.account.Account;
import br.com.yallandev.potepvp.permissions.group.Group;

public class AccountCommon {

	private HashMap<UUID, Account> players;

	public AccountCommon() {
		this.players = new HashMap<>();
	}

	public Account loadAccount(UUID uuid) {
		if (!BukkitMain.getConnection().isConnected()) {
			BukkitMain.getConnection().tryConnection();
		}

		try {
			Connection connection = BukkitMain.getConnection().getConnection();
			PreparedStatement stmt = connection
					.prepareStatement("SELECT * FROM `account` WHERE `Uuid`='" + uuid.toString() + "';");
			ResultSet result = stmt.executeQuery();
			if (result.next()) {
				Account player = BukkitMain.getInstance().getGson().fromJson(result.getString("json"), Account.class);
				loadAccount(uuid, player);
			}
			result.close();
			result = null;
			stmt = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return getAccount(uuid);
	}

	public Account loadAccount(UUID uuid, boolean value) {
		if (!BukkitMain.getConnection().isConnected()) {
			BukkitMain.getConnection().tryConnection();
		}

		Account player = null;
		try {
			Connection connection = BukkitMain.getConnection().getConnection();
			PreparedStatement stmt = connection
					.prepareStatement("SELECT * FROM `account` WHERE `Uuid`='" + uuid.toString() + "';");
			ResultSet result = stmt.executeQuery();
			if (result.next()) {
				player = BukkitMain.getInstance().getGson().fromJson(result.getString("json"), Account.class);
			}
			result.close();
			result = null;
			stmt = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return player;
	}

	public UUID getUUID(String userName) {
		if (!BukkitMain.getConnection().isConnected()) {
			BukkitMain.getConnection().tryConnection();
		}

		System.out.println("Pegando o uuid do " + userName);
		try {
			Connection connection = BukkitMain.getConnection().getConnection();
			PreparedStatement stmt = connection
					.prepareStatement("SELECT * FROM `players` WHERE `Name`='" + userName.toLowerCase() + "';");
			ResultSet result = stmt.executeQuery();
			if (result.next()) {
				UUID uuid = UUID.fromString(result.getString("Uuid"));
				System.out.println("UUID do " + userName + " -> " + uuid.toString());
				return uuid;
			}
			result.close();
			result = null;
			stmt = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("Uuid nao encontrado!");

		return null;
	}

	public void loadAccount(UUID uuid, Account player) {
		if (this.players.containsKey(uuid)) {
			return;
		}
		this.players.put(uuid, player);
	}

	public Account getAccount(UUID uuid) {
		if (!this.players.containsKey(uuid)) {
			return null;
		}
		this.players.get(uuid).updateCache();
		return this.players.get(uuid);
	}

	public void unloadAccount(UUID uuid) {
		if (this.players.containsKey(uuid)) {
			this.players.remove(uuid);
		} else {
			Bukkit.getLogger().log(Level.SEVERE, "NAO FOI POSSIVEL ENCONTRAR!");
		}
	}

	public void saveAccount(Account player) {
		String json = BukkitMain.getInstance().getGson().toJson(player);

		try {
			PreparedStatement stmt = BukkitMain.getConnection().prepareStatment(
					"INSERT INTO `account`(`uuid`, `json`) VALUES (?, ?) ON DUPLICATE KEY UPDATE `json` = ?;");
			stmt.setString(1, player.getUuid().toString());
			stmt.setString(2, json);
			stmt.setString(3, json);
			stmt.executeUpdate();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		try {
			PreparedStatement stmt = BukkitMain.getConnection().prepareStatment(
					"INSERT INTO `players`(`name`, `uuid`) VALUES (?, ?) ON DUPLICATE KEY UPDATE `uuid` = ?;");
			stmt.setString(1, player.getUserName().toLowerCase());
			stmt.setString(2, player.getUuid().toString());
			stmt.setString(3, player.getUuid().toString());
			stmt.executeUpdate();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		player.saveStatus();
	}

	public Collection<Account> getPlayers() {
		return this.players.values();
	}

	public HashMap<UUID, Account> asMap() {
		return players;
	}

//	public boolean isAlertEnable(UUID uuid) {
//		Account player = getAccount(uuid);
//		
//		if (player == null)
//			return false;
//		
//		return player.getConfiguration().isAlertEnable();
//	}

	public Group getGroup(UUID uuid) {
		Account player = getAccount(uuid);

		if (player == null)
			return Group.MEMBRO;

		return player.getServerGroup();
	}

	public boolean hasGroup(UUID uuid, Group group) {
		Account player = getAccount(uuid);

		if (player == null)
			return false;

		return player.hasServerGroup(group);
	}

	public boolean hasPermission(UUID uuid, String permission) {
		Account player = getAccount(uuid);

		if (player == null)
			return false;

		return player.hasPermission(permission);
	}

	public boolean isGroup(UUID uuid, Group group) {
		Account player = getAccount(uuid);

		if (player == null)
			return false;

		return player.getServerGroup() == group;
	}
}
