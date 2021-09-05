package br.com.yallandev.potepvp.kit.register;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import br.com.yallandev.potepvp.kit.Kit;
import br.com.yallandev.potepvp.utils.ItemManager;

public class Archer extends Kit {

	public Archer() {
		super("Archor", Material.BOW, 8000, Arrays.asList(new ItemManager(Material.BOW, "§aArcher").addEnchantment(Enchantment.ARROW_FIRE, 1).build(), new ItemManager(Material.ARROW, "§aArrow").build()), Arrays.asList("Jogue flechas flamejantes nos", "jogadores e receba 3", "flechas ao matar alguem."));
	}
	
	
}
