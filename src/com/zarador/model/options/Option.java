package com.zarador.model.options;

import com.zarador.world.entity.impl.player.Player;

public abstract class Option {
	protected abstract void display(Player player);

	public abstract void execute(Player player, OptionType option);

	public enum OptionType {
		OPTION_1_OF_2,
		OPTION_2_OF_2,
		OPTION_1_OF_3,
		OPTION_2_OF_3,
		OPTION_3_OF_3,
		OPTION_1_OF_4,
		OPTION_2_OF_4,
		OPTION_3_OF_4,
		OPTION_4_OF_4,
		OPTION_1_OF_5,
		OPTION_2_OF_5,
		OPTION_3_OF_5,
		OPTION_4_OF_5,
		OPTION_5_OF_5
	}
}
