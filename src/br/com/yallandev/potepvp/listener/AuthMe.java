package br.com.yallandev.potepvp.listener;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import br.com.yallandev.potepvp.BukkitMain;
import br.com.yallandev.potepvp.account.Account;
import br.com.yallandev.potepvp.check.Check;
import br.com.yallandev.potepvp.connection.SQLManager.Status;
import br.com.yallandev.potepvp.event.account.RealMoveEvent;
import br.com.yallandev.potepvp.event.authme.PlayerCheckEvent;
import br.com.yallandev.potepvp.exception.InvalidCheckException;

public class AuthMe implements Listener {

	@EventHandler
	private void onPlayerJoin(PlayerJoinEvent event) {
		if (BukkitMain.getStorage().getPremiumMap().get(event.getPlayer().getName()) == null) {
			try {
				BukkitMain.getStorage().setPremium(event.getPlayer().getName(),
						Check.fastCheck(event.getPlayer().getName()));
			} catch (InvalidCheckException e) {
				e.printStackTrace();
			}
		}

		PlayerCheckEvent checkEvent = new PlayerCheckEvent(event.getPlayer(), BukkitMain.getStorage().getState(event.getPlayer().getName()));
		BukkitMain.getPlugin().getServer().getPluginManager().callEvent(checkEvent);
	}

	@EventHandler
	private void onPlayerQuit(PlayerQuitEvent event) {
		if (!BukkitMain.getStorage().getPremiumMap().containsKey(event.getPlayer().getName())) {
			return;
		}

		BukkitMain.getStorage().removeVerified(event.getPlayer().getName(),
				BukkitMain.getStorage().getState(event.getPlayer().getName()));
	}

	private static final HashMap<Player, LoginType> LOGIN_MAP = new HashMap<>();

	public static HashMap<Player, LoginType> getLoginMap() {
		return LOGIN_MAP;
	}

	public enum LoginType {
		LOGIN, REGISTER
	}

	@EventHandler
	private void onPlayerCheck(PlayerCheckEvent event) {
		if (event.isCracked()) {
			if (BukkitMain.getSqlManager().hasOnDatabase(event.getPlayer().getName())) {
				getLoginMap().put(event.getPlayer(), LoginType.LOGIN);
			} else {
				getLoginMap().put(event.getPlayer(), LoginType.REGISTER);
			}
			
			BukkitMain.getStorage().addNeedLogin(event.getPlayer().getName());
		} else if (event.isPremium()) {
			if (!BukkitMain.getSqlManager().hasOnDatabase(event.getPlayer().getName())) {
				BukkitMain.getSqlManager().setStatus(event.getPlayer().getName(), Status.PREMIUM);
			}

			event.getPlayer().sendMessage("Logado automaticamente por ser original!");
		}
	}

	@EventHandler
	private void onPlayerLogin2(PlayerLoginEvent event) {
		if (event.getPlayer().getName().length() > 16) {
			event.disallow(Result.KICK_OTHER, "§c§lBURN MC\n§cSeu nome é muito grande!");
		}
	}

	@EventHandler
	private void onPlayerQuit2(PlayerQuitEvent event) {
		if (BukkitMain.getStorage().needLogin(event.getPlayer().getName())) {
			BukkitMain.getStorage().removeNeedLogin(event.getPlayer().getName());
		}
		if (getLoginMap().containsKey(event.getPlayer())) {
			getLoginMap().remove(event.getPlayer(), getLoginMap().get(event.getPlayer()));
		}
	}

	@EventHandler
	private void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			if (BukkitMain.getStorage().needLogin(((Player) event.getEntity()).getName())) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	private void onPlayerMove(PlayerMoveEvent event) {
		if (BukkitMain.getStorage().needLogin(event.getPlayer().getName())) {
			event.getPlayer().teleport(event.getFrom());
		}
	}

	@EventHandler
	private void onFoodLevelChange(FoodLevelChangeEvent event) {
		if (event.getEntity() instanceof Player) {
			if (BukkitMain.getStorage().needLogin(event.getEntity().getName())) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	private void onPlayerDrop(PlayerDropItemEvent event) {
		if (BukkitMain.getStorage().needLogin(event.getPlayer().getName())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	private void onPlayerInteract(PlayerInteractEvent event) {
		if (BukkitMain.getStorage().needLogin(event.getPlayer().getName())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	private void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
		if (BukkitMain.getStorage().needLogin(event.getPlayer().getName())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onRealMove(RealMoveEvent event) {
		if (!BukkitMain.getStorage().needLogin(event.getPlayer().getName()))
			return;
		
		Player p = event.getPlayer();
		Account player = BukkitMain.getAccountCommon().getAccount(p.getUniqueId());
		
		if (player == null)
			return;
		
		player.sendTimerMessage("§fUse /login (Senha) para se logar!");
		player.sendTimerMessage("§fUse /register (Senha) (Senha) para se registrar!");
	}
	
	@EventHandler
	private void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if (BukkitMain.getStorage().needLogin(event.getPlayer().getName())) {
			if (event.getMessage().toLowerCase().startsWith("/register") || event.getMessage().toLowerCase().startsWith("/login")) {
				event.setCancelled(false);
			} else {
				event.setCancelled(true);
				event.getPlayer().sendMessage("Você só pode usar comandos após se logar!");
			}
		}
	}

}
