package com.zarador.world.content;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zarador.util.Misc;
import com.zarador.world.entity.impl.player.Player;

/**
 *
 * @author Jonathan Sirens
 */
public final class Scoreboard {

	private static final String PKERS_DIRECTORY = "./data/saves/scoreboards/pkers_scoreboard.json";

	private static final String KILLSTREAKS_DIRECTORY = "./data/saves/scoreboards/killstreaks_scoreboard.json";

	private static final String TOPEXP_DIRECTORY = "./data/saves/scoreboards/topexp_scoreboard.json";

	private static final String TOURNAMENT_DIRECTORY = "./data/saves/scoreboards/tournament_scoreboard.json";

	public static void save() throws IOException {
		final Gson gson = new GsonBuilder().setPrettyPrinting().create();

		File file = new File(PKERS_DIRECTORY);
		FileWriter fileWriter = new FileWriter(file);
		gson.toJson(PKERS, fileWriter);
		fileWriter.close();

		file = new File(KILLSTREAKS_DIRECTORY);
		fileWriter = new FileWriter(file);
		gson.toJson(KILLSTREAKS, fileWriter);
		fileWriter.close();

		file = new File(TOPEXP_DIRECTORY);
		fileWriter = new FileWriter(file);
		gson.toJson(TOP_EXP, fileWriter);
		fileWriter.close();

		file = new File(TOURNAMENT_DIRECTORY);
		fileWriter = new FileWriter(file);
		gson.toJson(TOURNAMENT, fileWriter);
		fileWriter.close();
	}

	public static void load() throws IOException {
		final Gson gson = new GsonBuilder().setPrettyPrinting().create();

		File file = new File(PKERS_DIRECTORY);
		if (file.exists()) {
			FileReader fileReader = new FileReader(file);

			final ArrayList<Streak> pkerStreak = gson.fromJson(fileReader, new TypeToken<ArrayList<Streak>>() {
			}.getType());
			if (pkerStreak != null)
				PKERS = pkerStreak;
			fileReader.close();
		}

		file = new File(KILLSTREAKS_DIRECTORY);
		if (file.exists()) {
			FileReader fileReader = new FileReader(file);

			final ArrayList<Streak> killstreaksStrak = gson.fromJson(fileReader, new TypeToken<ArrayList<Streak>>() {
			}.getType());
			if (killstreaksStrak != null)
				KILLSTREAKS = killstreaksStrak;
			fileReader.close();
		}

		file = new File(TOPEXP_DIRECTORY);
		if (file.exists()) {
			FileReader fileReader = new FileReader(file);

			final ArrayList<Streak> topexp_streak = gson.fromJson(fileReader, new TypeToken<ArrayList<Streak>>() {
			}.getType());
			if (topexp_streak != null)
				TOP_EXP = topexp_streak;
			fileReader.close();
		}

		file = new File(TOURNAMENT_DIRECTORY);
		if (file.exists()) {
			FileReader fileReader = new FileReader(file);

			final ArrayList<Streak> tournamentStreak = gson.fromJson(fileReader, new TypeToken<ArrayList<Streak>>() {
			}.getType());
			if (tournamentStreak != null)
				TOURNAMENT = tournamentStreak;
			fileReader.close();
		}
	}

	public static void open(Player player, int type) {

		List<Streak> streakList = PKERS;
		switch (type) {
		case 1:
			player.getPacketSender().sendString(6399, "Argos's Top PK'ers");
			streakList = PKERS;
			break;
		case 2:
			player.getPacketSender().sendString(6399, "Argos's Top Killstreaks");
			streakList = KILLSTREAKS;
			break;
		case 3:
			player.getPacketSender().sendString(6399, "Argos's Top Skillers");
			streakList = TOP_EXP;
			break;
		case 4:
			player.getPacketSender().sendString(6399, "Argos's Tournament Leaders");
			streakList = TOURNAMENT;
			break;
		}

		Collections.sort(streakList, STREAK_COMPARATOR);

		int index = 0;
		for (Streak streak : streakList) {
			if (index >= CHILD_IDS.length)
				break;
			final String username = streak.username;
			final int kills = streak.kills;
			final long amt = streak.longamt;
			switch (type) {
			case 1:
				player.getPacketSender().sendString(CHILD_IDS[index],
						"@whi@" + (index + 1) + ". " + username + ": @or2@Kills @whi@- " + Misc.format(kills));
				break;
			case 2:
				player.getPacketSender().sendString(CHILD_IDS[index],
						"@whi@" + (index + 1) + ". " + username + ": @or2@Killstreak @whi@- " + Misc.format(kills));
				break;
			case 3:
				player.getPacketSender().sendString(CHILD_IDS[index],
						"@whi@" + (index + 1) + ". " + username + ": @or2@Experience @whi@- " + Misc.format(amt));
				break;
			case 4:
				player.getPacketSender().sendString(CHILD_IDS[index], "@whi@" + (index + 1) + ". " + username
						+ ": @or2@Tournament Points @whi@- " + Misc.format(amt));
				break;
			}
			index++;
		}
		for (int i = index; i < CHILD_IDS.length; i++) {
			player.getPacketSender().sendString(CHILD_IDS[i], "");
		}

		player.getPacketSender().sendInterface(6308);
	}

