package br.com.yallandev.potepvp.clan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import br.com.yallandev.potepvp.BukkitMain;
import br.com.yallandev.potepvp.account.Account;
import br.com.yallandev.potepvp.status.Status;

public class Clan {
	
	private String clanName;
	private String clanTag;
	private String clanDescription;
	private HashMap<UUID, MemberType> participants;
	private ClanStatus clanStatus;
	private int maxPlayers;
	
	public Clan(String clanName, String clanTag, String clanDescription, ClanStatus clanStatus) {
		this.clanName = clanName;
		this.clanTag = clanTag;
		this.clanStatus = clanStatus;
		this.clanDescription = clanDescription;
		this.participants = new HashMap<>();
		this.maxPlayers = 14;
	}
	
	public Clan(String clanName, String clanTag, String clanDescription, ClanStatus clanStatus, UUID owner) {
		this.clanName = clanName;
		this.clanTag = clanTag;
		this.clanStatus = clanStatus;
		this.clanDescription = clanDescription;
		this.participants = new HashMap<>();
		
		this.participants.put(owner, MemberType.OWNER);
		this.maxPlayers = 14;
	}
	
	public boolean hasAdmin(UUID uuid) {
		if (this.participants.containsKey(uuid))
			return this.participants.get(uuid).ordinal() < MemberType.ADMININISTRATOR.ordinal();
			
		return false;
	}
	
	public boolean isOwner(UUID uuid) {
		if (this.participants.containsKey(uuid))
			return this.participants.get(uuid) == MemberType.OWNER;
			
		return false;
	}
	
	public String getClanName() {
		return clanName;
	}
	
	public void setClanName(String clanName) {
		this.clanName = clanName;
	}
	
	public String getClanTag() {
		return clanTag;
	}
	
	public void setClanTag(String clanTag) {
		this.clanTag = clanTag;
	}
	
	public ClanStatus getClanStatus() {
		return clanStatus;
	}
	
	public void setClanStatus(ClanStatus clanStatus) {
		this.clanStatus = clanStatus;
	}
	
	public void addParticipant(UUID uuid) {
		if (this.participants.containsKey(uuid))
			return;
		
		this.participants.put(uuid, MemberType.MEMBER);
	}
	
	public boolean isParticipant(UUID uuid) {
		return this.participants.containsKey(uuid);
	}
	
	public void removeParticipant(UUID uuid) {
		if (!this.participants.containsKey(uuid))
			return;
		
		this.participants.remove(uuid);
	}
	
	public int size() {
		return this.participants.size();
	}
	
	public UUID getOwner() {
		UUID uuid = null;
		
		for (UUID uuids : this.participants.keySet()) {
			if (this.participants.get(uuids) == MemberType.OWNER) {
				uuid = uuids;
				break;
			}
		}
		
		return uuid;
	}
	
	public String getOwnerName() {
		UUID uuid = getOwner();
		
		Account owner = BukkitMain.getAccountCommon().loadAccount(uuid, false);
		
		return owner.getUserName();
	}
	
	public List<String> getNameOfAdmins() {
		List<String> names = new ArrayList<>();
		
		for (UUID uuids : this.participants.keySet()) {
			if (this.participants.get(uuids) == MemberType.ADMININISTRATOR) {
				UUID uuid = getOwner();
				
				Account admins = BukkitMain.getAccountCommon().loadAccount(uuid, false);
				
				if (admins == null)
					continue;
				
				names.add(admins.getUserName());
			}
		}
		
		return names;
	}
	
	public List<Account> getAccountOfAdmins() {
		List<Account> names = new ArrayList<>();
		
		for (UUID uuids : this.participants.keySet()) {
			if (this.participants.get(uuids) == MemberType.ADMININISTRATOR) {
				Account admins = BukkitMain.getAccountCommon().loadAccount(uuids, false);
				
				if (admins == null)
					continue;
				
				names.add(admins);
			}
		}
		
		return names;
	}
	
	public List<Account> getAccountOfMembers() {
		List<Account> names = new ArrayList<>();
		
		for (UUID uuids : this.participants.keySet()) {
			if (this.participants.get(uuids) == MemberType.MEMBER) {
				Account admins = BukkitMain.getAccountCommon().loadAccount(uuids, false);
				
				if (admins == null)
					continue;
				
				names.add(admins);
			}
		}
		
		return names;
	}
	
	public HashMap<UUID, MemberType> getParticipants() {
		return participants;
	}
	
	public int getMaxPlayers() {
		return maxPlayers;
	}
}
