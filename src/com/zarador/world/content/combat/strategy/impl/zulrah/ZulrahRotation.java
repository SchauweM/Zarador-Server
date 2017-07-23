package com.zarador.world.content.combat.strategy.impl.zulrah;

public class ZulrahRotation {
	
	private int rotationID;
	
	private String rotationName;
	
	public ZulrahRotation(int rotationID, String rotationName) {
		this.rotationID = rotationID;
		this.rotationName = rotationName;
	}
	
	public int getRotationID() {
		return rotationID;
	}
	
	public String getRotationName() {
		return rotationName;
	}

}
