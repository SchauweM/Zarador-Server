package org.scripts.kotlin.content.dialog.teleports;

import com.zarador.model.Position;
import com.zarador.model.options.fiveoption.FiveOption;
import com.zarador.model.options.threeoption.ThreeOption;
import com.zarador.model.options.twooption.TwoOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.content.transportation.TeleportHandler;
import com.zarador.world.entity.impl.player.Player;

public class TrainingTeleports extends Dialog {

    public Dialog dialog = this;

    public TrainingTeleports(Player player, int state) {
        super(player);
        setState(state);
        setEndState(2);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createOption(new FiveOption(
                        "Rock Crabs",
                        "Experiments",
                        "Yaks",
                        "Bandits",
                        "Slayer Tower") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_5:
                                setState(1);
                                player.getDialog().sendDialog(dialog);
                                break;
                            case OPTION_2_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(3559, 9946, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_3_OF_5:
                                setState(2);
                                player.getDialog().sendDialog(dialog);
                                break;
                            case OPTION_4_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(3172, 2981, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_5_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(3427, 3537, 0), player.getSpellbook().getTeleportType());
                                break;
                        }
                    }
                });
            case 1:
                return Dialog.createOption(new ThreeOption(
                        "Relleka",
                        "Lunar Isle",
                        "Waterbirth Island") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_3:
                                TeleportHandler.teleportPlayer(player, new Position(2679, 3720, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_2_OF_3:
                                TeleportHandler.teleportPlayer(player, new Position(2113, 3942, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_3_OF_3:
                                TeleportHandler.teleportPlayer(player, new Position(2547, 3759, 0), player.getSpellbook().getTeleportType());
                                break;
                        }
                    }
                });
            case 2:
                return Dialog.createOption(new TwoOption(
                        "Lumbridge",
                        "Neitiznot") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_2:
                                TeleportHandler.teleportPlayer(player, new Position(3216, 3261, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_2_OF_2:
                                TeleportHandler.teleportPlayer(player, new Position(2326, 3804, 0), player.getSpellbook().getTeleportType());
                                break;
                        }
                    }
                });
            }
        return null;
    }
}
