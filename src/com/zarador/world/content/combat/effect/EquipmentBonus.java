package com.zarador.world.content.combat.effect;

import com.zarador.model.Item;
import com.zarador.model.container.impl.Equipment;
import com.zarador.world.content.combat.CombatType;
import com.zarador.world.entity.impl.player.Player;

public class EquipmentBonus {

	/*
	* Tells you if you are wearing void armour
	*/
	public static boolean wearingVoid(Player player, CombatType attackType) {
		int amount = 0;
		boolean hasHelmet = false;
		Item head = player.getEquipment().getItems()[Equipment.HEAD_SLOT];
		Item body = player.getEquipment().getItems()[Equipment.BODY_SLOT];
		Item legs = player.getEquipment().getItems()[Equipment.LEG_SLOT];
		Item gloves = player.getEquipment().getItems()[Equipment.HANDS_SLOT];
		Item shield = player.getEquipment().getItems()[Equipment.SHIELD_SLOT];
		for(int i = 0; i < VOID_ARMOUR.length; i++) {
			if(body.getId() == VOID_ARMOUR[i]) {
				amount++;
			} else if(legs.getId() == VOID_ARMOUR[i]) {
				amount++;
			} else if(gloves.getId() == VOID_ARMOUR[i]) {
				amount++;
			} else if(shield.getId() == VOID_ARMOUR[i]) {
				amount++;
			}
		}
		if(attackType == CombatType.RANGED) {
			if(head.getId() == RANGED_VOID_HELM) {
				hasHelmet = true;
			}
		} else if(attackType == CombatType.MAGIC) {
			if(head.getId() == MAGE_VOID_HELM) {
				hasHelmet = true;
			}
		} else if(attackType == CombatType.MELEE) {
			if(head.getId() == MELEE_VOID_HELM) {
				hasHelmet = true;
			}
		}
		return amount >= 3 && hasHelmet;
	}

	/*
	* Tells you if you are wearing elite void armour
	*/
	public static boolean wearingEliteVoid(Player player, CombatType attackType) {
		int amount = 0;
		boolean hasHelmet = false;
		Item head = player.getEquipment().getItems()[Equipment.HEAD_SLOT];
		Item body = player.getEquipment().getItems()[Equipment.BODY_SLOT];
		Item legs = player.getEquipment().getItems()[Equipment.LEG_SLOT];
		Item gloves = player.getEquipment().getItems()[Equipment.HANDS_SLOT];
		Item shield = player.getEquipment().getItems()[Equipment.SHIELD_SLOT];
		for(int i = 0; i < ELITE_VOID_ARMOUR.length; i++) {
			if(body.getId() == ELITE_VOID_ARMOUR[i]) {
				amount++;
			} else if(legs.getId() == ELITE_VOID_ARMOUR[i]) {
				amount++;
			} else if(gloves.getId() == ELITE_VOID_ARMOUR[i]) {
				amount++;
			} else if(shield.getId() == ELITE_VOID_ARMOUR[i]) {
				amount++;
			}
		}
		if(attackType == CombatType.RANGED) {
			if(head.getId() == RANGED_VOID_HELM) {
				hasHelmet = true;
			}
		} else if(attackType == CombatType.MAGIC) {
			if(head.getId() == MAGE_VOID_HELM) {
				hasHelmet = true;
			}
		} else if(attackType == CombatType.MELEE) {
			if(head.getId() == MELEE_VOID_HELM) {
				hasHelmet = true;
			}
		}
		return amount >= 3 && hasHelmet;
	}

	/**
	 * Grabs waht your vanguard set bonus should be
	 * Vanguard set gives accuracy bonus 10% per piece.
	 * @param player
	 * @param attackType
	 * @return
	 */
	public static double getVanguardBonus(Player player, CombatType attackType) {
		if(attackType != CombatType.MELEE) {
			return 0;
		}
		Item head = player.getEquipment().getItems()[Equipment.HEAD_SLOT];
		Item body = player.getEquipment().getItems()[Equipment.BODY_SLOT];
		Item legs = player.getEquipment().getItems()[Equipment.LEG_SLOT];
		//Item gloves = player.getEquipment().getItems()[Equipment.HANDS_SLOT];
		Item boots = player.getEquipment().getItems()[Equipment.FEET_SLOT];
		double bonus = 0;
		for(int i = 0; i < VANGUARD_ARMOUR.length; i++) {
			if(head.getId() == VANGUARD_ARMOUR[i]) {
				bonus += .10;
			} else if(body.getId() == VANGUARD_ARMOUR[i]) {
				bonus += .10;
			} else if(legs.getId() == VANGUARD_ARMOUR[i]) {
				bonus += .10;
			} else if(boots.getId() == VANGUARD_ARMOUR[i]) {
				bonus += .10;
			}
		}
		//System.out.println("Vanguard bonus: "+bonus);
		return bonus;
	}

	private static final int MAGE_VOID_HELM = 11663;

	private static final int RANGED_VOID_HELM = 11664;

	private static final int MELEE_VOID_HELM = 11665;

	public static int[] VOID_ARMOUR = new int[]{8839, 8840, 8842, 19711};

	public static final int[] ELITE_VOID_ARMOUR = { 19785, 19786, 19787, 19788, 19789, 19790, 8842, 19711};

	public static final int[] VANGUARD_ARMOUR = { 21472, 21473, 21474, 21476};
}