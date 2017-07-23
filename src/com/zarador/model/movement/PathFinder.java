package com.zarador.model.movement;

import java.util.LinkedList;

import com.zarador.model.Position;
import com.zarador.world.World;
import com.zarador.world.entity.impl.Character;

public class PathFinder {

	public static boolean canTraverseTo(Character walker, Character target, boolean near) {
        /*
         * Check conditions so we do not waste resources.
         */
		if(walker.getPosition().distanceTo(target.getPosition()) >= 64 || target.getPosition().getX() == walker.getPosition().getX() && target.getPosition().getY() == walker.getPosition().getY() && near) {
			return false;
		}

		int destX = target.getPosition().getX() - 8 * walker.getLastKnownRegion().getRegionX();
		int destY = target.getPosition().getY() - 8 * walker.getLastKnownRegion().getRegionY();

		int[][] via = new int[104][104];
		int[][] cost = new int[104][104];
		LinkedList<Integer> tileQueueX = new LinkedList<Integer>();
		LinkedList<Integer> tileQueueY = new LinkedList<Integer>();
		for (int xx = 0; xx < 104; xx++) {
			for (int yy = 0; yy < 104; yy++) {
				cost[xx][yy] = 99999999;
			}
		}
		int curX = walker.getPosition().getLocalX(walker.getLastKnownRegion());
		int curY = walker.getPosition().getLocalY(walker.getLastKnownRegion());

		//System.out.println("fromLocalX: " + curX + ", fromLocalY: " + curY);

		via[curX][curY] = 99;
		cost[curX][curY] = 0;
		int currentIndex = 0;
		tileQueueX.add(curX);
		tileQueueY.add(curY);
		boolean foundPath = false;
		int pathLength = 4000;
		while (currentIndex != tileQueueX.size() && tileQueueX.size() < pathLength) {
			curX = tileQueueX.get(currentIndex);
			curY = tileQueueY.get(currentIndex);
			int curAbsX = walker.getLastKnownRegion().getRegionX() * 8 + curX;
			int curAbsY = walker.getLastKnownRegion().getRegionY() * 8 + curY;
			if (curX == destX && curY == destY) {
				foundPath = true;
				//System.out.println("Destionation Tiles: "+(m.getLastKnownRegion().getRegionX() * 8+destX)+" "+(m.getLastKnownRegion().getRegionY() * 8+destY));
				break;
			}
			currentIndex = (currentIndex + 1) % pathLength;
			int thisCost = cost[curX][curY] + 1;
			if (curY > 0 && via[curX][curY - 1] == 0 && (World.getMask(curAbsX, curAbsY - 1, walker.getPosition().getZ()) & 0x1280102) == 0) {
				tileQueueX.add(curX);
				tileQueueY.add(curY - 1);
				via[curX][curY - 1] = 1;
				cost[curX][curY - 1] = thisCost;
			}
			if (curX > 0 && via[curX - 1][curY] == 0 && (World.getMask(curAbsX - 1, curAbsY, walker.getPosition().getZ()) & 0x1280108) == 0) {
				tileQueueX.add(curX - 1);
				tileQueueY.add(curY);
				via[curX - 1][curY] = 2;
				cost[curX - 1][curY] = thisCost;
			}
			if (curY < 104 - 1 && via[curX][curY + 1] == 0 && (World.getMask(curAbsX, curAbsY + 1, walker.getPosition().getZ()) & 0x1280120) == 0) {
				tileQueueX.add(curX);
				tileQueueY.add(curY + 1);
				via[curX][curY + 1] = 4;
				cost[curX][curY + 1] = thisCost;
			}
			if (curX < 104 - 1 && via[curX + 1][curY] == 0 && (World.getMask(curAbsX + 1, curAbsY, walker.getPosition().getZ()) & 0x1280180) == 0) {
				tileQueueX.add(curX + 1);
				tileQueueY.add(curY);
				via[curX + 1][curY] = 8;
				cost[curX + 1][curY] = thisCost;
			}
			if (curX > 0 && curY > 0 && via[curX - 1][curY - 1] == 0 && (World.getMask(curAbsX - 1, curAbsY - 1, walker.getPosition().getZ()) & 0x128010e) == 0 && (World.getMask(curAbsX - 1, curAbsY, walker.getPosition().getZ()) & 0x1280108) == 0 && (World.getMask(curAbsX, curAbsY - 1, walker.getPosition().getZ()) & 0x1280102) == 0) {
				tileQueueX.add(curX - 1);
				tileQueueY.add(curY - 1);
				via[curX - 1][curY - 1] = 3;
				cost[curX - 1][curY - 1] = thisCost;
			}
			if (curX > 0 && curY < 104 - 1 && via[curX - 1][curY + 1] == 0 && (World.getMask(curAbsX - 1, curAbsY + 1, walker.getPosition().getZ()) & 0x1280138) == 0 && (World.getMask(curAbsX - 1, curAbsY, walker.getPosition().getZ()) & 0x1280108) == 0 && (World.getMask(curAbsX, curAbsY + 1, walker.getPosition().getZ()) & 0x1280120) == 0) {
				tileQueueX.add(curX - 1);
				tileQueueY.add(curY + 1);
				via[curX - 1][curY + 1] = 6;
				cost[curX - 1][curY + 1] = thisCost;
			}
			if (curX < 104 - 1 && curY > 0 && via[curX + 1][curY - 1] == 0 && (World.getMask(curAbsX + 1, curAbsY - 1, walker.getPosition().getZ()) & 0x1280183) == 0 && (World.getMask(curAbsX + 1, curAbsY, walker.getPosition().getZ()) & 0x1280180) == 0 && (World.getMask(curAbsX, curAbsY - 1, walker.getPosition().getZ()) & 0x1280102) == 0) {
				tileQueueX.add(curX + 1);
				tileQueueY.add(curY - 1);
				via[curX + 1][curY - 1] = 9;
				cost[curX + 1][curY - 1] = thisCost;
			}
			if (curX < 104 - 1 && curY < 104 - 1 && via[curX + 1][curY + 1] == 0 && (World.getMask(curAbsX + 1, curAbsY + 1, walker.getPosition().getZ()) & 0x12801e0) == 0 && (World.getMask(curAbsX + 1, curAbsY, walker.getPosition().getZ()) & 0x1280180) == 0 && (World.getMask(curAbsX, curAbsY + 1, walker.getPosition().getZ()) & 0x1280120) == 0) {
				tileQueueX.add(curX + 1);
				tileQueueY.add(curY + 1);
				via[curX + 1][curY + 1] = 12;
				cost[curX + 1][curY + 1] = thisCost;
			}
		}
		if (!foundPath) {
			if (near) {
				int i_223_ = 1000;
				int thisCost = 100;
				int offset = 10;
				for (int x = destX - offset; x <= destX + offset; x++) {
					for (int y = destY - offset; y <= destY + offset; y++) {
						if (x >= 0 && y >= 0 && x < 104 && y < 104 && cost[x][y] < 100) {
							int i_228_ = 0;
							if (x < destX) {
								i_228_ = destX - x;
							} else if (x > destX + walker.getSize() - 1) {
								i_228_ = x - (destX + walker.getSize() - 1);
							}
							int i_229_ = 0;
							if (y < destY) {
								i_229_ = destY - y;
							} else if (y > destY + walker.getSize() - 1) {
								i_229_ = y - (destY + walker.getSize() - 1);
							}
							int i_230_ = i_228_ * i_228_ + i_229_ * i_229_;
							if (i_230_ < i_223_ || i_230_ == i_223_ && cost[x][y] < thisCost) {
								i_223_ = i_230_;
								thisCost = cost[x][y];
								curX = x;
								curY = y;
							}
						}
					}
				}
				if (i_223_ == 1000) {
					System.out.println("Path Terminated: Value exceeds 1000");
					return false;
				}
			} else {
				System.out.println("Path Terminated: Path Not Found.");
				return false;
			}
		}
		currentIndex = 0;
		tileQueueX.set(currentIndex, curX);
		tileQueueY.set(currentIndex++, curY);
		int l5;
		for (int j5 = l5 = via[curX][curY]; curX != walker.getPosition().getLocalX(walker.getLastKnownRegion()) || curY != walker.getPosition().getLocalY(walker.getLastKnownRegion()); j5 = via[curX][curY]) {
			if (j5 != l5) {
				l5 = j5;
				tileQueueX.set(currentIndex, curX);
				tileQueueY.set(currentIndex++, curY);
			}
			if ((j5 & 2) != 0) {
				curX++;
			} else if ((j5 & 8) != 0) {
				curX--;
			}
			if ((j5 & 1) != 0) {
				curY++;
			} else if ((j5 & 4) != 0) {
				curY--;
			}
		}
		return true;
	}

