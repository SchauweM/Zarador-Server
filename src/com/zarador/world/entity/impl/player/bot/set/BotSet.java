package com.zarador.world.entity.impl.player.bot.set;

public abstract class BotSet {

	private final String name;

	private final BotContainer container;

	public BotSet(String name, BotContainer container) {
		super();
		this.name = name;
		this.container = container;
	}

	public String getName() {
		return name;
	}

	public BotContainer getContainer() {
		return container;
	}
}
