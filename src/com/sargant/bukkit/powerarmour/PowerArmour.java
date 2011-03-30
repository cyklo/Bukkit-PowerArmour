// PowerArmour - a Bukkit plugin
// Copyright (C) 2011 Robert Sargant
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.

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

import com.sargant.bukkit.common.Common;

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
		verbosity = Common.getVerbosity(this);
		pri = Common.getPriority(this);
		
		List <String> keys = Common.getRootKeys(this);
		
		if(keys == null) {
			log.warning(getDescription().getName() + ": no parent key not found");
			return;
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
			pac.armourdamage = getConfiguration().getBoolean("powerarmour." + paa.toString() + ".armourdamage", false);
			
			armourList.put(paa, pac);
			
			if(verbosity > 1) {
				log.info("Power ability " + paa.toString() + " is enabled.");
			}
		}
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.ENTITY_DAMAGE, entityListener, pri, this);
		
		log.info(getDescription().getName() + " " + getDescription().getVersion() + " loaded.");
	}
}
