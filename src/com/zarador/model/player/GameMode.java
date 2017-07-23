package com.zarador.model.player;

import com.zarador.util.Misc;

public enum GameMode {

	KNIGHT(500, 0, 1, 20, 0),
	REALISM(10, .35, .50, 10, 0),
	IRONMAN(50, .50, .75, 15, 13);

	GameMode(int modeExpRate, double monsterDropRate, double prayerDrainRate, int specialRecoveryRate, int crown) {
		this.modeExpRate = modeExpRate;
		this.monsterDropRate = monsterDropRate;
		this.prayerDrainRate = prayerDrainRate;
		this.specialRecoveryRate = specialRecoveryRate;
		this.crown = crown;
	}

	private int modeExpRate;
	private double monsterDropRate;
	private double prayerDrainRate;
	private int specialRecoveryRate;
	private int crown;

	/**
	 * Gets the xp rate for the mode.
	 * @return
	 */
	public int getModeExpRate() {
		return modeExpRate;
	}

	/**
	 * Gets the drop rate for the mode.
	 * @return
	 */
	public double getMonsterDropRate() {
		return monsterDropRate;
	}

	/**
	 * Gets how faster the players prayer will drain.
	 * @return
	 */
	public double getPrayerDrainRate() {
		return prayerDrainRate;
	}

	/**
	 * Gets how fast the player will recover their special attack.
	 * @return
	 */
	public int getSpecialRecoveryRate() {
		return specialRecoveryRate;
	}

	/**
	 * Returns the formatted name of the gamemode.
	 * @return
	 */
	public String getModeName() {
		return Misc.formatText(this.toString().toLowerCase().replace("_", ""));
	}

	/**
	 * Gets the crown for a certain mode
	 *
	 * @return
	 */
	public int getCrown() {
		return this.crown;
	}

}
