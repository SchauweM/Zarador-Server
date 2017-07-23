package com.zarador.net.packet.impl;

import com.zarador.GameSettings;
import com.zarador.model.Animation;
import com.zarador.model.Flag;
import com.zarador.model.action.PlayerAction;
import com.zarador.model.container.impl.Equipment;
import com.zarador.model.definitions.WeaponAnimations;
import com.zarador.model.input.impl.ChangePassword;
import com.zarador.net.packet.Packet;
import com.zarador.net.packet.PacketListener;
import com.zarador.world.content.BankPin;
import com.zarador.world.content.minigames.impl.Dueling;
import com.zarador.world.content.minigames.impl.Dueling.DuelRule;
import com.zarador.world.entity.impl.player.Player;

/**
 * This packet listener is called when a player has clicked on either the
 * mini-map or the actual game map to move around.
 * 
 * @author Gabriel Hannason
 */

public class MovementPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		if(player.getConstitution() <= 0) {
			return;
		}
		player.setEntityInteraction(null);
		player.getSkillManager().stopSkilling();

		if (packet.getOpcode() != COMMAND_MOVEMENT_OPCODE) {
			player.setWalkToTask(null);
			player.setCastSpell(null);
			player.getCombatBuilder().cooldown(false);
		}

		if (!checkReqs(player, packet.getOpcode()))
			return;
		if (player.getDiversionTask() != null) {
			player.getDiversionTask().stop();
		}
		player.getPacketSender().sendInterfaceRemoval();
		player.setTeleporting(false);
		player.setInactive(false);

		if (player.getActionQueue().hasAction() && player.getActionQueue().getCurrentAction().getActionPolicy() == PlayerAction.ActionPolicy.FIXED) {
			return;
		}

		int steps = packet.getSize();
		if (packet.getOpcode() == 248) {
			steps -= 14;
		}
		steps -= 5;
		steps /= 2;
		if (steps < 0) {
			return;
		}
		player.getWalkingQueue().clear();
		if (steps > 50) {
			return;
		}
		player.getActionQueue().clearNonQueueActions();
		int[] offsetsX = player.offsetX;
		int[] offsetsY = player.offsetY;
		int firstX = packet.readLEShortA();
		for (int i = 0; i < steps; i++) {
			offsetsX[i] = packet.readByte();
			offsetsY[i] = packet.readByte();
			if (offsetsX[i] > 104 || offsetsY[i] > 104) {
				return;
			}
		}
		int firstY = packet.readLEShort();
		boolean running = player.getWalkingQueue().isRunning();
		if (player.getRunEnergy() > 0) {
			player.getWalkingQueue().setRunningQueue(running);
		}
		player.getWalkingQueue().addStep(firstX, firstY);
		for (int i = 0; i < steps; i++) {
			player.getWalkingQueue().addStep(firstX + offsetsX[i], firstY + offsetsY[i]);
		}
	}


	public static boolean checkReqs(Player player, int opcode) {
		if (player.isFrozen()) {
			if (opcode != COMMAND_MOVEMENT_OPCODE)
				player.getWalkingQueue().clear();
			player.getPacketSender().sendMessage("A magical force stops you from moving.");
			return false;
		}
		if (!player.getDragonSpear().elapsed(3000)) {
			if (opcode != COMMAND_MOVEMENT_OPCODE)
				player.getWalkingQueue().clear();
			player.getPacketSender().sendMessage("You are stunned!");
			return false;
		}
		if (player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin()
				&& player.getBankPinAttributes().onDifferent(player)) {
			BankPin.init(player, false);
			return false;
		}
		if (player.getTrading().inTrade() && System.currentTimeMillis() - player.getTrading().lastAction <= 1000) {
			return false;
		}
		if (Dueling.checkRule(player, DuelRule.NO_MOVEMENT)) {
			if (opcode != COMMAND_MOVEMENT_OPCODE)
				player.getPacketSender().sendMessage("Movement has been turned off in this duel!");
			return false;
		}
		if (player.isResting()) {
			player.setResting(false);
			player.performAnimation(new Animation(11788));
			WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			return false;
		}
		if (player.getPasswordChanging() && player.getPasswordChange() != GameSettings.PASSWORD_CHANGE) {
			player.setInputHandling(new ChangePassword());
			player.getPacketSender().sendEnterInputPrompt("Please enter a new password to set for your account:");
			return false;
		}
		if (player.isPlayerLocked() || player.isCrossingObstacle())
			return false;
		if (player.isNeedsPlacement()) {
			return false;
		}
		return !player.getWalkingQueue().isLockMovement();
	}

	public static final int COMMAND_MOVEMENT_OPCODE = 98;
	public static final int GAME_MOVEMENT_OPCODE = 164;
	public static final int MINIMAP_MOVEMENT_OPCODE = 248;

}
