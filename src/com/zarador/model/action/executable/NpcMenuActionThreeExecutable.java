package com.zarador.model.action.executable;

import com.zarador.executable.Executable;
import com.zarador.world.entity.impl.npc.NPC;
import com.zarador.world.entity.impl.player.Player;

public final class NpcMenuActionThreeExecutable implements Executable {
	private final Player player;
	private final NPC npc;

	public NpcMenuActionThreeExecutable(Player player, NPC npc) {
		this.player = player;
		this.npc = npc;
	}

	@Override
	public int execute() {
		player.getActions().thirdClickNpc(npc);
		return STOP;
	}
}
