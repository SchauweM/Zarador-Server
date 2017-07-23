package org.scripts.kotlin.content.dialog.teleports.jewlery;

import com.zarador.model.Position;
import com.zarador.model.options.fiveoption.FiveOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.content.transportation.TeleportHandler;
import com.zarador.world.entity.impl.player.Player;

public class Jewelery extends Dialog {

    public Dialog dialog = this;

    public Jewelery(Player player) {
        super(player);
        setEndState(0);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createOption(new FiveOption(
                        "Edgeville Coffins",
                        "Market",
                        "Gambling Area",
                        "Barbarian Village",
                        "Cancel") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_5:
                                TeleportHandler.JewleryteleportPlayer(player, new Position(3094, 3478, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_2_OF_5:
                                TeleportHandler.JewleryteleportPlayer(player, new Position(3165, 3483, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_3_OF_5:
                                TeleportHandler.JewleryteleportPlayer(player, new Position(2441, 3090, 0), player.getSpellbook().getTeleportType());
                                player.getPacketSender().sendMessage("<col=3A3DA4>Welcome to the Gambling Area, make sure you always use a middle man for high bets!");
                                player.getPacketSender().sendMessage("<col=3A3DA4>Recording your stake will only get the player banned if they scam.");
                                break;
                            case OPTION_4_OF_5:
                                TeleportHandler.JewleryteleportPlayer(player, new Position(3079, 3425, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_5_OF_5:
                                player.getPacketSender().sendInterfaceRemoval();
                                break;
                        }
                    }
                });
        }
        return null;
    }
}
