package br.com.yallandev.potepvp.clan;

public class ClanStatus {

	private int kills;
	private int deaths;
	private int xp;
	private int money;

	public ClanStatus(String clanName, int kills, int deaths, int xp, int money) {
		this.kills = kills;
		this.deaths = deaths;
		this.xp = xp;
		this.money = money;
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

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}
}
