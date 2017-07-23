package org.scripts.kotlin.content.dialog.pets;

import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class CallistoCub extends Dialog {

    public Dialog dialog = this;

    public CallistoCub(Player player) {
        super(player);
        setEndState(7);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createPlayer(DialogHandler.CALM, "Why the grizzly face?");
            case 1:
                return Dialog.createNpc(DialogHandler.CALM, "You're not funny...");
            case 2:
                return Dialog.createPlayer(DialogHandler.CALM, "You should get in the.... sun more.");
            case 3:
                return Dialog.createNpc(DialogHandler.CALM, "You're really not funny...");
            case 4:
                return Dialog.createPlayer(DialogHandler.CALM, "One second, let me take a picture of you with my.... kodiak camera.");
            case 5:
                return Dialog.createNpc(DialogHandler.CALM, ".....");
            case 6:
                return Dialog.createPlayer(DialogHandler.CALM, "Feeling.... blue.");
            case 7:
                return Dialog.createNpc(DialogHandler.CALM, "If you don't stop, I'm going to leave some... brown... at your feet, human.");
        }
        return null;
    }
}
