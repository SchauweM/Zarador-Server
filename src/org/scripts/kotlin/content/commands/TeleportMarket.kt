package org.scripts.kotlin.content.commands

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
class TeleportMarket(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (player.location === Locations.Location.WILDERNESS && player.wildernessLevel > 20) {
            player.packetSender.sendMessage("You cannot do this at the moment.")
            return
        }
        val random = Misc.getRandom(3)
        when (random) {
            0 -> TeleportHandler.teleportPlayer(player, Position(3164, 3483, 0), player.spellbook.teleportType)
            1 -> TeleportHandler.teleportPlayer(player, Position(3165, 3483, 0), player.spellbook.teleportType)
            2 -> TeleportHandler.teleportPlayer(player, Position(3164, 3482, 0), player.spellbook.teleportType)
            3 -> TeleportHandler.teleportPlayer(player, Position(3165, 3482, 0), player.spellbook.teleportType)
        }
        player.packetSender.sendMessage("Welcome to the Market!")
    }


}
