package com.zarador.world.content.achievements;

import com.zarador.util.Misc;
import com.zarador.world.entity.impl.player.Player;

public class Achievements {

	public enum AchievementData {

		TELEPORT_HOME(Difficulty.EASY, Category.DISTRACTIONS, "No Place like Home", "Cast the Home Teleport spell.", null),
		BURY_BIG_BONE(Difficulty.EASY, Category.SKILLING, "I wonder if it'll Sprout", "Bury a big bone", null),
		CATCH_LOBSTER(Difficulty.EASY, Category.SKILLING, "Nice Catch!", "Catch a lobster while fishing", null),
		COOK_LOBSTER(Difficulty.EASY, Category.SKILLING, "Does it Scream?!", "Cook a raw lobster succesfully", null),
		EAT_LOBSTER(Difficulty.EASY, Category.DISTRACTIONS, "Omnomnom!",  "Eat a cooked lobster", null),
		MINE_IRON(Difficulty.EASY, Category.SKILLING, "Doing the Ironing", "Mine an iron ore", null),
		SMELT_IRON(Difficulty.EASY, Category.SKILLING, "Bar One", "Smelt an iron bar", null),
		SMITH_IRON_DAGGER(Difficulty.EASY, Category.SKILLING, "Cutting Edge", "Smith an iron dagger", null),
		CHOP_WILLOW(Difficulty.EASY, Category.SKILLING, "Timbeeer!", "Chop a willow tree", null),
		BURN_WILLOW(Difficulty.EASY, Category.SKILLING, "Got a Lighter?", "Burn a willow log", null),
		MAKE_POTION(Difficulty.EASY, Category.SKILLING, "Potion Brewer", "Make a potion", null),
		GNOME_COURSE(Difficulty.EASY, Category.SKILLING, "Just a Small Step", "Complete the Gnome Agility Course", null),
		FLETCH_ARROW_SHAFT(Difficulty.EASY, Category.SKILLING, "On The Straight And Arrow", "Fletch an arrow shaft", null),
		RUNECRAFT_RUNES(Difficulty.EASY, Category.SKILLING, "Elemental Craft", "Runecraft any elemental rune", null),
		COMPLETE_DUNG_FLOOR(Difficulty.EASY, Category.SKILLING, "Sweeping the Floor", "Complete a dungeoneering floor", null), //TODO
		KILL_ROCKCRAB(Difficulty.EASY, Category.COMBAT, "Dead Crab", "Kill a rock crab", null),
		KILL_SKELETON(Difficulty.EASY, Category.COMBAT, "Spooky Skeleton", "Kill a skeleton, doot doot", null),
		KILL_YAK(Difficulty.EASY,  Category.COMBAT, "Yak Attack", "Kill a yak", null),
		INFUSE_WOLF_POUCH(Difficulty.EASY, Category.SKILLING, "Pocket Wolf", "Infuse a wolf pouch", null),
		CREATE_CLAN_CHAT(Difficulty.EASY, Category.DISTRACTIONS, "Ensemble!", "Create a clan chat.", null), //TODO
		ADD_FRIEND(Difficulty.EASY, Category.DISTRACTIONS, "Making Friends", "Add someone to your friends list.", null),
		GET_SLAYER_TASK(Difficulty.EASY, Category.SKILLING, "The First Task", "Get a slayer task", null),
		SWITCH_SPELLBOOK(Difficulty.EASY, Category.DISTRACTIONS, "Magical Librarian", "Switch to another spellbook.", null), //TODO
		SWITCH_PRAYBOOK(Difficulty.EASY, Category.DISTRACTIONS, "Dark Beliefs", "Switch to the curse prayers.", null), //TODO
		KILL_A_MONSTER_USING_MELEE(Difficulty.EASY, Category.COMBAT, "Final Blow", "Kill a Monster using Melee", null),
		KILL_A_MONSTER_USING_RANGED(Difficulty.EASY, Category.COMBAT, "Final Hit", "Kill a Monster using Ranged", null),
		KILL_A_MONSTER_USING_MAGIC(Difficulty.EASY, Category.COMBAT, "Final Strike", "Kill a Monster using Magic", null),
		DEAL_EASY_DAMAGE_USING_MELEE(Difficulty.EASY, Category.COMBAT, "Melee Damage I", "Deal 1000 Melee Damage", new int[] {0, 1000 }),
		DEAL_EASY_DAMAGE_USING_RANGED(Difficulty.EASY, Category.COMBAT, "Ranged Damage I", "Deal 1000 Ranged Damage", new int[] {1, 1000 }),
		DEAL_EASY_DAMAGE_USING_MAGIC(Difficulty.EASY, Category.COMBAT, "Magic Damage I", "Deal 1000 Magic Damage", new int[] {2, 1000 }),
		PERFORM_A_SPECIAL_ATTACK(Difficulty.EASY, Category.COMBAT, "Making it Special", "Perform a Special Attack", null),
		BLOW_KISS(Difficulty.EASY, Category.DISTRACTIONS, "Kissing Booth", "Blow a Kiss", null),
        BURY_50_DRAGON_BONES(Difficulty.EASY, Category.SKILLING, "Grave Digger I", "Bury 50 dragon bones", new int[] { 3, 50 }),
        
