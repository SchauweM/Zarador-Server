package com.zarador.world.content;

import com.zarador.model.Flag;
import com.zarador.util.Misc;
import com.zarador.world.entity.impl.player.Player;

public class LoyaltyProgramme {

	public enum LoyaltyTitles {

		NONE(0, 0, 0) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				return true;
			}
		},

		// New
		DONATOR(0, 43095, -22444) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				return true;
			}
		};

		private LoyaltyTitles(int cost, int frame, int button) {
			this.cost = cost;
			this.frame = frame;
			this.button = button;
		}

		private int cost;
		private int frame;
		private int button;

		abstract boolean canBuy(Player p, boolean sendMessage);

		/**
		 * Gets the rank for a certain id.
		 * 
		 * @param id
		 *            The id (ordinal()) of the rank.
		 * @return rights.
		 */
		public static LoyaltyTitles forId(int id) {
			for (LoyaltyTitles rights : LoyaltyTitles.values()) {
				if (rights.ordinal() == id) {
					return rights;
				}
			}
			return null;
		}

		public static LoyaltyTitles getTitle(int button) {
			for (LoyaltyTitles t : LoyaltyTitles.values()) {
				if (t.button == button)
					return t;
			}
			return null;
		}
	}

	public static void unlock(Player player, LoyaltyTitles title) {
		if (player.getUnlockedLoyaltyTitles()[title.ordinal()])
			return;
		player.setUnlockedLoyaltyTitle(title.ordinal());
		player.getPacketSender()
				.sendMessage("You've unlocked the " + Misc.formatText(title.name().toLowerCase()) + " loyalty title!");
	}

	public static boolean handleButton(Player player, int button) {
		LoyaltyTitles title = LoyaltyTitles.getTitle(button);
		if (title != null) {
			if (title.canBuy(player, true)) {
				if (player.getLoyaltyTitle() == title) {
					player.getPacketSender().sendMessage("You are already using this title.");
					return true;
				}
				if (player.getPointsHandler().getLoyaltyPoints() >= title.cost) {
					player.setLoyaltyTitle(title);
					player.getPointsHandler().setLoyaltyPoints(-title.cost, true);
					player.getPointsHandler().refreshPanel();
					player.getPacketSender().sendMessage("You've changed your title.");
					player.getUpdateFlag().flag(Flag.APPEARANCE);
				} else {
					player.getPacketSender()
							.sendMessage("You need at least " + title.cost + " Loyalty Points to buy this title.");
				}
			}
			return true;
		}
		return false;
	}

	public static void reset(Player player) {
		player.setLoyaltyTitle(LoyaltyTitles.NONE);
		player.getUpdateFlag().flag(Flag.APPEARANCE);
	}

	public static void incrementPoints(Player player) {
		double pts = 1;
		player.getPointsHandler().incrementLoyaltyPoints(pts);
		player.getAchievementAttributes().incrementTotalLoyaltyPointsEarned(pts);

		int totalPoints = player.getPointsHandler().getLoyaltyPoints();

		if (player.getInterfaceId() == 43000) {
			player.getPacketSender().sendString(43120, "Your Loyalty Points: " + totalPoints);
		}
		//TODO: Add loyalty points for player panel
	}
}
