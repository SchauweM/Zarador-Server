package com.zarador.world.content.skill;

import com.zarador.model.Animation;
import com.zarador.model.Graphic;
import com.zarador.model.Item;
import com.zarador.model.Skill;
import com.zarador.world.content.achievements.Achievements;
import com.zarador.world.entity.impl.player.Player;

/**
 * Handles enchanting of interfaces calculations such as combat level and total
 * level.
 * 
 * @author Jonathan Sirens
 * @author High105
 */

public class Enchanting {

	enum Data {

		OPAL(-16530, 4, 49009, 879, 9236, new Item[] { new Item(564, 1), new Item(556, 2) }, 9),
		SAPPHIRE(-16521, 7, 49017, 9337, 9240, new Item[] { new Item(564, 2), new Item(555, 4) }, 17),
		JADE(-16513, 14, 49025, 9335, 9237, new Item[] { new Item(564, 3), new Item(557, 6) }, 19),
		PEARL(-16505, 24, 49033, 880, 9238, new Item[] { new Item(564, 4), new Item(555, 8) }, 29),
		EMERALD(-16497, 27, 49041, 9338, 9241, new Item[] { new Item(564, 5), new Item(561, 5) }, 37),
		RED_TOPAZ(-16489, 29, 49049, 9336, 9239, new Item[] { new Item(564, 6), new Item(554, 12) }, 33),
		RUBY(-16481, 49, 49057, 9339, 9242, new Item[] { new Item(565, 5), new Item(9075, 50) }, 59),
		DIAMOND(-16473, 57, 49065, 9340, 9243, new Item[] { new Item(563, 5), new Item(9075, 75) }, 67),
		DRAGONSTONE(-16465, 68, 49073, 9341, 9244, new Item[] { new Item(566, 5), new Item(9075, 100) }, 78),
		ONYX(-16457, 87, 49081, 9342, 9245, new Item[] { new Item(560, 10), new Item(9075, 115) }, 97);

		private int buttonId, levelRequired, stringId, regularBoltId, enchantedBoltId, experience;

		private Item[] items_required;

		Data(int buttonId, int levelRequired, int stringId, int regularId, int enchantedId, Item[] req, int xp) {
			this.buttonId = buttonId;
			this.levelRequired = levelRequired;
			this.stringId = stringId;
			this.regularBoltId = regularId;
			this.enchantedBoltId = enchantedId;
			this.items_required = req;
			this.experience = xp;
		}

		public int getButtonId() {
			return buttonId;
		}

		public int getRegularBoltId() {
			return regularBoltId;
		}

		public int getEnchantedBoltId() {
			return enchantedBoltId;
		}

		public int getLevelRequired() {
			return levelRequired;
		}

		public int getStringId() {
			return stringId;
		}

		public int getExperience() {
			return experience;
		}

		public Item[] getRequiredItems() {
			return items_required;
		}
	}

	public static boolean enchantButtons(Player player, int buttonId) {
		int magicLevel = player.getSkillManager().getCurrentLevel(Skill.MAGIC);
		for (Data d : Data.values()) {
			if (d.getButtonId() == buttonId) {
				if (magicLevel >= d.getLevelRequired()) {
					if (player.getInventory().contains(d.getRegularBoltId())
							&& player.getInventory().getAmount(d.getRegularBoltId()) >= 10) {
						if (player.getInventory().contains(d.getRequiredItems())) {
							player.getInventory().deleteItemSet(d.getRequiredItems());
							player.getInventory().delete(new Item(d.getRegularBoltId(), 10));
							player.performAnimation(new Animation(4462));
							player.performGraphic(new Graphic(759));
							player.getInventory().add(new Item(d.getEnchantedBoltId(), 10));
							player.getSkillManager().addSkillExperience(Skill.MAGIC, d.getExperience());
							Achievements.doProgress(player, Achievements.AchievementData.ENCHANT_1000_BOLTS, 10);
						} else {
							player.getPacketSender().sendMessage("You do not have the required runes for this spell.");
						}
					} else {
						player.getPacketSender().sendMessage("You do not have the required bolts to enchant!");
					}
				} else {
					player.getPacketSender().sendMessage(
							"You need a magic level of " + d.getLevelRequired() + " to enchant these bolts.");
				}

				return true;
			}
		}

		return false;
	}

	public static void update_interface(Player player) {
		player.getPacketSender().sendInterface(49000);
		int magic_level = player.getSkillManager().getCurrentLevel(Skill.MAGIC);
		for (Data d : Data.values()) {
			if (magic_level < d.getLevelRequired()) {
				player.getPacketSender().sendString(d.getStringId(), "@red@Magic " + d.getLevelRequired());
			}
		}
	}

}
