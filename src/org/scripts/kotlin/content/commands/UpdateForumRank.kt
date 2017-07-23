package org.scripts.kotlin.content.commands

import com.zarador.GameSettings
import com.zarador.model.StaffRights
import com.zarador.model.player.command.Command
import com.zarador.util.ForumDatabase
import com.zarador.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/27/2016.

 * @author Seba
 */
class UpdateForumRank(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (player.forumConnections > 0) {
            player.packetSender.sendMessage("You have just used this command, please relog and try again!")
            return
        }
        if (!GameSettings.FORUM_DATABASE_CONNECTIONS) {
            player.packetSender.sendMessage("This is currently disabled, try again in 30 minutes!")
            return
        }
        if (player.staffRights.ordinal == 0) {
            try {
                player.addForumConnections(60)
                ForumDatabase.forumRankUpdate(player)
                player.packetSender.sendMessage("Your in-game rank has been added to your forum account.")
            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else {
            player.packetSender.sendMessage("Staff members are not allowed to use this command.")
        }
    }
}
