package com.bergerkiller.bukkit.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import com.bergerkiller.bukkit.tc.Destinations;
import com.bergerkiller.bukkit.tc.GroupManager;

public class GlobalCommands {
	
	public static boolean execute(CommandSender sender, String[] args) {
		if (args[0].equals("removeall") || args[0].equals("destroyall")) {
			if (Commands.permission(sender, "train.command.remove")) {
				boolean destroy = args[0].equals("destroyall");
				if (args.length == 2) {
					String cname = args[1].toLowerCase();
					World w = null;
					for (World world : Bukkit.getServer().getWorlds()) {
						String wname = world.getName().toLowerCase();
						if (wname.equals(cname)) {
							w = world;
							break;
						}
					}
					if (w == null) {
						for (World world : Bukkit.getServer().getWorlds()) {
							String wname = world.getName().toLowerCase();
							if (wname.contains(cname)) {
								w = world;
								break;
							}
						}
					}
					if (w != null) {
						int count = GroupManager.removeAll(w, destroy);
						sender.sendMessage(ChatColor.RED + "All train information of '" + w.getName() + "' has been cleared!");
						if (destroy) {
							sender.sendMessage(ChatColor.RED.toString() + count + " (visible) trains have been destroyed!");	
						}
					} else {
						sender.sendMessage(ChatColor.RED + "World not found!");
					}
				} else {
					int count = GroupManager.removeAll(destroy);
					sender.sendMessage(ChatColor.RED + "All train information of this server has been cleared!");
					if (destroy) {
						sender.sendMessage(ChatColor.RED.toString() + count + " (visible) trains have been destroyed!");				
					}
				}
			}
			return true;
		} else if (args[0].equals("reroute")) {
			if (Commands.permission(sender, "train.build.destination")) {
				Destinations.clear();
				sender.sendMessage("All train routings will be recalculated.");
			}
			return true;
		}
		return false;
	}

}