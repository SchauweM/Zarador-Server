package com.zarador.model;

import com.zarador.world.entity.impl.player.Player;

public enum StaffRights {

    PLAYER("Player", "", "", 0),
    YOUTUBER("YouTuber", "<col=ff0000>", "<shad=620000>", 5),
    WIKI_EDITOR("Wiki Editor", "<col=ff7f00>", "<shad=0>", 15),
    WIKI_MANAGER("Wiki Manager", "<col=31a4ff>", "<shad=0>", 16),
    FORUM_MOD("Forum Mod", "<col=8624FF>", "<shad=000000>", 20),
    SUPPORT("Support", "<col=0000FF>", "<shad=00E0FF>", 4),
    MODERATOR("Moderator", "<col=00BFDA>", "<shad=000000>", 1),
    GLOBAL_MOD("Global Mod", "<col=289C39>", "<shad=000000>", 6),
    ADMINISTRATOR("Administrator", "<col=FFFF00>", "<shad=000000>", 2),
    MANAGER("Manager", "<col=ff0000>", "<shad=000000>", 14),
    OWNER("Owner", "<col=ff0000>", "<shad=000000>", 3);

    private String title;
    private String color;
    private String shad;
    private int crown;

    StaffRights(String title, String color, String shad, int crown) {
        this.title = title;
        this.color = color;
        this.shad = shad;
        this.crown = crown;
    }

    /**
     * Gets the rank for a certain id.
     *
     * @param id The id (ordinal()) of the rank.
     * @return rights.
     */
    public static StaffRights forId(int id) {
        for (StaffRights rights : StaffRights.values()) {
            if (rights.ordinal() == id) {
                return rights;
            }
        }
        return null;
    }

    /**
     * Gets the color code for a certain rank
     *
     * @return
     */
    public String getColor() {
        return this.color;
    }

    /**
     * Gets the color shad code for a certain rank
     *
     * @return
     */
    public String getShad() {
        return this.shad;
    }

    /**
     * Gets the rank title for a certain rank
     *
     * @return
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Gets the crown for a certain rank
     *
     * @return
     */
    public int getCrown() {
        return this.crown;
    }

    /**
     * Tells you if your opponent is a higher rank than you
     *
     * @return
     */
    public boolean isHigherRank(StaffRights otherRights) {
        if (this.ordinal() < otherRights.ordinal()) {
            return true;
        }
        return false;
    }

    /**
     * Tells you if you are a part of the staff team
     *
     * @return
     */
    public boolean isStaff() {
        return this.ordinal() > 0;
    }

    /**
     * Tells you if you are a developer or not
     *
     * @return
     */
    public boolean isDeveloper(Player player) {
        return this == OWNER && player.getUsername().equalsIgnoreCase("michael");
    }

    /**
     * Tells you if you are a management position
     *
     * @return
     */
    public boolean isManagement() {
        return this == ADMINISTRATOR || this == MANAGER || this == OWNER;
    }

}
