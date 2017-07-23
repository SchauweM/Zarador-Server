package org.scripts.kotlin.content.dialog.teleports;

import com.zarador.model.Position;
import com.zarador.model.options.fiveoption.FiveOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.content.transportation.TeleportHandler;
import com.zarador.world.entity.impl.player.Player;

public class SkillingAreas extends Dialog {

    public Dialog dialog = this;

    public SkillingAreas(Player player) {
        super(player);
        setEndState(1);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createOption(new FiveOption(
                        "Catherby",
                        "Low Skilling Zone",
                        "High Skilling Zone",
                        "Living Rock Cavern",
                        "Resource Area @bla@(@red@Wild@bla@)") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(2809, 3435, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_2_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(2802, 2785, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_3_OF_5:
                                if (player.getSkillManager().getTotalLevel() >= 1000) {
                                    TeleportHandler.teleportPlayer(player, new Position(2852, 2960, 0), player.getSpellbook().getTeleportType());
                                } else {
                                    player.getPacketSender().sendInterfaceRemoval();
                                    player.getPacketSender().sendMessage("@red@You need to have over 1000 total level to teleport to this zone.");
                                }
                                break;
                            case OPTION_4_OF_5:
                                TeleportHandler.teleportPlayer(player, new Position(3654, 5114, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_5_OF_5:
                                player.setNpcClickId(747);
                                player.getDialog().sendDialog(new ResourceArea(player));
                                break;
                        }
                    }
                });
        }
        return null;
    }
}
