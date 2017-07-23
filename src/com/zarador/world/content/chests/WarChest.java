package com.zarador.world.content.chests;

import com.zarador.engine.task.Task;
import com.zarador.engine.task.TaskManager;
import com.zarador.model.Animation;
import com.zarador.model.GameObject;
import com.zarador.model.Item;
import com.zarador.util.Misc;
import com.zarador.world.entity.impl.player.Player;

public class WarChest {

	public static void handleChest(final Player p, final GameObject chest) {
		if (!p.getClickDelay().elapsed(3000))
			return;
		if (!p.getInventory().contains(18236)) {
			p.getPacketSender().sendMessage("This chest can only be opened with a War key.");
			return;
		}
		if(p.getInventory().getFreeSlots() < 1) {
			p.getPacketSender().sendMessage("You need atleast 1 free inventory slots to open this chest.");
			return;
		}
		p.performAnimation(new Animation(827));
		p.getInventory().delete(18236, 1);
		p.getPacketSender().sendMessage("You open the chest..");
		p.setUsingChest(true);
		TaskManager.submit(new Task(1, p, false) {
			int tick = 0;

			@Override
			public void execute() {
				switch (tick) {
				case 2:
					Item[] loot = itemRewards[Misc.getRandom(itemRewards.length - 1)];
					for (Item item : loot) {
						p.getInventory().add(item);
					}
					p.getPacketSender().sendMessage("..and find some items!");
					p.setUsingChest(false);
					stop();
					break;
				}
				tick++;
			}
		});
		p.getClickDelay().reset();
	}

	private static final Item[][] itemRewards = {

			//Promethium
			{new Item(15771, 1)},
			{new Item(16291, 1)},
			{new Item(16313, 1)},
			{new Item(16357, 1)},
			{new Item(16379, 1)},
			{new Item(16401, 1)},
			{new Item(16423, 1)},
			{new Item(16665, 1)},
			{new Item(16687, 1)},
			{new Item(16709, 1)},
			{new Item(16731, 1)},
			{new Item(16829, 1)},
			{new Item(16907, 1)},
			{new Item(16953, 1)},
			{new Item(17037, 1)},
			{new Item(17135, 1)},
			{new Item(17257, 1)},
			{new Item(17359, 1)},

			//Gorgonite
			{new Item(15769, 1)},
			{new Item(16289, 1)},
			{new Item(16311, 1)},
			{new Item(16355, 1)},
			{new Item(16377, 1)},
			{new Item(16399, 1)},
			{new Item(16421, 1)},
			{new Item(16663, 1)},
			{new Item(16685, 1)},
			{new Item(16707, 1)},
			{new Item(16729, 1)},
			{new Item(16821, 1)},
			{new Item(16905, 1)},
			{new Item(16951, 1)},
			{new Item(16963, 1)},
			{new Item(16965, 1)},
			{new Item(16971, 1)},
			{new Item(17035, 1)},
			{new Item(17127, 1)},
			{new Item(17255, 1)},
			{new Item(17357, 1)},

			//Katagon
			{new Item(15767, 1)},
			{new Item(16287, 1)},
			{new Item(16309, 1)},
			{new Item(16353, 1)},
			{new Item(16375, 1)},
			{new Item(16397, 1)},
			{new Item(16419, 1)},
			{new Item(16661, 1)},
			{new Item(16683, 1)},
			{new Item(16705, 1)},
			{new Item(16727, 1)},
			{new Item(16813, 1)},
			{new Item(16903, 1)},
			{new Item(16949, 1)},
			{new Item(17033, 1)},
			{new Item(17119, 1)},
			{new Item(17253, 1)},
			{new Item(17355, 1)},

			//Argonite
			{new Item(15765, 1)},
			{new Item(16285, 1)},
			{new Item(16307, 1)},
			{new Item(16351, 1)},
			{new Item(16373, 1)},
			{new Item(16395, 1)},
			{new Item(16417, 1)},
			{new Item(16659, 1)},
			{new Item(16681, 1)},
			{new Item(16703, 1)},
			{new Item(16725, 1)},
			{new Item(16805, 1)},
			{new Item(16901, 1)},
			{new Item(16947, 1)},
			{new Item(17031, 1)},
			{new Item(17111, 1)},
			{new Item(17251, 1)},
			{new Item(17353, 1)},
			{new Item(17353, 1)},

	};

}
