/**
 * @author Dalton Harriman (Palidino/Palidino76)
 */
package com.zarador.world.clip.region;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.zarador.GameServer;
import com.zarador.cache.Container;
import com.zarador.model.Direction;
import com.zarador.model.GameObject;
import com.zarador.model.Position;
import com.zarador.util.ByteStream;
import com.zarador.world.World;
import com.zarador.world.entity.impl.Character;
import com.zarador.world.entity.impl.npc.NPC;

public final class Region {

    public static final int NORTH_WEST_BLOCKED = 0x1;
    public static final int NORTH_BLOCKED = 0x2;
    public static final int NORTH_EAST_BLOCKED = 0x4;
    public static final int EAST_BLOCKED = 0x8;
    public static final int SOUTH_EAST_BLOCKED = 0x10;
    public static final int SOUTH_BLOCKED = 0x20;
    public static final int SOUTH_WEST_BLOCKED = 0x40;
    public static final int WEST_BLOCKED = 0x80;
    public static final int TILE_BLOCKED = 0x100;
    public static final int PROJECTILE_NORTH_WEST_BLOCKED = 0x200;
    public static final int PROJECTILE_NORTH_BLOCKED = 0x400;
    public static final int PROJECTILE_NORTH_EAST_BLOCKED = 0x800;
    public static final int PROJECTILE_EAST_BLOCKED = 0x1000;
    public static final int PROJECTILE_SOUTH_EAST_BLOCKED = 0x2000;
    public static final int PROJECTILE_SOUTH_BLOCKED = 0x4000;
    public static final int PROJECTILE_SOUTH_WEST_BLOCKED = 0x8000;
    public static final int PROJECTILE_WEST_BLOCKED = 0x10000;
    public static final int PROJECTILE_TILE_BLOCKED = 0x20000;
    public static final int UNKNOWN = 0x80000;
    public static final int BLOCKED_TILE = 0x200000;
    public static final int UNLOADED_TILE = 0x1000000;
    private static final ArrayList<GameObject> customObjects = new ArrayList<>();
    private static final ArrayList<Integer> loadedRegions = new ArrayList<>();


    public static GameObject getCustomObject(Position p) {
        for(int i = 0; i < customObjects.size(); i++) {
            if(customObjects.get(i).getPosition() == p) {
                return customObjects.get(i);
            }
        }
        return null;
    }

    public static boolean hasCustomObject(Position p) {
        return getCustomObject(p) != null;
    }

    public static void addCustomObject(GameObject object) {
        if (object == null) {
            new Throwable().printStackTrace();
            return;
        }
        Region.customObjects.add(object);
    }

    public static void removeCustomObject(GameObject object) {
        Region.customObjects.remove(object);
    }

    /**
     * Check if the monster can move to the position.
     * @param startX The starting position.
     * @param startY The starting Y position.
     * @param endX The ending X position.
     * @param endY The ending Y position.
     * @param height The height of this check.
     * @param size The size of the attacking npcs.
     * @return if the entity can move.
     */
    public static boolean canMove(int startX, int startY, int endX, int endY, int height, int size) {
        int diffX = endX - startX;
        int diffY = endY - startY;
        height %= 4;
        int max = Math.max(Math.abs(diffX), Math.abs(diffY));
        for (int ii = 0; ii < max; ii++) {
            int currentX = endX - diffX;
            int currentY = endY - diffY;

            Direction dir = Direction.direction(diffX, diffY);
            if (World.directionBlocked(dir, height, currentX, currentY, size)) {
                return false;
            }

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
        }
        return true;
    }

    /**
     * This method is the easy method.
     * @param startX The starting X position.
     * @param startY The starting Y position.
     * @param endX The ending X position.
     * @param endY The ending Y position.
     * @param height The height of this projectile.
     * @param size The size of the attacking npcs.
     * @return If a projectile can travel.
     */
    public static boolean canProjectileMove(int startX, int startY, int endX, int endY, int height, int size) {
        int diffX = endX - startX;
        int diffY = endY - startY;
        height %= 4;
        int max = Math.max(Math.abs(diffX), Math.abs(diffY));
        for (int ii = 0; ii < max; ii++) {
            int currentX = endX - diffX;
            int currentY = endY - diffY;
            Direction dir = Direction.direction(diffX, diffY);
            if (World.projectileDirectionBlocked(dir, height, currentX, currentY, size == 0 ? 1 : size)) {
                return false;
            }

            if (diffX < 0) {
                diffX++;
            } else if (diffX > 0) {
                diffX--;
            }
            if (diffY < 0) {
                diffY++; // change
            } else if (diffY > 0) {
                diffY--;
            }
        }
        return true;
    }

