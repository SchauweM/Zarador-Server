package org.scripts.kotlin.content.commands

import com.zarador.model.StaffRights
import com.zarador.model.player.command.Command
import com.zarador.world.content.PlayerPunishment
import com.zarador.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class UnYellMute(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (args == null) {
            player.packetSender.sendMessage("Example: ::unyellmute-playername")
        } else {
            if (!PlayerPunishment.isYellMuted(args[0])) {
                player.packetSender.sendMessage(args[0] + " is currently not yell muted.")
                return
            }
            PlayerPunishment.unYellMute(args[0])
            player.packetSender.sendMessage(args[0] + " has been successfully un-yell-muted.")
        }
    }
}
