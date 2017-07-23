package org.scripts.kotlin.content.commands

import com.google.common.collect.Sets
import com.zarador.model.StaffRights
import com.zarador.model.player.command.Command
import com.zarador.world.World
import com.zarador.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/27/2016.

 * @author Seba
 */
class PromoteWiki(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player?, args: Array<String>?, privilege: StaffRights) {
        if (player!!.staffRights != StaffRights.WIKI_MANAGER && !player!!.staffRights.isManagement) {
            player.packetSender.sendMessage("You do not have sufficient privileges to use this command.")
            return
        }
        if (args == null) {
            player!!.packetSender.sendMessage("Please use the command as ::promote-playername")
            return
        }
        val promote = World.getPlayerByName(args[0])
        if (promote == null) {
            player!!.packetSender.sendMessage("Either that player is not online or you typed the name incorrectly.")
            return
        }

        if (PROMOTABLE.contains(promote.staffRights)) {
            promote.staffRights = StaffRights.WIKI_EDITOR
            promote.packetSender.sendRights()
            promote.packetSender.sendMessage("Congratulations, " + player!!.username + " has promoted you to a Wiki Editor!")
            player.packetSender.sendMessage("You have promoted " + promote.username + " to a Wiki Editor!")
        } else {
            player!!.packetSender.sendMessage("This player's current rank cannot be promoted")
        }
    }

    companion object {

        private val PROMOTABLE = Sets.immutableEnumSet(StaffRights.PLAYER)
    }
}
