package com.zarador.net.packet.impl;

import org.scripts.kotlin.content.dialog.*;
import org.scripts.kotlin.content.dialog.npcs.OpenScroll;

import com.zarador.GameSettings;
import com.zarador.engine.task.Task;
import com.zarador.engine.task.TaskManager;
import com.zarador.model.*;
import com.zarador.model.input.impl.EnterAmountToDice;
import com.zarador.net.packet.Packet;
import com.zarador.net.packet.PacketListener;
import com.zarador.util.Misc;
import com.zarador.world.content.*;
import com.zarador.world.content.achievements.Achievements;
import com.zarador.world.content.christmas.Presents;
import com.zarador.world.content.combat.range.DwarfMultiCannon;
import com.zarador.world.content.skill.impl.herblore.Herblore;
import com.zarador.world.content.skill.impl.herblore.IngridientsBook;
import com.zarador.world.content.skill.impl.hunter.BoxTrap;
import com.zarador.world.content.skill.impl.hunter.Hunter;
import com.zarador.world.content.skill.impl.hunter.JarData;
import com.zarador.world.content.skill.impl.hunter.PuroPuro;
import com.zarador.world.content.skill.impl.hunter.SnareTrap;
import com.zarador.world.content.skill.impl.hunter.Trap.TrapState;
import com.zarador.world.content.skill.impl.prayer.Prayer;
import com.zarador.world.content.skill.impl.runecrafting.Runecrafting;
import com.zarador.world.content.skill.impl.runecrafting.RunecraftingPouches;
import com.zarador.world.content.skill.impl.runecrafting.RunecraftingPouches.RunecraftingPouch;
import com.zarador.world.content.skill.impl.slayer.SlayerDialog;
import com.zarador.world.content.skill.impl.slayer.SlayerTeleports;
import com.zarador.world.content.skill.impl.summoning.SummoningData;
import com.zarador.world.content.skill.impl.woodcutting.BirdNests;
import com.zarador.world.content.transportation.TeleportHandler;
import com.zarador.world.content.transportation.TeleportType;
import com.zarador.world.content.transportation.jewelry.CombatTeleporting;
import com.zarador.world.content.transportation.jewelry.GloryTeleporting;
import com.zarador.world.content.transportation.jewelry.SkillsTeleporting;
import com.zarador.world.entity.impl.player.Player;

public class ItemActionPacketListener implements PacketListener {

	public static void handleHasta(Player player) {
		if (player.getInventory().contains(11716) && player.getMoneyInPouch() >= 10_000_000) {
			player.setMoneyInPouch(player.getMoneyInPouch() - 10_000_000);
			player.getPacketSender().sendMessage("10,000,000 Coins have been removed from your Money Pouch");
			player.getInventory().delete(11716, 1);
			player.getInventory().add(21120, 1);
		} else {
			player.getPacketSender().sendMessage("You either do not have 10M in your pouch or a Zamorakian Spear.");
		}
	}

