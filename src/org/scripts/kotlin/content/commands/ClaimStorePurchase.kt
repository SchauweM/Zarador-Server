package org.scripts.kotlin.content.commands

import com.zarador.GameSettings
import com.zarador.model.Locations
import com.zarador.model.StaffRights
import com.zarador.model.Store
import com.zarador.model.player.command.Command
import com.zarador.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/27/2016.

 * @author Seba
 */
class ClaimStorePurchase(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (!GameSettings.STORE_CONNECTIONS) {
            player.packetSender.sendMessage("The store is currently offline! Try again in 30 minutes.")
            return
        }
        if (player.claimingStoreItems) {
            player.packetSender.sendMessage("You already have a active store claim process going...")
            return
        }
        player.packetSender.sendMessage("Checking for any store purchases...")
        Store.claimItem(player)
    }
}
