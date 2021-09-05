package br.com.yallandev.potepvp.kit.register;

import java.util.Arrays;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import br.com.yallandev.potepvp.kit.Kit;

public class Pote extends Kit {

	public Pote() {
		super("Pote", Material.BOWL, 16000, true,
				Arrays.asList("Use seu pote para", "ter uma chance de 40%", "para tirar todos os",
						"do seu inimigo, mas caso", "n�o retire os potes,", "ficar� com efeitos negativos."));
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEntityEvent e) {
		if (!(e.getRightClicked() instanceof Player))
			return;

		Player p = e.getPlayer();

		if (!hasKit(p))
			return;

		if (p.getItemInHand() == null)
			return;

		if (p.getItemInHand().getType() != Material.BOWL)
			return;

		if (isCooldown(p)) {
			cooldownMessage(p);
			return;
		}

		Player t = (Player) e.getRightClicked();

		addCooldown(p, 26);

		int pro = new Random().nextInt(100) + 1;

		if (pro <= 40) {
			for (int x = 0; x < t.getInventory().getSize(); x++) {
				if (t.getInventory().getItem(x) == null)
					continue;

				if (t.getInventory().getItem(x).getType() == Material.AIR)
					continue;

				if (t.getInventory().getItem(x).getType() == Material.BOWL) {
					t.getInventory().getItem(x).setType(Material.AIR);
				}
			}

			sendAction(p, "Voc� �alimpou�f o os potes do jogador �a" + t.getName());
			sendMessage(p, "");
		} else {
			sendMessage(p, "Voc� recebeu os efeitos ");
		}
	}

}
