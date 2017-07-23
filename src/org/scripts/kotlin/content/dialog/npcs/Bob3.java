package org.scripts.kotlin.content.dialog.npcs;

import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class Bob3 extends Dialog {

    public Dialog dialog = this;

    public Bob3(Player player) {
        super(player);
        setEndState(0);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "I cannot repair this item. If it is a barrows item try unnoting it.");
        }
        return null;
    }
}
