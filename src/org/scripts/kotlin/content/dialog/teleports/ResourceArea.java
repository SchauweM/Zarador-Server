package org.scripts.kotlin.content.dialog.teleports;

import com.zarador.model.Position;
import com.zarador.model.options.twooption.TwoOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.content.transportation.TeleportHandler;
import com.zarador.world.entity.impl.player.Player;

public class ResourceArea extends Dialog {

    public Dialog dialog = this;

    public ResourceArea(Player player) {
        super(player);
        setEndState(1);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.SCARED, "Just so you know, this resource area is in the deep wilderness!");
            case 1:
                return Dialog.createOption(new TwoOption(
                        "I'm aware I could be attacked in the wilderness",
                        "Don't teleport me... I like staying safe") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_2:
                                TeleportHandler.teleportPlayer(player, new Position(3181, 3945, 0), player.getSpellbook().getTeleportType());
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
