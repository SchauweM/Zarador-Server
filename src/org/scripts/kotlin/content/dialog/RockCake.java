package org.scripts.kotlin.content.dialog;

import com.zarador.model.CombatIcon;
import com.zarador.model.Hit;
import com.zarador.model.Hitmask;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class RockCake extends Dialog {

    public Dialog dialog = this;

    public RockCake(Player player) {
        super(player);
        setEndState(0);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                getPlayer().dealDamage(null, new Hit(10, Hitmask.RED, CombatIcon.NONE));
                return Dialog.createPlayer(DialogHandler.CALM, "Ow! I nearly broke a tooth!");
        }
        return null;
    }
}