    public static boolean canMove(int startX, int startY, int endX, int endY, int height, int xLength, int yLength) {
        int diffX = endX - startX;
        int diffY = endY - startY;
        height %= 4;
        int max = Math.max(Math.abs(diffX), Math.abs(diffY));
        for (int ii = 0; ii < max; ii++) {
            int currentX = endX - diffX;
            int currentY = endY - diffY;
            for (int i = 0; i < xLength; i++) {
                for (int i2 = 0; i2 < yLength; i2++) {
                    if (diffX < 0 && diffY < 0) {
                        if ((World.getMask(currentX + i - 1, currentY + i2 - 1, height) & (Region.UNLOADED_TILE | Region.BLOCKED_TILE | Region.UNKNOWN | Region.TILE_BLOCKED | Region.EAST_BLOCKED
                                | Region.NORTH_EAST_BLOCKED | Region.NORTH_BLOCKED)) != 0
                                || (World.getMask(currentX + i - 1, currentY + i2, height) & (Region.UNLOADED_TILE | Region.BLOCKED_TILE | Region.UNKNOWN | Region.TILE_BLOCKED | Region.EAST_BLOCKED)) != 0
                                || (World.getMask(currentX + i, currentY + i2 - 1, height) & (Region.UNLOADED_TILE | Region.BLOCKED_TILE | Region.UNKNOWN | Region.TILE_BLOCKED | Region.NORTH_BLOCKED)) != 0) {
                            return false;
                        }
                    } else if (diffX > 0 && diffY > 0) {
                        if ((World.getMask(currentX + i + 1, currentY + i2 + 1, height) & (Region.UNLOADED_TILE | Region.BLOCKED_TILE | Region.UNKNOWN | Region.TILE_BLOCKED | Region.WEST_BLOCKED
                                | Region.SOUTH_WEST_BLOCKED | Region.SOUTH_BLOCKED)) != 0
                                || (World.getMask(currentX + i + 1, currentY + i2, height) & (Region.UNLOADED_TILE | Region.BLOCKED_TILE | Region.UNKNOWN | Region.TILE_BLOCKED | Region.WEST_BLOCKED)) != 0
                                || (World.getMask(currentX + i, currentY + i2 + 1, height) & (Region.UNLOADED_TILE | Region.BLOCKED_TILE | Region.UNKNOWN | Region.TILE_BLOCKED | Region.SOUTH_BLOCKED)) != 0) {
                            return false;
                        }
                    } else if (diffX < 0 && diffY > 0) {
                        if ((World.getMask(currentX + i - 1, currentY + i2 + 1, height) & (Region.UNLOADED_TILE | Region.BLOCKED_TILE | Region.UNKNOWN | Region.TILE_BLOCKED | Region.SOUTH_BLOCKED
                                | Region.SOUTH_EAST_BLOCKED | Region.EAST_BLOCKED)) != 0
                                || (World.getMask(currentX + i - 1, currentY + i2, height) & (Region.UNLOADED_TILE | Region.BLOCKED_TILE | Region.UNKNOWN | Region.TILE_BLOCKED | Region.EAST_BLOCKED)) != 0
                                || (World.getMask(currentX + i, currentY + i2 + 1, height) & (Region.UNLOADED_TILE | Region.BLOCKED_TILE | Region.UNKNOWN | Region.TILE_BLOCKED | Region.SOUTH_BLOCKED)) != 0) {
                            return false;
                        }
                    } else if (diffX > 0 && diffY < 0) {
                        if ((World.getMask(currentX + i + 1, currentY + i2 - 1, height) & (Region.UNLOADED_TILE | Region.BLOCKED_TILE | Region.UNKNOWN | Region.TILE_BLOCKED | Region.WEST_BLOCKED
                                | Region.NORTH_BLOCKED | Region.NORTH_WEST_BLOCKED)) != 0
                                || (World.getMask(currentX + i + 1, currentY + i2, height) & (Region.UNLOADED_TILE | Region.BLOCKED_TILE | Region.UNKNOWN | Region.TILE_BLOCKED | Region.WEST_BLOCKED)) != 0
                                || (World.getMask(currentX + i, currentY + i2 - 1, height) & (Region.UNLOADED_TILE | Region.BLOCKED_TILE | Region.UNKNOWN | Region.TILE_BLOCKED | Region.NORTH_BLOCKED)) != 0) {
                            return false;
                        }
                    } else if (diffX > 0 && diffY == 0) {
                        if ((World.getMask(currentX + i + 1, currentY + i2, height) & (Region.UNLOADED_TILE | Region.BLOCKED_TILE | Region.UNKNOWN | Region.TILE_BLOCKED | Region.WEST_BLOCKED)) != 0) {
                            return false;
                        }
                    } else if (diffX < 0 && diffY == 0) {
                        if ((World.getMask(currentX + i - 1, currentY + i2, height) & (Region.UNLOADED_TILE | Region.BLOCKED_TILE | Region.UNKNOWN | Region.TILE_BLOCKED | Region.EAST_BLOCKED)) != 0) {
                            return false;
                        }
                    } else if (diffX == 0 && diffY > 0) {
                        if ((World.getMask(currentX + i, currentY + i2 + 1, height) & (Region.UNLOADED_TILE | Region.BLOCKED_TILE | Region.UNKNOWN | Region.TILE_BLOCKED | Region.SOUTH_BLOCKED)) != 0) {
                            return false;
                        }
                    } else if (diffX == 0 && diffY < 0) {
                        if ((World.getMask(currentX + i, currentY + i2 - 1, height) & (Region.UNLOADED_TILE | Region.BLOCKED_TILE | Region.UNKNOWN | Region.TILE_BLOCKED | Region.NORTH_BLOCKED)) != 0) {
                            return false;
                        }
                    }
                }
            }
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
        }
        return true;
    }

