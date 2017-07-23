package org.scripts.kotlin.content.dialog.Well;

import com.zarador.model.options.threeoption.ThreeOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.util.Misc;
import com.zarador.world.content.wells.WellOfGoodness;
import com.zarador.world.entity.impl.player.Player;

public class LookDownWell extends Dialog {

    public Dialog dialog = this;
    Player p;
    String wellName = "";
    String wellNameActual = "";

    public LookDownWell(Player player) {
        super(player);
        setEndState(1);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createOption(new ThreeOption(
                        "Check Bonus Xp Well", //exp
                        "Check Well of Wealth", //drops
                        "Check Well of Execution") { //pkp
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_3:
                                wellName = "exp";
                                wellNameActual = "exp";
                                setState(1);
                                player.getDialog().sendDialog(dialog);
                                break;
                            case OPTION_2_OF_3:
                                wellName = "drops";
                                wellNameActual = "wealth";
                                setState(1);
                                player.getDialog().sendDialog(dialog);
                                break;
                            case OPTION_3_OF_3:
                                wellName = "pkp";
                                wellNameActual = "execution";
                                setState(1);
                                player.getDialog().sendDialog(dialog);
                                break;
                        }
                    }
                });
            case 1:
                if (WellOfGoodness.checkFull(p, wellName)) {
                    return Dialog.createNpc(DialogHandler.CALM, "The well of " + wellNameActual + "full and bonus is currently active.");
                } else {
                    return Dialog.createNpc(DialogHandler.CALM, "The well of " + wellNameActual + " needs another " + Misc.insertCommasToNumber("" + WellOfGoodness.getMissingAmount(wellName) + "") + " coins before it is active.");
                }
        }
        return null;
    }
}
