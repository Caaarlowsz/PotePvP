package br.com.yallandev.potepvp.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Listener;

import br.com.yallandev.potepvp.BukkitMain;
import br.com.yallandev.potepvp.utils.Config;

public class WarpManager extends Config {

	private HashMap<String, Warp> warps;
	private Warp screenshare;
	
	public WarpManager() {
		super("warps.yml");
		
		this.warps = new HashMap<>();
		
		if (getConfig().contains("ss.World")) {
			this.screenshare = new Warp("Screenshare", new Location(Bukkit.getWorld(getConfig().getString("ss.World")), getConfig().getDouble("ss.X"), getConfig().getDouble("ss.Y"), getConfig().getDouble("ss.Z")), Material.DIAMOND, 9999);
		} else {
			this.screenshare = new Warp("Screenshare", new Location(Bukkit.getWorld("world"), 0, 120, 0), Material.DIAMOND, 9999);
		}
		
		for (String list : getConfig().getConfigurationSection("warps").getKeys(false)) {
			String warpName = list.substring(0, 1).toUpperCase() + list.substring(1, list.length());
			
			addWarp(warpName);
		}
		
		
		if (getConfig().contains("1v1.Spawn")) {
			Location spawnLocation = new Location(Bukkit.getWorld(getConfig().getString("1v1.Spawn.World")), getConfig().getDouble("1v1.Spawn.X"), getConfig().getDouble("1v1.Spawn.Y"), getConfig().getDouble("1v1.Spawn.Z"));
			
			if (getConfig().contains("1v1.Spawn.Yaw"))
				spawnLocation.setYaw(getConfig().getFloat("1v1.Spawn.Yaw"));
			
			if (getConfig().contains("1v1.Spawn.Pitch"))
				spawnLocation.setYaw(getConfig().getFloat("1v1.Spawn.Pitch"));
			
			Location firstLocation = new Location(Bukkit.getWorld(getConfig().getString("1v1.Posicao1.World")), getConfig().getDouble("1v1.Posicao1.X"), getConfig().getDouble("1v1.Posicao1.Y"), getConfig().getDouble("1v1.Posicao1.Z"));
			
			if (getConfig().contains("1v1.Posicao1.Yaw"))
				firstLocation.setYaw(getConfig().getFloat("1v1.Posicao1.Yaw"));
			
			if (getConfig().contains("1v1.Posicao1.Pitch"))
				firstLocation.setYaw(getConfig().getFloat("1v1.Posicao1.Pitch"));
			
			Location secondLocation = new Location(Bukkit.getWorld(getConfig().getString("1v1.Posicao2.World")), getConfig().getDouble("1v1.Posicao2.X"), getConfig().getDouble("1v1.Posicao2.Y"), getConfig().getDouble("1v1.Posicao2.Z"));
			
			if (getConfig().contains("1v1.Posicao2.Yaw"))
				secondLocation.setYaw(getConfig().getFloat("1v1.Posicao2.Yaw"));
			
			if (getConfig().contains("1v1.Posicao2.Pitch"))
				secondLocation.setYaw(getConfig().getFloat("1v1.Posicao2.Pitch"));
			
			this.warps.put("1v1", new Warp1v1("1v1", spawnLocation, Material.BLAZE_ROD, firstLocation, secondLocation));
		}
		
		if (getConfig().contains("Sumo.Spawn")) {
			Location spawnLocation = new Location(Bukkit.getWorld(getConfig().getString("Sumo.Spawn.World")), getConfig().getDouble("Sumo.Spawn.X"), getConfig().getDouble("Sumo.Spawn.Y"), getConfig().getDouble("Sumo.Spawn.Z"));
		
			if (getConfig().contains("Sumo.Spawn.Yaw"))
				spawnLocation.setYaw(getConfig().getFloat("Sumo.Spawn.Yaw"));
		
			if (getConfig().contains("Sumo.Spawn.Pitch"))
				spawnLocation.setYaw(getConfig().getFloat("Sumo.Spawn.Pitch"));
		
			Location firstLocation = new Location(Bukkit.getWorld(getConfig().getString("Sumo.Posicao1.World")), getConfig().getDouble("Sumo.Posicao1.X"), getConfig().getDouble("Sumo.Posicao1.Y"), getConfig().getDouble("Sumo.Posicao1.Z"));
		
			if (getConfig().contains("Sumo.Posicao1.Yaw"))
				firstLocation.setYaw(getConfig().getFloat("Sumo.Posicao1.Yaw"));
		
			if (getConfig().contains("Sumo.Posicao1.Pitch"))
				firstLocation.setYaw(getConfig().getFloat("Sumo.Posicao1.Pitch"));
		
			Location secondLocation = new Location(Bukkit.getWorld(getConfig().getString("Sumo.Posicao2.World")), getConfig().getDouble("Sumo.Posicao2.X"), getConfig().getDouble("Sumo.Posicao2.Y"), getConfig().getDouble("Sumo.Posicao2.Z"));
			
			if (getConfig().contains("Sumo.Posicao2.Yaw"))
				secondLocation.setYaw(getConfig().getFloat("Sumo.Posicao2.Yaw"));
		
			if (getConfig().contains("Sumo.Posicao2.Pitch"))
				secondLocation.setYaw(getConfig().getFloat("Sumo.Posicao2.Pitch"));
		
			this.warps.put("sumo", new WarpSumo("Sumo", spawnLocation, Material.BLAZE_ROD, firstLocation, secondLocation));
		}
	}
	
