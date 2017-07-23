package org.scripts.kotlin.content.dialog;

import com.zarador.model.Position;
import com.zarador.model.options.twooption.TwoOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class BountyPortal extends Dialog {

    public Dialog dialog = this;

    public BountyPortal(Player player) {
        super(player);
        setEndState(1);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.SCARED, "Would you like to teleport to Bounty Hunter? This will teleport you directly to the wilderness (back side of bounty hunter)!");
            case 1:
                return Dialog.createOption(new TwoOption(
                        "Teleport to Bounty Hunter",
                        "Cancel") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_2:
                                player.moveTo(new Position(3116, 3752, 0));
                                player.getPacketSender().sendInterfaceRemoval();
                                break;
                            case OPTION_2_OF_2:
                                player.getPacketSender().sendInterfaceRemoval();
                                break;
                        }
                    }
                });
        }
        return null;
    }
}
