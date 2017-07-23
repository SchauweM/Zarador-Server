package com.zarador.net.packet.impl;

import com.zarador.model.definitions.NpcDefinition;
import com.zarador.net.packet.Packet;
import com.zarador.net.packet.PacketListener;
import com.zarador.world.entity.impl.player.Player;

public class ExamineNpcPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int npc = packet.readShort();
		if (npc <= 0) {
			return;
		}
		NpcDefinition npcDef = NpcDefinition.forId(npc);
		if (npcDef != null) {
			player.getPacketSender().sendMessage(npcDef.getExamine());
		}
	}

}
