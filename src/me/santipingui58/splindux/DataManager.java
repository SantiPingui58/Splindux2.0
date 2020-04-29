package me.santipingui58.splindux;

import java.net.InetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.SpleefArena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.replay.BrokenBlock;
import me.santipingui58.splindux.replay.GameReplay;
import me.santipingui58.splindux.scoreboard.hologram.HologramManager;
import me.santipingui58.splindux.stats.RankingType;
import me.santipingui58.splindux.stats.StatsManager;
import me.santipingui58.splindux.stats.level.LevelManager;
import me.santipingui58.splindux.utils.ItemBuilder;
import me.santipingui58.splindux.utils.Utils;
import me.santipingui58.translate.Language;




//In DataManager class is handled everything related to the Data of the Server, such as arenas, players, load of data and save of data, etc.

public class  DataManager {
	private static DataManager manager;	
	 public static DataManager getManager() {
	        if (manager == null)
	        	manager = new DataManager();
	        return manager;
	    }
	 
	 private List<SpleefArena> arenas = new ArrayList<SpleefArena>();
	 private List<SpleefPlayer> players = new ArrayList<SpleefPlayer>();
	 private List<GameReplay> recordings = new ArrayList<GameReplay>();
	 private HashMap<OfflinePlayer,SpleefPlayer> playershashmap = new HashMap<OfflinePlayer,SpleefPlayer>();
	 
	 public List<SpleefArena> getArenas() {
		 return this.arenas;
	 }
	 
	 public List<GameReplay> getReplays() {
		 return this.recordings;
	 }
	 
	 public List<SpleefPlayer> getPlayers() {
		 return this.players;
	 }
	 
	 public List<SpleefPlayer> getOnlinePlayers() {
		 List<SpleefPlayer> list = new ArrayList<SpleefPlayer>();
		 for (SpleefPlayer sp : getPlayers()) {
			 if (Bukkit.getOnlinePlayers().contains(sp.getPlayer())) {
			 list.add(sp);
			 }
		 }
		 return list;
	 }
	 
	 public HashMap<OfflinePlayer,SpleefPlayer> getPlayersCache() {
		 return this.playershashmap;
	 }
	 
	 
	 
