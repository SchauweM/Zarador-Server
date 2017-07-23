package org.scripts.kotlin.content.dialog.npcs;

import com.zarador.model.container.impl.Shop;
import com.zarador.model.input.impl.EnterYellTag;
import com.zarador.model.options.threeoption.ThreeOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.util.Misc;
import com.zarador.world.entity.impl.player.Player;

public class Ellis extends Dialog {

    public Ellis(Player player, int state) {
        super(player);
        setState(state);
        setEndState(6);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "Hello, would you like to buy any items?");
            case 1:
                return Dialog.createOption(new ThreeOption(
                        "Show me your store.",
                        "I want to change my yell tag. ("+Misc.formatAmount(getPlayer().getDonatorRights().getYellTagPrice())+")",
                        "Wait... who are you?") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_3:
                                Shop.ShopManager.getShops().get(45).open(player);
                                break;
                            case OPTION_2_OF_3:
                                player.getDialog().sendDialog(new Ellis(player, 5));
                                break;
                            case OPTION_3_OF_3:
                                player.getDialog().sendDialog(new Ellis(player, 2));
                                break;
                        }
                    }
                });
            case 2:
                setEndState(4);
                return Dialog.createNpc(DialogHandler.CALM, "My name is Ellis, I am a trader of many lands.");
            case 3:
                setEndState(4);
                return Dialog.createPlayer(DialogHandler.CALM, "WHAT? Your not a donator! Guards guards!!");
            case 4:
                setEndState(4);
                return Dialog.createStatement(DialogHandler.CALM, "Ellis gets taken away in hand cuffs...");
            case 5:
                setEndState(6);
                return Dialog.createNpc(DialogHandler.CALM, "Not a problem! That will be "+Misc.formatAmount(getPlayer().getDonatorRights().getYellTagPrice())+" coins. What would you like to change it to?");
            case 6:
                getPlayer().getPacketSender().sendInterfaceRemoval();
                getPlayer().getPacketSender().sendEnterInputPrompt("Enter what you want to set your yell tag to:");
                getPlayer().setInputHandling(new EnterYellTag());
                return null;
            case 7:
                setEndState(7);
                return Dialog.createNpc(DialogHandler.CALM, "You do not have enough money to set your yell tag! You need atleast "+ Misc.formatAmount(getPlayer().getDonatorRights().getYellTagPrice())+".");
            case 8:
                setEndState(8);
                return Dialog.createNpc(DialogHandler.CALM, "You have entered something into your yell tag that is not allowed.");
            case 9:
                setEndState(9);
                return Dialog.createNpc(DialogHandler.CALM, "Congratulations, you have set your yell tag to "+getPlayer().getYellTag()+".");
            }
        return null;
    }
}
