package org.scripts.kotlin.content.dialog.pets;

import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class RockGolem extends Dialog {

    public Dialog dialog = this;

    public RockGolem(Player player) {
        super(player);
        setEndState(4);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createPlayer(DialogHandler.CALM, "So you're made entirely of rocks?");
            case 1:
                return Dialog.createNpc(DialogHandler.CALM, "Not quite, my body is formed mostly of minerals.");
            case 2:
                return Dialog.createPlayer(DialogHandler.CALM, "Aren't minerals just rocks?");
            case 3:
                return Dialog.createNpc(DialogHandler.CALM, "No, rocks are rocks, minerals are minerals. I am formed from minerals.");
            case 4:
                return Dialog.createPlayer(DialogHandler.CALM, "But you're a Rock Golem...");
        }
        return null;
    }
}
