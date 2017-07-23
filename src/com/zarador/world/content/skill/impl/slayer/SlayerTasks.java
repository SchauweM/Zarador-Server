package com.zarador.world.content.skill.impl.slayer;

import com.zarador.model.Position;
import com.zarador.util.Misc;

/**
 * All Chaos Slayer Tasks
 * @Author Jonny
 */

public enum SlayerTasks {

    /**
     * Turael's slayer tasks
     * @SlayerMaster.TURAEL
     */
    BANSHEE(new int[] {1612}, new int[] {22}, SlayerMasters.TURAEL, 15, 50, 4166, new Position(3437, 3535, 0)),
    BAT(new int[] {412, 78}, new int[] {8, 32}, SlayerMasters.TURAEL, 15, 50, -1, new Position(3427, 3538, 0)),
    CHICKEN(new int[] {41}, new int[] {3}, SlayerMasters.TURAEL, 15, 50, -1, new Position(3236, 3295, 0)),
    ROCK_CRAB(new int[] {1265}, new int[] {20}, SlayerMasters.TURAEL, 15, 50, -1, new Position(2538, 3756, 0)),
    EXPERIMENT(new int[] {1677}, new int[] {27}, SlayerMasters.TURAEL, 15, 50, -1, new Position(3564, 9940, 0)),
    YAK(new int[] {5529}, new int[] {24}, SlayerMasters.TURAEL, 15, 50, -1, new Position(2324, 3803, 0)),
    CRAWLING_HAND(new int[] {1648, 1657}, new int[] {10, 16}, SlayerMasters.TURAEL, 15, 50, -1, new Position(3412, 3545, 0)),
    SPIDER(new int[] {59, 134, 4400}, new int[] {13, 72, 60}, SlayerMasters.TURAEL, 15, 50, -1, new Position(3100, 9882, 0)),
    COW(new int[] {81}, new int[] {8}, SlayerMasters.TURAEL, 15, 50, -1, new Position(3252, 3267, 0)),
    GOBLIN(new int[] {4261, 4262, 4263, 4269, 4270, 4407, 4411, 13469, 2685, 2688, 3583}, new int[] {8, 8, 8, 8, 8, 8, 14, 28, 21, 58}, SlayerMasters.TURAEL, 15, 50, -1, new Position(3246, 3246, 0)),

    /**
     * Mazchna's slayer tasks
     * @SlayerMaster.MAZCHNA
     */
    SKELETON(new int[] {90, 92, 2036, 4385, 4386, 6104, 6105}, new int[] {28, 28, 110, 72, 90, 160, 160}, SlayerMasters.MAZCHNA, 15, 65, -1, new Position(2884, 9806, 0)),
    CHAOS_DRUID(new int[] {181}, new int[] {32}, SlayerMasters.MAZCHNA, 15, 65, -1, new Position(2934, 9853, 0)),
    ZOMBIE(new int[] {73, 75, 76, 5377, 5379}, new int[] {18, 34, 36, 45, 60}, SlayerMasters.MAZCHNA, 15, 65, -1, new Position(3145, 9910, 0)),
    GHOST(new int[] {103, 4387}, new int[] {25, 90}, SlayerMasters.MAZCHNA, 15, 65, -1, new Position(2921, 9848, 0)),
    HILL_GIANT(new int[] {117}, new int[] {36}, SlayerMasters.MAZCHNA, 15, 65, -1, new Position(3116, 9855, 0)),

