package br.com.yallandev.potepvp.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.jdbc.CommunicationsException;

public class MySQL {

	private String host;
	private int port;
	private String username;
	private String password;
	private String database;
	private Connection connection;

	public MySQL(String host, String username, String password, String database) {
		this.host = host;
		this.port = 3306;
		this.username = username;
		this.password = password;
		this.database = database;

		this.connection = tryConnection();
	}

	public Connection tryConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			System.out.println("[DEBUG] Conectando se ao MySQL da host!");
			return DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?user="
					+ username + "&password=" + password + "");
		} catch (CommunicationsException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public void createTable() {
		System.out.println("[DEBUG] Criando tabelas!");
		SQLQuerySync("CREATE TABLE IF NOT EXISTS `account` (`Uuid` VARCHAR(128) PRIMARY KEY, `json` VARCHAR(8192));");
		SQLQuerySync(
				"CREATE TABLE IF NOT EXISTS `clan` (`ClanName` VARCHAR(128) PRIMARY KEY, `ClanTag` VARCHAR(128), `json` VARCHAR(8192));");
		SQLQuerySync(
				"CREATE TABLE IF NOT EXISTS `clan_status` (`ClanName` VARCHAR(128) PRIMARY KEY, `kills` INT, `deaths` INT, `xp` INT, `money` INT);");
		SQLQuerySync(
				"CREATE TABLE IF NOT EXISTS `cheater_account` (`Uuid` VARCHAR(128) PRIMARY KEY, `json` VARCHAR(2048));");
		SQLQuerySync(
				"CREATE TABLE IF NOT EXISTS `kitpvp_status` (`Uuid` VARCHAR(128) PRIMARY KEY, `kills` INT, `deaths` INT, `killstreak` INT, `money` INT, `xp` INT, `highestKS` INT);");
		SQLQuerySync("CREATE TABLE IF NOT EXISTS `players` (`Name` VARCHAR(128) PRIMARY KEY, `Uuid` VARCHAR(128));");
	}

	public void SQLQuerySync(String sql) {
		try {
			Statement stmt = getConnection().createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean isConnected() {
		try {
			if (connection == null)
				return false;
			if (connection.isClosed())
				return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public Connection getConnection() {
		if (!isConnected()) {
			tryConnection();
		}
		return connection;
	}

	public PreparedStatement prepareStatment(String sql)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		return getConnection().prepareStatement(sql);
	}

	public int updateSQL(String query) throws SQLException, ClassNotFoundException {
		if (!isConnected()) {
			this.tryConnection();
		}

		Statement statement = this.connection.createStatement();
		int result = statement.executeUpdate(query);

		return result;
	}

}
