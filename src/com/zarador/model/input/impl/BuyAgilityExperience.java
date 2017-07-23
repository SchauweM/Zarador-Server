package com.zarador.model.input.impl;

import com.zarador.model.Skill;
import com.zarador.model.input.EnterAmount;
import com.zarador.world.entity.impl.player.Player;

public class BuyAgilityExperience extends EnterAmount {

	@Override
	public void handleAmount(Player player, long value) {
		int amount = (int) value;
		if (value > Integer.MAX_VALUE) {
			amount = Integer.MAX_VALUE;
		}
		player.getPacketSender().sendInterfaceRemoval();
		int ticketAmount = player.getInventory().getAmount(11849);
		if (ticketAmount == 0) {
			player.getPacketSender().sendMessage("You do not have any marks of grace.");
			return;
		}
		if (ticketAmount > amount) {
			ticketAmount = amount;
		}

		if (player.getInventory().getAmount(11849) < ticketAmount) {
			return;
		}

		int exp = ticketAmount * 40;
		player.getInventory().delete(11849, ticketAmount);
		player.getSkillManager().addSkillExperience(Skill.AGILITY, exp);
		int expM = exp * player.getGameModeAssistant().getModeExpRate();
		player.getPacketSender().sendMessage("You've bought " + expM + " Agility experience for " + ticketAmount
				+ " Mark"+(ticketAmount == 1 ? "" : "s") + " of grace.");
	}

}
