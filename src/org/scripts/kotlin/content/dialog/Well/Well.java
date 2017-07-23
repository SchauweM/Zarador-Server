package org.scripts.kotlin.content.dialog.Well;

import com.zarador.model.options.fouroption.FourOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class Well extends Dialog {

    public Dialog dialog = this;

    public Well(Player player) {
        super(player);
        setEndState(0);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createOption(new FourOption(
                        "What is the Well",
                        "Look down Well",
                        "Contribute to Well",
                        "Cancel") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_4:
                                player.getDialog().sendDialog(new WellStatement(player));
                                break;
                            case OPTION_2_OF_4:
                                player.getDialog().sendDialog(new LookDownWell(player));
                                break;
                            case OPTION_3_OF_4:
                                player.getDialog().sendDialog(new DonateToWellDial(player));
                                break;
                            case OPTION_4_OF_4:
                                player.getPacketSender().sendInterfaceRemoval();
                                break;
                        }
                    }
                });
        }
        return null;
    }
}
