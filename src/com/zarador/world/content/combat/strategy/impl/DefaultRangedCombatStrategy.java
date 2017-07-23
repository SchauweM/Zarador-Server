package com.zarador.world.content.combat.strategy.impl;

import com.zarador.engine.task.Task;
import com.zarador.engine.task.TaskManager;
import com.zarador.model.*;
import com.zarador.model.container.impl.Equipment;
import com.zarador.model.definitions.WeaponAnimations;
import com.zarador.model.definitions.WeaponInterfaces;
import com.zarador.model.definitions.WeaponInterfaces.WeaponInterface;
import com.zarador.util.Misc;
import com.zarador.world.content.combat.CombatContainer;
import com.zarador.world.content.combat.CombatFactory;
import com.zarador.world.content.combat.CombatType;
import com.zarador.world.content.combat.CombatContainer.ContainerHit;
import com.zarador.world.content.combat.effect.CombatPoisonEffect.PoisonType;
import com.zarador.world.content.combat.range.CombatRangedAmmo.AmmunitionData;
import com.zarador.world.content.combat.range.CombatRangedAmmo.RangedWeaponData;
import com.zarador.world.content.combat.range.CombatRangedAmmo.RangedWeaponType;
import com.zarador.world.content.combat.strategy.CombatStrategy;
import com.zarador.world.content.combat.weapon.CombatSpecial;
import com.zarador.world.content.combat.weapon.FightStyle;
import com.zarador.world.content.minigames.impl.Dueling;
import com.zarador.world.content.minigames.impl.Dueling.DuelRule;
import com.zarador.world.entity.impl.Character;
import com.zarador.world.entity.impl.GroundItemManager;
import com.zarador.world.entity.impl.npc.NPC;
import com.zarador.world.entity.impl.player.Player;

/**
 * The default combat strategy assigned to an {@link Character} during a ranged
 * based combat session.
 * 
 * @author lare96
 */
public class DefaultRangedCombatStrategy implements CombatStrategy {

