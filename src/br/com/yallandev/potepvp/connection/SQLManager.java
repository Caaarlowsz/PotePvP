package br.com.yallandev.potepvp.connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.yallandev.potepvp.BukkitMain;

/**
 *
 * @author netindev
 *
 */
public class SQLManager {

	public SQLManager() {
		try {
			BukkitMain.getConnection().updateSQL(
					"CREATE TABLE IF NOT EXISTS `player_data` (`player` varchar(20), `status` varchar(7), `password` varchar(65))");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public enum Status {
		CRACKED, PREMIUM
	}

	public String getPassword(String playerName) {
		try {
			if (this.hasOnDatabase(playerName)) {
				PreparedStatement statement = BukkitMain.getConnection().getConnection()
						.prepareStatement("SELECT * FROM `player_data` WHERE `player`='" + playerName + "';");
				ResultSet set = statement.executeQuery();
				if (!set.next()) {
					return null;
				}
				return set.getString("password");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setStatus(String playerName, Status status) {
		try {
			PreparedStatement statement = BukkitMain.getConnection().getConnection()
					.prepareStatement("INSERT INTO `player_data` (`player`, `status`) VALUES ('" + playerName + "', '"
							+ status.toString().toLowerCase() + "');");
			statement.execute();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updatePassword(String playerName, String password) {
		try {
			PreparedStatement statement = BukkitMain.getConnection().getConnection().prepareStatement(
					"UPDATE `player_data` SET password = '" + password + "' WHERE player = '" + playerName + "';");
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setPasswordAndStatus(String playerName, Status status, String password) {
		try {
			PreparedStatement statement = BukkitMain.getConnection().getConnection()
					.prepareStatement("INSERT INTO `player_data` (`player`, `status`, `password`) VALUES ('"
							+ playerName + "', '" + status.toString().toLowerCase() + "', '" + password + "');");
			statement.execute();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean hasOnDatabase(String playerName) {
		try {
			PreparedStatement statement = BukkitMain.getConnection().getConnection()
					.prepareStatement("SELECT * FROM `player_data` WHERE player = '" + playerName + "';");
			ResultSet set = statement.executeQuery();
			return set.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
