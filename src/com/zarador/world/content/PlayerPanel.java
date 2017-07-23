package com.zarador.world.content;

import com.zarador.util.Misc;
import com.zarador.world.content.minigames.impl.RecipeForDisaster;
import com.zarador.world.content.wells.WellOfGoodness;
import com.zarador.world.entity.impl.player.Player;

public class PlayerPanel {

	public static void refreshPanel(Player player) {
		Scoreboard.update(player, 1);
		Scoreboard.update(player, 2);
		Scoreboard.update(player, 4);
		int kdr = 0;
		if (player.getPlayerKillingAttributes().getPlayerDeaths() > 0) {
			kdr = player.getPlayerKillingAttributes().getPlayerKills() / player.getPlayerKillingAttributes().getPlayerDeaths();
		} else {
		    kdr = player.getPlayerKillingAttributes().getPlayerKills();
        }
//		player.getPacketSender().sendString(55067, " Server Time: " + Misc.getCurrentServerTime());
		player.getPacketSender().sendString(55072, "  @whi@[Population]");
//		player.getPacketSender().sendString(55073, "   >- Players online: @cha@" + World.getPlayers().size());
//		player.getPacketSender().sendString(55074, "   >- Staff online: @cha@" + World.staffOnline());
//		player.getPacketSender().sendString(55075, "   >- Wilderness: @cha@" + Locations.PLAYERS_IN_WILD);
		player.getPacketSender().sendString(55076, "  @whi@[Personal]");
		player.getPacketSender().sendString(55077, "   >- Title: @cha@" + player.getLoyaltyRankString(player.getLoyaltyRank()));
//		player.getPacketSender().sendString(55078, "   >- Play Time: @cha@" + player.getTotalPlayTime());
		player.getPacketSender().sendString(55079, "   >- Game mode: @cha@ " + player.getGameModeAssistant().getModeName());
		player.getPacketSender().sendString(55080, "   >- Donator: @cha@ " + player.getDonatorRights().getTitle());
		player.getPacketSender().sendString(55081, "  @whi@[Points]");
		player.getPacketSender().sendString(55082, "   >- Pk points: @cha@ " + player.getPointsHandler().getPkPoints());
		player.getPacketSender().sendString(55083, "   >- Vote points: @cha@ " + player.getPointsHandler().getVotingPoints());
		player.getPacketSender().sendString(55084, "   >- Slayer points: @cha@ " + player.getPointsHandler().getSlayerPoints());
		player.getPacketSender().sendString(55085, "   >- Commendations: @cha@ " + player.getPointsHandler().getCommendations());
		player.getPacketSender().sendString(55086, "   >- Dung tokens: @cha@ " + player.getPointsHandler().getDungeoneeringTokens());
		player.getPacketSender().sendString(55087, "  @whi@[PvP]");
		player.getPacketSender().sendString(55088, "   >- Kdr: @cha@ " + kdr);
		player.getPacketSender().sendString(55089, "   >- Kills: @cha@ " + player.getPlayerKillingAttributes().getPlayerKills());
		player.getPacketSender().sendString(55090, "   >- Deaths: @cha@ " + player.getPlayerKillingAttributes().getPlayerDeaths());
		player.getPacketSender().sendString(55091, "   >- Killstreak: @cha@ " + player.getPlayerKillingAttributes().getPlayerKillStreak());
		player.getPacketSender().sendString(55092, "   >- Duel Victories: @cha@ " + player.getDueling().arenaStats[0]);
		player.getPacketSender().sendString(55093, "   >- Duel Losses: @cha@ " + player.getDueling().arenaStats[1]);
		player.getPacketSender().sendString(55094, "  @whi@[Slayer]");
		if(player.getSlayer().getSlayerMaster() == null) {
			player.getPacketSender().sendString(55095, "   >- Slayer Master: @cha@None");
		} else {
			player.getPacketSender().sendString(55095, "   >- Slayer Master: @cha@" + Misc.formatText(player.getSlayer().getSlayerMaster().toString().toLowerCase().replaceAll("_", " ")));
		}
		if (player.getSlayer().getSlayerTask() == null) {
			player.getPacketSender().sendString(55096, "   >- Task: @cha@None");
		} else {
			player.getPacketSender().sendString(55096, "   >- Task: @cha@" + player.getSlayer().getTaskName() + "s");
		}
		player.getPacketSender().sendString(55097, "   >- Task Amount: @cha@" + player.getSlayer().getAmountLeft());
		player.getPacketSender().sendString(55098, "   >- Task Streak: @cha@" + player.getSlayer().getSlayerStreak());
		if (player.getSlayer().getDuoSlayerName() != null) {
			player.getPacketSender().sendString(55099, "   >- Duo Partner: @cha@" + player.getSlayer().getDuoSlayerName() + "");
		} else {
			player.getPacketSender().sendString(55099, "   >- Duo Partner: @cha@None");
		}
		player.getPacketSender().sendString(55100, "  @whi@[Wells]");

		if (WellOfGoodness.isActive("exp")) {
			player.getPacketSender().sendString(55101, "   >- Well of Exp: @cha@ Active");
		} else {
			player.getPacketSender().sendString(55101, "   >- Well of Exp: @cha@ N/A");
		}
		if (WellOfGoodness.isActive("drops")) {
			player.getPacketSender().sendString(55102, "   >- Well of Wealth: @cha@ Active");
		} else {
			player.getPacketSender().sendString(55102, "   >- Well of Wealth: @cha@ N/A");
		}
		if (WellOfGoodness.isActive("pkp")) {
			player.getPacketSender().sendString(55103, "   >- Well of Execution: @cha@ Active");
		} else {
			player.getPacketSender().sendString(55103, "   >- Well of Execution: @cha@ N/A");
		}
		player.getPacketSender().sendString(55104, "  @whi@[Toggles]");
		player.getPacketSender().sendString(55105, "   >- Open Npc Kill Log");
		player.getPacketSender().sendString(55106, "   >- Open Drop Log");
		player.getPacketSender().sendString(55107, "   >- Open Drop Generator");
		for (int i = 55108; i < 55117; i++) {
			player.getPacketSender().sendString(i, " ");
		}


		/*   ********* Quest Panel **********     */


		player.getPacketSender().sendString(55202, "Quest Points: " + player.getQuestPoints());

		player.getPacketSender().sendString(55207,
				RecipeForDisaster.getQuestTabPrefix(player) + "Recipe For Disaster");
		for (int i = 55208; i < 55240; i++) {
			player.getPacketSender().sendString(i, " ");
		}
	}

}
