package org.scripts.kotlin.content.dialog.npcs;

import com.zarador.model.definitions.ItemDefinition;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.util.Misc;
import com.zarador.world.entity.impl.player.Player;

public class PumpkinPete extends Dialog {

    /**
     * Item ids for witch doctor items
     */
    public int[] WITCH_DOCTOR_ITEMS = {
        20046,
        20045,
        20044
    };

    /**
     * Grab a random reward depending on if you
     * already have that item.
     * @param player
     * @return
     */
    public int getRewardItem(Player player) {
        for(int i = 0; i < WITCH_DOCTOR_ITEMS.length; i++) {
            if (!player.hasItem(WITCH_DOCTOR_ITEMS[i])) {
                return WITCH_DOCTOR_ITEMS[i];
            }
        }
        return WITCH_DOCTOR_ITEMS[Misc.inclusiveRandom(WITCH_DOCTOR_ITEMS.length - 1)];
    }


    public Dialog dialog = this;

    public PumpkinPete(Player player, int state) {
        super(player);
        setState(state);
        setEndState(3);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "Dancing for Pumpkins!");
            case 1:
                return Dialog.createPlayer(DialogHandler.CALM, "What?");
            case 2:
                return Dialog.createNpc(DialogHandler.CALM, "Use a pumpkin on me and I will give you a suprise!");
            case 3:
                setEndState(3);
                return Dialog.createPlayer(DialogHandler.CALM, "OK!");
            case 4:
                setEndState(4);
                if(!getPlayer().getInventory().contains(1959)) {
                    return Dialog.createNpc(DialogHandler.ANGRY_1, "I need pumpkins!");
                } else {
                    int random = Misc.inclusiveRandom(0, 4);
                    int randomItem = WITCH_DOCTOR_ITEMS[Misc.inclusiveRandom(WITCH_DOCTOR_ITEMS.length - 1)];
                    if (random == 4) {
                        if (getPlayer().hasItem(randomItem)) {
                            randomItem = getRewardItem(getPlayer());
                        }
                        getPlayer().getInventory().delete(1959, 1);
                        getPlayer().getInventory().add(randomItem, 1);
                        return Dialog.createNpc(DialogHandler.CALM, "Trick or treat, smell my feet!\\nEnjoy " + ItemDefinition.forId(randomItem).getName() + ".");
                    } else {
                        int amount = Misc.inclusiveRandom(100_000, 200_000);
                        getPlayer().getInventory().delete(1959, 1);
                        if (getPlayer().getGameModeAssistant().isIronMan()) {
                            int id = getPlayer().casketRewards();
                            return Dialog.createNpc(DialogHandler.CALM, "Trick or treat, smell my feet!\\nEnjoy " + ItemDefinition.forId(id).getName() + ".");
                        } else {
                            getPlayer().getInventory().add(995, amount);
                        return Dialog.createNpc(DialogHandler.CALM, "Trick or treat, smell my feet!\\nEnjoy " + Misc.format(amount) + " coins.");
                        }
                    }
                }
            }
        return null;
    }
}
