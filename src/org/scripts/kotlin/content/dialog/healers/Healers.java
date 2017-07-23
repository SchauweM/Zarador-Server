package org.scripts.kotlin.content.dialog.healers;

import com.zarador.model.Skill;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

public class Healers extends Dialog {

    public Dialog dialog = this;

    public Healers(Player player) {
        super(player);
        setEndState(3);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createNpc(DialogHandler.CALM, "Hello! Would you like to be healed?");
            case 1:
                return Dialog.createPlayer(DialogHandler.CALM, "Yes, please.");
            case 2:
                return Dialog.createNpc(DialogHandler.CALM, "Okay no problem!");
            case 3:
                getPlayer().getSkillManager().setCurrentLevel(Skill.CONSTITUTION, (getPlayer().getSkillManager().getMaxLevel(Skill.CONSTITUTION)));
                if(getPlayer().getDonatorRights().isDonator()) {
                    if (getPlayer().getDonatorRights().ordinal() >= 1) {
                        getPlayer().setPoisonDamage(0);
                        getPlayer().getPacketSender().sendConstitutionOrbPoison(false);
                    }
                    if (getPlayer().getDonatorRights().ordinal() >= 2) {
                        getPlayer().getSkillManager().setCurrentLevel(Skill.PRAYER, (getPlayer().getSkillManager().getMaxLevel(Skill.PRAYER)));
                    }
                    if (getPlayer().getDonatorRights().ordinal() >= 3) {
                        for (Skill skill : Skill.values()) {
                            if (getPlayer().getSkillManager().getCurrentLevel(skill) < getPlayer().getSkillManager().getMaxLevel(skill)) {
                                getPlayer().getSkillManager().setCurrentLevel(skill, getPlayer().getSkillManager().getMaxLevel(skill));
                            }
                        }
                        getPlayer().getSkillManager().setCurrentLevel(Skill.PRAYER, (getPlayer().getSkillManager().getMaxLevel(Skill.PRAYER)));
                    }
                    if (getPlayer().getDonatorRights().ordinal() >= 4) {
                        if (getPlayer().getRunEnergy() < 100) {
                            getPlayer().setRunEnergy(100);
                        }
                    }
                    if (getPlayer().getDonatorRights().ordinal() >= 5) {
                        if(getPlayer().getSpecialPercentage() < 100) {
                            getPlayer().setSpecialPercentage(100);
                        }
                    }
                }
                return Dialog.createPlayer(DialogHandler.CALM, "Thank you!");
            }
        return null;
    }
}
