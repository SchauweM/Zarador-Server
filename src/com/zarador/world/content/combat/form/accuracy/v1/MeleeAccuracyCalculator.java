package com.zarador.world.content.combat.form.accuracy.v1;

import java.util.concurrent.ThreadLocalRandom;

import com.zarador.model.Item;
import com.zarador.model.Prayerbook;
import com.zarador.model.Skill;
import com.zarador.model.container.impl.Equipment;
import com.zarador.util.MathUtil;
import com.zarador.world.content.combat.CombatFactory;
import com.zarador.world.content.combat.CombatType;
import com.zarador.world.content.combat.effect.EquipmentBonus;
import com.zarador.world.content.combat.form.accuracy.AccuracyCalculator;
import com.zarador.world.content.combat.prayer.CurseHandler;
import com.zarador.world.content.combat.prayer.PrayerHandler;
import com.zarador.world.content.combat.weapon.CombatSpecial;
import com.zarador.world.content.combat.weapon.FightStyle;
import com.zarador.world.content.combat.weapon.FightType;
import com.zarador.world.content.skill.impl.summoning.Familiar;
import com.zarador.world.entity.impl.Character;
import com.zarador.world.entity.impl.npc.NPC;
import com.zarador.world.entity.impl.player.Player;

/**
 * An implementation of {@link AccuracyCalculator} used to calculate
 * whether a {@link com.zarador.world.entity.impl.Character} will land their next hit
 * in combat while using {@link com.zarador.world.content.combat.CombatType#MELEE}.
 *
 * @author Relex
 */
public final class MeleeAccuracyCalculator implements AccuracyCalculator {

