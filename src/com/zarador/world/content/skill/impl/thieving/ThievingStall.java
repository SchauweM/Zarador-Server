package com.zarador.world.content.skill.impl.thieving;

public enum ThievingStall {

    BAKERS_STALL(2561, 55, 45, StallRewards.BAKERS_STALL, 2, false),
    CRAFTING_STALL(4874, 5, 16, StallRewards.CRAFTING_STALL, 7, false),
    FOOD_STALL(4875, 15, 16, StallRewards.FOOD_STALL, 7, false),
    GENERAL_STALL(4876, 1, 16, StallRewards.GENERAL_STALL, 7, false),
    SILK_STALL(2560, 20, 24, StallRewards.SILK_STALL, 5, false),
    FUR_STALL(2563, 35, 36, StallRewards.FUR_STALL, 10, false),
    SILVER_STALL(2565, 50, 54, StallRewards.SILVER_STALL, 30, false),
    SPICE_STALL(2564, 65, 81, StallRewards.SPICE_STALL, 80, false),
    MAGIC_STALL(4877, 65, 100, StallRewards.MAGIC_STALL, 80, false),
    GEM_STALL(2562, 75, 160, StallRewards.GEM_STALL, 180, false),
    DONATOR_VEGETABLE_STALL(4706, 50, 75, StallRewards.DONATOR_VEGETABLE_STALL, 180, true),
    DONATOR_FISH_STALL(4705, 80, 200, StallRewards.DONATOR_FISH_STALL, 180, true),
    SCIMITAR_STALL(4878, 95, 200, StallRewards.SCIMITAR_STALL, 180, false);

    private int objectId, requiredLevel, experience, respawnDelay;
    private StallRewards stallRewards;
    private boolean donator;

    ThievingStall(int objectId, int requiredLevel, int experience, StallRewards stallRewards,
                  int respawnDelay, boolean donator) {
        this.objectId = objectId;
        this.requiredLevel = requiredLevel;
        this.experience = experience;
        this.stallRewards = stallRewards;
        this.respawnDelay = respawnDelay;
        this.donator = donator;
    }

    public int getObjectId() {
        return this.objectId;
    }

    public int getRequiredLevel() {
        return this.requiredLevel;
    }

    public int getExperience() {
        return this.experience;
    }

    public StallRewards getStallRewards() {
        return this.stallRewards;
    }

    public int getRespawnDelay() {
        return this.respawnDelay;
    }

    public boolean isDonator() { return this.donator; }

    public static ThievingStall forId(int objectId) {
        for (ThievingStall stall : ThievingStall.values()) {
            if (stall.getObjectId() == objectId) {
                return stall;
            }
        }

        return null;
    }

    public static ThievingStall forOrdinal(int ordinalId) {
        for (ThievingStall stall : ThievingStall.values()) {
            if (stall.ordinal() == ordinalId) {
                return stall;
            }
        }

        return null;
    }

}
