package com.zarador.world.content.skill.impl.slayer;

/**
 * Slayer Masters of Chaos
 * @Author Jonny
 */

public enum SlayerMasters {
    /**
     * Turael slayer master
     * Located in Burthorpe
     * Requires: Combat level 3
     */
    TURAEL(401, 3, 1, 2, 5),

    /**
     * Mazchna slayer master
     * Located in Canifis
     * Requires: Combat level 20
     */
    MAZCHNA(402, 20, 2, 5, 15),

    /**
     * Vannaka slayer master
     * Located in Edgeville Dungeon
     * Requires: Combat level 40
     */
    VANNAKA(403, 40, 4, 20, 60),

    /**
     * Chaeldar slayer master
     * Located in Lost City
     * Requires: Combat level 70
     */
    CHAELDAR(404, 70, 10, 50, 150),

    /**
     * Nieve slayer master
     * Located in Tree Gnome Stronghold
     * Requires: Combat level 90
     */
    NIEVE(490, 90, 12, 60, 180),

    /**
     * Duradel slayer master
     * Located in Shilo Village
     * Requires: Combat level 120
     */
    DURADEL(405, 120, 15, 75, 225),

    /**
     * Kuradel slayer master
     * Located in Kuradal's dungeon
     * Requires: Combat level 110
     */
    KURADAL(9085, 110, 18, 90, 270);

    SlayerMasters(int npcId, int combatLevelRequirement, int pointsPerTask, int tenTaskBonus, int fiftyTaskBonus) {
        this.npcId = npcId;
        this.combatLevelRequirement = combatLevelRequirement;
        this.pointsPerTask = pointsPerTask;
        this.tenTaskBonus = tenTaskBonus;
        this.fiftyTaskBonus = fiftyTaskBonus;
    }

    int npcId;
    int combatLevelRequirement;
    int pointsPerTask;
    int tenTaskBonus;
    int fiftyTaskBonus;

    /**
     * Get the npc id for the slayer master.
     * @return
     */
    public int getNpcId() {
        return this.npcId;
    }

    /**
     * Get the required combat level to get
     * a slayer task using this slayer master.
     * @return
     */
    public int getCombatLevelRequirement() {
        return this.combatLevelRequirement;
    }

    /**
     * Get the amount of points that you gain
     * per slayer task depending on your slayer master.
     * @return
     */
    public int getPointsPerTask() {
        return this.pointsPerTask;
    }

    /**
     * Get the amount of points that you gain
     * per 10 tasks you complete depending on your slayer master.
     * @return
     */
    public int getTenTaskBonus() {
        return this.tenTaskBonus;
    }

    /**
     * Get the amount of points that you gain
     * per 50 tasks you complete depending on your slayer master.
     * @return
     */
    public int getFiftyTaskBonus() {
        return this.fiftyTaskBonus;
    }

    /**
     * Gets a slayer master depend
     * @param npcId
     * @return
     */
    public static SlayerMasters forNpcId(int npcId) {
        for(SlayerMasters slayerMasters: SlayerMasters.values()) {
            if(slayerMasters.getNpcId() == npcId) {
                return slayerMasters;
            }
        }
        return null;
    }
}