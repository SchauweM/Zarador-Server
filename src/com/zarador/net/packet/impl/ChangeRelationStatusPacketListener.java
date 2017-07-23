package com.zarador.net.packet.impl;

import com.zarador.GameSettings;
import com.zarador.model.PlayerRelations.PrivateChatStatus;
import com.zarador.net.packet.Packet;
import com.zarador.net.packet.PacketListener;
import com.zarador.world.entity.impl.player.Player;

public class ChangeRelationStatusPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int actionId = packet.readInt();
		if (GameSettings.DEBUG_MODE) {
			// PlayerLogs.log(player, "" + player.getUsername()
			// + " has changed their private status in
			// ChangeRelationStatusPacketListener");
		}
		player.getRelations().setStatus(PrivateChatStatus.forActionId(actionId), true);
	}

}
