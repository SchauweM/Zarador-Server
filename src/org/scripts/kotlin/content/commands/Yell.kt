package org.scripts.kotlin.content.commands

import com.zarador.GameSettings
import com.zarador.model.DonatorRights
import com.zarador.model.StaffRights
import com.zarador.model.player.command.Command
import com.zarador.world.World
import com.zarador.world.content.PlayerPunishment
import com.zarador.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/27/2016.

 * @author Seba
 */
class Yell(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (player.donatorRights == DonatorRights.PLAYER) {
            player.packetSender.sendMessage("You must be a donator in order to use this command.")
            return
        }
        var timer = 0;
        when (player.donatorRights) {
            DonatorRights.PREMIUM -> timer = 20000
            DonatorRights.EXTREME -> timer = 10000
            DonatorRights.LEGENDARY -> timer = 0
            DonatorRights.UBER -> timer = 0
            DonatorRights.PLATINUM -> timer = 0
        }
        if(timer != 0 && !player.yellTimer.elapsed(timer.toLong())) {
            player.packetSender.sendMessage("You must wait "+timer / 1000+" seconds in between yell messages.")
            return
        }
        if (args == null) {
            player.packetSender.sendMessage("Please use the command as ::yell-message no spaces use -")
            return
        }
        if (!World.isGlobalYell()) {
            player.packetSender.sendMessage("An admin has temporarily disabled the global yell channel.")
            return
        }

        if (PlayerPunishment.isMuted(player.username) || PlayerPunishment.isIpMuted(player.hostAddress)) {
            player.packetSender.sendMessage("You are muted and cannot yell.")
            return
        }

        if (PlayerPunishment.isYellMuted(player.username)) {
            player.packetSender.sendMessage("You are yell muted and cannot yell.")
            return
        }

        if (player.isYellMute) {
            player.packetSender.sendMessage("You are muted from yelling and cannot yell.")
            return
        }
        if (!GameSettings.YELL_STATUS) {
            player.packetSender.sendMessage("Yell is currently turned off, please try again in 30 minutes!")
            return
        }
        val yellmessage = args[0]
        if (yellmessage.contains("<img=") || yellmessage.contains("<col=") || yellmessage.contains("<shad=")) {
            player.packetSender.sendMessage("You are not allowed to put these symbols in your yell message.")
            return
        }

        val builder = StringBuilder(1024)
        val isStaff = player.staffRights.ordinal > 0

        /**
         * Time to build our message
         */
        run {
            if(!player.staffRights.isStaff) {
                builder.append("<img=")
                builder.append(player.crown)
                builder.append("> ")
            }

            if (!isStaff) {
                builder.append("<col=0>[")
                builder.append(player.donatorRights.color)

                if (player.donatorRights.shad != null) {
                    builder.append(player.donatorRights.shad)
                }
            } else {
                builder.append("<col=0>[")
                builder.append(player.staffRights.color)

                if (player.staffRights.shad != null) {
                    builder.append(player.staffRights.shad)
                }
            }

            if (isStaff) {
                builder.append("<img=")
                builder.append(player.crown)
                builder.append(">")
            }

            if (player.yellTag != "invalid_yell_tag_set") {
                builder.append(player.yellTag)
            } else {
                if(player.staffRights.isStaff) {
                    builder.append(player.staffRights.title)
                } else {
                    builder.append(player.donatorRights.title)
                }
            }

            if (isStaff) {
                builder.append("<img=")
                builder.append(player.crown)
                builder.append(">")
            }
            if(player.staffRights.isStaff) {
                if (player.staffRights.shad != null) {
                    builder.append("</shad>")
                }
            } else {
                if (player.donatorRights.shad != null) {
                    builder.append("</shad>")
                }
            }
            builder.append("<col=0>] ")
            builder.append(player.username)
            builder.append(": ")
            builder.append(yellmessage)
        }
        player.yellTimer.reset();
        World.sendYell(builder.toString(), player)
    }
}
