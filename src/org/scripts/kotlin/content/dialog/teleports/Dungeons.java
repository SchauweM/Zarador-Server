package org.scripts.kotlin.content.dialog.teleports;

import com.zarador.model.Position;
import com.zarador.model.options.fiveoption.FiveOption;
import com.zarador.model.options.threeoption.ThreeOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.content.transportation.TeleportHandler;
import com.zarador.world.entity.impl.player.Player;

public class Dungeons extends Dialog {

    public Dialog dialog = this;

    public Dungeons(Player player) {
        super(player);
        setEndState(0);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createOption(new FiveOption(
                        "Edgeville Dungeon",
                        "Taverley Dungeon",
                        "Brimhaven Dungeon",
                        "Ancient Cavern",
                        "Next Page") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(3097, 9882, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_2_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(2884, 9799, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_3_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(2697, 9564, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_4_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(1748, 5325, 0), player.getSpellbook().getTeleportType());
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
                        "Strykewyrm Cavern",
                        "Ancient Guthix Temple",
                        "Kuradal's Dungeon") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_3:
                                TeleportHandler.teleportPlayer(player, new Position(2731, 5095, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_2_OF_3:
                                TeleportHandler.teleportPlayer(player, new Position(2570, 5735, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_3_OF_3:
                                TeleportHandler.teleportPlayer(player, new Position(1659, 5257, 0), player.getSpellbook().getTeleportType());
                                break;
                        }
                    }
                });
            }
        return null;
    }
}
