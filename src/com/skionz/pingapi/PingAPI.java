package com.skionz.pingapi;

import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import br.com.yallandev.potepvp.BukkitMain;

public class PingAPI extends JavaPlugin {

	public static void registerListener(PingListener listener) {
		BukkitMain.listeners.add(listener);
	}

	public static List<PingListener> getListeners() {
		return BukkitMain.listeners;
	}
}