    /**
     * Vannaka's slayer tasks
     * @SlayerMasters.VANNAKA
     */
    LESSER_DEMON(new int[] {82}, new int[] {90}, SlayerMasters.VANNAKA, 15, 75, -1, new Position(2924, 9801, 0)),
    FIRE_GIANT(new int[] {110, 1583}, new int[] {110, 110}, SlayerMasters.VANNAKA, 15, 75, -1, new Position(2630, 9587, 2)),
    CHAOS_DWARF(new int[] {119}, new int[] {55}, SlayerMasters.VANNAKA, 15, 75, -1, new Position(2914, 9757, 0)),
    MAGIC_AXE(new int[] {127}, new int[] {60}, SlayerMasters.VANNAKA, 15, 75, -1, new Position(2960, 9783, 0)),
    BABY_BLUE_DRAGON(new int[] {52}, new int[] {60}, SlayerMasters.VANNAKA, 15, 75, -1, new Position(2919, 9799, 0)),
    GREEN_DRAGON(new int[] {941, 5362}, new int[] {95, 250}, SlayerMasters.VANNAKA, 15, 75, -1, new Position(3360, 3664, 0), true),
    OGRE(new int[] {115}, new int[] {95}, SlayerMasters.VANNAKA, 15, 75, -1, new Position(2493, 3092, 0)),
    INFERNAL_MAGE(new int[] {1643}, new int[] {80}, SlayerMasters.VANNAKA, 15, 75, -1, new Position(3444, 3556)),
    BLOODVELD(new int[] {1618}, new int[] {90}, SlayerMasters.VANNAKA, 15, 75, -1, new Position(3410, 3574, 1)),
    LIVING_ROCK_CREATURE(new int[] {8834, 8833, 8832}, new int[] {160, 130, 100}, SlayerMasters.VANNAKA, 15, 75, -1, new Position(3638, 5104, 0)),

    /**
     * Chaeldar's slayer tasks
     * @SlayerMasters.CHAELDAR
     */
    ABERRANT_SPECTRE(new int[] {1604}, new int[] {115}, SlayerMasters.CHAELDAR, 15, 90, -1, new Position(3441, 3544, 1)),
    GARGOYLE(new int[] {1610}, new int[] {125}, SlayerMasters.CHAELDAR, 15, 90, -1, new Position(3440, 3537, 2)),
    NECHRYAEL(new int[] {1613}, new int[] {125}, SlayerMasters.CHAELDAR, 15, 90, -1, new Position(3444, 3567, 2)),
    HELLHOUND(new int[] {49}, new int[] {140}, SlayerMasters.CHAELDAR, 15, 90, -1, new Position(2870, 9824, 0)),
    BLUE_DRAGON(new int[] {52, 55}, new int[] {60, 125}, SlayerMasters.CHAELDAR, 15, 90, -1, new Position(2886, 9799, 0)),
    GREATER_DEMON(new int[] {83}, new int[] {115}, SlayerMasters.CHAELDAR, 15, 90, -1, new Position(2860, 9742, 0)),
    TZHAAR(new int[] {2591, 2600, 2605, 2611}, new int[] {110, 85, 140, 170}, SlayerMasters.CHAELDAR, 15, 90, -1, new Position(2445, 5170, 0)),

    /**
     * Nieve's slayer tasks
     * @SlayerMasters.NIEVE
     */
    BLACK_DEMON(new int[] {84}, new int[] {200}, SlayerMasters.NIEVE, 15, 125, -1, new Position(2871, 9797, 0)),
    FROST_DRAGON(new int[] {51}, new int[] {200}, SlayerMasters.NIEVE, 15, 125, -1, new Position(2973, 3947, 0), true),
    ABYSSAL_DEMON(new int[] {1615}, new int[] {140}, SlayerMasters.NIEVE, 15, 125, -1, new Position(3419, 3564, 2)),
    RED_DRAGON(new int[] {53, 3588}, new int[] {175, 65}, SlayerMasters.NIEVE, 15, 125, -1, new Position(2689, 9506, 0)),
    IRON_DRAGON(new int[] {1591}, new int[] {225}, SlayerMasters.NIEVE, 15, 125, -1, new Position(2721, 9464, 0)),
    STEEL_DRAGON(new int[] {1592}, new int[] {275}, SlayerMasters.NIEVE, 15, 125, -1, new Position(2721, 9464, 0)),
    JUNGLE_STRYKEWYRM(new int[] {9467}, new int[] {135}, SlayerMasters.NIEVE, 15, 125, -1, new Position(2728, 5098, 0)),
    GLACOR(new int[] {1382}, new int[] {250}, SlayerMasters.NIEVE, 15, 125, -1, new Position(3037, 9579, 0)),
    TORMENTED_DEMON(new int[] {8349}, new int[] {650}, SlayerMasters.DURADEL, 15, 50, -1, new Position(2570, 5735, 0), true),

