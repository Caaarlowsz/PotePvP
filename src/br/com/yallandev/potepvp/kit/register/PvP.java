package br.com.yallandev.potepvp.kit.register;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import br.com.yallandev.potepvp.kit.Kit;

public class PvP extends Kit {

	public PvP() {
		super("PvP", Material.STONE_SWORD, 0, false, Arrays.asList("Um kit sem habilidade", "que vem com espada de", "pedra com afiação 1."));
	}

}
