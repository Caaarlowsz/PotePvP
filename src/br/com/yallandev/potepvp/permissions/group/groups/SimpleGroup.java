package br.com.yallandev.potepvp.permissions.group.groups;

import java.util.ArrayList;
import java.util.List;

public class SimpleGroup extends GroupInterface {

	@Override
	public List<String> getPermissions() {
		return new ArrayList<>();
	}
	
}
