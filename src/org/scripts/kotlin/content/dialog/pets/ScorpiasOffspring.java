package org.scripts.kotlin.content.dialog.pets;

import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class ScorpiasOffspring extends Dialog {

    public Dialog dialog = this;

    public ScorpiasOffspring(Player player) {
        super(player);
        setEndState(6);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createPlayer(DialogHandler.CALM, "At night time, if I were to hold ultraviolet light over you, would you glow?");
            case 1:
                return Dialog.createNpc(DialogHandler.CALM, "Two things wrong there, human.");
            case 2:
                return Dialog.createPlayer(DialogHandler.CALM, "Oh?");
            case 3:
                return Dialog.createNpc(DialogHandler.CALM, "One, When has it ever been night time here?");
            case 4:
                return Dialog.createNpc(DialogHandler.CALM, "Two, When have you ever seen ultraviolet light around here?");
            case 5:
                return Dialog.createPlayer(DialogHandler.CALM, "Hm...");
            case 6:
                return Dialog.createPlayer(DialogHandler.CALM, "In answer to your question though. Yes I, like every scorpion, would glow.");
        }
        return null;
    }
}
