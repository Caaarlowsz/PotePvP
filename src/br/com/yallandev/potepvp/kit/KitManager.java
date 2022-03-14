package br.com.yallandev.potepvp.kit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.caaarlowsz.potemc.kitpvp.PotePvP;
import com.github.caaarlowsz.potemc.kitpvp.PotePvP.Configuration;
import br.com.yallandev.potepvp.account.Account;
import br.com.yallandev.potepvp.permissions.group.Group;
import br.com.yallandev.potepvp.utils.ClassGetter;
import br.com.yallandev.potepvp.utils.ItemManager;

public class KitManager {

	private PotePvP main;
	private List<Kit> kits;

	private HashMap<UUID, Kit> playerAbility;
	private HashMap<Group, List<String>> kitRotate;

	private List<Location> arenas;

	private HashMap<String, Map<String, Long>> playerCooldown;

	public KitManager() {
		this.main = PotePvP.getInstance();
		this.kits = new ArrayList<>();

		this.playerAbility = new HashMap<>();
		this.kitRotate = new HashMap<>();
		this.playerCooldown = new HashMap<>();
		this.kitRotate.put(Group.LIGHT, Configuration.KIT_VIP.getStringList());
		this.kitRotate.put(Group.MVP, Configuration.KIT_MASTER.getStringList());
		this.kitRotate.put(Group.MEMBRO, Configuration.KIT_MEMBER.getStringList());

		this.arenas = new ArrayList<>();
		loadKits();

		new BukkitRunnable() {

			@Override
			public void run() {
				loadArenas();
			}
		}.runTaskLater(PotePvP.getInstance(), 20);
	}

	public HashMap<String, Map<String, Long>> getPlayerCooldown() {
		return playerCooldown;
	}

	public List<Location> getArenas() {
		return arenas;
	}

	public HashMap<Group, List<String>> getKitRotate() {
		return kitRotate;
	}

	public void loadArenas() {
		if (!main.getWarpManager().getConfig().contains("arenas"))
			return;

		for (String list : main.getWarpManager().getConfig().getConfigurationSection("arenas").getKeys(false)) {
			System.out.println(list);
			Location location = null;
			if (main.getWarpManager().getConfig().contains("arenas." + list + ".World")) {
				System.out.println("2");
				location = new Location(
						Bukkit.getWorld(main.getWarpManager().getConfig().getString("arenas." + list + ".World")),
						main.getWarpManager().getConfig().getDouble("arenas." + list + ".X"),
						main.getWarpManager().getConfig().getDouble("arenas." + list + ".Y"),
						main.getWarpManager().getConfig().getDouble("arenas." + list + ".Z"));

				if (main.getWarpManager().getConfig().contains("arenas." + list + ".Yaw")) {
					location.setYaw(main.getWarpManager().getConfig().getFloat("arenas.A" + list + ".Yaw"));
				}

				if (main.getWarpManager().getConfig().contains("arenas." + list + ".Pitch")) {
					location.setYaw(main.getWarpManager().getConfig().getFloat("arenas.A" + list + ".Pitch"));
				}
			}

			if (location == null)
				continue;

			System.out.println("3");

			main.getKitManager().getArenas().add(location);
		}
	}

