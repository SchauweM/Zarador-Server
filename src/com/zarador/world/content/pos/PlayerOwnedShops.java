package com.zarador.world.content.pos;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.zarador.model.Item;
import com.zarador.model.container.impl.PlayerOwnedShopContainer;
import com.zarador.model.container.impl.PlayerOwnedShopContainer.PlayerOwnedShopManager;
import com.zarador.model.definitions.ItemDefinition;
import com.zarador.model.input.impl.PosItemSearch;
import com.zarador.model.input.impl.PosSearchShop;
import com.zarador.world.entity.impl.player.Player;

public class PlayerOwnedShops {

	public static ArrayList<PosOffers> SHOPS_ARRAYLIST = new ArrayList<>();
	public static ArrayList<String> SHOPS_TO_SEARCH = new ArrayList<>();

	public static void init() {
		System.out.println("Loading player owned shops...");
		File folder = new File("./data/saves/pos/shops");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				if (listOfFiles[i].getName().contains(".dat")) {
					loadPOS(listOfFiles[i].getName());
				}
				//System.out.println("File " + listOfFiles[i].getName());
			}
		}
		loadFeaturedShops();
		System.out.println("Finished loading player owned shops.");
	}

	public static void loadPOS(String fileName) {
		try {
			File file = new File("./data/saves/pos/shops/"+fileName);
			if (!file.exists()) {
				return;
			}
			DataInputStream in = new DataInputStream(new FileInputStream(file));

			String owner_name = in.readUTF();
			String store_caption = in.readUTF();
			int shopItems = in.readInt();
			long coins_to_collect = in.readLong();
			PosOffer[] sell_offers = new PosOffer[shopItems];
			for (int i2 = 0; i2 < shopItems; i2++) {
				sell_offers[i2] = new PosOffer(in.readInt(), in.readInt(), in.readInt(), in.readLong());
			}

			SHOPS_ARRAYLIST
					.add(new PosOffers(owner_name, store_caption, shopItems, coins_to_collect, sell_offers));
			SHOPS_TO_SEARCH.add(owner_name.toLowerCase());


			in.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void loadFeaturedShops() {
		try {
			File file = new File("./data/saves/pos/featured/shops.dat");
			if (!file.exists()) {
				return;
			}
			DataInputStream in = new DataInputStream(new FileInputStream(file));

			for(int i = 0; i < PosFeaturedShops.isEmpty.length; i++) {
				PosFeaturedShops.isEmpty[i] = in.readBoolean();
				PosFeaturedShops.timeRemaining[i] = in.readLong();
				String owner = in.readUTF();
				if(owner.equalsIgnoreCase("invalid_person")) {
					PosFeaturedShops.shopOwner[i] = null;
				} else {
					PosFeaturedShops.shopOwner[i] = owner;
				}
			}

			in.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static SortedMap<String, Object> getByPreffix(
			NavigableMap<String, Object> myMap,
			String preffix ) {
		return myMap.subMap( preffix, preffix + Character.MAX_VALUE );
	}

	private static final Executor saveWorker = Executors.newSingleThreadExecutor();

	private static final Runnable saveRunnable = () -> saveShops();

	private static final Runnable featuredShopsSaveRunnable = () -> saveFeaturedShops();

	public static void saveShops() {
		for(int i = 0; i < PlayerOwnedShops.getCount() - 1; i++) {
			PosOffers offer = PlayerOwnedShops.SHOPS_ARRAYLIST.get(i);
			saveShop(offer);
		}
	}

	public static void saveShop(PosOffers offer) {
		File pos = new File("./data/saves/pos");
		if (!pos.exists()) {
			pos.mkdirs();
		}
		RandomAccessFile file = null;
		try {
			file = new RandomAccessFile(pos + "/shops/" + offer.getOwner() + ".dat", "rw");
			offer.saveRAF(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (file != null) {
				try {
					file.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void saveFeaturedShops() {
		File pos = new File("./data/saves/pos/featured");
		if (!pos.exists()) {
			pos.mkdirs();
		}
		RandomAccessFile file = null;
		try {
			file = new RandomAccessFile(pos + "/shops.dat", "rw");

			for(int i = 0; i < PosFeaturedShops.isEmpty.length; i++) {
				file.writeBoolean(PosFeaturedShops.isEmpty[i]);
				file.writeLong(PosFeaturedShops.timeRemaining[i]);
				if(PosFeaturedShops.shopOwner[i] == null) {
					file.writeUTF("invalid_person");
				} else {
					file.writeUTF(PosFeaturedShops.shopOwner[i]);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (file != null) {
				try {
					file.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void save() {
		saveWorker.execute(saveRunnable);
		saveWorker.execute(featuredShopsSaveRunnable);
	}

	private static int getCount() {
		return SHOPS_ARRAYLIST.size();
	}

	public static boolean posButtons(Player player, int buttonId) {
		switch (buttonId) {
			case -24062:

				break;
			case 41462: // Search by Name
				if (player.getGameModeAssistant().isIronMan()) {
					player.getPacketSender().sendMessage("Ironmen can't use the player owned shops!");
					return false;
				}
				player.setInputHandling(new PosSearchShop());
				player.getPacketSender().sendEnterInputPrompt("Enter the name of a player's shop:");
				return true;
			case 41463: // Search by Item
				if (player.getGameModeAssistant().isIronMan()) {
					player.getPacketSender().sendMessage("Ironmen can't use the player owned shops!");
					return false;
				}
				player.setInputHandling(new PosItemSearch());
				player.getPacketSender().sendEnterInputPrompt("Enter the name of the item you wish to buy:");
				return true;
		}
		return false;
	}

	public static void soldItem(Player player, int index, int item_id, int item_amount, long price) {
		PosOffers o = SHOPS_ARRAYLIST.get(index);
		if (o != null) {
			PosOffer offer = o.forId(item_id);
			if (offer != null) {
				offer.increaseAmount(item_amount);
				PlayerOwnedShops.saveShop(o);
			} else {
				if (o.addOffer(new PosOffer(item_id, item_amount, 0, price))) {
					player.getPacketSender()
							.sendMessage("You have successfully placed your <col=CA024B>"
									+ ItemDefinition.forId(item_id).getName() + "</col> for sale for <col=CA024B>"
									+ formatAmount(price) + "</col>");
					PlayerOwnedShops.saveShop(o);
				} else {
					player.getPacketSender().sendMessage("Shop full!");
				}
			}
		} else {
			System.out.println("Error: Shop null");
		}
	}

	public static int getIndex(String name) {
		return SHOPS_TO_SEARCH.indexOf(name.toLowerCase());
	}

	public static int getFreeIndex() {
		for (int i = 0; i < SHOPS_ARRAYLIST.size(); i++) {
			PosOffers p = SHOPS_ARRAYLIST.get(i);
			if (p == null) {
				return i;
			}
		}
		return -1;
	}

	public static void openShop(String username, Player player) {
		if (player.getGameModeAssistant().isIronMan()) {
			player.getPacketSender().sendMessage("Ironmen can't use the player owned shops!");
			return;
		}
		int[] stock = new int[40];
		int[] stockAmount = new int[40];
		for (int i = 0; i < stock.length; i++) {
			stock[i] = -1;
			stockAmount[i] = 1;
		}
		int i = getIndex(username.toLowerCase());
		if (i >= 0) {
			String name = SHOPS_TO_SEARCH.get(i);
			if (name.equals(username.toLowerCase())) {
				player.setPlayerOwnedShopping(true);
				player.getPacketSender().sendString(3903, PlayerOwnedShops.SHOPS_ARRAYLIST.get(i).getCaption());
				PlayerOwnedShopManager.getShops().get(i).open(player, username.toLowerCase(), i);
				if (i == SHOPS_ARRAYLIST.size())
					player.getPacketSender().sendMessage("This shop does not exist!");
			}
		} else {
			if (player.getUsername().equalsIgnoreCase(username)) {
				PosOffer[] offers = new PosOffer[40];
				SHOPS_ARRAYLIST.add(new PosOffers(player.getUsername(), player.getUsername() + "'s store",
						offers.length, 0, offers));
				SHOPS_TO_SEARCH.add(player.getUsername().toLowerCase());
				Item[] default_items = new Item[0];
				PlayerOwnedShopManager.getShops().put(SHOPS_ARRAYLIST.size() - 1,
						new PlayerOwnedShopContainer(null, player.getUsername(), default_items));
				player.getPacketSender().sendString(41900, "");
				openShop(player.getUsername(), player);
			} else {
				player.getPacketSender().sendMessage("This shop does not exist!");
			}
		}
	}

	public static void collectCoinsOnLogin(Player player) {
		for (PosOffers o : SHOPS_ARRAYLIST) {
			if (o == null)
				continue;
			if (o.getOwner().toLowerCase().equals(player.getUsername().toLowerCase())) {
				if(o.getCoinsToCollect() == 0) {
					return;
				}
				if (o.getCoinsToCollect() >= 1 && player.getBankPinAttributes().hasEnteredBankPin()) {
					player.setMoneyInPouch((player.getMoneyInPouch() + (o.getCoinsToCollect())));
					player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
					player.getPacketSender().sendString(1, ":moneypouchearning:" + o.getCoinsToCollect());
					player.getPacketSender().sendMessage("Your items have sold for <col=CA024B>" + formatAmount(o.getCoinsToCollect()) + "</col>");
					o.resetCoinsCollect();
					player.save();
				} else {
					player.setMoneyInPouch((player.getMoneyInPouch() + (o.getCoinsToCollect())));
					player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
					player.getPacketSender().sendString(1, ":moneypouchearning:" + o.getCoinsToCollect());
					player.getPacketSender().sendMessage("<col=CA024B>Some of your Player Owned Shop items have been sold</col>");
					o.resetCoinsCollect();
					player.save();
				}
				PlayerOwnedShops.saveShop(o);
			}
		}
	}

	public static final String formatAmount(long amount) {
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

	public static void openItemSearch(Player player, boolean wipe) {

		if (wipe) {
			PosItemSearch.reset(player);
		}

		PosFeaturedShops.resetInterface(player);

		player.getPacketSender().sendInterface(41409);
	}

}