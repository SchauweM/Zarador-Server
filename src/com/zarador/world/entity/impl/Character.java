package com.zarador.world.entity.impl;

import java.util.HashMap;
import java.util.Map;

import com.zarador.engine.task.Task;
import com.zarador.engine.task.TaskManager;
import com.zarador.model.*;
import com.zarador.model.Locations.Location;
import com.zarador.model.movement.WalkingQueue;
import com.zarador.util.Stopwatch;
import com.zarador.world.content.combat.CombatBuilder;
import com.zarador.world.content.combat.CombatType;
import com.zarador.world.content.combat.magic.CombatSpell;
import com.zarador.world.content.combat.strategy.CombatStrategy;
import com.zarador.world.entity.Entity;
import com.zarador.world.entity.impl.player.Player;

/**
 * A player or NPC
 * 
 * @author Gabriel Hannason
 */

public abstract class Character extends Entity {

	public Character(Position position) {
		super(position);
		location = Location.getLocation(this);
	}

	/*
	 * Fields
	 */

	/*** STRINGS ***/
	private String forcedChat;

	/*** LONGS **/

	/*** INSTANCES ***/
	private Direction direction, walkingDirection = Direction.NONE, runningDirection = Direction.NONE,
			lastDirection = Direction.NONE;
	private CombatBuilder combatBuilder = new CombatBuilder(this);
	private Stopwatch lastCombat = new Stopwatch();
	private UpdateFlag updateFlag = new UpdateFlag();
	private Location location;
	private Position positionToFace;
	private Animation animation;
	private Graphic graphic;
	private Entity interactingEntity;
	private CombatType lastCombatType = CombatType.MELEE;
	public Position singlePlayerPositionFacing;
	private CombatSpell currentlyCasting;
	private Hit primaryHit;
	private Hit secondaryHit;
	private RegionInstance regionInstance;
	private boolean isFreeze;

	/*** INTS ***/
	private int npcTransformationId;
	private int poisonDamage;
	private int venomDamage;
	private int freezeDelay;

	/*** BOOLEANS ***/
	private boolean[] prayerActive = new boolean[30], curseActive = new boolean[20];
	private final HashMap<String, Object> attributes = new HashMap<>();
	private boolean registered;
	private boolean teleporting;
	private boolean resetMovementQueue;
	private boolean needsPlacement;

	/*** ABSTRACT METHODS ***/
	public abstract Character setConstitution(int constitution);

	public abstract CombatStrategy determineStrategy();

	public abstract void appendDeath();

	public abstract void heal(int damage);

	public abstract void poisonVictim(Character victim, CombatType type);

	public abstract void venomVictim(Character victim, CombatType type);

	public abstract int getConstitution();

	public abstract int getBaseAttack(CombatType type);

	public abstract int getBaseDefence(CombatType type);

	public abstract int getAttackSpeed();

	/*
	 * Getters and setters Also contains methods.
	 */

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Graphic getGraphic() {
		return graphic;
	}

	public void setFreeze(boolean freeze) {
		this.isFreeze = freeze;
	}

	public boolean isFreeze() {
		return isFreeze;
	}

	public Character setGraphic(Graphic graphic) {
		this.graphic = graphic;
		getUpdateFlag().flag(Flag.GRAPHIC);
		return this;
	}

	public Animation getAnimation() {
		return animation;
	}

	public Character setAnimation(Animation animation) {
		this.animation = animation;
		getUpdateFlag().flag(Flag.ANIMATION);
		return this;
	}

	public CombatType getLastCombatType() {
		return lastCombatType;
	}

	public void setLastCombatType(CombatType lastCombatType) {
		this.lastCombatType = lastCombatType;
	}

	/**
	 * Deals one damage to this entity.
	 * 
	 * @param hit
	 *            the damage to be dealt.
	 */
	public void dealDamage(Character attacker, Hit hit) {
		if (attacker != null) {
			getCombatBuilder().addDamage(attacker, hit.getDamage());
		}
		if (getUpdateFlag().flagged(Flag.SINGLE_HIT)) {
			dealSecondaryDamage(hit);
			return;
		}
		if (getConstitution() <= 0)
			return;
		getUpdateFlag().flag(Flag.SINGLE_HIT);
		primaryHit = decrementHealth(hit);

	}

