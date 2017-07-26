package org.scripts.kotlin.content.commands;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

import org.scripts.kotlin.content.commands.writenpc.SpawnList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zarador.model.StaffRights;
import com.zarador.model.player.command.Command;
import com.zarador.world.entity.impl.player.Player;

public class DumpNPCSpawns extends Command {

    public DumpNPCSpawns(StaffRights staffRights) {
        super(staffRights);
    }

    File file = new File("./data/def/npcSpawns.dat");
    
    @Override
    public void execute(Player player, String[] args, StaffRights privilege) {
    	if (SpawnList.spawnList == null) {
            SpawnList.spawnList = new HashMap<>();
            SpawnList.deSerialize(file);
        }
    	
    	Gson gson = new GsonBuilder().setPrettyPrinting().create();
    	Writer writer;
		try {
			writer = new FileWriter("NpcSpawns.json");
			gson.toJson(SpawnList.spawnList, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
