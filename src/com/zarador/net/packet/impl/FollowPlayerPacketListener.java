package com.zarador.net.packet.impl;

import com.zarador.model.action.distance.FollowMobileAction;
import com.zarador.net.packet.Packet;
import com.zarador.net.packet.PacketListener;
import com.zarador.world.World;
import com.zarador.world.content.BankPin;
import com.zarador.world.content.minigames.impl.Dueling;
import com.zarador.world.entity.impl.player.Player;

/**
 * Handles the follow player packet listener Sets the player to follow when the
 * packet is executed
 * 
 * @author Gabriel Hannason
 */
public class FollowPlayerPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player.getConstitution() <= 0)
			return;
		int otherPlayersIndex = packet.readLEShort();
		if (otherPlayersIndex < 0 || otherPlayersIndex > World.getPlayers().capacity())
			return;
		Player leader = World.getPlayers().get(otherPlayersIndex);
		if (leader == null)
			return;
		if (leader.getConstitution() <= 0 || player.getConstitution() <= 0
				|| !player.getLocation().isFollowingAllowed()) {
			player.getPacketSender().sendMessage("You cannot follow other players right now.");
			return;
		}
		if (player.getDueling().duelingStatus == 5 && Dueling.checkRule(player, Dueling.DuelRule.NO_MOVEMENT)) {
			player.getPacketSender().sendMessage("You cannot follow in the Duel Arena.");
			return;
		}
		if (player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin()
				&& player.getBankPinAttributes().onDifferent(player)) {
			BankPin.init(player, false);
			return;
		}
		player.getCombatBuilder().reset();
		player.setEntityInteraction(leader);
		player.getActionQueue().addAction(new FollowMobileAction(player, leader));
	}

}
