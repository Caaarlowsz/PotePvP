package br.com.yallandev.potepvp.commands.register;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import br.com.yallandev.potepvp.account.Account;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.Command;
import br.com.yallandev.potepvp.commands.BukkitCommandFramework.CommandArgs;
import br.com.yallandev.potepvp.manager.CommandClass;
import br.com.yallandev.potepvp.scoreboard.Scoreboarding;

public class ScoreboardCommand extends CommandClass {
	
	@Command(name = "score", onlyPlayers = true)
	public void onScore(CommandArgs cmdArgs) {
		Player p = cmdArgs.getPlayer();
		
		Account player = cmdArgs.getAccount();
		
		if (player == null)
			return;
		
		boolean scoreboardEnable = !player.isScoreboardEnable();
		
		send(p, "Você " + (scoreboardEnable ? "§a§lATIVOU" : "§4§lDESATIVOU") + "§f sua scoreboard.");
		player.setScoreboardEnable(scoreboardEnable);
		
		if (scoreboardEnable)
			Scoreboarding.setScoreboard(p);
		else {
			Objective obj = p.getScoreboard().getObjective("clear");
			
			if (obj == null)
				obj = p.getScoreboard().registerNewObjective("clear", "dummy");
			
			p.getScoreboard().getObjective("clear").setDisplaySlot(DisplaySlot.SIDEBAR);
		}
	}

}