    /**
     * Duradel's slayer tasks
     * @SlayerMasters.DURADEL
     */
    GENERAL_GRAARDOR(new int[] {6260, 6261, 6263, 6265}, new int[] {700, 200, 200, 200}, SlayerMasters.DURADEL, 15, 50, -1, new Position(2845, 5335, 2)),
    COMMANDER_ZILYANA(new int[] {6247, 6252, 6248, 6250}, new int[] {700, 200, 200, 200}, SlayerMasters.DURADEL, 15, 50, -1, new Position(2916, 5272, 0)),
    KRIL_TSUTSAROTH(new int[] {6203, 6208, 6206, 6204}, new int[] {700, 200, 200, 200}, SlayerMasters.DURADEL, 15, 50, -1, new Position(2891, 5356, 2)),
    KREE_ARRA(new int[] {6222, 6227, 6225, 6223}, new int[] {700, 200, 200, 200}, SlayerMasters.DURADEL, 15, 50, -1, new Position(2872, 5268, 2)),
    GANODERMIC_BEAST(new int[] {130}, new int[] {450}, SlayerMasters.DURADEL, 15, 50, -1, new Position(2245, 3182, 0)),
    DAGANNOTH_KING(new int[] {2882, 2881, 2883}, new int[] {400, 400, 400}, SlayerMasters.DURADEL, 15, 50, -1, new Position(1909, 4367, 0)),
    CORPOREAL_BEAST(new int[] {8133}, new int[] {900}, SlayerMasters.DURADEL, 15, 50, -1, new Position(2916, 4384, 0)),
    KRAKEN(new int[] {502}, new int[] {450}, SlayerMasters.DURADEL, 15, 50, -1, new Position(3696, 5807, 0)),
    CERBERUS(new int[] {5866}, new int[] {450}, SlayerMasters.DURADEL, 15, 50, -1, new Position(1240, 1226, 0)),
    KALPHITE_QUEEN(new int[] {1158}, new int[] {450}, SlayerMasters.DURADEL, 15, 50, -1, new Position(3508, 9492, 0)),
    PHOENIX(new int[] {8549}, new int[] {400}, SlayerMasters.DURADEL, 15, 50, -1, new Position(2839, 9557, 0)),
    BANDOS_AVATAR(new int[] {4540}, new int[] {400}, SlayerMasters.DURADEL, 15, 50, -1, new Position(2891, 4767, 0)),
    BORK(new int[] {5871}, new int[] {800}, SlayerMasters.DURADEL, 15, 50, -1, new Position(3102, 2965, 0)),
    SLASH_BASH(new int[] {2060}, new int[] {300}, SlayerMasters.DURADEL, 15, 50, -1, new Position(2547, 9448, 0)),
    SCORPIA(new int[] {2001}, new int[] {750}, SlayerMasters.DURADEL, 15, 50, -1, new Position(3182, 3685, 0), true),
    CALLISTO(new int[] {6609}, new int[] {750}, SlayerMasters.DURADEL, 15, 50, -1, new Position(3324, 3583, 0), true),
    VENENATIS(new int[] {2000}, new int[] {750}, SlayerMasters.DURADEL, 15, 50, -1, new Position(3126, 3815, 0), true),
    VETION(new int[] {6611}, new int[] {750}, SlayerMasters.DURADEL, 15, 50, -1, new Position(3246, 3796, 0), true),
    CHAOS_FANATIC(new int[] {6619}, new int[] {650}, SlayerMasters.DURADEL, 15, 50, -1, new Position(2967, 3848, 0), true),
    CRAZY_ARCHAEOLOGIST(new int[] {6618}, new int[] {650}, SlayerMasters.DURADEL, 15, 50, -1, new Position(2980, 3690, 0), true),

