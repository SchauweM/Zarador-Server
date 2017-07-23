package org.scripts.kotlin.content.dialog.pets;

import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class ChaosElementalJr extends Dialog {

    public Dialog dialog = this;

    public ChaosElementalJr(Player player) {
        super(player);
        setEndState(3);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createPlayer(DialogHandler.CALM, "Is it true a level 3 skiller caught one of your siblings?");
            case 1:
                return Dialog.createNpc(DialogHandler.CALM, "Yes, they killed my mummy, kidnapped my brother, smiled about it and went to sleep.");
            case 2:
                return Dialog.createPlayer(DialogHandler.CALM, "Aww, well you have me now! I shall call you Squishy and you shall be mine and you shall be my Squishy");
            case 3:
                return Dialog.createPlayer(DialogHandler.CALM, "Come on, Squishy come on, little Squishy!");
        }
        return null;
    }
}
