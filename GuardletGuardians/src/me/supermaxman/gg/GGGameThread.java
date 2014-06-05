package me.supermaxman.gg;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

public class GGGameThread extends Thread {

private final GGGame game;
private final GuardletGuardians plugin;
public boolean end;

public GGGameThread(GuardletGuardians pl, GGGame g){
    setName("GuardletGuardians-Thread-"+getId());
    game = g;
    plugin = pl;
    end = false;
}
	synchronized public void run() {
    	try {
    	if(end)this.interrupt();
		game.setEnded(true);
        int start = game.getStartTime();
        this.wait(5000);
    	if(end)this.interrupt();
        while(game.getPlayers().size()<game.getMinPlayers()) {
            int i = game.getMinPlayers()-game.getPlayers().size();
            plugin.getServer().broadcastMessage(ChatColor.AQUA+"[GG]: Game requires " +ChatColor.GOLD+i+ChatColor.AQUA+" more players to begin!");
            this.wait(10*1000);
        	if(end)this.interrupt();
        }
        plugin.getServer().broadcastMessage(ChatColor.AQUA+"[GG]: Game beginning in " +ChatColor.GOLD+start+ChatColor.AQUA+" seconds!");
     	start = start/2;
        this.wait(start*1000);
    	if(end)this.interrupt();
        plugin.getServer().broadcastMessage(ChatColor.AQUA+"[GG]: Game beginning in " +ChatColor.GOLD+start+ChatColor.AQUA+" seconds!");
     	start = start/2;
     	this.wait(start*1000);
    	if(end)this.interrupt();
        plugin.getServer().broadcastMessage(ChatColor.AQUA+"[GG]: Game beginning in " +ChatColor.GOLD+start+ChatColor.AQUA+" seconds!");
     	this.wait(start*1000);
    	if(end)this.interrupt();
        plugin.getServer().broadcastMessage(ChatColor.AQUA+"[GG]: "+ChatColor.GOLD+"Game Start"+ChatColor.AQUA+"!");
        
        game.addGuardian(game.chooseGuardian());
        game.addRunners();
        startGame();
        plugin.getServer().broadcastMessage(ChatColor.AQUA+"[GG]: " +ChatColor.RED+game.getFirstGuardian()+ChatColor.AQUA+" is the first Guardian! Start running!");
    	if(end)this.interrupt();
        game.setEnded(false);
        equipRunners();
        equipGuardian(game.getFirstGuardian());
        
        //start
        
     	while(!game.isEnded()) {
         	this.wait(100);
        	if(end)this.interrupt();
            for(String s : game.getRunners().keySet()) {
            	if(game.getLaps(s)>=game.getFinishLaps()) {
            		loseGame(s);
            		return;
            	}
            }
            if(game.getRunners().size()==0) {
        		winGame();
        		return;
            }
     	}
     	
     	
        
        endGame();
		} catch (InterruptedException e) {
			GuardletGuardians.log.warning("[" + plugin.getName() + "] Game interupted by server.");
		}
    	
        this.interrupt();   
    } 
    
	synchronized void winGame() {
        plugin.getServer().broadcastMessage(ChatColor.AQUA+"[GG]: "+ChatColor.GOLD+"Game End"+ChatColor.AQUA+"!");
        plugin.getServer().broadcastMessage(ChatColor.AQUA+"[GG]: "+ChatColor.GOLD+"Guardians"+ChatColor.AQUA+" have won!");
        int i = 0;
        String winner = "";
        for(String s : game.getGuardians().keySet()) {
        	if(game.getHits(s)>i) {
        		i = game.getHits(s);
        		winner = s;
        	}
        }
        plugin.getServer().broadcastMessage(ChatColor.AQUA+"[GG]: "+ChatColor.GOLD+winner+ChatColor.AQUA+" took out the most runners with "+ChatColor.GOLD+game.getHits(winner)+ChatColor.AQUA+" hits!");

        endGame();
	}
	synchronized void endGame() {
		game.setEnded(true);
        for(Player p : plugin.getServer().getOnlinePlayers()) {
        	p.getInventory().clear();
        	ItemStack[] is = new ItemStack[4];
        	p.getInventory().setArmorContents(is);
            for (PotionEffect effect : p.getActivePotionEffects()) {
                p.removePotionEffect(effect.getType());
            }
        	p.teleport(new Location(p.getWorld(), game.getLobyLocationX(), game.getLobyLocationY(), game.getLobyLocationZ()));
        }
        GuardletGuardians.startGame();
        this.interrupt();   
	}
	synchronized void startGame() {
        for(Player p : plugin.getServer().getOnlinePlayers()) {
        	if(game.isRunner(p)) {
        		p.teleport(new Location(p.getWorld(), game.getGameLocationX(),game.getGameLocationY(),game.getGameLocationZ()));
        	}else if(game.isGuardian(p)) {
        		game.setupGuardian(p);
        	}
        }
	}
	synchronized void loseGame(String p) {
        plugin.getServer().broadcastMessage(ChatColor.AQUA+"[GG]: "+ChatColor.GOLD+"Game End"+ChatColor.AQUA+"!");
        plugin.getServer().broadcastMessage(ChatColor.AQUA+"[GG]: "+ChatColor.GOLD+"Runners"+ChatColor.AQUA+" have won!");
        plugin.getServer().broadcastMessage(ChatColor.AQUA+"[GG]: "+ChatColor.GOLD+p+ChatColor.AQUA+" finished first!");
        endGame();
	}
	@SuppressWarnings("deprecation")
	synchronized void equipGuardian(String s) {
		Player p = plugin.getServer().getPlayer(s);
		ItemStack i = new ItemStack(Material.SNOW_BALL);
		i.setAmount(64);
		p.setItemInHand(i);
		
	}
	
	@SuppressWarnings("deprecation")
	synchronized void equipRunners() {
		for(String s : game.getPlayers()) {
			Player p = GuardletGuardians.plugin.getServer().getPlayer(s);
			
			if(p!=null) {				
				if(game.isRunner(p)){
					ItemStack i = new ItemStack(Material.SUGAR);
					ItemMeta m = i.getItemMeta();
					m.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Sprint");
					ArrayList<String> l = new ArrayList<String>();
					l.add(ChatColor.AQUA + "Right click to sprint");
					l.add(ChatColor.AQUA +  "every 15 seconds");
					m.setLore(l);
					i.setItemMeta(m);
					p.getInventory().addItem(i);
				}
				
			}
		}
	}
	
	
}