		CHOP_250_MAPLE_LOGS(Difficulty.MEDIUM, Category.SKILLING, "Chop it Down I", "Cut 250 Maple Logs", new int[] { 4, 250 }),
		BURN_200_MAPLE_LOGS(Difficulty.MEDIUM, Category.SKILLING, "Burn it Down I", "Burn 200 Maple Logs", new int[] { 5, 200 }),
		FISH_100_SHARKS(Difficulty.MEDIUM, Category.SKILLING, "Fisherman I", "Fish 100 sharks", new int[] { 6, 100 }),
		COOK_100_SHARKS(Difficulty.MEDIUM, Category.SKILLING, "Master Chef I", "Cook 100 sharks", new int[] { 7, 100 }),
		MINE_400_COAL(Difficulty.MEDIUM, Category.SKILLING, "Mining my own Business I", "Mine 400 Coal ores", new int[] { 8, 400 }),
		SMELT_50_MITH_BARS(Difficulty.MEDIUM, Category.SKILLING, "Blasting Furnace I", "Smelt 50 Mithril Bars", new int[] { 9, 50 }),
		COMPLETE_5_DUNG_FLOORS(Difficulty.MEDIUM, Category.SKILLING, "Sweeping Floors I", "Complete 5 Dung Floors", new int[] { 10, 5 }),
		INFUSE_25_TITAN_POUCHES(Difficulty.MEDIUM, Category.SKILLING, "Steel Infusion I", "Infuse 25 Steel Titans", new int[] { 11, 25 }),
		CATCH_5_KINGLY_IMPLINGS(Difficulty.MEDIUM, Category.SKILLING, "King Catching I", "Catch 5 Kingly Implings", new int[] {12, 5 }),
		COMPLETE_A_HARD_SLAYER_TASK(Difficulty.MEDIUM, Category.COMBAT, "Contract Killer", "Complete a Hard Slayer Task", null),
		CRAFT_20_BLACK_DHIDE_BODIES(Difficulty.MEDIUM, Category.SKILLING, "Dragon Fashion", "Craft 20 Black D'hide Bodies", new int[] {13, 20 }),
		FLETCH_450_RUNE_BOLTS(Difficulty.MEDIUM, Category.SKILLING, "So Pointy", "Smith 450 Rune Bolts", new int[] {14, 450 }),
		PICK_POCKET_150_TIMES(Difficulty.MEDIUM, Category.SKILLING, "Watch your Pockets", "Pick-Pocket 150 times", new int[] {15, 150 }),
		BARB_AGILITY(Difficulty.MEDIUM, Category.SKILLING, "The Graceful Barbarian", "Complete Barb Agility Course", null),
		CLIMB_50_AGILITY_OBSTACLES(Difficulty.MEDIUM, Category.SKILLING, "I'm the Fittest", "Climb 50 Agility Obstacles", new int[] {16, 50 }),
		RUNECRAFT_500_NATS(Difficulty.MEDIUM, Category.SKILLING, "At One With Nature", "Craft 500 Nature Runes", new int[] {17, 500 }),
		BURY_25_FROST_DRAGON_BONES(Difficulty.MEDIUM, Category.SKILLING, "Grave Digger II", "Bury 48 Frost Dragon Bones", new int[] {18, 48 }),
		MINE_200_GOLD(Difficulty.MEDIUM, Category.SKILLING, "Gold Digger", "Mine 200 Gold ores", new int[] {19, 200 }),
		DEAL_MEDIUM_DAMAGE_USING_MELEE(Difficulty.MEDIUM, Category.COMBAT, "Melee Damage II", "Deal 100K Melee Damage", new int[] {20, 100000 }),
		DEAL_MEDIUM_DAMAGE_USING_RANGED(Difficulty.MEDIUM, Category.COMBAT, "Ranged Damage II", "Deal 100K Ranged Damage", new int[] {21, 100000 }),
		DEAL_MEDIUM_DAMAGE_USING_MAGIC(Difficulty.MEDIUM, Category.COMBAT, "Magic Damage II", "Deal 100K Magic Damage", new int[] {22, 100000 }),
		DEFEAT_THE_KING_BLACK_DRAGON(Difficulty.MEDIUM, Category.COMBAT, "King Nothing", "Defeat the King Black Dragon", null),
		DEFEAT_THE_CHAOS_ELEMENTAL(Difficulty.MEDIUM, Category.COMBAT, "Chaotic Madness", "Defeat the Chaos Elemental", null),
		DEFEAT_A_TORMENTED_DEMON(Difficulty.MEDIUM, Category.COMBAT, "Tormented Soul", "Defeat a Tormented Demon", null),
		DEFEAT_THE_CULINAROMANCER(Difficulty.MEDIUM, Category.COMBAT, "Have you seen Chef?", "Defeat the Culinaromancer", null),
		DEFEAT_SCORPIA(Difficulty.MEDIUM, Category.SKILLING, "Skorpia King", "Defeat Scorpia", null),
		DEFEAT_10_PLAYERS(Difficulty.MEDIUM, Category.COMBAT, "Blood Lust", "Defeat 10 Players", new int[] {23, 10 }),
		LOW_ALCH_ITEMS(Difficulty.MEDIUM, Category.SKILLING, "The Alchemist", "Low Alch 300 Items", new int[] { 24, 300 }),
		MIX_AN_OVERLOAD_POTION(Difficulty.MEDIUM, Category.SKILLING, "Error, Overload", "Make an Overload Potion", null),