	public Hit decrementHealth(Hit hit) {
		if (getConstitution() <= 0)
			return hit;
		if (hit.getDamage() > getConstitution())
			hit.setDamage(getConstitution());
		if (hit.getDamage() < 0)
			hit.setDamage(0);
		int outcome = getConstitution() - hit.getDamage();
		if (outcome < 0)
			outcome = 0;
		setConstitution(outcome);
		return hit;
	}

	/**
	 * Deal secondary damage to this entity.
	 * 
	 * @param hit
	 *            the damage to be dealt.
	 */
	private void dealSecondaryDamage(Hit hit) {
		secondaryHit = decrementHealth(hit);
		getUpdateFlag().flag(Flag.DOUBLE_HIT);
	}

	/**
	 * Deals two damage splats to this entity.
	 * 
	 * @param hit
	 *            the first hit.
	 * @param secondHit
	 *            the second hit.
	 */
	public void dealDoubleDamage(Character attacker, Hit hit, Hit secondHit) {
		dealDamage(attacker, hit);
		dealSecondaryDamage(secondHit);
	}

	/**
	 * Deals three damage splats to this entity.
	 * 
	 * @param hit
	 *            the first hit.
	 * @param secondHit
	 *            the second hit.
	 * @param thirdHit
	 *            the third hit.
	 */
	public void dealTripleDamage(Character attacker, Hit hit, Hit secondHit, final Hit thirdHit) {
		dealDoubleDamage(attacker, hit, secondHit);

		TaskManager.submit(new Task(1, this, false) {
			@Override
			public void execute() {
				if (!registered) {
					this.stop();
					return;
				}
				dealDamage(attacker, thirdHit);
				this.stop();
			}
		});
	}

	/**
	 * Deals four damage splats to this entity.
	 * 
	 * @param hit
	 *            the first hit.
	 * @param secondHit
	 *            the second hit.
	 * @param thirdHit
	 *            the third hit.
	 * @param fourthHit
	 *            the fourth hit.
	 */
	public void dealQuadrupleDamage(Character attacker, Hit hit, Hit secondHit, final Hit thirdHit, final Hit fourthHit) {
		dealDoubleDamage(attacker, hit, secondHit);

		TaskManager.submit(new Task(1, this, false) {
			@Override
			public void execute() {
				if (!registered) {
					this.stop();
					return;
				}
				dealDoubleDamage(attacker, thirdHit, fourthHit);
				this.stop();
			}
		});
	}

	/**
	 * Get the primary hit for this entity.
	 * 
	 * @return the primaryHit.
	 */
	public Hit getPrimaryHit() {
		return primaryHit;
	}

	/**
	 * Get the secondary hit for this entity.
	 * 
	 * @return the secondaryHit.
	 */
	public Hit getSecondaryHit() {
		return secondaryHit;
	}

	/**
	 * Prepares to cast the argued spell on the argued victim.
	 * 
	 * @param spell
	 *            the spell to cast.
	 * @param victim
	 *            the victim to cast the spell on.
	 */
	public void prepareSpell(CombatSpell spell, Character victim) {
		currentlyCasting = spell;
		currentlyCasting.startCast(this, victim);
	}

	/**
	 * Gets if this entity is registered.
	 * 
	 * @return the unregistered.
	 */
	public boolean isRegistered() {
		return registered;
	}

	/**
	 * Sets if this entity is registered,
	 *
	 */
	public void setRegistered(boolean registered) {
		this.registered = registered;
	}

	/**
	 * Gets the combat session.
	 * 
	 * @return the combat session.
	 */
	public CombatBuilder getCombatBuilder() {
		return combatBuilder;
	}

	/**
	 * @return the lastCombat
	 */
	public Stopwatch getLastCombat() {
		return lastCombat;
	}

	public int getAndDecrementPoisonDamage() {
		return poisonDamage -= 15;
	}

	public int getAndDecrementVenomDamage() {
		if (venomDamage == 200) {
			venomDamage = 200;
		} else {
			venomDamage += 20;
		}
		return venomDamage;
	}

	public int getPoisonDamage() {
		return poisonDamage;
	}

	public int getVenomDamage() {
		return venomDamage;
	}

	public void setPoisonDamage(int poisonDamage) {
		this.poisonDamage = poisonDamage;
	}

