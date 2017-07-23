package com.zarador.world;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.logging.Level;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.zarador.GameServer;
import com.zarador.GameSettings;
import com.zarador.cache.Archive;
import com.zarador.model.*;
import com.zarador.model.Locations.Location;
import com.zarador.model.definitions.CacheObjectDefinition;
import com.zarador.model.definitions.GameObjectDefinition;
import com.zarador.net.login.LoginManager;
import com.zarador.net.login.LoginResponses;
import com.zarador.util.ByteStream;
import com.zarador.util.Filter;
import com.zarador.util.FilterExecutable;
import com.zarador.util.Misc;
import com.zarador.world.clip.region.Region;
import com.zarador.world.content.Gambling;
import com.zarador.world.content.HalloweenEvent;
import com.zarador.world.content.diversions.hourly.HourlyDiversionManager;
import com.zarador.world.content.lottery.LotterySaving;
import com.zarador.world.content.minigames.impl.FightPit;
import com.zarador.world.content.minigames.impl.PestControl;
import com.zarador.world.entity.Entity;
import com.zarador.world.entity.EntityHandler;
import com.zarador.world.entity.impl.CharacterList;
import com.zarador.world.entity.impl.npc.NPC;
import com.zarador.world.entity.impl.player.Player;
import com.zarador.world.entity.impl.player.PlayerHandler;
import com.zarador.world.entity.updating.NpcUpdateSequence;
import com.zarador.world.entity.updating.PlayerUpdateSequence;
import com.zarador.world.entity.updating.UpdateSequence;

/**
 * @author Gabriel Hannason Thanks to lare96 for help with parallel updating
 *         system
 */
public class World {

	public static long currentServerTime;

	public static Map<Integer, Region> regions;

	/**
	 * Is global yell enabled?
	 */
	private static boolean globalYell = true;

	public static boolean isGlobalYell() {
		return globalYell;
	}

	public static void setGlobalYell(boolean globalYell) {
		World.globalYell = globalYell;
	}

	/**
	 * All of the registered players.
	 */
	private static CharacterList<Player> players = new CharacterList<>(1_000);

	/**
	 * All of the registered NPCs.
	 */
	private static CharacterList<NPC> npcs = new CharacterList<>(5_000);

	/**
	 * Used to block the game thread until updating has completed.
	 */
	private static Phaser synchronizer = new Phaser(1);

	/**
	 * A thread pool that will update players in parallel.
	 */
	private static ExecutorService updateExecutor = Executors.newFixedThreadPool(
			Runtime.getRuntime().availableProcessors(),
			new ThreadFactoryBuilder().setNameFormat("UpdateThread").setPriority(Thread.MAX_PRIORITY).build());

	/**
	 * The queue of {@link Player}s waiting to be logged in.
	 **/
	// private static Queue<Player> logins = new ConcurrentLinkedQueue<>();

	/**
	 * The queue of {@link Player}s waiting to be logged out.
	 **/
	private static Queue<Player> logouts = new ConcurrentLinkedQueue<>();

	/**
	 * The queue of {@link Player}s waiting to be given their vote reward.
	 **/
	private static Queue<Player> voteRewards = new ConcurrentLinkedQueue<>();

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static void register(Entity entity) {
		EntityHandler.register(entity);
	}

	public static boolean deregister(Entity entity) {
		return EntityHandler.deregister(entity);
	}

	public static Player getPlayerByName(String username) {
		Optional<Player> op = players.search(p -> p != null && p.getUsername().equals(Misc.formatText(username)));
		return op.isPresent() ? op.get() : null;
	}

	public static Player getPlayerByLong(long encodedName) {
		Optional<Player> op = players.search(p -> p != null && p.getLongUsername().equals(encodedName));
		return op.isPresent() ? op.get() : null;
	}

	public static void sendMessage(String message) {
		players.forEach(p -> p.getPacketSender().sendMessage(message));
	}

	public static void sendWildernessMessage(String message) {
		for (Player player : players) {
			if (player != null
					&& (player.getLocation() == Location.WILDERNESS || player.getLocation() == Location.WILDKEY_ZONE)) {
				player.getPacketSender().sendMessage(message);
			}
		}
	}

	public static void sendYell(String message, Player player) {
		players.stream()
				.filter(p -> p != null && (p.yellToggle())
						&& (!p.getRelations().getIgnoreList().contains(player.getLongUsername())))
				.forEach(p -> p.getPacketSender().sendMessage(message));
	}

	public static void updateServerTime() {
		players.forEach(p -> p.getPacketSender().sendString(39161,
				"@or2@Server time: @or2@[ @yel@" + Misc.getCurrentServerTime() + "@or2@ ]"));
	}

	public static void updatePlayersOnline() {
		players.forEach(
				p -> p.getPacketSender().sendString(57003, "Players:  @gre@" + (World.getPlayers().size() + GameSettings.fakePlayerCount) + ""));
	}

	public static void savePlayers() {
		players.forEach(p -> p.save());
	}

	/**
	 * Sets a still graphic to a location
	 *
	 * @param id
	 *            The id of the graphic
	 * @param delay
	 *            The delay of the graphic
	 * @param location
	 *            The location of the graphic
	 */
	public static void sendStillGraphic(int id, int delay, Position location) {
		for (Player player : players) {
			if(player == null) {
				continue;
			}
			if(location.isViewableFrom(player.getPosition())) {
				player.getPacketSender().sendGraphic(new Graphic(id, delay), location);
			}
		}
	}

	public static CharacterList<Player> getPlayers() {
		return players;
	}

