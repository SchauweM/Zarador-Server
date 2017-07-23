package com.zarador.model.npc.drops;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/13/2016.
 *
 * @author Seba
 */
public class LootTable {

    /**
     * If this table has access to the RDT
     */
    private boolean rareTable;

    /**
     * The chance a clue scroll may drop.
     */
    private float clueChance;

    /**
     * The charms this table can drop
     */
    private LootCharm[] charms;

    /**
     * The sorted table by rarity for drops.
     */
    private LootItem[][] sortedLoot;

    /**
     * Returns if the npc has access to the RDT.
     * @return
     */
    public boolean hasRareTableAccess() {
        return rareTable;
    }

    /**
     * Sets if the table has access to the RDT
     * @param set
     */
    public void setRareTable(boolean set) {
        this.rareTable = set;
    }

    /**
     * This will calculate the chance a clue scroll will drop once we add them.
     * @return
     */
    public int calcClueChance() {
        if (this.clueChance == 0.00) {
            return 0;
        }
        return (int) Math.ceil(this.clueChance * 10);
    }

    /**
     * Set the clue scroll drop chance.
     * @param f
     */
    public void setClueChance(float f) {
        this.clueChance = f;
    }

    /**
     * Sets the charms that can be dropped by a npc
     * @param charms
     * @param i
     */
    public void setCharms(LootCharm charms, int i) {
        if (this.charms == null) {
            this.charms = new LootCharm[LootCharm.CHARM.values().length];
        }
        this.charms[i] = charms;
    }

    /**
     * Returns the charms the {@link com.zarador.world.entity.impl.npc.NPC} can drop.
     * @return
     */
    public LootCharm[] getCharms() {
        return this.charms;
    }

    /**
     * Sets the loot table for the {@link com.zarador.world.entity.impl.npc.NPC}
     * @param loot
     */
    public void setLoot(LootItem[][] loot) {
        this.sortedLoot = loot;
    }

    /**
     * Returns the drop table sorted by rarity.
     * @return
     */
    public LootItem[][] getSortedLoot() {
        return this.sortedLoot;
    }
}
