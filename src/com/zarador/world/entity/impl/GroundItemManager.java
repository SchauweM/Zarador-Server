package com.zarador.world.entity.impl;

import java.util.concurrent.CopyOnWriteArrayList;

import com.zarador.engine.task.impl.GroundItemsTask;
import com.zarador.model.GroundItem;
import com.zarador.model.Item;
import com.zarador.model.Position;
import com.zarador.model.definitions.ItemDefinition;
import com.zarador.world.World;
import com.zarador.world.content.Sounds;
import com.zarador.world.content.Sounds.Sound;
import com.zarador.world.content.logs.Logs;
import com.zarador.world.entity.impl.player.Player;

public class GroundItemManager {

	/*
	 * Our list which holds all grounditems, used an CopyOnWriteArrayList to
	 * prevent modification issues
	 */
	private static CopyOnWriteArrayList<GroundItem> groundItems = new CopyOnWriteArrayList<GroundItem>();

	/**
	 * Removes a grounditem from the world
	 * 
	 * @param groundItem
	 *            The grounditem to remove from the world.
	 * @param delistGItem
	 *            Should the grounditem be deleted from the arraylist aswell?
	 */
	public static void remove(GroundItem groundItem, boolean delistGItem) {
		if (groundItem != null) {
			if (groundItem.isGlobal()) {
				for (Player p : World.getPlayers()) {
					if (p == null)
						continue;
					if (p.getPosition().distanceToPoint(groundItem.getPosition().getX(),
							groundItem.getPosition().getY()) <= 120)
						p.getPacketSender().removeGroundItem(groundItem.getItem().getId(),
								groundItem.getPosition().getX(), groundItem.getPosition().getY(),
								groundItem.getItem().getAmount());
				}
			} else {
				Player person = World.getPlayerByName(groundItem.getOwner());
				if (person != null && person.getPosition().distanceToPoint(groundItem.getPosition().getX(),
						groundItem.getPosition().getY()) <= 120)
					person.getPacketSender().removeGroundItem(groundItem.getItem().getId(),
							groundItem.getPosition().getX(), groundItem.getPosition().getY(),
							groundItem.getItem().getAmount());
			}
			if (delistGItem)
				groundItems.remove(groundItem);
		}
	}

	/**
	 * This method spawns a grounditem for a player.
	 * 
	 * @param p
	 *            The owner of the grounditem
	 * @param g
	 *            The grounditem to spawn
	 */
	public static void spawnGroundItem(Player p, GroundItem g) {
		if (p == null)
			return;
		final Item item = g.getItem();
		if (item.getId() > ItemDefinition.getMaxAmountOfItems() || item.getId() <= 0) {
			return;
		}
		if (item.getDefinition().getName().toLowerCase().contains("clue scroll"))
			return;
		if (item.getId() >= 2412 && item.getId() <= 2414) {
			p.getPacketSender().sendMessage("The cape vanishes as it touches the ground.");
			return;
		}
		if (ItemDefinition.forId(item.getId()).isStackable()) {
			GroundItem it = getGroundItem(p, item, g.getPosition());
			if (it != null) {
				it.getItem().setAmount(it.getItem().getAmount() + g.getItem().getAmount() > Integer.MAX_VALUE
						? Integer.MAX_VALUE : it.getItem().getAmount() + g.getItem().getAmount());
				if (it.getItem().getAmount() <= 0)
					remove(it, true);
				else
					it.setRefreshNeeded(true);
				return;
			}
		}
		/*
		 * if(Misc.getMinutesPlayed(p) < 15) { g.setGlobalStatus(false);
		 * g.setGoGlobal(false); }
		 */
		add(g, true);
	}

