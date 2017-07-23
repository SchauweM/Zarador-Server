package com.zarador.world.content;

import com.zarador.model.Direction;
import com.zarador.model.Position;
import com.zarador.world.World;

public final class Area {
	public static Area create(Position position, int length) {
		return new Area(position.getZ(), position.getX(), position.getY(), length);
	}

	public static Area create(int z, int x, int y, int length) {
		return new Area(z, x, y, length);
	}

	private final int z;
	private final int x;
	private final int y;
	private final int size;

	public Area(int z, int x, int y, int size) {
		this.z = z;
		this.x = x;
		this.y = y;
		this.size = size + 1;
	}

	public int getZ() {
		return z;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getSize() {
		return size;
	}

	public int xDifference(int x) {
		if (x < this.x) {
			return this.x - x;
		}
		if (x >= this.x + size) {
			return (this.x + (size - 1)) - x;
		}
		return 0;
	}

	public int yDifference(int y) {
		if (y < this.y) {
			return this.y - y;
		}
		if (y >= this.y + size) {
			return (this.y + (size - 1)) - y;
		}
		return 0;
	}

	public boolean outsideMapRange(int x, int y) {
		return this.distance(x, y) > 14;
	}

	public boolean contains(int x, int y) {
		if (x < this.x || x >= this.x + size) {
			return false;
		}
		if (y < this.y || y >= this.y + size) {
			return false;
		}
		return true;
	}

	public int distance(int x, int y) {
		int distX, distY;
		if (x < this.x) {
			distX = this.x - x;
		} else if (x >= this.x + size) {
			distX = x - (this.x + (size - 1));
		} else {
			distX = 0;
		}
		if (y < this.y) {
			distY = this.y - y;
		} else if (y >= this.y + size) {
			distY = y - (this.y + (size - 1));
		} else {
			distY = 0;
		}
		if (distX == distY) {
			return distX + 1;
		}
		return distX > distY ? distX : distY;
	}

	public int distance(Position position) {
		return this.distance(position.getX(), position.getY());
	}

	public Direction moveAwayFrom(Position position, int attempts) {
		int z = position.getZ() % 4;
		int x = position.getX();
		int y = position.getY();
		int middleX = this.x + (size / 2);
		int middleY = this.y + (size / 2);
		int xDifference = Math.abs(middleX - x);
		int yDifference = Math.abs(middleY - y);
		if (xDifference > yDifference) {
			if (x < middleX) {
				if (attempts <= 0 && !World.directionBlocked(Direction.WEST, z, this.x + size, this.y, size)) {
					return Direction.EAST;
				}
				if (y > middleY) {
					if (attempts <= 1 && !World.directionBlocked(Direction.NORTH, z, x, y - 1, size)) {
						return Direction.SOUTH;
					}
					if (attempts <= 2 && !World.directionBlocked(Direction.SOUTH, z, x, y + size, size)) {
						return Direction.NORTH;
					}
				} else if (y < middleY) {
					if (attempts <= 1 && !World.directionBlocked(Direction.SOUTH, z, x, y + size, size)) {
						return Direction.NORTH;
					}
					if (attempts <= 2 && !World.directionBlocked(Direction.NORTH, z, x, y - 1, size)) {
						return Direction.SOUTH;
					}
				}
				if (!World.directionBlocked(Direction.EAST, z, this.x - 1, this.y, size)) {
					return Direction.WEST;
				}
				if (attempts > 0 && !World.directionBlocked(Direction.WEST, z, this.x + size, this.y, size)) {
					return Direction.EAST;
				}
				if (y > middleY) {
					if (attempts > 1 && !World.directionBlocked(Direction.NORTH, z, x, y - 1, size)) {
						return Direction.SOUTH;
					}
					if (attempts > 2 && !World.directionBlocked(Direction.SOUTH, z, x, y + size, size)) {
						return Direction.NORTH;
					}
				} else if (y < middleY) {
					if (attempts > 1 && !World.directionBlocked(Direction.SOUTH, z, x, y + size, size)) {
						return Direction.NORTH;
					}
					if (attempts > 2 && !World.directionBlocked(Direction.NORTH, z, x, y - 1, size)) {
						return Direction.SOUTH;
					}
				}
				return Direction.NONE;
			} else {
				if (attempts <= 0 && !World.directionBlocked(Direction.EAST, z, this.x - 1, this.y, size)) {
					return Direction.WEST;
				}
				if (y > middleY) {
					if (attempts <= 1 && !World.directionBlocked(Direction.NORTH, z, x, y - 1, size)) {
						return Direction.SOUTH;
					}
					if (attempts <= 2 && !World.directionBlocked(Direction.SOUTH, z, x, y + size, size)) {
						return Direction.NORTH;
					}
				} else if (y < middleY) {
					if (attempts <= 1 && !World.directionBlocked(Direction.SOUTH, z, x, y + size, size)) {
						return Direction.NORTH;
					}
					if (attempts <= 2 && !World.directionBlocked(Direction.NORTH, z, x, y - 1, size)) {
						return Direction.SOUTH;
					}
				}
				if (!World.directionBlocked(Direction.WEST, z, this.x + size, this.y, size)) {
					return Direction.EAST;
				}
				if (attempts > 0 && !World.directionBlocked(Direction.EAST, z, this.x - 1, this.y, size)) {
					return Direction.WEST;
				}
				if (y > middleY) {
					if (attempts > 1 && !World.directionBlocked(Direction.NORTH, z, x, y - 1, size)) {
						return Direction.SOUTH;
					}
					if (attempts > 2 && !World.directionBlocked(Direction.SOUTH, z, x, y + size, size)) {
						return Direction.NORTH;
					}
				} else if (y < middleY) {
					if (attempts > 1 && !World.directionBlocked(Direction.SOUTH, z, x, y + size, size)) {
						return Direction.NORTH;
					}
					if (attempts > 2 && !World.directionBlocked(Direction.NORTH, z, x, y - 1, size)) {
						return Direction.SOUTH;
					}
				}
				return Direction.NONE;
			}
		}
		if (yDifference > xDifference) {
			if (y > middleY) {
				if (attempts <= 0 && !World.directionBlocked(Direction.NORTH, z, this.x, this.y - 1, size)) {
					return Direction.SOUTH;
				}
				if (x < middleX) {
					if (attempts <= 1 && !World.directionBlocked(Direction.WEST, z, x + size, y, size)) {
						return Direction.EAST;
					}
					if (attempts <= 2 && !World.directionBlocked(Direction.EAST, z, x - 1, y, size)) {
						return Direction.WEST;
					}
				} else if (x > middleX) {
					if (attempts <= 1 && !World.directionBlocked(Direction.EAST, z, x - 1, y, size)) {
						return Direction.WEST;
					}
					if (attempts <= 2 && !World.directionBlocked(Direction.WEST, z, x + size, y, size)) {
						return Direction.EAST;
					}
				}
				if (!World.directionBlocked(Direction.SOUTH, z, this.x, this.y + size, size)) {
					return Direction.NORTH;
				}
				if (attempts > 0 && !World.directionBlocked(Direction.NORTH, z, this.x, this.y - 1, size)) {
					return Direction.SOUTH;
				}
				if (x < middleX) {
					if (attempts > 1 && !World.directionBlocked(Direction.WEST, z, x + size, y, size)) {
						return Direction.EAST;
					}
					if (attempts > 2 && !World.directionBlocked(Direction.EAST, z, x - 1, y, size)) {
						return Direction.WEST;
					}
				} else if (x > middleX) {
					if (attempts > 1 && !World.directionBlocked(Direction.EAST, z, x - 1, y, size)) {
						return Direction.WEST;
					}
					if (attempts > 2 && !World.directionBlocked(Direction.WEST, z, x + size, y, size)) {
						return Direction.EAST;
					}
				}
				return Direction.NONE;
			} else {
				if (attempts <= 0 && !World.directionBlocked(Direction.SOUTH, z, this.x, this.y + size, size)) {
					return Direction.NORTH;
				}
				if (x < middleX) {
					if (attempts <= 1 && !World.directionBlocked(Direction.WEST, z, x + size, y, size)) {
						return Direction.EAST;
					}
					if (attempts <= 2 && !World.directionBlocked(Direction.EAST, z, x - 1, y, size)) {
						return Direction.WEST;
					}
				} else if (x > middleX) {
					if (attempts <= 1 && !World.directionBlocked(Direction.EAST, z, x - 1, y, size)) {
						return Direction.WEST;
					}
					if (attempts <= 2 && !World.directionBlocked(Direction.WEST, z, x + size, y, size)) {
						return Direction.EAST;
					}
				}
				if (!World.directionBlocked(Direction.NORTH, z, this.x, this.y - 1, size)) {
					return Direction.SOUTH;
				}
				if (attempts > 0 && !World.directionBlocked(Direction.SOUTH, z, this.x, this.y + size, size)) {
					return Direction.NORTH;
				}
				if (x < middleX) {
					if (attempts > 1 && !World.directionBlocked(Direction.WEST, z, x + size, y, size)) {
						return Direction.EAST;
					}
					if (attempts > 2 && !World.directionBlocked(Direction.EAST, z, x - 1, y, size)) {
						return Direction.WEST;
					}
				} else if (x > middleX) {
					if (attempts > 1 && !World.directionBlocked(Direction.EAST, z, x - 1, y, size)) {
						return Direction.WEST;
					}
					if (attempts > 2 && !World.directionBlocked(Direction.WEST, z, x + size, y, size)) {
						return Direction.EAST;
					}
				}
				return Direction.NONE;
			}
		}
		if (x > middleX) {
			if (attempts <= 0 && !World.directionBlocked(Direction.EAST, z, this.x - 1, this.y, size)) {
				return Direction.WEST;
			}
			if (attempts <= 1 && !World.directionBlocked(Direction.SOUTH, z, x, y + size, size)) {
				return Direction.NORTH;
			}
			if (attempts <= 2 && !World.directionBlocked(Direction.NORTH, z, x, y - 1, size)) {
				return Direction.SOUTH;
			}
			if (!World.directionBlocked(Direction.WEST, z, this.x + size, this.y, size)) {
				return Direction.EAST;
			}
			if (attempts > 0 && !World.directionBlocked(Direction.EAST, z, this.x - 1, this.y, size)) {
				return Direction.WEST;
			}
			if (attempts > 1 && !World.directionBlocked(Direction.SOUTH, z, x, y + size, size)) {
				return Direction.NORTH;
			}
			if (attempts > 2 && !World.directionBlocked(Direction.NORTH, z, x, y - 1, size)) {
				return Direction.SOUTH;
			}
			return Direction.NONE;
		} else {
			if (attempts <= 0 && !World.directionBlocked(Direction.WEST, z, this.x + size, this.y, size)) {
				return Direction.EAST;
			}
			if (attempts <= 1 && !World.directionBlocked(Direction.SOUTH, z, x, y + size, size)) {
				return Direction.NORTH;
			}
			if (attempts <= 2 && !World.directionBlocked(Direction.NORTH, z, x, y - 1, size)) {
				return Direction.SOUTH;
			}
			if (!World.directionBlocked(Direction.EAST, z, this.x - 1, this.y, size)) {
				return Direction.WEST;
			}
			if (attempts > 0 && !World.directionBlocked(Direction.WEST, z, this.x + size, this.y, size)) {
				return Direction.EAST;
			}
			if (attempts > 1 && !World.directionBlocked(Direction.SOUTH, z, x, y + size, size)) {
				return Direction.NORTH;
			}
			if (attempts > 2 && !World.directionBlocked(Direction.NORTH, z, x, y - 1, size)) {
				return Direction.SOUTH;
			}
			return Direction.NONE;
		}
	}

	@Override
	public String toString() {
		return "X: "+this.getX()+",Y: "+this.getY();
	}
}
