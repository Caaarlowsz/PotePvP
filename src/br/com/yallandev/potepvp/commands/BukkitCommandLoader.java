package br.com.yallandev.potepvp.commands;

import org.bukkit.Bukkit;

import br.com.yallandev.potepvp.manager.CommandClass;
import br.com.yallandev.potepvp.utils.ClassGetter;

public class BukkitCommandLoader {

	private BukkitCommandFramework framework;

	public BukkitCommandLoader(BukkitCommandFramework framework) {
		this.framework = framework;
	}

	public int loadCommandsFromPackage(String packageName) {
		int i = 0;
		for (Class<?> commandClass : ClassGetter.getClassesForPackage(framework.getPlugin().getClass(), packageName)) {
			if (CommandClass.class.isAssignableFrom(commandClass)) {
				try {
					CommandClass commands = (CommandClass) commandClass.newInstance();
					framework.registerCommands(commands);
				} catch (Exception e) {
					e.printStackTrace();
					Bukkit.getLogger().warning("Erro ao carregar comandos da classe " + commandClass.getSimpleName() + "!");
				}
				i++;
			}
		}
		return i;
	}

}