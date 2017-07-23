package com.zarador.model.movement;

import java.util.LinkedList;

import com.zarador.engine.task.Task;
import com.zarador.engine.task.TaskManager;
import com.zarador.model.*;
import com.zarador.model.container.impl.Equipment;
import com.zarador.world.World;
import com.zarador.world.clip.region.Region;
import com.zarador.world.content.Area;
import com.zarador.world.entity.impl.Character;
import com.zarador.world.entity.impl.npc.NPC;
import com.zarador.world.entity.impl.player.Player;

public final class WalkingQueue {
	public static final int MAXIMUM_SIZE = 50;
	private static final int RUNNING_TOGGLED = 0x1;
	private static final int RUN_QUEUE = 0x2;
	private final Character mobile;
	private final LinkedList<Point> waypoints = new LinkedList<Point>();
	private int runningFlags;

	public WalkingQueue(Character mobile) {
		this.mobile = mobile;
	}

	public void setRunningToggled(boolean set) {
		this.setFlag(WalkingQueue.RUNNING_TOGGLED, set);
		//((Player) mobile).getPacketSender().sendRunStatus();
	}

	public void setRunningQueue(boolean set) {
		this.setFlag(WalkingQueue.RUN_QUEUE, set);
	}

	public boolean isRunning() {
		return (runningFlags & (WalkingQueue.RUNNING_TOGGLED | WalkingQueue.RUN_QUEUE)) != 0;
	}

	private static int distanceTo(Position position, Point point) {
		int distX = Math.abs(position.getX() - point.getX());
		int distY = Math.abs(position.getY() - point.getY());
		if (distX == distY) {
			return distX + 1;
		}
		return distX > distY ? distX : distY;
	}

	public Position getNextPosition() {
		int size = waypoints.size();
		if (size == 0) {
			return mobile.getPosition();
		}
		Point point = waypoints.peek();
		return new Position(point.getX(), point.getY(), mobile.getPosition().getZ());
	}

	public boolean deleteSteps(Position destination) {
		int size = waypoints.size();
		if (size == 0) {
			return false;
		}
		int distance = WalkingQueue.distanceTo(destination, waypoints.get(size - 1));
		if (size == 1 && distance <= 2) {
			waypoints.removeLast();
			return false;
		}
		for (int i = size - 2; i >= 0; i--) {
			int nextDistance = WalkingQueue.distanceTo(destination, waypoints.get(i));
			if (distance <= nextDistance) {
				return true;
			}
			waypoints.removeLast();
			distance = nextDistance;
		}
		return true;
	}

	public boolean deleteSteps(Character destination) {
		int size = waypoints.size();
		if (size == 0) {
			return false;
		}
		Point point = waypoints.get(size - 1);
		int distance = destination.distance(point.getX(), point.getY());
		if (size == 1 && distance <= 2) {
			waypoints.removeLast();
			return false;
		}
		for (int i = size - 2; i >= 0; i--) {
			point = waypoints.get(i);
			int nextDistance = destination.distance(point.getX(), point.getY());
			if (distance <= nextDistance) {
				return true;
			}
			waypoints.removeLast();
			distance = nextDistance;
		}
		return true;
	}

	public boolean deleteSteps(Area destination) {
		int size = waypoints.size();
		if (size == 0) {
			return false;
		}
		Point point = waypoints.get(size - 1);
		int distance = destination.distance(point.getX(), point.getY());
		if (size == 1 && distance <= 2) {
			waypoints.removeLast();
			return false;
		}
		int deleted = 0;
		for (int i = size - 2; i >= 0; i--) {
			point = waypoints.get(i);
			int nextDistance = destination.distance(point.getX(), point.getY());
			if (distance <= nextDistance) {
				return true;
			}
			waypoints.removeLast();
			deleted++;
			distance = nextDistance;
		}
		return true;
	}

    public int size() {
        return waypoints.size();
    }

	public void clear() {
		this.setRunningQueue(false);
		waypoints.clear();
	}

	private void setFlag(int flag, boolean set) {
		if ((runningFlags & flag) != 0) {
			if (!set) {
				runningFlags ^= flag;
			}
		} else {
			if (set) {
				runningFlags |= flag;
			}
		}
	}

	public void addStep(int x, int y) {
		Point last = waypoints.peekLast();
		int diffX;
		int diffY;
		if (last != null) {
			diffX = x - last.getX();
			diffY = y - last.getY();
		} else {
			Position position = mobile.getPosition();
			diffX = x - position.getX();
			diffY = y - position.getY();
		}
		int max = Math.max(Math.abs(diffX), Math.abs(diffY));
		for (; max >= 0; max--) {
			if (diffX < 0) {
				diffX++;
			} else if (diffX > 0) {
				diffX--;
			}
			if (diffY < 0) {
				diffY++;
			} else if (diffY > 0) {
				diffY--;
			}
			if (this.addStepInternal(x - diffX, y - diffY)) {
				continue;
			}
			return;
		}
	}

