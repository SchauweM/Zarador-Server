package org.scripts.kotlin.content.dialog;

import com.zarador.model.options.fouroption.FourOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.content.clan.ClanChatManager;
import com.zarador.world.entity.impl.player.Player;

public class ClanChatDialogue extends Dialog {

    public Dialog dialog = this;

    public ClanChatDialogue(Player player) {
        super(player);
        setEndState(0);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createOption(new FourOption(
                        "Create Clan Chat",
                        "Edit Clan Chat",
                        "Delete Clan Chat",
                        "Cancel") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_4:
                                ClanChatManager.createClan(player);
                                break;
                            case OPTION_2_OF_4:
                                ClanChatManager.clanChatSetupInterface(player, true);
                                break;
                            case OPTION_3_OF_4:
                                ClanChatManager.deleteClan(player);
                                break;
                            case OPTION_4_OF_4:
                                player.getPacketSender().sendInterfaceRemoval();
                                break;
                        }
                    }
                });
        }
        return null;
    }
}
