package com.zarador.net.packet.impl;

import com.zarador.engine.task.Task;
import com.zarador.engine.task.TaskManager;
import com.zarador.model.RegionInstance.RegionInstanceType;
import com.zarador.net.packet.Packet;
import com.zarador.net.packet.PacketListener;
import com.zarador.util.Misc;
import com.zarador.world.content.CustomObjects;
import com.zarador.world.content.Sounds;
import com.zarador.world.entity.impl.GroundItemManager;
import com.zarador.world.entity.impl.npc.NPC;
import com.zarador.world.entity.impl.player.Player;
import com.zarador.world.entity.updating.NPCUpdating;

public class RegionChangePacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player.isAllowRegionChangePacket()) {
			CustomObjects.handleRegionChange(player);
			GroundItemManager.handleRegionChange(player);
			Sounds.handleRegionChange(player);
			player.getTolerance().reset();
			// Hunter.handleRegionChange(player);
			if (player.getRegionInstance() != null && player.getPosition().getX() != 1
					&& player.getPosition().getY() != 1) {
				if (player.getRegionInstance().equals(RegionInstanceType.BARROWS)
						|| player.getRegionInstance().equals(RegionInstanceType.WARRIORS_GUILD))
					player.getRegionInstance().destruct();
			}

			/** NPC FACING **/
			TaskManager.submit(new Task(1, player, false) {
				@Override
				protected void execute() {
					for (NPC npc : player.getLocalNpcs()) {
						if (npc == null)
							continue;
						NPCUpdating.updateFacing(player, npc);
					}
					stop();
				}
			});

			player.getPlayerTimers().setAggressiveDelay(Misc.secondsToTicks(10));

			player.setRegionChange(false).setAllowRegionChangePacket(false);
		}
	}
}
