package com.zarador.world.content;

import org.scripts.kotlin.content.dialog.Effigys.CleanEffigy;

import com.zarador.model.Animation;
import com.zarador.model.Skill;
import com.zarador.util.Misc;
import com.zarador.world.entity.impl.player.Player;

public class Effigies {
    public static void handleEffigy(Player player, int effigy) {
        if (player == null)
            return;
        if (player.getInterfaceId() > 0) {
            player.getPacketSender().sendMessage("Please close the interface you have open before doing this.");
            return;
        }
        player.getDialog().sendDialog(new CleanEffigy(player, effigy));

    }

    public static boolean checkRequirement(Player player, int skillId, int req) {
        if (player.getSkillManager().getCurrentLevel(Skill.forId(skillId)) >= req) {
            return true;
        } else {
            String skill = Misc.formatText(Skill.forId(skillId).name().toLowerCase());
            player.getPacketSender().sendInterfaceRemoval().sendMessage(
                    "You need " + Misc.anOrA(skill) + " " + skill + " level of at least " + req + " to do that.");
            return false;
        }
    }

    public static void openEffigy(Player player, int skillId) {
        int[] levelReq = {91, 93, 95, 97};
        if (player.getInteractingItem() == null)
            return;
        if (!player.getClickDelay().elapsed(4000)) {
            player.getPacketSender().sendInterfaceRemoval();
            return;
        }
        if (player.getInteractingItem().getId() == 18778) {
            if (checkRequirement(player, skillId, levelReq[0]) && player.getInventory().contains(18778)) {
                player.getInventory().delete(18778, 1);
                player.getInventory().add(18779, 1);
                player.getSkillManager().addExactExperience(Skill.forId(skillId), 15000, false);
                player.getClickDelay().reset();
                player.performAnimation(new Animation(7368));
                player.getPacketSender().sendInterfaceRemoval();
                player.setEffigy(0);
                player.setInteractingItem(null);
                return;
            }
        }
        if (player.getInteractingItem().getId() == 18779) {
            if (checkRequirement(player, skillId, levelReq[1]) && player.getInventory().contains(18779)) {
                player.getInventory().delete(18779, 1);
                player.getInventory().add(18780, 1);
                player.getSkillManager().addExactExperience(Skill.forId(skillId), 30000, false);
                player.getClickDelay().reset();
                player.performAnimation(new Animation(7368));
                player.getPacketSender().sendInterfaceRemoval();
                player.setEffigy(0);
                player.setInteractingItem(null);
                return;
            }
        }
        if (player.getInteractingItem().getId() == 18780) {
            if (checkRequirement(player, skillId, levelReq[2]) && player.getInventory().contains(18780)) {
                player.getInventory().delete(18780, 1);
                player.getInventory().add(18781, 1);
                player.getSkillManager().addExactExperience(Skill.forId(skillId), 45000, false);
                player.getClickDelay().reset();
                player.performAnimation(new Animation(7368));
                player.getPacketSender().sendInterfaceRemoval();
                player.setEffigy(0);
                player.setInteractingItem(null);
                return;
            }
        }
        if (player.getInteractingItem().getId() == 18781) {
            if (checkRequirement(player, skillId, levelReq[3]) && player.getInventory().contains(18781)) {
                player.getInventory().delete(18781, 1);
                player.getInventory().add(18782, 1);
                player.getSkillManager().addExactExperience(Skill.forId(skillId), 60000, false);
                player.getClickDelay().reset();
                player.performAnimation(new Animation(7368));
                player.getPacketSender().sendInterfaceRemoval();
                player.setEffigy(0);
                player.setInteractingItem(null);
                return;
            }
        }
    }

    public static boolean isEffigy(int item) {
        return item >= 18778 && item <= 18781;
    }
}
