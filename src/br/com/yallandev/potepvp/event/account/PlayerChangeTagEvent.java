package br.com.yallandev.potepvp.event.account;

import org.bukkit.entity.Player;

import br.com.yallandev.potepvp.event.PlayerCancellableEvent;
import br.com.yallandev.potepvp.tag.Tag;

public class PlayerChangeTagEvent extends PlayerCancellableEvent {

	private Tag oldTag;
	private Tag newTag;
	private boolean isForced;

	public PlayerChangeTagEvent(Player p, Tag oldTag, Tag newTag, boolean isForced) {
		super(p);
		this.oldTag = oldTag;
		this.newTag = newTag;
		this.isForced = isForced;
	}

	public Tag getNewTag() {
		return newTag;
	}

	public Tag getOldTag() {
		return oldTag;
	}

	public boolean isForced() {
		return isForced;
	}

}
