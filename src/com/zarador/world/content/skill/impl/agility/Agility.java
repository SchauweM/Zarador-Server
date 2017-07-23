package com.zarador.world.content.skill.impl.agility;

import com.zarador.model.GameObject;
import com.zarador.model.Skill;
import com.zarador.model.container.impl.Equipment;
import com.zarador.util.Misc;
import com.zarador.world.content.achievements.Achievements;
import com.zarador.world.content.achievements.Achievements.AchievementData;
import com.zarador.world.entity.impl.player.Player;

public class Agility {

	public static boolean handleObject(Player p, GameObject object) {
		if (object.getId() == 2309) {
			if (p.getSkillManager().getMaxLevel(Skill.AGILITY) < 55) {
				p.getPacketSender().sendMessage("You need an Agility level of at least 55 to enter this course.");
				return true;
			} if((System.currentTimeMillis() - p.lastAgilityClick) < 2500) {
				p.getPacketSender().sendMessage("You can't do this, that quickly!");
				return true;
			}
			p.getPacketSender().sendMessage("If you get stuck in this course, type ::stuck!");
		}
		if (object.getId() == 9311 || object.getId() == 9312) {
			if (p.getSkillManager().getMaxLevel(Skill.AGILITY) < 21) {
				p.getPacketSender().sendMessage("You need an Agility level of at least 21 to use this obstacle.");
				return true;
			}
		}
		if((p.getPosition().getX() >= 2989 && p.getPosition().getX() <= 3009) && (p.getPosition().getY() >= 3916 && p.getPosition().getY() <= 3966)) {
			if (p.getSkillManager().getMaxLevel(Skill.AGILITY) < 52) {
				p.getPacketSender().sendMessage("You need an Agility level of at least 52 to use this course.");
				return true;
			}
		}
		ObstacleData agilityObject = ObstacleData.forId(object.getId());
		if (agilityObject != null) {
			if (p.isCrossingObstacle())
				return true;
			p.lastThieve = System.currentTimeMillis();
			p.setPositionToFace(object.getPosition());
			p.setResetPosition(p.getPosition());
			p.setCrossingObstacle(true);
			agilityObject.cross(p);
			Achievements.doProgress(p, AchievementData.CLIMB_50_AGILITY_OBSTACLES);
		}
		return false;
	}

	public static boolean passedAllObstacles(Player player) {
		for (boolean crossedObstacle : player.getCrossedObstacles()) {
			if (!crossedObstacle)
				return false;
		}
		return true;
	}

	public static void resetProgress(Player player) {
		for (int i = 0; i < player.getCrossedObstacles().length; i++)
			player.setCrossedObstacle(i, false);
	}

	public static boolean isSucessive(Player player) {
		return Misc.getRandom(player.getSkillManager().getCurrentLevel(Skill.AGILITY) / 2) > 1;
	}

	public static void addExperience(Player player, double experience) {
		boolean agile = player.getEquipment().get(Equipment.BODY_SLOT).getId() == 14936 && player.getEquipment().get(Equipment.LEG_SLOT).getId() == 14938;
		player.getSkillManager().addSkillExperience(Skill.AGILITY, agile ? (experience *= 1.5) : experience);
	}
}