		BURY_500_FROST_DRAGON_BONES(Difficulty.HARD, Category.SKILLING, "Grave Digger III", "Bury 500 Frost Dragon Bones", new int[] {25, 500 }),
		CHOP_750_YEW_LOGS(Difficulty.HARD, Category.SKILLING, "Chop it Down II", "Cut 750 Yew Logs", new int[] { 26, 750 }),
		BURN_500_YEW_LOGS(Difficulty.HARD, Category.SKILLING, "Burn it Down II", "Burn 500 Yew Logs", new int[] { 27, 500 }),
		FISH_700_MANTA(Difficulty.HARD, Category.SKILLING, "Fisherman II", "Fish 700 Manta Ray", new int[] { 28, 700 }),
		COOK_500_MANTA(Difficulty.HARD, Category.SKILLING, "Master Chef II", "Cook 500 Manta Ray", new int[] { 29, 500 }),
		MINE_400_ADDY(Difficulty.HARD, Category.SKILLING, "Mining my own Business II", "Mine 300 Adamant Ores", new int[] { 30, 300 }),
		CRAFT_1000_DIAMOND_GEMS(Difficulty.HARD, Category.SKILLING, "Artisan", "Craft 750 Diamond Gems", new int[] { 31, 750 }),
		ENCHANT_1000_BOLTS(Difficulty.HARD, Category.SKILLING, "Enchanting Ammo", "Enchant 1000 Bolts", new int[] { 32, 1000 }),
		HIGH_ALCH_ITEMS(Difficulty.HARD, Category.SKILLING, "The High Alchemist", "High Alch 1000 Items", new int[] { 33, 1000 }),
		STEAL_2000_TIMES(Difficulty.HARD, Category.SKILLING, "Robin Hood", "Steal 2,000 Times", new int[] { 34, 2000 }),
		SMELT_300_ADAMANT_BARS(Difficulty.HARD, Category.SKILLING, "Blasting Furnace II", "Smelt 300 Adamant Bars", new int[] { 35, 300 }),
		MIX_100_OVERLOAD_POTIONS(Difficulty.HARD, Category.SKILLING, "DJ Overload", "Mix 100 Overload Potions", new int[] { 36, 100 }),
		COMPLETE_AN_ELITE_SLAYER_TASK(Difficulty.HARD, Category.SKILLING, "Slayer Master", "Complete a task from Duradel", null),
		DEAL_HARD_DAMAGE_USING_MELEE(Difficulty.HARD, Category.COMBAT, "Melee Damage III", "Deal 500K Melee Damage", new int[] { 37, 500000 }),
		DEAL_HARD_DAMAGE_USING_RANGED(Difficulty.HARD, Category.COMBAT, "Ranged Damage III", "Deal 500K Ranged Damage", new int[] { 38, 500000 }),
		DEAL_HARD_DAMAGE_USING_MAGIC(Difficulty.HARD, Category.COMBAT, "Magic Damage III", "Deal 500K Magic Damage", new int[] { 39, 500000 }),
		DEFEAT_JAD(Difficulty.HARD, Category.COMBAT, "Defender of TzHaar", "Defeat Tz-Tok-Jad in the Fight Cave", null),
		DEFEAT_BANDOS_AVATAR(Difficulty.HARD, Category.COMBAT, "Breaking the Mould", "Defeat the Bandos Avatar", null),
		DEFEAT_GENERAL_GRAARDOR(Difficulty.HARD, Category.SKILLING, "General Down", "Defeat General Graardor", null),
		DEFEAT_KREE_ARRA(Difficulty.HARD, Category.COMBAT, "Bird in a Cage", "Defeat Kree'Arra", null),
		DEFEAT_COMMANDER_ZILYANA(Difficulty.HARD, Category.COMBAT, "Not my Commander", "Defeat Commander Zilyana", null),
		DEFEAT_KRIL_TSUTSAROTH(Difficulty.HARD, Category.COMBAT, "Bringing the Light", "Defeat K'ril Tsutsaroth", null),
		DEFEAT_THE_CORPOREAL_BEAST(Difficulty.HARD, Category.COMBAT, "Corporeal Death", "Defeat The Corporeal Beast", null),
		DEFEAT_NEX(Difficulty.HARD, Category.COMBAT, "Angel of Death", "Defeat Nex", null),
		DEFEAT_30_PLAYERS(Difficulty.HARD, Category.COMBAT, "Blood Thirsty", "Defeat 30 Players", new int[] { 40, 30 }),

