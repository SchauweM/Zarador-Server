package org.scripts.kotlin.content.dialog.pets;

import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class Hellpuppy3 extends Dialog {

    public Dialog dialog = this;

    public Hellpuppy3(Player player) {
        super(player);
        setEndState(4);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createPlayer(DialogHandler.CALM, "Why were the hot dogs shivering?");
            case 1:
                return Dialog.createNpc(DialogHandler.CALM, "Grrrrr...");
            case 2:
                return Dialog.createPlayer(DialogHandler.CALM, "Because they were served-");
            case 3:
                return Dialog.createNpc(DialogHandler.CALM, "GRRRRRR...");
            case 4:
                return Dialog.createPlayer(DialogHandler.CALM, "-with... chilli?");
        }
        return null;
    }
}
