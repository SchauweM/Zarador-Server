package com.zarador.model.input.impl;

import com.zarador.model.Item;
import com.zarador.model.container.impl.BeastOfBurden;
import com.zarador.model.input.EnterAmount;
import com.zarador.world.entity.impl.player.Player;

public class EnterAmountToRemoveFromBob extends EnterAmount {

	public EnterAmountToRemoveFromBob(int item, int slot) {
		super(item, slot);
	}

	@Override
	public void handleAmount(Player player, long value) {
		int amount = (int) value;
		if (value > Integer.MAX_VALUE) {
			amount = Integer.MAX_VALUE;
		}
		if (player.getSummoning().getBeastOfBurden() == null || player.getInterfaceId() != BeastOfBurden.INTERFACE_ID)
			return;
		if (!player.getSummoning().getBeastOfBurden().contains(getItem()))
			return;
		int invAmount = player.getSummoning().getBeastOfBurden().getAmount(getItem());
		if (amount > invAmount)
			amount = invAmount;
		if (amount <= 0)
			return;
		player.getSummoning().getBeastOfBurden().switchItem(player.getInventory(), new Item(getItem(), amount),
				player.getSummoning().getBeastOfBurden().getSlot(getItem()), false, true);
	}
}
