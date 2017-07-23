package org.scripts.kotlin.content.dialog;

import com.zarador.model.options.twooption.TwoOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.util.Misc;
import com.zarador.world.entity.impl.player.Player;

public class CoinsToPlatinumTokens extends Dialog {

    public Dialog dialog = this;

    public CoinsToPlatinumTokens(Player player) {
        super(player);
        setEndState(0);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createOption(new TwoOption(
                        "Convert " + Misc.format(getPlayer().getInventory().getAmount(995)) + " coins to "+ Misc.format(getPlayer().getInventory().getAmount(995) / 1000) + " Platinum tokens.",
                        "Cancel") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_2:
                                int coinsInventory = player.getInventory().getAmount(995);
                                int tokensToGet = player.getInventory().getAmount(995) / 1000;

                                player.getInventory().delete(995, coinsInventory);
                                player.getInventory().add(13204, tokensToGet);

                                player.getPacketSender().sendMessage("<col=2F4E5D>"+Misc.format(tokensToGet)+" Platinum Tokens have been added to your inventory.");

                                player.getPacketSender().sendInterfaceRemoval();
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
