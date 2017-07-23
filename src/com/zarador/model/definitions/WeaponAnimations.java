package com.zarador.model.definitions;

import com.zarador.model.Animation;
import com.zarador.model.CharacterAnimations;
import com.zarador.model.Flag;
import com.zarador.model.Item;
import com.zarador.model.container.impl.Equipment;
import com.zarador.world.entity.impl.player.Player;

/**
 * A static utility class that contains methods for changing the appearance
 * animation for a player whenever a new weapon is equipped or an existing item
 * is unequipped.
 * 
 * @author lare96
 */
public final class WeaponAnimations {

	/**
	 * Executes an animation for the argued player based on the animation of the
	 * argued item.
	 * 
	 * @param player
	 *            the player to animate.
	 * @param item
	 *            the item to get the animations for.
	 */
	public static void assign(Player player, Item item) {
		player.getCharacterAnimations().reset();
		player.setCharacterAnimations(getUpdateAnimation(player, item));
		if(player.isResting()) {
			player.setResting(false);
			player.performAnimation(new Animation(11788));
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}
	}

	public static CharacterAnimations getUpdateAnimation(Player player, Item item) {
		String weaponName = item.getDefinition().getName().toLowerCase();
		int shieldId = player.getEquipment().get(Equipment.SHIELD_SLOT).getId();
		int weaponId = player.getEquipment().get(Equipment.WEAPON_SLOT).getId();
		int playerStandIndex = 0x328;
		int playerWalkIndex = 0x333;
		int playerRunIndex = 0x338;
		if (weaponId == 21015) {
			playerStandIndex = 7508;
			playerWalkIndex = 7509;
			playerRunIndex = 7509;
		} else if(weaponId == 21006) {
			playerStandIndex = 8980;
			playerRunIndex = 1210;
			playerWalkIndex = 1146;
		} else if (weaponName.contains("halberd") || weaponName.contains("guthan")) {
			playerStandIndex = 809;
			playerWalkIndex = 1146;
			playerRunIndex = 1210;
		} else if (weaponName.contains("halberd") || weaponName.contains("guthan")) {
			playerStandIndex = 809;
			playerWalkIndex = 1146;
			playerRunIndex = 1210;
		} else if (weaponName.startsWith("basket")) {
			playerWalkIndex = 1836;
			playerRunIndex = 1836;
		} else if (weaponName.contains("ballista")) {
			playerStandIndex = 7220;
			playerRunIndex = 7223;
			playerWalkIndex = 7223;
		} else if (weaponName.contains("dharok")) {
			playerStandIndex = 0x811;
			playerWalkIndex = 0x67F;
			playerRunIndex = 0x680;
		} else if (weaponName.contains("sled")) {
			playerStandIndex = 1461;
			playerWalkIndex = 1468;
			playerRunIndex = 1467;
		} else if (weaponName.contains("ahrim")) {
			playerStandIndex = 809;
			playerWalkIndex = 1146;
			playerRunIndex = 1210;
		} else if (weaponName.contains("verac")) {
			playerStandIndex = 0x328;
			playerWalkIndex = 0x333;
			playerRunIndex = 824;
		} else if (weaponName.contains("with zanik")) {
			playerStandIndex = 4193;
			playerWalkIndex = 4194;
			playerRunIndex = 4194;
		} else if (weaponName.contains("longsword") || weaponName.contains("scimitar") || weaponName.contains("sir owen")) {
			playerStandIndex = 15069;// -1;
			playerRunIndex = 15070;// 12023;
			playerWalkIndex = 15073; // 12024;
		} else if (weaponName.contains("silverlight") || weaponName.contains("korasi's")) {
			playerStandIndex = 12021;
			playerRunIndex = 1210;
			playerWalkIndex = 12024;
		} else if (weaponName.contains("rapier")) {
			playerStandIndex = 8980;
			playerRunIndex = 1210;
			playerWalkIndex = 1146;
		} else if (item.getId() == 21074 || item.getId() == 21077 || item.getId() == 21114 || item.getId() == 21079
				|| item.getId() == 11905 || item.getId() == 12899 || item.getId() == 12900) {
			playerStandIndex = 809;
			playerRunIndex = 1210;
			playerWalkIndex = 1146;
		} else if (weaponName.contains("wand") || weaponName.contains("staff") || weaponName.contains("staff")
				|| weaponName.contains("spear") || item.getId() == 21005 || item.getId() == 21006 || item.getId() == 21010 || item.getId() == 21120) {
			playerStandIndex = 8980;
			playerRunIndex = 1210;
			playerWalkIndex = 1146;
		} else if (weaponName.contains("karil")) {
			playerStandIndex = 2074;
			playerWalkIndex = 2076;
			playerRunIndex = 2077;
		} else if (weaponName.contains("2h sword") || weaponName.contains("godsword")
				|| weaponName.contains("saradomin sw")) {
			playerStandIndex = 7047;
			playerWalkIndex = 7046;
			playerRunIndex = 7039;
		} else if (weaponName.contains("bow")) {
			playerStandIndex = 808;
			playerWalkIndex = 819;
			playerRunIndex = 824;
		}

		if (weaponName.toLowerCase().contains("fixed device")) {
			playerStandIndex = 2316;
			/*
			 * playerStandIndex = 8980; playerRunIndex = 1210; playerWalkIndex =
			 * 1146;
			 */
		}
		switch (item.getId()) {
		case 14018: // katana
			playerStandIndex = 809; // wrong
			playerWalkIndex = 7046;
			playerRunIndex = 7046;
			break;
		case 3271:
		case 21003:
		case 18353: // maul chaotic
			playerStandIndex = 13217;
			playerWalkIndex = 13218;
			playerRunIndex = 13220;
			break;
		case 16184:
			playerStandIndex = 13217;
			playerWalkIndex = 13218;
			playerRunIndex = 13220;
			break;
		case 16425:
			playerStandIndex = 13217;
			playerWalkIndex = 13218;
			playerRunIndex = 13220;
			break;
		case 4151:
		case 12006:
		case 13444:
		case 15441: // whip
		case 15442: // whip
		case 15443: // whip
		case 15444: // whip
		case 21000: // whip
		case 21001: // whip
		case 21002: // whip
		case 21004: // whip
		case 21005: // whip
		case 21007: // whip
		case 21371: // whip
		case 21372: // whip
		case 21373: // whip
		case 21374: // whip
		case 21375: // whip
			playerStandIndex = 11973;
			playerWalkIndex = 11975;
			playerRunIndex = 1661;
			break;
		case 15039:
			playerStandIndex = 12000;
			playerWalkIndex = 1663;
			playerRunIndex = 1664;
			break;
		case 10887:
			playerStandIndex = 5869;
			playerWalkIndex = 5867;
			playerRunIndex = 5868;
			break;
		case 6528:
		case 20084:
			playerStandIndex = 0x811;
			playerWalkIndex = 2064;
			playerRunIndex = 1664;
			break;
		case 4153:
			playerStandIndex = 1662;
			playerWalkIndex = 1663;
			playerRunIndex = 1664;
			break;
		case 15241:
			playerStandIndex = 12155;
			playerWalkIndex = 12154;
			playerRunIndex = 12154;
			break;
		case 20000:
		case 20001:
		case 20002:
		case 20003:
		case 11694:
		case 11696:
		case 11730:
		case 11698:
		case 11700:
			break;
		case 1305:
			playerStandIndex = 809;
			break;
		}
		return new CharacterAnimations(playerStandIndex, playerWalkIndex, playerRunIndex);
	}