	public void loadKits() {
		for (Class<?> c : ClassGetter.getClassesForPackage(main, "br.com.yallandev.potepvp.kit.register")) {
			if (Kit.class.isAssignableFrom(c) && (c != Kit.class)) {
				try {
					Kit kit = (Kit) c.newInstance();

					kits.add(kit);

					registerKit(kit);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		kits.stream().sorted((kit1, kit2) -> kit1.getKitName().compareTo(kit2.getKitName()));
	}

	public List<Kit> getKits() {
		return kits;
	}

	public Kit getKit(String kit) {
		for (Kit kits : getKits())
			if (kits.getKitName().equalsIgnoreCase(kit))
				return kits;

		return null;
	}

	public void registerKit(Kit listener) {
		Bukkit.getPluginManager().registerEvents(listener, main);
	}

	public Kit getAbility(UUID name) {
		return playerAbility.containsKey(name) ? playerAbility.get(name) : null;
	}

	public String getNameOfAbility(UUID name) {
		return playerAbility.containsKey(name) ? playerAbility.get(name).getKitName() : "Nenhum";
	}

	public boolean hasAbility(UUID name, String ability) {
		if (getNameOfAbility(name).equalsIgnoreCase(ability))
			return true;

		return false;
	}

	public void setAbility(UUID name, Kit kit) {
		playerAbility.put(name, kit);
	}

	public void removeAbility(UUID uuid) {
		playerAbility.remove(uuid);
	}

	public boolean isCooldown(String name, String kitName) {
		if (playerCooldown.containsKey(name)) {
			Map<String, Long> cooldown = playerCooldown.get(name);

			if (cooldown != null && cooldown.containsKey(kitName) && cooldown.get(kitName) > System.currentTimeMillis())
				return true;
		}

		return false;
	}

	public void setCooldown(String name, String kitName, long expire) {
		if (playerCooldown.containsKey(name)) {
			Map<String, Long> cooldown = playerCooldown.get(name);

			if (cooldown != null) {
				cooldown.put(kitName, expire);
				playerCooldown.put(name, cooldown);
				return;
			}
		}

		Map<String, Long> cooldown = new HashMap<>();
		cooldown.put(kitName, expire);
		playerCooldown.put(name, cooldown);
	}

	public long getCooldown(String name, String kitName) {
		if (playerCooldown.containsKey(name)) {
			if (playerCooldown.get(name).containsKey(kitName))
				return playerCooldown.get(name).get(kitName);
		}

		return -1l;
	}

	public void applyKit(Player p) {
		Account player = PotePvP.getAccountCommon().getAccount(p.getUniqueId());

		if (player == null)
			return;

		PotePvP.getInstance().getPlayerManager().setProtection(player.getUuid(), false);

		p.getInventory().clear();
		p.getInventory().setArmorContents(new ItemStack[4]);

		for (PotionEffect pot : p.getActivePotionEffects())
			p.removePotionEffect(pot.getType());

		p.getInventory().addItem(new ItemStack(Material.COMPASS));

		if (hasAbility(player.getUuid(), "Surprise")) {
			Kit surprised = kits.get(new Random().nextInt(kits.size()));

			while (!surprised.isActive())
				surprised = kits.get(new Random().nextInt(kits.size()));

			player.sendAction("Surprise selecionou o kit �a" + surprised.getKitName() + "�f.");
			player.sendMessage("O surprise selecionou o kit �a" + surprised.getKitName() + "�f.");
			setAbility(player.getUuid(), surprised);
		}

		if (Configuration.FULLIRON.isActive()) {
			if (hasAbility(p.getUniqueId(), "Viking"))
				p.getInventory().setItem(0, new ItemManager(Material.DIAMOND_AXE, "�aMachado de diamante!").build());
			else if (hasAbility(p.getUniqueId(), "PvP"))
				p.getInventory().setItem(0, new ItemManager(Material.DIAMOND_SWORD, "�aEspada de diamante!")
						.addEnchantment(Enchantment.DAMAGE_ALL, 1).build());
			else
				p.getInventory().setItem(0, new ItemManager(Material.DIAMOND_SWORD, "�aEspada de ferro!").build());

			p.getInventory().setHelmet(new ItemManager(Material.IRON_HELMET, "�aCapacete de diamante!").build());
			p.getInventory()
					.setChestplate(new ItemManager(Material.IRON_CHESTPLATE, "�aPeitoral de diamante!").build());
			p.getInventory().setLeggings(new ItemManager(Material.IRON_LEGGINGS, "�aCal�a de diamante!").build());
			p.getInventory().setBoots(new ItemManager(Material.IRON_BOOTS, "�aBotas de diamante!").build());
		} else {
			if (hasAbility(p.getUniqueId(), "Viking"))
				p.getInventory().setItem(0, new ItemManager(Material.STONE_AXE, "�aMachado de pedra!").build());
			else if (hasAbility(p.getUniqueId(), "PvP"))
				p.getInventory().setItem(0, new ItemManager(Material.STONE_SWORD, "�aEspada de pedra!")
						.addEnchantment(Enchantment.DAMAGE_ALL, 1).build());
			else
				p.getInventory().setItem(0, new ItemManager(Material.STONE_SWORD, "�aEspada de pedra!").build());
		}

		if (!hasAbility(player.getUuid(), "Nenhum")) {
			for (ItemStack itens : getAbility(player.getUuid()).getKitItens())
				p.getInventory().addItem(itens);
		}

		for (int x = 0; x < 36; x++)
			p.getInventory().addItem(new ItemStack(Material.MUSHROOM_SOUP));

		p.getInventory().setItem(13, new ItemStack(Material.BOWL, 64));
		p.getInventory().setItem(14, new ItemStack(Material.RED_MUSHROOM, 64));
		p.getInventory().setItem(15, new ItemStack(Material.BROWN_MUSHROOM, 64));

		if (this.arenas.isEmpty()) {
			player.sendMessage("Nenhum arena foi setada!");
		} else {
			p.teleport(this.arenas.get(new Random().nextInt(this.arenas.size())));
		}
	}
}
