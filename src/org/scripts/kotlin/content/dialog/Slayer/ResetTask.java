package org.scripts.kotlin.content.dialog.Slayer;

import com.zarador.model.options.twooption.TwoOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.content.PlayerPanel;
import com.zarador.world.entity.impl.player.Player;

public class ResetTask extends Dialog {

    public Dialog dialog = this;

    public ResetTask(Player player) {
        super(player);
        setEndState(1);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.SCARED, "Resetting your slayer task costs 5 slayer points. It also resets your streak.");
            case 1:
                return Dialog.createOption(new TwoOption(
                        "Reset my task",
                        "I'll keep my task") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_2:
                                player.getSlayer().setSlayerMaster(null);
                                player.getSlayer().setSlayerTask(null);
                                player.getSlayer().setDuoSlayer(null);
                                player.getSlayer().setAmountLeft(0);
                                player.getSlayer().setSlayerStreak(0);
                                player.getSlayer().setDuoTimes(0);
                                player.getPointsHandler().setSlayerPoints(-5, true);
                                PlayerPanel.refreshPanel(player);
                                player.getPacketSender().sendInterfaceRemoval();
                                player.getPacketSender().sendMessage("You have successfully reset your slayer task.");
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
