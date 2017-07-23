package com.zarador.world.content.wells;

import org.scripts.kotlin.content.dialog.Well.WellFull;

import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.util.Misc;
import com.zarador.util.ShutdownHook;
import com.zarador.world.World;
import com.zarador.world.entity.impl.player.Player;

import java.io.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

/**
 * Created by high105 on 9/16/2016.
 */
public class WellOfGoodness {

    public static final double BONUSPKP = 1.3; //multiplier to pkp
    public static final int BONUSDROPPERCENT = 3; //3% higher rolls
    public static final int BONUSEXPRATE = 30; //30% more xp

    private static final int LEAST_DONATE_AMOUNT_ACCEPTED = 10000; // 1m

    private static final int[] AMOUNT_NEEDED = { 7500000, 7500000, 7500000 };
    private static int[] BONUSES_DURATION = { 120, 120, 120 }; // number in minutes

    private static CopyOnWriteArrayList<Player> DONATORS = new CopyOnWriteArrayList<Player>();

    private static long[] START_TIMER = { 0, 0, 0 };
    private static int[] MONEY_IN_WELL = { 0, 0, 0 };
    private static boolean[] isFull = { false, false, false };

    private static final Logger logger = Logger.getLogger(ShutdownHook.class.getName());

    public static void init() {
        String[] args;
        String line;
            try {
                BufferedReader reader = new BufferedReader(new FileReader("./data/saves/wells.txt"));
                while ((line = reader.readLine()) != null) {
                    if (line.contains("well-exp")) {
                        args = line.split(": ");
                        if (Long.parseLong(args[1]) > 0) {
                            isFull[0] = true;
                            START_TIMER[0] = Long.parseLong(args[1]);
                            MONEY_IN_WELL[0] = AMOUNT_NEEDED[0];
                        }
                    } else if (line.contains("well-drops")) {
                        args = line.split(": ");
                        if (Long.parseLong(args[1]) > 0) {
                            isFull[1] = true;
                            START_TIMER[1] = Long.parseLong(args[1]);
                            MONEY_IN_WELL[1] = AMOUNT_NEEDED[1];
                        }
                    } else if (line.contains("well-pkp")) {
                        args = line.split(": ");
                        if (Long.parseLong(args[1]) > 0) {
                            isFull[2] = true;
                            START_TIMER[2] = Long.parseLong(args[1]);
                            MONEY_IN_WELL[2] = AMOUNT_NEEDED[2];
                        }
                    }
                }
                reader.close();
                logger.info("Loaded data from wells that had money in them...");
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public static void save() {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("./data/saves/wells.txt"));
            out.write("well-exp: " + START_TIMER[0]);
            out.newLine();
            out.write("well-drops: " + START_TIMER[1]);
            out.newLine();
            out.write("well-pkp: " + START_TIMER[2]);
            out.newLine();
            out.close();
            logger.info("Saved the wells in to wells.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static int getWell(String well) {
        if (well == "exp") {
            return 0;
        } else if (well == "drops") {
            return 1;
        } else if (well == "pkp") {
            return 2;
        } else {
            return 5;
        }
    }

    public static int getMissingAmount(String well) {
        if (well == "exp") {
            return (AMOUNT_NEEDED[getWell(well)] - MONEY_IN_WELL[getWell(well)]);
        } else if (well == "drops") {
            return (AMOUNT_NEEDED[getWell(well)] - MONEY_IN_WELL[getWell(well)]);
        } else if (well == "pkp") {
            return (AMOUNT_NEEDED[getWell(well)] - MONEY_IN_WELL[getWell(well)]);
        } else {
            return 0;
        }
    }

    public static boolean checkActive(String well) {
        if (well == "exp") {
            if (MONEY_IN_WELL[getWell(well)] == AMOUNT_NEEDED[getWell(well)]) {
                isFull[getWell(well)] = true;
                return true;
            }
            isFull[getWell(well)] = false;
            return false;
        } else if (well == "drops") {
            if (MONEY_IN_WELL[getWell(well)] == AMOUNT_NEEDED[getWell(well)]) {
                isFull[getWell(well)] = true;
                return true;
            }
            isFull[getWell(well)] = false;
            return false;
        } else if (well == "pkp") {
            if (MONEY_IN_WELL[getWell(well)] == AMOUNT_NEEDED[getWell(well)]) {
                isFull[getWell(well)] = true;
                return true;
            }
            isFull[getWell(well)] = false;
            return false;
        } else {
            return false;
        }
    }

    public static boolean checkFull(Player player, String well) {
        if (well == "exp") {
            if (MONEY_IN_WELL[getWell(well)] == AMOUNT_NEEDED[getWell(well)]) {
                isFull[getWell(well)] = true;
                player.getDialog().sendDialog(new WellFull(player));
                return true;
            }
            isFull[getWell(well)] = false;
            return false;
        } else if (well == "drops") {
            if (MONEY_IN_WELL[getWell(well)] == AMOUNT_NEEDED[getWell(well)]) {
                isFull[getWell(well)] = true;
                player.getDialog().sendDialog(new WellFull(player));
                return true;
            }
            isFull[getWell(well)] = false;
            return false;
        } else if (well == "pkp") {
            if (MONEY_IN_WELL[getWell(well)] == AMOUNT_NEEDED[getWell(well)]) {
                isFull[getWell(well)] = true;
                player.getDialog().sendDialog(new WellFull(player));
                return true;
            }
            isFull[getWell(well)] = false;
            return false;
        }
        return false;
    }

    public static void donate(Player player, int amount, String well) {
        if (checkFull(player, well)) {
            return;
        }
        if (amount < LEAST_DONATE_AMOUNT_ACCEPTED) {
            Dialog.createStatement(DialogHandler.CALM, "You must donate at least 10k coins.");
            return;
        }
        if (amount > getMissingAmount(well)) {
            amount = getMissingAmount(well);
        }
        boolean usePouch = player.getMoneyInPouch() >= amount;
        if (!usePouch && player.getInventory().getAmount(995) < amount) {
            Dialog.createStatement(DialogHandler.CALM, "You do not have that much money in your inventory or money pouch.");
            return;
        }
        if (usePouch) {
            player.setMoneyInPouch(player.getMoneyInPouch() - amount);
            player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
        } else {
            player.getInventory().delete(995, amount);
        }
//        if (!DONATORS.contains(player)) {
//            DONATORS.add(player);
//        }
        MONEY_IN_WELL[getWell(well)] += amount;
        if (amount > 20000) {
            switch (getWell(well)) {
                case 0:
                    World.sendMessage("<img=4> <col=6666FF>" + player.getUsername() + " has donated "
                            + Misc.insertCommasToNumber("" + amount + "") + " coins to the Well of Exp!");
                    break;
                case 1:
                    World.sendMessage("<img=4> <col=6666FF>" + player.getUsername() + " has donated "
                            + Misc.insertCommasToNumber("" + amount + "") + " coins to the Well of Wealth!");
                    break;
                case 2:
                    World.sendMessage("<img=4> <col=6666FF>" + player.getUsername() + " has donated "
                            + Misc.insertCommasToNumber("" + amount + "") + " coins to the Well of Execution!");
                    break;
            }
        }
        String wellName = "";
        if (getMissingAmount(well) <= 0) {
            switch(getWell(well)) {
                case 0:
                    wellName = "Exp";
                    isFull[getWell(well)] = true;
                    START_TIMER[getWell(well)] = System.currentTimeMillis();
                    World.sendMessage("<img=4> <col=6666FF>The Well of Exp has been filled!");
                    World.sendMessage("<img=4> <col=6666FF>It is now granting everyone 2 hours of 30% bonus experience.");
                    break;
                case 1:
                    wellName = "Wealth";
                    isFull[getWell(well)] = true;
                    START_TIMER[getWell(well)] = System.currentTimeMillis();
                    World.sendMessage("<img=4> <col=6666FF>The Well of Wealth has been filled!");
                    World.sendMessage("<img=4> <col=6666FF>It is now granting everyone 2 hours of 10% extra drop rate.");
                    break;
                case 2:
                    wellName = "Execution";
                    isFull[getWell(well)] = true;
                    START_TIMER[getWell(well)] = System.currentTimeMillis();
                    World.sendMessage("<img=4> <col=6666FF>The Well of Execution has been filled!");
                    World.sendMessage("<img=4> <col=6666FF>It is now granting everyone 2 hours of 30% extra pk points.");
                    break;
            }
        }
        player.getPacketSender().sendMessage("You donated " + Misc.insertCommasToNumber("" + amount) + " to the Well of " + wellName + ". Thanks for donating.");
    }

    public static int getMinutesRemaining(String well) {
        return (BONUSES_DURATION[getWell(well)] - Misc.getMinutesPassed(System.currentTimeMillis() - START_TIMER[getWell(well)]));
    }

    public static void setDefaults(String well) {
        isFull[getWell(well)] = false;
        START_TIMER[getWell(well)] = 0;
        MONEY_IN_WELL[getWell(well)] = 0;
    }

    public static boolean isActive(String well) {
        if (isFull[getWell(well)] == true) {
            updateState(well);
            return true;
        } else {
            updateState(well);
            return false;
        }
    }

    public static void updateState(String well) {
        if (isFull[getWell(well)]) {
            if (getMinutesRemaining(well) <= 0) {
                if (well == "exp") {
                    World.sendMessage("<img=4> <col=6666FF>The Well of Exp is no longer granting bonus experience.");
                    setDefaults(well);
                } else if (well == "drops") {
                    World.sendMessage("<img=4> <col=6666FF>The Well of Wealth is no longer granting bonus drop rates.");
                    setDefaults(well);
                } else if (well == "pkp") {
                    World.sendMessage("<img=4> <col=6666FF>The Well of Execution is no longer granting bonus pkp.");
                    setDefaults(well);
                }
            }
        }
    }
}
