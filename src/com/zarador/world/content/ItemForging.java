package com.zarador.world.content;

import com.zarador.model.Item;
import com.zarador.model.Skill;
import com.zarador.model.definitions.ItemDefinition;
import com.zarador.util.Misc;
import com.zarador.world.entity.impl.player.Player;

/**
 * Handles item forging, such as Spirit shields making etc.
 * 
 * @author Gabriel Hannason and Samy
 */
public class ItemForging {

	public static void forgeItem(final Player p, final int item1, final int item2) {
		if (item1 == item2)
			return;
		ItemForgeData data = ItemForgeData.getDataForItems(item1, item2);
		if (data == null || !p.getInventory().contains(item1) || !p.getInventory().contains(item2))
			return;
		if (!p.getClickDelay().elapsed(500))
			return;
		if (p.getInterfaceId() > 0) {
			p.getPacketSender().sendMessage("Please close the interface you have open before doing this.");
			return;
		}
		Skill skill = Skill.forId(data.skillRequirement[0]);
		int skillReq = data.skillRequirement[1];
		if (p.getSkillManager().getCurrentLevel(skill) >= skillReq) {
			for (Item reqItem : data.requiredItems) {
				if (!p.getInventory().contains(reqItem.getId())
						|| p.getInventory().getAmount(reqItem.getId()) < reqItem.getAmount()) {
					p.getPacketSender().sendMessage("You need " + Misc.anOrA(reqItem.getDefinition().getName()) + " "
							+ reqItem.getDefinition().getName() + " to forge a new item.");
					return;
				}
			}
			final String itemName = Misc.formatText(ItemDefinition.forId(data.product.getId()).getName().toLowerCase());
			for (Item reqItem : data.requiredItems) {
				if (reqItem.getId() == 1755 || reqItem.getId() == 1595)
					continue;
				p.getInventory().delete(reqItem);
			}
			if(data == ItemForgeData.DRAGONSTONE_BOLTS) {
				data.product.setAmount(15);
			}
			p.getInventory().add(data.product.getId(), data.product.getAmount());
			p.getPacketSender().sendMessage("You make " + Misc.anOrA(itemName) + " " + itemName + ".");
			p.getClickDelay().reset();
			p.getSkillManager().addExactExperience(skill, data.skillRequirement[2], true);
			return;
		} else {
			p.getPacketSender().sendMessage("You need " + Misc.anOrA(skill.getFormatName()) + " "
					+ skill.getFormatName() + " level of at least " + skillReq + " to forge this item.");
			return;
		}
	}

	/**
	 ** The enum holding all our data
	 */
	private enum ItemForgeData {
		BLESSED_SPIRIT_SHIELD(new Item[] { new Item(13754), new Item(13734) }, new Item(13736), new int[] { 1, -1, 0 }),
		SPECTRAL_SPIRIT_SHIELD(new Item[] { new Item(13752), new Item(13736) }, new Item(13744), new int[] { 13, 85, 40000 }),
		ARCANE_SPIRIT_SHIELD(new Item[] { new Item(13746), new Item(13736) }, new Item(13738), new int[] { 13, 85, 40000 }),
		ELYSIAN_SPIRIT_SHIELD(new Item[] { new Item(13750), new Item(13736) }, new Item(13742), new int[] { 13, 85, 40000 }),
		DIVINE_SPIRIT_SHIELD(new Item[] { new Item(13748), new Item(13736) }, new Item(13740), new int[] { 13, 85, 40000 }),

		AMULET_OF_GLORY(new Item[] { new Item(2357), new Item(1615) }, new Item(1704), new int[] { 12, 80, 10000 }),
		AMULET_OF_STRENGTH(new Item[] { new Item(2357), new Item(1603) }, new Item(1725), new int[] { 12, 50, 3500 }),
		AMULET_OF_MAGIC(new Item[] { new Item(2357), new Item(1607) }, new Item(1727), new int[] { 12, 24, 1400 }),
		AMULET_OF_POWER(new Item[] { new Item(2357), new Item(1691) }, new Item(1731), new int[] { 12, 70, 4500 }),
		AMULET_OF_DEFENCE(new Item[] { new Item(2357), new Item(1605) }, new Item(1729), new int[] { 12, 31, 2100 }),
		AMULET_OF_FURY(new Item[] { new Item(2357), new Item(6573) }, new Item(6585), new int[] { 12, 90, 175000 }),

		DRAGONSTONE_BOLTS(new Item[] { new Item(9144, 15), new Item(9193, 15) }, new Item(9341, 15), new int[] { 9, 80, 5000 }),
		DIAMOND_BOLTS(new Item[] { new Item(9143, 15), new Item(9192, 15) }, new Item(9340, 15), new int[] { 9, 70, 4000 }),
		RUBY_BOLTS(new Item[] { new Item(9143, 15), new Item(9191, 15) }, new Item(9339, 15), new int[] { 9, 60, 2000 }),
		EMERALD_BOLTS(new Item[] { new Item(9142, 15), new Item(9190, 15) }, new Item(9238, 15), new int[] { 9, 40, 1000 }),
		SAPPHIRE_BOLTS(new Item[] { new Item(9142, 15), new Item(9189, 15) }, new Item(9337, 15), new int[] { 9, 30, 500 }),

