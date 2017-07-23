package com.zarador.model.input.impl;

import com.zarador.model.input.Input;
import com.zarador.world.content.clan.ClanChatManager;
import com.zarador.world.entity.impl.player.Player;

public class EnterClanChatToJoin extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		if (syntax.length() <= 1) {
			player.getPacketSender().sendMessage("Invalid syntax entered.");
			return;
		}
		ClanChatManager.join(player, syntax);
	}
}
