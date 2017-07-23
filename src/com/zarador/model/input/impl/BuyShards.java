package com.zarador.model.input.impl;

import com.zarador.model.definitions.ItemDefinition;
import com.zarador.model.input.EnterAmount;
import com.zarador.util.Misc;
import com.zarador.world.entity.impl.player.Player;

/**
 * Handles purchasing spirit shards
 * 
 * @Jonathan Sirens
 **/

public class BuyShards extends EnterAmount {

	@Override
	public void handleAmount(Player player, long value) {
		int amount = (int) value;
		if (value > Integer.MAX_VALUE) {
			amount = Integer.MAX_VALUE;
		}
		if (amount < 1) {
			player.getPacketSender().sendMessage("You can't buy this amount of Spirit shards.");
			return;
		}
		if (player.getMoneyInPouch() < 0) {
			player.getPacketSender().sendMessage("Your money pouch is negative.");
			return;
		}
		// Sets the amount available to purchase from pouch to whatever your
		// inventory actually can hold
		if (player.getInventory().getAmount(18016) + (long) amount > Integer.MAX_VALUE) {
			amount = Integer.MAX_VALUE - player.getInventory().getAmount(18016);
		}

		if (player.getMoneyInPouch() >= (long) amount * ItemDefinition.forId(18016).getValue()) {
			player.getPacketSender().sendInterfaceRemoval();

			// If Money Pouch has enough money, purchase the spirit shards from
			// cash in money pouch
			player.setMoneyInPouch(
					(player.getMoneyInPouch() - ((long) amount * ItemDefinition.forId(18016).getValue())));
			player.getInventory().add(18016, amount);
			player.getPacketSender()
					.sendMessage("You have purchased <col=ff0000>" + Misc.formatAmount(amount)
							+ "</col> Spirit shards for <col=ff0000>"
							+ Misc.formatAmount((long) amount * ItemDefinition.forId(18016).getValue())
							+ "</col> coins from your money pouch.");
			player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
		} else if (player.getInventory().getAmount(995) >= (long) amount * ItemDefinition.forId(18016).getValue()) {
			if (!player.getInventory().hasRoomFor(18016, amount)) {
				player.getPacketSender().sendMessage("You do not have enough inventory space to hold <col=ff0000>"
						+ Misc.formatAmount(amount) + "</col> Spirit shards.");
				return;
			}
			player.getPacketSender().sendInterfaceRemoval();

			// If Inventory has enough money, purchase the spirit shards from
			// cash in inventory
			player.getInventory().delete(995, amount * ItemDefinition.forId(18016).getValue());
			player.getInventory().add(18016, amount);
			player.getPacketSender()
					.sendMessage("You have purchased <col=ff0000>" + Misc.formatAmount(amount)
							+ "</col> Spirit shards for <col=ff0000>"
							+ Misc.formatAmount((long) amount * ItemDefinition.forId(18016).getValue())
							+ "</col> coins from your inventory.");
		} else {
			player.getPacketSender()
					.sendMessage("You need <col=ff0000>"
							+ Misc.formatAmount((long) amount * ItemDefinition.forId(18016).getValue())
							+ "</col> in order to purchase these Spirit shards.");
		}
	}
}
