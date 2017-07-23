package org.scripts.kotlin.content.dialog.npcs;

import com.zarador.model.Item;
import com.zarador.model.options.twooption.TwoOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.util.Misc;
import com.zarador.world.entity.impl.player.Player;

public class SmallSacks extends Dialog {

    public SmallSacks(Player player) {
        super(player);
        setEndState(0);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                int goldInInventory = getPlayer().getInventory().getAmount(444) + getPlayer().getInventory().getAmount(445);
                int coalInInventory = getPlayer().getInventory().getAmount(453) + getPlayer().getInventory().getAmount(454);
                int totalMinerals = goldInInventory + coalInInventory;
                int coinsCharge = (goldInInventory * 4000) + (coalInInventory * 6000);
                return Dialog.createOption(new TwoOption(
                        "Convert "+totalMinerals+" ore to minerals (costs "+ Misc.formatAmount(coinsCharge)+" coins)",
                        "Cancel") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_2:
                                if(player.getInventory().getAmount(995) < coinsCharge) {
                                    player.getPacketSender().sendMessage("You do not have enough coins to convert this ore to minerals.");
                                    player.getPacketSender().sendInterfaceRemoval();
                                    return;
                                }
                                if(player.getInventory().getAmount(444) + player.getInventory().getAmount(445) == goldInInventory && player.getInventory().getAmount(453) + player.getInventory().getAmount(454) == coalInInventory) {
                                    player.getInventory().delete(453, coalInInventory);
                                    player.getInventory().delete(454, coalInInventory);
                                    player.getInventory().delete(444, goldInInventory);
                                    player.getInventory().delete(445, goldInInventory);
                                    player.getInventory().delete(995, coinsCharge);
                                    player.getInventory().add(new Item(15263, totalMinerals), true);
                                    player.getPacketSender().sendMessage("You have converted "+totalMinerals+" gold & coal ore into "+totalMinerals+" living minerals for "+Misc.format(coinsCharge)+" coins.");
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
