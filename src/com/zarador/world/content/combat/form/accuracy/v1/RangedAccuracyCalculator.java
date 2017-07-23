package com.zarador.world.content.combat.form.accuracy.v1;

import java.util.concurrent.ThreadLocalRandom;

import com.zarador.model.Item;
import com.zarador.model.Prayerbook;
import com.zarador.model.Skill;
import com.zarador.model.container.impl.Equipment;
import com.zarador.world.content.BonusManager;
import com.zarador.world.content.combat.CombatType;
import com.zarador.world.content.combat.effect.EquipmentBonus;
import com.zarador.world.content.combat.form.accuracy.AccuracyCalculator;
import com.zarador.world.content.combat.prayer.CurseHandler;
import com.zarador.world.content.combat.prayer.PrayerHandler;
import com.zarador.world.content.combat.weapon.CombatSpecial;
import com.zarador.world.content.combat.weapon.FightStyle;
import com.zarador.world.entity.impl.Character;
import com.zarador.world.entity.impl.npc.NPC;
import com.zarador.world.entity.impl.player.Player;

/**
 * An implementation of {@link AccuracyCalculator} used to calculate
 * whether a {@link com.zarador.world.entity.impl.Character} will land their next hit
 * in combat while using {@link com.zarador.world.content.combat.CombatType#RANGED}.
 *
 * @author Relex
 */
public final class RangedAccuracyCalculator implements AccuracyCalculator {

	@Override
	public double getAccuracy(Character source, Character victim) {
		
		/*
		 * Defining some constants
		 */
		Prayerbook victimPrayerBook = Prayerbook.NORMAL;
		if(victim.isPlayer()) {
			victimPrayerBook = ((Player)victim).getPrayerbook();
		}

		boolean[] victimPrayer = victim.getPrayerActive();

		/*
		 * Prayer protection
		 */
		double prayerProtection = 1.0;
		
		if (source.isNpc()) {
			//if {@param victim} has range protection prayer and our {@param source} is a
			//mob, block the hit completely
			if (victimPrayerBook == Prayerbook.NORMAL && victimPrayer[PrayerHandler.PROTECT_FROM_MISSILES]) {
				return 0;
			} else if (victimPrayerBook == Prayerbook.CURSES && victimPrayer[CurseHandler.CurseData.DEFLECT_MISSILES.ordinal()]) {
				return 0;
			}
		} else if (source.isPlayer()) {
			//if {@param victim} has range protection prayer and our {@param source}
			//is a {@link org.niobe.world.Player}, set our {@value prayerProtection} to 0.6
			//to reduce 40% of the damage
			if (victimPrayerBook == Prayerbook.NORMAL && victimPrayer[PrayerHandler.PROTECT_FROM_MISSILES]) {
				prayerProtection = 0.6;
			} else if (victimPrayerBook == Prayerbook.CURSES && victimPrayer[CurseHandler.CurseData.DEFLECT_MISSILES.ordinal()]) {
				prayerProtection = 0.6;
			}
		}
		
		/*
		 * Now let's get our rolls for both parties and determine
		 * whether the {@param source} will land the hit.
		 */
		int attackerRoll = getAttackerRoll(source, victim);
		int defenderRoll = getDefenderRoll(source, victim);
		
		ThreadLocalRandom random = ThreadLocalRandom.current();
		
		//source.sendMessage("attackerRoll=" + attackerRoll + "; defendeRoll=" + defenderRoll);
				
		if (attackerRoll >= defenderRoll) {
			//TODO: take into account the rolls and make a chance
			//to hit 0 based on them
			if (random.nextInt(15) == 0) {
				return 0;
			} else {
				return 1 * prayerProtection;
			}
		} else {
			int chanceToHit = (defenderRoll / 32) - (attackerRoll / 24);
			if (chanceToHit <= 0) {
				chanceToHit = 1;
			}
			if (chanceToHit > 0 && random.nextInt(chanceToHit) == 0) {
				return random.nextDouble(0.5, 1.0) * prayerProtection;
			} else {
				return 0;
			}
		}
	}
	