	/**
	 * This will find the path available to the mobile unit.
	 * @param m The mobile entity moving towards a path.
	 * @param destX The destination X coordinate.
	 * @param destY The destination Y coordinate.
	 * @param near If we are going near, or on the destination.
	 */
	public static void calculatePath(Character m, int destX, int destY, int xLength, int yLength, boolean near) {
        /*
         * Check conditions so we do not waste resources.
         */
		if(m.getPosition().distanceTo(new Position(destX, destY)) >= 100 || destX == m.getPosition().getX() && destY == m.getPosition().getY() && near) {
			return;
		}

		destX = destX - 8 * m.getLastKnownRegion().getRegionX();
		destY = destY - 8 * m.getLastKnownRegion().getRegionY();

		int[][] via = new int[104][104];
		int[][] cost = new int[104][104];
		LinkedList<Integer> tileQueueX = new LinkedList<Integer>();
		LinkedList<Integer> tileQueueY = new LinkedList<Integer>();
		for (int xx = 0; xx < 104; xx++) {
			for (int yy = 0; yy < 104; yy++) {
				cost[xx][yy] = 99999999;
			}
		}
		int curX = m.getPosition().getLocalX(m.getLastKnownRegion());
		int curY = m.getPosition().getLocalY(m.getLastKnownRegion());

		//System.out.println("fromLocalX: " + curX + ", fromLocalY: " + curY);

		via[curX][curY] = 99;
		cost[curX][curY] = 0;
		int currentIndex = 0;
		tileQueueX.add(curX);
		tileQueueY.add(curY);
		boolean foundPath = false;
		int pathLength = 4000;
		while (currentIndex != tileQueueX.size() && tileQueueX.size() < pathLength) {
			curX = tileQueueX.get(currentIndex);
			curY = tileQueueY.get(currentIndex);
			int curAbsX = m.getLastKnownRegion().getRegionX() * 8 + curX;
			int curAbsY = m.getLastKnownRegion().getRegionY() * 8 + curY;
			if (curX == destX && curY == destY) {
				foundPath = true;
				//System.out.println("Destionation Tiles: "+(m.getLastKnownRegion().getRegionX() * 8+destX)+" "+(m.getLastKnownRegion().getRegionY() * 8+destY));
				break;
			}
			currentIndex = (currentIndex + 1) % pathLength;
			int thisCost = cost[curX][curY] + 1;
			if (curY > 0 && via[curX][curY - 1] == 0 && (World.getMask(curAbsX, curAbsY - 1, m.getPosition().getZ()) & 0x1280102) == 0) {
				tileQueueX.add(curX);
				tileQueueY.add(curY - 1);
				via[curX][curY - 1] = 1;
				cost[curX][curY - 1] = thisCost;
			}
			if (curX > 0 && via[curX - 1][curY] == 0 && (World.getMask(curAbsX - 1, curAbsY, m.getPosition().getZ()) & 0x1280108) == 0) {
				tileQueueX.add(curX - 1);
				tileQueueY.add(curY);
				via[curX - 1][curY] = 2;
				cost[curX - 1][curY] = thisCost;
			}
			if (curY < 104 - 1 && via[curX][curY + 1] == 0 && (World.getMask(curAbsX, curAbsY + 1, m.getPosition().getZ()) & 0x1280120) == 0) {
				tileQueueX.add(curX);
				tileQueueY.add(curY + 1);
				via[curX][curY + 1] = 4;
				cost[curX][curY + 1] = thisCost;
			}
			if (curX < 104 - 1 && via[curX + 1][curY] == 0 && (World.getMask(curAbsX + 1, curAbsY, m.getPosition().getZ()) & 0x1280180) == 0) {
				tileQueueX.add(curX + 1);
				tileQueueY.add(curY);
				via[curX + 1][curY] = 8;
				cost[curX + 1][curY] = thisCost;
			}
			if (curX > 0 && curY > 0 && via[curX - 1][curY - 1] == 0 && (World.getMask(curAbsX - 1, curAbsY - 1, m.getPosition().getZ()) & 0x128010e) == 0 && (World.getMask(curAbsX - 1, curAbsY, m.getPosition().getZ()) & 0x1280108) == 0 && (World.getMask(curAbsX, curAbsY - 1, m.getPosition().getZ()) & 0x1280102) == 0) {
				tileQueueX.add(curX - 1);
				tileQueueY.add(curY - 1);
				via[curX - 1][curY - 1] = 3;
				cost[curX - 1][curY - 1] = thisCost;
			}
			if (curX > 0 && curY < 104 - 1 && via[curX - 1][curY + 1] == 0 && (World.getMask(curAbsX - 1, curAbsY + 1, m.getPosition().getZ()) & 0x1280138) == 0 && (World.getMask(curAbsX - 1, curAbsY, m.getPosition().getZ()) & 0x1280108) == 0 && (World.getMask(curAbsX, curAbsY + 1, m.getPosition().getZ()) & 0x1280120) == 0) {
				tileQueueX.add(curX - 1);
				tileQueueY.add(curY + 1);
				via[curX - 1][curY + 1] = 6;
				cost[curX - 1][curY + 1] = thisCost;
			}
			if (curX < 104 - 1 && curY > 0 && via[curX + 1][curY - 1] == 0 && (World.getMask(curAbsX + 1, curAbsY - 1, m.getPosition().getZ()) & 0x1280183) == 0 && (World.getMask(curAbsX + 1, curAbsY, m.getPosition().getZ()) & 0x1280180) == 0 && (World.getMask(curAbsX, curAbsY - 1, m.getPosition().getZ()) & 0x1280102) == 0) {
				tileQueueX.add(curX + 1);
				tileQueueY.add(curY - 1);
				via[curX + 1][curY - 1] = 9;
				cost[curX + 1][curY - 1] = thisCost;
			}
			if (curX < 104 - 1 && curY < 104 - 1 && via[curX + 1][curY + 1] == 0 && (World.getMask(curAbsX + 1, curAbsY + 1, m.getPosition().getZ()) & 0x12801e0) == 0 && (World.getMask(curAbsX + 1, curAbsY, m.getPosition().getZ()) & 0x1280180) == 0 && (World.getMask(curAbsX, curAbsY + 1, m.getPosition().getZ()) & 0x1280120) == 0) {
				tileQueueX.add(curX + 1);
				tileQueueY.add(curY + 1);
				via[curX + 1][curY + 1] = 12;
				cost[curX + 1][curY + 1] = thisCost;
			}
		}
		if (!foundPath) {
			if (near) {
				int i_223_ = 1000;
				int thisCost = 100;
				int offset = 10;
				for (int x = destX - offset; x <= destX + offset; x++) {
					for (int y = destY - offset; y <= destY + offset; y++) {
						if (x >= 0 && y >= 0 && x < 104 && y < 104 && cost[x][y] < 100) {
							int i_228_ = 0;
							if (x < destX) {
								i_228_ = destX - x;
							} else if (x > destX + xLength - 1) {
								i_228_ = x - (destX + xLength - 1);
							}
							int i_229_ = 0;
							if (y < destY) {
								i_229_ = destY - y;
							} else if (y > destY + yLength - 1) {
								i_229_ = y - (destY + yLength - 1);
							}
							int i_230_ = i_228_ * i_228_ + i_229_ * i_229_;
							if (i_230_ < i_223_ || i_230_ == i_223_ && cost[x][y] < thisCost) {
								i_223_ = i_230_;
								thisCost = cost[x][y];
								curX = x;
								curY = y;
							}
						}
					}
				}
				if (i_223_ == 1000) {
					System.out.println("Path Terminated: Value exceeds 1000");
					return;
				}
			} else {
				System.out.println("Path Terminated: Path Not Found.");
				return;
			}
		}
		currentIndex = 0;
		tileQueueX.set(currentIndex, curX);
		tileQueueY.set(currentIndex++, curY);
		int l5;
		for (int j5 = l5 = via[curX][curY]; curX != m.getPosition().getLocalX(m.getLastKnownRegion()) || curY != m.getPosition().getLocalY(m.getLastKnownRegion()); j5 = via[curX][curY]) {
			if (j5 != l5) {
				l5 = j5;
				tileQueueX.set(currentIndex, curX);
				tileQueueY.set(currentIndex++, curY);
			}
			if ((j5 & 2) != 0) {
				curX++;
			} else if ((j5 & 8) != 0) {
				curX--;
			}
			if ((j5 & 1) != 0) {
				curY++;
			} else if ((j5 & 4) != 0) {
				curY--;
			}
		}
		m.getWalkingQueue().clear();
		int size = currentIndex--;
		int pathX = m.getLastKnownRegion().getRegionX() * 8 + tileQueueX.get(currentIndex);
		int pathY = m.getLastKnownRegion().getRegionY() * 8 + tileQueueY.get(currentIndex);
		m.getWalkingQueue().addStep(pathX, pathY);
		for (int i = 1; i < size; i++) {
			currentIndex--;
			pathX = m.getLastKnownRegion().getRegionX() * 8 + tileQueueX.get(currentIndex);
			pathY = m.getLastKnownRegion().getRegionY() * 8 + tileQueueY.get(currentIndex);
			m.getWalkingQueue().addStep(pathX, pathY);
		}
	}

	/**
	 * This localizes the regional Coordinate.
	 * @param x The coordinate to be localized.
	 * @param mapRegion The region to be to localized.
	 * @return The variable.
	 */
	private static int localize(int x, int mapRegion) {
		return x - 8 * mapRegion;
	}
}
