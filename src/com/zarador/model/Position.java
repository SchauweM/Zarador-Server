package com.zarador.model;

import com.zarador.util.Misc;
import com.zarador.world.World;
import com.zarador.world.content.Area;

/**
 * Represents a single world tile position.
 * 
 * @author relex lawl
 */

public class Position {

	/**
	 * The Position constructor.
	 * 
	 * @param x
	 *            The x-type coordinate of the position.
	 * @param y
	 *            The y-type coordinate of the position.
	 * @param z
	 *            The height of the position.
	 */
	public Position(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * The Position constructor.
	 * 
	 * @param x
	 *            The x-type coordinate of the position.
	 * @param y
	 *            The y-type coordinate of the position.
	 */
	public Position(int x, int y) {
		this(x, y, 0);
	}

	/**
	 * The x coordinate of the position.
	 */
	private int x;

	/**
	 * Gets the x coordinate of this position.
	 * 
	 * @return The associated x coordinate.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets the x coordinate of this position.
	 * 
	 * @return The Position instance.
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * The y coordinate of the position.
	 */
	private int y;

	/**
	 * Gets the y coordinate of this position.
	 * 
	 * @return The associated y coordinate.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the y coordinate of this position.
	 * 
	 * @return The Position instance.
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * The height level of the position.
	 */
	private int z;

	/**
	 * Gets the height level of this position.
	 * 
	 * @return The associated height level.
	 */
	public int getZ() {
		return z;
	}

	/**
	 * Sets the height level of this position.
	 * 
	 * @return The Position instance.
	 */
	public Position setZ(int z) {
		this.z = z;
		return this;
	}

	/**
	 * Sets the player's associated Position values.
	 * 
	 * @param x
	 *            The new x coordinate.
	 * @param y
	 *            The new y coordinate.
	 * @param z
	 *            The new height level.
	 */
	public void set(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void setAs(Position other) {
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
	}

	/**
	 * Gets the local x coordinate relative to a specific region.
	 * 
	 * @param position
	 *            The region the coordinate will be relative to.
	 * @return The local x coordinate.
	 */
	public int getLocalX(Position position) {
		return x - 8 * position.getRegionX();
	}

	/**
	 * Gets the local y coordinate relative to a specific region.
	 * 
	 * @param position
	 *            The region the coordinate will be relative to.
	 * @return The local y coordinate.
	 */
	public int getLocalY(Position position) {
		return y - 8 * position.getRegionY();
	}

	/**
	 * Gets the local x coordinate relative to a specific region.
	 * 
	 * @return The local x coordinate.
	 */
	public int getLocalX() {
		return x - 8 * getRegionX();
	}

	/**
	 * Gets the local y coordinate relative to a specific region.
	 * 
	 * @return The local y coordinate.
	 */
	public int getLocalY() {
		return y - 8 * getRegionY();
	}

	/**
	 * Gets the region x coordinate.
	 * 
	 * @return The region x coordinate.
	 */
	public int getRegionX() {
		return (x >> 3) - 6;
	}

	/**
	 * Gets the region y coordinate.
	 * 
	 * @return The region y coordinate.
	 */
	public int getRegionY() {
		return (y >> 3) - 6;
	}

	/**
	 * Checks if a certain position equals a certain x/y
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean equals(int x, int y) {
		if(this.x == x && this.y == y) {
			return true;
		}
		return false;
	}

	public int getRegionId() {
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = (regionX / 8 << 8) + regionY / 8;
		return regionId;
	}

	/**
	 * Adds steps/coordinates to this position.
	 */
	public Position add(int x, int y) {
		this.x += x;
		this.y += y;
		return this;
	}

	/**
	 * Adds steps/coordinates to this position.
	 */
	public Position add(int x, int y, int z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}

	/**
	 * Checks if this location is within range of another.
	 * 
	 * @param other
	 *            The other location.
	 * @return <code>true</code> if the location is in range, <code>false</code>
	 *         if not.
	 */
	public boolean isWithinDistance(Position other) {
		if (z != other.z)
			return false;
		int deltaX = other.x - x, deltaY = other.y - y;
		return deltaX <= 14 && deltaX >= -15 && deltaY <= 14 && deltaY >= -15;
	}

	/**
	 * Checks if the position is within distance of another.
	 * 
	 * @param other
	 *            The other position.
	 * @param distance
	 *            The distance.
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean isWithinDistance(Position other, int distance) {
		if (z != other.getZ())
			return false;
		int deltaX = Math.abs(x - other.x);
		int deltaY = Math.abs(y - other.y);
		return deltaX <= distance && deltaY <= distance;
	}

	/**
	 * Checks if this location is within interaction range of another.
	 * 
	 * @param other
	 *            The other location.
	 * @return <code>true</code> if the location is in range, <code>false</code>
	 *         if not.
	 */
	public boolean isWithinInteractionDistance(Position other) {
		if (z != other.z) {
			return false;
		}
		int deltaX = other.x - x, deltaY = other.y - y;
		return deltaX <= 2 && deltaX >= -3 && deltaY <= 2 && deltaY >= -3;
	}

	/**
	 * Gets the distance between this position and another position. Only X and
	 * Y are considered (i.e. 2 dimensions).
	 * 
	 * @param other
	 *            The other position.
	 * @return The distance.
	 */
	public int getDistance(Position other) {
		int deltaX = x - other.x;
		int deltaY = y - other.y;
		return (int) Math.ceil(Math.sqrt(deltaX * deltaX + deltaY * deltaY));
	}

	/**
	 * Checks if {@code position} has the same values as this position.
	 * 
	 * @param position
	 *            The position to check.
	 * @return The values of {@code position} are the same as this position's.
	 */
	public boolean sameAs(Position position) {
		return position.x == x && position.y == y && position.z == z;
	}

	public double distanceToPoint(int pointX, int pointY) {
		return Math.sqrt(Math.pow(x - pointX, 2) + Math.pow(y - pointY, 2));
	}

	public Position copy() {
		return new Position(x, y, z);
	}

	public Position copyXY() {
		return new Position(x, y, 0);
	}

	@Override
	public String toString() {
		return "Position [x=" + x + ", y=" + y + ", z=" + z + "]";
	}

	@Override
	public int hashCode() {
		return z << 30 | x << 15 | y;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Position)) {
			return false;
		}
		Position position = (Position) other;
		return position.x == x && position.y == y && position.z == z;
	}

