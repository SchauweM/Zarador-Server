package org.scripts.kotlin.content.dialog.pets;

import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class KalphitePrincess extends Dialog {

    public Dialog dialog = this;

    public KalphitePrincess(Player player) {
        super(player);
        setEndState(7);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createPlayer(DialogHandler.CALM, "What is it with your kind and potato cactus?");
            case 1:
                return Dialog.createNpc(DialogHandler.CALM, "Truthfully?");
            case 2:
                return Dialog.createPlayer(DialogHandler.CALM, "Yeah, please.");
            case 3:
                return Dialog.createNpc(DialogHandler.CALM, "Soup. We make a fine soup with it.");
            case 4:
                return Dialog.createPlayer(DialogHandler.CALM, "Kalphites can cook?");
            case 5:
                return Dialog.createNpc(DialogHandler.CALM, "Nah, we just collect it and put it there because we know fools like yourself will come down looking for it then inevitably be killed by my mother.");
            case 6:
                return Dialog.createPlayer(DialogHandler.CALM, "Evidently not, that's how I got you!");
            case 7:
                return Dialog.createPlayer(DialogHandler.CALM, "Touché");
        }
        return null;
    }
}