		RUNECRAFT_6000_BLOOD_RUNES(Difficulty.ELITE, Category.SKILLING, "Vampiric Runes", "Runecraft 6000 Blood Runes", new int[] { 41, 6000 }),
		CHOP_2500_MAGIC_LOGS(Difficulty.ELITE, Category.SKILLING, "Chop it Down III", "Chop 2500 Magic Logs", new int[] { 42, 2500 }),
		BURN_2500_MAGIC_LOGS(Difficulty.ELITE, Category.SKILLING, "Burn it Down III", "Burn 2500 Magic Logs", new int[] { 43, 2500 }),
		FISH_1500_ROCKTAILS(Difficulty.ELITE, Category.SKILLING, "Fisherman III", "Fish 1500 Rocktails", new int[] { 44, 1500 }),
		COOK_1000_ROCKTAILS(Difficulty.ELITE, Category.SKILLING, "Master Chef III", "Cook 1000 Rocktails", new int[] { 45, 1000 }),
		MINE_2000_RUNITE_ORES(Difficulty.ELITE, Category.SKILLING, "Mining my own Business III", "Mine 1000 Runite Ores", new int[] { 46, 1000 }),
		SMELT_1000_RUNE_BARS(Difficulty.ELITE, Category.SKILLING, "Blasting Furnace III", "Smelt 1000 Rune Bars", new int[] { 47, 1000 }),
		COMPLETE_50_DUNG_FLOORS(Difficulty.ELITE, Category.SKILLING, "Sweeping Floors II", "Complete 50 Dung Floors", new int[] { 48, 50 }),
		INFUSE_500_STEEL_TITAN_POUCHES(Difficulty.ELITE, Category.SKILLING, "Steel Infusion II", "Infuse 500 Steel Titans", new int[] { 49, 500 }),
		CATCH_100_KINGLY_IMPLINGS(Difficulty.ELITE, Category.SKILLING, "King Catching II", "Catch 100 Kingly Imps", new int[] {50, 100 }),
		FLETCH_5000_RUNE_ARROWS(Difficulty.ELITE, Category.SKILLING, "Straight as an Arrow", "Fletch 5000 Rune Arrows", new int[] {51, 5000 }),
		CUT_AN_ONYX_STONE(Difficulty.ELITE, Category.SKILLING, "Romancing the Stone", "Cut an Onyx Stone", null),
		REACH_MAX_EXP_IN_A_SKILL(Difficulty.ELITE, Category.SKILLING, "Maxing Out", "Reach 200m Exp in a Skill", null),
		HIT_700_WITH_SPECIAL_ATTACK(Difficulty.ELITE, Category.SKILLING, "High Hitting", "Hit 700 with Special Attack", new int[] { 52, 1 }),
		DEFEAT_10000_MONSTERS(Difficulty.ELITE, Category.COMBAT, "Fuck Everything", "Defeat 10,000 Monsters", new int[] { 53, 10000 }),
		DEFEAT_500_BOSSES(Difficulty.ELITE, Category.COMBAT, "You're not my Boss", "Defeat a total of 500 Boss Monsters", new int[] { 54, 500 }),
		;
		public static int SIZE = AchievementData.values().length;

