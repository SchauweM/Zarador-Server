package com.zarador.world.content.chests;

import com.zarador.engine.task.Task;
import com.zarador.engine.task.TaskManager;
import com.zarador.model.Animation;
import com.zarador.model.GameObject;
import com.zarador.model.Item;
import com.zarador.model.player.GameMode;
import com.zarador.util.Misc;
import com.zarador.world.World;
import com.zarador.world.content.CustomObjects;
import com.zarador.world.entity.impl.player.Player;

/**
 * Handles the Treasure Chests at Treasure Island
 * Requires all 4 keys in order to open
 * @Author Jonny
 */
public class TreasureChest {

	/**
	 * Open the chest
	 * @param p
	 * @param chest
	 */
	public static void handleChest(final Player p, final GameObject chest) {
		int objectId = chest.getId();
		if(objectId != 10621 && objectId != 24204) {
			if (!p.getClickDelay().elapsed(3000))
				return;
		}
		if (!p.getInventory().contains(14678) || !p.getInventory().contains(18689) || !p.getInventory().contains(13758) || !p.getInventory().contains(13158)) {
			p.getPacketSender().sendMessage("You can only open this chest with 1 key from each boss. (Total of 4 keys)");
			return;
		}
		p.performAnimation(new Animation(827));
		p.getInventory().delete(14678, 1);
		p.getInventory().delete(18689, 1);
		p.getInventory().delete(13758, 1);
		p.getInventory().delete(13158, 1);
		p.getPacketSender().sendMessage("You open the treasure chest..");
		if(objectId == 10621 || objectId == 24204) {
			handleLoot(p);
		} else {
			TaskManager.submit(new Task(1, p, false) {
				int tick = 0;

				@Override
				public void execute() {
					switch (tick) {
						case 1:
							if (objectId == 29577) {
								CustomObjects.objectRespawnTask(p, new GameObject(29578, chest.getPosition().copy(), 10, 2), chest, 3);
							} else if (objectId == 18804) {
								CustomObjects.objectRespawnTask(p, new GameObject(18805, chest.getPosition().copy(), 10, 2), chest, 3);
							}
							break;
						case 2:
							handleLoot(p);
							stop();
							break;
					}
					tick++;
				}
			});
			p.getClickDelay().reset();
		}
	}

	public static void handleLoot(Player p) {
		/**
		 * 2 total common rewards
		 */
		for (int i = 0; i < 1; i++) {
			Item item = commonRewards[Misc.getRandom(commonRewards.length - 1)];
			int amount = item.getAmount();

			//Randomize amount
			if(item.getAmount() >= 2) {
				amount = Misc.inclusiveRandom(amount / 2, amount);
			}

			p.getInventory().add(item.getId(), amount);
		}

		if(Misc.inclusiveRandom(1, 5) == 2) { // 1/5 chance
			p.getInventory().add(uncommonRewards[Misc.getRandom(uncommonRewards.length - 1)]);
		}

		/**
		 * Knight = 1/250
		 * Realism = 1/162
		 * Ironman = 1/125
		 */
		int chance = 250;
		if(p.getGameModeAssistant().getGameMode() == GameMode.REALISM) {
			chance = 162;
		} else if(p.getGameModeAssistant().getGameMode() == GameMode.IRONMAN) {
			chance = 125;
		}
		if(Misc.inclusiveRandom(1, chance) == 2) {
			Item item = rareRewards[Misc.getRandom(rareRewards.length - 1)];
			p.getInventory().add(item);
			sendAnnouncment(p.getUsername(), item);
		}

		p.getPacketSender().sendMessage("..and find some loot!");
	}

	/**
	 * Checks to see if the item should get an announcement then processes if it can.
	 * @param playerName
	 * @param item
	 */
	public static void sendAnnouncment(String playerName, Item item) {
		World.sendMessage("<icon=1><shad=FF8C38>[News] " + playerName + " has received a " + item.getDefinition().getName() + " from Treasure Island.");
	}

	/**
	 * All common rewards
	 */
	public static final Item[] commonRewards = {
		new Item(527, 100), //Regular bones
		new Item(533, 50), //Big bones
		new Item(537, 25), //Dragon bones
		new Item(18831, 10), //Frost dragon bones
		new Item(9244, 75), //Dragon bolts (e)
		new Item(9242, 75), //Ruby bolts (e)
		new Item(9243, 75), //Diamond bolts (e)
		new Item(1632, 1), //Uncut dragonstone
		new Item(1617, 1), //Uncut diamond
		new Item(1619, 1), //Uncut ruby
		new Item(1319, 1), //Rune 2h sword
		new Item(7158, 1), //Dragon 2h sword
		new Item(4153, 1), //Granite maul
		new Item(2, 500), //Cannonballs
		new Item(565, 500), //Blood runes
		new Item(560, 500), //Death runes
		new Item(9075, 500), //Astral runes
		new Item(14428, 25), //Saradomin brew flask (6)
		new Item(14416, 25), //Super restore flask (6)
		new Item(384, 50), //Raw shark
		new Item(15271, 25), //Raw rocktail
		new Item(1359, 1), //Rune hatchet
		new Item(1275, 1), //Rune pickaxe
		new Item(1333, 1), //Rune scimitar
		new Item(4587, 1), //Dragon scimitar
		new Item(2364, 15), //Rune bar
		new Item(2362, 25), //Adamant bar
		new Item(2360, 50), //Mithril bar
		new Item(995, 5000000), //Coins
		new Item(13664, 1), //$50 lottery ticket
	};