	public boolean isViewableFrom(Position other) {
		if (this.getZ() != other.getZ())
			return false;
		Position p = Misc.delta(this, other);
		return p.x <= 14 && p.x >= -15 && p.y <= 14 && p.y >= -15;
	}

	/**
	 * Checks if {@code position} has the same values as this position.
	 *
	 * @return The values of {@code position} are the same as this position's.
	 */
	public boolean sameAs(int otherX, int otherY) {
		return x == otherX && y == otherY;
	}

	public boolean withinRegionNoHeight(Position position) {
		int regionX = this.getRegionX();
		int regionY = this.getRegionY();
		regionX *= 8;
		regionY *= 8;
		if (regionX > position.getX() || regionY > position.getY()) {
			return false;
		}
		if (regionX + 104 < position.getX() || regionY + 104 < position.getY()) {
			return false;
		}
		return true;
	}

	public boolean withinRegion(Position position) {
		if (z != position.getZ()) {
			return false;
		}
		int regionX = this.getRegionX();
		int regionY = this.getRegionY();
		regionX *= 8;
		regionY *= 8;
		if (regionX > position.getX() || regionY > position.getY()) {
			return false;
		}
		if (regionX + 104 < position.getX() || regionY + 104 < position.getY()) {
			return false;
		}
		return true;
	}

	public int distanceTo(Position position) {
		int distX = Math.abs(x - position.getX());
		int distY = Math.abs(y - position.getY());
		if (distX == distY) {
			return distX + 1;
		}
		return distX > distY ? distX : distY;
	}

	public Position transform(Direction direction) {
		return new Position(x + direction.getX(), y + direction.getY(), z);
	}

	public Position transform(int diffX, int diffY) {
		return new Position(x + diffX, y + diffY, z);
	}

	public Position transform(int diffZ, int diffX, int diffY) {
		return new Position(x + diffX, y + diffY, z + diffZ);
	}

	public boolean matches(int x, int y) {
		return this.x == x && this.y == y;
	}

