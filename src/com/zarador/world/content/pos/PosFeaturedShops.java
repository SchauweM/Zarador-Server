package com.zarador.world.content.pos;

import org.scripts.kotlin.content.dialog.npcs.POSMerchant2;

import com.zarador.util.Misc;
import com.zarador.world.entity.impl.player.Player;

/**
 * The Chaos Featured POS
 * @Author High105
 */
public class PosFeaturedShops {

    private final static int SIZE = 10;
    private final static int TIMER = 120; //1 = 1 minute

    public static boolean[] isEmpty = new boolean[SIZE];
    public static long timeRemaining[] = new long[SIZE];
    public static String shopOwner[] = new String[SIZE];

    private static int[] featuredShopNameIds = {41422, 41426, 41430, 41434, 41438,  41442, 41446, 41450, 41454, 41458};
    private static int[] featuredShopLastsIds = {41423, 41427, 41431, 41435, 41439, 41443, 41447, 41451, 41455, 41459};

    /**
     * Purchase or open a featured shop that you click
     * @param player
     * @param x
     */
    public static void buyShop(Player player, int x) {
        if (!isEmpty[x]) {
            for (int i = 0; i < SIZE; i++) {
                if(shopOwner[i] == null) {
                    continue;
                }
                if (shopOwner[i].equalsIgnoreCase(player.getUsername())) {
                    player.getPacketSender().sendMessage("You already purchased a featured slot.");
                    return;
                }
            }
            player.setNpcClickId(2593);
            player.getDialog().sendDialog(new POSMerchant2(player, x));
        } else {
            PlayerOwnedShops.openShop(shopOwner[x], player);
            player.setPlayerOwnedShopping(true);
        }
    }

    /**
     * Handles the buttons for the featured stores
     * @param player
     * @param button
     * @return
     */
    public static boolean handlePosButtons(Player player, int button) {
        switch (button) {
            case 41421:
                buyShop(player, 0);
                return true;
            case 41425:
                buyShop(player, 1);
                return true;
            case 41429:
                buyShop(player, 2);
                return true;
            case 41433:
                buyShop(player, 3);
                return true;
            case 41437:
                buyShop(player, 4);
                return true;
            case 41441:
                buyShop(player, 5);
                return true;
            case 41445:
                buyShop(player, 6);
                return true;
            case 41449:
                buyShop(player, 7);
                return true;
            case 41453:
                buyShop(player, 8);
                return true;
            case 41457:
                buyShop(player, 9);
                return true;
        }
        return false;
    }

    /**
     * Resets the featured shop interface
     * @param player
     */
    public static void resetInterface(Player player) {
        for (int j = 0; j < SIZE; j++) {
            if (!isEmpty[j]) {
                player.getPacketSender().sendString(featuredShopNameIds[j], "Rent Slot");
                player.getPacketSender().sendString(featuredShopLastsIds[j], "Costs: 10m");
            } else {
                player.getPacketSender().sendString(featuredShopLastsIds[j], getTimeRemaining(j));
                if(shopOwner[j] == null) {
                    player.getPacketSender().sendString(featuredShopNameIds[j], "Rent Slot");
                } else {
                    player.getPacketSender().sendString(featuredShopNameIds[j], shopOwner[j]);
                }
            }
        }
    }

    /**
     * When the timer runs out call here
     * @param x which slot 0-9 to reset
     **/
    public static void resetSlot(int x) {
        PosFeaturedShops.isEmpty[x] = false;
        PosFeaturedShops.timeRemaining[x] = 0;
        PosFeaturedShops.shopOwner[x] = null;
    }

    /**
     * Gets the time remaining for the featured shop
     * @param x
     * @return
     */
    public static String getTimeRemaining(int x) {
        int remain = (TIMER - Misc.getMinutesPassed(System.currentTimeMillis() - timeRemaining[x]));
        if(remain <= 0) {
            resetSlot(x);
            return "Costs: 10m";
        }
        if(remain == 120) {
            return "2 hours";
        } else if(remain > 60 && remain <= 120) {
            return "1 hour "+(60 - (120 - remain))+" min";
        } else {
            return ""+(remain)+" min";
        }
    }
}
