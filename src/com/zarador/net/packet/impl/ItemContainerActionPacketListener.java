package com.zarador.net.packet.impl;

import com.zarador.GameSettings;
import com.zarador.model.Flag;
import com.zarador.model.Item;
import com.zarador.model.Position;
import com.zarador.model.Locations.Location;
import com.zarador.model.container.impl.Bank;
import com.zarador.model.container.impl.BeastOfBurden;
import com.zarador.model.container.impl.Equipment;
import com.zarador.model.container.impl.PriceChecker;
import com.zarador.model.container.impl.Shop;
import com.zarador.model.definitions.ItemDefinition;
import com.zarador.model.definitions.WeaponAnimations;
import com.zarador.model.definitions.WeaponInterfaces;
import com.zarador.model.input.impl.EnterAmountToBank;
import com.zarador.model.input.impl.EnterAmountToBuyFromPos;
import com.zarador.model.input.impl.EnterAmountToBuyFromShop;
import com.zarador.model.input.impl.EnterAmountToPriceCheck;
import com.zarador.model.input.impl.EnterAmountToRemoveFromBank;
import com.zarador.model.input.impl.EnterAmountToRemoveFromBob;
import com.zarador.model.input.impl.EnterAmountToRemoveFromPriceCheck;
import com.zarador.model.input.impl.EnterAmountToRemoveFromStake;
import com.zarador.model.input.impl.EnterAmountToRemoveFromTrade;
import com.zarador.model.input.impl.EnterAmountToSellToPos;
import com.zarador.model.input.impl.EnterAmountToSellToShop;
import com.zarador.model.input.impl.EnterAmountToStake;
import com.zarador.model.input.impl.EnterAmountToStore;
import com.zarador.model.input.impl.EnterAmountToTrade;
import com.zarador.net.packet.Packet;
import com.zarador.net.packet.PacketListener;
import com.zarador.world.content.BonusManager;
import com.zarador.world.content.Trading;
import com.zarador.world.content.combat.CombatFactory;
import com.zarador.world.content.combat.magic.Autocasting;
import com.zarador.world.content.combat.weapon.CombatSpecial;
import com.zarador.world.content.minigames.impl.Dueling;
import com.zarador.world.content.minigames.impl.Dueling.DuelRule;
import com.zarador.world.content.skill.impl.smithing.EquipmentMaking;
import com.zarador.world.content.skill.impl.smithing.SmithingData;
import com.zarador.world.content.transportation.TeleportHandler;
import com.zarador.world.content.transportation.TeleportType;
import com.zarador.world.content.transportation.jewelry.CombatTeleporting;
import com.zarador.world.content.transportation.jewelry.GloryTeleporting;
import com.zarador.world.entity.impl.player.Player;

public class ItemContainerActionPacketListener implements PacketListener {