		TOKKUL(new Item[] { new Item(6570), new Item(6522) }, new Item(6529, 1000), new int[] { 12, 95, 10000 }),

		ARDOUGNE_CLOAK_4(new Item[] { new Item(15103), new Item(15104), new Item(15105), new Item(15106) }, new Item(19748), new int[] { 5, 94, 200000 }),

		DRAGON_SQ_SHIELD(new Item[] { new Item(2368), new Item(2366) }, new Item(1187), new int[] { 13, 60, 10000 }),
		DRAGON_PLATEBY(new Item[] { new Item(14472), new Item(14474), new Item(14476) }, new Item(14479), new int[] { 13, 92, 120000 }),
		DRAGONFIRE_SHIELD(new Item[] { new Item(11286), new Item(1540) }, new Item(11283), new int[] { 13, 82, 36000 }),

		CRYSTAL_KEY(new Item[] { new Item(985), new Item(987) }, new Item(989), new int[] { 1, -1, 0 }),

		GODSWORD_BLADE(new Item[] { new Item(11710), new Item(11712), new Item(11714) }, new Item(11690), new int[] { 13, 80, 0 }),
		ARMADYL_GODSWORD(new Item[] { new Item(11702), new Item(11690) }, new Item(11694), new int[] { 1, -1, 0 }),
		BANDOS_GODSWORD(new Item[] { new Item(11704), new Item(11690) }, new Item(11696), new int[] { 1, -1, 0 }),
		SARADOMIN_GODSWORD(new Item[] { new Item(11706), new Item(11690) }, new Item(11698), new int[] { 1, -1, 0 }),
		ZAMORAK_GODSWORD(new Item[] { new Item(11708), new Item(11690) }, new Item(11700), new int[] { 1, -1, 0 }),

		AMULET_OF_FURY_ORNAMENT(new Item[] { new Item(19333), new Item(6585) }, new Item(19335), new int[] { 1, -1, 0 }),
		DRAGON_FULL_HELM_SPIKE(new Item[] { new Item(19354), new Item(11335) }, new Item(19341), new int[] { 1, -1, 0 }),
		DRAGON_PLATELEGS_SPIKE(new Item[] { new Item(19356), new Item(4087) }, new Item(19343), new int[] { 1, -1, 0 }),
		DRAGON_PLATEBODY_SPIKE(new Item[] { new Item(19358), new Item(14479) }, new Item(19342), new int[] { 1, -1, 0 }),
		DRAGON_SQUARE_SHIELD_SPIKE(new Item[] { new Item(19360), new Item(1187) }, new Item(19345), new int[] { 1, -1, 0 }),
		DRAGON_FULL_HELM_GOLD(new Item[] { new Item(19346), new Item(11335) }, new Item(19336), new int[] { 1, -1, 0 }),
		DRAGON_PLATELEGS_GOLD(new Item[] { new Item(19348), new Item(4087) }, new Item(19338), new int[] { 1, -1, 0 }),
		DRAGON_PLATEBODY_GOLD(new Item[] { new Item(19350), new Item(14479) }, new Item(19337), new int[] { 1, -1, 0 }),
		DRAGON_SQUARE_SHIELD_GOLD(new Item[] { new Item(19352), new Item(1187) }, new Item(19340), new int[] { 1, -1, 0 }),

		ABYSSAL_VINE_WHIP_RED(new Item[] { new Item(21369), new Item(4151) }, new Item(21371), new int[] { 18, 75, 4500 }),
		ABYSSAL_VINE_WHIP_YELLOW(new Item[] { new Item(21369), new Item(15441) }, new Item(21372), new int[] { 18, 75, 4500 }),
		ABYSSAL_VINE_WHIP_WHITE(new Item[] { new Item(21369), new Item(15443) }, new Item(21374), new int[] { 18, 75, 4500 }),
		ABYSSAL_VINE_WHIP_BLUE(new Item[] { new Item(21369), new Item(15442) }, new Item(21373), new int[] { 18, 75, 4500 }),
		ABYSSAL_VINE_WHIP_GREEN(new Item[] { new Item(21369), new Item(15444) }, new Item(21375), new int[] { 18, 75, 4500 }),

		FULL_SLAYER_HELMET(new Item[] { new Item(13263), new Item(15490), new Item(15488) }, new Item(15492), new int[] { 18, 75, 0 }),

