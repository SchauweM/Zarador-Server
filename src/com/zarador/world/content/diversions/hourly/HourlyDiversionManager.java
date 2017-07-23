package com.zarador.world.content.diversions.hourly;

import com.zarador.util.Misc;

/**
 *
 * @author relex lawl
 */
public final class HourlyDiversionManager {

	public static void init() {
		chooseNextDiversion();
	}
	
	public static void pulse() {		
		if (NEXT_DIVERSION != null) {
			NEXT_DIVERSION.diversionPulse();
		}
		//System.out.println("div=" + NEXT_DIVERSION);
	}
	
	public static void chooseNextDiversion() {
		NEXT_DIVERSION = INSTANCED_DIVERSIONS[Misc.random(INSTANCED_DIVERSIONS.length - 1)];
	}
	
	public static void setNextDiversion(int index) {
		if (index >= INSTANCED_DIVERSIONS.length)
			index = INSTANCED_DIVERSIONS.length - 1;
		NEXT_DIVERSION = INSTANCED_DIVERSIONS[index];
	}
	
	public static HourlyDiversion getNextDiversion() {
		return NEXT_DIVERSION;
	}
	
	private static HourlyDiversion NEXT_DIVERSION;
			
	private static final HourlyDiversion[] INSTANCED_DIVERSIONS = {

		ShootingStar.getInstance(),
	};
}