	private static void firstAction(final Player player, Packet packet) {
		int interfaceId = packet.readUnsignedShort();
		int slot = packet.readShort();
		int itemId = packet.readShort();
		if(GameSettings.DEVELOPER_MODE) {
			player.getPacketSender().sendMessage("First click ItemActionPacket - Slot: " + slot + ", itemId: " + itemId);
		}
		if (slot < 0 || slot > player.getInventory().capacity())
			return;
		if (player.getInventory().getItems()[slot].getId() != itemId) {
			return;
		}
		player.setInteractingItem(player.getInventory().getItems()[slot]);
		if (Prayer.isBone(itemId)) {
			Prayer.buryBone(player, itemId);
			return;
		}
		if (!player.getDragonSpear().elapsed(3000)) {
			player.getPacketSender().sendMessage("You can't do that, you're stunned!");
			return;
		}
		if (Consumables.isFood(player, itemId, slot))
			return;
		if (Consumables.isPotion(itemId)) {
			Consumables.handlePotion(player, itemId, slot);
			return;
		}
		if (BirdNests.isNest(itemId)) {
			BirdNests.searchNest(player, itemId);
			return;
		}
		if (Effigies.isEffigy(itemId)) {
			Effigies.handleEffigy(player, itemId);
		}
		if (Herblore.cleanHerb(player, itemId))
			return;
		if (Effigies.isEffigy(itemId)) {
			Effigies.handleEffigy(player, itemId);
			return;
		}
		if (ExperienceLamps.handleLamp(player, itemId)) {
			return;
		}
		switch (itemId) {
			case 2379:
			    if (player.getConstitution() < 10) {
                    player.getPacketSender().sendMessage("Eating this will kill you. You need at least 10 hp.");
                    return;
                }
				player.getDialog().sendDialog(new RockCake(player));
				break;
			case 11262:
				PuroPuro.lootJar(player, new Item(itemId, 1), JarData.forJar(itemId));
				break;
			case 7478:
				if (player.getPlayerKillingAttributes().getTarget() != null) {
					if (player.getInventory().contains(7478)) {
						if (!player.getClickDelay().elapsed(5000))
							return;
						if (player.getPlayerKillingAttributes().getTarget().getLocation() != Locations.Location.WILDERNESS) {
							player.getPacketSender().sendMessage("Your target is not currently in the wild. You have not used up a token.");
							return;
						}
						player.getPacketSender().sendMessage("Your target is located at " + player.getPlayerKillingAttributes().getTarget().getWildernessLevel() + " wilderness.");
						player.getInventory().delete(7478, 1);
						player.getClickDelay().reset();
					}
				} else {
					player.getPacketSender().sendMessage("You currently do not have a target. Who are you trying to find?");
				}
				break;
			case 6808:
				player.getDialog().sendDialog(new TrustedDicerScroll(player));
				break;
			case 5023:
				if (player.getInventory().contains(5023)) {
					player.getInventory().delete(5023, 1);
					player.setMoneyInPouch(player.getMoneyInPouch() + 15_000_000);
					player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch() + "");
				}
				player.getPacketSender().sendMessage("You have claimed this ticket for 15m and it has been added to your money pouch.");
				break;
			case 6542:
			case 15420:
				Presents.openPresent(player, itemId);
				break;
			//Scrolls
			case 10934:
			case 10935:
			case 10943:
			case 7629:
				player.getDialog().sendDialog(new OpenScroll(player, 0, itemId));
				break;
			case 15707:
				if(player.getLocation() == Locations.Location.DAEMONHEIM) {
					player.getDungeoneering().showTab();
				} else {
					player.getPacketSender().sendMessage("This ring only works in Daehmonheim!");
				}
				break;
			//slayer
			case 4155:
				if(player.getSlayer().getSlayerTask() != null) {
					player.setNpcClickId(player.getSlayer().getSlayerMaster().getNpcId());
					player.getDialog().sendDialog(new SlayerDialog(player, 5, null));
					return;
				} else {
					player.getPacketSender().sendInterfaceRemoval();
					player.getPacketSender().sendMessage("Your Enchanted gem will only work if you have a Slayer task.");
				}
				break;
			case 5733:
				if(player.getRottenPotato().elapsed(43200000)) {
					player.rottenPotatoPrayer = 0;
					player.rottenPotatoHeal = 0;
				}
				if (!player.getRottenPotato().elapsed(43200000) && player.rottenPotatoHeal >= 5) {
					player.getPacketSender().sendMessage("You can use your rotten potato again in "+(Misc.getRottenPotatoTime((43200000 - player.getRottenPotato().elapsed()))+"."));
					return;
				}
				if(player.getLocation() == Locations.Location.WILDERNESS) {
					player.getPacketSender().sendMessage("You can't use the rotten potato (heal) in the wilderness.");
					return;
				}

				if (player.getLocation() == Locations.Location.DUEL_ARENA && player.getDueling().duelingStatus == 5) {
					player.getPacketSender().sendMessage("You can't use the rotten potato (heal) in the duel arena.");
					return;
				}
				if(player.rottenPotatoHeal == 0) {
					player.getRottenPotato().reset();
				}
				player.rottenPotatoHeal++;
				player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, player.getSkillManager().getMaxLevel(Skill.CONSTITUTION));
				player.performAnimation(new Animation(2770));
				player.getPacketSender().sendMessage("<col=009E44>You have used the rotten potato heal function "+player.rottenPotatoHeal+"/5 times for the next 12 hours.");
				player.forceChat("This potato tastes nasty...");
				break;
			case 10586:
				player.getDialog().sendDialog(new KnightLamp(player));
				break;
			case 21776:
				if (player.getInventory().contains(21776) && player.getInventory().getAmount(21776) >= 100) {
					if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) >= 80) {
						player.getInventory().delete(21776, 100);
						player.getInventory().add(21775, 1);
						player.getSkillManager().addExactExperience(Skill.CRAFTING, 1000, true);
						return;
					} else {
						player.getPacketSender().sendMessage("You need a Crafting level of 80 to make an Orb of Armadyl");
						return;
					}
				}
				player.getPacketSender().sendMessage("You do not have 100 Shards of Armadyl");
				break;
			case 13664:
				player.setNpcClickId(2998);
				player.getDialog().sendDialog(new UseLottery(player));
				break;
			case 8013:
				if (player.isJailed()) {
					player.getPacketSender().sendMessage("You can't teleport out of jail.");
					return;
				}
				if (!player.getClickDelay().elapsed(4500) || player.getWalkingQueue().isLockMovement())
					return;
				if (!TeleportHandler.checkReqs(player, new Position(3087, 3502, 0), false)) {
					return;
				}
				player.getInventory().delete(itemId, 1);
				if(player.getTabTimer().elapsed(1500)) {
					Sounds.sendSound(player, Sounds.Sound.TELEPORT);
					player.currentDialog = null;
					player.setTeleporting(true).getWalkingQueue().setLockMovement(true).clear();
					TeleportHandler.cancelCurrentActions(player);
					TaskManager.submit(new Task(1, player, true) {
						int tick = 0;
						@Override
						public void execute() {
							if (tick == 0) {
								player.performAnimation(new Animation(9597));
							} else if (tick == 4) {
								player.moveTo(new Position(3087, 3502, 0));
								player.performAnimation(new Animation(9598));
								player.performGraphic(new Graphic(678));
								player.getWalkingQueue().setLockMovement(false).clear();
								stop();
							}
							tick++;
						}

						@Override
						public void stop() {
							setEventRunning(false);
							player.setTeleporting(false);
							player.getClickDelay().reset(0);
						}
					});
				}
				player.getTabTimer().reset();
				break;
		case 12437:
			if (player.getSummoning().getFamiliar() != null) {
				if (player.getSummoning().getFamiliar().getSummonNpc().getId() == 6869) {
					if (!player.getSummoningTimer().elapsed(15000)) {
						player.getPacketSender()
								.sendMessage("You must wait 15 seconds in order to use the magic focus scroll effect.");
						return;
					}
					player.getSummoningTimer().reset();
					player.performAnimation(new Animation(7660));
					player.performGraphic(new Graphic(1303));
					player.getSkillManager().setCurrentLevel(Skill.forId(6),
							player.getSkillManager().getCurrentLevel(Skill.forId(6))
									+ Consumables.getBoostedStat(player, 500, false, false),
							true);
					player.getPacketSender().sendMessage("Your wolpertinger has boosted your magic to "
							+ player.getSkillManager().getCurrentLevel(Skill.forId(6)) + ".");

					player.getInventory().delete(12437, 1);
				} else {
					player.getPacketSender()
							.sendMessage("You must have a wolpertinger summoned in order to use this scroll.");
				}
			} else {
				player.getPacketSender()
						.sendMessage("You must have a wolpertinger summoned in order to use this scroll.");
			}
			break;
		case 739:
			if (player.getInventory().contains(739)) {
				Consumables.handlePotion(player, 739, slot);
			}
			break;
		case 6040:
			Position location = new Position(2036, 4535, 0);
			TeleportHandler.teleportPlayer(player, location, TeleportType.PURO_PURO);
			break;
		case 11211:
			if(!player.isSpecialPlayer()) {
				return;
			}
			player.setInputHandling(new EnterAmountToDice(1, 1));
			player.getPacketSender().sendEnterAmountPrompt("What would you like to roll?");
			break;
		case 4142:
			if(!player.isSpecialPlayer()) {
				return;
			}
			if (!player.boost_stats) {
				player.getPacketSender().sendMessage("<col=ff0000><shad=0>You have boosted to 1,000 defence.");
				player.getSkillManager().setCurrentLevel(Skill.DEFENCE, 1000, true);
				player.boost_stats = true;
			} else {
				player.getPacketSender().sendMessage("<col=ff0000><shad=0>You have restored your defence.");
				player.boost_stats = false;
				player.getSkillManager().setCurrentLevel(Skill.DEFENCE, 99, true);
			}
			break;
			case 2677:
			player.getPacketSender().sendInterface(17537);
			break;
		case 10944:
			if (player.getInventory().isFull()) {
				player.getPacketSender().sendMessage("You need to have atleast 1 free inventory space.");
				return;
			}
			VoteTokens.openToken(player);
			break;
		case 13663:
			if (player.getInterfaceId() > 0) {
				player.getPacketSender().sendMessage("Please close the interface you have open before doing this.");
				return;
			}
			player.setUsableObject(new Object[2]).setUsableObject(0, "reset");
			player.getPacketSender().sendString(38006, "Choose stat to reset!")
					.sendMessage("@red@Please select a skill you wish to reset and then click on the 'Confim' button.")
					.sendString(38090, "Which skill would you like to reset?");
			player.getPacketSender().sendInterface(38000);
			break;
		case 19670:
			if (player.busy()) {
				player.getPacketSender().sendMessage("You can not do this right now.");
				return;
			}
			break;
		case 7956:
			player.getInventory().delete(7956, 1);
			int[] rewards = { 200, 202, 204, 206, 208, 210, 212, 214, 216, 218, 220, 2486, 3052, 1624, 1622, 1620, 1618,
					1632, 1516, 1514, 454, 448, 450, 452, 378, 372, 7945, 384, 390, 15271, 533, 535, 537, 18831, 556,
					558, 555, 554, 557, 559, 564, 562, 566, 9075, 563, 561, 560, 565, 888, 890, 892, 11212, 9142, 9143,
					9144, 9341, 9244, 866, 867, 868, 2, 10589, 10564, 6809, 4131, 15126, 4153, 1704, 1149 };
			int[] rewardsAmount = { 50, 50, 50, 30, 20, 30, 30, 30, 30, 20, 10, 5, 4, 70, 40, 25, 10, 10, 100, 50, 100,
					80, 25, 25, 250, 200, 125, 50, 30, 25, 50, 20, 20, 5, 500, 500, 500, 500, 500, 500, 500, 500, 200,
					200, 200, 200, 200, 200, 1000, 750, 200, 100, 1200, 1200, 120, 50, 20, 1000, 500, 100, 100, 1, 1, 1,
					1, 1, 1, 1, 1 };
			int rewardPos = Misc.getRandom(rewards.length - 1);
			player.getInventory().add(rewards[rewardPos],
					(int) ((rewardsAmount[rewardPos] * 0.5) + (Misc.getRandom(rewardsAmount[rewardPos]))));
			break;
		case 15387:
			player.getInventory().delete(15387, 1);
			rewards = new int[] { 1377, 1149, 7158, 3000, 219, 5016, 6293, 6889, 2205, 3051, 269, 329, 3779, 6371, 2442,
					347, 247 };
			player.getInventory().add(rewards[Misc.getRandom(rewards.length - 1)], 1);
			break;
		case 407:
			player.getInventory().delete(407, 1);
			int chance = Misc.inclusiveRandom(1, 3);
			if (chance == 1) {
				player.getInventory().add(409, 1);
			} else if (chance == 2) {
				player.getInventory().add(411, 1);
			} else
				player.getInventory().add(413, 1);
			break;
		case 15084:
			if (player.getClanChatName() == null) {
				player.getPacketSender().sendMessage("You need to be in a clanchat channel to roll a dice.");
				return;
			}
			Gambling.rollDice(player);
			break;
		case 299:
			Gambling.plantSeed(player, Gambling.FlowersData.generate());
			break;
			case 4490:
				if(!player.isSpecialPlayer()) {
					return;
				}
				player.getDialog().sendDialog(new ChooseFlower(player, 0));
				break;
		case 15104:
			player.getPacketSender().sendMessage("Combine this with the three other missing parts...");
			return;
		case 15103:
			player.getPacketSender().sendMessage("Combine this with the three other missing parts...");
			return;
		case 15105:
			player.getPacketSender().sendMessage("Combine this with the three other missing parts...");
			return;
		case 15106:
			player.getPacketSender().sendMessage("Combine this with the three other missing parts...");
			return;
		case 11858:
		case 11860:
		case 11862:
		case 11848:
		case 11856:
		case 11850:
		case 11854:
		case 11852:
		case 11846:
			if (!player.getClickDelay().elapsed(2000) || !player.getInventory().contains(itemId))
				return;
			if (player.busy()) {
				player.getPacketSender().sendMessage("You cannot open this right now.");
				return;
			}

			int[] items = itemId == 11858 ? new int[] { 10350, 10348, 10346, 10352 }
					: itemId == 11860 ? new int[] { 10334, 10330, 10332, 10336 }
							: itemId == 11862 ? new int[] { 10342, 10338, 10340, 10344 }
									: itemId == 11848 ? new int[] { 4716, 4720, 4722, 4718 }
											: itemId == 11856 ? new int[] { 4753, 4757, 4759, 4755 }
													: itemId == 11850 ? new int[] { 4724, 4728, 4730, 4726 }
															: itemId == 11854 ? new int[] { 4745, 4749, 4751, 4747 }
																	: itemId == 11852
																			? new int[] { 4732, 4734, 4736, 4738 }
																			: itemId == 11846
																					? new int[] { 4708, 4712, 4714,
																							4710 }
																					: new int[] { itemId };

			if (player.getInventory().getFreeSlots() < items.length) {
				player.getPacketSender().sendMessage("You do not have enough space in your inventory.");
				return;
			}
			player.getInventory().delete(itemId, 1);
			for (int i : items) {
				player.getInventory().add(i, 1);
			}
			player.getPacketSender().sendMessage("You open the set and find items inside.");
			player.getClickDelay().reset();
			break;
		case 952:
			Digging.dig(player);
			break;
		case 10006:
			// Hunter.getInstance().laySnare(client);
			Hunter.layTrap(player, new SnareTrap(new GameObject(19175, new Position(player.getPosition().getX(),
					player.getPosition().getY(), player.getPosition().getZ())), TrapState.SET, 200, player));
			break;
		case 10008:
			Hunter.layTrap(player, new BoxTrap(new GameObject(19187, new Position(player.getPosition().getX(),
					player.getPosition().getY(), player.getPosition().getZ())), TrapState.SET, 200, player));
			break;
		case 5509:
		case 5510:
		case 5512:
			RunecraftingPouches.fill(player, RunecraftingPouch.forId(itemId));
			break;
		case 292:
			IngridientsBook.readBook(player, 0, false);
			break;
		case 21117:
			int AchievementRewards[][] = {
					{ 15501, 15272, 2503, 10499, 6326, 861, 1163, 1201, 6111, 544, 542, 5574, 5575, 5576, 1215, 3105,
							13734, 7400, 2572, 11118 }, // Common, 0
					{ 15501, 11133, 15126, 10828, 3751, 3753, 10589, 10564, 6809, 4587, 1249, 3204, 1305, 1377, 1434,
							6528, 7158, 4153, 6, 8, 10, 12, 4675, 6914, 6889 }, // Uncommon,
																				// 1
					{ 6739, 15259, 15332, 2579, 6920, 6922, 13879, 13883, 15241, 15243 } // Rare,
																							// 2
			};
			double achievementGen = Math.random();
			/**
			 * Chances 50% chance of Common Items - cheap gear, high-end
			 * consumables 40% chance of Uncommon Items - various high-end
			 * coin-bought gear 10% chance of Rare Items - Highest-end
			 * coin-bought gear, some voting-point/pk-point equipment
			 */
			int rewardChance = achievementGen >= 0.5 ? 0 : achievementGen >= 0.20 ? 1 : 2;
			rewardPos = Misc.getRandom(AchievementRewards[rewardChance].length - 1);
			player.getInventory().delete(21117, 1);
			player.getInventory().add(AchievementRewards[rewardChance][rewardPos], 1).refreshItems();
			break;
		case 6199:
			int rewards2[][] = {
					{ 592, 592, 592, 1079, 1093, 1127, 1201, 10828, 1163, 1201, 4131, 4587, 5698, 1305, 2491, 2503,
							2497, 861, 4675, 3849, 6524, 13734, 11118, 1712, 2550, 1079, 1093, 1127, 1201, 10828, 1163,
							1201, 4131, 4587, 5698, 1305, 2491, 2503, 2497, 861, 4675, 3849, 6524, 13734, 11118, 1712,
							2550 }, // Common, 0
					{ 592, 6889, 8849, 6570, 6570, 6570, 6570, 6570, 6570, 4151, 15486, 11235, 11732, 10548, 10551,
							10549, 2581, 2577, 6585, 15126, 592, 6889, 8849, 6570, 6570, 6570, 6570, 6570, 6570,
							4151, 15486, 11235, 11732, 10548, 10551, 10549, 2581, 2577, 6585, 15126, 11846,
							11850, 11852, 11854, 11856 }, // Uncommon, 1
					{ 592, 592, 592, 6, 8, 10, 12, 6570, 6570, 6570, 6570, 6570, 6570, 6739, 6731, 6733, 6735, 6737,
							6922, 6916, 6918, 6920, 19111, 7968 } // Rare, 2
			};
			double numGen = Math.random();
			/**
			 * Chances 50% chance of Common Items - cheap gear, high-end
			 * consumables 40% chance of Uncommon Items - various high-end
			 * coin-bought gear 10% chance of Rare Items - Highest-end
			 * coin-bought gear, some voting-point/pk-point equipment
			 */
			int rewardGrade = numGen >= 0.5 ? 0 : numGen >= 0.20 ? 1 : 2;
			rewardPos = Misc.getRandom(rewards2[rewardGrade].length - 1);
			player.getInventory().delete(6199, 1);
			player.getInventory().add(rewards2[rewardGrade][rewardPos], 1).refreshItems();
			break;
		/*
		 * case 15501: int superiorRewards[][] = { {11133, 15126, 10828, 3751,
		 * 3753, 10589, 10564, 6809, 4587, 1249, 3204, 1305, 1377, 1434, 6528,
		 * 7158, 4153, 6, 8, 10, 12, 4675, 6914, 6889}, // Uncommon, 0 {6739,
		 * 15259, 15332, 2579, 6920, 6922, 15241, 11882, 11884, 11906, 20084},
		 * // Rare, 1 {6570, 15018, 15019, 15020, 15220, 11730, 18349, 18353,
		 * 13896, 18357, 13899, 10551, 4151, 2577,}, // Epic, 2 {11235, 17273,
		 * 14484, 11696, 11698, 11700, 13262, 15486, 19336, 19337, 19338, 19339,
		 * 19340, 14009, 14010, 14008, 14011, 14012, 14013, 14014, 14015, 14016}
		 * // Legendary, // 3 }; double superiorNumGen = Math.random(); int
		 * superiorRewardGrade = superiorNumGen >= 0.46 ? 0 : superiorNumGen >=
		 * 0.16 ? 1 : superiorNumGen >= 0.05 ? 2 : 3; int superiorRewardPos =
		 * Misc.getRandom(superiorRewards[superiorRewardGrade].length - 1);
		 * player.getInventory().delete(15501, 1);
		 * player.getInventory().add(superiorRewards[superiorRewardGrade][
		 * superiorRewardPos], 1) .refreshItems(); break;
		 */
		case 15501:
			int superiorRewards[][] = {
					{ 11133, 15126, 10828, 3751, 3753, 10589, 10564, 6809, 4587, 1249, 3204, 1305, 1377, 1434, 6528,
							7158, 4153, 6, 8, 10, 12, 4675, 6914, 6889 }, // Uncommon,
																			// 0
					{ 6739, 15259, 15332, 2579, 6920, 6922, 15241, 11882, 11884, 20084 }, // Rare,
																									// 1
					{ 6570, 15018, 15019, 15020, 15220, 11730, 18349, 18353, 13896, 18357, 13899, 10551, 4151, 2577, }, // Epic,
																														// 2
					{ 11235, 17273, 14484, 11696, 11698, 11700, 12954, 20072, 15486, 19336, 19337, 19338, 19339, 19340} // Legendary,
																						// 3
			};
			double superiorNumGen = Math.random();
			int superiorRewardGrade = superiorNumGen >= 0.46 ? 0
					: superiorNumGen >= 0.16 ? 1 : superiorNumGen >= 0.05 ? 2 : 3;
			int superiorRewardPos = Misc.getRandom(superiorRewards[superiorRewardGrade].length - 1);
			player.getInventory().delete(15501, 1);
			player.getInventory().add(superiorRewards[superiorRewardGrade][superiorRewardPos], 1).refreshItems();
			break;
		case 11882:
			player.getInventory().delete(11882, 1);
			player.getInventory().add(2595, 1).refreshItems();
			player.getInventory().add(2591, 1).refreshItems();
			player.getInventory().add(3473, 1).refreshItems();
			player.getInventory().add(2597, 1).refreshItems();
			break;
		case 11884:
			player.getInventory().delete(11884, 1);
			player.getInventory().add(2595, 1).refreshItems();
			player.getInventory().add(2591, 1).refreshItems();
			player.getInventory().add(2593, 1).refreshItems();
			player.getInventory().add(2597, 1).refreshItems();
			break;
		//Wizard set
