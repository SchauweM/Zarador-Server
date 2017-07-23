package com.zarador.model.input.impl;

import com.zarador.model.input.EnterAmount;
import com.zarador.world.content.skill.impl.crafting.Tanning;
import com.zarador.world.entity.impl.player.Player;

public class EnterAmountOfHidesToTan extends EnterAmount {

	private int button;

	public EnterAmountOfHidesToTan(int button) {
		this.button = button;
	}

	@Override
	public void handleAmount(Player player, long value) {
		int amount = (int) value;
		if (value > Integer.MAX_VALUE) {
			amount = Integer.MAX_VALUE;
		}
		Tanning.tanHide(player, button, amount);
	}

}
