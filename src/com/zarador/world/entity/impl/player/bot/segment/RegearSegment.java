package com.zarador.world.entity.impl.player.bot.segment;

import com.zarador.engine.task.Task;
import com.zarador.engine.task.TaskManager;
import com.zarador.model.Animation;
import com.zarador.model.Position;
import com.zarador.model.Skill;
import com.zarador.util.Misc;
import com.zarador.world.entity.impl.player.bot.Bot;
import com.zarador.world.entity.impl.player.bot.BotUtil;

public class RegearSegment extends BotSegment {

	public static final Position FIRST_TILE = new Position(3088, 3510);
	public static final Position SECONDARY_TILE = new Position(3091, 3527);

	private final Position wildernessTile;

	public RegearSegment(Bot bot) {
		super(bot);
		this.wildernessTile = new Position(SECONDARY_TILE.getX()
				+ Misc.random(-10, 13), SECONDARY_TILE.getY()
				+ Misc.random(-1, 18));
	}

	// kati live
	// bra dac
	// kenny kall

	@Override
	public boolean canExec() {
		/*
		 * if (bot.inWild()) { // bot.getPA().movePlayer(Config.EDGEVILLE_X,
		 * Config.EDGEVILLE_Y, 0); //
		 * bot.forcedChat("I'm Stuck in the wilderness."); return false; // why
		 * was this set to false??
		 * 
		 * }
		 */
		if (bot.isDying()) {
			this.teleport(3086, 3493); // Edge supposedly
			return false;
		}
		return boll();
	}

	@Override
	public int execute() {
		try {
			boolean needsEquip = false;
			if (bot.getInventory().getFreeSlots() == bot.getInventory()
					.capacity())
				needsEquip = true;

			if (needsEquip) {
				bot.equip();
				return 2;
			}
			bot.getSkillManager().setCurrentLevel(Skill.CONSTITUTION,
					bot.getSkillManager().getMaxLevel(Skill.CONSTITUTION) * 10);
			chars().getFood().getId()
					.ifPresent(itemId -> bot.getInventory().add(itemId, 28));

			if (FIRST_TILE.getY() > bot.getPosition().getY()) {
				BotUtil.walkToArea(bot, FIRST_TILE);
			} else {
				if (bot.getPosition().getY() == 3520) {
					if (bot.getPosition().getY() == 3520
							|| bot.getPosition().getY() == 3519) {
						bot.performAnimation(new Animation(6132));
						TaskManager.submit(new Task(1, bot, false) {
							int tick = 1;

							@Override
							public void execute() {
								if (tick == 3) {
									bot.moveTo(new Position(bot.getPosition()
											.getX(), 3523, 0));
									stop();
								}
								tick++;
							}

							@Override
							public void stop() {

							}

						});
					} else if (bot.getPosition().getY() == 3523
							|| bot.getPosition().getY() == 3524) {
						bot.performAnimation(new Animation(6132));
						TaskManager.submit(new Task(1, bot, false) {
							int tick = 1;

							@Override
							public void execute() {
								if (tick == 3) {
									bot.moveTo(new Position(bot.getPosition()
											.getX(), 3520, 0));
									stop();
								}
								tick++;
							}

							@Override
							public void stop() {
							}
						});
					}
				}
				BotUtil.walkToArea(bot, wildernessTile);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 20;
	}

	public boolean boll() {
		/*
		 * if (bot.inWild()) { return false; }
		 */
		if (bot.getWildernessLevel() > 0) {
			return false;
		}

		return true;
	}

}
