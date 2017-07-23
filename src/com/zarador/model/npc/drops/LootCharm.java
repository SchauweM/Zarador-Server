package com.zarador.model.npc.drops;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/13/2016.
 *
 * @author Seba
 */
public class LootCharm {

    /**
     * Represents a charm from the the loot table
     * @param amount The amount that can be dropped.
     * @param rarity The chance of which charms can be dropped.
     */
    public LootCharm(int amount, int rarity) {
        this.amount = (byte) amount;
        this.rarity = (byte) rarity;
    }

    /**
     * The amount of charms that can be dropped.
     */
    private byte amount;

    /**
     * The rarity of which charms can be dropped.
     */
    private byte rarity;

    /**
     * Returns the amount of charms to be dropped.
     * @return
     */
    public byte getAmount() {
        return amount;
    }

    /**
     * Returns the chance to drop the charms.
     * @return
     */
    public byte getRarity() {
        return rarity;
    }

    /**
     * The charms that available to be dropped.
     */
    public enum CHARM {
        GOLD(12158),
        GREEN(12159),
        CRIMSON(12160),
        BLUE(12163),
        TALON_BEAST(12162),
        ABYSSAL(12161),
        OBSIDIAN(12168);

        private short charm;

        CHARM(int charm) {
            this.charm = (short) charm;
        }

        public short getCharm() {
            return charm;
        }
    }
}
