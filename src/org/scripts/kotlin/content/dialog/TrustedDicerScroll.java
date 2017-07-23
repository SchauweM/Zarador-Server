package org.scripts.kotlin.content.dialog;

import com.zarador.model.options.twooption.TwoOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class TrustedDicerScroll extends Dialog {

    public TrustedDicerScroll(Player player) {
        super(player);
        setEndState(1);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "Using this scroll is irreversible, would you like to become a trusted dicer?");
            case 1:
                return Dialog.createOption(new TwoOption(
                    "Claim Trusted Dicer Rank",
                    "I don't want to be a trusted dicer") {
                @Override
                public void execute(Player player, OptionType option) {
                    switch(option) {
                        case OPTION_1_OF_2:
                            if(player.getInventory().contains(6808)) {
                                player.getInventory().delete(6808, 1);
                                player.setTrustedDicer(true);
                            }
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
