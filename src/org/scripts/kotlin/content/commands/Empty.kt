package org.scripts.kotlin.content.commands

import com.zarador.model.StaffRights
import com.zarador.model.options.Option
import com.zarador.model.options.twooption.TwoOption
import com.zarador.model.player.command.Command
import com.zarador.model.player.dialog.Dialog
import com.zarador.model.player.dialog.DialogHandler
import com.zarador.model.player.dialog.DialogMessage
import com.zarador.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/7/2016.

 * @author Seba
 */
class Empty(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        player.dialog.sendDialog(EmptyDialog(player))
    }

    private inner class EmptyDialog(player: Player) : Dialog(player) {

        init {
            setEndState(1)
            player.npcClickId = 945
        }

        override fun getMessage(): DialogMessage? {
            when (state) {
                0 -> return Dialog.createNpc(DialogHandler.CALM, "Are you sure you want to delete all the items in your inventory? They will not be dropped to the ground rather permanently deleted.")
                1 -> return Dialog.createOption(object : TwoOption("Yes, delete all my inventory items", "No, I have changed my mind.") {
                    override fun execute(player: Player, option: Option.OptionType) {
                        when (option) {
                            Option.OptionType.OPTION_1_OF_2 -> for (item in player.inventory.items) {
                                player.inventory.delete(item)
                                player.packetSender.sendInterfaceRemoval()
                            }
                            Option.OptionType.OPTION_2_OF_2 -> player.packetSender.sendInterfaceRemoval()
                        }
                    }
                })
            }
            return null
        }
    }
}
