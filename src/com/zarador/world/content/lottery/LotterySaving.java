package com.zarador.world.content.lottery;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.zarador.util.Misc;
import com.zarador.world.World;
import com.zarador.world.entity.impl.player.Player;

public class LotterySaving {

	/**
	 * The length of the lottery in minutes
	 * 7 days
	 */
	public static final int lotteryLength = 10080; //10080 = 7 days

	public static Lotteries lottery1 = new Lotteries(null);
	public static long lottery1Timer = 0;

	public static boolean LOTTERY_ON = false;

	public static int getMinutesRemaining() {
		return (lotteryLength - Misc.getMinutesPassed(System.currentTimeMillis() - lottery1Timer));
	}

	public static String getTime() {
		return getMinutesRemaining()/24/60 + " days, " + getMinutesRemaining()/60%24 + " hours, " + getMinutesRemaining()%60+" minutes";
	}

	/**
	 * Log file path
	 **/
	private static final String LOG_FILE_PATH = "./data/saves/lottery/";

	public static void log(String action, String username) {
		try {
			FileWriter fw = new FileWriter(LOG_FILE_PATH + "log.txt", true);
			if (fw != null) {
				fw.write(getRealTime() + action + " - "+username+"\t");
				fw.write(System.lineSeparator());
				fw.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Fetches system time and formats it appropriately
	 *
	 * @return Formatted time
	 */
	private static String getRealTime() {
		Date getDate = new Date();
		String timeFormat = "M/d/yy hh:mma";
		SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
		return "[" + sdf.format(getDate) + "] ";
	}

	public static int getTicketsInLottery(Player player) {
		int total = 0;
		for(Lottery lottery: LotterySaving.lottery1.getOffers()) {
			if(lottery == null) {
				continue;
			}
			if(lottery.getUsername() == null) {
				continue;
			}
			if(lottery.getUsername().equalsIgnoreCase(player.getUsername())) {
				total++;
			}
		}
		return total;
	}

	public static void execute() {
		if(getMinutesRemaining() <= 0) {
			if(LOTTERY_ON) {
				int index = new Random().nextInt(LotterySaving.lottery1.getOffers().size());
				String winner = LotterySaving.lottery1.getOffers().get(index).getUsername();
				int totalInLottery = 0;
				for(Lottery lottery: LotterySaving.lottery1.getOffers()) {
					if(lottery == null) {
						continue;
					}
					if(lottery.getUsername() == null) {
						continue;
					}
					if(lottery.getUsername().equalsIgnoreCase(winner)) {
						totalInLottery++;
					}
				}
				double percentage = (double) totalInLottery / LotterySaving.lottery1.getOffers().size() * 100;
				DecimalFormat format = new DecimalFormat("#.##");
				percentage = Double.valueOf(format.format(percentage));
				Player other = World.getPlayerByName(winner);
				if(other != null) {
					other.forceChat("[Argos] I have just won the $50 Lottery!");
					other.getPacketSender().sendMessage("Congratulations, you won the $50 Lottery. Contact a Owner!");
				}
				LotterySaving.log("win", winner);
				World.sendMessage("<icon=1><shad=FF8C38>[News] " + winner + " has won the $50 cash Lottery with a "+percentage+"% chance.");
				LOTTERY_ON = false;
				lottery1Timer = 0;
				save();
			}
		}
	}

	/**
	 * Load the Lottery
	 */
	public static void load() {
		System.out.println("Loading the lottery...");
		File folder = new File("./data/saves/lottery");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				if (listOfFiles[i].getName().contains(".dat")) {
					loadLottery(listOfFiles[i].getName());
				}
			}
		}
		System.out.println("Finished loading the lottery.");
	}

	public static void loadLottery(String fileName) {
		try {
			File file = new File("./data/saves/lottery/"+fileName);
			if (!file.exists()) {
				return;
			}
			DataInputStream in = new DataInputStream(new FileInputStream(file));

			int total = in.readInt();
			lottery1Timer = in.readLong();
			LOTTERY_ON = in.readBoolean();
			Lottery[] offers = new Lottery[total];
			for (int i2 = 0; i2 < total; i2++) {
				offers[i2] = new Lottery(in.readUTF());
			}

			if(fileName.equals("1.dat")) {
				lottery1 = new Lotteries(offers);
			}

			in.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static SortedMap<String, Object> getByPreffix(
			NavigableMap<String, Object> myMap,
			String preffix ) {
		return myMap.subMap( preffix, preffix + Character.MAX_VALUE );
	}

	private static final Executor saveWorker = Executors.newSingleThreadExecutor();

	private static final Runnable saveRunnable = () -> saveLotteries();

	public static void saveLotteries() {
		saveLottery(lottery1);
	}

	public static void saveLottery(Lotteries offer) {
		File pos = new File("./data/saves/lottery");
		if (!pos.exists()) {
			pos.mkdirs();
		}
		RandomAccessFile file = null;
		try {
			file = new RandomAccessFile(pos + "/1.dat", "rw");
			offer.saveRAF(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (file != null) {
				try {
					file.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void save() {
		saveWorker.execute(saveRunnable);
	}

}