package org.scripts.kotlin.content.dialog.pets;

import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class Hellpuppy1 extends Dialog {

    public Dialog dialog = this;

    public Hellpuppy1(Player player) {
        super(player);
        setEndState(4);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createPlayer(DialogHandler.CALM, "How many souls have you devoured?");
            case 1:
                return Dialog.createNpc(DialogHandler.CALM, "None.");
            case 2:
                return Dialog.createPlayer(DialogHandler.CALM, "Awww p-");
            case 3:
                return Dialog.createNpc(DialogHandler.CALM, "Yet.");
            case 4:
                return Dialog.createPlayer(DialogHandler.CALM, "Oh.");
        }
        return null;
    }
}
