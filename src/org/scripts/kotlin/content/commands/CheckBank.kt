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
class CheckBank(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (args == null) {
            player.packetSender.sendMessage("Example usage: ::checkbank-playername")
        } else {
            val p = World.getPlayerByName(args[0])
            if (p == null) {
                player.packetSender.sendMessage("That player is not online")
                return
            }

            for (b in player.banks) {
                b.resetItems()
            }
            for (i in 0..p.banks.size - 1) {
                for (it in p.getBank(i).items) {
                    if (it != null) {
                        player.getBank(i).add(it, false)
                    }
                }
            }
            player.getBank(0).open()
        }
    }
}