	public void addClippedStep(int x, int y) {
		Point last = waypoints.peekLast();
		int diffX;
		int diffY;
		if (last != null) {
			diffX = x - last.getX();
			diffY = y - last.getY();
		} else {
			Position position = mobile.getPosition();
			diffX = x - position.getX();
			diffY = y - position.getY();
		}
		int max = Math.max(Math.abs(diffX), Math.abs(diffY));
		for (; max >= 0; max--) {
			if (diffX < 0) {
				diffX++;
			} else if (diffX > 0) {
				diffX--;
			}
			if (diffY < 0) {
				diffY++;
			} else if (diffY > 0) {
				diffY--;
			}
			if (this.addClippedStepInternal(x - diffX, y - diffY)) {
				continue;
			}
			return;
		}
	}

	public boolean addStepInternal(int x, int y) {
		if (waypoints.size() >= WalkingQueue.MAXIMUM_SIZE) {
			return false;
		}
		Point last = waypoints.peekLast();
		int diffX;
		int diffY;
		if (last != null) {
			diffX = x - last.getX();
			diffY = y - last.getY();
		} else {
			Position position = mobile.getPosition();
			diffX = x - position.getX();
			diffY = y - position.getY();
		}
		Direction dir = Direction.direction(diffX, diffY);
		if (dir == Direction.NONE) {
			return false;
		}
		waypoints.add(new Point(x, y, dir));
		return true;
	}

	public boolean addClippedStepInternal(int x, int y) {
		if (mobile.getWalkingQueue().isLockMovement() || mobile.isFrozen()) {
			return false;
		}
		if (waypoints.size() >= WalkingQueue.MAXIMUM_SIZE) {
			return false;
		}
		Point last = waypoints.peekLast();
		int diffX;
		int diffY;
		if (last != null) {
			diffX = x - last.getX();
			diffY = y - last.getY();
		} else {
			Position position = mobile.getPosition();
			diffX = x - position.getX();
			diffY = y - position.getY();
		}
		Direction dir = Direction.direction(diffX, diffY);
		if (dir == Direction.NONE) {
			return false;
		}
		waypoints.add(new Point(x, y, dir, true));
		return true;
	}

	public void processNextMovement() {
		boolean teleporting = mobile.isTeleporting();
		Direction walkDirection = Direction.NONE, runDirection = Direction.NONE;
		if (teleporting) {
			this.clear();
			if (mobile.isPlayer()) {
				Player player = ((Player) mobile);
				player.getActionQueue().clearNonFixedActions();
			}
		} else {
            if (mobile.isFrozen() || mobile.getWalkingQueue().isLockMovement()) {
                waypoints.clear();
            }

            if (!waypoints.isEmpty()) {
                walkDirection = this.getNextPoint();
                if (this.isRunning()) {
                    runDirection = this.getNextPoint();
                    if (runDirection != null && mobile.isPlayer()) {
                        Player player = (Player) mobile;
                        int armorBenefit = 0;
                        if (player.getEquipment().get(Equipment.FEET_SLOT).getId() == 88) {
                            armorBenefit += 10;
                        }
                        if (player.getEquipment().get(Equipment.BODY_SLOT).getId() == 14936) {
                            armorBenefit += 15;
                        }
                        if (player.getEquipment().get(Equipment.LEG_SLOT).getId() == 14938) {
                            armorBenefit += 15;
                        }
                        if (player.getRunEnergy() <= 0) {
                            this.setRunningToggled(false);
                            player.getPacketSender().sendMessage("You ran out of run energy!");
							player.getPacketSender().sendRunStatus();
                        } else {
							if(player.getEquipment().get(Equipment.HEAD_SLOT).getId() != 18744 && player.getEquipment().get(Equipment.HEAD_SLOT).getId() != 18745 && player.getEquipment().get(Equipment.HEAD_SLOT).getId() != 18746) {
								if (player.getDonatorRights().ordinal() < DonatorRights.UBER.ordinal()) {
									player.setRunEnergy(player.getRunEnergy() - (1.5F - ((player.getSkillManager().getMaxLevel(Skill.AGILITY) + armorBenefit) / 100F)));
								}
							}
						}
                    }
                }
            }
        }

		mobile.setWalkingDirection(walkDirection);
		mobile.setRunningDirection(runDirection);

		if (walkDirection != Direction.NONE) {
			mobile.moving = true;
		} else {
			mobile.moving = false;
		}

		int diffX = mobile.getPosition().getX() - (mobile.getLastKnownRegion().getRegionX() * 8);
		int diffY = mobile.getPosition().getY() - (mobile.getLastKnownRegion().getRegionY() * 8);
		if (diffX < 16 || diffX >= 88 || diffY < 16 || diffY >= 88) {
			if (mobile instanceof Player) {
				((Player) mobile).getPacketSender().sendMapRegion();
			}
		}
		Locations.process(mobile);
	}

