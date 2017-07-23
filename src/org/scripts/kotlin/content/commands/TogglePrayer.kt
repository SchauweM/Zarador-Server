package org.scripts.kotlin.content.commands

import com.zarador.GameSettings
import com.zarador.model.StaffRights
import com.zarador.model.Prayerbook
import com.zarador.model.Skill
import com.zarador.model.player.command.Command
import com.zarador.world.content.combat.prayer.CurseHandler
import com.zarador.world.content.combat.prayer.PrayerHandler
import com.zarador.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/27/2016.

 * @author Seba
 */
class TogglePrayer(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (player.skillManager.getMaxLevel(Skill.DEFENCE) < 30) {
            player.packetSender.sendMessage("You need a Defence level of at least 30 to use this altar.")
            return
        }
        if (player.prayerbook == Prayerbook.NORMAL) {
            player.packetSender.sendMessage("You sense a surge of power flow through your body!")
            player.prayerbook = Prayerbook.CURSES
        } else {
            player.packetSender.sendMessage("You sense a surge of purity flow through your body!")
            player.prayerbook = Prayerbook.NORMAL
        }
        player.packetSender.sendTabInterface(GameSettings.PRAYER_TAB, player.prayerbook.interfaceId)
        PrayerHandler.deactivateAll(player)
        CurseHandler.deactivateAll(player)
    }
}
