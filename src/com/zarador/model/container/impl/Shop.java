package com.zarador.model.container.impl;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.zarador.engine.task.TaskManager;
import com.zarador.engine.task.impl.ShopRestockTask;
import com.zarador.model.Item;
import com.zarador.model.Skill;
import com.zarador.model.container.ItemContainer;
import com.zarador.model.container.StackType;
import com.zarador.model.definitions.ItemDefinition;
import com.zarador.model.input.impl.EnterAmountToBuyFromShop;
import com.zarador.model.input.impl.EnterAmountToSellToShop;
import com.zarador.util.JsonLoader;
import com.zarador.util.Misc;
import com.zarador.world.World;
import com.zarador.world.content.minigames.impl.RecipeForDisaster;
import com.zarador.world.entity.impl.player.Player;

/**
 * Messy but perfect Shop System
 * 
 * @author Gabriel Hannason
 */

public class Shop extends ItemContainer {

	/*
	 * The shop constructor
	 */
	public Shop(Player player, int id, String name, Item currency, Item[] stockItems) {
		super(player);
		if (stockItems.length > 42)
			throw new ArrayIndexOutOfBoundsException(
					"Stock cannot have more than 40 items; check shop[" + id + "]: stockLength: " + stockItems.length);
		this.id = id;
		this.name = name.length() > 0 ? name : "General Store";
		this.currency = currency;
		this.originalStock = new Item[stockItems.length];
		for (int i = 0; i < stockItems.length; i++) {
			Item item = new Item(stockItems[i].getId(), stockItems[i].getAmount());
			add(item, false);
			this.originalStock[i] = item;
		}
	}

	public final int id;

	private String name;

	private Item currency;

	private Item[] originalStock;

	public Item[] getOriginalStock() {
		return this.originalStock;
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return name;
	}

	public Shop setName(String name) {
		this.name = name;
		return this;
	}

	public Item getCurrency() {
		return currency;
	}

	public Shop setCurrency(Item currency) {
		this.currency = currency;
		return this;
	}

	private boolean restockingItems;

	public boolean isRestockingItems() {
		return restockingItems;
	}

	public void setRestockingItems(boolean restockingItems) {
		this.restockingItems = restockingItems;
	}

	/**
	 * Opens a shop for a player
	 * 
	 * @param player
	 *            The player to open the shop for
	 * @return The shop instance
	 */
	public Shop open(Player player) {
		if (player.getGameModeAssistant().isIronMan()) {
			if (id != IRON_SLAYER_STORE && id != IRON_VOTING_REWARDS_STORE && id !=DUNGEONEERING_STORE && id != PEST_CONTROL_STORE
					&& id != SKILLCAPE_STORE_1 && id != SKILLCAPE_STORE_2 && id != SKILLCAPE_STORE_3
					&& id != 11 && id != 27 && id != 15 && id != 17 && id != 18 && id != 21 && id != 22 && id != 24 && id != 26 && id != 27 && id != 28 && id != 30 && id != 32 && id != 33 && id != 38 && id != 39 && id != 35 && id != 34 && id != 40 && id != 43 && id != GENERAL_STORE) {
				player.getPacketSender().sendInterfaceRemoval();
				player.getPacketSender().sendMessage("You're unable to access this shop because you're an iron man.");
				return this;
			}
		}
		setPlayer(player);
		if (id == 46) {
			player.getPacketSender().sendString(41900, "Buy experience");
		} else {
			player.getPacketSender().sendString(41900, "");
		}
		writeInterface(getPlayer(), id);
		getPlayer().getPacketSender().sendInterfaceRemoval().sendClientRightClickRemoval();
		getPlayer().setShop(ShopManager.getShops().get(id)).setInterfaceId(INTERFACE_ID).setShopping(true);
		refreshItems();
		if(id == DONATOR_STORE_ARMOUR_WEAPONS || id == DONATOR_STORE_RARES) {
			getPlayer().getPacketSender().sendMessage("You currently have "+Misc.format(getPlayer().getPoints())+" donator points.");
		}
		if (Misc.getMinutesPlayed(getPlayer()) <= 190)
			getPlayer().getPacketSender()
					.sendMessage("Note: When selling an item to a store, it loses 15% of its original value.");
		return this;
	}

	/**
	 * Refreshes a shop for every player who's viewing it
	 */
	public void publicRefresh() {
		Shop publicShop = ShopManager.getShops().get(id);
		if (publicShop == null)
			return;
		publicShop.setItems(getItems());
		for (Player player : World.getPlayers()) {
			if (player == null)
				continue;
			if (player.getShop() != null && player.getShop().id == id && player.isShopping())
				player.getShop().setItems(publicShop.getItems());
		}
	}

