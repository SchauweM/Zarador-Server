package com.zarador.world.content;

import com.zarador.model.GroundItem;
import com.zarador.model.Item;
import com.zarador.model.definitions.ItemDefinition;
import com.zarador.util.Misc;
import com.zarador.world.World;
import com.zarador.world.entity.impl.GroundItemManager;
import com.zarador.world.entity.impl.player.Player;

public class Artifacts {

	public static int artifacts[] = { 14876, 14877, 14878, 14879, 14880, 14881, 14882, 14883, 14884, 14885, 14886,
			14887, 14888, 14889, 14890, 14891, 14892 };

	public static enum ArtifactE {

		BROKEN_STATUE(14892, 80_000),
		THIRD_AGE_CARAFE(14891, 100_000),
		BRONZE_DRAGON_CLAW(14890, 150_000),
		ANCIENT_PSALTERY_BRIDGE(14889, 250_000),
		SARADOMIN_AMPHORA(14888, 400_000),
		BANDOS_SCRIMSHAW(14887, 600_000),
		SARADOMIN_CARVIN(14886, 800_000),
		ZAMORAK_MEDALLION(14885, 1_000_000),
		ARMADYL_TOTEM(14884, 1_200_000),
		GUTHIXIAN_BRAZIER(14883, 1_600_000),
		RUBY_CHALLICE(14882, 2_000_000),
		BANDOS_STATUE(14881, 3_000_000),
		SARADOMIN_STATUE(14880, 5_000_000),
		ZAMORAK_STATUE(14879, 8_000_000),
		ARMADYL_STATUE(14878, 10_000_000),
		SEREN_STATUE(14877, 20_000_000),
		ANCIENT_STATUE(14876, 50_000_000);

		private int id;
		private int price;

		public int getId() { return id; }

		public int getPrice() { return price; }

		ArtifactE(int id, int price) {
			this.id = id;
			this.price = price;
		}

	};

	public static void sellArtifacts(Player c) {
		c.getPacketSender().sendInterfaceRemoval();
		boolean artifact = false;
		for (int k = 0; k < artifacts.length; k++) {
			if (c.getInventory().contains(artifacts[k])) {
				artifact = true;
			}
		}
		if (!artifact) {
			c.getPacketSender().sendMessage("You do not have any Artifacts in your inventory to sell.");
			return;
		}
		for (ArtifactE lel : ArtifactE.values()) {
			if(c.getInventory().contains(lel.getId())) {
				c.getInventory().delete(lel.getId(), 1);
				c.getInventory().add(995, lel.getPrice());
				c.getInventory().refreshItems();
			}
		}
		c.getPacketSender().sendMessage("You've sold your artifacts.");
	}

	/*
	 * Artifacts
	 */
	private final static int[] LOW_ARTIFACTS = { 14888, 14889, 14890, 14891, 14892 };
	private final static int[] MED_ARTIFACTS = { 14883, 14884, 14885, 14886 };
	private final static int[] HIGH_ARTIFACTS = { 14878, 14879, 14880, 14881, 14882 };
	private final static int[] EXR_ARTIFACTS = { 14876, 14877 };
	private final static int[] PVP_ARMORS = { 13899, 13893, 13887, 13902, 13896, 13890, 13858, 13861 };

	public static void handleDrops(Player killer, Player death, boolean targetKill) {
		if (Misc.getRandom(100) >= 85 || targetKill)
			GroundItemManager.spawnGroundItem(killer, new GroundItem(new Item(getRandomItem(LOW_ARTIFACTS)),
					death.getPosition().copy(), killer.getUsername(), false, 110, true, 100));
		if (Misc.getRandom(100) >= 90)
			GroundItemManager.spawnGroundItem(killer, new GroundItem(new Item(getRandomItem(MED_ARTIFACTS)),
					death.getPosition().copy(), killer.getUsername(), false, 110, true, 100));
		if (Misc.getRandom(100) >= 97)
			GroundItemManager.spawnGroundItem(killer, new GroundItem(new Item(getRandomItem(HIGH_ARTIFACTS)),
					death.getPosition().copy(), killer.getUsername(), false, 110, true, 100));
		if (Misc.getRandom(100) >= 99)
			GroundItemManager.spawnGroundItem(killer, new GroundItem(new Item(getRandomItem(PVP_ARMORS)),
					death.getPosition().copy(), killer.getUsername(), false, 110, true, 100));
		if (Misc.getRandom(100) >= 99) {
			int rareDrop = getRandomItem(EXR_ARTIFACTS);
			String itemName = Misc.formatText(ItemDefinition.forId(rareDrop).getName());
			GroundItemManager.spawnGroundItem(killer, new GroundItem(new Item(rareDrop), death.getPosition().copy(),
					killer.getUsername(), false, 110, true, 100));
			World.sendMessage("<img=4><col=009966> " + killer.getUsername() + " has just received "
					+ Misc.anOrA(itemName) + " " + itemName + " from Bounty Hunter!");
		}
	}

	/**
	 * Get's a random int from the array specified
	 * 
	 * @param array
	 *            The array specified
	 * @return The random integer
	 */
	public static int getRandomItem(int[] array) {
		return array[Misc.getRandom(array.length - 1)];
	}

}
