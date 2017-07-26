package org.scripts.kotlin.content.commands;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.Map;

import org.scripts.kotlin.content.commands.writenpc.SpawnList;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.zarador.model.StaffRights;
import com.zarador.model.player.command.Command;
import com.zarador.world.entity.impl.player.Player;

public class LoadNPCSpawns extends Command {

    public LoadNPCSpawns(StaffRights staffRights) {
        super(staffRights);
    }

    @Override
    public void execute(Player player, String[] args, StaffRights privilege) {
    	
    	Map<Integer, SpawnList> spawnList = null;
    	
    	Gson gson = new Gson();
    	
    	Type stringStringMap = new TypeToken<Map<Integer, SpawnList>>(){}.getType();
    	
    	
    	
    	System.out.println(spawnList.size());

    }
}
