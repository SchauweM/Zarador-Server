package com.zarador.engine.task.impl;

import com.zarador.engine.task.Task;
import com.zarador.world.entity.impl.player.Player;

/**
 * Staff of light special attack
 *
 * @author Gabriel Hannason
 */
public class ToxicStaffOfDeadSpecialAttackTask extends Task {

	public ToxicStaffOfDeadSpecialAttackTask(Player player) {
		super(2, player, false);
		this.player = player;
	}

	private Player player;

	@Override
	public void execute() {
		if (player == null || !player.isRegistered()) {
			stop();
			return;
		}

		player.decrementStaffOfLightEffect();

		if (!player.hasStaffOfLightEffect()) {
			player.getPacketSender().sendMessage("Your Toxic Staff of the Dead's effect has faded away!");
			player.setStaffOfLightEffect(-1);
			stop();
		}
	}
}
