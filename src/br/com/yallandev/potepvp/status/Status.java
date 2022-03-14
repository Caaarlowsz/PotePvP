package br.com.yallandev.potepvp.status;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.caaarlowsz.potemc.kitpvp.PotePvP;
import com.github.caaarlowsz.potemc.kitpvp.PotePvP.Configuration;
import br.com.yallandev.potepvp.tag.Tag;

public class Status {

	private UUID uuid;
	private int kills;
	private int deaths;
	private int killstreak;
	private int money;
	private int highestKillstreak;
	private int xp;
	private long time;
	private long timedeaths;
	private int rank;
	private int deathrank;

	public Status(UUID uuid, int kills, int deaths, int killstreak, int money, int xp, int highestKillstreak) {
		this.uuid = uuid;
		this.kills = kills;
		this.deaths = deaths;
		this.killstreak = killstreak;
		this.money = money;
		this.xp = xp;
		this.highestKillstreak = highestKillstreak;
		this.time = -1l;
		this.timedeaths = -1l;
		this.rank = getRank();
		this.rank = getRank();
	}

	public int getKills() {
		return kills;
	}

	public void addKills(int kills) {
		this.kills += kills;
	}

	public void removeKills(int kills) {
		this.kills -= kills;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public int getDeaths() {
		return deaths;
	}

	public void addDeaths(int deaths) {
		this.deaths += deaths;
	}

	public void removeDeaths(int deaths) {
		this.deaths -= deaths;
	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	public int getKillstreak() {
		return killstreak;
	}

	public void addKillstreak(int killstreak) {
		this.killstreak += killstreak;
		checkKillStreak();
	}

	public void removeKillstreak(int killstreak) {
		this.killstreak -= killstreak;
		checkKillStreak();
	}

	public void setKillstreak(int killstreak) {
		this.killstreak = killstreak;
		checkKillStreak();
	}

	public int getMoney() {
		if (money < 0)
			money = 0;
		return money;
	}

	public void addMoney(int money) {
		this.money += money;
	}

	public void removeMoney(int money) {
		this.money -= money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getXp() {
		if (xp < 0)
			xp = 0;

		return xp;
	}

	public void addXp(int xp) {
		this.xp += xp;
	}

	public void removeXp(int xp) {
		this.xp -= xp;
	}

	public void setXp(int xp) {
		this.xp = xp;
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(uuid);
	}

	public int getRank() {
		if (time > System.currentTimeMillis())
			return this.rank;

		time = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(30);

		try {
			Statement e = PotePvP.getConnection().getConnection().createStatement();
			ResultSet rs = e.executeQuery("SELECT COUNT(*) from `kitpvp_status` WHERE `Kills` > " + this.kills);
			rs.next();

			int rank = Integer.valueOf(rs.getInt(1) + 1);

			if (rank == 1) {
				if (this.rank != 1) {
					if (getPlayer() != null)
						getPlayer().sendMessage(Configuration.PREFIX.getMessage() + "Voc� alcan�ou o �2�lTOP 1�f!");
				}
			} else if (this.rank == 1) {
				if (rank != 1) {
					if (getPlayer() != null) {
						getPlayer().sendMessage(Configuration.PREFIX.getMessage() + "Voc� n�o � mais �2�lTOP 1�f!");

						PotePvP.getAccountCommon().getAccount(uuid).setTag(
								Tag.valueOf(PotePvP.getAccountCommon().getAccount(uuid).getServerGroup().name()));
					}
				}
			}
			this.rank = rank;

			return rank;
		} catch (SQLException var3) {
			var3.printStackTrace();
			return Integer.valueOf(10000);
		}
	}

	public int getRankDeath() {
		if (timedeaths > System.currentTimeMillis())
			return this.deathrank;

		timedeaths = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(30);

		try {
			Statement e = PotePvP.getConnection().getConnection().createStatement();
			ResultSet rs = e.executeQuery("SELECT COUNT(*) from `kitpvp_status` WHERE `Deaths` > " + this.deaths);
			rs.next();

			int rank = Integer.valueOf(rs.getInt(1) + 1);

			if (rank == 1) {
				if (this.deathrank != 1) {
					if (getPlayer() != null)
						getPlayer().sendMessage(Configuration.PREFIX.getMessage() + "Voc� � o �8�lTOP 1�f de deaths!");
				}
			} else if (this.deathrank == 1) {
				if (rank != 1) {
					if (getPlayer() != null) {
						getPlayer().sendMessage(
								Configuration.PREFIX.getMessage() + "Voc� n�o � mais o �8�lTOP 1�f de deaths!");

						PotePvP.getAccountCommon().getAccount(uuid).setTag(
								Tag.valueOf(PotePvP.getAccountCommon().getAccount(uuid).getServerGroup().name()));
					}
				}
			}
			this.deathrank = rank;

			return rank;
		} catch (SQLException var3) {
			var3.printStackTrace();
			return Integer.valueOf(10000);
		}
	}

	public int getHighestKillstreak() {
		return highestKillstreak;
	}

	public void checkKillStreak() {
		if (this.killstreak >= highestKillstreak)
			this.highestKillstreak = this.killstreak;
	}
}
