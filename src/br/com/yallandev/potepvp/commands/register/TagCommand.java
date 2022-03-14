package br.com.yallandev.potepvp.commands.register;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.caaarlowsz.potemc.kitpvp.PotePvP;
import com.github.caaarlowsz.potemc.kitpvp.PotePvP.Configuration;
import br.com.yallandev.potepvp.account.Account;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.Command;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.CommandArgs;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.Completer;
import br.com.yallandev.potepvp.manager.CommandClass;
import br.com.yallandev.potepvp.permissions.group.Group;
import br.com.yallandev.potepvp.tag.Tag;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class TagCommand extends CommandClass {

	private BaseComponent buildComponent(Tag tag, String displayName) {
		BaseComponent baseComponent = new TextComponent(
				tag == Tag.MEMBRO ? "�f�lMEMBRO" : tag.getPrefix().replace(" ", ""));
		baseComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new ComponentBuilder(
						"Como seu nickname ir� ficar: \"" + tag.getPrefix().replace(" ", "") + " " + displayName)
								.create()));
		baseComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tag " + tag.name()));
		return baseComponent;
	}

	@Command(name = "tag", aliases = "tags")
	public void onTag(CommandArgs args) {
		if (!args.isPlayer())
			return;

		Player p = args.getPlayer();
		String[] a = args.getArgs();
		Account player = (Account) PotePvP.getAccountCommon().getAccount(p.getUniqueId());

		if (a.length == 0) {
			send(p, "Use �a/tag [tag]�f para alterar sua tag!");
			send(p, "Use �a/tag TAB�f para ver suas tags.");

			TextComponent tagsMessage = new TextComponent(Configuration.PREFIX.getMessage() + "Suas tags: ");

			List<Tag> tags = new ArrayList<>();

			for (Tag t : Tag.values()) {
				if (t == Tag.TOP1) {
					if (player.getStatus().getRank() == 1) {
						tags.add(t);
					}
					continue;
				}
				if (t == Tag.LOSER) {
					if (player.getStatus().getRankDeath() == 1) {
						tags.add(t);
					}
					continue;
				}
				if (t == Tag.BETA) {
					if (player.isBeta() || player.hasServerGroup(Group.BETA)) {
						tags.add(t);
					}
					continue;
				}
				if (t.isExclusive()) {
					if (player.isGroup(t.getGroup()) || player.hasServerGroup(Group.ADMIN)
							|| player.hasPermission("tag." + t.name())) {
						tags.add(t);
					}
				} else {
					if (player.hasServerGroup(t.getGroup())) {
						tags.add(t);
					}
				}
			}

			for (int i = 0; i < tags.size(); i++) {
				if (i == tags.size() - 1) {
					tagsMessage.addExtra(".");
					break;
				}

				Tag tag = tags.get(i);
				tagsMessage.addExtra(i == 0 ? "" : ", ");
				tagsMessage.addExtra(buildComponent(tag, p.getName()));
			}

			p.spigot().sendMessage(tagsMessage);

		} else {
			Tag tag = null;

			try {
				tag = Tag.getTag(a[0]);
			} catch (Exception ex) {
				player.sendMessage("A tag �a\"" + a[0] + "\"�f n�o existe!");
				return;
			}

			if (tag == Tag.TOP1) {
				if (player.getStatus().getRank() == 1) {
					for (Player players : Bukkit.getOnlinePlayers()) {
						Account account = PotePvP.getAccountCommon().getAccount(players.getUniqueId());

						if (account == null)
							continue;

						if (account.getTag() == Tag.TOP1)
							account.setTag(Tag.valueOf(account.getGroup().name()));
					}

					player.sendMessage("Voc� alterou sua tag para " + tag.getPrefix().replace(" ", "") + "�f.");
					player.setTag(tag);
				} else {
					player.sendMessage("Voc� n�o � o TOP 1�f.");
				}
				return;
			}

			if (tag == Tag.LOSER) {
				if (player.getStatus().getRankDeath() == 1) {
					for (Player players : Bukkit.getOnlinePlayers()) {
						Account account = PotePvP.getAccountCommon().getAccount(players.getUniqueId());

						if (account == null)
							continue;

						if (account.getTag() == Tag.LOSER)
							account.setTag(Tag.valueOf(account.getGroup().name()));
					}

					player.sendMessage("Voc� alterou sua tag para " + tag.getPrefix().replace(" ", "") + "�f.");
					player.setTag(tag);
				} else {
					player.sendMessage("Voc� n�o � o o �8�lTOP DEATHS�f.");
				}
				return;
			}

			if (tag == Tag.BETA) {
				if (player.isBeta() || player.hasServerGroup(Group.BETA)) {
					player.sendMessage("Voc� alterou sua tag para " + tag.getPrefix().replace(" ", "") + "�f.");
					player.setTag(tag);

					if (!player.isBeta())
						player.setBeta(true);
				} else {
					player.sendMessage("Voc� n�o tem permiss�o para essa tag.");
				}
				return;
			}

			if (tag.isExclusive()) {
				if (!player.isGroup(tag.getGroup()) && !player.hasServerGroup(Group.ADMIN)
						&& !player.hasPermission("tag." + tag.name())) {
					player.sendMessage("Voc� n�o tem permiss�o para essa tag.");
					return;
				}

				player.setTag(tag);
				player.sendMessage("Voc� alterou sua tag para " + tag.getPrefix().replace(" ", "") + "�f.");
				return;
			}

			if (!player.hasServerGroup(tag.getGroup())) {
				player.sendMessage("Voc� n�o tem permiss�o para essa tag.");
				return;
			}

			player.setTag(tag);
			player.sendMessage("Voc� alterou sua tag para "
					+ (tag == Tag.MEMBRO ? "�f�lMEMBRO" : tag.getPrefix().replace(" ", "")) + "�f.");
		}
	}

	@Completer(name = "tag")
	public List<String> tagCompleter(CommandArgs cmdArgs) {
		if (cmdArgs.isPlayer()) {
			ArrayList<String> tags = new ArrayList<>();
			String[] args = cmdArgs.getArgs();
			if (args.length == 1) {
				Player p = cmdArgs.getPlayer();
				Account player = (Account) PotePvP.getAccountCommon().getAccount(p.getUniqueId());
				String s = args[0].toUpperCase();
				for (Tag t : Tag.values()) {
					if (t.name().startsWith(s)) {
						if (t == Tag.TOP1) {
							if (player.getStatus().getRank() == 1) {
								tags.add(t.name());
							}
							continue;
						}
						if (t == Tag.LOSER) {
							if (player.getStatus().getRankDeath() == 1) {
								tags.add(t.name());
							}
							continue;
						}
						if (t.isExclusive()) {
							if (player.isGroup(t.getGroup()) || player.hasServerGroup(Group.ADMIN)
									|| player.hasPermission("tag." + t.name())) {
								tags.add(t.name());
							}
						} else {
							if (player.hasServerGroup(t.getGroup())) {
								tags.add(t.name());
							}
						}
					}
				}
				player = null;
				p = null;
			}
			args = null;
			return tags;
		} else {
			return new ArrayList<>();
		}
	}
}
