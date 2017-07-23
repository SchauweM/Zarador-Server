package com.zarador.world.entity.impl.player.bot;

import java.util.ArrayList;
import java.util.List;

import com.zarador.world.entity.impl.player.bot.segment.*;

public final class BotTask {
	
	public static String TESTING = "12elf";
	
	private final Bot bot;
	
	private final List<BotSegment> segments = new ArrayList<>();
	
	BotTask(Bot bot) {
		this.bot = bot;
		BotUtil.addBot(bot);

		segments.add(new RegearSegment(bot));
		segments.add(new TeleportationBotSegment(bot));
		segments.add(new ObtainingFightSegment(bot));
		segments.add(new DuringFightBotSegment(bot));
	}

	public int execute() {
		try {
			for (BotSegment segment : segments) {
				if (segment.canExec()) {
				if (bot.getUsername().equalsIgnoreCase(TESTING)) {
						System.err.println("segment:" + segment.getClass().getSimpleName());
					}
					return segment.execute();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	

}
