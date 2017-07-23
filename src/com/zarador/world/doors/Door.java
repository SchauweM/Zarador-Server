package com.zarador.world.doors;

/**
 *
 * @author Relex
 */
public final class Door {

	public Door(DoorState openState, DoorState closedState) {
		this.openState = openState;
		this.closedState = closedState;
	}
	
	private final DoorState openState;
	
	private final DoorState closedState;
	
	public DoorState getOpenState() {
		return openState;
	}
	
	public DoorState getClosedState() {
		return closedState;
	}
}