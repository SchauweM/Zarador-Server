package com.zarador.world.content.skill.impl.prayer;

public enum BonesData {
	BONES(526, 5),
	BAT_BONES(530, 5),
	WOLF_BONES(2859, 5),
	BIG_BONES(532, 15),
	BABYDRAGON_BONES(534, 30),
	JOGRE_BONE(3125, 15),
	ZOGRE_BONES(4812, 23),
	LONG_BONES(10976, 25),
	CURVED_BONE(10977, 25),
	SHAIKAHAN_BONES(3123, 25),
	DRAGON_BONES(536, 72),
	FAYRG_BONES(4830, 84),
	RAURG_BONES(4832, 96),
	DAGANNOTH_BONES(6729, 125),
	OURG_BONES(14793, 140),
	WYVERN_BONES(6812, 50),
	FROSTDRAGON_BONES(18830, 180),
	IMPIOUS_ASHES(20264, 4),
	ACCURSED_ASHES(20266, 13),
	INFERNAL_ASHES(20268, 63),
	LAVA_DRAGON_BONES(11943, 150);

	BonesData(int boneId, int buryXP) {
		this.boneId = boneId;
		this.buryXP = buryXP;
	}

	private int boneId;
	private int buryXP;

	public int getBoneID() {
		return this.boneId;
	}

	public int getBuryingXP() {
		return this.buryXP;
	}

	public static BonesData forId(int bone) {
		for (BonesData prayerData : BonesData.values()) {
			if (prayerData.getBoneID() == bone) {
				return prayerData;
			}
		}
		return null;
	}

}
