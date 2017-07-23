package com.zarador.model;

/**
 * Represents a single movement direction.
 *
 * @author Graham
 */
public enum Direction {
	NORTH(1, 0, 1),
	NORTH_EAST(2, 1, 1),
	EAST(4, 1, 0),
	SOUTH_EAST(7, 1, -1),
	SOUTH(6, 0, -1),
	SOUTH_WEST(5, -1, -1),
	WEST(3, -1, 0),
	NORTH_WEST(0, -1, 1),
	NONE(-1, 0, 0);

	/**
	 * An empty direction array.
	 */
	public static final Direction[] EMPTY_DIRECTION_ARRAY = new Direction[0];

	// // north west
	// // north
	// // north east
	// , west
	// , east
	// // south west
	// // south
	// // south east

	/**
	 * Creates a direction from the differences between X and Y.
	 *
	 * @param deltaX
	 *            The difference between two X coordinates.
	 * @param deltaY
	 *            The difference between two Y coordinates.
	 * @return The direction.
	 */
	public static Direction fromDeltas(int deltaX, int deltaY) {
		if (deltaY == 1) {
			if (deltaX == 1)
				return Direction.NORTH_EAST;
			else if (deltaX == 0)
				return Direction.NORTH;
			else
				return Direction.NORTH_WEST;
		} else if (deltaY == -1) {
			if (deltaX == 1)
				return Direction.SOUTH_EAST;
			else if (deltaX == 0)
				return Direction.SOUTH;
			else
				return Direction.SOUTH_WEST;
		} else if (deltaX == 1)
			return Direction.EAST;
		else if (deltaX == -1)
			return Direction.WEST;
		return Direction.NONE;
	}

	public static Direction direction(int startX, int targetX, int startY, int targetY) {
		return direction(targetX - startX, targetY - startY);
	}

	public static Direction direction(int diffX, int diffY) {
		if (diffX < 0) {
			if (diffY < 0) {
				return Direction.SOUTH_WEST;
			}
			if (diffY > 0) {
				return Direction.NORTH_WEST;
			}
			return Direction.WEST;
		}
		if (diffX > 0) {
			if (diffY < 0) {
				return Direction.SOUTH_EAST;
			}
			if (diffY > 0) {
				return Direction.NORTH_EAST;
			}
			return Direction.EAST;
		}
		if (diffY < 0) {
			return Direction.SOUTH;
		}
		if (diffY > 0) {
			return Direction.NORTH;
		}
		return Direction.NONE;
	}

	/**
	 * Checks if the direction represented by the two delta values can connect
	 * two points together in a single direction.
	 *
	 * @param deltaX
	 *            The difference in X coordinates.
	 * @param deltaY
	 *            The difference in X coordinates.
	 * @return {@code true} if so, {@code false} if not.
	 */
	public static boolean isConnectable(int deltaX, int deltaY) {
		return Math.abs(deltaX) == Math.abs(deltaY) || deltaX == 0 || deltaY == 0;
	}

	private final String name;
	private final int direction;
	private final int x;
	private final int y;

	Direction(int direction, int x, int y) {
		this.name = this.name().toLowerCase().replace("_", "-");
		this.direction = direction;
		this.x = x;
		this.y = y;
	}

	public String getName() {
		return name;
	}

	public int getDirection() {
		return direction;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int[] getDirectionDelta() {
		switch (this) {
		case NORTH:
			return new int[] { 0, 1 };
		case NORTH_EAST:
			return new int[] { 1, 1 };
		case EAST:
			return new int[] { 1, 0 };
		case SOUTH_EAST:
			return new int[] { 1, -1 };
		case SOUTH:
			return new int[] { 0, -1 };
		case SOUTH_WEST:
			return new int[] { -1, -1 };
		case WEST:
			return new int[] { -1, 0 };
		case NORTH_WEST:
			return new int[] { -1, 1 };
		default:
			return new int[] { 0, 0 };
		}
	}

	/**
	 * Gets the direction as an integer which the client can understand.
	 *
	 * @return The movement as an integer.
	 */
	public int toInteger() {
		return direction;
	}

}
