package org.scripts.kotlin.content.commands

import com.zarador.GameSettings
import com.zarador.model.Locations
import com.zarador.model.MagicSpellbook
import com.zarador.model.StaffRights
import com.zarador.model.player.command.Command
import com.zarador.world.content.achievements.Achievements
import com.zarador.world.content.combat.magic.Autocasting
import com.zarador.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/27/2016.

 * @author Seba
 */
class EnableLunar(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (player.location != null && player.location === Locations.Location.WILDERNESS) {
            player.packetSender.sendMessage("You cannot do this at the moment.")
            return
        }
        player.spellbook = MagicSpellbook.LUNAR
        player.packetSender.sendTabInterface(GameSettings.MAGIC_TAB, player.spellbook.interfaceId).sendMessage("Your magic spellbook is changed to lunars..")
        Autocasting.resetAutocast(player, true)
        Achievements.finishAchievement(player, Achievements.AchievementData.SWITCH_SPELLBOOK)
    }
}