    public static boolean canProjectileMove(int startX, int startY, int endX, int endY, int height, int xLength, int yLength) {
        int diffX = endX - startX;
        int diffY = endY - startY;
        height %= 4;
        int max = Math.max(Math.abs(diffX), Math.abs(diffY));
        for (int ii = 0; ii < max; ii++) {
            int currentX = endX - diffX;
            int currentY = endY - diffY;
            for (int i = 0; i < xLength; i++) {
                for (int i2 = 0; i2 < yLength; i2++) {
                    if (diffX < 0 && diffY < 0) {
                        if ((World.getMask(currentX + i - 1, currentY + i2 - 1, height) & (Region.UNLOADED_TILE | /*
																												 * BLOCKED_TILE
																												 * |
																												 */Region.UNKNOWN | Region.PROJECTILE_TILE_BLOCKED | Region.PROJECTILE_EAST_BLOCKED
                                | Region.PROJECTILE_NORTH_EAST_BLOCKED | Region.PROJECTILE_NORTH_BLOCKED)) != 0
                                || (World.getMask(currentX + i - 1, currentY + i2, height) & (Region.UNLOADED_TILE | /*
																													 * BLOCKED_TILE
																													 * |
																													 */Region.UNKNOWN | Region.PROJECTILE_TILE_BLOCKED | Region.PROJECTILE_EAST_BLOCKED)) != 0
                                || (World.getMask(currentX + i, currentY + i2 - 1, height) & (Region.UNLOADED_TILE | /*
																													 * BLOCKED_TILE
																													 * |
																													 */Region.UNKNOWN | Region.PROJECTILE_TILE_BLOCKED | Region.PROJECTILE_NORTH_BLOCKED)) != 0) {
                            return false;
                        }
                    } else if (diffX > 0 && diffY > 0) {
                        if ((World.getMask(currentX + i + 1, currentY + i2 + 1, height) & (Region.UNLOADED_TILE | /*
																												 * BLOCKED_TILE
																												 * |
																												 */Region.UNKNOWN | Region.PROJECTILE_TILE_BLOCKED | Region.PROJECTILE_WEST_BLOCKED
                                | Region.PROJECTILE_SOUTH_WEST_BLOCKED | Region.PROJECTILE_SOUTH_BLOCKED)) != 0
                                || (World.getMask(currentX + i + 1, currentY + i2, height) & (Region.UNLOADED_TILE | /*
																													 * BLOCKED_TILE
																													 * |
																													 */Region.UNKNOWN | Region.PROJECTILE_TILE_BLOCKED | Region.PROJECTILE_WEST_BLOCKED)) != 0
                                || (World.getMask(currentX + i, currentY + i2 + 1, height) & (Region.UNLOADED_TILE | /*
																													 * BLOCKED_TILE
																													 * |
																													 */Region.UNKNOWN | Region.PROJECTILE_TILE_BLOCKED | Region.PROJECTILE_SOUTH_BLOCKED)) != 0) {
                            return false;
                        }
                    } else if (diffX < 0 && diffY > 0) {
                        if ((World.getMask(currentX + i - 1, currentY + i2 + 1, height) & (Region.UNLOADED_TILE | /*
																												 * BLOCKED_TILE
																												 * |
																												 */Region.UNKNOWN | Region.PROJECTILE_TILE_BLOCKED | Region.PROJECTILE_SOUTH_BLOCKED
                                | Region.PROJECTILE_SOUTH_EAST_BLOCKED | Region.PROJECTILE_EAST_BLOCKED)) != 0
                                || (World.getMask(currentX + i - 1, currentY + i2, height) & (Region.UNLOADED_TILE | /*
																													 * BLOCKED_TILE
																													 * |
																													 */Region.UNKNOWN | Region.PROJECTILE_TILE_BLOCKED | Region.PROJECTILE_EAST_BLOCKED)) != 0
                                || (World.getMask(currentX + i, currentY + i2 + 1, height) & (Region.UNLOADED_TILE | /*
																													 * BLOCKED_TILE
																													 * |
																													 */Region.UNKNOWN | Region.PROJECTILE_TILE_BLOCKED | Region.PROJECTILE_SOUTH_BLOCKED)) != 0) {
                            return false;
                        }
                    } else if (diffX > 0 && diffY < 0) {
                        if ((World.getMask(currentX + i + 1, currentY + i2 - 1, height) & (Region.UNLOADED_TILE | /*
																												 * BLOCKED_TILE
																												 * |
																												 */Region.UNKNOWN | Region.PROJECTILE_TILE_BLOCKED | Region.PROJECTILE_WEST_BLOCKED
                                | Region.PROJECTILE_NORTH_BLOCKED | Region.PROJECTILE_NORTH_WEST_BLOCKED)) != 0
                                || (World.getMask(currentX + i + 1, currentY + i2, height) & (Region.UNLOADED_TILE | /*
																													 * BLOCKED_TILE
																													 * |
																													 */Region.UNKNOWN | Region.PROJECTILE_TILE_BLOCKED | Region.PROJECTILE_WEST_BLOCKED)) != 0
                                || (World.getMask(currentX + i, currentY + i2 - 1, height) & (Region.UNLOADED_TILE | /*
																													 * BLOCKED_TILE
																													 * |
																													 */Region.UNKNOWN | Region.PROJECTILE_TILE_BLOCKED | Region.PROJECTILE_NORTH_BLOCKED)) != 0) {
                            return false;
                        }
                    } else if (diffX > 0 && diffY == 0) {
                        if ((World.getMask(currentX + i + 1, currentY + i2, height) & (Region.UNLOADED_TILE | /*
																											 * BLOCKED_TILE
																											 * |
																											 */Region.UNKNOWN | Region.PROJECTILE_TILE_BLOCKED | Region.PROJECTILE_WEST_BLOCKED)) != 0) {
                            return false;
                        }
                    } else if (diffX < 0 && diffY == 0) {
                        if ((World.getMask(currentX + i - 1, currentY + i2, height) & (Region.UNLOADED_TILE | /*
																											 * BLOCKED_TILE
																											 * |
																											 */Region.UNKNOWN | Region.PROJECTILE_TILE_BLOCKED | Region.PROJECTILE_EAST_BLOCKED)) != 0) {
                            return false;
                        }
                    } else if (diffX == 0 && diffY > 0) {
                        if ((World.getMask(currentX + i, currentY + i2 + 1, height) & (Region.UNLOADED_TILE | /*
																											 * BLOCKED_TILE
																											 * |
																											 */Region.UNKNOWN | Region.PROJECTILE_TILE_BLOCKED | Region.PROJECTILE_SOUTH_BLOCKED)) != 0) {
                            return false;
                        }
                    } else if (diffX == 0 && diffY < 0) {
                        if ((World.getMask(currentX + i, currentY + i2 - 1, height) & (Region.UNLOADED_TILE | /*
																											 * BLOCKED_TILE
																											 * |
																											 */Region.UNKNOWN | Region.PROJECTILE_TILE_BLOCKED | Region.PROJECTILE_NORTH_BLOCKED)) != 0) {
                            return false;
                        }
                    }
                }
            }
            if (diffX < 0) {
                diffX++;
            } else if (diffX > 0) {
                diffX--;
            }
            if (diffY < 0) {
                diffY++; // change
            } else if (diffY > 0) {
                diffY--;
            }
        }
        return true;
    }

