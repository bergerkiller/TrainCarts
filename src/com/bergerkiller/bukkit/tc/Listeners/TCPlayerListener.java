package com.bergerkiller.bukkit.tc.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.block.Sign;

import com.bergerkiller.bukkit.tc.MinecartGroup;
import com.bergerkiller.bukkit.tc.TrainProperties;
import com.bergerkiller.bukkit.tc.Utils.BlockUtil;

public class TCPlayerListener extends PlayerListener {

	@Override
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (BlockUtil.isRails(event.getClickedBlock())) {
				ItemStack item = event.getPlayer().getItemInHand();
				if (item != null) {
					if (item.getType() == Material.MINECART || 
							item.getType() == Material.POWERED_MINECART || 
							item.getType() == Material.STORAGE_MINECART) {
						//Placing a minecart on the tracks
						if (event.getPlayer().hasPermission("train.place.minecart")) {
							TCVehicleListener.lastPlayer = event.getPlayer();
						} else {
							event.setCancelled(true);
						}
		        return;
					}
				}
			}
		}
    if ((event.getAction() == Action.RIGHT_CLICK_BLOCK) || (event.getAction() == Action.LEFT_CLICK_BLOCK)) {
			if (event.getClickedBlock().getState() instanceof Sign){
			  Sign s = (Sign)event.getClickedBlock().getState();
			  if (!s.getLine(0).equalsIgnoreCase("[train]")) return; //ignore non-train signs
			  if (!s.getLine(1).toLowerCase().startsWith("destination")) return; //ignore
		    //get the train this player is editing
        Player p = event.getPlayer();
	      TrainProperties prop = TrainProperties.getEditing(p);
	      //permissions
	      if (prop == null) {
	        if (TrainProperties.canBeOwner(p)) {
	          p.sendMessage(ChatColor.YELLOW + "You haven't selected a train to edit yet!");
	        } else {
	          p.sendMessage(ChatColor.RED + "You don't own a train you can change!");
	        }     
	      } else if (!prop.isOwner(p)) {
	        p.sendMessage(ChatColor.RED + "You don't own this train!");
	      } else {
	        prop.destination = s.getLine(2);
          p.sendMessage(ChatColor.YELLOW + "You have selected " + ChatColor.WHITE + prop.destination + ChatColor.YELLOW + " as your destination!");
	      }
			  return;
			}
		}
	}
	
	@Override
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		MinecartGroup g = MinecartGroup.get(event.getRightClicked());
		if (g != null) {
			g.getProperties().setEditing(event.getPlayer());
			MinecartGroup entered = MinecartGroup.get(event.getPlayer().getVehicle());
			if (entered != null && !entered.getProperties().allowPlayerExit) {
				event.setCancelled(true);
			}
		}
	}
	
}
