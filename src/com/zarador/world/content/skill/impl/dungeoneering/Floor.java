package com.zarador.world.content.skill.impl.dungeoneering;

import com.zarador.model.GameObject;
import com.zarador.model.Position;
import com.zarador.world.entity.impl.npc.NPC;

/**
 * Handles the Global Floors
 * @Author Jonny
 */
public abstract class Floor {

    /**
     * Enters the Dungeoneering floor
     */
    public abstract void enterFloor();

    /**
     * Leaves the Dungeoneering floor
     */
    public abstract void leaveFloor();

    /**
     * You have completed the floor!
     */
    public abstract void completeFloor();

    /**
     * Fight the boss
     */
    public abstract void fightBoss(GameObject gameObject);

    /**
     * Gets all the npc spawns for the floor
     * @return
     */
    public abstract NPC[] getMinions();

    /**
     * Gets all the boss npcs for the floor
     * @return
     */
    public abstract NPC[] getBosses();

    /**
     * Gets the floor ID for your current floor
     * @return
     */
    public abstract int getFloorId();

    /**
     * The position in which you start your floor.
     * @return
     */
    public abstract Position getStartPosition();

    /**
     * The amount of kills required to kill the boss.
     * @return
     */
    public abstract int getRequiredKills();

    /**
     * Get the boss npc
     * @return
     */
    public abstract NPC getBoss();

    /**
     * Handles killing the boss
     */
    public abstract void killedBoss();

}
