package com.zarador.world.content;

import com.zarador.engine.task.TaskManager;
import com.zarador.engine.task.impl.ShearSheepTask;
import com.zarador.model.Animation;
import com.zarador.model.Flag;
import com.zarador.model.Position;
import com.zarador.world.World;
import com.zarador.world.entity.impl.npc.NPC;
import com.zarador.world.entity.impl.player.Player;

public class Sheep {

    public static final int[][] sheepSpawns = {
            {43, 3199, 3261}, {43, 2924, 3325},
            {43, 3197, 3270}, {43, 3270, 3267},
            {43, 2923, 3319}, {43, 2924, 3325},
            {43, 2923, 3319}, {43, 2924, 3325},
            {43, 2913, 3326}, {43, 2912, 3319}
    };

    public static void spawnSheep() {

        for (int i = 0; i < sheepSpawns.length; i++) {
            NPC n = new NPC(sheepSpawns[i][0], new Position(sheepSpawns[i][1], sheepSpawns[i][2]));
            World.register(n);
        }
    }

    public static void shearSheep(Player player, final NPC sheepNPC) {
        if (player.getInterfaceId() > 0 || player == null || sheepNPC == null || !sheepNPC.isRegistered()
                || !player.getClickDelay().elapsed(1000))
            return;
        if (!player.getInventory().contains(1735)) {
            player.getPacketSender().sendMessage("You need shears for this.");
            return;
        }
        player.performAnimation(new Animation(893));
        if (sheepNPC.isRegistered()) {
            sheepNPC.setTransformationId(42);
            sheepNPC.getUpdateFlag().flag(Flag.TRANSFORM);
            TaskManager.submit(new ShearSheepTask(player, sheepNPC));
            player.getInventory().add(1759, 1);
            player.getPacketSender().sendMessage("You successfully sheared the sheep.");
        }
        player.getClickDelay().reset();
    }

}