	/**
	 * This method spawns a grounditem for a player.
	 *
	 * @param p
	 *            The owner of the grounditem
	 * @param g
	 *            The grounditem to spawn
	 */
	public static void spawnGroundItemGlobally(Player p, GroundItem g) {
		if (p == null)
			return;
		final Item item = g.getItem();
		if (item.getId() > ItemDefinition.getMaxAmountOfItems() || item.getId() <= 0) {
			return;
		}
		if (item.getDefinition().getName().toLowerCase().contains("clue scroll"))
			return;
		if (item.getId() >= 2412 && item.getId() <= 2414) {
			p.getPacketSender().sendMessage("The cape vanishes as it touches the ground.");
			return;
		}
		if (ItemDefinition.forId(item.getId()).isStackable()) {
			GroundItem it = getGroundItem(p, item, g.getPosition());
			if (it != null) {
				it.getItem().setAmount(it.getItem().getAmount() + g.getItem().getAmount() > Integer.MAX_VALUE
						? Integer.MAX_VALUE : it.getItem().getAmount() + g.getItem().getAmount());
				if (it.getItem().getAmount() <= 0)
					remove(it, true);
				else
					it.setRefreshNeeded(true);
				return;
			}
		}
		g.setGlobalStatus(true);
		g.setGoGlobal(true);
		add(g, true);
	}

	/**
	 * This method spawns a grounditem for the world.
	 * 
	 * @param p
	 *            The owner of the grounditem
	 * @param g
	 *            The grounditem to spawn
	 */
	public static void spawnWorldItem(GroundItem g) {
		final Item item = g.getItem();
		if (item.getId() > ItemDefinition.getMaxAmountOfItems() || item.getId() <= 0) {
			return;
		}
		if (ItemDefinition.forId(item.getId()).isStackable()) {
			GroundItem it = getGroundItem(null, item, g.getPosition());
			if (it != null) {
				it.getItem().setAmount(it.getItem().getAmount() + g.getItem().getAmount() > Integer.MAX_VALUE
						? Integer.MAX_VALUE : it.getItem().getAmount() + g.getItem().getAmount());
				if (it.getItem().getAmount() <= 0)
					remove(it, true);
				else
					it.setRefreshNeeded(true);
				return;
			}
		}
		g.setGlobalStatus(true);
		g.setGoGlobal(true);
		add(g, true);
	}

	/**
	 * Adds a grounditem to the world
	 * 
	 * @param groundItem
	 *            The grounditem to add to the world
	 * @param listGItem
	 *            Should the grounditem be added to the arraylist?
	 */
	public static void add(GroundItem groundItem, boolean listGItem) {
		if (groundItem.isGlobal()) {
			for (Player p : World.getPlayers()) {
				if (p == null)
					continue;
				if (groundItem.getPosition().getZ() == p.getPosition().getZ() && p.getPosition()
						.distanceToPoint(groundItem.getPosition().getX(), groundItem.getPosition().getY()) <= 120)
					p.getPacketSender().createGroundItem(groundItem.getItem().getId(), groundItem.getPosition().getX(),
							groundItem.getPosition().getY(), groundItem.getItem().getAmount());
			}
		} else {
			Player person = World.getPlayerByName(groundItem.getOwner());
			if (person != null && groundItem.getPosition().getZ() == person.getPosition().getZ() && person.getPosition()
					.distanceToPoint(groundItem.getPosition().getX(), groundItem.getPosition().getY()) <= 120)
				person.getPacketSender().createGroundItem(groundItem.getItem().getId(), groundItem.getPosition().getX(),
						groundItem.getPosition().getY(), groundItem.getItem().getAmount());
		}
		if (listGItem) {
			groundItems.add(groundItem);
			GroundItemsTask.fireTask();
		}
	}

