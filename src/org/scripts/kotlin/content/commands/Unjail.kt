package org.scripts.kotlin.content.commands

import com.zarador.model.StaffRights
import com.zarador.model.Position
import com.zarador.model.player.command.Command
import com.zarador.world.World
import com.zarador.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/28/2016.

 * @author Seba
 */
class Unjail(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (args == null) {
            player.packetSender.sendMessage("Please use the command like ::unjail-player")
            return
        }
        val player1 = World.getPlayerByName(args[0])
        if (player1 == null) {
            player.packetSender.sendMessage("Either the player is offline or you typed in a wrong playername.")
        }
        player1!!.playerTimers.jailTicks = -1
        player1.forceChat("Im free!!! I'm finally out of jail... Hooray!")
        player1.moveTo(Position(3087, 3502, 0))
        player.packetSender.sendMessage("You have successfully unjailed " + player1.username)
    }
}