	public static int getAttackAnimation(Player c) {
		int weaponId = c.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId();
		int shieldId = c.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId();
		String weaponName = ItemDefinition.forId(weaponId).getName().toLowerCase();
		String prop = c.getFightType().toString().toLowerCase();
		if(weaponId == 21015) {
			return 7507;
		}
		if (weaponId == 18373)
			return 1074;
		if (weaponId == 10033 || weaponId == 10034)
			return 2779;
		if (prop.contains("dart")) {
			if (prop.contains("long"))
				return 6600;
			return 582;
		}
		if (weaponName.contains("javelin") || weaponName.contains("thrownaxe")) {
			return 806;
		}
		if (weaponName.contains("halberd")) {
			return 440;
		}
		if (weaponName.startsWith("dragon dagger")) {
			if (prop.contains("slash"))
				return 377;
			return 376;
		}
		if (weaponName.endsWith("dagger")) {
			if (prop.contains("slash"))
				return 13048;
			return 13049;
		}
		if (weaponName.equals("staff of light") || weaponId == 21005 || weaponId == 21010 || weaponId == 11905 || weaponId == 12899 || weaponId == 12900
				|| weaponId == 21104) {
			if (prop.contains("stab"))
				return 13044;
			else if (prop.contains("lunge"))
				return 13047;
			else if (prop.contains("slash"))
				return 13048;
			else if (prop.contains("block"))
				return 13049;
		} else if (weaponName.startsWith("staff") || weaponName.endsWith("staff")) {
			// bash = 400
			return 401;
		}
		if (weaponName.endsWith("warhammer") || weaponName.endsWith("battleaxe") || weaponId == 13904)
			return 401;
		if (weaponName.contains("2h sword") || weaponName.contains("godsword")
				|| weaponName.contains("saradomin sword")) {
			/*
			 * if(prop.contains("stab")) return 11981; else
			 * if(prop.contains("slash")) return 11980;
			 */
			return 11979;
		}
		if (weaponName.contains("brackish")) {
			if (prop.contains("lunge") || prop.contains("slash"))
				return 12029;
			return 12028;
		}
		if (weaponName.contains("rapier") || weaponName.contains("sir owen")) {
			if (prop.contains("slash") && !weaponName.contains("sir owen"))
				return 12029;
			return 386;
		}
		if (weaponName.contains("scimitar") || weaponName.contains("longsword") || weaponName.contains("korasi's")) {
			if (prop.contains("lunge"))
				return 15072;
			return 15071;
		}
		if (weaponName.contains("spear") || weaponId == 21120) {
			if (prop.contains("lunge"))
				return 13045;
			else if (prop.contains("slash"))
				return 13047;
			return 13044;
		}
		if (weaponName.contains("claws"))
			return 393;
		if (weaponName.contains("bludgeon"))
			return 3297;
		if (weaponName.contains("maul") && !weaponName.contains("granite"))
			return 13055;
		if (weaponName.contains("dharok")) {
			if (prop.contains("block"))
				return 2067;
			return 2066;
		}
		if (weaponName.contains("sword")) {
			return prop.contains("slash") ? 12311 : 12310;
		}
		if (weaponName.contains("karil"))
			return 2075;
		else if (weaponName.contains("'bow") || weaponName.contains("crossbow"))
			return 4230;
		if (weaponName.contains("bow") && !weaponName.contains("'bow"))
			return 426;
		if (weaponName.contains("pickaxe")) {
			if (prop.contains("smash"))
				return 401;
			return 400;
		}
		if (weaponName.contains("mace")) {
			if (prop.contains("spike"))
				return 13036;
			return 13035;
		}
		switch (weaponId) { // if you don't want
		// to use strings
		case 20000:
		case 20001:
		case 20002:
		case 20003:
			return 7041;
		case 6522: // Obsidian throw
			return 2614;
		case 4153: // granite maul
			return 1665;
		case 13879:
		case 13883:
			return 806;
		case 16184:
			return 2661;
		case 16425:
			return 2661;
		case 15241:
			return 12153;
		case 4747: // torag
			return 0x814;
		case 4710: // ahrim
			return 406;
		case 21003:
		case 18353:
			return 13055;
		case 18349:
			return 13049;
		case 19146:
			return 386;
		case 4755: // verac
			return 2062;
		case 4734: // karil
			return 2075;
		case 10887:
			return 5865;
		case 4151:
		case 13444:
		case 12006:
		case 15441: // whip
		case 15442: // whip
		case 15443: // whip
		case 15444: // whip
		case 21000: // whip
		case 21001: // whip
		case 21002: // whip
		case 21004: // whip
		case 21005: // whip
		case 21006: // whip
		case 21007: // whip
			return 11968;
		case 6528:
		case 20084:
			return 2661;
		default:
			return c.getFightType().getAnimation();
		}
	}

