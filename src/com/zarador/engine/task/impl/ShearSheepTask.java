package com.zarador.engine.task.impl;

import com.zarador.engine.task.Task;
import com.zarador.model.Flag;
import com.zarador.world.entity.impl.npc.NPC;
import com.zarador.world.entity.impl.player.Player;

public class ShearSheepTask extends Task {
	private int timer = 60;
	private NPC sheep;
	public ShearSheepTask(Player player, NPC sheep) {
		super(1, player, true);
		this.player = player;
		this.sheep = sheep;
	}

	final Player player;

	@Override
	public void execute() {
		if (player == null || !player.isRegistered()) {
			stop();
			return;
		}
		timer = timer - 1;
		if (timer > 0) {
		} else {
			//sheep was wool again
			sheep.setTransformationId(43);
			sheep.getUpdateFlag().flag(Flag.TRANSFORM);
			timer = 0;
			stop();
		}
	}
}
