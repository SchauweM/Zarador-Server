package org.scripts.kotlin.content.dialog.pets;

import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class KreearraJr extends Dialog {

    public Dialog dialog = this;

    public KreearraJr(Player player) {
        super(player);
        setEndState(7);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createPlayer(DialogHandler.CALM, "Huh... that's odd... I thought that would be big news.");
            case 1:
                return Dialog.createNpc(DialogHandler.CALM, "You thought what would be big news?");
            case 2:
                return Dialog.createPlayer(DialogHandler.CALM, "Well there seems to be an absence of a certain ornithological piece: a headline regarding mass awareness of a certain avian variety.");
            case 3:
                return Dialog.createNpc(DialogHandler.CALM, "What are you talking about?");
            case 4:
                return Dialog.createPlayer(DialogHandler.CALM, "Oh have you not heard? It was my understanding that everyone had heard....");
            case 5:
                return Dialog.createNpc(DialogHandler.CALM, "Heard wha...... OH NO!!!!?!?!!?!");
            case 6:
                return Dialog.createPlayer(DialogHandler.CALM, "OH WELL THE BIRD, BIRD, BIRD, BIRD BIRD IS THE WORD. OH WELL THE BIRD, BIRD, BIRD, BIRD BIRD IS THE WORD.");
            case 7:
                return Dialog.createStatement(DialogHandler.CALM, "There's a slight pause as Kree'Arra Jr. goes stiff.");

        }
        return null;
    }
}
