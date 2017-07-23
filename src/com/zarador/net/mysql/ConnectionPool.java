package com.zarador.net.mysql;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * A Thread-Safe connection pool, In the future, if needed a class can be a
 * DatabaseConnection sub-instance to add onto the pooled connection types
 * 
 * @author Nikki
 * 
 */
public class ConnectionPool<T extends DatabaseConnection> {

	/**
	 * The configuration to use for this pool
	 */
	private DatabaseConfiguration configuration;

	/**
	 * The max connections this pool can have.
	 */
	private int maxConnections;

	/**
	 * The amount of current connections..
	 */
	private int currentConnections = 0;

	/**
	 * A thread-safe linked queue which contains our connections
	 */
	private Queue<DatabaseConnection> pool = new ConcurrentLinkedQueue<>();

	/**
	 * Create a new database pool
	 * 
	 * @param configuration
	 *            The configuration
	 */
	public ConnectionPool(DatabaseConfiguration configuration) {
		this(configuration, 10);
	}

	/**
	 * Create a new database pool
	 * 
	 * @param configuration
	 *            The configuration
	 * @param maxConnections
	 *            The connection limit
	 */
	public ConnectionPool(DatabaseConfiguration configuration, int maxConnections) {
		this.configuration = configuration;
		this.maxConnections = maxConnections;

		Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
			for (Iterator<DatabaseConnection> it$ = pool.iterator(); it$.hasNext();) {
				DatabaseConnection connection = it$.next();
				Statement s = null;
				try {
					s = connection.getConnection().createStatement();
					// System.out.println(++lol+" - mysql query pings.");
					s.execute("/* ping */ SELECT 1");
				} catch (SQLException e) {
					e.printStackTrace();
					connection.close();
					it$.remove();
				} finally {
					try {
						if (s != null)
							s.close();
					} catch (SQLException e) {

					}
				}
			}
		}, 0, 30000, TimeUnit.MILLISECONDS);
	}

	/**
	 * Get the next free database connection
	 * 
	 * @return The connection if found, or a new connection.
	 */
	@SuppressWarnings("unchecked")
	public DatabaseConnection nextFree() {
		// First check if a connection is free
		DatabaseConnection connection = pool.poll();
		if (connection != null) {
			if (!connection.isFresh()) {
				// DISCARD, since the connection is bad
				// connection.close();
				// System.out.println("CLOSED: "+currentConnections);
				currentConnections--;
				pool.remove(connection);
				return nextFree();
			}
			// System.out.println("CAUGHT: "+currentConnections);
			return connection;
		}
		if (currentConnections >= maxConnections) {
			return null;
		}
		// If we don't find a connection, create a new one!
		connection = configuration.newConnection();
		connection.setPool((ConnectionPool<DatabaseConnection>) this);
		if (!connection.connect()) {
			System.out.println("Connection was unable to connect! Current connections: " + currentConnections);
			return nextFree();
		} else {
			currentConnections++;
		}
		return connection;
	}

	/**
	 * Return a connection to this pool
	 * 
	 * @param connection
	 *            The connection
	 */
	public void returnConnection(DatabaseConnection connection) {
		pool.offer(connection);
	}
}