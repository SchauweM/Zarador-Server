package com.zarador.model.definitions;

import java.nio.ByteBuffer;

// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

public final class GameObjectDefinition {

	public static GameObjectDefinition class46;

	public boolean rangableObject() {
		int[] rangableObjects = { 3007, 980, 4262, 14437, 14438, 4437, 4439, 3487, 3457 };
		for (int i : rangableObjects) {
			if (i == id) {
				return true;
			}
		}
		if (name != null) {
			final String name1 = name.toLowerCase();
			String[] rangables = { "altar", "pew", "log", "stump", "stool", "sign", "cart", "chest", "rock", "bush",
					"hedge", "chair", "table", "crate", "barrel", "box", "skeleton", "corpse", "vent", "stone",
					"rockslide" };
			for (String i : rangables) {
				if (name1.contains(i)) {
					return true;
				}
			}
		}
		return false;
	}

	private static final int[] osrsObjects = {
			//Cerberus
			21772,

			//Kraken
			316,
			324,
			536,
			538,
			655,
			816,
			1457,
			1459,
			1460,
			2745,
			4909,
			5456,
			5587,
			12299,
			14390,
			14456,
			14457,
			14459,
			14460,
			14468,
			26529,
			26530,
			26534,
			26552,
			26555,

			//Resource area (wilderness)
			10627,
			14437,
			14438,
			14461,
			14464,
			14465,
			14466,
			14467,
			14468,
			14471,
			14497,
			14498,
			14499,
			14665,
			14677,
			14678,
			14679,
			14684,
			14691,
			179,
			197,
			332,
			333,
			334,
			706,
			733,
			909,
			1287,
			1288,
			1643,
			1753,
			1761,
			1791,
			1815,
			2097,
			2741,
			2742,
			2743,
			2745,
			2759,
			2767,
			7389,
			7455,
			7459,
			7489,
			7491,
			7493,
			11726,
			11735,
			11852,
			14389,
			14416,
			14417,
			14418,
			14419,
			14420,
			14421,
			14422,
			14423,
			14424,
			14425,
			14426,
			14427,
			14455,
			14470,
			14500,
			14501,
			14502,
			14508,
			14509,
			14510,
			14511,
			14526,
			14578,
			14584,
			14605,
			14703,
			14704,
			14705,
			14707,
			14708,
			14714,
			14723,
			14724,
			14725,
			14726,
			14729,
			14730,
			14738,
			16390,
			16443,
			16490,
			16553,
			16554,
			26185,
			26300,
			26759,
			26760,
			29056,
			14666,
			14673,
			14675,
			14676,
			14680,
			14685,

			//Chaos fanatic
			26765,

			//Cerberus
			11853,
			14645,
			14674,
			14675,
			17118,
			20196,
			21696,
			21697,
			21698,
			21699,
			21700,
			21701,
			21702,
			21703,
			21704,
			21705,
			21706,
			21707,
			21708,
			21709,
			21710,
			21711,
			21712,
			21713,
			21714,
			21715,
			21716,
			21717,
			21718,
			21748,
			21749,
			21750,
			21751,
			21752,
			21753,
			21754,
			21755,
			21756,
			21757,
			21758,
			21759,
			21760,
			21761,
			21762,
			21763,
			21765,
			21766,
			21767,
			21768,
			21769,
			21770,
			21772,
			21773,
			21775,
			21776,
			21777,
			21779,
			21780,
			21946,
			21947,
			22494,
			22495,
			23100,
			23101,
			26571,
			27059,
	};

/*
	public static boolean removedObject(int id) {
		return id == 2956 || id == 463 || id == 462 || id == 25026 || id == 25020 || id == 25019 || id == 25024
				|| id == 25025 || id == 25016 || id == 10527 || id == 10529 || id == 40257 || id == 296 || id == 300
				|| id == 1747 || id == 7332 || id == 7326 || id == 7325 || id == 7385 || id == 7331 || id == 7385
				|| id == 7320 || id == 7317 || id == 7323 || id == 7354 || id == 1536 || id == 1537 || id == 5126
				|| id == 1551 || id == 1553 || id == 1516 || id == 1519 || id == 1557 || id == 1558 || id == 7126
				|| id == 733 || id == 14233 || id == 14235 || id == 1596 || id == 1597 || id == 14751 || id == 14752
				|| id == 14923 || id == 36844 || id == 30864 || id == 2514 || id == 1805 || id == 15536 || id == 2399
				|| id == 14749 || id == 29315 || id == 29316 || id == 29319 || id == 29320 || id == 29360 || id == 1528
				|| id == 36913 || id == 36915 || id == 15516 || id == 35549 || id == 35551 || id == 26808 || id == 26910
				|| id == 26913 || id == 24381 || id == 15514 || id == 25891 || id == 26082 || id == 26081 || id == 1530
				|| id == 16776 || id == 16778 || id == 28589 || id == 1533 || id == 17089 || id == 1600 || id == 1601
				|| id == 11707 || id == 24376 || id == 24378 || id == 40108 || id == 59 || id == 2069 || id == 36846
				|| id == 57264 || id == 23983 || id == 632 || id == 24265 || id == 24271 || id == 24272 || id == 24274
				|| id == 24273 || id == 24275 || id == 24266 || id == 24267 || id == 24268 || id == 24269 || id == 24270
				|| id == 8985 || id == 307 || id == 23633 || id == 23897 || id == 52843 || id == 4651 || id == 4655
				|| id == 4656 || id == 23987;
	}
	*/

