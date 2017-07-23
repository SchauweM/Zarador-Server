package com.zarador.world.content;

import org.scripts.kotlin.content.dialog.npcs.OpenScroll;

import com.zarador.model.DonatorRights;
import com.zarador.world.entity.impl.player.Player;

public class Scrolls {

	/**
	 * Handles the Donation Scrolls
	 * @Author Jonny
	 */

	public enum Scroll {

		/**
		 * $10 Scroll
		 * Gives 1,000 Donator points.
		 *
		 * @Store
		 */
		$10_SCROLL(10943, 1000, 10),

		/**
		 * $25 Scroll
		 * Gives 2,500 Donator points.
		 *
		 * @Store
		 */
		$25_SCROLL(10934, 2500, 25),

		/**
		 * $50 Scroll
		 * Gives 5,500 Donator points.
		 *
		 * @Store
		 */
		$50_SCROLL(10935, 5500, 50),

		/**
		 * $100 Scroll
		 * Gives 12,000 Donator points.
		 *
		 * @Store
		 */
		$100_SCROLL(7629, 12000, 100);

		Scroll(int itemId, int points, int claim) {
			this.itemId = itemId;
			this.points = points;
			this.claim = claim;
		}

		private int itemId;
		private int points;
		private int claim;

		/**
		 * Get the item ID for the donation scroll.
		 *
		 * @return itemId
		 */
		public int getId() {
			return this.itemId;
		}

		/**
		 * Get the amount of points for the donation scroll.
		 *
		 * @return itemId
		 */
		public int getPoints() {
			return this.points;
		}

		/**
		 * Get the amount of money claimed per scroll
		 *
		 * @return claim
		 */
		public int getClaim() {
			return this.claim;
		}

		/**
		 * Get a scroll depending on its item id.
		 * @param itemId
		 * @return
		 */
		public static Scroll forItemId(int itemId) {
			for (Scroll scrolls : Scroll.values()) {
				if (scrolls.getId() == itemId) {
					return scrolls;
				}
			}
			return null;
		}
	}

	/**
	 * Update your donator rank depending on how much you have donated.
	 * @param player
	 */
	public static void updateRank(Player player) {
		int donated = player.getAmountDonated();
		if (donated >= 1000) {
			if (player.getDonatorRights() != DonatorRights.PLATINUM) {
				player.setDonatorRights(DonatorRights.PLATINUM);
				player.getPacketSender().sendMessage(DonatorRights.PLATINUM.getColor() + DonatorRights.PLATINUM.getShad() + "Congratulations! You are now a Platinum Donator.");
			}
		} else if (donated >= 500) {
			if (player.getDonatorRights() != DonatorRights.UBER) {
				player.setDonatorRights(DonatorRights.UBER);
				player.getPacketSender().sendMessage(DonatorRights.UBER.getColor() + DonatorRights.UBER.getShad() + "Congratulations! You are now a Uber Donator.");
			}
		} else if (donated >= 150) {
			if (player.getDonatorRights() != DonatorRights.LEGENDARY) {
				player.setDonatorRights(DonatorRights.LEGENDARY);
				player.getPacketSender().sendMessage(DonatorRights.LEGENDARY.getColor() + DonatorRights.LEGENDARY.getShad() + "Congratulations! You are now a Legendary Donator.");
			}
		} else if (donated >= 50) {
			if (player.getDonatorRights() != DonatorRights.EXTREME) {
				player.setDonatorRights(DonatorRights.EXTREME);
				player.getPacketSender().sendMessage(DonatorRights.EXTREME.getColor() + DonatorRights.EXTREME.getShad() + "Congratulations! You are now a Extreme Donator.");
			}
		} else if (donated >= 10) {
			if (player.getDonatorRights() != DonatorRights.PREMIUM) {
				player.setDonatorRights(DonatorRights.PREMIUM);
				player.getPacketSender().sendMessage(DonatorRights.PREMIUM.getColor() + DonatorRights.PREMIUM.getShad() + "Congratulations! You are now a Premium Donator.");
			}
		}
	}

	/**
	 * Use a donation scroll and get your points, then send the dialog.
	 * @param player
	 * @param itemId
	 */
	public static void useScroll(Player player, int itemId) {
		Scroll scroll = Scroll.forItemId(itemId);
		if (scroll == null) {
			player.getPacketSender().sendMessage("Scroll error: Contact staff, error: " + itemId);
			return;
		}
		if (player.getInventory().contains(scroll.getId())) {
			player.setPoints(scroll.getPoints(), true);
			player.incrementAmountDonated(scroll.getClaim());
			player.getInventory().delete(scroll.getId(), 1);
			Scrolls.updateRank(player);
			PlayerPanel.refreshPanel(player);
			player.save();
			player.setNpcClickId(945);
			player.getDialog().sendDialog(new OpenScroll(player, 1, itemId));
		} else {
			player.getPacketSender().sendMessage("You do not have a scroll to open.");
		}
	}
}