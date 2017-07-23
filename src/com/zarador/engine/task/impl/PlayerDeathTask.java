package com.zarador.engine.task.impl;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import com.zarador.GameSettings;
import com.zarador.engine.task.Task;
import com.zarador.model.*;
import com.zarador.model.Locations.Location;
import com.zarador.model.definitions.ItemDefinition;
import com.zarador.util.Misc;
import com.zarador.world.World;
import com.zarador.world.content.Degrading;
import com.zarador.world.content.ItemsKeptOnDeath;
import com.zarador.world.content.PlayerPanel;
import com.zarador.world.content.logs.Logs;
import com.zarador.world.entity.impl.GroundItemManager;
import com.zarador.world.entity.impl.npc.NPC;
import com.zarador.world.entity.impl.player.Player;

/**
 * Represents a player's death task, through which the process of dying is
 * handled, the animation, dropping items, etc.
 * 
 * @author relex lawl, redone by Gabbe.
 */

public class PlayerDeathTask extends Task {

	/**
	 * The PlayerDeathTask constructor.
	 *
	 * @param player
	 *            The player setting off the task.
	 */
	public PlayerDeathTask(Player player) {
		super(1, player, false);
		this.player = player;
	}

	private Player player;
	private int ticks = 5;
	private boolean dropItems = true;
	Position oldPosition;
	Location loc;
	ArrayList<Item> itemsToKeep = null;
	Degrading degrading;
	//NPC death;

