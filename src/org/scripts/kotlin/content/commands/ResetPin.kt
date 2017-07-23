package org.scripts.kotlin.content.commands

import com.zarador.model.StaffRights
import com.zarador.model.player.command.Command
import com.zarador.world.World
import com.zarador.world.content.AccountTools
import com.zarador.world.entity.impl.player.Player
import com.zarador.world.entity.impl.player.PlayerLoading
import com.zarador.world.entity.impl.player.PlayerSaving

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class ResetPin(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (args == null) {
            player.packetSender.sendMessage("Example usage: ::resetpin-playername")
        } else {
            val victimUsername = args[0]
            if(PlayerLoading.accountExists(victimUsername)) {
                // account exists
                val other = World.getPlayerByName(victimUsername)
                if (other == null) {
                    AccountTools.resetPin(player, victimUsername, Player(null))
                } else {
                    if (other.bankPinAttributes.hasBankPin()) {
                        for (i in 0..other.bankPinAttributes.bankPin.size - 1) {
                            other.bankPinAttributes.bankPin[i] = 0
                            other.bankPinAttributes.enteredBankPin[i] = 0
                        }
                        other.bankPinAttributes.setHasBankPin(false)
                        player.packetSender.sendMessage("The player $victimUsername's account pin has been reset.")
                        World.deregister(other)
                    } else {
                        player.packetSender.sendMessage("The player $victimUsername currently does not have an account pin.")
                    }
                }
            } else {
                player.packetSender.sendMessage("Player $victimUsername does not exist.")
            }
        }
    }
}
