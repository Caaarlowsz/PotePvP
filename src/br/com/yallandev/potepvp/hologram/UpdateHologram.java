package br.com.yallandev.potepvp.hologram;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import br.com.yallandev.potepvp.BukkitMain;
import br.com.yallandev.potepvp.account.Account;
import br.com.yallandev.potepvp.event.update.UpdateEvent;
import br.com.yallandev.potepvp.event.update.UpdateEvent.UpdateType;
import br.com.yallandev.potepvp.permissions.group.Group;

public class UpdateHologram implements Listener {

	private BukkitMain main;
	private List<Hologram> hologram;
	private int x;

	public UpdateHologram() {
		this.main = BukkitMain.getInstance();
		this.hologram = new ArrayList<>();
		this.x = 0;

		main.getServer().getPluginManager().registerEvents(new HologramListener(), main);

		update();
	}

	@EventHandler
	public void onUpdate(UpdateEvent e) {
		if (e.getType() != UpdateType.SECOND)
			return;

		x = 0;

		if (x == 15) {
			update();
			return;
		}
	}

	public void removeAll() {
		for (Hologram h : hologram) {
			h.remove();
		}
	}

	public void update() {
		if (!hologram.isEmpty()) {
			for (Hologram holograms : this.hologram) {
				holograms.remove();
			}

			this.hologram.clear();

			System.out.println("Update");

			Location spawnLocation = main.getWarpManager().getWarp("Spawn").getWarpLocation();

			Hologram hologram = new Hologram("", spawnLocation.clone().add(5, 3, 5), true);

			hologram.addLine("�6�lTOP KILLS");

			for (String tops : getTop("kills", 10)) {
				hologram.addLine(tops);
			}

			this.hologram.add(hologram);

			hologram = new Hologram("", spawnLocation.clone().add(-5, 3, -5), true);

			hologram.addLine("�4�lTOP DEATHS");

			for (String tops : getTop("deaths", 10)) {
				hologram.addLine(tops);
			}

			this.hologram.add(hologram);
			return;
		}

		System.out.println("Criando hologramas!");

		Location spawnLocation = main.getWarpManager().getWarp("Spawn").getWarpLocation();

		Hologram hologram = new Hologram("", spawnLocation.clone().add(5, 2, 5), true);

		hologram.addLine("�6�lTOP KILLS");

		for (String tops : getTop("kills", 10)) {
			hologram.addLine(tops);
		}

		this.hologram.add(hologram);

		hologram = new Hologram("", spawnLocation.clone().add(-5, 2, -5), true);

		hologram.addLine("�4�lTOP DEATHS");

		for (String tops : getTop("deaths", 10)) {
			hologram.addLine(tops);
		}

		this.hologram.add(hologram);
	}

	public String getColor(Account player) {
		if (player.hasServerGroup(Group.HEIGHT)) {
			return "�2";
		}
		if (player.hasServerGroup(Group.BETA)) {
			return "�1";
		}
		if (player.hasServerGroup(Group.PRO)) {
			return "�6";
		}
		if (player.hasServerGroup(Group.MVP)) {
			return "�9";
		}
		if (player.hasServerGroup(Group.LIGHT)) {
			return "�a";
		}
		return "�f";
	}

	public List<String> getTop(String type, int max) {
		List<String> tops = new ArrayList<String>();

		try {
			PreparedStatement stm = BukkitMain.getConnection().getConnection()
					.prepareStatement("SELECT * FROM `kitpvp_status` ORDER BY `" + type + "` DESC LIMIT 0," + max + "");
			ResultSet rs = stm.executeQuery();
			int i = 0;
			while (rs.next()) {
				UUID uuid = UUID.fromString(rs.getString("Uuid"));

				if (uuid == null)
					continue;

				Account player = BukkitMain.getAccountCommon().getAccount(uuid);

				if (player == null) {
					player = BukkitMain.getAccountCommon().loadAccount(uuid, false);

					if (player == null) {
						continue;
					}
				}

				i++;

				if (i <= 3) {
					tops.add("�a" + i + " �8- �f" + getColor(player) + player.getUserName() + " �8|�a "
							+ rs.getInt(type));
				} else if (i <= 5) {
					tops.add("�e" + i + " �8- �f" + getColor(player) + player.getUserName() + " �8|�a "
							+ rs.getInt(type));
				} else if (i <= 7) {
					tops.add("�c" + i + " �8- �f" + getColor(player) + player.getUserName() + " �8|�a "
							+ rs.getInt(type));
				} else {
					tops.add("�4" + i + " �8- �f" + getColor(player) + player.getUserName() + " �8|�a "
							+ rs.getInt(type));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
		return tops;
	}

}
