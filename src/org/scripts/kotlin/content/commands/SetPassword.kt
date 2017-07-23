package org.scripts.kotlin.content.commands

import com.zarador.model.StaffRights
import com.zarador.model.input.impl.SetPassword
import com.zarador.model.player.command.Command
import com.zarador.world.World
import com.zarador.world.entity.impl.player.Player
import com.zarador.world.entity.impl.player.PlayerLoading
import com.zarador.world.entity.impl.player.PlayerSaving

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class SetPassword(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if(args == null) {
            player.packetSender.sendMessage("Example usage: ::setpass-playername")
        } else {
            val victimUsername = args[0]
            if(PlayerLoading.accountExists(victimUsername)) {
                // account exists
                val other = World.getPlayerByName(victimUsername)
                if (other == null) {
                    player.changingPasswordOf = victimUsername
                    player.inputHandling = SetPassword()
                    player.packetSender.sendEnterInputPrompt("(OFFLINE) Enter a new password for " + victimUsername)
                } else {
                    player.changingPasswordOf = victimUsername
                    player.inputHandling = SetPassword()
                    player.packetSender.sendEnterInputPrompt("(ONLINE) Enter a new password for " + victimUsername)
                }
            } else {
                player.packetSender.sendMessage("Player $victimUsername does not exist.")
            }
        }
    }
}