    /**
     * Kuradal's slayer tasks
     * @SlayerMasters.KURADAL
     */
    HELLHOUND_KURADAL(new int[] {49}, new int[] {140}, SlayerMasters.KURADAL, 40, 125, -1, new Position(1641, 5257, 0)),
    GREATER_DEMON_KURADAL(new int[] {83}, new int[] {115}, SlayerMasters.KURADAL, 40, 125, -1, new Position(1620, 5256, 0)),
    BLUE_DRAGON_KURADAL(new int[] {52, 55}, new int[] {60, 125}, SlayerMasters.KURADAL, 40, 125, -1, new Position(1607, 5273, 0)),
    GARGOYLE_KURADAL(new int[] {1610}, new int[] {125}, SlayerMasters.KURADAL, 40, 125, -1, new Position(1609, 5297, 0)),
    ABYSSAL_DEMON_KURADAL(new int[] {1615}, new int[] {140}, SlayerMasters.KURADAL, 40, 125, -1, new Position(1638, 5300, 0)),
    DARK_BEAST_KURADAL(new int[] {2783}, new int[] {190}, SlayerMasters.KURADAL, 40, 125, -1, new Position(1650, 5290, 0)),
    IRON_DRAGON_KURADAL(new int[] {1591}, new int[] {225}, SlayerMasters.KURADAL, 40, 125, -1, new Position(1651, 5271, 0)),
    MITHRIL_DRAGON_KURADAL(new int[] {5363}, new int[] {270}, SlayerMasters.KURADAL, 40, 125, -1, new Position(1629, 5281, 0));

    SlayerTasks(int[] npcId, int[] experience, SlayerMasters slayerMaster, int minimumAmount, int maximumAmount, int equipmentId, Position pos, boolean wild) {
        this.npcId = npcId;
        this.experience = experience;
        this.slayerMaster = slayerMaster;
        this.minimumAmount = minimumAmount;
        this.maximumAmount = maximumAmount;
        this.equipmentId = equipmentId;
        this.taskPos = pos;
        this.wild = wild;
    }
    SlayerTasks(int[] npcId, int[] experience, SlayerMasters slayerMaster, int minimumAmount, int maximumAmount, int equipmentId, Position pos) {
        this.npcId = npcId;
        this.experience = experience;
        this.slayerMaster = slayerMaster;
        this.minimumAmount = minimumAmount;
        this.maximumAmount = maximumAmount;
        this.equipmentId = equipmentId;
        this.taskPos = pos;
    }

    private SlayerMasters slayerMaster;
    private int[] npcId;
    private int[] experience;
    private int minimumAmount;
    private int maximumAmount;
    private int equipmentId;
    private Position taskPos;
    private boolean wild;

    /**
     * Get the required slayer master(s) for the task.
     * @return
     */
    public SlayerMasters getSlayerMaster() {
        return this.slayerMaster;
    }

    /**
     * Get the required npc id for the task
     * depending on the index you choose.
     * @param index
     * @return
     */
    public int getNpcId(int index) {
        return this.npcId[index];
    }

    /**
     * Get all the npc ids required
     * for the task you are on.
     * @return
     */
    public int[] getNpcIds() {
        return this.npcId;
    }

    /**
     * Get the experience that you gain
     * per kill. Depends on the index.
     * @return
     */
    public int getExperience(int index) {
        return this.experience[index];
    }

    /**
     * Gets a raw version of all possible slayer exps.
     * @return
     */
    public int[] getExperience() {
        return this.experience;
    }

    /**
     * Gets the minimum amount that can be assigned
     * to your slayer assignment to kill.
     * @return
     */
    public int getMinimumAmount() {
        return this.minimumAmount;
    }

    /**
     * Gets the maximum amount that can be assigned
     * to your slayer assignment to kill.
     * @return
     */
    public int getMaximumAmount() {
        return this.maximumAmount;
    }

    /**
     * Gets the item id for any potential required
     * equipment to kill your slayer monster.
     * @return
     */
    public int getEquipmentId() {
        return this.equipmentId;
    }

    /**
     * Returns the formatted name of the slayer task.
     * @return
     */
    public String getName() {
        return Misc.formatText(this.toString().toLowerCase().replace("_", " ").replace("kuradal", ""));
    }

    public Position getTaskPos() {
        return taskPos;
    }

    public boolean isWild() {
        return wild;
    }
}
