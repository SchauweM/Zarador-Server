package org.scripts.kotlin.content.commands

import com.zarador.model.StaffRights
import com.zarador.model.player.command.Command
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
class Unban(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (args == null) {
            player.packetSender.sendMessage("Exmaple usage: ::unban-playername")
        } else {
            val victim = args[0]
            if(PlayerLoading.accountExists(victim)) {
                // account exists
                if (!PlayerPunishment.isPlayerBanned(victim)) {
                    player.packetSender.sendMessage("Player $victim does not have an active ban!")
                    return
                }
                PlayerPunishment.unBan(victim)
                player.packetSender.sendMessage("Player $victim was successfully unbanned!")
            } else {
                player.packetSender.sendMessage("Player $victim does not exist.")
            }
        }
    }
}
