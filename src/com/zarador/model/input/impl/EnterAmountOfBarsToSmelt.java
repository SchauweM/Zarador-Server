package com.zarador.model.input.impl;

import com.zarador.model.input.EnterAmount;
import com.zarador.world.content.skill.impl.smithing.Smelting;
import com.zarador.world.content.skill.impl.smithing.SmithingData;
import com.zarador.world.entity.impl.player.Player;

public class EnterAmountOfBarsToSmelt extends EnterAmount {

	public EnterAmountOfBarsToSmelt(int bar) {
		this.bar = bar;
	}

	@Override
	public void handleAmount(Player player, long value) {
		int amount = (int) value;
		if (value > Integer.MAX_VALUE) {
			amount = Integer.MAX_VALUE;
		}
		for (int barId : SmithingData.SMELT_BARS) {
			if (barId == bar) {
				Smelting.smeltBar(player, barId, amount);
				break;
			}
		}
	}

	private int bar;

	public int getBar() {
		return bar;
	}

}
