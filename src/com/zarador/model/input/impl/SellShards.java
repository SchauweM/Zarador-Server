package com.zarador.model.input.impl;

import com.zarador.model.definitions.ItemDefinition;
import com.zarador.model.input.EnterAmount;
import com.zarador.util.Misc;
import com.zarador.world.entity.impl.player.Player;

/**
 * Handles selling spirit shards
 * 
 * @Jonathan Sirens
 **/

public class SellShards extends EnterAmount {

	@Override
	public void handleAmount(Player player, long value) {
		int amount = (int) value;
		if (value > Integer.MAX_VALUE) {
			amount = Integer.MAX_VALUE;
		}
		if (amount < 1) {
			player.getPacketSender().sendMessage("You can't sell this amount of Spirit shards.");
			return;
		}
		if (player.getMoneyInPouch() < 0) {
			player.getPacketSender().sendMessage("Your money pouch is negative.");
			return;
		}
		// Sets the amount that you actually have in your inventory of Spirit
		// shards
		if (player.getInventory().getAmount(18016) < amount) {
			amount = player.getInventory().getAmount(18016);
		}
		// Tells if the amount of Spirit shards in your inventory is 0 or
		// duplication amount under
		// negative
		if (amount <= 0) {
			player.getPacketSender().sendMessage("You do not have this amount of Spirit shards!");
			return;
		}
		if (player.getInventory().getAmount(995)
				+ ((long) amount * ItemDefinition.forId(18016).getValue()) > Integer.MAX_VALUE) {
			player.getPacketSender().sendInterfaceRemoval();

			// If the inventory doesn't have enough room, sell shards to pouch
			player.setMoneyInPouch(
					(player.getMoneyInPouch() + ((long) amount * ItemDefinition.forId(18016).getValue())));
			player.getInventory().delete(18016, amount);
			player.getPacketSender()
					.sendMessage("You have sold <col=ff0000>" + Misc.formatAmount(amount)
							+ "</col> Spirit shards for <col=ff0000>"
							+ Misc.formatAmount((long) amount * ItemDefinition.forId(18016).getValue())
							+ "</col> coins to your money pouch.");
			player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
		} else {
			if (!player.getInventory().hasRoomFor(995, amount)) {
				player.getPacketSender().sendMessage("You do not have enough inventory space to hold <col=ff0000>"
						+ Misc.formatAmount(amount) + "</col> coins.");
				return;
			}
			player.getPacketSender().sendInterfaceRemoval();

			// If inventory can hold the coins, sell the spirit shards for coins
			// to inventory
			player.getInventory().delete(18016, amount);
			player.getInventory().add(995, amount * ItemDefinition.forId(18016).getValue());
			player.getPacketSender()
					.sendMessage("You have sold <col=ff0000>" + Misc.formatAmount(amount)
							+ "</col> Spirit shards for <col=ff0000>"
							+ Misc.formatAmount((long) amount * ItemDefinition.forId(18016).getValue())
							+ "</col> coins to your inventory.");
		}
	}

}
