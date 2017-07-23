package org.scripts.kotlin.content.dialog.pets;

import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class TzRekJad1 extends Dialog {

    public Dialog dialog = this;

    public TzRekJad1(Player player) {
        super(player);
        setEndState(4);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createPlayer(DialogHandler.CALM, "Do you miss your people?");
            case 1:
                return Dialog.createNpc(DialogHandler.CALM, "Mej-TzTok-Jad Kot-Kl! (TzTok-Jad will protect us!)");
            case 2:
                return Dialog.createPlayer(DialogHandler.CALM, "No.. I don't think so.");
            case 3:
                return Dialog.createPlayer(DialogHandler.CALM, "Jal-Zek Kl? (Foreigner hurt us?)");
            case 4:
                return Dialog.createPlayer(DialogHandler.CALM, "No, no, I wouldn't hurt you.");
        }
        return null;
    }
}
