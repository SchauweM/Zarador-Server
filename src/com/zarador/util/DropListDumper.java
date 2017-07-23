package com.zarador.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.zarador.model.definitions.ItemDefinition;
import com.zarador.model.definitions.NpcDefinition;
import com.zarador.model.npc.drops.LootItem;
import com.zarador.model.npc.drops.LootSystem;
import com.zarador.model.npc.drops.LootTable;

/**
 * Dumps a list of all drops in Chaos.
 * Used for the Wiki.
 *
 * @Author Jonny
 */
public class DropListDumper {

	/**
	 * Dumps LootSystem into a .txt file.
	 * @LootSystem
	 */
	public static void dump() {
		try {
			File file = new File("lists/drops.txt");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			for (int i = 0; i < NpcDefinition.getDefinitions().length; i++) {
				LootTable table = LootSystem.tables.get(i);
				if(table == null) {
					continue;
				}
				if(NpcDefinition.forId(i) == null) {
					continue;
				}
				write(bw, "Monster: "+NpcDefinition.forId(i).getName()+ "(Level: "+NpcDefinition.forId(i).getCombatLevel()+") (id = "+i+")");
				write(bw, "---------------");
				write(bw, "");
				LootItem[][] tableSorted = table.getSortedLoot();
				for (int i2 = 0; i2 < tableSorted.length; i2++) {
					if(tableSorted[i2].length > 0) {
						if (i2 == 0) {
							write(bw, "Always");
							for (int i3 = 0; i3 < tableSorted[i2].length; i3++) {
								if (tableSorted[i2][i3].getMaximum() != tableSorted[i2][i3].getMinimum()) {
									write(bw, "" + tableSorted[i2][i3].getMinimum() + "-" + tableSorted[i2][i3].getMaximum() + "x " + ItemDefinition.forId(tableSorted[i2][i3].getId()).getName());
								} else {
									write(bw, "1x " + ItemDefinition.forId(tableSorted[i2][i3].getId()).getName());
								}
							}
							bw.newLine();
						} else if (i2 == 1) {
							write(bw, "Common");
							for (int i3 = 0; i3 < tableSorted[i2].length; i3++) {
								System.out.println("TEST: "+tableSorted[i2].length);
								if (tableSorted[i2][i3].getMaximum() != tableSorted[i2][i3].getMinimum()) {
									write(bw, "" + tableSorted[i2][i3].getMinimum() + "-" + tableSorted[i2][i3].getMaximum() + "x " + ItemDefinition.forId(tableSorted[i2][i3].getId()).getName());
								} else {
									write(bw, "1x " + ItemDefinition.forId(tableSorted[i2][i3].getId()).getName());
								}
							}
							bw.newLine();
						} else if (i2 == 2) {
							write(bw, "Uncommon (1/10)");
							for (int i3 = 0; i3 < tableSorted[i2].length; i3++) {
								if (tableSorted[i2][i3].getMaximum() != tableSorted[i2][i3].getMinimum()) {
									write(bw, "" + tableSorted[i2][i3].getMinimum() + "-" + tableSorted[i2][i3].getMaximum() + "x " + ItemDefinition.forId(tableSorted[i2][i3].getId()).getName());
								} else {
									write(bw, "1x " + ItemDefinition.forId(tableSorted[i2][i3].getId()).getName());
								}
							}
							bw.newLine();
						} else if (i2 == 3) {
							write(bw, "Rare (1/" + NpcDefinition.forId(i).getRare() + ")");
							for (int i3 = 0; i3 < tableSorted[i2].length; i3++) {
								if (tableSorted[i2][i3].getMaximum() != tableSorted[i2][i3].getMinimum()) {
									write(bw, "" + tableSorted[i2][i3].getMinimum() + "-" + tableSorted[i2][i3].getMaximum() + "x " + ItemDefinition.forId(tableSorted[i2][i3].getId()).getName()+" (1/" + NpcDefinition.forId(i).getRare() * tableSorted[i2].length + ")");
								} else {
									write(bw, "1x " + ItemDefinition.forId(tableSorted[i2][i3].getId()).getName()+" (1/" + NpcDefinition.forId(i).getRare() * tableSorted[i2].length + ")");
								}
							}
							bw.newLine();
						} else if (i2 == 4) {
							write(bw, "Very Rare (1/" + NpcDefinition.forId(i).getVeryRare() + ")");
							for (int i3 = 0; i3 < tableSorted[i2].length; i3++) {
								if (tableSorted[i2][i3].getMaximum() != tableSorted[i2][i3].getMinimum()) {
									write(bw, "" + tableSorted[i2][i3].getMinimum() + "-" + tableSorted[i2][i3].getMaximum() + "x " + ItemDefinition.forId(tableSorted[i2][i3].getId()).getName()+" (1/" + NpcDefinition.forId(i).getVeryRare() * tableSorted[i2].length + ")");
								} else {
									write(bw, "1x " + ItemDefinition.forId(tableSorted[i2][i3].getId()).getName()+" (1/" + NpcDefinition.forId(i).getVeryRare() * tableSorted[i2].length + ")");
								}
							}
							bw.newLine();
						} else if (i2 == 5) {
							write(bw, "Epic (1/" + NpcDefinition.forId(i).getEpicRare() + ")");
							for (int i3 = 0; i3 < tableSorted[i2].length; i3++) {
								if (tableSorted[i2][i3].getMaximum() != tableSorted[i2][i3].getMinimum()) {
									write(bw, "" + tableSorted[i2][i3].getMinimum() + "-" + tableSorted[i2][i3].getMaximum() + "x " + ItemDefinition.forId(tableSorted[i2][i3].getId()).getName()+" (1/" + NpcDefinition.forId(i).getEpicRare() * tableSorted[i2].length + ")");
								} else {
									write(bw, "1x " + ItemDefinition.forId(tableSorted[i2][i3].getId()).getName()+" (1/" + NpcDefinition.forId(i).getEpicRare() * tableSorted[i2].length + ")");
								}
							}
							bw.newLine();
						}
					}
				}
				write(bw, "");
			}
			bw.close();
			System.out.println("Successfully finished dumping drops into lists/drops.txt");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Writes to a BufferedWriter
	 * @param bw
	 * @param content
	 */
	public static void write(BufferedWriter bw, String content) {
		try {
			bw.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