	@Override
	public boolean canAttack(Character entity, Character victim) {

		// We do not need to check npcs.
		if (entity.isNpc()) {
			return true;
		}

		// Create the player instance.
		Player player = (Player) entity;

		if (Dueling.checkRule(player, DuelRule.NO_RANGED)) {
			player.getPacketSender().sendMessage("Ranged-attacks have been turned off in this duel!");
			player.getCombatBuilder().reset();
			return false;
		}

		// If we are using a crystal bow then we don't need to check for ammo.
		if (CombatFactory.crystalBow(player)) {
			return true;
		}

		// Check the ammo before proceeding.
		if (!checkAmmo(player)) {
			if (player.isSpecialActivated()) {
				player.setSpecialActivated(false);
				CombatSpecial.updateBar(player);
			}
			return false;
		}
		return true;
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {

		if (entity.isNpc()) {
			NPC npc = (NPC) entity;
			AmmunitionData ammo = AmmunitionData.ADAMANT_ARROW;
			switch (npc.getId()) {
			case 688:
				ammo = AmmunitionData.BRONZE_ARROW;
				break;
			case 27:
				ammo = AmmunitionData.STEEL_ARROW;
				break;
			case 2028:
				ammo = AmmunitionData.BOLT_RACK;
				break;
			case 6220:
			case 6256:
			case 6276:
				ammo = AmmunitionData.RUNE_ARROW;
				break;
			case 6225:
				ammo = AmmunitionData.STEEL_JAVELIN;
				break;
			case 6252:
				ammo = AmmunitionData.RUNE_ARROW;
				break;
			}

			entity.performAnimation(new Animation(npc.getDefinition().getAttackAnimation()));

			entity.performGraphic(new Graphic(ammo.getStartGfxId(),
					ammo.getStartHeight() >= 43 ? GraphicHeight.HIGH : GraphicHeight.MIDDLE));

			fireProjectile(npc, victim, ammo, false);

			return new CombatContainer(entity, victim, 1, CombatType.RANGED, true);
		}

		Player player = (Player) entity;
		player.getWalkingQueue().clear();

		final boolean dBow = CombatFactory.darkBow(player);

		player.setFireAmmo(0);

		startAnimation(player);

		AmmunitionData ammo = RangedWeaponData.getAmmunitionData(player);
		if (player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == 12926) {
			ammo = AmmunitionData.DRAGON_DART;
		}
		if (player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == 20171) {
			ammo = AmmunitionData.RUNE_ARROW;
		}
		if (!player.isSpecialActivated()) {

			if (!CombatFactory.crystalBow(player) && !CombatFactory.blowPipe(player) && !CombatFactory.zaryteBow(player)) {
				decrementAmmo(player, victim.getPosition());
				if (dBow || player.getRangedWeaponData() == RangedWeaponData.MAGIC_SHORTBOW
						&& player.isSpecialActivated() && player.getCombatSpecial() != null
						&& player.getCombatSpecial() == CombatSpecial.MAGIC_SHORTBOW) {
					decrementAmmo(player, victim.getPosition());
				}
			}

			player.performGraphic(new Graphic(ammo.getStartGfxId(), ammo.getStartGfxId() == 2138 ? GraphicHeight.LOW
					: ammo.getStartHeight() >= 43 ? GraphicHeight.HIGH : GraphicHeight.MIDDLE));

			fireProjectile(player, victim, ammo, dBow);
		}

		CombatContainer container = new CombatContainer(entity, victim, dBow ? 2 : 1, CombatType.RANGED, true);
		/** CROSSBOW BOLTS EFFECT **/
//		if (player.getEquipment().contains(12926)) {
//			int hit = container.getHits()[0].getHit().getDamage();
//			int bonus = hit / 4;
//			//container.setModifiedDamage(hit * 2 - bonus);
//			container.getHits()[0].getHit().setDamage(hit + bonus);
//		}
		if (player.getEquipment().get(Equipment.WEAPON_SLOT).getDefinition() != null && player.getEquipment()
				.get(Equipment.WEAPON_SLOT).getDefinition().getName().toLowerCase().contains("crossbow")) {
			if (Misc.getRandom(12) >= 10) {
				container.setModifiedDamage(getModifiedDamage(player, victim, container));
			}
		}
		return container;
	}

	public static void fireProjectile(Character e, Character victim, final AmmunitionData ammo, boolean dBow) {
		TaskManager.submit(new Task(1, e.getCombatBuilder(), false) {
			@Override
			protected void execute() {
				new Projectile(e, victim, ammo.getProjectileId(), ammo.getProjectileDelay() + 16,
						ammo.getProjectileSpeed(), ammo.getStartHeight(), ammo.getEndHeight(), 0).sendProjectile();
				if (dBow) {
					new Projectile(e, victim, ammo.getProjectileId(), ammo.getProjectileDelay() + 32,
							ammo.getProjectileSpeed(), ammo.getStartHeight() - 2, ammo.getEndHeight(), 0)
									.sendProjectile();
				}
				stop();
			}
		});
	}

	@Override
	public int attackDelay(Character entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(Character entity) {

		// The default distance for all npcs using ranged is 6.
		if (entity.isNpc()) {
			return 6;
		}

		// Create the player instance.

		Player player = (Player) entity;

		int distance = 5;
		if (player.getRangedWeaponData() != null)
			distance = player.getRangedWeaponData().getType().getDistanceRequired();

		return distance + (player.getFightType().getStyle() == FightStyle.DEFENSIVE ? 2 : 0);
	}

	/**
	 * Starts the performAnimation for the argued {@link Player} in the current
	 * combat hook.
	 * 
	 * @param player
	 *            the player to start the performAnimation for.
	 */
	private void startAnimation(Player player) {
		if (player.getEquipment().get(Equipment.WEAPON_SLOT).getDefinition().getName().startsWith("Karils")) {
			player.performAnimation(new Animation(2075));
		} else {
			player.performAnimation(new Animation(WeaponAnimations.getAttackAnimation(player)));
		}
	}

	/**
	 * Checks the ammo to make sure the argued {@link Player} has the right type
	 * and amount before attacking.
	 * 
	 * @param player
	 *            the player's ammo to check.
	 * @return <code>true</code> if the player has the right ammo,
	 *         <code>false</code> otherwise.
	 */
	private boolean checkAmmo(Player player) {
		RangedWeaponData data = player.getRangedWeaponData();
		if (data.getType() == RangedWeaponType.THROW || data.getType() == RangedWeaponType.DART || data.getType() == RangedWeaponType.BLOWPIPE || player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == 20171)
			return true;
		Item ammunition = player.getEquipment().getItems()[data.getType() == RangedWeaponType.THROW
				? Equipment.WEAPON_SLOT : Equipment.AMMUNITION_SLOT];
		if(data.getType() == RangedWeaponType.THROW) {
			ammunition = player.getEquipment().getItems()[Equipment.WEAPON_SLOT];
		}
		boolean darkBow = data.getType() == RangedWeaponType.DARK_BOW && ammunition.getAmount() < 2
				|| data == RangedWeaponData.MAGIC_SHORTBOW && player.isSpecialActivated()
						&& player.getCombatSpecial() != null
						&& player.getCombatSpecial() == CombatSpecial.MAGIC_SHORTBOW && ammunition.getAmount() < 2;
		if (ammunition.getId() == -1 || ammunition.getAmount() < 1 || darkBow) {
			player.getPacketSender().sendMessage(darkBow ? "You need at least 2 arrows to fire this bow."
					: "You don't have any ammunition to fire.");
			player.getCombatBuilder().reset();
			return false;
		}
		boolean properEquipment = false;
		for (AmmunitionData ammo : data.getAmmunitionData()) {
			for (int i : ammo.getItemIds()) {
				if (i == ammunition.getId()) {
					properEquipment = true;
					break;
				}
			}
		}
		if (!properEquipment) {
			String ammoName = ammunition.getDefinition().getName(),
					weaponName = player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getDefinition().getName(),
					add = !ammoName.endsWith("s") && !ammoName.endsWith("(e)") ? "s" : "";
			player.getPacketSender().sendMessage("You can not use " + ammoName + "" + add + " with "
					+ Misc.anOrA(weaponName) + " " + weaponName + ".");
			player.getCombatBuilder().reset();
			return false;
		}

		return true;
	}

	/**
	 * Decrements the amount ammo the {@link Player} currently has equipped.
	 * 
	 * @param player
	 *            the player to decrement ammo for.
	 */
	/**
	 * @param player
	 * @param pos
	 */
	public static void decrementAmmo(Player player, Position pos) {

		// Determine which slot we are decrementing ammo from.
		int slot = player.getWeapon() == WeaponInterface.SHORTBOW || player.getWeapon() == WeaponInterface.BALLISTA || player.getWeapon() == WeaponInterface.LONGBOW
				|| player.getWeapon() == WeaponInterface.CROSSBOW ? Equipment.AMMUNITION_SLOT : Equipment.WEAPON_SLOT;
		if (player.getWeapon() == WeaponInterface.BLOWPIPE || player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == 20171) {
			return;
		}

		// Set the ammo we are currently using.
		player.setFireAmmo(player.getEquipment().get(slot).getId());
		int[] compIds = { 14022 };
		final boolean compCapes = player.getEquipment().containsAny(compIds);
		final boolean ardy = player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 19748;
		final boolean avas = player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 10499 || compCapes || player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 21148;
		if (avas) { // Avas
			if (Misc.getRandom(8) == 1 || Misc.getRandom(8) == 2 || Misc.getRandom(8) == 3 || Misc.getRandom(8) == 4
					|| Misc.getRandom(8) == 5 || Misc.getRandom(8) == 6 || Misc.getRandom(8) == 7)
				return;
		}
		if (ardy) { // ardy
			if (Misc.getRandom(7) == 1 || Misc.getRandom(7) == 2 || Misc.getRandom(7) == 3 || Misc.getRandom(7) == 4
					|| Misc.getRandom(7) == 5 || Misc.getRandom(7) == 0 || Misc.getRandom(7) == 6)
				return;
		}
		int ammo_id = player.getEquipment().get(slot).getId();
		if (player.getEquipment().get(slot).getAmount() == 1) {
			player.getEquipment().setItem(slot, new Item(-1));
			player.getEquipment().refreshItems();
			player.getInventory().refreshItems();
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}
		// Decrement the ammo in the selected slot.
		player.getEquipment().get(slot).decrementAmount();

		if (!avas && player.getFireAmmo() != 15243 && player.getLocation() != Locations.Location.KRAKEN) {
			int random = Misc.random(10);
			if (random > 2) {
				GroundItemManager.spawnGroundItem(player,
						new GroundItem(new Item(player.getFireAmmo()), pos, player.getUsername(), false, 120, true, 120));
			}
		}

		// If we are at 0 ammo remove the item from the equipment completely.
		if (player.getEquipment().get(slot).getAmount() == 0) {
			player.getPacketSender().sendMessage("You have no ammunition!");
			player.getEquipment().refreshItems();
			WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
			WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}

		// Refresh the equipment interface.
		player.getEquipment().refreshItems();
	}

	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		return false;
	}

	private static final int getModifiedDamage(Player player, Character target, CombatContainer container) {
		if (container == null || container.getHits().length < 1)
			return 0;
		ContainerHit hit = container.getHits()[0];
		if(hit.getHit().getDamage() == 0) {
			return 0;
		}
		int damage = container.getHits()[0].getHit().getDamage();
		int ammo = player.getFireAmmo();
		if (ammo == -1) {
			return damage;
		}
		double multiplier = 1;
		Player pTarget = target.isPlayer() ? ((Player) target) : null;
		switch (ammo) {
		case 9236: // Lucky Lightning
			target.performGraphic(new Graphic(749));
			multiplier = 1.3;
			break;
		case 9237: // Earth's Fury
			target.performGraphic(new Graphic(755));
			multiplier = 1.05;
			break;
		case 9238: // Sea Curse
			target.performGraphic(new Graphic(750));
			multiplier = 1.1;
			break;
		case 9239: // Down To Earth
			target.performGraphic(new Graphic(757));
			if (pTarget != null) {
				pTarget.getSkillManager().setCurrentLevel(Skill.MAGIC,
						pTarget.getSkillManager().getCurrentLevel(Skill.MAGIC) - 3);
				pTarget.getPacketSender().sendMessage("Your Magic level has been reduced.");
			}
			break;
		case 9240: // Clear Mind
			target.performGraphic(new Graphic(751));
			if (pTarget != null) {
				pTarget.getSkillManager().setCurrentLevel(Skill.PRAYER,
						pTarget.getSkillManager().getCurrentLevel(Skill.PRAYER) - 40);
				if (pTarget.getSkillManager().getCurrentLevel(Skill.PRAYER) < 0) {
					pTarget.getSkillManager().setCurrentLevel(Skill.PRAYER, 0);
				}
				pTarget.getPacketSender().sendMessage("Your Prayer level has been leeched.");
				player.getSkillManager().setCurrentLevel(Skill.PRAYER,
						pTarget.getSkillManager().getCurrentLevel(Skill.PRAYER) + 40);
				if (player.getSkillManager().getCurrentLevel(Skill.PRAYER) > player.getSkillManager()
						.getMaxLevel(Skill.PRAYER)) {
					player.getSkillManager().setCurrentLevel(Skill.PRAYER,
							player.getSkillManager().getMaxLevel(Skill.PRAYER));
				} else {
					player.getPacketSender()
							.sendMessage("Your enchanced bolts leech some Prayer points from your opponent..");
				}
			}
			break;
		case 9241: // Magical Posion
			target.performGraphic(new Graphic(752));
			CombatFactory.poisonEntity(target, PoisonType.MILD);
			break;
		case 9242:
			int removePlayerHitpoints = player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) / 10; //10% of the players hitpoints
			int removeNpcHitpoints = (target.getConstitution() - damage) / 5; //20% of the npcs hitpoints

			if(removeNpcHitpoints < damage) {
				break;
			}

			if(Misc.inclusiveRandom(1, 3) != 2) {
				break;
			}

			if(removeNpcHitpoints <= 0) {
				break;
			}

			target.performGraphic(new Graphic(754));
			player.dealDamage(target, new Hit(removePlayerHitpoints, Hitmask.RED, CombatIcon.RANGED));
			return removeNpcHitpoints;
		case 9243:
			target.performGraphic(new Graphic(758, GraphicHeight.MIDDLE));
			multiplier = 1.15;
			break;
		case 9244:
			target.performGraphic(new Graphic(756));
			if (pTarget != null && (pTarget.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 1540
					|| pTarget.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 11283 || pTarget.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 11284  || pTarget.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 11613 || pTarget.getFireImmunity() > 0)) {
				return damage;
			}
			//if (Misc.getRandom(10) == 0) {
				//damage *= 1.45;
			//}
			multiplier = 1.45;
			break;
		case 11625:
			target.performGraphic(new Graphic(756));
			if (pTarget != null && (pTarget.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 1540
					|| pTarget.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 11283 || pTarget.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 11284 || pTarget.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 11613 || pTarget.getFireImmunity() > 0)) {
				return damage;
			}
			if (damage < 300 && Misc.getRandom(3) <= 1) {
				damage = 300 + Misc.getRandom(150);
			}
			multiplier = 1.25;
			break;
		case 9245:
			target.performGraphic(new Graphic(753));
			multiplier = 1.26;
			int heal = (int) (damage * 0.25) + 10;
			player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION,
					player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) + heal);
			if (player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) >= 1120) {
				player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, 1120);
			}
			player.getSkillManager().updateSkill(Skill.CONSTITUTION);
			if (damage < 250 && Misc.getRandom(3) <= 1) {
				damage += 150 + Misc.getRandom(80);
			}
			break;
		}
		return (int) (damage * multiplier);
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.RANGED;
	}
}
