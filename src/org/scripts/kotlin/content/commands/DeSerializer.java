package org.scripts.kotlin.content.commands;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.zarador.ect.dropwriting.DropManager;
import com.zarador.model.StaffRights;
import com.zarador.model.player.command.Command;
import com.zarador.world.entity.impl.player.Player;

public class DeSerializer extends Command {
	
    public DeSerializer(StaffRights staffRights) {
        super(staffRights);
    }

    @Override
    public void execute(Player player, String[] args, StaffRights privilege) {

    }
}
