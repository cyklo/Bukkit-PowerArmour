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

import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class PowerArmourComponents {
	
	public Material head;
	public Material body;
	public Material legs;
	public Material feet;
	public Material hand;
	
	public Boolean armourdamage;
	
	private List<String> protections;
	
	public boolean compareComponents(PowerArmourComponents test) {
		
		if(test == null) return false;
	    
	    if(this.head != Material.AIR && this.head != test.head) return false;
	    if(this.body != Material.AIR && this.body != test.body) return false;
	    if(this.legs != Material.AIR && this.legs != test.legs) return false;
	    if(this.feet != Material.AIR && this.feet != test.feet) return false;
	    if(this.hand != Material.AIR && this.hand != test.hand) return false;
	    
	    return true;
	    
	}
	
	public void addProtection(String p) {
		if(this.isValidProtection(p) && !this.protections.contains(p)) this.protections.add(p);
	}
	
	public void removeProtection(String p) {
		this.protections.remove(p);
	}
	
	public boolean containsProtection(String p) {
		return this.protections.contains(p);
	}

	private boolean isValidProtection(String p) {
		
		if(p.startsWith("DAMAGE_")) {
			try {
				DamageCause.valueOf(p.substring(7));
				return true;
			} catch(NullPointerException ex) {
				return false;
			}
		}
		
		return false;
	}
}
