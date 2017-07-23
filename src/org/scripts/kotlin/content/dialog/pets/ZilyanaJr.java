package org.scripts.kotlin.content.dialog.pets;

import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class ZilyanaJr extends Dialog {

    public Dialog dialog = this;

    public ZilyanaJr(Player player) {
        super(player);
        int endState = 3;
        if(player.getInventory().contains(11698) || player.getInventory().contains(11699) || player.getEquipment().contains(11698)) {
            setEndState(3);
        } else {
            setEndState(1);
        }
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createPlayer(DialogHandler.CALM, "FIND THE GODSWORD!");
            case 1:
                return Dialog.createNpc(DialogHandler.CALM, "FIND THE GODSWORD!");
            case 2:
                return Dialog.createPlayer(DialogHandler.CALM, "I FOUND THE GODSWORD!");
            case 3:
                return Dialog.createNpc(DialogHandler.CALM, "GOOD!!!!!");
        }
        return null;
    }
}