	@Override
	public void execute() {
		if (player == null) {
			stop();
			return;
		}

		try {
			switch (ticks) {
			case 5:
				player.getPacketSender().sendInterfaceRemoval();
				player.getWalkingQueue().clear();
				break;
			case 3:
				player.performAnimation(new Animation(0x900));
				player.getPacketSender().sendMessage("Oh dear, you are dead!");
				player.save();
				//this.death = getDeathNpc(player);
				break;
			case 1:
				this.oldPosition = player.getPosition().copy();
				this.loc = player.getLocation();
				if (loc != Location.PEST_CONTROL_GAME && loc != Location.DUEL_ARENA
						&& loc != Location.FREE_FOR_ALL_ARENA && loc != Location.FREE_FOR_ALL_WAIT
						&& loc != Location.SOULWARS && loc != Location.FIGHT_PITS
						&& loc != Location.FIGHT_PITS_WAIT_ROOM && loc != Location.FIGHT_CAVES
						&& loc != Location.ZULRAH_PIT
						&& loc != Location.RECIPE_FOR_DISASTER && loc != Location.GRAVEYARD) {
					DamageDealer damageDealer = player.getCombatBuilder().getTopDamageDealer(true, null);
					Player killer = damageDealer == null ? null : damageDealer.getPlayer();
					// final boolean doubleDeath = player.isDying() &&
					// killer.getConstitution() <= 0;
					// if(killer.getLocation() == Location.WILDERNESS ||
					// killer.getLocation() ==
					// Location.WILDKEY_ZONE) {
					// if(doubleDeath)
					// killer.restart();
					// }
					boolean spawnItems = false;
					if (dropItems) {
//						degrading.deleteBarrowsAmour(player);
						itemsToKeep = ItemsKeptOnDeath.getItemsToKeep(player);
						final CopyOnWriteArrayList<Item> playerItems = new CopyOnWriteArrayList<Item>();
						playerItems.addAll(player.getInventory().getValidItems());
						playerItems.addAll(player.getEquipment().getValidItems());
						//playerItems.addAll(degrading.getBarrowsItems()); //adds degraded barrows items
						final Position position = player.getPosition();
						if (loc == Location.WILDERNESS || loc == Location.WILDKEY_ZONE) {
							deletePvpArmour(player);
							spawnItems = true;
							for (Item item : playerItems) {
								for(Item itemsKeep: itemsToKeep) {
									if(itemsKeep.getId() == item.getId()) {
										item.setAmount(item.getAmount() - itemsKeep.getAmount());
									}
								}
								if (!item.tradeable(player) || itemsToKeep.contains(item)) {
									if (!itemsToKeep.contains(item)) {
										if(!item.getDefinition().getName().toLowerCase().contains("(deg)")) {
											itemsToKeep.add(item);
											continue;
										}
									}
								}
								if (spawnItems && !itemsToKeep.contains(item)) {
									if(item.getDefinition().getName().contains("(deg)")) {
										int address = Misc.random(0, Integer.MAX_VALUE);
										GroundItemManager.spawnGroundItem((killer != null ? killer : player), new GroundItem(new Item(995, item.getDefinition().getValue()), position,
														killer != null ? killer.getUsername() : player.getUsername(),
														player.getHostAddress(), false, 150, true, 150, address, (killer != null && killer.getGameModeAssistant().isIronMan()), player.getUsername()));
										continue;
//									} else if(item.getDefinition().getName().toLowerCase().contains("chaotic")) {
//										int address = Misc.random(0, Integer.MAX_VALUE);
//										GroundItemManager.spawnGroundItem(
//												(killer != null ? killer
//														: player),
//												new GroundItem(new Item(995, 5000000), position,
//														killer != null ? killer.getUsername() : player.getUsername(),
//														player.getHostAddress(), false, 150, true, 150, address, (killer != null && killer.getGameModeAssistant().isIronMan()), player.getUsername()));
//										continue;
									} else if (item != null && item.getId() > 0 && item.getAmount() > 0) {
										int address = Misc.random(0, Integer.MAX_VALUE);
										GroundItemManager.spawnGroundItem(
												(killer != null ? killer
														: player),
												new GroundItem(item, position,
														killer != null ? killer.getUsername() : player.getUsername(),
														player.getHostAddress(), false, 150, true, 150, address, (killer != null && killer.getGameModeAssistant().isIronMan()), player.getUsername()));
										if (killer != null) {
											Logs.log(killer, "killLoot",
													new String[] { "Killer: " + killer.getUsername() + "",
															"Looser: " + player.getUsername() + "",
															"Item: " + item.getDefinition().getName() + " (" + item.getId() + ")",
															"Amount: " + item.getAmount() + "", "Tracking: " + address + "" });
										}
									}
								}
							}
						}
						if (killer != null) {
							killer.getPacketSender()
									.sendMessage("You have just killed the player " + player.getUsername() + ". ");
							player.getPacketSender()
									.sendMessage("You were just killed by the player " + killer.getUsername() + ".");
							killer.getPlayerKillingAttributes().add(player);
							player.getPlayerKillingAttributes()
									.setPlayerDeaths(player.getPlayerKillingAttributes().getPlayerDeaths() + 1);
							player.getPlayerKillingAttributes().setPlayerKillStreak(0);
							PlayerPanel.refreshPanel(player);
						}
						if (loc == Location.WILDERNESS || loc == Location.WILDKEY_ZONE) {
							player.getInventory().resetItems().refreshItems();
							player.getEquipment().resetItems().refreshItems();
						}
					}
				} else
					dropItems = false;
				player.getPacketSender().sendInterfaceRemoval();
				player.setEntityInteraction(null);
				player.getCombatBuilder().cooldown(false);
				player.setTeleporting(false);
				player.setWalkToTask(null);
				player.getSkillManager().stopSkilling();
				break;
			case 0:
				/*
				if (dropItems) {
					boolean found = false;
					if (loc == Location.WILDERNESS || loc == Location.WILDKEY_ZONE) {
						if (itemsToKeep != null) {
							for (Item it : itemsToKeep) {
								if (it != null) {
									found = false;
									if (it.getAmount() == 0) {
										it.setAmount(1);
									}
								}
									for (int i = 0; i < player.itemToBuyBack.size(); i++) {
										if (player.itemToBuyBack.get(i).getId() == it.getId()) {
											found = true;
											System.out.println("FOUND");
											System.out.println("ItemsToKeep: " + itemsToKeep.get(i).toString() + " amount: " + it.getAmount());
											System.out.println("itemToBuyBack: " + player.itemToBuyBack.get(i).toString() + " amount: " + it.getAmount());
											player.itemToBuyBack.get(i).setAmount(player.itemToBuyBack.get(i).getAmount() + it.getAmount());
											System.out.println("NEW! itemToBuyBack: " + player.itemToBuyBack.get(i).toString() + " amount: " + it.getAmount());
										}
									}
									if (!found) {
										System.out.println("NOT FOUND");
										player.itemToBuyBack.add(it);
									}
//								player.getInventory().add(it.getId(), 1);
							}
							itemsToKeep.clear();
						}
					}
				}*/
				if (dropItems) {
					if (loc == Location.WILDERNESS || loc == Location.WILDKEY_ZONE) {
						if (itemsToKeep != null) {
							for (Item it : itemsToKeep) {
								player.getInventory().add(it.getId(), 1);
							}
							itemsToKeep.clear();
						}
					}
				}
				//if (death != null) {
					//World.deregister(death);
				//}
				player.restart();
				player.getUpdateFlag().flag(Flag.APPEARANCE);
				loc.onDeath(player);
				player.moveTo(new Position(3094 + Misc.random(1), 3478 + Misc.random(5), 0));
				if (player.getLocation().equals(Location.DUEL_ARENA)) {
					player.moveTo(new Position(3368 + Misc.getRandom(5), 3267 + Misc.getRandom(3)));
				} else if (player.getLocation() == Location.DUNGEONEERING_FLOOR_1) {
					player.moveTo(new Position(3277, 9171, player.getDungeoneering().getZ()));
				} else if (player.getPosition().equals(oldPosition)) {
					player.moveTo(new Position(3094 + Misc.random(1), 3478 + Misc.random(3), 0));
				}
				player = null;
				oldPosition = null;
				stop();
				break;
			}
			ticks--;
		} catch (Exception e) {
			setEventRunning(false);
			e.printStackTrace();
			if (player != null) {
					player.moveTo(GameSettings.DEFAULT_POSITION_EDGEVILLE.copy());
					player.setConstitution(player.getSkillManager().getMaxLevel(Skill.CONSTITUTION) + player.getEquipment().getBoost());
			}
		}
	}

