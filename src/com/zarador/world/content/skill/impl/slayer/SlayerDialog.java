package com.zarador.world.content.skill.impl.slayer;

import com.zarador.model.definitions.ItemDefinition;
import com.zarador.model.options.fiveoption.FiveOption;
import com.zarador.model.options.threeoption.ThreeOption;
import com.zarador.model.options.twooption.TwoOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class SlayerDialog extends Dialog {

    public Dialog dialog = this;
    public SlayerMasters slayerMaster;
    public Player other;

    public SlayerDialog(Player player, int state, Player other) {
        super(player);
        setState(state);
        setEndState(13);
        this.other = other;
    }

    @Override
    public DialogMessage getMessage() {
        slayerMaster = SlayerMasters.forNpcId(getPlayer().getNpcClickId());
        switch (getState()) {
            case 0:
                if(getPlayer().getSlayer().hasMasterRequirements(slayerMaster)) {
                    return Dialog.createNpc(DialogHandler.CALM, "Hello! How may I assist you today?");
                } else {
                    setEndState(0);
                    return Dialog.createNpc(DialogHandler.CALM, "Hmm a creature of a interesting kind... Come back when you have atleast "+slayerMaster.getCombatLevelRequirement()+" combat!");
                }
            case 1:
                if(getPlayer().getSlayer().getSlayerTask() != null) {
                    return Dialog.createOption(new FiveOption(
                            "I need a slayer assignment.",
                            "How many slayer kills do I have left?",
                            "Do you have any advice for my current task?",
                            "Can you give me an easier task please.",
                            "Can you reset my slayer task?") {
                        @Override
                        public void execute(Player player, OptionType option) {
                            switch (option) {
                                case OPTION_1_OF_5:
                                    player.getSlayer().assignSlayerTask(slayerMaster, false);
                                    break;
                                case OPTION_2_OF_5:
                                    player.getSlayer().checkAmountLeft();
                                    break;
                                case OPTION_3_OF_5:
                                    player.getSlayer().checkRecommendations();
                                    break;
                                case OPTION_4_OF_5:
                                    player.getSlayer().giveEasierTask(slayerMaster);
                                    break;
                                case OPTION_5_OF_5:
                                    player.getSlayer().resetTask();
                                    break;
                            }
                        }
                    });
                } else if(!getPlayer().getSlayer().isOnlyBossDuradel() && slayerMaster == SlayerMasters.DURADEL) {
                    return Dialog.createOption(new ThreeOption (
                            "I need a slayer assignment.",
                            "I want only boss tasks from you (50 points)",
                            "I don't need anything.") {
                        @Override
                        public void execute(Player player, OptionType option) {
                            switch (option) {
                                case OPTION_1_OF_3:
                                    player.getSlayer().assignSlayerTask(slayerMaster, false);
                                    break;
                                case OPTION_2_OF_3:
                                    if(player.getPointsHandler().getSlayerPoints() < 50) {
                                        player.getDialog().sendDialog(new SlayerDialog(player, 14, null));
                                    } else {
                                        player.getPointsHandler().setSlayerPoints(-50, true);
                                        player.refreshPanel();
                                        player.getSlayer().setOnlyBossDuradel(true);
                                        player.save();
                                        player.getDialog().sendDialog(new SlayerDialog(player, 15, null));
                                    }
                                    break;
                                case OPTION_3_OF_3:
                                    player.getPacketSender().sendInterfaceRemoval();
                                    break;
                            }
                        }
                    });
                } else {
                    return Dialog.createOption(new TwoOption (
                            "I need a slayer assignment.",
                            "I don't need anything.") {
                        @Override
                        public void execute(Player player, OptionType option) {
                            switch (option) {
                                case OPTION_1_OF_2:
                                    player.getSlayer().assignSlayerTask(slayerMaster, false);
                                    break;
                                case OPTION_2_OF_2:
                                    player.getPacketSender().sendInterfaceRemoval();
                                    break;
                            }
                        }
                    });
                }
            case 2:
                setEndState(2);
                return Dialog.createNpc(DialogHandler.CALM, "You are assigned to kill "+getPlayer().getSlayer().getAmountLeft()+" "+getPlayer().getSlayer().getTaskName()+"s! Come back when you are done for a new assignment.");
            case 3:
                setEndState(4);
                return Dialog.createNpc(DialogHandler.CALM, "You have been assigned to kill "+getPlayer().getSlayer().getAmountLeft()+" "+getPlayer().getSlayer().getTaskName()+"s.");
            case 4:
                return Dialog.createPlayer(DialogHandler.CALM, "Awesome I will get started!");
            case 5:
                setEndState(5);
                return Dialog.createNpc(DialogHandler.CALM, "You have "+getPlayer().getSlayer().getAmountLeft()+" "+getPlayer().getSlayer().getTaskName()+"s left to kill on your current slayer task.");
            case 6:
                setEndState(6);
                if(getPlayer().getSlayer().getSlayerTask().getEquipmentId() == -1) {
                    return Dialog.createNpc(DialogHandler.CALM, "I don't have any advice, good luck!");
                } else {
                    return Dialog.createNpc(DialogHandler.CALM, "Hmm, a wise one once told me something about "+ ItemDefinition.forId(getPlayer().getSlayer().getSlayerTask().getEquipmentId()).getName()+"...");
                }
            case 7:
                 setEndState(7);
                 return Dialog.createNpc(DialogHandler.CALM, "I can't offer you a easier slayer task. Sorry!");
            case 8:
                 setEndState(8);
                 getPlayer().getPacketSender().sendMessage("Congratulations! You have completed "+getPlayer().getSlayer().getSlayerStreak()+" tasks in a row and gained "+getPlayer().getSlayer().getPointsToGive()+" Slayer Points.");
                 return Dialog.createNpc(DialogHandler.CALM, "Congratulations! You have completed "+getPlayer().getSlayer().getSlayerStreak()+" tasks in a row and gained "+getPlayer().getSlayer().getPointsToGive()+" Slayer Points.");
            case 9:
                setEndState(9);
                return Dialog.createNpc(DialogHandler.CALM, "Hmm a creature of a interesting kind... Come back when you have atleast "+slayerMaster.getCombatLevelRequirement()+" combat!");
            case 10:
                return Dialog.createNpc(DialogHandler.CALM, "You already have a task, what can I do for you?");
            case 11:
                setEndState(12);
                return Dialog.createNpc(DialogHandler.CALM, "Hello! My student "+other.getUsername()+" is wanting to do a duo slayer task with you. He is currently assigned to slay "+other.getSlayer().getAmountLeft()+" "+other.getSlayer().getSlayerTask().getName()+"s.");
            case 12:
                return Dialog.createOption(new TwoOption(
                        "Accept duo assignment",
                        "Decline duo assignment") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_2:
                                player.getSlayer().assignDuoTask(other);
                                setEndState(13);
                                break;
                            case OPTION_2_OF_2:
                                player.getPacketSender().sendInterfaceRemoval();
                                break;
                        }
                    }
                });
            case 13:
                setEndState(13);
                return Dialog.createNpc(DialogHandler.CALM, "Goodluck, your assignment has been set!");
            case 14:
                setEndState(14);
                return Dialog.createNpc(DialogHandler.CALM, "You need atleast 50 slayer points in order to only get boss tasks from me.");
            case 15:
                setEndState(15);
                return Dialog.createNpc(DialogHandler.CALM, "Congratulations! You will now only get boss slayer assignments from me.");
        }
        return null;
    }
}
