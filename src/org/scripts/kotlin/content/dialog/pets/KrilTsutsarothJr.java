package org.scripts.kotlin.content.dialog.pets;

import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class KrilTsutsarothJr extends Dialog {

    public Dialog dialog = this;

    public KrilTsutsarothJr(Player player) {
        super(player);
        setEndState(6);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createPlayer(DialogHandler.CALM, "How's life in the light?");
            case 1:
                return Dialog.createNpc(DialogHandler.CALM, "Burns slightly.");
            case 2:
                return Dialog.createPlayer(DialogHandler.CALM, "You seem much nicer than your father. He's mean.");
            case 3:
                return Dialog.createNpc(DialogHandler.CALM, "If you were stuck in a very dark cave for centuries you'd be pretty annoyed too.");
            case 4:
                return Dialog.createPlayer(DialogHandler.CALM, "I guess.");
            case 5:
                return Dialog.createNpc(DialogHandler.CALM, "He's actually quite mellow really.");
            case 6:
                return Dialog.createPlayer(DialogHandler.CALM, "Uh.... Yeah.");
        }
        return null;
    }
}
