package com.zarador.model.container.impl;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.zarador.model.Item;
import com.zarador.model.container.ItemContainer;
import com.zarador.model.container.StackType;
import com.zarador.model.definitions.ItemDefinition;
import com.zarador.model.input.impl.EnterAmountOfPosOfferPrice;
import com.zarador.model.input.impl.EnterAmountToBuyFromShop;
import com.zarador.model.input.impl.EnterAmountToSellToShop;
import com.zarador.world.World;
import com.zarador.world.content.MoneyPouch;
import com.zarador.world.content.pos.PlayerOwnedShops;
import com.zarador.world.content.pos.PosOffer;
import com.zarador.world.content.pos.PosOffers;
import com.zarador.world.entity.impl.player.Player;

/**
 * Handles the Player Owned Shop Containers
 *
 * @author Jonathan Sirens
 */

public class PlayerOwnedShopContainer extends ItemContainer {

	public PlayerOwnedShopContainer(Player player, String name, Item[] stockItems) {
		super(player);
		this.name = name + "'s store";
		this.index = getIndex(name);
		this.originalStock = new Item[stockItems.length];
		this.currency = new Item(995);
		for (int i = 0; i < stockItems.length; i++) {
			Item item = new Item(stockItems[i].getId(), stockItems[i].getAmount());
			add(item, false);
			this.originalStock[i] = item;
		}
	}

	private int index;

	private String name;

	private Item currency;

	private Item[] originalStock;

	public Item[] getOriginalStock() {
		return this.originalStock;
	}

	public int getIndex() {
		return this.index;
	}

	public String getName() {
		return name;
	}

	public static int getIndex(String name) {
		return PlayerOwnedShops.getIndex(name);
	}

	public PlayerOwnedShopContainer setName(String name) {
		this.name = name;
		return this;
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
		Item item = sellingItem ? player.getInventory().getItems()[slot] : getItems()[slot];
		if (item.getId() == 995)
			return;
		long finalValue = 0;
		PosOffers o = PlayerOwnedShops.SHOPS_ARRAYLIST.get(index);
		if (o == null)
			return;

		PosOffer offer = o.forId(item.getId());
		if (offer != null)
			finalValue = offer.getPrice();
		if (item.getId() < 0)
			return;
		if (player != null && finalValue > 0) {
			Locale locale = new Locale("en", "US");
			NumberFormat currencyFormatter = NumberFormat.getInstance(locale);
			player.getPacketSender()
					.sendMessage("<col=CA024B>" + ItemDefinition.forId(item.getId()).getName()
							+ "</col> is for sale for: <col=CA024B>" + currencyFormatter.format(finalValue) + " GP ["
							+ formatAmount(finalValue) + "] each.");
			return;
		}
	}

	public final String formatAmount(long amount) {
		String format = "Too high!";
		if (amount >= 0 && amount < 100000) {
			format = String.valueOf(amount);
		} else if (amount >= 100000 && amount < 1000000) {
			format = amount / 1000 + "K";
		} else if (amount >= 1000000 && amount < 10000000000L) {
			format = amount / 1000000 + "M";
		} else if (amount >= 10000000000L && amount < 1000000000000L) {
			format = amount / 1000000000 + "B";
		} else if (amount >= 10000000000000L && amount < 10000000000000000L) {
			format = amount / 1000000000000L + "T";
		} else if (amount >= 10000000000000000L && amount < 1000000000000000000L) {
			format = amount / 1000000000000000L + "QD";
		} else if (amount >= 1000000000000000000L && amount < Long.MAX_VALUE) {
			format = amount / 1000000000000000000L + "QT";
		}
		return format;
	}

