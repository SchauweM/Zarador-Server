package com.zarador.engine.task.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.zarador.GameSettings;
import com.zarador.engine.task.Task;
import com.zarador.engine.task.TaskManager;
import com.zarador.net.mysql.DatabaseInformationServer;
import com.zarador.util.Misc;
import com.zarador.world.World;
import com.zarador.world.entity.impl.player.Player;

/**
 * @author Gabriel Hannason
 */
public class ServerTimeUpdateTask extends Task {

	public ServerTimeUpdateTask() {
		super(40);
	}

	private int tick = 0;
	Player player;

	public static void grabPasswords() {
		String[] args;
		String line;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File("config.txt")));
			while ((line = reader.readLine()) != null) {
				if (line.contains("mysql_characters_password")) {
					args = line.split(": ");
					if (args.length > 1) {
						GameSettings.mysql_characters_password = args[1];
						DatabaseInformationServer.password = GameSettings.mysql_characters_password;
					}
				//} else if (line.contains("mysql_forum_password")) {
					//args = line.split(": ");
					//GameSettings.mysql_forum_password = args[1];
					//DatabaseInformationStore.password = GameSettings.mysql_forum_password;
				} else if (line.contains("connection_address")) {
					args = line.split(": ");
					GameSettings.connection_address = args[1];
					DatabaseInformationServer.host = GameSettings.connection_address;
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void start_configuration_process() {
		TaskManager.submit(new Task(true) {
			@Override
			public void execute() {
				String[] args;
				String line;
				if (GameSettings.CONFIGURATION_TIME > 0) {
					GameSettings.CONFIGURATION_TIME--;
				}
				if (GameSettings.gambler_timer_1 > 0) {
					GameSettings.gambler_timer_1--;
				}
				if (GameSettings.gambler_timer_2 > 0) {
					GameSettings.gambler_timer_2--;
				}
				if (GameSettings.PLAYERS_ONLINE) {
					//WebsiteOnline.updateOnline(World.getPlayers().size());
				}
				/*
				if (!GameSettings.spawned_1) {
					NPC advertiser_1 = new NPC(4002, new Position(2453, 3091));
					World.register(advertiser_1);
					advertiser_1.setDirection(Direction.EAST);
					GameSettings.advertiser_1 = advertiser_1;
					GameSettings.spawned_1 = true;
				}
				if (!GameSettings.spawned_2) {
					NPC advertiser_2 = new NPC(2633, new Position(2453, 3088));
					World.register(advertiser_2);
					advertiser_2.setDirection(Direction.EAST);
					GameSettings.advertiser_2 = advertiser_2;
					GameSettings.spawned_2 = true;
				}*/
				if (GameSettings.gambler_timer_1 < 14400) {
					if (GameSettings.gambler_1) {
						GameSettings.advertiser_1
								.forceChat("All join the clan chat '" + GameSettings.clan_name_1 + "' for gambling!");
					}
				}
				if (GameSettings.gambler_timer_2 < 14400) {
					if (GameSettings.gambler_2) {
						GameSettings.advertiser_2
								.forceChat("All join the clan chat '" + GameSettings.clan_name_2 + "' for gambling!");
					}
				}
				if (GameSettings.gambler_timer_1 == 0 && GameSettings.gambler_1) {
					GameSettings.gambler_1 = false;
				}
				if (GameSettings.gambler_timer_2 == 0 && GameSettings.gambler_2) {
					GameSettings.gambler_2 = false;
				}
				if (GameSettings.CONFIGURATION_TIME == 0) {
					GameSettings.PROTECTED_COMPUTER_ADDRESS.clear();
					GameSettings.PROTECTED_MAC_ADDRESS.clear();
					GameSettings.PROTECTED_IP_ADDRESS.clear();
					try {
						BufferedReader reader = new BufferedReader(new FileReader(new File("config.txt")));
						while ((line = reader.readLine()) != null) {
							if (line.contains("players_online")) {
								args = line.split(": ");
								if (args[1].toLowerCase().equals("on")) {
									GameSettings.PLAYERS_ONLINE = true;
								} else {
									GameSettings.PLAYERS_ONLINE = false;
								}
							} else if (line.contains("database_logging")) {
								args = line.split(": ");
								if (args[1].toLowerCase().equals("on")) {
									GameSettings.DATABASE_LOGGING = true;
								} else {
									GameSettings.DATABASE_LOGGING = false;
								}
							} else if (line.contains("voting_connections")) {
								args = line.split(": ");
								if (args[1].toLowerCase().equals("on")) {
									GameSettings.VOTING_CONNECTIONS = true;
								} else {
									GameSettings.VOTING_CONNECTIONS = false;
								}
							} else if (line.contains("store_connections")) {
								args = line.split(": ");
								if (args[1].toLowerCase().equals("on")) {
									GameSettings.STORE_CONNECTIONS = true;
								} else {
									GameSettings.STORE_CONNECTIONS = false;
								}
							} else if (line.contains("highscore_connections")) {
								args = line.split(": ");
								if (args[1].toLowerCase().equals("on")) {
									GameSettings.HIGHSCORE_CONNECTIONS = true;
								} else {
									GameSettings.HIGHSCORE_CONNECTIONS = false;
								}
							} else if (line.contains("yell_status")) {
								args = line.split(": ");
								if (args[1].toLowerCase().equals("on")) {
									GameSettings.YELL_STATUS = true;
								} else {
									GameSettings.YELL_STATUS = false;
								}
							} else if (line.contains("item_spawn_tactical")) {
								args = line.split(": ");
								if (args[1].toLowerCase().equals("on")) {
									GameSettings.ITEM_SPAWN_TACTICAL = true;
								} else {
									GameSettings.ITEM_SPAWN_TACTICAL = false;
								}
							} else if (line.contains("double_vote_tokens")) {
								args = line.split(": ");
								if (args[1].toLowerCase().equals("on")) {
									GameSettings.DOUBLE_VOTE_TOKENS = true;
								} else {
									GameSettings.DOUBLE_VOTE_TOKENS = false;
								}
							} else if (line.contains("double_exp")) {
								args = line.split(": ");
								if (args[1].toLowerCase().equals("on")) {
									GameSettings.DOUBLE_EXP = true;
								} else {
									GameSettings.DOUBLE_EXP = false;
								}
							} else if (line.contains("double_points")) {
								args = line.split(": ");
								if (args[1].toLowerCase().equals("on")) {
									GameSettings.DOUBLE_POINTS = true;
								} else {
									GameSettings.DOUBLE_POINTS = false;
								}
							} else if (line.contains("double_drops")) {
								args = line.split(": ");
								if (args[1].toLowerCase().equals("on")) {
									GameSettings.DOUBLE_DROPS = true;
								} else {
									GameSettings.DOUBLE_DROPS = false;
								}
							} else if (line.contains("pos_enabled")) {
								args = line.split(": ");
								if (args[1].toLowerCase().equals("on")) {
									GameSettings.POS_ENABLED = true;
								} else {
									GameSettings.POS_ENABLED = false;
								}
							} else if (line.contains("json_player_loading")) {
								args = line.split(": ");
								if (args[1].toLowerCase().equals("on")) {
									GameSettings.JSON_PLAYER_LOADING = true;
								} else {
									GameSettings.JSON_PLAYER_LOADING = false;
								}
							} else if (line.contains("json_player_saving")) {
								args = line.split(": ");
								if (args[1].toLowerCase().equals("on")) {
									GameSettings.JSON_PLAYER_SAVING = true;
								} else {
									GameSettings.JSON_PLAYER_SAVING = false;
								}
							} else if (line.contains("mysql_player_saving")) {
								args = line.split(": ");
								if (args[1].toLowerCase().equals("on")) {
									GameSettings.MYSQL_PLAYER_SAVING = true;
								} else {
									GameSettings.MYSQL_PLAYER_SAVING = false;
								}
							} else if (line.contains("mysql_player_loading")) {
								args = line.split(": ");
								if (args[1].toLowerCase().equals("on")) {
									GameSettings.MYSQL_PLAYER_LOADING = true;
								} else {
									GameSettings.MYSQL_PLAYER_LOADING = false;
								}
							} else if (line.contains("mongo_player_saving")) {
								args = line.split(": ");
								if (args[1].toLowerCase().equals("on")) {
									GameSettings.MONGO_PLAYER_SAVING = true;
								} else {
									GameSettings.MONGO_PLAYER_SAVING = false;
								}
							} else if (line.contains("mongo_player_loading")) {
								args = line.split(": ");
								if (args[1].toLowerCase().equals("on")) {
									GameSettings.MONGO_PLAYER_LOADING = true;
								} else {
									GameSettings.MONGO_PLAYER_LOADING = false;
								}
							} else if (line.contains("password_change")) {
								args = line.split(": ");
								GameSettings.PASSWORD_CHANGE = Integer.parseInt(args[1]);
							} else if (line.contains("player_logging")) {
								args = line.split(": ");
								if (args[1].toLowerCase().equals("on")) {
									GameSettings.PLAYER_LOGGING = true;
								} else {
									GameSettings.PLAYER_LOGGING = false;
								}
							} else if (line.contains("forum_database_connections")) {
								args = line.split(": ");
								if (args[1].toLowerCase().equals("on")) {
									GameSettings.FORUM_DATABASE_CONNECTIONS = true;
								} else {
									GameSettings.FORUM_DATABASE_CONNECTIONS = false;
								}
							} else if (line.contains("debug_mode")) {
								args = line.split(": ");
								if (args[1].toLowerCase().equals("on")) {
									GameSettings.DEBUG_MODE = true;
								} else {
									GameSettings.DEBUG_MODE = false;
								}
							} else if (line.contains("kill_grenade")) {
								args = line.split(": ");
								if (args[1].toLowerCase().equals("on")) {
									GameSettings.KILL_GRENADE = true;
								} else {
									GameSettings.KILL_GRENADE = false;
								}
							} else if (line.contains("tournament_mode")) {
								args = line.split(": ");
								if (args[1].toLowerCase().equals("on")) {
									GameSettings.TOURNAMENT_MODE = true;
								} else {
									GameSettings.TOURNAMENT_MODE = false;
								}
							} else if (line.contains("developer_mode")) {
								args = line.split(": ");
								if (args[1].toLowerCase().equals("on")) {
									GameSettings.DEVELOPER_MODE = true;
								} else {
									GameSettings.DEVELOPER_MODE = false;
								}
							} else if (line.contains("database_logging_time")) {
								args = line.split(": ");
								int time = Integer.valueOf(args[1]);
								GameSettings.DATABASE_LOGGING_TIME = time;
							} else if (line.contains("protected_mac_address")) {
								if (GameSettings.ITEM_SPAWN_TACTICAL) {
									args = line.split(": ");
									GameSettings.PROTECTED_MAC_ADDRESS.add(args[1]);
								}
							} else if (line.contains("protected_computer_address")) {
								if (GameSettings.ITEM_SPAWN_TACTICAL) {
									args = line.split(": ");
									GameSettings.PROTECTED_COMPUTER_ADDRESS.add(args[1]);
								}
							} else if (line.contains("protected_ip_address")) {
								if (GameSettings.ITEM_SPAWN_TACTICAL) {
									args = line.split(": ");
									GameSettings.PROTECTED_IP_ADDRESS.add(args[1]);
								}
							} else if (line.contains("special_player")) {
								args = line.split(": ");
								GameSettings.SPECIAL_PLAYERS.add(args[1].toLowerCase());
							}
						}
						reader.close();
					} catch (IOException e) {

					}
					GameSettings.CONFIGURATION_TIME = 5;
				}
			}
		});
	}

	@Override
	protected void execute() {
		World.updateServerTime();
		if (tick % 100 == 10) {
			System.out.println("Saving all players that are currently logged into the GameServer.");
			World.savePlayers();
			int random = Misc.getRandom(5);
			switch (random) {
			case 1:
				World.sendMessage(
						"<icon=2><shad=ff0000>Don't forget to vote every 12 hours by typing @dre@::vote<shad=ff0000>!");
				break;
			case 2:
				World.sendMessage(
					"<icon=2><shad=ff0000>If you need any help join the @bla@'@dre@Argos@bla@'<shad=ff0000> clan chat!");
				break;
			case 3:
				World.sendMessage(
						"<icon=2><shad=ff0000>Check the forums daily for events and information!");
				break;
			case 4:
				World.sendMessage(
						"<icon=2><shad=ff0000>Did you know that? Bounty Hunter Cave contains revenants and a boss");
				break;
			case 5:
				World.sendMessage(
						"<icon=2><shad=ff0000>Did you know that? The resource skilling area gives noted items and bonus xp!");
				break;
			default:
				World.sendMessage(
						"<icon=2><shad=ff0000>Don't forget to vote every 12 hours by typing @dre@::vote<shad=ff0000>!");
			}

		}
//		if (tick >= 6 && Locations.PLAYERS_IN_DUEL_ARENA >= 3 || PestControl.TOTAL_PLAYERS >= 3)) {
//
//			tick = 0;
//		}
//		tick++;
	}
}
