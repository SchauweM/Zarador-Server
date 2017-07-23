package org.scripts.kotlin.content.dialog.pets;

import com.zarador.model.Gender;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.util.Misc;
import com.zarador.world.entity.impl.player.Player;

public class AbyssalOrphan extends Dialog {

    public Dialog dialog = this;

    public AbyssalOrphan(Player player) {
        super(player);
        setEndState(3);
    }

    @Override
    public DialogMessage getMessage() {
        int chance = Misc.inclusiveRandom(1, 2);
        if(getState() == 0) {
            return Dialog.createNpc(DialogHandler.CALM, "You killed my father.");
        }
        switch (getState()) {
            case 1:
                return Dialog.createPlayer(DialogHandler.CALM, "Yeah, don't take it personally.");
            case 2:
                return Dialog.createNpc(DialogHandler.CALM, "In his dying moment, my father poured his last ounce of strength into my creation. My being is formed from his remains.");
            case 3:
                setEndState(3);
                return Dialog.createNpc(DialogHandler.CALM, "When your own body is consumed to nourish the Nexus, and an army of scions arises from your corpse, I trust you will not take it personally either.");
        }
        return null;
    }
}