package org.scripts.kotlin.content.commands;

import com.zarador.model.Flag;
import com.zarador.model.Skill;
import com.zarador.model.StaffRights;
import com.zarador.model.player.command.Command;
import com.zarador.world.content.skill.SkillManager;
import com.zarador.world.entity.impl.player.Player;

/**
 * Initiates the command ::Master-amount-npc id
 * @author Seba
 */
public class Master extends Command {

    public Master(StaffRights staffRights) {
        super(staffRights);
    }

    @Override
    public void execute(Player player, String[] args, StaffRights privilege) {
        for(int i = 0; i <= 24; i++) {
        	int level = SkillManager.getMaxAchievingLevel(Skill.forId(i));
                player.getSkillManager().setCurrentLevel(Skill.forId(i), level).setMaxLevel(Skill.forId(i), level).setExperience(Skill.forId(i),
                        SkillManager.getExperienceForLevel(99));
            player.getPacketSender().sendMessage("You are now a master of all skills.");
            player.getUpdateFlag().flag(Flag.APPEARANCE);
        }
    }
}
