package com.zarador.net.login;

import com.zarador.GameServer;
import com.zarador.GameSettings;
import com.zarador.net.security.ConnectionHandler;
import com.zarador.util.NameUtils;
import com.zarador.world.World;
import com.zarador.world.entity.impl.player.Player;

public final class LoginResponses {

	public static final int getResponse(Player player, LoginDetailsMessage msg) {
		int playerLoadingResponse = player.getResponse();
		if (World.getPlayers().isFull()) {
			return LOGIN_WORLD_FULL;
		}
		if (GameServer.isUpdating()) {
			return LOGIN_GAME_UPDATE;
		}
		if (!NameUtils.isValidName(player.getUsername())) {
			return LOGIN_INVALID_CREDENTIALS;
		}
		if (player.getUsername().startsWith(" ")) {
			return USERNAME_STARTS_WITH_SPACE;
		}
		/*if (!GameSettings.client_version.equals(msg.getClientVersion())
				&& !GameSettings.client_version.equals("none")
				&& !msg.getClientVersion().equals("invalid_connection")) {
			return OLD_CLIENT_VERSION;
		}*/
		if (World.getPlayerByName(player.getUsername()) != null) {
			return LOGIN_ACCOUNT_ONLINE;
		}

		/** CHAR FILE LOADING **/
		if (playerLoadingResponse != LOGIN_SUCCESSFUL && playerLoadingResponse != NEW_ACCOUNT) {
			return playerLoadingResponse;
		}

		/** BANS AND ACCESS LIMITS **/
		int hostHandlerResponse = ConnectionHandler.getResponse(player, msg);
		if (hostHandlerResponse != LOGIN_SUCCESSFUL) {
			System.out.println("Access limit reached for " + player.getUsername() + "");
			return hostHandlerResponse;
		}

		return playerLoadingResponse;
	}

	/**
	 * This opcode is used for data (session keys, name, password, etc)
	 * exchange.
	 */
	public static final int LOGIN_EXCHANGE_DATA = 0;

	/**
	 * This login opcode is used for a 2000ms delay, after which a reconnection
	 * is attempted.
	 */
	public static final int LOGIN_DELAY = 1;

	/**
	 * This login opcode signifies a successful login.
	 */
	public static final int LOGIN_SUCCESSFUL = 2;

	/**
	 * This login opcode is used when the player has entered an invalid username
	 * and/or password.
	 */
	public static final int LOGIN_INVALID_CREDENTIALS = 3;

	/**
	 * This login opcode is used when the account has been disabled.
	 */
	public static final int LOGIN_DISABLED_ACCOUNT = 4;

	/**
	 * This login opcode is used when the player's IP has been disabled.
	 */
	public static final int LOGIN_DISABLED_COMPUTER = 22;

	/**
	 * This login opcode is used when the player's IP has been disabled.
	 */
	public static final int LOGIN_DISABLED_IP = 27;

	/**
	 * This login opcode is used when the account attempting to connect is
	 * already online in the server.
	 */
	public static final int LOGIN_ACCOUNT_ONLINE = 5;

	/**
	 * This login opcode is used when the game has been or is being updated.
	 */
	public static final int LOGIN_GAME_UPDATE = 6;

	/**
	 * This login opcode is used when server is launching.
	 */
	public static final int SERVER_LAUNCHING = 23;

	/**
	 * This login opcode is used when the world being connected to is full.
	 */
	public static final int LOGIN_WORLD_FULL = 7;

	/**
	 * This login opcode is used when server is offline.
	 */
	public static final int LOGIN_SERVER_OFFLINE = 8;

	/**
	 * This login opcode is used when the connections
	 */
	public static final int LOGIN_CONNECTION_LIMIT = 9;

	/**
	 * This login opcode is used when a connection has received a bad session
	 * id.
	 */
	public static final int LOGIN_BAD_SESSION_ID = 10;

	/**
	 * This login opcode is used when the login procedure has rejected the
	 * session.
	 */
	public static final int LOGIN_REJECT_SESSION = 11;

	/**
	 * This login opcode is used when a non-member player is attempting to login
	 * to a members world.
	 */
	public static final int LOGIN_WORLD_MEMBER_ACCOUNT_REQUIRED = 12;

	/**
	 * This login opcode is used when the login procedure could not be
	 * completed.
	 */
	public static final int LOGIN_COULD_NOT_COMPLETE = 13;

	/**
	 * This login opcode is used when the game is being updated.
	 */
	public static final int LOGIN_WORLD_UPDATE = 14;

	/**
	 * This login opcode is received upon a reconnection so that chat messages
	 * will not dissappear.
	 */
	public static final int LOGIN_RECONNECTION = 15;

	/**
	 * This login opcode is used when a player has exceeded their attempts to
	 * login.
	 */
	public static final int LOGIN_EXCESSIVE_ATTEMPTS = 16;

	/**
	 * This login opcode is used when a member is attempting to login to a
	 * non-members world in a members-only area.
	 */
	public static final int LOGIN_AREA_MEMBER_ACCOUNT_REQUIRED = 17;

	/**
	 * This login opcode is used when the login server is invalid.
	 */
	public static final int LOGIN_INVALID_LOGIN_SERVER = 20;

	/**
	 * This login opcode is used when a player has just left another world and
	 * has to wait a delay before entering a new world.
	 */
	public static final int LOGIN_WORLD_DELAY = 21;

	/**
	 * This login opcode is used when a player has entered invalid credentials.
	 */
	public static final int INVALID_CREDENTIALS = 28;

	/**
	 * This login opcode is used when a player has attempted to login with a old
	 * client.
	 */
	public static final int OLD_CLIENT_VERSION = 30;

	/**
	 * This login opcode is used when a player's username is started with a
	 * space
	 */
	public static final int USERNAME_STARTS_WITH_SPACE = 31;

	/**
	 * The new connection login request byte data sent by the client.
	 */
	public static final int NEW_CONNECTION_LOGIN_REQUEST = 16;

	/**
	 * The reconnection login request byte data sent by the client.
	 */
	public static final int RECONNECTING_LOGIN_REQUEST = 18;

	/**
	 * New account
	 */
	public static final int NEW_ACCOUNT = -1;

}
