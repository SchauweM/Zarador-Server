package com.zarador.world.content.skill.impl.woodcutting;

import com.zarador.engine.task.Task;
import com.zarador.engine.task.TaskManager;
import com.zarador.model.Animation;
import com.zarador.model.GameObject;
import com.zarador.model.Item;
import com.zarador.model.Skill;
import com.zarador.model.container.impl.Equipment;
import com.zarador.model.definitions.ItemDefinition;
import com.zarador.util.Misc;
import com.zarador.world.World;
import com.zarador.world.content.CustomObjects;
import com.zarador.world.content.EvilTrees;
import com.zarador.world.content.Sounds;
import com.zarador.world.content.Sounds.Sound;
import com.zarador.world.content.achievements.Achievements;
import com.zarador.world.content.achievements.Achievements.AchievementData;
import com.zarador.world.content.skill.impl.firemaking.Logdata;
import com.zarador.world.content.skill.impl.firemaking.Logdata.logData;
import com.zarador.world.content.skill.impl.woodcutting.WoodcuttingData.Hatchet;
import com.zarador.world.content.skill.impl.woodcutting.WoodcuttingData.Trees;
import com.zarador.world.entity.impl.player.Player;

public class Woodcutting {

	public static void rollPet(Player player) {
		int PET_ID = 12487;
		int PET_CHANCE = Misc.inclusiveRandom(1, 25_000);
		if (PET_CHANCE == 1 && player.getInventory().getFreeSlots() >= 1) {
			player.getInventory().add(PET_ID, 1);
			player.getPacketSender()
					.sendMessage("You hit the tree with a strong force, that a baby raccoon fell into your inventory!");
			World.sendMessage("<icon=1><shad=FF8C38> " + player.getUsername() + " has just received "
					+ ItemDefinition.forId(PET_ID).getName() + " from the Woodcutting skill!");
		} else if (PET_CHANCE == 1 && player.getInventory().isFull()) {
			player.getBank(player.getCurrentBankTab()).add(PET_ID, 1);
			player.getPacketSender()
					.sendMessage("You hit the tree with a strong force, that a baby raccoon ran to the bank!");
			World.sendMessage("<icon=1><shad=FF8C38> " + player.getUsername() + " has just received "
					+ ItemDefinition.forId(PET_ID).getName() + " from the Woodcutting skill!");
		}
	}

	public static void lumberJackOutfit(Player player) {
		int[] outfitIds = { 10933, 10939, 10940, 10941 };
		int CHANCE = Misc.inclusiveRandom(1, 250);
		if (CHANCE == 1) {
			int random = Misc.inclusiveRandom(3);
			if (player.getInventory().getFreeSlots() > 1) {
				player.getInventory().add(outfitIds[random], 1);
				player.getPacketSender().sendMessage("You have just had a " + new Item(outfitIds[random]).getDefinition().getName() + " added to your inventory.");
			} else {
				player.getBank(0).add(outfitIds[random], 1);
				player.getPacketSender().sendMessage("You have just had a " + new Item(outfitIds[random]).getDefinition().getName() + " added to your bank.");
			}
		}
	}

