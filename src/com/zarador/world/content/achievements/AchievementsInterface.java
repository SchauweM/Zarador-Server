package com.zarador.world.content.achievements;

import com.zarador.world.entity.impl.player.Player;

/**
 * Handles all stuff for the teleporting interface
 * @Author Michaël
 */
public class AchievementsInterface {

	/**
	 * Open the interface depending on which 'category'
	 * @param player
	 * @param category
	 */
	public static void showInterface(Player player, String category) {
		player.setAchievementCategory(category);
		switch(category) {
			case "skilling":
				player.getPacketSender().sendInterface(61000);
				break;
			case "combat":
				player.getPacketSender().sendInterface(61000);
				break;
			case "distractions":
				player.getPacketSender().sendInterface(61000);
				break;
		}
	}

}
