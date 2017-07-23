package com.zarador.world.content.skill.impl.mining;

import com.zarador.engine.task.Task;
import com.zarador.engine.task.TaskManager;
import com.zarador.model.Animation;
import com.zarador.model.GameObject;
import com.zarador.model.Locations;
import com.zarador.model.Skill;
import com.zarador.model.container.impl.Equipment;
import com.zarador.model.definitions.ItemDefinition;
import com.zarador.util.MathUtil;
import com.zarador.util.Misc;
import com.zarador.world.World;
import com.zarador.world.content.CustomObjects;
import com.zarador.world.content.Sounds;
import com.zarador.world.content.Emotes.Skillcape_Data;
import com.zarador.world.content.Sounds.Sound;
import com.zarador.world.content.achievements.Achievements;
import com.zarador.world.content.achievements.Achievements.AchievementData;
import com.zarador.world.content.skill.impl.mining.MiningData.Ores;
import com.zarador.world.entity.impl.npc.NPC;
import com.zarador.world.entity.impl.player.Player;

public class Mining {

	public static void rollPet(Player player) {
		int PET_ID = 21250;
		int PET_CHANCE = Misc.inclusiveRandom(1, 25_000);
		if (PET_CHANCE == 1 && player.getInventory().getFreeSlots() >= 1) {
			player.getInventory().add(PET_ID, 1);
			player.getPacketSender()
					.sendMessage("You hit the rock with such a force, that a small chunk starts to move!");
			World.sendMessage("<icon=1><shad=FF8C38> " + player.getUsername() + " has just received "
					+ ItemDefinition.forId(PET_ID).getName() + " from the Mining skill!");
		} else if (PET_CHANCE == 1 && player.getInventory().isFull()) {
			player.getBank(player.getCurrentBankTab()).add(PET_ID, 1);
			player.getPacketSender()
					.sendMessage("You hit the rock with such a force, that a small chunk, it's moved to your bank!");
			World.sendMessage("<icon=1><shad=FF8C38> " + player.getUsername() + " has just received "
					+ ItemDefinition.forId(PET_ID).getName() + " from the Mining skill!");
		}
	}

