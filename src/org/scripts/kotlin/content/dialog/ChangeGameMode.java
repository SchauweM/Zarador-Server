package org.scripts.kotlin.content.dialog;

import com.zarador.model.options.threeoption.ThreeOption;
import com.zarador.model.options.twooption.TwoOption;
import com.zarador.model.player.GameMode;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.content.PlayerPanel;
import com.zarador.world.content.Titles;
import com.zarador.world.entity.impl.player.Player;

public class ChangeGameMode extends Dialog {

    public Dialog next = this;

    private GameMode newMode;
    private GameMode currentMode = getPlayer().getGameModeAssistant().getGameMode();

    private boolean sameMode(Player player, GameMode mode) {
        player.getPacketSender().sendInterfaceRemoval();
        if (player.getGameModeAssistant().getGameMode() == mode) {
            player.getPacketSender().sendMessage("You cannot change your game mode to the mode you're already on.");
            setEndState(1);
            return true;
        }
        return false;
    }


    public ChangeGameMode(Player player) {
        super(player);
        setEndState(4);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                setState(3);
                return Dialog.createNpc(DialogHandler.PLAIN_EVIL, "Hello, would you like me to change your game mode or title?");
            case 1:
                setEndState(1);
                return Dialog.createOption(new ThreeOption(
                        "I want to be a Knight",
                        "I want to be Realism",
                        "Cancel") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_3:
                                if (player.getEquipment().getFreeSlots() != player.getEquipment().capacity()) {
                                    player.getPacketSender().sendInterfaceRemoval();
                                    player.getPacketSender().sendMessage("Please unequip all your items first.");
                                    return;
                                }
                                if (!sameMode(getPlayer(), GameMode.KNIGHT)) {
                                    newMode = GameMode.KNIGHT;
                                    setState(2);
                                    getPlayer().getDialog().sendDialog(next);
                                }
                                break;
                            case OPTION_2_OF_3:
                                if (player.getEquipment().getFreeSlots() != player.getEquipment().capacity()) {
                                    player.getPacketSender().sendInterfaceRemoval();
                                    player.getPacketSender().sendMessage("Please unequip all your items first.");
                                    return;
                                }
                                if (!sameMode(getPlayer(), GameMode.REALISM)) {
                                    newMode = GameMode.REALISM;
                                    setState(2);
                                    getPlayer().getDialog().sendDialog(next);
                                }
                                break;
                            case OPTION_3_OF_3:
                                player.getPacketSender().sendInterfaceRemoval();
                                break;
                        }
                    }
                });
            case 2:
                if (getPlayer().getGameModeAssistant().isIronMan()) {
                    return Dialog.createNpc(DialogHandler.PLAIN_EVIL, "When changing from a @blu@" + currentMode.getModeName() + "@bla@ to a @blu@" + newMode.getModeName() + "@bla@ I can allow you to keep your items and stats. This @red@cannot be undone@bla@.");
                } else if (getPlayer().getGameModeAssistant().getGameMode() == GameMode.REALISM && newMode != GameMode.IRONMAN) {
                    return Dialog.createNpc(DialogHandler.PLAIN_EVIL, "When changing from a @blu@" + currentMode.getModeName() + "@bla@ to a @blu@" + newMode.getModeName() + "@bla@ I can allow you to keep your items and stats. This @red@cannot be undone@bla@");
                } else if (getPlayer().getGameModeAssistant().getGameMode() == GameMode.KNIGHT && newMode == GameMode.REALISM) {
                    return Dialog.createNpc(DialogHandler.PLAIN_EVIL, "When changing from a @blu@" + currentMode.getModeName() + "@bla@ to a @blu@" + newMode.getModeName() + "@bla@ I can allow you to keep your items and but NOT your stats. This @red@cannot be undone@bla@.");
                } else {
                }
            case 3:
                return Dialog.createOption(new TwoOption(
                        "Yes change my mode from " + currentMode.getModeName() + " to " + newMode.getModeName(),
                        "No I want to stay as a(n) " + currentMode.getModeName()) {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_2:
                                    if (currentMode == GameMode.KNIGHT) {
                                        switch (newMode) {
                                            case REALISM:
                                                player.getPacketSender().sendInterfaceRemoval();
                                                player.getPacketSender().sendMessage("You have just changed your game mode from " + currentMode.getModeName() + " to " + newMode.getModeName() + ".");
                                                player.getGameModeAssistant().setGameMode(GameMode.REALISM);
                                                player.getGameModeAssistant().resetStats(player, false);
                                                PlayerPanel.refreshPanel(player);
                                                break;
                                            default:
                                                player.getPacketSender().sendMessage("Error changing game mode....");
                                        }
                                    } else if (currentMode == GameMode.REALISM) {
                                        switch (newMode) {
                                            case KNIGHT:
                                                player.getPacketSender().sendInterfaceRemoval();
                                                player.getPacketSender().sendMessage("You have just changed your game mode from " + currentMode.getModeName() + " to " + newMode.getModeName() + ".");
                                                player.getGameModeAssistant().setGameMode(GameMode.KNIGHT);
                                                PlayerPanel.refreshPanel(player);
                                                player.save();
                                                break;
                                            default:
                                                player.getPacketSender().sendMessage("Error changing game mode....");
                                        }

                                    } else if (currentMode == GameMode.IRONMAN) {
                                        switch (newMode) {
                                            case KNIGHT:
                                                player.getPacketSender().sendInterfaceRemoval();
                                                player.getPacketSender().sendMessage("You have just changed your game mode from " + currentMode.getModeName() + " to " + newMode.getModeName() + ".");
                                                player.getGameModeAssistant().setGameMode(GameMode.KNIGHT);
                                                player.save();
                                                PlayerPanel.refreshPanel(player);
                                                break;
                                            case REALISM:
                                                player.getPacketSender().sendInterfaceRemoval();
                                                player.getPacketSender().sendMessage("You have just changed your game mode from " + currentMode.getModeName() + " to " + newMode.getModeName() + ".");
                                                player.getGameModeAssistant().setGameMode(GameMode.REALISM);
                                                player.save();
                                                PlayerPanel.refreshPanel(player);
                                                break;
                                            default:
                                                player.getPacketSender().sendMessage("Error changing game mode....");
                                        }
                                    }
                                break;
                            case OPTION_2_OF_2:
                                player.getPacketSender().sendInterfaceRemoval();
                                break;
                        }

                    }
                });
            case 4:
                setEndState(1);
                return Dialog.createOption(new ThreeOption(
                        "I want to change my Title",
                        "I want to change my Game Mode",
                        "Cancel") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_3:
                                Titles.openInterface(player);
                                player.getPacketSender().sendInterface(45400);
                                break;
                            case OPTION_2_OF_3:
                                setEndState(1);
                                setState(1);
                                getPlayer().getDialog().sendDialog(next);
                                break;
                            case OPTION_3_OF_3:
                                player.getPacketSender().sendInterfaceRemoval();
                                break;
                        }
                    }
                });
        }
        return null;
    }
}
