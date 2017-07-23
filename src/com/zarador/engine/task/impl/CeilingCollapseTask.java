package com.zarador.engine.task.impl;

import com.zarador.engine.task.Task;
import com.zarador.model.CombatIcon;
import com.zarador.model.Graphic;
import com.zarador.model.Hit;
import com.zarador.model.Hitmask;
import com.zarador.model.Locations.Location;
import com.zarador.util.Misc;
import com.zarador.world.entity.impl.player.Player;

/**
 * Barrows
 * 
 * @author Gabriel Hannason
 */
public class CeilingCollapseTask extends Task {

	public CeilingCollapseTask(Player player) {
		super(9, player, false);
		this.player = player;
	}

	private Player player;

	@Override
	public void execute() {
		if (player == null || !player.isRegistered() || player.getLocation() != Location.BARROWS
				|| player.getLocation() == Location.BARROWS && player.getPosition().getY() < 8000) {
			player.getPacketSender().sendCameraNeutrality();
			stop();
			return;
		}
		player.performGraphic(new Graphic(60));
		player.getPacketSender().sendMessage("Some rocks fall from the ceiling and hit you.");
		player.forceChat("Ouch!");
		player.dealDamage(null, new Hit(30 + Misc.getRandom(20), Hitmask.RED, CombatIcon.BLOCK));
	}
}