	public static void cutWood(final Player player, final GameObject object, boolean restarting) {
		if (!restarting)
			player.getSkillManager().stopSkilling();
		if (player.getInventory().getFreeSlots() == 0) {
			player.getPacketSender().sendMessage("You don't have enough free inventory space.");
			return;
		}
		player.setPositionToFace(object.getPosition());
		final int objId = object.getId();
		final Hatchet h = Hatchet.forId(WoodcuttingData.getHatchet(player));
		if (h != null) {
			if (player.getSkillManager().getCurrentLevel(Skill.WOODCUTTING) >= h.getRequiredLevel()) {
				final WoodcuttingData.Trees t = WoodcuttingData.Trees.forId(objId);
				if (t != null) {
					player.setEntityInteraction(object);
					if (player.getSkillManager().getCurrentLevel(Skill.WOODCUTTING) >= t.getReq()) {
						player.performAnimation(new Animation(h.getAnim()));
						int delay = Misc.getRandom(t.getTicks() - WoodcuttingData.getChopTimer(player, h)) + 1;
						player.setCurrentTask(new Task(1, player, false) {
							int cycle = 0, reqCycle = delay >= 2 ? delay : Misc.getRandom(1) + 1;

							@Override
							public void execute() {
								if (player.getInventory().getFreeSlots() == 0) {
									player.performAnimation(new Animation(65535));
									player.getPacketSender().sendMessage("You don't have enough free inventory space.");
									this.stop();
									return;
								}
								if (cycle != reqCycle) {
									cycle++;
									player.performAnimation(new Animation(h.getAnim()));
								} else if (cycle >= reqCycle) {
									double xp = t.getXp();
									if (lumberJack(player))
										xp *= 1.5;
									if(infernalAxe(player))
										xp *= 2.0;
									player.getSkillManager().addSkillExperience(Skill.WOODCUTTING, xp);
									cycle = 0;
									BirdNests.dropNest(player);
									//rollPet(player);
									lumberJackOutfit(player);
									this.stop();
									if (object.getId() == 11434) {
										if (EvilTrees.SPAWNED_TREE == null || EvilTrees.SPAWNED_TREE.getTreeObject()
												.getCutAmount() >= EvilTrees.MAX_CUT_AMOUNT) {
											player.getPacketSender().sendClientRightClickRemoval();
											player.getSkillManager().stopSkilling();
											return;
										} else {
											EvilTrees.SPAWNED_TREE.getTreeObject().incrementCutAmount();
										}
									} else {
										player.performAnimation(new Animation(65535));
									}
									if (!t.isMulti() || Misc.getRandom(10) == 2) {
										treeRespawn(player, object);
										player.getPacketSender().sendMessage("You've chopped the tree down.");
										player.performAnimation(new Animation(65535));
										//rollPet(player);
									} else {
										cutWood(player, object, true);
										player.getPacketSender().sendMessage("You get some logs..");
										//rollPet(player);
									}
									if (t == Trees.MAPLE) {
										Achievements.doProgress(player, AchievementData.CHOP_250_MAPLE_LOGS);
									}
									if (t == Trees.YEW || t == Trees.RESOURCE_YEW) {
										Achievements.doProgress(player, AchievementData.CHOP_750_YEW_LOGS);
									}
									if (t == Trees.MAGIC || t == Trees.RESOURCE_MAGIC) {
										Achievements.doProgress(player, AchievementData.CHOP_2500_MAGIC_LOGS);
									}
									if (t == Trees.WILLOW) {
										Achievements.finishAchievement(player, AchievementData.CHOP_WILLOW);
									}
									Sounds.sendSound(player, Sound.WOODCUT);
									if(infernalAxe(player)) {
										player.getInventory().add(t.getReward(), 2);
										return;
									} else if (!(infernoAdze(player) && Misc.getRandom(5) <= 2)) {
										player.getInventory().add(t.getReward(), 1);
									} else if (Misc.getRandom(5) <= 2) {
										logData fmLog = Logdata.getLogData(player, t.getReward());
										if (fmLog != null) {
											player.getSkillManager().addExactExperience(Skill.FIREMAKING, fmLog.getXp(), true);
											player.getPacketSender().sendMessage(
													"Your Inferno Adze burns the log, granting you Firemaking experience.");
											if (fmLog == Logdata.logData.MAGIC) {
												Achievements.doProgress(player, AchievementData.BURN_2500_MAGIC_LOGS);
											}
											if (fmLog == Logdata.logData.MAPLE) {
												Achievements.doProgress(player, AchievementData.BURN_200_MAPLE_LOGS);
											}
											if (fmLog == Logdata.logData.YEW) {
												Achievements.doProgress(player, AchievementData.BURN_500_YEW_LOGS);
											}
											if (fmLog == logData.WILLOW) {
												Achievements.finishAchievement(player, AchievementData.BURN_WILLOW);
											}
										}
									}
								}
							}
						});
						TaskManager.submit(player.getCurrentTask());
					} else {
						player.getPacketSender().sendMessage(
								"You need a Woodcutting level of at least " + t.getReq() + " to cut this tree.");
					}
				}
			} else {
				player.getPacketSender()
						.sendMessage("You do not have a hatchet which you have the required Woodcutting level to use.");
			}
		} else {
			player.getPacketSender().sendMessage("You do not have a hatchet that you can use.");
		}
	}

	public static boolean lumberJack(Player player) {
		return player.getEquipment().get(Equipment.HEAD_SLOT).getId() == 10941
				&& player.getEquipment().get(Equipment.BODY_SLOT).getId() == 10939
				&& player.getEquipment().get(Equipment.LEG_SLOT).getId() == 10940
				&& player.getEquipment().get(Equipment.FEET_SLOT).getId() == 10933;
	}

	public static boolean infernoAdze(Player player) {
		return player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 13661 || player.getInventory().contains(13661);
	}

	public static boolean infernalAxe(Player player) {
		return player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 12706 || player.getInventory().contains(12706);
	}

	public static void treeRespawn(final Player player, final GameObject oldTree) {
		if (oldTree == null || oldTree.getPickAmount() >= 1)
			return;
		oldTree.setPickAmount(1);
		for (Player players : player.getLocalPlayers()) {
			if (players == null)
				continue;
			if (players.getInteractingObject() != null && players.getInteractingObject().getPosition()
					.equals(player.getInteractingObject().getPosition().copy())) {
				players.getSkillManager().stopSkilling();
				players.getPacketSender().sendClientRightClickRemoval();
			}
		}
		player.getPacketSender().sendClientRightClickRemoval();
		player.getSkillManager().stopSkilling();
		CustomObjects.globalObjectRespawnTask(new GameObject(1343, oldTree.getPosition().copy(), 10, 0), oldTree,
				20 + Misc.getRandom(10));
	}

}
