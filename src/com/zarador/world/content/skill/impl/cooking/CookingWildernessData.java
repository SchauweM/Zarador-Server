package com.zarador.world.content.skill.impl.cooking;

import java.security.SecureRandom;

import com.zarador.model.Skill;
import com.zarador.world.content.Emotes.Skillcape_Data;
import com.zarador.world.entity.impl.player.Player;

/**
 * Data for the cooking skill.
 * 
 * @author Admin Gabriel
 */
public enum CookingWildernessData {

	SHRIMP(317, 315, 7954, 1, 30, 33, "shrimp"), ANCHOVIES(321, 319, 323, 1, 30, 34, "anchovies"), TROUT(335, 333, 343, 15, 70, 50, "trout"),
	COD(341, 339, 343, 18, 75, 54, "cod"), SALMON(331, 329, 343, 25, 90, 58, "salmon"), TUNA(359, 361, 367, 30, 100, 58, "tuna"),
	LOBSTER(377, 379, 381, 40, 120, 74, "lobster"), BASS(363, 365, 367, 40, 8000, 75, "bass"), SWORDFISH(371, 373, 375, 45, 140, 86, "swordfish"),
	MONKFISH(7944, 7946, 7948, 62, 150, 92, "monkfish"), SHARK(383, 385, 387, 80, 210, 99, "shark"),
	SEA_TURTLE(395, 397, 399, 82, 212, 105, "sea turtle"), MANTA_RAY(389, 391, 393, 91, 216, 105, "manta ray"),
	ROCKTAIL(15270, 15272, 15274, 92, 250, 106, "rocktail"), CAVEFISH(15264, 15266, 15268, 88, 214, 101, "cavefish"),
	KARAMBWAN(3142, 3144, 3148, 30, 190, 90, "karambwan");

	int rawItem, cookedItem, burntItem, levelReq, xp, stopBurn;
	String name;

	CookingWildernessData(int rawItem, int cookedItem, int burntItem, int levelReq, int xp, int stopBurn, String name) {
		this.rawItem = rawItem;
		this.cookedItem = cookedItem;
		this.burntItem = burntItem;
		this.levelReq = levelReq;
		this.xp = xp;
		this.stopBurn = stopBurn;
		this.name = name;
	}

	public int getRawItem() {
		return rawItem;
	}

	public int getCookedItem() {
		return cookedItem;
	}

	public int getBurntItem() {
		return burntItem;
	}

	public int getLevelReq() {
		return levelReq;
	}

	public int getXp() {
		return xp;
	}

	public int getStopBurn() {
		return stopBurn;
	}

	public String getName() {
		return name;
	}

	public static CookingWildernessData forFish(int fish) {
		for (CookingWildernessData data : CookingWildernessData.values()) {
			if (data.getRawItem() == fish) {
				return data;
			}
		}
		return null;
	}

	public static final int[] cookingRanges = { 2732, 114, 2728, 9682, 41687 };

	public static boolean isRange(int object) {
		for (int i : cookingRanges)
			if (object == i)
				return true;
		return false;
	}

	/**
	 * Get's the rate for burning or successfully cooking food.
	 * 
	 * @param player
	 *            Player cooking.
	 * @return Successfully cook food.
	 */
	public static boolean success(Player player, int burnBonus, int levelReq, int stopBurn) {
		if (player.getSkillManager().getCurrentLevel(Skill.COOKING) >= stopBurn
				|| Skillcape_Data.MASTER_COOKING.isWearingCape(player)
				|| Skillcape_Data.COOKING.isWearingCape(player)) {
			return true;
		}
		double burn_chance = (45.0 - burnBonus);
		double cook_level = player.getSkillManager().getCurrentLevel(Skill.COOKING);
		double lev_needed = levelReq;
		double burn_stop = stopBurn;
		double multi_a = (burn_stop - lev_needed);
		double burn_dec = (burn_chance / multi_a);
		double multi_b = (cook_level - lev_needed);
		burn_chance -= (multi_b * burn_dec);
		double randNum = cookingRandom.nextDouble() * 100.0;
		return burn_chance <= randNum;
	}

	private static SecureRandom cookingRandom = new SecureRandom(); // The
																	// random
																	// factor

	public static boolean canCook(Player player, int id) {
		CookingWildernessData fish = forFish(id);
		if (fish == null)
			return false;
		if (player.getSkillManager().getMaxLevel(Skill.COOKING) < fish.getLevelReq() - 2) {
			player.getPacketSender()
					.sendMessage("You need a Cooking level of atleast " + (fish.getLevelReq() - 2) + " to cook this.");
			return false;
		}
		if (!player.getInventory().contains(id + 1)) {
			player.getPacketSender().sendMessage("You have run out of fish.");
			return false;
		}
		return true;
	}

}
