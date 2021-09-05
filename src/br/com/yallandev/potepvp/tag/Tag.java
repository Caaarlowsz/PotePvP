package br.com.yallandev.potepvp.tag;

import br.com.yallandev.potepvp.permissions.group.Group;

public enum Tag {
	
	DEVELOPER("§3§lDEVELOPER §3", Group.DONO),
	DONO("§4§lDONO §4", Group.DONO),
	ADMIN("§c§lADMIN §c", Group.ADMIN),
	GERENTE("§c§lGERENTE §c", Group.GERENTE),
	MODPLUS("§5§lMOD+ §5", Group.MODPLUS),
	MODGC("§5§lMODGC §5", Group.MODGC),
	MOD("§5§lMOD §5", Group.MOD),
	TRIAL("§d§lTRIAL §d", Group.TRIAL),
	YOUTUBERPLUS("§3§lYT+ §3", Group.YOUTUBERPLUS, true),
	HELPER("§9§lHELPER §9", Group.HELPER),
	BUILDER("§e§lBUILDER §e", Group.BUILDER),
	YOUTUBER("§b§lYT §b", Group.YOUTUBER, true),
	HEIGHT("§2§lHEIGHT §2", Group.HEIGHT),
	BETA("§1§lBETA §1", Group.BETA),
	PRO("§6§lPRO §6", Group.PRO),
	MVP("§9§lMVP §9", Group.MVP),
	TOP1("§2§lTOP1 §2", null),
	LOSER("§8§lLOSER §8", null),
	LIGHT("§a§lLIGHT §a", Group.LIGHT),
	MEMBRO("§f", Group.MEMBRO);
	
	private String prefix;
	private Group group;
	private boolean exclusive;
	
	private Tag(String prefix, Group group, boolean exclusive) {
		this.prefix = prefix;
		this.group = group;
		this.exclusive = exclusive;
	}
	
	private Tag(String prefix, Group group) {
		this.prefix = prefix;
		this.group = group;
		this.exclusive = false;
	}
	
	private Tag(String prefix) {
		this(prefix, Group.MEMBRO, false);
	}
	
	public Group getGroup() {
		return group;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public boolean isExclusive() {
		return exclusive;
	}

	public static Tag getTag(String match) throws Exception {
		try {
			return Tag.valueOf(match.toUpperCase());
		} catch (Exception e) {}
		
		if (match.equalsIgnoreCase("normal")||match.equalsIgnoreCase("default")) {
			return Tag.MEMBRO;
		}
		
		if (match.equalsIgnoreCase("ajudante")) {
			return Tag.HELPER;
		}
		
		if (match.equalsIgnoreCase("mod+")) {
			return Tag.MODPLUS;
		}
		
		if (match.equalsIgnoreCase("yt")) {
			return Tag.YOUTUBER;
		}
		
		if (match.equalsIgnoreCase("yt+") || match.equalsIgnoreCase("youtuber+")) {
			return Tag.YOUTUBERPLUS;
		}
		
		if (match.equalsIgnoreCase("dev")) {
			return Tag.DEVELOPER;
		}
		
		if (match.equalsIgnoreCase("top")) {
			return Tag.TOP1;
		}
		
		throw new Exception();
	}
	
}
