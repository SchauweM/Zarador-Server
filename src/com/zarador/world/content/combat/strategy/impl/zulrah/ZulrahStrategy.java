package com.zarador.world.content.combat.strategy.impl.zulrah;

import com.zarador.engine.task.Task;
import com.zarador.engine.task.TaskManager;
import com.zarador.model.Flag;
import com.zarador.model.Position;
import com.zarador.util.Misc;
import com.zarador.world.content.combat.CombatContainer;
import com.zarador.world.content.combat.CombatType;
import com.zarador.world.content.combat.strategy.CombatStrategy;
import com.zarador.world.entity.impl.Character;
import com.zarador.world.entity.impl.npc.NPC;

public class ZulrahStrategy implements CombatStrategy {
	
	public static int taskPhase = 0;
	public static boolean isTransforming = false;

	@Override
	public boolean canAttack(Character entity, Character victim) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		
		if(isTransforming)
			return true; 
		
		int rand = Misc.getRandom(10);
		System.out.println(rand);
		if(rand >= 8) {
			transformZulrah(ZulrahColour.RED, entity, victim, Zulrah.SOUTH_POSITION);
		}
		return true;
	}
	
	public static void transformZulrah(ZulrahColour colour, Character entity, Character victim, Position newPosition) {
		isTransforming = true;
		victim.getCombatBuilder().reset();
		TaskManager.submit(new Task(3, entity, false) {
			@Override
			public void execute() {
				if(taskPhase == 0) {
					entity.performAnimation(Zulrah.ZULRAH_DIVE_ANIMATION);
				} else {
					NPC zulrah = (NPC) entity;
					entity.moveTo(newPosition);
					zulrah.setTransformationId(colour.getNpc());
					zulrah.getUpdateFlag().flag(Flag.TRANSFORM);
					zulrah.performAnimation(Zulrah.ZULRAH_RISE_ANIMATION);
					taskPhase = 0;
					isTransforming = false;
					stop();
				}
				taskPhase++;
			}
		});
	}

	@Override
	public int attackDelay(Character entity) {
		// TODO Auto-generated method stub
		return 8;
	}

	@Override
	public int attackDistance(Character entity) {
		// TODO Auto-generated method stub
		return 16;
	}

	@Override
	public CombatType getCombatType() {
		// TODO Auto-generated method stub
		return CombatType.MIXED;
	}

}
