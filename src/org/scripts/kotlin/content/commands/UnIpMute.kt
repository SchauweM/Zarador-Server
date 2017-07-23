package org.scripts.kotlin.content.commands

import com.zarador.model.StaffRights
import com.zarador.model.player.command.Command
import com.zarador.world.World
import com.zarador.world.content.PlayerPunishment
import com.zarador.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class UnIpMute(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (args == null) {
            player.packetSender.sendMessage("Example usage: ::unipmute-playername")
        } else {
            val p = World.getPlayerByName(args[0])
            if (p == null) {
                player.packetSender.sendMessage("Unable to find that player.")
                return
            }
            if (!PlayerPunishment.isIpMuted(p.hostAddress)) {
                player.packetSender.sendMessage(p.username + " currently does not have an active ban.")
                return
            }
            PlayerPunishment.unIpMute(p.hostAddress)
            player.packetSender.sendMessage("You have successfully un ipmuted " + p.username)
            p.packetSender.sendMessage("You have been un ipmuted.")
        }
    }
}
