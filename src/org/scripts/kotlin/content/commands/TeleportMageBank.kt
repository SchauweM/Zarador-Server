package org.scripts.kotlin.content.commands

import com.zarador.model.Locations
import com.zarador.model.StaffRights
import com.zarador.model.Position
import com.zarador.model.player.command.Command
import com.zarador.world.content.transportation.TeleportHandler
import com.zarador.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/7/2016.

 * @author Seba
 */
class TeleportMageBank(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (player.location === Locations.Location.WILDERNESS && player.wildernessLevel >= 20) {
            player.packetSender.sendMessage("You cannot do this at the moment.")
            return
        }
        val position = Position(2539, 4715, 0)
        TeleportHandler.teleportPlayer(player, position, player.spellbook.teleportType)
        player.packetSender.sendMessage("Teleporting you to the mage bank!")
    }
}
