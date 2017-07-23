package com.zarador.ect.dropwriting;

import javafx.collections.FXCollections;

import java.io.File;
import java.util.HashMap;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 9/4/2016.
 *
 * @author Seba
 */
public class DropManager {

    /**
     * The file location of our save file.
     */
    private static final File file = new File("./data/def/npcDrops.dat");

    /**
     * Adds a drop to the drop table.
     * @param npcId The npc id of of the drop we are adding
     * @param itemId The item id of the drop we are adding
     * @param minimum The minimum amount the npc can drop
     * @param maximum the maximum amount the npc can drop
     * @param rarity The rarity of the drop we are adding.
     * @param condition The condition the player need to meet to gain access to the drop.
     */
    public static void addDrop(int npcId, int itemId, int minimum, int maximum, Drop.RARITY rarity, Drop.CONDITION condition) {
        if (DropTable.dropTables == null) {
            DropTable.dropTables = new HashMap<>();
            DropTable.deSerialize(file);
        }

        DropTable dropTable;

        /**
         * Check to see if we already have a entry for the npc id.  If we don't then we will make a new table for that npc.
         */
        if (DropTable.dropTables.containsKey(npcId)) {
            dropTable = DropTable.dropTables.get(npcId);
        } else {
            dropTable = new DropTable();
            dropTable.id = npcId;
            DropTable.dropTables.put(dropTable.id, dropTable);
        }

        /**
         * Makes a new empty drop and sets the values for the drop.
         */
        Drop drop = new Drop();
        drop.setItem(itemId);
        drop.setMinimum(minimum);
        drop.setMaximum(maximum);
        drop.setRarity(rarity);
        drop.setCondition(condition);


        /**
         * Add the drop to the drop table.
         */
        dropTable.drops.add(drop);
    }

    /**
     * Adds a charm chance to the drop table.
     * @param npcId The npc id of the npc we are adding a charm to.
     * @param charm The charm we are adding to the npc
     * @param amount The amount of charms the npc can drop.
     * @param chance The chance the npc can drop the charm.
     */
    public static void addCharm(int npcId, DropTable.CHARM charm, int amount, int chance) {
        if (DropTable.dropTables == null) {
            DropTable.dropTables = new HashMap<>();
            DropTable.deSerialize(file);
        }

        DropTable dropTable;

        /**
         * Check to see if we already have a entry for the npc id.  If we don't then we will make a new table for that npc.
         */
        if (DropTable.dropTables.containsKey(npcId)) {
            dropTable = DropTable.dropTables.get(npcId);
        } else {
            dropTable = new DropTable();
            dropTable.id = npcId;
            DropTable.dropTables.put(dropTable.id, dropTable);
        }

        /**
         * Check to see if the npc already has a charm to be dropped.  If not we make a new list for the charm to be added.
         */
        if (dropTable.charmRate == null) {
            dropTable.charmRate = FXCollections.observableArrayList(new Charm[]{new Charm(12158, 1, 0), new Charm(12159, 1, 0), new Charm(12160, 1, 0), new Charm(12163, 1, 0), new Charm(12162, 1, 0), new Charm(12161, 1, 0), new Charm(12168, 1, 0)});
        }

        /**
         * Makes a new charm drop
         */

        for (Charm charm1 : dropTable.charmRate) {
            if (charm1.getId() == charm.getCharm()) {
                charm1.setChance(chance);
                charm1.setAmount(amount);
                break;
            }
        }

    }

    /**
     * Saves our current drops to the droptable.dat
     */
    public static void saveDrops() {
        DropTable.serialize(file);
    }
}
