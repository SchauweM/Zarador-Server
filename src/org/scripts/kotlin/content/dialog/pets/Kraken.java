package org.scripts.kotlin.content.dialog.pets;

import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class Kraken extends Dialog {

    public Dialog dialog = this;

    public Kraken(Player player) {
        super(player);
        setEndState(4);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createPlayer(DialogHandler.CALM, "What's Kraken?");
            case 1:
                return Dialog.createNpc(DialogHandler.CALM, "Not heard that one before.");
            case 2:
                return Dialog.createPlayer(DialogHandler.CALM, "How are you actually walking on land?");
            case 3:
                return Dialog.createNpc(DialogHandler.CALM, "We have another leg, just below the center of our body that we use to move across solid surfaces.");
            case 4:
                return Dialog.createPlayer(DialogHandler.CALM, "That's.... interesting.");
        }
        return null;
    }
}
