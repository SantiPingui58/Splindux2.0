package me.santipingui58.splindux.economy;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.yapzhenyie.GadgetsMenu.api.GadgetsMenuAPI;
import com.yapzhenyie.GadgetsMenu.economy.GEconomyProvider;
import com.yapzhenyie.GadgetsMenu.player.OfflinePlayerManager;
import com.yapzhenyie.GadgetsMenu.player.PlayerManager;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.utils.WeightedRandomList;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class EconomyManager extends GEconomyProvider {

	


	public EconomyManager(Plugin plugin, String storage) {
		super(plugin, storage);
	}

	
	private static EconomyManager pm;
	
	
	
	  public static EconomyManager getManager() {
	        if (pm == null)
	            pm = new EconomyManager(Main.get(), "Splindux");

	        return pm;
	    }


	
	  
	  public void checkSplinboxes() {
		  for (SpleefPlayer sp : DataManager.getManager().getOnlinePlayers()) {
			  if (sp.getSplinboxPoints()>=18000) {
				  sp.resetSplinboxPoints();

				   int r = new Random().nextInt((100 - 1) + 1) + 1;
				  boolean luck = false;
				   
				  
				  if (sp.getPlayer().hasPermission("splinudux.extreme")) {
					  if (r<75) {
						  luck =true;
					  }
				  } else if (sp.getPlayer().hasPermission("splinudux.epic")) {
					  if (r<70) {
						  luck =true;
					  }
				  }else if (sp.getPlayer().hasPermission("splinudux.vip")) {
					  if (r<65) {
						  luck =true;
					  }
				  }else {
					  if (r<60) {
						  luck =true;
					  }
				  }
				  
				  if(luck) {
					  
				  WeightedRandomList<String> itemDrops = new WeightedRandomList<>();
				  itemDrops.addEntry("1",  25.0);
				  itemDrops.addEntry("2",   25.0);
				  itemDrops.addEntry("3", 20.0);
				  itemDrops.addEntry("4",   15.0);
				  itemDrops.addEntry("5",  15.0);
				  
				  String i = itemDrops.getRandom();
				  
				  Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gmysteryboxes give " + sp.getOfflinePlayer().getName()+ " 1 "  + i);
				  
				  int in = Integer.parseInt(i);
				  String stars = "";
				  int x = 0;
				  while (x<in) {
					  stars = stars + "✰";
					  x++;
				  }
				  
				  Bukkit.broadcastMessage("§fThe player &b" + sp.getOfflinePlayer().getName() + "§f has found a §bSplinBox §e" + stars + "§f!");
				  
				  
				  }
			  }
		  }
	  }
	  
	  
	
	  public void addCoins(SpleefPlayer sp, Integer i,boolean multiplier) {
	  
		  int coins = sp.getCoins();
			if (multiplier) {
			if (PermissionsEx.getUser(sp.getOfflinePlayer().getName()).has("splindux.extreme")) {
				i = (int) ((i*1.5));
			} else if (PermissionsEx.getUser(sp.getOfflinePlayer().getName()).has("splindux.epic")) {
				i = (int) ((i*1.25));
			} else if (PermissionsEx.getUser(sp.getOfflinePlayer().getName()).has("splindux.vip")) {
				i = (int) ((i*1.1));
			} 
			}
			coins = coins+i;
			sp.setCoins(coins);

			if (Bukkit.getOnlinePlayers().contains(sp.getOfflinePlayer())) {
				sp.getPlayer().sendMessage("§aYou have won §6"+i+" coins");
			}
	  } 
		
		
		
	//}

	  public PlayerManager getPlayerManager(Player player) {
		    PlayerManager playerManager = GadgetsMenuAPI.getPlayerManager(player);
			return playerManager;
		}
	  
	  
	@Override
	public void addMysteryDust(OfflinePlayerManager pm, int i) {
		
		OfflinePlayer p = pm.getPlayer();
		SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
		sp.setCoins(sp.getCoins()+i);
	}

	@Override
	public int getMysteryDust(OfflinePlayerManager pm) {
		OfflinePlayer p = pm.getPlayer();
		SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
		return sp.getCoins();
	}

	@Override
	public void removeMysteryDust(OfflinePlayerManager pm, int i) {		
		OfflinePlayer p = pm.getPlayer();
		SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
		sp.setCoins(sp.getCoins()-i);		
	}

	@Override
	public void setMysteryDust(OfflinePlayerManager arg0, int arg1) {
		
	}

	@Override
	public int syncMysteryDust(OfflinePlayerManager arg0) {
		return 0;
	}




	
	
	
	
	
}