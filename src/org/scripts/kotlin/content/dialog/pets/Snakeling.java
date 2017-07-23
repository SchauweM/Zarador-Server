package org.scripts.kotlin.content.dialog.pets;

import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class Snakeling extends Dialog {

    public Dialog dialog = this;

    public Snakeling(Player player) {
        super(player);
        setEndState(6);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createPlayer(DialogHandler.CALM, "Hey little snake!");
            case 1:
                return Dialog.createNpc(DialogHandler.CALM, "Soon, Zulrah shall establish dominion over this plane.");
            case 2:
                return Dialog.createPlayer(DialogHandler.CALM, "Wanna play fetch?");
            case 3:
                return Dialog.createNpc(DialogHandler.CALM, "Submit to the almighty Zulrah.");
            case 4:
                return Dialog.createPlayer(DialogHandler.CALM, "Walkies? Or slidies...?");
            case 5:
                return Dialog.createNpc(DialogHandler.CALM, "Zulrah's wilderness as a God will soon be demonstrated.");
            case 6:
                return Dialog.createPlayer(DialogHandler.CALM, "I give up...");
        }
        return null;
    }
}
