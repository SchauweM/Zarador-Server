package com.zarador.world.content;

import com.zarador.model.GroundItem;
import com.zarador.model.Item;
import com.zarador.util.Misc;
import com.zarador.world.entity.impl.GroundItemManager;
import com.zarador.world.entity.impl.npc.NPC;
import com.zarador.world.entity.impl.player.Player;

/**
 * Created by High105 on 12/22/2016.
 */
public class BarrowsDzone {

    public static int[] barrowsBrother = { 2025, 2026, 2027, 2028, 2029, 2030 };

    private static int[] dhDrops = { 4716, 4718, 4720, 4722 };

    private static int[] ahrimDrops = { 4708, 4710, 4712, 4714 };

    private static int[] toragsDrops = { 4745, 4747, 4749, 4751 };

    private static int[] guthansDrops = { 4724, 4726, 4728, 4730 };

    private static int[] veracDrops = { 4753, 4755, 4757, 4759 };

    private static int[] karilsDrops = { 4732, 4734, 4736, 4738 };

    private static boolean rollDrop() {
        int chance = Misc.random(1, 35);
        if (chance == 30) {
            return true;
        }
        return false;
    }

    private static int pickDrop(int[] data) {
        int id = 0;
        int random = Misc.random(0, 3);
        id = data[random];
        return id;
    }

    public static void killedBrother(Player player, NPC npc) {
        boolean drop = rollDrop();
        switch (npc.getId()) {
            case 2025:
                if (drop) {
                    GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pickDrop(ahrimDrops)), npc.getPosition(),
                            player.getUsername(), false, 150, true, 200));
                }
                break;
            case 2026:
                if (drop) {
                    GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pickDrop(dhDrops)), npc.getPosition(),
                            player.getUsername(), false, 150, true, 200));
                }
                break;
            case 2027:
                if (drop) {
                    GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pickDrop(guthansDrops)), npc.getPosition(),
                            player.getUsername(), false, 150, true, 200));
                }
                break;
            case 2028:
                if (drop) {
                    GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pickDrop(karilsDrops)), npc.getPosition(),
                            player.getUsername(), false, 150, true, 200));
                }
                GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(4740).setAmount(10), npc.getPosition(),
                        player.getUsername(), false, 150, true, 200));
                break;
            case 2029:
                if (drop) {
                    GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pickDrop(toragsDrops)), npc.getPosition(),
                            player.getUsername(), false, 150, true, 200));
                }
                break;
            case 2030:
                if (drop) {
                    GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pickDrop(veracDrops)), npc.getPosition(),
                            player.getUsername(), false, 150, true, 200));
                }
                break;
            default:
        }
    }

}
