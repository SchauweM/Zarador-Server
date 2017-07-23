package org.scripts.kotlin.content.dialog.npcs;

import static org.scripts.kotlin.content.dialog.npcs.PkSetsDialogue.PkSets.buyRunes;

import com.zarador.model.Item;
import com.zarador.model.options.fiveoption.FiveOption;
import com.zarador.model.options.threeoption.ThreeOption;
import com.zarador.model.player.GameMode;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.util.Misc;
import com.zarador.world.entity.impl.player.Player;

public class PkSetsDialogue extends Dialog {
    public enum PkSets {

        PURE_SET(750000,
                new Item[] {new Item(662), new Item(6107), new Item(6108), new Item(3105), new Item(4587),
                        new Item(1215), new Item(1191), new Item(11118), new Item(2550), new Item(1704),
                        new Item(386, 300), new Item(2441, 15), new Item(2437, 15), new Item(3025, 15)}),

        HYBRIDING_MAIN_SET(2500000,
                new Item[] {new Item(10828), new Item(1704), new Item(4091), new Item(4093), new Item(2503),
                        new Item(2497), new Item(3105), new Item(4675), new Item(13734), new Item(1725),
                        new Item(1127), new Item(1163), new Item(4587), new Item(1215), new Item(1187),
                        new Item(6568), new Item(555, 600), new Item(560, 400), new Item(565, 200),
                        new Item(386, 100), new Item(6686, 15), new Item(3025, 25), new Item(3041, 15),
                        new Item(2441, 15), new Item(2437, 15), new Item(2443, 15)}),

        RANGE_MAIN_SET(1000000,
                new Item[] {new Item(10828), new Item(1704), new Item(10499), new Item(4131), new Item(1079),
                        new Item(2503), new Item(9185), new Item(2491), new Item(9185), new Item(9244, 50),
                        new Item(861), new Item(892, 100), new Item(13734), new Item(560, 100),
                        new Item(9075, 200), new Item(557, 500), new Item(386, 100), new Item(2445, 15),
                        new Item(3025, 15), new Item(2443, 15)}),

        MELEE_MAIN_SET(1500000,
                new Item[] {new Item(10828), new Item(1725), new Item(2550), new Item(1127), new Item(1079),
                        new Item(4131), new Item(4587), new Item(1215), new Item(1187), new Item(6568),
                        new Item(560, 100), new Item(9075, 200), new Item(557, 500), new Item(386, 100),
                        new Item(2441, 15), new Item(2437, 15), new Item(2443, 15), new Item(3025, 25)}),

        MAGIC_MAIN_SET(1000000,
                new Item[] {new Item(3755), new Item(1704), new Item(4091), new Item(4093), new Item(4097),
                        new Item(4675), new Item(2550), new Item(13734), new Item(555, 600), new Item(560, 400),
                        new Item(565, 200), new Item(386, 100), new Item(3041, 15), new Item(3025, 25)});

        PkSets(int cost, Item[] items) {
            this.cost = cost;
            this.items = items;
        }

        private int cost;
        private Item[] items;

