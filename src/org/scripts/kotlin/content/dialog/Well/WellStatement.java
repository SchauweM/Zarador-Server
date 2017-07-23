package org.scripts.kotlin.content.dialog.Well;

import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class WellStatement extends Dialog {

    public Dialog dialog = this;
    Player player;
    public WellStatement(Player player) {
        super(player);
        setEndState(0);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "The Well of Goodness has 3 different wells inside it. " +
                        "You can donate towards getting bonus xp rates, bonus drop rates and/or bonus pkp. They apply globally to every player online.");
        }
        return null;
    }
}