	/**
	 * Checks a value of an item in a shop
	 * 
	 * @param player
	 *            The player who's checking the item's value
	 * @param slot
	 *            The shop item's slot (in the shop!)
	 * @param sellingItem
	 *            Is the player selling the item?
	 */
	public void checkValue(Player player, int slot, boolean sellingItem) {
		this.setPlayer(player);
		Item shopItem = new Item(getItems()[slot].getId());
		if (!player.isShopping()) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}
		Item item = sellingItem ? player.getInventory().getItems()[slot] : getItems()[slot];
		if (item.getId() == 995)
			return;
		if (sellingItem) {
			if (!shopBuysItem(id, item)) {
				player.getPacketSender().sendMessage("You cannot sell this item to this store.");
				return;
			}
		}
		int finalValue = 0;
		String finalString = sellingItem ? "" + ItemDefinition.forId(item.getId()).getName() + ": shop will buy for "
				: "" + ItemDefinition.forId(shopItem.getId()).getName() + " currently costs ";
		if (getCurrency().getId() != -1) {
			finalValue = ItemDefinition.forId(item.getId()).getValue();
			if(item.getId() >= 18349 && item.getId() <=  18363) {
				finalValue *= 10;
			}
			if (item.getId() >= 207 && item.getId() <= 2220 || item.getId() == 3050 || item.getId() == 3052 || item.getId() == 2486) {
				finalValue *= 7;
			}
			if (item.getId() == 21631) {
				finalValue *= 100;
			}
			if (item.getId() == 9242 || item.getId() == 9244) {
				finalValue = 7000;
			}
			String s = currency.getDefinition().getName().toLowerCase().endsWith("s")
					? currency.getDefinition().getName().toLowerCase()
					: currency.getDefinition().getName().toLowerCase() + "s";
			/** CUSTOM CURRENCY, CUSTOM SHOP VALUES **/
			if (id == TOKKUL_EXCHANGE_STORE || id == STARDUST_EXCHANGE_STORE || id == AGILITY_TICKET_STORE
					|| id == ENERGY_FRAGMENT_STORE) {
				Object[] obj = ShopManager.getCustomShopData(id, item.getId());
				if (obj == null)
					return;
				finalValue = (int) obj[0];
				s = (String) obj[1];
			}
			if (sellingItem) {
				if (finalValue != 1) {
					finalValue = (int) (finalValue * 0.85);
				}
			}
			finalString += "" + finalValue + " " + s + "" + shopPriceEx(finalValue) + ".";
		} else {
			Object[] obj = ShopManager.getCustomShopData(id, item.getId());
			if (obj == null)
				return;
			finalValue = (int) obj[0];
			if (sellingItem) {
				if (finalValue != 1) {
					finalValue = (int) (finalValue * 0.85);
				}
			}
			finalString += "" + Misc.format(finalValue) + " " + (String) obj[1] + ".";
		}
		if (player != null && finalValue > 0) {
			player.getPacketSender().sendMessage(finalString);
			return;
		}
	}

	public void sellItem(Player player, int slot, int amountToSell) {
		this.setPlayer(player);
		if (!player.isShopping() || player.isBanking()) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}
		if (id == DONATOR_STORE_ARMOUR_WEAPONS || id == DONATOR_STORE_RARES || id == PKING_REWARDS_STORE || id == PKING_REWARDS_STORE2 || id == PKING_REWARDS_STORE3
				|| id == ENERGY_FRAGMENT_STORE || id == VOTING_REWARDS_STORE || id == VOTING_REWARDS_STORE2) {
			player.getPacketSender().sendMessage("You cannot sell items to this store.");
			return;
		}
		if (!player.isShopping() || player.isBanking()) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}
		Item itemToSell = player.getInventory().getItems()[slot];
		if (itemToSell.getId() == 12183 || itemToSell.getId() == 12530 || itemToSell.getId() == 18016) {
			player.getPacketSender()
					.sendMessage("You can't sell this item to this shop, please exchange shards with pikkupstix.");
			return;
		}
		if (!itemToSell.sellable()) {
			player.getPacketSender().sendMessage("This item cannot be sold.");
			return;
		}
		if (!shopBuysItem(id, itemToSell)) {
			player.getPacketSender().sendMessage("You cannot sell this item to this store.");
			return;
		}
		if (!player.getInventory().contains(itemToSell.getId()) || itemToSell.getId() == 995)
			return;
		if (this.full(itemToSell.getId()))
			return;
		if (player.getInventory().getAmount(itemToSell.getId()) < amountToSell)
			amountToSell = player.getInventory().getAmount(itemToSell.getId());
		if (amountToSell == 0)
			return;
		/*
		 * if(amountToSell > 300) { String s =
		 * ItemDefinition.forId(itemToSell.getId()).getName().endsWith("s") ?
		 * ItemDefinition.forId(itemToSell.getId()).getName() :
		 * ItemDefinition.forId(itemToSell.getId()).getName() + "s";
		 * player.getPacketSender().sendMessage("You can only sell 300 "+s+
		 * " at a time."); return; }
		 */
		int itemId = itemToSell.getId();
		int count = 0;
		boolean customShop = this.getCurrency().getId() == -1;
		boolean inventorySpace = customShop ? true : false;
		if (!customShop) {
			if (!itemToSell.getDefinition().isStackable()) {
				if (!player.getInventory().contains(this.getCurrency().getId()))
					inventorySpace = true;
			}
			if (player.getInventory().getFreeSlots() <= 0
					&& player.getInventory().getAmount(this.getCurrency().getId()) > 0)
				inventorySpace = true;
			if (player.getInventory().getFreeSlots() > 0
					|| player.getInventory().getAmount(this.getCurrency().getId()) > 0)
				inventorySpace = true;
		}
		int itemValue = 0;
		if (getCurrency().getId() > 0) {
			itemValue = ItemDefinition.forId(itemToSell.getId()).getValue();
			if(itemToSell.getId() >= 18349 && itemToSell.getId() <= 18363) {
				itemValue *= 10;
			}
		} else {
			Object[] obj = ShopManager.getCustomShopData(id, itemToSell.getId());
			if (obj == null)
				return;
			itemValue = (int) obj[0];
		}
		if (itemValue <= 0)
			return;
		itemValue = (int) (itemValue * 0.85);
		if (itemValue <= 0) {
			itemValue = 1;
		}
		for (int i = amountToSell; i > 0; i--) {
			itemToSell = new Item(itemId);
			if (this.full(itemToSell.getId()) || !player.getInventory().contains(itemToSell.getId())
					|| !player.isShopping())
				break;
			if (!itemToSell.getDefinition().isStackable()) {
				if (inventorySpace) {
					super.switchItem(player.getInventory(), this, itemToSell.getId(), -1);
					if (!customShop) {
						player.getInventory().add(new Item(getCurrency().getId(), itemValue), false);
						player.save();
					}
				} else {
					player.getPacketSender().sendMessage("Please free some inventory space before doing that.");
					break;
				}
			} else {
				if (inventorySpace) {
					super.switchItem(player.getInventory(), this, itemToSell.getId(), amountToSell);
					if (!customShop) {
						player.getInventory().add(new Item(getCurrency().getId(), itemValue * amountToSell), false);
					}
					break;
				} else {
					player.getPacketSender().sendMessage("Please free some inventory space before doing that.");
					break;
				}
			}
			amountToSell--;
			count++;
		}
		if (customShop) {
			player.getPointsHandler().refreshPanel();
		}
		player.getInventory().refreshItems();
		fireRestockTask();
		refreshItems();
		publicRefresh();
	}

	/**
	 * Buying an item from a shop
	 */
	@Override
	public Shop switchItem(ItemContainer to, Item item, int slot, boolean sort, boolean refresh) {
		final Player player = getPlayer();
		if (player == null)
			return this;
		if (!player.isShopping() || player.isBanking()) {
			player.getPacketSender().sendInterfaceRemoval();
			return this;
		}
		if (this.id == GENERAL_STORE) {
			if (player.getGameModeAssistant().isIronMan()) {
				player.getPacketSender().sendMessage("Ironman-players are not allowed to buy items from the general-store.");
				return this;
			}
		}
		if (!shopSellsItem(item))
			return this;
		if (getItems()[slot].getAmount() <= 1 && id != GENERAL_STORE && id != DIANGO_STORE) {
			player.getPacketSender().sendMessage("The shop has  of stock for this item.");
			return this;
		}
		if (item.getAmount() > getItems()[slot].getAmount())
			item.setAmount(getItems()[slot].getAmount());
		int amountBuying = item.getAmount();
		if (amountBuying == 0)
			return this;
		if (amountBuying > 5000) {
			if (ItemDefinition.forId(item.getId()).getName().endsWith("s")) {
				player.getPacketSender().sendMessage(
						"You can only buy 5000 " + ItemDefinition.forId(item.getId()).getName() + " at a time.");
			} else {
				player.getPacketSender().sendMessage(
						"You can only buy 5000 " + ItemDefinition.forId(item.getId()).getName() + "s at a time.");
			}
			return this;
		}
		boolean customShop = getCurrency().getId() == -1;
		boolean usePouch = false;
		int playerCurrencyAmount = 0;
		int value = ItemDefinition.forId(item.getId()).getValue();
		if(item.getId() >= 18349 && item.getId() <= 18363) {
			value *= 10;
		}
		String currencyName = "";
		if (getCurrency().getId() != -1) {
			playerCurrencyAmount = player.getInventory().getAmount(currency.getId());
			currencyName = ItemDefinition.forId(currency.getId()).getName().toLowerCase();
			if (currency.getId() == 995) {
				if (player.getMoneyInPouch() >= value) {
					playerCurrencyAmount = player.getMoneyInPouchAsInt();
					if (!(player.getInventory().getFreeSlots() == 0
							&& player.getInventory().getAmount(currency.getId()) == value)) {
						usePouch = true;
					}
				}
			} else {
				/** CUSTOM CURRENCY, CUSTOM SHOP VALUES **/
				if (id == TOKKUL_EXCHANGE_STORE || id == STARDUST_EXCHANGE_STORE
						|| id == AGILITY_TICKET_STORE || id == ENERGY_FRAGMENT_STORE) {
					value = (int) ShopManager.getCustomShopData(id, item.getId())[0];
				}
			}
		} else {
			Object[] obj = ShopManager.getCustomShopData(id, item.getId());
			if (obj == null)
				return this;
			value = (int) obj[0];
			currencyName = (String) obj[1];
			if (id == PKING_REWARDS_STORE || id == PKING_REWARDS_STORE2 || id == PKING_REWARDS_STORE3) {
				playerCurrencyAmount = player.getPointsHandler().getPkPoints();
			} else if (id == DONATOR_STORE_ARMOUR_WEAPONS || id == DONATOR_STORE_RARES) {
				playerCurrencyAmount = player.getPoints();
			} else if (id == VOTING_REWARDS_STORE || id == VOTING_REWARDS_STORE2 || id == IRON_VOTING_REWARDS_STORE) {
				playerCurrencyAmount = player.getPointsHandler().getVotingPoints();
			} else if (id == DUNGEONEERING_STORE) {
				playerCurrencyAmount = player.getPointsHandler().getDungeoneeringTokens();
			} else if (id == PEST_CONTROL_STORE) {
				playerCurrencyAmount = player.getPointsHandler().getCommendations();
			} else if (id == SLAYER_STORE || id == IRON_SLAYER_STORE) {
				playerCurrencyAmount = player.getPointsHandler().getSlayerPoints();
			}
		}
		if (value <= 0) {
			return this;
		}
		if (!hasInventorySpace(player, item, getCurrency().getId(), value)) {
			player.getPacketSender().sendMessage("You do not have any free inventory slots.");
			return this;
		}
		if (playerCurrencyAmount <= 0 || playerCurrencyAmount < value) {
			player.getPacketSender()
					.sendMessage("You do not have enough "
							+ ((currencyName.endsWith("s") ? (currencyName) : (currencyName + "s")))
							+ " to purchase this item.");
			return this;
		}
		if (id == SKILLCAPE_STORE_1 || id == SKILLCAPE_STORE_2 || id == SKILLCAPE_STORE_3) {
			for (int i = 0; i < item.getDefinition().getRequirement().length; i++) {
				int req = item.getDefinition().getRequirement()[i];
				if ((i == 3 || i == 5) && req == 99)
					req *= 10;
				if (req > player.getSkillManager().getMaxLevel(i)) {
					player.getPacketSender().sendMessage("You need to have at least level 99 in "
							+ Misc.formatText(Skill.forId(i).toString().toLowerCase()) + " to buy this item.");
					return this;
				}
			}
		}

		for (int i = amountBuying; i > 0; i--) {
			if (!shopSellsItem(item)) {
				break;
			}
			if (getItems()[slot].getAmount() <= 1 && id != GENERAL_STORE & id != DIANGO_STORE) {
				player.getPacketSender().sendMessage("The shop has run out of stock for this item.");
				break;
			}
			if (!item.getDefinition().isStackable()) {
				if (playerCurrencyAmount >= value && hasInventorySpace(player, item, getCurrency().getId(), value)) {
					if (!customShop) {
						if (usePouch) {
							player.setMoneyInPouch((player.getMoneyInPouch() - value));
						} else {
							player.getInventory().delete(currency.getId(), value, false);
						}
					} else {
						if (id == PKING_REWARDS_STORE || id == PKING_REWARDS_STORE2 || id == PKING_REWARDS_STORE3) {
							player.getPointsHandler().setPkPoints(-value, true);
						} else if (id == DONATOR_STORE_ARMOUR_WEAPONS || id == DONATOR_STORE_RARES) {
							player.setPoints(-value, true);
						} else if (id == VOTING_REWARDS_STORE || id == VOTING_REWARDS_STORE2 || id == IRON_VOTING_REWARDS_STORE) {
							player.getPointsHandler().setVotingPoints(-value, true);
						} else if (id == SLAYER_STORE || id == IRON_SLAYER_STORE) {
							player.getPointsHandler().setSlayerPoints(-value, true);
						} else if (id == DUNGEONEERING_STORE) {
							player.getPointsHandler().setDungeoneeringTokens(-value, true);
						} else if (id == PEST_CONTROL_STORE) {
							player.getPointsHandler().setCommendations(-value, true);
						}
						writeInterface(player, id);
					}
					if(id == DIANGO_STORE) {
						//REMOVE FROM SHOP
						for (int j = 0; j < player.itemToBuyBack.size() - 1; j++) {
							//Is the item in the arraylist = to the item being bought?
							if (player.itemToBuyBack.get(i).getId() == item.getId()) {
								System.out.println(player.itemToBuyBack.get(i).getId() + " x" + player.itemToBuyBack.get(i).getAmount());
								System.out.println(item.toString() + " x" + item.getAmount());
								player.itemToBuyBack.get(i).setAmount(player.itemToBuyBack.get(i).getAmount() - item.getAmount());
								if (player.itemToBuyBack.get(i).getAmount() < 1) {
									player.itemToBuyBack.remove(i);
								}
//								player.openUnTradeableShop(player, player.itemToBuyBack);
							}
						}
					}

					super.switchItem(to, new Item(item.getId(), 1), slot, false, false);

					playerCurrencyAmount -= value;
				} else {
					break;
				}
			} else {
				if (playerCurrencyAmount >= value && hasInventorySpace(player, item, getCurrency().getId(), value)) {

					int canBeBought = playerCurrencyAmount / (value);
					if (canBeBought >= amountBuying) {
						canBeBought = amountBuying;
					}
					if (canBeBought == 0)
						break;

					if (!customShop) {
						if (usePouch) {
							player.setMoneyInPouch((player.getMoneyInPouch() - (value * canBeBought)));
						} else {
							player.getInventory().delete(currency.getId(), value * canBeBought, false);
						}
					} else {
						if (id == PKING_REWARDS_STORE || id == PKING_REWARDS_STORE2 || id == PKING_REWARDS_STORE3) {
							player.getPointsHandler().setPkPoints(-value * canBeBought, true);
						} else if (id == DONATOR_STORE_ARMOUR_WEAPONS || id == DONATOR_STORE_RARES) {
							player.setPoints(-value * canBeBought, true);
						} else if (id == VOTING_REWARDS_STORE || id == VOTING_REWARDS_STORE2 || id == IRON_VOTING_REWARDS_STORE) {
							player.getPointsHandler().setVotingPoints(-value * canBeBought, true);
						} else if (id == DUNGEONEERING_STORE) {
							player.getPointsHandler().setDungeoneeringTokens(-value * canBeBought, true);
						} else if (id == PEST_CONTROL_STORE) {
							player.getPointsHandler().setCommendations(-value * canBeBought, true);
						} else if (id == SLAYER_STORE || id == IRON_SLAYER_STORE) {
							player.getPointsHandler().setSlayerPoints(-value * canBeBought, true);
						}
						writeInterface(player, id);
					}
					if (getItems()[slot].getAmount() - canBeBought <= 0 && id != GENERAL_STORE & id != DIANGO_STORE) {
						canBeBought -= 1;
						player.getInventory().add(item.getId(), 1);
						player.getPacketSender().sendMessage("The shop has run out of stock for this item.");
					}
					super.switchItem(to, new Item(item.getId(), canBeBought), slot, false, false);
					playerCurrencyAmount -= value;
					break;
				} else {
					break;
				}
			}
			amountBuying--;
		}
		if (!customShop) {
			if (usePouch) {
				player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch()); // Update
																							// the
																							// money
																							// pouch
			}
		} else {
			player.getPointsHandler().refreshPanel();
		}
		player.getInventory().refreshItems();
		fireRestockTask();
		refreshItems();
		publicRefresh();
		return this;
	}

	/**
	 * Checks if a player has enough inventory space to buy an item
	 * 
	 * @param item
	 *            The item which the player is buying
	 * @return true or false if the player has enough space to buy the item
	 */
	public static boolean hasInventorySpace(Player player, Item item, int currency, int pricePerItem) {
		if (player.getInventory().getFreeSlots() >= 1) {
			return true;
		}
		if (item.getDefinition().isStackable()) {
			if (player.getInventory().contains(item.getId())) {
				return true;
			}
		}
		if (currency != -1) {
			if (player.getInventory().getFreeSlots() == 0
					&& player.getInventory().getAmount(currency) == pricePerItem) {
				return true;
			}
		}
		return false;
	}

	/**
	 * On the 'custom' shops it will tell you how much
	 * 'points' you have.
	 * @param player
	 * @param shopId
	 */
	public static void writeInterface(Player player, int shopId) {
		if(shopId == PKING_REWARDS_STORE || shopId == PKING_REWARDS_STORE2 || shopId == PKING_REWARDS_STORE3) {
			player.getPacketSender().sendString(3903, "You currently have "+player.getPointsHandler().getPkPoints()+" pk points");
		} else if(shopId == VOTING_REWARDS_STORE || shopId == VOTING_REWARDS_STORE2 || shopId == IRON_VOTING_REWARDS_STORE) {
			player.getPacketSender().sendString(3903, "You currently have " + player.getPointsHandler().getVotingPoints() + " vote points");
		} else if(shopId == DUNGEONEERING_STORE) {
			player.getPacketSender().sendString(3903, "You currently have " + player.getPointsHandler().getDungeoneeringTokens() + " dungeoneering tokens");
		} else if(shopId == PEST_CONTROL_STORE) {
			player.getPacketSender().sendString(3903, "You currently have " + player.getPointsHandler().getCommendations() + " commendations (pest control points)");
		} else if(shopId == SLAYER_STORE || shopId == IRON_SLAYER_STORE) {
			player.getPacketSender().sendString(3903, "You currently have "+player.getPointsHandler().getSlayerPoints()+" slayer points");
		} else {
			player.getPacketSender().sendString(3903, "Right-click on shop to buy item - Right-click on inventory to sell item");
		}
	}

	@Override
	public Shop add(Item item, boolean refresh) {
		super.add(item, false);
		if (id != RECIPE_FOR_DISASTER_STORE)
			publicRefresh();
		return this;
	}

	@Override
	public int capacity() {
		return 42;
	}

	@Override
	public StackType stackType() {
		return StackType.STACKS;
	}

	@Override
	public Shop refreshItems() {
		if (id == RECIPE_FOR_DISASTER_STORE) {
			RecipeForDisaster.openRFDShop(getPlayer());
			return this;
		}
		if (id == DIANGO_STORE) {
//			getPlayer().openUnTradeableShop(getPlayer(), getPlayer().itemToBuyBack);
			return this;
		}
		for (Player player : World.getPlayers()) {
			if (player == null || !player.isShopping() || player.getShop() == null || player.getShop().id != id)
				continue;
			player.getPacketSender().sendItemContainer(player.getInventory(), INVENTORY_INTERFACE_ID);
			player.getPacketSender().sendItemContainer(ShopManager.getShops().get(id), ITEM_CHILD_ID);
			player.getPacketSender().sendString(NAME_INTERFACE_CHILD_ID, name);
			if (player.getInputHandling() == null || !(player.getInputHandling() instanceof EnterAmountToSellToShop
					|| player.getInputHandling() instanceof EnterAmountToBuyFromShop))
				player.getPacketSender().sendInterfaceSet(INTERFACE_ID, INVENTORY_INTERFACE_ID - 1);
		}
		return this;
	}

	@Override
	public Shop full() {
		getPlayer().getPacketSender().sendMessage("The shop is currently full. Please come back later.");
		return this;
	}

	public String shopPriceEx(int shopPrice) {
		String ShopAdd = "";
		if (shopPrice >= 1000 && shopPrice < 1000000) {
			ShopAdd = " (" + (shopPrice / 1000) + "K)";
		} else if (shopPrice >= 1000000) {
			ShopAdd = " (" + (shopPrice / 1000000) + " million)";
		}
		return ShopAdd;
	}

	private boolean shopSellsItem(Item item) {
		return contains(item.getId());
	}

	public void fireRestockTask() {
		if (isRestockingItems() || fullyRestocked())
			return;
		setRestockingItems(true);
		TaskManager.submit(new ShopRestockTask(this));
	}

	public boolean fullyRestocked() {
		if (id == GENERAL_STORE) {
			return getValidItems().size() == 0;
		} else if (id == RECIPE_FOR_DISASTER_STORE) {
			return true;
//		} else if (id == DIANGO_STORE) {
//			return true;
		}
		if (getOriginalStock() != null) {
			for (int shopItemIndex = 0; shopItemIndex < getOriginalStock().length; shopItemIndex++) {
				if (getItems()[shopItemIndex].getAmount() != getOriginalStock()[shopItemIndex].getAmount())
					return false;
			}
		}
		return true;
	}

	public static boolean shopBuysItem(int shopId, Item item) {
		if (shopId == GENERAL_STORE)
			return true;
		if (shopId == STARDUST_EXCHANGE_STORE || shopId == RECIPE_FOR_DISASTER_STORE || shopId == DIANGO_STORE
				|| shopId == IRON_VOTING_REWARDS_STORE || shopId == VOTING_REWARDS_STORE || shopId == VOTING_REWARDS_STORE2
				|| shopId == AGILITY_TICKET_STORE || shopId == TOKKUL_EXCHANGE_STORE || shopId == ENERGY_FRAGMENT_STORE
				|| shopId == SLAYER_STORE || shopId == IRON_SLAYER_STORE
				|| shopId == DUNGEONEERING_STORE || shopId == PEST_CONTROL_STORE)
			return false;
		Shop shop = ShopManager.getShops().get(shopId);
		if (shop != null && shop.getOriginalStock() != null) {
			for (Item it : shop.getOriginalStock()) {
				if (it != null && it.getId() == item.getId())
					return true;
			}
		}
		return false;
	}

	public static class ShopManager {

		private static Map<Integer, Shop> shops = new HashMap<Integer, Shop>();

		public static Map<Integer, Shop> getShops() {
			return shops;
		}

		public static JsonLoader parseShops() {
			return new JsonLoader() {
				@Override
				public void load(JsonObject reader, Gson builder) {
					int id = reader.get("id").getAsInt();
					String name = reader.get("name").getAsString();
					Item[] items = builder.fromJson(reader.get("items").getAsJsonArray(), Item[].class);
					Item currency = new Item(reader.get("currency").getAsInt());
					shops.put(id, new Shop(null, id, name, currency, items));
				}

				@Override
				public String filePath() {
					return "./data/def/json/world_shops.json";
				}
			};
		}

		public static Object[] getCustomShopData(int shop, int item) {
			if (shop == VOTING_REWARDS_STORE || shop == VOTING_REWARDS_STORE2) {
				switch (item) {
					case 15220:
						return new Object[] { 20, "Vote points" };
					case 15019:
					case 15018:
						return new Object[] { 10, "Vote points" };
					case 6199:
						return new Object[] { 15, "Vote points" };
					case 4151:
						return new Object[] { 25, "Vote points" };
					case 6570:
						return new Object[] { 15, "Vote points" };
					case 19111:
						return new Object[] { 20, "Vote points" };
					case 15492:
						return new Object[] { 100, "Vote points" };
					case 15602:
						return new Object[] { 50, "Vote points" };
					case 15600:
						return new Object[] { 75, "Vote points" };
					case 15604:
						return new Object[] { 75, "Vote points" };
					case 10732:
						return new Object[] { 80, "Vote points" };
					case 5023:
						return new Object[] { 10, "Vote points" };
					case 14484:
						return new Object[] { 75, "Vote points" };
					case 11724:
					case 11726:
						return new Object[] { 50, "Vote points" };
					case 11863:
						return new Object[] { 2500, "Vote points" };
					case 5608:
						return new Object[] { 80, "Vote points" };
					case 11846:
					case 11848:
					case 11850:
					case 11852:
					case 11854:
					case 11856:
						return new Object[] { 20, "Vote points" };
					case 6:
					case 8:
					case 10:
					case 12:
						return new Object[] { 5, "Vote points" };
					case 11665:
					case 11664:
					case 11663:
						return new Object[] { 75, "Vote points" };
					case 8840:
					case 8839:
						return new Object[] { 125, "Vote points" };
					case 8842:
						return new Object[] { 50, "Vote points" };
					case 11283:
					case 11235:
						return new Object[] { 25, "Vote points" };
					case 10551:
						return new Object[] { 15, "Vote points" };
				}
			} else if (shop == PKING_REWARDS_STORE || shop == PKING_REWARDS_STORE2 || shop == PKING_REWARDS_STORE3) {
				switch (item) {
					case 7478:
						return new Object[] { 1250, "Pk points" };

					case 13664:
						return new Object[] { 1000, "Pk points" };

					case 14484:
						return new Object[] { 5750, "Pk points" };

					case 11694:
						return new Object[] { 6500, "Pk points" };

					case 15273:
					case 3145:
						return new Object[] { 3, "Pk points" };

					case 11700:
					case 11698:
					case 11696:
						return new Object[] {3000, "Pk points" };

					case 19780:
						return new Object[] {6000, "Pk points" };

					case 13896:
						return new Object[] {750, "Pk points" };

					case 13884:
						return new Object[] {1125, "Pk points" };

					case 13890:
						return new Object[] {1125, "Pk points" };

					case 13887:
						return new Object[] {1500, "Pk points" };

					case 13893:
						return new Object[] {1875, "Pk points" };

					case 13876:
						return new Object[] {500, "Pk points" };

					case 13870:
						return new Object[] {1000, "Pk points" };

					case 13873:
						return new Object[] {1000, "Pk points" };

					case 13864:
						return new Object[] {500, "Pk points" };

					case 13858:
						return new Object[] {1000, "Pk points" };

					case 13861:
						return new Object[] {1000, "Pk points" };

					case 13938:
						return new Object[] {500, "Pk points" };

					case 13932:
						return new Object[] {1000, "Pk points" };

					case 13935:
						return new Object[] {1000, "Pk points" };

					case 11517:
						return new Object[] {10, "Pk points" };

					case 11525:
						return new Object[] {10, "Pk points" };

					case 6570:
						return new Object[] {500, "Pk points" };

					case 19111:
						return new Object[] {2000, "Pk points" };

					case 8850:
						return new Object[] {500, "Pk points" };

					case 20072:
					case 12954:
						return new Object[] {1000, "Pk points" };

					case 10551:
						return new Object[] {1000, "Pk points" };

					case 4151:
						return new Object[] {500, "Pk points" };

					case 11283:
						return new Object[] {750, "Pk points" };

					case 6585:
						return new Object[] {500, "Pk points" };

					case 21140:
						return new Object[] {5000, "Pk points" };

					case 15126:
						return new Object[] {500, "Pk points" };

					case 13576:
						return new Object[] {3000, "Pk points" };

					case 20171:
						return new Object[] {3250, "Pk points" };

					case 12926:
						return new Object[] {7500, "Pk points" };

					case 21144:
						return new Object[] {12000, "Pk points" };

					case 21146:
					case 13879:
					case 13883:
						return new Object[] {15, "Pk points" };

					case 13905:
					case 13899:
					case 13902:
						return new Object[] {1500, "Pk points" };

					case 21107:
						return new Object[] {7500, "Pk points" };

					case 19673:
						return new Object[] {3000, "Pk points" };
					case 19672:
					case 19674:
						return new Object[] {2500, "Pk points" };

					case 11718:
						return new Object[] {1000, "Pk points" };
					case 7968:
						return new Object[] {12500, "Pk points" };

					case 11720:
					case 11724:
					case 11726:
						return new Object[] {2500, "Pk points" };
					case 11722:
						return new Object[] {3000, "Pk points" };
					case 13754:
						return new Object[] {3000, "Pk points" };
					case 6731:
					case 6733:
					case 6735:
					case 6737:
						return new Object[] {500, "Pk points" };
					case 5023:
						return new Object[] {800, "Pk points" };
					case 4716:
					case 4720:
					case 4722:
					case 4718:
					case 4749:
					case 4751:
					case 4724:
					case 4728:
					case 4730:
					case 4726:
					case 4753:
					case 4757:
					case 4759:
					case 4755:
					case 4736:
					case 4738:
					case 4712:
					case 4714:
						return new Object[] {400, "Pk points" };
					case 4708:
					case 4710:
					case 4745:
					case 4747:
					case 4732:
					case 4734:
						return new Object[] {200, "Pk points" };
					case 4740:
						return new Object[] {1, "Pk points" };
					case 19748:
						return new Object[] {1000, "Pk points" };

				}
			} else if (shop == TOKKUL_EXCHANGE_STORE) {
				switch (item) {
					case 13225:
						return new Object[]{300000, "tokkul"};
					case 438:
					case 436:
						return new Object[]{10, "tokkul"};
					case 440:
						return new Object[]{25, "tokkul"};
					case 453:
						return new Object[]{30, "tokkul"};
					case 442:
						return new Object[]{30, "tokkul"};
					case 444:
						return new Object[]{40, "tokkul"};
					case 447:
						return new Object[]{70, "tokkul"};
					case 449:
						return new Object[]{120, "tokkul"};
					case 451:
						return new Object[]{250, "tokkul"};
					case 1623:
						return new Object[]{20, "tokkul"};
					case 1621:
						return new Object[]{40, "tokkul"};
					case 1619:
						return new Object[]{70, "tokkul"};
					case 1617:
						return new Object[]{150, "tokkul"};
					case 1631:
						return new Object[]{1600, "tokkul"};
					case 6571:
						return new Object[]{50000, "tokkul"};
					case 11128:
						return new Object[]{22000, "tokkul"};
					case 6522:
						return new Object[]{20, "tokkul"};
					case 6524:
					case 6523:
					case 6526:
						return new Object[]{5000, "tokkul"};
					case 6528:
					case 6568:
						return new Object[]{800, "tokkul"};
				}
			} else if (shop == AGILITY_TICKET_STORE) {
				switch (item) {
					case 14936:
					case 14938:
						return new Object[] { 60, "marks of grace" };
					case 10941:
					case 10939:
					case 10940:
					case 10933:
						return new Object[] { 20, "marks of grace" };
					case 20786:
						return new Object[] { 100, "marks of grace" };
				}
			} else if (shop == SLAYER_STORE || shop == IRON_SLAYER_STORE) {
				switch (item) {
					case 13263:
						return new Object[] { 250, "slayer points" };
					case 13281:
						return new Object[] { 5, "slayer points" };
					case 15403:
					case 11730:
					case 10887:
					case 15241:
						return new Object[] { 300, "slayer points" };
					case 15490:
					case 15488:
					case 21369:
						return new Object[] { 125, "slayer points" };
					case 11235:
					case 4151:
					case 15486:
						return new Object[] { 250, "slayer points" };
					case 15243:
						return new Object[] { 3, "slayer points" };
					case 10551:
						return new Object[] { 200, "slayer points" };
					case 2572:
						return new Object[] { 500, "slayer points" };
					case 21110:
						return new Object[] { 1250, "slayer points" };
					//iron man
					case 5574:
					case 5575:
					case 5576:
						return new Object[] { 50, "slayer points" };
					case 544:
					case 542:
						return new Object[] { 10, "slayer points" };
				}
			} else if (shop == STARDUST_EXCHANGE_STORE) {
				switch (item) {
					case 15259:
						return new Object[] {2100, "stardust"};
					case 6180:
					case 6181:
					case 6182:
						return new Object[] {2500, "stardust"};
					case 7409:
						return new Object[] {3500, "stardust"};
					case 20786:
						return new Object[] {5000, "stardust"};
					case 453:
						return new Object[] {5, "stardust"};
					case 9185:
						return new Object[] {250, "stardust"};
					case 20787:
					case 20788:
						return new Object[] {500, "stardust"};
					case 20789:
						return new Object[] {1000, "stardust"};
					case 20791:
					case 20790:
						return new Object[] {2500, "stardust"};
				}
			} else if (shop == DONATOR_STORE_ARMOUR_WEAPONS || shop == DONATOR_STORE_RARES) {
				switch (item) {
					/**
					 * Rare items
					 */
					case 1038:
					case 1040:
					case 1042:
					case 1044:
					case 1046:
					case 1048:
						return new Object[] {5000, "donator points"};
					case 1050:
					case 9920:
						return new Object[] {3500, "donator points"};
					case 21048:
						return new Object[] {22500, "donator points"};
					case 21024:
					case 21026:
						return new Object[] {15000, "donator points"};
					case 21025:
						return new Object[] {20000, "donator points"};
					case 21049:
						return new Object[] {10000, "donator points"};
					case 21109:
					case 21118:
					case 21119:
						return new Object[] {12500, "donator points"};
					case 4084:
						return new Object[] {7500, "donator points"};
					case 20135:
					case 20139:
					case 20143:
					case 20159:
					case 20163:
					case 20167:
					case 20147:
					case 20151:
					case 20155:
						return new Object[] {2500, "donator points"};
					case 1419:
						return new Object[] {5500, "donator points"};
					case 1037:
						return new Object[] {3500, "donator points"};
					case 9925:
					case 9924:
					case 9923:
					case 9921:
					case 9922:
					case 15352:
						return new Object[] {2000, "donator points"};

					/**
					 * Armour & weapons
					 */
					case 4151:
						return new Object[] {300, "donator points"};
					case 21372:
						return new Object[] {600, "donator points"};
					case 11235:
					case 15241:
					case 15019:
					case 15018:
					case 15020:
					case 15220:
					case 11732:
					case 2577:
					case 6920:
					case 11283:
					case 6585:
						return new Object[] {300, "donator points"};
					case 20000:
					case 20002:
					case 20001:
						return new Object[] {800, "donator points"};
					case 6570:
					case 6889:
						return new Object[] {500, "donator points"};
					case 19111:
						return new Object[] {1000, "donator points"};
					case 20072:
					case 12954:
						return new Object[] {700, "donator points"};
					case 13744:
						return new Object[] {1500, "donator points"};
					case 13738:
					case 13742:
						return new Object[] {3000, "donator points"};
					case 13740:
						return new Object[] {5000, "donator points"};
					case 21140:
						return new Object[] {3000, "donator points"};
					case 11724:
						return new Object[] {1500, "donator points"};
					case 11726:
						return new Object[] {1500, "donator points"};
					case 20821:
						return new Object[] {50000, "donator points"};
					case 14018:
						return new Object[] {20000, "donator points"};
					case 4565:
						return new Object[] {25000, "donator points"};
					case 11718:
					case 11720:
					case 11722:
						return new Object[] {1200, "donator points"};
					case 21075:
						return new Object[] {3000, "donator points"};
					case 20171:
						return new Object[] {2500, "donator points"};
					case 12926:
					case 21144:
						return new Object[] {3500, "donator points"};
					case 14484:
					case 11694:
						return new Object[] {1500, "donator points"};
					case 19780:
						return new Object[] {1250, "donator points"};

					case 21148:
						return new Object[] {3000, "donator points"};

					case 21107:
						return new Object[] {1500, "donator points"};
				}
			} else if (shop == ENERGY_FRAGMENT_STORE) {
				switch (item) {
					case 5509:
						return new Object[] {400, "energy fragments"};
					case 5510:
						return new Object[] {750, "energy fragments"};
					case 5512:
						return new Object[] {1000, "energy fragments"};
					case 13613:
						return new Object[] {800, "energy fragments"};
					case 13619:
						return new Object[] {1000, "energy fragments"};
					case 13622:
						return new Object[] {1000, "energy fragments"};
					case 13623:
						return new Object[] {450, "energy fragments"};
				}
			} else if (shop == DUNGEONEERING_STORE) {
				switch (item) {
					case 18351:
					case 18349:
					case 18353:
					case 18357:
					case 18355:
					case 18359:
					case 18361:
					case 18363:
						return new Object[]{200000, "Dungeoneering tokens"};
					case 18344:
						return new Object[]{153000, "Dungeoneering tokens"};
					case 18839:
						return new Object[]{140000, "Dungeoneering tokens"};
					case 18346:
						return new Object[]{100000, "Dungeoneering tokens"};
					case 18335:
						return new Object[]{75000, "Dungeoneering tokens"};
					case 19669:
						return new Object[]{50000, "Dungeoneering tokens"};
					case 6500:
						return new Object[]{75000, "Dungeoneering tokens"};
					case 18337:
						return new Object[]{75000, "Dungeoneering tokens"};
				}
			} else if (shop == PEST_CONTROL_STORE) {
				switch (item) {
					case 8839:
					case 8840:
						return new Object[]{200, "Commendations"};
					case 8842:
					case 19711:
					case 11663:
					case 11664:
					case 11665:
						return new Object[]{150, "Commendations"};
					case 13664:
						return new Object[]{200, "Commendations"};
					case 10551:
						return new Object[]{400, "Commendations"};
					case 19780:
						return new Object[]{2000, "Commendations"};
					case 19785:
					case 19786:
					case 19787:
					case 19788:
					case 19789:
					case 19790:
						return new Object[]{300, "Commendations"};
				}
			}
			return null;
		}
	}

	/**
	 * The shop interface id.
	 */
	public static final int INTERFACE_ID = 3824;

	/**
	 * The starting interface child id of items.
	 */
	public static final int ITEM_CHILD_ID = 3900;

	/**
	 * The interface child id of the shop's name.
	 */
	public static final int NAME_INTERFACE_CHILD_ID = 3901;

	/**
	 * The inventory interface id, used to set the items right click values to
	 * 'sell'.
	 */
	public static final int INVENTORY_INTERFACE_ID = 3823;

	/*
	 * Declared shops
	 */
	public static final int GENERAL_STORE = 0;
	public static final int RECIPE_FOR_DISASTER_STORE = 36;
	public static final int DIANGO_STORE = 100;

	private static final int SKILLCAPE_STORE_1 = 8;
	private static final int SKILLCAPE_STORE_2 = 9;
	private static final int SKILLCAPE_STORE_3 = 10;

	private static final int SLAYER_STORE = 15;
	private static final int IRON_SLAYER_STORE = 33;

	private static final int TOKKUL_EXCHANGE_STORE = 17;

	private static final int STARDUST_EXCHANGE_STORE = 41;

	private static final int PKING_REWARDS_STORE = 21;
	private static final int PKING_REWARDS_STORE2 = 22;
	private static final int PKING_REWARDS_STORE3 = 48;

	private static final int VOTING_REWARDS_STORE = 23;
	private static final int VOTING_REWARDS_STORE2 = 24;
	private static final int IRON_VOTING_REWARDS_STORE = 25;

	private static final int ENERGY_FRAGMENT_STORE = 27;

	private static final int AGILITY_TICKET_STORE = 28;

	private static final int DONATOR_STORE_ARMOUR_WEAPONS = 42;
	private static final int DONATOR_STORE_RARES = 43;

	private static final int DUNGEONEERING_STORE = 46;

	private static final int PEST_CONTROL_STORE = 47;

}
