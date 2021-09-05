package br.com.yallandev.potepvp.commands.register;

import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import br.com.yallandev.potepvp.account.Account;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.Command;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.CommandArgs;
import br.com.yallandev.potepvp.manager.CommandClass;
import br.com.yallandev.potepvp.permissions.group.Group;
import br.com.yallandev.potepvp.permissions.group.PaymentGroup;
import br.com.yallandev.potepvp.status.Status;
import br.com.yallandev.potepvp.tag.Tag;
import br.com.yallandev.potepvp.utils.DateUtils;
import br.com.yallandev.potepvp.utils.ItemManager;
import br.com.yallandev.potepvp.utils.string.CenterChat;

public class AccountCommand extends CommandClass {

	@Command(name = "list")
	public void onList(CommandArgs cmdArgs) {
		cmdArgs.getSender().sendMessage(
				"�6�l� �e" + Bukkit.getOnlinePlayers().length + "/" + Bukkit.getMaxPlayers() + " jogadores");
	}

	@Command(name = "account", aliases = { "acc" })
	public void onAccount(CommandArgs cmdArgs) {
		String[] a = cmdArgs.getArgs();
		CommandSender s = cmdArgs.getSender();
		Account player = null;

		if (a.length == 0) {
			if (cmdArgs.isPlayer()) {
				player = cmdArgs.getAccount();
			} else {
				send(s, "Use �a/account [Player]�f para ver");
				return;
			}
		} else {
			UUID uuid = getAccountCommon().getUUID(a[0]);

			if (uuid == null) {
				send(s, "O jogador �c\"" + a[0] + "\" �fn�o existe no nosso banco de dados!");
				return;
			}

			player = getAccountCommon().getAccount(uuid);

			if (player == null) {
				player = getAccountCommon().loadAccount(uuid, false);

				if (player == null) {
					send(s, "O jogador �c\"" + a[0] + "\" �fn�o existe!");
					return;
				}
			}
		}

		s.sendMessage(CenterChat.centered("�e-=-=-=-=(*)=-=-=-=-"));
		s.sendMessage(CenterChat.centered(""));
		s.sendMessage(CenterChat.centered("�eNome: �3" + player.getUserName()));
		s.sendMessage(CenterChat.centered("�eUniqueID: �3" + player.getUuid().toString()));
		s.sendMessage(CenterChat.centered("�eGrupo: �3" + (player.getGroup() == Group.MEMBRO ? "�f�lMEMBRO"
				: Tag.valueOf(player.getGroup().name()).getPrefix().replaceAll(" ", ""))));
		s.sendMessage(CenterChat.centered("�eRank: �3" + player.getRanking().getColor() + player.getRanking().getIcon()
				+ " " + player.getRanking().name()));
		s.sendMessage(CenterChat.centered(""));

		if (!player.getPayment().isEmpty()) {
			for (Entry<PaymentGroup, Long> expireRank : player.getPayment().entrySet()) {
				s.sendMessage(CenterChat.centered(Tag.valueOf(expireRank.getKey().name()).getPrefix()));
				s.sendMessage(CenterChat.centered("�eExpira em " + DateUtils.getTime(expireRank.getValue())));
			}

			s.sendMessage(CenterChat.centered(""));
		}

		s.sendMessage(CenterChat.centered("�e-=-=-=-=(*)=-=-=-=-"));
	}

	@Command(name = "status", onlyPlayers = true)
	public void onStatus(CommandArgs cmdArgs) {
		String[] a = cmdArgs.getArgs();
		CommandSender s = cmdArgs.getSender();
		Player p = cmdArgs.getPlayer();
		Account player = null;

		if (a.length == 0) {
			player = cmdArgs.getAccount();
		} else {
			player = getAccountCommon().getAccount(getAccountCommon().getUUID(a[0]));

			if (player == null) {
				player = getAccountCommon().loadAccount(getAccountCommon().getUUID(a[0]), false);

				if (player == null) {
					send(s, "O jogador �c\"" + a[0] + "\" �fn�o existe!");
					return;
				}
			}
		}

		open(player, p);
	}

	public void open(Account player, Player p) {
		Inventory inv = Bukkit.createInventory(null, 36, "�8Status");

		Status status = player.getStatus();

		if (status == null) {
			send(p, "N�o foi possivel localizar o status desse jogador!");
			return;
		}

		ItemStack itemStack = new ItemStack(Material.SKULL_ITEM);
		itemStack.setDurability((short) 3);
		SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
		skullMeta.setOwner(player.getUserName());
		skullMeta.setDisplayName("�aStatus de " + player.getUserName());
		itemStack.setItemMeta(skullMeta);

		inv.setItem(4, itemStack);

		ItemManager item = new ItemManager(Material.DIAMOND_SWORD, "�aKitPvP");

		item.addLore("");
		item.addLore("�7Kills: �3" + status.getKills());
		item.addLore("�7Deaths: �3" + status.getDeaths());
		item.addLore("�0");
		item.addLore("�71v1 Wins: �30");
		item.addLore("�71v1 Losses: �30");
		item.addLore("�0");
		item.addLore("�7Highest KillStreak: �3" + status.getHighestKillstreak());

		inv.setItem(22, item.build());

		item = new ItemManager(Material.MUSHROOM_SOUP, "�aHG");

		item.addLore("");
		item.addLore("�7Kills: �30");
		item.addLore("�7Deaths: �30");
		item.addLore("�7Wins: �30");
		item.addLore("�0");
		item.addLore("�7Highest KillStreak: �30");

		inv.setItem(20, item.build());

		item = new ItemManager(Material.IRON_FENCE, "�aGladiator");

		item.addLore("");
		item.addLore("�7Wins: �3");
		item.addLore("�7Deaths: �3");
		item.addLore("�0");
		item.addLore("�7Highest KillStreak: �30");

		inv.setItem(24, item.build());

		p.openInventory(inv);
	}
}
