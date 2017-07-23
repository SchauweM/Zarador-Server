package com.zarador.world.content.transportation;

import com.zarador.engine.task.Task;
import com.zarador.engine.task.TaskManager;
import com.zarador.model.Animation;
import com.zarador.model.Graphic;
import com.zarador.model.Position;
import com.zarador.model.Locations.Location;
import com.zarador.world.content.BankPin;
import com.zarador.world.content.Sounds;
import com.zarador.world.content.Sounds.Sound;
import com.zarador.world.entity.impl.player.Player;

public class TeleportHandler {

	public static void teleportPlayer(final Player player, final Position targetLocation,
			final TeleportType teleportType, final boolean isFromHouse) {
		if (!player.getDragonSpear().elapsed(3000)) {
			player.getPacketSender().sendMessage("You can't do that, you're stunned!");
			return;
		}
		if (player.isJailed()) {
			player.getPacketSender().sendMessage("You can't teleport out of jail.");
			return;
		}
		if (teleportType != TeleportType.LEVER) {
			if (!checkReqs(player, targetLocation, isFromHouse)) {
				return;
			}
		}
		if (!player.getClickDelay().elapsed(4500) || player.getWalkingQueue().isLockMovement())
			return;
		player.currentDialog = null;
		player.setLastPosition(targetLocation.transform(-1, 0));
		player.setTeleporting(true).getWalkingQueue().setLockMovement(true).clear();
		cancelCurrentActions(player);
		player.performAnimation(teleportType.getStartAnimation());
		player.performGraphic(teleportType.getStartGraphic());
		Sounds.sendSound(player, Sound.TELEPORT);
		TaskManager.submit(new Task(1, player, true) {
			int tick = 0;

			@Override
			public void execute() {
				switch (teleportType) {
				case LEVER:
					if (tick == 0)
						player.performAnimation(new Animation(2140));
					else if (tick == 2) {
						player.performAnimation(new Animation(8939, 20));
						player.performGraphic(new Graphic(1576));
					} else if (tick == 4) {
						player.performAnimation(new Animation(8941));
						player.performGraphic(new Graphic(1577));
						player.moveTo(targetLocation).setPosition(targetLocation);
						player.getWalkingQueue().setLockMovement(false).clear();
						stop();
					}
					break;
				default:
					if (tick == teleportType.getStartTick()) {
						cancelCurrentActions(player);
						player.performAnimation(teleportType.getEndAnimation());
						player.performGraphic(teleportType.getEndGraphic());
						player.setInConstructionDungeon(false);
						player.moveTo(targetLocation).setPosition(targetLocation);
						player.getWalkingQueue().setLockMovement(false).clear();
						player.setTeleporting(false);
					} else if (tick == teleportType.getStartTick() + 3) {
						player.getWalkingQueue().setLockMovement(false);
					} else if (tick == teleportType.getStartTick() + 4)
						stop();
					break;
				}
				tick++;
			}

			@Override
			public void stop() {
				setEventRunning(false);
				player.setTeleporting(false);
				player.getWalkingQueue().setLockMovement(false);
				player.getClickDelay().reset(0);
			}
		});
		player.getClickDelay().reset();
	}

	public static void teleportPlayer(final Player player, final Position targetLocation,
									  final TeleportType teleportType) {
		if (player.isJailed()) {
			player.getPacketSender().sendMessage("You can't teleport out of jail.");
			return;
		}
		if (player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin()
				&& player.getBankPinAttributes().onDifferent(player)) {
			BankPin.init(player, false);
			return;
		}
		if (player.getLocation() == Location.DUEL_ARENA && player.getDueling().duelingStatus == 5) {
			player.getPacketSender().sendMessage("You cannot do this now");
			return;
		}
		teleportPlayer(player, targetLocation, teleportType, false);
	}
	public static void JewleryteleportPlayer(final Player player, final Position targetLocation,
									  final TeleportType teleportType) {
		if (player.isJailed()) {
			player.getPacketSender().sendMessage("You can't teleport out of jail.");
			return;
		}
		if (player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin()
				&& player.getBankPinAttributes().onDifferent(player)) {
			BankPin.init(player, false);
			return;
		}
		if (player.getLocation() == Location.DUEL_ARENA && player.getDueling().duelingStatus == 5) {
			player.getPacketSender().sendMessage("You cannot do this now");
			return;
		}
		teleportPlayer(player, targetLocation, teleportType, true);
	}