		public void createSpleefPlayer(Player p) {
			Main.data.getConfig().set("players."+p.getUniqueId()+".stats.ELO",1000);
			 Main.data.getConfig().set("players."+p.getUniqueId()+".stats.1vs1_wins",0);
			 Main.data.getConfig().set("players."+p.getUniqueId()+".stats.1vs1_games",0);
			 Main.data.getConfig().set("players."+p.getUniqueId()+".stats.FFA_wins",0);
			 Main.data.getConfig().set("players."+p.getUniqueId()+".stats.FFA_games",0);
			 Main.data.getConfig().set("players."+p.getUniqueId()+".stats.FFA_kills",0);
			 
			 Main.data.getConfig().set("players."+p.getUniqueId()+".stats.monthly.FFA_wins",0);
			 Main.data.getConfig().set("players."+p.getUniqueId()+".stats.monthly.FFA_games",0);
			 Main.data.getConfig().set("players."+p.getUniqueId()+".stats.monthly.FFA_kills",0);
			 
			 Main.data.getConfig().set("players."+p.getUniqueId()+".stats.weekly.FFA_wins",0);
			 Main.data.getConfig().set("players."+p.getUniqueId()+".stats.weekly.FFA_games",0);
			 Main.data.getConfig().set("players."+p.getUniqueId()+".stats.weekly.FFA_kills",0);
			 Main.data.getConfig().set("players."+p.getUniqueId()+".dailywinlimit",0);
			
			 
			 Date now = new Date();
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			Main.data.getConfig().set("players."+p.getUniqueId()+".registerdate", format.format(now));
			Main.data.getConfig().set("players."+p.getUniqueId()+".onlinetime",0);
			Main.data.getConfig().set("players."+p.getUniqueId()+".coins",0);		
			Main.data.getConfig().set("players."+p.getUniqueId()+".options.translate",true);	
			Main.data.saveConfig();
			
			loadPlayer(p.getUniqueId().toString());
			
			p.kickPlayer("§cPlayerData created, please join again!");
			
			
			
		}
	
		
		public void loadReplays() {
			if (Main.recordings.getConfig().contains("replays")) {
				 Set<String> replays = Main.data.getConfig().getConfigurationSection("replays").getKeys(false);
				 for (String r : replays) {
					 r.split("");
				 }
			}
		}
		
		
		public void loadPlayer(String p) {
			 if (Main.data.getConfig().contains("players."+p)) {
				 int ELO = Main.data.getConfig().getInt("players."+p+".stats.ELO");
				 int _1vs1wins = Main.data.getConfig().getInt("players."+p+".stats.1vs1_wins");
				 int _1vs1games = Main.data.getConfig().getInt("players."+p+".stats.1vs1_games");
				 int FFAwins = Main.data.getConfig().getInt("players."+p+".stats.FFA_wins");
				 int FFAgames = Main.data.getConfig().getInt("players."+p+".stats.FFA_games");
				 int FFAkills = 0;
				 if (Main.data.getConfig().contains("players."+p+".stats.FFA_kills")) {
				  FFAkills = Main.data.getConfig().getInt("players."+p+".stats.FFA_kills");
				 }
				 
				 int FFAWeeklyWins = 0;
				 int FFAWeeklyGames = 0;
				 int FFAWeeklyKills = 0;
				 int FFAMonthlyWins =0;
				 int FFAMonthlyGames = 0;
				 int FFAMonthlyKills = 0;
				 int coins = 0;
				 int dailylimit= 0;
				 int level = 0;
				 String ip = "";
				 
				 boolean translate = false;
				 boolean nightVision = false;
				 Language language = null;
				 int totalonlinetime =  Main.data.getConfig().getInt("players."+p+".onlinetime");
				 if (Main.data.getConfig().contains("players."+p+".dailywinlimit")) {
					 dailylimit =  Main.data.getConfig().getInt("players."+p+".dailywinlimit");
				 }

				 if (Main.data.getConfig().contains("players."+p+".level")) {
					 level =  Main.data.getConfig().getInt("players."+p+".level");
				 }
				 if (Main.data.getConfig().contains("players."+p+".stats.weekly")) {
					 FFAWeeklyWins = Main.data.getConfig().getInt("players."+p+".stats.weekly.FFA_wins");
					 FFAWeeklyGames = Main.data.getConfig().getInt("players."+p+".stats.weekly.FFA_games");
					 FFAWeeklyKills = Main.data.getConfig().getInt("players."+p+".stats.weekly.FFA_kills");
					 }
				 
				 if (Main.data.getConfig().contains("players."+p+".stats.monthly")) {
					 FFAMonthlyWins = Main.data.getConfig().getInt("players."+p+".stats.monthly.FFA_wins");
					 FFAMonthlyGames = Main.data.getConfig().getInt("players."+p+".stats.monthly.FFA_games");
					 FFAMonthlyKills = Main.data.getConfig().getInt("players."+p+".stats.monthly.FFA_kills");
					 }
				 
				 if (Main.data.getConfig().contains("players."+p+".coins")) {
					 coins = Main.data.getConfig().getInt("players."+p+".coins");
				 }
				 
				 if (Main.data.getConfig().contains("players."+p+".IP")) {
					 ip = Main.data.getConfig().getString("players."+p+".IP");
				 }
				 
				 
				 if (Main.data.getConfig().contains("players."+p+".options.translate")) {
					 translate = Main.data.getConfig().getBoolean("players."+p+".options.translate");
				 }
				 
				 if (Main.data.getConfig().contains("players."+p+".options.nightvision")) {
					 nightVision = Main.data.getConfig().getBoolean("players."+p+".options.nightvision");
				 }
				 
				 if (Main.data.getConfig().contains("players."+p+".options.language")) {
					 language = Language.valueOf(Main.data.getConfig().getString("players."+p+".options.language"));
				 }
				 
				 if (Main.data.getConfig().contains("players."+p+".IP")) {
					 ip = Main.data.getConfig().getString("players."+p+".IP");
				 }
				 
				 String register = Main.data.getConfig().getString("players."+p+".registerdate");
				 Date date = null;
					   try {
						date=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(register);
					} catch (ParseException e) {
						e.printStackTrace();
					} 
				
				 
				 SpleefPlayer sp = new SpleefPlayer(UUID.fromString(p));	
				 sp.setTotalOnlineTIme(totalonlinetime);
				 sp.setELO(ELO);
				 sp.set1vs1Wins(_1vs1wins);
				 sp.set1vs1Games(_1vs1games);
				 sp.setFFAWins(FFAwins);
				 sp.setFFAGames(FFAgames);
				 sp.setFFAKills(FFAkills);
				 sp.setRegisterDate(date);
				 sp.setDailyWinLimit(dailylimit);
				 sp.setCoins(coins);
				 sp.setWeeklyFFAWins(FFAWeeklyWins);
				 sp.setWeeklyFFAGames(FFAWeeklyGames);
				 sp.setWeeklyFFAKills(FFAWeeklyKills);
				 sp.setLevel(level);
				 sp.setMonthlyFFAWins(FFAMonthlyWins);
				 sp.setMonthlyFFAGames(FFAMonthlyGames);
				 sp.setMonthlyFFAKills(FFAMonthlyKills);
				 sp.setIP(ip);
				 
				 sp.getOptions().setLanguage(language);
				 sp.getOptions().translate(translate);
				 sp.getOptions().nightVision(nightVision);
				 
				 this.players.add(sp);
				 if (sp.getOfflinePlayer().isOnline()) {
					sp.giveLobbyItems();
				 }
				 
			 }
			 
			 
		}
		
