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
public enum CookingData {

	SHRIMP(317, 315, 7954, 1, 30, 33, false, "shrimp"),
	ANCHOVIES(321, 319, 323, 1, 30, 34, false, "anchovies"),
	SARDINE(327, 325, 324, 1, 40, 34, false, "sardine"),
	HERRING(345, 347, 324, 5, 50, 34, false, "herring"),
	TROUT(335, 333, 343, 15, 70, 50, false, "trout"),
	COD(341, 339, 343, 18, 75, 54,false, "cod"),
	SALMON(331, 329, 343, 25, 90, 58, false, "salmon"),
	TUNA(359, 361, 367, 30, 100, 58, false, "tuna"),
	LOBSTER(377, 379, 381, 40, 120, 74, true, "lobster"),
	BASS(363, 365, 367, 40, 130, 75, false, "bass"),
	SWORDFISH(371, 373, 375, 45, 140, 86, true, "swordfish"),
	MONKFISH(7944, 7946, 7948, 62, 150, 92, true, "monkfish"),
	SHARK(383, 385, 387, 80, 210, 99, true, "shark"),
	SEA_TURTLE(395, 397, 399, 82, 211.3, 105, false, "sea turtle"),
	MANTA_RAY(389, 391, 393, 91, 216.2, 105, false, "manta ray"),
	ROCKTAIL(15270, 15272, 15274, 93, 225, 94, true, "rocktail"),
	CAVEFISH(15264, 15266, 15268, 88, 214, 94, true, "cavefish"),
	KARAMBWAN(3142, 3144, 3148, 30, 190, 90, false, "karambwan"),
	HEIM_CRAB(17797, 18159, 18179, 5, 22, 40, false, "heim crab"),
	RED_EYE(17799, 18161, 18181, 10, 41, 45, false, "red-eye"),
	DUSK_EEL(17801, 18163, 18183, 12, 61, 47, false, "dusk eel"),
	GIANT_FLATFISH(17803, 18165, 18185, 15, 82, 50, false, "giant flatfish"),
	SHORT_FINNED_EEL(17805, 18167, 18187, 18, 103, 54, false, "short-finned eel"),
	WEB_SNIPPER(17807, 18169, 18189, 30, 124, 60, false, "web snipper"),
	BOULDABASS(17809, 18171, 18191, 40, 146, 75, false, "bouldabass"),
	SALVE_EEL(17811, 18173, 18193, 60, 168, 81, false, "salve eel"),
	BLUE_CRAB(17813, 18175, 18195, 75, 191, 92, false, "blue crab");

	int rawItem, cookedItem, burntItem, levelReq, stopBurn;
	double xp;
	boolean withGloves;
	String name;

	CookingData(int rawItem, int cookedItem, int burntItem, int levelReq, double xp, int stopBurn, boolean withGloves, String name) {
		this.rawItem = rawItem;
		this.cookedItem = cookedItem;
		this.burntItem = burntItem;
		this.levelReq = levelReq;
		this.xp = xp;
		this.stopBurn = stopBurn;
		this.withGloves = withGloves;
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

	public double getXp() {
		return xp;
	}

	public int getStopBurn() {
		return stopBurn;
	}

	public boolean getWithGloves() {
		return withGloves;
	}

	public String getName() {
		return name;
	}

	public static CookingData forFish(int fish) {
		for (CookingData data : CookingData.values()) {
			if (data.getRawItem() == fish) {
				return data;
			}
		}
		return null;
	}

	public static final int[] cookingRanges = { 41687, 4172, 2732, 114, 2728, 21302, 41687};

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
		CookingData fish = forFish(id);
		if (fish == null)
			return false;
		if (player.getSkillManager().getMaxLevel(Skill.COOKING) < fish.getLevelReq()) {
			player.getPacketSender()
					.sendMessage("You need a Cooking level of atleast " + fish.getLevelReq() + " to cook this.");
			return false;
		}
		if (!player.getInventory().contains(id)) {
			player.getPacketSender().sendMessage("You have run out of fish.");
			return false;
		}
		return true;
	}

}