	public Warp getScreenshare() {
		return screenshare;
	}
	
	public void addWarp(String warpName, Location warpLocation, int icon, double protection) {
		if (warps.containsKey(warpName.toLowerCase()))
			return;
		
		warps.put(warpName.toLowerCase(), new Warp(warpName, warpLocation, Material.getMaterial(icon), protection));
		BukkitMain.getInstance().debug("A warp " + warpName + " foi carregada!");
	}
	
	public void addWarp(String warpName) {
		if (getConfig().contains("warps." + warpName.toLowerCase() + ".World")) {
			Location warpLocation = new Location(Bukkit.getWorld(getConfig().getString("warps." + warpName.toLowerCase() + ".World")), getConfig().getDouble("warps." + warpName.toLowerCase() + ".X"), getConfig().getDouble("warps." + warpName.toLowerCase() + ".Y"), getConfig().getDouble("warps." + warpName.toLowerCase() + ".Z"));
			
			if (getConfig().contains("warps." + warpName.toLowerCase() + ".Yaw")) {
				warpLocation.setYaw(getConfig().getFloat("warps." + warpName.toLowerCase() + ".Yaw"));
			}
			
			if (getConfig().contains("warps." + warpName.toLowerCase() + ".Pitch")) {
				warpLocation.setYaw(getConfig().getFloat("warps." + warpName.toLowerCase() + ".Pitch"));
			}
			
			double protection = 0.0;
			
			if (getConfig().contains("warps." + warpName.toLowerCase() + ".Protection")) {
				protection = getConfig().getDouble("warps." + warpName.toLowerCase() + ".Protection");
			}
			
			addWarp(warpName, warpLocation, getConfig().getInt("warps." + warpName.toLowerCase() + ".Item"), protection);
		} else {
			if (warpName.equalsIgnoreCase("Spawn")) {
				addWarp(warpName, Bukkit.getWorld("world").getSpawnLocation(), 0, 90);
			} else {
				addWarp(warpName, new Location(Bukkit.getWorld("world"), 0.5, 120, 0.5), 20, 7);
			}
		}
	}
	
	public Warp getWarp(String warpName) {
		if (warps.containsKey(warpName.toLowerCase())) {
			return warps.get(warpName.toLowerCase());
		}
		
		return null;
	}
	
	public static class WarpLocation {
		
		private Location firstLocation;
		private Location secondLocation;
		
		public WarpLocation(Location firstLocation, Location secondLocation) {
			this.firstLocation = firstLocation;
			this.secondLocation = secondLocation;
		}
		
		public Location getFirstLocation() {
			return firstLocation;
		}
		
		public void setFirstLocation(Location firstLocation) {
			this.firstLocation = firstLocation;
		}
		
		public void setSecondLocation(Location secondLocation) {
			this.secondLocation = secondLocation;
		}
		
		public Location getSecondLocation() {
			return secondLocation;
		}
	}
	
	public class WarpSumo extends Warp {
		
		private WarpLocation warpLocation;
		
		public WarpSumo(String warpName, Location warpLocation, Material warpIcon, Location firstLocation, Location secondLocation) {
			super(warpName, warpLocation, warpIcon, 99.9);
			this.warpLocation = new WarpLocation(firstLocation, secondLocation);
		}
		
		public Location getFirstLocation() {
			return this.warpLocation.firstLocation;
		}
		
		public void setFirstLocation(Location firstLocation) {
			this.warpLocation.firstLocation = firstLocation;
		}
		
		public void setSecondLocation(Location secondLocation) {
			this.warpLocation.secondLocation = secondLocation;
		}
		
		public Location getSecondLocation() {
			return this.warpLocation.secondLocation;
		}
	}
	
	public class Warp1v1 extends Warp {
		
		private WarpLocation warpLocation;
		
		public Warp1v1(String warpName, Location warpLocation, Material warpIcon, Location firstLocation, Location secondLocation) {
			super(warpName, warpLocation, warpIcon, 99.9);
			this.warpLocation = new WarpLocation(firstLocation, secondLocation);
		}
		
		public Location getFirstLocation() {
			return this.warpLocation.firstLocation;
		}
		
		public void setFirstLocation(Location firstLocation) {
			this.warpLocation.firstLocation = firstLocation;
		}
		
		public void setSecondLocation(Location secondLocation) {
			this.warpLocation.secondLocation = secondLocation;
		}
		
		public Location getSecondLocation() {
			return this.warpLocation.secondLocation;
		}
	}
	
	public class Warp {
		
		private String warpName;
		private Location warpLocation;
		private Material warpIcon;
		private double protection;
		
		public Warp(String warpName, Location warpLocation, Material warpIcon, double protection) {
			this.warpName = warpName;
			this.warpLocation = warpLocation;
			this.warpIcon = warpIcon;
			this.protection = protection;
		}
		
		public Location getWarpLocation() {
			return warpLocation;
		}
		
		public void setWarpLocation(Location warpLocation) {
			this.warpLocation = warpLocation;
		}
		
		public String getWarpName() {
			return warpName;
		}
		
		public Material getWarpIcon() {
			return warpIcon;
		}
		
		public double getProtection() {
			return protection;
		}
		
		public void setProtection(double protection) {
			this.protection = protection;
		}
		
	}
	
	public Collection<Warp> getWarps() {
		return this.warps.values();
	}
	
	public Location getLocation(String warp) {
		return null;
	}

}
