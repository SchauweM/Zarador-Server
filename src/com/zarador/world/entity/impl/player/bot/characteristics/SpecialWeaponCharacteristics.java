package com.zarador.world.entity.impl.player.bot.characteristics;

import java.util.OptionalInt;

public final class SpecialWeaponCharacteristics {

	/**
	 * The {@link Item} the {@link Bot} will choose to bring out when the other
	 * {@link Player} is on {@link #
	 */
	public final OptionalInt weaponId;

	/**
	 * The percentage remaining of the opponent in which the {@link Bot} will
	 * bring out the {@link #weaponId}
	 */
	private final OptionalInt opponentPercentage;
	
	/**
	 * The delay between each special attack by the {@link Bot}, when applicable
	 */
	private final OptionalInt delayBetween;

	public SpecialWeaponCharacteristics(int weaponId, int opponentPercentage,  OptionalInt delayBetween) {
		this.weaponId = OptionalInt.of(weaponId);
		this.opponentPercentage = OptionalInt.of(opponentPercentage);
		this.delayBetween = delayBetween;
	}

	public SpecialWeaponCharacteristics() {
		this.weaponId = OptionalInt.empty();
		this.opponentPercentage = OptionalInt.empty();
		this.delayBetween = OptionalInt.empty();
	}

	public OptionalInt getWeaponId() {
		return weaponId;
	}

	public OptionalInt getOpponentPercentage() {
		return opponentPercentage;
	}

	public OptionalInt getDelayBetween() {
		return delayBetween;
	}

}
