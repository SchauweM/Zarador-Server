package com.zarador.world.content.skill.impl.runecrafting;

import com.zarador.model.*;
import com.zarador.model.container.impl.Equipment;
import com.zarador.world.content.achievements.Achievements;
import com.zarador.world.content.achievements.Achievements.AchievementData;
import com.zarador.world.content.skill.impl.runecrafting.RunecraftingData.RuneData;
import com.zarador.world.content.skill.impl.runecrafting.RunecraftingData.TalismanData;
import com.zarador.world.content.transportation.TeleportHandler;
import com.zarador.world.entity.impl.player.Player;

/**
 * Handles the Runecrafting skill
 * 
 * @author Gabriel Hannason
 */
public class Runecrafting {

	public static void craftRunes(final Player player, RunecraftingData.RuneData rune) {
		if (!canRuneCraft(player, rune))
			return;
		int essence = -1;
		if (player.getInventory().contains(1436) && !rune.pureRequired())
			essence = 1436;
		if (player.getInventory().contains(7936) && essence < 0)
			essence = 7936;
		if (essence == -1)
			return;
		player.performGraphic(new Graphic(186));
		player.performAnimation(new Animation(791));
		int amountToMake = RunecraftingData.getMakeAmount(rune, player);
		int amountMade = 0;
		for (int i = 28; i > 0; i--) {
			if (!player.getInventory().contains(essence))
				break;
			player.getInventory().delete(essence, 1);
			player.getInventory().add(rune.getRuneID(), amountToMake);
			amountMade += amountToMake;
			player.getSkillManager().addSkillExperience(Skill.RUNECRAFTING, rune.getXP() * Runecrafting.getRunecraftingBoost(player));
		}
		if (rune == RuneData.NATURE_RUNE) {
			Achievements.doProgress(player, AchievementData.RUNECRAFT_500_NATS, amountMade);
		}
		if (rune == RuneData.BLOOD_RUNE) {
			Achievements.doProgress(player, AchievementData.RUNECRAFT_6000_BLOOD_RUNES, amountMade);
		}
		player.performGraphic(new Graphic(129));
		player.getSkillManager().addSkillExperience(Skill.RUNECRAFTING, rune.getXP() * Runecrafting.getRunecraftingBoost(player));
		player.getPacketSender().sendMessage("You bind the altar's power into " + rune.getName() + "s..");
		Achievements.finishAchievement(player, AchievementData.RUNECRAFT_RUNES);
		player.getClickDelay().reset();
	}

	public static void handleTalisman(Player player, int ID) {
		TalismanData talisman = RunecraftingData.TalismanData.forId(ID);
		if (talisman == null)
			return;
		if (player.getSkillManager().getMaxLevel(Skill.RUNECRAFTING) < talisman.getLevelRequirement()) {
			player.getPacketSender().sendMessage("You need a Runecrafting level of at least "
					+ talisman.getLevelRequirement() + " to use this Talisman's teleport function.");
			return;
		}
		Position targetLocation = talisman.getLocation();
		TeleportHandler.teleportPlayer(player, targetLocation, player.getSpellbook().getTeleportType());
	}

	public static boolean canRuneCraft(Player player, RunecraftingData.RuneData rune) {
		if (rune == null)
			return false;
		if (player.getSkillManager().getMaxLevel(Skill.RUNECRAFTING) < rune.getLevelRequirement()) {
			player.getPacketSender().sendMessage(
					"You need a Runecrafting level of at least " + rune.getLevelRequirement() + " to craft this.");
			return false;
		}
		if (rune.pureRequired() && !player.getInventory().contains(7936) && !player.getInventory().contains(1436)) {
			player.getPacketSender().sendMessage("You do not have any Pure essence in your inventory.");
			return false;
		} else if (rune.pureRequired() && !player.getInventory().contains(7936)
				&& player.getInventory().contains(1436)) {
			player.getPacketSender().sendMessage("Only Pure essence has the power to bind this altar's energy.");
			return false;
		}
		if (!player.getInventory().contains(7936) && !player.getInventory().contains(1436)) {
			player.getPacketSender().sendMessage("You do not have any Rune or Pure essence in your inventory.");
			return false;
		}
		if (!player.getClickDelay().elapsed(4500))
			return false;
		return true;
	}

	public static boolean runecraftingAltar(Player player, int ID) {
		return ID >= 2478 && ID < 2489 || ID == 17010 || ID == 30624 || ID == 47120;
	}

	/**
	 * Get your runecrafting xp boost
	 * @return
	 */
	public static double getRunecraftingBoost(Player player) {
		double boost = 1;
		if(player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 13613) {
			boost += .25;
		}
		if(player.getEquipment().getItems()[Equipment.BODY_SLOT].getId() == 13619) {
			boost += .25;
		}
		if(player.getEquipment().getItems()[Equipment.LEG_SLOT].getId() == 13622) {
			boost += .25;
		}
		if(player.getEquipment().getItems()[Equipment.HANDS_SLOT].getId() == 13623) {
			boost += .25;
		}
		return boost;
	}

}