		/*
		DRAGONBONE_HAT(new Item[] { new Item(11601), new Item(14014) }, new Item(11602), new int[] { 13, 99, 0 }),
		DRAGONBONE_MAGE_TOP(new Item[] { new Item(11601), new Item(14015) }, new Item(11603), new int[] { 13, 99, 0 }),
		DRAGONBONE_MAGE_BOTTOMS(new Item[] { new Item(11601), new Item(14016) }, new Item(11604), new int[] { 13, 99, 0 }),
		DRAGONBONE_MAGE_GLOVES(new Item[] { new Item(11601), new Item(6922) }, new Item(11605), new int[] { 13, 99, 0 }),
		DRAGONBONE_MAGE_BOOTS(new Item[] { new Item(11601), new Item(20002) }, new Item(11606), new int[] { 13, 99, 0 }),

		DRAGONBONE_FULL_HELM(new Item[] { new Item(11601), new Item(14008) }, new Item(11607), new int[] { 13, 99, 0 }),
		DRAGONBONE_PLATEBODY(new Item[] { new Item(11601), new Item(14009) }, new Item(11608), new int[] { 13, 99, 0 }),
		DRAGONBONE_PLATELEGS(new Item[] { new Item(11601), new Item(14010) }, new Item(11611), new int[] { 13, 99, 0 }),
		DRAGONBONE_MELEE_BOOTS(new Item[] { new Item(11601), new Item(20000) }, new Item(11610), new int[] { 13, 99, 0 }),
		DRAGONBONE_MELEE_GLOVES(new Item[] { new Item(11601), new Item(7462) }, new Item(11609), new int[] { 13, 99, 0 }),

		DRAGONBONE_COIF(new Item[] { new Item(11601), new Item(14011) }, new Item(11616), new int[] { 13, 99, 0 }),
		DRAGONBONE_BODY(new Item[] { new Item(11601), new Item(14012) }, new Item(11618), new int[] { 13, 99, 0 }),
		DRAGONBONE_CHAPS(new Item[] { new Item(11601), new Item(14013) }, new Item(11615), new int[] { 13, 99, 0 }),
		DRAGONBONE_RANGED_BOOTS(new Item[] { new Item(11601), new Item(20001) }, new Item(11617), new int[] { 13, 99, 0 }),
		DRAGONBONE_RANGED_GLOVES(new Item[] { new Item(11601), new Item(2491) }, new Item(11614), new int[] { 13, 99, 0 }),
		DRAGONBONE_SPIRIT_SHIELD(new Item[] { new Item(11601), new Item(11613) }, new Item(21104), new int[] { 13, 99, 0 }),
		*/
		/**
		 * Hastas
		 */

		BRONZE_HASTA(new Item[] { new Item(2349), new Item(1237) }, new Item(11367), new int[] { 13, 18, 1750 }),
		IRON_HASTA(new Item[] { new Item(2351), new Item(1239) }, new Item(11369), new int[] { 13, 33, 2250 }),
		STEEL_HASTA(new Item[] { new Item(2353), new Item(1241) }, new Item(11371), new int[] { 13, 48, 4350 }),
		MITHRIL_HASTA(new Item[] { new Item(2359), new Item(1243) }, new Item(11373), new int[] { 13, 68, 6250 }),
		ADAMANT_HASTA(new Item[] { new Item(2361), new Item(1245) }, new Item(11375), new int[] { 13, 88, 7800 }),
		RUNE_HASTA(new Item[] { new Item(2363), new Item(1247) }, new Item(11377), new int[] { 13, 96, 8700 }),

		/**
		 * Cerberus drops
		 */
		INFERNAL_PICKAXE(new Item[] { new Item(6643), new Item(15259) }, new Item(12704), new int[] { 13, 85, 350 }),
		INFERNAL_AXE(new Item[] { new Item(6643), new Item(6739) }, new Item(12706), new int[] { 11, 85, 350 }),
		PEGASIAN_BOOTS(new Item[] { new Item(6642), new Item(2577) }, new Item(12708), new int[] { 12, 99, 1250 }),
		ETERNAL_BOOTS(new Item[] { new Item(6641), new Item(6920) }, new Item(12712), new int[] { 12, 99, 1250 }),
		PRIMORDIAL_BOOTS(new Item[] { new Item(6640), new Item(11732) }, new Item(12710), new int[] { 12, 99, 1250 }),

		/**
		 * Imbuing rings
		 */
		BERSERKER_RING(new Item[] { new Item(7968), new Item(6737) }, new Item(15220), new int[] { 1, -1, 0 }),
		ARCHERS_RING(new Item[] { new Item(7968), new Item(6733) }, new Item(15019), new int[] { 1, -1, 0 }),
		SEERS_RING(new Item[] { new Item(7968), new Item(6731) }, new Item(15018), new int[] { 1, -1, 0 }),
		WARRIOR_RING(new Item[] { new Item(7968), new Item(6735) }, new Item(15020), new int[] { 1, -1, 0 }),
		RING_OF_WEALTH(new Item[] { new Item(7968), new Item(2572) }, new Item(21110), new int[] { 1, -1, 0 });

		ItemForgeData(Item[] requiredItems, Item product, int[] skillRequirement) {
			this.requiredItems = requiredItems;
			this.product = product;
			this.skillRequirement = skillRequirement;
		}

		private Item[] requiredItems;
		private Item product;
		private int[] skillRequirement;

		public static ItemForgeData getDataForItems(int item1, int item2) {
			for (ItemForgeData shieldData : ItemForgeData.values()) {
				int found = 0;
				for (Item it : shieldData.requiredItems) {
					if (it.getId() == item1 || it.getId() == item2)
						found++;
				}
				if (found >= 2)
					return shieldData;
			}
			return null;
		}
	}
}
