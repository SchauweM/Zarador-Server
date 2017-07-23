package org.scripts.kotlin.content.dialog.pets;

import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class Hellpuppy2 extends Dialog {

    public Dialog dialog = this;

    public Hellpuppy2(Player player) {
        super(player);
        setEndState(1);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createPlayer(DialogHandler.CALM, "I wonder if I need to invest in a trowel when I take you out for a walk.");
            case 1:
                return Dialog.createNpc(DialogHandler.CALM, "More like a shovel.");
        }
        return null;
    }
}
