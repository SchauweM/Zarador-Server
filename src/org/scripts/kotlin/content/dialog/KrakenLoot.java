package org.scripts.kotlin.content.dialog;

import com.zarador.model.options.twooption.TwoOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class KrakenLoot extends Dialog {

    public Dialog dialog = this;

    public KrakenLoot(Player player) {
        super(player);
        setEndState(4);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "Hooow dare you kill me and take my stuff?! I demand a rematch!");
            case 1:
                return Dialog.createPlayer(DialogHandler.CALM, "Hmm... Let me think about it.");
            case 2:
                return Dialog.createOption(new TwoOption(
                        "Rematch Kraken",
                        "No, don't rematch") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_2:
                                player.getKraken().enter(player, false);
                                player.getPacketSender().sendInterfaceRemoval();
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
