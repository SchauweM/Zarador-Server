package org.scripts.kotlin.content.dialog.pets;

import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class GeneralGraardorJr extends Dialog {

    public Dialog dialog = this;

    public GeneralGraardorJr(Player player) {
        super(player);
        setEndState(2);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createPlayer(DialogHandler.CALM, "Not sure this is going to be worth my time but... how are you?");
            case 1:
                return Dialog.createNpc(DialogHandler.CALM, "SFudghoigdfpDSOPGnbSOBNfdb dnopbdnopbddfnopdfpofhdARRRGGGGH");
            case 2:
                return Dialog.createPlayer(DialogHandler.CALM, "Nope. Not worth it.");
        }
        return null;
    }
}
