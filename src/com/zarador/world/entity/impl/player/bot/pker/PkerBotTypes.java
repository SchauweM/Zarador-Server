package com.zarador.world.entity.impl.player.bot.pker;
import java.util.EnumSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public enum PkerBotTypes {
	MASTER, PURE;

	public static final Set<PkerBotTypes> SET = EnumSet.allOf(PkerBotTypes.class);

	public static PkerBotTypes getRandom() {
		int index = 0;
		int randomElement = ThreadLocalRandom.current().nextInt(SET.size());
		for (PkerBotTypes type : SET) {
			if (index == randomElement)
				return type;
			index++;
		}
		return null;
	}
}
