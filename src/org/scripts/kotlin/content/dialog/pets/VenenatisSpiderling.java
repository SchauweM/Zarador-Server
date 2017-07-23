package org.scripts.kotlin.content.dialog.pets;

import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class VenenatisSpiderling extends Dialog {

    public Dialog dialog = this;

    public VenenatisSpiderling(Player player) {
        super(player);
        setEndState(3);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createPlayer(DialogHandler.CALM, "It's a damn good thing I don't have arachnophobia.");
            case 1:
                return Dialog.createNpc(DialogHandler.CALM, "We're misunderstood. Without us in your house, you'd be infested with flies and other REAL nasties.");
            case 2:
                return Dialog.createPlayer(DialogHandler.CALM, "Thanks for that enlightening fact.");
            case 3:
                return Dialog.createNpc(DialogHandler.CALM, "Everybody gets one.");
        }
        return null;
    }
}
