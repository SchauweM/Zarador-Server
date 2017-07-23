package com.zarador.world.entity.impl.player.bot.characteristics;

public class AggressivenessCharacteristics {

	private final boolean asks;
	
	private final int distance;

	public AggressivenessCharacteristics(boolean asks, int distance) {
		this.asks = asks;
		this.distance = distance;
	}

	public boolean doesAsk() {
		return asks;
	}

	public int getDistance() {
		return distance;
	}

}
