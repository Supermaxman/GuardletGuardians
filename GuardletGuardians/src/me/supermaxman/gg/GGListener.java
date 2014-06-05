package me.supermaxman.gg;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

	
public class GGListener implements Listener {
	
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		e.setRespawnLocation(new Location(p.getWorld(), GG.game.getLobyLocationX(), GG.game.getLobyLocationY(), GG.game.getLobyLocationZ()));
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if(e.getPlayer().isOp()) {
			if(e.getBlock().getTypeId() == GG.game.getGuardianId()) {
				GG.game.removeGspawn(e.getBlock().getRelative(BlockFace.UP).getLocation());
				e.getPlayer().sendMessage(ChatColor.AQUA+"[GG]: New Guardian Spawn removed.");
			}
		}else {
			e.setCancelled(true);
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if(e.getPlayer().isOp()) {
			if(e.getBlock().getTypeId() == GG.game.getGuardianId()) {
				GG.game.addGspawn(e.getBlock().getRelative(BlockFace.UP).getLocation());
				e.getPlayer().sendMessage(ChatColor.AQUA+"[GG]: New Guardian Spawn added.");
			}
		}else {
			e.setCancelled(true);
		}
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		//Stop Guardian movement
		Player p = e.getPlayer();
		if(GG.game.isRunner(p)) {
			Block b = p.getLocation().getBlock().getRelative(BlockFace.DOWN);
			if(b.getTypeId()==GG.game.getFinishId1() && (GG.game.getLaps(p) % 2==0)) {
				GG.game.addLap(p);
				if(GG.game.getLaps(p)<GG.game.getFinishLaps()) {
					if(GG.game.getLaps(p) == GG.game.getFinishLaps()-1) {
				        p.getServer().broadcastMessage(ChatColor.AQUA+"[GG]: "+ChatColor.GOLD+p.getName()+ChatColor.AQUA+" is on his "+ChatColor.GOLD+"final "+ChatColor.AQUA+"lap!");
					}else {
				        p.getServer().broadcastMessage(ChatColor.AQUA+"[GG]: "+ChatColor.GOLD+p.getName()+ChatColor.AQUA+" is on lap "+ChatColor.GOLD+(GG.game.getLaps(p)+1)+ChatColor.AQUA+"!");
					}
				}
			}else if(b.getTypeId()==GG.game.getFinishId2() && (GG.game.getLaps(p) % 2!=0)) {
				GG.game.addLap(p);
				if(GG.game.getLaps(p)<GG.game.getFinishLaps()) {
					if(GG.game.getLaps(p) == GG.game.getFinishLaps()-1) {
				        p.getServer().broadcastMessage(ChatColor.AQUA+"[GG]: "+ChatColor.GOLD+p.getName()+ChatColor.AQUA+" is on his "+ChatColor.GOLD+"final "+ChatColor.AQUA+"lap!");
					}else {
				        p.getServer().broadcastMessage(ChatColor.AQUA+"[GG]: "+ChatColor.GOLD+p.getName()+ChatColor.AQUA+" is on lap "+ChatColor.GOLD+(GG.game.getLaps(p)+1)+ChatColor.AQUA+"!");
					}
				}
			}
		}else if(GG.game.isGuardian(p)) {
			Location loc = e.getFrom().setDirection(e.getTo().getDirection());
			e.setTo(loc);
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (e.getClickedBlock().getState() instanceof Sign) {
				Sign sign = (Sign) e.getClickedBlock().getState();
				if(sign.getLine(0).equalsIgnoreCase("GG")&&sign.getLine(1).equalsIgnoreCase("join")) {
					if(GG.game.isEnded()) {
						if(GG.game.getPlayers().size()<GG.game.getMaxPlayers()) {
							if(!GG.game.isPlayer(e.getPlayer())) {
								GG.game.addPlayer(e.getPlayer());
					            int i = GG.game.getMinPlayers()-GG.game.getPlayers().size();
								e.getPlayer().sendMessage(ChatColor.AQUA+"[GG]: Joined game, needs "+ChatColor.GOLD+i+ChatColor.AQUA+" more players!");
							}else {
								e.getPlayer().sendMessage(ChatColor.RED+"[GG]: You have already joined!");
							}
						}else {
							e.getPlayer().sendMessage(ChatColor.RED+"[GG]: Cannot join, game is full!");
						}
					}else {
						e.getPlayer().sendMessage(ChatColor.RED+"[GG]: Cannot join, game is active!");
					}
				}
			}
		}
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Player p = e.getPlayer();
			if(p.getItemInHand()!=null) {
				ItemStack i = p.getItemInHand();
				if(i.hasItemMeta()) {
					if(i.getItemMeta().hasDisplayName()) {
						if(i.getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "" + ChatColor.BOLD + "Sprint")) {
							if(GG.game.isCooldown(p)) {
								if(GG.game.getCooldown(p)+(GG.game.getSugarTime()*1000) < System.currentTimeMillis()) {
									GG.game.addCooldown(p);
									p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, GG.game.getSugarTime()*20, 1, true));
									e.getPlayer().sendMessage(ChatColor.AQUA+"[GG]: Sprinting!");
								}else {
									e.getPlayer().sendMessage(ChatColor.RED+"[GG]: Sprinting is on cooldown for "+ChatColor.GOLD+((GG.game.getCooldown(p)+(GG.game.getSugarTime()*1000) - System.currentTimeMillis())/1000)+ ChatColor.RED+" more second(s)!");
								}
							}else {
								GG.game.addCooldown(p);
								p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, GG.game.getSugarTime()*20, 1, true));
								e.getPlayer().sendMessage(ChatColor.AQUA+"[GG]: Sprinting!");
							}							
						}
					}
				}
				if(i.getType()==Material.SNOW_BALL) {
					if(GG.game.isCooldown(p)) {
						if(GG.game.getCooldown(p)+(GG.game.getSnowballTime()*1000) < System.currentTimeMillis()) {
							GG.game.addCooldown(p);
							p.launchProjectile(Snowball.class).setShooter(p);
						}else {
							e.getPlayer().sendMessage(ChatColor.RED+"[GG]: Snowball is on cooldown for "+ChatColor.GOLD+((GG.game.getCooldown(p)+(GG.game.getSnowballTime()*1000) - System.currentTimeMillis())/1000)+ ChatColor.RED+" more second(s)!");
						}
					}else {
						GG.game.addCooldown(p);
						p.launchProjectile(Snowball.class).setShooter(p);
					}
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerHunger(FoodLevelChangeEvent e) {
		if (e.getEntity() instanceof Player) {
			e.setCancelled(true);
			e.setFoodLevel(20);
		}
	}
	
	@EventHandler
	public void onPlayerquit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (GG.game.isPlayer(p)) {
			GG.game.removePlayer(p);
			if(GG.game.isRunner(p)) {
				GG.game.removeRunner(p);
				if(GG.game.getRunners().size()==0) {
					GG.game.getThread().endGame();
				}
			}else if(GG.game.isGuardian(p)) {
				GG.game.removeGuardian(p);
				if(GG.game.getGuardians().size()==0) {
					GG.game.getThread().endGame();
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
    	p.teleport(new Location(p.getWorld(), GG.game.getLobyLocationX(), GG.game.getLobyLocationY(), GG.game.getLobyLocationZ()));
    	p.getInventory().clear();
    	ItemStack[] is = new ItemStack[4];
    	p.getInventory().setArmorContents(is);
        for (PotionEffect effect : p.getActivePotionEffects()) {
            p.removePotionEffect(effect.getType());
        }
	}
	
	@EventHandler
	public void onItemDrop(ItemSpawnEvent e) {
		e.setCancelled(true);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent e) {
		if(e instanceof EntityDamageByEntityEvent) {
			if(e.getEntity() instanceof Player && ((EntityDamageByEntityEvent) e).getDamager() instanceof Snowball) {
				if(e.getCause().equals(DamageCause.PROJECTILE)) {
					Player p = (Player) e.getEntity();
					Snowball b = (Snowball) ((EntityDamageByEntityEvent) e).getDamager();
					GG.game.addHit(p);
					if(b.getShooter() !=null) {
						if(b.getShooter() instanceof Player) {
							Player d = (Player) b.getShooter();
							GG.game.addHit(d);
					        d.sendMessage(ChatColor.AQUA+"[GG]: You hit "+ChatColor.GOLD+p.getName()+ ChatColor.AQUA+"! He has "+ChatColor.GOLD+(GG.game.getMaxHits()-GG.game.getHits(p))+ ChatColor.AQUA+" more left until he is out!");
						}
					}
					p.sendMessage(ChatColor.AQUA+"[GG]: You have been hit by the Guardians! "+ChatColor.GOLD+(GG.game.getMaxHits()-GG.game.getHits(p))+ ChatColor.AQUA+" more and you are out!");
					if(GG.game.getHits(p) >= GG.game.getMaxHits()) {
						GG.game.addGuardian(p);
					}
				}else {
					e.setCancelled(true);
				}
			}
		}
	}
	
	
	
}
