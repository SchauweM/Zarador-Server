package com.zarador.world.content.combat.strategy.impl.zulrah;

import com.zarador.engine.task.Task;
import com.zarador.engine.task.TaskManager;
import com.zarador.model.Animation;
import com.zarador.model.Direction;
import com.zarador.model.Flag;
import com.zarador.model.GameObject;
import com.zarador.model.Position;
import com.zarador.model.Projectile;
import com.zarador.util.Misc;
import com.zarador.world.World;
import com.zarador.world.content.CustomObjects;
import com.zarador.world.content.combat.CombatContainer;
import com.zarador.world.content.combat.CombatType;
import com.zarador.world.content.combat.strategy.CombatStrategy;
import com.zarador.world.entity.impl.Character;
import com.zarador.world.entity.impl.npc.NPC;
import com.zarador.world.entity.impl.player.Player;

public class ZulrahStrategy implements CombatStrategy {
	
	public static int taskPhase = 0;
	public static boolean isTransforming = false;
	public static boolean isSpawningClouds = false;
	public static int zulrahPhase = 1;
	public static boolean invisable = false;
	
	public static NPC ZULRAH;
	public static NPC CLOUD1;
	public static NPC CLOUD2;
	public static NPC CLOUD3;
	public static NPC CLOUD4;
	public static NPC CLOUD5;
	public static NPC CLOUD6;
	public static NPC CLOUD7;
	public static NPC CLOUD8;
	
	private NPC[] CloudTiles = {CLOUD1, CLOUD2, CLOUD3, CLOUD4, CLOUD5, CLOUD6, CLOUD7, CLOUD8};
	
	public void spawnCloudTiles(Character victim) { 
		for(int i = 0; i < 8; i++) {
			CloudTiles[i] = new NPC(1666, new Position(Zulrah.TOXIC_CLOUD_LOCATIONS[i][0], Zulrah.TOXIC_CLOUD_LOCATIONS[i][1], victim.getIndex() * 4));
			World.register(CloudTiles[i]);
			System.out.println("XAAYAY");
		}
		invisable = true;
	}

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
		if(!invisable) 
			spawnCloudTiles(victim);
		if(isTransforming || isSpawningClouds)
			return true; 
		
		String phaseName = Zulrah.zulrahPhases.get(zulrahPhase).getPhaseName();
		System.out.println(phaseName);
		switch(phaseName) {
			case "SpawnAllToxicClouds":
				isSpawningClouds = true;
				
				TaskManager.submit(new Task(1, true) {
					
					int tick;
					int cloud = 0;

					@Override
					public void execute() {
						if(tick == 4);
							entity.getCombatBuilder().attack(CLOUD1);
							//new Projectile(entity, CLOUD1, 1044, 44, 1, 43, 0, 0).sendProjectile();
							CustomObjects.globalToxicCloudTask(
									new GameObject(11700, new Position(Zulrah.TOXIC_CLOUD_LOCATIONS[0][0], Zulrah.TOXIC_CLOUD_LOCATIONS[0][1], entity.getPosition().getZ())), (Player) victim, 20);
							cloud++;
						if(tick == 4)
							CustomObjects.globalToxicCloudTask(
									new GameObject(11700, new Position(Zulrah.TOXIC_CLOUD_LOCATIONS[1][0], Zulrah.TOXIC_CLOUD_LOCATIONS[1][1], entity.getPosition().getZ())), (Player) victim, 20);
							cloud++;
						if(tick == 8)
							CustomObjects.globalToxicCloudTask(
									new GameObject(11700, new Position(Zulrah.TOXIC_CLOUD_LOCATIONS[2][0], Zulrah.TOXIC_CLOUD_LOCATIONS[2][1], entity.getPosition().getZ())), (Player) victim, 20);
							cloud++;
						if(tick == 12)
							CustomObjects.globalToxicCloudTask(
									new GameObject(11700, new Position(Zulrah.TOXIC_CLOUD_LOCATIONS[3][0], Zulrah.TOXIC_CLOUD_LOCATIONS[3][1], entity.getPosition().getZ())), (Player) victim, 20);
							cloud++;
						if(tick == 16)
							CustomObjects.globalToxicCloudTask(
									new GameObject(11700, new Position(Zulrah.TOXIC_CLOUD_LOCATIONS[4][0], Zulrah.TOXIC_CLOUD_LOCATIONS[4][1], entity.getPosition().getZ())), (Player) victim, 20);
							cloud++;
						if(tick == 24)
							CustomObjects.globalToxicCloudTask(
									new GameObject(11700, new Position(Zulrah.TOXIC_CLOUD_LOCATIONS[5][0], Zulrah.TOXIC_CLOUD_LOCATIONS[5][1], entity.getPosition().getZ())), (Player) victim, 20);
							cloud++;
						if(tick == 28)
							CustomObjects.globalToxicCloudTask(
									new GameObject(11700, new Position(Zulrah.TOXIC_CLOUD_LOCATIONS[6][0], Zulrah.TOXIC_CLOUD_LOCATIONS[6][1], entity.getPosition().getZ())), (Player) victim, 20);
							cloud++;
						if(tick == 32)
							CustomObjects.globalToxicCloudTask(
									new GameObject(11700, new Position(Zulrah.TOXIC_CLOUD_LOCATIONS[7][0], Zulrah.TOXIC_CLOUD_LOCATIONS[7][1], entity.getPosition().getZ())), (Player) victim, 20);
							cloud++;
						tick++;
					}
				});
				break;
		}
		

		return true;
	}
	
	public static void transformZulrah(ZulrahColour colour, Character entity, Character victim, Position newPosition) {
		isTransforming = true;
		victim.getCombatBuilder().reset();
		TaskManager.submit(new Task(2, entity, false) {
			@Override
			public void execute() {
				if(taskPhase == 0) {
					entity.performAnimation(Zulrah.ZULRAH_DIVE_ANIMATION);
					taskPhase++;
				} else {
					NPC zulrah = (NPC) entity;
					zulrah.setTransformationId(colour.getNpc());
					zulrah.getUpdateFlag().flag(Flag.TRANSFORM);
					zulrah.performAnimation(Zulrah.ZULRAH_RISE_ANIMATION);
					isTransforming = false;
					taskPhase = 0;
					stop();
				}
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
