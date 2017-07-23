package org.scripts.kotlin.content.commands;

import com.zarador.model.StaffRights;
import com.zarador.model.player.command.Command;
import com.zarador.world.entity.impl.player.Player;

public class Announce extends Command {

    public Announce(StaffRights staffRights) {
        super(staffRights);
    }

    @Override
    public void execute(Player player, String[] args, StaffRights privilege) {
        if (args == null) {
            player.getPacketSender().sendMessage("Example usage: ::announce-time");
        } else {
            player.setAnnouncementTime(Integer.parseInt(args[0]));
            player.setInputHandling(new com.zarador.model.input.impl.GlobalAnnouncement());
            player.getPacketSender().sendEnterInputPrompt("Enter a announcement:");
        }
    }
}
