package org.scripts.kotlin.content.dialog.pets;

import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class DarkCore extends Dialog {

    public Dialog dialog = this;

    public DarkCore(Player player) {
        super(player);
        setEndState(3);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createPlayer(DialogHandler.CALM, "Got any sigils for me?");
            case 1:
                return Dialog.createStatement(DialogHandler.CALM, "The Core shakes its head.");
            case 2:
                return Dialog.createPlayer(DialogHandler.CALM, "Damnit Core-al!");
            case 3:
                return Dialog.createPlayer(DialogHandler.CALM, "Let's bounce!");
        }
        return null;
    }
}
