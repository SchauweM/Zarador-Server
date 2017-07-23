package com.zarador.world.entity.impl.player.bot;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

import com.zarador.model.Item;
import com.zarador.model.Position;
import com.zarador.model.Skill;
import com.zarador.model.Locations.Location;
import com.zarador.model.movement.PathFinder;
import com.zarador.world.World;
import com.zarador.world.content.combat.CombatFactory;
import com.zarador.world.content.skill.SkillManager;
import com.zarador.world.entity.impl.player.Player;
import com.zarador.world.entity.impl.player.bot.pker.PkerBotConstants;
import com.zarador.world.entity.impl.player.bot.pker.PkerBotTypes;

public class BotUtil {

	public static ArrayList<Bot> bots = new ArrayList<Bot>();

	public static void addBot(Bot bot) {
		bots.add(bot);
	}

	public static void getMaxed(Player player) {

		for (int i = 0; i < 7; i++) {
			setLevel(player, 99, i);
		}
	}

	private final static Random random = new Random();

	public static Position getRandomCoordinateHome() {
		int xOffset = PkerBotConstants.END_HOME_X
				- PkerBotConstants.START_HOME_X;
		int yOffset = PkerBotConstants.END_HOME_Y
				- PkerBotConstants.START_HOME_Y;

		int xPos = PkerBotConstants.START_HOME_X + random.nextInt(xOffset);
		int yPos = PkerBotConstants.START_HOME_Y + random.nextInt(yOffset);

		return new Position(xPos, yPos);
	}

	public static PkerBotTypes randomType(Player bot) {
		return PkerBotTypes.values()[rnd.nextInt(PkerBotTypes.values().length)];
	}

	private static void setLevel(Player player, int level, int id) {
		Skill skill = Skill.forId(id);
		player.getSkillManager().setMaxLevel(skill, level);
		player.getSkillManager().setCurrentLevel(skill, level);
		player.getSkillManager().setExperience(skill,
				SkillManager.getExperienceForLevel(level));
	}

	public static void getPured(Player player) {
		setLevel(player, 75, 0);
		setLevel(player, 1, 1);
		setLevel(player, 99, 2);
		setLevel(player, 99, 3);
		setLevel(player, 99, 4);
		setLevel(player, 52, 5);
		setLevel(player, 99, 6);
	}

	public static int amountOfItem(Player bot, int itemId) {
		int amount = 0;

		for (Item item : bot.getInventory().getItems()) {
			if (item != null) {
				if (item.getAmount() == itemId) {
					amount += item.getAmount();
				}
			}
		}
		return amount;
	}

	public static void removeBot(Bot bot) {
		/*
		 * for(Bot b : bots) { if(b == bot) {
		 * 
		 * b.logout(); System.out.println("tripped Removebot - logout"); } }
		 */
		bot.logout();
		//bot.cleanUp();
		bots.remove(bot);
	}

	public static int findFirstSlot(Player bot, int type) {
		return bot.getInventory().getSlot(type);
	}

	/**
	 * Attacks the closest target to the {@link bot}
	 * 
	 * @param bot
	 *            - the bot looking for target
	 * @param prompt
	 *            - asks before fight
	 * @return - the players int
	 */
	public static int attackRandomPlayer(Player bot, boolean prompt) {
		int yourX = bot.getPosition().getX();
		int yourY = bot.getPosition().getY();

		int closestDistance = 200;
		int indexAttacking = 0;
		@SuppressWarnings("unused")
		Player targ = null;

		for (Player player : World.getPlayers()) {
			
			if (player == null || player.getIndex() == bot.getIndex()
					|| player.getCombatBuilder().isBeingAttacked() || bot.getCombatBuilder().isBeingAttacked()
					|| bot.getSkillManager().getCombatLevel() != player.getSkillManager().getCombatLevel())
				continue;

			int botsX = player.getPosition().getX();
			int botsY = player.getPosition().getY();

			int distance = (int) Math.abs(Math.hypot(botsX - yourX, botsY
					- yourY));

			if (distance < closestDistance) {
				closestDistance = distance;
				indexAttacking = player.getIndex();
				targ = player;
			}
		}

		if (targ == null || targ.getConstitution() <= 0 || targ.equals(bot)) {
			bot.getWalkingQueue().clear();
			return -1;
		}
		if (bot.isPlayerLocked() || bot.getWalkingQueue().isLockMovement())
			return -1;

		if (!bot.getDragonSpear().elapsed(3000)) {
			bot.getPacketSender().sendMessage("You can't do that, you're stunned!");
			return -1;
		}
		if (targ.getBankPinAttributes().hasBankPin() && !targ.getBankPinAttributes().hasEnteredBankPin()
				&& targ.getBankPinAttributes().onDifferent(targ)) {
			bot.getPacketSender().sendMessage(
					"The other player hasn't inserted their bank pin, they must insert it before they can do anything.");
			return -1;
		}
		if (bot.getCombatBuilder().getStrategy() == null) {
			bot.getCombatBuilder().determineStrategy();
		}
		if (CombatFactory.checkAttackDistance(bot, targ)) {
			bot.getWalkingQueue().clear();
		}

		bot.getCombatBuilder().attack(targ);

		return indexAttacking;
	}

	public static SecureRandom rnd = new SecureRandom();

	/**
	 * Walk to random location in a square area
	 * 
	 * @param x
	 *            - Bottom right square
	 * @param topLeft
	 *            - Top left square
	 */
	public static void walkToArea(Player bot, Position t) {
		
		PathFinder.calculatePath(bot, t.getX(), t.getY(), 1, 1, true);
	}

	public static void walkToArea(Player bot, Position[] t) {
		int index = rnd.nextInt(t.length);
		Position pos = t[index];
		walkToArea(bot, pos);
	}

}
