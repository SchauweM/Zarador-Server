package com.zarador.world.entity.impl.player.bot.set;

import java.util.Set;

import com.zarador.model.Item;

public class BotContainer {
	private final Set<Item> inventory;
	private final Set<Item> equipment;

	public BotContainer(Set<Item> inventory, Set<Item> equipment) {
		super();
		this.inventory = inventory;
		this.equipment = equipment;
	}

	public Set<Item> getInventory() {
		return inventory;
	}

	public Set<Item> getEquipment() {
		return equipment;
	}
}
