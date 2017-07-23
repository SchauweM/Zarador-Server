package org.scripts.kotlin.content.commands

import com.zarador.model.StaffRights
import com.zarador.model.player.command.Command
import com.zarador.world.World
import com.zarador.world.content.PlayerPunishment
import com.zarador.world.entity.impl.player.Player
import com.zarador.world.entity.impl.player.PlayerLoading
import com.zarador.world.entity.impl.player.PlayerSaving

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class UnVoteBan(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (args == null) {
            player.packetSender.sendMessage("Example usage: ::unbanvote-playername")
        } else {
            val victim = args[0]
            if(PlayerLoading.accountExists(victim)) {
                if (!PlayerPunishment.isVoteBanned(victim)) {
                    player.packetSender.sendMessage("That player is currently not vote banned.")
                    return
                }
                PlayerPunishment.unVoteBan(victim)
                val p = World.getPlayerByName(victim)
                if (p != null) {
                    p.packetSender.sendMessage("You have been un vote banned.")
                }
                player.packetSender.sendMessage("You have successfully un vote banned " + victim)
            } else {
                player.packetSender.sendMessage("That account does not exist")
            }
        }
    }
}
