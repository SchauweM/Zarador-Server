package com.zarador.util;

import com.zarador.model.Item;
import com.zarador.model.npc.drops.LootSystem;
import com.zarador.model.npc.drops.LootTable;

/**
 * Tests the chances of a drop for a specific npc.
 *
 * @Author Jonny
 */
public class TestDropChance {

	/**
	 * Generates a random formula and spits out the answer.
	 * @LootSystem
	 */
	public static void chance(int npcId) {
		int total = 0;
		LootTable table = LootSystem.tables.get(npcId);
		for (int i = 0; i < 100000; i++) {
			Item roll = LootSystem.rollDrop(null, null, table.getSortedLoot(), npcId, false);
			if(roll == null) {
				continue;
			}
			if(roll.getId() == 13178) {
				total++;
			}
		}
		System.out.println("["+npcId+"] Total generated in epic rarity drop test: "+total);
	}
}
