package br.com.yallandev.potepvp.utils.fake;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.Iterables;

import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.EntityLiving;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.MinecraftServer;
import net.minecraft.server.v1_7_R4.WorldServer;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import net.minecraft.util.com.mojang.authlib.properties.Property;

public class CustomPlayerAPI {

	public static HashMap<UUID, Skin> playerSkins = new HashMap<>();
	public static final Cache<GameProfile, Property> Textures = CacheBuilder.newBuilder()
			.expireAfterWrite(30L, TimeUnit.MINUTES).build(new CacheLoader<GameProfile, Property>() {
				@Override
				public Property load(GameProfile key) throws Exception {
					return loadTextures(key);
				}
			});

	public static class Skin {

		private String value;
		private String signature;

		public Skin(String value, String signature) {
			this.value = value;
			this.signature = signature;
		}

		public String getSignature() {
			return signature;
		}

		public String getValue() {
			return value;
		}
	}

	private static final Property loadTextures(GameProfile profile) {
		MinecraftServer.getServer().av().fillProfileProperties(profile, true);
		return Iterables.getFirst(profile.getProperties().get("textures"), null);
	}

	private static MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();

	public static MinecraftServer getNmsServer() {
		return nmsServer;
	}

	public static String[] getFromPlayer(Player playerBukkit) {
		EntityPlayer playerNMS = ((CraftPlayer) playerBukkit).getHandle();
		GameProfile profile = playerNMS.getProfile();
		Property property = profile.getProperties().get("textures").iterator().next();
		String texture = property.getValue();
		String signature = property.getSignature();

		return new String[] { texture, signature };
	}

	public static Skin getFromName(UUID uuid) {
		if (playerSkins.containsKey(uuid))
			return playerSkins.get(uuid);

		try {
			Property property = MojangAPI.getSkinProperty(uuid.toString());

			return new Skin(property.getValue(), property.getSignature());
		} catch (Exception e) {
//				Property property = MojangAPI.getSkinProperty(uuid.toString());
//				
//				return new Skin(property.getValue(), property.getSignature());
			System.err.println("Could not get skin data from session servers!");
			e.printStackTrace();
			return null;
		}
	}

	public static WorldServer getNmsWorld(World world) {
		return ((CraftWorld) world).getHandle();
	}

	public static void setHeadYaw(Entity en, float yaw) {
		if (!(en instanceof EntityLiving))
			return;
		EntityLiving handle = (EntityLiving) en;
		while (yaw < -180.0F) {
			yaw += 360.0F;
		}

		while (yaw >= 180.0F) {
			yaw -= 360.0F;
		}
		handle.aO = yaw;
		if (!(handle instanceof EntityHuman))
			handle.aM = yaw;
		handle.aP = yaw;
	}

}