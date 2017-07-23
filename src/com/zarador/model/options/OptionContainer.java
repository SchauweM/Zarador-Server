package com.zarador.model.options;

import com.zarador.world.entity.impl.player.Player;

public class OptionContainer {
	private final Player player;
	private Option option;

	public OptionContainer(Player player) {
		this.player = player;
	}

	public void display(Option option) {
		this.option = option;
		option.display(player);
	}

	public boolean handle(Option.OptionType type) {
		if (option == null) {
			return false;
		}
		Option oldOption = option;
		//player.getPacketSender().sendInterfaceRemoval();
		this.close();
		oldOption.execute(player, type);
		return true;
	}

	public Option getOption() {
		return option;
	}

	public void close() {
		option = null;
	}
}