	public Direction getNextPoint() {
		Point p = waypoints.peek();
		if (p == null) {
			return Direction.NONE;
		}
		if (p.getDirection() == Direction.NONE) {
			throw new RuntimeException();
		}
		Position position = mobile.getPosition();
		int z = position.getZ() % 4;
		int x = position.getX();
		int y = position.getY();
		Direction direction = p.getDirection();
		if (p.isClipped() || (mobile.isPlayer() && ((Player) mobile).ignoreClip)) {
			waypoints.poll();
			mobile.setPosition(position.transform(direction));
			return direction;
		}
		if (z < 0) {
			waypoints.clear();
			try {
				if (mobile.isPlayer()) {
					System.err.println(((Player) mobile).getUsername() + " is at: " + z + ", " + x + ", " + y);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return Direction.NONE;
		}
		if (mobile instanceof NPC && ((NPC)mobile).isProjectileClipping()) {
			if (World.projectileDirectionBlocked(direction, z, x, y, mobile.getSize())) {
				return Direction.NONE;
			}
		} else {
			if (World.directionBlocked(direction, z, x, y, mobile.getSize())) {
				return Direction.NONE;
			}
		}
		waypoints.poll();
		mobile.setPosition(position.transform(direction));
		return direction;
	}

	/**
	 * If this entity's movement is locked.
	 */
	private boolean lockMovement;

	/**
	 * Gets whether or not this entity is 'frozen'.
	 *
	 * @return true if this entity cannot move.
	 */
	public boolean isLockMovement() {
		return lockMovement;
	}

	/**
	 * Sets if this entity can move or not.
	 *
	 * @param lockMovement
	 *            true if this entity cannot move.
	 */
	public WalkingQueue setLockMovement(boolean lockMovement) {
		this.lockMovement = lockMovement;
		return this;
	}

	public boolean canWalk(int deltaX, int deltaY) {
		final Position to = new Position(mobile.getPosition().getX() + deltaX,
				mobile.getPosition().getY() + deltaY, mobile.getPosition().getZ());
		if (mobile.getPosition().getZ() == -1 && to.getZ() == -1 && mobile.isNpc()
				&& !((NPC) mobile).isSummoningNpc() || mobile.getLocation() == Locations.Location.RECIPE_FOR_DISASTER)
			return true;
		return canWalk(mobile.getPosition(), to, mobile.getSize());
	}

	public static boolean canWalk(Position from, Position to, int size) {
		return Region.canMove(from.getX(), from.getY(), to.getX(), to.getY(), from.getZ(), size);
	}

	public void walkStep(int x, int y) {
		Position position = mobile.getPosition().copy();
		position.setX(position.getX() + x);
		position.setY(position.getY() + y);
		addClippedStep(position.getX(), position.getY());
	}

	public static void stepAway(Character character) {
		if (character.getWalkingQueue().canWalk(-1, 0))
			character.getWalkingQueue().walkStep(-1, 0);
		else if (character.getWalkingQueue().canWalk(1, 0))
			character.getWalkingQueue().walkStep(1, 0);
		else if (character.getWalkingQueue().canWalk(0, -1))
			character.getWalkingQueue().walkStep(0, -1);
		else if (character.getWalkingQueue().canWalk(0, 1))
			character.getWalkingQueue().walkStep(0, 1);
	}

	public void freeze(int delay) {
		if (mobile.isFrozen() || (System.currentTimeMillis() - mobile.getLastFreeze()) < 1000) {
			mobile.setFreeze(false);
			return;
		}
		mobile.setFreeze(true);
		mobile.setFreezeDelay(delay);
		if (mobile.isPlayer()) {
			((Player) mobile).getPacketSender().sendMessage("You have been frozen for " + delay + " seconds!");
		}
		clear();
		TaskManager.submit(new Task(2, mobile, true) {
			@Override
			protected void execute() {
				if (!mobile.isRegistered() || mobile.getConstitution() <= 0) {
					stop();
					return;
				}
				if (mobile.decrementAndGetFreezeDelay() == 0) {
					mobile.setLastFreeze();
					stop();
				}
			}
		});
	}

	/**
	 * The force movement array index values.
	 */
	public static final int FIRST_MOVEMENT_X = 0, FIRST_MOVEMENT_Y = 1, SECOND_MOVEMENT_X = 2, SECOND_MOVEMENT_Y = 3,
			MOVEMENT_SPEED = 4, MOVEMENT_REVERSE_SPEED = 5, MOVEMENT_DIRECTION = 6;

	public static final class Point {
		private final int x;
		private final int y;
		private final Direction direction;
		private final boolean clipped;

		Point(int x, int y, Direction direction) {
			this(x, y, direction, false);
		}

		Point(int x, int y, Direction direction, boolean clipped) {
			this.x = x;
			this.y = y;
			this.direction = direction;
			this.clipped = clipped;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public Direction getDirection() {
			return direction;
		}

		public boolean isClipped() {
			return clipped;
		}
	}
}
