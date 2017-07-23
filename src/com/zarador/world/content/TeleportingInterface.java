package com.zarador.world.content;

import org.scripts.kotlin.content.dialog.teleports.BossTeleports;
import org.scripts.kotlin.content.dialog.teleports.Minigames;
import org.scripts.kotlin.content.dialog.teleports.TrainingTeleports;

import com.zarador.model.Position;
import com.zarador.model.Skill;
import com.zarador.util.Misc;
import com.zarador.world.content.transportation.TeleportHandler;
import com.zarador.world.entity.impl.player.Player;

/**
 * Handles all stuff for the teleporting interface
 * @Author Jonny
 */
public class TeleportingInterface {

	/**
	 * Open the interface depending on which 'type'
	 * @param player
	 * @param type
	 */
	public static void showInterface(Player player, String type) {
		player.setTeleportType(type);
		switch(type) {
			case "cities":
				player.getPacketSender().sendInterface(60700);
				player.getPacketSender().sendString(60706, "Cities");
				player.getPacketSender().sendString(60662, "Varrock");
				player.getPacketSender().sendString(60663, "");
				player.getPacketSender().sendString(60664, "Falador");
				player.getPacketSender().sendString(60665, "");
				player.getPacketSender().sendString(60666, "Lumbridge");
				player.getPacketSender().sendString(60667, "");
				player.getPacketSender().sendString(60668, "Draynor");
				player.getPacketSender().sendString(60669, "");
				player.getPacketSender().sendString(60670, "Ardougne");
				player.getPacketSender().sendString(60671, "");
				player.getPacketSender().sendString(60672, "Camelot");
				player.getPacketSender().sendString(60673, "");
				player.getPacketSender().sendString(60674, "Yanille");
				player.getPacketSender().sendString(60675, "");
				player.getPacketSender().sendString(60676, "Catherby");
				player.getPacketSender().sendString(60677, "");
				player.getPacketSender().sendString(60678, "Al Kharid");
				player.getPacketSender().sendString(60679, "");
				player.getPacketSender().sendString(18374, "Burthorpe");
				player.getPacketSender().sendString(18375, "");
				player.getPacketSender().sendString(60702, "Canifis");
				player.getPacketSender().sendString(60703, "");
				player.getPacketSender().sendString(60704, "Entrana");
				player.getPacketSender().sendString(60705, "");
				break;
			case "skilling":
				player.getPacketSender().sendInterface(60800);
				player.getPacketSender().sendString(60706, "Skilling Areas");
				player.getPacketSender().sendString(60662, "Catherby");
				player.getPacketSender().sendString(60663, "");
				player.getPacketSender().sendString(60664, "Low Skilling");
				player.getPacketSender().sendString(60665, "Zone");
				player.getPacketSender().sendString(60666, "High Skilling");
				player.getPacketSender().sendString(60667, "Zone");
				player.getPacketSender().sendString(60668, "Living Rock");
				player.getPacketSender().sendString(60669, "Cavern");
				player.getPacketSender().sendString(60670, "");
				player.getPacketSender().sendString(60671, "");
				player.getPacketSender().sendString(60672, "");
				player.getPacketSender().sendString(60673, "");
				player.getPacketSender().sendString(60674, "");
				player.getPacketSender().sendString(60675, "");
				player.getPacketSender().sendString(60676, "");
				player.getPacketSender().sendString(60677, "");
				player.getPacketSender().sendString(60678, "");
				player.getPacketSender().sendString(60679, "");
				player.getPacketSender().sendString(18374, "");
				player.getPacketSender().sendString(18375, "");
				player.getPacketSender().sendString(60702, "");
				player.getPacketSender().sendString(60703, "");
				player.getPacketSender().sendString(60704, "");
				player.getPacketSender().sendString(60705, "");
				break;
			case "minigames":
				player.getPacketSender().sendInterface(60900);
				player.getPacketSender().sendString(60706, "Minigames");
				player.getPacketSender().sendString(60662, "Warriors");
				player.getPacketSender().sendString(60663, "Guild");
				player.getPacketSender().sendString(60664, "Pest");
				player.getPacketSender().sendString(60665, "Control");
				player.getPacketSender().sendString(60666, "Duel");
				player.getPacketSender().sendString(60667, "Arena");
				player.getPacketSender().sendString(60668, "Barrows");
				player.getPacketSender().sendString(60669, "");
				player.getPacketSender().sendString(60670, "TzHaar Fight");
				player.getPacketSender().sendString(60671, "Cave");
				player.getPacketSender().sendString(60672, "TzHaar Fight");
				player.getPacketSender().sendString(60673, "Pit");
				player.getPacketSender().sendString(60674, "Treasure");
				player.getPacketSender().sendString(60675, "Island");
				player.getPacketSender().sendString(60676, "");
				player.getPacketSender().sendString(60677, "");
				player.getPacketSender().sendString(60678, "");
				player.getPacketSender().sendString(60679, "");
				player.getPacketSender().sendString(18374, "");
				player.getPacketSender().sendString(18375, "");
				player.getPacketSender().sendString(60702, "");
				player.getPacketSender().sendString(60703, "");
				player.getPacketSender().sendString(60704, "");
				player.getPacketSender().sendString(60705, "");
				break;
			case "monsters":
				player.getPacketSender().sendInterface(61000);
				player.getPacketSender().sendString(60706, "Monsters");
				player.getPacketSender().sendString(60662, "Rock Crabs");
				player.getPacketSender().sendString(60663, "");
				player.getPacketSender().sendString(60664, "Experiments");
				player.getPacketSender().sendString(60665, "");
				player.getPacketSender().sendString(60666, "Yaks");
				player.getPacketSender().sendString(60667, "");
				player.getPacketSender().sendString(60668, "Bandits");
				player.getPacketSender().sendString(60669, "");
				player.getPacketSender().sendString(60670, "Slayer Tower");
				player.getPacketSender().sendString(60671, "");
				player.getPacketSender().sendString(60672, "Glacors");
				player.getPacketSender().sendString(60673, "");
				player.getPacketSender().sendString(60674, "Dagannoth");
				player.getPacketSender().sendString(60675, "Kings");
				player.getPacketSender().sendString(60676, "Naza");
				player.getPacketSender().sendString(60677, "Boss");
				player.getPacketSender().sendString(60678, "Dust Devils");
				player.getPacketSender().sendString(60679, "");
				player.getPacketSender().sendString(18374, "Barrel");
				player.getPacketSender().sendString(18375, "Chest");
				player.getPacketSender().sendString(60702, "Chaos");
				player.getPacketSender().sendString(60703, "Druids");
				player.getPacketSender().sendString(60704, "ChickenPen");
				player.getPacketSender().sendString(60705, "");
				break;
			case "dungeons":
				player.getPacketSender().sendInterface(61100);
				player.getPacketSender().sendString(60706, "Dungeons");
				player.getPacketSender().sendString(60662, "Edgeville");
				player.getPacketSender().sendString(60663, "Dungeon");
				player.getPacketSender().sendString(60664, "Taverley");
				player.getPacketSender().sendString(60665, "Dungeon");
				player.getPacketSender().sendString(60666, "Brimhaven");
				player.getPacketSender().sendString(60667, "Dungeon");
				player.getPacketSender().sendString(60668, "Ancient");
				player.getPacketSender().sendString(60669, "Cavern");
				player.getPacketSender().sendString(60670, "Strykewyrm");
				player.getPacketSender().sendString(60671, "Cavern");
				player.getPacketSender().sendString(60672, "Ancient Guthix");
				player.getPacketSender().sendString(60673, "Temple");
				player.getPacketSender().sendString(60674, "Kuradal's");
				player.getPacketSender().sendString(60675, "Dungeon");
				player.getPacketSender().sendString(60676, "");
				player.getPacketSender().sendString(60677, "");
				player.getPacketSender().sendString(60678, "");
				player.getPacketSender().sendString(60679, "");
				player.getPacketSender().sendString(18374, "");
				player.getPacketSender().sendString(18375, "");
				player.getPacketSender().sendString(60702, "");
				player.getPacketSender().sendString(60703, "");
				player.getPacketSender().sendString(60704, "");
				player.getPacketSender().sendString(60705, "");
				break;
			case "bosses":
				player.getPacketSender().sendInterface(60600);
				player.getPacketSender().sendString(60706, "Bosses");
				player.getPacketSender().sendString(60662, "Godwars");
				player.getPacketSender().sendString(60663, "");
				player.getPacketSender().sendString(60664, "Ganodermic");
				player.getPacketSender().sendString(60665, "Beast");
				player.getPacketSender().sendString(60666, "Abyssal");
				player.getPacketSender().sendString(60667, "Sire");
				player.getPacketSender().sendString(60668, "Corporeal");
				player.getPacketSender().sendString(60669, "Beast");
				player.getPacketSender().sendString(60670, "Kraken");
				player.getPacketSender().sendString(60671, "");
				player.getPacketSender().sendString(60672, "Skotizo");
				player.getPacketSender().sendString(60673, "");
				player.getPacketSender().sendString(60674, "Nex");
				player.getPacketSender().sendString(60675, "");
				player.getPacketSender().sendString(60676, "Cerberus");
				player.getPacketSender().sendString(60677, "");
				player.getPacketSender().sendString(60678, "Kalphite");
				player.getPacketSender().sendString(60679, "Queen");
				player.getPacketSender().sendString(18374, "Phoenix");
				player.getPacketSender().sendString(18375, "");
				player.getPacketSender().sendString(60702, "Bandos");
				player.getPacketSender().sendString(60703, "Avatar");
				player.getPacketSender().sendString(60704, "Bork");
				player.getPacketSender().sendString(60705, "");
				break;
		}
	}