	public static void startMining(final Player player, final GameObject oreObject, NPC npc) {
		player.getSkillManager().stopSkilling();
		player.getPacketSender().sendInterfaceRemoval();
		if (!Locations.goodDistance(player.getPosition().copy(), oreObject.getPosition(), 1)
				&& oreObject.getId() != 24444 && oreObject.getId() != 30000 && oreObject.getId() != 24445 && oreObject.getId() != 38660)
			return;
		if (player.busy() || player.getCombatBuilder().isBeingAttacked() || player.getCombatBuilder().isAttacking()) {
			player.getPacketSender().sendMessage("You cannot do that right now.");
			return;
		}
		if (player.getInventory().getFreeSlots() == 0) {
			player.getPacketSender().sendMessage("You do not have any free inventory space left.");
			return;
		}
		player.setInteractingObject(oreObject);
		player.setPositionToFace(oreObject.getPosition());
		Ores initialOre = null;
		if(oreObject.getId() == 24444 || oreObject.getId() == 24445) {
			if(player.getSkillManager().getCurrentLevel(Skill.MINING) >= 30) {
				int random = Misc.inclusiveRandom(1, 2);
				if(random == 1) {
					initialOre = Ores.RUNE_ESSENCE;
				} else {
					initialOre = Ores.PURE_ESSENCE;
				}
			} else {
				initialOre = MiningData.forRock(oreObject.getId());
			}
		} else {
			initialOre = MiningData.forRock(oreObject.getId());
		}

		final Ores o = initialOre;

		final boolean giveGem = o != Ores.RUNE_ESSENCE && o != Ores.PURE_ESSENCE && o != Ores.LIVING_ROCK_RESOURCE;
		final int reqCycle = o == Ores.RUNITE ? 6 + Misc.getRandom(2) : Misc.getRandom(o.getTicks() - 1);
		if (o != null) {
			final int pickaxe = MiningData.getPickaxe(player);
			final int miningLevel = player.getSkillManager().getCurrentLevel(Skill.MINING);
			if (pickaxe > 0) {
				if (miningLevel >= o.getLevelReq()) {
					final MiningData.Pickaxe p = MiningData.forPick(pickaxe);
					if (miningLevel >= p.getReq()) {
						player.performAnimation(new Animation(p.getAnim()));
						final int delay = o.getTicks() - MiningData.getReducedTimer(player, p) + 1;
						player.setCurrentTask(new Task(delay >= 2 ? delay : 1, player, false) {
							int cycle = 0;
							@Override
							public void execute() {
								if (player.getInteractingObject() == null
										|| player.getInteractingObject().getId() != oreObject.getId()) {
									player.getSkillManager().stopSkilling();
									player.performAnimation(new Animation(65535));
									stop();
									return;
								}
								if (player.getInventory().getFreeSlots() == 0) {
									player.performAnimation(new Animation(65535));
									stop();
									player.getPacketSender()
											.sendMessage("You do not have any free inventory space left.");
									return;
								}
//								int removeChance = Misc.inclusiveRandom(1, 2);
//								if(removeChance == 1) {
//									TaskManager.submit(new NPCRespawnTask(npc, 75));
//									World.deregister(npc);
//									stop();
//								}
								int itemId = o.getItemId();
								if(oreObject.getId() == 30000) {
									if(Misc.inclusiveRandom(1, 3) == 1) {
										itemId = 444;
									} else {
										itemId = 453;
									}
								}
								if (cycle != reqCycle) {
									cycle++;
									player.performAnimation(new Animation(p.getAnim()));
								}
								if (giveGem) {
									boolean onyx = (o == Ores.RUNITE)
											&& Misc.getRandom(20000) == 1;
									if (onyx || Misc.getRandom(50) == 15) {
										int gemId = onyx ? 6571
												: MiningData.RANDOM_GEMS[(int) (MiningData.RANDOM_GEMS.length
												* Math.random())];
										player.getInventory().add(gemId, 1);
										player.getPacketSender().sendMessage("You've found a gem!");
										if (gemId == 6571) {
											String s = o == Ores.RUNITE ? "RUNITE ore" : "Crashed star";
											World.sendMessage("<img=4><col=009966> " + player.getUsername()
													+ " has just received an Uncut Onyx from mining a " + s + "!");
										}
									}
								}
								if (cycle == reqCycle) {
									if (o == Ores.RUNITE) {
										Achievements.doProgress(player, AchievementData.MINE_2000_RUNITE_ORES);
									}
									if (o == Ores.COAL || o == Ores.RESOURCE_COAL || (itemId == 453 && o == Ores.LIVING_ROCK_RESOURCE)) {
										Achievements.doProgress(player, AchievementData.MINE_400_COAL);
									}
									if (o == Ores.GOLD || o == Ores.RESOURCE_GOLD || (itemId == 444 && o == Ores.LIVING_ROCK_RESOURCE)) {
										Achievements.doProgress(player, AchievementData.MINE_200_GOLD);
									}
									if (o == Ores.ADAMANTITE || o == Ores.RESOURCE_ADAMANTITE) {
										Achievements.doProgress(player, AchievementData.MINE_400_ADDY);
									}
									if (o == Ores.IRON || o == Ores.RESOURCE_IRON) {
										Achievements.finishAchievement(player, AchievementData.MINE_IRON);
									}
									int multiplier = (Skillcape_Data.MINING.isWearingCape(player)
											&& Misc.inclusiveRandom(0, 9) == 0) ? 2 : 1;
									if (o.getItemId() != -1) {
										player.getInventory().add(itemId, 1 * multiplier);
									}
									if(oreObject.getId() == 30000) {
										double experience = 50;
										if(itemId == 444) {
											experience = 60;
										}
										experience *= getGoldenMiningBoost(player);
										player.getSkillManager().addSkillExperience(Skill.MINING, experience);
									} else {
										double experience = o.getXpAmount();
										experience *= getGoldenMiningBoost(player);
										player.getSkillManager().addSkillExperience(Skill.MINING, experience);
									}
									if(player.getInventory().contains(20786) || player.getEquipment().contains(20786)) {
										if(MathUtil.random(3) == 0) {
											if (o == Ores.RUNITE) {
												Achievements.doProgress(player, AchievementData.MINE_2000_RUNITE_ORES);
											}
											if (o == Ores.COAL || o == Ores.RESOURCE_COAL) {
												Achievements.doProgress(player, AchievementData.MINE_400_COAL);
											}
											if (o == Ores.ADAMANTITE || o == Ores.RESOURCE_ADAMANTITE) {
												Achievements.doProgress(player, AchievementData.MINE_400_ADDY);
											}
											if (o == Ores.IRON || o == Ores.RESOURCE_IRON) {
												Achievements.finishAchievement(player, AchievementData.MINE_IRON);
											}
											int multiplier2 = (Skillcape_Data.MINING.isWearingCape(player)
													&& Misc.inclusiveRandom(0, 9) == 0) ? 2 : 1;
											if (o.getItemId() != -1) {
												player.getInventory().add(o.getItemId(), 1 * multiplier2);
											}
										}
									}
									player.getPacketSender().sendMessage("You mine some ore.");
									//rollPet(player);
									Sounds.sendSound(player, Sound.MINE_ITEM);
									cycle = 0;
									this.stop();
									if (o.getRespawn() > 0) {
										player.performAnimation(new Animation(65535));
										oreRespawn(player, oreObject, o);
									} else {
										player.performAnimation(new Animation(65535));
										startMining(player, oreObject, null);
									}
								}
							}
						});
						TaskManager.submit(player.getCurrentTask());
					} else {
						player.getPacketSender().sendMessage(
								"You need a Mining level of at least " + p.getReq() + " to use this pickaxe.");
					}
				} else {
					player.getPacketSender().sendMessage(
							"You need a Mining level of at least " + o.getLevelReq() + " to mine this rock.");
				}
			} else {
				player.getPacketSender().sendMessage("You don't have a pickaxe to mine this rock with.");
			}
		}
	}

