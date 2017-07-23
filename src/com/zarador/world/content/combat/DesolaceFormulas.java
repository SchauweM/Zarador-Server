package com.zarador.world.content.combat;

import com.zarador.model.Graphic;
import com.zarador.model.Skill;
import com.zarador.model.container.impl.Equipment;
import com.zarador.util.Misc;
import com.zarador.world.content.combat.effect.EquipmentBonus;
import com.zarador.world.content.combat.magic.CombatSpell;
import com.zarador.world.content.combat.prayer.CurseHandler;
import com.zarador.world.content.combat.prayer.PrayerHandler;
import com.zarador.world.content.combat.weapon.FightStyle;
import com.zarador.world.content.skill.SkillManager;
import com.zarador.world.entity.impl.Character;
import com.zarador.world.entity.impl.npc.NPC;
import com.zarador.world.entity.impl.player.Player;

public class DesolaceFormulas {

	public static int calculateMaxMeleeHit(Character source, Character victim) {
		double strMult = 1;
		int strLevel = 0;
		double strBonus = 0;
		int styleBonus = -1;
		double maxHit = 0;
		double specialBonus = 1;
		if (source.isNpc()) {
			NPC npc = (NPC) source;
			maxHit = npc.getDefinition().getMaxHit();
			if (npc.getStrengthWeakened()[0]) {
				maxHit -= (int) ((0.10) * (maxHit));
			} else if (npc.getStrengthWeakened()[1]) {
				maxHit -= (int) ((0.20) * (maxHit));
			} else if (npc.getStrengthWeakened()[2]) {
				maxHit -= (int) ((0.30) * (maxHit));
			}

			if (npc.getId() == 2026) { // Dharok the wretched
				maxHit += (int) ((npc.getDefaultConstitution() - npc.getConstitution()) * 0.2);
			}
			return (int) maxHit;
		} else {
			Player player = (Player) source;

			strBonus = player.getBonusManager().getOtherBonus()[0];
			strLevel = player.getSkillManager().getCurrentLevel(Skill.STRENGTH);

			if (player.getFightType().getStyle() == FightStyle.AGGRESSIVE) {
				styleBonus = 3;
			} else if (player.getFightType().getStyle() == FightStyle.CONTROLLED) {
				styleBonus = 1;
			} else if (player.getFightType().getStyle() == FightStyle.DEFENSIVE) {
				styleBonus = 0;
			} else if (player.getFightType().getStyle() == FightStyle.ACCURATE) {
				styleBonus = 0;
			}

	 		strMult = getPrayerStr(player);

		}

		double cumulativeStr = (strLevel + styleBonus * strMult);
		maxHit += ((13 + (cumulativeStr) + (strBonus / 8) + ((cumulativeStr * strBonus) / 64)) * strMult);
		if (source.isPlayer()) {
			Player player = (Player) source;
			if (player.getEquipment().getItems()[3].getId() == 4718 && player.getEquipment().getItems()[0].getId() == 4716 && player.getEquipment().getItems()[4].getId() == 4720 && player.getEquipment().getItems()[7].getId() == 4722) {
				double healthBonus = player.getSkillManager().getMaxLevel(Skill.CONSTITUTION) - player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION);
				healthBonus = healthBonus / 1000;
				specialBonus += healthBonus;
			} else if (hasObsidianEffect(player) || EquipmentBonus.wearingVoid(player, CombatType.MELEE) || EquipmentBonus.wearingEliteVoid(player, CombatType.MELEE)) {
				if(EquipmentBonus.wearingVoid(player, CombatType.MELEE)) {
					specialBonus += .1;
				} else {
					specialBonus += .2;
				}
			}

			maxHit *= specialBonus;

			if(player.isSpecialActivated()) {
				maxHit *= player.getCombatSpecial().getStrengthBonus();
			}
		}

		if (victim.isPlayer()) {
			Player p = (Player) victim;
			if (p.hasStaffOfLightEffect()) {
				maxHit = maxHit / 2;
				p.performGraphic(new Graphic(2319));
			}
		}

