package org.scripts.kotlin.content.commands

import com.zarador.model.StaffRights
import com.zarador.model.options.Option
import com.zarador.model.options.twooption.TwoOption
import com.zarador.model.player.command.Command
import com.zarador.util.Misc
import com.zarador.world.World
import com.zarador.world.content.PlayerPunishment
import com.zarador.world.entity.impl.player.Player
import com.zarador.world.entity.impl.player.PlayerLoading
import com.zarador.world.entity.impl.player.PlayerSaving

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class Mute(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (args == null) {
            player.packetSender.sendMessage("Example usage: ::mute-playername-time-timeunit")
            player.packetSender.sendMessage("Time units are M for minutes, H for hours, D for days.")
            return
        }
        val victim = args[0]
        if (args.size == 1) {
            player.optionContainer.display(object : TwoOption("Yes, permanent mute " + victim, "No, I want to time mute this player.") {
                override fun execute(player: Player, option: Option.OptionType) {
                    if (option == Option.OptionType.OPTION_1_OF_2) {
                        mutePlayer(player, victim, -1)
                    } else if (option == Option.OptionType.OPTION_2_OF_2) {
                        player.packetSender.sendMessage("Example usage: ::mute-playername-time-timeunit")
                        player.packetSender.sendMessage("Time units are M for minutes, H for hours, D for days.")
                    }
                }
            })
        } else {
            var time = -1
            try {
                time = Integer.parseInt(args[1])
            } catch (e: NumberFormatException) {
                player.packetSender.sendMessage("There was an error parsing your time argument. Try using numbers!")
            }

            val u = args[2].toLowerCase()
            val unit = u[0]

            var calculatedTime = 0

            when (unit) {
                'm' -> calculatedTime = time * Misc.MINUTE
                'h' -> calculatedTime = time * Misc.HOUR
                'd' -> calculatedTime = time * Misc.DAY
            }

            calculatedTime += System.currentTimeMillis().toInt()

            mutePlayer(player, victim, calculatedTime.toLong())
        }
    }

    /**
     * Process our mute
     */
    private fun mutePlayer(player: Player, victim: String, time: Long) {
        player.packetSender.sendInterfaceRemoval()
        val v = World.getPlayerByName(victim)
        if (v == null) {
            if(PlayerLoading.accountExists(victim)) {
                if (PlayerPunishment.isMuted(victim)) {
                    player.packetSender.sendMessage("Player $victim already has an active mute.")
                    return
                }
                PlayerPunishment.mute(victim, time)
                player.packetSender.sendMessage(victim + " was successfully muted.")
            } else {
                player.packetSender.sendMessage("Player $victim does not exist.")
            }
        } else {
            if (PlayerPunishment.isMuted(victim)) {
                player.packetSender.sendMessage("Player $victim already has an active mute.")
                return
            }
            PlayerPunishment.mute(victim, time)
            player.packetSender.sendMessage(victim + " was successfully muted.")
            v.packetSender.sendMessage("You have been muted! If you feel this is unjustified you can appeal on the forums")
        }
    }
}
