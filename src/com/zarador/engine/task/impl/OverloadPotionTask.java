package com.zarador.engine.task.impl;

import com.zarador.engine.task.Task;
import com.zarador.model.Animation;
import com.zarador.model.CombatIcon;
import com.zarador.model.Hit;
import com.zarador.model.Hitmask;
import com.zarador.model.Skill;
import com.zarador.model.Locations.Location;
import com.zarador.world.content.Consumables;
import com.zarador.world.entity.impl.player.Player;

public class OverloadPotionTask extends Task {

	public OverloadPotionTask(Player player) {
		super(1, player, true);
		this.player = player;
	}

	final Player player;

	@Override
	public void execute() {
		if (player == null || !player.isRegistered()) {
			stop();
			return;
		}
		int timer = player.getOverloadPotionTimer();
		if (timer == 600 || timer == 598 || timer == 596 || timer == 594 || timer == 592) {
			player.performAnimation(new Animation(3170));
			player.dealDamage(null, new Hit(100, Hitmask.RED, CombatIcon.NONE));
		}
		if (timer == 600 || timer == 570 || timer == 540 || timer == 510 || timer == 480 || timer == 450 || timer == 420
				|| timer == 390 || timer == 360 || timer == 330 || timer == 300 || timer == 270 || timer == 240
				|| timer == 210 || timer == 180 || timer == 150 || timer == 120 || timer == 90 || timer == 60
				|| timer == 30) {
			Consumables.overloadIncrease(player, Skill.ATTACK, 0.27);
			Consumables.overloadIncrease(player, Skill.STRENGTH, 0.27);
			Consumables.overloadIncrease(player, Skill.DEFENCE, 0.27);
			Consumables.overloadIncrease(player, Skill.RANGED, 0.235);
			player.getSkillManager().setCurrentLevel(Skill.MAGIC,
					player.getSkillManager().getMaxLevel(Skill.MAGIC) + 7);
		}
		player.setOverloadPotionTimer(timer - 1);
		if (player.getOverloadPotionTimer() == 20)
			player.getPacketSender().sendMessage("@red@Your Overload's effect is about to run out.");
		if (player.getOverloadPotionTimer() <= 0 || player.getLocation() == Location.DUEL_ARENA
				|| player.getLocation() == Location.WILDERNESS
						&& !player.getStaffRights().isManagement()) {
			player.getPacketSender().sendMessage("@red@Your Overload's effect has run out.");
			for (int i = 0; i < 7; i++) {
				if (i == 3 || i == 5)
					continue;
				player.getSkillManager().setCurrentLevel(Skill.forId(i), player.getSkillManager().getMaxLevel(i));
			}
			player.setOverloadPotionTimer(0);

			// ovl effect runs out - 500 HP restored
			int currentHP = player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION);
			int maxHP = player.getSkillManager().getMaxLevel(Skill.CONSTITUTION);
			if (currentHP + 500 <= maxHP) {
				player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, currentHP + 500, true);
			} else {
				player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, maxHP, true);
			}
			stop();
		}
	}
}
