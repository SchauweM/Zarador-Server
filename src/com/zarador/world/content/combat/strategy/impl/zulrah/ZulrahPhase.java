package com.zarador.world.content.combat.strategy.impl.zulrah;

import com.zarador.model.Position;

public class ZulrahPhase {

	private int nextPhase;
	
	private String phaseName;
	
	private Class<?> phaseClass;
	
	private Position zulrahPosition;
	
	public ZulrahPhase(int nextPhase, String phaseName, Class<?> phaseClass, Position zulrahPosition) {
		this.nextPhase = nextPhase;
		this.phaseName = phaseName;
		this.phaseClass = phaseClass;
		this.zulrahPosition = zulrahPosition;
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
	
	public Position getZulrahPosition() {
		return zulrahPosition;
	}
}
