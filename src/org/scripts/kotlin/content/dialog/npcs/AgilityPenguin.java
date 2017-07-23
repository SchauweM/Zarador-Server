package org.scripts.kotlin.content.dialog.npcs;

import com.zarador.model.Position;
import com.zarador.model.Skill;
import com.zarador.model.container.impl.Shop;
import com.zarador.model.input.impl.BuyAgilityExperience;
import com.zarador.model.options.threeoption.ThreeOption;
import com.zarador.model.options.twooption.TwoOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.content.transportation.TeleportHandler;
import com.zarador.world.entity.impl.player.Player;

public class AgilityPenguin extends Dialog {

    public AgilityPenguin(Player player, int state) {
        super(player);
        setState(state);
        setEndState(4);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "Squishy squishy!");
            case 1:
                return Dialog.createPlayer(DialogHandler.CALM, "What?");
            case 2:
                return Dialog.createNpc(DialogHandler.CALM, "What would you like me to assist you with today?");
            case 3:
                return Dialog.createOption(new ThreeOption(
                        "Teleport me to an agility course.",
                        "I want to trade marks of grace for items.",
                        "I want to trade marks of grace for experience.") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_3:
                                player.getDialog().sendDialog(new AgilityPenguin(player, 4));
                                break;
                            case OPTION_2_OF_3:
                                Shop.ShopManager.getShops().get(28).open(player);
                                break;
                            case OPTION_3_OF_3:
                                player.getPacketSender().sendEnterAmountPrompt("How many tickets would you like to exchange? (1 mark = "+player.getSkillManager().getBoostedExperience(8, false, true)+" xp)");
                                player.setInputHandling(new BuyAgilityExperience());
                                break;
                        }
                    }
                });
            case 4:
                return Dialog.createOption(new ThreeOption(
                        "Gnome Course",
                        "Barbarian Course",
                        "Wilderness Course @bla@(@red@Wild@bla@)") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_3:
                                TeleportHandler.teleportPlayer(player, new Position(2474, 3438, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_2_OF_3:
                                if(player.getSkillManager().getCurrentLevel(Skill.AGILITY) < 35) {
                                    player.getPacketSender().sendMessage("The barbarian outpost agility course requires 35 agility to train at!");
                                    player.getPacketSender().sendInterfaceRemoval();
                                    return;
                                }
                                TeleportHandler.teleportPlayer(player, new Position(2551, 3554, 0), player.getSpellbook().getTeleportType());
                                break;
                            case OPTION_3_OF_3:
                                if(player.getSkillManager().getCurrentLevel(Skill.AGILITY) < 52) {
                                    player.getPacketSender().sendMessage("The barbarian outpost agility course requires 52 agility to train at!");
                                    player.getPacketSender().sendInterfaceRemoval();
                                    return;
                                }
                                TeleportHandler.teleportPlayer(player, new Position(2998, 3932, 0), player.getSpellbook().getTeleportType());
                                break;
                        }
                    }
                });
            case 5:
                return Dialog.createOption(new TwoOption(
                        "I want to trade marks of grace for items.",
                        "I want to trade marks of grace for experience.") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_2:
                                Shop.ShopManager.getShops().get(28).open(player);
                                break;
                            case OPTION_2_OF_2:
                                player.getPacketSender().sendInterfaceRemoval();
                                player.getPacketSender().sendEnterAmountPrompt("How many tickets would you like to exchange? (1 mark = "+player.getSkillManager().getBoostedExperience(8, false, true)+" xp)");
                                player.setInputHandling(new BuyAgilityExperience());
                                break;
                        }
                    }
                });
            }
        return null;
    }
}
