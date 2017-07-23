package com.zarador.world.content.combat.strategy.impl.zulrah;

public class ZulrahPhase {

	private int nextPhase;
	
	private String phaseName;
	
	private Class<?> phaseClass;
	
	private int zulrahX;
	
	private int zulrahY;
	
	public ZulrahPhase(int nextPhase, String phaseName, Class<?> phaseClass, int zulrahX, int zulrahY) {
		this.nextPhase = nextPhase;
		this.phaseName = phaseName;
		this.phaseClass = phaseClass;
		this.zulrahX = zulrahX;
		this.zulrahY = zulrahY;
	}
	
	public int getNextPhase() {
		return nextPhase;
	}
	
	public String getPhaseName() {
		return phaseName;
	}
	
	public Class<?> phaseClass()  {
		return phaseClass;
	}
	
	public int getZulrahX() {
		return zulrahX;
	}
	
	public int getZulrahY() {
		return zulrahY;
	}
	
}
