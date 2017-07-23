package com.zarador.world.content.skill.impl.prayer;

import com.zarador.engine.task.Task;
import com.zarador.engine.task.TaskManager;
import com.zarador.model.Animation;
import com.zarador.model.Item;
import com.zarador.model.Skill;
import com.zarador.model.definitions.ItemDefinition;
import com.zarador.world.content.Sounds;
import com.zarador.world.content.Sounds.Sound;
import com.zarador.world.content.achievements.Achievements;
import com.zarador.world.content.achievements.Achievements.AchievementData;
import com.zarador.world.entity.impl.player.Player;

/**
 * The prayer skill is based upon burying the corpses of enemies. Obtaining a
 * higher level means more prayer abilities being unlocked, which help out in
 * combat.
 * 
 * @author Gabriel Hannason
 */

public class Prayer {

	public static boolean isBone(int bone) {
		return BonesData.forId(bone) != null;
	}

	public static void buryBone(final Player player, final int itemId) {
		if (!player.getClickDelay().elapsed(2000))
			return;
		final BonesData currentBone = BonesData.forId(itemId);
		if (currentBone == null)
			return;
		if(ItemDefinition.forId(itemId).getName().contains("ashes")) {
			player.getSkillManager().stopSkilling();
			player.getPacketSender().sendInterfaceRemoval();
			player.performAnimation(new Animation(2292));
			/*switch(BonesData.forId(itemId)) {
				case IMPIOUS_ASHES:
					player.performGraphic(new Graphic(56));
					break;
				case ACCURSED_ASHES:
					player.performGraphic(new Graphic(47));
					break;
				case INFERNAL_ASHES:
					player.performGraphic(new Graphic(40));
					break;
			}*/
			player.getPacketSender().sendMessage("You scatter the ashes...");
			final Item bone = new Item(itemId);
			player.getInventory().delete(bone);
			player.getSkillManager().addSkillExperience(Skill.PRAYER, currentBone.getBuryingXP());
			return;
		}
		player.getSkillManager().stopSkilling();
		player.getPacketSender().sendInterfaceRemoval();
		player.performAnimation(new Animation(827));
		player.getPacketSender().sendMessage("You dig a hole in the ground..");
		final Item bone = new Item(itemId);
		player.getInventory().delete(bone);
		TaskManager.submit(new Task(3, player, false) {
			@Override
			public void execute() {
				player.getPacketSender().sendMessage("..and bury the " + bone.getDefinition().getName() + ".");
				player.getSkillManager().addSkillExperience(Skill.PRAYER, currentBone.getBuryingXP());
				Sounds.sendSound(player, Sound.BURY_BONE);
				if (currentBone == BonesData.BIG_BONES) {
					Achievements.finishAchievement(player, AchievementData.BURY_BIG_BONE);
				} else if (currentBone == BonesData.DRAGON_BONES) {
						Achievements.doProgress(player, AchievementData.BURY_50_DRAGON_BONES);
				} else if (currentBone == BonesData.FROSTDRAGON_BONES) {
					Achievements.doProgress(player, AchievementData.BURY_25_FROST_DRAGON_BONES);
					Achievements.doProgress(player, AchievementData.BURY_500_FROST_DRAGON_BONES);
				}
				stop();
			}
		});
		player.getClickDelay().reset();
	}

	public static void crushBone(final Player player, final int itemId) {
		final BonesData currentBone = BonesData.forId(itemId);
		if (currentBone == null)
			return;
		if(ItemDefinition.forId(itemId).getName().contains("ashes")) {
			player.getSkillManager().stopSkilling();
			player.getPacketSender().sendInterfaceRemoval();
			final Item bone = new Item(itemId);
			player.getInventory().delete(bone);
			player.getSkillManager().addSkillExperience(Skill.PRAYER, currentBone.getBuryingXP());
			return;
		}
		else if (currentBone == BonesData.FROSTDRAGON_BONES) {
			Achievements.doProgress(player, AchievementData.BURY_25_FROST_DRAGON_BONES);
			Achievements.doProgress(player, AchievementData.BURY_500_FROST_DRAGON_BONES);
		}
		if (currentBone == BonesData.DRAGON_BONES) {
			Achievements.doProgress(player, AchievementData.BURY_50_DRAGON_BONES);
		}
		player.getSkillManager().addSkillExperience(Skill.PRAYER, currentBone.getBuryingXP());
		player.getPacketSender().sendMessage("Your bonecrusher has crushed "+ItemDefinition.forId(itemId).getName()+" for "+currentBone.getBuryingXP() * player.getGameModeAssistant().getModeExpRate()+" prayer experience.");
	}
}
