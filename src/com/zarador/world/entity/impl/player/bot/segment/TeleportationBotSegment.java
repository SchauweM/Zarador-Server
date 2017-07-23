package com.zarador.world.entity.impl.player.bot.segment;

import com.zarador.world.entity.impl.player.bot.Bot;
import com.zarador.world.entity.impl.player.bot.characteristics.FoodCharacteristics;

public final class TeleportationBotSegment extends BotSegment {

	public TeleportationBotSegment(Bot bot) {
		super(bot);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canExec() {
		if (bot.getPosition().getX() == 3087 && bot.getPosition().getY() == 3498) {
			return false;
		}
		int percentageHealth = (int) getHealthPercentage();
		int backAmount = chars().getBackAmount().orElse(-1);
		boolean hasFood = false;
		FoodCharacteristics  food = chars().getFood();
		if (food.getId().isPresent()) {
			if (inventoryContains(food.getId().getAsInt())) {
				hasFood = true;
			}
		}
		if (backAmount != -1 && percentageHealth <= backAmount && hasFood) {
			return true;
		}
		if(!hasFood)
			return true;
		return false;
	}

	@Override
	public int execute() {
		return teleport();
	}

	

}
