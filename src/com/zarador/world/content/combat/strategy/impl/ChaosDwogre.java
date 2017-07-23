package com.zarador.world.content.combat.strategy.impl;

import com.zarador.engine.task.Task;
import com.zarador.engine.task.TaskManager;
import com.zarador.model.Animation;
import com.zarador.model.Locations;
import com.zarador.model.Projectile;
import com.zarador.util.Misc;
import com.zarador.world.content.combat.CombatContainer;
import com.zarador.world.content.combat.CombatType;
import com.zarador.world.content.combat.strategy.CombatStrategy;
import com.zarador.world.entity.impl.Character;
import com.zarador.world.entity.impl.npc.NPC;

public class ChaosDwogre implements CombatStrategy {

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
		NPC dwogre = (NPC) entity;
		if (dwogre.isChargingAttack() || victim.getConstitution() <= 0) {
			return true;
		}
		if (Locations.goodDistance(dwogre.getPosition().copy(), victim.getPosition().copy(), 1)
				&& Misc.getRandom(5) <= 3) {
			dwogre.performAnimation(new Animation(dwogre.getDefinition().getAttackAnimation()));
			dwogre.getCombatBuilder().setContainer(new CombatContainer(dwogre, victim, 1, 1, CombatType.MELEE, true));
		} else {
			dwogre.setChargingAttack(true);
			dwogre.performAnimation(new Animation(dwogre.getDefinition().getAttackAnimation()));
			dwogre.getCombatBuilder().setContainer(new CombatContainer(dwogre, victim, 1, 3, CombatType.MAGIC, true));
			TaskManager.submit(new Task(1, dwogre, false) {
				int tick = 0;

				@Override
				public void execute() {
					if (tick == 0) {
						new Projectile(dwogre, victim, 339, 44, 3, 43, 31, 0).sendProjectile();
					} else if (tick == 1) {
						dwogre.setChargingAttack(false);
						stop();
					}
					tick++;
				}
			});
		}
		return true;
	}

	@Override
	public int attackDelay(Character entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(Character entity) {
		return 4;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MIXED;
	}
}
