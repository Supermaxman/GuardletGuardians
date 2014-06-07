package me.supermaxman.gg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GGGame{
	
	private int minPlayers;
	private int maxPlayers;
    private int lobyLocationX;
    private int lobyLocationY;
    private int lobyLocationZ;

    private int startTime;
    private int finishLaps;
    
    private int gameLocationX;
    private int gameLocationY;
    private int gameLocationZ;
    
    private int finishId1;
    private int finishId2;
    private int guardianId;
    private int sugarTime;
    private int snowballTime;
    private int maxHits;
    private int lapcoins;
    private int hitcoins;
    private int killcoins;
    private boolean ended;
    private GGGameThread thread;
    private ArrayList<String> players = new ArrayList<String>();
	private HashMap<String, Integer> runners = new HashMap<String, Integer>();
	private HashMap<String, Integer> runnerhits = new HashMap<String, Integer>();
	
	private HashMap<String, Integer> guardians = new HashMap<String, Integer>();
	private HashMap<String, Long> cooldowns = new HashMap<String, Long>();
	private HashMap<String, Integer> gspawns = new HashMap<String, Integer>();
	
	private HashMap<String, ArrayList<ItemStack>> ritems = new HashMap<String, ArrayList<ItemStack>>();
	private HashMap<String, ArrayList<ItemStack>> gitems = new HashMap<String, ArrayList<ItemStack>>();
	
	private HashMap<Zombie, Long> minions = new HashMap<Zombie, Long>();
	
	private HashMap<String, Integer> coins = new HashMap<String, Integer>();
	
    public GGGame(int min, int max, int x, int y, int z, int start, int laps, int x2, int y2, int z2, int id1, int id2, int id3, int stime, int btime, int maxhits, int lapcoins, int hitcoins, int killcoins) {
    	setMinPlayers(min);
        setMaxPlayers(max);
        setLobyLocationX(x);
        setLobyLocationY(y);
        setLobyLocationZ(z);
        setStartTime(start);
        setFinishLaps(laps);
        setGameLocationX(x2);
        setGameLocationY(y2);
        setGameLocationZ(z2);
        setFinishId1(id1);
        setFinishId2(id2);
        setGuardianId(id3);
        setSugarTime(stime);
        setSnowballTime(btime);
        setMaxHits(maxhits);
        setLapcoins(lapcoins);
        setHitcoins(hitcoins);
        setKillcoins(killcoins);
        setRunners(new HashMap<String, Integer>());
        runnerhits = new HashMap<String, Integer>();
        setGuardians(new HashMap<String, Integer>());
        setCooldowns(new HashMap<String, Long>());
        setPlayers(new ArrayList<String>());
        thread = new GGGameThread(GG.plugin, this);
        setEnded(false);
        thread.start();
    }
    
    public void givePurchases(Player p) {
    	for(ItemStack i :  givePurchases(p.getName())) {
    		p.getInventory().addItem(i);
    	}
    }
    public ArrayList<ItemStack> givePurchases(String s) {
    	if(isRunner(s)) {
    		if(!ritems.containsKey(s)) {
    			ritems.put(s, new ArrayList<ItemStack>());
    		}
    		return ritems.get(s);
    	}else if(isGuardian(s)) {
    		if(!gitems.containsKey(s)) {
    			gitems.put(s, new ArrayList<ItemStack>());
    		}
    		return gitems.get(s);
    	}else {
    		return new ArrayList<ItemStack>();
    	}
    }
    
    public HashMap<String, Integer> getCoins() {
		return coins;
	}

	public void setCoins(HashMap<String, Integer> coins) {
		this.coins = coins;
	}
	
    public Integer getPlayerCoins(String s) {
		if(!coins.containsKey(s)) {
			coins.put(s, 0);
		}
		return coins.get(s);
	}
    
	public void setPlayerCoins(String s, Integer i) {
		coins.put(s, i);
	}
	public void addPlayerCoins(String s, Integer i) {
		if(coins.containsKey(s)) {
			coins.put(s, coins.get(s)+i);
		}else {
			coins.put(s, i);
		}
	}
	public void removePlayerCoins(String s, Integer i) {
		coins.put(s, coins.get(s)-i);
	}
	public boolean hasEnoughCoins(String s, Integer i) {
		if(coins.containsKey(s)) {
			if(coins.get(s)>=i) {
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
	}
	
	
	public String getFirstGuardian() {
    	return (String) guardians.keySet().toArray()[0];
    }
    
    public void addGuardian(Player p) {
    	addGuardian(p.getName());
    }
    
    @SuppressWarnings("deprecation")
	public void addGuardian(String s) {
    	runners.remove(s);
    	runnerhits.remove(s);
    	cooldowns.remove(s);
    	guardians.put(s, 0);
    	Player p = GG.plugin.getServer().getPlayerExact(s);
    	if(p!=null) {
        	p.getInventory().clear();
    		ItemStack i = new ItemStack(Material.SNOW_BALL);
    		i.setAmount(64);
    		p.setItemInHand(i);
        	givePurchases(p);
    	}
    }
    
    public void addRunner(Player p) {
    	addRunner(p.getName());
    	givePurchases(p);
    }
    @SuppressWarnings("deprecation")
	public void addRunner(String s) {
    	runners.put(s, 0);
    	runnerhits.put(s, 0);
    	Player p = GG.plugin.getServer().getPlayerExact(s);
    	if(p!=null) {
        	p.getInventory().clear();
			ItemStack i = new ItemStack(Material.SUGAR);
			ItemMeta m = i.getItemMeta();
			m.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Sprint");
			ArrayList<String> l = new ArrayList<String>();
			l.add(ChatColor.AQUA + "Right click to sprint");
			l.add(ChatColor.AQUA +  "every 15 seconds");
			m.setLore(l);
			i.setItemMeta(m);
			p.getInventory().addItem(i);
        	givePurchases(p);
    	}
    }
    
	public void addRunners() {
		for(String p : players) {
			if(!isGuardian(p)) {
				addRunner(p);
			}
		}
	}
    
    
    public int getHits(Player p) {
    	return getHits(p.getName());
    }
    
    
    public void addGitem(String s, ItemStack i) {
    	if(!gitems.containsKey(s)) {
    		gitems.put(s, new ArrayList<ItemStack>());
    	}
    	gitems.get(s).add(i);
    }
    
    public ArrayList<ItemStack> getGitem(String s) {
    	if(!gitems.containsKey(s)) {
    		gitems.put(s, new ArrayList<ItemStack>());
    	}
    	return gitems.get(s);
    }
    
    public void addRitem(String s, ItemStack i) {
    	if(!ritems.containsKey(s)) {
    		ritems.put(s, new ArrayList<ItemStack>());
    	}
    	ritems.get(s).add(i);
    }
    
    public ArrayList<ItemStack> getRitem(String s) {
    	if(!ritems.containsKey(s)) {
    		ritems.put(s, new ArrayList<ItemStack>());
    	}
    	return ritems.get(s);
    }
    
    public int getHits(String s) {
    	if(isGuardian(s)) {
        	return guardians.get(s);
    	}else {
        	return runnerhits.get(s);
    	}
    }
    
    public int getLaps(Player p) {
    	return getLaps(p.getName());
    }
    public int getLaps(String s) {
    	return runners.get(s);
    }
    
    public void setHits(Player p, int i) {
    	setHits(p.getName(), i);
    }
    public void setHits(String s, int i) {
    	if(isGuardian(s)) {
        	guardians.put(s, i);
    	}else {
        	runnerhits.put(s, i);
    	}
    }
    
    public void setLaps(Player p, int i) {
    	setLaps(p.getName(), i);
    }
    public void setLaps(String s, int i) {
    	runners.put(s, i);
    }
    
    public void addLap(Player p) {
    	addLap(p.getName());
    }
    public void addLap(String s) {
    	runners.put(s, runners.get(s)+1);
    	addPlayerCoins(s, getLapcoins());
    }
    
    public void addHit(Player p) {
    	addHit(p.getName());
    }
    public void addHit(String s) {
    	if(isGuardian(s)) {
        	guardians.put(s, guardians.get(s)+1);
        	addPlayerCoins(s, getHitcoins());
    	}else {
        	runnerhits.put(s, runnerhits.get(s)+1);
    	}
    }
    
    public void addCooldown(Player p) {
    	addCooldown(p.getName());
    }
    
    public void addCooldown(String s) {
    	cooldowns.put(s, System.currentTimeMillis());
    }
    
    public Long getCooldown(Player p) {
    	return getCooldown(p.getName());
    }
    public Long getCooldown(String s) {
    	return cooldowns.get(s);
    }
    
    public boolean isCooldown(String s) {
    	return cooldowns.containsKey(s);
    }
    
    public boolean isCooldown(Player p) {
    	return isCooldown(p.getName());
    }
    
    public void addPlayer(Player p) {
    	addPlayer(p.getName());
    }
    public void addPlayer(String s) {
    	players.add(s);
    }
    
    public void removePlayer(Player p) {
    	removePlayer(p.getName());
    }
    public void removePlayer(String s) {
    	players.remove(s);
    }
    public void removeGuardian(Player p) {
    	removeGuardian(p.getName());
    }
    public void removeGuardian(String s) {
    	guardians.remove(s);
    }
    public void removeRunner(Player p) {
    	removeRunner(p.getName());
    }
    public void removeRunner(String s) {
    	runners.remove(s);
    }
    public void addGspawn(Location loc) {
    	gspawns.put(makeString(loc), 0);
    }
    
    public void removeGspawn(Location loc) {
    	gspawns.remove(makeString(loc));
    }
    
	public String makeString(Location loc) {
		return loc.getWorld().getName() + "&&" + loc.getBlockX() + "&&" + loc.getBlockY() + "&&" + loc.getBlockZ(); 
	}
	
	public static Location makeLocation(String s) {
		String[] loc = s.split("&&");
		
		return new Location(GG.plugin.getServer().getWorld(loc[0]), Integer.parseInt(loc[1]), Integer.parseInt(loc[2]), Integer.parseInt(loc[3])); 
	}
    
    public ArrayList<String> getPlayers(){
    	return players;
    }
    public void setPlayers(ArrayList<String> players){
    	this.players = players;
    }
    public Integer getScore(String s) {
    	if(isRunner(s)) {
    		return runners.get(s);
    	}else if(isGuardian(s)) {
    		return guardians.get(s);
    	}
    	return 0;
    }
    
    
    public boolean isGuardian(String s){
    	if(guardians.containsKey(s)) {
    		return true;
    	}
    	return false;
    }
    
    public boolean isRunner(String s){
    	if(runners.containsKey(s)) {
    		return true;
    	}
    	return false;
    }
    
    public boolean isPlayer(String s){
    	if(players.contains(s)) {
    		return true;
    	}
    	return false;
    }
    
    public boolean isGuardian(Player p){
    	return isGuardian(p.getName());
    }
    
    public boolean isRunner(Player p){
    	return isRunner(p.getName());
    }
    
    public boolean isPlayer(Player p){
    	return isPlayer(p.getName());

    }
    
	public String chooseGuardian() {
		if(guardians.size()==0) {
			Random r = new Random();
			int i = r.nextInt(players.size());
			return players.get(i);
		}else {
			return getFirstGuardian();
		}
	}
	
	public void setupGuardian(Player p) {
		Random r = new Random();
		ArrayList<Location> spawns = new ArrayList<Location>();
		
		for(String s : gspawns.keySet()) {
			if(gspawns.get(s)==0) {
				spawns.add(makeLocation(s));
			}
		}
    	
		int i = r.nextInt(spawns.size());
		Location loc = spawns.get(i);
		gspawns.put(makeString(loc), 1);
		p.teleport(loc);
	}
	
	public void resetGspawns() {
		for(String s : gspawns.keySet()) {
			gspawns.put(s, 0);
		}
	}
	
	public GGGameThread getThread() {
		return thread;
	}


	public void setThread(GGGameThread thread) {
		this.thread = thread;
	}


	public boolean isEnded() {
		return ended;
	}


	public void setEnded(boolean ended) {
		this.ended = ended;
	}

	public int getGameLocationX() {
		return gameLocationX;
	}


	public void setGameLocationX(int gameLocationX) {
		this.gameLocationX = gameLocationX;
	}


	public int getGameLocationY() {
		return gameLocationY;
	}


	public void setGameLocationY(int gameLocationY) {
		this.gameLocationY = gameLocationY;
	}


	public int getGameLocationZ() {
		return gameLocationZ;
	}


	public void setGameLocationZ(int gameLocationZ) {
		this.gameLocationZ = gameLocationZ;
	}


	public int getFinishLaps() {
		return finishLaps;
	}


	public void setFinishLaps(int finishLaps) {
		this.finishLaps = finishLaps;
	}


	public int getFinishId1() {
		return finishId1;
	}


	public void setFinishId1(int finishId1) {
		this.finishId1 = finishId1;
	}


	public int getFinishId2() {
		return finishId2;
	}


	public void setFinishId2(int finishId2) {
		this.finishId2 = finishId2;
	}


	public int getGuardianId() {
		return guardianId;
	}


	public void setGuardianId(int guardianId) {
		this.guardianId = guardianId;
	}


	public int getSugarTime() {
		return sugarTime;
	}


	public void setSugarTime(int sugarTime) {
		this.sugarTime = sugarTime;
	}


	public int getSnowballTime() {
		return snowballTime;
	}


	public void setSnowballTime(int snowballTime) {
		this.snowballTime = snowballTime;
	}


	public int getMinPlayers() {
		return minPlayers;
	}


	public void setMinPlayers(int minPlayers) {
		this.minPlayers = minPlayers;
	}


	public int getMaxPlayers() {
		return maxPlayers;
	}


	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}


	public int getLobyLocationX() {
		return lobyLocationX;
	}


	public void setLobyLocationX(int lobyLocationX) {
		this.lobyLocationX = lobyLocationX;
	}


	public int getLobyLocationY() {
		return lobyLocationY;
	}


	public void setLobyLocationY(int lobyLocationY) {
		this.lobyLocationY = lobyLocationY;
	}


	public int getLobyLocationZ() {
		return lobyLocationZ;
	}


	public void setLobyLocationZ(int lobyLocationZ) {
		this.lobyLocationZ = lobyLocationZ;
	}


	public int getStartTime() {
		return startTime;
	}


	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}


	public HashMap<String, Integer> getRunners() {
		return runners;
	}


	public void setRunners(HashMap<String, Integer> runners) {
		this.runners = runners;
	}


	public HashMap<String, Integer> getGuardians() {
		return guardians;
	}


	public void setGuardians(HashMap<String, Integer> guardians) {
		this.guardians = guardians;
	}


	public HashMap<String, Long> getCooldowns() {
		return cooldowns;
	}


	public void setCooldowns(HashMap<String, Long> cooldowns) {
		this.cooldowns = cooldowns;
	}
	
    
	public ArrayList<Location> getGspawnsLocations() {
		ArrayList<Location> loc = new ArrayList<Location>();
		for(String s : gspawns.keySet()) {
			loc.add(makeLocation(s));
		}
		return loc;
	}
	public Location getGspawnsLocation(String s) {
		return makeLocation(s);
	}


	public HashMap<String, Integer> getGspawns() {
		return gspawns;
	}


	public void setGspawns(HashMap<String, Integer> gspawns) {
		this.gspawns = gspawns;
	}

	public int getMaxHits() {
		return maxHits;
	}

	public void setMaxHits(int maxHits) {
		this.maxHits = maxHits;
	}

	public HashMap<String, ArrayList<ItemStack>> getGitems() {
		return gitems;
	}

	public void setGitems(HashMap<String, ArrayList<ItemStack>> gitems) {
		this.gitems = gitems;
	}

	public HashMap<String, ArrayList<ItemStack>> getRitems() {
		return ritems;
	}

	public void setRitems(HashMap<String, ArrayList<ItemStack>> ritems) {
		this.ritems = ritems;
	}

	public int getLapcoins() {
		return lapcoins;
	}

	public void setLapcoins(int lapcoins) {
		this.lapcoins = lapcoins;
	}

	public int getHitcoins() {
		return hitcoins;
	}

	public void setHitcoins(int hitcoins) {
		this.hitcoins = hitcoins;
	}

	public int getKillcoins() {
		return killcoins;
	}

	public void setKillcoins(int killcoins) {
		this.killcoins = killcoins;
	}

	public HashMap<Zombie, Long> getMinions() {
		return minions;
	}

	public void setMinions(HashMap<Zombie, Long> minions) {
		this.minions = minions;
	}
	
	public Long getMinion(Zombie z) {
		return minions.get(z);
	}
	
	public void setMinion(Zombie z, Long l) {
		minions.put(z, l);
	}
}
