package br.com.yallandev.potepvp.commands.register;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.github.caaarlowsz.potemc.kitpvp.PotePvP;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.Command;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.CommandArgs;
import br.com.yallandev.potepvp.manager.CommandClass;
import br.com.yallandev.potepvp.permissions.group.Group;
import br.com.yallandev.potepvp.utils.ChatAPI.ChatState;
import br.com.yallandev.potepvp.utils.string.CenterChat;

public class ChatCommand extends CommandClass {

	@Command(name = "clearchat", aliases = { "cc" }, groupToUse = Group.HELPER)
	public void onClearchat(CommandArgs cmdArgs) {
		CommandSender s = cmdArgs.getSender();

		for (int x = 0; x < 100; x++)
			Bukkit.broadcastMessage("");

		Bukkit.broadcastMessage(CenterChat.centered("�aO chat foi limpo!"));
		broadcast("O chat foi limpo pelo �a" + s.getName() + "�f.", Group.HELPER);
	}

	@Command(name = "chat", groupToUse = Group.TRIAL)
	public void onChat(CommandArgs args) {
		String[] a = args.getArgs();
		CommandSender sender = args.getSender();

		if (a.length == 0) {
			send(sender, "Use �a/chat [on/off]�f para ativar ou desativar o chat!");
		} else {
			if (a[0].equalsIgnoreCase("on")) {
				if (PotePvP.getInstance().getChatAPI().getChatState() == ChatState.ENABLED) {
					send(sender, "O chat j� est� ativado.");
				} else {
					PotePvP.getInstance().getChatAPI().setChatState(ChatState.ENABLED);
					send(sender, "Voc� �a�lATIVOU�f o chat!");
					broadcast("O �a" + sender.getName() + "�f ativou o chat!", getPosteriorGroup(sender, 2));
				}
			} else if (a[0].equalsIgnoreCase("off")) {
				if (PotePvP.getInstance().getChatAPI().getChatState() == ChatState.DISABLED) {
					send(sender, "O chat j� est� desativado.");
				} else {
					PotePvP.getInstance().getChatAPI().setChatState(ChatState.DISABLED);
					send(sender, "Voc� �4�lDESATIVOU�f o chat!");
					broadcast("O �a" + sender.getName() + "�f desativou o chat!", getPosteriorGroup(sender, 2));
				}
			} else {
				send(sender, "Use �a/chat [on/off]�f para ativar ou desativar o chat!");
			}
		}
	}

}
