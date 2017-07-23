package org.scripts.kotlin.content.dialog.Slayer;

import com.zarador.model.options.twooption.TwoOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class Rewards extends Dialog {

    public Dialog dialog = this;

    public Rewards(Player player) {
        super(player);
        setEndState(1);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.SCARED, "Resetting your slayer task costs 5 slayer points. It also resets your streak.");
            case 1:
                return Dialog.createOption(new TwoOption(
                        "Reset my task",
                        "I'll keep my task") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_2:
                                break;
                            case OPTION_2_OF_2:
                                break;
                        }
                    }
                });
        }
        return null;
    }
}