	/**
	 * All uncommon rewards
	 */
	public static final Item[] uncommonRewards = {
		new Item(2615, 1), //Rune platebody (g)
		new Item(2623, 1), //Rune platebody (t)
		new Item(2617, 1), //Rune platelegs (g)
		new Item(2625, 1), //Rune platelegs (t)
		new Item(2619, 1), //Rune full helm (g)
		new Item(2627, 1), //Rune platelegs (t)
		new Item(2621, 1), //Rune kiteshield (g)
		new Item(2629, 1), //Rune kiteshield (t)
		new Item(2653, 1), //Zamorak platebody
		new Item(2655, 1), //Zamorak platelegs
		new Item(2657, 1), //Zamorak full helm
		new Item(2659, 1), //Zamorak kite
		new Item(2669, 1), //Guthix platebody
		new Item(2671, 1), //Guthix platelegs
		new Item(2673, 1), //Guthix full helm
		new Item(2675, 1), //Guthix kite
		new Item(2661, 1), //Saradomin plate
		new Item(2663, 1), //Saradomin legs
		new Item(2665, 1), //Saradomin full
		new Item(2667, 1), //Saradomin kite
		new Item(2639, 1), //Tan cavalier
		new Item(2641, 1), //Dark cavalier
		new Item(2643, 1), //Black cavalier
		new Item(2631, 1), //Highwayman mask
		new Item(7319, 1), //Red boater
		new Item(7321, 1), //Orange boater
		new Item(7323, 1), //Green boater
		new Item(7325, 1), //Blue boater
		new Item(7327, 1), //Black boater
		new Item(10400, 1), //Black elegant shirt
		new Item(10402, 1), //Black elegant legs
		new Item(10404, 1), //Red elegant shirt
		new Item(10406, 1), //Red elegant legs
		new Item(10408, 1), //Blue elegant shirt
		new Item(10410, 1), //Blue elegant legs
		new Item(10412, 1), //Green elegant shirt
		new Item(10414, 1), //Green elegant legs
		new Item(10416, 1), //Purple elegant shirt
		new Item(10418, 1), //Purple elegant legs
		new Item(10420, 1), //White elegant shirt
		new Item(10422, 1), //White elegant legs
		new Item(19368, 1), //Armadyl cloak
		new Item(19370, 1), //Bandos cloak
		new Item(19372, 1), //Ancient cloak
		new Item(10446, 1), //Saradomin cloak
		new Item(10448, 1), //Guthix cloak
		new Item(10450, 1), //Zamorak cloak
		new Item(2579, 1), //Wizard boots
		new Item(2645, 1), //Red headband
		new Item(2647, 1), //Black headband
		new Item(2649, 1), //Brown headband
		new Item(19281, 1), //Green dragon mask
		new Item(19284, 1), //Blue dragon mask
		new Item(19287, 1), //Red dragon mask
		new Item(19290, 1), //Black dragon mask
		new Item(19293, 1), //Frost dragon mask
		new Item(19296, 1), //Bronze dragon mask
		new Item(19299, 1), //Iron dragon mask
		new Item(19302, 1), //Steel dragon mask
		new Item(19305, 1), //Mithril dragon mask
		new Item(13099, 1), //Rune cane
		new Item(10362, 1), //Amulet of glory (t)
		new Item(11716, 1), //Zamorakian spear
	};

	/**
	 * All rare rewards
	 */
	public static final Item[] rareRewards = {
		new Item(21089, 1), //Drygore longsword
		new Item(21091, 1), //Drygore rapier
		new Item(21100, 1), //Drygore mace
		new Item(10330, 1), //3rd age range top
		new Item(10332, 1), //3rd age range legs
		new Item(10334, 1), //3rd age range coif
		new Item(10336, 1), //3rd age vambraces
		new Item(10338, 1), //3rd age robe top
		new Item(10340, 1), //3rd age robe
		new Item(10342, 1), //3rd age mage hat
		new Item(10344, 1), //3rd age amulet
		new Item(10346, 1), //3rd age platelegs
		new Item(10348, 1), //3rd age platebody
		new Item(10350, 1), //3rd age full helmet
		new Item(10352, 1), //3rd age kiteshield
		new Item(1053, 1),
		new Item(1055, 1),
		new Item(1057, 1),
		new Item(21027, 1),
		new Item(21028, 1),
		new Item(21029, 1),
		new Item(21030, 1),
		new Item(21031, 1),
		new Item(21032, 1),
		new Item(21033, 1),
		new Item(21034, 1),
	};

}
