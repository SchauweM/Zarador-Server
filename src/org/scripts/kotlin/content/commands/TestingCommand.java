package org.scripts.kotlin.content.commands;

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
public class TestingCommand extends Command {

    public TestingCommand(StaffRights staffRights) {
        super(staffRights);
    }

    @Override
    public void execute(Player player, String[] args, StaffRights privilege) {
        /**
         * Lets just send the person the message they pass threw so example ::test-send this message back to the player.
         */
//        DropManager.addDrop(1665, 11694, 1, 1, Drop.RARITY.EPIC, Drop.CONDITION.NONE);
//        DropManager.addCharm(1665, DropTable.CHARM.BLUE, 1, 20);
//        DropManager.saveDrops();
        if (args == null) {
            player.getPacketSender().sendMessage("Example usage: ::test-id");
        } else {
            int id = 0;
            try {
                id = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                player.getPacketSender().sendMessage("Error parsing the int value. Use numbers");
            }

            player.getPacketSender().sendItemOnInterface(id, 4151, 1);
            player.getPacketSender().sendMessage("Sending object: " + id + " Direction: ");
        }
    }
}
