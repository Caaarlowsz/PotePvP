package br.com.yallandev.potepvp.commands.register;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import br.com.yallandev.potepvp.account.Account;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.Command;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.CommandArgs;
import br.com.yallandev.potepvp.manager.CommandClass;
import br.com.yallandev.potepvp.permissions.group.Group;
import br.com.yallandev.potepvp.tag.Tag;
import br.com.yallandev.potepvp.utils.ReflectionUtils;
import br.com.yallandev.potepvp.utils.Util;
import br.com.yallandev.potepvp.utils.fake.CustomPlayerAPI;
import br.com.yallandev.potepvp.utils.fake.CustomPlayerAPI.Skin;
import br.com.yallandev.potepvp.utils.string.CenterChat;
import br.com.yallandev.potepvp.utils.fake.FakePlayerUtils;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.MinecraftServer;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_7_R4.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_7_R4.PacketPlayOutPosition;
import net.minecraft.server.v1_7_R4.PacketPlayOutRespawn;
import net.minecraft.server.v1_7_R4.PlayerList;
import net.minecraft.server.v1_7_R4.WorldServer;
import net.minecraft.util.com.mojang.authlib.properties.Property;
import net.minecraft.util.com.mojang.authlib.properties.PropertyMap;

public class FakeCommand extends CommandClass {
	
	private List<String> randomFake;
	
	@Command(name = "fake", onlyPlayers = true, groupToUse = Group.YOUTUBER)
	public void onFake(CommandArgs cmdArgs) {
		Account player = cmdArgs.getAccount();
		Player p = cmdArgs.getPlayer();
		String[] a = cmdArgs.getArgs();
		
		if (a.length == 0) {
			player.sendMessage("Use §a/fake [Nick/Random]§f para alterar seu fake!");
			player.sendMessage("Use §a/fake list§f para ver todas as pessoas de fake!");
			player.sendMessage("Use §a/tfake §fpara retirar o seu fake!");
			return;
		}
		
		if (a[0].equalsIgnoreCase("list")) {
			List<Account> fakes = new ArrayList<>();
			
			
			for (Account accounts : getAccountCommon().getPlayers())
				if (!accounts.getUserName().equals(accounts.getFakeName()))
					fakes.add(accounts);
			
			if (fakes.isEmpty()) {
				player.sendMessage("Ninguem usando fake no momento!");
			} else {
				p.sendMessage(CenterChat.centered("Pessoas usando fake:"));
				p.sendMessage("");
				for (Account fake : fakes)
					p.sendMessage("§f- §a§l" + fake.getUserName() + "§8 -> §c§l" + fake.getFakeName());
			}
			return;
		}
		
		if (!player.getFakeName().equals(player.getUserName())) {
			player.sendMessage("Você já está usando um fake!");
			return;
		}
		
		String fakeName = a[0];
		
		if (a[0].equalsIgnoreCase("random")) {
			if (randomFake == null) {
				randomFake = new ArrayList<>();
				
				randomFake.add("SrAliciaPvP");
				randomFake.add("ScooutZinhoHG");
				randomFake.add("OCarinhaDoPvP");
				randomFake.add("BrincadeiraTemHR");
				randomFake.add("DeixaOsGRTBricar");
				randomFake.add("yThiaguinhoDEV_");
				randomFake.add("yAllanDecompiler");
				randomFake.add("POPOTAOGRANDAO");
				randomFake.add("JoaoESuaMae");
				randomFake.add("VelhoGUAGUA");
			}
			
			fakeName = randomFake.get(new Random().nextInt(randomFake.size()));
		}
		
		if (!FakePlayerUtils.validateName(fakeName)) {
			player.sendMessage("O fake §c\"" + fakeName + "\"§f é invalido!");
			return;
		}
		
		if (fakeName.length() > 16) {
			player.sendMessage("O fake §c\"" + fakeName + "\"§f é muito grande!");
			return;
		}
		if (fakeName.length() < 2) {
			player.sendMessage("O fake §c\"" + fakeName + "\"§f é muito pequeno!");
			return;
		}
		
		if (isPremium(fakeName)) {
			player.sendMessage("O fake §c\"" + fakeName + "\"§f é pertence a uma conta que já existe, escolha outro nick!");
			return;
		}
		
		player.setFakeName(fakeName);
		fakePlayer(player, ((CraftPlayer)p).getHandle(), fakeName);
        FakePlayerUtils.changePlayerName(p, fakeName);
        
        FakePlayerUtils.respawnPlayer(p, p.getWorld().getPlayers());
        
        p.setDisplayName(fakeName);
        p.setPlayerListName(fakeName);
        
        player.setTag(Tag.MEMBRO);
		player.sendMessage("Você alterou seu fake §a\"" + fakeName + "\"§f.");
	}
	
	@Command(name = "tfake", onlyPlayers = true, groupToUse = Group.YOUTUBER)
	public void onTFake(CommandArgs cmdArgs) {
		Account player = cmdArgs.getAccount();
		Player p = cmdArgs.getPlayer();
		String[] a = cmdArgs.getArgs();
		
		if (player.getFakeName().equals(player.getUserName())) {
			player.sendMessage("Você não está usando fake!");
			return;
		}
		
		player.setFakeName(player.getUserName());
		FakePlayerUtils.changePlayerName(p, player.getUserName(), true);
		player.sendMessage("Você resetou o seu fake!");	
		player.setTag(Tag.MEMBRO);
	}
	
	
	public void fakePlayer(Account p, EntityPlayer entityPlayer, String fake) {
		ReflectionUtils.setValue("name", entityPlayer.getProfile(), fake);
		
		Player skinPlayer = p.getPlayer().getWorld().getPlayers().get(new Random().nextInt(p.getPlayer().getWorld().getPlayers().size()));
		
		String value = CustomPlayerAPI.getFromPlayer(skinPlayer)[0];
		String signature = CustomPlayerAPI.getFromPlayer(skinPlayer)[1];
		
		PropertyMap propertyMap = new PropertyMap();
		propertyMap.put("textures", new Property("textures", value, signature));

		ReflectionUtils.setValue("properties", entityPlayer.getProfile(), propertyMap);
	}
	
	private String getUrlContent(String url) {
		try {
			InputStream connection = new URL(url).openStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection));
			String line = null;
			String content = "";
			while ((line = reader.readLine()) != null)
				content += line;
			return content;
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return null;
	}

	public boolean isPremium(String fake) {
		return getUrlContent("https://api.mojang.com/users/profiles/minecraft/" + fake).length() > 0;
	}

}