	/**
	 * Handles the pick up of a grounditem
	 * 
	 * @param p
	 *            The player picking up the item
	 * @param item
	 * @param position
	 */
	public static void pickupGroundItem(Player p, Item item, Position position) {
		if (!p.getLastItemPickup().elapsed(500))
			return;
		boolean canAddItem = p.getInventory().getFreeSlots() > 0
				|| item.getDefinition().isStackable() && p.getInventory().contains(item.getId());
		if (!canAddItem) {
			p.getInventory().full();
			return;
		}
		GroundItem gt = getGroundItem(p, item, position);
		if (gt == null || gt.hasBeenPickedUp() || !groundItems.contains(gt)) // last
																				// one
																				// isn't
																				// needed,
																				// but
																				// hey,
																				// just
																				// trying
																				// to
																				// be
																				// safe
			return;
		else {
			/**
			 * Start our Ironman Pked check
			 */
			if (gt.isIronmanPked()) {
				if (gt.getOwner().equals(p.getUsername())) {
					p.getPacketSender().sendMessage("Unfortunately since you are an ironman you cannot pickup your pked loots.");
					return;
				} else if (gt.getOldOwner().equals(p.getUsername())) {
					p.getPacketSender().sendMessage("You got totally pwn'd by a ironman, so you don't deserve this stuff anymore");
					return;
				} else {
					p.getPacketSender().sendMessage("Items of a player killed by an ironman cannot be retrieved");
					return;
				}
			}

			/**
			 * End our Ironman PKed check
			 */

			if (p.getGameModeAssistant().isIronMan()) {
				if ((gt.getOwner() != null && !gt.getOwner().equals("null") && !gt.getOwner().equals(p.getUsername()))
						|| (gt.getIronman() && p.getGameModeAssistant().isIronMan())) {
					p.getPacketSender().sendMessage("You cannot pick this item up because it was not spawned for you.");
					return;
				}
			}
			item = gt.getItem();
			gt.setPickedUp(true);
			remove(gt, true);
			p.getInventory().add(item);
			p.getLastItemPickup().reset();
			Sounds.sendSound(p, Sound.PICKUP_ITEM);
			Logs.log(p, "pickups",
					new String[] {
							"Item: "+item.getDefinition().getName()+" ("+item.getId()+")",
							"Amount: "+item.getAmount()+"",
							"Tracking: "+gt.getAddress()+"",
					});
		}
	}

	/**
	 * Handles a region change for a player. This method respawns all
	 * grounditems for a player who has changed region.
	 * 
	 * @param p
	 *            The player who has changed region
	 */
	public static void handleRegionChange(Player p) {
		for (GroundItem gi : groundItems) {
			if (gi == null)
				continue;
			p.getPacketSender().removeGroundItem(gi.getItem().getId(), gi.getPosition().getX(), gi.getPosition().getY(),
					gi.getItem().getAmount());
		}
		for (GroundItem gi : groundItems) {
			if (gi == null || p.getPosition().getZ() != gi.getPosition().getZ()
					|| p.getPosition().distanceToPoint(gi.getPosition().getX(), gi.getPosition().getY()) > 120)
				continue;
			if (gi.isGlobal() || !gi.isGlobal() && gi.getOwner().equals(p.getUsername()))
				p.getPacketSender().createGroundItem(gi.getItem().getId(), gi.getPosition().getX(),
						gi.getPosition().getY(), gi.getItem().getAmount());
		}
	}

	/**
	 * Checks if a grounditem exists in the stated position.
	 * 
	 * @param p
	 *            The player trying to check if the grounditem exists
	 * @param item
	 *            The grounditem's item
	 * @param position
	 *            The position to check if a grounditem exists on
	 * @return true if a grounditem exists in the specified position, otherwise
	 *         false
	 */
	public static GroundItem getGroundItem(Player p, Item item, Position position) {
		for (GroundItem l : groundItems) {
			if (l == null || l.getPosition().getZ() != position.getZ())
				continue;
			if (l.getPosition().equals(position) && l.getItem().getId() == item.getId()) {
				if (l.isGlobal())
					return l;
				else if (p != null) {
					Player owner = World.getPlayerByName(l.getOwner());
					if (owner == null || owner.getIndex() != p.getIndex())
						continue;
					return l;
				}
			}
		}
		return null;
	}

	/**
	 * Clears a position of ground items
	 * 
	 * @param pos
	 *            The position to remove all ground items on
	 * @param owner
	 *            The owner of the grounditems to remove
	 */
	public static void clearArea(Position pos, String owner) {
		for (GroundItem l : groundItems) {
			if (l == null || l.getPosition().getZ() != pos.getZ())
				continue;
			if (l.getPosition().equals(pos) && l.getOwner().equals(owner))
				remove(l, true);
		}
	}

	public static CopyOnWriteArrayList<GroundItem> getGroundItems() {
		return groundItems;
	}
}
