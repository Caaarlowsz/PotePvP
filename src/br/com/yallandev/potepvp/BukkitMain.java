package br.com.yallandev.potepvp;

import java.io.File;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.skionz.pingapi.PingAPI;
import com.skionz.pingapi.PingListener;

import br.com.yallandev.potepvp.clan.ClanCommon;
import br.com.yallandev.potepvp.clan.listener.ClanListener;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework;
import br.com.yallandev.potepvp.commands.BukkitCommandLoader;
import br.com.yallandev.potepvp.connection.MySQL;
import br.com.yallandev.potepvp.connection.SQLManager;
import br.com.yallandev.potepvp.event.update.UpdateEvent;
import br.com.yallandev.potepvp.event.update.UpdateEvent.UpdateType;
import br.com.yallandev.potepvp.hologram.UpdateHologram;
import br.com.yallandev.potepvp.event.update.UpdateScheduler;
import br.com.yallandev.potepvp.kit.KitManager;
import br.com.yallandev.potepvp.kit.register.gladiator.GladiatorFightController;
import br.com.yallandev.potepvp.listener.AccountListener;
import br.com.yallandev.potepvp.listener.AdminListener;
import br.com.yallandev.potepvp.listener.AuthMe;
import br.com.yallandev.potepvp.listener.ChatListener;
import br.com.yallandev.potepvp.listener.FreezerListener;
import br.com.yallandev.potepvp.listener.InventoryListener;
import br.com.yallandev.potepvp.listener.LauncherListener;
import br.com.yallandev.potepvp.listener.MoveListener;
import br.com.yallandev.potepvp.listener.PingListeners;
import br.com.yallandev.potepvp.listener.PlayerListener;
import br.com.yallandev.potepvp.listener.ScreenshareListener;
import br.com.yallandev.potepvp.listener.SignListener;
import br.com.yallandev.potepvp.listener.WarpListener;
import br.com.yallandev.potepvp.listener.umvum.Warp1v1;
import br.com.yallandev.potepvp.listener.umvum.WarpSumo;
import br.com.yallandev.potepvp.manager.AccountCommon;
import br.com.yallandev.potepvp.manager.BanManager;
import br.com.yallandev.potepvp.manager.EventManager;
import br.com.yallandev.potepvp.manager.PlayerHideManager;
import br.com.yallandev.potepvp.manager.PlayerManager;
import br.com.yallandev.potepvp.manager.ServerManager;
import br.com.yallandev.potepvp.manager.SimpleKit;
import br.com.yallandev.potepvp.manager.VanishMode;
import br.com.yallandev.potepvp.manager.WarpManager;
import br.com.yallandev.potepvp.permissions.group.Group;
import br.com.yallandev.potepvp.permissions.listener.LoginListener;
import br.com.yallandev.potepvp.permissions.regex.PermissionMatcher;
import br.com.yallandev.potepvp.permissions.regex.RegExpMatcher;
import br.com.yallandev.potepvp.permissions.regex.regexpermissions.RegexPermissions;
import br.com.yallandev.potepvp.protocol.LoginReceiver;
import br.com.yallandev.potepvp.scoreboard.Scoreboarding;
import br.com.yallandev.potepvp.storage.Storage;
import br.com.yallandev.potepvp.utils.ChatAPI;
import br.com.yallandev.potepvp.utils.Util;
import net.minecraft.util.com.google.gson.Gson;

public class BukkitMain extends JavaPlugin {
	
	private static final Storage STORAGE;
	
	private static BukkitMain instance;
	private static AccountCommon accountCommon;
	private BanManager banManager;
	private WarpManager warpManager;
	private PlayerManager playerManager;
	private KitManager kitManager;
	private VanishMode vanishMode;
	private PlayerHideManager playerHideManager;
	private ChatAPI chatAPI;
	private SimpleKit simpleKitManager;
	private ServerManager serverManager;
	private EventManager eventManager;
	private ClanCommon clanCommon;
	private GladiatorFightController gladiatorFightController;
	private Gson gson;
	private static MySQL mysql;
	private static SQLManager sqlManager;
	
	private RegexPermissions regexPerms;
	protected PermissionMatcher matcher = new RegExpMatcher();
	public LoginListener superms;
	
	public static List<String> players;
	public static List<PingListener> listeners;
	
	static {
		STORAGE = new Storage();
	}
	
	public static Storage getStorage() {
		return STORAGE;
	}
	
	public static SQLManager getSqlManager() {
		return sqlManager;
	};
	
	public static AccountCommon getAccountCommon() {
		return accountCommon;
	}
	
	public BanManager getBanManager() {
		return banManager;
	}
	
	public WarpManager getWarpManager() {
		return warpManager;
	}
	
