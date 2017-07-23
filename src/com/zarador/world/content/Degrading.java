package com.zarador.world.content;

import java.text.DecimalFormat;

import com.zarador.model.Flag;
import com.zarador.model.Item;
import com.zarador.model.container.impl.Equipment;
import com.zarador.model.definitions.ItemDefinition;
import com.zarador.util.Misc;
import com.zarador.world.entity.impl.player.Player;

/**
 * @Author Jonny, High105
 * Handles item degrading for items in Argos.
 */

public class Degrading {

    //All charges
    //0 - Abyssal tentacle
    private int[] charges = new int[DegradingItems.values().length];

    /**
     * Grabs charges depending on what index.
     * @param index
     * @return
     */
    public int getCharges(DegradingItems degradingItems) {
        return this.charges[degradingItems.ordinal()];
    }

    /**
     * Decrements your charges for your weapon/armour
     * @param degradingItems
     */
    public void decrementCharges(DegradingItems degradingItems) {
        if(this.charges[degradingItems.ordinal()] <= 0)
            return;
        this.charges[degradingItems.ordinal()] -= 1;
    }

    /**
     * Grabs the raw charges array
     * @return
     */
    public int[] getCharges() {
        return this.charges;
    }

    /**
     * Sets the raw charges value
     * @param charges
     */
    public void setCharges(int[] charges) {
        this.charges = charges;
    }

    /**
     * Sets max charges for your degradable item
     * @param degradingItems
     */
    public void maxCharges(DegradingItems degradingItems) {
        this.charges[degradingItems.ordinal()] = ItemDefinition.forId(degradingItems.getRegularID()).getCharges();
    }

    public enum DegradingType {
        DEFAULT(),
        BARROWS(),
        PVP()
    }

    public enum DegradingItems {

        STATIUS_FULL_HELM(13896, 13898, -1, DegradingType.PVP),
        STATIUS_PLATEBODY(13884, 13886, -1, DegradingType.PVP),
        STATIUS_PLATELEGS(13890, 13892, -1, DegradingType.PVP),
        STATIUS_WARHAMMER(13902, 13904, -1, DegradingType.PVP),

        VESTAS_CHAINBODY(13887, 13889, -1, DegradingType.PVP),
        VESTAS_PLATESKIRT(13893, 13895, -1, DegradingType.PVP),
        VESTAS_LONGSWORD(13899, 13901, -1, DegradingType.PVP),
        VESTAS_SPEAR(13905, 13907, -1, DegradingType.PVP),

        ZURIELS_HOOD(13864, 13866, -1, DegradingType.PVP),
        ZURIELS_ROBE_TOP(13858, 13860, -1, DegradingType.PVP),
        ZURIELS_ROBE_BOTTOM(13861, 13863, -1, DegradingType.PVP),
        ZURIELS_STAFF(13867, 13869, -1, DegradingType.PVP),

        MORRIGANS_COIF(13876, 13878, -1, DegradingType.PVP),
        MORRIGANS_LEATHER_BODY(13870, 13872, -1, DegradingType.PVP),
        MORRIGANS_LEATHER_CHAPS(13873, 13875, -1, DegradingType.PVP),

        // Statius's equipment

        CORRUPT_STATIUS_FULL_HELM(13920, 13922, -1, DegradingType.PVP),
        CORRUPT_STATIUS_PLATEBODY(13908, 13910, -1, DegradingType.PVP),
        CORRUPT_STATIUS_PLATELEGS(13914, 13916, -1, DegradingType.PVP),
        CORRUPT_STATIUS_WARHAMMER(13926, 13928, -1, DegradingType.PVP),

        // Vesta's equipment

        CORRUPT_VESTAS_CHAINBODY(13911, 13913, -1, DegradingType.PVP),
        CORRUPT_VESTAS_PLATESKIRT(13917, 13919, -1, DegradingType.PVP),
        CORRUPT_VESTAS_LONGSWORD(13923, 13925, -1, DegradingType.PVP),
        CORRUPT_VESTAS_SPEAR(13929, 13931, -1, DegradingType.PVP),

