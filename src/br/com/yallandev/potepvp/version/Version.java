package br.com.yallandev.potepvp.version;

/**
 *
 * @author netindev
 *
 */
public enum Version {

	v1_7_R4("f", "ai");

	private static final Version VERSION;

	private Version(String networkManager, String serverConnection) {
		this.networkManager = networkManager;
		this.serverConnection = serverConnection;
	}

	private String networkManager;
	private String serverConnection;

	public String getServerConnection() {
		return this.serverConnection;
	}

	public String getNetworkManager() {
		return this.networkManager;
	}

	public static boolean is1_7() {
		return Version.getPackageVersion().toString().contains("1_7");
	}

	public static Version getPackageVersion() {
		return Version.VERSION;
	}

	static {
		final String packageVersion = org.bukkit.Bukkit.getServer().getClass().getPackage().getName().replace(".", ",")
				.split(",")[3];
		Version tempVersion = null;
		for (Version versions : Version.values()) {
			if (packageVersion.equals(versions.toString())) {
				tempVersion = versions;
				break;
			}
		}
		VERSION = tempVersion;
	}

}
