package com.zarador.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.zarador.model.definitions.ItemDefinition;

/**
 * Dumps a list of all items in Chaos.
 * Used for the DataSuite.
 *
 * @Author Jonny
 */
public class ItemListDumper {

	/**
	 * Dumps ItemDefinitions into a .txt file.
	 * @ItemDefinitions
	 */
	public static void dump() {
		try {
			File file = new File("lists/items.txt");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			for (int i = 0; i < ItemDefinition.getDefinitions().length; i++) {
				String content = i+"\t"+ItemDefinition.forId(i).getName();
				bw.write(content);
				bw.newLine();
			}
			bw.close();
			System.out.println("Successfully dumped "+ItemDefinition.getDefinitions().length+" items into lists/items.txt");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
