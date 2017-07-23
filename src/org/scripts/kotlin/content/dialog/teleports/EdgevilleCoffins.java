package org.scripts.kotlin.content.dialog.teleports;

import com.zarador.model.Position;
import com.zarador.model.options.fouroption.FourOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.content.transportation.TeleportHandler;
import com.zarador.world.entity.impl.player.Player;

public class EdgevilleCoffins extends Dialog {

    public Dialog dialog = this;

    public EdgevilleCoffins(Player player) {
        super(player);
        setEndState(1);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.SCARED, "Be careful! All of these locations are deep wilderness!");
            case 1:
                return Dialog.createOption(new FourOption(
                        "Ice Plateau (51 Wilderness)",
                        "Wilderness Castle (51 Wilderness)",
                        "Greater Demons (47 Wilderness)",
                        "Wilderness Lever (51 Wilderness)") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_4:
                                TeleportHandler.teleportPlayer(player, new Position(2971, 3915, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_2_OF_4:
                                TeleportHandler.teleportPlayer(player, new Position(3293, 3926, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_3_OF_4:
                                TeleportHandler.teleportPlayer(player, new Position(3303, 3889, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_4_OF_4:
                                TeleportHandler.teleportPlayer(player, new Position(3154, 3923, 0), player.getSpellbook().getTeleportType());
                                break;
                        }
                    }
                });
        }
        return null;
    }
}