//		case 11906:
//			player.getInventory().delete(11906, 1);
//			player.getInventory().add(7394, 1).refreshItems();
//			player.getInventory().add(7390, 1).refreshItems();
//			player.getInventory().add(7386, 1).refreshItems();
//			break;
		case 15262:
			if (!player.getClickDelay().elapsed(1000))
				return;
			if (player.getInventory().getAmount(18016) >= Integer.MAX_VALUE - 10000) {
				player.getPacketSender().sendMessage("You have too many spirit shards in your inventory!");
				return;
			}
			player.getInventory().delete(15262, 1);
			player.getInventory().add(18016, 10000).refreshItems();
			player.getClickDelay().reset();
			break;
		case 6:
			DwarfMultiCannon.setupCannon(player);
			break;
		}
	}

	public static void secondAction(Player player, Packet packet) {
		int interfaceId = packet.readLEShortA();
		int slot = packet.readLEShort();
		int itemId = packet.readShortA();
		if (slot < 0 || slot > player.getInventory().capacity())
			return;
		if (player.getInventory().getItems()[slot].getId() != itemId)
			return;
		if (SummoningData.isPouch(player, itemId, 2))
			return;
		if(player.getDegrading().isCheckCharges(player, itemId, slot)) {
			return;
		}
		if(GameSettings.DEVELOPER_MODE) {
			player.getPacketSender().sendMessage("Second click ItemActionPacket - Slot: " + slot + ", itemId: " + itemId);
		}
		switch (itemId) {
		//slayer rings
		case 13281:
		case 13282:
		case 13283:
		case 13284:
		case 13285:
		case 13286:
		case 13287:
		case 13288:
			player.getSlayer().handleSlayerRingTP(itemId);
			break;
			case 5733:
				if(player.getRottenPotato().elapsed(43200000)) {
					player.rottenPotatoDrop = 0;
					player.rottenPotatoPrayer = 0;
					player.rottenPotatoDrop = 0;
					player.getRottenPotatoDropTimer().reset();
				}
				if (!player.getRottenPotato().elapsed(43200000) && player.rottenPotatoPrayer >= 2) {
					player.getPacketSender().sendMessage("You can use your rotten potato again in "+(Misc.getRottenPotatoTime((43200000 - player.getRottenPotato().elapsed()))+"."));
					return;
				}
				if(player.getLocation() == Locations.Location.WILDERNESS) {
					player.getPacketSender().sendMessage("You can't use the rotten potato (prayer) in the wilderness.");
					return;
				}
				if (player.getLocation() == Locations.Location.DUEL_ARENA && player.getDueling().duelingStatus == 5) {
					player.getPacketSender().sendMessage("You can't use the rotten potato (prayer) in the duel arena.");
					return;
				}
				if(player.rottenPotatoPrayer == 0) {
					player.getRottenPotato().reset();
				}
				player.rottenPotatoPrayer++;
				player.getSkillManager().setCurrentLevel(Skill.PRAYER, (int)((double) player.getSkillManager().getMaxLevel(Skill.PRAYER) * 1.5));
				player.performAnimation(new Animation(9098));
				player.getPacketSender().sendMessage("<col=009E44>You have used the rotten potato pray function "+player.rottenPotatoPrayer+"/2 times for the next 12 hours.");
				player.getPacketSender().sendMessage("<col=009E44>Your prayer has been re-generated 1.5x the regular amount!");
				player.forceChat("ALL HAIL THE MIGHTY POTATOS!");
				break;
		case 6500:
			player.getPacketSender().sendMessage("You're already gaining EXP and picking up charms! don't be greedy");
			break;
		case 1712:
		case 1710:
		case 1708:
		case 1706:
			GloryTeleporting.rub(player, itemId);
			break;
		case 11105:
		case 11107:
		case 11109:
		case 11111:
			SkillsTeleporting.rub(player, itemId);
		break;
		case 11113:
			player.getPacketSender().sendMessage("Your necklace has run out of charges.");
			break;

		case 11118:
		case 11120:
		case 11122:
		case 11124:
			CombatTeleporting.rub(player, itemId);
			break;
		case 1704:
			player.getPacketSender().sendMessage("Your amulet has run out of charges.");
			break;
		case 11126:
			player.getPacketSender().sendMessage("Your bracelet has run out of charges.");
			break;
		case 5509:
		case 5510:
		case 5512:
			RunecraftingPouches.check(player, RunecraftingPouch.forId(itemId));
			break;
		case 995:
			MoneyPouch.depositMoney(player, player.getInventory().getAmount(995));
			break;
		case 1438:
		case 1448:
		case 1440:
		case 1442:
		case 1444:
		case 1446:
		case 1454:
		case 1452:
		case 1462:
		case 1458:
		case 1456:
		case 1450:
		case 21106:
			Runecrafting.handleTalisman(player, itemId);
			break;
		}
	}

	public void thirdClickAction(Player player, Packet packet) {
		int itemId = packet.readShortA();
		int slot = packet.readLEShortA();
		int interfaceId = packet.readLEShortA();
		if (slot < 0 || slot > player.getInventory().capacity())
			return;
		if(GameSettings.DEVELOPER_MODE) {
			player.getPacketSender().sendMessage("Third click ItemActionPacket - Slot: " + slot + ", itemId: " + itemId);
		}
		if(itemId != 14022) {
			if (player.getInventory().getItems()[slot].getId() != itemId)
				return;
		}
		if (JarData.forJar(itemId) != null) {
			PuroPuro.lootJar(player, new Item(itemId, 1), JarData.forJar(itemId));
			return;
		}
		if (SummoningData.isPouch(player, itemId, 3)) {
			return;
		}
		if (SummoningData.isPouch(player, itemId, 3)) {
			return;
		}
		if(player.getDungeoneering().isDungItem(new Item(itemId, 1).getDefinition().getName())) {
			player.getDungeoneering().bindItem(new Item(itemId, 1));
			return;
		}
		switch (itemId) {
			case 15707:
				TeleportHandler.teleportPlayer(player, new Position(3450, 3716, 0), TeleportType.DUNGEONEERING);
				break;
			case 14022:
				for (Achievements.AchievementData d : Achievements.AchievementData.values()) {
					if (!player.getAchievementAttributes().getCompletion()[d.ordinal()]) {
						player.getPacketSender().sendMessage("You must have completed the tier 4 achievements in order to customize this cape.");
						return;
					}
				}
				player.getPacketSender().sendInterface(18700);
				break;
			case 5733:
				if(player.getRottenPotatoDropTimer().elapsed(43200000)) {
					player.rottenPotatoDrop = 0;
					player.getRottenPotatoDropTimer().reset();
					player.debug(1);
				}
				if (!player.getRottenPotatoDropTimer().elapsed(43200000) && player.rottenPotatoDrop >= 1) {
					player.getPacketSender().sendMessage("You can use your rotten potato again in "+(Misc.getRottenPotatoTime((43200000 - player.getRottenPotatoDropTimer().elapsed()))+"."));
					return;
				}
				if(player.getLocation() == Locations.Location.WILDERNESS) {
					player.getPacketSender().sendMessage("You can't use the rotten potato (drop rate) in the wilderness.");
					return;
				}
				if (player.getLocation() == Locations.Location.DUEL_ARENA && player.getDueling().duelingStatus == 5) {
					player.getPacketSender().sendMessage("You can't use the rotten potato (drop rate) in the duel arena.");
					return;
				}
				player.getRottenPotatoDropTimer().reset();
				player.rottenPotatoDrop = 1;
				player.dropRateBoost = .05;
				player.performAnimation(new Animation(6404));
				player.getPacketSender().sendMessage("<col=009E44>You have used the rotten potato drop rate function "+player.rottenPotatoDrop+"/1 times for the next 12 hours.");
				player.getPacketSender().sendMessage("<col=009E44>Your drop rate is boosted 5% for the next 1 hour.");
				player.forceChat("The legend of Argos has been born.");
				break;
		case 13281:
		case 13282:
		case 13283:
		case 13284:
		case 13285:
		case 13286:
		case 13287:
		case 13288:
			player.getPacketSender().sendInterfaceRemoval();
			player.getPacketSender()
					.sendMessage(player.getSlayer().getSlayerTask() == null
							? ("You do not have a Slayer task.")
							: ("Your current task is to kill another "
							+ (player.getSlayer().getAmountLeft()) + " " + Misc.formatText(player
							.getSlayer().getSlayerTask().toString().toLowerCase().replaceAll("_", " "))
							+ "s."));
			break;
		case 11716:
			//handleHasta(player);
			break;
		case 19670:
			if (player.busy()) {
				player.getPacketSender().sendMessage("You can not do this right now.");
				return;
			}
			break;
		case 6500:
			player.getPacketSender().sendMessage("You're currently gain experience and picking up charms.");
			break;
		case 4155:
				player.getDialog().sendDialog(new SlayerTeleports(player));
			break;
		case 6570:
			if (player.getInventory().contains(6570) && player.getInventory().getAmount(6529) >= 10000) {
				player.getInventory().delete(6570, 1).delete(6529, 10000).add(19111, 1);
				player.getPacketSender().sendMessage("You have upgraded your Fire cape into a TokHaar-Kal cape!");
			} else {
				player.getPacketSender().sendMessage(
						"You need at least 10,000 Tokkul to upgrade your Fire Cape into a TokHaar-Kal cape.");
			}
			break;
		case 21077:
			/*
			 * double charges = (double) player.getToxicStaffCharges() / 110;
			 * DecimalFormat double_decimal_format = new DecimalFormat("#.00");
			 * player.getPacketSender().sendMessage(
			 * "Your Toxic staff of the dead has " +
			 * "Your Toxic staff of the dead has " +
			 * double_decimal_format.format(charges) + "% of it's charges left."
			 * );
			 */
			break;
		case 15262:
			if (!player.getClickDelay().elapsed(1300))
				return;
			int boxAmount = player.getInventory().getAmount(15262);
			int shardAmount = player.getInventory().getAmount(18016);

			long finalAmount = (long) boxAmount * 10000 + shardAmount;
			int canAdd = Integer.MAX_VALUE - shardAmount;

			if (finalAmount >= Integer.MAX_VALUE) {
				boxAmount = canAdd / 10000;

			}

			if (boxAmount > 0)
				player.getInventory().delete(15262, boxAmount).add(18016, 10000 * boxAmount);
			player.getClickDelay().reset();
			break;
		case 5509:
		case 5510:
		case 5512:
			RunecraftingPouches.empty(player, RunecraftingPouch.forId(itemId));
			break;
		case 11283: // DFS
		case 11284: // DFS
			player.getPacketSender()
					.sendMessage("Your Dragonfire shield has " + player.getDfsCharges() + "/50 dragon-fire charges.");
			break;
		}
	}

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player.getConstitution() <= 0 && player.isDying()) {
			return;
		}
		switch (packet.getOpcode()) {
			case FIRST_ITEM_ACTION_OPCODE: {
				firstAction(player, packet);
				break;
			}
			case SECOND_ITEM_ACTION_OPCODE:
				secondAction(player, packet);
				break;
			case THIRD_ITEM_ACTION_OPCODE:
				thirdClickAction(player, packet);
				break;
		}
	}

	public static final int SECOND_ITEM_ACTION_OPCODE = 75;

	public static final int FIRST_ITEM_ACTION_OPCODE = 122;

	public static final int THIRD_ITEM_ACTION_OPCODE = 16;

}
