package com.zarador.world.entity.impl.player.bot.characteristics;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import com.zarador.world.entity.impl.player.bot.util.EquipmentSlots;

public class CombatCharacteristics {

	private final Map<CombatStyle, Map<Integer, Integer>> gear = new HashMap<>();

	private final CombatStyle primary;
	
	
	private final Optional<Spell> spell;

	/**
	 * The tendency to change to a different weapon. This is out of 100
	 */
	private final OptionalInt changeTendency;

	public CombatCharacteristics(CombatStyle primary, Map<Integer, Integer> gear, Optional<Spell> spell) {
		this.primary = primary;
		this.gear.put(primary, gear);
		changeTendency = OptionalInt.empty();
		this.spell = spell;
	}

	public CombatCharacteristics(Map<CombatStyle, Map<Integer, Integer>> gear, CombatStyle primary, int changeTendency, Optional<Spell> spell) {
		if (!gear.containsKey(primary)) {
			System.err.println("Map doesn't contain " + primary.name());
		}
		this.gear.putAll(gear);
		this.primary = primary;
		this.changeTendency = OptionalInt.of(changeTendency);
		this.spell = spell;
	}

	public Map.Entry<CombatStyle, Map<Integer, Integer>> getRandomEntry() {
		Set<CombatStyle> keys = gear.keySet();
		int random = ThreadLocalRandom.current().nextInt(keys.size());
		CombatStyle style = null;
		int i = 0;
		for (CombatStyle s : keys) {
			if (i == random) {
				style = s;
			}
			i++;
		}
		return new AbstractMap.SimpleEntry<CombatStyle, Map<Integer, Integer>>(style, gear.get(style));
	}
	
	public int getSize() {
		return gear.size();
	}
	
	public Optional<Map<Integer, Integer>> of(CombatStyle style) {
		return Optional.ofNullable(gear.get(style));
	}

	public OptionalInt getChangeTendency() {
		return changeTendency;
	}

	public CombatStyle getPrimary() {
		return primary;
	}

	public enum CombatStyle {
		MELEE, RANGE, MAGIC;
	}

	public int getPrimaryWeapon() {
		return of(primary).get().get(EquipmentSlots.WEAPON);
	}

	public Optional<Spell> getSpell() {
		return spell;
	}


}
