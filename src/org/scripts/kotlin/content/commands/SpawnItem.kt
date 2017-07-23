package org.scripts.kotlin.content.commands

import com.zarador.model.Item
import com.zarador.model.StaffRights
import com.zarador.model.player.command.Command
import com.zarador.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class SpawnItem(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (args == null) {
            player.packetSender.sendMessage("Example usage: ::item-itemid-amount")
        } else if (args.size == 2) {
            val id = Integer.parseInt(args[0])
            var amount = Integer.parseInt(args[1].trim { it <= ' ' }.toLowerCase().replace("k".toRegex(), "000").replace("m".toRegex(), "000000").replace("b".toRegex(), "000000000"))
            if (amount > 2147483647) {
                amount = 2147000000
            }
            val item = Item(id, amount)
            player.inventory.add(item, true)

            player.packetSender.sendItemOnInterface(47052, 11694, 1)
        }
    }
}
