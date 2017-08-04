package com.zarador.world.content.combat.strategy.impl.zulrah;

import com.zarador.model.Animation;
import com.zarador.model.Position;
import com.zarador.model.RegionInstance;
import com.zarador.model.RegionInstance.RegionInstanceType;
import com.zarador.world.World;
import com.zarador.world.entity.impl.npc.NPC;
import com.zarador.world.entity.impl.player.Player;

public class Zulrah {

	public final static Position NORTH_POSITION = new Position(2268, 3074);
	public final static Position WEST_POSITION = new Position(2258, 3075);
	public final static Position EAST_POSITION = new Position(2278, 3075);
	public final static Position SOUTH_POSITION = new Position(2268, 3064);
	
	public static final Animation ZULRAH_SPAWN_POISON = new Animation(5069);
    public static final Animation SNAKELING_SPAWN_ANIMATION = new Animation(5069);
    public static final Animation MELEE_TARGET = new Animation(5806);
    public static final Animation MELEE_ATTACK = new Animation(5807);
    public static final Animation ZULRAH_DIVE_ANIMATION = new Animation(5072);
    public static final Animation ZULRAH_RISE_ANIMATION = new Animation(5073);
    public static final Animation PROJECTILE_ATTACK = SNAKELING_SPAWN_ANIMATION;

    public static final Position SPAWN_LOCATION = new Position(2266, 3071, 0);

    public static final Position START_LOCATION = new Position(2268, 3069, 0);

    private static final int[][] VENOM_CLOUD_LOCATIONS = new int[][] {
            {2267, 3068},
            {2264, 3069},
            {2262, 3073},
            {2272, 3069},
            {2272, 3073}
    };
	
	public static void startZulrah(Player player) {
		player.moveTo(new Position(2268, 3070, player.getIndex() * 4));
		
		NPC zulrah = new NPC(2042, new Position(2268, 3074, player.getIndex() * 4));
		zulrah.setSpawnedFor(player);
		
		player.setRegionInstance(new RegionInstance(player, RegionInstanceType.ZULRAH_SHRINE));
		player.getRegionInstance().getNpcsList().add(zulrah);
		
		World.register(zulrah);
		
		zulrah.performAnimation(ZULRAH_RISE_ANIMATION);
	}

}
