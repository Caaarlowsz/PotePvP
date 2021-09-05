package br.com.yallandev.potepvp.commands.register;

import org.bukkit.command.CommandSender;

import br.com.yallandev.potepvp.account.Account;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.Command;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.CommandArgs;
import br.com.yallandev.potepvp.manager.CommandClass;
import br.com.yallandev.potepvp.ranking.Ranking;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class RankCommand extends CommandClass {
	
	@Command(name = "rank", onlyPlayers = true)
	public void onRank(CommandArgs cmdArgs) {
		for (Ranking ranks : Ranking.values()) {
			if (ranks == Ranking.POTTER) {
				TextComponent chat = new TextComponent(ranks.getColor() + ranks.getIcon() + " " + ranks.name());
				chat.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new ComponentBuilder("Ultimo rank, o rank dos vencedores!").create()));
				cmdArgs.getPlayer().spigot().sendMessage(chat);
			} else {
				TextComponent chat = new TextComponent(ranks.getColor() + ranks.getIcon() + " " + ranks.name());
				chat.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new ComponentBuilder("Você precisa de " + ranks.getXp() + " e para sair\n§fdesse rank e upar para §a" + Ranking.values()[ranks.ordinal() + 1]).create()));
				cmdArgs.getPlayer().spigot().sendMessage(chat);
			}
		}
		
		CommandSender s = cmdArgs.getSender();
		Account player = cmdArgs.getAccount();
		
		cmdArgs.getAccount().sendMessage("Você está no rank " + player.getRanking().getColor() + player.getRanking().getIcon() + " " + player.getRanking().name() + "§f.");
		cmdArgs.getAccount().sendMessage("Você está com §a" + player.getStatus().getXp() + "§f.");
		
	}

}
