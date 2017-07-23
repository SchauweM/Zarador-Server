package com.zarador.model.input.impl;

import com.zarador.model.StaffRights;
import com.zarador.model.input.Input;
import com.zarador.world.World;
import com.zarador.world.entity.impl.player.Player;

public class GlobalAnnouncement extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		player.getPacketSender().sendInterfaceRemoval();
		if (syntax == null || syntax.length() <= 2 || syntax.length() > 100) {
			player.getPacketSender().sendMessage("That syntax is invalid.");
			return;
		}
		if(player.getStaffRights() == StaffRights.PLAYER) {
			return;
		}
		String text = syntax;
		if(player.getAnnouncementTime() >= 10000) {
			player.getPacketSender().sendMessage("There is a max of 10,000 seconds.");
			return;
		}
		for (Player players : World.getPlayers()) {
			if (players == null)
				continue;
			players.getPacketSender().sendString(1, "[ANNOUNCE]-"+player.getAnnouncementTime()+"-"+text);
		}
		player.getPacketSender().sendMessage("You have sent an announcement for "+player.getAnnouncementTime()+" seconds.");
	}
}