		AchievementData(Difficulty difficulty, Category category, String achievementName, String description, int[] progressData) {
			this.difficulty = difficulty;
			this.category = category;
			this.achievementName = achievementName;
			this.description = description;
			this.progressData = progressData;
		}

		private Difficulty difficulty;
		private Category category;
		private String achievementName;
		private String description;
		public int[] progressData;

		/**
		 * @return Achievement Difficulty
		 */
		public Difficulty getDifficulty() {
			return difficulty;
		}
		
		/**
		 * @return Achievement Category
		 */
		public Category getCategory() {
			return category;
		}

		/**
		 * Get the name of your achievement
		 * @return achievementName
		 */
		public String getName() {
			return this.achievementName;
		}
	}

	public enum Difficulty {
		BEGINNER, EASY, MEDIUM, HARD, ELITE;
	}
	
	public enum Category {
		SKILLING, COMBAT, DISTRACTIONS;
	}

	public static void updateInterface(Player player) {
		for (AchievementData achievement : AchievementData.values()) {
			
			boolean completed = player.getAchievementAttributes().getCompletion()[achievement.ordinal()];
			boolean progress = achievement.progressData != null
					&& player.getAchievementAttributes().getProgress()[achievement.progressData[0]] > 0;
					
			//Update Color interfaceFrame 
			/*player.getPacketSender().sendString(achievement.interfaceFrame,
					(completed ? "@gre@" : progress ? "@yel@" : "@red@") + achievement.achievementName);*/
			
			/*
			 * Get amount of Achievements per Category
			 */
			if (achievement.getCategory() == Category.COMBAT) {
				player.getAchievementAttributes().combatAchievements += 1;
			} else if (achievement.getCategory() == Category.DISTRACTIONS) {
				player.getAchievementAttributes().dndAchievements += 1;
			} else if (achievement.getCategory() == Category.SKILLING) {
				player.getAchievementAttributes().skillingAchievements += 1;
			}
						
			/*
			 * Get amount of completed Achievements per Category
			 */
			if (completed) {
				player.getAchievementAttributes().updateAchievementsPerCat(achievement);
			}
			
			player.getPacketSender().sendString(37010, player.getAchievementAttributes().skillingCompleted + "/" + player.getAchievementAttributes().skillingAchievements);
			player.getPacketSender().sendString(37015, player.getAchievementAttributes().combatCompleted + "/" + player.getAchievementAttributes().combatAchievements);
			player.getPacketSender().sendString(37020, player.getAchievementAttributes().dndCompleted + "/" + player.getAchievementAttributes().dndAchievements);
		}
		player.getPacketSender().sendString(37005, "Progress: " + player.getPointsHandler().getAchievementPoints()
				+ "/" + AchievementData.values().length);
		player.getPacketSender().sendString(61002, "Achievements Completed(" + player.getPointsHandler().getAchievementPoints()
				+ "/" + AchievementData.values().length + ")");
	}

