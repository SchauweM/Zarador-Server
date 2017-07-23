package com.zarador.model.action.executable;

import com.zarador.executable.Executable;
import com.zarador.world.entity.impl.npc.NPC;
import com.zarador.world.entity.impl.player.Player;

public final class NpcMenuActionTwoExecutable implements Executable {
	private final Player player;
	private final NPC npc;

	public NpcMenuActionTwoExecutable(Player player, NPC npc) {
		this.player = player;
		this.npc = npc;
	}

	@Override
	public int execute() {
		player.getActions().secondClickNpc(npc);
		return STOP;
	}
}