    /*public static void displayObjects(Player player) {
        for (GameObject o : Region.customObjects) {
            if (player.getLastKnownRegion().withinRegionNoHeight(o.getPosition()) && (player.usingDungeoneering() || o.getZ() % 4 == player.getHeight() % 4)) {// were gonna do this because we reUse static regions currently.
                player.getPA().object(o.getId(), o.getX(), o.getY(), o.getRotation(), o.getType());
            }
        }
    }*/

    /**
     * Npc ids for which can still magic attack for wall blocks etc
     */
    static final int[] ignores = {
            3782, 5250, 5249, 5248, 6611
    };

    public static boolean canAttack(Character a, Character b) {
        if (a.getPosition().getZ() != b.getPosition().getZ()) {
            return false;
        }
        if (!a.isPlayer()) {
            NPC n = (NPC) a;
            for (int i : Region.ignores) {
                if (n.getId() == i) {
                    return true;
                }
            }
        }
        if (!b.isPlayer()) {
            NPC n = (NPC) b;
            for (int i : Region.ignores) {
                if (n.getId() == i) {
                    return true;
                }
            }
        }
        return Region.canMove(a.getPosition().getX(), a.getPosition().getY(), b.getPosition().getX(), b.getPosition().getY(), a.getPosition().getZ(), 1);
    }

