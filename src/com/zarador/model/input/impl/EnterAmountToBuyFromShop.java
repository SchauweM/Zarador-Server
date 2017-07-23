package com.zarador.model.input.impl;

import com.zarador.model.Item;
import com.zarador.model.container.impl.Shop;
import com.zarador.model.input.EnterAmount;
import com.zarador.world.entity.impl.player.Player;

public class EnterAmountToBuyFromShop extends EnterAmount {

	public EnterAmountToBuyFromShop(int item, int slot) {
		super(item, slot);
	}

	@Override
	public void handleAmount(Player player, long value) {
		int amount = (int) value;
		if (value > Integer.MAX_VALUE) {
			amount = Integer.MAX_VALUE;
		}
		if (player.isShopping() && getItem() > 0 && getSlot() >= 0) {
			Shop shop = player.getShop();
			if (shop != null) {
				if (getSlot() >= shop.getItems().length || shop.getItems()[getSlot()].getId() != getItem())
					return;
				player.getShop().setPlayer(player).forSlot(getSlot()).copy().setAmount(amount).copy();
				shop.switchItem(player.getInventory(), new Item(getItem(), amount), getSlot(), false, true);
			} else
				player.getPacketSender().sendInterfaceRemoval();
		} else
			player.getPacketSender().sendInterfaceRemoval();

	}

}