	public static NPC getDeathNpc(Player player) {
		NPC death = new NPC(2862, new Position(player.getPosition().getX() + 1, player.getPosition().getY() + 1,
				player.getPosition().getZ()));
		World.register(death);
		death.setEntityInteraction(player);
		death.performAnimation(new Animation(401));
		death.forceChat(randomDeath(player.getUsername()));
		return death;
	}

	public static void deletePvpArmour(Player player) {
		for (int i = 0; i < ItemDefinition.getMaxAmountOfItems(); i++) {
			if (ItemDefinition.forId(i).getName().toLowerCase().contains("(deg)")) {
				player.getEquipment().delete(i, 1);
				player.getInventory().delete(i, 1);
			}
		}
	}

	public static String randomDeath(String name) {
		switch (Misc.getRandom(8)) {
		case 0:
			return "There is no escape, " + Misc.formatText(name) + "...";
		case 1:
			return "Muahahahaha!";
		case 2:
			return "You belong to me!";
		case 3:
			return "Beware mortals, " + Misc.formatText(name) + " travels with me!";
		case 4:
			return "Your time here is over, " + Misc.formatText(name) + "!";
		case 5:
			return "Now is the time you die, " + Misc.formatText(name) + "!";
		case 6:
			return "I claim " + Misc.formatText(name) + " as my own!";
		case 7:
			return "" + Misc.formatText(name) + " is mine!";
		case 8:
			return "Let me escort you back to Edgeville, " + Misc.formatText(name) + "!";
		case 9:
			return "I have come for you, " + Misc.formatText(name) + "!";
		}
		return "";
	}

}
