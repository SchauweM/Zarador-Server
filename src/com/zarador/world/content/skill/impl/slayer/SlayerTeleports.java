package com.zarador.world.content.skill.impl.slayer;

import com.zarador.model.Direction;
import com.zarador.model.Position;
import com.zarador.model.options.fiveoption.FiveOption;
import com.zarador.model.options.threeoption.ThreeOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.content.transportation.TeleportHandler;
import com.zarador.world.content.transportation.TeleportType;
import com.zarador.world.entity.impl.player.Player;

public class SlayerTeleports extends Dialog {

    public Dialog dialog = this;

    public SlayerTeleports(Player player) {
        super(player);
        setEndState(2);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                    return Dialog.createOption(new FiveOption(
                            "Turadel (Level 3 Minimum)",
                            "Mazchna (Level 20 Minimum)",
                            "Vannaka (Level 40 Minimum)",
                            "Chaeldar (Level 70 Minimum)",
                            "Next Page") {
                        @Override
                        public void execute(Player player, OptionType option) {
                            switch (option) {
                                case OPTION_1_OF_5:
                                    TeleportHandler.teleportPlayer(player, new Position(2929, 3536, 0), TeleportType.NORMAL);
                                    break;
                                case OPTION_2_OF_5:
                                    TeleportHandler.teleportPlayer(player, new Position(3505, 3501, 0), TeleportType.NORMAL);
                                    break;
                                case OPTION_3_OF_5:
                                    TeleportHandler.teleportPlayer(player, new Position(3146, 9914, 0), TeleportType.NORMAL);
                                    break;
                                case OPTION_4_OF_5:
                                    TeleportHandler.teleportPlayer(player, new Position(2446, 4429, 0), TeleportType.NORMAL);
                                    player.setDirection(Direction.NORTH);
                                    break;
                                case OPTION_5_OF_5:
                                    setState(1);
                                    player.getDialog().sendDialog(dialog);
                                    break;
                            }
                        }
                    });
                    case 1:
                        return Dialog.createOption(new ThreeOption(
                                "Nieve (Level 90 Minimum)",
                                "Duradel (Level 120 Minimum)",
                                "Previous Page") {
                            @Override
                            public void execute(Player player, OptionType option) {
                                switch (option) {
                                    case OPTION_1_OF_3:
                                        TeleportHandler.teleportPlayer(player, new Position(2436, 3421, 0), TeleportType.NORMAL);
                                        break;
                                    case OPTION_2_OF_3:
                                        TeleportHandler.teleportPlayer(player, new Position(2970, 3342, 0), TeleportType.NORMAL);
                                        player.setDirection(Direction.SOUTH);
                                        break;
                                    case OPTION_3_OF_3:
                                        setState(0);
                                        player.getDialog().sendDialog(dialog);
                                        break;
                                }
                            }
                });
        }
        return null;
    }
}