	/**
	 * Manages an item's first action.
	 *
	 * @param player
	 *            The player clicking the item.
	 * @param packet
	 *            The packet to read values from.
	 */
	private static void firstAction(Player player, Packet packet) {
		int interfaceId = packet.readShortA();
		int slot = packet.readShortA();
		int id = packet.readShortA();
		Item item = new Item(id);
		if (GameSettings.DEBUG_MODE) {
			// PlayerLogs.log(player,
			// "" + player.getUsername() + " in
			// ItemContainerActionPacketListener: First Action - " + id
			// + " - " + interfaceId + " - " + slot + "");
		}
		switch (interfaceId) {
			//Drop generator interfaces
			case -22933:
			case -22932:
			case -22931:
			case -22930:
			case -22929:
			case -22928:
			case -22927:
			case -22926:
			case -22925:
			case -22924:
			case -22923:
			case -22922:
			case -22921:
			case -22920:
			case -22919:
			case -22918:
			case -22917:
			case -22916:
			case -22915:
			case -22914:
				player.getDropGenerator().checkChance(player, id);
				break;
		case Trading.INTERFACE_ID:
			if (player.getTrading().inTrade()) {
				player.getTrading().tradeItem(id, 1, slot);
			} else if (Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) {
				player.getDueling().stakeItem(id, 1, slot);
			}
			break;
		case Trading.INTERFACE_REMOVAL_ID:
			if (player.getTrading().inTrade())
				player.getTrading().removeTradedItem(id, 1);
			break;
		case Dueling.INTERFACE_REMOVAL_ID:
			if (Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) {
				player.getDueling().removeStakedItem(id, 1);
				return;
			}
			break;
		case Equipment.INVENTORY_INTERFACE_ID:
			item = slot < 0 ? null : player.getEquipment().getItems()[slot];
			if (item == null || item.getId() != id)
				return;

			if (player.getLocation() == Location.DUEL_ARENA) {
				if (player.getDueling().selectedDuelRules[DuelRule.LOCK_WEAPON.ordinal()]) {
					if (item.getDefinition().getEquipmentSlot() == Equipment.WEAPON_SLOT
							|| item.getDefinition().isTwoHanded()) {
						player.getPacketSender().sendMessage("Weapons have been locked during this duel!");
						return;
					}
				}
			}
			boolean stackItem = item.getDefinition().isStackable() && player.getInventory().getAmount(item.getId()) > 0;
			int inventorySlot = player.getInventory().getEmptySlot();
			if (inventorySlot != -1) {
				Item itemReplacement = new Item(-1, 0);
				player.getEquipment().setItem(slot, itemReplacement);
				if (!stackItem)
					player.getInventory().setItem(inventorySlot, item);
				else
					player.getInventory().add(item.getId(), item.getAmount());
				BonusManager.update(player);
				if (item.getDefinition().getEquipmentSlot() == Equipment.WEAPON_SLOT) {
					WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
					WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
					if (player.getAutocastSpell() != null || player.isAutocast()) {
						Autocasting.resetAutocast(player, true);
						player.getPacketSender().sendMessage("Autocast spell cleared.");
					}
					player.setSpecialActivated(false);
					CombatSpecial.updateBar(player);
					if (player.hasStaffOfLightEffect()) {
						player.setStaffOfLightEffect(-1);
						player.getPacketSender()
								.sendMessage("You feel the spirit of the Staff of Light begin to fade away...");
					}
				}
				player.getEquipment().refreshItems();
				player.getInventory().refreshItems();
				player.getUpdateFlag().flag(Flag.APPEARANCE);
			} else {
				player.getInventory().full();
			}
			break;
		case Bank.INTERFACE_ID:
			if (!player.isBanking() || player.getInterfaceId() != 5292)
				break;
			player.getBank(player.getCurrentBankTab()).switchItem(player.getInventory(), item, slot, true, true);
			player.getBank(player.getCurrentBankTab()).open();
			break;
		case Bank.INVENTORY_INTERFACE_ID:
			if (!player.isBanking() || !player.getInventory().contains(item.getId())
					|| player.getInterfaceId() != 5292) {
				return;
			}
			player.setCurrentBankTab(Bank.getTabForItem(player, item.getId()));
			player.getInventory().switchItem(player.getBank(player.getCurrentBankTab()), item, slot, false, true);
			break;
		case Shop.ITEM_CHILD_ID:
			if (player.getShop() != null) {
				player.getShop().checkValue(player, slot, false);
			} else if (player.getPlayerOwnedShop() != null) {
				player.getPlayerOwnedShop().checkValue(player, slot, false);
				return;
			}
			break;
		case Shop.INVENTORY_INTERFACE_ID:
			if (player.getShop() != null)
				player.getShop().checkValue(player, slot, true);
			break;
		case BeastOfBurden.INTERFACE_ID:
			if (player.getInterfaceId() == BeastOfBurden.INTERFACE_ID
					&& player.getSummoning().getBeastOfBurden() != null) {
				if (item.getDefinition().isStackable()) {
					player.getPacketSender().sendMessage("You cannot store stackable items.");
					return;
				}
				player.getInventory().switchItem(player.getSummoning().getBeastOfBurden(), item, slot, false, true);
			}
			break;
		case PriceChecker.INTERFACE_PC_ID:
			if (player.getInterfaceId() == PriceChecker.INTERFACE_ID && player.getPriceChecker().isOpen()) {
				player.getInventory().switchItem(player.getPriceChecker(), item, slot, false, true);
			}
			break;
		case 1119: // smithing interface row 1
		case 1120: // row 2
		case 1121: // row 3
		case 1122: // row 4
		case 1123: // row 5
			int barsRequired = SmithingData.getBarAmount(item);
			Item bar = new Item(player.getSelectedSkillingItem(), barsRequired);
			int x = 1;
			if (x > (player.getInventory().getAmount(bar.getId()) / barsRequired))
				x = (player.getInventory().getAmount(bar.getId()) / barsRequired);
			for (int i : GameSettings.SMITHABLE_ITEMS) {
				if (item.getId() == i) {
					EquipmentMaking.smithItem(player, new Item(player.getSelectedSkillingItem(), barsRequired),
							new Item(item.getId(), SmithingData.getItemAmount(item)), x);
					break;
				}
			}

			if (BeastOfBurden.beastOfBurdenSlot(interfaceId) >= 0) {
				if (player.getInterfaceId() == BeastOfBurden.INTERFACE_ID
						&& player.getSummoning().getBeastOfBurden() != null) {
					player.getSummoning().getBeastOfBurden().switchItem(player.getInventory(), item,
							BeastOfBurden.beastOfBurdenSlot(interfaceId), false, true);
				}
			} else if (PriceChecker.priceCheckerSlot(interfaceId) >= 0) {
				if (player.getPriceChecker().isOpen()) {
					player.getPriceChecker().switchItem(player.getInventory(), new Item(id, 1),
							PriceChecker.priceCheckerSlot(interfaceId), false, true);
				}
			}
		}
	}

