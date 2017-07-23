package org.scripts.kotlin.content.dialog;

import com.zarador.model.options.twooption.TwoOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class SpearOnNpc extends Dialog {

    public SpearOnNpc(Player player, int state) {
        super(player);
        setEndState(3);
        setState(state);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "Hi, I turn your Zamorakian spear into a Zamorakian hasta for 35 million coins.");
            case 1:
            return Dialog.createOption(new TwoOption(
                    "Yes, exchange spear for hasta (35M)",
                    "No, don't exchange.") {
                @Override
                public void execute(Player player, OptionType option) {
                    switch(option) {
                        case OPTION_1_OF_2:
                           if(player.getInventory().getAmount(995) < 35000000) {
                               player.getDialog().sendDialog(new SpearOnNpc(player, 3));
                           } else {
                               if(player.getInventory().contains(11716)) {
                                   player.getInventory().delete(995, 35000000);
                                   player.getInventory().delete(11716, 1);
                                   player.getInventory().add(21120, 1);
                                   player.getDialog().sendDialog(new SpearOnNpc(player, 2));
                               }
                           }
                            break;
                        case OPTION_2_OF_2:
                            player.getPacketSender().sendInterfaceRemoval();
                            break;
                    }
                }
            });
            case 2:
                setEndState(2);
                return Dialog.createNpc(DialogHandler.HAPPY, "Here you go!");
            case 3:
                return Dialog.createNpc(DialogHandler.HAPPY, "You do not have 35 million coins in your inventory!");
            }
        return null;
    }
}
