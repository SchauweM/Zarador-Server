package com.zarador.world.entity.impl.player.bot.characteristics;

public enum Spell {
	AIR_STRIKE(1152),
	ICE_BARRAGE(12891);
	
	private final int id;

	Spell(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
}
