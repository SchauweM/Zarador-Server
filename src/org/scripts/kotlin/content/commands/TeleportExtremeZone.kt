package org.scripts.kotlin.content.commands

import com.zarador.model.DonatorRights
import com.zarador.model.Locations
import com.zarador.model.StaffRights
import com.zarador.model.Position
import com.zarador.model.player.command.Command
import com.zarador.util.Misc
import com.zarador.world.content.transportation.TeleportHandler
import com.zarador.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/27/2016.

 * @author Seba
 */
class TeleportExtremeZone(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (player.donatorRights.ordinal < 3) {
            player.packetSender.sendMessage("You must be a Legendary donator in order to use this command.")
            return
        }
        if (player.location === Locations.Location.WILDERNESS && player.wildernessLevel >= 20) {
            player.packetSender.sendMessage("You cannot do this at the moment.")
            return
        }
        var position: Position? = null
        val ran = Misc.getRandom(3)
        when (ran) {
            0 -> position = Position(3363, 9641, 0)
            1 -> position = Position(3364, 9640, 0)
            2 -> position = Position(3363, 9639, 0)
            3 -> position = Position(3362, 9640, 0)
        }
        TeleportHandler.teleportPlayer(player, position, player.spellbook.teleportType)
        player.packetSender.sendMessage("<img=10><col=570057><shad=0> Welcome to the Legendary Donator Zone!")
    }
}
