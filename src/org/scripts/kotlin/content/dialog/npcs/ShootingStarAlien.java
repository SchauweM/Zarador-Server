package org.scripts.kotlin.content.dialog.npcs;

import java.util.List;

import com.zarador.model.Item;
import com.zarador.model.options.twooption.TwoOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.util.Misc;
import com.zarador.world.content.diversions.hourly.ShootingStar;
import com.zarador.world.entity.impl.player.Player;

public class ShootingStarAlien extends Dialog {

    public Dialog dialog = this;

    public ShootingStarAlien(Player player) {
        super(player);
        setEndState(3);
    }

    @Override
    public DialogMessage getMessage() {
        final List<Item> rewards = ShootingStar.getRewards(getPlayer());
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.JOYOUS_TALK, "Is that stardust? Will you be willing to exchange 200 of it for some rewards I have collected?");
            case 1:
            return Dialog.createOption(new TwoOption(
                    "Yes",
                    "No") {
                @Override
                public void execute(Player player, OptionType option) {
                    switch(option) {
                        case OPTION_1_OF_2:
                            int amount = player.getInventory().getAmount(ShootingStar.STARDUST);
                            if (amount >= 200) {
                                player.getInventory().delete(new Item(ShootingStar.STARDUST, 200));
                                for (Item item : rewards) {
                                    player.getInventory().add(item);
                                }
                                int stardust = player.getInventory().getAmount(ShootingStar.STARDUST);
                                if (stardust <= 0)
                                    return;

                                int chance = Misc.inclusiveRandom(0, 1);
                                switch(chance) {
                                    case 0:
                                        int chance2 = Misc.inclusiveRandom(1, 50);
                                        if(chance2 == 1) {
                                            player.getPacketSender().sendMessage("You have received a Gilded dragon pickaxe from a Shooting Star!");
                                            player.getInventory().add(new Item(20786, 1), true);
                                        }
                                        break;
                                    case 1:
                                        int chance3 = Misc.inclusiveRandom(1, 20);
                                        if(chance3 == 1) {
                                            player.getPacketSender().sendMessage("You have received a Golden cracker from a Shooting Star!");
                                            player.getInventory().add(new Item(20083, 1), true);
                                        }
                                        break;
                                }

                                setState(3);
                                setEndState(3);
                                player.getDialog().sendDialog(dialog);
                            } else {
                                player.getPacketSender().sendMessage("You need at least 200 stardusts to claim a reward.");
                                player.getPacketSender().sendInterfaceRemoval();
                            }
                            break;
                        case OPTION_2_OF_2:
                            setState(2);
                            setEndState(2);
                            player.getDialog().sendDialog(dialog);
                            break;
                    }
                }
            });
            case 2:
                return Dialog.createNpc(DialogHandler.GLANCE_DOWN, "I could always use stardust; come back if you change your mind human.");
            case 3:
                return Dialog.createNpc(DialogHandler.HAPPY_AND_SURPRISED_TALK, "I misjudged you humans. You're not that bad!");
        }
        return null;
    }
}
