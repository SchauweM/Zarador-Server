package com.zarador.world.content.combat.form.max.v1;

import com.zarador.model.Item;
import com.zarador.model.Locations;
import com.zarador.model.Prayerbook;
import com.zarador.model.Skill;
import com.zarador.model.container.impl.Equipment;
import com.zarador.world.content.BonusManager;
import com.zarador.world.content.combat.CombatFactory;
import com.zarador.world.content.combat.CombatType;
import com.zarador.world.content.combat.effect.EquipmentBonus;
import com.zarador.world.content.combat.form.max.MaxHitCalculator;
import com.zarador.world.content.combat.prayer.CurseHandler;
import com.zarador.world.content.combat.prayer.PrayerHandler;
import com.zarador.world.content.combat.weapon.CombatSpecial;
import com.zarador.world.content.combat.weapon.FightStyle;
import com.zarador.world.content.skill.impl.slayer.SlayerMasters;
import com.zarador.world.entity.impl.Character;
import com.zarador.world.entity.impl.npc.NPC;
import com.zarador.world.entity.impl.player.Player;

/**
 * An implementation of {@link MaxHitCalculator} used for our {@link com.zarador.world.content.combat.CombatType#MELEE}
 * attack type.
 *
 * @author Relex
 */
public final class MeleeMaxHitCalculator implements MaxHitCalculator {