		if (maxHit < 1) {
			maxHit = 1;
		} else {
			maxHit = Math.floor(maxHit);
		}
		return (int) maxHit;
	}

	/**
	 * Calculates a player's Melee attack level (how likely that they're going
	 * to hit through defence)
	 *
	 * @param plr
	 *            The player's Meelee attack level
	 * @return The player's Melee attack level
	 */
	@SuppressWarnings("incomplete-switch")
	public static int getMeleeAttack(Player plr) {
		int attackLevel = plr.getSkillManager().getCurrentLevel(Skill.ATTACK);
		switch (plr.getFightType().getStyle()) {
		case AGGRESSIVE:
			attackLevel += 3;
			break;
		case CONTROLLED:
			attackLevel += 1;
			break;
		}

		if (PrayerHandler.isActivated(plr, PrayerHandler.CLARITY_OF_THOUGHT)) {
			attackLevel += plr.getSkillManager().getMaxLevel(Skill.ATTACK) * 0.05;
		} else if (PrayerHandler.isActivated(plr, PrayerHandler.IMPROVED_REFLEXES)) {
			attackLevel += plr.getSkillManager().getMaxLevel(Skill.ATTACK) * 0.1;
		} else if (PrayerHandler.isActivated(plr, PrayerHandler.INCREDIBLE_REFLEXES)) {
			attackLevel += plr.getSkillManager().getMaxLevel(Skill.ATTACK) * 0.15;
		} else if (PrayerHandler.isActivated(plr, PrayerHandler.CHIVALRY)) {
			attackLevel += plr.getSkillManager().getMaxLevel(Skill.ATTACK) * 0.15;
		} else if (PrayerHandler.isActivated(plr, PrayerHandler.PIETY)) {
			attackLevel += plr.getSkillManager().getMaxLevel(Skill.ATTACK) * 0.2;
		} else if (CurseHandler.isActivated(plr, CurseHandler.LEECH_ATTACK)) {
			attackLevel += plr.getSkillManager().getMaxLevel(Skill.ATTACK) * 0.05 + plr.getLeechedBonuses()[2];
		} else if (CurseHandler.isActivated(plr, CurseHandler.TURMOIL)) {
			attackLevel += plr.getSkillManager().getMaxLevel(Skill.ATTACK) * 0.3 + plr.getLeechedBonuses()[2];
		}
		attackLevel *= plr.isSpecialActivated() ? plr.getCombatSpecial().getAccuracyBonus() + plr.getDonatorRights().getSpecialAccuracyBoost(plr) : 1;
		int i = (int) plr.getBonusManager().getAttackBonus()[bestMeleeAtk(plr)];

		if (hasObsidianEffect(plr)) {
			i *= 1.20;
		} else if(EquipmentBonus.getVanguardBonus(plr, CombatType.MELEE) != 0) {
			i *= EquipmentBonus.getVanguardBonus(plr, CombatType.MELEE);
		} else if(EquipmentBonus.wearingVoid(plr, CombatType.MELEE)) {
			i *= 1.1;
		} else if(EquipmentBonus.wearingEliteVoid(plr, CombatType.MELEE)) {
			i *= 1.2;
		}

		return (int) (attackLevel + (attackLevel * 0.15) + (i + i * 0.04));
	}

	/**
	 * Calculates a player's Melee Defence level
	 *
	 * @param plr
	 *            The player to calculate Melee defence for
	 * @return The player's Melee defence level
	 */
	public static int getMeleeDefence(Player plr) {
		int defenceLevel = plr.getSkillManager().getCurrentLevel(Skill.DEFENCE);
		int i = (int) plr.getBonusManager().getDefenceBonus()[bestMeleeDef(plr)];
		if (plr.getPrayerActive()[PrayerHandler.THICK_SKIN]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.05;
		} else if (plr.getPrayerActive()[PrayerHandler.ROCK_SKIN]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.1;
		} else if (plr.getPrayerActive()[PrayerHandler.STEEL_SKIN]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.15;
		} else if (plr.getPrayerActive()[PrayerHandler.CHIVALRY]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.2;
		} else if (plr.getPrayerActive()[PrayerHandler.PIETY]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
		} else if (plr.getPrayerActive()[PrayerHandler.RIGOUR]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
		} else if (plr.getPrayerActive()[PrayerHandler.AUGURY]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
		} else if (plr.getCurseActive()[CurseHandler.TURMOIL]) { // turmoil
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.15;
		}
		return (int) (defenceLevel + (defenceLevel * 0.15) + (i + i * 0.05));
	}

	public static int bestMeleeDef(Player p) {
		if (p.getBonusManager().getDefenceBonus()[0] > p.getBonusManager().getDefenceBonus()[1]
				&& p.getBonusManager().getDefenceBonus()[0] > p.getBonusManager().getDefenceBonus()[2]) {
			return 0;
		}
		if (p.getBonusManager().getDefenceBonus()[1] > p.getBonusManager().getDefenceBonus()[0]
				&& p.getBonusManager().getDefenceBonus()[1] > p.getBonusManager().getDefenceBonus()[2]) {
			return 1;
		}
		return p.getBonusManager().getDefenceBonus()[2] <= p.getBonusManager().getDefenceBonus()[0]
				|| p.getBonusManager().getDefenceBonus()[2] <= p.getBonusManager().getDefenceBonus()[1] ? 0 : 2;
	}

	public static int bestMeleeAtk(Player p) {
		if (p.getBonusManager().getAttackBonus()[0] > p.getBonusManager().getAttackBonus()[1]
				&& p.getBonusManager().getAttackBonus()[0] > p.getBonusManager().getAttackBonus()[2]) {
			return 0;
		}
		if (p.getBonusManager().getAttackBonus()[1] > p.getBonusManager().getAttackBonus()[0]
				&& p.getBonusManager().getAttackBonus()[1] > p.getBonusManager().getAttackBonus()[2]) {
			return 1;
		}
		return p.getBonusManager().getAttackBonus()[2] <= p.getBonusManager().getAttackBonus()[1]
				|| p.getBonusManager().getAttackBonus()[2] <= p.getBonusManager().getAttackBonus()[0] ? 0 : 2;
	}

	/**
	 * Obsidian items
	 */

	public static final int[] obsidianWeapons = { 746, 747, 6523, 6525, 6526, 6527, 6528 };

	public static boolean hasObsidianEffect(Player plr) {
		if (plr.getEquipment().getItems()[2].getId() != 11128)
			return false;

		for (int weapon : obsidianWeapons) {
			if (plr.getEquipment().getItems()[3].getId() == weapon)
				return true;
		}
		return false;
	}

	@SuppressWarnings("incomplete-switch")
	public static int getStyleBonus(Player plr) {
		switch (plr.getFightType().getStyle()) {
		case AGGRESSIVE:
		case ACCURATE:
			return 3;
		case CONTROLLED:
			return 1;
		}
		return 0;
	}

	public static double getEffectiveStr(Player plr) {
		return ((plr.getSkillManager().getCurrentLevel(Skill.STRENGTH)) * getPrayerStr(plr)) + getStyleBonus(plr);
	}

	public static double getPrayerStr(Player plr) {
		if (plr.getPrayerActive()[1] || plr.getCurseActive()[CurseHandler.LEECH_STRENGTH])
			return 1.05;
		else if (plr.getPrayerActive()[6])
			return 1.1;
		else if (plr.getPrayerActive()[14])
			return 1.15;
		else if (plr.getPrayerActive()[24])
			return 1.18;
		else if (plr.getPrayerActive()[25])
			return 1.23;
		else if (plr.getCurseActive()[CurseHandler.TURMOIL])
			return 1.24;
		return 1;
	}

	/**
	 * Calculates a player's Ranged attack (level). Credits: Dexter Morgan
	 * 
	 * @param plr
	 *            The player to calculate Ranged attack level for
	 * @return The player's Ranged attack level
	 */
	public static int getRangedAttack(Player plr) {
		int rangeLevel = plr.getSkillManager().getCurrentLevel(Skill.RANGED);
		boolean hasVoid = EquipmentBonus.wearingVoid(plr, CombatType.RANGED);
		double accuracy = plr.isSpecialActivated() ? plr.getCombatSpecial().getAccuracyBonus() + plr.getDonatorRights().getSpecialAccuracyBoost(plr) : 1;
		rangeLevel *= accuracy;
		if (hasVoid) {
			rangeLevel += SkillManager.getLevelForExperience(Skill.RANGED, plr.getSkillManager().getExperience(Skill.RANGED)) * 0.15;
		}
		if (plr.getCurseActive()[PrayerHandler.SHARP_EYE] || plr.getCurseActive()[CurseHandler.SAP_RANGER]) {
			rangeLevel *= 1.05;
		} else if (plr.getPrayerActive()[PrayerHandler.HAWK_EYE]) {
			rangeLevel *= 1.10;
		} else if (plr.getPrayerActive()[PrayerHandler.EAGLE_EYE]) {
			rangeLevel *= 1.15;
		} else if (plr.getPrayerActive()[PrayerHandler.RIGOUR]) {
			rangeLevel *= 1.22;
		} else if (plr.getCurseActive()[CurseHandler.LEECH_RANGED]) {
			rangeLevel *= 1.10;
		}
		if (hasVoid) {
			rangeLevel *= 1.10;
		} else if(EquipmentBonus.wearingEliteVoid(plr, CombatType.RANGED)) {
			rangeLevel *= 1.20;
		}
		/*
		 * Slay helm if(plr.getAdvancedSkills().getSlayer().getSlayerTask() !=
		 * null && plr.getEquipment().getRareDrops()[Equipment.HEAD_SLOT].getId() ==
		 * 15492) { if(plr.getCombatAttributes().getCurrentEnemy() != null &&
		 * plr.getCombatAttributes().getCurrentEnemy().isNpc()) { NPC n =
		 * (NPC)plr.getCombatAttributes().getCurrentEnemy(); if(n != null &&
		 * n.getId() ==
		 * plr.getAdvancedSkills().getSlayer().getSlayerTask().getNpcId())
		 * rangeLevel *= 1.12; } }
		 */
		return (int) (rangeLevel + (plr.getBonusManager().getAttackBonus()[4] * 2));
	}

	/**
	 * Calculates a player's Ranged defence level.
	 * 
	 * @param plr
	 *            The player to calculate the Ranged defence level for
	 * @return The player's Ranged defence level
	 */
	public static int getRangedDefence(Player plr) {
		int defenceLevel = plr.getSkillManager().getCurrentLevel(Skill.DEFENCE);
		if (plr.getPrayerActive()[PrayerHandler.THICK_SKIN]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.05;
		} else if (plr.getPrayerActive()[PrayerHandler.ROCK_SKIN]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.1;
		} else if (plr.getPrayerActive()[PrayerHandler.STEEL_SKIN]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.15;
		} else if (plr.getPrayerActive()[PrayerHandler.CHIVALRY]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.2;
		} else if (plr.getPrayerActive()[PrayerHandler.PIETY]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
		} else if (plr.getPrayerActive()[PrayerHandler.RIGOUR]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
		} else if (plr.getPrayerActive()[PrayerHandler.AUGURY]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
		} else if (plr.getCurseActive()[CurseHandler.TURMOIL]) { // turmoil
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.20 + plr.getLeechedBonuses()[0];
		}
		return (int) (defenceLevel + plr.getBonusManager().getDefenceBonus()[4]
				+ (plr.getBonusManager().getDefenceBonus()[4] / 2));
	}

	public static int getMagicAttack(Player plr) {
		boolean voidEquipment = EquipmentBonus.wearingVoid(plr, CombatType.MAGIC);
		boolean eliteVoidEquipment = EquipmentBonus.wearingEliteVoid(plr, CombatType.MAGIC);
		int attackLevel = plr.getSkillManager().getCurrentLevel(Skill.MAGIC);
		if (voidEquipment) {
			attackLevel += plr.getSkillManager().getCurrentLevel(Skill.MAGIC) * 0.2;
		} else if(eliteVoidEquipment) {
			attackLevel += plr.getSkillManager().getCurrentLevel(Skill.MAGIC) * 0.3;
		}
		if (plr.getPrayerActive()[PrayerHandler.MYSTIC_WILL] || plr.getCurseActive()[CurseHandler.SAP_MAGE]) {
			attackLevel *= 1.05;
		} else if (plr.getPrayerActive()[PrayerHandler.MYSTIC_LORE]) {
			attackLevel *= 1.10;
		} else if (plr.getPrayerActive()[PrayerHandler.MYSTIC_MIGHT]) {
			attackLevel *= 1.15;
		} else if (plr.getPrayerActive()[PrayerHandler.AUGURY]) {
			attackLevel *= 1.22;
		} else if (plr.getCurseActive()[CurseHandler.LEECH_MAGIC]) {
			attackLevel *= 1.18;
		}
		attackLevel *= plr.isSpecialActivated() ? plr.getCombatSpecial().getAccuracyBonus() + plr.getDonatorRights().getSpecialAccuracyBoost(plr) : 1;

		return (int) (attackLevel + (plr.getBonusManager().getAttackBonus()[3] * 2));
	}

	/**
	 * Calculates a player's magic defence level
	 * 
	 * @return The player's magic defence level
	 */
	public static int getMagicDefence(Player plr) {

		int defenceLevel = plr.getSkillManager().getCurrentLevel(Skill.DEFENCE) / 2
				+ plr.getSkillManager().getCurrentLevel(Skill.MAGIC) / 2;

		if (plr.getPrayerActive()[PrayerHandler.THICK_SKIN]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.05;
		} else if (plr.getPrayerActive()[PrayerHandler.ROCK_SKIN]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.1;
		} else if (plr.getPrayerActive()[PrayerHandler.STEEL_SKIN]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.15;
		} else if (plr.getPrayerActive()[PrayerHandler.CHIVALRY]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.2;
		} else if (plr.getPrayerActive()[PrayerHandler.PIETY]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
		} else if (plr.getPrayerActive()[PrayerHandler.RIGOUR]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
		} else if (plr.getPrayerActive()[PrayerHandler.AUGURY]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
		} else if (plr.getCurseActive()[CurseHandler.TURMOIL]) { // turmoil
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.20 + plr.getLeechedBonuses()[0];
		}

		return (int) (defenceLevel + plr.getBonusManager().getDefenceBonus()[3]
				+ (plr.getBonusManager().getDefenceBonus()[3] / 3));
	}

	/**
	 * Calculates a player's magic max hit
	 * 
	 * @return The player's magic max hit damage
	 */
	public static int getMagicMaxhit(Character c) {
		int damage = 0;
		CombatSpell spell = c.getCurrentlyCasting();
		if (spell != null) {
			if (spell.maximumHit() > 0)
				damage += spell.maximumHit();
			else {
				if (c.isNpc()) {
					damage = ((NPC) c).getDefinition().getMaxHit();
				} else {
					damage = 1;
				}
			}
		}

		if (c.isNpc()) {
			if (spell == null) {
				damage = Misc.getRandom(((NPC) c).getDefinition().getMaxHit());
			}
			return damage;
		}

		Player p = (Player) c;
		double damageMultiplier = 1;

		switch (p.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId()) {
		case 4675:
		case 6914:
		case 15246:
			damageMultiplier += .10;
			break;
		case 18355:
			damageMultiplier += .20;
			break;
		}

		boolean specialAttack = p.isSpecialActivated();

		int maxHit = -1;

		if (specialAttack) {
			switch (p.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId()) {

			case 19780:
				if (p.getCurrentlyCasting() != null) {
					damage = maxHit = spell.maximumHit();
				} else {
					damage = maxHit = 750;
				}
				break;
			case 11730:
				if (p.getCurrentlyCasting() != null) {
					damage = maxHit = spell.maximumHit();
				} else {
					damage = maxHit = 310;
				}
				break;
			case 19674:
				damageMultiplier += 0.25;
				break;
			case 11235:
			case 21016:
			case 21017:
			case 21018:
			case 21019:
			case 21020:
			case 21021:
			case 21022:
			case 21023:
				damage = maxHit = 420;
				break;
			case 13905:
			case 13907:
				if (p.getCurrentlyCasting() != null) {
					damage = maxHit = spell.maximumHit();
				} else {
					damage = maxHit = 485;
				}
				break;
			}
		} else {
			damageMultiplier += 0.25;
		}

		if (p.getEquipment().getItems()[Equipment.AMULET_SLOT].getId() == 18335) {
			damageMultiplier += .10;
		}

		damage *= damageMultiplier;

		if (maxHit > 0) {
			if (damage > maxHit) {
				damage = maxHit;
			}
		}

		return damage;
	}
}
