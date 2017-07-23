package com.zarador.world.doors;

import com.zarador.model.HashedPosition;

/**
 *
 * @author Relex
 */
public final class DoorState {
	
	public DoorState(int id, int direction, HashedPosition position) {
		this.id = id;
		this.direction = direction;
		this.position = position;
	}
	
	private final int id;
	
	private final int direction;
	
	private final HashedPosition position;
	
	public int getId() {
		return id;
	}
	
	public int getDirection() {
		return direction;
	}
	
	public HashedPosition getPosition() {
		return position;
	}
}