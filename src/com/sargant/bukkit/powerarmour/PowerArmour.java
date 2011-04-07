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
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

import com.sargant.bukkit.common.*;

public class PowerArmour extends JavaPlugin {
	
	protected HashMap<String, PowerArmourComponents> armourList;
	protected final Logger log;
	protected Integer verbosity;
	protected Priority pri;
	private final PowerArmourEntityListener entityListener;
	
	public PowerArmour() {
		log = Logger.getLogger("Minecraft");
		verbosity = 2;
		pri = Priority.Lowest;
		entityListener = new PowerArmourEntityListener(this);
		armourList = new HashMap<String, PowerArmourComponents>();
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
		verbosity = CommonPlugin.getVerbosity(this);
		pri = CommonPlugin.getPriority(this);
		
		List <String> keys = CommonPlugin.getRootKeys(this);
		
		if(keys == null) {
			log.warning(getDescription().getName() + ": no parent key not found");
			return;
		}
		
		if(!keys.contains("powerarmour")) {
			log.warning(getDescription().getName() + ": no 'powerarmour' key found");
			return;
		}
		
		List<String> blocks = getConfiguration().getKeys("powerarmour");
		
		if(blocks == null) {
			log.warning(getDescription().getName() + ": no definition blocks found!");
			return;
		}
		
		for(String s : blocks) {
		    
			PowerArmourComponents pac = null;
			pac = new PowerArmourComponents();
			
			pac.head = Material.valueOf(getConfiguration().getString("powerarmour." + s + ".equipment.head", "AIR"));
			pac.body = Material.valueOf(getConfiguration().getString("powerarmour." + s + ".equipment.body", "AIR"));
			pac.legs = Material.valueOf(getConfiguration().getString("powerarmour." + s + ".equipment.legs", "AIR"));
			pac.feet = Material.valueOf(getConfiguration().getString("powerarmour." + s + ".equipment.feet", "AIR"));
			pac.hand = Material.valueOf(getConfiguration().getString("powerarmour." + s + ".equipment.hand", "AIR"));
			
			pac.armourdamage = getConfiguration().getBoolean("powerarmour." + s + ".armourdamage", false);
			
			List<Object> o = getConfiguration().getList("powerarmour." + s + ".protection");
			
			if(o != null) {
			    for(Object k : o) pac.addProtection((String) k);
			} else {
			    log.warning(getDescription().getName() + ": no protection found in " + s);
			    continue;
			}
			
			armourList.put(s, pac);
			
			if(verbosity  > 1) log.info("PowerArmour ability " + s + " is enabled.");
		}
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.ENTITY_DAMAGE, entityListener, pri, this);
		pm.registerEvent(Event.Type.ENTITY_TARGET, entityListener, pri, this);
		
		log.info(getDescription().getName() + " " + getDescription().getVersion() + " loaded.");
	}
	
    protected void runDamages(PlayerInventory i, Integer dmg) {
        
        // Helmet
        i.getHelmet().setDurability((short) (i.getHelmet().getDurability() + dmg));
        if(i.getHelmet().getDurability() > i.getHelmet().getType().getMaxDurability()) {
            i.setHelmet(null);
        }
        
        // Chestplate
        i.getChestplate().setDurability((short) (i.getChestplate().getDurability() + dmg));
        if(i.getChestplate().getDurability() > i.getChestplate().getType().getMaxDurability()) {
            i.setChestplate(null);
        }
        
        // Leggings
        i.getLeggings().setDurability((short) (i.getLeggings().getDurability() + dmg));
        if(i.getLeggings().getDurability() > i.getLeggings().getType().getMaxDurability()) {
            i.setLeggings(null);
        }
        
        // Boots
        i.getBoots().setDurability((short) (i.getBoots().getDurability() + dmg));
        if(i.getBoots().getDurability() > i.getBoots().getType().getMaxDurability()) {
            i.setBoots(null);
        }
    }
    
    protected PowerArmourComponents getLoadout(Player player) {
        PowerArmourComponents loadout = new PowerArmourComponents();
        loadout.head = player.getInventory().getHelmet().getType();
        loadout.body = player.getInventory().getChestplate().getType();
        loadout.legs = player.getInventory().getLeggings().getType();
        loadout.feet = player.getInventory().getBoots().getType();
        loadout.hand = player.getItemInHand().getType();
        return loadout;
    }
    
    protected boolean isWearerProtected(PowerArmourComponents loadout, String damageEvent) {
        
        for(String name : this.armourList.keySet()) {    
            PowerArmourComponents c = this.armourList.get(name);
            // Check loadout matches
            if(!c.compareComponents(loadout)) continue;
            if(c.containsProtection(damageEvent)) {
                loadout.armourdamage = c.armourdamage;
                return true;
            }
        }
        return false;
    }
}
