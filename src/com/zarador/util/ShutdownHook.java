package com.zarador.util;

import java.io.IOException;
import java.util.logging.Logger;

import com.zarador.GameServer;
import com.zarador.world.World;
import com.zarador.world.content.Scoreboard;
import com.zarador.world.content.clan.ClanChatManager;
import com.zarador.world.content.lottery.LotterySaving;
import com.zarador.world.content.pos.PlayerOwnedShops;
import com.zarador.world.content.wells.WellOfGoodness;
import com.zarador.world.entity.impl.player.Player;
import com.zarador.world.entity.impl.player.PlayerHandler;

public class ShutdownHook extends Thread {

	/**
	 * The ShutdownHook logger to print out information.
	 */
	private static final Logger logger = Logger.getLogger(ShutdownHook.class.getName());

	@Override
	public void run() {
		logger.info("The shutdown hook is processing all required actions...");
		World.savePlayers();
		GameServer.setUpdating(true);
		for (Player player : World.getPlayers()) {
			if (player != null) {
				PlayerHandler.handleLogout(player);
			}
		}
		try {
			Scoreboard.save();
		} catch (IOException e) {

		}
		WellOfGoodness.save();
		ClanChatManager.save();
		LotterySaving.save();
		PlayerOwnedShops.save();
		logger.info("The shudown hook actions have been completed, shutting the server down...");
	}
}
