package org.scripts.kotlin.content.dialog.pets;

import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class VetionJr extends Dialog {

    public Dialog dialog = this;

    public VetionJr(Player player) {
        super(player);
        setEndState(5);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createPlayer(DialogHandler.CALM, "Who is the true lord and king of the lands?");
            case 1:
                return Dialog.createNpc(DialogHandler.CALM, "The mighty heir and lord of the Wilderness.");
            case 2:
                return Dialog.createPlayer(DialogHandler.CALM, "Where is he? Why hasn't he lifted your burden?");
            case 3:
                return Dialog.createNpc(DialogHandler.CALM, "I have not fulfilled my purpose.");
            case 4:
                return Dialog.createPlayer(DialogHandler.CALM, "What is your purpose?");
            case 5:
                return Dialog.createNpc(DialogHandler.CALM, "Not what is, what was. A great war tore this land apart and, for my failings in protecting this land, I carry the burden of its waste.");
        }
        return null;
    }
}
