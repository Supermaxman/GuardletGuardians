package me.supermaxman.gg;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class GGExecutor extends BaseExecutor {
    @Override
    protected void run(Player player, String[] args) {
        if(player.isOp()){
        	if(args.length>=1) {
        		String s = args[0];
        		if(s.equalsIgnoreCase("setlobby")) {
        			int x = player.getLocation().getBlockX();
        			int y = player.getLocation().getBlockY();
        			int z = player.getLocation().getBlockZ();
        			GuardletGuardians.conf.set("settings.arena.lobbylocationx", x);
        			GuardletGuardians.conf.set("settings.arena.lobbylocationy", y);
        			GuardletGuardians.conf.set("settings.arena.lobbylocationz", z);
        			GuardletGuardians.game.setLobyLocationX(x);
        			GuardletGuardians.game.setLobyLocationX(y);
        			GuardletGuardians.game.setLobyLocationX(z);
        			GuardletGuardians.plugin.saveConfig();
                	player.sendMessage(ChatColor.AQUA+"[GG]: Lobby now set at current location.");
        		}else if(s.equalsIgnoreCase("endgame")) {
        			GuardletGuardians.game.getThread().endGame();
                	player.sendMessage(ChatColor.AQUA+"[GG]: Game restarted.");
        		}else if(s.equalsIgnoreCase("runnerpoint")) {
        			int x = player.getLocation().getBlockX();
        			int y = player.getLocation().getBlockY();
        			int z = player.getLocation().getBlockZ();
        			GuardletGuardians.conf.set("settings.arena.gamelocationx", x);
        			GuardletGuardians.conf.set("settings.arena.gamelocationy", y);
        			GuardletGuardians.conf.set("settings.arena.gamelocationz", z);
        			GuardletGuardians.game.setGameLocationX(x);
        			GuardletGuardians.game.setGameLocationY(y);
        			GuardletGuardians.game.setGameLocationZ(z);
        			GuardletGuardians.plugin.saveConfig();
                	player.sendMessage(ChatColor.AQUA+"[GG]: Game now set at current location.");
        		}else if(s.equalsIgnoreCase("snowballtime")&&(args.length>=2)) {
        			try {
            			int t = Integer.parseInt(args[1]);
            			GuardletGuardians.conf.set("settings.arena.snowballtime", t);
            			GuardletGuardians.game.setSnowballTime(t);
            			GuardletGuardians.plugin.saveConfig();
                    	player.sendMessage(ChatColor.AQUA+"[GG]: Snowball timer changed to "+ ChatColor.GOLD+t+ChatColor.AQUA+".");
        			}catch(Exception e) {
                    	player.sendMessage(ChatColor.RED+"[GG]: Command used incorrectly, please only set numbers for time.");
        			}
        		}else if(s.equalsIgnoreCase("sugartime")&&(args.length>=2)) {
        			try {
            			int t = Integer.parseInt(args[1]);
            			GuardletGuardians.conf.set("settings.arena.sugartime", t);
            			GuardletGuardians.game.setSugarTime(t);
            			GuardletGuardians.plugin.saveConfig();
                    	player.sendMessage(ChatColor.AQUA+"[GG]: Sugar timer changed to "+ ChatColor.GOLD+t+ChatColor.AQUA+".");
        			}catch(Exception e) {
                    	player.sendMessage(ChatColor.RED+"[GG]: Command used incorrectly, please only set numbers for time.");
        			}
        		}else if(s.equalsIgnoreCase("starttime")&&(args.length>=2)) {
        			try {
            			int t = Integer.parseInt(args[1]);
            			GuardletGuardians.conf.set("settings.arena.starttime", t);
            			GuardletGuardians.game.setStartTime(t);
            			GuardletGuardians.plugin.saveConfig();
                    	player.sendMessage(ChatColor.AQUA+"[GG]: Start time changed to "+ ChatColor.GOLD+t+ChatColor.AQUA+".");
        			}catch(Exception e) {
                    	player.sendMessage(ChatColor.RED+"[GG]: Command used incorrectly, please only set numbers for time.");
        			}
        		}else if(s.equalsIgnoreCase("finishlaps")&&(args.length>=2)) {
        			try {
            			int t = Integer.parseInt(args[1]);
            			GuardletGuardians.conf.set("settings.arena.finishlaps", t);
            			GuardletGuardians.game.setFinishLaps(t);
            			GuardletGuardians.plugin.saveConfig();
                    	player.sendMessage(ChatColor.AQUA+"[GG]: Finish laps changed to "+ ChatColor.GOLD+t+ChatColor.AQUA+".");
        			}catch(Exception e) {
                    	player.sendMessage(ChatColor.RED+"[GG]: Command used incorrectly, please only set numbers for time.");
        			}
        		}else if(s.equalsIgnoreCase("finishid1")&&(args.length>=2)) {
        			try {
            			int t = Integer.parseInt(args[1]);
            			GuardletGuardians.conf.set("settings.arena.finishid1", t);
            			GuardletGuardians.game.setFinishId1(t);
            			GuardletGuardians.plugin.saveConfig();
                    	player.sendMessage(ChatColor.AQUA+"[GG]: Finish id 1 changed to "+ ChatColor.GOLD+t+ChatColor.AQUA+".");
        			}catch(Exception e) {
                    	player.sendMessage(ChatColor.RED+"[GG]: Command used incorrectly, please only set numbers for ids.");
        			}
        		}else if(s.equalsIgnoreCase("finishid2")&&(args.length>=2)) {
        			try {
            			int t = Integer.parseInt(args[1]);
            			GuardletGuardians.conf.set("settings.arena.finishid2", t);
            			GuardletGuardians.game.setFinishId2(t);
            			GuardletGuardians.plugin.saveConfig();
                    	player.sendMessage(ChatColor.AQUA+"[GG]: Finish id 2 changed to "+ ChatColor.GOLD+t+ChatColor.AQUA+".");
        			}catch(Exception e) {
                    	player.sendMessage(ChatColor.RED+"[GG]: Command used incorrectly, please only set numbers for ids.");
        			}
        		}else if(s.equalsIgnoreCase("guardianid")&&(args.length>=2)) {
        			try {
            			int t = Integer.parseInt(args[1]);
            			GuardletGuardians.conf.set("settings.arena.guardianid", t);
            			GuardletGuardians.game.setGuardianId(t);
            			GuardletGuardians.plugin.saveConfig();
                    	player.sendMessage(ChatColor.AQUA+"[GG]: Guardian id changed to "+ ChatColor.GOLD+t+ChatColor.AQUA+".");
        			}catch(Exception e) {
                    	player.sendMessage(ChatColor.RED+"[GG]: Command used incorrectly, please only set numbers for ids.");
        			}
        		}else if(s.equalsIgnoreCase("minplayers")&&(args.length>=2)) {
        			try {
            			int t = Integer.parseInt(args[1]);
            			GuardletGuardians.conf.set("settings.arena.minplayers", t);
            			GuardletGuardians.game.setMinPlayers(t);
            			GuardletGuardians.plugin.saveConfig();
                    	player.sendMessage(ChatColor.AQUA+"[GG]: Minimum player limit changed to "+ ChatColor.GOLD+t+ChatColor.AQUA+".");
        			}catch(Exception e) {
                    	player.sendMessage(ChatColor.RED+"[GG]: Command used incorrectly, please only set numbers for limits.");
        			}
        		}else if(s.equalsIgnoreCase("maxplayers")&&(args.length>=2)) {
        			try {
            			int t = Integer.parseInt(args[1]);
            			GuardletGuardians.conf.set("settings.arena.maxplayers", t);
            			GuardletGuardians.game.setMaxPlayers(t);
            			GuardletGuardians.plugin.saveConfig();
                    	player.sendMessage(ChatColor.AQUA+"[GG]: Maximum player limit changed to "+ ChatColor.GOLD+t+ChatColor.AQUA+".");
        			}catch(Exception e) {
                    	player.sendMessage(ChatColor.RED+"[GG]: Command used incorrectly, please only set numbers for limits.");
        			}
        		}else {
                	player.sendMessage(ChatColor.RED+"[GG]: Command used incorrectly, use /hvd setlobby,runnerpoint,endgame,starttime [time],timelimit [time],appletimer [time], minplayers [limit],maxplayers [limit].");
        		}
        	}else {
            	player.sendMessage(ChatColor.RED+"[GG]: Command used incorrectly, use /hvd setlobby,setgame,setapple,endgame,starttime [time],timelimit [time],appletimer [time], minplayers [limit],maxplayers [limit].");
        	}
        }else {
        	player.sendMessage(ChatColor.RED+"[GG]: You do not have permission to use this command.");
        }
    }

    public GGExecutor(GuardletGuardians pl) {
        super(pl);
    }
}
