package com.zarador.model.action.distance;

import com.zarador.executable.Executable;
import com.zarador.model.Direction;
import com.zarador.model.Position;
import com.zarador.model.action.PlayerAction;
import com.zarador.model.movement.WalkingQueue;
import com.zarador.util.RandomGenerator;
import com.zarador.world.World;
import com.zarador.world.clip.region.Region;
import com.zarador.world.content.Area;
import com.zarador.world.entity.impl.npc.NPC;
import com.zarador.world.entity.impl.player.Player;

public final class DistanceToNpcAction extends PlayerAction {
	private final NPC following;
	private final Executable executable;
	private int containsCount;

	public DistanceToNpcAction(Player player, NPC npc, Executable executable) {
		super(player);
		this.executable = executable;
		this.following = npc;
	}

    @Override
	public ActionPolicy getActionPolicy() {
		return ActionPolicy.CLEAR;
	}

	@Override
	public void initialize() {
		Area area = Area.create(following.getPosition(), following.getSize() - 1);
		if (area.contains(player.getPosition().getX(), player.getPosition().getY())) {
			player.getWalkingQueue().clear();
		} else {
			player.getWalkingQueue().deleteSteps(area);
		}
	}

	@Override
	public int execute() {
        if (player.getPosition().distanceTo(following.getPosition()) <= 1) {
            return executable.execute();
        }
		WalkingQueue walkingQueue = player.getWalkingQueue();
		Position followPosition = following.getPosition();
		if (walkingQueue.deleteSteps(Area.create(following.getPosition(), following.getSize() - 1))) {
			return 1;
		}
		Position position = player.getPosition();
		if (followPosition.outsideDistance(position)) {
			return Executable.STOP;
		}
		Area destination = Area.create(followPosition, following.getSize() - 1);
		if (!destination.contains(player.getPosition().getX(), player.getPosition().getY())) {
			//int xDiff = position.getX() - followPosition.getX();
			//int yDiff = position.getY() - followPosition.getY();
			//Server.getLogger().log(Level.INFO, xDiff + " " + yDiff);
			if (Region.reached(position, followPosition.getX(), followPosition.getY(), followPosition.getX() + (following.getSize() - 1), followPosition.getY() + (following.getSize() - 1), 0)) {
				return executable.execute();
			}
		}
		Direction walk = this.getNextFollowPoint(position, destination);
		if (walk == Direction.NONE) {
			return 1;
		}
		position = position.transform(walk.getX(), walk.getY());
		walkingQueue.addStepInternal(position.getX(), position.getY());
		if (!destination.contains(player.getPosition().getX(), player.getPosition().getY())) {
			//int xDiff = position.getX() - followPosition.getX();
			//int yDiff = position.getY() - followPosition.getY();
			//Server.getLogger().log(Level.INFO, xDiff + " " + yDiff);
			if (Region.reached(position, followPosition.getX(), followPosition.getY(), followPosition.getX() + (following.getSize() - 1), followPosition.getY() + (following.getSize() - 1), 0)) {
				return executable.execute();
			}
		}
		if (walkingQueue.isRunning()) {
			Direction run = this.getNextFollowPoint(position, destination);
			if (run == Direction.NONE) {
				return 1;
			}
			walkingQueue.addStepInternal(position.getX() + run.getX(), position.getY() + run.getY());
		}
		return 1;
	}

	@Override
	public void stop() {
		if(following != null) {
			player.setNpcClickId(following.getId());
		}
		super.stop();
	}

	private Direction getNextFollowPoint(Position position, Area destination) {
		int x = position.getX();
		int y = position.getY();
		if (destination.contains(x, y)) {
			Direction direction = position.moveAwayFrom(destination, containsCount % 4);
			if (direction == Direction.NONE || destination.getX() + direction.getX() == player.getPosition().getX() && destination.getY() + direction.getY() == player.getPosition().getY()) {
				containsCount += RandomGenerator.nextInt(3) + 1;
			} else {
				containsCount = 0;
			}
			return direction;
		}
		int distanceTo = destination.distance(x, y);
		if (distanceTo == 1) {
			return Direction.NONE;
		}
		int diffX = destination.xDifference(x);
		int diffY = destination.yDifference(y);
		Direction dirX = Direction.direction(diffX, 0);
		Direction dirY = Direction.direction(0, diffY);
		if (dirX == Direction.NONE && dirY == Direction.NONE) {
			return Direction.NONE;
		}
		int z = position.getZ() % 4;
		if (World.directionBlocked(dirX, z, x, y, destination.getSize())) {
			dirX = Direction.NONE;
		}
		if (World.directionBlocked(dirY, z, x, y, destination.getSize())) {
			dirY = Direction.NONE;
		}
		if (dirX == Direction.NONE) {
			return dirY;
		}
		if (dirY == Direction.NONE) {
			return dirX;
		}
		Direction direction = Direction.direction(dirX.getX(), dirY.getY());
		if (World.directionBlocked(direction, z, x, y, destination.getSize())) {
			if (Math.abs(diffX) >= Math.abs(diffY)) {
				return dirX;
			} else {
				return dirY;
			}
		}
		return direction;
	}
}
