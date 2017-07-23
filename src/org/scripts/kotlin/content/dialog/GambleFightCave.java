package org.scripts.kotlin.content.dialog;

import com.zarador.model.Item;
import com.zarador.model.definitions.ItemDefinition;
import com.zarador.model.options.twooption.TwoOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.util.Misc;
import com.zarador.world.World;
import com.zarador.world.entity.impl.player.Player;

public class GambleFightCave extends Dialog {

    int itemId;

    public GambleFightCave(Player player, int state, int itemId) {
        super(player);
        this.itemId = itemId;
        setState(state);
        setEndState(3);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "Hello! Would you like a chance at capturing me?");
            case 1:
                return Dialog.createNpc(DialogHandler.CALM, "If you sacrifice your Fire cape you have a 1/50 chance, or a TokHaar-Kal (1/20 chance).");
            case 2:
                return Dialog.createOption(new TwoOption(
                        "Yes, sacrifice my "+ ItemDefinition.forId(itemId).getName(),
                        "Cancel") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_2:
                                player.getInventory().delete(itemId, 1);
                                boolean gotIt = false;
                                if(itemId == 6570) {
                                    gotIt = Misc.inclusiveRandom(0, 50) == 5;
                                } else if(itemId == 19111) {
                                    gotIt = Misc.inclusiveRandom(0, 20) == 5;
                                }

                                if(gotIt) {
                                    player.getInventory().add(new Item(13225, 1));
                                    player.getDialog().sendDialog(new GambleFightCave(player, 3, -1));
                                    World.sendMessage("<icon=1><shad=FF8C38> " + player.getUsername() + " has just received "
                                            + ItemDefinition.forId(13225).getName() + " from Fight Cave!");
                                } else {
                                    player.getPacketSender().sendInterfaceRemoval();
                                }
                                break;
                            case OPTION_2_OF_2:
                                player.getPacketSender().sendInterfaceRemoval();
                                break;
                        }
                    }
                });
            case 3:
                setEndState(3);
                return Dialog.createNpc(DialogHandler.CALM, "Woohooooo CONGRATULATIONS! My dad would be proud.");
        }
        return null;
    }
}