        // Zuriel's equipment

        CORRUPT_ZURIELS_HOOD(13938, 13940, -1, DegradingType.PVP),
        CORRUPT_ZURIELS_ROBE_TOP(13932, 13934, -1, DegradingType.PVP),
        CORRUPT_ZURIELS_ROBE_BOTTOM(13935, 13937, -1, DegradingType.PVP),
        CORRUPT_ZURIELS_STAFF(13941, 13943, -1, DegradingType.PVP),

        // Morrigan's equipment

        CORRUPT_MORRIGANS_COIF(13950, 13952, -1, DegradingType.PVP),
        CORRUPT_MORRIGANS_LEATHER_BODY(13944, 13946, -1, DegradingType.PVP),
        CORRUPT_MORRIGANS_LEATHER_CHAPS(13944, 13946, -1, DegradingType.PVP);

//        DH_HELM(4716, 4884, 60000, DegradingType.BARROWS),
//        DH_AXE(4718, 4890, 100000, DegradingType.BARROWS),
//        DH_BODY(4720, 4896, 90000, DegradingType.BARROWS),
//        DH_LEGS(4722, 4902, 80000, DegradingType.BARROWS),
//        VERAC_HELM(4753, 4980, 60000, DegradingType.BARROWS),
//        VERAC_FLAIL(4755, 4986, 100000, DegradingType.BARROWS),
//        VERAC_BODY(4757, 4992, 90000, DegradingType.BARROWS),
//        VERAC_LEGS(4759, 4998, 80000, DegradingType.BARROWS),
//        AHRIM_HOOD(4708, 4860, 60000, DegradingType.BARROWS),
//        AHRIM_STAFF(4710, 4866, 100000, DegradingType.BARROWS),
//        AHRIM_BODY(4712, 4872, 90000, DegradingType.BARROWS),
//        AHRIM_LEGS(4714, 4878, 80000, DegradingType.BARROWS),
//        GUTHAN_HELM(4724, 4908, 60000, DegradingType.BARROWS),
//        GUTHAN_SPEAR(4726, 4914, 100000, DegradingType.BARROWS),
//        GUTHAN_BODY(4728, 4920, 90000, DegradingType.BARROWS),
//        GUTHAN_LEGS(4730, 4926, 80000, DegradingType.BARROWS);

        DegradingItems(int regularID, int degradeID, int repairPrice, DegradingType degradingType) {
            this.regularID = regularID;
            this.degradeID = degradeID;
            this.repairPrice = repairPrice;
            this.degradingType = degradingType;
        }

        private int regularID;
        private int degradeID;
        private int repairPrice;
        private DegradingType degradingType;

        /**
         * Gets the regular id for degrading items
         * @return
         */
        public int getRegularID() {
            return regularID;
        }

        /**
         * Gets the degraded item ids
         * @return
         */
        public int getDegradeID() {
            return degradeID;
        }

        /**
         * Gets the repair price to repair degraded items
         * @return
         */
        public int getRepairPrice() {
            return repairPrice;
        }

        /**
         * Gets the type of item you are degrading
         * @return
         */
        public DegradingType getDegradingType() {
            return degradingType;
        }
    }

