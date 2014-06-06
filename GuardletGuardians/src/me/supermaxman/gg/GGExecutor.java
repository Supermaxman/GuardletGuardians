package me.supermaxman.gg;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class GGExecutor extends BaseExecutor {
    @SuppressWarnings("deprecation")
	@Override
    protected void run(Player player, String[] args) {
        if(player.isOp()){
        	if(args.length>=1) {
        		String s = args[0];
        		if(s.equalsIgnoreCase("setlobby")) {
        			int x = player.getLocation().getBlockX();
        			int y = player.getLocation().getBlockY();
        			int z = player.getLocation().getBlockZ();
        			GG.conf.set("settings.arena.lobbylocationx", x);
        			GG.conf.set("settings.arena.lobbylocationy", y);
        			GG.conf.set("settings.arena.lobbylocationz", z);
        			GG.game.setLobyLocationX(x);
        			GG.game.setLobyLocationY(y);
        			GG.game.setLobyLocationZ(z);
        			GG.plugin.saveConfig();
                	player.sendMessage(ChatColor.AQUA+"[GG]: Lobby now set at current location.");
        		}else if(s.equalsIgnoreCase("endgame")) {
        			GG.game.getThread().endGame();
                	player.sendMessage(ChatColor.AQUA+"[GG]: Game restarted.");
        		}else if(s.equalsIgnoreCase("runnerpoint")) {
        			int x = player.getLocation().getBlockX();
        			int y = player.getLocation().getBlockY();
        			int z = player.getLocation().getBlockZ();
        			GG.conf.set("settings.arena.gamelocationx", x);
        			GG.conf.set("settings.arena.gamelocationy", y);
        			GG.conf.set("settings.arena.gamelocationz", z);
        			GG.game.setGameLocationX(x);
        			GG.game.setGameLocationY(y);
        			GG.game.setGameLocationZ(z);
        			GG.plugin.saveConfig();
                	player.sendMessage(ChatColor.AQUA+"[GG]: Game now set at current location.");
        		}else if(s.equalsIgnoreCase("snowballtime")&&(args.length>=2)) {
        			try {
            			int t = Integer.parseInt(args[1]);
            			GG.conf.set("settings.arena.snowballtime", t);
            			GG.game.setSnowballTime(t);
            			GG.plugin.saveConfig();
                    	player.sendMessage(ChatColor.AQUA+"[GG]: Snowball timer changed to "+ ChatColor.GOLD+t+ChatColor.AQUA+".");
        			}catch(Exception e) {
                    	player.sendMessage(ChatColor.RED+"[GG]: Command used incorrectly, please only set numbers for time.");
        			}
        		}else if(s.equalsIgnoreCase("sugartime")&&(args.length>=2)) {
        			try {
            			int t = Integer.parseInt(args[1]);
            			GG.conf.set("settings.arena.sugartime", t);
            			GG.game.setSugarTime(t);
            			GG.plugin.saveConfig();
                    	player.sendMessage(ChatColor.AQUA+"[GG]: Sugar timer changed to "+ ChatColor.GOLD+t+ChatColor.AQUA+".");
        			}catch(Exception e) {
                    	player.sendMessage(ChatColor.RED+"[GG]: Command used incorrectly, please only set numbers for time.");
        			}
        		}else if(s.equalsIgnoreCase("starttime")&&(args.length>=2)) {
        			try {
            			int t = Integer.parseInt(args[1]);
            			GG.conf.set("settings.arena.starttime", t);
            			GG.game.setStartTime(t);
            			GG.plugin.saveConfig();
                    	player.sendMessage(ChatColor.AQUA+"[GG]: Start time changed to "+ ChatColor.GOLD+t+ChatColor.AQUA+".");
        			}catch(Exception e) {
                    	player.sendMessage(ChatColor.RED+"[GG]: Command used incorrectly, please only set numbers for time.");
        			}
        		}else if(s.equalsIgnoreCase("finishlaps")&&(args.length>=2)) {
        			try {
            			int t = Integer.parseInt(args[1]);
            			GG.conf.set("settings.arena.finishlaps", t);
            			GG.game.setFinishLaps(t);
            			GG.plugin.saveConfig();
                    	player.sendMessage(ChatColor.AQUA+"[GG]: Finish laps changed to "+ ChatColor.GOLD+t+ChatColor.AQUA+".");
        			}catch(Exception e) {
                    	player.sendMessage(ChatColor.RED+"[GG]: Command used incorrectly, please only set numbers for time.");
        			}
        		}else if(s.equalsIgnoreCase("finishid1")&&(args.length>=2)) {
        			try {
            			int t = Integer.parseInt(args[1]);
            			GG.conf.set("settings.arena.finishid1", t);
            			GG.game.setFinishId1(t);
            			GG.plugin.saveConfig();
                    	player.sendMessage(ChatColor.AQUA+"[GG]: Finish id 1 changed to "+ ChatColor.GOLD+t+ChatColor.AQUA+".");
        			}catch(Exception e) {
                    	player.sendMessage(ChatColor.RED+"[GG]: Command used incorrectly, please only set numbers for ids.");
        			}
        		}else if(s.equalsIgnoreCase("finishid2")&&(args.length>=2)) {
        			try {
            			int t = Integer.parseInt(args[1]);
            			GG.conf.set("settings.arena.finishid2", t);
            			GG.game.setFinishId2(t);
            			GG.plugin.saveConfig();
                    	player.sendMessage(ChatColor.AQUA+"[GG]: Finish id 2 changed to "+ ChatColor.GOLD+t+ChatColor.AQUA+".");
        			}catch(Exception e) {
                    	player.sendMessage(ChatColor.RED+"[GG]: Command used incorrectly, please only set numbers for ids.");
        			}
        		}else if(s.equalsIgnoreCase("guardianid")&&(args.length>=2)) {
        			try {
            			int t = Integer.parseInt(args[1]);
            			GG.conf.set("settings.arena.guardianid", t);
            			GG.game.setGuardianId(t);
            			GG.plugin.saveConfig();
                    	player.sendMessage(ChatColor.AQUA+"[GG]: Guardian id changed to "+ ChatColor.GOLD+t+ChatColor.AQUA+".");
        			}catch(Exception e) {
                    	player.sendMessage(ChatColor.RED+"[GG]: Command used incorrectly, please only set numbers for ids.");
        			}
        		}else if(s.equalsIgnoreCase("minplayers")&&(args.length>=2)) {
        			try {
            			int t = Integer.parseInt(args[1]);
            			GG.conf.set("settings.arena.minplayers", t);
            			GG.game.setMinPlayers(t);
            			GG.plugin.saveConfig();
                    	player.sendMessage(ChatColor.AQUA+"[GG]: Minimum player limit changed to "+ ChatColor.GOLD+t+ChatColor.AQUA+".");
        			}catch(Exception e) {
                    	player.sendMessage(ChatColor.RED+"[GG]: Command used incorrectly, please only set numbers for limits.");
        			}
        		}else if(s.equalsIgnoreCase("maxplayers")&&(args.length>=2)) {
        			try {
            			int t = Integer.parseInt(args[1]);
            			GG.conf.set("settings.arena.maxplayers", t);
            			GG.game.setMaxPlayers(t);
            			GG.plugin.saveConfig();
                    	player.sendMessage(ChatColor.AQUA+"[GG]: Maximum player limit changed to "+ ChatColor.GOLD+t+ChatColor.AQUA+".");
        			}catch(Exception e) {
                    	player.sendMessage(ChatColor.RED+"[GG]: Command used incorrectly, please only set numbers for limits.");
        			}
        		}else if(s.equalsIgnoreCase("guardian")&&(args.length>=2)) {
        			try {
            			GG.game.addGuardian(GG.plugin.getServer().getPlayer(args[1]));
                    	player.sendMessage(ChatColor.AQUA+"[GG]: First Guardian changed this round to "+ ChatColor.GOLD+args[1]+ChatColor.AQUA+".");
        			}catch(Exception e) {
                    	player.sendMessage(ChatColor.RED+"[GG]: Could not find player.");
        			}
        		}else if(s.equalsIgnoreCase("addcoins")&&(args.length>=2)) {
        			try {
            			int t = Integer.parseInt(args[1]);
            			GG.game.addPlayerCoins(player.getName(), t);
                    	player.sendMessage(ChatColor.AQUA+"[GG]: "+ ChatColor.GOLD+t+ChatColor.AQUA+" coins added.");
        			}catch(Exception e) {
                    	player.sendMessage(ChatColor.RED+"[GG]: Command used incorrectly, please only set numbers for coins.");
        			}
        		}else {
                	player.sendMessage(ChatColor.RED+"[GG]: Command used incorrectly, use /gg setlobby, runnerpoint, endgame, starttime [time], finishlaps [lap number], sugartime [time], snowballtime [time], minplayers [limit], maxplayers [limit], finishid1 [block id], finishid2 [block id], guardianid [block id], guardian [username], addcoins [number]");
        		}
        	}else {
            	player.sendMessage(ChatColor.RED+"[GG]: Command used incorrectly, use /gg setlobby, runnerpoint, endgame, starttime [time], finishlaps [lap number], sugartime [time], snowballtime [time], minplayers [limit], maxplayers [limit], finishid1 [block id], finishid2 [block id], guardianid [block id], guardian [username], addcoins [number]");
        	}
        }else {
        	player.sendMessage(ChatColor.RED+"[GG]: You do not have permission to use this command.");
        }
    }

    public GGExecutor(GG pl) {
        super(pl);
    }
}
