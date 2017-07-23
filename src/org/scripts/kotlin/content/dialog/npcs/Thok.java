package org.scripts.kotlin.content.dialog.npcs;

import com.zarador.model.container.impl.Shop;
import com.zarador.model.options.twooption.TwoOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class Thok extends Dialog {

    public Thok(Player player, int state) {
        super(player);
        setState(state);
        setEndState(6);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "Hello kind sir! We are the army of the Fremenniks.");
            case 1:
                return Dialog.createNpc(DialogHandler.CALM, "We are always looking for new recruits to our army, would you like to help us?");
            case 2:
                return Dialog.createOption(new TwoOption(
                        "Sure, how do I start?",
                        "I want to trade in my tokens.") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_2:
                                player.getDialog().sendDialog(new Thok(player, 3));
                                break;
                            case OPTION_2_OF_2:
                                Shop.ShopManager.getShops().get(46).open(player);
                                break;
                        }
                    }
                });
            case 3:
                return Dialog.createNpc(DialogHandler.CALM, "Great! You can get started by going down the dungeon entrance north-east of me.");
            case 4:
                return Dialog.createNpc(DialogHandler.CALM, "At the end of every round, you will get dungeoneering xp and tokens.");
            case 5:
                return Dialog.createNpc(DialogHandler.CALM, "Come back to me, and I can turn in your dungeoneering tokens for items or experience.");
            case 6:
                return Dialog.createPlayer(DialogHandler.CALM, "Awesome! Thank you for your help.");
            }
        return null;
    }
}
