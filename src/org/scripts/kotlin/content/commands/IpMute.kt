package org.scripts.kotlin.content.commands

import com.zarador.model.StaffRights
import com.zarador.model.options.Option
import com.zarador.model.options.twooption.TwoOption
import com.zarador.model.player.command.Command
import com.zarador.util.Misc
import com.zarador.world.World
import com.zarador.world.content.PlayerPunishment
import com.zarador.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class IpMute(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (args == null) {
            player.packetSender.sendMessage("Example usage: ::ipmute-playername-time-timeunit")
            player.packetSender.sendMessage("Time units are M for minutes, H for hours, D for days.")
        } else {
            val victim = args[0]
            if (args.size == 1) {
                player.optionContainer.display(object : TwoOption("Yes permanently ip mute " + victim, "No, I want to apply a timed ip mute.") {
                    override fun execute(player: Player, option: Option.OptionType) {
                        when (option) {
                            Option.OptionType.OPTION_1_OF_2 -> handleMute(player, victim, -1L)
                            Option.OptionType.OPTION_2_OF_2 -> {
                                player.packetSender.sendMessage("Example usage: ::ipmute-playername-time-timeunit")
                                player.packetSender.sendMessage("Time units are M for minutes, H for hours, D for days.")
                            }
                        }
                    }
                })
            } else {
                var time = 0

                try {
                    time = Integer.parseInt(args[1])
                } catch (e: NumberFormatException) {
                    player.packetSender.sendMessage("There was an error parsing your time argument. Please use numbers")
                }

                val u = args[2].toLowerCase()
                val unit = u[0]

                var calculatedTime: Long = 0

                when (unit) {
                    'm' -> calculatedTime = (time * Misc.MINUTE).toLong()
                    'h' -> calculatedTime = (time * Misc.HOUR).toLong()
                    'd' -> calculatedTime = (time * Misc.DAY).toLong()
                }

                calculatedTime += System.currentTimeMillis()

                handleMute(player, victim, calculatedTime)
            }
        }
    }

    private fun handleMute(player: Player, victim: String, time: Long?) {
        player.packetSender.sendInterfaceRemoval()
        val p = World.getPlayerByName(victim)
        if (p == null) {
            player.packetSender.sendMessage("That player is not online.")
        }
        if (PlayerPunishment.isIpMuted(p!!.hostAddress)) {
            player.packetSender.sendMessage("That player is already ip mutted")
            return
        }
        PlayerPunishment.ipMute(p.hostAddress, time)
        player.packetSender.sendMessage("You have successfully ip muted " + victim)
        p.packetSender.sendMessage("You have been ip muted.")
        p.packetSender.sendMessage("If you feel this was unjustified you can appeal it on the forums.")
    }
}
