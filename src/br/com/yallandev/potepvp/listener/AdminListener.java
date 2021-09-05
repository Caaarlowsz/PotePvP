package br.com.yallandev.potepvp.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.yallandev.potepvp.BukkitMain;

public class AdminListener implements Listener {

	private BukkitMain main;

	public AdminListener(BukkitMain main) {
		this.main = main;
	}

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
		if (!(e.getRightClicked() instanceof Player))
			return;

		Player p = e.getPlayer();

		if (main.getVanishMode().isAdmin(p.getUniqueId())) {
			if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR)
				p.openInventory(((Player) e.getRightClicked()).getInventory());
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();

		if (main.getVanishMode().isAdmin(p.getUniqueId())) {
			if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR)
				return;

			if (p.getItemInHand().getType() == Material.MAGMA_CREAM) {
				p.performCommand("admin");

				new BukkitRunnable() {

					@Override
					public void run() {
						p.performCommand("admin");
					}
				}.runTaskLater(BukkitMain.getInstance(), 10);
			}
		}
	}

}
