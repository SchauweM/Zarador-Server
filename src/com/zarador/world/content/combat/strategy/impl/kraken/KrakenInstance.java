package com.zarador.world.content.combat.strategy.impl.kraken;

import com.zarador.model.Position;
import com.zarador.model.RegionInstance;
import com.zarador.model.RegionInstance.RegionInstanceType;
import com.zarador.world.World;
import com.zarador.world.entity.impl.npc.NPC;
import com.zarador.world.entity.impl.player.Player;

public class KrakenInstance {

	public static NPC whirlpool;
	public static NPC whirlpool1;
	public static NPC whirlpool2;
	public static NPC whirlpool3;
	public static NPC whirlpool4;
	
	public static void createInstance(Player player) {
		player.getPacketSender().sendInterfaceRemoval();
		player.moveTo(new Position(3696, 5807, player.getIndex() * 4));
		player.setRegionInstance(new RegionInstance(player, RegionInstanceType.KRAKEN));
		spawn(player);
	}

	public static void spawn(Player player) {
		
		whirlpool = new NPC(496, new Position(3696, 5811, player.getPosition().getZ())).setSpawnedFor(player);
		World.register(whirlpool);
		player.getRegionInstance().getNpcsList().add(whirlpool);
		
		
		whirlpool1 = new NPC(493, new Position(3700, 5809, player.getPosition().getZ())).setSpawnedFor(player);
		World.register(whirlpool1);
		player.getRegionInstance().getNpcsList().add(whirlpool1);
		
		
		whirlpool2 = new NPC(493, new Position(3700, 5814, player.getPosition().getZ())).setSpawnedFor(player);
		World.register(whirlpool2);
		player.getRegionInstance().getNpcsList().add(whirlpool2);
		

		whirlpool3 = new NPC(493, new Position(3692, 5809, player.getPosition().getZ())).setSpawnedFor(player);
		World.register(whirlpool3);
		player.getRegionInstance().getNpcsList().add(whirlpool3);
		

		whirlpool4 = new NPC(493, new Position(3692, 5814, player.getPosition().getZ())).setSpawnedFor(player);
		World.register(whirlpool4);
		player.getRegionInstance().getNpcsList().add(whirlpool4);
	}
	
}
