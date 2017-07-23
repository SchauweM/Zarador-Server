package org.scripts.kotlin.content.dialog.pets;

import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class Hellpuppy4 extends Dialog {

    public Dialog dialog = this;

    public Hellpuppy4(Player player) {
        super(player);
        setEndState(3);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createPlayer(DialogHandler.CALM, "Hell yeah! Such a cute puppy!");
            case 1:
                return Dialog.createNpc(DialogHandler.CALM, "Silence mortal! Or I'll eat your soul.");
            case 2:
                return Dialog.createPlayer(DialogHandler.CALM, "Would that go well with lemon?");
            case 3:
                return Dialog.createNpc(DialogHandler.CALM, "Grrr...");
        }
        return null;
    }
}
