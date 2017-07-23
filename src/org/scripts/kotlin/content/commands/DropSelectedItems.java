package org.scripts.kotlin.content.commands;

import com.zarador.model.StaffRights;
import com.zarador.model.player.command.Command;
import com.zarador.net.packet.impl.DropItemPacketListener;
import com.zarador.world.entity.impl.player.Player;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/28/2016.
 *
 * @author Seba
 */
public class DropSelectedItems extends Command {

    public DropSelectedItems(StaffRights staffRights) {
        super(staffRights);
    }

    @Override
    public void execute(Player player, String[] args, StaffRights privilege) {
        try {
            String[] slots = args[0].split("#");
            boolean[] drop = new boolean[28]; //Tells you if you are set to drop the item
            for (int i = 0; i < slots.length; i++) {
                drop[i] = slots[i].equals("1") ? true : false;
            }

            for (int i = 0; i < drop.length; i++) {
                if (!drop[i]) {
                    continue;
                }
                int itemId = player.getInventory().get(i).getId();
                int slot = i;
                DropItemPacketListener.dropItem(player, itemId, i);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
