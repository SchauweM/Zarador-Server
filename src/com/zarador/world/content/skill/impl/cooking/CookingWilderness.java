package com.zarador.world.content.skill.impl.cooking;

import com.zarador.engine.task.Task;
import com.zarador.engine.task.TaskManager;
import com.zarador.model.Animation;
import com.zarador.model.Skill;
import com.zarador.model.definitions.ItemDefinition;
import com.zarador.model.input.impl.EnterAmountToCook;
import com.zarador.world.content.achievements.Achievements;
import com.zarador.world.content.achievements.Achievements.AchievementData;
import com.zarador.world.entity.impl.player.Player;

public class CookingWilderness {

	public static void selectionInterface(Player player, CookingWildernessData CookingWildernessData) {
		if (CookingWildernessData == null)
			return;
		player.setSelectedSkillingItem(CookingWildernessData.getRawItem());
		player.setInputHandling(new EnterAmountToCook());
		player.getPacketSender().sendString(2799, ItemDefinition.forId(CookingWildernessData.getCookedItem()).getName())
				.sendInterfaceModel(1746, CookingWildernessData.getCookedItem(), 150).sendChatboxInterface(4429);
		player.getPacketSender().sendString(2800, "How many would you like to cook?");
	}

	public static void cook(final Player player, final int rawFish, final int amount) {
		final CookingWildernessData fish = CookingWildernessData.forFish(rawFish);
		if (fish == null)
			return;
		player.getSkillManager().stopSkilling();
		player.getPacketSender().sendInterfaceRemoval();
		if (!CookingWildernessData.canCook(player, rawFish))
			return;
		player.performAnimation(new Animation(896));
		player.setCurrentTask(new Task(2, player, false) {
			int amountCooked = 0;

			@Override
			public void execute() {
				if (!CookingWildernessData.canCook(player, rawFish)) {
					stop();
					return;
				}
				player.performAnimation(new Animation(896));
				if (ItemDefinition.forId(rawFish + 1).isNoted()) {
					player.getInventory().delete(rawFish + 1, 1);
				} else {
					player.getInventory().delete(rawFish, 1);
				}
				if (!CookingWildernessData.success(player, 3, fish.getLevelReq() - 2, fish.getStopBurn())) {
					if (ItemDefinition.forId(fish.getBurntItem() + 1).isNoted()) {
						player.getInventory().add(fish.getBurntItem() + 1, 1);
					} else {
						player.getInventory().add(fish.getBurntItem(), 1);
					}
					player.getPacketSender().sendMessage("You accidently burn the " + fish.getName() + ".");
				} else {
					if (ItemDefinition.forId(fish.getCookedItem() + 1).isNoted()) {
						player.getInventory().add(fish.getCookedItem() + 1, 1);
					} else {
						player.getInventory().add(fish.getCookedItem(), 1);
					}
					player.getSkillManager().addSkillExperience(Skill.COOKING, fish.getXp());
					if (fish == CookingWildernessData.LOBSTER) {
						Achievements.finishAchievement(player, AchievementData.COOK_LOBSTER);
					} else if (fish == CookingWildernessData.SHARK) {
						Achievements.doProgress(player, AchievementData.COOK_100_SHARKS);
					} else if (fish == CookingWildernessData.MANTA_RAY) {
						Achievements.doProgress(player, AchievementData.COOK_500_MANTA);
					} else if (fish == CookingWildernessData.ROCKTAIL) {
						Achievements.doProgress(player, AchievementData.COOK_1000_ROCKTAILS);
					}
				}
				amountCooked++;
				if (amountCooked >= amount)
					stop();
			}

			@Override
			public void stop() {
				setEventRunning(false);
				player.setSelectedSkillingItem(-1);
				player.performAnimation(new Animation(65535));
			}
		});
		TaskManager.submit(player.getCurrentTask());
	}
}
