package org.scripts.kotlin.content.dialog;

import com.zarador.model.options.twooption.TwoOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.content.PlayerPanel;
import com.zarador.world.entity.impl.player.Player;

public class Rfd extends Dialog {

    public Dialog dialog = this;

    public Rfd(Player player, int setS) {
        super(player);
        setState(setS);
        setEndState(6);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.PLAIN_EVIL, "Please! You must help me, adventurer! The Culinaromancer is back!" +
                        " He's going to destroy us! Will you help defeat him?");
            case 1:
                setEndState(1);
                return Dialog.createOption(new TwoOption(
                        "Of course I'll help you with this quest!",
                        "No, I'm a coward") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_2:
                                player.getPacketSender().sendInterfaceRemoval();
                                player.getMinigameAttributes().getRecipeForDisasterAttributes().setPartFinished(0, true);
                                PlayerPanel.refreshPanel(player);
                                break;
                            case OPTION_2_OF_2:
                                player.getPacketSender().sendInterfaceRemoval();
                                break;
                        }
                    }
                });
            case 2:
                return Dialog.createPlayer(DialogHandler.PLAIN_EVIL, "How do I defeat the Culinaromancer?");
            case 3:
                return Dialog.createNpc(DialogHandler.PLAIN_EVIL, "Enter the portal and gain the Culinaromancer's attention by defeating all his servants." +
                        " Once you've defeated all of them, the Culinaromancer will probably fight you himself. You must slay him!");
            case 4:
                return Dialog.createNpc(DialogHandler.PLAIN_EVIL, "Every servant that you defeat will unlock deeper access into the Culinaromancer's chest which is located next to me.");
            case 5:
                setEndState(5);
                return Dialog.createNpc(DialogHandler.PLAIN_EVIL, "Thank you for helping adventurer! I wish you the best of luck!");
            case 6:
                setEndState(6);
                return Dialog.createNpc(DialogHandler.PLAIN_EVIL, "You're a true hero! You defeated the Culinaromancer!");
        }
        return null;
    }
}
