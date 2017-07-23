package com.zarador.model.input.impl;

import com.zarador.model.input.EnterAmount;
import com.zarador.world.content.skill.impl.crafting.Flax;
import com.zarador.world.entity.impl.player.Player;

public class EnterAmountToSpin extends EnterAmount {

	boolean flax;

	public EnterAmountToSpin(boolean flax) {
		this.flax = flax;
	}

	@Override
	public void handleAmount(Player player, long value) {
		int amount = (int) value;
		if (value > Integer.MAX_VALUE) {
			amount = Integer.MAX_VALUE;
		}
		Flax.spinFlax(player, amount, flax);
	}

}