	/**
	 * Manages an item's second action.
	 *
	 * @param player
	 *            The player clicking the item.
	 * @param packet
	 *            The packet to read values from.
	 */
	private static void secondAction(Player player, Packet packet) {
		int interfaceId = packet.readLEShortA();
		int id = packet.readLEShortA();
		int slot = packet.readLEShort();
		if (GameSettings.DEBUG_MODE) {
			// PlayerLogs.log(player,
			// "" + player.getUsername() + " in
			// ItemContainerActionPacketListener: Second Action - " + id
			// + " - " + interfaceId + " - " + slot + "");
		}
		Item item = new Item(id);
		switch (interfaceId) {
		case Trading.INTERFACE_ID:
			if (player.getTrading().inTrade()) {
				player.getTrading().tradeItem(id, 5, slot);
			} else if (Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) {
				player.getDueling().stakeItem(id, 5, slot);
			}
			break;
		case Trading.INTERFACE_REMOVAL_ID:
			if (player.getTrading().inTrade())
				player.getTrading().removeTradedItem(id, 5);
			break;
		case Dueling.INTERFACE_REMOVAL_ID:
			if (Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) {
				player.getDueling().removeStakedItem(id, 5);
				return;
			}
			break;
		case Bank.INTERFACE_ID:
			if (!player.isBanking() || item.getId() != id || player.getInterfaceId() != 5292)
				return;
			player.getBank(player.getCurrentBankTab()).switchItem(player.getInventory(), new Item(id, 5), slot, true,
					true);
			player.getBank(player.getCurrentBankTab()).open();
			break;
		case Bank.INVENTORY_INTERFACE_ID:
			item = player.getInventory().forSlot(slot).copy().setAmount(5).copy();
			if (!player.isBanking() || item.getId() != id || !player.getInventory().contains(item.getId())
					|| player.getInterfaceId() != 5292)
				return;
			player.setCurrentBankTab(Bank.getTabForItem(player, item.getId()));
			player.getInventory().switchItem(player.getBank(player.getCurrentBankTab()), item, slot, false, true);
			break;
		case Shop.ITEM_CHILD_ID:
			if (player.getShop() != null) {
				item = player.getShop().forSlot(slot).copy().setAmount(1).copy();
				player.getShop().setPlayer(player).switchItem(player.getInventory(), item, slot, false, true);
			} else if (player.getPlayerOwnedShop() != null) {
				item = player.getPlayerOwnedShop().forSlot(slot).copy().setAmount(1).copy();
				player.getPlayerOwnedShop().setPlayer(player).switchItem(player.getInventory(), item, slot, false,
						true);
			}
			break;
		case Shop.INVENTORY_INTERFACE_ID:
			if (player.isShopping()) {
				player.getShop().sellItem(player, slot, 1);
				return;
			} else if (player.isPlayerOwnedShopping()) {
				player.getPlayerOwnedShop().sellItem(player, slot, 1, 0);
				return;
			}
			break;
		case BeastOfBurden.INTERFACE_ID:
			if (player.getInterfaceId() == BeastOfBurden.INTERFACE_ID
					&& player.getSummoning().getBeastOfBurden() != null) {
				if (item.getDefinition().isStackable()) {
					player.getPacketSender().sendMessage("You cannot store stackable items.");
					return;
				}
				player.getInventory().switchItem(player.getSummoning().getBeastOfBurden(), new Item(id, 5), slot, false,
						true);
			}
			break;
		case PriceChecker.INTERFACE_PC_ID:
			if (player.getInterfaceId() == PriceChecker.INTERFACE_ID && player.getPriceChecker().isOpen()) {
				player.getInventory().switchItem(player.getPriceChecker(), new Item(id, 5), slot, false, true);
			}
			break;
		case 1119: // smithing interface row 1
		case 1120: // row 2
		case 1121: // row 3
		case 1122: // row 4
		case 1123: // row 5
			int barsRequired = SmithingData.getBarAmount(item);
			Item bar = new Item(player.getSelectedSkillingItem(), barsRequired);
			int x = 5;
			if (x > (player.getInventory().getAmount(bar.getId()) / barsRequired))
				x = (player.getInventory().getAmount(bar.getId()) / barsRequired);
			for (int i : GameSettings.SMITHABLE_ITEMS) {
				if (item.getId() == i) {
					EquipmentMaking.smithItem(player, new Item(player.getSelectedSkillingItem(), barsRequired),
							new Item(item.getId(), SmithingData.getItemAmount(item)), x);
					break;
				}
			}

			if (BeastOfBurden.beastOfBurdenSlot(interfaceId) >= 0) {
				if (player.getInterfaceId() == BeastOfBurden.INTERFACE_ID
						&& player.getSummoning().getBeastOfBurden() != null) {
					player.getSummoning().getBeastOfBurden().switchItem(player.getInventory(), new Item(id, 5),
							BeastOfBurden.beastOfBurdenSlot(interfaceId), false, true);
				}
			} else if (PriceChecker.priceCheckerSlot(interfaceId) >= 0) {
				if (player.getPriceChecker().isOpen()) {
					player.getPriceChecker().switchItem(player.getInventory(), new Item(id, 5),
							PriceChecker.priceCheckerSlot(interfaceId), false, true);
				}
			}
		}
	}

