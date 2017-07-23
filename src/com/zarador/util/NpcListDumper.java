package com.zarador.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.zarador.model.definitions.NpcDefinition;

/**
 * Dumps a list of all npcs in Chaos.
 * Used for the DataSuite.
 *
 * @Author Jonny
 */
public class NpcListDumper {

    /**
     * Dumps NpcDefinition into a .txt file.
	 * @NpcDefinition
	 */
	public static void dump() {
		try {
			File file = new File("lists/npcs.txt");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			String content = "invalid";
			for (int i = 0; i < NpcDefinition.getDefinitions().length; i++) {
				if(NpcDefinition.forId(i) != null) {
					content = i + "\t" + NpcDefinition.forId(i).getName();
				} else {
					content = "invalid";
				}
				bw.write(content);
				bw.newLine();
			}
			bw.close();
			System.out.println("Successfully dumped "+NpcDefinition.getDefinitions().length+" items into lists/npcs.txt");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