	private static GameObjectDefinition forId667(int id) {
		if (id > totalObjects667 || id > streamIndices667.length - 1) {
			id = 0;
		}
		for (int j = 0; j < 20; j++) {
			if (cache[j].id == id) {
				return cache[j];
			}
		}
		cacheIndex = (cacheIndex + 1) % 20;
		GameObjectDefinition object = cache[cacheIndex];

		dataBuffer667.position(streamIndices667[id]);
		object.id = id;
		object.nullLoader();
		try {
			object.readValues667(dataBuffer667);
		} catch (Exception e) {
			e.printStackTrace();
		}
		/* Removing objects etc */
		for (int ids = 0; ids < removeObjects.length; ids++) {
			if (id == removeObjects[ids]) {
				object.unwalkable = false;
				return object;
			}
		}
		if(id == 41687) {
			object.unwalkable = false;
		}
		return object;
	}

	private static GameObjectDefinition forIdOsrs(int id) {
		if (id > totalObjectsOsrs || id > streamIndicesOsrs.length - 1) {
			id = 0;
		}
		for (int j = 0; j < 20; j++) {
			if (cache[j].id == id) {
				return cache[j];
			}
		}
		cacheIndex = (cacheIndex + 1) % 20;
		GameObjectDefinition object = cache[cacheIndex];
		/* Removing objects etc */
		for (int ids = 0; ids < removeObjects.length; ids++) {
			if (id == removeObjects[ids]) {
				object.unwalkable = false;
				return object;
			}
		}
		dataBufferOsrs.position(streamIndicesOsrs[id]);
		object.id = id;
		object.nullLoader();
		try {
			object.readValues(dataBufferOsrs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return object;
	}

	public static final int[] removeObjects = {26933, 614, 6644, 11726, 733, 5126, 10527, 10529, 12988, 12989, 12987, 15514, 15516, 12986, 28122, 23987, 4651, 4565, 52843, 23897, 23633, 307, 8985, 57264, 23983, 632, 4656,
			24265, 24271, 24272, 24274, 24273, 24275, 24266, 24267, 24268, 24269, 24270, 55349, 2309, 4879};

	public static GameObjectDefinition forId(int i) {

		boolean oldschoolObjects = false;
		for (int is = 0; is < osrsObjects.length; is++) {
			if(osrsObjects[is] == i) {
				oldschoolObjects = true;
			}
		}
		if(oldschoolObjects) {
			return forIdOsrs(i);
		}
		int id = i;
		boolean loadNew = (id == 32159 || id == 32157 || id == 36672
				|| id == 36675 || id == 36692 || id == 34138 || id >= 39260 && id <= 39271 || id == 39229 || id == 39230 || id == 849
				|| id == 39231 || id == 36676 || id == 36692 || id > 11915 && id <= 11929 || id >= 11426 && id <= 11444
				|| id >= 14835 && id <= 14845 || id >= 11391 && id <= 11397 || id >= 12713 && id <= 12715 || id == 8390 || id == 8389 || id == 8388 || id == 8550 || id == 8551 || id == 8552 || id == 8553 || id == 8554 || id == 8555 || id == 8556 || id == 8557 || id == 7847 || id == 7849 || id == 7850 || id == 7579 || id == 8337 || id == 8150 || id == 8151 || id == 8152 || id == 8153 || id == 7848);
		if (i > streamIndices525.length || loadNew) {
			return forId667(i);
		}

		for (int j = 0; j < 20; j++) {
			if (cache[j].id == i) {
				return cache[j];
			}
		}

		cacheIndex = (cacheIndex + 1) % 20;
		class46 = cache[cacheIndex];

		if (i > streamIndices525.length - 1 || i < 0) {
			return null;
		}

		dataBuffer525.position(streamIndices525[i]);
		/* Removing objects etc */
		for (int ids = 0; ids < removeObjects.length; ids++) {
			if (i == removeObjects[ids]) {
				class46.unwalkable = false;
				return class46;
			}
		}
		class46.id = i;
		class46.nullLoader();
		class46.readValues(dataBuffer525);
		return class46;
	}

	public int[] solidObjects = { 1902, 1903, 1904, 1905, 1906, 1907, 1908, 1909, 1910, 1911, 1912, 1536, 1535, 1537,
			1538, 5139, 5140, 5141, 5142, 5143, 5144, 5145, 5146, 5147, 5148, 5149, 5150, 1534, 1533, 1532, 1531, 1530,
			1631, 1624, 733, 1658, 1659, 1631, 1620, 14723, 14724, 14726, 14622, 14625, 14627, 11668, 11667, 14543,
			14749, 14561, 14750, 14752, 14751, 1547, 1548, 1415, 1508, 1506, 1507, 1509, 1510, 1511, 1512, 1513, 1514,
			1515, 1516, 1517, 1518, 1519, 1520, 1521, 1522, 1523, 1524, 1525, 1526, 1527, 1528, 1529, 1505, 1504, 3155,
			3154, 3152, 10748, 9153, 9154, 9473, 1602, 1603, 1601, 1600, 9544, 9563, 9547, 2724, 6966, 6965, 9587, 9588,
			9626, 9627, 9596, 9598, 11712, 11713, 11773, 11776, 11652, 11818, 11716, 11721, 14409, 11715, 11714, 11825,
			11409, 11826, 11819, 14411, 14410, 11719, 11717, 14402, 11828, 11772, 11775, 11686, 12278, 1853, 11611,
			11610, 11609, 11608, 11607, 11561, 11562, 11563, 11564, 11558, 11616, 11617, 11625, 11624, 12990, 12991,
			5634, 1769, 1770, 135, 134, 11536, 11512, 11529, 11513, 11521, 11520, 11519, 11518, 11517, 11516, 11514,
			11509, 11538, 11537, 11470, 11471, 136, 11528, 11529, 11530, 11531, 1854, 1000, 9265, 9264, 1591, 11708,
			11709, 11851 };

	public void setSolid(int type) {
		aBoolean779 = false;
		for (int i = 0; i < solidObjects.length; i++) {
			if (type == solidObjects[i]) {
				unwalkable = true;
				aBoolean779 = true;
				continue;
			}
		}

	}

	public void nullLoader() {
		modelArray = null;
		objectModelType = null;
		name = null;
		description = null;
		modifiedModelColors = null;
		originalModelColors = null;
		tileSizeX = 1;
		tileSizeY = 1;
		unwalkable = true;
		impenetrable = true;
		interactive = false;
		aBoolean762 = false;
		aBoolean769 = false;
		aBoolean764 = false;
		anInt781 = -1;
		anInt775 = 16;
		aByte737 = 0;
		aByte742 = 0;
		actions = null;
		anInt746 = -1;
		anInt758 = -1;
		aBoolean751 = false;
		aBoolean779 = true;
		anInt748 = 128;
		anInt772 = 128;
		anInt740 = 128;
		anInt768 = 0;
		anInt738 = 0;
		anInt745 = 0;
		anInt783 = 0;
		aBoolean736 = false;
		aBoolean766 = false;
		anInt760 = -1;
		anInt774 = -1;
		anInt749 = -1;
		childrenIDs = null;
	}

	private static ByteBuffer dataBuffer525;
	private static ByteBuffer dataBuffer667;
	private static ByteBuffer dataBufferOsrs;

	public static int totalObjects667;
	public static int totalObjectsOsrs;

	public static void init() {
		dataBuffer525 = ByteBuffer.wrap(getBuffer("loc.dat"));
		ByteBuffer idxBuffer525 = ByteBuffer.wrap(getBuffer("loc.idx"));

		dataBuffer667 = ByteBuffer.wrap(getBuffer("667loc.dat"));
		ByteBuffer idxBuffer667 = ByteBuffer.wrap(getBuffer("667loc.idx"));

		dataBufferOsrs = ByteBuffer.wrap(getBuffer("osrsloc.dat"));
		ByteBuffer idxBufferOsrs = ByteBuffer.wrap(getBuffer("osrsloc.idx"));

		int totalObjects525 = idxBuffer525.getShort() & 0xFFFF;
		totalObjects667 = idxBuffer667.getShort() & 0xFFFF;
		totalObjectsOsrs = idxBufferOsrs.getShort() & 0xFFFF;

		streamIndices525 = new int[totalObjects525];
		int i = 2;
		for (int j = 0; j < totalObjects525; j++) {
			streamIndices525[j] = i;
			i += idxBuffer525.getShort() & 0xFFFF;
		}

		streamIndices667 = new int[totalObjects667];

		i = 2;
		for (int j = 0; j < totalObjects667; j++) {
			streamIndices667[j] = i;
			i += idxBuffer667.getShort() & 0xFFFF;
		}

		streamIndicesOsrs = new int[totalObjectsOsrs];

		i = 2;
		for (int j = 0; j < totalObjectsOsrs; j++) {
			streamIndicesOsrs[j] = i;
			i += idxBufferOsrs.getShort() & 0xFFFF;
		}

		cache = new GameObjectDefinition[20];
		for (int k = 0; k < 20; k++) {
			cache[k] = new GameObjectDefinition();
		}

		System.out.println("Loaded " + totalObjects525 + " cache object definitions #525 and " + totalObjects667
				+ " cache object definitions #667");
	}

	public static byte[] getBuffer(String s) {
		try {
			java.io.File f = new java.io.File("./data/clipping/objects/" + s);
			if (!f.exists())
				return null;
			byte[] buffer = new byte[(int) f.length()];
			java.io.DataInputStream dis = new java.io.DataInputStream(new java.io.FileInputStream(f));
			dis.readFully(buffer);
			dis.close();
			return buffer;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getString(ByteBuffer buffer) {
		StringBuilder builder = new StringBuilder();
		char character;
		while ((character = (char) buffer.get()) != 10) {
			builder.append(character);
		}

		return builder.toString();
	}

	private void readValues(ByteBuffer buffer) {
		int i = -1;
		label0: do {
			int opcode;
			do {
				opcode = buffer.get() & 0xFF;
				if (opcode == 0)
					break label0;
				if (opcode == 1) {
					int k = buffer.get() & 0xFF;
					if (k > 0)
						if (modelArray == null || lowMem) {
							objectModelType = new int[k];
							modelArray = new int[k];
							for (int k1 = 0; k1 < k; k1++) {
								modelArray[k1] = buffer.getShort() & 0xFFFF;
								objectModelType[k1] = buffer.get() & 0xFF;
							}
						} else {
							buffer.position(buffer.position() + k * 3);
						}
				} else if (opcode == 2)
					name = getString(buffer);
				else if (opcode == 3)
					description = getString(buffer);
				else if (opcode == 5) {
					int l = buffer.get() & 0xFF;
					if (l > 0)
						if (modelArray == null || lowMem) {
							objectModelType = null;
							modelArray = new int[l];
							for (int l1 = 0; l1 < l; l1++)
								modelArray[l1] = buffer.getShort() & 0xFFFF;
						} else {
							;// buffer.currentOffset += l * 2;
						}
				} else if (opcode == 14)
					tileSizeX = buffer.get() & 0xFF;
				else if (opcode == 15)
					tileSizeY = buffer.get() & 0xFF;
				else if (opcode == 17)
					unwalkable = false;
				else if (opcode == 18)
					impenetrable = false;
				else if (opcode == 19) {
					i = buffer.get() & 0xFF;
					if (i == 1)
						interactive = true;
				} else if (opcode == 21)
					aBoolean762 = true;
				else if (opcode == 22)
					aBoolean769 = false;//
				else if (opcode == 23)
					aBoolean764 = true;
				else if (opcode == 24) {
					anInt781 = buffer.getShort() & 0xFFFF;
					if (anInt781 == 65535)
						anInt781 = -1;
				} else if (opcode == 28)
					anInt775 = buffer.get() & 0xFF;
				else if (opcode == 29)
					aByte737 = buffer.get();
				else if (opcode == 39)
					aByte742 = buffer.get();
				else if (opcode >= 30 && opcode < 39) {
					if (actions == null)
						actions = new String[10];
					actions[opcode - 30] = getString(buffer);
					if (actions[opcode - 30].equalsIgnoreCase("hidden"))
						actions[opcode - 30] = null;
				} else if (opcode == 40) {
					int i1 = buffer.get() & 0xFF;
					modifiedModelColors = new int[i1];
					originalModelColors = new int[i1];
					for (int i2 = 0; i2 < i1; i2++) {
						modifiedModelColors[i2] = buffer.getShort() & 0xFFFF;
						originalModelColors[i2] = buffer.getShort() & 0xFFFF;
					}
				} else if (opcode == 60)
					anInt746 = buffer.getShort() & 0xFFFF;
				else if (opcode == 62)
					aBoolean751 = true;
				else if (opcode == 64)
					aBoolean779 = false;
				else if (opcode == 65)
					anInt748 = buffer.getShort() & 0xFFFF;
				else if (opcode == 66)
					anInt772 = buffer.getShort() & 0xFFFF;
				else if (opcode == 67)
					anInt740 = buffer.getShort() & 0xFFFF;
				else if (opcode == 68)
					anInt758 = buffer.getShort() & 0xFFFF;
				else if (opcode == 69)
					anInt768 = buffer.get() & 0xFF;
				else if (opcode == 70)
					anInt738 = buffer.getShort();
				else if (opcode == 71)
					anInt745 = buffer.getShort();
				else if (opcode == 72)
					anInt783 = buffer.getShort();
				else if (opcode == 73)
					aBoolean736 = true;
				else if (opcode == 74) {
					aBoolean766 = true;
				} else {
					if (opcode != 75)
						continue;
					anInt760 = buffer.get() & 0xFF;
				}
				continue label0;
			} while (opcode != 77);
			anInt774 = buffer.getShort() & 0xFFFF;
			if (anInt774 == 65535)
				anInt774 = -1;
			anInt749 = buffer.getShort() & 0xFFFF;
			if (anInt749 == 65535)
				anInt749 = -1;
			int j1 = buffer.get() & 0xFF;
			childrenIDs = new int[j1 + 1];
			for (int j2 = 0; j2 <= j1; j2++) {
				childrenIDs[j2] = buffer.getShort() & 0xFFFF;
				if (childrenIDs[j2] == 65535)
					childrenIDs[j2] = -1;
			}

		} while (true);
		if (i == -1) {
			interactive = modelArray != null && (objectModelType == null || objectModelType[0] == 10);
			if (actions != null)
				interactive = true;
		}
		if (aBoolean766) {
			unwalkable = false;
			impenetrable = false;
		}
		if (anInt760 == -1)
			anInt760 = unwalkable ? 1 : 0;
	}

	private void readValues667(ByteBuffer buffer) {
		int i = -1;
		label0: do {
			int opcode;
			do {
				opcode = buffer.get() & 0xFF;
				if (opcode == 0)
					break label0;
				if (opcode == 1) {
					int k = buffer.get() & 0xFF;
					if (k > 0)
						if (modelArray == null || lowMem) {
							objectModelType = new int[k];
							modelArray = new int[k];
							for (int k1 = 0; k1 < k; k1++) {
								modelArray[k1] = buffer.getShort() & 0xFFFF;
								objectModelType[k1] = buffer.get() & 0xFF;
							}
						} else {
							buffer.position(buffer.position() + k * 3);
						}
				} else if (opcode == 2)
					name = getString(buffer);
				else if (opcode == 3)
					description = getString(buffer);
				else if (opcode == 5) {
					int l = buffer.get() & 0xFF;
					if (l > 0)
						if (modelArray == null || lowMem) {
							objectModelType = null;
							modelArray = new int[l];
							for (int l1 = 0; l1 < l; l1++)
								modelArray[l1] = buffer.getShort() & 0xFFFF;
						} else {
							;// buffer.offset += l * 2;
						}
				} else if (opcode == 14)
					tileSizeX = buffer.get() & 0xFF;
				else if (opcode == 15)
					tileSizeY = buffer.get() & 0xFF;
				else if (opcode == 17)
					unwalkable = false;
				else if (opcode == 18)
					impenetrable = false;
				else if (opcode == 19) {
					i = buffer.get() & 0xFF;
					if (i == 1)
						interactive = true;
				} else if (opcode == 21) {
					// conformable = true;
				} else if (opcode == 22) {
					// blackModel = false;//
				} else if (opcode == 23)
					aBoolean764 = true;
				else if (opcode == 24) {
					buffer.getShort();
				} else if (opcode == 28)
					buffer.get();
				else if (opcode == 29)
					aByte737 = buffer.get();
				else if (opcode == 39)
					aByte742 = buffer.get();
				else if (opcode >= 30 && opcode < 39) {
					if (actions == null)
						actions = new String[10];
					actions[opcode - 30] = getString(buffer);
					if (actions[opcode - 30].equalsIgnoreCase("hidden")
							|| actions[opcode - 30].equalsIgnoreCase("null"))
						actions[opcode - 30] = null;
				} else if (opcode == 40) {
					int i1 = buffer.get() & 0xFF;
					for (int i2 = 0; i2 < i1; i2++) {
						buffer.getShort();
						buffer.getShort();
					}
				} else if (opcode == 60)
					buffer.getShort();
				else if (opcode == 62)
					aBoolean751 = true;
				else if (opcode == 64)
					aBoolean779 = false;
				else if (opcode == 65)
					anInt748 = buffer.getShort() & 0xFFFF;
				else if (opcode == 66)
					anInt772 = buffer.getShort() & 0xFFFF;
				else if (opcode == 67)
					anInt740 = buffer.getShort() & 0xFFFF;
				else if (opcode == 68)
					buffer.getShort();
				else if (opcode == 69)
					anInt768 = buffer.get() & 0xFF;
				else if (opcode == 70)
					anInt738 = buffer.getShort();
				else if (opcode == 71)
					anInt745 = buffer.getShort();
				else if (opcode == 72)
					anInt783 = buffer.getShort();
				else if (opcode == 73)
					aBoolean736 = true;
				else if (opcode == 74) {
					aBoolean766 = true;
				} else {
					if (opcode != 75)
						continue;
					anInt760 = buffer.get() & 0xFF;
				}
				continue label0;
			} while (opcode != 77);
			anInt774 = buffer.getShort() & 0xFFFF;
			if (anInt774 == 65535)
				anInt774 = -1;
			anInt749 = buffer.getShort() & 0xFFFF;
			if (anInt749 == 65535)
				anInt749 = -1;
			int j1 = buffer.get() & 0xFF;
			childrenIDs = new int[j1 + 1];
			for (int j2 = 0; j2 <= j1; j2++) {
				childrenIDs[j2] = buffer.getShort() & 0xFFFF;
				if (childrenIDs[j2] == 65535)
					childrenIDs[j2] = -1;
			}

		} while (true);
		if (i == -1) {
			interactive = modelArray != null && (objectModelType == null || objectModelType[0] == 10);
			if (actions != null)
				interactive = true;
		}
		if (aBoolean766) {
			unwalkable = false;
			impenetrable = false;
		}
		if (anInt760 == -1)
			anInt760 = unwalkable ? 1 : 0;
	}

	public GameObjectDefinition() {
		id = -1;
	}

	public boolean hasActions() {
		return interactive;
	}

	public boolean hasName() {
		return name != null && name.length() > 1;
	}

	public int xLength() {
		return tileSizeX;
	}

	public int yLength() {
		return tileSizeY;
	}

	public String[] getActions() {
		return actions;
	}

	public boolean aBoolean736;
	public byte aByte737;
	public int anInt738;
	public String name;
	public int anInt740;
	public byte aByte742;
	public int tileSizeX;
	public int anInt745;
	public int anInt746;
	public int[] originalModelColors;
	public int anInt748;
	public int anInt749;
	public boolean aBoolean751;
	public static boolean lowMem;
	public int id;
	public static int[] streamIndices525;
	public static int[] streamIndices667;
	public static int[] streamIndicesOsrs;
	public boolean impenetrable;
	public int anInt758;
	public int childrenIDs[];
	public int anInt760;
	public int tileSizeY;
	public boolean aBoolean762;
	public boolean aBoolean764;
	public boolean aBoolean766;
	public boolean unwalkable;
	public int anInt768;
	public boolean aBoolean769;
	public static int cacheIndex;
	public int anInt772;
	public int[] modelArray;
	public int anInt774;
	public int anInt775;
	public int[] objectModelType;
	public String description;
	public boolean interactive;
	public boolean aBoolean779;
	public int anInt781;
	public static GameObjectDefinition[] cache;
	public int anInt783;
	public int[] modifiedModelColors;
	public String actions[];

	public int actionCount() {
		return interactive ? 1 : 0;
	}

	public String getName() {
		return name;
	}

	public int getSizeX() {
		switch(id) {
			case 28119:
				return 2;
			case 28121:
				return 2;
			//case 14584:
				//return 2;
		}
		return tileSizeX;
	}

	public int getSizeY() {
		switch(id) {
			//case 14584:
				//return 2;
		}
		return tileSizeY;
	}

}
