package com.zarador.world.entity.impl.player.bot.characteristics;

import java.util.OptionalInt;

public class FoodCharacteristics {
	
	/**
	 * The id of the item that the {@link Bot} object will eat when it can
	 */
	private OptionalInt id;
	
	/**
	 * The percentage the {@link Bot} object needs to be on (in terms of health) to trigger eating
	 */
	private OptionalInt eatPercentage;
	
	public FoodCharacteristics() {
		id = OptionalInt.empty();
		eatPercentage = OptionalInt.empty();
	}
	
	public FoodCharacteristics(int id, int eatPercentage) {
		this.id = OptionalInt.of(id);
		this.eatPercentage = OptionalInt.of(eatPercentage);
	}

	public OptionalInt getEatPercentage() {
		return eatPercentage;
	}
	public OptionalInt getId() {
		return id;
	}

}
