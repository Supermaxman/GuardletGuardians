package me.supermaxman.gg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;



public class GG extends JavaPlugin {
    public static FileConfiguration conf;
	public static GG plugin;
	public static final Logger log = Logger.getLogger("Minecraft");
	public static GGGame game;
	
    public static IconMenu menu;
	
    
    
	public void onEnable() {
		
		plugin = this;
		saveDefaultConfig();
        getCommand("gg").setExecutor(new GGExecutor(this));    
        
		getServer().getPluginManager().registerEvents(new GGListener(), plugin);
		
		log.info(getName() + " has been enabled.");
		startGame();
		setupMenu();
		
	}
	
	static void setupMenu() {
		menu = new IconMenu("Shop", 18, new IconMenu.OptionClickEventHandler() {
	        @Override
	        public void onOptionClick(IconMenu.OptionClickEvent event) {
	        	if(game.hasEnoughCoins(event.getPlayer().getName(), Integer.parseInt(GG.conf.getString("settings.items."+event.getName().replace(" ", "").toLowerCase())))) {
	        		if(event.getPlayer().getName().equals(game.getThread().tempg) && event.getName().contains("Ball")) {
			        	game.removePlayerCoins(event.getPlayer().getName(), Integer.parseInt(GG.conf.getString("settings.items."+event.getName().replace(" ", "").toLowerCase())));
			            game.addGitem(event.getPlayer().getName(), event.getItems());
		            	event.getPlayer().sendMessage(ChatColor.AQUA+"[GG]: You have purchased 3 "+ event.getName()+"s for the next round!");
	        		}else if(GG.game.isPlayer(event.getPlayer().getName()) && !event.getPlayer().getName().equals(game.getThread().tempg)) {
			        	game.removePlayerCoins(event.getPlayer().getName(), Integer.parseInt(GG.conf.getString("settings.items."+event.getName().replace(" ", "").toLowerCase())));
			            game.addRitem(event.getPlayer().getName(), event.getItems());
		            	event.getPlayer().sendMessage(ChatColor.AQUA+"[GG]: You have purchased 3 "+ event.getName()+"s for the next round!");
	        		}else {
			            event.getPlayer().sendMessage(ChatColor.RED+"[GG]: You are not playing that role this round!");
	        		}
	        	}else {
		            event.getPlayer().sendMessage(ChatColor.RED+"[GG]: You do not have enough coins!");
	        	}
	            event.setWillClose(false);
	        }
	    }, plugin)
	    .setOption(0, new ItemStack(Material.TNT, 3), "Explosive Ball", ChatColor.GOLD+GG.conf.getString("settings.items."+"Explosive Ball".replace(" ", "").toLowerCase())+" coins: ",ChatColor.DARK_PURPLE+"Use this ball to push",ChatColor.DARK_PURPLE+"runners around and disrupt",ChatColor.DARK_PURPLE+"their movement")//add price to lore
	    .setOption(1, new ItemStack(Material.FIREBALL, 3), "Fire Ball", ChatColor.GOLD+GG.conf.getString("settings.items."+"Fire Ball".replace(" ", "").toLowerCase())+" coins: ",ChatColor.DARK_PURPLE+"Hurt those runners with",ChatColor.DARK_PURPLE+"fire and disrupt their movement")
	    .setOption(2, new ItemStack(Material.WEB, 3), "Slow Ball", ChatColor.GOLD+GG.conf.getString("settings.items."+"Slow Ball".replace(" ", "").toLowerCase())+" coins: ",ChatColor.DARK_PURPLE+"Slow down the runners so",ChatColor.DARK_PURPLE+"they are easy to hit")
	    .setOption(3, new ItemStack(Material.CACTUS, 3), "Confuse Ball", ChatColor.GOLD+GG.conf.getString("settings.items."+"Confuse Ball".replace(" ", "").toLowerCase())+" coins: ",ChatColor.DARK_PURPLE+"Confuse runners so they",ChatColor.DARK_PURPLE+"run the wrong way")
	    .setOption(4, new ItemStack(Material.ARROW, 3), "Minion Ball", ChatColor.GOLD+GG.conf.getString("settings.items."+"Minion Ball".replace(" ", "").toLowerCase())+" coins: ",ChatColor.DARK_PURPLE+"Distract runners with a",ChatColor.DARK_PURPLE+"little baby zombie minion")
	    
	    .setOption(9, new ItemStack(Material.CLAY_BRICK, 3), "Armor", ChatColor.GOLD+GG.conf.getString("settings.items."+"Armor".replace(" ", "").toLowerCase())+" coins: ",ChatColor.DARK_PURPLE+"Become invincible to Guardian",ChatColor.DARK_PURPLE+"damage for a short time")
	    .setOption(10, new ItemStack(Material.CLAY_BALL, 3), "Camouflage", ChatColor.GOLD+GG.conf.getString("settings.items."+"Camouflage".replace(" ", "").toLowerCase())+" coins: ",ChatColor.DARK_PURPLE+"Go temporarily invisible",ChatColor.DARK_PURPLE+"to hide from Guardians")
	    .setOption(11, new ItemStack(Material.SUGAR_CANE, 3), "Sprint Boost", ChatColor.GOLD+GG.conf.getString("settings.items."+"Sprint Boost".replace(" ", "").toLowerCase())+" coins: ",ChatColor.DARK_PURPLE+"Sprint even faster",ChatColor.DARK_PURPLE+"with this sprint boost");
	}
	

