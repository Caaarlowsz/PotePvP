package br.com.yallandev.potepvp.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import br.com.yallandev.potepvp.BukkitMain;

public class Config {
	
	public BukkitMain main;
	private String configurationName;
	private FileConfiguration fConfig;
	private File config;
	
	public Config(String configurationName, BukkitMain main) {
		this.configurationName = configurationName;
		this.main = main;
		
		loadConfig();
	}
	
	public Config(String configurationName) {
		this.configurationName = configurationName;
		this.main = BukkitMain.getInstance();
		
		loadConfig();
	}
	
	public FileConfiguration getConfig() {
		return fConfig;
	}
	
	public File getFile() {
		return config;
	}
	
	public boolean saveConfig() {
		try {
			fConfig.save(config);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public String getString(String path) {
		return fConfig.getString(path);
	}
	
	public int getInteger(String path) {
		return fConfig.getInt(path);
	}
	
	public long getLong(String path) {
		return fConfig.getLong(path);
	}
	
	public String getConfigurationName() {
		return configurationName;
	}
	
	public void loadConfig() {
		File file = new File(main.getDataFolder(), getConfigurationName());

		if (!file.exists()) {
			file.getParentFile().mkdirs();
			copy(main.getResource(getConfigurationName()), file);
		}
		
		fConfig = YamlConfiguration.loadConfiguration(config = new File(main.getDataFolder(), getConfigurationName()));
	}
	
	private void copy(InputStream i, File config) {
		try {
			OutputStream out = new FileOutputStream(config);
			byte[] buf = new byte[710];
			int len;
			while ((len = i.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.close();
			i.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
