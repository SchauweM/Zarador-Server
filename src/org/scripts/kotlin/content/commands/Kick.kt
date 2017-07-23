package org.scripts.kotlin.content.commands

import com.zarador.model.Locations
import com.zarador.model.StaffRights
import com.zarador.model.player.command.Command
import com.zarador.world.World
import com.zarador.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class Kick(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (args == null) {
            player.packetSender.sendMessage("Example usage: ::kick-playername")
        } else {
            val kick = World.getPlayerByName(args[0])
            if (kick == null) {
                player.packetSender.sendMessage("We are unable to find that player.")
                return
            }
            if (kick.location === Locations.Location.DUEL_ARENA) {
                player.packetSender.sendMessage("You cannot kick someone in the duel arena")
                return
            }
            if (kick.location === Locations.Location.WILDERNESS && !player.staffRights.isManagement()) {
                player.packetSender.sendMessage("You cannot kick someone who is in the wilderness")
            }
            kick.forceOffline = true
            World.deregister(kick)
            player.packetSender.sendMessage("You have successfully kicked " + kick.username + ".")
        }
    }
}
