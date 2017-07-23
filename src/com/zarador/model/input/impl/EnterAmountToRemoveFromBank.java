package com.zarador.model.input.impl;

import com.zarador.model.Item;
import com.zarador.model.container.impl.Bank;
import com.zarador.model.input.EnterAmount;
import com.zarador.world.entity.impl.player.Player;

public class EnterAmountToRemoveFromBank extends EnterAmount {

	public EnterAmountToRemoveFromBank(int item, int slot) {
		super(item, slot);
	}

	@Override
	public void handleAmount(Player player, long value) {
		int amount = (int) value;
		if (value > Integer.MAX_VALUE) {
			amount = Integer.MAX_VALUE;
		}
		if (!player.isBanking())
			return;
		int tab = Bank.getTabForItem(player, getItem());
		int item = player.getBankSearchingAttributes().isSearchingBank()
				&& player.getBankSearchingAttributes().getSearchedBank() != null
						? player.getBankSearchingAttributes().getSearchedBank().getItems()[getSlot()].getId()
						: player.getBank(tab).getItems()[getSlot()].getId();
		if (item != getItem())
			return;
		if (!player.getBank(tab).contains(item))
			return;
		int invAmount = player.getBank(tab).getAmount(item);
		if (amount > invAmount)
			amount = invAmount;
		if (amount <= 0)
			return;
		player.getPacketSender().sendString(1, "[WITHDRAWX]-"+amount);
		player.setWithdrawX(amount);
		player.getBank(tab).setPlayer(player).switchItem(player.getInventory(), new Item(item, amount),
				player.getBank(tab).getSlot(item), false, true);
	}
}
