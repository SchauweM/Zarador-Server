package com.zarador.net.packet.impl;

import com.zarador.GameSettings;
import com.zarador.net.packet.Packet;
import com.zarador.net.packet.PacketListener;
import com.zarador.world.entity.impl.player.Player;

public class CloseInterfacePacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		player.getPacketSender().sendClientRightClickRemoval();
		player.getPacketSender().sendInterfaceRemoval();
		// player.getPacketSender().sendTabInterface(Constants.CLAN_CHAT_TAB,
		// 29328); //Clan chat tab
		// player.getAttributes().setSkillGuideInterfaceData(null);
		if (GameSettings.DEBUG_MODE) {
			// PlayerLogs.log(player, "" + player.getUsername()
			// + " has changed close interface in
			// CloseInterfacePacketListener");
		}
	}
}
