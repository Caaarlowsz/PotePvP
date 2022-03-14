package br.com.yallandev.potepvp.manager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import com.github.caaarlowsz.potemc.kitpvp.PotePvP;
import com.github.caaarlowsz.potemc.kitpvp.PotePvP.Configuration;
import br.com.yallandev.potepvp.manager.WarpManager.Warp;
import br.com.yallandev.potepvp.permissions.group.Group;
import br.com.yallandev.potepvp.utils.Util;

public class SimpleKit {

	private HashMap<String, Skit> skit;

	public SimpleKit() {
		this.skit = new HashMap<>();
	}

	public void applyInPlayer(Player player, Player target, String skitName) {
		if (skit.containsKey(skitName.toLowerCase())) {
			Skit skit = this.skit.get(skitName.toLowerCase());

			skit.applySkit(target);
			target.sendMessage(
					Configuration.PREFIX.getMessage() + "O skit �a\"" + skitName + "\"�f foi aplicado em voc�.");

			player.sendMessage(Configuration.PREFIX.getMessage() + "Voc� aplicou o kit �a\"" + skitName
					+ "\"�f no jogador �a" + target.getName() + "�f.");
			PotePvP.broadcast("O skit �a\"" + skitName + "\"�f foi aplicado pelo �a" + player.getName()
					+ "�f somente no jogador �a" + target.getName() + "�f.", Group.YOUTUBERPLUS);
		} else {
			player.sendMessage(Configuration.PREFIX.getMessage() + "O skit �a\"" + skitName + "\"�f n�o existe!");
		}
	}

	public void applyInWarp(Player player, Warp warp, String skitName) {
		if (skit.containsKey(skitName.toLowerCase())) {
			Skit skit = this.skit.get(skitName.toLowerCase());

			int playersCount = 0;

			for (Player players : Util.getOnlinePlayers()) {
				if (!PotePvP.getInstance().getPlayerManager().getWarp(players.getUniqueId()).getWarpName()
						.equalsIgnoreCase(warp.getWarpName()))
					continue;

				if (PotePvP.getInstance().getVanishMode().isAdmin(players.getUniqueId())) {
					players.sendMessage(Configuration.PREFIX.getMessage() + "O skit �a\"" + skitName
							+ "\"�f n�o foi aplicado em voc�, por que voc� est� no modo �4�lADMIN�f.");
					continue;
				}
				playersCount++;
				skit.applySkit(players);
				players.sendMessage(
						Configuration.PREFIX.getMessage() + "O skit �a\"" + skitName + "\"�f foi aplicado em voc�.");
			}

			player.sendMessage(Configuration.PREFIX.getMessage() + "Voc� aplicou o kit �a\"" + skitName + "\"�f em �a"
					+ playersCount + " jogadores�f.");
			PotePvP.broadcast("O skit �a\"" + skitName + "\"�f foi aplicado pelo �a" + player.getName()
					+ "�f para todos os jogadores.", Group.YOUTUBERPLUS);
		} else {
			player.sendMessage(Configuration.PREFIX.getMessage() + "O skit �a\"" + skitName + "\"�f n�o existe!");
		}
	}

	public void applyInAll(Player player, String skitName) {
		if (skit.containsKey(skitName.toLowerCase())) {
			Skit skit = this.skit.get(skitName.toLowerCase());

			int playersCount = 0;

			for (Player players : Util.getOnlinePlayers()) {
				if (PotePvP.getInstance().getVanishMode().isAdmin(players.getUniqueId())) {
					players.sendMessage(Configuration.PREFIX.getMessage() + "O skit �a\"" + skitName
							+ "\"�f n�o foi aplicado em voc�, por que voc� est� no modo �4�lADMIN�f.");
					continue;
				}
				playersCount++;
				skit.applySkit(players);
				players.sendMessage(
						Configuration.PREFIX.getMessage() + "O skit �a\"" + skitName + "\"�f foi aplicado em voc�.");
			}

			player.sendMessage(Configuration.PREFIX.getMessage() + "Voc� aplicou o kit �a\"" + skitName + "\"�f em �a"
					+ playersCount + " jogadores�f.");
			PotePvP.broadcast("O skit �a\"" + skitName + "\"�f foi aplicado pelo �a" + player.getName()
					+ "�f para todos os jogadores.", Group.YOUTUBERPLUS);
		} else {
			player.sendMessage(Configuration.PREFIX.getMessage() + "O skit �a\"" + skitName + "\"�f n�o existe!");
		}
	}

