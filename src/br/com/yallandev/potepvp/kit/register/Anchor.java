package br.com.yallandev.potepvp.kit.register;

import java.util.Arrays;

import org.bukkit.Material;

import br.com.yallandev.potepvp.kit.Kit;

public class Anchor extends Kit {

	public Anchor() {
		super("Anchor", Material.ANVIL, 18000, false,
				Arrays.asList("Use seu kit anchor para", "n�o dar e nem tomar", "knockback."));
	}

}