	/**
	 * Manages an item's third action.
	 *
	 * @param player
	 *            The player clicking the item.
	 * @param packet
	 *            The packet to read values from.
	 */
	private static void thirdAction(Player player, Packet packet) {
		int interfaceId = packet.readLEShort();
		int id = packet.readShortA();
		int slot = packet.readShortA();
		if (GameSettings.DEBUG_MODE) {
			// PlayerLogs.log(player,
			// "" + player.getUsername() + " in
			// ItemContainerActionPacketListener: Third Action - " + id
			// + " - " + interfaceId + " - " + slot + "");
		}
		Item item1 = new Item(id);
		switch (interfaceId) {
		case Equipment.INVENTORY_INTERFACE_ID:
			if (!player.getEquipment().contains(id))
				return;
			switch (id) {
			case 1712:
			case 1710:
			case 1708:
			case 1706:
				GloryTeleporting.rub(player, id);
				break;

			case 11118:
			case 11120:
			case 11122:
			case 11124:
				CombatTeleporting.rub(player, id);
				break;
			case 1704:
				player.getPacketSender().sendMessage("Your amulet has run out of charges.");
				break;
			case 11126:
				player.getPacketSender().sendMessage("Your bracelet has run out of charges.");
				break;
				case 15707:
				case 19709:
					TeleportHandler.teleportPlayer(player, new Position(3450, 3715), TeleportType.DUNGEONEERING, false);
					break;
			case 11283:
			case 11284:
				int charges = player.getDfsCharges();
				if (!player.getDragonfireShield().elapsed(30000)) {
					player.getPacketSender().sendMessage("Your dragonfire shield needs to cool down.");
					return;
				}
				if (charges >= 1) {
					if (player.getCombatBuilder().isAttacking()) {
						if(player.getCombatBuilder().getVictim().isPlayer()) {
							Player targetPlayer = (Player) player.getCombatBuilder().getVictim();
							if(targetPlayer.getLocation() != Location.WILDERNESS) {
								player.getPacketSender().sendMessage("Your opponent is not in the wilderness.");
								return;
							}
							int combatDifference = CombatFactory.combatLevelDifference(player.getSkillManager().getCombatLevel(),
									targetPlayer.getSkillManager().getCombatLevel());
							if (combatDifference > player.getWildernessLevel() || combatDifference > targetPlayer.getWildernessLevel()) {
								player.getPacketSender()
										.sendMessage("Your combat level difference is too great to attack that player here.");
								player.getWalkingQueue().clear();
								return;
							}
						}
						CombatFactory.handleDragonFireShield(player, player.getCombatBuilder().getVictim());
					} else
						player.getPacketSender().sendMessage("You can only use this in combat.");
				} else
					player.getPacketSender().sendMessage("Your shield doesn't have enough power yet. It has "
							+ player.getDfsCharges() + "/50 dragon-fire charges.");
				break;
			}
			break;
		case Trading.INTERFACE_ID:
			if (player.getTrading().inTrade()) {
				player.getTrading().tradeItem(id, 10, slot);
			} else if (Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) {
				player.getDueling().stakeItem(id, 10, slot);
			}
			break;
		case Trading.INTERFACE_REMOVAL_ID:
			if (player.getTrading().inTrade())
				player.getTrading().removeTradedItem(id, 10);
			break;
		case Dueling.INTERFACE_REMOVAL_ID:
			if (Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) {
				player.getDueling().removeStakedItem(id, 10);
				return;
			}
			break;
		case Bank.INTERFACE_ID:
			if (!player.isBanking() || player.getInterfaceId() != 5292)
				return;
			player.getBank(player.getCurrentBankTab()).switchItem(player.getInventory(), new Item(id, 10), slot, true,
					true);
			player.getBank(player.getCurrentBankTab()).open();
			break;
		case Bank.INVENTORY_INTERFACE_ID:
			Item item = player.getInventory().forSlot(slot).copy().setAmount(10).copy();
			if (!player.isBanking() || item.getId() != id || !player.getInventory().contains(item.getId())
					|| player.getInterfaceId() != 5292)
				return;
			player.setCurrentBankTab(Bank.getTabForItem(player, item.getId()));
			player.getInventory().switchItem(player.getBank(player.getCurrentBankTab()), item, slot, false, true);
			break;
		case Shop.ITEM_CHILD_ID:
			if (player.getShop() != null) {
				item = player.getShop().forSlot(slot).copy().setAmount(5).copy();
				player.getShop().setPlayer(player).switchItem(player.getInventory(), item, slot, false, true);
			} else if (player.getPlayerOwnedShop() != null) {
				item = player.getPlayerOwnedShop().forSlot(slot).copy().setAmount(5).copy();
				player.getPlayerOwnedShop().setPlayer(player).switchItem(player.getInventory(), item, slot, false,
						true);
			}
			break;
		case Shop.INVENTORY_INTERFACE_ID:
			if (player.isShopping()) {
				player.getShop().sellItem(player, slot, 5);
				return;
			} else if (player.isPlayerOwnedShopping()) {
				player.getPlayerOwnedShop().sellItem(player, slot, 5, 0);
				return;
			}
			break;
		case BeastOfBurden.INTERFACE_ID:
			if (player.getInterfaceId() == BeastOfBurden.INTERFACE_ID
					&& player.getSummoning().getBeastOfBurden() != null) {
				Item storeItem = new Item(id, 10);
				if (storeItem.getDefinition().isStackable()) {
					player.getPacketSender().sendMessage("You cannot store stackable items.");
					return;
				}
				player.getInventory().switchItem(player.getSummoning().getBeastOfBurden(), storeItem, slot, false,
						true);
			}
			break;
		case PriceChecker.INTERFACE_PC_ID:
			if (player.getInterfaceId() == PriceChecker.INTERFACE_ID && player.getPriceChecker().isOpen()) {
				player.getInventory().switchItem(player.getPriceChecker(), new Item(id, 10), slot, false, true);
			}
			break;

		case 1119: // smithing interface row 1
		case 1120: // row 2
		case 1121: // row 3
		case 1122: // row 4
		case 1123: // row 5
			int barsRequired = SmithingData.getBarAmount(item1);
			Item bar = new Item(player.getSelectedSkillingItem(), barsRequired);
			int x = 10;
			if (x > (player.getInventory().getAmount(bar.getId()) / barsRequired))
				x = (player.getInventory().getAmount(bar.getId()) / barsRequired);
			for (int i : GameSettings.SMITHABLE_ITEMS) {
				if (item1.getId() == i) {
					EquipmentMaking.smithItem(player, new Item(player.getSelectedSkillingItem(), barsRequired),
							new Item(item1.getId(), SmithingData.getItemAmount(item1)), x);
					break;
				}
			}

			if (BeastOfBurden.beastOfBurdenSlot(interfaceId) >= 0) {
				if (player.getInterfaceId() == BeastOfBurden.INTERFACE_ID
						&& player.getSummoning().getBeastOfBurden() != null) {
					player.getSummoning().getBeastOfBurden().switchItem(player.getInventory(), new Item(id, 10),
							BeastOfBurden.beastOfBurdenSlot(interfaceId), false, true);
				}
			} else if (PriceChecker.priceCheckerSlot(interfaceId) >= 0) {
				if (player.getPriceChecker().isOpen()) {
					player.getPriceChecker().switchItem(player.getInventory(), new Item(id, 10),
							PriceChecker.priceCheckerSlot(interfaceId), false, true);
				}
			}
		}
	}

