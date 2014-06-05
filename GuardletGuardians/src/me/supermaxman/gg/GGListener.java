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
		e.setRespawnLocation(new Location(p.getWorld(), GuardletGuardians.game.getLobyLocationX(), GuardletGuardians.game.getLobyLocationY(), GuardletGuardians.game.getLobyLocationZ()));
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if(e.getPlayer().isOp()) {
			if(e.getBlock().getTypeId() == GuardletGuardians.game.getGuardianId()) {
				GuardletGuardians.game.removeGspawn(e.getBlock().getRelative(BlockFace.UP).getLocation());
				e.getPlayer().sendMessage(ChatColor.AQUA+"[GuardletGuardians]: New Guardian Spawn removed.");
			}
		}else {
			e.setCancelled(true);
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if(e.getPlayer().isOp()) {
			if(e.getBlock().getTypeId() == GuardletGuardians.game.getGuardianId()) {
				GuardletGuardians.game.addGspawn(e.getBlock().getRelative(BlockFace.UP).getLocation());
				e.getPlayer().sendMessage(ChatColor.AQUA+"[GuardletGuardians]: New Guardian Spawn added.");
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
		if(GuardletGuardians.game.isRunner(p)) {
			Block b = p.getLocation().getBlock().getRelative(BlockFace.DOWN);
			if(b.getTypeId()==GuardletGuardians.game.getFinishId1() && (GuardletGuardians.game.getLaps(p) % 2==0)) {
				GuardletGuardians.game.addLap(p);
				if(GuardletGuardians.game.getLaps(p)<GuardletGuardians.game.getFinishLaps()) {
					if(GuardletGuardians.game.getLaps(p) == GuardletGuardians.game.getFinishLaps()-1) {
				        p.getServer().broadcastMessage(ChatColor.AQUA+"[GuardletGuardians]: "+ChatColor.GOLD+p.getName()+ChatColor.AQUA+" is on his "+ChatColor.GOLD+"final "+ChatColor.AQUA+"lap!");
					}else {
				        p.getServer().broadcastMessage(ChatColor.AQUA+"[GuardletGuardians]: "+ChatColor.GOLD+p.getName()+ChatColor.AQUA+" is on lap "+ChatColor.GOLD+(GuardletGuardians.game.getLaps(p)+1)+ChatColor.AQUA+"!");
					}
				}
			}else if(b.getTypeId()==GuardletGuardians.game.getFinishId2() && (GuardletGuardians.game.getLaps(p) % 2!=0)) {
				GuardletGuardians.game.addLap(p);
				if(GuardletGuardians.game.getLaps(p)<GuardletGuardians.game.getFinishLaps()) {
					if(GuardletGuardians.game.getLaps(p) == GuardletGuardians.game.getFinishLaps()-1) {
				        p.getServer().broadcastMessage(ChatColor.AQUA+"[GuardletGuardians]: "+ChatColor.GOLD+p.getName()+ChatColor.AQUA+" is on his "+ChatColor.GOLD+"final "+ChatColor.AQUA+"lap!");
					}else {
				        p.getServer().broadcastMessage(ChatColor.AQUA+"[GuardletGuardians]: "+ChatColor.GOLD+p.getName()+ChatColor.AQUA+" is on lap "+ChatColor.GOLD+(GuardletGuardians.game.getLaps(p)+1)+ChatColor.AQUA+"!");
					}
				}
			}
		}else if(GuardletGuardians.game.isGuardian(p)) {
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
					if(GuardletGuardians.game.isEnded()) {
						if(GuardletGuardians.game.getPlayers().size()<GuardletGuardians.game.getMaxPlayers()) {
							if(!GuardletGuardians.game.isPlayer(e.getPlayer())) {
								GuardletGuardians.game.addPlayer(e.getPlayer());
					            int i = GuardletGuardians.game.getMinPlayers()-GuardletGuardians.game.getPlayers().size();
								e.getPlayer().sendMessage(ChatColor.AQUA+"[GuardletGuardians]: Joined game, needs "+ChatColor.GOLD+i+ChatColor.AQUA+" more players!");
							}else {
								e.getPlayer().sendMessage(ChatColor.RED+"[GuardletGuardians]: You have already joined!");
							}
						}else {
							e.getPlayer().sendMessage(ChatColor.RED+"[GuardletGuardians]: Cannot join, game is full!");
						}
					}else {
						e.getPlayer().sendMessage(ChatColor.RED+"[GuardletGuardians]: Cannot join, game is active!");
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
							if(GuardletGuardians.game.isCooldown(p)) {
								if(GuardletGuardians.game.getCooldown(p)+(GuardletGuardians.game.getSugarTime()*1000) < System.currentTimeMillis()) {
									GuardletGuardians.game.addCooldown(p);
									p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, GuardletGuardians.game.getSugarTime()*20, 1, true));
									e.getPlayer().sendMessage(ChatColor.AQUA+"[GuardletGuardians]: Sprinting!");
								}else {
									e.getPlayer().sendMessage(ChatColor.RED+"[GuardletGuardians]: Sprint is on cooldown!");
								}
							}else {
								GuardletGuardians.game.addCooldown(p);
								p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, GuardletGuardians.game.getSugarTime()*20, 1, true));
								e.getPlayer().sendMessage(ChatColor.AQUA+"[GuardletGuardians]: Sprinting!");
							}							
						}
					}
				}
				if(i.getType()==Material.SNOW_BALL) {
					if(GuardletGuardians.game.isCooldown(p)) {
						if(GuardletGuardians.game.getCooldown(p)+(GuardletGuardians.game.getSnowballTime()*1000) < System.currentTimeMillis()) {
							GuardletGuardians.game.addCooldown(p);
							i.setAmount(i.getAmount()+1);
							p.updateInventory();
						}else {
							e.getPlayer().sendMessage(ChatColor.RED+"[GuardletGuardians]: Snowball is on cooldown!");
							e.setCancelled(true);
						}
					}else {
						GuardletGuardians.game.addCooldown(p);
						i.setAmount(i.getAmount()+1);
						p.updateInventory();
					}
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
		if (GuardletGuardians.game.isPlayer(p)) {
			GuardletGuardians.game.removePlayer(p);
			//add more stuff here for removing player in game
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
    	p.teleport(new Location(p.getWorld(), GuardletGuardians.game.getLobyLocationX(), GuardletGuardians.game.getLobyLocationY(), GuardletGuardians.game.getLobyLocationZ()));
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
					if(b.getShooter() !=null) {
						if(b.getShooter() instanceof Player) {
							Player d = (Player) b.getShooter();
							GuardletGuardians.game.addHit(d);
					        p.getServer().broadcastMessage(ChatColor.AQUA+"[GuardletGuardians]: You hit "+ChatColor.GOLD+p.getName()+ ChatColor.AQUA+"! He has "+ChatColor.GOLD+(GuardletGuardians.game.getMaxHits()-GuardletGuardians.game.getHits(p))+ ChatColor.AQUA+" more left until he is out!");
						}
					}
					GuardletGuardians.game.addHit(p);
			        p.getServer().broadcastMessage(ChatColor.AQUA+"[GuardletGuardians]: You have been hit by the Guardians! "+ChatColor.GOLD+(GuardletGuardians.game.getMaxHits()-GuardletGuardians.game.getHits(p))+ ChatColor.AQUA+" more and you are out!");
					if(GuardletGuardians.game.getHits(p) >= GuardletGuardians.game.getMaxHits()) {
						GuardletGuardians.game.addGuardian(p);
					}
				}else {
					e.setCancelled(true);
				}
			}
		}
	}
	
	
	
}
