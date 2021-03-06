package com.skionz.pingapi.v1_7_R4;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

import org.bukkit.craftbukkit.v1_7_R4.util.CraftIconCache;

import com.skionz.pingapi.PingReply;
import com.skionz.pingapi.ServerInfoPacket;

import net.minecraft.server.v1_7_R4.ChatComponentText;
import net.minecraft.server.v1_7_R4.PacketStatusOutServerInfo;
import net.minecraft.server.v1_7_R4.ServerPing;
import net.minecraft.server.v1_7_R4.ServerPingPlayerSample;
import net.minecraft.server.v1_7_R4.ServerPingServerData;
import net.minecraft.util.com.mojang.authlib.GameProfile;

public class ServerInfoPacketHandler implements ServerInfoPacket {

	private PingReply reply;

	public ServerInfoPacketHandler(PingReply reply) {
		this.reply = reply;
	}

	@Override
	public void send() {
		try {
			Field field = this.reply.getClass().getDeclaredField("ctx");
			field.setAccessible(true);
			Object ctx = field.get(this.reply);
			Method writeAndFlush = ctx.getClass().getMethod("writeAndFlush", Object.class);
			writeAndFlush.setAccessible(true);
			writeAndFlush.invoke(ctx, this.constructPacket());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public PingReply getPingReply() {
		return this.reply;
	}

	@Override
	public void setPingReply(PingReply reply) {
		this.reply = reply;
	}

	private PacketStatusOutServerInfo constructPacket() {
		GameProfile[] sample = new GameProfile[reply.getPlayerSample().size()];
		List<String> list = reply.getPlayerSample();
		for (int i = 0; i < list.size(); i++) {
			sample[i] = new GameProfile(UUID.randomUUID(), list.get(i));
		}
		ServerPingPlayerSample playerSample = new ServerPingPlayerSample(reply.getMaxPlayers(),
				reply.getOnlinePlayers());
		playerSample.a(sample);
		ServerPing ping = new ServerPing();
		ping.setMOTD(new ChatComponentText(reply.getMOTD()));
		ping.setPlayerSample(playerSample);
		ping.setServerInfo(new ServerPingServerData(reply.getProtocolName(), reply.getProtocolVersion()));
		ping.setFavicon(((CraftIconCache) reply.getIcon()).value);
		return new PacketStatusOutServerInfo(ping);
	}
}