	/**
	 * Manages an item's fourth action.
	 *
	 * @param player
	 *            The player clicking the item.
	 * @param packet
	 *            The packet to read values from.
	 */
	private static void fourthAction(Player player, Packet packet) {
		int slot = packet.readShortA();
		int interfaceId = packet.readShort();
		int id = packet.readShortA();
		if (GameSettings.DEBUG_MODE) {
			// PlayerLogs.log(player,
			// "" + player.getUsername() + " in
			// ItemContainerActionPacketListener: Fourth Action - " + id
			// + " - " + interfaceId + " - " + slot + "");
		}
		switch (interfaceId) {
		case Trading.INTERFACE_ID:
			if (player.getTrading().inTrade()) {
				player.getTrading().tradeItem(id, getAmount(player, id, slot), slot);
			} else if (Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) {
				player.getDueling().stakeItem(id, getAmount(player, id, slot), slot);
			}
			break;
		case Trading.INTERFACE_REMOVAL_ID:
			if (player.getTrading().inTrade()) {
				for (Item item : player.getTrading().offeredItems) {
					if (item != null && item.getId() == id) {
						player.getTrading().removeTradedItem(id, item.getAmount());
						if (ItemDefinition.forId(id) != null && ItemDefinition.forId(id).isStackable())
							break;
					}
				}
			}
			break;
		case Dueling.INTERFACE_REMOVAL_ID:
			if (Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) {
				for (Item item : player.getDueling().stakedItems) {
					if (item != null && item.getId() == id) {
						player.getDueling().removeStakedItem(id, item.getAmount());
						if (ItemDefinition.forId(id) != null && ItemDefinition.forId(id).isStackable())
							break;
					}
				}
			}
			break;
		case Bank.INTERFACE_ID:
			if (!player.isBanking() || player.getBank(Bank.getTabForItem(player, id)).getAmount(id) <= 0
					|| player.getInterfaceId() != 5292)
				return;
			player.getBank(player.getCurrentBankTab()).switchItem(player.getInventory(),
					new Item(id, player.getBank(Bank.getTabForItem(player, id)).getAmount(id)), slot, true, true);
			player.getBank(player.getCurrentBankTab()).open();
			break;
		case Bank.INVENTORY_INTERFACE_ID:
			Item item = player.getInventory().forSlot(slot).copy().setAmount(getAmount(player, id, slot));
			if (!player.isBanking() || item.getId() != id || !player.getInventory().contains(item.getId())
					|| player.getInterfaceId() != 5292)
				return;
			player.setCurrentBankTab(Bank.getTabForItem(player, item.getId()));
			player.getInventory().switchItem(player.getBank(player.getCurrentBankTab()), item, slot, false, true);
			break;
		case Shop.ITEM_CHILD_ID:
			if (player.getShop() != null) {
				item = player.getShop().forSlot(slot).copy().setAmount(10).copy();
				player.getShop().setPlayer(player).switchItem(player.getInventory(), item, slot, false, true);
			} else if (player.getPlayerOwnedShop() != null) {
				item = player.getPlayerOwnedShop().forSlot(slot).copy().setAmount(10).copy();
				player.getPlayerOwnedShop().setPlayer(player).switchItem(player.getInventory(), item, slot, false,
						true);
			}
			break;
		case Shop.INVENTORY_INTERFACE_ID:
			if (player.isShopping()) {
				player.getShop().sellItem(player, slot, 10);
				return;
			} else if (player.isPlayerOwnedShopping()) {
				player.getPlayerOwnedShop().sellItem(player, slot, 10, 0);
				return;
			}
			break;
		case BeastOfBurden.INTERFACE_ID:
			if (player.getInterfaceId() == BeastOfBurden.INTERFACE_ID
					&& player.getSummoning().getBeastOfBurden() != null) {
				Item storeItem = new Item(id, 29);
				if (storeItem.getDefinition().isStackable()) {
					player.getPacketSender().sendMessage("You cannot store stackable items.");
					return;
				}
				player.getInventory().switchItem(player.getSummoning().getBeastOfBurden(), storeItem, slot, false,
						true);
			}
			break;
		case PriceChecker.INTERFACE_PC_ID:
			if (player.getInterfaceId() == PriceChecker.INTERFACE_ID && player.getPriceChecker().isOpen()) {
				player.getInventory().switchItem(player.getPriceChecker(),
						new Item(id, getAmount(player, id, slot)), slot, false, true);
			}
			break;
		}

		if (BeastOfBurden.beastOfBurdenSlot(interfaceId) >= 0) {
			if (player.getInterfaceId() == BeastOfBurden.INTERFACE_ID
					&& player.getSummoning().getBeastOfBurden() != null) {
				player.getSummoning().getBeastOfBurden().switchItem(player.getInventory(), new Item(id, 29),
						BeastOfBurden.beastOfBurdenSlot(interfaceId), false, true);
			}
		} else if (PriceChecker.priceCheckerSlot(interfaceId) >= 0) {
			if (player.getPriceChecker().isOpen()) {
				player.getPriceChecker().switchItem(player.getInventory(),
						new Item(id, player.getPriceChecker().getAmount(id)),
						PriceChecker.priceCheckerSlot(interfaceId), false, true);
			}
		}
	}