	 public void loadPlayers() {
			 if (Main.data.getConfig().contains("players")) {
				 Set<String> players = Main.data.getConfig().getConfigurationSection("players").getKeys(false);
				 for (String p : players) {
					 loadPlayer(p);
			 }
				
			 }
			 Bukkit.getServer().getLogger().info(this.players.size()+" players loaded!");
		 }
	 
	 
	 
	 public void savePlayers() {
		 for (SpleefPlayer sp : DataManager.getManager().getOnlinePlayers()) {
			 saveData(sp);
		 }
	 }
	 
	 public void saveReplays() {
		 for (GameReplay replay : DataManager.getManager().getReplays()) {
			 for (BrokenBlock broken : replay.getBrokenBlocks()) {
			 Main.recordings.getConfig().set("replays."+replay.getName()+".brokenblocks."+broken.getUUID().toString()+".time",broken.getTime());
			 Main.recordings.getConfig().set("replays."+replay.getName()+".brokenblocks."+broken.getUUID().toString()+".location",Utils.getUtils().setLoc(broken.getLocation(), false));
		 }
		 }
		 
		 Main.recordings.saveConfig();
	 }
	 
	 
	 public void saveData(SpleefPlayer sp) {
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".name",sp.getOfflinePlayer().getName());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".stats.totalgames",sp.getTotalGames());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".stats.ELO",sp.getELO());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".stats.1vs1_wins",sp.get1vs1Wins());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".stats.1vs1_games",sp.get1vs1Games());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".stats.FFA_wins",sp.getFFAWins());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".stats.FFA_games",sp.getFFAGames());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".stats.FFA_kills",sp.getFFAKills());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".level",sp.getLevel());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".onlinetime", sp.getTotalOnlineTime());
		
		 
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".stats.weekly.FFA_wins",sp.getWeeklyFFAWins());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".stats.weekly.FFA_games",sp.getWeeklyFFAGames());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".stats.weekly.FFA_kills",sp.getWeeklyFFAKills());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".stats.monthly.FFA_wins",sp.getMonthlyFFAWins());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".stats.monthly.FFA_games",sp.getMonthlyFFAGames());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".stats.monthly.FFA_kills",sp.getMonthlyFFAKills());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".coins",sp.getCoins());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".dailywinlimit",sp.getDailyWinLimit());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".country",sp.getCountry());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".options.translate",sp.getOptions().hasTranslate());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".options.nightvision",sp.getOptions().hasNightVision());
		 Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".options.language",sp.getOptions().getLanguage().toString());
		 
		 if (sp.getPlayer()!=null) {
		 saveIP(sp.getPlayer());
		 }
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			if (sp.getOfflinePlayer().isOnline()) {
		Main.data.getConfig().set("players."+sp.getOfflinePlayer().getUniqueId()+".lastlogin", format.format(sp.getLastLogin()));
			}
		
		 Main.data.saveConfig();
		 
	 }
	  
	 
	 public void saveIP(Player p) {
		 InetAddress ip = p.getAddress().getAddress();
		 if (!Main.data.getConfig().contains("players."+p.getUniqueId()+".IP")) {
			 if (!ip.toString().equalsIgnoreCase(Main.data.getConfig().getString("players."+p.getUniqueId()+".IP"))) {
		   Main.data.getConfig().set("players." + p.getUniqueId() + ".IP", ip.toString());
		   Main.data.saveConfig();
			 }
			 
			
		 }
		 
		 if (!Main.data.getConfig().contains("players."+p.getUniqueId()+".country")) {
			 if (!ip.toString().equalsIgnoreCase(Main.data.getConfig().getString("players."+p.getUniqueId()+".IP"))) {
		   Main.data.getConfig().set("players." + p.getUniqueId() + ".IP", ip.toString());
		   Main.data.saveConfig();
			 }
			 
			
		 }
		   
	 }
	 public SpleefArena loadArena(String name, Location mainspawn,Location spawn1,Location spawn2,Location lobby,Location arena1,Location arena2,SpleefType spleeftype,GameType gametype,Material item,int min,int max) {  
		 SpleefArena a = null;
		 if (gametype.equals(GameType.FFA)) {
		  a = new SpleefArena(name,mainspawn,lobby,arena1,arena2,spleeftype,gametype);
		 } else if (gametype.equals(GameType.DUEL)) {
			 a = new SpleefArena(name,spawn1,spawn2,arena1,arena2,spleeftype,gametype,item,min,max);
		 }
        this.arenas.add(a);      
       a.reset(false,true);
        return a;
    }
    
    
    
    public void loadArenas() { 
    	int arenasint = 0;
    	if (Main.arenas.getConfig().contains("arenas")) {
    	Set<String> arenas = Main.arenas.getConfig().getConfigurationSection("arenas").getKeys(false);
    	
    		for (String b : arenas) {		
    			try {
    			
        			Location arena1 = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas." + b + ".arena1"), false);
        			Location arena2 = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas." + b + ".arena2"), false);
    				String stype = Main.arenas.getConfig().getString("arenas."+b+".spleeftype");
    				String gtype = Main.arenas.getConfig().getString("arenas."+b+".gametype");
    				stype = stype.toUpperCase();	
    				gtype = gtype.toUpperCase();	
    				SpleefType spleeftype = SpleefType.valueOf(stype);
    				GameType gametype = GameType.valueOf(gtype);
    				
    				if (gametype.equals(GameType.FFA)) {
    				Location lobby = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas." + b + ".lobby"), false);	
    				Location mainspawn = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas." + b + ".mainspawn"), false);
    				DataManager.getManager().loadArena(b,mainspawn,null,null,lobby,arena1,arena2,spleeftype,gametype,null,0,0);
    				} else if (gametype.equals(GameType.DUEL)) {
    					Location spawn1 = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas." + b + ".spawn1"), true);
            			Location spawn2 = Utils.getUtils().getLoc(Main.arenas.getConfig().getString("arenas." + b + ".spawn2"), true);
            			int min = Main.arenas.getConfig().getInt("arenas."+b+".min_size");
						int max = Main.arenas.getConfig().getInt("arenas."+b+".max_size");
            			String it = null;
            			 it = Main.arenas.getConfig().getString("arenas."+b+".item");
         				it = it.toUpperCase();
         				Material item = Material.valueOf(it);
            			DataManager.getManager().loadArena(b,null,spawn1,spawn2,null,arena1,arena2,spleeftype,gametype,item,min,max);
    				}
				arenasint++;
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
    		}
    		
    	}
    		Main.get().getLogger().info(arenasint+ " arenas cargadas!");
    }
     
    
    @SuppressWarnings("deprecation")
	public void resetMonthlyStats() {
    	HologramManager.getManager().updateHolograms();
    	for (String s : Main.data.getConfig().getConfigurationSection("players").getKeys(false)) {
    		Main.data.getConfig().set("players."+s+".stats.monthly.FFA_kills", 0);
    		Main.data.getConfig().set("players."+s+".stats.monthly.FFA_games", 0);
    		Main.data.getConfig().set("players."+s+".stats.monthly.FFA_wins", 0);
    	}
    	
    	
    	Main.data.saveConfig();
    	
    	for (SpleefPlayer sp : this.players) {
    		sp.setMonthlyFFAGames(0);
    		sp.setMonthlyFFAKills(0);
    		sp.setMonthlyFFAWins(0);
    	}
    	
    	
    	HashMap<String, Integer> hashmap = StatsManager.getManager().getRanking(RankingType.SPLEEFFFA_WINS_MONTHLY);
    	HashMap<String,Integer> topPositions = new HashMap<String,Integer>();
   	 Iterator<Entry<String, Integer>> it = hashmap.entrySet().iterator();
   	 int i = 1;
   	    while (it.hasNext()) {
   	        Map.Entry<String,Integer> pair = (Map.Entry<String,Integer>)it.next();
   	        while(i<=10) {
   	        String s = pair.getKey();
   	       topPositions.put(s, i);
   	       i++;
   	        } 
   	    }
   	
   	
   	for (String s : Main.data.getConfig().getConfigurationSection("players").getKeys(false)) {
   		Main.data.getConfig().set("players."+s+".stats.weekly.FFA_kills", 0);
   		Main.data.getConfig().set("players."+s+".stats.weekly.FFA_games", 0);
   		Main.data.getConfig().set("players."+s+".stats.weekly.FFA_wins", 0);
   		String name = Bukkit.getOfflinePlayer(s).getName();
   		if (topPositions.containsKey(name)) {
   			int stars = 0;
   			if (topPositions.get(name)==1) stars = 5;
   			else if (topPositions.get(name)<=4) stars = 4;
   			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gmysteryboxes give " + name+ " 3 "  + stars);
   			
   			OfflinePlayer p = Bukkit.getOfflinePlayer(name);
   			SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
   			
   			switch(topPositions.get(name)) {
   			case 1:LevelManager.getManager().addLevel(sp, 750);
   			case 2:LevelManager.getManager().addLevel(sp, 625);
   			case 3:LevelManager.getManager().addLevel(sp, 560);
   			case 4:LevelManager.getManager().addLevel(sp, 500);
   			case 5:LevelManager.getManager().addLevel(sp, 430);
   			case 6:LevelManager.getManager().addLevel(sp, 375);
   			case 7:LevelManager.getManager().addLevel(sp, 310);
   			case 8:LevelManager.getManager().addLevel(sp, 250);
   			case 9:LevelManager.getManager().addLevel(sp, 180);
   			case 10:LevelManager.getManager().addLevel(sp, 125);
   			}
		
   		}
   	}
    }

    public void resetDailyWinsLimit() {
    	for (String s : Main.data.getConfig().getConfigurationSection("players").getKeys(false)) {
    		Main.data.getConfig().set("players."+s+".dailywinlimit", 0);
    	}
    	
    	Main.data.saveConfig();
    }
    
    @SuppressWarnings("deprecation")
	public void resetWeeklyStats() {
    	HologramManager.getManager().updateHolograms();
    	
    	HashMap<String, Integer> hashmap = StatsManager.getManager().getRanking(RankingType.SPLEEFFFA_WINS_WEEKLY);
    	
    	HashMap<String,Integer> topPositions = new HashMap<String,Integer>();
    	 Iterator<Entry<String, Integer>> it = hashmap.entrySet().iterator();
    	 int i = 1;
    	    while (it.hasNext()) {
    	        Map.Entry<String,Integer> pair = (Map.Entry<String,Integer>)it.next();
    	        while(i<=10) {
    	        String s = pair.getKey();
    	       topPositions.put(s, i);
    	       i++;
    	        } 
    	    }
    	
    	
    	for (String s : Main.data.getConfig().getConfigurationSection("players").getKeys(false)) {
    		Main.data.getConfig().set("players."+s+".stats.weekly.FFA_kills", 0);
    		Main.data.getConfig().set("players."+s+".stats.weekly.FFA_games", 0);
    		Main.data.getConfig().set("players."+s+".stats.weekly.FFA_wins", 0);
    		String name = Bukkit.getOfflinePlayer(s).getName();
    		if (topPositions.containsKey(name)) {
    			int stars = 0;
    			if (topPositions.get(name)==1) stars = 5;
    			else if (topPositions.get(name)<=4) stars = 4;
    			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gmysteryboxes give " + name+ " 1 "  + stars);
    			
    			OfflinePlayer p = Bukkit.getOfflinePlayer(name);
    			SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
    			
    			switch(topPositions.get(name)) {
    			case 1:LevelManager.getManager().addLevel(sp, 250);
    			case 2:LevelManager.getManager().addLevel(sp, 230);
    			case 3:LevelManager.getManager().addLevel(sp, 190);
    			case 4:LevelManager.getManager().addLevel(sp, 170);
    			case 5:LevelManager.getManager().addLevel(sp, 150);
    			case 6:LevelManager.getManager().addLevel(sp, 130);
    			case 7:LevelManager.getManager().addLevel(sp, 110);
    			case 8:LevelManager.getManager().addLevel(sp, 90);
    			case 9:LevelManager.getManager().addLevel(sp, 70);
    			case 10:LevelManager.getManager().addLevel(sp, 50);
    			}
		
    		}
    		 
    	}
    	
    	 	
    	Main.data.saveConfig();
    	
    	 
    	
    	for (SpleefPlayer sp : this.players) {
    		sp.setWeeklyFFAGames(0);
    		sp.setWeeklyFFAKills(0);
    		sp.setWeeklyFFAWins(0);
    	}
    	
    	
    }
    public ItemStack[] gameitems() {
		ItemStack iron_shovel = new ItemStack(Material.IRON_SPADE);
		ItemMeta ironMeta = iron_shovel.getItemMeta();
		ironMeta.setUnbreakable(true);
		ironMeta.addEnchant(Enchantment.DIG_SPEED, 5, true);
		ironMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		iron_shovel.setItemMeta(ironMeta);
		
		ItemStack diamond_shovel = new ItemStack(Material.DIAMOND_SPADE);
		ItemMeta diamondMeta = diamond_shovel.getItemMeta();
		diamondMeta.setUnbreakable(true);
		diamondMeta.addEnchant(Enchantment.DIG_SPEED, 5, true);
		diamondMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		diamond_shovel.setItemMeta(diamondMeta);
		
		ItemStack golden_shovel = new ItemStack(Material.GOLD_SPADE);
		ItemMeta goldenMeta = diamond_shovel.getItemMeta();
		goldenMeta.setUnbreakable(true);
		goldenMeta.addEnchant(Enchantment.DIG_SPEED, 5, true);
		goldenMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		golden_shovel.setItemMeta(goldenMeta);
		
		ItemStack x2snowball = new ItemBuilder(Material.SNOW_BALL).setAmount(2).build();
		ItemStack x4snowball = new ItemBuilder(Material.SNOW_BALL).setAmount(4).build();
		ItemStack x6snowball = new ItemBuilder(Material.SNOW_BALL).setAmount(6).build();
		ItemStack x8snowball = new ItemBuilder(Material.SNOW_BALL).setAmount(8).build();
		ItemStack x10snowball = new ItemBuilder(Material.SNOW_BALL).setAmount(10).build();
		
		ItemStack redflag = new ItemBuilder(Material.WOOL,1,(byte)14).build();
		ItemStack blueflag = new ItemBuilder(Material.WOOL,1,(byte)11).build();
	
		ItemStack[] items = {iron_shovel, diamond_shovel, x2snowball,x4snowball,x6snowball,x8snowball,x10snowball,blueflag,redflag,golden_shovel};
		return items;
	}
    
	public ItemStack[] lobbyitems() {
		
		ItemStack gadgets = new ItemBuilder(Material.CHEST).setTitle("§6§lGadgets").build();
		ItemStack ranked = new ItemBuilder(Material.NETHER_STAR).setTitle("§a§lRanked").addLore("§cComing Soon").build();
		ItemStack[] items = {ranked,gadgets};
		return items;
	}
	
	public ItemStack[] queueitems() {
		ItemStack powerups = new ItemBuilder(Material.ENDER_CHEST).setTitle("§d§lPowerUps").addLore("§cComing Soon").build();
		ItemStack leave = new ItemBuilder(Material.REDSTONE_TORCH_ON).setTitle("§c§lLeave").build();
		ItemStack[] items = {powerups, leave};
		return items;
	}
	
	
	
	public Language languageFromCountry(String s) {
		if (s.equalsIgnoreCase("AR") 
			|| s.equalsIgnoreCase("ES")
			|| s.equalsIgnoreCase("BO")
			|| s.equalsIgnoreCase("BR")
			|| s.equalsIgnoreCase("CL")
			|| s.equalsIgnoreCase("UY")
			|| s.equalsIgnoreCase("PY")
			|| s.equalsIgnoreCase("CO")
			|| s.equalsIgnoreCase("PE")
			|| s.equalsIgnoreCase("VE")
			|| s.equalsIgnoreCase("MX")
			|| s.equalsIgnoreCase("CU")
			|| s.equalsIgnoreCase("EC")
			|| s.equalsIgnoreCase("GT")
			|| s.equalsIgnoreCase("HN")) {
			return Language.SPANISH;
		} else if (s.equalsIgnoreCase("RU")
				|| s.equalsIgnoreCase("BY")
				|| s.equalsIgnoreCase("KZ")
				|| s.equalsIgnoreCase("KG")
				|| s.equalsIgnoreCase("UZ")){
			return Language.RUSSIAN;
		} else {
			return Language.ENGLISH;
		}
	}

	
	}
