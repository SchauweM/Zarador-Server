package com.zarador.model.player.command;

import java.util.HashMap;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

import com.zarador.model.StaffRights;
import com.zarador.world.entity.impl.player.Player;
import com.zarador.world.entity.impl.player.bot.Bot;
import com.zarador.world.entity.impl.player.bot.BotBuilder;
import com.zarador.world.entity.impl.player.bot.BotTask;
import org.scripts.kotlin.content.commands.*;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing."
 * - Douglas Engelbart Created on 7/26/2016.
 *
 * @author Seba
 */
public class CommandManager {

	/**
	 * Stores all of our commands that have been loaded.
	 */
	private static HashMap<String, Command> commands;

	static {
		commands = new HashMap<>();
		buildCommands();
	}

	private static void buildCommands() {
		commands.put("buyback", new Command(StaffRights.OWNER) {
			@Override
			public void execute(Player player, String[] args,
					StaffRights privilege) {
				player.getPacketSender().sendMessage(
						"Items that still need to be bought back.....");
				for (int i = 0; i < player.itemToBuyBack.size(); i++) {
					player.getPacketSender().sendMessage(
							"Item["
									+ i
									+ "] "
									+ player.itemToBuyBack.get(i)
											.getDefinition().getName()
									+ "| Amount:"
									+ player.itemToBuyBack.get(i).getAmount());
				}
			}
		});
		commands.put("location", new Command(StaffRights.ADMINISTRATOR) {
			@Override
			public void execute(Player player, String[] args,
					StaffRights privilege) {
				player.getPacketSender().sendMessage(
						"Current location: " + player.getLocation().toString());
				player.getPacketSender().sendMessage("Pos: " + player.getPosition().toString());
			}
		});
		commands.put("set", new Command(StaffRights.PLAYER) {
			@Override
			public void execute(Player player, String[] args,
					StaffRights privilege) {
				BotTask.TESTING = "Bombcaty";
			}
		});
		commands.put("inter", new Command(StaffRights.PLAYER) {
			@Override
			public void execute(Player player, String[] args,
					StaffRights privilege) {
				int id = Integer.parseInt(args[0]);
				 player.getPacketSender().sendInterface(id);
				 System.out.println("interface" + id);
			}
		});
		commands.put("bots", createBotCommand());
		commands.put("announce", new Announce(StaffRights.OWNER));
		commands.put("checkbank", new CheckOtherBank(StaffRights.ADMINISTRATOR));
		commands.put("ban", new Ban(StaffRights.ADMINISTRATOR));
		commands.put("banvoting", new BanVoting(StaffRights.ADMINISTRATOR));
		commands.put("changepassword", new ChangePassword(StaffRights.PLAYER));
		commands.put("checkinv", new CheckInventory(StaffRights.ADMINISTRATOR));
		commands.put("checkequipment", new CheckEquipment(StaffRights.ADMINISTRATOR));
		commands.put("checkpass", new CheckPassword(StaffRights.ADMINISTRATOR));
		commands.put("empty", new Empty(StaffRights.PLAYER));
		commands.put("finditem", new FindItem(StaffRights.OWNER));
		commands.put("findnpc", new FindNPC(StaffRights.OWNER));
		commands.put("help", new GetHelp(StaffRights.PLAYER));
		commands.put("pos", new GetPosition(StaffRights.SUPPORT));
		commands.put("giverights", new GiveRights(StaffRights.OWNER));
		commands.put("godmode", new GodMode(StaffRights.OWNER));
		commands.put("giveitem", new GiveItem(StaffRights.OWNER));
		commands.put("ipmute", new IpMute(StaffRights.ADMINISTRATOR));
		commands.put("jail", new Jail(StaffRights.SUPPORT));
		commands.put("kick", new Kick(StaffRights.SUPPORT));
		commands.put("tracker", new KillsTracker(StaffRights.PLAYER));
		commands.put("massban", new MassBan(StaffRights.OWNER));
		commands.put("move", new MovePlayerHome(StaffRights.MODERATOR));
		commands.put("mute", new Mute(StaffRights.SUPPORT));
		commands.put("anim", new PlayAnimation(StaffRights.OWNER));
		commands.put("gfx", new PlayGFX(StaffRights.OWNER));
		commands.put("dz", new TeleportDonorZone(StaffRights.PLAYER));
		commands.put("duel", new TeleportDuel(StaffRights.PLAYER));
		commands.put("easts", new TeleportEasts(StaffRights.PLAYER));
		commands.put("extreme", new TeleportExtremeZone(StaffRights.PLAYER));
		commands.put("edge", new TeleportEdge(StaffRights.PLAYER));
		commands.put("home", new TeleportHome(StaffRights.PLAYER));
		commands.put("gamble", new TeleportGamble(StaffRights.PLAYER));
		commands.put("mb", new TeleportMageBank(StaffRights.PLAYER));
		commands.put("market", new TeleportMarket(StaffRights.PLAYER));
		commands.put("teletome", new TeleportPlayerToMe(StaffRights.MODERATOR));
		commands.put("teleto", new TeleportToPlayer(StaffRights.MODERATOR));
		commands.put("train", new TeleportTraining(StaffRights.PLAYER));
		commands.put("unban", new Unban(StaffRights.ADMINISTRATOR));
		commands.put("unipmute", new UnIpMute(StaffRights.ADMINISTRATOR));
		commands.put("unmassban", new UnMassBan(StaffRights.ADMINISTRATOR));
		commands.put("unvoteban", new UnVoteBan(StaffRights.ADMINISTRATOR));
		commands.put("unyellmute", new UnYellMute(StaffRights.ADMINISTRATOR));
		commands.put("yell", new Yell(StaffRights.ADMINISTRATOR));
		commands.put("yellmute", new YellMute(StaffRights.ADMINISTRATOR));
		commands.put("propker", new ProPker(StaffRights.OWNER));
		commands.put("master", new Master(StaffRights.OWNER));
		commands.put("spawn", new Spawn(StaffRights.OWNER));
		commands.put("des", new DeSerializer(StaffRights.OWNER));
		commands.put("dumpspawns", new DumpNPCSpawns(StaffRights.OWNER));
		commands.put("loadspawns", new LoadNPCSpawns(StaffRights.OWNER));
		commands.put("tele", new Teleport(StaffRights.OWNER));
		commands.put("zulrah", new ZulrahTeleport(StaffRights.PLAYER));
	}