	public PlayerManager getPlayerManager() {
		return playerManager;
	}
	
	public KitManager getKitManager() {
		return kitManager;
	}
	
	public VanishMode getVanishMode() {
		return vanishMode;
	}
	
	public PlayerHideManager getPlayerHideManager() {
		return playerHideManager;
	}
	
	public EventManager getEventManager() {
		return eventManager;
	}
	
	public GladiatorFightController getGladiatorFightController() {
		return gladiatorFightController;
	}
	
	public ChatAPI getChatAPI() {
		return chatAPI;
	}
	
	public SimpleKit getSimpleKitManager() {
		return simpleKitManager;
	}
	
	public ClanCommon getClanCommon() {
		return clanCommon;
	}
	
	public ServerManager getServerManager() {
		return serverManager;
	}
	
	public Gson getGson() {
		return gson;
	}
	
	public static MySQL getConnection() {
		return mysql;
	}
	
	public static BukkitMain getInstance() {
		return instance;
	}
	
	@Override
	public void onLoad() {
		instance = this;
		super.onLoad();
	}
	
	@Override
	public void onEnable() {
		Bukkit.getConsoleSender().sendMessage("[" + getDescription().getName() +"] O servidor está sendo ativada!");
		
		try {
			if (!this.getServer().getOnlineMode()) {
				Util.setOnlineMode(true);
			}
			
			accountCommon = new AccountCommon();
			this.gson = new Gson();
			this.banManager = new BanManager();
			this.warpManager = new WarpManager();
			this.playerManager = new PlayerManager();
			this.kitManager = new KitManager();
			this.vanishMode = new VanishMode();
			this.playerHideManager = new PlayerHideManager();
			this.chatAPI = new ChatAPI();
			this.simpleKitManager = new SimpleKit();
			this.serverManager = new ServerManager();
			this.eventManager = new EventManager();
			this.clanCommon = new ClanCommon();
			this.gladiatorFightController = new GladiatorFightController();
			setupConfigs();
			mysql = new MySQL(Configuration.MYSQL_HOSTNAME.getMessage(), Configuration.MYSQL_USERNAME.getMessage(), Configuration.MYSQL_PASSWORD.getMessage(), Configuration.MYSQL_DATABASE.getMessage());
			mysql.createTable();
			sqlManager = new SQLManager();
			
			getServer().getScheduler().runTaskTimer(getInstance(), new UpdateScheduler(), 1l, 1l);
			
			regexPerms = new RegexPermissions(getInstance());
			registerListeners();
			new BukkitCommandLoader(new BukkitCommandFramework(this)).loadCommandsFromPackage("br.com.yallandev.potepvp.commands.register");
			
			new LoginReceiver();
			
			Scoreboarding.startUpdater();
			Bukkit.getConsoleSender().sendMessage("[" + getDescription().getName() +"] O plugin foi ativado com sucesso!");
		} catch (Exception e) {
			Bukkit.getConsoleSender().sendMessage("[" + getDescription().getName() +"] Ocorreu um erro ao ativar o plugin!");
			e.printStackTrace();
		}
		
		super.onEnable();
	}
	
	public enum Configuration {
		
		BANS_CONSUMER_KEY(getInstance().getValue("Twitter.Bans.ConsumerKey")),
		BANS_CONSUMER_SECRET(getInstance().getValue("Twitter.Bans.ConsumerSecret")),
		BANS_ACESS_TOKEN(getInstance().getValue("Twitter.Bans.AcessToken")),
		BANS_ACESS_SECRET(getInstance().getValue("Twitter.Bans.AcessSecret")),
		
		OFICIAL_CONSUMER_KEY(getInstance().getValue("Twitter.Oficial.ConsumerKey")),
		OFICIAL_CONSUMER_SECRET(getInstance().getValue("Twitter.Oficial.ConsumerSecret")),
		OFICIAL_ACESS_TOKEN(getInstance().getValue("Twitter.Oficial.AcessToken")),
		OFICIAL_ACESS_SECRET(getInstance().getValue("Twitter.Oficial.AcessSecret")),
		
		MYSQL_USERNAME(getInstance().getValue("MySQL.UserName")),
		MYSQL_HOSTNAME(getInstance().getValue("MySQL.HostName")),
		MYSQL_PASSWORD(getInstance().getValue("MySQL.Password")),
		MYSQL_DATABASE(getInstance().getValue("MySQL.Database")),
		
		TRIAL(getInstance().getValue("Aplicar.Trial")),
		MODGC(getInstance().getValue("Aplicar.ModGC")),
		HELPER(getInstance().getValue("Aplicar.Helper")),
		
		JOIN_MESSAGES(getInstance().getStringList("JoinMessages")),
		
