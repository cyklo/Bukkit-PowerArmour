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