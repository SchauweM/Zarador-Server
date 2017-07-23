package org.scripts.kotlin.content.dialog.npcs;

import com.zarador.model.container.impl.Shop;
import com.zarador.model.options.fouroption.FourOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.util.Misc;
import com.zarador.world.entity.impl.player.Player;

public class Donovan extends Dialog {

    public Donovan(Player player, int state) {
        super(player);
        setState(state);
        setEndState(0);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createOption(new FourOption(
                        "Can I view your rares store?",
                        "Can I view your armour/weapons store?",
                        "I want to buy scrolls from the website.",
                        "How many donator points do I have?") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            /**
                             * Rares
                             */
                            case OPTION_1_OF_4:
                                Shop.ShopManager.getShops().get(43).open(player);
                                break;
                            /**
                             * Weapons & armour
                             */
                            case OPTION_2_OF_4:
                                Shop.ShopManager.getShops().get(42).open(player);
                                break;

                            /**
                             * Buy scrolls @ website
                             */
                            case OPTION_3_OF_4:
                                player.getPacketSender().sendString(1, "www.argos.com/store/");
                                setEndState(1);
                                player.getDialog().sendDialog(new Donovan(player, 1));
                                break;

                            /**
                             * Check how many points you have
                             */
                            case OPTION_4_OF_4:
                                setEndState(2);
                                player.getDialog().sendDialog(new Donovan(player, 2));
                                break;
                        }
                    }
                });
            case 1:
                setEndState(1);
                return Dialog.createStatement(DialogHandler.JOYOUS_TALK, "Opening website...", "~ www.argos.com/store ~");
            case 2:
                setEndState(2);
                return Dialog.createStatement(DialogHandler.JOYOUS_TALK, "You currently have "+ Misc.format(getPlayer().getPoints())+" donator points.");
        }
        return null;
    }
}
