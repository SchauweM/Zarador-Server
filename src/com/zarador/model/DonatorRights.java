package com.zarador.model;

import com.zarador.world.entity.impl.player.Player;

public enum DonatorRights {

    PLAYER("Player", "<col=ff0000>", "", 0, 0),
    PREMIUM("Premium", "<col=ff0000>", "", 7, .2),
    EXTREME("Extreme", "<col=2FAC45>", "<shad=0>", 9, .4),
    LEGENDARY("Legendary", "<col=570057>", "", 10, .6),
    UBER("Uber", "<col=ffff00>", "<shad=0>", 11, .8),
    PLATINUM("Platinum", "<col=060000>", "<shad=FFFFF9>", 19, 1);

    private String title;
    private String color;
    private String shad;
    private int crown;
    private double dungTokenBoost;

    DonatorRights(String title, String color, String shad, int crown, double dungTokenBoost) {
        this.title = title;
        this.color = color;
        this.shad = shad;
        this.crown = crown;
        this.dungTokenBoost = dungTokenBoost;
    }

    /**
     * Gets the rank for a certain id.
     *
     * @param id The id (ordinal()) of the rank.
     * @return rights.
     */
    public static DonatorRights forId(int id) {
        for (DonatorRights rights : DonatorRights.values()) {
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
     * Get the dung token boost multiplier
     * @return
     */
    public double getDungTokenBoost() {
        return this.dungTokenBoost;
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
     * Are you a donator?
     *
     * @return
     */
    public boolean isDonator() {
        return this.ordinal() > 0;
    }

    /**
     * Gets the crown for a certain rank
     *
     * @return
     */
    public int getCrown() {
        return this.crown;
    }

    public double getSpecialAccuracyBoost(Player player) {
        if(player.getLocation() == Locations.Location.WILDERNESS || player.getLocation() == Locations. Location.WILDKEY_ZONE) {
            return 0;
        } else {
            return this.ordinal() / 100;
        }
    }

    /**
     * Get the amount required to set your yell tag.
     * @return
     */
    public int getYellTagPrice() {
        int amount = 0;
        switch(this) {
            case PREMIUM:
                amount = 10000000;
                break;
            case EXTREME:
                amount = 8000000;
                break;
            case LEGENDARY:
                amount = 6000000;
                break;
            case UBER:
                amount = 4000000;
                break;
            case PLATINUM:
                amount = 2000000;
                break;
        }
        return amount;
    }

}
