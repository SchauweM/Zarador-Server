package org.scripts.kotlin.content.commands

import com.zarador.model.StaffRights
import com.zarador.model.Position
import com.zarador.model.player.command.Command
import com.zarador.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class Teleport(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (args == null) {
            player.packetSender.sendMessage("Exmaple usage: ::tele-x-y-z")
        } else {
            var x = 0
            var y = 0
            var z = 0
            try {
                x = Integer.parseInt(args[0])
                y = Integer.parseInt(args[1])
            } catch (e: NumberFormatException) {
                player.packetSender.sendMessage("Error parsing coords.  Try using numbers.")
            }

            if (args.size == 3) {
                try {
                    z = Integer.parseInt(args[2])
                } catch (e: NumberFormatException) {
                    player.packetSender.sendMessage("Error parsing coords.  Try using numbers.")
                }
            }
            val position = Position(x, y, z)
            player.moveTo(position)
            player.packetSender.sendMessage("Teleport to " + position.toString())
        }
    }
}
