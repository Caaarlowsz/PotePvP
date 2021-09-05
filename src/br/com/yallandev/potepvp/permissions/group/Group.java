package br.com.yallandev.potepvp.permissions.group;

import java.util.List;

import br.com.yallandev.potepvp.permissions.group.groups.AdminGroup;
import br.com.yallandev.potepvp.permissions.group.groups.GroupInterface;
import br.com.yallandev.potepvp.permissions.group.groups.ModerationGroup;
import br.com.yallandev.potepvp.permissions.group.groups.OwnerGroup;
import br.com.yallandev.potepvp.permissions.group.groups.SimpleGroup;
import br.com.yallandev.potepvp.permissions.group.groups.YoutuberGroup;

public enum Group {

	MEMBRO, LIGHT, MVP, PRO, BETA, HEIGHT, YOUTUBER, BUILDER(new ModerationGroup()), HELPER,
	YOUTUBERPLUS(new YoutuberGroup()), TRIAL(new ModerationGroup()), MOD(new ModerationGroup()),
	MODGC(new ModerationGroup()), MODPLUS(new ModerationGroup()), GERENTE(new AdminGroup()), ADMIN(new AdminGroup()),
	DONO(new OwnerGroup()), DEVELOPER(new OwnerGroup());

	private GroupInterface groupInterface;

	private Group(GroupInterface groupInterface) {
		this.groupInterface = groupInterface;
	}

	public GroupInterface getGroupInterface() {
		return groupInterface;
	}

	public List<String> getPermissions() {
		return groupInterface.getPermissions();
	}

	private Group() {
		this(new SimpleGroup());
	}
}
