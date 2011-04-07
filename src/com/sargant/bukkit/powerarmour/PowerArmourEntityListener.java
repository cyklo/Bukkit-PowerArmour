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

import org.bukkit.entity.Player;
import org.bukkit.event.entity.*;

import com.sargant.bukkit.powerarmour.PowerArmour;

public class PowerArmourEntityListener extends EntityListener
{
	private PowerArmour parent;

	public PowerArmourEntityListener(PowerArmour instance) { 
		parent = instance;
	}

	@Override
	public void onEntityDamage(EntityDamageEvent event) {
		
		if(event.isCancelled()) return;
		if(!(event.getEntity() instanceof Player)) return;

		Player human = (Player) event.getEntity();
		PowerArmourComponents loadout = parent.getLoadout(human); 

		EntityDamageEvent.DamageCause d =  event.getCause();
        
		// Compress FIRE_TICK and FIRE into one event
        if(d == EntityDamageEvent.DamageCause.FIRE_TICK) d = EntityDamageEvent.DamageCause.FIRE;
        
        if(parent.isWearerProtected(loadout, "DAMAGE_" + d.toString())) {   
            
            event.setCancelled(true);
            // Reset ticker on fire-based events
            if(d == EntityDamageEvent.DamageCause.FIRE) human.setFireTicks(0);
                
            // Increase armour / tool damage 
            if(loadout.armourdamage) parent.runDamages(human.getInventory(), event.getDamage());
        }
	}
	
	@Override
	public void onEntityTarget(EntityTargetEvent event) {
	    
	    if(event.isCancelled()) return;
	    if(!(event.getTarget() instanceof Player)) return;
	    
	    Player human = (Player) event.getTarget();
	    PowerArmourComponents loadout = parent.getLoadout(human);
	    
	    if(parent.isWearerProtected(loadout, "ENTITY_TARGET")) event.setCancelled(true);
	}
	
	
}

