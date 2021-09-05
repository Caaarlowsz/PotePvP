package br.com.yallandev.potepvp.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import br.com.yallandev.potepvp.BukkitMain;
import br.com.yallandev.potepvp.BukkitMain.Configuration;
import br.com.yallandev.potepvp.ban.constructor.Ban;
import br.com.yallandev.potepvp.ban.constructor.Mute;
import br.com.yallandev.potepvp.ban.history.PunishmentHistory;
import br.com.yallandev.potepvp.clan.Clan;
import br.com.yallandev.potepvp.event.account.PlayerChangeTagEvent;
import br.com.yallandev.potepvp.permissions.group.Group;
import br.com.yallandev.potepvp.permissions.group.PaymentGroup;
import br.com.yallandev.potepvp.ranking.Ranking;
import br.com.yallandev.potepvp.server.ServerVersion;
import br.com.yallandev.potepvp.status.Status;
import br.com.yallandev.potepvp.tag.Tag;
import br.com.yallandev.potepvp.utils.Util;
import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.IChatBaseComponent;
import net.minecraft.server.v1_7_R4.PacketPlayOutChat;

public class Account {
	
	private UUID uuid;
	private String userName;
	private String fakeName;
	
	private boolean scoreboardEnable;
	private boolean alertEnable;
	
	private String ipAddress;
	private String lastIpAddress;
	
	private Tag tag;
	private Group group;
	private HashMap<PaymentGroup, Long> paymentGroup;
	private List<String> permissions;
	
	private String skinValue;
	private String skinSignature;
	
	private PunishmentHistory punishmentHistory;
	private HashMap<String, Long> timerMessage;
	
	private Ranking ranking;
	
	private boolean beta;
	
	private Clan clan;
	private ServerVersion serverVersion;
	
	public Account(UUID uuid, String userName, String ipAddress) {
		this.uuid = uuid;
		this.userName = userName;
		this.fakeName = userName;
		
		this.scoreboardEnable = true;
		this.alertEnable = false;
		
		this.ipAddress = ipAddress;
		this.lastIpAddress = ipAddress;
		
		this.tag = Tag.MEMBRO;
		this.group = Group.MEMBRO;
		this.paymentGroup = new HashMap<>();
		this.permissions = new ArrayList<>();
		
		this.punishmentHistory = new PunishmentHistory();
		this.timerMessage = new HashMap<>();
		
		this.ranking = Ranking.UNRANKED;
		
		this.beta = false;
		
		this.clan = null;
		
		this.skinSignature = null;
		this.skinValue = null;
		
		this.serverVersion = ServerVersion.BETA;
	}
	
	public ServerVersion getServerVersion() {
		if (serverVersion == null)
			return ServerVersion.NONE;
		
		return serverVersion;
	}
	
	public void setServerVersion(ServerVersion serverVersion) {
		this.serverVersion = serverVersion;
	}
	
	public boolean isBeta() {
		return beta;
	}
	
	public void setBeta(boolean beta) {
		this.beta = beta;
	}
	
	public void setSkinSignature(String skinSignature) {
		this.skinSignature = skinSignature;
	}
	
	public String getSkinSignature() {
		return skinSignature;
	}
	
	public void setSkinValue(String skinValue) {
		this.skinValue = skinValue;
	}
	
	public String getSkinValue() {
		return skinValue;
	}
	
	public Clan getClan() {
		return clan;
	}
	
	public boolean hasClan() {
		return clan != null;
	}
	
	public void setClan(Clan clan) {
		this.clan = clan;
	}
	
	public boolean isScoreboardEnable() {
		return scoreboardEnable;
	}
	
	public boolean isAlertEnable() {
		return alertEnable;
	}
	
	public void setScoreboardEnable(boolean scoreboardEnable) {
		this.scoreboardEnable = scoreboardEnable;
	}
	
