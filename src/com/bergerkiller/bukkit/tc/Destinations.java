package com.bergerkiller.bukkit.tc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.util.config.Configuration;

public class Destinations {
	private static HashMap<String, Destinations> properties = new HashMap<String, Destinations>();
	public static Destinations get(String destname) {
		if (destname == null) return null;
		Destinations prop = properties.get(destname);
		if (prop == null) {
			return new Destinations(destname);
		}
		return prop;
	}
	public static boolean exists(String destname) {
		return properties.containsKey(destname);
	}
	
	private String destname;
	public List<String> north = new ArrayList<String>();
  public List<String> east = new ArrayList<String>();
  public List<String> west = new ArrayList<String>();
  public List<String> south = new ArrayList<String>();
	
	private Destinations() {};
	private Destinations(String destname) {
		properties.put(destname, this);
		this.destname = destname;
	}
	
	public String getDestName() {
		return this.destname;
	}
	
	public void remove() {
		properties.remove(this.destname);
	}
	public void add() {
		properties.put(this.destname, this);
	}
	public void rename(String newdestname) {
		this.remove();
		this.destname = newdestname;
		properties.put(newdestname, this);
	}

	public void load(Configuration config, String key) {
		this.north = config.getStringList(key + ".north", this.north);
    this.east = config.getStringList(key + ".east", this.east);
    this.south = config.getStringList(key + ".south", this.south);
    this.west = config.getStringList(key + ".west", this.west);
	}
	public void load(Destinations source) {
		this.north.addAll(source.north);
    this.east.addAll(source.east);
    this.south.addAll(source.south);
    this.west.addAll(source.west);
	}
	public void save(Configuration config, String key) {		
		config.setProperty(key + ".north", this.north);
    config.setProperty(key + ".east", this.east);
    config.setProperty(key + ".south", this.south);
    config.setProperty(key + ".west", this.west);
	}
	public static void load(String filename) {
		Configuration config = new Configuration(new File(filename));
		config.load();
		for (String destname : config.getKeys()) {
			get(destname).load(config, destname);
		}
	}
	public static void save(String filename) {
		Configuration config = new Configuration(new File(filename));
		for (Destinations prop : properties.values()) {
			//does this train even exist?!
			if (GroupManager.contains(prop.getDestName())) {
				prop.save(config, prop.getDestName());
			} else {
				config.removeProperty(prop.getDestName());
			}
		}
		config.save();
	}

}
