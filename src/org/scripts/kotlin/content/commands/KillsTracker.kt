package org.scripts.kotlin.content.commands

import com.zarador.model.StaffRights
import com.zarador.model.player.command.Command
import com.zarador.world.content.KillsTracker
import com.zarador.world.entity.impl.player.Player

class KillsTracker(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        KillsTracker.open(player, 0)
    }
}
