package br.com.yallandev.potepvp.listener.umvum;

import org.bukkit.entity.Player;

public class Desafio {

	private Player desafiante;
	private Player desafiado;
	private String espada;
	private String armadura;
	private boolean refil;
	private boolean speed;
	private boolean strenght;
	private boolean sharp;
	private boolean normal;
	private long expire;

	public Desafio(Player desafiante, Player desafiado) {
		this.desafiante = desafiante;
		this.desafiado = desafiado;
		if (true) {
			this.espada = "DIAMOND_SWORD";
			this.armadura = "IRON_CHESTPLATE";
		}
		this.refil = false;
		this.speed = false;
		this.strenght = false;
		this.sharp = true;
		this.normal = true;
		this.expire = (System.currentTimeMillis() + 10000L);
	}

	public Desafio(Player desafiante, Player desafiado, String espada, String armadura, boolean refil, boolean speed,
			boolean strenght, boolean sharp) {
		this.desafiante = desafiante;
		this.desafiado = desafiado;
		this.espada = espada;
		this.armadura = armadura;
		this.refil = refil;
		this.speed = speed;
		this.strenght = strenght;
		this.sharp = sharp;
		this.normal = false;
		this.expire = (System.currentTimeMillis() + 20000L);
	}

	public Player getDesafiante() {
		return this.desafiante;
	}

	public Player getDesafiado() {
		return this.desafiado;
	}

	public String getSwordType() {
		return this.espada;
	}

	public String getArmorType() {
		return this.armadura;
	}

	public boolean isRefill() {
		return this.refil;
	}

	public boolean isSpeed() {
		return this.speed;
	}

	public boolean isStreght() {
		return this.strenght;
	}

	public boolean isNormal() {
		return this.normal;
	}

	public boolean hasSharp() {
		return this.sharp;
	}

	public boolean hasExpire() {
		return this.expire < System.currentTimeMillis();
	}

	public long getExpire() {
		return expire;
	}
}
