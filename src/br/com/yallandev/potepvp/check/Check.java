package br.com.yallandev.potepvp.check;

import java.util.ArrayList;
import java.util.List;

import br.com.yallandev.potepvp.BukkitMain;
import br.com.yallandev.potepvp.check.api.MCAPICA;
import br.com.yallandev.potepvp.check.api.MCUUID;
import br.com.yallandev.potepvp.check.api.MineTools;
import br.com.yallandev.potepvp.check.api.MinecraftAPI;
import br.com.yallandev.potepvp.check.api.Mojang;
import br.com.yallandev.potepvp.exception.InvalidCheckException;
import br.com.yallandev.potepvp.utils.Util;

public class Check {

	private static final List<Verify> VERIFY_LIST;

	public static boolean fastCheck(String playerName) throws InvalidCheckException {
		if (Util.hasClass("org.bukkit.Bukkit")) {
			if (BukkitMain.getStorage().getPremiumMap().containsKey(playerName)) {
				return BukkitMain.getStorage().getPremiumMap().get(playerName);
			}
		}
		for (Verify verify : Check.VERIFY_LIST) {
			boolean check = verify.verify(playerName);
			if (!verify.getResult()) {
				continue;
			}
			return check;
		}
		throw new InvalidCheckException("Impossible to check: " + playerName);
	}

	public enum CheckAPI {

		MOJANG_API("https://api.mojang.com/users/profiles/minecraft/"), MC_UUID("https://api.mcuuid.com/v1/uuid/"),
		MINECRAFT_API("https://minecraft-api.com/api/uuid/uuid.php?pseudo="),
		MINETOOLS("https://api.minetools.eu/uuid/"), MCAPI_CA("https://mcapi.ca/rawskin/");

		private final String link;

		CheckAPI(String link) {
			this.link = link;
		}

		public String getLink() {
			return this.link;
		}

	}

	static {
		VERIFY_LIST = new ArrayList<>();
		Check.VERIFY_LIST.add(new Mojang());
		Check.VERIFY_LIST.add(new MCUUID());
		Check.VERIFY_LIST.add(new MCAPICA());
		Check.VERIFY_LIST.add(new MinecraftAPI());
		Check.VERIFY_LIST.add(new MineTools());
	}

}
