package br.com.yallandev.potepvp.commands.register;

import br.com.yallandev.potepvp.commands.BukkitCommandFramework.Command;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.CommandArgs;
import br.com.yallandev.potepvp.manager.CommandClass;
import br.com.yallandev.potepvp.utils.string.CenterChat;

public class YoutuberCommand extends CommandClass {
	
	@Command(name = "youtuber", aliases = { "yt" })
	public void onYoutuber(CommandArgs cmdArgs) {
		cmdArgs.getSender().sendMessage("");
		cmdArgs.getSender().sendMessage(CenterChat.centered("§7Formulario de §b§lYOUTUBER §8- §7500 visualizações e 50 likes."));
		cmdArgs.getSender().sendMessage(CenterChat.centered("§7Formulario de §6§lPRO §8- §7100 visualizações e 10 likes"));
		cmdArgs.getSender().sendMessage("");
	}

}
