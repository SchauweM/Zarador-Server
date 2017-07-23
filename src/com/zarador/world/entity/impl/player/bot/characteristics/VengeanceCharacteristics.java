package com.zarador.world.entity.impl.player.bot.characteristics;

import java.util.EnumSet;
import java.util.Set;


public class VengeanceCharacteristics {
	/**
	 * Determines whether or not the {@link Bot} makes use of the Vengeance
	 * spell. This will deliberately ignore the players currently spellbook, to
	 * allow a {@link Bot} object to have both access to Vengeance and the
	 * Ancient Magicks.
	 */
	private final boolean usesVengeance;

	private final Set<VengeanceTendency> tendencySet;

	public VengeanceCharacteristics() {
		super();
		this.usesVengeance = false;
		this.tendencySet = EnumSet.noneOf(VengeanceTendency.class);
	}

	public VengeanceCharacteristics(Set<VengeanceTendency> tendencySet) {
		super();
		this.usesVengeance = true;
		this.tendencySet = tendencySet;
	}

	public boolean isUsesVengeance() {
		return usesVengeance;
	}

	public Set<VengeanceTendency> getTendencySet() {
		return tendencySet;
	}

	public enum VengeanceTendency {
		/**
		 * Upon switching to a new weapon
		 */
		WEAPON_SWITCH,

		/**
		 * Upon eating food
		 */
		EATING_FOOD,

		/**
		 * Upon the cooldown immediately going down
		 */
		THIRTY_SECOND,

	}
}
