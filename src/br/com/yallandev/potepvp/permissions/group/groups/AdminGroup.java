package br.com.yallandev.potepvp.permissions.group.groups;

import java.util.ArrayList;
import java.util.List;

public class AdminGroup extends GroupInterface {

	@Override
	public List<String> getPermissions() {
		List<String> permissions = new ArrayList<>();
		for (String str : new String[] { "*" }) {
			permissions.add("minecraft.command." + str);
			permissions.add("bukkit.command." + str);
			permissions.add("bukkit.cmd." + str);
		}
		permissions.add("worldedit.*");
		return permissions;
	}

}
