package com.zarador.world.content.combat.strategy.impl;

import com.zarador.engine.task.Task;
import com.zarador.engine.task.TaskManager;
import com.zarador.model.Animation;
import com.zarador.model.Graphic;
import com.zarador.model.Locations;
import com.zarador.util.Misc;
import com.zarador.world.content.combat.CombatContainer;
import com.zarador.world.content.combat.CombatType;
import com.zarador.world.content.combat.strategy.CombatStrategy;
import com.zarador.world.content.skill.impl.summoning.BossPets;
import com.zarador.world.entity.impl.Character;
import com.zarador.world.entity.impl.npc.NPC;
import com.zarador.world.entity.impl.player.Player;

public class Dragon implements CombatStrategy {

	@Override
	public boolean canAttack(Character entity, Character victim) {
		return true;
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {
		return null;
	}

	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		NPC dragon = (NPC) entity;
		if (dragon.isChargingAttack() || dragon.getConstitution() <= 0) {
			dragon.getCombatBuilder().setAttackTimer(4);
			return true;
		}
		if ((Locations.goodDistance(dragon.getPosition().copy(), victim.getPosition().copy(), 1) && Misc.getRandom(5) <= 3) || BossPets.hasPet((Player) victim, BossPets.BossPet.PRINCE_BLACK_DRAGON)) {
			dragon.performAnimation(new Animation(dragon.getDefinition().getAttackAnimation()));
			dragon.getCombatBuilder().setContainer(new CombatContainer(dragon, victim, 1, 1, CombatType.MELEE, true));
		} else {
			dragon.setChargingAttack(true);
			dragon.performAnimation(new Animation(getAnimation(dragon.getId())));
			dragon.getCombatBuilder()
					.setContainer(new CombatContainer(dragon, victim, 1, 3, CombatType.DRAGON_FIRE, true));
			TaskManager.submit(new Task(1, dragon, false) {
				int tick = 0;

				@Override
				public void execute() {
					/*
					 * if(tick == 1 && dragon.getId() == 50) { new
					 * Projectile(dragon, victim, 393 + Misc.getRandom(3), 44,
					 * 3, 43, 43, 0).sendProjectile(); } else
					 */
					if (tick == 2) {
						victim.performGraphic(new Graphic(5));
					} else if (tick == 3) {
						victim.performGraphic(new Graphic(5));
						dragon.setChargingAttack(false).getCombatBuilder().setAttackTimer(6);
						stop();
					}
					tick++;
				}
			});

		}
		return true;
	}

	public static int getAnimation(int npc) {
		int anim = 12259;
		if (npc == 50 || npc == 4000 || npc == 6593)
			anim = 81;
		else if (npc == 5363 || npc == 1590 || npc == 1591 || npc == 1592)
			anim = 14246;
		else if (npc == 51)
			anim = 13152;
		else if (npc == 3068)
			anim = 1589;
		else if (npc == 3069)
			anim = 1589;
		return anim;
	}

	@Override
	public int attackDelay(Character entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(Character entity) {
		return 5;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MIXED;
	}
}
