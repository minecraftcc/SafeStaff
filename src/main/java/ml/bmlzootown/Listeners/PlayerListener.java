package ml.bmlzootown.Listeners;

import java.util.logging.Logger;

import ml.bmlzootown.BlockHeights;
import ml.bmlzootown.SafeStaff;
import ml.bmlzootown.Tasks.AirFreshener;
import ml.bmlzootown.Tasks.AntiBurnCreme;
import org.bukkit.ChatColor;
//import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;

import static ml.bmlzootown.SafeStaff.sendPluginMessage;

public class PlayerListener implements Listener{
	public SafeStaff pl;
	Logger log = Logger.getLogger("Minecraft");
	
	public PlayerListener(SafeStaff plugin){
		this.pl = plugin;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Location belowp;
		Player p = event.getPlayer();
		if (SafeStaff.hasPerm(p, "SafeStaff.use")) {
			p.sendMessage(ChatColor.RED + "Please login! /login [password]");
			this.pl.notLoggedIn.put(p.getName(), "true");
			this.pl.attempts.put(p.getName(), Integer.valueOf(this.pl.getConfig().getInt("maxAttempts")));
			sendPluginMessage(this.pl, "SafeStaff", "Joined", p.getUniqueId().toString());

			belowp = p.getLocation().subtract(0,1,0).getBlock().getLocation();
			Material belowb = p.getLocation().subtract(0,1,0).getBlock().getType();

			boolean test = true;
			Material below = p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType();
			if (below.name().toLowerCase().contains("_slab")) {
				test = false;
			}
			while (test) {
				if (belowp.getBlock().getType() == Material.AIR) {
					if (belowp.getY() < 1) {
						break;
					}
					belowp = belowp.subtract(0,1,0);
				} else {
					test = false;
				}
			}

			//if (!p.getGameMode().equals(GameMode.CREATIVE)) {
				if (belowb.equals(Material.AIR)) {
					if (p.getLocation().getBlock().getType().equals(Material.AIR)) {

						Double height = 0.0;
						Location loc = belowp.getBlock().getLocation().add(0.5, 0, 0.5);

						for (BlockHeights bh : BlockHeights.values()) {
							String bName = belowp.getBlock().getType().name();
							//if (bName.equalsIgnoreCase(bh.name())) {
							if (bName.toLowerCase().contains(bh.name().toLowerCase())) {
								height = bh.getHeights();
							}
						}

						//if (belowp.getBlock().getType().name().equalsIgnoreCase(Material.STEP.name())) {
						if (belowp.getBlock().getType().name().toLowerCase().contains("_slab")) {
							int stepData = (int) belowp.getBlock().getData();
							if (stepData >= 0 && stepData <= 7) {
								height = 0.5;
							} else {
								height = 0.0;
							}
						}

						if (belowp.getBlock().getType().name().equalsIgnoreCase(Material.LAVA.name())) {
							AntiBurnCreme.noBurn(p, p.getHealth(), p.getFoodLevel());
						}

						if (height != 0.0) {
							p.teleport(loc.add(0, height, 0));
						} else {
							p.teleport(loc.add(0, 1, 0));
						}

					}
				}

				Block phead = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() + 1.0, p.getLocation().getZ()).getBlock();
				Block pbod =  p.getLocation().getBlock();
				Block pbelow = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() - 1.0, p.getLocation().getZ()).getBlock();

				if (phead.getType() == Material.WATER) {
					AirFreshener.airFreshener(p,this.pl);
				}
				if (phead.getType() == Material.LAVA) {
					AntiBurnCreme.noBurn(p, p.getHealth(), p.getFoodLevel());
				} else if (pbod.getType() == Material.LAVA) {
					AntiBurnCreme.noBurn(p, p.getHealth(), p.getFoodLevel());
				} else if (pbelow.getType() == Material.LAVA) {
					AntiBurnCreme.noBurn(p, p.getHealth(), p.getFoodLevel());
				}

			//}
		}

	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onCancelDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (this.pl.notLoggedIn.get(p.getName()) == "true") {
					e.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if (this.pl.notLoggedIn.get(p.getName()) == "true") {
			event.setCancelled(true);
		}
	}


	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDamagedByEntityEvent(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			Player p = (Player) event.getDamager();
			if (this.pl.notLoggedIn.get(p.getName()) == "true") {
				p.sendMessage(ChatColor.RED + "Please login! /login [password]");
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event) {
		Player p = event.getPlayer();
		if (this.pl.notLoggedIn.get(p.getName()) == "true") {
			p.sendMessage(ChatColor.RED + "Please login! /login [password]");
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
	    Player p = event.getPlayer();
	    if (this.pl.notLoggedIn.get(p.getName()) == "true") {
	      p.sendMessage(ChatColor.RED + "Please login! /login [password]");
	      event.setCancelled(true);
	    }
	}
	
	@EventHandler(ignoreCancelled = false, priority = EventPriority.LOWEST)
	public void onPlayerMove(PlayerMoveEvent event) {
	    Player p = event.getPlayer();
	    if (this.pl.notLoggedIn.get(p.getName()) == "true") {
			event.getPlayer().teleport(event.getFrom());
			//event.setCancelled(true);
	    }
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerDrop(PlayerDropItemEvent event) {
	    Player p = event.getPlayer();
	    if (this.pl.notLoggedIn.get(p.getName()) == "true"){
	      p.sendMessage(ChatColor.RED + "Please login! /login [password]");
	      event.setCancelled(true);
	    }
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerPickup(PlayerPickupItemEvent event) {
		Player p = event.getPlayer();
		if (this.pl.notLoggedIn.get(p.getName()) == "true"){
			p.sendMessage(ChatColor.RED + "Please login! /login [password]");
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockBreak(BlockBreakEvent event) {
	    Player p = event.getPlayer();
	    if (this.pl.notLoggedIn.get(p.getName()) == "true"){
	      p.sendMessage(ChatColor.RED + "Please login! /login [password]");
	      event.setCancelled(true);
	    }
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockPlace(BlockPlaceEvent event) {
	    Player p = event.getPlayer();
	    if (this.pl.notLoggedIn.get(p.getName()) == "true"){
	      p.sendMessage(ChatColor.RED + "Please login! /login [password]");
	      event.setCancelled(true);
	    }
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
	    Player p = event.getPlayer();
	    String command = event.getMessage();
	    if ((!command.contains("login")) && 
	      (this.pl.notLoggedIn.get(p.getName()) == "true")) {
	      p.sendMessage(ChatColor.RED + "Please login! /login [password]");
	      event.setCancelled(true);
	    }
	}
	
}
