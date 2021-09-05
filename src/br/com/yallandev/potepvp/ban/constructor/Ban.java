package br.com.yallandev.potepvp.ban.constructor;

import br.com.yallandev.potepvp.BukkitMain.Configuration;
import br.com.yallandev.potepvp.utils.DateUtils;

public class Ban {

	private String bannedBy;
	private String reason;

	private long banTime;
	private long expire;
	private long duration;

	private boolean unbanned;
	private String unbannedBy;
	private long unbanTime;
	private String unbanReason;

	public Ban(String bannedBy, String reason, long expire, long duration) {
		this.bannedBy = bannedBy;
		this.reason = reason;
		this.banTime = System.currentTimeMillis();
		this.expire = expire;
		this.duration = duration;
	}

	public Ban(String bannedBy, String reason, long expire) {
		this(bannedBy, reason, expire, System.currentTimeMillis() - expire);
	}

	public Ban(String bannedBy, String reason) {
		this(bannedBy, reason, -1, System.currentTimeMillis());
	}

	public String getBannedBy() {
		return bannedBy;
	}

	public String getReason() {
		return reason;
	}

	public long getBanTime() {
		return banTime;
	}

	public long getExpire() {
		return expire;
	}

	public long getDuration() {
		return duration;
	}

	public boolean isPermanent() {
		return this.expire == -1l;
	}

	public void unban(String unbannedBy, String unbanReason) {
		this.unbanned = true;
		this.unbannedBy = unbannedBy;
		this.unbanTime = System.currentTimeMillis();
		this.unbanReason = unbanReason;
	}

	public void unban(String reason) {
		this.unban("CONSOLE", reason);
	}

	public String getUnbannedBy() {
		return unbannedBy;
	}

	public long getUnbanTime() {
		return unbanTime;
	}

	public boolean isUnbanned() {
		return unbanned;
	}

	public boolean hasExpired() {
		return (this.expire != -1L) && (this.expire < System.currentTimeMillis());
	}

	public String getUnbanReason() {
		return unbanReason;
	}

	public String getMessage() {
		if (isPermanent()) {
			return "�cSua conta foi suspensa permanentemente!\nMotivo do banimento: " + getReason()
					+ "\n\nAdquira UNBAN em �n" + Configuration.SITE.getMessage() + "�c"
					+ "\nPara solicitar appeal entre �nhttps://discord.gg/ZNA89EZ";
		}
		return "�cSua conta foi suspensa temporariamente!\nMotivo do banimento: " + getReason() + "\nExpire em "
				+ DateUtils.getTime(getExpire());
	}

}
