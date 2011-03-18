// OtherBlocks - a Bukkit plugin
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

import java.util.Arrays;
import java.util.List;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public enum PowerArmourAbilities {
	WATERPROOF (Arrays.asList(DamageCause.DROWNING)),
	FIREPROOF (Arrays.asList(DamageCause.FIRE, DamageCause.FIRE_TICK, DamageCause.LAVA)),
	BOMBPROOF (Arrays.asList(DamageCause.BLOCK_EXPLOSION, DamageCause.ENTITY_EXPLOSION)),
	MONSTERPROOF (Arrays.asList(DamageCause.CONTACT, DamageCause.ENTITY_ATTACK)),
	FEATHERWEIGHT (Arrays.asList(DamageCause.FALL)),
	INVINCIBLE (Arrays.asList(DamageCause.values()));
	
	private final List<DamageCause> abilities;
	
	PowerArmourAbilities(List<DamageCause> abilities) {
		this.abilities = abilities;
	}
	
	public List<DamageCause> getAbilities() {
		return abilities;
	}
}