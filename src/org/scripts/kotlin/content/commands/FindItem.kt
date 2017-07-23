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
class FindItem(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (args == null) {
            player.packetSender.sendMessage("Example usage: ::find-itemname")
        } else {
            val name = args[0].toLowerCase()
            var found = false
            player.packetSender.sendMessage("Trying to find item id for item name " + name)
            for (i in 0..ItemDefinition.getMaxAmountOfItems() - 1 - 1) {
                if (ItemDefinition.forId(i).getName().toLowerCase().contains(name)) {
                    player.packetSender.sendMessage("Found item with name [" + ItemDefinition.forId(i).getName() + "] - id: " + i)
                    found = true
                }
            }
            if (!found) {
                player.packetSender.sendMessage("No items for the name $name were found.")
            }
        }
    }
}
