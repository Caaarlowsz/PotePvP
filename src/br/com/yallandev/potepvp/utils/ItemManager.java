package br.com.yallandev.potepvp.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import br.com.yallandev.potepvp.BukkitMain;
import br.com.yallandev.potepvp.permissions.group.Group;

public class ItemManager {

	private String name;
	private List<String> lore;
	private Material material;
	private ItemStack item;
	private ItemMeta itemm;
	private boolean permissions;
	private String permissao;

	public ItemManager(Material mat, String name) {
		material = mat;
		this.name = name;
		lore = new ArrayList<>();

		ItemStack stack = new ItemStack(mat);
		ItemMeta stackmeta = stack.getItemMeta();
		stackmeta.setDisplayName(name);

		item = stack;
		itemm = stackmeta;
		permissions = false;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public void setNome(String name) {
		this.name = name;
	}

	public void resetLore() {
		this.lore.clear();
	}

	public ItemManager addLore(String lore) {
		this.lore.add("�f" + lore);
		return this;
	}

	public void setAmount(int amount) {
		item.setAmount(amount);
	}

	public void addPermission(String permissao) {
		permissions = true;
		this.permissao = permissao;
	}

	public void removePermission() {
		permissions = false;
		this.permissao = null;
	}

	public ItemManager setDurability(int durability) {
		item.setDurability((short) durability);
		return this;
	}

	public ItemManager addEnchantment(Enchantment enchantment, int level) {
		itemm.addEnchant(enchantment, level, true);
		return this;
	}

	public ItemStack build() {
		itemm.setLore(lore);
		item.setType(material);
		itemm.setDisplayName(name);
		item.setItemMeta(itemm);
		return item;
	}

	public ItemStack build(Player p) {
		if (!permissions) {
			return build();
		}
		itemm.setLore(lore);
		item.setType(material);
		itemm.setDisplayName(name);
		item.setItemMeta(itemm);

		if (p.hasPermission(permissao) && BukkitMain.getAccountCommon().hasGroup(p.getUniqueId(), Group.DONO)) {
			return item;
		}
		return new ItemStack(Material.AIR);
	}

	public ItemManager setLore(List<String> kitInfo) {
		this.lore = new ArrayList<>();

		for (String list : lore) {
			addLore(list);
		}

		return this;
	}

}