	public void setAlertEnable(boolean alertEnable) {
		this.alertEnable = alertEnable;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public String getFakeName() {
		return fakeName;
	}
	
	public void setFakeName(String fakeName) {
		this.fakeName = fakeName;
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	public List<Ban> getBanHistory() {
		return punishmentHistory.getBanHistory();
	}
	
	public List<Mute> getMuteHistory() {
		return punishmentHistory.getMuteHistory();
	}
	
	public PunishmentHistory getPunishmentHistory() {
		return punishmentHistory;
	}
	
	public HashMap<PaymentGroup, Long> getPayment() {
		return paymentGroup;
	}
	
	public void setGroup(Group group) {
		this.group = group;
	}
	
	public void checkVip() {
		if (!getPayment().isEmpty()) {
			Iterator<Entry<PaymentGroup, Long>> it = getPayment().entrySet().iterator();
			while (it.hasNext()) {
				Entry<PaymentGroup, Long> entry = it.next();
				if (System.currentTimeMillis() > entry.getValue()) {
					it.remove();
					sendMessage("O seu vip " + Tag.valueOf(entry.getKey().toString()).getPrefix().replace(" ", "") + "§f acabou!");
					sendMessage("Você pode adquiri-ló novamente no nosso site: §a§n" + Configuration.SITE.getMessage() + "§f.");
					setTag(Tag.valueOf(getServerGroup().name()));
				}
			}
		}
	}
	
	public Group getGroup() {
		Group group = Group.MEMBRO;

		group = this.group;

		if (group == Group.MEMBRO) {
			if (!getPayment().isEmpty()) {
				PaymentGroup expire = null;
				for (Entry<PaymentGroup, Long> expireRank : getPayment().entrySet()) {
					if (expire == null) {
						expire = expireRank.getKey();
					} else if (expireRank.getKey().ordinal() > expire.ordinal()) {
						expire = expireRank.getKey();
					}
				}

				if (expire != null)
					group = Group.valueOf(expire.name());
			}
		}

		return group;
	}
	
	public Group getServerGroup() {
		Group group = Group.MEMBRO;

		group = this.group;

		if (group == Group.MEMBRO) {
			if (!getPayment().isEmpty()) {
				PaymentGroup expire = null;
				for (Entry<PaymentGroup, Long> expireRank : getPayment().entrySet()) {
					if (expire == null) {
						expire = expireRank.getKey();
					} else if (expireRank.getKey().ordinal() > expire.ordinal()) {
						expire = expireRank.getKey();
					}
				}

				if (expire != null)
					group = Group.valueOf(expire.name());
			}
		}

		return group;
	}

	public boolean hasServerGroup(Group group) {
		if (getGroup() == Group.YOUTUBERPLUS) {
			return Group.MOD.ordinal() >= group.ordinal();
		}
		
		return getServerGroup() != null && getServerGroup().ordinal() >= group.ordinal();
	}
	
	public boolean isStaff() {
		if (this.group.ordinal() > Group.HELPER.ordinal()) {
			return true;
		}
		
		return false;
	}

	public void updateCache() {
		checkVip();
	}
	
	public Player getPlayer() {
		Player player = Bukkit.getPlayer(getUuid());
		
		if (player == null) {
			player = Bukkit.getPlayer(this.userName);
			
			if (player == null)
				player = Bukkit.getPlayer(this.fakeName);
		}
		
		return player;
	}

	public boolean isOnline() {
		return Bukkit.getPlayer(this.userName) != null || Bukkit.getPlayer(this.fakeName) != null;
	}

	public boolean hasGroup(Group group) {
		return hasServerGroup(group);
	}
	
	public boolean isGroup(Group group) {
		return getGroup() == group;
	}

	public void sendMessage(String message) {
		if (isOnline())
			getPlayer().sendMessage(Configuration.CHAT_PREFIX.getMessage() + message);
	}
	
	public String getIpAddress() {
		return ipAddress;
	}
	
	public String getLastIpAddress() {
		return lastIpAddress;
	}
	
	public void setIpAddress(String ipAddress) {
		this.lastIpAddress = this.ipAddress;
		this.ipAddress = ipAddress;
	}
	
	public boolean hasPermission(String string) {
		if (isOnline())
			return getPlayer().hasPermission(string.toLowerCase()) || getPermissions().contains(string.toLowerCase());
		
		return getPermissions().contains(string.toLowerCase());
	}
	
	public boolean addPermission(String permission) {
		if (getPermissions().contains(permission.toLowerCase()))
			return false;
		
		getPermissions().add(permission.toLowerCase());
		return true;
	}
	
	public List<String> getPermissions() {
		return permissions;
	}
	
	public void sendAction(String text) {
		if (((CraftPlayer)getPlayer()).getHandle().playerConnection.networkManager.getVersion() < 47) {
			return;
		}
        IChatBaseComponent comp = ChatSerializer.a("{\"text\":\"" + text + " \"}");
        PacketPlayOutChat bar = new PacketPlayOutChat(comp, 2);
        ((CraftPlayer)getPlayer()).getHandle().playerConnection.sendPacket(bar);
	}
	
	public Status getStatus() {
		if (BukkitMain.getInstance().getPlayerManager().getStatus(getUuid()) != null)
			return BukkitMain.getInstance().getPlayerManager().getStatus(getUuid());
		
		return BukkitMain.getInstance().getPlayerManager().loadStatus(getUuid(), false);
	}

	public void saveStatus() {
		if (getStatus() != null)
		BukkitMain.getInstance().getPlayerManager().saveStatus(getUuid(), getStatus());
	}
	
	public void sendTimerMessage(String message) {
		if (timerMessage.containsKey(message)) {
			if (timerMessage.get(message) > System.currentTimeMillis())
				return;
		}
		
		timerMessage.put(message, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(3));
		sendMessage(message);
	}
	
	public Ranking getRanking() {
		return ranking;
	}
	
	public int multiplier() {
		if (hasGroup(Group.BETA))
			return 3;
		if (hasGroup(Group.MVP))
			return 2;
		return 1;
	}
	
	public boolean setTag(Tag tag) {
		return setTag(tag, false);
	}
	
	public Tag getTag() {
		return tag;
	}
	
	public boolean setTag(Tag tag, boolean forcetag) {
		if (!isOnline()) {
			this.tag = tag;
			return false;
		}
		
		PlayerChangeTagEvent event = new PlayerChangeTagEvent(getPlayer(), getTag(), tag, forcetag);
		BukkitMain.getPlugin().getServer().getPluginManager().callEvent(event);
		if (!event.isCancelled()) {
			this.tag = tag;
		}
		return !event.isCancelled();
	}
	
	public void updateVanished() {
		if (getPlayer() == null)
			return;
		
		for (Player p : Util.getOnlinePlayers()) {
			if (!p.getName().equals(getPlayer().getName())) {
				Account Account = BukkitMain.getAccountCommon().getAccount(p.getUniqueId());

				if (Account == null)
					continue;

				if (BukkitMain.getInstance().getVanishMode().isAdmin(p.getUniqueId())) {
					if (BukkitMain.getInstance().getVanishMode().isAdmin(getUuid())) {
						getPlayer().showPlayer(p);
					} else {
						getPlayer().hidePlayer(p);
					}
				} else {
					getPlayer().showPlayer(p);
				}
			}
		}

		for (Player players : Util.getOnlinePlayers()) {
			if (!players.getName().equals(getPlayer().getName())) {
				Account Account = BukkitMain.getAccountCommon().getAccount(players.getUniqueId());

				if (Account == null)
					continue;

				if (BukkitMain.getInstance().getVanishMode().isAdmin(getPlayer().getUniqueId())) {
					if (BukkitMain.getInstance().getVanishMode().isAdmin(Account.getUuid())) {
						players.showPlayer(getPlayer());
					} else {
						players.hidePlayer(getPlayer());
					}
				} else {
					players.showPlayer(getPlayer());
				}
			}
		}
		
		BukkitMain.getInstance().getPlayerHideManager().playerJoin(getPlayer());
	}
	
	public void makeVanish() {
		if (getPlayer() == null)
			return;
		
		for (Player player :  Util.getOnlinePlayers()) {
			if (!player.getName().equals(getPlayer().getName())) {
				Account Account = BukkitMain.getAccountCommon().getAccount(player.getUniqueId());

				if (Account == null)
					continue;
				
				if (Account.hasServerGroup(Group.YOUTUBERPLUS)) {
					if (BukkitMain.getInstance().getVanishMode().isAdmin(Account.getUuid())) {
						player.showPlayer(getPlayer());
					} else {
						player.hidePlayer(getPlayer());
					}
				} else {
					if (player.canSee(getPlayer())) {
						player.hidePlayer(getPlayer());
					}
				}
			}
		}
	}

	public void desmakeVanish() {
		if (getPlayer() == null)
			return;
		
		for (Player player : Util.getOnlinePlayers()) {
			if (!player.getName().equals(getPlayer().getName())) {
				player.showPlayer(getPlayer());
			}
		}
	}
	
	public void setRanking(Ranking ranking) {
		this.ranking = ranking;
	}
	
	public void setUserName(String userName) {
		if (userName.equals(this.userName))
			return;
		
		System.out.println("Alterando nome do jogador " + this.userName + " para " + userName);
		
		this.userName = userName;
	}

	public void checkRank() {
		Ranking actualRanking = getRanking();
		
		if (actualRanking.getXp() < getStatus().getXp()) {
			Ranking newRanking = Ranking.values()[actualRanking.ordinal() + 1];
			getStatus().setXp(getStatus().getXp() - newRanking.getXp());
			setRanking(newRanking);
			sendMessage("Você subiu para o Ranking §a" + newRanking.name() + "§f.");
			checkRank();
			return;
		}
//		Ranking actualRanking = getRanking();
//		
//		System.out.println("Testando o rank!");
//		
//		if (actualRanking == Ranking.POTTER)
//			return;
//		
//		System.out.println("Passou o rank potter!");
//		
//		if (getStatus().getXp() > actualRanking.getXp()) {
//			Ranking newRanking = Ranking.values()[actualRanking.ordinal() + 1];
//			
//			sendMessage("Você upou para o ranking §a" + newRanking.name() + "§f.");
//			setRanking(newRanking);
//			System.out.println("Upou para o rank " + newRanking.name());
//			checkRank();
//			return;
//		}
//		
//		System.out.println("Passou do update");
//		
//		if (actualRanking == Ranking.UNRANKED)
//			return;
//		
//		System.out.println("Passou do unranked!");
//		
//		Ranking lastedRanking = Ranking.values()[actualRanking.ordinal() - 1];
//		
//		if (lastedRanking.getXp() > getStatus().getXp()) {
//			sendMessage("Você desceu para o ranking §a" + lastedRanking.name() + "§f.");
//			setRanking(lastedRanking);
//			System.out.println("Desceu para o rank " + lastedRanking.name() + ".");
//			return;
//		}
		
		
	}

	public boolean hasFake() {
		return !getUserName().equals(getFakeName());
	}

}
