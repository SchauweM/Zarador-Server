package org.scripts.kotlin.content.dialog.npcs;

import com.zarador.GameSettings;
import com.zarador.model.options.twooption.TwoOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.content.pos.PlayerOwnedShops;
import com.zarador.world.content.pos.PosFeaturedShops;
import com.zarador.world.entity.impl.player.Player;

public class POSMerchant2 extends Dialog {
    int x = 0;
    public POSMerchant2(Player player, int x) {
        super(player);
        this.x = x;
        setEndState(1);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "Hello "+getPlayer().getUsername()+", would you like to purchase a featured shop for 10m? It will display for 2 hours with a quick link to your shop.");
            case 1:
            return Dialog.createOption(new TwoOption(
                    "Yes, purchase this slot for 10m",
                    "No, thank you") {
                @Override
                public void execute(Player player, OptionType option) {
                    switch(option) {
                        case OPTION_1_OF_2:
                            player.getPacketSender().sendInterfaceRemoval();
                            if (!GameSettings.POS_ENABLED) {
                                player.getPacketSender().sendMessage("Player owned shops have been disabled.");
                                return;
                            }
                            if (player.getGameModeAssistant().isIronMan()) {
                                player.getPacketSender().sendMessage("Ironmen can't use the player owned shops!");
                                return;
                            }

                            if (player.getMoneyInPouch() >= 10000000) {
                                player.setMoneyInPouch(player.getMoneyInPouch() - 10000000);
                                player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
                                PosFeaturedShops.isEmpty[x] = true;
                                PosFeaturedShops.timeRemaining[x] = System.currentTimeMillis(); //120k ticks = 2hours
                                PosFeaturedShops.shopOwner[x] = player.getUsername();
                                player.getPacketSender().sendMessage("You have just rented featured shop #" + (x + 1) + " for 2 hours.");
                            } else {
                                player.getPacketSender().sendMessage("You do not have 10m coins in your money pouch.");
                            }
                            PlayerOwnedShops.openItemSearch(player, false);
                            break;
                        case OPTION_2_OF_2:
                            player.getPacketSender().sendInterfaceRemoval();
                            break;
                    }
                }
            });
            }
        return null;
    }
}
