package com.zarador.model;

import com.zarador.GameSettings;
import com.zarador.model.definitions.ItemDefinition;
import com.zarador.world.content.Effigies;
import com.zarador.world.content.skill.impl.summoning.BossPets;
import com.zarador.world.entity.impl.player.Player;

/**
 * Represents an item which is owned by a player.
 *
 * @author relex lawl
 */

public class Item {

	/**
	 * An Item object constructor.
	 *
	 * @param id
	 *            Item id.
	 * @param amount
	 *            Item amount.
	 */
	public Item(int id, int amount) {
		this.id = id;
		this.amount = amount;
	}

	/**
	 * An Item object constructor.
	 *
	 * @param id
	 *            Item id.
	 */
	public Item(int id) {
		this(id, 1);
	}

	/**
	 * The item id.
	 */
	private int id;

	/**
	 * Gets the item's id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the item's id.
	 *
	 * @param id
	 *            New item id.
	 */
	public Item setId(int id) {
		this.id = id;
		return this;
	}

	/**
	 * Amount of the item.
	 */
	private int amount;

	/**
	 * Gets the amount of the item.
	 */
	public int getAmount() {
		return amount;
	}

	private int slot;

	public int getSlot() {
		return this.slot;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}

	/**
	 * Sets the amount of the item.
	 */
	public Item setAmount(int amount) {
		this.amount = amount;
		return this;
	}

	/**
	 * untradeable item costs in wild.
	 * 
	 * @param id
	 * @return
	 */

	public static boolean specialCase(int id) {
		switch (id) {
		// PvP armour & weapons
		case 13889:
		case 13895:
		case 13901:
		case 13907:
		case 13913:
		case 13919:
		case 13925:
		case 13931:
		case 13860:
		case 13863:
		case 13866:
		case 13869:
		case 13934:
		case 13937:
		case 13940:
		case 13943:
		case 13872:
		case 13875:
		case 13878:
		case 13946:
		case 13949:
		case 13952:
		case 13886:
		case 13892:
		case 13898:
		case 13904:
		case 13910:
		case 13916:
		case 13922:
			return true;
		}

		return false;
	}

	public static final int[][] brokenBarrows = { { 4708, 4860 }, { 4710, 4866 }, { 4712, 4872 }, { 4714, 4878 },
			{ 4716, 4884 }, { 4720, 4896 }, { 4718, 4890 }, { 4720, 4896 }, { 4722, 4902 }, { 4732, 4932 },
			{ 4734, 4938 }, { 4736, 4944 }, { 4738, 4950 }, { 4724, 4908 }, { 4726, 4914 }, { 4728, 4920 },
			{ 4730, 4926 }, { 4745, 4956 }, { 4747, 4926 }, { 4749, 4968 }, { 4751, 4994 }, { 4753, 4980 },
			{ 4755, 4986 }, { 4757, 4992 }, { 4759, 4998 } };

	public static int getUntradePrice(int id) {
		switch (id) {
		case 6570:
			return 500_000;

		case 18349:
		case 18351:
		case 18353:
		case 18355:
		case 18357:
			return 2_000_000;

		case 19111:
			return 1_000_000;

		case 8844:
		case 8845:
		case 8846:
		case 8847:
		case 8848:
			return 100_000;

		case 8849:
		case 8850:
			return 150_000;

		case 13889:
		case 13895:
		case 13901:
		case 13907:
		case 13913:
		case 13919:
		case 13925:
		case 13931:
		case 13860:
		case 13863:
		case 13866:
		case 13869:
		case 13934:
		case 13937:
		case 13940:
		case 13943:
		case 13872:
		case 13875:
		case 13878:
		case 13946:
		case 13949:
		case 13952:
		case 13886:
		case 13892:
		case 13898:
		case 13904:
		case 13910:
		case 13916:
		case 13922:
			return 750_000;
		}

		return 0;
	}

	/**
	 * Gets item's definition.
	 */
	public ItemDefinition getDefinition() {
		return ItemDefinition.forId(id);
	}

