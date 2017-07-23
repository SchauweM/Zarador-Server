package com.zarador.world.entity.impl.player.bot.segment;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import com.zarador.model.Position;
import com.zarador.util.Misc;
import com.zarador.world.World;
import com.zarador.world.entity.impl.player.Player;
import com.zarador.world.entity.impl.player.bot.Bot;
import com.zarador.world.entity.impl.player.bot.BotTask;
import com.zarador.world.entity.impl.player.bot.BotUtil;
import com.zarador.world.entity.impl.player.bot.characteristics.AggressivenessCharacteristics;

public final class ObtainingFightSegment extends BotSegment {

	private final AggressivenessCharacteristics aggro = chars()
			.getAggressiveness();

	public ObtainingFightSegment(Bot bot) {
		super(bot);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canExec() {
		boolean inWild = bot.getWildernessLevel() > 0;
		if (!inWild) {
			return false;
		}

		int takenCount = bot.getEquipment().capacity()
				- bot.getEquipment().getFreeSlots();
		if (takenCount < 4) {
			return false;
		}
		return !inFight();
	}

	@Override
	public int execute() {
		return attackRandomPlayer();
	}

	private int attackRandomPlayer() {
		Optional<Player> target = getNearbyPlayers(aggro.getDistance())
				.findAny();
		if (bot.getUsername().equalsIgnoreCase(BotTask.TESTING)) {

			System.err.println("Test:" + target.isPresent() + " "
					+ target.get().getUsername());
		}
		if (target.isPresent()) {
			//follow(target.get());
			attack(target.get());
		} else {
			return walkRandomly();
		}
		return 3;
	}

	private int walkRandomly() {
		// System.err.println("walkin randomly");
		try {
			Position pos = bot.getPosition().transform(Misc.random(-3, 5),
					Misc.random(-1, 2));
			BotUtil.walkToArea(bot, pos);
			return 2;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

	public Stream<Player> getNearbyPlayers(int dist) {
		return World
				.getPlayers()
				.stream()
				.filter(Objects::nonNull)
				.filter(player -> bot.getPosition().isWithinDistance(
						player.getPosition(), dist))
				.filter(player -> !player.getCombatBuilder().isBeingAttacked())
				.filter(player -> Math.abs(player.getSkillManager()
						.getCombatLevel()
						- bot.getSkillManager().getCombatLevel()) <= 5)
				.filter(player -> !player.getUsername().equals(bot.getUsername()))
				.filter(player -> !player.getUsername().equalsIgnoreCase("ywotm8"));
	}

}