	public Direction moveAwayFrom(Area area, int attempts) {
		int z = this.z % 4;
		int middleX = area.getX() + (area.getSize() / 2);
		int middleY = area.getY() + (area.getSize() / 2);
		int xDifference = Math.abs(middleX - x);
		int yDifference = Math.abs(middleY - y);
		if (xDifference > yDifference) {
			if (x < middleX) {
				if (attempts <= 0 && !World.directionBlocked(Direction.EAST, z, x - 1, y, 1)) {
					return Direction.WEST;
				}
				if (y > middleY) {
					if (attempts <= 1 && !World.directionBlocked(Direction.SOUTH, z, x, y + 1, 1)) {
						return Direction.NORTH;
					}
					if (attempts <= 2 && !World.directionBlocked(Direction.NORTH, z, x, y - 1, 1)) {
						return Direction.SOUTH;
					}
				} else if (y < middleY) {
					if (attempts <= 1 && !World.directionBlocked(Direction.NORTH, z, x, y - 1, 1)) {
						return Direction.SOUTH;
					}
					if (attempts <= 2 && !World.directionBlocked(Direction.SOUTH, z, x, y + 1, 1)) {
						return Direction.NORTH;
					}
				}
				if (!World.directionBlocked(Direction.WEST, z, x + 1, y, 1)) {
					return Direction.EAST;
				}
				if (attempts > 0 && !World.directionBlocked(Direction.EAST, z, x - 1, y, 1)) {
					return Direction.WEST;
				}
				if (y > middleY) {
					if (attempts > 1 && !World.directionBlocked(Direction.SOUTH, z, x, y + 1, 1)) {
						return Direction.NORTH;
					}
					if (attempts > 2 && !World.directionBlocked(Direction.NORTH, z, x, y - 1, 1)) {
						return Direction.SOUTH;
					}
				} else if (y < middleY) {
					if (attempts > 1 && !World.directionBlocked(Direction.NORTH, z, x, y - 1, 1)) {
						return Direction.SOUTH;
					}
					if (attempts > 2 && !World.directionBlocked(Direction.SOUTH, z, x, y + 1, 1)) {
						return Direction.NORTH;
					}
				}
				return Direction.NONE;
			} else {
				if (attempts <= 0 && !World.directionBlocked(Direction.WEST, z, x + 1, y, 1)) {
					return Direction.EAST;
				}
				if (y > middleY) {
					if (attempts <= 1 && !World.directionBlocked(Direction.SOUTH, z, x, y + 1, 1)) {
						return Direction.NORTH;
					}
					if (attempts <= 2 && !World.directionBlocked(Direction.NORTH, z, x, y - 1, 1)) {
						return Direction.SOUTH;
					}
				} else if (y < middleY) {
					if (attempts <= 1 && !World.directionBlocked(Direction.NORTH, z, x, y - 1, 1)) {
						return Direction.SOUTH;
					}
					if (attempts <= 2 && !World.directionBlocked(Direction.SOUTH, z, x, y + 1, 1)) {
						return Direction.NORTH;
					}
				}
				if (!World.directionBlocked(Direction.EAST, z, x - 1, y, 1)) {
					return Direction.WEST;
				}
				if (attempts > 0 && !World.directionBlocked(Direction.WEST, z, x + 1, y, 1)) {
					return Direction.EAST;
				}
				if (y > middleY) {
					if (attempts > 1 && !World.directionBlocked(Direction.SOUTH, z, x, y + 1, 1)) {
						return Direction.NORTH;
					}
					if (attempts > 2 && !World.directionBlocked(Direction.NORTH, z, x, y - 1, 1)) {
						return Direction.SOUTH;
					}
				} else if (y < middleY) {
					if (attempts > 1 && !World.directionBlocked(Direction.NORTH, z, x, y - 1, 1)) {
						return Direction.SOUTH;
					}
					if (attempts > 2 && !World.directionBlocked(Direction.SOUTH, z, x, y + 1, 1)) {
						return Direction.NORTH;
					}
				}
				return Direction.NONE;
			}
		}
		if (yDifference > xDifference) {
			if (y > middleY) {
				if (attempts <= 0 && !World.directionBlocked(Direction.SOUTH, z, x, y + 1, 1)) {
					return Direction.NORTH;
				}
				if (x < middleX) {
					if (attempts <= 1 && !World.directionBlocked(Direction.EAST, z, x - 1, y, 1)) {
						return Direction.WEST;
					}
					if (attempts <= 2 && !World.directionBlocked(Direction.WEST, z, x + 1, y, 1)) {
						return Direction.EAST;
					}
				} else if (x > middleX) {
					if (attempts <= 1 && !World.directionBlocked(Direction.WEST, z, x + 1, y, 1)) {
						return Direction.EAST;
					}
					if (attempts <= 2 && !World.directionBlocked(Direction.EAST, z, x - 1, y, 1)) {
						return Direction.WEST;
					}
				}
				if (attempts <= 0 && !World.directionBlocked(Direction.NORTH, z, x, y - 1, 1)) {
					return Direction.SOUTH;
				}
				if (attempts > 0 && !World.directionBlocked(Direction.SOUTH, z, x, y + 1, 1)) {
					return Direction.NORTH;
				}
				if (x < middleX) {
					if (attempts > 1 && !World.directionBlocked(Direction.EAST, z, x - 1, y, 1)) {
						return Direction.WEST;
					}
					if (attempts > 2 && !World.directionBlocked(Direction.WEST, z, x + 1, y, 1)) {
						return Direction.EAST;
					}
				} else if (x > middleX) {
					if (attempts > 1 && !World.directionBlocked(Direction.WEST, z, x + 1, y, 1)) {
						return Direction.EAST;
					}
					if (attempts > 2 && !World.directionBlocked(Direction.EAST, z, x - 1, y, 1)) {
						return Direction.WEST;
					}
				}
				return Direction.NONE;
			} else {
				if (attempts <= 0 && !World.directionBlocked(Direction.NORTH, z, x, y - 1, 1)) {
					return Direction.SOUTH;
				}
				if (x < middleX) {
					if (attempts <= 1 && !World.directionBlocked(Direction.EAST, z, x - 1, y, 1)) {
						return Direction.WEST;
					}
					if (attempts <= 2 && !World.directionBlocked(Direction.WEST, z, x + 1, y, 1)) {
						return Direction.EAST;
					}
				} else if (x > middleX) {
					if (attempts <= 1 && !World.directionBlocked(Direction.WEST, z, x + 1, y, 1)) {
						return Direction.EAST;
					}
					if (attempts <= 2 && !World.directionBlocked(Direction.EAST, z, x - 1, y, 1)) {
						return Direction.WEST;
					}
				}
				if (!World.directionBlocked(Direction.SOUTH, z, x, y + 1, 1)) {
					return Direction.NORTH;
				}
				if (attempts > 0 && !World.directionBlocked(Direction.NORTH, z, x, y - 1, 1)) {
					return Direction.SOUTH;
				}
				if (x < middleX) {
					if (attempts > 1 && !World.directionBlocked(Direction.EAST, z, x - 1, y, 1)) {
						return Direction.WEST;
					}
					if (attempts > 2 && !World.directionBlocked(Direction.WEST, z, x + 1, y, 1)) {
						return Direction.EAST;
					}
				} else if (x > middleX) {
					if (attempts > 1 && !World.directionBlocked(Direction.WEST, z, x + 1, y, 1)) {
						return Direction.EAST;
					}
					if (attempts > 2 && !World.directionBlocked(Direction.EAST, z, x - 1, y, 1)) {
						return Direction.WEST;
					}
				}
				return Direction.NONE;
			}
		}
		if (x > middleX) {
			if (attempts <= 0 && !World.directionBlocked(Direction.WEST, z, x + 1, y, 1)) {
				return Direction.EAST;
			}
			if (attempts <= 1 && !World.directionBlocked(Direction.SOUTH, z, x, y + 1, 1)) {
				return Direction.NORTH;
			}
			if (attempts <= 2 && !World.directionBlocked(Direction.NORTH, z, x, y - 1, 1)) {
				return Direction.SOUTH;
			}
			if (!World.directionBlocked(Direction.EAST, z, this.x - 1, this.y, 1)) {
				return Direction.WEST;
			}
			if (attempts > 0 && !World.directionBlocked(Direction.WEST, z, this.x + 1, this.y, 1)) {
				return Direction.EAST;
			}
			if (attempts > 1 && !World.directionBlocked(Direction.SOUTH, z, x, y + 1, 1)) {
				return Direction.NORTH;
			}
			if (attempts > 2 && !World.directionBlocked(Direction.NORTH, z, x, y - 1, 1)) {
				return Direction.SOUTH;
			}
			return Direction.NONE;
		} else {
			if (attempts <= 0 && !World.directionBlocked(Direction.EAST, z, this.x - 1, this.y, 1)) {
				return Direction.WEST;
			}
			if (attempts <= 1 && !World.directionBlocked(Direction.SOUTH, z, x, y + 1, 1)) {
				return Direction.NORTH;
			}
			if (attempts <= 2 && !World.directionBlocked(Direction.NORTH, z, x, y - 1, 1)) {
				return Direction.SOUTH;
			}
			if (!World.directionBlocked(Direction.WEST, z, this.x + 1, this.y, 1)) {
				return Direction.EAST;
			}
			if (attempts > 0 && !World.directionBlocked(Direction.EAST, z, this.x - 1, this.y, 1)) {
				return Direction.WEST;
			}
			if (attempts > 1 && !World.directionBlocked(Direction.SOUTH, z, x, y + 1, 1)) {
				return Direction.NORTH;
			}
			if (attempts > 2 && !World.directionBlocked(Direction.NORTH, z, x, y - 1, 1)) {
				return Direction.SOUTH;
			}
			return Direction.NONE;
		}
	}

	public boolean outsideDistance(Position other) {
		if (z != other.getZ()) {
			return true;
		}
		int deltaX = other.getX() - x, deltaY = other.getY() - y;
		return deltaX > 14 || deltaX < -15 || deltaY > 14 || deltaY < -15;
	}
}
