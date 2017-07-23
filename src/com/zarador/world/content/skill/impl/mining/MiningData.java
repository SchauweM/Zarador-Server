package com.zarador.world.content.skill.impl.mining;

import com.zarador.model.Skill;
import com.zarador.model.container.impl.Equipment;
import com.zarador.world.entity.impl.player.Player;

public class MiningData {

	public static final int[] RANDOM_GEMS = { 1623, 1621, 1619, 1617, 1631 };

	public static enum Pickaxe {

		INFERNAL_PICKAXE(12704, 90, 10228, 1.60),
		ADZE(13661, 80, 10226, 1.60),
		DRAGON_GILDED(20786, 61, 12190, 1.65),
		DRAGON(15259, 61, 12188, 1.50),
		RUNE(1275, 41, 6752, 1.3),
		ADAMANT(1271, 31, 6756, 1.25),
		MITHRIL(1273, 21, 6757, 1.2),
		STEEL(1269, 6, 6755, 1.1),
		IRON(1267, 1, 6754, 1.05),
		BRONZE(1265, 1, 6753, 1.0);

		private int id, req, anim;
		private double speed;

		private Pickaxe(int id, int req, int anim, double speed) {
			this.id = id;
			this.req = req;
			this.anim = anim;
			this.speed = speed;
		}

		public int getId() {
			return id;
		}

		public int getReq() {
			return req;
		}

		public int getAnim() {
			return anim;
		}

		public double getSpeed() {
			return this.speed;
		}
	}

	public static Pickaxe forPick(int id) {
		for (Pickaxe p : Pickaxe.values()) {
			if (p.getId() == id) {
				return p;
			}
		}
		return null;
	}

	public static enum Ores {
		RUNE_ESSENCE(new int[] { 24444 }, 1, 5, 1436, 2, -1),
		PURE_ESSENCE(new int[] { 24445 }, 30, 5, 7936, 3, -1),
		CLAY(new int[] { 9711, 9712, 9713, 15503, 15504, 15505 }, 1, 5, 434, 3, 2),
		COPPER(new int[] { 9708, 9709, 9710, 11936, 11960, 11961, 2091, 11962, 11189, 11190, 11191, 29231, 29230, 2090 }, 1, 17.5, 436, 4, 4),
		TIN(new int[] { 9714, 9715, 9716, 11933, 11957, 11958, 11959, 2095, 11186, 11187, 11188, 2094, 29227, 29229 }, 1, 17.5, 438, 4, 4),
		IRON(new int[] { 14856, 9717, 9718, 9719, 2093, 2092, 11954, 11955, 11956, 29221, 29222, 29223 }, 15, 35, 440, 5, 5),
		SILVER(new int[] { 2100, 2101, 29226, 29225, 11948, 11949 }, 20, 40, 442, 5, 7),
		COAL(new int[] { 2111, 14851, 14852, 14850, 5770, 29216, 29215, 29217, 11965, 11964, 11963, 11930, 11931, 11932, 2097 }, 30, 50, 453, 5, 7),
		GOLD(new int[] { 2098, 9720, 9721, 9722, 11951, 11183, 11184, 11185, 2099 }, 40, 60, 444, 5, 10),
		MITHRIL(new int[] { 2102, 14855, 14853, 14854, 25370, 25368, 5786, 5784, 11942, 11943, 11944, 11945, 11946, 29236, 11947, 11942, 11943 }, 55, 80, 447, 6, 11),
		ADAMANTITE(new int[] {2104,  14862, 11941, 11939, 29233, 29235 }, 70, 95, 449, 7, 14),
		RUNITE(new int[] { 14859, 14860, 4860, 2106, 2107 }, 85, 125, 451, 7, 45),
		RESOURCE_IRON(new int[] { 7455 }, 15, 65, 441, 5, 5),
		RESOURCE_COAL(new int[] { 7489, 4389 }, 30, 70, 454, 5, 7),
		RESOURCE_GOLD(new int[] { 7491 }, 40, 77, 445, 5, 10),
		RESOURCE_MITHRIL(new int[] { 7459 }, 55, 95, 448, 6, 16),
		RESOURCE_ADAMANTITE(new int[] { 7493 }, 70, 125, 450, 7, 18),
		LIVING_ROCK_RESOURCE(new int[] { 30000 }, 80, 70, 444, 5, -1);

		private int objid[];
		private int itemid, req, ticks, respawnTimer;
		double xp;

		private Ores(int[] objid, int req, double xp, int itemid, int ticks, int respawnTimer) {
			this.objid = objid;
			this.req = req;
			this.xp = xp;
			this.itemid = itemid;
			this.ticks = ticks;
			this.respawnTimer = respawnTimer;
		}

		public int getRespawn() {
			return respawnTimer;
		}

		public int getLevelReq() {
			return req;
		}

		public double getXpAmount() {
			return xp;
		}

		public int getItemId() {
			return itemid;
		}

		public int getTicks() {
			return ticks;
		}
	}

	public static Ores forRock(int id) {
		for (Ores ore : Ores.values()) {
			for (int obj : ore.objid) {
				if (obj == id) {
					return ore;
				}
			}
		}
		return null;
	}

	public static int getPickaxe(final Player plr) {
		for (Pickaxe p : Pickaxe.values()) {
			if (plr.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == p.getId())
				return p.getId();
			else if (plr.getInventory().contains(p.getId())) {
				return p.getId();
			}
		}
		return -1;
	}

	public static int getReducedTimer(final Player plr, Pickaxe pickaxe) {
		int skillReducement = (int) (plr.getSkillManager().getMaxLevel(Skill.MINING) * 0.03);
		int pickaxeReducement = (int) pickaxe.getSpeed();
		return skillReducement + pickaxeReducement;
	}
}