	/*
	 * Tells you how many staff are online
	 */
	public static int staffOnline() {
		int total = 0;
		for (Player player : players) {
			if(player == null) {
				continue;
			}
			if(player.getStaffRights() != StaffRights.PLAYER) {
				total++;
			}
		}
		return total;
	}
	/*
	 * Tells you what staff are online
	 */
	public static ArrayList<Player> getStaffOnline() {
		ArrayList<Player> names = new ArrayList<>();
		for (Player player : players) {
			if(player == null) {
				continue;
			}
			if (!player.getStaffRights().isStaff()) {
				continue;
			}
			names.add(player);
		}
		return names;
	}

	public static CharacterList<NPC> getNpcs() {
		return npcs;
	}

	/*
	 * Gets the player according to said name in long format.
	 * 
	 * @param name The name of the player to search for in long format.
	 * 
	 * @return The player who has the same name as said param.
	 */
	public static Player getPlayerForLongName(long longName) {
		for (Player player : players) {
			if (player != null && player.getLongUsername() == longName) {
				return player;
			}
		}
		return null;
	}

	public static Player getPlayer(Filter<Player> filter) {
		for (Player player : World.players) {
			if (player == null) {
				continue;
			}
			if (filter.accept(player)) {
				return player;
			}
		}
		return null;
	}

	public static void executeSingle(FilterExecutable<Player> executable) {
		for (Player player : World.players) {
			if (player == null) {
				continue;
			}
			if (executable.accept(player)) {
				executable.execute(player);
				return;
			}
		}
	}

	public static void executeSingleNpc(FilterExecutable<NPC> executable) {
		for (NPC player : World.npcs) {
			if (player == null) {
				continue;
			}
			if (executable.accept(player)) {
				executable.execute(player);
				return;
			}
		}
	}

	public static void executePlayersGroup(Player[] players, FilterExecutable<Player> executable) {
		for (Player p : players) {
			if (p == null) {
				continue;
			}
			if (executable.accept(p)) {
				executable.execute(p);
			}
		}
	}

	public static void executeAll(FilterExecutable<Player> executable) {
		for (Player player : World.players) {
			if (player == null) {
				continue;
			}
			if (executable.accept(player)) {
				executable.execute(player);
			}
		}
	}

	public static void executeAllRegion(FilterExecutable<Player> executable, int regionId) {
		for (Player player : World.players) {
			if (player == null || player.getPosition().getRegionId() != regionId) {
				continue;
			}
			if (executable.accept(player)) {
				executable.execute(player);
			}
		}
	}

	public static void executeNpcs(FilterExecutable<NPC> executable) {
		for (NPC n : World.npcs) {
			if (n == null) {
				continue;
			}
			if (executable.accept(n)) {
				executable.execute(n);
			}
		}
	}

	public static void executeEntity(FilterExecutable<Entity> executable) {
		for (Player player : World.players) {
			if (player == null) {
				continue;
			}
			if (executable.accept(player)) {
				executable.execute(player);
			}
		}
		for (NPC player : World.npcs) {
			if (player == null) {
				continue;
			}
			if (executable.accept(player)) {
				executable.execute(player);
			}
		}
	}

	public static void sequence() {
		long start = System.currentTimeMillis();

		// Handle queued logins.
		if (!LoginManager.getSuccessfulLogins().isEmpty()) {
			Iterator<Long> it = LoginManager.getSuccessfulLogins().keySet().iterator();
			while (it.hasNext()) {
				long encodedName = it.next();
				Player player = LoginManager.getSuccessfulLogins().get(encodedName);
				if (player == null) {
					continue;
				}
				if (player.getResponse() == 2) {
					if (logouts.contains(player)) {
						player.setResponse(LoginResponses.LOGIN_ACCOUNT_ONLINE);
					} else {
						PlayerHandler.handleLogin(player);
					}
					LoginManager.getSuccessfulLogins().remove(encodedName);
				}
			}
		}
		/*
		 * for (int amount = 0; amount < GameSettings.LOGIN_THRESHOLD; amount++)
		 * { Player player = logins.poll(); if (player == null) break; if
		 * (player.getResponse() == 2) { if (logouts.contains(player)) {
		 * player.setResponse(LoginResponses.LOGIN_ACCOUNT_ONLINE); } else {
		 * PlayerHandler.handleLogin(player); } } }
		 */
		long logout_start = System.currentTimeMillis();
		long loginCycle = logout_start - start;

		// Handle queued logouts.
		int amount = 0;
		Iterator<Player> $it = logouts.iterator();
		while ($it.hasNext()) {
			Player player = $it.next();
			if (player == null || amount >= GameSettings.LOGOUT_THRESHOLD)
				break;
			if (PlayerHandler.handleLogout(player)) {
				$it.remove();
				amount++;
			}
		}
		long minigame_start = System.currentTimeMillis();
		long logoutCycle = minigame_start - logout_start;
		LotterySaving.execute();
		FightPit.sequence();
		PestControl.sequence();
		HourlyDiversionManager.pulse();
		//HalloweenEvent.spawnPumpkins();
		// EvilTrees.sequence();
		long player_start = System.currentTimeMillis();
		long minigameCycle = player_start - minigame_start;

		// First we construct the update sequences.
		UpdateSequence<Player> playerUpdate = new PlayerUpdateSequence(synchronizer, updateExecutor);
		UpdateSequence<NPC> npcUpdate = new NpcUpdateSequence();
		// Then we execute pre-updating code.
		players.forEach(playerUpdate::executePreUpdate);
		npcs.forEach(npcUpdate::executePreUpdate);
		// Then we execute parallelized updating code.
		synchronizer.bulkRegister(players.size());
		players.forEach(playerUpdate::executeUpdate);
		synchronizer.arriveAndAwaitAdvance();
		// Then we execute post-updating code.
		players.forEach(playerUpdate::executePostUpdate);
		npcs.forEach(npcUpdate::executePostUpdate);
		long end = System.currentTimeMillis();
		long entityUpdateCycle = end - player_start;
		// long cycleTime = end - start;
		//GameServer.getPanel().addWorldCycle(loginCycle, logoutCycle, minigameCycle, entityUpdateCycle);
	}