		SERVERADRESS(getInstance().getValue("ServerAddress")),
		MAX_PLAYERS(getInstance().getInteger("MaxPlayers")),
		
		GLADIATOR_ADDRESS("glad.burnpvp.tk"),
		
		SITE(getInstance().getValue("Site")),
		SCOREBOARD_SITE(getInstance().getValue("ScoreboardSite")),
		
		CHAT_PREFIX(getInstance().getValue("Prefix.Chat").replace("&", "§").replace("{seta}", "»")),
		PREFIX(Configuration.CHAT_PREFIX.getMessage()),
		KICK_PREFIX(getInstance().getValue("Prefix.Kick").replace("&", "§").replace("{seta}", "»")),
		
		KIT_MEMBER(getInstance().getStringList("Kits.Membro")),
		KIT_VIP(getInstance().getStringList("Kits.Vip")),
		KIT_MASTER(getInstance().getStringList("Kits.Master")),
		
		FULLIRON(false),
		
		SEMPERM(getInstance().getValue("Messages.SemPerm").replace("{prefix}", Configuration.PREFIX.getMessage().replace("» ", "»")).replace("&", "§").replace("{seta}", "»")),
		LOGIN(true);
		
		private String message;
		private boolean active;
		private int value;
		private List<String> stringList;
		private Long expire;
		
		public static String getString(String path) {
			return getInstance().getValue(path);
		}

		private Configuration(String message) {
			this.message = message;
		}

		private Configuration(boolean active) {
			this.active = active;
		}

		private Configuration(int value) {
			this.value = value;
		}

		private Configuration(List<String> stringList) {
			this.stringList = stringList;
		}
		
		private Configuration(Long expire) {
			this.expire = expire;
		}

		public String getMessage() {
			return message;
		}

		public boolean isActive() {
			return active;
		}

		public int getValue() {
			return value;
		}

		public List<String> getStringList() {
			return stringList;
		}
		
		public Long getExpire() {
			return expire;
		}
		
	}
	
	public void debug(String message) {
		System.out.println("[DEBUG] " + message);
	}

	private HashMap<String, Object> saveConfiguration = new HashMap<>();

	public String getValue(String path) {
		if (saveConfiguration.containsKey(path)) {
			return (String) saveConfiguration.get(path);
		}
		if (getConfig().getString(path) != null) {
			saveConfiguration.put(path, getConfig().getString(path).replace("{linha}", "\n"));
			debug("A configuracao " + path + " foi carregada!");
			return getConfig().getString(path).replace("{linha}", "\n");
		} else {
			debug("Nao foi possivel carregar a config " + path.toLowerCase().replace("_", "-") + "§f.");
			return "[NOT FOUND: " + path + "]";
		}
	}
	
	public Long getExpire(String path, boolean saved) {
		if (saved) {
			if (saveConfiguration.containsKey(path)) {
				return (Long) saveConfiguration.get(path);
			}
		}
		if (getConfig().getString(path) != null) {
			if (saved) {
				saveConfiguration.put(path, getConfig().getLong(path));
			}
			debug("A configuracao " + path + " foi carregada!");
			return getConfig().getLong(path);
		} else {
			return -1L;
		}
	}

	public boolean getBoolean(String path) {
		if (saveConfiguration.containsKey(path)) {
			return (boolean) saveConfiguration.get(path);
		}

		if (getConfig().contains(path)) {
			debug("A configuracao " + path + " foi carregada!");
			saveConfiguration.put(path, getConfig().getBoolean(path));
			return getConfig().getBoolean(path);
		} else {
			debug("Nao foi possivel carregar a config " + path.toLowerCase().replace("_", "-") + "§f.");
			return false;
		}
	}

	public int getInteger(String path) {
		if (saveConfiguration.containsKey(path)) {
			return (int) saveConfiguration.get(path);
		}

		if (getConfig().contains(path)) {
			debug("A configuracao " + path + " foi carregada!");
			saveConfiguration.put(path, getConfig().getInt(path));
			return getConfig().getInt(path);
		} else {
			debug("Nao foi possivel carregar a config " + path.toLowerCase().replace("_", "-") + "§f.");
			return new Random().nextInt(1);
		}
	}
	
	public double getDouble(String path) {
		if (saveConfiguration.containsKey(path)) {
			return (double) saveConfiguration.get(path);
		}

		if (getConfig().contains(path)) {
			debug("A configuracao " + path + " foi carregada!");
			saveConfiguration.put(path, getConfig().getDouble(path));
			return getConfig().getDouble(path);
		} else {
			debug("Nao foi possivel carregar a config " + path.toLowerCase().replace("_", "-") + "§f.");
			return (double) new Random().nextInt(5);
		}
	}