	public void setVenomDamage(int abc) {
		this.venomDamage = abc;
	}

	public boolean isPoisoned() {
		if (poisonDamage < 0)
			poisonDamage = 0;

		if (poisonDamage != 0) {
			if (isPlayer()) {
				((Player) this).getPacketSender().sendConstitutionOrbPoison(true);
			}
		} else {
			if (isPlayer()) {
				((Player) this).getPacketSender().sendConstitutionOrbPoison(false);
			}
		}
		return poisonDamage != 0;
	}

	public boolean isVenomed() {
		if (venomDamage < 0)
			venomDamage = 0;

		if (venomDamage != 0) {
			if (isPlayer()) {
				((Player) this).getPacketSender().sendConstitutionOrbVenom(true);
			}
		} else {
			if (isPlayer()) {
				((Player) this).getPacketSender().sendConstitutionOrbVenom(false);
			}
		}
		return venomDamage != 0;
	}

	public Position getPositionToFace() {
		return positionToFace;
	}

	public Character setPositionToFace(Position positionToFace) {
		this.positionToFace = positionToFace;
		getUpdateFlag().flag(Flag.FACE_POSITION);
		return this;
	}

	public Character moveTo(Position teleportTarget) {
		super.setPosition(teleportTarget.copy());
		setNeedsPlacement(true);
		setResetMovementQueue(true);
		setTeleporting(true);
		if (isPlayer()) {
			((Player) this).getPacketSender().sendMapRegion();
		}
		return this;
	}

	public boolean moving;

	public void delayedMoveTo(final Position teleportTarget, final int delay) {
		//if (moving)
		//	return;
		//moving = true;
		TaskManager.submit(new Task(delay, this, false) {
			@Override
			protected void execute() {
				moveTo(teleportTarget);
				stop();
			}

			@Override
			public void stop() {
				setEventRunning(false);
				moving = false;
			}
		});
	}

	public UpdateFlag getUpdateFlag() {
		return updateFlag;
	}

	protected final WalkingQueue walkingQueue = new WalkingQueue(this);

	public WalkingQueue getWalkingQueue() {
		return walkingQueue;
	}

	public Character forceChat(String message) {
		setForcedChat(message);
		getUpdateFlag().flag(Flag.FORCED_CHAT);
		return this;
	}

	public Character setEntityInteraction(Entity entity) {
		this.interactingEntity = entity;
		getUpdateFlag().flag(Flag.ENTITY_INTERACTION);
		return this;
	}

	public Entity getInteractingEntity() {
		return interactingEntity;
	}

	public Player player;

	@Override
	public void performAnimation(Animation animation) {
		if (animation == null)
			return;
		setAnimation(animation);
	}

	@Override
	public void performGraphic(Graphic graphic) {
		if (graphic == null)
			return;
		setGraphic(graphic);
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
		int[] directionDeltas = direction.getDirectionDelta();
		setPositionToFace(getPosition().copy().add(directionDeltas[0], directionDeltas[1]));
	}

	/**
	 *
	 * @param runningDirection
	 *            the new value to set.
	 */
	public final void setRunningDirection(Direction runningDirection) {
		this.runningDirection = runningDirection;
	}

	/**
	 * Gets the last direction this character was facing.
	 *
	 * @return the last direction.
	 */
	public final Direction getLastDirection() {
		return lastDirection;
	}

	/**
	 *
	 * @param lastDirection
	 *            the new value to set.
	 */
	public final void setLastDirection(Direction lastDirection) {
		this.lastDirection = lastDirection;
	}

	public boolean isTeleporting() {
		return this.teleporting;
	}

	public Character setTeleporting(boolean teleporting) {
		this.teleporting = teleporting;
		return this;
	}

	public String getForcedChat() {
		return forcedChat;
	}

	public Character setForcedChat(String forcedChat) {
		this.forcedChat = forcedChat;
		return this;
	}

	public boolean[] getPrayerActive() {
		return prayerActive;
	}

	public boolean[] getCurseActive() {
		return curseActive;
	}

	public Character setPrayerActive(boolean[] prayerActive) {
		this.prayerActive = prayerActive;
		return this;
	}

	public Character setPrayerActive(int id, boolean prayerActive) {
		this.prayerActive[id] = prayerActive;
		return this;
	}

	public Character setCurseActive(boolean[] curseActive) {
		this.curseActive = curseActive;
		return this;
	}

