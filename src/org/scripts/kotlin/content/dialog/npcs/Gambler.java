package org.scripts.kotlin.content.dialog.npcs;

import com.zarador.model.options.fiveoption.FiveOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.content.lottery.Lottery;
import com.zarador.world.content.lottery.LotterySaving;
import com.zarador.world.entity.impl.player.Player;

public class Gambler extends Dialog {

    public Gambler(Player player, int state, int endState) {
        super(player);
        setState(state);
        setEndState(endState);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "Hmmm, be careful with these sneaky gamblers...");
            case 1:
                return Dialog.createOption(new FiveOption(
                        "What is the Lottery?",
                        "I want to enter the $50 Lottery.",
                        "How many tickets do I have in the Lottery?",
                        "How many tickets are in total are in the Lottery?",
                        "When is the next drawing?") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_5:
                                player.getDialog().sendDialog(new Gambler(player, 2, 2));
                                break;
                            case OPTION_2_OF_5:
//                                if(player.getStaffRights() == StaffRights.OWNER || player.getStaffRights() == StaffRights.MANAGER) {
//                                    player.getDialog().sendDialog(new Gambler(player, 5, 5));
//                                    break;
//                                }
                                if(!LotterySaving.LOTTERY_ON) {
                                    player.getDialog().sendDialog(new Gambler(player, 6, 6));
                                    break;
                                }
                                if(player.getInventory().contains(13664)) {
                                    LotterySaving.lottery1.addOffer(new Lottery(player.getUsername()));
                                    player.getInventory().delete(13664, 1);
                                    player.getDialog().sendDialog(new Gambler(player, 4, 4));
                                    LotterySaving.log("enter", player.getUsername());
                                } else {
                                    player.getDialog().sendDialog(new Gambler(player, 3, 3));
                                }
                                break;
                            case OPTION_3_OF_5:
                                if(!LotterySaving.LOTTERY_ON) {
                                    player.getDialog().sendDialog(new Gambler(player, 6, 6));
                                    break;
                                }
                                player.getDialog().sendDialog(new Gambler(player, 7, 7));
                                break;
                            case OPTION_4_OF_5:
                                if(!LotterySaving.LOTTERY_ON) {
                                    player.getDialog().sendDialog(new Gambler(player, 6, 6));
                                    break;
                                }
                                player.getDialog().sendDialog(new Gambler(player, 8, 8));
                                break;
                            case OPTION_5_OF_5:
                                if(!LotterySaving.LOTTERY_ON) {
                                    player.getDialog().sendDialog(new Gambler(player, 6, 6));
                                    break;
                                }
                                player.getDialog().sendDialog(new Gambler(player, 9, 9));
                                break;
                        }
                    }
                });
            case 2:
                return Dialog.createNpc(DialogHandler.CALM, "The Lottery is a pot of $50 that is drawn every 7 days. You can get lottery tickets by killing monsters, or other various things in Argos.");
            case 3:
                return Dialog.createNpc(DialogHandler.CALM, "You need to have a $50 Lottery ticket in order to enter the lottery.");
            case 4:
                return Dialog.createNpc(DialogHandler.CALM, "Congratulations, you have entered the Lottery!\\nNext drawing:  "+LotterySaving.getTime()+"");
            case 5:
                return Dialog.createNpc(DialogHandler.CALM, "Sorry, Managers & Owners can't enter the lottery.");
            case 6:
                return Dialog.createNpc(DialogHandler.CALM, "Sorry, the next $50 Lottery has not started yet.");
            case 7:
                return Dialog.createNpc(DialogHandler.CALM, "You currently have "+LotterySaving.getTicketsInLottery(getPlayer())+" tickets in the lottery.");
            case 8:
                return Dialog.createNpc(DialogHandler.CALM, "There are currently "+LotterySaving.lottery1.getOffers().size()+" tickets in the Lottery.");
            case 9:
                return Dialog.createNpc(DialogHandler.CALM, "Next drawing:  "+LotterySaving.getTime()+"");
            }
        return null;
    }
}
