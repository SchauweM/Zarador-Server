package org.scripts.kotlin.content.dialog;

import com.zarador.model.options.fiveoption.FiveOption;
import com.zarador.model.options.threeoption.ThreeOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.content.Gambling;
import com.zarador.world.entity.impl.player.Player;

public class ChooseFlower extends Dialog {

    public Dialog dialog = this;

    public ChooseFlower(Player player, int state) {
        super(player);
        setEndState(1);
        setState(state);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createOption(new FiveOption(
                        "Pastel",
                        "Red",
                        "Blue",
                        "Yellow",
                        "Next page ->") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_5:
                                player.getPacketSender().sendInterfaceRemoval();
                                Gambling.plantSeed(player, Gambling.FlowersData.PASTEL_FLOWERS);
                                break;
                            case OPTION_2_OF_5:
                                player.getPacketSender().sendInterfaceRemoval();
                                Gambling.plantSeed(player, Gambling.FlowersData.RED_FLOWERS);
                                break;
                            case OPTION_3_OF_5:
                                player.getPacketSender().sendInterfaceRemoval();
                                Gambling.plantSeed(player, Gambling.FlowersData.BLUE_FLOWERS);
                                break;
                            case OPTION_4_OF_5:
                                player.getPacketSender().sendInterfaceRemoval();
                                Gambling.plantSeed(player, Gambling.FlowersData.YELLOW_FLOWERS);
                                break;
                            case OPTION_5_OF_5:
                                player.getDialog().sendDialog(new ChooseFlower(player, 1));
                                break;
                        }
                    }
                });
            case 1:
                return Dialog.createOption(new ThreeOption(
                        "Purple",
                        "Orange",
                        "<- Previous") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_3:
                                player.getPacketSender().sendInterfaceRemoval();
                                Gambling.plantSeed(player, Gambling.FlowersData.PURPLE_FLOWERS);
                                break;
                            case OPTION_2_OF_3:
                                player.getPacketSender().sendInterfaceRemoval();
                                Gambling.plantSeed(player, Gambling.FlowersData.ORANGE_FLOWERS);
                                break;
                            case OPTION_3_OF_3:
                                player.getDialog().sendDialog(new ChooseFlower(player, 0));
                                break;
                        }
                    }
                });
            }
        return null;
    }
}
