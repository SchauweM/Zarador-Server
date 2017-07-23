package org.scripts.kotlin.content.dialog.npcs;

import com.zarador.model.container.impl.Shop;
import com.zarador.model.options.fouroption.FourOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.content.diversions.hourly.ShootingStar;
import com.zarador.world.entity.impl.player.Player;

public class MinerMagnus extends Dialog {

    public MinerMagnus(Player player, int state) {
        super(player);
        setState(state);
        setEndState(6);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "Get away from me! I am mining.");
            case 1:
                return Dialog.createPlayer(DialogHandler.CALM, "Jesus, what side of the bed did you wake up on?");
            case 2:
                return Dialog.createNpc(DialogHandler.CALM, "I didn't sleep last night, I was too busy mining this adamantite ore... ALL NIGHT.");
            case 3:
                return Dialog.createNpc(DialogHandler.CALM, "What do you need my help with?");
            case 4:
                return Dialog.createOption(new FourOption(
                        "I need to purchase mining items from you.",
                        "Where is the current shooting star located?",
                        "How long until the next shooting star crashes?",
                        "Nevermind.") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_4:
                                Shop.ShopManager.getShops().get(44).open(player);
                                break;
                            case OPTION_2_OF_4:
                                player.getDialog().sendDialog(new MinerMagnus(player, 5));
                                break;
                            case OPTION_3_OF_4:
                                player.getDialog().sendDialog(new MinerMagnus(player, 6));
                                break;
                            case OPTION_4_OF_4:
                                player.getPacketSender().sendInterfaceRemoval();
                                break;
                        }
                    }
                });
            case 5:
                return Dialog.createNpc(DialogHandler.CALM, "I think I saw a Shooting star at "+ ShootingStar.getInstance().getLocation().getClue()+", you better head over there!");
            case 6:
                if(ShootingStar.getInstance().alreadyCrashed()) {
                    return Dialog.createNpc(DialogHandler.CALM, "Hmm, according to my calculations " + ShootingStar.getInstance().getTimeToCrash() + ".");
                } else {
                    return Dialog.createNpc(DialogHandler.CALM, "Hmm, according to my calculations the Shooting star has not been fully mined yet.");
                }
            }
        return null;
    }
}
