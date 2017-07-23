package org.scripts.kotlin.content.commands

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
class GiveRights(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (args == null) {
            player.packetSender.sendMessage("Example usage: ::giverights-playername-rights")
        } else {
            val p = World.getPlayerByName(args[0])
            val rights = args[1].toLowerCase()
            var staffRights = StaffRights.PLAYER
            when (rights) {
                "demote", "derank" -> staffRights = StaffRights.PLAYER

                "ss", "serversupport", "support" -> staffRights = StaffRights.SUPPORT

                "mod", "moderator" -> staffRights = StaffRights.MODERATOR

                "globalmod", "gmod", "global_mod", "globalmoderator" -> staffRights = StaffRights.GLOBAL_MOD

                "youtube", "youtuber" -> staffRights = StaffRights.YOUTUBER

                else -> player.packetSender.sendMessage("We were unable to find that command")
            }

            p!!.staffRights = staffRights
            p.packetSender.sendRights()
            p.packetSender.sendMessage("You have been give " + staffRights.title + " rights.")
            player.packetSender.sendMessage("You have given " + args[0] + " rights: " + staffRights.title)
        }
    }
}
