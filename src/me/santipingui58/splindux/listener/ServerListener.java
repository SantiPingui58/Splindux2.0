package me.santipingui58.splindux.listener;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;

import com.yapzhenyie.GadgetsMenu.api.GadgetsMenuAPI;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.GameState;
import me.santipingui58.splindux.game.death.DeathReason;
import me.santipingui58.splindux.game.death.SpleefKill;
import me.santipingui58.splindux.game.spleef.SpleefArena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.utils.Utils;
import net.apcat.simplesit.SimpleSitPlayer;
import net.apcat.simplesit.events.PlayerSitEvent;
import net.apcat.simplesit.events.PlayerStopSittingEvent;
import net.md_5.bungee.api.ChatColor;

public class ServerListener implements Listener {

	private List<SpleefPlayer> cooldown = new ArrayList<SpleefPlayer>();
	@EventHandler
	public void onSmelt(BlockFadeEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onDecay(LeavesDecayEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler 
	public void onDeath(EntityDamageByEntityEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onHunger(FoodLevelChangeEvent e) {
		e.setCancelled(true);
	}
	
	/*@EventHandler
	public void onSign(SignChangeEvent e) {
		
			for (int i = 0; i < 4; i++) {
	            String line = e.getLine(i);
	            if (line != null && !line.equals("")) {
	                e.setLine(i, ChatColor.translateAlternateColorCodes('&', line));
	            }
			}
		
		if (e.getLine(0).equalsIgnoreCase("[Spleef]") && e.getLine(1).equalsIgnoreCase("Join")) {
			for (SpleefArena arena : DataManager.getManager().getArenas()) {
				if (arena.getName().equals(e.getLine(2))) {
					e.setLine(0, "�0�l[Spleef]");
					e.setLine(1, "�aJoin");
					e.setLine(2, "�5�l"+arena.getName());
					return;
				}
			}
			e.getPlayer().sendMessage("�cThe arena �b"+ e.getLine(2) + " �cdoesnt exist.");
		} else if (e.getLine(0).equalsIgnoreCase("[Spleef]") && e.getLine(1).equalsIgnoreCase("Leave")) {
			for (SpleefArena arena : DataManager.getManager().getArenas()) {
				if (arena.getName().equals(e.getLine(2))) {
					e.setLine(0, "�0�l[Spleef]");
					e.setLine(1, "�cLeave");
					e.setLine(2, "�5�l"+arena.getName());
					return;
				}
			}
			e.getPlayer().sendMessage("�cThe arena �b"+ e.getLine(2) + " �cdoesnt exist.");
		} else if (e.getLine(0).equalsIgnoreCase("[Spleef]") && e.getLine(1).equalsIgnoreCase("Leaderboard")
				&& e.getLine(2).equalsIgnoreCase("FFAWins"))  {
			e.setCancelled(true);
			LeaderboardManager.getManager().generateWallLeaderboard(LeaderboardType.ALL_TIME_FFA_WINS, Manager.getManager().getSpleefPlayer(e.getPlayer()), e.getBlock().getLocation());
		}
	}
	*/
	
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		 SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
		 if (sp.needsAdminLoginQuestionmark() && !sp.isLogged()) {
			 e.setCancelled(true);
			 return;
		 }
		                                                          
		                                                         
	    if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
	    	if (p.getItemInHand().isSimilar(new ItemStack(Material.GOLD_SPADE))) {
	    		Snowball snowball = p.getWorld().spawn(p.getEyeLocation(), Snowball.class);
	    		snowball.setVelocity(p.getLocation().getDirection().multiply(1.5)); 
	    		snowball.setShooter(p);
	    	} else if (p.getItemInHand().equals(DataManager.getManager().queueitems()[1])) {
	    			GameManager.getManager().leaveQueue(sp, GameManager.getManager().getArenaByPlayer(sp));    		
	    	} else if (p.getItemInHand().equals(DataManager.getManager().lobbyitems()[0])) {
	    		GadgetsMenuAPI.goBackToMainMenu(p);
	    	}
	    }
		
	}
	
	@EventHandler
	public void onInventoryMove(InventoryClickEvent e) {		
		for (ItemStack i : DataManager.getManager().lobbyitems()) {
			if (e.getCurrentItem()!=null) {
				if (e.getCurrentItem().equals(i)) {
					e.setCancelled(true);
					return;
				}
			}
		}
		
		for (ItemStack i : DataManager.getManager().queueitems()) {
			if (e.getCurrentItem()!=null) {
				if (e.getCurrentItem().equals(i)) {
					e.setCancelled(true);
					return;
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	  public void onProjectileHit(ProjectileHitEvent e)  {
		if (e.getEntity().getShooter() instanceof Player) {
	    Player p = (Player)e.getEntity().getShooter();
	    SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
	    if (GameManager.getManager().isInGame(sp)) {
	    	if (GameManager.getManager().getArenaByPlayer(sp).getState().equals(GameState.GAME)) {
	    BlockIterator iterator = new BlockIterator(e.getEntity().getWorld(), e.getEntity().getLocation().toVector(), e.getEntity().getVelocity().normalize(), 0.0D, 4);
	    Block hitblock = null;
	    while (iterator.hasNext()) {
	      hitblock = iterator.next();

	      if (hitblock.getTypeId() != 0)
	      {
	        break;
	      }
	    }
	    if (hitblock.getType() == Material.SNOW_BLOCK)
	    {
	      p.playSound(hitblock.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.5F, 2.0F);
	      hitblock.setType(Material.AIR);
	      SpleefArena arena = GameManager.getManager().getArenaByPlayer(sp);
	  
	      arena.getKills().add(new SpleefKill(hitblock.getLocation(),sp,DeathReason.SNOWBALLED));
	    }
	    
	    	}
	  } 
	    }
		}
	
	@EventHandler
	public static void onCommand(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();	
		 SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
		
		 String msg = e.getMessage();
		    String[] args = msg.split(" ");
		    if ((args.length >= 1) &&  (args[0].startsWith("/"))) {
		    	 if (sp.needsAdminLoginQuestionmark() && !sp.isLogged()) {
		    		 if (!args[0].equalsIgnoreCase("/splinduxregister") && !args[0].equalsIgnoreCase("/splinduxlogin")) {
		    			 e.setCancelled(true);
						 return;
		    		 }	
				 }
		    	
		    	if (args[0].equalsIgnoreCase("/sit")) {
		    		   e.setCancelled(true);
		    		   
				       if (e.getPlayer().hasPermission("splindux.sit")) {
				    	  
				    	   if (!GameManager.getManager().isInGame(sp)) {
				    	   SimpleSitPlayer player = new SimpleSitPlayer(p);
				    	    if (player.isSitting()) {
				    	      player.setSitting(false);
				    	    }
				    	    else if (player.getBukkitPlayer().isOnGround())
				    	      player.setSitting(true);
				    	   } else {
									p.sendMessage("�cYou can't execute this command while playing a match.");								
				    	   }
				       } else {

					p.sendMessage("�cYou don't have permission to execute this command.");
					p.sendMessage("�aYou need a rank "
							+ "�6[Donator] �ato use this, visit the store for more info: �bhttp://jhspleef.buycraft.net/");
						
								} 
				       
				      } else if (args[0].equalsIgnoreCase("/lay")) {
				    	   e.setCancelled(true);
				      } 
		    }
	
		    }
	
	  @EventHandler
	  public void onSit(PlayerSitEvent e) {
			  e.setMessage("�aYou have sat");
		  
	  }
	  
	  
	  @EventHandler
	  public void onExitSit(PlayerStopSittingEvent e) {
			  e.setMessage("�cYou have stood up");		  
	  }
	  
	  
	  @EventHandler
	  public void onParkour (PlayerInteractEvent e) {
	  if(e.getAction().equals(Action.PHYSICAL)){
	  if(e.getClickedBlock().getType() == Material.GOLD_PLATE){
		  SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(e.getPlayer());
		  if (!cooldown.contains(sp)) {
			  cooldown.add(sp);
			  new BukkitRunnable() {
				  public void run() {
					  if (cooldown.contains(sp)) {
					  cooldown.remove(sp);
					  }
				  }
			  }.runTaskLater(Main.get(), 20L*2);
		  if (e.getPlayer().getLocation().distance(Utils.getUtils().getLoc(Main.arenas.getConfig().getString("parkour.start")))<2) {
	
			  sp.getPlayer().sendMessage(ChatColor.YELLOW+"You have started the parkour!");
			  sp.joinParkour();
		  } else if (e.getPlayer().getLocation().distance(Utils.getUtils().getLoc(Main.arenas.getConfig().getString("parkour.end")))<2) {
			  if (sp.isInParkour()) {
			  sp.getPlayer().sendMessage(ChatColor.YELLOW+"You have finished the parkour!");
			  sp.getPlayer().sendMessage(ChatColor.YELLOW+"Your time: " +getParkourTime(sp)+"s");
			  sp.leaveParkour();
			  }
		  }
	  }  
	  }
	  }
	  }	  
	  
	  
	  
	  @EventHandler
	  public void onTeleport(PlayerTeleportEvent e) {
		  Player p = e.getPlayer();
		  SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
		  if (sp.isSpectating()) {
		 if( e.getCause().equals(TeleportCause.SPECTATE)) {
			 e.setCancelled(true);
		 }		  
	  }
		  }
	  
	  private String getParkourTime(SpleefPlayer sp) {
		  int s = sp.getParkourTimer();
		  int seconds = s /2;
		  int mili = s %2;
		  return String.format("%02d.%02d",  seconds, mili);
	  }
	  
	  
	  

}