	public static boolean isButton(Player player, int buttonId) {
		switch(buttonId) {
			/**
			 * Teleport 1
			 */
			case 60622:
				switch(player.getTeleportType()) {
					case "cities":
						/**
						 * Varrock
						 */
						TeleportHandler.teleportPlayer(player, new Position(3213, 3430, 0), player.getSpellbook().getTeleportType());
						return true;
					case "skilling":
						/**
						 * Catherby
						 */
						TeleportHandler.teleportPlayer(player, new Position(2809, 3435, 0), player.getSpellbook().getTeleportType());
						return true;
					case "minigames":
						/**
						 * Warriors Guild
						 */
						if(player.getSkillManager().getCurrentLevel(Skill.ATTACK) + player.getSkillManager().getCurrentLevel(Skill.STRENGTH) < 130) {
							player.getPacketSender().sendMessage("A true warrior requires a total of 130 Strength and Attack.");
							return true;
						}
						TeleportHandler.teleportPlayer(player, new Position(2873, 3546, 0), player.getSpellbook().getTeleportType());
						return true;
					case "monsters":
						/**
						 * Rock crabs
						 */
						player.getDialog().sendDialog(new TrainingTeleports(player, 1));
						return true;
					case "dungeons":
						/**
						 * Edgeville Dungeon
						 */
						TeleportHandler.teleportPlayer(player, new Position(3097, 9882, 0), player.getSpellbook().getTeleportType());
						return true;
					case "bosses":
						/**
						 * Godwars
						 */
						player.getDialog().sendDialog(new BossTeleports(player, 3));
						return true;

				}
				break;

			/**
			 * Teleport 2
			 */
			case 60625:
				switch(player.getTeleportType()) {
					case "cities":
						/**
						 * Falador
						 */
						TeleportHandler.teleportPlayer(player, new Position(2965, 3378, 0), player.getSpellbook().getTeleportType());
						return true;
					case "skilling":
						/**
						 * Low Skilling Zone
						 */
						TeleportHandler.teleportPlayer(player, new Position(2802, 2785, 0), player.getSpellbook().getTeleportType());
						return true;
					case "minigames":
						/**
						 * Pest Control
						 */
						TeleportHandler.teleportPlayer(player, new Position(2661, 2649, 0), player.getSpellbook().getTeleportType());
						return true;
					case "monsters":
						/**
						 * Experiments
						 */
						TeleportHandler.teleportPlayer(player, new Position(3559, 9946, 0), player.getSpellbook().getTeleportType());
						return true;
					case "dungeons":
						/**
						 * Taverley Dungeon
						 */
						TeleportHandler.teleportPlayer(player, new Position(2884, 9799, 0), player.getSpellbook().getTeleportType());
						return true;
					case "bosses":
						/**
						 * Ganodermic Beast
						 */
						TeleportHandler.teleportPlayer(player, new Position(2245, 3182, 0), player.getSpellbook().getTeleportType());
						return true;
				}
				break;

			/**
			 * Teleport 3
			 */
			case 60628:
				switch(player.getTeleportType()) {
					case "cities":
						/**
						 * Lumbridge
						 */
						TeleportHandler.teleportPlayer(player, new Position(3222, 3219, 0), player.getSpellbook().getTeleportType());
						return true;
					case "skilling":
						/**
						 * High Skilling Zone
						 */
						if (player.getSkillManager().getTotalLevel() >= 1000) {
							TeleportHandler.teleportPlayer(player, new Position(2852, 2960, 0), player.getSpellbook().getTeleportType());
						} else {
							player.getPacketSender().sendMessage("@red@You need to have over 1000 total level to teleport to this zone.");
						}
						return true;
					case "minigames":
						/**
						 * Duel Arena
						 */
						player.getDialog().sendDialog(new Minigames(player, 2));
						return true;
					case "monsters":
						/**
						 * Yaks
						 */
						player.getDialog().sendDialog(new TrainingTeleports(player, 2));
						return true;
					case "dungeons":
						/**
						 * Brimhaven Dungeon
						 */
						TeleportHandler.teleportPlayer(player, new Position(2697, 9564, 0), player.getSpellbook().getTeleportType());
						return true;
					case "bosses":
						/**
						 * Slash Bash
						 */
						TeleportHandler.teleportPlayer(player, new Position(2516, 4636, 0), player.getSpellbook().getTeleportType());
						return true;
				}
				break;

			/**
			 * Teleport 4
			 */
			case 60631:
				switch(player.getTeleportType()) {
					case "cities":
						/**
						 * Draynor
						 */
						TeleportHandler.teleportPlayer(player, new Position(3104, 3249, 0), player.getSpellbook().getTeleportType());
						return true;
					case "skilling":
						/**
						 * Living Rock Cavern
						 */
						TeleportHandler.teleportPlayer(player, new Position(3654, 5114, 0), player.getSpellbook().getTeleportType());
						return true;
					case "minigames":
						/**
						 * Barrows
						 */
						TeleportHandler.teleportPlayer(player, new Position(3565, 3313, 0), player.getSpellbook().getTeleportType());
						return true;
					case "monsters":
						/**
						 * Bandits
						 */
						TeleportHandler.teleportPlayer(player, new Position(3172, 2981, 0), player.getSpellbook().getTeleportType());
						return true;
					case "dungeons":
						/**
						 * Ancient Cavern
						 */
						TeleportHandler.teleportPlayer(player, new Position(1748, 5325, 0), player.getSpellbook().getTeleportType());
						return true;
					case "bosses":
						/**
						 * Corporeal Beast
						 */
						TeleportHandler.teleportPlayer(player, new Position(2916, 4384, 0), player.getSpellbook().getTeleportType());
						return true;
				}
				break;

			/**
			 * Teleport 5
			 */
			case 60634:
				switch(player.getTeleportType()) {
					case "cities":
						/**
						 * Ardougne
						 */
						TeleportHandler.teleportPlayer(player, new Position(2662, 3306, 0), player.getSpellbook().getTeleportType());
						return true;
					case "minigames":
						/**
						 * Tzhaar Fight Cave
						 */
						TeleportHandler.teleportPlayer(player, new Position(2445, 5170, 0), player.getSpellbook().getTeleportType());
						return true;
					case "monsters":
						/**
						 * Slayer Tower
						 */
						TeleportHandler.teleportPlayer(player, new Position(3427, 3537, 0), player.getSpellbook().getTeleportType());
						return true;
					case "dungeons":
						/**
						 * Strykewyrm Cavern
						 */
						TeleportHandler.teleportPlayer(player, new Position(2731, 5095, 0), player.getSpellbook().getTeleportType());
						return true;
					case "bosses":
						/**
						 * Kraken
						 */
						player.getKraken().enter(player, true);
						return true;
				}
				break;

			/**
			 * Teleport 6
			 */
			case 60637:
				switch(player.getTeleportType()) {
					case "cities":
						/**
						 * Camelot
						 */
						TeleportHandler.teleportPlayer(player, new Position(2757, 3477, 0), player.getSpellbook().getTeleportType());
						return true;
					case "minigames":
						/**
						 * Tzhaar Fight Pit
						 */
						TeleportHandler.teleportPlayer(player, new Position(2399, 5177, 0), player.getSpellbook().getTeleportType());
						return true;
					case "monsters":
						/**
						 * Glacors
						 */
						TeleportHandler.teleportPlayer(player, new Position(3050, 9573, 0), player.getSpellbook().getTeleportType());
						return true;
					case "dungeons":
						/**
						 * Ancient Guthix Temple
						 */
						TeleportHandler.teleportPlayer(player, new Position(2570, 5735, 0), player.getSpellbook().getTeleportType());
						return true;
					case "bosses":
						/**
						 * Skotizo
						 */
						int random = Misc.inclusiveRandom(1, 4);
						switch(random) {
							case 1:
								TeleportHandler.teleportPlayer(player, new Position(2560, 4947, 0), player.getSpellbook().getTeleportType());
								break;
							case 2:
								TeleportHandler.teleportPlayer(player, new Position(2572, 4959, 0), player.getSpellbook().getTeleportType());
								break;
							case 3:
								TeleportHandler.teleportPlayer(player, new Position(2559, 4972, 0), player.getSpellbook().getTeleportType());
								break;
							case 4:
								TeleportHandler.teleportPlayer(player, new Position(2549, 4960, 0), player.getSpellbook().getTeleportType());
								break;
						}
						return true;
				}
				break;

			/**
			 * Teleport 7
			 */
			case 60640:
				switch(player.getTeleportType()) {
					case "cities":
						/**
						 * Yanille
						 */
						TeleportHandler.teleportPlayer(player, new Position(2605, 3097, 0), player.getSpellbook().getTeleportType());
						return true;
					case "minigames":
						/**
						 * Treasure Island
						 */
						TeleportHandler.teleportPlayer(player, new Position(3039 + Misc.inclusiveRandom(0, 1), 2912, 0), player.getSpellbook().getTeleportType());
						return true;
					case "monsters":
						/**
						 * Dagannoth Kings
						 */
						TeleportHandler.teleportPlayer(player, new Position(1909, 4367, 0), player.getSpellbook().getTeleportType());
						return true;
					case "dungeons":
						/**
						 * Kuradal's Dungeon
						 */
						TeleportHandler.teleportPlayer(player, new Position(1659, 5257, 0), player.getSpellbook().getTeleportType());
						return true;
					case "bosses":
						/**
						 * Nex
						 */
						TeleportHandler.teleportPlayer(player, new Position(2903, 5204, 0), player.getSpellbook().getTeleportType());
						return true;
				}
				break;

			/**
			 * Teleport 8
			 */
			case 60643:
				switch(player.getTeleportType()) {
					case "cities":
						/**
						 * Catherby
						 */
						TeleportHandler.teleportPlayer(player, new Position(2813, 3447, 0), player.getSpellbook().getTeleportType());
						return true;
					case "bosses":
						/**
						 * Cerberus
						 */
						TeleportHandler.teleportPlayer(player, new Position(1240, 1226, 0), player.getSpellbook().getTeleportType());
						return true;
				}
				break;

			/**
			 * Teleport 9
			 */
			case 60646:
				switch(player.getTeleportType()) {
					case "cities":
						/**
						 * Al Kharid
						 */
						TeleportHandler.teleportPlayer(player, new Position(3276, 3166, 0), player.getSpellbook().getTeleportType());
						return true;
					case "bosses":
						/**
						 * Kalphite Queen
						 */
						TeleportHandler.teleportPlayer(player, new Position(3508, 9492, 0), player.getSpellbook().getTeleportType());
						return true;
				}
				break;

			/**
			 * Teleport 10
			 */
			case 60691:
				switch(player.getTeleportType()) {
					case "cities":
						/**
						 * Burthorpe
						 */
						TeleportHandler.teleportPlayer(player, new Position(2899, 3546, 0), player.getSpellbook().getTeleportType());
						return true;
					case "bosses":
						/**
						 * Phoenix
						 */
						TeleportHandler.teleportPlayer(player, new Position(2839, 9557, 0), player.getSpellbook().getTeleportType());
						return true;
				}
				break;

			/**
			 * Teleport 11
			 */
			case 60694:
				switch(player.getTeleportType()) {
					case "cities":
						/**
						 * Canifis
						 */
						TeleportHandler.teleportPlayer(player, new Position(3494, 3483, 0), player.getSpellbook().getTeleportType());
						return true;
					case "bosses":
						/**
						 * Bandos Avatar
						 */
						TeleportHandler.teleportPlayer(player, new Position(2891, 4767, 0), player.getSpellbook().getTeleportType());
						return true;
				}
				break;

			/**
			 * Teleport 12
			 */
			case 60697:
				switch(player.getTeleportType()) {
					case "cities":
						/**
						 * Entrana
						 */
						TeleportHandler.teleportPlayer(player, new Position(2827, 3344, 0), player.getSpellbook().getTeleportType());
						return true;
					case "bosses":
						/**
						 * Bork
						 */
						TeleportHandler.teleportPlayer(player, new Position(3102, 2965, 0), player.getSpellbook().getTeleportType());
						return true;
				}
				break;
		}
		return false;
	}

}