	static void startGame() {
		try {
			plugin.reloadConfig();
			conf = plugin.getConfig();
			int min = Integer.parseInt(GG.conf.getString("settings.arena.minplayers"));
			int max = Integer.parseInt(GG.conf.getString("settings.arena.maxplayers"));
			
			int x = Integer.parseInt(GG.conf.getString("settings.arena.lobbylocationx"));
			int y = Integer.parseInt(GG.conf.getString("settings.arena.lobbylocationy"));
			int z = Integer.parseInt(GG.conf.getString("settings.arena.lobbylocationz"));
			
			int start = Integer.parseInt(GG.conf.getString("settings.arena.starttime"));
			int laps = Integer.parseInt(GG.conf.getString("settings.arena.finishlaps"));
			
			int x2 = Integer.parseInt(GG.conf.getString("settings.arena.gamelocationx"));
			int y2 = Integer.parseInt(GG.conf.getString("settings.arena.gamelocationy"));
			int z2 = Integer.parseInt(GG.conf.getString("settings.arena.gamelocationz"));
			
			int id1 = Integer.parseInt(GG.conf.getString("settings.arena.finishid1"));
			int id2 = Integer.parseInt(GG.conf.getString("settings.arena.finishid2"));
			int id3 = Integer.parseInt(GG.conf.getString("settings.arena.guardianid"));
			
			int sugar = Integer.parseInt(GG.conf.getString("settings.arena.sugartime"));
			int ball = Integer.parseInt(GG.conf.getString("settings.arena.snowballtime"));
			int maxhits = Integer.parseInt(GG.conf.getString("settings.arena.maxhits"));
			
			int lapcoins = Integer.parseInt(GG.conf.getString("settings.arena.lapcoins"));
			int hitcoins = Integer.parseInt(GG.conf.getString("settings.arena.hitcoins"));
			int killcoins = Integer.parseInt(GG.conf.getString("settings.arena.killcoins"));
			
			
			game = new GGGame(min, max, x, y, z, start, laps, x2, y2, z2, id1, id2, id3, sugar, ball, maxhits, lapcoins, hitcoins, killcoins);
			loadFiles();
			game.resetGspawns();
			
		} catch (Exception e) {
			log.warning("[" + plugin.getName() + "] Settings are invalid in config.yml! Could not load the values.");
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	static void loadFiles() {
		try {
			game.setGspawns((HashMap<String, Integer>) new ObjectInputStream(new FileInputStream(plugin.getDataFolder() + File.separator + "gspawns.ser")).readObject());
			game.setCoins((HashMap<String, Integer>) new ObjectInputStream(new FileInputStream(plugin.getDataFolder() + File.separator + "coins.ser")).readObject());
			new ObjectInputStream(new FileInputStream(plugin.getDataFolder() + File.separator + "gspawns.ser")).close();
			new ObjectInputStream(new FileInputStream(plugin.getDataFolder() + File.separator + "coins.ser")).close();
		} catch (Exception e) {
			log.warning("[" + plugin.getName() + "] Files could not be read! All files are now ignored.");
		}
		
	}
	
	public void onDisable() {
		
		game.getThread().end = true;
		saveConfig();
		log.info(getName() + " has been disabled.");
	}
	public static void saveLocations() {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(plugin.getDataFolder() + File.separator + "gspawns.ser"));
			oos.writeObject(game.getGspawns());
			oos.close();
		} catch (Exception e) {
			log.warning("[" + plugin.getName() + "] Locations could not be saved!");
			e.printStackTrace();
		}
	}
	public static void saveCoins() {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(plugin.getDataFolder() + File.separator + "coins.ser"));
			oos.writeObject(game.getCoins());
			oos.close();
		} catch (Exception e) {
			log.warning("[" + plugin.getName() + "] Coins could not be saved!");
			e.printStackTrace();
		}
	}
	
	
}