	/**
	 * Manages an item's fifth action.
	 *
	 * @param player
	 *            The player clicking the item.
	 * @param packet
	 *            The packet to read values from.
	 */
	private static void fifthAction(Player player, Packet packet) {
		int slot = packet.readLEShort();
		int interfaceId = packet.readShortA();
		int id = packet.readLEShort();
		if (GameSettings.DEBUG_MODE) {
			// PlayerLogs.log(player,
			// "" + player.getUsername() + " in
			// ItemContainerActionPacketListener: Fifth Action - " + id
			// + " - " + interfaceId + " - " + slot + "");
		}
		switch (interfaceId) {
		case Trading.INTERFACE_ID:
			if (player.getTrading().inTrade()) {
				player.setInputHandling(new EnterAmountToTrade(id, slot));
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to trade?");
			} else if (Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) {
				player.setInputHandling(new EnterAmountToStake(id, slot));
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to stake?");
			}
			break;
		case Trading.INTERFACE_REMOVAL_ID:
			if (player.getTrading().inTrade()) {
				player.setInputHandling(new EnterAmountToRemoveFromTrade(id));
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to remove?");
			}
			break;
		case Dueling.INTERFACE_REMOVAL_ID:
			if (Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) {
				player.setInputHandling(new EnterAmountToRemoveFromStake(id));
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to remove?");
			}
			break;
		case Bank.INVENTORY_INTERFACE_ID: // BANK X
			if (player.isBanking()) {
				player.setInputHandling(new EnterAmountToBank(id, slot));
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to bank?");
			}
			break;
		case Bank.INTERFACE_ID:
			if (player.isBanking()) {
				if (interfaceId == 11) {
					player.setInputHandling(new EnterAmountToRemoveFromBank(id, slot));
					player.getPacketSender().sendEnterAmountPrompt("How many would you like to withdraw?");
				} else {
					if(player.getWithdrawX() > 0) {
						player.getBank(player.getCurrentBankTab()).switchItem(player.getInventory(),
								new Item(id, player.getWithdrawX()), slot, true,
								true);
						player.getBank(player.getCurrentBankTab()).open();
					} else {
						player.getBank(player.getCurrentBankTab()).switchItem(player.getInventory(),
								new Item(id, player.getBank(Bank.getTabForItem(player, id)).getAmount(id) - 1), slot, true,
								true);
						player.getBank(player.getCurrentBankTab()).open();
					}
				}
			}
			break;
		case Shop.ITEM_CHILD_ID:
			if (player.isBanking())
				return;
			if (player.isShopping()) {
				player.setInputHandling(new EnterAmountToBuyFromShop(id, slot));
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to buy?");
				player.getShop().setPlayer(player);
			} else if (player.isPlayerOwnedShopping()) {
				player.setInputHandling(new EnterAmountToBuyFromPos(id, slot));
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to buy?");
				player.getPlayerOwnedShop().setPlayer(player);
			}
			break;
		case Shop.INVENTORY_INTERFACE_ID:
			if (player.isBanking())
				return;
			if (player.isShopping()) {
				player.setInputHandling(new EnterAmountToSellToShop(id, slot));
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to sell?");
				player.getShop().setPlayer(player);
			} else if (player.isPlayerOwnedShopping()) {
				player.setInputHandling(new EnterAmountToSellToPos(id, slot));
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to sell?");
				return;
			}
			break;
		case PriceChecker.INTERFACE_PC_ID:
			if (player.getInterfaceId() == PriceChecker.INTERFACE_ID && player.getPriceChecker().isOpen()) {
				player.setInputHandling(new EnterAmountToPriceCheck(id, slot));
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to pricecheck?");
			}
			break;
		case BeastOfBurden.INTERFACE_ID:
			if (player.getInterfaceId() == BeastOfBurden.INTERFACE_ID
					&& player.getSummoning().getBeastOfBurden() != null) {
				Item storeItem = new Item(id, 10);
				if (storeItem.getDefinition().isStackable()) {
					player.getPacketSender().sendMessage("You cannot store stackable items.");
					return;
				}
				player.setInputHandling(new EnterAmountToStore(id, slot));
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to store?");
			}
			break;
		}

		if (BeastOfBurden.beastOfBurdenSlot(interfaceId) >= 0) {
			if (player.getInterfaceId() == BeastOfBurden.INTERFACE_ID
					&& player.getSummoning().getBeastOfBurden() != null) {
				player.setInputHandling(new EnterAmountToRemoveFromBob(id, slot));
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to remove?");
			}
		} else if (PriceChecker.priceCheckerSlot(interfaceId) >= 0) {
			if (player.getPriceChecker().isOpen()) {
				player.setInputHandling(new EnterAmountToRemoveFromPriceCheck(id, slot));
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to remove?");
			}
		}
	}