        public static void buyRunes(Player player, int setNumber) {
            int[] prices = { 160_000, 1_600_000, 140_000, 1_400_000 };
            switch (setNumber) {
                case 0:
                    if (player.getMoneyInPouch() >= prices[setNumber]) {
                        if (player.getInventory().getFreeSlots() >= 3) {
                            player.setMoneyInPouch(player.getMoneyInPouch() - prices[setNumber]);
                            player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch() + "");
                            player.getInventory().add(565, 200);
                            player.getInventory().add(560, 400);
                            player.getInventory().add(555, 600);
                        }
                        player.getPacketSender().sendInterfaceRemoval();
                    } else {
                        player.getPacketSender().sendInterfaceRemoval();
                        player.getPacketSender().sendMessage("You do not have enough money in your money pouch to buy this.");
                    }
                break;
                case 1:
                    if (player.getMoneyInPouch() >= prices[setNumber]) {
                        if (player.getInventory().getFreeSlots() >= 3) {
                            player.setMoneyInPouch(player.getMoneyInPouch() - prices[setNumber]);
                            player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch() + "");
                            player.getInventory().add(565, 2000);
                            player.getInventory().add(560, 4000);
                            player.getInventory().add(555, 6000);
                        }
                        player.getPacketSender().sendInterfaceRemoval();
                    } else {
                        player.getPacketSender().sendInterfaceRemoval();
                        player.getPacketSender().sendMessage("You do not have enough money in your money pouch to buy this.");
                    }
                break;
                case 2:
                    if (player.getMoneyInPouch() >= prices[setNumber]) {
                        if (player.getInventory().getFreeSlots() >= 3) {
                            player.setMoneyInPouch(player.getMoneyInPouch() - prices[setNumber]);
                            player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch() + "");
                            player.getInventory().add(560, 200);
                            player.getInventory().add(9075, 400);
                            player.getInventory().add(557, 1000);
                        }
                        player.getPacketSender().sendInterfaceRemoval();
                    } else {
                        player.getPacketSender().sendInterfaceRemoval();
                        player.getPacketSender().sendMessage("You do not have enough money in your money pouch to buy this.");
                    }
                break;
                case 3:
                    if (player.getMoneyInPouch() >= prices[setNumber]) {
                        if (player.getInventory().getFreeSlots() >= 3) {
                            player.setMoneyInPouch(player.getMoneyInPouch() - prices[setNumber]);
                            player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch() + "");
                            player.getInventory().add(560, 2000);
                            player.getInventory().add(9075, 4000);
                            player.getInventory().add(557, 10000);
                        }
                        player.getPacketSender().sendInterfaceRemoval();
                    } else {
                        player.getPacketSender().sendInterfaceRemoval();
                        player.getPacketSender().sendMessage("You do not have enough money in your money pouch to buy this.");
                    }
                break;
            }
        }

        public static void buySet(Player player, PkSets set) {
            player.getPacketSender().sendInterfaceRemoval();
            if (player.getGameModeAssistant().getGameMode() == GameMode.IRONMAN ) {
                player.getPacketSender().sendMessage(
                        "You're unable to access this shop because you're an " + player.getGameModeAssistant().getModeName() + ".");
                return;
            }
            if (player.getInventory().getFreeSlots() < set.items.length) {
                player.getPacketSender().sendMessage(
                        "You need at least " + set.items.length + " free inventory slots to buy this set.");
                return;
            }
            int cost = set.cost;
            boolean usePouch = player.getMoneyInPouch() >= cost;
            int plrMoney =
                    (int) (usePouch ? player.getMoneyInPouchAsInt() : player.getInventory().getAmount(995));
            if (plrMoney < cost) {
                player.getPacketSender().sendMessage("You do not have enough money to buy this set. It costs "
                        + Misc.insertCommasToNumber("" + set.cost) + " coins.");
                return;
            }
            if (usePouch) {
                player.setMoneyInPouch(player.getMoneyInPouch() - cost);
                player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
            } else {
                player.getInventory().delete(995, cost);
            }
            player.getInventory().addItems(set.items, true);
            player.getClickDelay().reset();
            player.getPacketSender().sendMessage("You've bought a pvp set.");
        }
    }
    public Dialog dialog = this;

    public PkSetsDialogue(Player player) {
        super(player);
        setEndState(3);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.PLAIN_EVIL, "Hello " + getPlayer().getUsername() + "! Do you want to buy some pk quick sets?");
            case 1:
                return Dialog.createOption(new ThreeOption(
                        "Buy Armour Sets",
                        "Buy Rune Sets",
                        "Cancel") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_3:
                                setState(2);
                                player.getDialog().sendDialog(dialog);
                                break;
                            case OPTION_2_OF_3:
                                setState(3);
                                player.getDialog().sendDialog(dialog);
                                break;
                            case OPTION_3_OF_3:
                                player.getPacketSender().sendInterfaceRemoval();
                                break;
                        }

                    }
                });
            case 2:
                return Dialog.createOption(new FiveOption(
                        "Pure Set (750k)",
                        "Main Hybrid Set (2.5m)",
                        "Main Range Set (1m)",
                        "Melee Main Set (1.5m)",
                        "Magic Main Set (1m)") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_5:
                                PkSets.buySet(getPlayer(), PkSets.PURE_SET);
                                break;
                            case OPTION_2_OF_5:
                                PkSets.buySet(getPlayer(), PkSets.HYBRIDING_MAIN_SET);
                                break;
                            case OPTION_3_OF_5:
                                PkSets.buySet(getPlayer(), PkSets.RANGE_MAIN_SET);
                                break;
                            case OPTION_4_OF_5:
                                PkSets.buySet(getPlayer(), PkSets.MELEE_MAIN_SET);
                                break;
                            case OPTION_5_OF_5:
                                PkSets.buySet(getPlayer(), PkSets.MAGIC_MAIN_SET);
                                break;
                        }
                    }
                });
            case 3:

                return Dialog.createOption(new FiveOption(
                        "Buy @blu@100@bla@ Barrage Casts 160k",
                        "Buy @blu@1,000@bla@ Barrage Casts 1.6m",
                        "Buy @blu@100@bla@ Vengeance Casts 140k",
                        "Buy @blu@1,000@bla@ Vengeance Casts 1.4m",
                        "Close") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_5:
                                buyRunes(player, 0);
                                break;
                            case OPTION_2_OF_5:
                                buyRunes(player, 1);
                                break;
                            case OPTION_3_OF_5:
                                buyRunes(player, 2);
                                break;
                            case OPTION_4_OF_5:
                                buyRunes(player, 3);
                                break;
                            case OPTION_5_OF_5:
                                player.getPacketSender().sendInterfaceRemoval();
                                break;
                        }

                    }
                });
            }
        return null;
    }
}
