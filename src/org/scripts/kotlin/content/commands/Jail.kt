package org.scripts.kotlin.content.commands

import com.zarador.model.StaffRights
import com.zarador.model.Position
import com.zarador.model.player.command.Command
import com.zarador.util.Misc
import com.zarador.world.World
import com.zarador.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/28/2016.

 * @author Seba
 */
class Jail(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (args == null) {
            player.packetSender.sendMessage("Example usage: ::jail-playername-time-timeunit")
            player.packetSender.sendMessage("Time units are M for minutes, H for hours, D for days.")
        } else if (args.size == 3) {
            val playerName = args[0]

            val victim = World.getPlayerByName(playerName)

            if (victim == null) {
                player.packetSender.sendMessage("The offending player is not currently online.")
                return
            }

            var time = -1
            try {
                time = Integer.parseInt(args[1])
            } catch (e: NumberFormatException) {
                player.packetSender.sendMessage("There was an error parsing your time arguement, try using numbers :).")
                return
            }

            if (time == -1 || time == 0) {
                return
            } else if (time > 60) {
                player.packetSender.sendMessage("Time amount cannot be more than 60.")
                return
            }

            val u = args[2].toLowerCase()

            val unit = u[0]

            if (unit == 'd') {
                if (player.staffRights != StaffRights.OWNER) {
                    player.packetSender.sendMessage("You must be an an owner to use the day parameter.")
                    return
                }
            }

            var calculatedTime = 0

            when (unit) {
                'm' -> calculatedTime = time * minute
                'h' -> calculatedTime = time * hour
                'd' -> calculatedTime = time * day
                else -> {
                    player.packetSender.sendMessage("Time units are M for minutes, H for hours, D for days. Please try again.")
                    return
                }
            }
            player.packetSender.sendMessage("You have sent " + victim.username + " to jail for " + time + " " + unit)
            val cellCords = coordinates[Misc.random(coordinates.size - 1)]
            val position = Position(cellCords[0], cellCords[1], 0)
            victim.moveTo(position)
            victim.playerTimers.jailTicks = calculatedTime
            victim.packetSender.sendMessage("You have been sent to jail for your bad deeds!")
        } else {
            player.packetSender.sendMessage("Example usage: ::jail-playername-time-timeunit")
            player.packetSender.sendMessage("Time units are M for minutes, H for hours, D for days.")
        }
    }

    companion object {

        val coordinates = arrayOf(intArrayOf(1969, 5011), intArrayOf(1969, 5008), intArrayOf(1969, 5005), intArrayOf(1969, 5002), intArrayOf(1969, 4999), intArrayOf(1980, 5011), intArrayOf(1980, 5008), intArrayOf(1980, 5005), intArrayOf(1980, 5002), intArrayOf(1980, 4999))

        val minute = Misc.secondsToTicks(60)

        val hour = minute * 60

        val day = hour * 24
    }
}
