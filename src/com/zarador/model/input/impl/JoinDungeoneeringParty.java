package com.zarador.model.input.impl;

import com.zarador.model.input.Input;
import com.zarador.world.World;
import com.zarador.world.entity.impl.player.Player;

public class JoinDungeoneeringParty extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		player.getPacketSender().sendInterfaceRemoval();
		if (syntax == null || syntax.length() <= 0 || syntax.length() > 20) {
			player.getPacketSender().sendMessage("That party is invalid!");
			return;
		}
		Player other = World.getPlayerByName(syntax);
		if(other == null) {
			player.getPacketSender().sendMessage("This party holder is currently offline.");
			return;
		}
		if(other.getDungeoneering().isPartyFull()) {
			player.getPacketSender().sendMessage("This dungeoneering party is full.");
			return;
		}
		player.getPacketSender().sendMessage("You have been put into the dungeoneering party ("+ (other.getDungeoneering().getAmountInParty() + 1) + "/5).");
		other.getDungeoneering().addToParty(player);
	}
}
