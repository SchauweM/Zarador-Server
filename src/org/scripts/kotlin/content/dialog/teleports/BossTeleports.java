package org.scripts.kotlin.content.dialog.teleports;

import com.zarador.model.Position;
import com.zarador.model.options.fiveoption.FiveOption;
import com.zarador.model.options.fouroption.FourOption;
import com.zarador.model.options.twooption.TwoOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.content.transportation.TeleportHandler;
import com.zarador.world.entity.impl.player.Player;

public class BossTeleports extends Dialog {

    public Dialog dialog = this;

    public BossTeleports(Player player, int state) {
        super(player);
        setState(state);
        setEndState(4);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createOption(new FiveOption(
                        "Godwars Dungeon",
                        "Ganodermic Beast",
                        "Dagannoth Kings",
                        "Corporeal Beast",
                        "Next Page") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_5:
                                setState(3);
                                player.getDialog().sendDialog(dialog);
                                break;
                            case OPTION_2_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(2245, 3182, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_3_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(1909, 4367, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_4_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(2916, 4384, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_5_OF_5:
                                setState(1);
                                player.getDialog().sendDialog(dialog);
                                break;
                        }
                    }
                });
            case 1:
                return Dialog.createOption(new FiveOption(
                        "Kraken",
                        "King Black Dragon @bla@(@red@Wild@bla@)",
                        "Nex",
                        "Cerberus",
                        "Next Page") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_5:
                                player.getKraken().enter(player, true);
                                break;
                            case OPTION_2_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(2999, 3850, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_3_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(2903, 5204, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_4_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(1240, 1226, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_5_OF_5:
                                setState(2);
                                player.getDialog().sendDialog(dialog);
                                break;
                        }
                    }
                });
            case 2:
                return Dialog.createOption(new FiveOption(
                        "Kalphite Queen",
                        "Phoenix",
                        "Bandos Avatar",
                        "Glacors",
                        "Next Page") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(3508, 9492, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_2_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(2839, 9557, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_3_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(2891, 4767, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_4_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(3050, 9573, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_5_OF_5:
                                setState(4);
                                player.getDialog().sendDialog(dialog);
                                break;
                        }
                    }
                });
            case 3:
                return Dialog.createOption(new FourOption(
                        "Bandos",
                        "Saradomin",
                        "Zamorak",
                        "Armadyl") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_4:
                                TeleportHandler.teleportPlayer(player, new Position(2845, 5335, 2), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_2_OF_4:
                                TeleportHandler.teleportPlayer(player, new Position(2917, 5272, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_3_OF_4:
                                TeleportHandler.teleportPlayer(player, new Position(2891, 5356, 2), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_4_OF_4:
                                TeleportHandler.teleportPlayer(player, new Position(2872, 5268, 2), player.getSpellbook().getTeleportType());
                                break;
                        }
                    }
                });
            case 4:
                return Dialog.createOption(new TwoOption(
                        "Bork",
                        "Abyssal SIre") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_2:
                                TeleportHandler.teleportPlayer(player, new Position(3102, 2965, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_2_OF_2:
                                TeleportHandler.teleportPlayer(player, new Position(2516, 4636, 0), player.getSpellbook().getTeleportType());
                                break;
                        }
                    }
                });
            }
        return null;
    }
}
