package com.zarador.model.definitions;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zarador.model.container.impl.Equipment;
import com.zarador.util.JsonLoader;
import com.zarador.world.content.skill.impl.dungeoneering.DungItems;
import com.zarador.world.content.skill.impl.dungeoneering.Dungeoneering;

/**
 * This file manages every item definition, which includes their name,
 * description, value, skill requirements, etc.
 *
 * @author relex lawl
 */

public class ItemDefinition {

	/**
	 * The max amount of items that will be loaded.
	 */
	private static final int MAX_AMOUNT_OF_ITEMS = 22694;

	/**
	 * ItemDefinition array containing all items' definition values.
	 */
	private static ItemDefinition[] definitions;

	/**
	 * Loading all item definitions
	 */
	public static JsonLoader init() {
		definitions = new ItemDefinition[MAX_AMOUNT_OF_ITEMS];
		return new JsonLoader() {

			@Override
			public void load(JsonObject reader, Gson builder) {
				try {
					int id = reader.get("id").getAsInt();
					ItemDefinition definition = new ItemDefinition();
					definition.charges = reader.get("charges").getAsInt();
					definition.name = reader.get("name").getAsString();
					definition.description = reader.get("description").getAsString();
					definition.stackable = reader.get("stackable").getAsBoolean();
					definition.value = reader.get("value").getAsInt();
					definition.highAlch = reader.get("highAlch").getAsInt();
					definition.lowAlch = reader.get("lowAlch").getAsInt();
					definition.noted = reader.get("noted").getAsBoolean();
					definition.isTwoHanded = reader.get("isTwoHanded").getAsBoolean();
					definition.has_charges = reader.get("has_charges").getAsBoolean();
					definition.weapon = reader.get("weapon").getAsBoolean();
					definition.equipmentType = EquipmentType.valueOf(reader.get("equipmentType").getAsString());

					if (reader.has("bonus")) {
						JsonArray bonus = reader.get("bonus").getAsJsonArray();
						for (int index = 0; index < definition.bonus.length; index++) {
							definition.bonus[index] = bonus.get(index).getAsDouble();
						}
					}

					if (reader.has("requirement")) {
						JsonArray req = reader.get("requirement").getAsJsonArray();
						for (int index = 0; index < definition.requirement.length; index++) {
							definition.requirement[index] = req.get(index).getAsInt();
						}
						//Too many dung items to add manually...
						if(Dungeoneering.isDungItem(definition.name)) {
							DungItems type = DungItems.NOVITE;
							for(DungItems dungItems: DungItems.values()) {
								if(definition.name.contains(dungItems.getName())) {
									type = dungItems;
								}
							}
							if(definition.isWeapon()) {
								if(definition.getName().toLowerCase().contains("maul")) {
									definition.requirement[2] = type.getRequirement();
								} else {
									definition.requirement[0] = type.getRequirement();
								}
							} else {
								definition.requirement[1] = type.getRequirement();
							}
						}
					}

					if (reader.has("actions")) {
						JsonArray actions = reader.get("actions").getAsJsonArray();
						for (int index = 0; index < definition.actions.length; index++) {
							definition.actions[index] = actions.get(index).getAsString();
						}
					}

					definitions[id] = definition;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public String filePath() {
				return "data/def/json/item_definitions.json";
			}
		};
	}

	public static ItemDefinition[] getDefinitions() {
		return definitions;
	}

	/**
	 * Gets the item definition correspondent to the id.
	 *
	 * @param id
	 *            The id of the item to fetch definition for.
	 * @return definitions[id].
	 */
	public static ItemDefinition forId(int id) {
		if (id < 0 || id > definitions.length) {
			if (id != -1) {
				// System.out.println("Definition for id: " + id + " is out of
				// bounds.");
			}
			return new ItemDefinition();
		}
		if (definitions[id] == null) {
			// if (id != -1)
			// System.out.println("Definition for id: " + id + " does not
			// exist.");
			return new ItemDefinition();
		}
		return definitions[id];
	}

	/**
	 * Gets the max amount of items that will be loaded in Niobe.
	 *
	 * @return The maximum amount of item definitions loaded.
	 */
	public static int getMaxAmountOfItems() {
		return MAX_AMOUNT_OF_ITEMS;
	}

	/**
	 * The id of the item.
	 */
	private int id = 0;

	/**
	 * The charges the item can hold @ custom.
	 */
	private int charges = 0;

	/**
	 * Gets the item's id.
	 *
	 * @return id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the amount of charges held in the item @ custom.
	 *
	 * @return charges.
	 */
	public int getCharges() {
		return charges;
	}

	/**
	 * The name of the item.
	 */
	public String name = "Unarmed";

	/**
	 * Gets the item's name.
	 *
	 * @return name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * The item's description.
	 */
	private String description = "Null";

	/**
	 * Gets the item's description.
	 *
	 * @return description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Flag to check if item is stackable.
	 */
	private boolean stackable;

	/**
	 * Checks if the item is stackable.
	 *
	 * @return stackable.
	 */
	public boolean isStackable() {
		if (noted)
			return true;
		return stackable;
	}

	/**
	 * The item's shop value.
	 */
	private int value;

	/**
	 * The item's high alch value.
	 */
	private int highAlch;

	/**
	 * The item's low alch value.
	 */
	private int lowAlch;

	/**
	 * Gets the item's shop value.
	 *
	 * @return value.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Gets the item's high alch value.
	 *
	 * @return highAlch.
	 */
	public int getHighAlchValue() {
		return highAlch;
	}

	/**
	 * Gets the item's high alch value.
	 *
	 * @return highAlch.
	 */
	public int getLowAlchValue() {
		return lowAlch;
	}

	public void setHighAlchValue(int value) {
		this.highAlch = value;
	}

	public void setLowAlchValue(int value) {
		this.lowAlch = value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * Gets the item's equipment slot index.
	 *
	 * @return equipmentSlot.
	 */
	public int getEquipmentSlot() {
		return equipmentType.slot;
	}

	/**
	 * Flag that checks if item is noted.
	 */
	private boolean noted;

	/**
	 * Flag that checks if item has custom charges.
	 */
	private boolean has_charges;

	/**
	 * Checks if item is noted.
	 *
	 * @return noted.
	 */
	public boolean isNoted() {
		return noted;
	}

	/**
	 * Checks if item has charges.
	 *
	 * @return noted.
	 */
	public boolean hasCharges() {
		return has_charges;
	}

	private boolean isTwoHanded;

	/**
	 * Checks if item is two-handed
	 */
	public boolean isTwoHanded() {
		return isTwoHanded;
	}

	private boolean weapon;

	public boolean isWeapon() {
		return weapon;
	}

	private EquipmentType equipmentType = EquipmentType.WEAPON;

	public EquipmentType getEquipmentType() {
		return equipmentType;
	}

	/**
	 * Checks if item is full body.
	 */
	public boolean isFullBody() {
		return equipmentType.equals(EquipmentType.PLATEBODY);
	}

	/**
	 * Checks if item is full helm.
	 */
	public boolean isFullHelm() {
		return equipmentType.equals(EquipmentType.FULL_HELMET);
	}

	private double[] bonus = new double[18];

	public double[] getBonus() {
		return bonus;
	}

	private int[] requirement = new int[25];

	public int[] getRequirement() {
		return requirement;
	}

	private String[] actions = new String[5];

	public String[] getActions() {
		return actions;
	}

	public void setAction(int index, String action) {
		actions[index] = action;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAction(int index) {
		return actions[index];
	}

	public boolean isWearable() {
		String action = Strings.nullToEmpty(getAction(1)).toLowerCase();
		return action.equals("wear") || action.equals("wield") || action.equals("equip") || action.equals("hold")
				|| action.equals("ride");
	}

	private enum EquipmentType {
		HAT(Equipment.HEAD_SLOT),
		CAPE(Equipment.CAPE_SLOT),
		SHIELD(Equipment.SHIELD_SLOT),
		GLOVES(Equipment.HANDS_SLOT),
		BOOTS(Equipment.FEET_SLOT),
		AMULET(Equipment.AMULET_SLOT),
		RING(Equipment.RING_SLOT),
		ARROWS(Equipment.AMMUNITION_SLOT),
		FULL_MASK(Equipment.HEAD_SLOT),
		FULL_HELMET(Equipment.HEAD_SLOT),
		BODY(Equipment.BODY_SLOT),
		PLATEBODY(Equipment.BODY_SLOT),
		LEGS(Equipment.LEG_SLOT),
		WEAPON(Equipment.WEAPON_SLOT);

		EquipmentType(int slot) {
			this.slot = slot;
		}

		private int slot;
	}

	@Override
	public String toString() {
		return "[ItemDefinition(" + id + ")] - Name: " + name + "; equipment slot: " + getEquipmentSlot() + "; value: "
				+ value + "; stackable ? " + Boolean.toString(stackable) + "; noted ? " + Boolean.toString(noted)
				+ "; 2h ? " + isTwoHanded;
	}

}
