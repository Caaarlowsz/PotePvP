package br.com.yallandev.potepvp.commands.register;

import org.bukkit.entity.Player;

import com.github.caaarlowsz.potemc.kitpvp.PotePvP.Configuration;
import br.com.yallandev.potepvp.account.Account;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.Command;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.CommandArgs;
import br.com.yallandev.potepvp.kit.Kit;
import br.com.yallandev.potepvp.manager.CommandClass;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class KitCommand extends CommandClass {

	@Command(name = "kit", onlyPlayers = true)
	public void onKit(CommandArgs cmdArgs) {
		String[] a = cmdArgs.getArgs();
		Player p = cmdArgs.getPlayer();
		Account player = getAccountCommon().getAccount(p.getUniqueId());

		if (player == null)
			return;

		if (a.length == 0) {
			player.sendMessage("Use �a/kit [Kit]�f para selecionar um kit!");

			TextComponent kitContains = new TextComponent("�fKits que voc� possui: �a");

			for (Kit kits : main.getKitManager().getKits()) {
				if (kits.canUse(player)) {
					TextComponent kit = new TextComponent(kits.getKitName() + ", ");

					kit.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
							new ComponentBuilder("�aVoc� possui esse kit!").create()));
					kit.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kit " + kits.getKitName()));

					kitContains.addExtra(kit);
				}
			}

			kitContains = new TextComponent("�fKits que voc� n�o possui: �c");

			for (Kit kits : main.getKitManager().getKits()) {
				if (!kits.canUse(player)) {
					TextComponent kit = new TextComponent(kits.getKitName() + ", ");

					kit.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
							new ComponentBuilder("�aVoc� possui esse kit!").create()));
					kit.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, Configuration.SITE.getMessage()));

					kitContains.addExtra(kit);
				}
			}
			return;
		}

		if (!main.getKitManager().hasAbility(p.getUniqueId(), "Nenhum")) {
			player.sendMessage("Voc� j� est� com um kit selecionado!");
			return;
		}

		if (!main.getPlayerManager().getWarp(p.getUniqueId()).getWarpName().equalsIgnoreCase("Spawn")) {
			player.sendMessage("Os kits s� podem ser selecionados no spawn!");
			return;
		}

		Kit kit = main.getKitManager().getKit(a[0]);

		if (kit == null) {
			player.sendMessage("O kit �a\"" + a[0] + "\"�f n�o existe!");
			return;
		}

		if (!kit.canUse(player)) {
			player.sendMessage("Voc� n�o tem esse kit!");
			player.sendMessage("Compre em �a�n" + Configuration.SITE.getMessage() + "�f.");
			return;
		}

		main.getKitManager().setAbility(p.getUniqueId(), kit);
		main.getKitManager().applyKit(p);
		player.sendMessage("Voc� selecionou o kit �a" + kit.getKitName() + "�f.");
	}

}
