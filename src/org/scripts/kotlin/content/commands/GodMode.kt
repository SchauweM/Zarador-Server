package org.scripts.kotlin.content.commands

import com.zarador.model.StaffRights
import com.zarador.model.Skill
import com.zarador.model.player.command.Command
import com.zarador.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class GodMode(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        player.skillManager.setCurrentLevel(Skill.ATTACK, 1, true)
        player.skillManager.setCurrentLevel(Skill.STRENGTH, 1, true)
        player.skillManager.setCurrentLevel(Skill.RANGED, 1, true)
        player.skillManager.setCurrentLevel(Skill.DEFENCE, 99999, true)
        player.skillManager.setCurrentLevel(Skill.MAGIC, 1, true)
        player.skillManager.setCurrentLevel(Skill.CONSTITUTION, 99999, true)
        player.skillManager.setCurrentLevel(Skill.PRAYER, 1, true)
    }
}