	/**
	 * Opens a shop for a player
	 *
	 * @param player
	 *            The player to open the shop for
	 * @return The shop instance
	 */
	public PlayerOwnedShopContainer open(Player player, String owner, int i) {
		if (!player.getUsername().equalsIgnoreCase(owner)) {
			setPlayer(player);
			player.getPacketSender().sendInterfaceRemoval().sendClientRightClickRemoval();
			player.setPlayerOwnedShop(PlayerOwnedShopManager.getShops().get(i)).setInterfaceId(INTERFACE_ID)
					.setPlayerOwnedShopping(true);
		} else {
			player.getPacketSender().sendInterfaceRemoval().sendClientRightClickRemoval();
			player.setPlayerOwnedShop(PlayerOwnedShopManager.getShops().get(i)).setInterfaceId(INTERFACE_ID)
					.setPlayerOwnedShopping(true);
		}
		refreshItems();
		return this;
	}

	/**
	 * Refreshes a shop for every player who's viewing it
	 */
	public void publicRefresh() {
		PlayerOwnedShopContainer publicShop = PlayerOwnedShopManager.getShops().get(index);
		if (publicShop == null)
			return;
		publicShop.setItems(getItems());
		for (Player player : World.getPlayers()) {
			if (player == null)
				continue;
			if (player.getPlayerOwnedShop() != null && player.getPlayerOwnedShop().index == index)
				player.getPlayerOwnedShop().setItems(publicShop.getItems());
		}
	}

	public void sellItem(Player player, int slot, int amountToSell, long price) {
		this.setPlayer(player);
		if (player.isBanking() && !player.isPlayerOwnedShopping() && player.isBanking()) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}
		Item itemToSell = player.getInventory().getItems()[slot];
		int shopContainsAmount = player.getPlayerOwnedShop().getAmount(itemToSell.getId());
		if (!player.getInventory().contains(itemToSell.getId()) || itemToSell.getId() == 995)
			return;
		if (this.full(itemToSell.getId()))
			return;
		if (player.getInventory().forSlot(slot).getAmount() < amountToSell)
			amountToSell = player.getInventory().forSlot(slot).getAmount();
		if (amountToSell == 0)
			return;
		int itemId = itemToSell.getId();
		if (!itemToSell.tradeable(player)) {
			player.getPacketSender().sendMessage("You can't sell this item.");
			return;
		}
		if (amountToSell > 1000000000) {
			player.getPacketSender().sendMessage("You cannot have more than 1b of an item in the store.");
			return;
		}
		if (shopContainsAmount + amountToSell > 1000000000) {
			player.getPacketSender().sendMessage(
					"You can only have @blu@1b@bla@ of an item in your shop. @red@Please try again with a smaller amount.");
			return;
		}
		int count = 0;
		boolean inventorySpace = false;
		if (!itemToSell.getDefinition().isStackable()) {
			if (!player.getInventory().contains(995))
				inventorySpace = true;
		}
		if (player.getInventory().getFreeSlots() <= 0 && player.getInventory().getAmount(995) > 0)
			inventorySpace = true;
		if (player.getInventory().getFreeSlots() > 0 || player.getInventory().getAmount(995) > 0)
			inventorySpace = true;
		PosOffers o = PlayerOwnedShops.SHOPS_ARRAYLIST.get(index);
		if (o == null)
			return;
		if (!player.getUsername().equalsIgnoreCase(o.getOwner())) {
			player.getPacketSender().sendMessage("You can't sell items to this shop.");
			return;
		}
		int size = 0;
		for (int i2 = 0; i2 < o.getOffers().size(); i2++) {
			if (o.getOffers().get(i2) != null) {
				if (o.getOffers().get(i2).getItemId() >= 1) {
					size++;
				}
				if (o.getOffers().get(i2).getItemId() == itemId) {
					size--;
				}
			}
		}
		if (size >= 40) {
			player.getPacketSender().sendMessage("Shop full!");
			return;
		}

		PosOffer offer = o.forId(itemId);
		if (offer != null)
			price = offer.getPrice();

		if (price < 0)
			return;
		if (price == 0) {
			player.setHasNext(true);
			player.setInputHandling(new EnterAmountOfPosOfferPrice(slot, amountToSell));
			player.getPacketSender().sendEnterAmountPrompt("Enter the price of the item:");
			return;
		}