    /**
     * Processes the actual degrade of the item.
     * @param player
     * @param isAttacker
     */
    public void processDegrade(Player player, boolean isAttacker) {
        for (DegradingItems degradingItems: DegradingItems.values()) {
            for (Item equipmentItem: player.getEquipment().getItems()) {
                if(equipmentItem.getId() == degradingItems.getRegularID()) {
                    ItemDefinition equipmentDefinition = ItemDefinition.forId(degradingItems.getRegularID());
                    if(isAttacker) {
                        if(equipmentDefinition.getEquipmentSlot() != Equipment.WEAPON_SLOT) {
                            continue;
                        }
                    } else if(!isAttacker) {
                        if(equipmentDefinition.getEquipmentSlot() == Equipment.WEAPON_SLOT) {
                            continue;
                        }
                    }
                    decrementCharges(degradingItems);
                    switch (degradingItems.getDegradingType()) {
                        case PVP:
                            player.getEquipment().setItem(equipmentDefinition.getEquipmentSlot(), new Item(degradingItems.getDegradeID(), 1)).refreshItems();
                            maxCharges(degradingItems);
                            player.getUpdateFlag().flag(Flag.APPEARANCE);
                            player.getPacketSender().sendMessage("Your "+equipmentDefinition.getName()+" has degraded.");
                            break;
                        case BARROWS:
                            //TODO: Add barrows degrading
                            break;
                        case DEFAULT:
//                            if(getCharges(degradingItems) == 0) {
//                                player.getEquipment().setItem(equipmentDefinition.getEquipmentSlot(), new Item(-1)).refreshItems();
//                                player.getUpdateFlag().flag(Flag.APPEARANCE);
//                                player.getPacketSender().sendMessage("<col=ff0000>Your "+equipmentDefinition.getName()+" has degraded fully and disappeared!");
//                            }
                            break;
                    }
                } else if(equipmentItem.getId() == degradingItems.getDegradeID()) {
                    ItemDefinition equipmentDefinition = ItemDefinition.forId(degradingItems.getRegularID());
                    if(isAttacker) {
                        if(equipmentDefinition.getEquipmentSlot() != Equipment.WEAPON_SLOT) {
                            continue;
                        }
                    } else if(!isAttacker) {
                        if(equipmentDefinition.getEquipmentSlot() == Equipment.WEAPON_SLOT) {
                            continue;
                        }
                    }
                    decrementCharges(degradingItems);
                    switch (degradingItems.getDegradingType()) {
                        case PVP:
                            if(getCharges(degradingItems) == 10) {
                                player.getPacketSender().sendMessage("<col=ff0000>Warning! Your "+equipmentDefinition.getName()+" will completely degrade and disappear in 10 hits.");
                            } else if(getCharges(degradingItems) == 0) {
                                player.getEquipment().setItem(equipmentDefinition.getEquipmentSlot(), new Item(-1)).refreshItems();
                                player.getUpdateFlag().flag(Flag.APPEARANCE);
                                player.getPacketSender().sendMessage("Your " + equipmentDefinition.getName() + " has completely degraded and disappeared.");
                            }
                            break;
                        case BARROWS:
                            //TODO: Add barrows degrading
                            break;
                        case DEFAULT:
                            if(getCharges(degradingItems) == 0) {
                                player.getEquipment().setItem(equipmentDefinition.getEquipmentSlot(), new Item(-1)).refreshItems();
                                player.getUpdateFlag().flag(Flag.APPEARANCE);
                                player.getPacketSender().sendMessage("<col=ff0000>Your "+equipmentDefinition.getName()+" has degraded fully and disappeared!");
                            }
                            break;
                    }

                }
            }
        }
    }

    /**
     * Tells you how many charges you have left on your item.
     * @param player
     * @param slot
     * @return
     */
    public boolean isCheckCharges(Player player, int itemId, int slot) {
        if(player.getInventory().getItems()[slot] == null) {
            return false;
        }
        for (DegradingItems degradingItems: DegradingItems.values()) {
            if(player.getInventory().getItems()[slot].getId() == itemId) {
                Item inventoryItem = player.getInventory().getItems()[slot];
                if (inventoryItem.getId() == degradingItems.getRegularID()) {
                    ItemDefinition equipmentDefinition = ItemDefinition.forId(degradingItems.getRegularID());
                    int currentCharges = getCharges(degradingItems);
                    double percentage = (double) currentCharges / equipmentDefinition.getCharges() * 100;
                    DecimalFormat format = new DecimalFormat("#.##");
                    percentage = Double.valueOf(format.format(percentage));
                    player.getPacketSender().sendMessage("You have " + Misc.format(currentCharges) + "/" + Misc.format(equipmentDefinition.getCharges()) + " charges (" + percentage + "%) left on your " + equipmentDefinition.getName() + ".");
                    return true;
                }
            }
        }
        return false;
    }
}