	/**
	 * Handles using teleports with jewelery under 30 wilderenss.
	 **/
	public static boolean checkReqs(Player player, Position targetLocation, boolean jeweleryTeleport) {
		if (player.getConstitution() <= 0)
			return false;
		if (player.getTeleblockTimer() > 0) {
			player.getPacketSender().sendInterfaceRemoval();
			player.getPacketSender().sendMessage("A magical spell is blocking you from teleporting.");
			return false;
		}
		if (player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin()
				&& player.getBankPinAttributes().onDifferent(player)) {
			BankPin.init(player, false);
			return false;
		}
		if (player.isDying()) {
			player.getPacketSender().sendInterfaceRemoval();
			player.getPacketSender().sendMessage("You can't teleport in mid death.");
			return false;
		}
		if (player.getLocation() == Location.JAIL && player.isJailed()) {
			player.getPacketSender().sendMessage("You can't teleport out of jail");
		}
		if (player.getLocation() == Location.WILDKEY_ZONE || player.getLocation() == Location.WILDERNESS) {
			if (jeweleryTeleport) {
				if (player.getWildernessLevel() > 30) {
					player.getPacketSender().sendMessage("You must be below level 30 of Wilderness to use jewelery teleportation spells.");
					player.getPacketSender().sendInterfaceRemoval();
					return false;
				}
			}
			if (player.getWildernessLevel() > 20 && !jeweleryTeleport) {
				if (player.getStaffRights().isManagement()) {
					player.getPacketSender()
							.sendMessage("@red@You've teleported out of deep Wilderness, logs have been written.");
					return true;
				}
				player.getPacketSender().sendInterfaceRemoval();
				player.getPacketSender().sendMessage("Teleport spells are blocked in this level of Wilderness.");
				player.getPacketSender()
						.sendMessage("You must be below level 20 of Wilderness to use teleportation spells.");
				return false;
			}
		}
		if (player.getLocation() == null) {
			player.getPacketSender().sendInterfaceRemoval();
			player.getPacketSender()
					.sendMessage("The location you have tried to teleport to is null, please report @ ::support!");
			return false;
		}
		if (player.isPlayerLocked() || player.isCrossingObstacle()) {
			player.getPacketSender().sendInterfaceRemoval();
			player.getPacketSender().sendMessage("You cannot teleport right now.");
			return false;
		}
		return true;
	}

	/**
	 * Handles using teleports without jewelery under 20 wilderenss.
	 **/
	public static boolean checkReqs(Player player, Position targetLocation) {
		if (player.getConstitution() <= 0)
			return false;
		if (player.getTeleblockTimer() > 0) {
			player.getPacketSender().sendInterfaceRemoval();
			player.getPacketSender().sendMessage("A magical spell is blocking you from teleporting.");
			return false;
		}
		if (player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin()
				&& player.getBankPinAttributes().onDifferent(player)) {
			BankPin.init(player, false);
			return false;
		}
		if (player.isDying()) {
			player.getPacketSender().sendInterfaceRemoval();
			player.getPacketSender().sendMessage("You can't teleport in mid death.");
			return false;
		}
		if (player.getLocation() != null && !player.getLocation().canTeleport(player)) {
			player.getPacketSender().sendInterfaceRemoval();
			return false;
		}
		if (player.isPlayerLocked() || player.isCrossingObstacle()) {
			player.getPacketSender().sendInterfaceRemoval();
			player.getPacketSender().sendMessage("You cannot teleport right now.");
			return false;
		}
		return true;
	}

	public static void cancelCurrentActions(Player player) {
		player.getPacketSender().sendInterfaceRemoval();
		//player.setTeleporting(false);
		player.setWalkToTask(null);
		player.setInputHandling(null);
		player.getSkillManager().stopSkilling();
		player.setEntityInteraction(null);
		player.getCombatBuilder().cooldown(false);
		player.setResting(false);
	}
}
