package org.scripts.kotlin.content.dialog.npcs;

import com.zarador.model.input.impl.ChangePassword;
import com.zarador.model.options.fouroption.FourOption;
import com.zarador.model.options.threeoption.ThreeOption;
import com.zarador.model.player.GameMode;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class ArgosGuide extends Dialog {

    public Dialog dialog = this;

    public ArgosGuide(Player player) {
        super(player);
        setEndState(5);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "Hello "+getPlayer().getUsername()+", do you need any assistance?");
            case 1:
                if(getPlayer().getGameModeAssistant().getGameMode() == GameMode.REALISM) {
                    return Dialog.createOption(new FourOption(
                            "I need to change my password.",
                            "How do I make money?",
                            "What do I do for additional help?",
                            "Can I purchase a Sir owen's longsword for 10M?") {
                        @Override
                        public void execute(Player player, OptionType option) {
                            switch (option) {
                                case OPTION_1_OF_4:
                                    player.setInputHandling(new ChangePassword());
                                    player.getPacketSender().sendEnterInputPrompt("Enter a new password:");
                                    break;
                                case OPTION_2_OF_4:
                                    setState(2);
                                    setEndState(3);
                                    player.getDialog().sendDialog(dialog);
                                    break;
                                case OPTION_3_OF_4:
                                    setState(4);
                                    setEndState(4);
                                    player.getDialog().sendDialog(dialog);
                                    break;
                                case OPTION_4_OF_4:
                                    if(player.getInventory().getAmount(995) >= 10000000) {
                                        player.getInventory().delete(995, 10000000);
                                        player.getInventory().add(16389, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                    } else {
                                        setState(6);
                                        setEndState(6);
                                        player.getDialog().sendDialog(dialog);
                                    }
                                    break;
                            }
                        }
                    });
                } else if(getPlayer().getGameModeAssistant().getGameMode() == GameMode.KNIGHT) {
                    return Dialog.createOption(new FourOption(
                            "I need to change my password.",
                            "How do I make money?",
                            "What do I do for additional help?",
                            "Can I purchase a Cape of knights for 1M?") {
                        @Override
                        public void execute(Player player, OptionType option) {
                            switch (option) {
                                case OPTION_1_OF_4:
                                    player.setInputHandling(new ChangePassword());
                                    player.getPacketSender().sendEnterInputPrompt("Enter a new password:");
                                    break;
                                case OPTION_2_OF_4:
                                    setState(2);
                                    setEndState(3);
                                    player.getDialog().sendDialog(dialog);
                                    break;
                                case OPTION_3_OF_4:
                                    setState(4);
                                    setEndState(4);
                                    player.getDialog().sendDialog(dialog);
                                    break;
                                case OPTION_4_OF_4:
                                    if(player.getInventory().getAmount(995) >= 1000000) {
                                        player.getInventory().delete(995, 1000000);
                                        player.getInventory().add(1052, 1);
                                        player.getPacketSender().sendInterfaceRemoval();
                                    } else {
                                        setState(7);
                                        setEndState(7);
                                        player.getDialog().sendDialog(dialog);
                                    }
                                    break;
                            }
                        }
                    });
                } else {
                    return Dialog.createOption(new ThreeOption(
                            "I need to change my password.",
                            "How do I make money?",
                            "What do I do for additional help?") {
                        @Override
                        public void execute(Player player, OptionType option) {
                            switch(option) {
                                case OPTION_1_OF_3:
                                    player.setInputHandling(new ChangePassword());
                                    player.getPacketSender().sendEnterInputPrompt("Enter a new password:");
                                    break;
                                case OPTION_2_OF_3:
                                    setState(2);
                                    setEndState(3);
                                    player.getDialog().sendDialog(dialog);
                                    break;
                                case OPTION_3_OF_3:
                                    setState(4);
                                    setEndState(4);
                                    player.getDialog().sendDialog(dialog);
                                    break;
                            }
                        }
                    });
                }
            case 2:
                return Dialog.createNpc(DialogHandler.CALM, "Making money on Argos can be pretty easy. You can kill frost dragons for bones at ice plateau and sell them to players.");
            case 3:
                return Dialog.createNpc(DialogHandler.CALM, "If you need more help with making money, go to wiki.rune.live in your browser to view a list of money making methods.");
            case 4:
                return Dialog.createNpc(DialogHandler.CALM, "Need help? Contact a staff member. If they can't help you with your issue go to Argos.com and ask for further assistance.");
            case 5:
                return Dialog.createNpc(DialogHandler.CALM, "The tutorial will be created at a later date.");
            case 6:
                return Dialog.createNpc(DialogHandler.CALM, "Sure but that will be 40 million gp! Come back when you have the money sir.");
            case 7:
                return Dialog.createNpc(DialogHandler.CALM, "Sure but that will be 5 million gp! Come back when you have the money sir.");
        }
        return null;
    }
}
