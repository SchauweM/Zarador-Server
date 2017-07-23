package com.zarador.world.content.skill.impl.summoning;

import java.util.concurrent.TimeUnit;

import com.zarador.util.Misc;
import com.zarador.world.entity.impl.player.Player;

public class SummoningTab {

	public static void handleDismiss(Player c, boolean dismiss) {
		if (!dismiss && c.busy()) {
			c.getPacketSender().sendMessage("Please finish what you're doing first.");
			return;
		}
		c.getPacketSender().sendInterfaceRemoval();
		if (dismiss) {
			if (c.getSummoning().getFamiliar() != null) {
				BossPets.BossPet pet = BossPets.BossPet.forId(c.getSummoning().getFamiliar().getSummonNpc().getId());
				if (pet == null) {
					c.getSummoning().unsummon(true, true);
					c.getPacketSender().sendMessage("You've dismissed your familiar.");
				} else {
					c.getPacketSender().sendMessage("You must pick up your boss pet, not dismiss!");
				}
			} else {
				c.getPacketSender().sendMessage("You don't have a familiar to dismiss.");
			}
		}
	}

	public static void callFollower(final Player c) {
		if (c.getSummoning().getFamiliar() != null && c.getSummoning().getFamiliar().getSummonNpc() != null) {
			if (!c.getLastSummon().elapsed(30000)) {
				c.getPacketSender()
						.sendMessage("You must wait another "
								+ Misc.getTimeLeft(c.getLastSummon().elapsed(), 30, TimeUnit.SECONDS)
								+ " seconds before being able to do this again.");
				return;
			}
			c.getSummoning().moveFollower(true);
		} else {
			c.getPacketSender().sendMessage("You don't have a familiar to call.");
		}
	}

	public static void renewFamiliar(Player c) {
		if (c.getSummoning().getFamiliar() != null) {
			int pouchRequired = FamiliarData.forNPCId(c.getSummoning().getFamiliar().getSummonNpc().getId())
					.getPouchId();
			if (c.getInventory().contains(pouchRequired)) {
				c.getSummoning().summon(FamiliarData.forNPCId(c.getSummoning().getFamiliar().getSummonNpc().getId()),
						true, false);
			} else {
				c.getPacketSender().sendMessage("You don't have the pouch required to renew this familiar.");
			}
		} else {
			c.getPacketSender().sendMessage("You don't have a familiar to renew.");
		}
	}
}