	private static void sixthAction(Player player, Packet packet) {
		int interfaceId = packet.readShortA();
		int slot = packet.readShortA();
		int id = packet.readShortA();
		if (GameSettings.DEBUG_MODE) {
			// PlayerLogs.log(player,
			// "" + player.getUsername() + " in
			// ItemContainerActionPacketListener: Sixth Action - " + id
			// + " - " + interfaceId + " - " + slot + "");
		}
		switch (interfaceId) {
		case Bank.INTERFACE_ID:
			if (player.isBanking()) {
				player.setInputHandling(new EnterAmountToRemoveFromBank(id, slot));
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to withdraw?");
			}
			break;
		case Shop.INVENTORY_INTERFACE_ID:
			if (player.isShopping()) {
				player.getShop().sellItem(player, slot, player.getInventory().getAmount(player.getInventory().get(slot).getId()));
				return;
			}
			break;
		}
	}

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player.getConstitution() <= 0)
			return;
		switch (packet.getOpcode()) {
		case FIRST_ITEM_ACTION_OPCODE:
			firstAction(player, packet);
			break;
		case SECOND_ITEM_ACTION_OPCODE:
			secondAction(player, packet);
			break;
		case THIRD_ITEM_ACTION_OPCODE:
			thirdAction(player, packet);
			break;
		case FOURTH_ITEM_ACTION_OPCODE:
			fourthAction(player, packet);
			break;
		case FIFTH_ITEM_ACTION_OPCODE:
			fifthAction(player, packet);
			break;
		case SIXTH_ITEM_ACTION_OPCODE:
			sixthAction(player, packet);
			break;
		}
	}

	/**
	 * To fix the dupe
	 */
	public static int getAmount(Player player, int itemId, int slot) {
		if (ItemDefinition.forId(itemId).isStackable()) {
			return player.getInventory().forSlot(slot).getAmount();
		} else {
			return player.getInventory().getAmount(itemId);
		}
	}

	public static final int FIRST_ITEM_ACTION_OPCODE = 145;
	public static final int SECOND_ITEM_ACTION_OPCODE = 117;
	public static final int THIRD_ITEM_ACTION_OPCODE = 43;
	public static final int FOURTH_ITEM_ACTION_OPCODE = 129;
	public static final int FIFTH_ITEM_ACTION_OPCODE = 135;
	public static final int SIXTH_ITEM_ACTION_OPCODE = 138;
}
