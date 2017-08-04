package org.scripts.kotlin.content.commands

import com.zarador.model.Flag
import com.zarador.model.StaffRights
import com.zarador.model.Skill
import com.zarador.model.player.command.Command
import com.zarador.world.content.skill.SkillManager
import com.zarador.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class ResetSkills(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {

    }
}
