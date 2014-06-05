package me.supermaxman.gg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;



public class GuardletGuardians extends JavaPlugin {
    public static FileConfiguration conf;
	public static GuardletGuardians plugin;
	public static final Logger log = Logger.getLogger("Minecraft");
	public static GGGame game;
	
	
	public void onEnable() {
		plugin = this;
		
		saveDefaultConfig();
        getCommand("gg").setExecutor(new GGExecutor(this));    
        
		getServer().getPluginManager().registerEvents(new GGListener(), plugin);
		
		log.info(getName() + " has been enabled.");
		startGame();
		
	}
	
	

	static void startGame() {
		try {
			plugin.reloadConfig();
			conf = plugin.getConfig();
			int min = Integer.parseInt(GuardletGuardians.conf.getString("settings.arena.minplayers"));
			int max = Integer.parseInt(GuardletGuardians.conf.getString("settings.arena.maxplayers"));
			
			int x = Integer.parseInt(GuardletGuardians.conf.getString("settings.arena.lobbylocationx"));
			int y = Integer.parseInt(GuardletGuardians.conf.getString("settings.arena.lobbylocationy"));
			int z = Integer.parseInt(GuardletGuardians.conf.getString("settings.arena.lobbylocationz"));
			
			int start = Integer.parseInt(GuardletGuardians.conf.getString("settings.arena.starttime"));
			int laps = Integer.parseInt(GuardletGuardians.conf.getString("settings.arena.finishlaps"));
			
			int x2 = Integer.parseInt(GuardletGuardians.conf.getString("settings.arena.gamelocationx"));
			int y2 = Integer.parseInt(GuardletGuardians.conf.getString("settings.arena.gamelocationy"));
			int z2 = Integer.parseInt(GuardletGuardians.conf.getString("settings.arena.gamelocationz"));
			
			int id1 = Integer.parseInt(GuardletGuardians.conf.getString("settings.arena.finishid1"));
			int id2 = Integer.parseInt(GuardletGuardians.conf.getString("settings.arena.finishid2"));
			int id3 = Integer.parseInt(GuardletGuardians.conf.getString("settings.arena.guardianid"));
			
			int sugar = Integer.parseInt(GuardletGuardians.conf.getString("settings.arena.sugartime"));
			int ball = Integer.parseInt(GuardletGuardians.conf.getString("settings.arena.snowballtime"));
			int maxhits = Integer.parseInt(GuardletGuardians.conf.getString("settings.arena.maxhits"));
			
			game = new GGGame(min, max, x, y, z, start, laps, x2, y2, z2, id1, id2, id3, sugar, ball, maxhits);
			loadFiles();
			
		} catch (Exception e) {
			log.warning("[" + plugin.getName() + "] Settings are invalid in config.yml! Could not load the values.");
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	static void loadFiles() {
		try {
			game.setGspawns((HashMap<String, Integer>) new ObjectInputStream(new FileInputStream(plugin.getDataFolder() + File.separator + "gspawns.ser")).readObject());
			new ObjectInputStream(new FileInputStream(plugin.getDataFolder() + File.separator + "gspawns.ser")).close();
			
		} catch (Exception e) {
			log.warning("[" + plugin.getName() + "] Files could not be read! All files are now ignored.");
		}
		
	}
	
	public void onDisable() {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(getDataFolder() + File.separator + "gspawns.ser"));
			oos.writeObject(game.getGspawns());
			oos.close();
		} catch (Exception e) {
			log.warning("[" + getName() + "] Files could not be saved!");
			e.printStackTrace();
		}
		
		game.getThread().end = true;
		saveConfig();
		log.info(getName() + " has been disabled.");
	}
	
}