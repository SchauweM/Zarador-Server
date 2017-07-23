package com.zarador.world.content;

import java.util.Collections;
import java.util.Comparator;

import com.zarador.model.definitions.ItemDefinition;
import com.zarador.model.npc.drops.LootSystem;
import com.zarador.world.entity.impl.player.Player;

public class DropLog {

	public static void submit(Player player, DropLogEntry[] drops) {
		for (DropLogEntry drop : drops) {
			if (drop != null)
				submit(player, drop);
		}
	}

	public static void submit(Player player, DropLogEntry drop) {
		int index = getIndex(player, drop);
		if (index >= 0) {
			player.getDropLog().get(index).amount += drop.amount;
		} else {
			if (addItem(player, drop)) {
				player.getDropLog().add(drop);
			}
		}
	}

	public static void open(Player player, int page) {
		try {
			/* RESORT THE KILLS */
			Collections.sort(player.getDropLog(), new Comparator<DropLogEntry>() {
				@Override
				public int compare(DropLogEntry drop1, DropLogEntry drop2) {
					ItemDefinition def1 = ItemDefinition.forId(drop1.item);
					ItemDefinition def2 = ItemDefinition.forId(drop2.item);
					int value1 = def1.getValue() * drop1.amount;
					int value2 = def2.getValue() * drop2.amount;
					if (value1 > value2) {
						return -1;
					} else if (value2 > value1) {
						return 1;
					} else {
						if (def2.getName().compareTo(def2.getName()) > 0) {
							return 1;
						} else {
							return -1;
						}
					}
				}
			});
			/* SHOW THE INTERFACE */
			resetInterface(player);
			player.getPacketSender().sendString(35252, "Drop Log Tracker");
			player.getPacketSender().sendString(35257, "Rare");
			player.setDropLogOpen(true);
			player.setKillsTrackerOpen(false);
			resetInterface(player);
			player.getPacketSender().sendInterface(35250);
			int index = 0;
			if (page == 0) {
				for (DropLogEntry entry : player.getDropLog()) {
					if (35261 + index >= 35311)
						break;
					if (!entry.rareDrop) {
						player.getPacketSender().sendString(35261 + index,
						"@or1@" + ItemDefinition.forId(entry.item).getName() + ": x" + entry.amount + "");

						index++;
					}
				}
			} else if (page == 1) {
				for (DropLogEntry entry : player.getDropLog()) {
					if (!entry.rareDrop)
						continue;
					if (35261 + index >= 35311)
						break;
					player.getPacketSender().sendString(35261 + index, "@or2@ " + ItemDefinition.forId(entry.item).getName() + ": " + entry.amount + "");
					index++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void resetInterface(Player player) {
		for (int i = 35261; i < 35311; i++) {
			player.getPacketSender().sendString(i, "");
		}
	}

	public static boolean addItem(Player player, DropLogEntry drop) {
		if (player.getDropLog().size() >= 98) {
			DropLogEntry drop2 = player.getDropLog().get(player.getDropLog().size() - 1);
			int value1 = ItemDefinition.forId(drop.item).getValue() * drop.amount;
			int value2 = ItemDefinition.forId(drop2.item).getValue() * drop2.amount;
			if (value1 > value2) {
				player.getDropLog().remove(drop2);
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	public static int getIndex(Player player, DropLogEntry drop) {
		for (int i = 0; i < player.getDropLog().size(); i++) {
			if (player.getDropLog().get(i).item == drop.item) {
				return i;
			}
		}
		return -1;
	}

	public static class DropLogEntry {

		public DropLogEntry(int item, int amount) {
			this.item = item;
			this.amount = amount;
			this.rareDrop = ItemDefinition.forId(item).getValue() > 500000 || LootSystem.getAnnouncment().isAnnouncable(item) || LootSystem.isBarrows(item);
		}

		public int item;
		public int amount;
		public boolean rareDrop;
	}

}
