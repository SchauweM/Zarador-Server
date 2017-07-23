package com.zarador.model.input.impl;

import java.text.NumberFormat;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import com.zarador.model.definitions.ItemDefinition;
import com.zarador.model.input.Input;
import com.zarador.world.content.PlayerPunishment;
import com.zarador.world.content.pos.PlayerOwnedShops;
import com.zarador.world.content.pos.PosDetails;
import com.zarador.world.content.pos.PosOffer;
import com.zarador.world.content.pos.PosOffers;
import com.zarador.world.entity.impl.player.Player;

public class PosItemSearch extends Input {

	private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

	private static final class SearchTask implements Runnable {

		private final Player player;
		private final String syntax;

		public SearchTask(Player player, String syntax) {
			this.player = player;
			this.syntax = syntax;
		}

		@Override
		public void run() {
			Locale locale = new Locale("en", "US");
			NumberFormat currencyFormatter = NumberFormat.getInstance(locale);
			if (syntax.length() <= 1) {
				player.getPacketSender().sendMessage("Invalid syntax entered.");
				return;
			}
			int start_button = -24062;
			int start_owner_name = 41468;
			int start_caption = 41469;
			int index = 0;

			String itemName = syntax;
			if (itemName.length() < 3) {
				player.getPacketSender().sendMessage("Your search must contain atleast 3 characters.");
				return;
			}
			reset(player);

			for (PosOffers o : PlayerOwnedShops.SHOPS_ARRAYLIST) {
				if (o == null)
					continue;

				for (int q = 0; q < o.getOffers().size(); q++) {
					if (o.getOffers().get(q) != null && !o.getOwner().equalsIgnoreCase(player.getUsername())
							&& !PlayerPunishment.isPlayerBanned(o.getOwner())) {
						ItemDefinition def = ItemDefinition.forId(o.getOffers().get(q).getItemId());
						if (def != null && def.getName().toLowerCase().contains(itemName)) {
							player.foundOffers.put(new PosDetails(start_button, o.getOwner(), o.getCaption()),
									new PosOffer(o.getOffers().get(q).getItemId(), o.getOffers().get(q).getAmount(),
											o.getOffers().get(q).getSoldAmount(), o.getOffers().get(q).getPrice()));
							start_button += 4;
						}
					}
				}
			}

			if (player.foundOffers.size() < 1) {
				player.getPacketSender().sendMessage("Your search did not return any offers.");
				return;
			}

			player.foundOffers = sortByValue(player.foundOffers);

			for (Map.Entry<PosDetails, PosOffer> entry : player.foundOffers.entrySet()) {
				if (index >= 100) {
					break;
				}
				PosDetails pd = entry.getKey();
				PosOffer p = entry.getValue();
				String item_name = ItemDefinition.forId(p.getItemId()).getName();
				start_caption += 4;
				start_owner_name += 4;
				pd.setButtonId(-24062 + (4 * index));
				player.getPacketSender().sendString(start_owner_name, pd.getOwner());
				player.getPacketSender().sendString(start_caption, ":storeowner:-"+p.getItemId()+"-"+p.getPrice()+"-"+pd.getCaption());
				index++;
			}
		}
	}

	@Override
	public void handleSyntax(Player player, String syntax) {

//		player.getPacketSender().sendMessage("<col=ff0000>Searching items has been disabled temporarily, this was confirmed to be");
//		player.getPacketSender().sendMessage("<col=ff0000>the source of the lag issue in the game. Enjoy no lag :)");

		player.getPacketSender().sendMessage("Conducting search...");
		EXECUTOR.submit(new SearchTask(player, syntax));
		/*
		Locale locale = new Locale("en", "US");
		NumberFormat currencyFormatter = NumberFormat.getInstance(locale);
		if (syntax.length() <= 1) {
			player.getPacketSender().sendMessage("Invalid syntax entered.");
			return;
		}
		int start_button = -24102;
		int start_owner_name = 41428;
		int start_caption = 41429;
		int index = 0;

		String itemName = syntax;
		if (itemName.length() < 3) {
			player.getPacketSender().sendMessage("Your search must contain atleast 3 characters.");
			return;
		}
		reset(player);
		for (PosOffers o : PlayerOwnedShops.SHOPS_ARRAYLIST) {
			if (o == null)
				continue;

			for (int q = 0; q < o.getOffers().size(); q++) {
				if (o.getOffers().get(q) != null && !o.getOwner().equalsIgnoreCase(player.getUsername())
						&& !PlayerPunishment.isPlayerBanned(o.getOwner())) {
					ItemDefinition def = ItemDefinition.forId(o.getOffers().get(q).getItemId());
					if (def != null && def.getName().toLowerCase().contains(itemName)) {
						Player.foundOffers.put(new PosDetails(start_button, o.getOwner(), o.getCaption()),
								new PosOffer(o.getOffers().get(q).getItemId(), o.getOffers().get(q).getAmount(),
										o.getOffers().get(q).getSoldAmount(), o.getOffers().get(q).getPrice()));
						// start_button += 4;
					}
				}
			}
		}

		if (Player.foundOffers.size() < 1) {
			player.getPacketSender().sendMessage("Your search did not return any offers.");
			return;
		}

		Player.foundOffers = sortByValue(Player.foundOffers);

		for (Map.Entry<PosDetails, PosOffer> entry : Player.foundOffers.entrySet()) {
			if (index >= 100) {
				break;
			}
			PosDetails pd = entry.getKey();
			PosOffer p = entry.getValue();
			String item_name = ItemDefinition.forId(p.getItemId()).getName();
			start_caption += 4;
			start_owner_name += 4;
			pd.setButtonId(-24102 + (4 * index));
			player.getPacketSender().sendString(start_owner_name, pd.getOwner());
			player.getPacketSender().sendString(start_caption,
					"Found: " + item_name + " for " + currencyFormatter.format(p.getPrice()) + " GP");
			index++;
		}

		for (start_caption = start_caption; start_caption < 41834; start_caption = start_caption) {
			start_caption += 4;
			start_owner_name += 4;
			player.getPacketSender().sendString(start_caption, "");
			player.getPacketSender().sendString(start_owner_name, "");
		}
		*/
	}

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		Map<K, V> result = new LinkedHashMap<>();
		Stream<Entry<K, V>> st = map.entrySet().stream();

		st.sorted(Comparator.comparing(e -> e.getValue())).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));

		return result;
	}

	public final String formatAmount(long amount) {
		String format = "Too high!";
		if (amount >= 0 && amount < 100000) {
			format = String.valueOf(amount);
		} else if (amount >= 100000 && amount < 1000000) {
			format = amount / 1000 + "K";
		} else if (amount >= 1000000 && amount < 1000000000L) {
			format = amount / 1000000 + "M";
		} else if (amount >= 1000000000L && amount < 1000000000000L) {
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

	public static PosDetails forId(int i, Player player) {
		for (Map.Entry<PosDetails, PosOffer> map : player.foundOffers.entrySet()) {
			PosDetails pd = map.getKey();
			if (pd.getButtonId() == i)
				return pd;
		}
		return null;
	}

	public static void reset(Player player) {
		player.foundOffers.clear();
		player.getPacketSender().sendString(41409, ":clearinterface:");
	}

}