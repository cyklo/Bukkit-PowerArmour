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

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.PlayerInventory;

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
		PowerArmourComponents loadout = new PowerArmourComponents();

		loadout.head = human.getInventory().getHelmet().getType();
		loadout.body = human.getInventory().getChestplate().getType();
		loadout.legs = human.getInventory().getLeggings().getType();
		loadout.feet = human.getInventory().getBoots().getType();
		loadout.hand = human.getItemInHand().getType();

		for(String name : parent.armourList.keySet()) {
			
			PowerArmourComponents c = parent.armourList.get(name);

			// Check loadout matches
			if(!c.compareComponents(c)) continue;

			// The loadout is correct for the current power ability
			// Does the current damage type match a corresponding proof-ness?
			DamageCause d =  event.getCause();
			
			if(d == DamageCause.FIRE_TICK) d = DamageCause.FIRE;
			
			if(c.containsProtection("DAMAGE_" + d.toString())) {
				
				event.setCancelled(true);
				
				// Reset ticker on fire-based events
				if(d == DamageCause.FIRE) human.setFireTicks(0);
				
				// Increase armour / tool damage 
				if(c.armourdamage) {
					
					Integer dmg = event.getDamage();
					PlayerInventory i = human.getInventory();
					
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
				
				return;
			}
		}
	}
}

