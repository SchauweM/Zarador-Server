package com.zarador.model.action.distance;

import com.zarador.model.Direction;
import com.zarador.model.Position;
import com.zarador.model.action.PlayerAction;
import com.zarador.model.movement.WalkingQueue;
import com.zarador.world.World;
import com.zarador.world.entity.impl.Character;
import com.zarador.world.entity.impl.player.Player;

public final class FollowMobileAction extends PlayerAction {
	private Character following;

	public FollowMobileAction(Player player, Character following) {
		super(player);
		this.following = following;
	}

	@Override
	public ActionPolicy getActionPolicy() {
		return ActionPolicy.CLEAR;
	}

	@Override
	public void initialize() {
		player.setPositionToFace(following.getPositionToFace());
		player.getWalkingQueue().deleteSteps(following.getPosition());
	}

	@Override
	public int execute() {
		WalkingQueue walkingQueue = player.getWalkingQueue();
		Position lastPosition = following.getLastPosition();
		if (walkingQueue.deleteSteps(lastPosition)) {
			return 1;
		}
		Position position = player.getPosition();
		Direction walk = this.getNextFollowPoint(position, lastPosition);
		if (walk == Direction.NONE) {
			return 1;
		}
		position = position.transform(walk.getX(), walk.getY());
		walkingQueue.addStepInternal(position.getX(), position.getY());
		if (walkingQueue.isRunning()) {
			Direction run = this.getNextFollowPoint(position, lastPosition);
			if (run == Direction.NONE) {
				return 1;
			}
			walkingQueue.addStepInternal(position.getX() + run.getX(), position.getY() + run.getY());
		}
		return 1;
	}

	@Override
	public void stop() {
		super.stop();
	}

	@Override
	public void dispose() {
		following = null;
	}

	private Direction getNextFollowPoint(Position position, Position destination) {
		if (destination.getZ() != position.getZ()) {
			return Direction.NONE;
		}
		int lastX = destination.getX();
		int lastY = destination.getY();
		if (position.matches(lastX, lastY)) {
			return Direction.NONE;
		}
		int x, y;
		int diffX = lastX - (x = position.getX());
		int diffY = lastY - (y = position.getY());
		Direction dirX = Direction.direction(diffX, 0);
		Direction dirY = Direction.direction(0, diffY);
		if (dirX == Direction.NONE && dirY == Direction.NONE) {
			return Direction.NONE;
		}
		int z = position.getZ() % 4;
		if (World.directionBlocked(dirX, z, x, y, following.getSize())) {
			dirX = Direction.NONE;
		}
		if (World.directionBlocked(dirY, z, x, y, following.getSize())) {
			dirY = Direction.NONE;
		}
		if (dirX == Direction.NONE) {
			return dirY;
		}
		if (dirY == Direction.NONE) {
			return dirX;
		}
		Direction direction = Direction.direction(dirX.getX(), dirY.getY());
		if (World.directionBlocked(direction, z, x, y, following.getSize())) {
			if (Math.abs(diffX) >= Math.abs(diffY)) {
				return dirX;
			} else {
				return dirY;
			}
		}
		return direction;
	}
}
