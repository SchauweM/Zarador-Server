package org.scripts.kotlin.content.dialog.npcs;

import com.zarador.model.Position;
import com.zarador.model.container.impl.Shop;
import com.zarador.model.options.threeoption.ThreeOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.content.transportation.TeleportHandler;
import com.zarador.world.content.transportation.TeleportType;
import com.zarador.world.entity.impl.player.Player;

public class Aleck extends Dialog {

    public Aleck(Player player, int state) {
        super(player);
        setState(2);
        setEndState(1);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "Hello! Would you like to see what hunter items I have for sale?");
            case 1:
                return Dialog.createOption(new ThreeOption(
                        "Yes, show me what you have to offer!",
                        "I need to go to a hunter training location.",
                        "Nevermind, I don't need anything.") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_3:
                                Shop.ShopManager.getShops().get(39).open(player);
                                break;
                            case OPTION_2_OF_3:
                                player.getDialog().sendDialog(new Aleck(player, 2));
                                break;
                            case OPTION_3_OF_3:
                                player.getPacketSender().sendInterfaceRemoval();
                                break;
                        }
                    }
                });
            case 2:
                return Dialog.createOption(new ThreeOption(
                        "Puro Puro",
                        "Karamja",
                        "Ape Atoll") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_3:
                                TeleportHandler.teleportPlayer(player, new Position(2589, 4319, 0), TeleportType.PURO_PURO);
                                break;
                            case OPTION_2_OF_3:
                                TeleportHandler.teleportPlayer(player, new Position(2922, 2885, 0), TeleportType.PURO_PURO);
                                break;
                            case OPTION_3_OF_3:
                                TeleportHandler.teleportPlayer(player, new Position(2704, 2775, 0), TeleportType.PURO_PURO);
                                break;
                        }
                    }
                });
            }
        return null;
    }
}
