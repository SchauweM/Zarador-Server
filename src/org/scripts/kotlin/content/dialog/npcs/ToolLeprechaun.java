package org.scripts.kotlin.content.dialog.npcs;

import com.zarador.model.Item;
import com.zarador.model.Position;
import com.zarador.model.options.fouroption.FourOption;
import com.zarador.model.options.threeoption.ThreeOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.net.packet.impl.UseItemPacketListener;
import com.zarador.world.content.transportation.TeleportHandler;
import com.zarador.world.content.transportation.TeleportType;
import com.zarador.world.entity.impl.player.Player;

public class ToolLeprechaun extends Dialog {

    public ToolLeprechaun(Player player, int state) {
        super(player);
        setState(state);
        setEndState(3);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "How may I help you?");
            case 1:
            return Dialog.createOption(new ThreeOption(
                    "Teleport me to another location.",
                    "Can you note my crops?",
                    "How do I start farming?") {
                @Override
                public void execute(Player player, OptionType option) {
                    switch(option) {
                        case OPTION_1_OF_2:
                            player.getDialog().sendDialog(new ToolLeprechaun(player, 3));
                            break;
                        case OPTION_2_OF_3:
                            for (Item item : player.getInventory().getItems()) {
                                Item noted = UseItemPacketListener.getNotedHarvest(player, item);
                                if(noted == null) {

                                } else {
                                    int amount = player.getInventory().getAmount(item.getId());
                                    player.getInventory().delete(new Item(item.getId(), amount));
                                    player.getInventory().add(new Item(noted.getId(), amount));
                                }
                            }
                            player.getPacketSender().sendMessage("You exchange your harvested crops for their noted form.");
                            player.getDialog().sendDialog(new ToolLeprechaun(player, 2));
                            break;
                        case OPTION_3_OF_3:
                            player.getDialog().sendDialog(new ToolLeprechaun(player, 4));
                            break;
                    }
                }
            });
            case 2:
                setEndState(2);
                return Dialog.createNpc(DialogHandler.HAPPY, "All your crops have been noted.");
            case 3:
                return Dialog.createOption(new FourOption(
                        "Falador Farming Patch",
                        "Catherby Farming Patch",
                        "Ardougne Farming Patch",
                        "Canifis Farming Patch") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_4:
                                TeleportHandler.teleportPlayer(player, new Position(3052, 3304, 0), TeleportType.NORMAL);
                                break;
                            case OPTION_2_OF_4:
                                TeleportHandler.teleportPlayer(player, new Position(2816, 3461, 0), TeleportType.NORMAL);
                                break;
                            case OPTION_3_OF_4:
                                TeleportHandler.teleportPlayer(player, new Position(2664, 3374, 0), TeleportType.NORMAL);
                                break;
                            case OPTION_4_OF_4:
                                TeleportHandler.teleportPlayer(player, new Position(3600, 3523, 0), TeleportType.NORMAL);
                                break;
                        }
                    }
                });
            case 4:
                setEndState(6);
                return Dialog.createNpc(DialogHandler.HAPPY, "To start farming, you can buy supplies from Farmer Fromund in Catherby.");
            case 5:
                setEndState(6);
                return Dialog.createNpc(DialogHandler.HAPPY, "There are many patches all over Argos. I can bring you to all of them!");
            case 6:
                setEndState(6);
                return Dialog.createNpc(DialogHandler.HAPPY, "If you need any more help, you can contact a staff member for assistance. Have fun farming!");

        }
        return null;
    }
}
