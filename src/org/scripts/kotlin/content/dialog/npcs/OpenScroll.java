package org.scripts.kotlin.content.dialog.npcs;

import com.zarador.model.definitions.ItemDefinition;
import com.zarador.model.options.twooption.TwoOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.util.Misc;
import com.zarador.world.content.Scrolls;
import com.zarador.world.entity.impl.player.Player;

public class OpenScroll extends Dialog {

    private int itemId;

    public OpenScroll(Player player, int state, int itemId) {
        super(player);
        this.itemId = itemId;
        setState(state);
        setEndState(2);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createOption(new TwoOption(
                        "Open "+ItemDefinition.forId(itemId).getName(),
                        "Cancel") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_2:
                                Scrolls.useScroll(player, itemId);
                                break;
                            case OPTION_2_OF_2:
                                player.getPacketSender().sendInterfaceRemoval();
                                break;
                        }
                    }
                });

            case 1:
                return Dialog.createPlayer(DialogHandler.JOYOUS_TALK, "I open my "+ ItemDefinition.forId(itemId).getName()+" and\\n receive "+ Misc.format(Scrolls.Scroll.forItemId(itemId).getPoints())+" donator points.");
            case 2:
                return Dialog.createPlayer(DialogHandler.JOYOUS_TALK, "The scroll reads...\\n 'Speak with Donovan in Edgeville bank.'");
        }
        return null;
    }
}
