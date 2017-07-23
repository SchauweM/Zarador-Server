package org.scripts.kotlin.content.dialog;

import com.zarador.model.Position;
import com.zarador.model.options.fiveoption.FiveOption;
import com.zarador.model.options.fouroption.FourOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.content.transportation.TeleportHandler;
import com.zarador.world.content.transportation.TeleportType;
import com.zarador.world.entity.impl.player.Player;

public class PortalDevice extends Dialog {

    public Dialog dialog = this;

    public PortalDevice(Player player, int state) {
        super(player);
        setEndState(0);
        setState(state);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createOption(new FiveOption(
                        "Frost Dragons",
                        "Scorpia @bla@(@red@Wild@bla@)",
                        "Vet'ion @bla@(@red@Wild@bla@)",
                        "Callisto @bla@(@red@Wild@bla@)",
                        "More") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_5:
                                player.getPacketSender().sendInterfaceRemoval();
                                TeleportHandler.teleportPlayer(player, new Position(2793, 3794, 0), TeleportType.NORMAL);
                                break;
                            case OPTION_2_OF_5:
                                player.getPacketSender().sendInterfaceRemoval();
                                TeleportHandler.teleportPlayer(player, new Position(3136, 3715, 0), TeleportType.NORMAL);
                                break;
                            case OPTION_3_OF_5:
                                player.getPacketSender().sendInterfaceRemoval();
                                TeleportHandler.teleportPlayer(player, new Position(3242, 3790, 0), TeleportType.NORMAL);
                                break;
                            case OPTION_4_OF_5:
                                player.getPacketSender().sendInterfaceRemoval();
                                TeleportHandler.teleportPlayer(player, new Position(3184, 3668, 0), TeleportType.NORMAL);
                                break;
                            case OPTION_5_OF_5:
                                player.getDialog().sendDialog(new PortalDevice(player, 1));
                                break;
                        }
                    }
                });
            case 1:
                setEndState(1);
                return Dialog.createOption(new FourOption(
                        "Chaos Fanatic @bla@(@red@Wild@bla@)",
                        "Crazy Archaeologist @bla@(@red@Wild@bla@)",
                        "Venenatis @bla@(@red@Wild@bla@)",
                        "WildyWyrm @bla@(@red@Wild@bla@)") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_4:
                                player.getPacketSender().sendInterfaceRemoval();
                                TeleportHandler.teleportPlayer(player, new Position(2981, 3854, 0), TeleportType.NORMAL);
                                break;
                            case OPTION_2_OF_4:
                                player.getPacketSender().sendInterfaceRemoval();
                                TeleportHandler.teleportPlayer(player, new Position(2978, 3702, 0), TeleportType.NORMAL);
                                break;
                            case OPTION_3_OF_4:
                                player.getPacketSender().sendInterfaceRemoval();
                                TeleportHandler.teleportPlayer(player, new Position(3146, 3800, 0), TeleportType.NORMAL);
                                break;
                            case OPTION_4_OF_4:
                                player.getPacketSender().sendInterfaceRemoval();
                                TeleportHandler.teleportPlayer(player, new Position(3194, 3831, 0), TeleportType.NORMAL);
                                break;
                        }
                    }
                });
        }
        return null;
    }
}