	public static void update(Player player, int type) {
		switch (type) {
		case 1:
			Streak streak = new Streak(player.getUsername(), 0);
			int index = PKERS.lastIndexOf(streak);
			if (index != -1) {
				if (player.getPlayerKillingAttributes().getPlayerKills() > 0)
					PKERS.set(index,
							new Streak(player.getUsername(), player.getPlayerKillingAttributes().getPlayerKills()));
			} else {
				PKERS.add(streak);
			}
			break;
		case 2:
			Streak killstreak_streak = new Streak(player.getUsername(), 0);
			int index2 = KILLSTREAKS.lastIndexOf(killstreak_streak);
			if (index2 != -1) {
				if (player.getPlayerKillingAttributes().getPlayerKillStreak() > 0)
					KILLSTREAKS.set(index2, new Streak(player.getUsername(),
							player.getPlayerKillingAttributes().getPlayerKillStreak()));
			} else {
				KILLSTREAKS.add(killstreak_streak);
			}
			break;
		case 3:
			Streak topexp_streak = new Streak(player.getUsername(), 0);
			int index3 = TOP_EXP.lastIndexOf(topexp_streak);
			if (index3 != -1) {
				if (player.getSkillManager().getTotalExp() > 0)
					TOP_EXP.set(index3, new Streak(player.getUsername(), player.getSkillManager().getTotalExp()));
			} else {
				TOP_EXP.add(topexp_streak);
			}
			break;
		case 4:
			Streak tournamentStreak = new Streak(player.getUsername(), 0);
			int index4 = TOURNAMENT.lastIndexOf(tournamentStreak);
			if (index4 != -1) {
				if (player.getPointsHandler().getTournamentPoints() > 0)
					TOURNAMENT.set(index4,
							new Streak(player.getUsername(), player.getPointsHandler().getTournamentPoints()));
			} else {
				TOURNAMENT.add(tournamentStreak);
			}
			break;
		}
	}

	private static ArrayList<Streak> PKERS = new ArrayList<>();

	private static ArrayList<Streak> KILLSTREAKS = new ArrayList<>();

	private static ArrayList<Streak> TOP_EXP = new ArrayList<>();

	private static ArrayList<Streak> TOURNAMENT = new ArrayList<>();

	private static final int[] CHILD_IDS = { 6402, 6403, 6404, 6405, 6406, 6407, 6408, 6409, 6410, 6411, 8578, 8579,
			8580, 8581, 8582, 8583, 8584, 8585, 8586, 8587, 8588, 8589, 8590, 8591, 8592, 8593, 8594, 8595, 8596, 8597,
			8598, 8599, 8600, 8601, 8602, 8603, 8604, 8605, 8606, 8607, 8608, 8609, 8610, 8611, 8612, 8613, 8614, 8615,
			8616, 8617 };

	private static final Comparator<Streak> STREAK_COMPARATOR = new Comparator<Streak>() {

		@Override
		public int compare(Streak o1, Streak o2) {
			if (o1 == null && o2 == null) {
				return 0;
			}
			if (o1 == null) {
				return -1;
			}
			if (o2 == null) {
				return 1;
			}
			if (o2.longamt > 0) {
				return Long.compare(o2.longamt, o1.longamt);
			}
			return Integer.compare(o2.kills, o1.kills);
		}

	};

	private static final class Streak {

		private Streak(String username, int kills) {
			this.username = username;
			this.kills = kills;
		}

		private Streak(String username, long kills) {
			this.username = username;
			this.longamt = kills;
		}

		private String username;

		private int kills;

		private long longamt;

		@Override
		public boolean equals(Object object) {
			if (object.getClass() != getClass())
				return false;
			final Streak other = (Streak) object;
			return other.username.equals(username);
		}
	}
}
