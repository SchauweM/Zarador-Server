package org.scripts.kotlin.content.dialog;

import com.zarador.model.Animation;
import com.zarador.model.Graphic;
import com.zarador.model.options.fiveoption.FiveOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class GoldenMiningArmour extends Dialog {

    public GoldenMiningArmour(Player player) {
        super(player);
        setEndState(2);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return createPlayer(DialogHandler.HAPPY_AND_SURPRISED_TALK, "Oooo a cracker, it's Christmas already!");
            case 1:
                return createNpc(DialogHandler.CALM_CONTINUED, "Not quite yet...");
            case 2:
            return Dialog.createOption(new FiveOption(
                    "Golden mining gloves",
                    "Golden mining boots",
                    "Golden mining helmet",
                    "Golden mining trousers",
                    "Golden mining top") {
                @Override
                public void execute(Player player, OptionType option) {
                    switch(option) {
                        case OPTION_1_OF_5:
                            if(player.getInventory().contains(20083)) {
                                player.getInventory().delete(20083, 1);
                                player.getInventory().add(20787, 1);
                                player.performGraphic(new Graphic(1310));
                                player.performAnimation(new Animation(15153));
                            }
                            player.getPacketSender().sendInterfaceRemoval();
                            break;
                        case OPTION_2_OF_5:
                            if(player.getInventory().contains(20083)) {
                                player.getInventory().delete(20083, 1);
                                player.getInventory().add(20788, 1);
                                player.performGraphic(new Graphic(1310));
                                player.performAnimation(new Animation(15153));
                            }
                            player.getPacketSender().sendInterfaceRemoval();
                            break;
                        case OPTION_3_OF_5:
                            if(player.getInventory().contains(20083)) {
                                player.getInventory().delete(20083, 1);
                                player.getInventory().add(20789, 1);
                                player.performGraphic(new Graphic(1310));
                                player.performAnimation(new Animation(15153));
                            }
                            player.getPacketSender().sendInterfaceRemoval();
                            break;
                        case OPTION_4_OF_5:
                            if(player.getInventory().contains(20083)) {
                                player.getInventory().delete(20083, 1);
                                player.getInventory().add(20790, 1);
                                player.performGraphic(new Graphic(1310));
                                player.performAnimation(new Animation(15153));
                            }
                            player.getPacketSender().sendInterfaceRemoval();
                            break;
                        case OPTION_5_OF_5:
                            if(player.getInventory().contains(20083)) {
                                player.getInventory().delete(20083, 1);
                                player.getInventory().add(20791, 1);
                                player.performGraphic(new Graphic(1310));
                                player.performAnimation(new Animation(15153));
                            }
                            player.getPacketSender().sendInterfaceRemoval();
                            break;
                    }
                }
            });
        }
        return null;
    }
}
