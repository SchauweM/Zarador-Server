package com.zarador.model.input.impl;

import org.scripts.kotlin.content.dialog.npcs.Ellis;

import com.zarador.model.Item;
import com.zarador.model.input.Input;
import com.zarador.world.entity.impl.player.Player;

public class EnterYellTag extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		if(syntax.length() >= 15) {
			player.getPacketSender().sendMessage("Your yell title can only have 15 characters!");
			return;
		}
		String yell_tag = syntax;
		boolean invalidSymbols = false;
		int value = player.getDonatorRights().getYellTagPrice();
		String[] not_allowed = { "owner", "moderator", "admin", "fuck", "bitch", "shit", "nigger", "cancer", ".com",
				".org", ".net", "asshole", "faggot", "porn", "penis", "vagina", "ballsack", "nutsack", "<", ">", "img=",
				"col=", "shad=", "hair pube", "@" };
		for (int i = 0; i < not_allowed.length; i++) {
			if (yell_tag.toLowerCase().contains(not_allowed[i])) {
				invalidSymbols = true;
			}
		}
		if (invalidSymbols) {
			player.getDialog().sendDialog(new Ellis(player, 8));
			return;
		}
		if (player.getInventory().getAmount(995) >= value) {
			player.getInventory().delete(new Item(995, value));
		} else if (player.getMoneyInPouch() >= value) {
			player.setMoneyInPouch((player.getMoneyInPouch() - value));
		} else {
			player.getDialog().sendDialog(new Ellis(player, 7));
			return;
		}
		player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());

		player.setYellTag(yell_tag);
		player.getDialog().sendDialog(new Ellis(player, 9));
	}
}