	@Override
	public double getAccuracy(Character source, Character victim) {

		/*
		 * Defining some constants
		 */
		Prayerbook victimPrayerBook = Prayerbook.NORMAL;
		boolean[] victimPrayer = victim.getPrayerActive();
		boolean[] victimCurses = victim.getCurseActive();

		if(victim.isPlayer()) {
			Player player = ((Player)victim);
			victimPrayerBook = player.getPrayerbook();
		}

		/*
		 * Prayer protection
		 */
		double prayerProtection = 1.0;
		
		if (source.isNpc()) {
			//if {@param victim} has melee protection prayer and our {@param source}
			//is a {@link org.niobe.world.Mob}, block the hit completely
			if (victimPrayerBook == Prayerbook.NORMAL && victimPrayer[PrayerHandler.PROTECT_FROM_MELEE]) {
				return 0;
			} else if (victimPrayerBook == Prayerbook.CURSES && victimCurses[CurseHandler.CurseData.DEFLECT_MELEE.ordinal()]) {
				return 0;
			}
		} else if (source.isPlayer()) {
			//if {@param victim} has melee protection prayer and our {@param source}
			//is a {@link org.niobe.world.Player}, set our {@value prayerProtection} to 0.6
			//to reduce 40% of the damage
			if (victimPrayerBook == Prayerbook.NORMAL && victimPrayer[PrayerHandler.PROTECT_FROM_MELEE]) {
				prayerProtection = 0.6;
			} else if (victimPrayerBook == Prayerbook.CURSES && victimCurses[CurseHandler.CurseData.DEFLECT_MELEE.ordinal()]) {
				prayerProtection = 0.6;
			}
		}
		
		/*
		 * Now let's get our rolls for both parties and determine
		 * whether the {@param source} will land the hit.
		 */
		int attackerRoll = getAttackerRoll(source, victim);
		int defenderRoll = getDefenderRoll(source, victim);

		defenderRoll *= .75;

		if (CombatFactory.fullDharoks(source) && victim.isPlayer()
				&& MathUtil.random(2) == 0) {
			attackerRoll += (attackerRoll / 2) + MathUtil.random(attackerRoll / 4);
		}
		
		ThreadLocalRandom random = ThreadLocalRandom.current();
				
		if (attackerRoll >= defenderRoll) {
			//to hit 0 based on them
			if (random.nextInt(15) == 0) {
				return 0;
			} else {
				return 1 * prayerProtection;
			}
		} else {
			int division = attackerRoll / 2;
			if (division < 1)
				division = 1;
			
			int chanceToHit = (int) Math.ceil(defenderRoll / division);
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
		Item[] equipment = attacker.isPlayer() ? ((Player)attacker).getEquipment().getItems() : null;
		
		Prayerbook prayerBook = Prayerbook.NORMAL;
		boolean[] activePrayer = attacker.getPrayerActive();
		boolean[] activeCurses = attacker.getCurseActive();

		if(attacker.isPlayer()) {
			Player player = ((Player)attacker);
			prayerBook = player.getPrayerbook();
		}

		boolean usingSpecial = false;

		if(attacker.isPlayer()) {
			Player player = ((Player)attacker);
			if(player.isSpecialActivated()) {
				usingSpecial = true;
			}
		}

		Item weapon = attacker.isPlayer() ? ((Player)attacker).getEquipment().get(Equipment.WEAPON_SLOT) : null;

		FightStyle attackStyle = FightStyle.CONTROLLED;

		if(attacker.isPlayer()) {
			Player player = ((Player) attacker);
			attackStyle = player.getFightType().getStyle();
		}

		//CombatType combatStyle = weapon != null ? weapon.getCombatDefinition().getCombatStyle(0) : CombatStyle.SLASH;

		if(attacker.isPlayer()) {
			Player player = ((Player) attacker);
			if (weapon != null && player.getFightType().ordinal() < FightType.values().length) {
				//attackStyle = weapon.getCombatDefinition().getAttackStyle()[attacker.getFields().getCombatAttributes().getAttackStyle()];
				//combatStyle = weapon.getCombatDefinition().getCombatStyle(attacker.getFields().getCombatAttributes().getAttackStyle());
			}
		}
		
		/*
		 * Effective level
		 */
		int effectiveLevel = 0;

		if(attacker.isPlayer()) {
			Player player = ((Player) attacker);
			effectiveLevel = player.getSkillManager().getCurrentLevel(Skill.ATTACK);
		} else {
			NPC npc = ((NPC) attacker);
			effectiveLevel = npc.getDefinition().getAttackBonus();
		}
		
		if (prayerBook == Prayerbook.CURSES
				&& activeCurses[CurseHandler.CurseData.LEECH_ATTACK.ordinal()]) {
			//TODO: Add leeches
			//currently for every 25 seconds of our leech curse being active, it will add +1 to our effectiveLevel
//			int timerBoost = (int) ((System.currentTimeMillis() - attacker.getFields().getPrayerActiveTime()[CurseHandler.CurseData.LEECH_ATTACK.ordinal()]) / 25000);
//			effectiveLevel += 4 + (timerBoost > 5 ? 5 : timerBoost);
		}
		
		/*
		 * Prayer bonus
		 */
		double prayerBonus = 1.0;
		
		if (prayerBook == Prayerbook.NORMAL) {
			
			if (activePrayer[PrayerHandler.CLARITY_OF_THOUGHT]) {
				prayerBonus += 0.05;
			} else if (activePrayer[PrayerHandler.IMPROVED_REFLEXES]) {
				prayerBonus += 0.1;
			} else if (activePrayer[PrayerHandler.INCREDIBLE_REFLEXES]) {
				prayerBonus += 0.15;
			} else if (activePrayer[PrayerHandler.CHIVALRY]) {
				prayerBonus += 0.15;
			} else if (activePrayer[PrayerHandler.PIETY]) {
				prayerBonus += 0.2;
			}
			
		} else if (prayerBook == Prayerbook.CURSES) {
			
			if (activeCurses[CurseHandler.CurseData.LEECH_ATTACK.ordinal()]) {
				prayerBonus += 0.05;
			} else if (activeCurses[CurseHandler.CurseData.TURMOIL.ordinal()]) {
				prayerBonus += 0.15;
			}
		}
		
		/*
		 * Combat style bonuses
		 */
		int effectiveAttack = (int) (effectiveLevel * prayerBonus) + 8;
		
		if (attackStyle == FightStyle.ACCURATE) {
			effectiveAttack += 3;
		} else if (attackStyle == FightStyle.CONTROLLED) {
			effectiveAttack += 1;
		}
		
		/*
		 * Void knight set bonuses
		 */
		if(attacker.isPlayer()) {
			Player player = ((Player) attacker);
			if (EquipmentBonus.wearingEliteVoid((Player)attacker, CombatType.MELEE)) {
				effectiveAttack *= 1.15;
			} else if (EquipmentBonus.wearingVoid((Player)attacker, CombatType.MELEE)) {
				effectiveAttack *= 1.1;
			}
		}

		double equipmentBonus = 32;

		if(attacker.isPlayer()) {
			Player player = ((Player) attacker);
			equipmentBonus = player.getBonusManager().getAttackBonus()[player.getFightType().getBonusType()];
		}

		//double equipmentBonus = 32;
		
		/*
		 * Maximum roll
		 */
		int roll = (int) (effectiveAttack * (1 + (equipmentBonus / 64)));


		if(attacker.isPlayer()) {
			Player player = ((Player) attacker);
			if (usingSpecial) {
				CombatSpecial special = player.getCombatSpecial();
				if (special != null) {
					roll *= special.getAccuracyBonus();
				}
			}
		}
		
		if (equipment != null) {
			
//			if (victim.isMob() && equipment[Equipment.RING_SLOT].getId() >= 15398 && equipment[Equipment.RING_SLOT].getId() <= 15402
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
//			} else if (victim.isMob() && victim.getId() == 8133 && equipment[Equipment.WEAPON_SLOT].getId() == 11716) {
//				//zamorakian spear on corporeal beast bonus
//				roll += 10;
//			}
			
			if (victim.isNpc() && CombatFactory.fullDharoks(attacker)) {
				roll = ThreadLocalRandom.current().nextInt(2) == 0 ? 0 : roll / 2;
			}
		}
		
		return roll;
	}
	
	private int getDefenderRoll(Character attacker, Character victim) {
		

		/*
		 * Defining some constants
		 */
		Prayerbook prayerBook = Prayerbook.NORMAL;
		boolean[] activePrayer = victim.getPrayerActive();
		boolean[] activeCurses = victim.getCurseActive();

		if(victim.isPlayer()) {
			Player player = ((Player)victim);
			prayerBook = player.getPrayerbook();
		}

		Familiar familiar = null;

		if(victim.isPlayer()) {
			Player player = ((Player)victim);
			familiar = player.getSummoning().getFamiliar();
		}

		Item defenderWeapon = null;

		if(victim.isPlayer()) {
			Player player = ((Player)victim);
			defenderWeapon = player.getEquipment().get(Equipment.WEAPON_SLOT);
		}


		FightStyle attackStyle = FightStyle.CONTROLLED;

		if(victim.isPlayer()) {
			Player player = ((Player) victim);
			attackStyle = player.getFightType().getStyle();
		}
		
		/*
		 * Attacker constants
		 */
		Item attackerWeapon = null;
		if(attacker.isPlayer()) {
			Player player = ((Player)attacker);
			attackerWeapon = player.getEquipment().get(Equipment.WEAPON_SLOT);
		}

//		CombatStyle combatStyle = attackerWeapon != null ? attackerWeapon.getCombatDefinition().getCombatStyle(0) : CombatStyle.SLASH;
//		if (attackerWeapon != null && attacker.getFields().getCombatAttributes().getAttackStyle() < attackerWeapon.getCombatDefinition().getAttackStyle().length) {
//			combatStyle = attackerWeapon.getCombatDefinition().getCombatStyle(attacker.getFields().getCombatAttributes().getAttackStyle());
//		}
		
		/*
		 * Effective level
		 */
		int effectiveLevel;

		if(victim.isPlayer()) {
			Player player = ((Player) victim);
			effectiveLevel = player.getSkillManager().getCurrentLevel(Skill.DEFENCE);
		} else {
			NPC npc = ((NPC) victim);
			effectiveLevel = npc.getDefinition().getDefenceMelee();
		}

		
		if (prayerBook == Prayerbook.CURSES
				&& activeCurses[CurseHandler.CurseData.LEECH_DEFENCE.ordinal()]) {
			//currently for every 25 seconds of our leech curse being active, it will add +1 to our effectiveLevel
//			int timerBoost = (int) ((System.currentTimeMillis() - victim.getFields().getPrayerActiveTime()[Curse.LEECH_DEFENCE.ordinal()]) / 25000);
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
			equipmentBonus = ((Player)victim).getBonusManager().getDefenceBonus()[((Player)victim).getFightType().getBonusType()];
		}
		
		/*
		 * Maximum roll
		 */
		int roll = (int) (effectiveDefence * (1 + (equipmentBonus / 64)));

//		if (familiar != null) {
//			if (familiar.getType() == FamiliarType.WOLPERTINGER) {
//				roll *= 1.05;
//			}
//		}
		
		return roll;
	}
}