	public Character setCurseActive(int id, boolean curseActive) {
		this.curseActive[id] = curseActive;
		return this;
	}

	public int getNpcTransformationId() {
		return npcTransformationId;
	}

	public Character setNpcTransformationId(int npcTransformationId) {
		this.npcTransformationId = npcTransformationId;
		return this;
	}

	public Character getCharacter() {
		return this;
	}

	/*
	 * Movement queue
	 */

	public void setWalkingDirection(Direction primaryDirection) {
		this.walkingDirection = primaryDirection;
	}

	public Direction getWalkingDirection() {
		return walkingDirection;
	}

	public Direction getRunningDirection() {
		return runningDirection;
	}

	public CombatSpell getCurrentlyCasting() {
		return currentlyCasting;
	}

	public void setCurrentlyCasting(CombatSpell currentlyCasting) {
		this.currentlyCasting = currentlyCasting;
	}

	public int getFreezeDelay() {
		return freezeDelay;
	}

	public void setFreezeDelay(int freezeDelay) {
		this.freezeDelay = freezeDelay;
	}

	public int decrementAndGetFreezeDelay() {
		return this.freezeDelay--;
	}

	public boolean isFrozen() {
		return freezeDelay > 0;
	}

	private long lastFreeze;

	public void setLastFreeze() {
		this.lastFreeze = System.currentTimeMillis();
	}

	public long getLastFreeze() {
		return lastFreeze;
	}

	/**
	 * Determines if this character needs to reset their movement queue.
	 *
	 * @return {@code true} if this character needs to reset their movement
	 *         queue, {@code false} otherwise.
	 */
	public final boolean isResetMovementQueue() {
		return resetMovementQueue;
	}

	/**
	 *
	 * @param resetMovementQueue
	 *            the new value to set.
	 */
	public final void setResetMovementQueue(boolean resetMovementQueue) {
		this.resetMovementQueue = resetMovementQueue;
	}

	public void setNeedsPlacement(boolean needsPlacement) {
		this.needsPlacement = needsPlacement;
	}

	public boolean isNeedsPlacement() {
		return needsPlacement;
	}

	public RegionInstance getRegionInstance() {
		return regionInstance;
	}

	public void setRegionInstance(RegionInstance regionInstance) {
		this.regionInstance = regionInstance;
	}

	public <T> T removeAttribute(String key) {
		@SuppressWarnings("unchecked")
		T t = (T) attributes.remove(key);
		return t;
	}

	public void setAttribute(final String key, final Object value) {
		attributes.put(key, value);
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public <T> T getAttribute(String key, T fail) {

		if (fail == null) {
			return null;
		}
		Object object = attributes.get(key);

		if (object != null && object.getClass() == fail.getClass()) {
			@SuppressWarnings("unchecked")
			T t = (T) object;
			return t;
		}

		return fail;
	}

	public int distance(Entity entity) {
		return dist(entity, false);
	}

	public int distance(Entity entity, boolean math) {
		return dist(entity, math);
	}

	private int dist(Entity entity, boolean math) {
		Position position = entity.getPosition();
		return dist(position.getX(), position.getY(), position.getX() + (entity.getSize() - 1), position.getY() + (entity.getSize() - 1), math);
	}

	public int distance(int x, int y) {
		return dist(x, y, x, y, false);
	}

	private int dist(int otherX, int otherY, int otherEndX, int otherEndY, boolean math) {
		int x = getPosition().getX();
		int y = getPosition().getY();
		int endX = x + (this.getSize() - 1);
		int endY = y + (this.getSize() - 1);
		int distX, distY;
		if (endX < otherX) {
			distX = otherX - endX;
		} else if (x > otherEndX) {
			distX = x - otherEndX;
		} else {
			distX = 0;
		}
		if (endY < otherY) {
			distY = otherY - endY;
		} else if (y > otherEndY) {
			distY = y - otherEndY;
		} else {
			distY = 0;
		}
		if (!math && distX == distY) {
			if (distX == 0) {
				return 0;
			}
			return distX + 1;
		}
		return distX > distY ? distX : distY;
	}

	public int getProjectileLockonIndex(Character character) {
		return (character.isPlayer() ? -character.getIndex() - 1 : character.getIndex() + 1);
	}
}