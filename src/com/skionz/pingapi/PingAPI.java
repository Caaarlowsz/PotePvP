package com.skionz.pingapi;

import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.caaarlowsz.potemc.kitpvp.PotePvP;

public class PingAPI extends JavaPlugin {

	public static void registerListener(PingListener listener) {
		PotePvP.listeners.add(listener);
	}

	public static List<PingListener> getListeners() {
		return PotePvP.listeners;
	}
}