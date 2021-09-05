package br.com.yallandev.potepvp.ranking;

public enum Ranking {

	UNRANKED(100, "§f", "-"), INICIANTE(300, "§a", "="), AVANCADO(550, "§e", "☲"), EXPERIENTE(800, "§1", "☷"),
	VETERANO(1200, "§5", "✹"), PRATA(1500, "§7", "*"), OURO(2200, "§6", "✻"), DIAMANTE(3000, "§b", "✦"),
	ESMERALDA(3500, "§2", "✢"), CRISTAL(4300, "§9", "✼"), SAFIRA(5700, "§3", "❁"), POTTER(Integer.MAX_VALUE, "§8", "❆");

	private int xp;
	private String color;
	private String icon;

	private Ranking(int xp, String color, String icon) {
		this.xp = xp;
		this.color = color;
		this.icon = icon;
	}

	public int getXp() {
		return xp;
	}

	public String getColor() {
		return color;
	}

	public String getIcon() {
		return icon;
	}
}
