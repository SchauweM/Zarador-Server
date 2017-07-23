package org.scripts.kotlin.core.login

import com.zarador.world.entity.impl.player.Player;
import com.zarador.GameSettings
import com.zarador.util.Misc
import com.zarador.world.content.wells.WellOfGoodness
import java.util.concurrent.TimeUnit

/**
 * Created by Dave on 01/07/2016.
 */

class LoginMessageParser {
    companion object LoginMessageParser {
        fun sendLogin(player: Player) {
            if (GameSettings.DOUBLE_EXP) {
                player.getPacketSender().sendMessage(
                        "@bla@Welcome to Argos! We're currently in Double EXP mode! (@red@X2.0@bla@)")
            } else {
                player.getPacketSender().sendMessage(
                        "@bla@Welcome to Argos! We're currently in Normal EXP mode! (@red@X1.0@bla@)")
            }
            if (player.isInvisible) {
                player.getPacketSender().sendMessage(
                        "@red@You are currently invisible...")
            }
            if (Misc.isSaturday()) {
                player.getPacketSender().sendMessage(
                        "<img=4> <col=008FB2>There is currently a double points boost for pest control.")
            }
           // player.getPacketSender().sendMessage(
                   // "<icon=0><col=EE6800><shad=0>Type ::thread-275 to view the halloween event!")
            /*
            val days = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - player.getLastLogin())
            if (player.getLastIpAddress() != null && player.showIpAddress()) {
                if (days > 0) {
                    player.getPacketSender().sendMessage("You last logged in @blu@" + days + "@bla@ "
                            + (if (days > 1) "days" else "day") + " ago from @blu@" + player.getLastIpAddress())
                } else {
                    player.getPacketSender().sendMessage("You last logged in earlier today from @blu@" + player.getLastIpAddress())
                }
            } else if ((player.getLastIpAddress() != null && !player.showIpAddress())) {
                if (days > 0) {
                    player.getPacketSender().sendMessage("You last logged in @blu@" + days + "@bla@ "
                            + (if (days > 1) "days" else "day") + " ago.")
                } else {
                    player.getPacketSender().sendMessage("You last logged in earlier today.")
                }
            }
            */
            if (player.experienceLocked())
                player.getPacketSender().sendMessage("@red@Warning: your experience is currently locked.")

            if (GameSettings.DOUBLE_POINTS) {
                player.getPacketSender().sendMessage(
                        "<img=4> <col=008FB2>argos currently has a double points event going on, make sure to use it!")
            }

            if (GameSettings.DOUBLE_VOTE_TOKENS) {
                player.getPacketSender().sendMessage(
                        "<img=4> <col=008FB2>Argos currently has a double vote rewards event going on, make sure to use it!")
            }

            if (GameSettings.TRIPLE_VOTE_TOKENS) {
                player.getPacketSender().sendMessage(
                        "<img=4> <col=008FB2>Argos currently has a triple vote rewards event going on, make sure to use it!")
            }

            if (WellOfGoodness.isActive("exp")) {
                player.getPacketSender().sendMessage(
                        "<img=4><col=008FB2> The Well of Exp is granting " + WellOfGoodness.BONUSEXPRATE + "% bonus experience for another "
                                + WellOfGoodness.getMinutesRemaining("exp") + " minutes.")
            }

            if (WellOfGoodness.isActive("drops")) {
                player.getPacketSender().sendMessage(
                        "<img=4><col=008FB2> The Well of Wealth is granting " + WellOfGoodness.BONUSDROPPERCENT + "% higher rolls on drops for another "
                                + WellOfGoodness.getMinutesRemaining("drops") + " minutes.")
            }

            if (WellOfGoodness.isActive("pkp")) {
                player.getPacketSender().sendMessage(
                        "<img=4><col=008FB2> The Well of Execution is granting " + WellOfGoodness.BONUSPKP + "x pk points for another "
                                + WellOfGoodness.getMinutesRemaining("pkp") + " minutes.")
            }

        }
    }
}