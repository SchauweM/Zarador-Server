package com.zarador.model.input.impl;

import com.zarador.model.input.EnterAmount;
import com.zarador.world.World;
import com.zarador.world.entity.impl.player.Player;

public class EnterAmountToDiceOther extends EnterAmount {

	public EnterAmountToDiceOther(int item, int slot) {
		super(item, slot);
	}

	@Override
	public void handleAmount(Player player, long value) {
		int amount = (int) value;
		if (value > Integer.MAX_VALUE) {
			amount = Integer.MAX_VALUE;
		}
		if (amount > 100) {
			player.getPacketSender().sendMessage("You can't roll over 100.");
			return;
		}
		if (amount < 0) {
			player.getPacketSender().sendMessage("You can't roll under 0.");
			return;
		}
		Player other = World.getPlayerByName(player.dice_other_name);
		other.dice_other_amount = amount;
		other.dice_other = true;
		player.getPacketSender().sendMessage("The player <col=ff0000><shad=0>" + player.dice_other_name
				+ "</col></shad> will roll a <col=ff0000><shad=0>" + amount + "</col></shad> on their next roll.");
	}
}