	@Override
	public int getMaxHit(Character source, Character victim) {

		if(source.isNpc()) {
			return ((NPC)source).getDefinition().getMaxHit();
		}
		
		/*
		 * Defining some constants.
		 */
		Item[] equipment = null;

		if(source.isPlayer()) {
			equipment = ((Player)source).getEquipment().getItems();
		}
		
		Item weapon = null;

		if(source.isPlayer()) {
			weapon = ((Player)source).getEquipment().get(Equipment.WEAPON_SLOT);
		}

		boolean usingSpecial = false;
		if(source.isPlayer()) {
			if (((Player)source).isSpecialActivated()) {
				usingSpecial = true;
			}
		}
		
		Prayerbook prayerBook = Prayerbook.NORMAL;
		if(source.isPlayer()) {
			prayerBook = ((Player)source).getPrayerbook();
		}

		boolean[] activePrayer = source.getPrayerActive();
		boolean[] activeCurses = source.getCurseActive();

		FightStyle attackStyle = FightStyle.CONTROLLED;

		if(weapon != null && source.isPlayer()) {
			attackStyle = ((Player)source).getFightType().getStyle();
		}

		/*
		 * Effective strength level
		 */
		int effectiveLevel = 0;

		if(source.isPlayer()) {
			effectiveLevel = ((Player)source).getSkillManager().getCurrentLevel(Skill.STRENGTH);
		}
		
		if (prayerBook == Prayerbook.CURSES
				&& activeCurses[CurseHandler.CurseData.LEECH_STRENGTH.ordinal()]) {
			//currently for every 25 seconds of our leech curse being active, it will add +1 to our effectiveLevel
//			int timerBoost = (int) ((System.currentTimeMillis() - source.getFields().getPrayerActiveTime()[Curse.LEECH_STRENGTH.ordinal()]) / 25000);
//			effectiveLevel += 4 + (timerBoost > 5 ? 5 : timerBoost);
		}
		
		
		/*
		 * Prayer and curses
		 */
		double prayerBonus = 1.0;
		
		if (prayerBook == Prayerbook.NORMAL) {
			
			if (activePrayer[PrayerHandler.BURST_OF_STRENGTH]) {
				prayerBonus += 0.05;
			} else if (activePrayer[PrayerHandler.SUPERHUMAN_STRENGTH]) {
				prayerBonus += 0.1;
			} else if (activePrayer[PrayerHandler.ULTIMATE_STRENGTH]) {
				prayerBonus += 0.15;
			} else if (activePrayer[PrayerHandler.CHIVALRY]) {
				prayerBonus += 0.18;
			} else if (activePrayer[PrayerHandler.PIETY]) {
				prayerBonus += 0.23;
			}
			
		} else if (prayerBook == Prayerbook.CURSES) {
			
			if (activeCurses[CurseHandler.CurseData.LEECH_STRENGTH.ordinal()]) {
				prayerBonus += 0.05;
			} else if (activeCurses[CurseHandler.CurseData.TURMOIL.ordinal()]) {
				prayerBonus += 0.23;
			}
		}
		
		/*
		 * Combat style bonuses
		 */
		int effectiveStrength = (int) (effectiveLevel * prayerBonus) + 8;
		
		if (prayerBook == Prayerbook.CURSES && activeCurses[CurseHandler.CurseData.TURMOIL.ordinal()]) {
			effectiveStrength += 9;
		}
		
		if (attackStyle == FightStyle.AGGRESSIVE) {
			effectiveStrength += 3;
		} else if (attackStyle == FightStyle.CONTROLLED) {
			effectiveStrength += 1;
		}
		
		/*
		 * Void knight set bonuses
		 */
		if(source.isPlayer()) {
			if (EquipmentBonus.wearingEliteVoid((Player)source, CombatType.MELEE)) {
				effectiveStrength *= 1.15;
			} else if (EquipmentBonus.wearingVoid((Player)source, CombatType.MELEE)) {
				effectiveStrength *= 1.1;
			}
		}
		
		
		/*
		 * Equipment bonuses
		 */
		double equipmentBonus = 0;

		if(source.isPlayer()) {
			equipmentBonus = ((Player)source).getBonusManager().getOtherBonus()[BonusManager.BONUS_STRENGTH];
		}
		
//		if (victim.isMob() && equipment != null) {
//			if (equipment[Equipment.WEAPON_SLOT].getId() == 7807
//					&& MobDefinition.forId(victim.getId()).getLevel() < source.getSkillManager().getCombatLevel()) {
//				//anger battle axe bonus
//				equipmentBonus += 100;
//			} else if (victim.getId() >= 2881 && victim.getId() <= 2883) {
//				//bonus on dagannoth kings
//				equipmentBonus += 25;
//			} else if (MobDefinition.forId(victim.getId()).getLevel() < 100) {
//				if (equipment[Equipment.WEAPON_SLOT].getId() == 7806
//						&& source.getFields().getGameMode() == GameMode.LEGEND) {
//					//anger sword bonus
//					equipmentBonus += 50;
//				} else if (equipment[Equipment.WEAPON_SLOT].getId() == 21335
//						&& source.getFields().getGameMode() == GameMode.EXTREME) {
//					//eva's longsword bonus
//					equipmentBonus += 25;
//				}
//			}
//
//			if (equipment[Equipment.WEAPON_SLOT].getId() == 15426
//					&& Christmas.inTheSeason()) {
//				//candy cane bonus on christmas and oly on npcs
//				equipmentBonus += 100;
//			}
//		}
//
		/*
		 * Base damage
		 */
		int maxHit = (int) (5 + effectiveStrength * (equipmentBonus + 64) / 64);
		
		/*
		 * Bonus damage
		 */
		if(source.isPlayer()) {
			if (usingSpecial) {
				CombatSpecial special = ((Player)source).getCombatSpecial();
				if (special != null) {
					maxHit *= special.getStrengthBonus();
					//maxHit += special.getExtraDamage(source, victim);
				}
			}
		}
		
		if (equipment != null) {
			
			if (CombatFactory.fullDharoks(source) && source.isPlayer()) {
				//dharok's bonus
				
				//first, we get set our base equal to an equal proportion to the proportion of 
				//our current hp
				double base = (1 - ((double) ((Player)source).getSkillManager().getCurrentLevel(Skill.CONSTITUTION) / (double) ((Player)source).getSkillManager().getMaxLevel(Skill.CONSTITUTION)));
				
				//if our base is higher than 0, we'll add a whole number to it so we can multiply
				//to our max hit
				//if current hp is 99/990, it'll add 1 to 0.9 equaling 1.9
				//otherwise if our hp is 990/990 it won't add anything to our max
				if (base > 0) {
					base += 1;
					maxHit *= base;
				}
			} else if (equipment[Equipment.AMULET_SLOT].getId() == 11128) {
				//berserker necklace bonus
				switch (equipment[Equipment.WEAPON_SLOT].getId()) {
				case 6523: //toktz-xil-ak
				case 6525: //toktz-xil-ek
				case 6526: //toktz-mej-tal
				case 6527: //tzhaar-ket-em
				case 6528: //tzhaar-ket-om
					maxHit *= 1.2;
					break;
				}
			}

			if(source.isPlayer()) {
				Player player = ((Player)source);
				if (victim.isNpc() && equipment[Equipment.RING_SLOT].getId() >= 15398 && equipment[Equipment.RING_SLOT].getId() <= 15402) {
					if(player.getSlayer().getSlayerMaster() != null) {
						if(player.getSlayer().getSlayerMaster() == SlayerMasters.KURADAL) {
							if(player.getLocation() == Locations.Location.KURADALS_DUNGEON) {
								maxHit *= 1.1;
							}
						}
					}
				}
			}
		}
		
		return maxHit;
	}
}