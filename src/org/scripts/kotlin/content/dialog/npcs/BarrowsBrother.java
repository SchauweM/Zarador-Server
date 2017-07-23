package org.scripts.kotlin.content.dialog.npcs;

import com.zarador.model.options.twooption.TwoOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.content.minigames.impl.Barrows;
import com.zarador.world.entity.impl.player.Player;

public class BarrowsBrother extends Dialog {

    public BarrowsBrother(Player player, int state) {
        super(player);
        setState(state);
        setEndState(5);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "Hello traveler, where did you come from?");
            case 1:
                return Dialog.createPlayer(DialogHandler.CALM, "Uhmmm I don't know!");
            case 2:
                return Dialog.createNpc(DialogHandler.CALM, "That's alright! I love seeing new visitors here. What can I assist you with?");
            case 3:
                return Dialog.createOption(new TwoOption(
                        "Can I reset my current kill count?",
                        "I need to borrow a spade.") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_2:
                                Barrows.resetBarrows(player);
                                player.getDialog().sendDialog(new BarrowsBrother(player, 4));
                                break;
                            case OPTION_2_OF_2:
                                player.getInventory().add(952, 1);
                                player.getDialog().sendDialog(new BarrowsBrother(player, 5));
                                break;
                        }
                    }
                });
            case 4:
                setEndState(4);
                return Dialog.createNpc(DialogHandler.CALM, "Sure, I have gone ahead and reset your barrows kill count.");
            case 5:
                setEndState(5);
                return Dialog.createNpc(DialogHandler.CALM, "Your in luck! These goons keep dieing and dropping their spades all over the place...");
            }
        return null;
    }
}
