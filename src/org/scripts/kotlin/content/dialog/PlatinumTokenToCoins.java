package org.scripts.kotlin.content.dialog;

import com.zarador.model.options.twooption.TwoOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.util.Misc;
import com.zarador.world.entity.impl.player.Player;

public class PlatinumTokenToCoins extends Dialog {

    public Dialog dialog = this;

    public PlatinumTokenToCoins(Player player) {
        super(player);
        setEndState(0);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createOption(new TwoOption(
                        "Convert " + Misc.format(getPlayer().getInventory().getAmount(13204)) + " Platinum tokens to "+ Misc.format((long)getPlayer().getInventory().getAmount(13204) * 1000) + " coins.",
                        "Cancel") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_2:
                                long coinsInventory = player.getInventory().getAmount(995);
                                long coinsToGet = (long) player.getInventory().getAmount(13204) * 1000;
                                boolean moneyPouch = false;
                                if((coinsInventory + coinsToGet) > Integer.MAX_VALUE) {
                                    moneyPouch = true;
                                }
                                if(moneyPouch) {
                                    player.getInventory().delete(13204, player.getInventory().getAmount(13204));
                                    player.setMoneyInPouch(player.getMoneyInPouch() + coinsToGet);
                                    player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
                                    player.getPacketSender().sendMessage("<col=2F4E5D>"+Misc.format(coinsToGet)+" coins have been added to your money pouch.");
                                } else {
                                    player.getInventory().delete(13204, player.getInventory().getAmount(13204));
                                    player.getInventory().add(995, (int) coinsToGet);
                                    player.getPacketSender().sendMessage("<col=2F4E5D>"+Misc.format(coinsToGet)+" coins have been added to your inventory.");
                                }
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
