package br.com.yallandev.potepvp.kit.register;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import com.github.caaarlowsz.potemc.kitpvp.PotePvP;
import br.com.yallandev.potepvp.kit.Kit;

public class Ninja extends Kit {

	public Ninja() {
		super("Ninja", Material.NETHER_STAR, 18000, false,
				Arrays.asList("Use seu ninja para", "teletransporta-se para o ultimo", "jogador hitado."));
	}

	public HashMap<UUID, Player> ninjado = new HashMap<>();
	public HashMap<UUID, Long> cooldown = new HashMap<>();

	@EventHandler
	public void asd(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (e.getDamager() instanceof Player) {
				Player d = (Player) e.getDamager();
				if (hasKit(d)) {
					ninjado.put(d.getUniqueId(), p);
				}
			}
		}
	}

	@EventHandler
	public void asd(PlayerToggleSneakEvent e) {
		Player p = e.getPlayer();

		if (hasKit(p)) {
			if (!isCooldown(p)) {

				if (ninjado.containsKey(p.getUniqueId())) {
					if (ninjado.get(p.getUniqueId()).isOnline()) {
						Player pninjado = ninjado.get(p.getUniqueId());
//						if (BukkitMain.getInstance().getVanishMode().isAdmin(pninjado.getUniqueId())) {
//							sendMessage(p, "Jogador offline!");
//						} else {
						if (pninjado.getLocation().distance(p.getLocation()) > 60) {
							sendMessage(p, "Jogador muito distante.");
							return;
						}
						if (PotePvP.getInstance().getGladiatorFightController().isInFight(p)) {
							if (!PotePvP.getInstance().getGladiatorFightController().isInFight(pninjado)) {
								sendMessage(p, "Voc� est� no gladiator!");
								return;
							}
						}
						p.teleport(pninjado);
						sendMessage(p, "Voc� foi teletransportado!");
						sendAction(p, "Voc� foi teletransportado!");
						addCooldown(p, 6);
						ninjado.remove(p.getUniqueId());
//						}
					} else {
						sendMessage(p, "Jogador offline!");
					}
				} else {
					sendMessage(p, "Voc� ainda n�o bateu em ningu�m.");
				}
			} else {
				cooldownMessage(p);
			}
		}
	}

}
