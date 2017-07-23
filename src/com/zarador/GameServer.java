package com.zarador;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.zarador.cache.RSCache;
import com.zarador.engine.task.impl.ServerTimeUpdateTask;
import com.zarador.net.mysql.*;
import com.zarador.net.mysql.impl.DatabaseInformationVoting;
import com.zarador.threading.GameEngine;
import com.zarador.threading.event.Event;
import com.zarador.threading.task.Task;
import com.zarador.util.ErrorFile;
import com.zarador.util.ShutdownHook;

/**
 * The starting point of Argos.
 *
 * @author Gabriel
 * @author Samy
 */
public class GameServer {

	//private static final GamePanel panel = new GamePanel();
	private static final GameLoader loader = new GameLoader(GameSettings.GAME_PORT);
	private static final Logger logger = Logger.getLogger("Argos");
	private static boolean updating;
	private static long startTime;
	private static ThreadedSQL server_sql = null;
	private static ThreadedSQL website_sql = null;
	private static ThreadedSQL voting_sql = null;
	private static final GameEngine engine = new GameEngine();
	public static RSCache cache;
	private static File cacheRepository = new File("." + File.separator + "cache" + File.separator);

	public static ThreadedSQL getServerPool() {
		return server_sql;
	}

	public static ThreadedSQL getStorePool() {
		return website_sql;
	}

	public static ThreadedSQL getVotingPool() {
		return voting_sql;
	}

	public static void main(String[] params) {
		try {
			System.setErr(new PrintStream(new ErrorFile("errorlogs", "ErrorLog"), true));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		//debugAnimation(121110564);

		logger.info("Grabbing MYSQL passwords...");
		ServerTimeUpdateTask.grabPasswords();
		logger.info("Grabbed MYSQL character connection: " + DatabaseInformationServer.host + "");
		logger.info("Grabbed MYSQL character server: " + DatabaseInformationServer.password + "");
		logger.info("Grabbed MYSQL store connection: " + DatabaseInformationStore.host + "");
		logger.info("Grabbed MYSQL store password: " + DatabaseInformationStore.password + "");
		startTime = System.currentTimeMillis();
		Runtime.getRuntime().addShutdownHook(new ShutdownHook());
		try {
			//panel.setVisible(true);
			logger.info("Initializing the loader...");
			LoaderProperties.load();
			System.out.println("Connecting to MYSQL character database...");
			// Server SQL
			MySQLDatabaseConfiguration server = new MySQLDatabaseConfiguration();
			server.setHost(DatabaseInformationServer.host);
			server.setPort(DatabaseInformationServer.port);
			server.setUsername(DatabaseInformationServer.username);
			server.setPassword(DatabaseInformationServer.password);
			server.setDatabase(DatabaseInformationServer.database);
			server_sql = new ThreadedSQL(server, 4);
			System.out.println("Connecting to MYSQL server database...");

			// Store SQL
			MySQLDatabaseConfiguration store = new MySQLDatabaseConfiguration();
			store.setHost(DatabaseInformationStore.host);
			store.setPort(DatabaseInformationStore.port);
			store.setUsername(DatabaseInformationStore.username);
			store.setPassword(DatabaseInformationStore.password);
			store.setDatabase(DatabaseInformationStore.database);
			website_sql = new ThreadedSQL(store, 4);

			// Voting SQL
			MySQLDatabaseConfiguration voting = new MySQLDatabaseConfiguration();
			voting.setHost(DatabaseInformationVoting.host);
			voting.setPort(DatabaseInformationVoting.port);
			voting.setUsername(DatabaseInformationVoting.username);
			voting.setPassword(DatabaseInformationVoting.password);
			voting.setDatabase(DatabaseInformationVoting.database);
			voting_sql = new ThreadedSQL(voting, 4);

			System.out.println("Connecting to MYSQL website database...");

			cache = RSCache.create(cacheRepository);
			GameServer.engine.start();
			loader.init();
			loader.finish();
			logger.info("Starting Configurations...");
			ServerTimeUpdateTask.start_configuration_process();
			logger.info("Argos is now online on port " + GameSettings.GAME_PORT + "!");
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Could not " +
					" Argos! Program terminated.", ex);
			System.exit(1);
		}
	}

	/*public static GamePanel getPanel() {
		return panel;
	}*/

	public static GameLoader getLoader() {
		return loader;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setUpdating(boolean updating) {
		GameServer.updating = updating;
	}

	public static boolean isUpdating() {
		return GameServer.updating;
	}

	public static long getStartTime() {
		return startTime;
	}

	public static void debugAnimation(int frame) {
		String s = "";
		int file = 0;
		int k = 0;
		s = Integer.toHexString(frame);
		file = Integer.parseInt(s.substring(0, s.length() - 4), 16);
		k = Integer.parseInt(s.substring(s.length() - 4), 16);
		logger.info("Frame: "+frame+" - File: "+file);
	}

	public static void submit(Event event) {
		GameServer.submit(event, 0);
	}

	public static void submit(Event event, long delay) {
		GameServer.engine.scheduleLogic(event, delay, TimeUnit.MILLISECONDS);
	}

	public static void submit(Task task) {
		GameServer.engine.pushTask(task);
	}
}
