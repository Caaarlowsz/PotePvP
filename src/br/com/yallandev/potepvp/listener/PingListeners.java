package br.com.yallandev.potepvp.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;

import com.skionz.pingapi.PingEvent;
import com.skionz.pingapi.PingListener;
import com.skionz.pingapi.PingReply;

import com.github.caaarlowsz.potemc.kitpvp.PotePvP;
import br.com.yallandev.potepvp.utils.string.CenterChat;

public class PingListeners implements PingListener {

	@Override
	public void onPing(PingEvent event) {
		PingReply reply = event.getReply();
		reply.setOnlinePlayers(
				reply.getOnlinePlayers() - PotePvP.getInstance().getVanishMode().getAdmin().size() < 0 ? 0
						: reply.getOnlinePlayers() - PotePvP.getInstance().getVanishMode().getAdmin().size());
		reply.setMaxPlayers(reply.getOnlinePlayers() + 1);
		reply.setProtocolName("�7" + reply.getOnlinePlayers() + "�a/�7" + reply.getMaxPlayers());
		reply.setMOTD(CenterChat.centered("�6�lKitPvP �7[1.7 - 1.8]", 127) + "\n" + CenterChat
				.centered((Bukkit.hasWhitelist() ? "�c�nServidor est� em manuten��o!" : "�e�nVagas na staff!"), 127));

		List<String> list = new ArrayList<>();
		list.add("       �6�lKitPvP");
		list.add(" �f");
		list.add("�fVers�o: �a1.7 at� 1.8");
		reply.setPlayerSample(list);
	}

}