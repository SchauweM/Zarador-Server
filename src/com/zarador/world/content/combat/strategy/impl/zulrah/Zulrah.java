package com.zarador.world.content.combat.strategy.impl.zulrah;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zarador.model.Animation;
import com.zarador.model.Position;
import com.zarador.world.entity.impl.player.Player;

public class Zulrah {

	public final static Position NORTH_POSITION = new Position(2268, 3074);
	public final static Position WEST_POSITION = new Position(2258, 3075);
	public final static Position EAST_POSITION = new Position(2278, 3075);
	public final static Position SOUTH_POSITION = new Position(2268, 3064);
	
	public final static int[][] TOXIC_FUME_LOCATIONS_1 = { { 2263, 3076 }, { 2263, 3073 }, { 2263, 3070 }, { 2266, 3069 },
													{ 2269, 3069 }, { 2272, 3070 }, { 2272, 3073 }, { 2273, 3076 } };
	
	public final static int[][] TOXIC_FUME_LOCATIONS_2 = { { 2263, 3070 }, { 2266, 3069 }, { 2269, 3069 }, { 2272, 3070 } };
	
	public final static int CRIMSON_ZULRAH_ID = 2044;
	public final static int GREEN_ZULRAH_ID = 2043;
	public final static int BLUE_ZULRAH_ID = 2042;
	
	public final static Animation RISE = new Animation(5073);
	public final static Animation DIVE = new Animation(5072);
	
	public static List<ZulrahRotation> zulrahRotations = new ArrayList<>();
	public static Map<Integer, ZulrahPhase> zulrahPhases = new HashMap<>();
	
	public static void initialize() {
		setRotations();
		setPhases();
	}

	private static void setRotations() {
		zulrahRotations.add(new ZulrahRotation(1, "first rotation"));
	}
	
	private static void setPhases() {

	}
	
	public static void startBossFight(Player player) {
		ZulrahRotation rotation = zulrahRotations.get(0);
		
		int initialPhase = 0;
		switch(rotation.getRotationID()) {
			case 1:
				initialPhase = 1;
				break;	
			case 2:
				initialPhase = 2;
				break;	
			case 3:
				initialPhase = 3;
				break;
		}
		
		try {
			getNextPhase(player, 5000, initialPhase);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return;
	}
	
	public static void getNextPhase(Player player, int zulrahConstitution, int phaseID) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		ZulrahPhase phase = zulrahPhases.get(phaseID);
		Method method = phase.phaseClass().getMethod("spawn", Player.class, int.class, int.class);
		try {
			method.invoke(phase.phaseClass().newInstance(), player, zulrahConstitution, phaseID);
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
	}

}
