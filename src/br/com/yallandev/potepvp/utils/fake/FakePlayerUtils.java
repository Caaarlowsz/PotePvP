package br.com.yallandev.potepvp.utils.fake;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import com.github.caaarlowsz.potemc.kitpvp.PotePvP;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.EnumGamemode;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_7_R4.PacketPlayOutHeldItemSlot;
import net.minecraft.server.v1_7_R4.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_7_R4.PacketPlayOutPosition;
import net.minecraft.server.v1_7_R4.PacketPlayOutRespawn;
import net.minecraft.server.v1_7_R4.PlayerConnection;
import net.minecraft.server.v1_7_R4.WorldServer;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import net.minecraft.util.com.mojang.authlib.properties.Property;

public class FakePlayerUtils {

	public static void changePlayerName(Player player, String name) {
		changePlayerName(player, name, true);
	}

	public static void changePlayerName(Player player, String name, boolean respawn) {
		Collection<? extends Player> players = player.getWorld().getPlayers();
		EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
		GameProfile playerProfile = entityPlayer.getProfile();

		if (respawn) {
			removeFromTab(player, players);
		}

		try {
			Field field = playerProfile.getClass().getDeclaredField("name");
			field.setAccessible(true);
			field.set(playerProfile, name);
			field.setAccessible(false);
			entityPlayer.getClass().getDeclaredField("displayName").set(entityPlayer, name);
			entityPlayer.getClass().getDeclaredField("listName").set(entityPlayer, name);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (respawn) {
			respawnPlayer(player, players);
		}
	}

	public static void removePlayerSkin(Player player) {
		removePlayerSkin(player, true);
	}

	public static void removePlayerSkin(Player player, boolean respawn) {
		EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
		GameProfile playerProfile = entityPlayer.getProfile();
		playerProfile.getProperties().clear();
		if (respawn) {
			respawnPlayer(player, player.getWorld().getPlayers());
		}
	}

	public static void changePlayerSkin(Player player, String name, UUID uuid) {
		changePlayerSkin(player, name, uuid, true);
	}

	public static void changePlayerSkin(Player player, String name, UUID uuid, boolean respawn) {
		EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
		GameProfile playerProfile = entityPlayer.getProfile();
		playerProfile.getProperties().clear();
		try {
			playerProfile.getProperties().put("textures",
					(Property) CustomPlayerAPI.Textures.get(new GameProfile(uuid, name)));
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		if (respawn) {
			respawnPlayer(player, player.getWorld().getPlayers());
		}
	}

	public void addToTab(Player player, Collection<? extends Player> players) {
		PacketPlayOutPlayerInfo addPlayerInfo = PacketPlayOutPlayerInfo.addPlayer(((CraftPlayer) player).getHandle());
		PacketPlayOutPlayerInfo updatePlayerInfo = PacketPlayOutPlayerInfo
				.updateDisplayName(((CraftPlayer) player).getHandle());
		for (Player online : players) {
			if (online.canSee(player)) {
				((CraftPlayer) online).getHandle().playerConnection.sendPacket(addPlayerInfo);
				((CraftPlayer) online).getHandle().playerConnection.sendPacket(updatePlayerInfo);
			}
		}
	}

	public static void removeFromTab(Player player, Collection<? extends Player> players) {
		PacketPlayOutPlayerInfo removePlayerInfo = PacketPlayOutPlayerInfo
				.removePlayer(((CraftPlayer) player).getHandle());
		for (Player online : players) {
			if (online.canSee(player)) {
				((CraftPlayer) online).getHandle().playerConnection.sendPacket(removePlayerInfo);
			}
		}
	}

	public static void respawnPlayer(Player p, Collection<? extends Player> players) {
		try {
			if (!p.isOnline()) {
				return;
			}

			CraftPlayer cp = (CraftPlayer) p;
			EntityPlayer ep = cp.getHandle();
			int entId = ep.getId();
			Location l = p.getLocation();

			PacketPlayOutPlayerInfo removeInfo = PacketPlayOutPlayerInfo.removePlayer(ep);
			PacketPlayOutEntityDestroy removeEntity = new PacketPlayOutEntityDestroy(entId);
			PacketPlayOutNamedEntitySpawn addNamed = new PacketPlayOutNamedEntitySpawn(ep);
			PacketPlayOutPlayerInfo addInfo = PacketPlayOutPlayerInfo.addPlayer(ep);

			PacketPlayOutRespawn respawn = new PacketPlayOutRespawn(((WorldServer) ep.getWorld()).dimension,
					ep.getWorld().difficulty, ep.getWorld().worldData.getType(),
					EnumGamemode.getById(p.getGameMode().getValue()));

			PacketPlayOutPosition pos = new PacketPlayOutPosition(l.getX(), l.getY(), l.getZ(), l.getYaw(),
					l.getPitch(), false);
			PacketPlayOutEntityEquipment itemhand = new PacketPlayOutEntityEquipment(entId, 0,
					CraftItemStack.asNMSCopy(p.getItemInHand()));
			PacketPlayOutEntityEquipment helmet = new PacketPlayOutEntityEquipment(entId, 4,
					CraftItemStack.asNMSCopy(p.getInventory().getHelmet()));
			PacketPlayOutEntityEquipment chestplate = new PacketPlayOutEntityEquipment(entId, 3,
					CraftItemStack.asNMSCopy(p.getInventory().getChestplate()));
			PacketPlayOutEntityEquipment leggings = new PacketPlayOutEntityEquipment(entId, 2,
					CraftItemStack.asNMSCopy(p.getInventory().getLeggings()));
			PacketPlayOutEntityEquipment boots = new PacketPlayOutEntityEquipment(entId, 1,
					CraftItemStack.asNMSCopy(p.getInventory().getBoots()));
			PacketPlayOutHeldItemSlot slot = new PacketPlayOutHeldItemSlot(p.getInventory().getHeldItemSlot());

			for (Player pOnline : (Bukkit.getServer()).getOnlinePlayers()) {
				final CraftPlayer craftOnline = (CraftPlayer) pOnline;
				PlayerConnection con = craftOnline.getHandle().playerConnection;

				if (pOnline.equals(p)) {
					con.sendPacket(removeInfo);
					con.sendPacket(addInfo);
					con.sendPacket(respawn);
					con.sendPacket(pos);
					con.sendPacket(slot);

					craftOnline.updateScaledHealth();
					craftOnline.getHandle().triggerHealthUpdate();
					craftOnline.updateInventory();

					if (pOnline.isOp()) {
						pOnline.setOp(false);
						pOnline.setOp(true);
					}

					Bukkit.getScheduler().runTask(PotePvP.getInstance(), new Runnable() {

						@Override
						public void run() {
							craftOnline.getHandle().updateAbilities();

						}

					});
					continue;
				}

				if (pOnline.canSee(p) && pOnline.getWorld().equals(p.getWorld())) {
					con.sendPacket(removeEntity);
					con.sendPacket(removeInfo);
					con.sendPacket(addInfo);
					con.sendPacket(addNamed);
					con.sendPacket(itemhand);
					con.sendPacket(helmet);
					con.sendPacket(chestplate);
					con.sendPacket(leggings);
					con.sendPacket(boots);
				} else {
					con.sendPacket(removeInfo);
					con.sendPacket(addInfo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean validateName(String username) {
		Pattern pattern = Pattern.compile("[a-zA-Z0-9_]{1,16}");
		Matcher matcher = pattern.matcher(username);
		return matcher.matches();
	}
}
