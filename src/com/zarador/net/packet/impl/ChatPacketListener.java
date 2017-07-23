package com.zarador.net.packet.impl;

import com.zarador.model.Flag;
import com.zarador.model.ChatMessage.Message;
import com.zarador.net.packet.Packet;
import com.zarador.net.packet.PacketListener;
import com.zarador.util.Misc;
import com.zarador.world.content.PlayerPunishment;
import com.zarador.world.entity.impl.player.Player;

/**
 * This packet listener manages the spoken text by a player.
 * 
 * @author relex lawl
 */

public class ChatPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int effects = packet.readUnsignedByteS();
		int color = packet.readUnsignedByteS();
		int size = packet.getSize();
		byte[] text = packet.readReversedBytesA(size);
		if (PlayerPunishment.isMuted(player.getUsername()) || PlayerPunishment.isIpMuted(player.getHostAddress())) {
			player.getPacketSender().sendMessage("You are muted and cannot chat.");
			return;
		}

		String str = Misc.textUnpack(text, size).toLowerCase().replaceAll(";", ".");
		if (Misc.blockedWord(str)) {
			return;
		}
		// Logs.write_data(player.getUsername()+ ".txt", "global_chats",
		// ""+str+"");
		player.getChatMessages().set(new Message(color, effects, text));
		player.getUpdateFlag().flag(Flag.CHAT);
	}

}
