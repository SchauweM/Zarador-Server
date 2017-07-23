package com.zarador.model.definitions;

import java.util.Arrays;
import java.util.logging.Level;

import com.zarador.GameServer;
import com.zarador.cache.Archive;
import com.zarador.cache.ByteBuffer;

public final class CacheObjectDefinition {
	private static int objectCount;
	private static CacheObjectDefinition[] definitions;

	public static int objectCount() {
		return objectCount;
	}

	public static CacheObjectDefinition[] getDefinitions() {
		return definitions;
	}

	public static CacheObjectDefinition definition(int i) {
		if(i <= -1) {
			return null;
		}
		if (i >= definitions.length) {
			i = 0;
		}
		return definitions[i];
	}
	
	
	public static void load(Archive archive) {
		byte[] idxBytes = archive.getNamedFile("loc.idx").data();
		ByteBuffer dataBuffer = new ByteBuffer(archive.getNamedFile("loc.dat").data());
		ByteBuffer indexBuffer = new ByteBuffer(idxBytes);
		int objectCount578 = indexBuffer.getUnsignedShort();
		definitions = new CacheObjectDefinition[objectCount578];
		int offset = 2;
		for (int j = 0; j < objectCount578; j++) {
			definitions[j] = new CacheObjectDefinition(j);
			dataBuffer.position = offset;
			definitions[j].readValues(dataBuffer);
            setUnwalkable(j);
			offset += indexBuffer.getUnsignedShort();
		}
		idxBytes = archive.getNamedFile("667loc.idx").data();
		dataBuffer = new ByteBuffer(archive.getNamedFile("667loc.dat").data());
		indexBuffer = new ByteBuffer(idxBytes);
		objectCount = indexBuffer.getUnsignedShort();
		definitions = Arrays.copyOf(definitions, objectCount);
		offset = 2;
		for (int j = 0; j < objectCount; j++) {
			if (j >= objectCount578) {
				definitions[j] = new CacheObjectDefinition(j);
				dataBuffer.position = offset;
				definitions[j].readValues(dataBuffer);
                setUnwalkable(j);
			}
			offset += indexBuffer.getUnsignedShort();
		}
	}

    /**
     * This is to shorten the amount of lines in the class.
     * @param id
     */
    private static void setUnwalkable(int id) {
        switch (id) {
            case 26972://edgeville bank booth
            case 2213://draynor bank booth
            case 11758://falador bank booth
            case 11402://varrock bank booth
            case 25808://camelot bank booth
            case 34752://ardougne bank booth
			case 36_786://lumbridge.
			case 35_647://alkarad bank
            case 2214:
            case 16700://lunar bank booth
            case 2215:
                // case 11402:
                definitions[id].unwalkable = true;
                break;
        }
    }

	private String name = null;
	public byte[] description = null;
	private int tileSizeX = 1;
	private int tileSizeY = 1;
	private final int id;
	private boolean projectileBlocked = true;
	public int[] anIntArray759 = null;
	private int anInt760 = -1;
	private boolean unwalkable = true;
	public int[] modelArray = null;
	public int[] anIntArray776 = null;
	private boolean interactable = false;
	public String[] actions = null;
	public int entryMasks;

