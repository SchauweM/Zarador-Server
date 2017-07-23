package com.zarador.model;

import com.zarador.util.Misc;
import com.zarador.world.World;
import com.zarador.world.content.PlayerPanel;
import com.zarador.world.entity.impl.player.Player;

/**
 * Chaos Vote Books
 * @Author Jonny
 */
public class VoteTokens {

	/**
	 * Open your vote token and receive rewards
	 * @param player
	 */
	public static void openToken(Player player) {

		Item reward = null;

		if(Misc.inclusiveRandom(1, 10000) == 1000) {
			reward = ultraRareRewards[Misc.getRandom(ultraRareRewards.length - 1)];
		}

		player.getInventory().delete(10944, 1);

		int votePoints = 1;
		int dungTokens = 10000;
		int doubleXP = 60 * 60; //60 minutes
		player.getPointsHandler().incrementVotingPoints(votePoints);
		player.getPointsHandler().setDungeoneeringTokens(dungTokens, true);
		player.addDoubleXP(doubleXP);
		PlayerPanel.refreshPanel(player);

		if(reward != null) {
			player.getInventory().add(reward);
		}

		player.getPacketSender().sendMessage("<col=C70000>You now have "+Misc.format(player.getDoubleXP() / 60) +" minutes of double XP, "+Misc.format(player.getPointsHandler().getDungeoneeringTokens())+" Dungeoneering Tokens,");

		if(reward != null) {
			player.getPacketSender().sendMessage("<col=C70000>" + player.getPointsHandler().getVotingPoints() + " Vote Points, and you have received " + reward.getDefinition().getName() + " as a reward.");
			sendAnnouncment(player.getUsername(), reward);
		} else {
			player.getPacketSender().sendMessage("<col=C70000>and " + player.getPointsHandler().getVotingPoints() + " Vote Points.");
		}
		player.getPacketSender().sendMessage("<col=C70000><shad=0>Thank you for voting for Argos!");
	}

	/**
	 * Ultra rare rewards (1/10000)
	 */
	public static Item[] ultraRareRewards = {
		new Item(1038, 1),
		new Item(1040, 1),
		new Item(1042, 1),
		new Item(1044, 1),
		new Item(1046, 1),
		new Item(1048, 1),
		new Item(21024, 1),
		new Item(21025, 1),
		new Item(21026, 1),
		new Item(21049, 1),
		new Item(21109, 1),
		new Item(21118, 1),
		new Item(21119, 1),
		new Item(1050, 1),
		new Item(21035, 1),
		new Item(21036, 1),
		new Item(21037, 1),
		new Item(21038, 1),
		new Item(21039, 1),
		new Item(21040, 1),
		new Item(21041, 1),
		new Item(21048, 1),
	};

	/**
	 * Checks to see if the item should get an announcement then processes if it can.
	 * @param playerName
	 * @param item
	 */
	public static void sendAnnouncment(String playerName, Item item) {
		World.sendMessage("<icon=1><shad=FF8C38>[News] " + playerName + " has received a " + item.getDefinition().getName() + " from ::vote.");
	}
}
