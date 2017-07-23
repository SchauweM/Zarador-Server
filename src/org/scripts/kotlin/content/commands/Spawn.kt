package org.scripts.kotlin.content.commands

import com.zarador.model.StaffRights
import com.zarador.model.definitions.ItemDefinition
import com.zarador.model.player.command.Command
import com.zarador.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class Spawn(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (args == null) {
            player.packetSender.sendMessage("Example useage: ::spawn-itemname-amount")
        } else {
            val name = args[0].toLowerCase()
            var amount = 0

            try {
                amount = Integer.parseInt(args[1])
            } catch (e: NumberFormatException) {
                player.packetSender.sendMessage("Error parsing the amount argument, try using numbers")
            }

            var found = false

            for (i in 0..ItemDefinition.getMaxAmountOfItems() - 1 - 1) {
                if (ItemDefinition.forId(i).getName().toLowerCase().contains(name)) {
                    player.inventory.add(i, amount)
                    player.packetSender.sendMessage("You have spawned [" + ItemDefinition.forId(i).getName() + "] - id: " + i)
                    found = true
                    break
                }
            }
            if (!found) {
                player.packetSender.sendMessage("There was no item found for " + name)
            }
            player.packetSender.sendItemOnInterface(47052, 11694, 1)
        }
    }
}