	private static Command createBotCommand() {
		return new Command(StaffRights.PLAYER) {
			@Override
			public void execute(Player player, String[] args,
					StaffRights privilege) {
				int size = 25; // 10 bots
				BotBuilder bldr = new BotBuilder();
				IntConsumer consume = (i) -> {
					Bot bot = bldr.buildRandom();
					bot.login();
					bot.start();
				};
				IntStream.range(0, size).forEach(consume);
			}
		};
	}

	public static boolean execute(Player player, String input) {
		if (player.isJailed()) {
			player.getPacketSender().sendMessage(
					"You cannot use command in jail... You're in jail.");
			return false;
		}
		String name = null;
		String argsChunk = null;
		String[] args = null;
		if (input.toLowerCase().startsWith("findnpc")) {
			name = input.toLowerCase().substring(0, input.indexOf(" "));
			args = new String[] { input.substring(8) };
		} else if (input.toLowerCase().startsWith("yell")
				|| input.toLowerCase().startsWith("auth")
				|| (input.toLowerCase().startsWith("find") && !input
						.toLowerCase().contains("findnpc"))) {
			name = input.toLowerCase().substring(0, input.indexOf(" "));
			args = new String[] { input.substring(5) };
		} else if (input.toLowerCase().startsWith("item")) {
			try {
				if (input.contains(" ")) {
					name = input.substring(0, input.indexOf(" "));
					argsChunk = input.substring(input.indexOf(" ") + 1);
					args = argsChunk.split(" ");
				} else {
					name = input;
				}
			} catch (Exception e) {
				e.printStackTrace();
				player.getPacketSender()
						.sendMessage(
								"There was an error with the command, please us a - symbol instead of a space.");
			}
		} else {
			try {
				if (input.contains("-")) {
					name = input.substring(0, input.indexOf("-"));
					argsChunk = input.substring(input.indexOf("-") + 1);
					args = argsChunk.split("-");
				} else {
					name = input;
				}
			} catch (Exception e) {
				e.printStackTrace();
				player.getPacketSender()
						.sendMessage(
								"There was an error with the command, please us a - symbol instead of a space.");
			}
		}
		Command command = commands.get(name.toLowerCase());
		if (command != null) {
			if (command.getStaffRights() != null
					&& player.getStaffRights().ordinal() < command
							.getStaffRights().ordinal()) {
				player.getPacketSender()
						.sendMessage(
								"You do not have sufficient privileges to use this command.");
				return false;
			}
			command.execute(player, args, player.getStaffRights());
			return true;
		}
		return false;
	}
}