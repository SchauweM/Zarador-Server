package org.scripts.kotlin.content.dialog.pets;

import com.zarador.model.Gender;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.util.Misc;
import com.zarador.world.entity.impl.player.Player;

public class AbyssalOrphan2 extends Dialog {

    public Dialog dialog = this;

    public AbyssalOrphan2(Player player) {
        super(player);
        setEndState(3);
    }

    @Override
    public DialogMessage getMessage() {
        int chance = Misc.inclusiveRandom(1, 2);
        if(getState() == 0) {
            return Dialog.createNpc(DialogHandler.CALM, "You killed my father.");
        }
        if(getPlayer().getAppearance().getGender() == Gender.MALE) {
            switch (getState()) {
                case 1:
                    return Dialog.createPlayer(DialogHandler.CALM, "No, I am your father.");
                case 2:
                    setEndState(2);
                    return Dialog.createNpc(DialogHandler.CALM, "No you're not.");
            }
        } else {
            switch (getState()) {
                case 1:
                    return Dialog.createPlayer(DialogHandler.CALM, "No, I am your father.");
                case 2:
                    setEndState(2);
                    return Dialog.createNpc(DialogHandler.CALM, "Human biology may be unfamiliar to me, but nevertheless I doubt that very much.");
            }
        }
        return null;
    }
}