package org.scripts.kotlin.content.dialog;

import com.zarador.model.Skill;
import com.zarador.model.options.fiveoption.FiveOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class KnightLamp extends Dialog {

    private String msg = "";

    private void bonusLevel(Player player, Skill skill) {
        player.getPacketSender().sendInterfaceRemoval();
        if (player.getInventory().contains(10586)) {
            player.getPacketSender().sendInterfaceRemoval();
            player.getInventory().delete(10586, 1);
            player.getSkillManager().setMaxLevel(skill, 99);
            player.getSkillManager().setCurrentLevel(skill, 99);
            player.getSkillManager().setExperience(skill, 13034431, true);
            player.getPacketSender().sendInterfaceRemoval();
            msg = "You have boosted your " + skill.getName() + " level to 99.";
        } else {
            msg = "You do not have a lamp in your inventory.";
        }
    }

    public Dialog dialog = this;

    public KnightLamp(Player player) {
        super(player);
        setEndState(2);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createStatement(DialogHandler.CALM, "As a Knight you're given an xp lamp that will grant you a 99",  "in a certain combat stats. Please choose which " , "stat you want to make 99.");
            case 1:
                return Dialog.createOption(new FiveOption(
                        "Attack",
                        "Defence",
                        "Strength",
                        "Range",
                        "Magic") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_5:
                                bonusLevel(player, Skill.ATTACK);
                                setState(2);
                                player.getDialog().sendDialog(dialog);
                                break;
                            case OPTION_2_OF_5:
                                bonusLevel(player, Skill.DEFENCE);
                                setState(2);
                                player.getDialog().sendDialog(dialog);
                                break;
                            case OPTION_3_OF_5:
                                bonusLevel(player, Skill.STRENGTH);
                                setState(2);
                                player.getDialog().sendDialog(dialog);
                                break;
                            case OPTION_4_OF_5:
                                bonusLevel(player, Skill.RANGED);
                                setState(2);
                                player.getDialog().sendDialog(dialog);
                                break;
                            case OPTION_5_OF_5:
                                bonusLevel(player, Skill.MAGIC);
                                setState(2);
                                player.getDialog().sendDialog(dialog);
                                break;
                        }
                    }
                });
            case 2:
                return Dialog.createStatement(DialogHandler.CALM, msg);
        }
        return null;
    }
}
