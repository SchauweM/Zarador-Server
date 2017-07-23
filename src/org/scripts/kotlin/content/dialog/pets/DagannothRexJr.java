package org.scripts.kotlin.content.dialog.pets;

import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class DagannothRexJr extends Dialog {

    public Dialog dialog = this;

    public DagannothRexJr(Player player) {
        super(player);
        setEndState(7);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createPlayer(DialogHandler.CALM, "Do you have any berserker rings?");
            case 1:
                return Dialog.createNpc(DialogHandler.CALM, "Nope.");
            case 2:
                return Dialog.createPlayer(DialogHandler.CALM, "You sure?");
            case 3:
                return Dialog.createNpc(DialogHandler.CALM, "Yes.");
            case 4:
                return Dialog.createPlayer(DialogHandler.CALM, "So, if I tipped you upside down and shook you, you'd not drop any berserker rings?");
            case 5:
                return Dialog.createNpc(DialogHandler.CALM, "Nope.");
            case 6:
                return Dialog.createPlayer(DialogHandler.CALM, "What if I endlessly killed your father for weeks on end, would I get one then?");
            case 7:
                return Dialog.createPlayer(DialogHandler.CALM, "Been done by someone, nope.");
        }
        return null;
    }
}
