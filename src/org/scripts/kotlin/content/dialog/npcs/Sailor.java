package org.scripts.kotlin.content.dialog.npcs;

import com.zarador.model.Position;
import com.zarador.model.options.threeoption.ThreeOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.content.transportation.TeleportHandler;
import com.zarador.world.entity.impl.player.Player;

public class Sailor extends Dialog {

    public Dialog dialog = this;

    public Sailor(Player player, int state) {
        super(player);
        setState(state);
        setEndState(1);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "Arrrg the captain is ready to go yee timbers! Where do you want to go?");
            case 1:
                return Dialog.createOption(new ThreeOption(
                        "Chaos Altar",
                        "Desert Pyramid",
                        "Lunar Isle") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_3:
                                TeleportHandler.teleportPlayer(player, new Position(2940, 3512, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_2_OF_3:
                                TeleportHandler.teleportPlayer(player, new Position(3233, 2919, 0), player.getSpellbook().getTeleportType());
                                player.getPacketSender().sendMessage("<col=ff0000>To change your spellbook, travel onto the south part of the pyramid.");
                                break;
                            case OPTION_3_OF_3:
                                TeleportHandler.teleportPlayer(player, new Position(2100, 3914, 0), player.getSpellbook().getTeleportType());
                                break;
                        }
                    }
                });
        }
        return null;
    }
}
