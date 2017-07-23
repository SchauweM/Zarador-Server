package org.scripts.kotlin.content.commands

import com.zarador.model.Flag
import com.zarador.model.StaffRights
import com.zarador.model.player.command.Command
import com.zarador.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class PlayerToNPC(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if(player.staffRights == StaffRights.PLAYER && player.username != "Multak" || player.staffRights == StaffRights.SUPPORT) {
            player.packetSender.sendMessage("Only support+ can use this command.");
            return;
        }
        if (args == null) {
            player.packetSender.sendMessage("Example usage: ::playnpc-id")
        } else {
            var id = 0
            try {
                id = Integer.parseInt(args[0])
            } catch (e: NumberFormatException) {
                player.packetSender.sendMessage("Error parsing the int value. Use numbers")
            }

            player.npcTransformationId = id
            player.packetSender.sendMessage("Transforming to npc: " + id)
            player.updateFlag.flag(Flag.APPEARANCE)
        }
    }
}