	/**
	 * Get the boost for the golden mining armour set.
	 * @param player
	 * @return
	 */
	public static double getGoldenMiningBoost(Player player) {
		double boost = 1.0;
		if(player.getEquipment().get(Equipment.HEAD_SLOT).getId() == 20789) {
			boost += .2;
		} else if(player.getEquipment().get(Equipment.BODY_SLOT).getId() == 20791) {
			boost += .2;
		} else if(player.getEquipment().get(Equipment.LEG_SLOT).getId() == 20790) {
			boost += .2;
		} else if(player.getEquipment().get(Equipment.FEET_SLOT).getId() == 20788) {
			boost += .2;
		} else if(player.getEquipment().get(Equipment.HANDS_SLOT).getId() == 20787) {
			boost += .2;
		}
		return boost;
	}

	public static void oreRespawn(final Player player, final GameObject oldOre, Ores o) {
		if (oldOre == null || oldOre.getPickAmount() >= 1)
			return;
		oldOre.setPickAmount(1);
		for (Player players : player.getLocalPlayers()) {
			if (players == null)
				continue;
			if (players.getInteractingObject() != null && players.getInteractingObject().getPosition()
					.equals(player.getInteractingObject().getPosition().copy())) {
				players.getPacketSender().sendClientRightClickRemoval();
				players.getSkillManager().stopSkilling();
			}
		}
		player.getPacketSender().sendClientRightClickRemoval();
		player.getSkillManager().stopSkilling();
		CustomObjects.globalObjectRespawnTask(new GameObject(452, oldOre.getPosition().copy(), 10, 0), oldOre,
				o.getRespawn());
	}
}