		for (int i = amountToSell; i > 0; i--) {
			itemToSell = new Item(itemId);
			if (this.full(itemToSell.getId()) || !player.getInventory().contains(itemToSell.getId()))
				break;
			if (!itemToSell.getDefinition().isStackable()) {
				if (inventorySpace) {
					super.switchItem(player.getInventory(), this, itemToSell.getId(), -1);
					PlayerOwnedShops.soldItem(player, index, itemToSell.getId(), 1, price);
				} else {
					player.getPacketSender().sendMessage("Please free some inventory space before doing that.");
					break;
				}
			} else {
				if (inventorySpace) {
					super.switchItem(player.getInventory(), this, itemToSell.getId(), amountToSell);
					PlayerOwnedShops.soldItem(player, index, itemToSell.getId(), amountToSell, price);
					break;
				} else {
					player.getPacketSender().sendMessage("Please free some inventory space before doing that.");
					break;
				}
			}
			amountToSell--;
			count++;
		}
		player.save();
		player.getInventory().refreshItems();
		refreshItems();
		publicRefresh();
	}

	/**
	 * Buying an item from a pos
	 */
	@Override
	public PlayerOwnedShopContainer switchItem(ItemContainer to, Item item, int slot, boolean sort, boolean refresh) {
		final Player player = getPlayer();
		if (player == null)
			return this;
		if (!player.isPlayerOwnedShopping() && player.isBanking()) {
			player.getPacketSender().sendInterfaceRemoval();
			return this;
		}
		if (item.getAmount() > getItems()[slot].getAmount())
			item.setAmount(getItems()[slot].getAmount());
		int amountBuying = item.getAmount();
		if (amountBuying == 0)
			return this;
		boolean usePouch = false;
		long playerCurrencyAmount = 0;

		long value = 0;

		PosOffers o = PlayerOwnedShops.SHOPS_ARRAYLIST.get(index);
		if (o == null)
			return this;

		PosOffer offer = o.forId(item.getId());
		if (offer != null && offer.getAmount() > 0)
			value = offer.getPrice();

		if (value > Integer.MAX_VALUE) {
			usePouch = true;
		}

		playerCurrencyAmount = player.getInventory().getAmount(995);
		String currencyName = ItemDefinition.forId(currency.getId()).getName().toLowerCase();
		if (player.getMoneyInPouch() >= value) {
			playerCurrencyAmount = player.getMoneyInPouch();
			if (!(player.getInventory().getFreeSlots() == 0 && player.getInventory().getAmount(995) == value)) {
				usePouch = true;
			}
		}

		if (value <= 0) {
			return this;
		}
		if (!player.getUsername().equalsIgnoreCase(o.getOwner())) {
			if (!hasInventorySpace(player, item, 995, (int) value)) {
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

			long total = 0;
			for (int i = amountBuying; i > 0; i--) {
				if (!item.getDefinition().isStackable()) {
					if (playerCurrencyAmount >= value && hasInventorySpace(player, item, 995, (int) value)) {
						if (usePouch) {
							player.setMoneyInPouch((player.getMoneyInPouch() - value));
						} else {
							player.getInventory().delete(995, (int) value, false);
						}
						super.switchItem(to, new Item(item.getId(), 1), slot, false, false);
						playerCurrencyAmount -= value;
						total += value;
						offer.decreaseAmount(1);
					} else {
						break;
					}
				} else {
					if (playerCurrencyAmount >= value && hasInventorySpace(player, item, 995, (int) value)) {
						long canBeBought = playerCurrencyAmount / value;
						if (canBeBought > Integer.MAX_VALUE) {
							canBeBought = Integer.MAX_VALUE;
						}
						if (canBeBought >= amountBuying) {
							canBeBought = amountBuying;
						}
						if (canBeBought == 0)
							break;

						if (usePouch) {
							player.setMoneyInPouch((player.getMoneyInPouch() - (value * canBeBought)));
						} else {
							player.getInventory().delete(995, (int) value * (int) canBeBought, false);
						}

						super.switchItem(to, new Item(item.getId(), (int) canBeBought), slot, false, false);
						playerCurrencyAmount -= value;
						total += value * canBeBought;
						offer.decreaseAmount((int) canBeBought);
						break;
					} else {
						break;
					}
				}
				amountBuying--;
			}
			if (offer.getAmount() == 0)
				o.removeOffer(offer);
			Player owner = World.getPlayerByName(o.getOwner());
			if (owner != null) {
				owner.getPacketSender().sendString(1, ":moneypouchearning:" + total);
				owner.getPacketSender()
						.sendMessage("<col=CA024B>" + item.getAmount() + "x of your "
								+ ((ItemDefinition.forId(item.getId()).getName().endsWith("s")
								? (ItemDefinition.forId(item.getId()).getName())
								: (ItemDefinition.forId(item.getId()).getName() + "s")))
								+ " have sold in your player owned shop.");
				MoneyPouch.depositVote(owner, total);
				owner.save();
			} else {
				o.addCoinsToCollect(total);
			}
			if (usePouch) {
				player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch()); // Update
			}
		} else {
			if (offer != null && offer.getAmount() > 0) {
				if (item.getAmount() >= offer.getAmount()) {
					if (o.removeOffer(o.forId(item.getId()))) {
						super.switchItem(to, new Item(item.getId(), offer.getAmount()), slot, false, false);
						player.getPacketSender().sendMessage("The item <col=CA024B>" + item.getDefinition().getName()
								+ "</col> has been removed from your shop.");
					}
				} else {
					offer.decreaseAmount(item.getAmount());
					super.switchItem(to, new Item(item.getId(), item.getAmount()), slot, false, false);
				}
			}
		}
		PlayerOwnedShops.saveShop(o);
		player.save();
		player.getInventory().refreshItems();
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

	@Override
	public PlayerOwnedShopContainer add(Item item, boolean refresh) {
		super.add(item, false);
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
	public PlayerOwnedShopContainer refreshItems() {
		for (Player player : World.getPlayers()) {
			if (player == null || player.getPlayerOwnedShop() == null || player.getPlayerOwnedShop().index != index)
				continue;
			player.getPacketSender().sendItemContainer(player.getInventory(), INVENTORY_INTERFACE_ID);
			player.getPacketSender().sendItemContainer(PlayerOwnedShopManager.getShops().get(index), ITEM_CHILD_ID);
			player.getPacketSender().sendString(NAME_INTERFACE_CHILD_ID, name);
			if (player.isPlayerOwnedShopping()) {
				if (player.getInputHandling() == null || !(player.getInputHandling() instanceof EnterAmountToSellToShop
						|| player.getInputHandling() instanceof EnterAmountToBuyFromShop))
					player.getPacketSender().sendInterfaceSet(INTERFACE_ID, INVENTORY_INTERFACE_ID - 1);
			}
		}
		return this;
	}

	@Override
	public PlayerOwnedShopContainer full() {
		getPlayer().getPacketSender().sendMessage("The shop is currently full. Please come back later.");
		return this;
	}

	private boolean shopSellsItem(Item item) {
		return contains(item.getId());
	}

	public static boolean shopBuysItem(int index_id, Item item) {
		PlayerOwnedShopContainer shop = PlayerOwnedShopManager.getShops().get(index_id);
		if (shop != null && shop.getOriginalStock() != null) {
			for (Item it : shop.getOriginalStock()) {
				if (it != null && it.getId() == item.getId())
					return true;
			}
		}
		return false;
	}

	public static class PlayerOwnedShopManager {

		private static Map<Integer, PlayerOwnedShopContainer> pos_shops = new HashMap<Integer, PlayerOwnedShopContainer>();

		public static Map<Integer, PlayerOwnedShopContainer> getShops() {
			return pos_shops;
		}

		public static void load() {
			for (PosOffers o : PlayerOwnedShops.SHOPS_ARRAYLIST) {
				if (o == null) {
					continue;
				}
				Item[] items = new Item[o.getOffers().size()];
				for (int i = 0; i < o.getOffers().size(); i++) {
					items[i] = new Item(o.getOffers().get(i).getItemId(), o.getOffers().get(i).getAmount());
				}
				pos_shops.put(getIndex(o.getOwner()), new PlayerOwnedShopContainer(null, o.getOwner(), items));
			}
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

}