package com.zarador.world.content.skill.impl.runecrafting;

import com.zarador.engine.task.Task;
import com.zarador.engine.task.TaskManager;
import com.zarador.model.Animation;
import com.zarador.model.Skill;
import com.zarador.model.definitions.ItemDefinition;
import com.zarador.model.input.impl.GrindArmadylRune;
import com.zarador.world.entity.impl.player.Player;

/**
 * Created by Dave on 12/07/2016.
 */
public class DustOfArmadyl {
    public static void openArmadylInterface(Player player) {
        player.getSkillManager().stopSkilling();
        player.setSelectedSkillingItem(21776);
        player.setInputHandling(new GrindArmadylRune());
        player.getPacketSender().sendString(2799, ItemDefinition.forId(21774).getName()).sendInterfaceModel(1746, 21774, 150).sendChatboxInterface(4429);
        player.getPacketSender().sendString(2800, "How many would you like to grind?");
    }

    public static void grindRunes(final Player player, final int amount) {
        final int armadylShard = player.getSelectedSkillingItem();
        final int dustOfArmadyl = 21774;
        player.getSkillManager().stopSkilling();
        player.getPacketSender().sendInterfaceRemoval();
        player.setCurrentTask(new Task(2, player, true) {
            int amountSacrificed = 0;

            @Override
            public void execute() {
                if (amountSacrificed >= amount) {
                    stop();
                    return;
                }
                if (!player.getInventory().contains(armadylShard)) {
                    player.getPacketSender()
                            .sendMessage("You have run out of " + ItemDefinition.forId(armadylShard).getName() + ".");
                    stop();
                    return;
                }
                amountSacrificed++;
                player.getInventory().delete(armadylShard, 1);
                player.getInventory().add(dustOfArmadyl, 7);
                player.performAnimation(new Animation(713));
                player.getSkillManager().addExactExperience(Skill.RUNECRAFTING, 22, true);
                player.getPacketSender().sendMessage("You finely grind the Shards of Armadyl into a fine Dust of Armadyl.");
            }

            @Override
            public void stop() {
                setEventRunning(false);
            }
        });
        TaskManager.submit(player.getCurrentTask());
    }
}
