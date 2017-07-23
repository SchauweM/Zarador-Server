package com.zarador.model.action.distance;

import com.zarador.executable.Executable;
import com.zarador.model.GroundItem;
import com.zarador.model.Item;
import com.zarador.model.Position;
import com.zarador.model.action.PlayerAction;
import com.zarador.model.definitions.ItemDefinition;
import com.zarador.world.World;
import com.zarador.world.entity.impl.GroundItemManager;
import com.zarador.world.entity.impl.player.Player;

public final class DistanceToGroundItemAction extends PlayerAction {
	private final GroundItem groundItem;

	public DistanceToGroundItemAction(Player player, GroundItem groundItem) {
		super(player);
		this.groundItem = groundItem;
	}

	@Override
	public ActionPolicy getActionPolicy() {
		return ActionPolicy.CLEAR;
	}

	@Override
	public int execute() {
		if (groundItem  == null) {
			return Executable.STOP;
		}
		Position position = player.getPosition();
		if (!position.equals(groundItem.getPosition()) && !itemOnObject()) {
			return 1;
		}
		Item item = groundItem.getItem();
		boolean canPickup = player.getInventory().getFreeSlots() > 0
				|| (player.getInventory().getFreeSlots() == 0 && ItemDefinition.forId(item.getId()).isStackable()
				&& player.getInventory().contains(item.getId()));
		if (!canPickup) {
			player.getInventory().full();
			return Executable.STOP;
		}
		if(!groundItem.getItem().tradeable(player) && !groundItem.getOwner().equals(player.getUsername())) {
			player.getPacketSender().sendMessage("You cannot pickup other player's untradable items.");
			return Executable.STOP;
		}

		if (player.getInventory().getAmount(groundItem.getItem().getId())
				+ groundItem.getItem().getAmount() > Integer.MAX_VALUE
				|| player.getInventory().getAmount(groundItem.getItem().getId())
				+ groundItem.getItem().getAmount() <= 0) {
			player.getPacketSender()
					.sendMessage("You cannot hold that amount of this item. Clear your inventory!");
			return Executable.STOP;
		}
		GroundItemManager.pickupGroundItem(player, new Item(item.getId()),
				new Position(groundItem.getPosition().getX(), groundItem.getPosition().getY(), player.getPosition().getZ()));
		return Executable.STOP;
	}

    private boolean itemOnObject() {
        return World.tileBlocked(groundItem.getPosition()) && groundItem.getPosition().distanceTo(player.getPosition()) == 1;
    }

}
