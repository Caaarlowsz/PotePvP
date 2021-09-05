package br.com.yallandev.potepvp.kit.register;

import java.util.Arrays;

import org.bukkit.Material;

import br.com.yallandev.potepvp.kit.Kit;

public class Surprise extends Kit {

	public Surprise() {
		super("Surprise", Material.CAKE, 18000, false,
				Arrays.asList("Selecione um kit aleatorio", "de todos os kits do servidor!"));
	}

}
