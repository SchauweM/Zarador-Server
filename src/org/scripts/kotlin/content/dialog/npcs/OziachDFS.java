package org.scripts.kotlin.content.dialog.npcs;

import com.zarador.model.options.twooption.TwoOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class OziachDFS extends Dialog {

    public Dialog dialog = this;

    public OziachDFS(Player player) {
        super(player);
        setEndState(1);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.PLAIN_EVIL, "I can turn this into a Dragon Fire Shield if you have an Anti Shield and 2 million coins!");
            case 1:
                return Dialog.createOption(new TwoOption(
                        "Please Smith my shield together",
                        "Cancel") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_2:
                                player.getPacketSender().sendInterfaceRemoval();
                                if (player.getMoneyInPouch() >= 2_000_000 && player.getInventory().containsAll(11286, 1540)) {
                                    player.getInventory().delete(11286, 1);
                                    player.getInventory().delete(1540, 1);
                                    player.setMoneyInPouch(player.getMoneyInPouch() - 2_000_000);
                                    getPlayer().getPacketSender().sendString(8135, "" + player.getMoneyInPouch() + "");
                                    player.getInventory().add(11283, 1);
                                    player.getPacketSender().sendMessage("Oziach has created your Dragonfire Shield.");
                                } else {
                                    player.getPacketSender().sendMessage("You need 2m coins a dfs and anti shield.");
                                }
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
