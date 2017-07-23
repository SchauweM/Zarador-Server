package com.zarador.net.login;

import java.util.concurrent.ConcurrentHashMap;

import org.jboss.netty.channel.Channel;

import com.zarador.GameSettings;
import com.zarador.net.PlayerSession;
import com.zarador.net.packet.PacketBuilder;
import com.zarador.world.entity.impl.player.Player;
import com.zarador.world.entity.impl.player.PlayerLoading;
import com.zarador.world.entity.impl.player.bot.Bot;

public final class LoginManager {

	private static final ConcurrentHashMap<Long, Player> successfulLogins = new ConcurrentHashMap<>();

	public static ConcurrentHashMap<Long, Player> getSuccessfulLogins() {
		return successfulLogins;
	}

	/**
	 * Starts the login process by adding the player's session info to the
	 * pending login list and starting the loading request (either JSON or SQL).
	 *
	 * @param session
	 * @param msg
	 * @return
	 */
	public static void startLogin(PlayerSession session, LoginDetailsMessage msg) {
		final Player player = session.getPlayer();
		PendingLogin.add(session, msg);
		if (GameSettings.MYSQL_PLAYER_LOADING) {
			PlayerLoading.performSqlRequest(player);
		} else if (GameSettings.JSON_PLAYER_LOADING) {
			PlayerLoading.loadJSON(player);
			LoginManager.finalizeLogin(session, msg);
		} else if (GameSettings.MONGO_PLAYER_LOADING) {
			//Do MongoDB Loading
		}
	}

	/**
	 * Submits the login finalization task to the threaded game engine for the
	 * specified player.
	 *
	 * @param player
	 */
	public static void finalizeLogin(final Player player) {
		PendingLogin pendingLogin = PendingLogin.get(player.getLongUsername());
		if (pendingLogin == null) {
			throw new RuntimeException("SEVERE LOGIN ERROR FOR " + player.getUsername()
					+ "! PendingLogin not found! Response: " + player.getResponse());
		}
		LoginManager.finalizeLogin(pendingLogin.getSession(), pendingLogin.getLoginDetails());
	}

	/**
	 * Finalizes the login for the specified session and login details.
	 *
	 * @param session
	 * @param msg
	 * @return
	 */
	private static void finalizeLogin(PlayerSession session, LoginDetailsMessage msg) {
		final Player player = session.getPlayer();
		player.setResponse(LoginResponses.getResponse(player, msg));
		boolean newAccount = player.getResponse() == LoginResponses.NEW_ACCOUNT;
		if (session.getPlayer() instanceof Bot) {
			newAccount = false; 
			System.err.println("found");
		}
		if (newAccount) {
			player.setNewPlayer(true);
			player.setPlayerLocked(true);
			player.setResponse(LoginResponses.LOGIN_SUCCESSFUL);
		}
		if (player.getResponse() == LoginResponses.LOGIN_SUCCESSFUL) {
			int rank = player.getCrown();
			/**
			 * Successful login.
			 */
			if (session.getChannel() != null)
			session.getChannel().write(new PacketBuilder().put((byte) 2).put((byte) rank).put((byte) 0).toPacket());
			/**
			 * Initialize the player in game.
			 */
			successfulLogins.put(player.getLongUsername(), player);
			// PlayerHandler.handleLogin(player);
		} else {
			LoginManager.sendReturnCode(session.getChannel(), player.getResponse());
		}
		/**
		 * Remove the player's pending login now that the task is complete.
		 */
		PendingLogin.remove(player.getLongUsername());
	}

	public static void sendReturnCode(final Channel channel, final int code) {
		channel.write(new PacketBuilder().put((byte) code).toPacket())
				.addListener(listener -> listener.getChannel().close());
	}

}