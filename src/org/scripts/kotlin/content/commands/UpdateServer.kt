package org.scripts.kotlin.content.commands

import com.zarador.GameServer
import com.zarador.engine.task.Task
import com.zarador.engine.task.TaskManager
import com.zarador.model.StaffRights
import com.zarador.model.player.command.Command
import com.zarador.util.FilterExecutable
import com.zarador.world.World
import com.zarador.world.content.wells.WellOfGoodness
import com.zarador.world.content.clan.ClanChatManager
import com.zarador.world.content.lottery.LotterySaving
import com.zarador.world.entity.impl.player.Player
import com.zarador.world.content.pos.PlayerOwnedShops

import java.io.IOException

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class UpdateServer(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (args == null) {
            player.packetSender.sendMessage("Example usage: ::update-time")
        } else {
            var t = 0
            try {
                t = Integer.parseInt(args[0])
            } catch (e: NumberFormatException) {
                player.packetSender.sendMessage("Error parsing your time argument. Try to use numbers.")
            }

            val time = t
            if (time > 0) {
                GameServer.setUpdating(true)
                World.executeAll(object : FilterExecutable<Player>() {
                    override fun execute(player: Player) {
                        player.packetSender.sendSystemUpdate(time)
                    }
                })
                TaskManager.submit(object : Task(time) {
                    override fun execute() {
                        World.executeAll(object : FilterExecutable<Player>() {
                            override fun execute(player: Player) {
                                World.deregister(player)
                            }
                        })
                        LotterySaving.save()
                        PlayerOwnedShops.save()
                        ClanChatManager.save()
                        WellOfGoodness.save()
                        GameServer.getLogger().info("Update task finished!")
                        stop()
                    }
                })
            }
        }
    }
}
