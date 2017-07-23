package com.zarador.world.content.skill.impl.farming;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zarador.world.content.skill.impl.farming.patch.Patch;
import com.zarador.world.content.skill.impl.farming.patch.PatchType;
import com.zarador.world.entity.impl.player.Player;

/**
 *
 * @author relex lawl
 */
public final class PatchSaving {

	private static final File DIRECTORY = new File("./data/objects/farming");
	
	public static void save(Player player) {
		if (!DIRECTORY.exists()
				&& !DIRECTORY.mkdirs()) {
			return;
		}
		if (player.getPatches().size() <= 0
				&& !new File(DIRECTORY.getAbsolutePath() + "/" + player.getUsername() + ".json").exists())
			return;
		final Gson gson = new GsonBuilder().setPrettyPrinting().create();
		final File file = new File(DIRECTORY.getAbsolutePath() + "/" + player.getUsername() + ".json");
		
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			
			gson.toJson(player.getPatches(), writer);
			
			writer.close();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
	public static void load(Player player) {
		if (!new File(DIRECTORY.getAbsolutePath() + "/" + player.getUsername() + ".json").exists())
			return;
		final Gson gson = new GsonBuilder().setPrettyPrinting().create();		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(DIRECTORY.getAbsolutePath() + "/" + player.getUsername() + ".json"));
			
			final Map<PatchType, Patch> patches = gson.fromJson(reader, new TypeToken<Map<PatchType, Patch>>(){}.getType());
			
			for (Patch patch : patches.values()) {
				if (patch.getSeedTypeName() != null)
					patch.setSeedType(patch.getSeedClass().getInstance(patch.getSeedTypeName()));
			}
			
			player.setPatches(patches);
			
			reader.close();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
}
