package br.com.yallandev.potepvp.ban.constructor;

public class Mute {
	
	private String mutedBy;
	private String reason;
	private long muteTime;
	private long expire;
	private long duration;
	
	private boolean unmuted;
	private long unmutedTime;
	private String unmutedBy;
	
	public Mute(String mutedBy, String reason, long expire) {
		this.mutedBy = mutedBy;
		this.reason = reason;
		this.expire = expire;
		this.duration = System.currentTimeMillis() - expire;
		this.muteTime = System.currentTimeMillis();
		
		this.unmuted = false;
		this.unmutedTime = -1l;
	}
	
	public Mute(String bannedBy, String reason) {
		this(bannedBy, reason, -1l);
	}
	
	public String getMutedBy() {
		return mutedBy;
	}
	
	public String getReason() {
		return reason;
	}
	
	public long getMuteTime() {
		return muteTime;
	}
	
	public long getExpire() {
		return expire;
	}
	
	public boolean hasExpired() {
		return (!isPermanent()) && (this.expire < System.currentTimeMillis());
	}
	
	public boolean isPermanent() {
		return this.expire == -1l;
	}
	
	public long getDuration() {
		return duration;
	}
	
	public void unmute() {
		unmute("CONSOLE");
	}
	
	public void unmute(String unbannedBy) {
		this.unmuted = true;
		this.unmutedBy = unbannedBy;
		this.unmutedTime = System.currentTimeMillis();
	}
	
	public boolean isUnmuted() {
		return unmuted;
	}
	
	public String getUnmutedBy() {
		return unmutedBy;
	}
	
	public long getUnmutedTime() {
		return unmutedTime;
	}
	
	public String getMessage() {
		return "§cVocê está mutado pelo motivo " + getReason();
	}

}
