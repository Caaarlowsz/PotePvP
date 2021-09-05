package br.com.yallandev.potepvp.manager;

import java.util.HashMap;

import br.com.yallandev.potepvp.BukkitMain;
import br.com.yallandev.potepvp.manager.WarpManager.Warp;

public class ServerManager {
	
	private HashMap<Warp, Boolean> damage;
	private boolean damageAll;
	
	public ServerManager() {
		this.damage = new HashMap<>();
		
		
		for (Warp warp : BukkitMain.getInstance().getWarpManager().getWarps()) {
			damage.put(warp, true);
		}
		
		damageAll = true;
	}
	
	public HashMap<Warp, Boolean> getDamage() {
		return damage;
	}
	
	public boolean isDamageAll() {
		return damageAll;
	}
	
	public void setDamageAll(boolean damageAll) {
		this.damageAll = damageAll;
	}
	
	public void setDamageEnable(Warp warp, boolean damage) {
		this.damage.put(warp, damage);
	}
	
	public boolean isDamageEnable(Warp warp) {
		return damage.containsKey(warp) && damage.get(warp) == true;
	}

}