	public void applySkit(Player player, int raio, String skitName) {
		if (raio > 2000) {
			player.sendMessage(Configuration.PREFIX.getMessage() + "O raio n�o pode ser maior que �a2000�f.");
		}

		if (skit.containsKey(skitName.toLowerCase())) {
			Skit skit = this.skit.get(skitName.toLowerCase());

			int playersCount = 0;

			for (Entity entities : player.getNearbyEntities(raio, 120, raio)) {
				if (entities instanceof Player) {
					Player players = (Player) entities;
					if (PotePvP.getInstance().getVanishMode().isAdmin(players.getUniqueId())) {
						players.sendMessage(Configuration.PREFIX.getMessage() + "O skit �a\"" + skitName
								+ "\"�f n�o foi aplicado em voc�, por que voc� est� no modo �4�lADMIN�f.");
						continue;
					}
					playersCount++;
					skit.applySkit(players);
					players.sendMessage(Configuration.PREFIX.getMessage() + "O skit �a\"" + skitName
							+ "\"�f foi aplicado em voc�.");
				}
			}

			player.sendMessage(Configuration.PREFIX.getMessage() + "Voc� aplicou o kit �a\"" + skitName + "\"�f em �a"
					+ playersCount + " jogadores�f.");
			PotePvP.broadcast("O skit �a\"" + skitName + "\"�f foi aplicado pelo �a" + player.getName()
					+ "�f em um raio de " + raio + " �a(" + playersCount + " jogadores)�f.", Group.YOUTUBERPLUS);

			if (PotePvP.getInstance().getVanishMode().isAdmin(player.getUniqueId())) {
				player.sendMessage(Configuration.PREFIX.getMessage() + "O skit �a\"" + skitName
						+ "\"�f n�o foi aplicado em voc�, por que voc� est� no modo �4�lADMIN�f.");
				return;
			}

			playersCount++;
			skit.applySkit(player);
		} else {
			player.sendMessage(Configuration.PREFIX.getMessage() + "O skit �a\"" + skitName + "\"�f n�o existe!");
		}
	}

	public void createSkit(Player player, String skitName) {
		if (skit.containsKey(skitName.toLowerCase())) {
			player.sendMessage(Configuration.PREFIX.getMessage() + "O skit �a\"" + skitName + "\"�f j� existe!");
			player.sendMessage(Configuration.PREFIX.getMessage() + "Use �a/skit update " + skitName
					+ " �fpara atualizar esse skit!");
		} else {
			Skit skit = new Skit(player.getInventory().getContents(), player.getInventory().getArmorContents(),
					player.getActivePotionEffects());

			this.skit.put(skitName.toLowerCase(), skit);
			player.sendMessage(
					Configuration.PREFIX.getMessage() + "O skit �a\"" + skitName + "\"�f foi criado com sucesso!");
		}
	}

	public void updateSkit(Player player, String skitName) {
		Skit skit = new Skit(player.getInventory().getContents(), player.getInventory().getArmorContents(),
				player.getActivePotionEffects());

		this.skit.put(skitName.toLowerCase(), skit);
		player.sendMessage(
				Configuration.PREFIX.getMessage() + "O skit �a\"" + skitName + "\"�f foi atualizado com sucesso!");
	}

	public Set<String> values() {
		return skit.keySet();
	}

	public class Skit {

		private ItemStack[] items;
		private ItemStack[] armors;
		private Collection<PotionEffect> potions;

		public Skit(ItemStack[] items, ItemStack[] armors, Collection<PotionEffect> potions) {
			this.items = items;
			this.armors = armors;
			this.potions = potions;
		}

		public void applySkit(Player player) {
			for (PotionEffect pot : player.getActivePotionEffects()) {
				player.removePotionEffect(pot.getType());
			}

			player.getInventory().clear();
			player.getInventory().setArmorContents(new ItemStack[4]);

			player.getInventory().setArmorContents(armors);
			player.getInventory().setContents(items);

			player.addPotionEffects(potions);
		}
	}

}