	public List<String> getStringList(String path) {
		if (saveConfiguration.containsKey(path)) {
			return (List<String>) saveConfiguration.get(path);
		}

		if (getConfig().contains(path)) {
			debug("A configuracao " + path + " foi carregada!");
			saveConfiguration.put(path, getConfig().getInt(path));
			return getConfig().getStringList(path);
		} else {
			debug("Nao foi possivel carregar a config " + path.toLowerCase().replace("_", "-") + "§f.");
			return new ArrayList<>();
		}
	}
	
	private void registerListeners() {
		PluginManager pluginManager = Bukkit.getPluginManager();
		
		ping();
		PingAPI.registerListener(new PingListeners());
		
		pluginManager.registerEvents(new PlayerListener(getInstance()), this);
		pluginManager.registerEvents(new WarpListener(getInstance()), this);
		pluginManager.registerEvents(new AdminListener(getInstance()), this);
		pluginManager.registerEvents(new InventoryListener(), this);
		pluginManager.registerEvents(new AccountListener(), this);
		pluginManager.registerEvents(new AuthMe(), this);
		pluginManager.registerEvents(new ChatListener(), this);
		pluginManager.registerEvents(new Warp1v1(), this);
		pluginManager.registerEvents(new WarpSumo(), this);
		pluginManager.registerEvents(new FreezerListener(), this);
		pluginManager.registerEvents(new SignListener(), this);
		pluginManager.registerEvents(new MoveListener(), this);
		pluginManager.registerEvents(new LauncherListener(), this);
		pluginManager.registerEvents(new ScreenshareListener(), this);
		pluginManager.registerEvents(new UpdateHologram(), this);
		pluginManager.registerEvents(new ClanListener(), this);
		pluginManager.registerEvents(new Listener() {
			
			int x = 0;
			String[] messages = { "§fAcesse nossa loja e adquira beneficios no servidor, §e§n" + Configuration.SITE.getMessage(),
					"§fLute para ser o §1§lTOP1§f do servidor, e receba alguns beneficios!", 
					"§fCrie seu clã com guerreiros fortes e seja o §1§lTOP1 CLÃ§f!",
					"§fSe encontrar algum bug, reporte ele para algum §3§lDESENVOLVEDOR §fou §4§lDONO§f e receba algumas recompensas!",
					"§fO plugin do servidor sofre atualizações diárias!" };
			
			@EventHandler
			public void onUpdate(UpdateEvent e) {
				if (e.getType() == UpdateType.MINUTE) {
					x++;
					
					broadcast(messages[new Random().nextInt(messages.length)]);
					
					if (x == 15) {
						x = 0;
						long used = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024L / 1024L;
						System.gc();
						Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Foram liberados " + ChatColor.WHITE + Long.toString(used - ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024L / 1024L)) + " megabytes" + ChatColor.YELLOW + " de memória RAM.");
					}
				}
			}
		}, this);
		
		pluginManager.registerEvents(superms = new LoginListener(getInstance()), getPlugin());
		
		setupConfigs();
	}
	
	
	public void ping() {
		try {
			listeners = new ArrayList<PingListener>();
			String name = Bukkit.getServer().getClass().getPackage().getName();
	        String version = name.substring(name.lastIndexOf('.') + 1);
	        Class<?> injector = Class.forName("com.skionz.pingapi." + version + ".PingInjector");
	        Bukkit.getPluginManager().registerEvents((Listener) injector.newInstance(), this);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setupConfigs() {
		File file = new File(getInstance().getDataFolder(), "config.yml");
		
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			copiarConfig(getInstance().getResource("config.yml"), file);
		}
	}

	protected void copiarConfig(InputStream i, File config) {
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

	public static String getPrefix() {
		return Configuration.PREFIX.getMessage();
	}

	public static BukkitMain getPlugin() {
		return instance;
	}
	
	public RegexPermissions getRegexPerms() {
		return this.regexPerms;
	}

	public PermissionMatcher getPermissionMatcher() {
		return this.matcher;
	}
	
	public static void broadcast(String message, Group group) {
		for (Player players : Bukkit.getOnlinePlayers()) {
			if (BukkitMain.getAccountCommon().hasGroup(players.getUniqueId(), group)) {
				players.sendMessage(Configuration.PREFIX.getMessage() + message);
			}
		}
	}
	
	public static void broadcastD(String message, Group group) {
		for (Player players : Bukkit.getOnlinePlayers()) {
			if (BukkitMain.getAccountCommon().hasGroup(players.getUniqueId(), group)) {
				players.sendMessage(message);
			}
		}
	}
	
	public static void broadcast(String message) {
		Bukkit.broadcastMessage(Configuration.PREFIX.getMessage() + message);
	}

}
