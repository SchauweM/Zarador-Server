package org.scripts.kotlin.content.commands

import com.zarador.GameSettings
import com.zarador.model.StaffRights
import com.zarador.model.player.command.Command
import com.zarador.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/27/2016.

 * @author Seba
 */
class OpenStore(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (!GameSettings.STORE_CONNECTIONS) {
            player.packetSender.sendMessage("The Store is currently turned off, please try again in 30 minutes!")
            return
        }
        player.packetSender.sendString(1, "www.argos.com/store")
        player.packetSender.sendMessage("Attempting to open: www.argos.com/store")
    }
}
