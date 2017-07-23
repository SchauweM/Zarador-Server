package org.scripts.kotlin.content.dialog.pets;

import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class DagannothPrimeJr extends Dialog {

    public Dialog dialog = this;

    public DagannothPrimeJr(Player player) {
        super(player);
        setEndState(6);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createPlayer(DialogHandler.CALM, "So despite there being three kings, you're clearly the leader, right?");
            case 1:
                return Dialog.createNpc(DialogHandler.CALM, "Definitely.");
            case 2:
                return Dialog.createPlayer(DialogHandler.CALM, "I'm glad I got you as a pet.");
            case 3:
                return Dialog.createNpc(DialogHandler.CALM, "Ugh. Human, I'm not a pet.");
            case 4:
                return Dialog.createPlayer(DialogHandler.CALM, "Stop following me then.");
            case 5:
                return Dialog.createNpc(DialogHandler.CALM, "I can't seem to stop.");
            case 6:
                return Dialog.createPlayer(DialogHandler.CALM, "Pet.");

        }
        return null;
    }
}