	public static void setPoints(Player player) {
		int points = 0;
		for (AchievementData achievement : AchievementData.values()) {
			if (player.getAchievementAttributes().getCompletion()[achievement.ordinal()]) {
				points++;
			}
		}
		player.getPointsHandler().setAchievementPoints(points, false);
	}

	public static void doProgress(Player player, AchievementData achievement) {
		doProgress(player, achievement, 1);
	}

	public static void doProgress(Player player, AchievementData achievement, int amt) {
		if (player.getAchievementAttributes().getCompletion()[achievement.ordinal()])
			return;
		if (achievement.progressData != null) {
			int progressIndex = achievement.progressData[0];
			int amountNeeded = achievement.progressData[1];
			int previousDone = player.getAchievementAttributes().getProgress()[progressIndex];
			if ((previousDone + amt) < amountNeeded) {
				player.getAchievementAttributes().getProgress()[progressIndex] = previousDone + amt;
				/*
				if (previousDone == 0)
					player.getPacketSender().sendString(achievement.interfaceFrame,
							"@yel@" + achievement.achievementName);
							*/
			} else {
				finishAchievement(player, achievement);
			}
		}
	}

	public static void finishAchievement(Player player, AchievementData achievement) {
		if (player.getAchievementAttributes().getCompletion()[achievement.ordinal()])
			return;
		player.getPointsHandler().setAchievementPoints(1, true);
		player.getAchievementAttributes().getCompletion()[achievement.ordinal()] = true;
		player.getPacketSender()//.sendString(achievement.interfaceFrame, ("@gre@") + achievement.achievementName)
				.sendMessage("<icon=3> <col=339900>You have completed the achievement "
						+ Misc.formatText(achievement.getName() + "."))
				.sendString(37005, "Progress: " + player.getPointsHandler().getAchievementPoints()
						+ "/" + AchievementData.values().length);
		player.getPacketSender().sendString(1, "[ACHIEVEMENT]-"+ achievement.achievementName +"-"+achievement.getDifficulty().ordinal());
		player.getAchievementAttributes().updateAchievementsPerCat(achievement);
	}

	public static class AchievementAttributes {

		public AchievementAttributes() {
		}

		/** ACHIEVEMENTS **/
		private int skillingAchievements = 0, combatAchievements = 0, dndAchievements = 0;
		private int skillingCompleted = 0, combatCompleted = 0, dndCompleted = 0;
		private boolean[] completed = new boolean[AchievementData.values().length];
		private int[] progress = new int[55];

		public boolean[] getCompletion() {
			return completed;
		}

		public void setCompletion(int index, boolean value) {
			this.completed[index] = value;
		}

		public void setCompletion(boolean[] completed) {
			this.completed = completed;
		}

		public int[] getProgress() {
			return progress;
		}
		
		public void setProgress(int index, int value) {
			this.progress[index] = value;
		}

		public void setProgress(int[] progress) {
			this.progress = progress;
		}
		
		public void updateAchievementsPerCat(AchievementData achievement) {
			if (achievement.getCategory() == Achievements.Category.COMBAT) {
				combatCompleted += 1;
			} else if (achievement.getCategory() == Achievements.Category.DISTRACTIONS) {
				dndCompleted += 1;
			} else if (achievement.getCategory() == Achievements.Category.SKILLING) {
				skillingCompleted += 1;
			}
		}

		/** MISC **/
		private int coinsGambled;
		private double totalLoyaltyPointsEarned;
		private boolean[] godsKilled = new boolean[5];

		public int getCoinsGambled() {
			return coinsGambled;
		}

		public void setCoinsGambled(int coinsGambled) {
			this.coinsGambled = coinsGambled;
		}

		public double getTotalLoyaltyPointsEarned() {
			return totalLoyaltyPointsEarned;
		}

		public void incrementTotalLoyaltyPointsEarned(double totalLoyaltyPointsEarned) {
			this.totalLoyaltyPointsEarned += totalLoyaltyPointsEarned;
		}

		public boolean[] getGodsKilled() {
			return godsKilled;
		}

		public void setGodKilled(int index, boolean godKilled) {
			this.godsKilled[index] = godKilled;
		}

		public void setGodsKilled(boolean[] b) {
			this.godsKilled = b;
		}
	}
}
