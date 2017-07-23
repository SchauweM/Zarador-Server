package org.scripts.kotlin.content.dialog.pets;

import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class PrinceBlackDragon extends Dialog {

    public Dialog dialog = this;

    public PrinceBlackDragon(Player player) {
        super(player);
        setEndState(3);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createPlayer(DialogHandler.CALM, "Shouldn't a prince only have two heads?");
            case 1:
                return Dialog.createNpc(DialogHandler.CALM, "Why is that?");
            case 2:
                return Dialog.createPlayer(DialogHandler.CALM, "Well, a standard Black dragon has one, the King has three so inbetween must have two?");
            case 3:
                return Dialog.createNpc(DialogHandler.CALM, "You're overthinking this.");
        }
        return null;
    }
}
