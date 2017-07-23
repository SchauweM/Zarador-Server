package com.zarador.model.npc.drops;

import com.zarador.util.Misc;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/13/2016.
 *
 * @author Seba
 */
public class LootItem {

    /**
     * Represents an item from the loot table.
     * @param id The id of the item
     * @param minimum The minimum amount to be dropped.
     * @param maximum The maximum that can be dropped.
     * @param condition The condition to be met to drop the item.
     */
    public LootItem(short id, int minimum, int maximum, byte condition) {
        this.id = id;
        this.minimum = minimum;
        this.maximum = maximum;
        this.condition = condition;
    }

    /**
     * The id that is connected to the drop.
     */
    private short id;

    /**
     * The minimum amount to be dropped.
     */
    private int minimum;

    /**
     * The maximum amount that can be dropped.
     */
    private int maximum;

    /**
     * The condition that must be met for the item to be dropped.
     */
    private byte condition;

    /**
     * Gets the item connected to the drop.
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Generates the amount of the item to be dropped.
     * @return
     */
    public int getRandomAmount() {
        if (maximum == minimum) {
            return minimum;
        } else if (minimum > maximum) {
            System.out.println("Error: Item " + id + " minimum is larger then the maximum.");
            return maximum;
        }
        return Misc.nextInt(maximum - minimum) + minimum;
    }

    public int getMinimum() {
        return this.minimum;
    }

    public int getMaximum() {
        return this.maximum;
    }

    /**
     * Returns the conditions that need to be met.
     */
    public CONDITION getCondition() {
        return CONDITION.values()[condition];
    }

    /**
     * List of the conditions
     */
    public enum CONDITION {
        NONE, TASK, ONE_ITEM, DONOR
    }
}
