package br.com.yallandev.potepvp.listener;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class FreezerListener implements Listener {

	public static HashMap<UUID, Long> freezer = new HashMap<>();

	@EventHandler
	public void onRealMove(PlayerMoveEvent e) {
		UUID uuid = e.getPlayer().getUniqueId();

		if (freezer.containsKey(uuid))
			if (freezer.get(uuid) < System.currentTimeMillis()) {
				freezer.remove(uuid);
			} else {
				e.setCancelled(false);
			}
	}

}
