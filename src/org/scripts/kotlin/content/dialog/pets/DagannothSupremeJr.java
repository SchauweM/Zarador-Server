package org.scripts.kotlin.content.dialog.pets;

import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class DagannothSupremeJr extends Dialog {

    public Dialog dialog = this;

    public DagannothSupremeJr(Player player) {
        super(player);
        setEndState(2);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createPlayer(DialogHandler.CALM, "Hey, so err... I kind of own you now.");
            case 1:
                return Dialog.createNpc(DialogHandler.CALM, "Tsssk. Next time you enter those caves, human, my father will be having words.");
            case 2:
                return Dialog.createPlayer(DialogHandler.CALM, "Maybe next time I'll add your brothers to my collection.");

        }
        return null;
    }
}