	public static void logError(String name, Exception ex) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(name, true));
			bw.write("Date: " + new Date().toString());
			bw.newLine();
			if (ex.getCause() != null) {
				bw.write("cause: " + ex.getCause().toString());
				bw.newLine();
			}
			if (ex.getClass() != null) {
				bw.write(ex.getClass().getSimpleName().toString());
			}
			if (ex.getMessage() != null) {
				bw.write(": " + ex.getMessage());
				bw.newLine();
			}
			if (ex.getStackTrace() == null)
				ex.fillInStackTrace();
			if (ex.getStackTrace() != null) {
				for (StackTraceElement s : ex.getStackTrace()) {
					bw.write("  at " + s.getClassName() + "." + s.getMethodName());
					if (s.getFileName() != null)
						bw.write("(" + s.getFileName() + ":" + s.getLineNumber() + ")");
					else
						bw.write("(Unknown Source)");
					bw.newLine();
				}
			}
			bw.newLine();
			bw.write("================================");
			bw.newLine();
			bw.flush();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * public static Queue<Player> getLoginQueue() { return logins; }
	 */

	public static Queue<Player> getLogoutQueue() {
		return logouts;
	}

	public static Queue<Player> getVoteRewardingQueue() {
		return voteRewards;
	}

	public static Region loadRegion(int x, int y) {
		Region region = regions.get(((x >> 6) << 8) + (y >> 6));
		if (region != null) {
			region.load();
		}
		return region;
	}

	public static int getMask(int x, int y, int z) {
		Region region = World.loadRegion(x, y);
		if (region == null) {
			//System.err.println("Null region: " + x + " " + y);
			return Region.BLOCKED_TILE;
		}
		return region.getMask(x, y, z % 4);
	}

	public static void flag(int x, int y, int z, int mask) {
		if (x == 2810 && y == 3436 || x == 2809 && y == 3437) {
			return;
		}
		if (x == 2720 && y == 9992) {
			new Exception().printStackTrace();
		}
		Region region = World.loadRegion(x, y);
		if (region != null) {
			region.flag(x, y, z, mask);
		}
	}

	public static void unflag(int z, int x, int y, int mask) {
		Region region = World.loadRegion(x, y);
		if (region != null) {
			region.unflag(x, y, z, mask);
		}
	}

	public static void removeObjectClip(GameObject o) {
		unflag(o);
	}

	public static void addObjectClip(GameObject o) {
		flag(o);
	}

	private static void flag(GameObject object) {
		CacheObjectDefinition definition = CacheObjectDefinition.definition(object.getId());
		if (definition == null) {
			return;
		}
		if(definition.getName() != null) {
			if(definition.getName().equalsIgnoreCase("gate")) {
				return;
			}
		}
		for (int ids = 0; ids < GameObjectDefinition.removeObjects.length; ids++) {
			if (object.getId() == GameObjectDefinition.removeObjects[ids]) {
				object.getDefinition().unwalkable = false;
				return;
			}
		}
		if (!definition.isUnwalkable()) {
			return;
		}
		if(object.getType() == 22) {
			return;
		}
		for(Gambling.FlowersData flower: Gambling.FlowersData.values()) {
			if(flower == null) {
				continue;
			}
			if(flower.objectId == object.getId()) {
				return;
			}
		}
		Position position = object.getPosition();
		int type = object.getType();
		int z = position.getZ();
		int x = position.getX();
		int y = position.getY();
		int rotation = object.getRotation();
		if (type >= 0 && type <= 3) {
			if (type == 0) {
				if (rotation == 0) {
					flag(x, y, z, Region.WEST_BLOCKED);
					flag(x - 1, y, z, Region.EAST_BLOCKED);
				} else if (rotation == 1) {
					flag(x, y, z, Region.NORTH_BLOCKED);
					flag(x, y + 1, z, Region.SOUTH_BLOCKED);
				} else if (rotation == 2) {
					flag(x, y, z, Region.EAST_BLOCKED);
					flag(x + 1, y, z, Region.WEST_BLOCKED);
				} else if (rotation == 3) {
					flag(x, y, z, Region.SOUTH_BLOCKED);
					flag(x, y - 1, z, Region.NORTH_BLOCKED);
				}
			} else if (type == 1 || type == 3) {
				if (rotation == 0) {
					flag(x, y, z, Region.NORTH_WEST_BLOCKED);
					flag(x - 1, y + 1, z, Region.SOUTH_EAST_BLOCKED);
				} else if (rotation == 1) {
					flag(x, y, z, Region.NORTH_EAST_BLOCKED);
					flag(x + 1, y + 1, z, Region.SOUTH_WEST_BLOCKED);
				} else if (rotation == 2) {
					flag(x, y, z, Region.SOUTH_EAST_BLOCKED);
					flag(x + 1, y - 1, z, Region.NORTH_WEST_BLOCKED);
				} else if (rotation == 3) {
					flag(x, y, z, Region.SOUTH_WEST_BLOCKED);
					flag(x - 1, y - 1, z, Region.NORTH_EAST_BLOCKED);
				}
			} else if (type == 2) {
				if (rotation == 0) {
					flag(x, y, z, Region.NORTH_BLOCKED | Region.WEST_BLOCKED);
					flag(x - 1, y, z, Region.EAST_BLOCKED);
					flag(x, y + 1, z, Region.SOUTH_BLOCKED);
				} else if (rotation == 1) {
					flag(x, y, z, Region.NORTH_BLOCKED | Region.EAST_BLOCKED);
					flag(x, y + 1, z, Region.SOUTH_BLOCKED);
					flag(x + 1, y, z, Region.WEST_BLOCKED);
				} else if (rotation == 2) {
					flag(x, y, z, Region.SOUTH_BLOCKED | Region.EAST_BLOCKED);
					flag(x + 1, y, z, Region.WEST_BLOCKED);
					flag(x, y - 1, z, Region.NORTH_BLOCKED);
				} else if (rotation == 3) {
					flag(x, y, z, Region.SOUTH_BLOCKED | Region.WEST_BLOCKED);
					flag(x, y - 1, z, Region.NORTH_BLOCKED);
					flag(x - 1, y, z, Region.EAST_BLOCKED);
				}
			}
			if (definition.isProjectileBlocked()) {
				if (type == 0) {
					if (rotation == 0) {
						flag(x, y, z, Region.PROJECTILE_WEST_BLOCKED);
						flag(x - 1, y, z, Region.PROJECTILE_EAST_BLOCKED);
					} else if (rotation == 1) {
						flag(x, y, z, Region.PROJECTILE_NORTH_BLOCKED);
						flag(x, y + 1, z, Region.PROJECTILE_SOUTH_BLOCKED);
					} else if (rotation == 2) {
						flag(x, y, z, Region.PROJECTILE_EAST_BLOCKED);
						flag(x + 1, y, z, Region.PROJECTILE_WEST_BLOCKED);
					} else if (rotation == 3) {
						flag(x, y, z, Region.PROJECTILE_SOUTH_BLOCKED);
						flag(x, y - 1, z, Region.PROJECTILE_NORTH_BLOCKED);
					}
				} else if (type == 1 || type == 3) {
					if (rotation == 0) {
						flag(x, y, z, Region.PROJECTILE_NORTH_WEST_BLOCKED);
						flag(x - 1, y + 1, z, Region.PROJECTILE_SOUTH_EAST_BLOCKED);
					} else if (rotation == 1) {
						flag(x, y, z, Region.PROJECTILE_NORTH_EAST_BLOCKED);
						flag(x + 1, y + 1, z, Region.PROJECTILE_SOUTH_WEST_BLOCKED);
					} else if (rotation == 2) {
						flag(x, y, z, Region.PROJECTILE_SOUTH_EAST_BLOCKED);
						flag(x + 1, y - 1, z, Region.PROJECTILE_NORTH_WEST_BLOCKED);
					} else if (rotation == 3) {
						flag(x, y, z, Region.PROJECTILE_SOUTH_WEST_BLOCKED);
						flag(x - 1, y - 1, z, Region.PROJECTILE_NORTH_EAST_BLOCKED);
					}
				} else if (type == 2) {
					if (rotation == 0) {
						flag(x, y, z, Region.PROJECTILE_NORTH_BLOCKED | Region.PROJECTILE_WEST_BLOCKED);
						flag(x - 1, y, z, Region.PROJECTILE_EAST_BLOCKED);
						flag(x, y + 1, z, Region.PROJECTILE_SOUTH_BLOCKED);
					} else if (rotation == 1) {
						flag(x, y, z, Region.PROJECTILE_NORTH_BLOCKED | Region.PROJECTILE_EAST_BLOCKED);
						flag(x, y + 1, z, Region.PROJECTILE_SOUTH_BLOCKED);
						flag(x + 1, y, z, Region.PROJECTILE_WEST_BLOCKED);
					} else if (rotation == 2) {
						flag(x, y, z, Region.PROJECTILE_EAST_BLOCKED | Region.PROJECTILE_SOUTH_BLOCKED);
						flag(x + 1, y, z, Region.PROJECTILE_WEST_BLOCKED);
						flag(x, y - 1, z, Region.PROJECTILE_NORTH_BLOCKED);
					} else if (rotation == 3) {
						flag(x, y, z, Region.PROJECTILE_SOUTH_BLOCKED | Region.PROJECTILE_WEST_BLOCKED);
						flag(x, y - 1, z, Region.PROJECTILE_NORTH_BLOCKED);
						flag(x - 1, y, z, Region.PROJECTILE_EAST_BLOCKED);
					}
				}
			}
		} else if (type >= 4 && type <= 8) {//Wall Decoration
		} else if ((type == 9) || (type == 10 || type == 11) || (type >= 12 && type <= 21)) {//Diagonal Wall || Interactive Object || Roof
			int mask = Region.TILE_BLOCKED;
			if (definition.isProjectileBlocked()) {
				mask += Region.PROJECTILE_TILE_BLOCKED;
			}
			int xLength = object.getLengthX();
			int yLength = object.getLengthY();
			for (int xOff = 0; xOff < xLength; xOff++) {
				for (int yOff = 0; yOff < yLength; yOff++) {
					flag(x + xOff, y + yOff, z, mask);
				}
			}
		} else if (type == 22) {//Floor Decoration
			if (definition.isInteractable()) {
				flag(x, y, z, Region.BLOCKED_TILE);
			}
		} else {
			//System.err.println("UNEXPECTED OBJECT TYPE: " + object.getType());
			//throw new AssertionError(object.getType());
		}
	}

	private static void unflag(GameObject object) {
		Position position = object.getPosition();
		GameObjectDefinition definition = GameObjectDefinition.forId(object.getId());
		if (definition == null) {
			return;
		}
		//if (!definition.unwalkable) {
			//return;
		//}
		int type = object.getType();
		int z = position.getZ();
		int x = position.getX();
		int y = position.getY();
		int rotation = object.getRotation();
		if (type >= 0 && type <= 3) {
			if (type == 0) {
				if (rotation == 0) {
					unflag(z, x, y, Region.WEST_BLOCKED);
					unflag(z, x - 1, y, Region.EAST_BLOCKED);
				} else if (rotation == 1) {
					unflag(z, x, y, Region.NORTH_BLOCKED);
					unflag(z, x, y + 1, Region.SOUTH_BLOCKED);
				} else if (rotation == 2) {
					unflag(z, x, y, Region.EAST_BLOCKED);
					unflag(z, x + 1, y, Region.WEST_BLOCKED);
				} else if (rotation == 3) {
					unflag(z, x, y, Region.SOUTH_BLOCKED);
					unflag(z, x, y - 1, Region.NORTH_BLOCKED);
				}
			} else if (type == 1 || type == 3) {
				if (rotation == 0) {
					unflag(z, x, y, Region.NORTH_WEST_BLOCKED);
					unflag(z, x - 1, y + 1, Region.SOUTH_EAST_BLOCKED);
				} else if (rotation == 1) {
					unflag(z, x, y, Region.NORTH_EAST_BLOCKED);
					unflag(z, x + 1, y + 1, Region.SOUTH_WEST_BLOCKED);
				} else if (rotation == 2) {
					unflag(z, x, y, Region.SOUTH_EAST_BLOCKED);
					unflag(z, x + 1, y - 1, Region.NORTH_WEST_BLOCKED);
				} else if (rotation == 3) {
					unflag(z, x, y, Region.SOUTH_WEST_BLOCKED);
					unflag(z, x - 1, y - 1, Region.NORTH_EAST_BLOCKED);
				}
			} else if (type == 2) {
				if (rotation == 0) {
					unflag(z, x, y, Region.NORTH_BLOCKED | Region.WEST_BLOCKED);
					unflag(z, x - 1, y, Region.EAST_BLOCKED);
					unflag(z, x, y + 1, Region.SOUTH_BLOCKED);
				} else if (rotation == 1) {
					unflag(z, x, y, Region.NORTH_BLOCKED | Region.EAST_BLOCKED);
					unflag(z, x, y + 1, Region.SOUTH_BLOCKED);
					unflag(z, x + 1, y, Region.WEST_BLOCKED);
				} else if (rotation == 2) {
					unflag(z, x, y, Region.SOUTH_BLOCKED | Region.EAST_BLOCKED);
					unflag(z, x + 1, y, Region.WEST_BLOCKED);
					unflag(z, x, y - 1, Region.NORTH_BLOCKED);
				} else if (rotation == 3) {
					unflag(z, x, y, Region.SOUTH_BLOCKED | Region.WEST_BLOCKED);
					unflag(z, x, y - 1, Region.NORTH_BLOCKED);
					unflag(z, x - 1, y, Region.EAST_BLOCKED);
				}
			}
			if (definition.impenetrable) {
				if (type == 0) {
					if (rotation == 0) {
						unflag(z, x, y, Region.PROJECTILE_WEST_BLOCKED);
						unflag(z, x - 1, y, Region.PROJECTILE_EAST_BLOCKED);
					} else if (rotation == 1) {
						unflag(z, x, y, Region.PROJECTILE_NORTH_BLOCKED);
						unflag(z, x, y + 1, Region.PROJECTILE_SOUTH_BLOCKED);
					} else if (rotation == 2) {
						unflag(z, x, y, Region.PROJECTILE_EAST_BLOCKED);
						unflag(z, x + 1, y, Region.PROJECTILE_WEST_BLOCKED);
					} else if (rotation == 3) {
						unflag(z, x, y, Region.PROJECTILE_SOUTH_BLOCKED);
						unflag(z, x, y - 1, Region.PROJECTILE_NORTH_BLOCKED);
					}
				} else if (type == 1 || type == 3) {
					if (rotation == 0) {
						unflag(z, x, y, Region.PROJECTILE_NORTH_WEST_BLOCKED);
						unflag(z, x - 1, y + 1, Region.PROJECTILE_SOUTH_EAST_BLOCKED);
					} else if (rotation == 1) {
						unflag(z, x, y, Region.PROJECTILE_NORTH_EAST_BLOCKED);
						unflag(z, x + 1, y + 1, Region.PROJECTILE_SOUTH_WEST_BLOCKED);
					} else if (rotation == 2) {
						unflag(z, x, y, Region.PROJECTILE_SOUTH_EAST_BLOCKED);
						unflag(z, x + 1, y - 1, Region.PROJECTILE_NORTH_WEST_BLOCKED);
					} else if (rotation == 3) {
						unflag(z, x, y, Region.PROJECTILE_SOUTH_WEST_BLOCKED);
						unflag(z, x - 1, y - 1, Region.PROJECTILE_NORTH_EAST_BLOCKED);
					}
				} else if (type == 2) {
					if (rotation == 0) {
						unflag(z, x, y, Region.PROJECTILE_NORTH_BLOCKED | Region.PROJECTILE_WEST_BLOCKED);
						unflag(z, x - 1, y, Region.PROJECTILE_EAST_BLOCKED);
						unflag(z, x, y + 1, Region.PROJECTILE_SOUTH_BLOCKED);
					} else if (rotation == 1) {
						unflag(z, x, y, Region.PROJECTILE_NORTH_BLOCKED | Region.PROJECTILE_EAST_BLOCKED);
						unflag(z, x, y + 1, Region.PROJECTILE_SOUTH_BLOCKED);
						unflag(z, x + 1, y, Region.PROJECTILE_WEST_BLOCKED);
					} else if (rotation == 2) {
						unflag(z, x, y, Region.PROJECTILE_EAST_BLOCKED | Region.PROJECTILE_SOUTH_BLOCKED);
						unflag(z, x + 1, y, Region.PROJECTILE_WEST_BLOCKED);
						unflag(z, x, y - 1, Region.PROJECTILE_NORTH_BLOCKED);
					} else if (rotation == 3) {
						unflag(z, x, y, Region.PROJECTILE_SOUTH_BLOCKED | Region.PROJECTILE_WEST_BLOCKED);
						unflag(z, x, y - 1, Region.PROJECTILE_NORTH_BLOCKED);
						unflag(z, x - 1, y, Region.PROJECTILE_EAST_BLOCKED);
					}
				}
			}
		} else if (type >= 4 && type <= 8) {//Wall Decoration
		} else if ((type == 9) || (type == 10 || type == 11) || (type >= 12 && type <= 21)) {//Diagonal Wall || Interactive Object || Roof
			int mask = Region.TILE_BLOCKED;
			if (definition.impenetrable) {
				mask += Region.PROJECTILE_TILE_BLOCKED;
			}
			int xLength = object.getLengthX();
			int yLength = object.getLengthY();
			for (int xOff = 0; xOff < xLength; xOff++) {
				for (int yOff = 0; yOff < yLength; yOff++) {
					unflag(z, x + xOff, y + yOff, mask);
				}
			}
		} else if (type == 22) {//Floor Decoration
			if (definition.interactive) {
				unflag(z, x, y, Region.BLOCKED_TILE);
			}
		} else {
			throw new AssertionError(object.getType());
		}
	}

	public static void remove(GameObject object) {
		if (object == null) {
			//System.out.println("Object["+object.getId()+"] x["+object.getX()+"] y["+object.getY()+"] z["+object.getZ()+"].");
			return;
		}
		Region region = World.loadRegion(object.getPosition().getX(), object.getPosition().getY());
		if (region != null) {
			region.remove(object);
		}
		unflag(object);
	}

	public static GameObject addObject(int objectId, int x, int y, int z, int type, int rotation) {
		if (objectId == -1) {
			new Error().printStackTrace();
			return null;
		}
		GameObject object = new GameObject(objectId, new Position(x, y, z), type, rotation);
		Region region = loadRegion(x, y);
		if (region != null) {
			region.add(object);
		}
		flag(object);
		return object;
	}

	public static GameObject addObject(GameObject o) {
		if (o == null) {
			new Error().printStackTrace();
			return null;
		}
		GameObject object = new GameObject(o.getId(), new Position(o.getPosition().getX(), o.getPosition().getY(), o.getPosition().getZ()), o.getType(), o.getRotation());
		Region region = loadRegion(o.getPosition().getX(), o.getPosition().getY());
		if (region != null) {
			region.add(object);
		}
		flag(object);
		executeAll(new PlayerFilterExecutable(o));
		return object;
	}

	public static GameObject addCustomObject(GameObject o) {
		if (o == null) {
			new Error().printStackTrace();
			return null;
		}
		Region region = loadRegion(o.getPosition().getX(), o.getPosition().getY());
		region.addCustomObject(o);
		flag(o);
		executeAll(new PlayerFilterExecutable(o));
		return o;
	}

	public static void removeCustomObject(GameObject object) {
		if (object == null) {
			//System.out.println("Object["+object.getId()+"] x["+object.getX()+"] y["+object.getY()+"] z["+object.getZ()+"].");
			return;
		}
		Region region = World.loadRegion(object.getPosition().getX(), object.getPosition().getY());
		region.removeCustomObject(object);
		executeAll(new RemoveObject(object));
		unflag(object);
	}

	private static class PlayerFilterExecutable extends FilterExecutable<Player> {
		private final GameObject object;

		public PlayerFilterExecutable(GameObject object) {
			this.object = object;
		}

		public boolean accept(Player player) {
			if (object == null || object.getId() == 0) {
				return false;
			}
			return player.getLastKnownRegion().withinRegion(object.getPosition()) && player.getPosition().getZ() == object.getPosition().getZ();
		}

		public void execute(Player player) {
			player.getPacketSender().sendObject(object);
		}
	}

	private static class RemoveObject extends FilterExecutable<Player> {
		private final GameObject object;

		public RemoveObject(GameObject object) {
			this.object = object;
		}

		public boolean accept(Player player) {
			if (object == null || object.getId() == 0) {
				return false;
			}
			return player.getLastKnownRegion().withinRegion(object.getPosition()) && player.getPosition().getZ() == object.getPosition().getZ();
		}

		public void execute(Player player) {
			player.getPacketSender().sendObjectRemoval(object);
		}
	}

	public static boolean objectExists(GameObject object) {
		return World.getObject(object.getId(), object.getPosition().getX(), object.getPosition().getY(), object.getPosition().getZ()) != null;
	}

	public static GameObject getObject(int id, int x, int y, int z) {

		Region region = World.loadRegion(x, y);
		if (region == null) {
			return null;
		}
		return region.getObject(id, z % 4, x, y);
	}

	public static GameObject getInteractiveObject(Position position) {
		Region region = World.loadRegion(position.getX(), position.getY());
		if (region == null) {
			return null;
		}
		GameObject object = region.getObject(position, 10);
		if (object != null) {
			return object;
		}
		return region.getObject(position, 11);
	}

	public static GameObject getObject(Position position, int type) {
		Region region = World.loadRegion(position.getX(), position.getY());
		if (region == null) {
			return null;
		}
		return region.getObject(position, type);
	}

	public static GameObject getObject(Position position){
		Region region = World.loadRegion(position.getX(), position.getY());
		if (region == null) {
			return null;
		}
		return region.getObject(position);
	}

	public static Object[] getAllObjects(Position position) {
		Region region = World.loadRegion(position.getX(), position.getY());
		if (region == null) {
			return null;
		}
		return region.getAllObjects(position);
	}

	public static boolean directionBlocked(Direction direction, int z, int x, int y, int size) {
		switch (direction) {
			case NORTH_WEST: {
				if (directionBlocked(Direction.NORTH, z, x, y, size)) {
					return true;
				}
				if (directionBlocked(Direction.WEST, z, x, y, size)) {
					return true;
				}
				return (World.getMask(x - size, y + size, z) & (Region.SOUTH_BLOCKED | Region.EAST_BLOCKED | Region.SOUTH_EAST_BLOCKED | Region.TILE_BLOCKED | Region.BLOCKED_TILE | Region.UNLOADED_TILE)) != 0;
			}
			case NORTH: {
				for (int i = 0; i < size; i++) {
					if ((World.getMask(x + i, y + size, z) & (Region.SOUTH_BLOCKED | Region.TILE_BLOCKED | Region.BLOCKED_TILE | Region.UNLOADED_TILE)) != 0) {
						return true;
					}
				}
				return false;
			}
			case NORTH_EAST: {
				if (directionBlocked(Direction.NORTH, z, x, y, size)) {
					return true;
				}
				if (directionBlocked(Direction.EAST, z, x, y, size)) {
					return true;
				}
				return (World.getMask(x + size, y + size, z) & (Region.SOUTH_BLOCKED | Region.WEST_BLOCKED | Region.SOUTH_WEST_BLOCKED | Region.TILE_BLOCKED | Region.BLOCKED_TILE | Region.UNLOADED_TILE)) != 0;
			}
			case EAST: {
				for (int i = 0; i < size; i++) {
					if ((World.getMask(x + size, y + i, z) & (Region.WEST_BLOCKED | Region.TILE_BLOCKED | Region.BLOCKED_TILE | Region.UNLOADED_TILE)) != 0) {
						return true;
					}
				}
				return false;
			}
			case SOUTH_EAST: {
				if (directionBlocked(Direction.SOUTH, z, x, y, size)) {
					return true;
				}
				if (directionBlocked(Direction.EAST, z, x, y, size)) {
					return true;
				}
				return (World.getMask(x + size, y - size, z) & (Region.NORTH_BLOCKED | Region.WEST_BLOCKED | Region.NORTH_WEST_BLOCKED | Region.TILE_BLOCKED | Region.BLOCKED_TILE | Region.UNLOADED_TILE)) != 0;
			}
			case SOUTH: {
				for (int i = 0; i < size; i++) {
					if ((World.getMask(x + i, y - 1, z) & (Region.NORTH_BLOCKED | Region.TILE_BLOCKED | Region.BLOCKED_TILE | Region.UNLOADED_TILE)) != 0) {
						return true;
					}
				}
				return false;
			}
			case SOUTH_WEST: {
				if (directionBlocked(Direction.SOUTH, z, x, y, size)) {
					return true;
				}
				if (directionBlocked(Direction.WEST, z, x, y, size)) {
					return true;
				}
				return (World.getMask(x - 1, y - 1, z) & (Region.NORTH_BLOCKED | Region.EAST_BLOCKED | Region.NORTH_EAST_BLOCKED | Region.TILE_BLOCKED | Region.BLOCKED_TILE | Region.UNLOADED_TILE)) != 0;
			}
			case WEST: {
				for (int i = 0; i < size; i++) {
					if ((World.getMask(x - 1, y + i, z) & (Region.EAST_BLOCKED | Region.TILE_BLOCKED | Region.BLOCKED_TILE | Region.UNLOADED_TILE)) != 0) {
						return true;
					}
				}
				return false;
			}
			case NONE: {
				return (World.getMask(x , y , z) & (Region.SOUTH_BLOCKED | Region.TILE_BLOCKED | Region.BLOCKED_TILE | Region.UNLOADED_TILE)) != 0;
			}
		}
		throw new AssertionError();
	}

	public static boolean projectileDirectionBlocked(Direction direction, int z, int x, int y, int size) {
		switch (direction) {
			case NORTH_WEST: {
				if (projectileDirectionBlocked(Direction.NORTH, z, x, y, size)) {
					return true;
				}
				if (projectileDirectionBlocked(Direction.WEST, z, x, y, size)) {
					return true;
				}
				return (World.getMask(x - 1, y + size, z) & (Region.PROJECTILE_SOUTH_BLOCKED | Region.PROJECTILE_EAST_BLOCKED | Region.PROJECTILE_SOUTH_EAST_BLOCKED | Region.PROJECTILE_TILE_BLOCKED)) != 0;
			}
			case NORTH: {
				for (int i = 0; i < size; i++) {
					if ((World.getMask(x + i, y + size, z) & (Region.PROJECTILE_SOUTH_BLOCKED | Region.PROJECTILE_TILE_BLOCKED)) != 0) {
						return true;
					}
				}
				return false;
			}
			case NORTH_EAST: {
				if (projectileDirectionBlocked(Direction.NORTH, z, x, y, size)) {
					return true;
				}
				if (projectileDirectionBlocked(Direction.EAST, z, x, y, size)) {
					return true;
				}
				return (World.getMask(x + size, y + size, z) & (Region.PROJECTILE_SOUTH_BLOCKED | Region.PROJECTILE_WEST_BLOCKED | Region.PROJECTILE_SOUTH_WEST_BLOCKED | Region.PROJECTILE_TILE_BLOCKED)) != 0;
			}
			case EAST: {
				for (int i = 0; i < size; i++) {
					if ((World.getMask(x + size, y + i, z) & (Region.PROJECTILE_WEST_BLOCKED | Region.PROJECTILE_TILE_BLOCKED)) != 0) {
						return true;
					}
				}
				return false;
			}
			case SOUTH_EAST: {
				if (projectileDirectionBlocked(Direction.SOUTH, z, x, y, size)) {
					return true;
				}
				if (projectileDirectionBlocked(Direction.EAST, z, x, y, size)) {
					return true;
				}
				return (World.getMask(x + size, y - 1, z) & (Region.PROJECTILE_NORTH_BLOCKED | Region.PROJECTILE_WEST_BLOCKED | Region.PROJECTILE_NORTH_WEST_BLOCKED | Region.PROJECTILE_TILE_BLOCKED)) != 0;
			}
			case SOUTH: {
				for (int i = 0; i < size; i++) {
					if ((World.getMask(x + i, y - 1, z) & (Region.PROJECTILE_NORTH_BLOCKED | Region.PROJECTILE_TILE_BLOCKED)) != 0) {
						return true;
					}
				}
				return false;
			}
			case SOUTH_WEST: {
				if (projectileDirectionBlocked(Direction.SOUTH, z, x, y, size)) {
					return true;
				}
				if (projectileDirectionBlocked(Direction.WEST, z, x, y, size)) {
					return true;
				}
				return (World.getMask(x - 1, y - 1, z) & (Region.PROJECTILE_NORTH_BLOCKED | Region.PROJECTILE_EAST_BLOCKED | Region.PROJECTILE_NORTH_EAST_BLOCKED | Region.PROJECTILE_TILE_BLOCKED)) != 0;
			}
			case WEST: {
				for (int i = 0; i < size; i++) {
					if ((World.getMask(x - 1, y + i, z) & (Region.PROJECTILE_EAST_BLOCKED | Region.PROJECTILE_TILE_BLOCKED)) != 0) {
						return true;
					}
				}
				return false;
			}
			case NONE: {
				return (World.getMask(x , y , z) & (Region.SOUTH_BLOCKED | Region.TILE_BLOCKED | Region.BLOCKED_TILE | Region.UNLOADED_TILE)) != 0;
			}
		}
		throw new AssertionError();
	}

	/**
	 * Do not delete
	 */
	public static boolean tileBlocked(Position pos) {
		return (World.getMask(pos.getX(), pos.getY(), pos.getZ()) & Region.TILE_BLOCKED) != 0;
	}

	public static void resetFlag(int z, int x, int y) {
		Region region = World.loadRegion(x, y);
		if (region != null) {
			region.removeFlag(x, y, z);
		}
	}

	public static void loadRegions(Archive archive) throws Exception {
		GameServer.getLogger().log(Level.INFO, "Loading regions...");
		ByteStream in = new ByteStream(archive.getNamedFile("map_index").data());
		int size = in.getUnsignedShort();
		int[] regionIds = new int[size];
		int[] mapGroundFileIds = new int[size];
		int[] mapObjectsFileIds = new int[size];
		for (int i = 0; i < size; i++) {
			regionIds[i] = in.getUnsignedShort();
			mapGroundFileIds[i] = in.getUnsignedShort();
			mapObjectsFileIds[i] = in.getUnsignedShort();
		}
		//Zulrah
		regionIds[107] = 8751;
		mapGroundFileIds[107] = 1946;
		mapObjectsFileIds[107] = 1947;

		regionIds[108] = 8752;
		mapGroundFileIds[108] = 938;
		mapObjectsFileIds[108] = 939;

		regionIds[129] = 9007;
		mapGroundFileIds[129] = 1938;
		mapObjectsFileIds[129] = 1939;

		regionIds[130] = 9008;
		mapGroundFileIds[130] = 946;
		mapObjectsFileIds[130] = 947;

		regionIds[149] = 9263;
		mapGroundFileIds[149] = 1210;
		mapObjectsFileIds[149] = 1211;

		regionIds[150] = 9264;
		mapGroundFileIds[150] = 956;
		mapObjectsFileIds[150] = 957;

		//Kraken Cave
		regionIds[941] = 14681;
		mapGroundFileIds[941] = 1960;
		mapObjectsFileIds[941] = 1961;

		regionIds[1604] = 14682;
		mapGroundFileIds[1604] = 1870;
		mapObjectsFileIds[1604] = 1871;

		regionIds[1605] = -1;
		mapGroundFileIds[1605] = -1;
		mapObjectsFileIds[1605] = -1;

		//Cerberus
		regionIds[151] = 4883;
		mapGroundFileIds[151] = 1984;
		mapObjectsFileIds[151] = 1985;


		regions = new HashMap<>(size);
		for (int i = 0; i < size; i++) {
			regions.put(regionIds[i], new Region(regionIds[i], mapGroundFileIds[i], mapObjectsFileIds[i]));
		}

        /*World.flag(2378, 3084, 0, Region.TILE_BLOCKED);
        World.flag(2378, 3085, 0, Region.TILE_BLOCKED);
        World.flag(2377, 3085, 0, Region.TILE_BLOCKED);
        World.flag(2377, 3086, 0, Region.TILE_BLOCKED);
        World.flag(2377, 3087, 0, Region.TILE_BLOCKED);
        World.flag(2377, 3088, 0, Region.TILE_BLOCKED);*/
	}
}
