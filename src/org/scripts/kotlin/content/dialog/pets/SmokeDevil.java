package org.scripts.kotlin.content.dialog.pets;

import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class SmokeDevil extends Dialog {

    public Dialog dialog = this;

    public SmokeDevil(Player player) {
        super(player);
        setEndState(2);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createPlayer(DialogHandler.CALM, "Your kind comes in three different sizes?");
            case 1:
                return Dialog.createNpc(DialogHandler.CALM, "Four, actually.");
            case 2:
                return Dialog.createPlayer(DialogHandler.CALM, "Wow. Whoever created you wasn't very creative. You're just resized versions of one another!");
        }
        return null;
    }
}
