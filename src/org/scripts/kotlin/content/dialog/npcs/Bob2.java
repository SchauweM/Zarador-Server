package org.scripts.kotlin.content.dialog.npcs;

import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class Bob2 extends Dialog {

    public Dialog dialog = this;

    public Bob2(Player player) {
        super(player);
        setEndState(0);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "You do not have enough money in your money pouch to repair this item.");
        }
        return null;
    }
}
