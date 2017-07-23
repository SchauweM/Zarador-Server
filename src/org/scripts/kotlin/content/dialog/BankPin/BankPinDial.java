package org.scripts.kotlin.content.dialog.BankPin;

import com.zarador.model.options.twooption.TwoOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.content.BankPin;
import com.zarador.world.entity.impl.player.Player;

public class BankPinDial extends Dialog {

    public Dialog dialog = this;

    public BankPinDial(Player player) {
        super(player);
        setEndState(1);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "You do not have a bank PIN set. Would you like to set one?");
            case 1:
                return Dialog.createOption(new TwoOption(
                        "Setup Bank Pin",
                        "I don't want my account to be safe") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_2:
                                BankPin.init(player, false);
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
