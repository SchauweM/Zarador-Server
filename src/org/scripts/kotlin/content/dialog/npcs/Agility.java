package org.scripts.kotlin.content.dialog.npcs;

import com.zarador.model.container.impl.Shop;
import com.zarador.model.input.impl.BuyAgilityExperience;
import com.zarador.model.options.threeoption.ThreeOption;
import com.zarador.model.options.twooption.TwoOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class Agility extends Dialog {

    public Dialog dialog = this;

    public Agility(Player player) {
        super(player);
        setEndState(2);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "Hello! What can I do for you?");
            case 1:
            return Dialog.createOption(new ThreeOption(
                    "I want to buy experience",
                    "I want to buy equipment",
                    "Cancel") {
                @Override
                public void execute(Player player, OptionType option) {
                    switch(option) {
                        case OPTION_1_OF_3:
                            Dialog.createNpc(DialogHandler.CALM, "@bla@Would you like to exchange tickets for experience?" +
                                    " One ticket currently grants @red@7680@bla@ Agility experience.?");
                            setState(2);
                            player.getDialog().sendDialog(dialog);
                            break;
                        case OPTION_2_OF_3:
                            Shop.ShopManager.getShops().get(28).open(player);
                            break;
                        case OPTION_3_OF_3:
                            player.getPacketSender().sendInterfaceRemoval();
                            break;
                    }
                }
            });
            case 2:
                return Dialog.createOption(new TwoOption(
                        "Yes I want to exchange some tickets",
                        "Cancel") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_2:
                                player.getPacketSender().sendInterfaceRemoval();
                                player.getPacketSender().sendEnterAmountPrompt("How many tickets would you like to exchange?");
                                player.setInputHandling(new BuyAgilityExperience());
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
