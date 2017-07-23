package com.zarador.world.entity;

import com.zarador.GameSettings;
import com.zarador.model.Animation;
import com.zarador.model.GameObject;
import com.zarador.model.Graphic;
import com.zarador.model.Position;
import com.zarador.world.entity.impl.npc.NPC;
import com.zarador.world.entity.impl.player.Player;
import com.zarador.world.entity.impl.player.bot.Bot;

public class Entity {

	/**
	 * The Entity constructor.
	 * 
	 * @param position
	 *            The position the entity is currently in.
	 */
	public Entity(Position position) {
		setPosition(position);
		lastKnownRegion = position;
	}

	/**
	 * The entity's unique index.
	 */
	private int index;

	/**
	 * The entity's tile size.
	 */
	private int size = 1;

	/**
	 * The default position the entity is in.
	 */
	private Position position = GameSettings.DEFAULT_POSITION_VARROCK.copy();

	/**
	 * The last position for the character.
	 */
	private Position lastPosition;

	/**
	 * The entity's first position in current map region.
	 */
	private Position lastKnownRegion;

	/**
	 * Gets the entity's unique index.
	 * 
	 * @return The entity's index.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Sets the entity's index.
	 * 
	 * @param index
	 *            The value the entity's index will contain.
	 * @return The Entity instance.
	 */
	public Entity setIndex(int index) {
		this.index = index;
		return this;
	}

	/**
	 * Gets this entity's first position upon entering their current map region.
	 * 
	 * @return The lastKnownRegion instance.
	 */
	public Position getLastKnownRegion() {
		return lastKnownRegion;
	}

	/**
	 * Sets the entity's current region's position.
	 * 
	 * @param lastKnownRegion
	 *            The position in which the player first entered the current
	 *            region.
	 * @return The Entity instance.
	 */
	public Entity setLastKnownRegion(Position lastKnownRegion) {
		this.lastKnownRegion = lastKnownRegion;
		return this;
	}

	/**
	 * Sets the entity position
	 * 
	 * @param position
	 *            the world position
	 */
	public Entity setPosition(Position position) {
		this.lastPosition = this.position;
		this.position = position;
		return this;
	}

	/**
	 * Gets the entity position.
	 * 
	 * @return the entity's world position
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * Gets the characters last position.
	 * @return The characters last position
     */
	public Position getLastPosition() {
		return lastPosition;
	}

	/**
	 * Sets the last position for the character.
	 * @param lastPosition The last position for the character.
     */
	public void setLastPosition(Position lastPosition) {
		this.lastPosition = lastPosition;
	}

	/**
	 * Performs an animation.
	 * 
	 * @param animation
	 *            The animation to perform.
	 */
	public void performAnimation(Animation animation) {

	}

	/**
	 * Performs a graphic.
	 * 
	 * @param graphic
	 *            The graphic to perform.
	 */
	public void performGraphic(Graphic graphic) {

	}

	/**
	 * gets the entity's tile size.
	 * 
	 * @return The size the entity occupies in the world.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Sets the entity's tile size
	 * 
	 * @return The Entity instance.
	 */
	public Entity setSize(int size) {
		this.size = size;
		return this;
	}

	public boolean isNpc() {
		return this instanceof NPC;
	}

	public boolean isPlayer() {
		return this instanceof Player || this instanceof Bot;
	}

	public boolean isGameObject() {
		return this instanceof GameObject;
	}

}
