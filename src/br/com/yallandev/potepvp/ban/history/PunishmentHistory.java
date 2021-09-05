package br.com.yallandev.potepvp.ban.history;

import java.util.ArrayList;
import java.util.List;

import br.com.yallandev.potepvp.ban.constructor.Ban;
import br.com.yallandev.potepvp.ban.constructor.Kick;
import br.com.yallandev.potepvp.ban.constructor.Mute;

public class PunishmentHistory {

	private List<Ban> banHistory;
	private List<Mute> muteHistory;
	private List<Kick> kickHistory;

	public PunishmentHistory() {
		this.banHistory = new ArrayList<>();
		this.muteHistory = new ArrayList<>();
		this.kickHistory = new ArrayList<>();
	}

	public Ban getActualBan() {
		for (Ban ban : this.banHistory) {
			if (!ban.isUnbanned()) {
				if (!ban.hasExpired()) {
					return ban;
				}
			}
		}
		return null;
	}

	public Mute getActualMute() {
		for (Mute mute : this.muteHistory) {
			if (!mute.isUnmuted()) {
				if (!mute.hasExpired()) {
					return mute;
				}
			}
		}
		return null;
	}

	public List<Kick> getKickHistory() {
		return kickHistory;
	}

	public List<Ban> getBanHistory() {
		return this.banHistory;
	}

	public List<Mute> getMuteHistory() {
		return this.muteHistory;
	}
}