    public static boolean canAttack(Position a, Character b) {
        if (!b.isPlayer()) {
            NPC n = (NPC) b;
            for (int i : Region.ignores) {
                return true;
            }
        }
        return Region.canMove(a.getX(), a.getY(), b.getPosition().getX(), b.getPosition().getY(), a.getZ(), 1);
    }

    public static boolean canMagicAttack(Character a2, Character b2) {
        if (!a2.isPlayer()) {
            NPC n = (NPC) a2;
            for (int i : Region.ignores) {
                if (n.getId() == i) {
                    return true;
                }
            }
        }
        if (!b2.isPlayer()) {
            NPC n = (NPC) b2;
            for (int i : Region.ignores) {
                if (n.getId() == i) {
                    return true;
                }
            }
        }
        return (Region.canProjectileMove(a2.getPosition().getX(), a2.getPosition().getY(), b2.getPosition().getX(), b2.getPosition().getY(), a2.getPosition().getZ(), a2.getSize() > 1 ? a2.getSize() / 2 : 1) ||
                Region.canProjectileMove(b2.getPosition().getX(), b2.getPosition().getY(), a2.getPosition().getX(), a2.getPosition().getY(), b2.getPosition().getZ(), a2.getSize() > 1 ? a2.getSize() / 2 : 1));
    }

