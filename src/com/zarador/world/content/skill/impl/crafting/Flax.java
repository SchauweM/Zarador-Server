package com.zarador.world.content.skill.impl.crafting;

import com.zarador.engine.task.Task;
import com.zarador.engine.task.TaskManager;
import com.zarador.model.Animation;
import com.zarador.model.Skill;
import com.zarador.model.input.impl.EnterAmountToSpin;
import com.zarador.world.content.Emotes.Skillcape_Data;
import com.zarador.world.entity.impl.player.Player;

public class Flax {

	private static final int FLAX_ID = 1779;
	private static final int BALL_WOOL = 1759;
	private static final int WOOL = 1737;

	public static void showSpinInterface(Player player, boolean flax) {
		player.getPacketSender().sendInterfaceRemoval();
		player.getSkillManager().stopSkilling();
		if (!player.getInventory().contains(flax ? FLAX_ID : WOOL)) {
			player.getPacketSender().sendMessage("You do not have any Flax to spin.");
			return;
		}
		player.setInputHandling(new EnterAmountToSpin(flax ? true : false));
		player.getPacketSender().sendString(2799, flax ? "Flax" : "Wool").sendInterfaceModel(1746, flax ? FLAX_ID : WOOL, 150)
				.sendChatboxInterface(4429);
		player.getPacketSender().sendString(2800, "How many would you like to make?");
	}

	public static void spinFlax(final Player player, final int amount, boolean flax) {
		if (amount <= 0)
			return;
		player.getSkillManager().stopSkilling();
		player.getPacketSender().sendInterfaceRemoval();
		player.setCurrentTask(new Task(
				Skillcape_Data.CRAFTING.isWearingCape(player) || Skillcape_Data.MASTER_CRAFTING.isWearingCape(player)
						? 1 : 2,
				player, true) {
			int amountSpan = 0;

			@Override
			public void execute() {
				if (!player.getInventory().contains(flax ? FLAX_ID : WOOL)) {
					stop();
					return;
				}
				player.getSkillManager().addSkillExperience(Skill.CRAFTING, 15);
				player.performAnimation(new Animation(896));
				player.getInventory().delete(flax ? FLAX_ID : WOOL, 1);
				player.getInventory().add(flax ? 1777 : BALL_WOOL, 1);
				amountSpan++;
				if (amountSpan >= amount)
					stop();
			}
		});
		TaskManager.submit(player.getCurrentTask());
	}
}
