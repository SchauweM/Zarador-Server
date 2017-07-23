package com.zarador.model;

import com.zarador.model.definitions.GameObjectDefinition;
import com.zarador.world.World;
import com.zarador.world.entity.Entity;
import com.zarador.world.entity.impl.player.Player;

/**
 * This file manages a game object entity on the globe.
 * 
 * @author Relex lawl / iRageQuit2012
 *
 */

public class GameObject extends Entity {

	/**
	 * GameObject constructor to call upon a new game object.
	 * 
	 * @param id
	 *            The new object's id.
	 * @param position
	 *            The new object's position on the globe.
	 */
	public GameObject(int id, Position position) {
		super(position);
		this.id = id;
	}

	/**
	 * GameObject constructor to call upon a new game object.
	 * 
	 * @param id
	 *            The new object's id.
	 * @param position
	 *            The new object's position on the globe.
	 * @param type
	 *            The new object's type.
	 */
	public GameObject(int id, Position position, int type) {
		super(position);
		this.id = id;
		this.type = type;
	}

	/**
	 * GameObject constructor to call upon a new game object.
	 * 
	 * @param id
	 *            The new object's id.
	 * @param position
	 *            The new object's position on the globe.
	 * @param type
	 *            The new object's type.
	 * @param face
	 *            The new object's facing position.
	 */
	public GameObject(int id, Position position, int type, int face) {
		super(position);
		this.id = id;
		this.type = type;
		this.rotation = face;
	}

	/**
	 * GameObject constructor to call upon a new game object.
	 * @param id		The object's id.
	 * @param position	The object's position on the globe.
	 */
	public GameObject(int id, int face, int type, Position position) {
		super(position);
		this.id = id;
		this.type = type;
		this.rotation = face;
	}

	/**
	 * The object's id.
	 */
	private int id;

	/**
	 * Gets the object's id.
	 * 
	 * @return id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * The object's type (default=10).
	 */
	private int type = 10;

	/**
	 * Gets the object's type.
	 * 
	 * @return type.
	 */
	public int getType() {
		return type;
	}

	/**
	 * Sets the object's type.
	 * 
	 * @param type
	 *            New type value to assign.
	 */
	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return GameObjectDefinition.forId(id).getName() != null ? GameObjectDefinition.forId(id).getName() : "";
	}

	/**
	 * Sets the id of the object.
	 * @param id	the new object id.
	 * @return		the game object instance.
	 */
	public GameObject setId(int id) {
		this.id = id;
		return this;
	}

	/**
	 * The object's current direction to face.
	 */
	private int rotation;

	/**
	 * Gets the object's current face direction.
	 * 
	 * @return face.
	 */
	public int getRotation() {
		return rotation;
	}

	/**
	 * Checks if this game object has an action to be performed
	 * upon being clicked.
	 * @param player	the player clicking the game object menu.
	 * @param option	the option being clicked.
	 * @return			{@value true} if game object has an action for said option.
	 */
	public boolean hasMenuAction(Player player, int option) {
		return false;
	}

	/**
	 * Sets the object's face direction.
	 * 
	 * @param
	 *            Face value to which object will face.
	 */
	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	/**
	 * Value that handles the object's 'Picks'
	 */
	private int picked;

	/**
	 * Value that handles the object's 'Cuts'
	 */
	private int cut;

	/**
	 * Gets the object's definition.
	 * 
	 * @return definition.
	 */
	public GameObjectDefinition getDefinition() {
		return GameObjectDefinition.forId(id);
	}

	@Override
	public void performAnimation(Animation animation) {
		for (Player player : World.getPlayers()) {
			if (player == null)
				continue;
			if (player.getPosition().isWithinDistance(getPosition()))
				player.getPacketSender().sendObjectAnimation(this, animation);
		}
	}

	@Override
	public void performGraphic(Graphic graphic) {
		for (Player player : World.getPlayers()) {
			if (player == null)
				continue;
			if (player.getPosition().isWithinDistance(getPosition()))
				player.getPacketSender().sendGraphic(graphic, getPosition());
		}
	}

	@Override
	public int getSize() {
		GameObjectDefinition definition = getDefinition();
		if (definition == null)
			return 1;

		switch (id) {
			case 2515:
				return 1;
			case 38660:
			case 410:
			case 2320:
				return 2;
			case 2282:
				return 1;
			case 1767:
				return 9;
			case 5013:
				return 1;
			case 28119:
				return 3;
			case 28121:
				return 1;
			case 62:
				return 2;
			case 2:
				return 3;
			case 2879:
			case 2878:
				return 3;
			case 1733:
				return 3;

		}

		if (definition.getSizeX() == 1)
			return definition.getSizeY();
		else if (definition.getSizeX() > 1 && definition.getSizeY() == 1)
			return definition.getSizeX();
		else
			return definition.getSizeY() + definition.getSizeX();

	}

	public int getPickAmount() {
		return this.picked;
	}

	public void setPickAmount(int amount) {
		this.picked = amount;
	}

	public void incrementPickAmount() {
		this.picked++;
	}

	public int getCutAmount() {
		return this.cut;
	}

	public void setCutAmount(int amountcut) {
		this.cut = amountcut;
	}

	public void incrementCutAmount() {
		this.cut++;
	}

	public int getSizeX() {
		return GameObjectDefinition.forId(id).getSizeX();
	}

	public int getSizeY() {
		return GameObjectDefinition.forId(id).getSizeY();
	}

	public int getLengthX() {
		if (rotation == 1 || rotation == 3) {
			return GameObjectDefinition.forId(id).getSizeY();
		}
		return GameObjectDefinition.forId(id).getSizeX();
	}

	public int getLengthY() {
		if (rotation == 1 || rotation == 3) {
			return GameObjectDefinition.forId(id).getSizeX();
		}
		return GameObjectDefinition.forId(id).getSizeY();
	}
}
