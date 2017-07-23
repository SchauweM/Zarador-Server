package org.scripts.kotlin.content.dialog;

import org.scripts.kotlin.content.dialog.npcs.Gambler;

import com.zarador.model.options.twooption.TwoOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.content.lottery.Lottery;
import com.zarador.world.content.lottery.LotterySaving;
import com.zarador.world.entity.impl.player.Player;

public class UseLottery extends Dialog {

    public Dialog dialog = this;

    public UseLottery(Player player) {
        super(player);
        setEndState(0);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createOption(new TwoOption(
                        "Enter Lottery",
                        "Cancel") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_2:
                                if(!LotterySaving.LOTTERY_ON) {
                                    player.getDialog().sendDialog(new Gambler(player, 6, 6));
                                    break;
                                }
                                if(player.getInventory().contains(13664)) {
                                    LotterySaving.lottery1.addOffer(new Lottery(player.getUsername()));
                                    player.getInventory().delete(13664, 1);
                                    player.getDialog().sendDialog(new Gambler(player, 4, 4));
                                    LotterySaving.log("enter", player.getUsername());
                                } else {
                                    player.getDialog().sendDialog(new Gambler(player, 3, 3));
                                }
                                break;
                            case OPTION_2_OF_2:
                                player.getPacketSender().sendInterfaceRemoval();
                                break;
                        }
                    }
                });
        }
        return null;
    }
}
