package com.zarador.net.packet.impl;

import com.zarador.GameSettings;
import com.zarador.model.GroundItem;
import com.zarador.model.Item;
import com.zarador.model.Position;
import com.zarador.model.action.distance.DistanceToGroundItemAction;
import com.zarador.net.packet.Packet;
import com.zarador.net.packet.PacketListener;
import com.zarador.world.entity.impl.GroundItemManager;
import com.zarador.world.entity.impl.player.Player;

/**
 * This packet listener is used to pick up ground items that exist in the world.
 * 
 * @author relex lawl
 */

public class PickupItemPacketListener implements PacketListener {

	@Override
	public void handleMessage(final Player player, Packet packet) {
		final int y = packet.readLEShort();
		final int itemId = packet.readShort();
		final int x = packet.readLEShort();
		if (player.isTeleporting())
			return;
		final Position position = new Position(x, y, player.getPosition().getZ());
		if (!player.getLastItemPickup().elapsed(100))
			return;
		if (player.getConstitution() <= 0 || player.isTeleporting())
			return;
		player.setWalkToTask(null);
		player.setCastSpell(null);
		player.getCombatBuilder().cooldown(false);
		if (GameSettings.DEBUG_MODE) {
			// PlayerLogs.log(player,
			// "" + player.getUsername() + " in PickupItemPacketListener: " +
			// itemId + "");
		}
		GroundItem gItem = GroundItemManager.getGroundItem(player, new Item(itemId), position);
		if (gItem != null) {
			player.getActionQueue().addAction(new DistanceToGroundItemAction(player, gItem));
		}

	}
}
