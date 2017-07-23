package com.zarador.model.input.impl;

import com.zarador.model.container.impl.Bank.BankSearchAttributes;
import com.zarador.model.input.Input;
import com.zarador.world.entity.impl.player.Player;

public class EnterSyntaxToBankSearchFor extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		boolean searchingBank = player.isBanking() && player.getBankSearchingAttributes().isSearchingBank();
		if (searchingBank)
			BankSearchAttributes.beginSearch(player, syntax);
	}
}
