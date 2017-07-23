package com.zarador.world.content.skill.impl.fletching;

public enum ArrowData {

	HEADLESS(52, 314, 53, 1, 1),
	BRONZE(53, 39, 882, 1.3, 1),
	IRON(53, 40, 884, 2.5, 15),
	STEEL(53, 41, 886, 5, 30),
	MITHRIL(53, 42, 888, 7.5, 45),
	ADAMANT(53, 43, 890, 10, 60),
	RUNE(53, 44, 892, 12.5, 75),
	DRAGON(53, 11237, 11212, 15, 90);

	public int item1, item2, outcome, levelReq;
	public double xp;

	ArrowData(int item1, int item2, int outcome, double xp, int levelReq) {
		this.item1 = item1;
		this.item2 = item2;
		this.outcome = outcome;
		this.xp = xp;
		this.levelReq = levelReq;
	}

	public int getItem1() {
		return item1;
	}

	public int getItem2() {
		return item2;
	}

	public int getOutcome() {
		return outcome;
	}

	public double getXp() {
		return xp;
	}

	public int getLevelReq() {
		return levelReq;
	}

	public static ArrowData forArrow(int id) {
		for (ArrowData ar : ArrowData.values()) {
			if (ar.getItem2() == id) {
				return ar;
			}
		}
		return null;
	}
}
