package com.sargant.bukkit.powerarmour;

import org.bukkit.Material;

public class PowerArmourComponents {
	public Material head;
	public Material body;
	public Material legs;
	public Material feet;
	public Material hand;
	
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
