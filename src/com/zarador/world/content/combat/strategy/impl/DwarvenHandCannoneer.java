package com.zarador.world.content.combat.strategy.impl;

import com.zarador.engine.task.Task;
import com.zarador.engine.task.TaskManager;
import com.zarador.model.Animation;
import com.zarador.model.Graphic;
import com.zarador.model.Projectile;
import com.zarador.world.content.combat.CombatContainer;
import com.zarador.world.content.combat.CombatType;
import com.zarador.world.content.combat.strategy.CombatStrategy;
import com.zarador.world.entity.impl.Character;
import com.zarador.world.entity.impl.npc.NPC;

public class DwarvenHandCannoneer implements CombatStrategy {

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
		NPC cannoneer = (NPC) entity;
		if (cannoneer.isChargingAttack() || victim.getConstitution() <= 0) {
			return true;
		}
		cannoneer.setChargingAttack(true);
		cannoneer.performAnimation(new Animation(cannoneer.getDefinition().getAttackAnimation()));
		cannoneer.getCombatBuilder()
				.setContainer(new CombatContainer(cannoneer, victim, 1, 3, CombatType.RANGED, true));
		TaskManager.submit(new Task(1, cannoneer, false) {
			int tick = 0;

			@Override
			public void execute() {
				if (tick == 0) {
					cannoneer.performGraphic(new Graphic(2138));
					new Projectile(cannoneer, victim, 2143, 44, 3, 43, 31, 0).sendProjectile();
				} else if (tick == 1) {
					cannoneer.setChargingAttack(false);
					stop();
				}
				tick++;
			}
		});
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
