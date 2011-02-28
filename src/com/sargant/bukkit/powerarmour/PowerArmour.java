package com.sargant.bukkit.powerarmour;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

public class PowerArmour extends JavaPlugin {
	
	protected Map<PowerArmourAbilities, PowerArmourComponents> armourList;
	protected final Logger log;
	protected Integer verbosity;
	protected Priority pri;
	private final PowerArmourEntityListener entityListener;
	
	public PowerArmour() {
		log = Logger.getLogger("Minecraft");
		verbosity = 2;
		pri = Priority.Lowest;
		entityListener = new PowerArmourEntityListener(this);
		armourList = new HashMap<PowerArmourAbilities, PowerArmourComponents>();
	}

	@Override
	public void onDisable() {
		log.info(getDescription().getName() + " " + getDescription().getVersion() + " unloaded.");
	}

	@Override
	public void onEnable() {
		
		getDataFolder().mkdirs();
		File yml = new File(getDataFolder(), "config.yml");

		if (!yml.exists())
		{
			try {
				yml.createNewFile();
				log.info("Created an empty file " + getDataFolder() +"/config.yml, please edit it!");
				getConfiguration().setProperty("powerarmour", null);
				getConfiguration().save();
			} catch (IOException ex){
				log.warning(getDescription().getName() + ": could not generate config.yml. Are the file permissions OK?");
			}
		}
		
		// Load in the values from the configuration file
		List <String> keys;
		try { 
			keys = getConfiguration().getKeys(null); 
		} catch(NullPointerException ex) {
			log.warning(getDescription().getName() + ": no parent key not found");
			return;
		}
		
		if(keys.contains("verbosity")) {
			String verb_string = getConfiguration().getString("verbosity", "normal");
			
			if(verb_string.equalsIgnoreCase("low")) { verbosity = 1; }
			else if(verb_string.equalsIgnoreCase("high")) { verbosity = 3; }
			else { verbosity = 2; }
		}
		
		if(keys.contains("priority")) {
			String priority_string = getConfiguration().getString("priority", "lowest");
			if(priority_string.equalsIgnoreCase("low")) { pri = Priority.Low; }
			else if(priority_string.equalsIgnoreCase("normal")) { pri = Priority.Normal; }
			else if(priority_string.equalsIgnoreCase("high")) { pri = Priority.High; }
			else if(priority_string.equalsIgnoreCase("highest")) { pri = Priority.Highest; }
			else { pri = Priority.Lowest; }
		}
		
		if(!keys.contains("powerarmour"))
		{
			log.warning(getDescription().getName() + ": no 'powerarmour' key found");
			return;
		}
		
		for(PowerArmourAbilities paa : PowerArmourAbilities.values()) {
			
			if(!getConfiguration().getKeys("powerarmour").contains(paa.toString())) {
				continue;
			}
			
			PowerArmourComponents pac = new PowerArmourComponents();
			
			pac.head = Material.valueOf(getConfiguration().getString("powerarmour." + paa.toString() + ".head", "AIR"));
			pac.body = Material.valueOf(getConfiguration().getString("powerarmour." + paa.toString() + ".body", "AIR"));
			pac.legs = Material.valueOf(getConfiguration().getString("powerarmour." + paa.toString() + ".legs", "AIR"));
			pac.feet = Material.valueOf(getConfiguration().getString("powerarmour." + paa.toString() + ".feet", "AIR"));
			pac.hand = Material.valueOf(getConfiguration().getString("powerarmour." + paa.toString() + ".hand", "AIR"));
			
			armourList.put(paa, pac);
			
			if(verbosity > 1) {
				log.info("Power ability " + paa.toString() + " is enabled.");
			}
		}
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.ENTITY_DAMAGED, entityListener, pri, this);
		
		log.info(getDescription().getName() + " " + getDescription().getVersion() + " loaded.");
	}

}
