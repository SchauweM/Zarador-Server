package com.zarador.net.login;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import com.zarador.net.PlayerSession;
import com.zarador.util.NameUtils;
import com.zarador.world.entity.impl.player.Player;

public final class PendingLogin {

	private static final Logger log = Logger.getLogger(PendingLogin.class.getName());
	private static final ConcurrentHashMap<Long, PendingLogin> pendingLogins = new ConcurrentHashMap<>();

	/**
	 * Returns the pending login instance for the specified user hash.
	 * 
	 * @param userHash
	 * @return
	 */
	public static PendingLogin get(long userHash) {
		if (!contains(userHash)) {
			log.severe("Pending login does not exist for player: " + userHash + "(" + NameUtils.longToString(userHash)
					+ ")");
			return null;
		}
		return pendingLogins.get(userHash);
	}

	public static ConcurrentHashMap getPendingLogins() {
		return pendingLogins;
	}

	/**
	 * Adds the player's session and login details to the pending login map.
	 * 
	 * @param session
	 * @param loginDetails
	 * @return
	 */
	public static boolean add(PlayerSession session, LoginDetailsMessage loginDetails) {
		final long userHash = session.getPlayer().getLongUsername();
		if (contains(userHash)) {
			log.info("Pending logins already contains player: " + loginDetails.getUsername());
			return false;
		}
		PendingLogin pendingLogin = new PendingLogin(session, loginDetails);
		pendingLogins.put(userHash, pendingLogin);
		return true;
	}

	/**
	 * Attempts to remove the specified user hash's pending login instance.
	 * 
	 * @param user
	 * @return
	 */
	public static boolean remove(long user) {
		if (!pendingLogins.containsKey(user)) {
			log.severe("Pending logins does not contain user hash: " + user + "(" + NameUtils.longToString(user) + ")");
			return false;
		}
		pendingLogins.remove(user);
		return true;
	}

	/**
	 * Returns whether or not there is a pending login for the specified user
	 * hash.
	 * 
	 * @param user
	 * @return
	 */
	public static boolean contains(long user) {
		return pendingLogins.containsKey(user);
	}

	private final PlayerSession session;
	private final LoginDetailsMessage loginDetails;

	public LoginDetailsMessage getLoginDetails() {
		return this.loginDetails;
	}

	public PlayerSession getSession() {
		return this.session;
	}

	public Player getPlayer() {
		return this.session.getPlayer();
	}

	public PendingLogin(PlayerSession session, LoginDetailsMessage loginDetails) {
		this.session = session;
		this.loginDetails = loginDetails;
	}

}