    public static boolean method219(Position position, int destX, int destY, int objectType, int rotation) {
        int srcX = position.getX();
        int srcY = position.getY();
        if (srcX == destX && srcY == destY) {
            return true;
        }
        int z = position.getZ() % 4;
        switch (objectType) {
            case 0: {
                switch (rotation) {
                    case 0: {
                        if (srcX == destX - 1 && srcY == destY) {
                            return true;
                        }
                        if (srcX == destX && srcY == destY + 1 && (World.getMask(srcX, srcY, z) & (Region.SOUTH_BLOCKED | Region.TILE_BLOCKED)) == 0) {
                            return true;
                        }
                        if (srcX == destX && srcY == destY - 1 && (World.getMask(srcX, srcY, z) & (Region.NORTH_BLOCKED | Region.TILE_BLOCKED)) == 0) {
                            return true;
                        }
                        return false;
                    }
                    case 1: {
                        if (srcX == destX && srcY == destY + 1) {
                            return true;
                        }
                        if (srcX == destX - 1 && srcY == destY && (World.getMask(srcX, srcY, z) & (Region.EAST_BLOCKED | Region.TILE_BLOCKED)) == 0) {
                            return true;
                        }
                        if (srcX == destX + 1 && srcY == destY && (World.getMask(srcX, srcY, z) & (Region.WEST_BLOCKED | Region.TILE_BLOCKED)) == 0) {
                            return true;
                        }
                        return false;
                    }
                    case 2: {
                        if (srcX == destX + 1 && srcY == destY) {
                            return true;
                        }
                        if (srcX == destX && srcY == destY + 1 && (World.getMask(srcX, srcY, z) & (Region.SOUTH_BLOCKED | Region.TILE_BLOCKED)) == 0) {
                            return true;
                        }
                        if (srcX == destX && srcY == destY - 1 && (World.getMask(srcX, srcY, z) & (Region.NORTH_BLOCKED | Region.TILE_BLOCKED)) == 0) {
                            return true;
                        }
                        return false;
                    }
                    case 3: {
                        if (srcX == destX && srcY == destY - 1) {
                            return true;
                        }
                        if (srcX == destX - 1 && srcY == destY && (World.getMask(srcX, srcY, z) & (Region.EAST_BLOCKED | Region.TILE_BLOCKED)) == 0) {
                            return true;
                        }
                        if (srcX == destX + 1 && srcY == destY && (World.getMask(srcX, srcY, z) & (Region.WEST_BLOCKED | Region.TILE_BLOCKED)) == 0) {
                            return true;
                        }
                        return false;
                    }
                }
                return false;
                // throw new AssertionError();
            }
            case 2: {
                switch (rotation) {
                    case 0: {
                        if (srcX == destX - 1 && srcY == destY) {
                            return true;
                        }
                        if (srcX == destX && srcY == destY + 1) {
                            return true;
                        }
                        if (srcX == destX + 1 && srcY == destY && (World.getMask(srcX, srcY, z) & (Region.WEST_BLOCKED | Region.TILE_BLOCKED)) == 0) {
                            return true;
                        }
                        if (srcX == destX && srcY == destY - 1 && (World.getMask(srcX, srcY, z) & (Region.NORTH_BLOCKED | Region.TILE_BLOCKED)) == 0) {
                            return true;
                        }
                        return false;
                    }
                    case 1: {
                        if (srcX == destX - 1 && srcY == destY && (World.getMask(srcX, srcY, z) & (Region.EAST_BLOCKED | Region.TILE_BLOCKED)) == 0) {
                            return true;
                        }
                        if (srcX == destX && srcY == destY + 1) {
                            return true;
                        }
                        if (srcX == destX + 1 && srcY == destY) {
                            return true;
                        }
                        if (srcX == destX && srcY == destY - 1 && (World.getMask(srcX, srcY, z) & (Region.NORTH_BLOCKED | Region.TILE_BLOCKED)) == 0) {
                            return true;
                        }
                        return false;
                    }
                    case 2: {
                        if (srcX == destX - 1 && srcY == destY && (World.getMask(srcX, srcY, z) & (Region.EAST_BLOCKED | Region.TILE_BLOCKED)) == 0) {
                            return true;
                        }
                        if (srcX == destX && srcY == destY + 1 && (World.getMask(srcX, srcY, z) & (Region.SOUTH_BLOCKED | Region.TILE_BLOCKED)) == 0) {
                            return true;
                        }
                        if (srcX == destX + 1 && srcY == destY) {
                            return true;
                        }
                        if (srcX == destX && srcY == destY - 1) {
                            return true;
                        }
                        return false;
                    }
                    case 3: {
                        if (srcX == destX - 1 && srcY == destY) {
                            return true;
                        }
                        if (srcX == destX && srcY == destY + 1 && (World.getMask(srcX, srcY, z) & (Region.SOUTH_BLOCKED | Region.TILE_BLOCKED)) == 0) {
                            return true;
                        }
                        if (srcX == destX + 1 && srcY == destY && (World.getMask(srcX, srcY, z) & (Region.WEST_BLOCKED | Region.TILE_BLOCKED)) == 0) {
                            return true;
                        }
                        if (srcX == destX && srcY == destY - 1) {
                            return true;
                        }
                        return false;
                    }
                }
                return false;
                // throw new AssertionError();
            }
            case 9: {
                if (srcX == destX && srcY == destY + 1 && (World.getMask(srcX, srcY, z) & Region.SOUTH_BLOCKED) == 0) {
                    return true;
                }
                if (srcX == destX && srcY == destY - 1 && (World.getMask(srcX, srcY, z) & Region.NORTH_BLOCKED) == 0) {
                    return true;
                }
                if (srcX == destX - 1 && srcY == destY && (World.getMask(srcX, srcY, z) & Region.EAST_BLOCKED) == 0) {
                    return true;
                }
                if (srcX == destX + 1 && srcY == destY && (World.getMask(srcX, srcY, z) & Region.WEST_BLOCKED) == 0) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public static boolean method220(Position position, int destX, int destY, int objectType, int rotation) {
        int srcX = position.getX();
        int srcY = position.getY();
        if (srcX == destX && srcY == destY) {
            return true;
        }
        int z = position.getZ() % 4;
        switch (objectType) {
            case 4:
                //System.out.println("here");
                if (srcX + 1 == destX || srcY + 1 == destY || srcX - 1 == destX || srcY - 1 == destY) {
                    return true;
                }
            case 6:
            case 7: {
                if (objectType == 7) {
                    rotation = (rotation + 2) & 3;
                }
                switch (rotation) {
                    case 0: {
                        if (srcX == destX + 1 && srcY == destY && (World.getMask(srcX, srcY, z) & Region.WEST_BLOCKED) == 0) {
                            return true;
                        }
                        if (srcX == destX && srcY == destY - 1 && (World.getMask(srcX, srcY, z) & Region.NORTH_BLOCKED) == 0) {
                            return true;
                        }
                        return false;
                    }
                    case 1: {
                        if (srcX == destX - 1 && srcY == destY && (World.getMask(srcX, srcY, z) & Region.EAST_BLOCKED) == 0) {
                            return true;
                        }
                        if (srcX == destX && srcY == destY - 1 && (World.getMask(srcX, srcY, z) & Region.NORTH_BLOCKED) == 0) {
                            return true;
                        }
                        return false;
                    }
                    case 2: {
                        if (srcX == destX - 1 && srcY == destY && (World.getMask(srcX, srcY, z) & Region.EAST_BLOCKED) == 0) {
                            return true;
                        }
                        if (srcX == destX && srcY == destY + 1 && (World.getMask(srcX, srcY, z) & Region.SOUTH_BLOCKED) == 0) {
                            return true;
                        }
                        return false;
                    }
                    case 3: {
                        if (srcX == destX + 1 && srcY == destY && (World.getMask(srcX, srcY, z) & Region.WEST_BLOCKED) == 0) {
                            return true;
                        }
                        if (srcX == destX && srcY == destY + 1 && (World.getMask(srcX, srcY, z) & Region.SOUTH_BLOCKED) == 0) {
                            return true;
                        }
                        return false;
                    }
                }
                return false;
                // throw new AssertionError();
            }
            case 8: {
                if (srcX == destX && srcY == destY + 1 && (World.getMask(srcX, srcY, z) & Region.SOUTH_BLOCKED) == 0) {
                    return true;
                }
                if (srcX == destX && srcY == destY - 1 && (World.getMask(srcX, srcY, z) & Region.NORTH_BLOCKED) == 0) {
                    return true;
                }
                if (srcX == destX - 1 && srcY == destY && (World.getMask(srcX, srcY, z) & Region.EAST_BLOCKED) == 0) {
                    return true;
                }
                if (srcX == destX + 1 && srcY == destY && (World.getMask(srcX, srcY, z) & Region.WEST_BLOCKED) == 0) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    static {
        //Region.printMasks(2370, 3133, 3);
    }

    public static boolean newReached(Position position, GameObject o, int minimumX, int minimumY, int maximumX, int maximumY, int mask) {
        int z = position.getZ() % 4;
        int srcX = position.getX();
        int srcY = position.getY();

        if (srcX == minimumX + o.getDefinition().getSizeX() && srcY >= minimumY && srcY <= maximumY &&
                ((World.getMask(srcX, srcY, z) & Region.WEST_BLOCKED) == 0)) {
            return true;
        }
        if (srcX == minimumX - o.getDefinition().getSizeX()  && srcY >= minimumY && srcY <= maximumY &&
                (World.getMask(srcX, srcY, z) & Region.EAST_BLOCKED) == 0) {
            return true;
        }
        if (srcY == minimumY - o.getDefinition().getSizeY()  && srcX >= minimumX && srcX <= maximumX &&
                (World.getMask(srcX, srcY, z) & Region.NORTH_BLOCKED) == 0) {
            return true;
        }
        if (srcY == minimumY + o.getDefinition().getSizeY() && srcX >= minimumX && srcX <= maximumX &&
                (World.getMask(srcX, srcY, z) & Region.SOUTH_BLOCKED) == 0) {
            return true;
        }
        return false;
    }

    public static boolean reached(Position position, int minimumX, int minimumY, int maximumX, int maximumY, int mask) {
        int z = position.getZ() % 4;
        int srcX = position.getX();
        int srcY = position.getY();

        if (srcX >= minimumX && srcX <= maximumX && srcY >= minimumY && srcY <= maximumY) {
            return true;
        }
        if (srcX == minimumX - 1 && srcY >= minimumY && srcY <= maximumY && (World.getMask(srcX, srcY, z) & Region.EAST_BLOCKED) == 0 && (mask & Region.EAST_BLOCKED) == 0) {
            return true;
        }
        if (srcX == maximumX + 1 && srcY >= minimumY && srcY <= maximumY && (World.getMask(srcX, srcY, z) & Region.WEST_BLOCKED) == 0 && (mask & Region.NORTH_BLOCKED) == 0) {
            return true;
        }
        if (srcY == minimumY - 1 && srcX >= minimumX && srcX <= maximumX && (World.getMask(srcX, srcY, z) & Region.NORTH_BLOCKED) == 0 && (mask & Region.NORTH_EAST_BLOCKED) == 0) {
            return true;
        }
        if (srcY == maximumY + 1 && srcX >= minimumX && srcX <= maximumX && (World.getMask(srcX, srcY, z) & Region.SOUTH_BLOCKED) == 0 && (mask & Region.NORTH_WEST_BLOCKED) == 0) {
            return true;
        }

        return false;
    }

    private boolean loaded;
    private final int area;
    private final int groundFile;
    private final int objectFile;
    private int[][][] collisionMasks;
    private List<GameObject> objects;

    public List<GameObject> getObjects() {
        return objects;
    }

    public Region(int area, int groundFile, int objectFile) {
        this.area = area;
        this.groundFile = groundFile;
        this.objectFile = objectFile;
    }

    public  int getMask(int x, int y, int z) {
        return collisionMasks[z][x % 64][y % 64];
    }

    public void flag(int x, int y, int z, int mask) {
        collisionMasks[z][x % 64][y % 64] |= mask;
    }

    public void unflag(int x, int y, int z, int mask) {
        collisionMasks[z][x % 64][y % 64] ^= mask;
    }

    public void remove(GameObject object) {
        objects.remove(object);
    }

    public void add(GameObject object) {
        objects.add(object);
    }

    public GameObject getObject(int id, int z, int x, int y) {
        for (GameObject object : objects) {
            if (id != object.getId()) {
                continue;
            }
            if (z == object.getPosition().getZ() && x == object.getPosition().getX() && y == object.getPosition().getY()) {
                return object;
            }
        }
        return null;
    }

//    public GameObject gateAtPosition(String name, int x, int y) {
//        for (GameObject object : objects) {
//            if(object == null) {
//                continue;
//            }
//            if(object.getName().toLowerCase().equals(name.toLowerCase())) {
//                if(x == object.getPosition().getX() + 1 || x == object.getPosition().getX() - 1 || x == object.getPosition().getX()) {
//                    if(y == object.getPosition().getY() + 1 || y == object.getPosition().getY() - 1) {
//                        return object;
//                    }
//                } else if(x == object.getPosition().getX() + 1 || x == object.getPosition().getX() - 1) {
//                    if(y == object.getPosition().getY() + 1 || y == object.getPosition().getY() - 1 || y == object.getPosition().getY()) {
//                        return object;
//                    }
//                }
//            }
//        }
//        return null;
//    }

    public GameObject getObject(Position position, int type) {
        for (GameObject object : objects) {
            if(object == null) {
                continue;
            }
            if (type != object.getType()) {
                continue;
            }
            if (position.equals(object.getPosition())) {
                return object;
            }
        }
        return null;
    }

    public GameObject getCustomObejct(Position position) {
        for (GameObject object : customObjects) {
            if(object == null) {
                continue;
            }
            if(position.equals(object.getPosition())) {
                return object;
            }
        }
        return null;
    }

    public GameObject getObject(Position position) {
        for (GameObject object : objects) {
            if(object == null) {
                continue;
            }
            if (position.equals(object.getPosition())) {
                return object;
            }
        }
        return null;
    }

    public Object[] getAllObjects(Position position) {
        ArrayList<GameObject> cachedObjects = new ArrayList<>();
        int size = 0;

        for (GameObject object : objects) {
            if (object == null) {
                continue;
            }
            if(position.equals(object.getPosition())) {
                cachedObjects.add(object);
            }
        }

        return cachedObjects.toArray();
    }

    public void load() {
        if (loaded) {
            return;
        }
        loaded = true;
        try {
            this.objects = new LinkedList<>();
            this.collisionMasks = new int[4][64][64];
            byte[] objectDataBytes = GameServer.cache.getFile(4, objectFile);
            byte[] groundDataBytes = GameServer.cache.getFile(4, groundFile);
            if (objectDataBytes == null || objectDataBytes.length == 0 || groundDataBytes == null || groundDataBytes.length == 0) {
                System.err.println("Could not get " + (objectDataBytes == null || objectDataBytes.length == 0 ? ("object file " + objectFile) : ("ground file " + groundFile)));
                return;
            }
            ByteStream objectData = new ByteStream(Container.unpack(objectDataBytes));
            ByteStream groundData = new ByteStream(Container.unpack(groundDataBytes));
            int regionX = (area >> 8) * 64;
            int regionY = (area & 0xFF) * 64;
            byte[][][] someArray = new byte[4][64][64];
            for (int i = 0; i < 4; i++) {
                for (int i2 = 0; i2 < 64; i2++) {
                    for (int i3 = 0; i3 < 64; i3++) {
                        while (true) {
                            int v = groundData.getUnsignedByte();
                            if (v == 0) {
                                break;
                            } else if (v == 1) {
                                groundData.skip();
                                break;
                            } else if (v <= 49) {
                                groundData.skip();
                            } else if (v <= 81) {
                                someArray[i][i2][i3] = (byte) (v - 49);
                            }
                        }
                    }
                }
            }
            for (int i = 0; i < 4; i++) {
                for (int i2 = 0; i2 < 64; i2++) {
                    for (int i3 = 0; i3 < 64; i3++) {
                        if ((someArray[i][i2][i3] & 1) == 1) {
                            int z = i;
                            if ((someArray[1][i2][i3] & 2) == 2) {
                                z--;
                            }
                            if (z >= 0 && z <= 3) {
                                int realX = regionX + i2;
                                int realY = regionY + i3;
                                this.flag(realX, realY, z, Region.BLOCKED_TILE);
                            }
                        }
                    }
                }
            }
            int objectId = -1;
            int incr;
            while ((incr = objectData.getUSmart()) != 0) {
                objectId += incr;
                int location = 0;
                int incr2;
                while ((incr2 = objectData.getUSmart()) != 0) {
                    location += incr2 - 1;
                    int localX = location >> 6 & 0x3f;
                    int localY = location & 0x3f;
                    int height = location >> 12;
                    int info = objectData.getUnsignedByte();
                    int type = info >> 2;
                    int direction = info & 0x3;
                    if (localX < 0 || localX >= 64 || localY < 0 || localY >= 64) {
                        continue;
                    }
                    if ((someArray[1][localX][localY] & 2) == 2) {
                        height--;
                    }
                    if (height >= 0 && height <= 3) {
                        World.addObject(objectId, regionX + localX, regionY + localY, height, type, direction);
                    }
                }
            }

        } catch (Throwable e) {
            System.err.println("Corrupt map: g-" + groundFile + " o-" + objectFile + " loc-(" + ((area >> 8) * 64) + ", " + ((area & 0xFF) * 64) + ")");
            e.printStackTrace();
        }
    }

    public void removeFlag(int x, int y, int z) {
        collisionMasks[z][x % 64][y % 64] = 0;
    }
}