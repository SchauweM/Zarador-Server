package com.zarador.world.content.skill.impl.firemaking;

import com.zarador.world.entity.impl.player.Player;

public class Logdata {

	public static enum logData {

		LOG(1511, 1, 40, 60), ACHEY(2862, 1, 40, 60), OAK(1521, 15, 60, 80), WILLOW(1519, 30, 90, 90), TEAK(
				6333, 35, 105, 90), ARCTIC_PINE(10810, 42, 125, 90), MAPLE(1517, 45, 135, 100), MAHOGANY(6332, 50,
						157.5,
				100), EUCALYPTUS(12581, 58, 194, 100), YEW(1515, 60, 202.5, 125), MAGIC(1513, 75, 303.8, 150);

		private int logId, level, burnTime;
		private double xp;

		private logData(int logId, int level, double xp, int burnTime) {
			this.logId = logId;
			this.level = level;
			this.xp = xp;
			this.burnTime = burnTime;
		}

		public int getLogId() {
			return logId;
		}

		public int getLevel() {
			return level;
		}

		public double getXp() {
			return xp;
		}

		public int getBurnTime() {
			return this.burnTime;
		}
	}

	public static logData getLogData(Player p, int log) {
		for (final Logdata.logData l : Logdata.logData.values()) {
			if (log == l.getLogId() || log == -1 && p.getInventory().contains(l.getLogId())) {
				return l;
			}
		}
		return null;
	}

}