	private int getAttackerRoll(Character attacker, Character victim) {
		
		/*
		 * Defining some constants
		 */
		Item[] equipment = attacker.isPlayer() ? (((Player)attacker).getEquipment()).getItems() : null;
		
		Prayerbook prayerBook = Prayerbook.NORMAL;
		if(attacker.isPlayer()) {
			prayerBook = ((Player)attacker).getPrayerbook();
		}

		boolean[] activePrayer = attacker.getPrayerActive();
		boolean[] activeCurses = attacker.getPrayerActive();

		boolean usingSpecial = false;
		if(attacker.isPlayer()) {
			usingSpecial = ((Player)attacker).isSpecialActivated();
		}
		
		Item weapon = null;
		if(attacker.isPlayer()) {
			weapon = ((Player)attacker).getEquipment().get(Equipment.WEAPON_SLOT);
		}

		FightStyle attackStyle = FightStyle.CONTROLLED;
		if(weapon != null) {
			if(attacker.isPlayer()) {
				attackStyle = ((Player)attacker).getFightType().getStyle();
			}
		}
		
		/*
		 * Effective level
		 */
		int effectiveLevel = 0;
		if(attacker.isPlayer()) {
			effectiveLevel = ((Player)attacker).getSkillManager().getCurrentLevel(Skill.RANGED);
		} else {
			effectiveLevel = ((NPC)attacker).getDefinition().getAttackBonus();
		}
		
		if (prayerBook == Prayerbook.CURSES
				&& activeCurses[CurseHandler.CurseData.LEECH_RANGED.ordinal()]) {
			//currently for every 25 seconds of our leech curse being active, it will add +1 to our effectiveLevel
//			int timerBoost = (int) ((System.currentTimeMillis() - attacker.getFields().getPrayerActiveTime()[Curse.LEECH_RANGED.ordinal()]) / 25000);
//			effectiveLevel += 4 + (timerBoost > 5 ? 5 : timerBoost);
		}
		
		/*
		 * Prayer bonus
		 */
		double prayerBonus = 1.0;
		
		if (prayerBook == Prayerbook.NORMAL) {
			
			if (activePrayer[PrayerHandler.SHARP_EYE]) {
				prayerBonus += 0.05;
			} else if (activePrayer[PrayerHandler.HAWK_EYE]) {
				prayerBonus += 0.1;
			} else if (activePrayer[PrayerHandler.EAGLE_EYE]) {
				prayerBonus += 0.15;
			} else if (activePrayer[PrayerHandler.RIGOUR]) {
				prayerBonus += 0.2;
			}
			
		} else if (prayerBook == Prayerbook.CURSES) {
			
			if (activeCurses[CurseHandler.CurseData.LEECH_RANGED.ordinal()]) {
				prayerBonus += 0.05;
			}
		}
		
		/*
		 * Combat style bonuses
		 */
		int effectiveRange = (int) (effectiveLevel * prayerBonus) + 8;
		
		if (attackStyle == FightStyle.ACCURATE) {
			effectiveRange += 3;
		} else if (attackStyle == FightStyle.CONTROLLED) {
			effectiveRange += 1;
		}
		
		/*
		 * Void knight set bonuses
		 * This bonus is doubled with ranged
		 */
		if(attacker.isPlayer()) {
			if (EquipmentBonus.wearingEliteVoid((Player)attacker, CombatType.RANGED)) {
				effectiveRange *= 1.15;
				effectiveRange *= 1.15;
			} else if (EquipmentBonus.wearingVoid((Player)attacker, CombatType.RANGED)) {
				effectiveRange *= 1.1;
				effectiveRange *= 1.1;
			}
		}
		/*
		 * Equipment bonuses
		 */
		double equipmentBonus = 0;

		if(attacker.isPlayer()) {
			equipmentBonus = ((Player)attacker).getBonusManager().getAttackBonus()[BonusManager.ATTACK_RANGE];
		}
		
		/*
		 * Maximum roll
		 */
		int roll = (int) (effectiveRange * (1 + (equipmentBonus / 64)));

		if(attacker.isPlayer()) {
			if (usingSpecial) {
				CombatSpecial special = ((Player)attacker).getCombatSpecial();
				if (special != null) {
					roll *= special.getAccuracyBonus();
				}
			}
		}
		
		if (equipment != null) {
//			if (victim.isNpc() && equipment[Equipment.RING_SLOT].getId() >= 15398 && equipment[Equipment.RING_SLOT].getId() <= 15402
//					&& attacker.getPlayerFields().getSkillAttributes().getSlayerAssignment() != null) {
//				//ferocious ring bonus on slayer tasks
//				SlayerKey key = MobDefinition.forId(victim.getId()).getSlayerKey();
//				if (attacker.getPlayerFields().getSkillAttributes().getSlayerAssignment().getKey() == key) {
//					roll += 4;
//				}
//			} else if (victim.isMob() && equipment[Equipment.HEAD_SLOT].getId() == 15492
//					&& attacker.getPlayerFields().getSkillAttributes().getSlayerAssignment() != null) {
//				//full slayer helmet bonus on slayer tasks
//				SlayerKey key = MobDefinition.forId(victim.getId()).getSlayerKey();
//				if (attacker.getPlayerFields().getSkillAttributes().getSlayerAssignment().getKey() == key) {
//					roll += 4;
//				}
//			}
		}
		
		return roll;
	}
	
