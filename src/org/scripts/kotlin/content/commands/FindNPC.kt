package org.scripts.kotlin.content.commands

import com.zarador.model.StaffRights
import com.zarador.model.definitions.ItemDefinition
import com.zarador.model.definitions.NpcDefinition
import com.zarador.model.player.command.Command
import com.zarador.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.

 * @author Seba
 */
class FindNPC(staffRights: StaffRights) : Command(staffRights) {

    override fun execute(player: Player, args: Array<String>?, privilege: StaffRights) {
        if (args == null) {
            player.packetSender.sendMessage("Example usage: ::findnpc-npcname")
        } else {
            val name = args[0].toLowerCase()
            var found = false
            player.packetSender.sendMessage("Trying to find name id for npc name " + name)
            for (i in 0..NpcDefinition.getMaxAmountOfNpcs() - 1 - 1) {
                if(NpcDefinition.forId(i) == null) {
                    continue
                }
                if (NpcDefinition.forId(i).name.toLowerCase().equals(name)) {
                    player.packetSender.sendMessage("Found npc with name [" + NpcDefinition.forId(i).getName() + " lvl "+NpcDefinition.forId(i).combatLevel+"] - id: " + i)
                    found = true
                }
            }
            if (!found) {
                player.packetSender.sendMessage("No name for the name $name were found.")
            }
        }
    }
}
