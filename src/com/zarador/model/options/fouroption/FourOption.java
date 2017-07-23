package com.zarador.model.options.fouroption;

import com.zarador.model.options.Option;
import com.zarador.world.entity.impl.player.Player;

public abstract class FourOption extends Option {
	private final String firstOption;
	private final String secondOption;
	private final String thirdOption;
	private final String fourthOption;

	protected FourOption(String firstOption, String secondOption, String thirdOption, String fourthOption) {
		this.firstOption = firstOption;
		this.secondOption = secondOption;
		this.thirdOption = thirdOption;
		this.fourthOption = fourthOption;
	}

	@Override
	protected final void display(Player player) {
		player.getDialog().sendOption4(firstOption, secondOption, thirdOption, fourthOption);
	}
}
