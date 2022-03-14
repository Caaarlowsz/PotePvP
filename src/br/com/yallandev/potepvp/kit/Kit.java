package br.com.yallandev.potepvp.kit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.github.caaarlowsz.potemc.kitpvp.PotePvP;
import com.github.caaarlowsz.potemc.kitpvp.PotePvP.Configuration;
import br.com.yallandev.potepvp.account.Account;
import br.com.yallandev.potepvp.permissions.group.Group;
import br.com.yallandev.potepvp.utils.DateUtils;
import br.com.yallandev.potepvp.utils.ItemManager;
import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.IChatBaseComponent;
import net.minecraft.server.v1_7_R4.PacketPlayOutChat;

public class Kit implements Listener {

	public PotePvP main = PotePvP.getInstance();
	private String kitName;
	private ItemStack kitIcon;
	private List<ItemStack> kitItens;
	private int price;
	private boolean active = true;

	public Kit(String kitName, ItemStack kitIcon, List<ItemStack> kitItens, int price) {
		this.main = PotePvP.getInstance();
		this.kitName = kitName;
		this.kitIcon = kitIcon;

		if (kitItens == null) {
			this.kitItens = new ArrayList<>();
		} else {
			this.kitItens = new ArrayList<>(kitItens);
		}

		this.price = price;
	}

	public Kit(String kitName, ItemStack kitIcon, ItemStack kitItens, int price) {
		this(kitName, kitIcon, Arrays.asList(kitItens), price);
	}

	public Kit(String kitName, ItemStack kitIcon, ItemStack kitItens) {
		this(kitName, kitIcon, kitItens, 10000);
	}

	public Kit(String kitName, Material kitIcon, int price, List<ItemStack> kitItens) {
		this(kitName, new ItemManager(kitIcon, "�a" + kitName).build(), kitItens, price);
	}

	public Kit(String kitName, Material kitIcon, int price, List<ItemStack> kitItens, List<String> kitInfo) {
		this(kitName, new ItemManager(kitIcon, "�a" + kitName).setLore(kitInfo).build(), kitItens, price);
	}

	public Kit(String kitName, Material kitIcon, int price, boolean kitItens, List<String> kitInfo) {
		List<ItemStack> itens = new ArrayList<>();
		if (kitItens) {
			itens.add(new ItemManager(kitIcon, "�a" + kitName).build());
		}

		this.kitName = kitName;
		this.kitIcon = new ItemManager(kitIcon, "�a" + kitName).setLore(kitInfo).build();

		this.kitItens = new ArrayList<>(itens);

		this.price = price;
	}

	public String getKitName() {
		return kitName;
	}

	public ItemStack getKitIcon() {
		return kitIcon;
	}

	public List<ItemStack> getKitItens() {
		return kitItens;
	}

	public int getPrice() {
		return price;
	}

	public Kit clone() {
		return new Kit(kitName, kitIcon, kitItens, price);
	}

	public boolean canUse(Account account) {
		if (account.hasServerGroup(Group.PRO))
			return true;

		if (getKitName().equalsIgnoreCase("PvP"))
			return true;

		if (account.hasServerGroup(Group.LIGHT) || account.getStatus().getRank() == 1) {
			if (main.getKitManager().getKitRotate().get(Group.LIGHT).contains(getKitName().toLowerCase())) {
				return true;
			}
		}

		if (account.hasServerGroup(Group.MVP)) {
			if (main.getKitManager().getKitRotate().get(Group.MVP).contains(getKitName().toLowerCase())) {
				return true;
			}
		}

		if (main.getKitManager().getKitRotate().get(Group.MEMBRO).contains(getKitName().toLowerCase())) {
			return true;
		}

		return account.hasPermission("kit." + kitName.toLowerCase());
	}

	public Kit getKit() {
		return this;
	}

	public boolean hasKit(Player player) {
		return main.getKitManager().hasAbility(player.getUniqueId(), getKitName());
	}

	public boolean hasKit(Player player, String kitName) {
		return main.getKitManager().hasAbility(player.getUniqueId(), kitName);
	}

	public boolean isCooldown(Player player) {
		return main.getKitManager().isCooldown(player.getName(), getKitName());
	}

	public void setCooldown(Player player, Long time) {
		main.getKitManager().setCooldown(player.getName(), getKitName(), time);
	}

	public void addCooldown(Player player, int time) {
		setCooldown(player, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(time));
	}

	public void cooldownMessage(Player player) {
		if (!isCooldown(player))
			return;

		sendMessage(player, "Voc� est� em cooldown de �a"
				+ DateUtils.getTime(main.getKitManager().getCooldown(player.getName(), getKitName())) + "�f.");
	}

	public void sendMessage(Player player, String message) {
		player.sendMessage(Configuration.PREFIX.getMessage() + message);
	}

	public void sendAction(Player player, String action) {
		if (((CraftPlayer) player).getHandle().playerConnection.networkManager.getVersion() < 47) {
			return;
		}
		IChatBaseComponent comp = ChatSerializer.a("{\"text\":\"" + action + " \"}");
		PacketPlayOutChat bar = new PacketPlayOutChat(comp, 2);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(bar);
	}

	public PotePvP getMain() {
		return main;
	}

	public String getPrefix() {
		return Configuration.PREFIX.getMessage();
	}

	public boolean isActive() {
		return active;
	}

	public boolean containsKitItem(ItemStack itemStack) {
		for (ItemStack kitItens : this.kitItens) {
			return itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()
					&& itemStack.getItemMeta().getDisplayName().contains(kitItens.getItemMeta().getDisplayName());
		}

		return false;
	}
}
