package com.zarador.net.packet.impl;

import com.zarador.net.packet.Packet;
import com.zarador.net.packet.PacketListener;
import com.zarador.world.entity.impl.player.Player;

/**
 * This packet listener handles player's mouse click on the
 * "Click here to continue" option, etc.
 * 
 * @author relex lawl
 */

public class DialoguePacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		switch (packet.getOpcode()) {
		case DIALOGUE_OPCODE:
			if (player.currentDialog != null) {
				player.currentDialog.incrementState();
				player.getDialog().sendDialog(player.currentDialog);
			} else {
				player.getPacketSender().sendInterfaceRemoval();
			}
			break;
		}
	}

	public static final int DIALOGUE_OPCODE = 40;
}
