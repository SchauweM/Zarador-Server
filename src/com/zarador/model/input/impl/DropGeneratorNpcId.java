package com.zarador.model.input.impl;

import com.zarador.model.definitions.NpcDefinition;
import com.zarador.model.input.EnterAmount;
import com.zarador.world.entity.impl.player.Player;

public class DropGeneratorNpcId extends EnterAmount {

	@Override
	public void handleAmount(Player player, long value) {
		int amount = (int) value;
		if (value > Integer.MAX_VALUE) {
			amount = Integer.MAX_VALUE;
		}
		if(NpcDefinition.forId(amount) == null) {
			player.getPacketSender().sendMessage("This npc does not exist.");
			return;
		}
		player.getDropGenerator().setNpcId(amount);
		player.setHasNext(true);
		player.setInputHandling(new DropGeneratorAmount());
		player.getPacketSender().sendEnterAmountPrompt("Enter how many drops to generate:");
	}

}