	public boolean tradeable(Player player) {
		String name = getDefinition().getName().toLowerCase();
		if (name.contains("clue scroll"))
			return false;
		if (name.contains("overload") || name.contains("extreme") || name.contains("renewal"))
			return false;
		if (name.toLowerCase().contains("brawling") || name.toLowerCase().contains("(deg)"))
			return false;
		if (name.toLowerCase().contains("eagle-eye")
				|| name.toLowerCase().contains("farseer ki"))
			return false;
		for (int i : GameSettings.UNTRADEABLE_ITEMS) {
			if(id == 6570 || id == 19111) {
				if(BossPets.hasPet(player, BossPets.BossPet.TZREK_JAD)) {
					return true;
				}
			}
			if (id == i) {
				return false;
			}
		}
		if(player.getDungeoneering().isDungItem(name)) {
			return false;
		}
		if (Effigies.isEffigy(id))
			return false;
		return true;
	}

	public boolean tradeable() {
		String name = getDefinition().getName().toLowerCase();
		if (name.contains("clue scroll"))
			return false;
		if (name.contains("overload") || name.contains("extreme") || name.contains("renewal"))
			return false;
		if (name.toLowerCase().contains("brawling") || name.toLowerCase().contains("(deg)"))
			return false;
		if (name.toLowerCase().contains("eagle-eye")
				|| name.toLowerCase().contains("farseer ki"))
			return false;
		for (int i : GameSettings.UNTRADEABLE_ITEMS) {
			if (id == i) {
				return false;
			}
		}
		if (Effigies.isEffigy(id))
			return false;
		return true;
	}

	public boolean sellable() {
		String name = getDefinition().getName().toLowerCase();
		if (name.contains("clue scroll"))
			return false;
		if (name.contains("overload") || name.contains("extreme"))
			return false;
		if (name.toLowerCase().contains("(deg)") || name.toLowerCase().contains("brawling"))
			return false;
		for (int i : GameSettings.UNSELLABLE_ITEMS) {
			if (id == i)
				return false;
		}
		if (Effigies.isEffigy(id))
			return false;
		return true;
	}

	public static Item getNoted(int id, int amount) {
		int notedItem = id + 1;
		if (ItemDefinition.forId(notedItem).getName().equals(ItemDefinition.forId(id).getName())) {
			return new Item(notedItem, amount);
		}
		return new Item(id, amount);
	}

	public static int getNoted(int id) {
		int noted = id + 1;
		if (id == 11283) {
			noted = 11285;
		}
		if (ItemDefinition.forId(noted).getName().equals(ItemDefinition.forId(id).getName())) {
			return noted;
		}
		return id;
	}

	public static int getUnNoted(int id) {
		int unNoted = id - 1;
		if (id == 11285) {
			unNoted = 11283;
		}
		if (ItemDefinition.forId(unNoted).getName().equals(ItemDefinition.forId(id).getName())) {
			return unNoted;
		}
		return id;
	}

	public static boolean tradeable(Player player, int item) {
		return new Item(item).tradeable(player);
	}

	public static boolean sellable(int item) {
		return new Item(item).sellable();
	}

	/**
	 * Copying the item by making a new item with same values.
	 */
	public Item copy() {
		return new Item(id, amount);
	}

	/**
	 * Increment the amount by 1.
	 */
	public void incrementAmount() {
		if ((amount + 1) > Integer.MAX_VALUE) {
			return;
		}
		amount++;
	}

	/**
	 * Decrement the amount by 1.
	 */
	public void decrementAmount() {
		if ((amount - 1) < 0) {
			return;
		}
		amount--;
	}

	/**
	 * Increment the amount by the specified amount.
	 */
	public void incrementAmountBy(int amount) {
		if ((this.amount + amount) > Integer.MAX_VALUE) {
			this.amount = Integer.MAX_VALUE;
		} else {
			this.amount += amount;
		}
	}

	/**
	 * Decrement the amount by the specified amount.
	 */
	public void decrementAmountBy(int amount) {
		if ((this.amount - amount) < 1) {
			this.amount = 0;
		} else {
			this.amount -= amount;
		}
	}

	/**
	 * ITEM RARITY
	 **/
	public ItemRarity rarity;

	public Item setRarity(ItemRarity rarity) {
		this.rarity = rarity;
		return this;
	}

	@Override
	public String toString() {
		return "Item[" + id + ", " + amount + "]";
	}

}