	public CacheObjectDefinition(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getLengthX() {
		return tileSizeX;
	}

	public int getLengthY() {
		return tileSizeY;
	}

	public boolean isUnwalkable() {
		return unwalkable;
	}

	public boolean isProjectileBlocked() {
		return projectileBlocked;
	}

	public boolean isInteractable() {
		return interactable;
	}

	private void skip(ByteBuffer buffer) {
		int count = buffer.getUnsignedByte();
		for (int i = 0; i != count; ++i) {
			++buffer.position;
			int childCount = buffer.getUnsignedByte();
			buffer.position += childCount * 2;
		}
	}

	private void readValues(ByteBuffer buffer) {
		boolean aBoolean766 = false;
		int prevOpcode = -1;
		int i = -1;
		while (true) {
			int opcode = buffer.getUnsignedByte();
			if (opcode == 0) {
				break;
			}
			if (opcode == 1) {
				int k = buffer.getUnsignedByte();
				if (k > 0) {
					if (modelArray == null) {
						anIntArray776 = new int[k];
						modelArray = new int[k];
						for (int k1 = 0; k1 < k; k1++) {
							modelArray[k1] = buffer.getUnsignedShort();
							anIntArray776[k1] = buffer.getUnsignedByte();
						}
					} else {
						buffer.position += k * 3;
					}
				}
			} else if (opcode == 2) {
				name = buffer.getLine();
			} else if (opcode == 3) {
				description = buffer.getLineBytes();
			} else if (opcode == 5) {
				int l = buffer.getUnsignedByte();
				if (l > 0) {
					if (modelArray == null) {
						anIntArray776 = null;
						modelArray = new int[l];
						for (int l1 = 0; l1 < l; l1++) {
							modelArray[l1] = buffer.getUnsignedShort();
						}
					} else {
						buffer.position += l * 2;
					}
				}
			} else if (opcode == 14) {
				tileSizeX = buffer.getUnsignedByte();
			} else if (opcode == 15) {
				tileSizeY = buffer.getUnsignedByte();
			} else if (opcode == 17) {
				unwalkable = false;
			} else if (opcode == 18) {
				projectileBlocked = false;
			} else if (opcode == 19) {
				i = buffer.getUnsignedByte();
				if (i == 1 && name != null) {
					interactable = true;
				}
			} else if (opcode == 21) {
					/*conformable = true;*/
			} else if (opcode == 22) {
					/*blendsWithObject = true;*/
			} else if (opcode == 23) {
					/*aBoolean764 = true;*/
			} else if (opcode == 24) {
					/*animationId = */buffer.getUnsignedShort();
					/*if (animationId == 65535) {
						animationId = -1;
					}*/
			} else if (opcode == 28) {
					/*renderOffset = */buffer.getUnsignedByte();
			} else if (opcode == 29) {
					/*modelBrightness = */buffer.getByte();
			} else if (opcode == 39) {
					/*modelShadowing = */buffer.getByte();
			} else if (opcode >= 30 && opcode < 39) {
				if (actions == null) {
					actions = new String[10];
				}
				actions[opcode - 30] = buffer.getLine();
				if (actions[opcode - 30].equalsIgnoreCase("hidden")) {
					actions[opcode - 30] = null;
				}
			} else if (opcode == 40) {
				int i1 = buffer.getUnsignedByte();
					/*newColors = new int[i1];
					oldColors = new int[i1];*/
				for (int i2 = 0; i2 < i1; i2++) {
						/*newColors[i2] = */buffer.getUnsignedShort();
						/*oldColors[i2] = */buffer.getUnsignedShort();
				}
			} else if (opcode == 60) {
					/*mapFunctionId = */buffer.getUnsignedShort();
			} else if (opcode == 62) {
					/*mirrorModel = true;*/
			} else if (opcode == 64) {
					/*isSolid = false;*/
			} else if (opcode == 65) {
					/*modelScaleX = */buffer.getUnsignedShort();
			} else if (opcode == 66) {
					/*modelScaleY = */buffer.getUnsignedShort();
			} else if (opcode == 67) {
					/*modelScaleZ = */buffer.getUnsignedShort();
			} else if (opcode == 68) {
					/*mapSceneId = */buffer.getUnsignedShort();
			} else if (opcode == 69) {
					entryMasks = buffer.getUnsignedByte();
			} else if (opcode == 70) {
					/*vertexOffsetX = */buffer.getShort();
			} else if (opcode == 71) {
					/*vertexOffsetZ = */buffer.getShort();
			} else if (opcode == 72) {
					/*vertexOffsetY = */buffer.getShort();
			} else if (opcode == 73) {
					/*aBoolean736 = true;*/
			} else if (opcode == 74) {
				aBoolean766 = true;
			} else if (opcode == 75) {
				anInt760 = buffer.getUnsignedByte();
			} else if (opcode == 77) {
			/*anInt774 = */buffer.getUnsignedShort();
			/*if (anInt774 == 65535) {
				anInt774 = -1;
			}*/
			/*anInt749 = */buffer.getUnsignedShort();
			/*if (anInt749 == 65535) {
				anInt749 = -1;
			}*/
				int j1 = buffer.getUnsignedByte();
			/*childrenIDs = new int[j1 + 1];*/
				for (int j2 = 0; j2 <= j1; j2++) {
				/*childrenIDs[j2] = */buffer.getUnsignedShort();
				/*if (childrenIDs[j2] == 65535) {
					childrenIDs[j2] = -1;
				}*/
				}
			}
			prevOpcode = opcode;
		}
		if (i == -1) {
			interactable = modelArray != null && (anIntArray776 == null || anIntArray776[0] == 10);
			if (actions != null) {
				interactable = true;
			}
			if (name == null || name.equalsIgnoreCase("null")) {
				interactable = false;
			}
		}
		if (aBoolean766) {
			unwalkable = false;
			projectileBlocked = false;
		}
		if (anInt760 == -1) {
			anInt760 = unwalkable ? 1 : 0;
		}
	}

	public static String getName(int id2) {
		if(definitions[id2] == null || definitions[id2].name == null) {
			return "null";
		}
		return definitions[id2].name.toLowerCase();
	}
}
