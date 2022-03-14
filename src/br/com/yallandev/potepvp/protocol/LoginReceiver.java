package br.com.yallandev.potepvp.protocol;

import org.bukkit.entity.Player;

import com.comphenix.tinyprotocol.NMSReflection;
import com.comphenix.tinyprotocol.NMSReflection.FieldAccessor;
import com.comphenix.tinyprotocol.v1_7.TinyProtocol;

import com.github.caaarlowsz.potemc.kitpvp.PotePvP;
import com.github.caaarlowsz.potemc.kitpvp.PotePvP.Configuration;
import br.com.yallandev.potepvp.check.Check;
import br.com.yallandev.potepvp.exception.InvalidCheckException;
import br.com.yallandev.potepvp.utils.Util;
import br.com.yallandev.potepvp.version.Version;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import net.minecraft.util.io.netty.channel.Channel;

public class LoginReceiver extends TinyProtocol {

	private Version version = Version.getPackageVersion();

	private FieldAccessor<GameProfile> profileField = NMSReflection.getField("{nms}.PacketLoginInStart",
			GameProfile.class, 0);

	@Override
	public Object onPacketInAsync(Player receiver, Channel channel, Object packet) {
		if (this.profileField.hasField(packet)) {
			if (this.receiveLogin(packet, channel)) {
				return null;
			}
		}
		return super.onPacketInAsync(receiver, channel, packet);
	}

	private boolean receiveLogin(Object packet, Channel channel) {
		boolean check;
		try {
			check = Check.fastCheck(this.profileField.get(packet).getName());
		} catch (InvalidCheckException e) {
			e.printStackTrace();
			return false;
		}
		PotePvP.getStorage().setPremium(this.profileField.get(packet).getName(), check);
		if (check) {
			return false;
		}
		if (!Configuration.LOGIN.isActive()) {
			return false;
		}
		try {
			Class<?> loginClass = Class.forName("br.com.yallandev.potepvp.version.rewrite.v1_7_R4");
			loginClass.getConstructors()[0].newInstance(Util.networkList(channel.remoteAddress(),
					this.version.getServerConnection(), this.version.getNetworkManager()),
					this.profileField.get(packet).getName());
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
		return true;
	}

}
