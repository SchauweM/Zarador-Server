package com.zarador.model.input.impl;

import com.zarador.model.input.EnterAmount;
import com.zarador.world.entity.impl.player.Player;

public class EnterAmountToRemoveFromTrade extends EnterAmount {

	public EnterAmountToRemoveFromTrade(int item) {
		super(item);
	}

	@Override
	public void handleAmount(Player player, long value) {
		int amount = (int) value;
		if (value > Integer.MAX_VALUE) {
			amount = Integer.MAX_VALUE;
		}
		if (player.getTrading().inTrade() && getItem() > 0)
			player.getTrading().removeTradedItem(getItem(), amount);
		else
			player.getPacketSender().sendInterfaceRemoval();
	}

}
