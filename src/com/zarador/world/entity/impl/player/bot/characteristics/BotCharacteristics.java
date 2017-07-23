package com.zarador.world.entity.impl.player.bot.characteristics;

import java.util.OptionalInt;

import com.zarador.world.entity.impl.player.Player;
import com.zarador.world.entity.impl.player.bot.Bot;

/**
 * Several characteristics of a {@link Bot} that determine how they perform.
 * Theses values will always be immutable.
 * 
 * @author Kaleem
 *
 */
public final class BotCharacteristics {

	/**
	 * Determines whether or not the {@link Bot} will ask another {@link Player}
	 * obejct for a fight, or if they will simply attack them.
	 */
	private final boolean asks;

	/**
	 * The time in which the {@link Bot} is within the Wilderness and waits
	 * before asking another {@link Player} object for a fight or randomly
	 * attacks
	 */
	private final int waitTime;

	/**
	 * The time in which the {@link Bot} will choose to back away when they have
	 * run out of food. When the value is set to {@link OptionalInt#empty}, the
	 * {@link Bot} will <b>never</b> back away
	 */
	private final OptionalInt backAmount;

	/**
	 * Characteristics relating to the {@link Bot} objects choice of weapon and
	 * other characteristics regarding special weapons
	 */
	private final SpecialWeaponCharacteristics specialWeapon;

	private final CombatCharacteristics combat;

	/**
	 * Characteristics relating to the {@link Bot} objects making use of the
	 * vengeance spell
	 */
	private final VengeanceCharacteristics vengeance;
	
	/**
	 * Characteristics relating to the {@link Bot} being aggressive to other {@link Player} objects
	 */
	private final AggressivenessCharacteristics aggressiveness;
	
	
	/**
	 * Charactersitics relating to the {@link Bot} consuming Food
	 */
	private final FoodCharacteristics food;

	public BotCharacteristics(boolean asks, int waitTime, OptionalInt backAmount,
			SpecialWeaponCharacteristics specialWeapon, CombatCharacteristics combat, VengeanceCharacteristics vengeance, AggressivenessCharacteristics aggressiveness, FoodCharacteristics food) {
		super();
		this.asks = asks;
		this.waitTime = waitTime;
		this.backAmount = backAmount;
		this.specialWeapon = specialWeapon;
		this.combat = combat;
		this.vengeance = vengeance;
		this.aggressiveness = aggressiveness;
		this.food = food;
	}

	public boolean isAsks() {
		return asks;
	}

	public int getWaitTime() {
		return waitTime;
	}

	public OptionalInt getBackAmount() {
		return backAmount;
	}

	public SpecialWeaponCharacteristics getSpecialWeapon() {
		return specialWeapon;
	}
	public VengeanceCharacteristics getVengeance() {
		return vengeance;
	}

	public AggressivenessCharacteristics getAggressiveness() {
		return aggressiveness;
	}

	public FoodCharacteristics getFood() {
		return food;
	}

	public CombatCharacteristics getCombat() {
		return combat;
	}

}
