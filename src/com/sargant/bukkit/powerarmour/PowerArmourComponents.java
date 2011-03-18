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

import org.bukkit.Material;

public class PowerArmourComponents {
	public Material head;
	public Material body;
	public Material legs;
	public Material feet;
	public Material hand;
	public Boolean armourdamage;
	
	public String toString() {
		
		String retval = "{";
		
		retval += "head=" + head + ";";
		retval += "body=" + body + ";";
		retval += "legs=" + legs + ";";
		retval += "feet=" + feet + ";";
		retval += "hand=" + hand + "}";
		
		return retval;
	}
}
