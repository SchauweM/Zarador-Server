package com.zarador.world.content.skill.impl.prayer;

import com.zarador.engine.task.Task;
import com.zarador.engine.task.TaskManager;
import com.zarador.model.Animation;
import com.zarador.model.Graphic;
import com.zarador.model.Skill;
import com.zarador.model.container.impl.Equipment;
import com.zarador.model.definitions.ItemDefinition;
import com.zarador.model.input.impl.EnterAmountOfBonesToSacrifice;
import com.zarador.util.Misc;
import com.zarador.world.content.achievements.Achievements;
import com.zarador.world.content.achievements.Achievements.AchievementData;
import com.zarador.world.entity.impl.player.Player;

public class BonesOnAltar {

	public static void openInterface(Player player, int itemId, boolean gilded) {
		player.getSkillManager().stopSkilling();
		player.setSelectedSkillingItem(itemId);
		player.setInputHandling(new EnterAmountOfBonesToSacrifice());
		player.getPacketSender().sendString(2799, ItemDefinition.forId(itemId).getName())
				.sendInterfaceModel(1746, itemId, 150).sendChatboxInterface(4429);
		player.getPacketSender().sendString(2800, "How many would you like to offer?");
		player.setAttribute("gilded", gilded);
	}

	public static void offerBones(final Player player, final int amount) {
		final int boneId = player.getSelectedSkillingItem();
		final boolean gilded = player.getAttributes().get("gilded") != null
				? (boolean) player.getAttributes().get("gilded") : false;
		player.getSkillManager().stopSkilling();
		final BonesData currentBone = BonesData.forId(boneId);
		if (currentBone == null)
			return;
		player.getPacketSender().sendInterfaceRemoval();
		player.setCurrentTask(new Task(2, player, true) {
			int amountSacrificed = 0;

			@Override
			public void execute() {
				if (amountSacrificed >= amount) {
					stop();
					return;
				}
				if (!player.getInventory().contains(boneId)) {
					player.getPacketSender()
							.sendMessage("You have run out of " + ItemDefinition.forId(boneId).getName() + ".");
					stop();
					return;
				}
				if (player.getInteractingObject() != null) {
					player.setPositionToFace(player.getInteractingObject().getPosition().copy());
					player.getInteractingObject().performGraphic(new Graphic(624));
				}
				if (currentBone == BonesData.BIG_BONES) {
					Achievements.finishAchievement(player, AchievementData.BURY_BIG_BONE);
				}
				if (currentBone == BonesData.DRAGON_BONES) {
					Achievements.doProgress(player, AchievementData.BURY_50_DRAGON_BONES);
				}
				if (currentBone == BonesData.FROSTDRAGON_BONES) {
					Achievements.doProgress(player, AchievementData.BURY_25_FROST_DRAGON_BONES);
					Achievements.doProgress(player, AchievementData.BURY_500_FROST_DRAGON_BONES);
				}
				amountSacrificed++;
				boolean skipDelete = false;
				if(player.getEquipment().get(Equipment.HEAD_SLOT).getId() == 18744 || player.getEquipment().get(Equipment.HEAD_SLOT).getId() == 18745 || player.getEquipment().get(Equipment.HEAD_SLOT).getId() == 18746) {
					if (Misc.inclusiveRandom(1, 3) == 2) {
						skipDelete = true;
					}
				}
				if(!skipDelete) {
					player.getInventory().delete(boneId, 1);
				}
				player.performAnimation(new Animation(713));
				double experience = currentBone.getBuryingXP() * 2;
				player.getSkillManager().addSkillExperience(Skill.PRAYER, experience);
			}

			@Override
			public void stop() {
				setEventRunning(false);
				player.getPacketSender().sendMessage("You have pleased the gods with your "
						+ (amountSacrificed == 1 ? "sacrifice" : "sacrifices") + ".");
			}
		});
		TaskManager.submit(player.getCurrentTask());
	}
}
