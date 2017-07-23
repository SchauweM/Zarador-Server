package org.scripts.kotlin.content.commands;

import org.scripts.kotlin.content.dialog.GiveDonatorDialog;

import com.zarador.model.StaffRights;
import com.zarador.model.player.command.Command;
import com.zarador.world.entity.impl.player.Player;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/28/2016.
 *
 * @author Seba
 */
public class GiveDonor extends Command {

    public GiveDonor(StaffRights staffRights) {
        super(staffRights);
    }

    @Override
    public void execute(Player player, String[] args, StaffRights privilege) {
        if(args.length < 1) {
            player.getPacketSender().sendMessage("You must use the command as ::givedonator-name");
            return;
        }
        String name = args[0];
        player.getDialog().sendDialog(new GiveDonatorDialog(player, name));
    }
}