	private int getDefenderRoll(Character attacker, Character victim) {
		
		/*
		 * Victim constants
		 */		
		Prayerbook prayerBook = Prayerbook.NORMAL;
		if(victim.isPlayer()) {
			prayerBook = ((Player)victim).getPrayerbook();
		}

		boolean[] activePrayer = victim.getPrayerActive();
		boolean[] activeCurses = victim.getCurseActive();

		Item defenderWeapon = null;
		if(victim.isPlayer()) {
			defenderWeapon = ((Player)victim).getEquipment().get(Equipment.WEAPON_SLOT);
		}

		FightStyle attackStyle = FightStyle.CONTROLLED;

		if(defenderWeapon != null) {
			if(victim.isPlayer()) {
				attackStyle = ((Player)victim).getFightType().getStyle();
			}
		}

		/*
		 * Effective level
		 */
		int effectiveLevel = 0;
		if(victim.isPlayer()) {
			effectiveLevel = ((Player)victim).getSkillManager().getCurrentLevel(Skill.DEFENCE);
		} else {
			effectiveLevel = ((NPC)victim).getDefinition().getDefenceRange();
		}
		
		if (prayerBook == Prayerbook.CURSES
				&& activeCurses[CurseHandler.CurseData.LEECH_DEFENCE.ordinal()]) {
			//currently for every 25 seconds of our leech curse being active, it will add +1 to our effectiveLevel
//			int timerBoost = (int) ((System.currentTimeMillis() - victim.g().getPrayerActiveTime()[Curse.LEECH_DEFENCE.ordinal()]) / 25000);
//			effectiveLevel += 4 + (timerBoost > 5 ? 5 : timerBoost);
		}
		
		/*
		 * Prayer bonus
		 */
		double prayerBonus = 1.0;
		
		if (prayerBook == Prayerbook.NORMAL) {
			
			if (activePrayer[PrayerHandler.THICK_SKIN]) {
				prayerBonus += 0.05;
			} else if (activePrayer[PrayerHandler.ROCK_SKIN]) {
				prayerBonus += 0.1;
			} else if (activePrayer[PrayerHandler.STEEL_SKIN]) {
				prayerBonus += 0.15;
			} else if (activePrayer[PrayerHandler.CHIVALRY]) {
				prayerBonus += 0.2;
			} else if (activePrayer[PrayerHandler.PIETY]) {
				prayerBonus += 0.25;
			} else if (activePrayer[PrayerHandler.RIGOUR]) {
				prayerBonus += 0.25;
			} else if (activePrayer[PrayerHandler.AUGURY]) {
				prayerBonus += 0.25;
			}
			
		} else if (prayerBook == Prayerbook.CURSES) {
			
			if (activeCurses[CurseHandler.CurseData.LEECH_DEFENCE.ordinal()]) {
				prayerBonus += 0.05;
			} else if (activeCurses[CurseHandler.CurseData.TURMOIL.ordinal()]) {
				prayerBonus += 0.15;
			}
		}
		
		/*
		 * Combat style bonuses
		 */
		int effectiveDefence = (int) (effectiveLevel * prayerBonus) + 8;
		
		if (attackStyle == FightStyle.DEFENSIVE) {
			effectiveDefence += 3;
		} else if (attackStyle == FightStyle.CONTROLLED) {
			effectiveDefence += 1;
		}
		
		/*
		 * Equipment bonuses
		 */
		double equipmentBonus = 0;

		if(victim.isPlayer()) {
			equipmentBonus = ((Player)victim).getBonusManager().getDefenceBonus()[BonusManager.DEFENCE_RANGE];
		}
		
		/*
		 * Maximum roll
		 */
		int roll = (int) (effectiveDefence * (1 + (equipmentBonus / 64)));
		
		return roll;
	}
}