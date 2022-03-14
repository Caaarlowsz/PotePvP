package br.com.yallandev.potepvp.clan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.caaarlowsz.potemc.kitpvp.PotePvP;
import com.github.caaarlowsz.potemc.kitpvp.PotePvP.Configuration;
import br.com.yallandev.potepvp.account.Account;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ClanCommon {

	private PotePvP main;
	private HashMap<String, Clan> clan;
	private HashMap<UUID, Map<UUID, Invite>> invite;

	public ClanCommon() {
		this.main = PotePvP.getInstance();
		this.clan = new HashMap<>();
		this.invite = new HashMap<>();
	}

	public class Invite {

		private Clan clan;
		private Long time;
		private UUID inviter;
		private String inviterName;

		public Invite(Clan clan, Account inviter) {
			this.clan = clan;
			this.time = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(60);
			this.inviter = inviter.getUuid();
			this.inviterName = inviter.getUserName();
		}

		public Clan getClan() {
			return clan;
		}

		public Long getTime() {
			return time;
		}

		public UUID getInviter() {
			return inviter;
		}

		public String getInviterName() {
			return inviterName;
		}

	}

	public void createInvite(Account player, Account target, Clan clan) {
		Map<UUID, Invite> invites = null;

		if (invite.containsKey(target.getUuid())) {
			invites = invite.get(target.getUuid());
		} else {
			invites = new HashMap<>();
		}

		if (this.invite.containsKey(target.getUuid()))
			for (Invite inv : this.invite.get(target.getUuid()).values()) {
				if (inv.getInviter().equals(target.getUuid())) {
					player.sendMessage("Voc� j� convidou esse jogador!");
					return;
				}
			}

		invites.put(player.getUuid(), new Invite(clan, player));
		this.invite.put(target.getUuid(), invites);

		TextComponent text = new TextComponent(Configuration.PREFIX.getMessage() + "Voc� foi convidado para o cl� �a"
				+ clan.getClanName() + " �f(" + clan.getClanTag().replace("&", "�") + "�f), clique ");

		TextComponent agree = new TextComponent("�a�lAQUI");
		agree.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan aceitar " + player.getUserName()));
		agree.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new ComponentBuilder("�aClique para aceitar o pedido").create()));
		text.addExtra(agree);

		text.addExtra(new TextComponent(" �fpara aceitar ou clique "));

		TextComponent reject = new TextComponent("�c�lAQUI");
		reject.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan rejeitar " + player.getUserName()));
		reject.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new ComponentBuilder("�cClique para rejeitar o pedido").create()));
		text.addExtra(reject);

		text.addExtra(new TextComponent(
				" �frejeitar o pedido de cl�, voc� tem �e120 segundos�f para responder ou a proposta ir� se expirar. "));

		target.getPlayer().spigot().sendMessage(text);
	}

	public void acceptInvite(Account player, UUID uuid) {
		if (!invite.containsKey(player.getUuid())) {
			player.sendMessage("Voc� n�o tem nenhum pedido para aceitar!");
			return;
		}

		if (invite.get(player.getUuid()).containsKey(uuid)) {
			if (invite.get(player.getUuid()).get(uuid).getTime() < System.currentTimeMillis()) {
				player.sendMessage("O pedido de cl� expirou!");
				invite.get(player.getUuid()).remove(uuid);
				return;
			}
		} else {
			return;
		}

		Clan clan = invite.get(player.getUuid()).get(uuid).getClan();
		String inviterName = invite.get(player.getUuid()).get(uuid).getInviterName();

		player.sendMessage("Voc� aceitou o pedido de cl� do �a" + inviterName + "�f.");
		broadcastClan(clan, "O jogador �a" + player.getUserName() + "�f entrou na cl�!");
		player.setClan(clan);
		clan.addParticipant(player.getUuid());

		invite.clear();
		main.getClanCommon().saveClan(clan);
	}

	public void rejectInvite(Account player, UUID uuid) {
		if (!invite.containsKey(player.getUuid())) {
			player.sendMessage("O pedido de cl� expirou!");
			return;
		}

		if (invite.get(player.getUuid()).containsKey(uuid)) {
			if (invite.get(player.getUuid()).get(uuid).getTime() < System.currentTimeMillis()) {
				player.sendMessage("O pedido de cl� foi rejetado!");
				invite.get(player.getUuid()).remove(uuid);
				return;
			}
		} else {
			return;
		}

		invite.get(player.getUuid()).remove(uuid);
		player.sendMessage("Voc� rejeitou o pedido de cl�!");
	}

	public void broadcastClan(Clan clan, String message) {
		for (Player players : Bukkit.getOnlinePlayers()) {
			Account player = PotePvP.getAccountCommon().getAccount(players.getUniqueId());

			if (player == null)
				continue;

			if (!player.hasClan())
				continue;

			if (player.getClan().getClanName().equalsIgnoreCase(clan.getClanName()))
				players.sendMessage(message);
		}
	}

	public void loadClan(String clanName, Clan clan) {
		if (this.clan.containsKey(clanName.toLowerCase()))
			return;

		this.clan.put(clanName.toLowerCase(), clan);
	}

	public Clan loadClanFromTag(String clanTag) {
		for (Clan clans : this.clan.values()) {
			if (clans.getClanTag().equalsIgnoreCase(clanTag))
				return clans;
		}

		try {
			Connection connection = PotePvP.getConnection().getConnection();
			PreparedStatement stmt = connection
					.prepareStatement("SELECT * FROM `clan` WHERE `ClanTag`='" + clanTag.toLowerCase() + "';");
			ResultSet result = stmt.executeQuery();

			if (result.next()) {
				Clan clan = PotePvP.getInstance().getGson().fromJson(result.getString("json"), Clan.class);
				return clan;
			}

			result.close();
			stmt.close();
			result = null;
			stmt = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public Clan loadClan(String clanName) {
		try {
			Connection connection = PotePvP.getConnection().getConnection();
			PreparedStatement stmt = connection
					.prepareStatement("SELECT * FROM `clan` WHERE `ClanName`='" + clanName.toLowerCase() + "';");
			ResultSet result = stmt.executeQuery();

			if (result.next()) {
				Clan clan = PotePvP.getInstance().getGson().fromJson(result.getString("json"), Clan.class);
				loadClan(clan.getClanName(), clan);
				loadStatus(clan);
			}

			result.close();
			stmt.close();
			result = null;
			stmt = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return getClan(clanName);
	}

	public Clan loadClan(String clanName, boolean value) {
		Clan clan = null;

		try {
			Connection connection = PotePvP.getConnection().getConnection();
			PreparedStatement stmt = connection
					.prepareStatement("SELECT * FROM `clan` WHERE `ClanName`='" + clanName.toLowerCase() + "';");
			ResultSet result = stmt.executeQuery();

			if (result.next()) {
				clan = PotePvP.getInstance().getGson().fromJson(result.getString("json"), Clan.class);
				loadStatus(clan);

				if (value)
					loadClan(clan.getClanName(), clan);
			}

			result.close();
			stmt.close();
			result = null;
			stmt = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return clan;
	}

	public Clan loadClanIfExistOrCreateClanIfNotExist(String clanName, String clanTag) {
		try {
			Connection connection = PotePvP.getConnection().getConnection();
			PreparedStatement stmt = connection
					.prepareStatement("SELECT * FROM `clan` WHERE `ClanName`='" + clanName.toLowerCase() + "';");
			ResultSet result = stmt.executeQuery();

			if (result.next()) {
				Clan clan = PotePvP.getInstance().getGson().fromJson(result.getString("json"), Clan.class);
				loadClan(clan.getClanName(), clan);
				loadStatus(clan);
			} else {
				loadClan(clanName, new Clan(clanName, clanTag, "", new ClanStatus(clanName, 0, 0, 0, 0)));
			}

			result.close();
			stmt.close();
			result = null;
			stmt = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return getClan(clanName);
	}

	public void loadStatus(Clan clan) {
		try {
			Connection connection = PotePvP.getConnection().getConnection();
			PreparedStatement stmt = connection.prepareStatement(
					"SELECT * FROM `clan_status` WHERE `ClanName`='" + clan.getClanName().toLowerCase() + "';");
			ResultSet result = stmt.executeQuery();

			if (result.next()) {
				clan.setClanStatus(new ClanStatus(clan.getClanName(), result.getInt("kills"), result.getInt("deaths"),
						result.getInt("xp"), result.getInt("money")));
			}

			result.close();
			stmt.close();
			result = null;
			stmt = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Clan getClan(String clanName) {
		if (this.clan.containsKey(clanName.toLowerCase()))
			return this.clan.get(clanName.toLowerCase());

		return null;
	}

	public void deleteClan(Clan clan) {
		try {
			PreparedStatement stmt = PotePvP.getConnection().prepareStatment(
					"DELETE FROM `clan` WHERE `ClanName` = '" + clan.getClanName().toLowerCase() + "';");
			stmt.executeUpdate();

			stmt = PotePvP.getConnection().prepareStatment(
					"DELETE FROM `clan_status` WHERE `ClanName` = '" + clan.getClanName().toLowerCase() + "';");
			stmt.executeUpdate();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		for (Player players : Bukkit.getOnlinePlayers()) {
			Account player = PotePvP.getAccountCommon().getAccount(players.getUniqueId());

			if (player == null)
				continue;

			if (!player.hasClan())
				continue;

			if (player.getClan().getClanName().equalsIgnoreCase(clan.getClanName())) {
				player.sendMessage("O seu cl� foi desfeito!");
				player.setClan(null);
			}
		}
	}

	public void saveClan(Clan clan) {
		String json = PotePvP.getInstance().getGson().toJson(clan);

		try {
			PreparedStatement stmt = PotePvP.getConnection().prepareStatment(
					"INSERT INTO `clan` (`ClanName`, `ClanTag`, `json`) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE `ClanTag`=VALUES(`ClanTag`), `json`=VALUES(`json`);");
			stmt.setString(1, clan.getClanName().toLowerCase());
			stmt.setString(2, clan.getClanTag().toLowerCase());
			stmt.setString(3, json);
			stmt.executeUpdate();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		for (Player players : Bukkit.getOnlinePlayers()) {
			Account player = PotePvP.getAccountCommon().getAccount(players.getUniqueId());

			if (player == null)
				continue;

			if (!player.hasClan())
				continue;

			if (player.getClan().getClanName().equalsIgnoreCase(clan.getClanName()))
				player.setClan(clan);
		}

		this.clan.put(clan.getClanName(), clan);

		try {
			PreparedStatement stmt = PotePvP.getConnection().prepareStatment(
					"INSERT INTO `clan_status` (`ClanName`, `kills`, `deaths`, `xp`) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE `kills`=VALUES(`kills`), `deaths`=VALUES(`deaths`), `xp`=VALUES(`xp`);");
			stmt.setString(1, clan.getClanName().toLowerCase());
			stmt.setInt(2, clan.getClanStatus().getKills());
			stmt.setInt(3, clan.getClanStatus().getDeaths());
			stmt.setInt(4, clan.getClanStatus().getXp());
			stmt.executeUpdate();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

}
