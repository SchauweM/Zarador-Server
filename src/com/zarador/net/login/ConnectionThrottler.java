package com.zarador.net.login;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public final class ConnectionThrottler {

	private static final Logger log = Logger.getLogger(ConnectionThrottler.class.getName());

	private final ConcurrentHashMap<String, Long> throttledHostTimestamps;
	private final ConcurrentHashMap<String, Long> lastConnectionAttempt;
	private final ConcurrentHashMap<String, Integer> totalAttempts;
	private final int throttleTimer;

	public ConnectionThrottler(int throttleInSeconds) {
		this.throttledHostTimestamps = new ConcurrentHashMap<>();
		this.lastConnectionAttempt = new ConcurrentHashMap<>();
		this.totalAttempts = new ConcurrentHashMap<>();
		this.throttleTimer = throttleInSeconds;
	}

	public boolean verifyConnection(String host) {
		/**
		 * If the connection is throttled, reject the connection.
		 */
		if (isThrottled(host)) {
			return false;
		}

		/**
		 * Increment the total attempts to connect by 1 for the host.
		 */
		if (this.totalAttempts.containsKey(host)) {
			this.totalAttempts.put(host, this.totalAttempts.get(host) + 1);
		} else {
			this.totalAttempts.put(host, 1);
		}

		/**
		 * Check to see how long it's been since the last connection attempt. If
		 * less than or equal to 1 second and the total attempts is 5 or more,
		 * the connection will be throttled.
		 */
		long now = System.currentTimeMillis();
		if (this.lastConnectionAttempt.containsKey(host)) {
			long lastAttempt = this.lastConnectionAttempt.get(host);
			long timeSinceLast = now - lastAttempt;
			int totalAttempts = this.totalAttempts.get(host);
			if (timeSinceLast < 1000 && totalAttempts > 10) {
				log.info("Throttled " + host + "! Time since last attempt: " + (timeSinceLast) + "ms, total attempts: "
						+ totalAttempts);
				this.throttle(host, now);
				return false;
			}
		}

		this.lastConnectionAttempt.put(host, now);

		/**
		 * The connection is free to establish.
		 */
		return true;
	}

	/**
	 * If the connection is still throttled, return true. If the connection
	 * shouldn't be throttled anymore, remove reset connection maps for host.
	 * 
	 * @param host
	 * @return
	 */
	private boolean isThrottled(String host) {
		if (!this.throttledHostTimestamps.containsKey(host)) {
			return false;
		}
		long throttleTimestamp = this.throttledHostTimestamps.get(host);
		long timeSinceThrottled = System.currentTimeMillis() - throttleTimestamp;
		long throttleTimer = (this.throttleTimer * 1000);
		if (timeSinceThrottled >= throttleTimer) {
			this.throttledHostTimestamps.remove(host);
			this.totalAttempts.remove(host);
			this.lastConnectionAttempt.remove(host);
			log.info(host + " no longer being throttled!");
			return false;
		}
		log.severe(host + " still throttled. Time since connection throttled: " + (timeSinceThrottled)
				+ "ms, throttle timer: " + (this.throttleTimer * 1000) + "ms");
		return true;
	}

	private boolean throttle(String host, long timestamp) {
		if (this.throttledHostTimestamps.containsKey(host)) {
			log.severe(host + " already being throttled!");
			return false;
		}
		this.throttledHostTimestamps.put(host, timestamp);
		return true;
	}

}
