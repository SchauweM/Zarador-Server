package com.zarador.net.packet.impl;

import com.zarador.net.packet.Packet;
import com.zarador.net.packet.PacketListener;
import com.zarador.util.Misc;
import com.zarador.world.content.PlayerPunishment;
import com.zarador.world.content.clan.ClanChatManager;
import com.zarador.world.entity.impl.player.Player;

public class SendClanChatMessagePacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		String clanMessage = Misc.readString(packet.getBuffer());
		if (clanMessage == null || clanMessage.length() < 1)
			return;
		if (PlayerPunishment.isMuted(player.getUsername()) || PlayerPunishment.isIpMuted(player.getHostAddress())
				|| Misc.blockedWord(clanMessage)) {
			player.getPacketSender().sendMessage("You either said a bad word, or currently muted!");
			return;
		}
		ClanChatManager.sendMessage(player, clanMessage);
	}

}