	public static int getBlockAnimation(Player c) {
		int weaponId = c.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId();
		String shield = ItemDefinition.forId(c.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId()).getName()
				.toLowerCase();
		String weapon = ItemDefinition.forId(weaponId).getName().toLowerCase();
		if (shield.contains("defender"))
			return 4177;
		if(weaponId == 16389) {
			return -1;
		}
		if (shield.contains("2h"))
			return 7050;
		if (shield.contains("book") && (weapon.contains("wand")))
			return 420;
		if (shield.contains("shield"))
			return 1156;
		if (weapon.contains("scimitar") || weapon.contains("longsword") || weapon.contains("korasi"))
			return 15074;
		switch (weaponId) {
		case 4755:
			return 2063;
		case 15241:
			return 12156;
		case 13899:
			return 13042;
		case 18355:
			return 13046;
		case 14484:
			return 397;
		case 11716:
			return 12008;
		case 4153:
			return 1666;
		case 4151:
		case 21000: // whip
		case 21001: // whip
		case 21002: // whip
		case 21004: // whip
		case 21005: // whip
		case 21006: // whip
		case 21007: // whip
		case 13444:
		case 12006:
		case 15441: // whip
		case 15442: // whip
		case 15443: // whip
		case 15444: // whip
			return 11974;
		case 15486:
		case 21077:
		case 21114:
		case 11905:
		case 12900:
		case 12899:
		case 21079:
		case 15502:
		case 22209:
		case 22211:
		case 22207:
		case 22213:
		case 14004:
		case 14005:
		case 14006:
		case 14007:
			return 12806;
		case 18349:
			return 13038;
		case 21003:
		case 18353:
			return 13054;
		case 18351:
			return 13042;
		case 20000:
		case 20001:
		case 20002:
		case 20003:
		case 11694:
		case 11698:
		case 11700:
		case 11696:
		case 11730:
			return 7050;
		case -1:
			return 424;
		default:
			return 424;
		}
	}
}
