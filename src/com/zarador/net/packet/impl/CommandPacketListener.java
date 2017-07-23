package com.zarador.net.packet.impl;

import com.zarador.model.player.command.CommandManager;
import com.zarador.net.packet.Packet;
import com.zarador.net.packet.PacketListener;
import com.zarador.util.Misc;
import com.zarador.world.entity.impl.player.Player;

/**
 * This packet listener manages commands a player uses by using the command
 * console prompted by using the "`" char.
 *
 * @author Gabriel Hannason
 */

public class CommandPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (packet.getSize() == 0) {
			return;
		}
		String command = Misc.readString(packet.getBuffer());
		if (command == null) {
			return;
		}
		if (CommandManager.execute(player, command)) {
			return;
		}
	}
}