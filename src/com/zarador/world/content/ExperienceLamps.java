package com.zarador.world.content;

import com.zarador.model.Skill;
import com.zarador.model.player.GameMode;
import com.zarador.util.Misc;
import com.zarador.world.entity.impl.player.Player;

public class ExperienceLamps {

	public static boolean handleLamp(Player player, int item) {
		LampData lamp = LampData.forId(item);
		if (lamp == null)
			return false;
		if (player.getInterfaceId() > 0) {
			player.getPacketSender().sendMessage("Please close the interface you have open before doing this.");
		} else {
			player.getPacketSender().sendString(38006, "Choose XP type...").sendString(38090,
					"What sort of XP would you like?");
			player.getPacketSender().sendInterface(38000);
			player.setUsableObject(new Object[3]).setUsableObject(0, "xp").setUsableObject(2, lamp);
		}
		return true;
	}

	public static boolean handleButton(Player player, int button) {
		if (button == -27451) {
			try {
				player.getPacketSender().sendInterfaceRemoval();
				player.getPacketSender().sendString(38006, "Choose XP type...");
				if (player.getUsableObject()[0] != null) {
					Skill skill = (Skill) player.getUsableObject()[1];
					switch (((String) player.getUsableObject()[0]).toLowerCase()) {
					case "reset":
						player.getSkillManager().resetSkill(skill, false);
						break;
					case "xp":
						LampData lamp = (LampData) player.getUsableObject()[2];
						if (!player.getInventory().contains(lamp.getItemId()))
							return true;
						int maxLvl = player.getSkillManager().getMaxLevel(skill);
						if(maxLvl < 90 && lamp == LampData.DRAGONKIN_LAMP) {
							player.getPacketSender().sendMessage("You must have atleast level 90 in this skill.");
							return true;
						}
						int exp = getExperienceReward(player, lamp, skill);
						player.getInventory().delete(lamp.getItemId(), 1);
						player.getSkillManager().addExactExperience(skill, exp, false);
						player.getPacketSender().sendMessage("You've received some experience in "
								+ Misc.formatText(skill.toString().toLowerCase()) + ".");
						break;
					}
				}
			} catch (Exception e) {
			}
			return true;
		} else {
			Interface_Buttons interfaceButton = Interface_Buttons.forButton(button);
			if (interfaceButton == null)
				return false;
			if (interfaceButton == Interface_Buttons.CONSTRUCTION) {
				player.getPacketSender().sendMessage("That skill is not trainable yet.");
				return true;
			}
			Skill skill = Skill.forName(interfaceButton.toString());
			if (skill == null)
				return true;
			player.setUsableObject(1, skill);
			player.getPacketSender().sendString(38006, Misc.formatText(interfaceButton.toString().toLowerCase()));
		}
		return false;
	}

	enum LampData {
		LAMP_10K(11137, 10000),
		LAMP_25K(11139, 25000),
		LAMP_50K(11141, 50000),
		LAMP_100K(11185, 100000),
		LAMP_500K(11186, 500000),
		LAMP_1M(11187, 1000000),
		LAMP_2M(11188, 2000000),
		DRAGONKIN_LAMP(18782, 2000000);

		LampData(int itemId, int experience) {
			this.itemId = itemId;
			this.experience = experience;
		}

		private int itemId;

		private int experience;

		public int getItemId() {
			return this.itemId;
		}

		public int getExperience() {
			return this.experience;
		}

		public static LampData forId(int id) {
			for (LampData lampData : LampData.values()) {
				if (lampData != null && lampData.getItemId() == id)
					return lampData;
			}
			return null;
		}
	}

	enum Interface_Buttons {

		ATTACK(-27529), MAGIC(-27526), MINING(-27523), WOODCUTTING(-27520), AGILITY(-27517), FLETCHING(
				-27514), THIEVING(-27511), STRENGTH(-27508), RANGED(-27505), SMITHING(-27502), FIREMAKING(
						-27499), HERBLORE(-27496), SLAYER(-27493), CONSTRUCTION(-27490), DEFENCE(-27487), PRAYER(
								-27484), FISHING(-27481), CRAFTING(-27478), FARMING(-27475), HUNTER(-27472), SUMMONING(
										-27469), CONSTITUTION(
												-27466), DUNGEONEERING(-27463), COOKING(-27460), RUNECRAFTING(-27457);

		Interface_Buttons(int button) {
			this.button = button;
		}

		private int button;

		public static Interface_Buttons forButton(int button) {
			for (Interface_Buttons skill : Interface_Buttons.values()) {
				if (skill != null && skill.button == button) {
					return skill;
				}
			}
			return null;
		}
	}

	public static int getExperienceReward(Player player, LampData lamp, Skill skill) {
		int base = lamp.getExperience();
		int experience = player.getSkillManager().getExperience(skill);
		if(experience >= 13000000) {
			experience = 13000000;
		}
		if(lamp == LampData.DRAGONKIN_LAMP) {
			base = experience / 7;
			if(player.getGameModeAssistant().getGameMode() == GameMode.REALISM || player.getGameModeAssistant().getGameMode() == GameMode.IRONMAN) {
				base /= 10;
			}
		}
		return base;
	}

	public static boolean selectingExperienceReward(Player player) {
		return player.getInterfaceId() == 38000;
	}
}
