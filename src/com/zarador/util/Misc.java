package com.zarador.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.buffer.ChannelBuffer;

import com.zarador.model.Item;
import com.zarador.model.Position;
import com.zarador.world.content.combat.CombatContainer.ContainerHit;
import com.zarador.world.entity.impl.CharacterList;
import com.zarador.world.entity.impl.player.Player;

public class Misc {

	/** Random instance, used to generate pseudo-random primitive types. */
	public static final Random RANDOM = new Random(System.currentTimeMillis());

	private static ZonedDateTime zonedDateTime;
	public static final int HALF_A_DAY_IN_MILLIS = 43200000;

	public static final int MINUTE = 1_000;

	public static final int HOUR = 60 * MINUTE;

	public static final int DAY = 24 * HOUR;

	protected static final java.util.Random random = new java.util.Random();

	public static ContainerHit[] concat(ContainerHit[] a, ContainerHit[] b) {
		int aLen = a.length;
		int bLen = b.length;
		ContainerHit[] c = new ContainerHit[aLen + bLen];
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);
		return c;
	}

	/**
	 * Formats the name by checking if it starts with a vowel.
	 * @param name	The string to format.
	 */
	public static String getArticle(String name) {
		char letter = Character.toLowerCase(name.charAt(0));
		boolean vowel = letter == 'a' || letter == 'e' || letter == 'i' || letter == 'o' || letter == 'u';
		String other = vowel ? "an" : "a";
		if (name.endsWith("s")) {
			other += " pair of";
		}
		return other + " " + name;
	}

	/**
	 * Returns if the starting character is a vowel
	 *
	 * @param ch The character being checked
	 */
	public static boolean isVowel(char ch) {
		ch = Character.toLowerCase(ch);
		return (ch == 'a') || (ch == 'e') || (ch == 'i') || (ch == 'o') || (ch == 'u');
	}

	public static boolean contains(final int[] arr, final int key) {
		return Arrays.stream(arr).anyMatch(i -> i == key);
	}

	public static final String formatAmount(long amount) {
		String format = "Too high!";
		if (amount >= 0 && amount < 100000) {
			format = String.valueOf(amount);
		} else if (amount >= 100000 && amount < 1000000) {
			format = amount / 1000 + "K";
		} else if (amount >= 1000000 && amount < 10000000000L) {
			format = amount / 1000000 + "M";
		} else if (amount >= 10000000000L && amount < 1000000000000L) {
			format = amount / 1000000000 + "B";
		} else if (amount >= 10000000000000L && amount < 10000000000000000L) {
			format = amount / 1000000000000L + "T";
		} else if (amount >= 10000000000000000L && amount < 1000000000000000000L) {
			format = amount / 1000000000000000L + "QD";
		} else if (amount >= 1000000000000000000L && amount < Long.MAX_VALUE) {
			format = amount / 1000000000000000000L + "QT";
		}
		return format;
	}

	public static List<Player> getCombinedPlayerList(Player p) {
		List<Player> plrs = new LinkedList<Player>();
		for (Player localPlayer : p.getLocalPlayers()) {
			if (localPlayer != null)
				plrs.add(localPlayer);
		}
		plrs.add(p);
		return plrs;
	}

	public static String getCurrentServerTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
		String formattedDate = dateFormat.format(new Date()).toString();
		return formattedDate;
	}

	public static String getTimePlayed(long totalPlayTime) {
		final int sec = (int) (totalPlayTime / 1000), h = sec / 3600, m = sec / 60 % 60, s = sec % 60;
		return (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s);
	}

	public static String getRottenPotatoTime(long time) {
		final int sec = (int) (time / 1000), h = sec / 3600, m = sec / 60 % 60, s = sec % 60;
		return (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s);
	}

	public static int getMinutesPlayed(Player p) {
		long totalPlayTime = p.getTotalPlayTime() + (p.getRecordedLogin().elapsed());
		int sec = (int) (totalPlayTime / 1000), h = sec / 3600, m = sec / 60 % 60;
		for (int i = 0; i < h; i++)
			m += 60;
		return m;
	}

	public static String getHoursPlayed(long totalPlayTime) {
		final int sec = (int) (totalPlayTime / 1000), h = sec / 3600;
		return (h < 10 ? "0" + h : h) + "h";
	}

	public static int getHoursPlayedNumeric(long totalPlayTime) {
		final int sec = (int) (totalPlayTime / 1000), h = sec / 3600;
		return h;
	}

	public static int getMinutesPassed(long t) {
		int seconds = (int) ((t / 1000) % 60);
		int minutes = (int) (((t - seconds) / 1000) / 60);
		return minutes;
	}

	public static Item[] concat(Item[] a, Item[] b) {
		int aLen = a.length;
		int bLen = b.length;
		Item[] c = new Item[aLen + bLen];
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);
		return c;
	}

	public static Player getCloseRandomPlayer(CharacterList<Player> plrs) {
		int index = Misc.getRandom(plrs.size() - 1);
		if (index > 0)
			return plrs.get(index);
		return null;
	}

	public static byte directionDeltaX[] = new byte[] { 0, 1, 1, 1, 0, -1, -1, -1 };
	public static byte directionDeltaY[] = new byte[] { 1, 1, 0, -1, -1, -1, 0, 1 };
	public static byte xlateDirectionToClient[] = new byte[] { 1, 2, 4, 7, 6, 5, 3, 0 };

	public static int getRandom(int range) {
		return (int) (java.lang.Math.random() * (range + 1));
	}

	public static int direction(int srcX, int srcY, int x, int y) {
		double dx = (double) x - srcX, dy = (double) y - srcY;
		double angle = Math.atan(dy / dx);
		angle = Math.toDegrees(angle);
		if (Double.isNaN(angle))
			return -1;
		if (Math.signum(dx) < 0)
			angle += 180.0;
		return (int) ((((90 - angle) / 22.5) + 16) % 16);
		/*
		 * int changeX = x - srcX; int changeY = y - srcY; for (int j = 0; j <
		 * directionDeltaX.length; j++) { if (changeX == directionDeltaX[j] &&
		 * changeY == directionDeltaY[j]) return j; } return -1;
		 */
	}

	public static String ucFirst(String str) {
		str = str.toLowerCase();
		if (str.length() > 1) {
			str = str.substring(0, 1).toUpperCase() + str.substring(1);
		} else {
			return str.toUpperCase();
		}
		return str;
	}

	public static String toTitleCase(String str) {
		str = str.toLowerCase();
		String[] arr = str.split(" ");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			sb.append(Character.toUpperCase(arr[i].charAt(0))).append(arr[i].substring(1)).append(" ");
		}
		return sb.toString().trim();
	}

	public static String format(int num) {
		return NumberFormat.getInstance().format(num);
	}

	public static String format(long num) {
		return NumberFormat.getInstance().format(num);
	}

	public static String formatText(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (i == 0) {
				s = String.format("%s%s", Character.toUpperCase(s.charAt(0)), s.substring(1));
			}
			if (!Character.isLetterOrDigit(s.charAt(i))) {
				if (i + 1 < s.length()) {
					s = String.format("%s%s%s", s.subSequence(0, i + 1), Character.toUpperCase(s.charAt(i + 1)),
							s.substring(i + 2));
				}
			}
		}
		return s.replace("_", " ");
	}

	/**
	 * Returns a linearly distributed pseudorandom integer.
	 * 
	 * @param min
	 *            The inclusive lower bound.
	 * @param max
	 *            The exclusive upper bound.
	 * @return Random integer min <= n < max.
	 */
	public static int random(final int min, final int max) {
		return min + (max == min ? 0 : random.nextInt(max - min));
	}

	public static int random(int rand) {
		return RandomGenerator.nextInt(rand + 1);
	}

	public static int nextInt(int n) {
		try {
			if(n < 0) {
				throw new Exception("Negative random generator call");
			} else {
				return random.nextInt(n);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return random.nextInt(1);
		}
	}

	public static String getTotalAmount(int j) {
		if (j >= 10000 && j < 10000000) {
			return j / 1000 + "K";
		} else if (j >= 10000000 && j <= 2147483647) {
			return j / 1000000 + "M";
		} else {
			return "" + j + " coins";
		}
	}

	public static String formatPlayerName(String str) {
		String str1 = Misc.ucFirst(str);
		str1.replace("_", " ");
		return str1;
	}

	public static String insertCommasToNumber(String number) {
		return number.length() < 4 ? number
				: insertCommasToNumber(number.substring(0, number.length() - 3)) + ","
						+ number.substring(number.length() - 3, number.length());
	}

	private static char decodeBuf[] = new char[4096];

	public static String textUnpack(byte packedData[], int size) {
		int idx = 0, highNibble = -1;
		for (int i = 0; i < size * 2; i++) {
			int val = packedData[i / 2] >> (4 - 4 * (i % 2)) & 0xf;
			if (highNibble == -1) {
				if (val < 13)
					decodeBuf[idx++] = xlateTable[val];
				else
					highNibble = val;
			} else {
				decodeBuf[idx++] = xlateTable[((highNibble << 4) + val) - 195];
				highNibble = -1;
			}
		}

		return new String(decodeBuf, 0, idx);
	}

	public static char xlateTable[] = { ' ', 'e', 't', 'a', 'o', 'i', 'h', 'n', 's', 'r', 'd', 'l', 'u', 'm', 'w', 'c',
			'y', 'f', 'g', 'p', 'b', 'v', 'k', 'x', 'j', 'q', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			' ', '!', '?', '.', ',', ':', ';', '(', ')', '-', '&', '*', '\\', '\'', '@', '#', '+', '=', '\243', '$',
			'%', '"', '[', ']' };

	public static String anOrA(String s) {
		s = s.toLowerCase();
		if (s.equalsIgnoreCase("anchovies") || s.equalsIgnoreCase("soft clay") || s.equalsIgnoreCase("cheese")
				|| s.equalsIgnoreCase("ball of wool") || s.equalsIgnoreCase("spice")
				|| s.equalsIgnoreCase("steel nails") || s.equalsIgnoreCase("snape grass") || s.equalsIgnoreCase("coal"))
			return "some";
		if (s.startsWith("a") || s.startsWith("e") || s.startsWith("i") || s.startsWith("o") || s.startsWith("u"))
			return "an";
		return "a";
	}

	@SuppressWarnings("rawtypes")
	public static Class[] getClasses(String packageName) throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}
		ArrayList<Class> classes = new ArrayList<Class>();
		for (File directory : dirs) {
			classes.addAll(findClasses(directory, packageName));
		}
		return classes.toArray(new Class[classes.size()]);
	}

	@SuppressWarnings("rawtypes")
	private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
		List<Class> classes = new ArrayList<Class>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				classes.add(
						Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
			}
		}
		return classes;
	}

	public static int randomMinusOne(int length) {
		return getRandom(length - 1);
	}

	public static String removeSpaces(String s) {
		return s.replace(" ", "");
	}

	public static int getMinutesElapsed(int minute, int hour, int day, int year) {
		Calendar i = Calendar.getInstance();

		if (i.get(1) == year) {
			if (i.get(6) == day) {
				if (hour == i.get(11)) {
					return i.get(12) - minute;
				}
				return (i.get(11) - hour) * 60 + (59 - i.get(12));
			}

			int ela = (i.get(6) - day) * 24 * 60 * 60;
			return ela > 2147483647 ? 2147483647 : ela;
		}

		int ela = getElapsed(day, year) * 24 * 60 * 60;

		return ela > 2147483647 ? 2147483647 : ela;
	}

	public static int getDayOfYear() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int days = 0;
		int[] daysOfTheMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		if ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0)) {
			daysOfTheMonth[1] = 29;
		}
		days += c.get(Calendar.DAY_OF_MONTH);
		for (int i = 0; i < daysOfTheMonth.length; i++) {
			if (i < month) {
				days += daysOfTheMonth[i];
			}
		}
		return days;
	}

	public static int getYear() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.YEAR);
	}

	public static int getElapsed(int day, int year) {
		if (year < 2013) {
			return 0;
		}

		int elapsed = 0;
		int currentYear = Misc.getYear();
		int currentDay = Misc.getDayOfYear();

		if (currentYear == year) {
			elapsed = currentDay - day;
		} else {
			elapsed = currentDay;

			for (int i = 1; i < 5; i++) {
				if (currentYear - i == year) {
					elapsed += 365 - day;
					break;
				} else {
					elapsed += 365;
				}
			}
		}

		return elapsed;
	}

	public static boolean isWeekend() {
		int day = Calendar.getInstance().get(7);
		return (day == 1) || (day == 6) || (day == 7);
	}
	public static boolean isSaturday() {
		int day = Calendar.getInstance().get(7);
		return (day == 7);
	}

	/**
	 * Returns a pseudo-random {@code int} value between inclusive
	 * <code>min</code> and exclusive <code>max</code>.
	 * 
	 * <br>
	 * <br>
	 * This method is thread-safe. </br>
	 * 
	 * @param min
	 *            The minimum inclusive number.
	 * @param max
	 *            The maximum exclusive number.
	 * @return The pseudo-random {@code int}.
	 * @throws IllegalArgumentException
	 *             If the specified range is less <tt>0</tt>
	 */
	public static int exclusiveRandom(int min, int max) {
		if (max <= min) {
			max = min + 1;
		}
		return RANDOM.nextInt((max - min)) + min;
	}

	/**
	 * Returns a pseudo-random {@code int} value between inclusive <tt>0</tt>
	 * and exclusive <code>range</code>.
	 * 
	 * <br>
	 * <br>
	 * This method is thread-safe. </br>
	 * 
	 * @param range
	 *            The exclusive range.
	 * @return The pseudo-random {@code int}.
	 * @throws IllegalArgumentException
	 *             If the specified range is less <tt>0</tt>
	 */
	public static int exclusiveRandom(int range) {
		return exclusiveRandom(0, range);
	}

	/**
	 * Returns a pseudo-random {@code int} value between inclusive
	 * <code>min</code> and inclusive <code>max</code>.
	 * 
	 * @param min
	 *            The minimum inclusive number.
	 * @param max
	 *            The maximum inclusive number.
	 * @return The pseudo-random {@code int}.
	 * @throws IllegalArgumentException
	 *             If {@code max - min + 1} is less than <tt>0</tt>.
	 * @see {@link #exclusiveRandom(int)}.
	 */
	public static int inclusiveRandom(int min, int max) {
		if (max < min) {
			max = min + 1;
		}
		return exclusiveRandom((max - min) + 1) + min;
	}

	/**
	 * Returns a pseudo-random {@code int} value between inclusive <tt>0</tt>
	 * and inclusive <code>range</code>.
	 * 
	 * @param range
	 *            The maximum inclusive number.
	 * @return The pseudo-random {@code int}.
	 * @throws IllegalArgumentException
	 *             If {@code max - min + 1} is less than <tt>0</tt>.
	 * @see {@link #exclusiveRandom(int)}.
	 */
	public static int inclusiveRandom(int range) {
		return inclusiveRandom(0, range);
	}

	public static byte[] readFile(File s) {
		try {
			FileInputStream fis = new FileInputStream(s);
			FileChannel fc = fis.getChannel();
			ByteBuffer buf = ByteBuffer.allocate((int) fc.size());
			fc.read(buf);
			buf.flip();
			fis.close();
			return buf.array();
		} catch (Exception e) {
			System.out.println("FILE : " + s.getName() + " missing.");
			return null;
		}
	}

	public static int getTimeLeft(long start, int timeAmount, TimeUnit timeUnit) {
		start -= timeUnit.toMillis(timeAmount);
		long elapsed = System.currentTimeMillis() - start;
		int toReturn = timeUnit == TimeUnit.SECONDS ? (int) ((elapsed / 1000) % 60) - timeAmount
				: (int) ((elapsed / 1000) / 60) - timeAmount;
		if (toReturn <= 0)
			toReturn = 1;
		return timeAmount - toReturn;
	}

	/**
	 * Converts an array of bytes to an integer.
	 * 
	 * @param data
	 *            the array of bytes.
	 * @return the newly constructed integer.
	 */
	public static int hexToInt(byte[] data) {
		int value = 0;
		int n = 1000;
		for (int i = 0; i < data.length; i++) {
			int num = (data[i] & 0xFF) * n;
			value += num;
			if (n > 1) {
				n = n / 1000;
			}
		}
		return value;
	}

	public static Position delta(Position a, Position b) {
		return new Position(b.getX() - a.getX(), b.getY() - a.getY());
	}

	/**
	 * Picks a random element out of any array type.
	 * 
	 * @param array
	 *            the array to pick the element from.
	 * @return the element chosen.
	 */
	public static <T> T randomElement(T[] array) {
		return array[(int) (RANDOM.nextDouble() * array.length)];
	}

	/**
	 * Picks a random element out of any list type.
	 * 
	 * @param list
	 *            the list to pick the element from.
	 * @return the element chosen.
	 */
	public static <T> T randomElement(List<T> list) {
		return list.get((int) (RANDOM.nextDouble() * list.size()));
	}

	/**
	 * Reads string from a data input stream.
	 * 
	 * @return The String value.
	 */
	public static String readString(ChannelBuffer buffer) {
		StringBuilder builder = null;
		try {
			byte data;
			builder = new StringBuilder();
			while ((data = buffer.readByte()) != 10) {
				builder.append((char) data);
			}
		} catch (IndexOutOfBoundsException e) {

		}
		return builder.toString();
	}

	private static final String[] BLOCKED_WORDS = new String[] { "devious", "firepk", "ventrilica", "ventrili", "hade5",
			"hades5", "pvplegacy", "junglepk", "runeinsanit", "n3t", "c  0 m", "c 0 m", "C 0  m", "c  o  m",
			"runeinsanity", "runeinsanit", "runeinsan", "rune-insanity", "rune-insanit", ".c0", ".c0m", "legendsdomain",
			"createaforum", "vampirez", "-scape", "rswebclients", "pvplegacy", "junglepk", ".n3t", ".c0m", "c0m", "n3t",
			".tk", ",net", ",org", ",com", ",be", ",nl", ",info", "dspk", ".info", ".org", ".tk", ".net", ".com",
			".co.uk", ".be", ".nl", ".dk", ".co.cz", ".co", ".us", ".biz", ".eu", ".de", ".cc", ". n e  t", ".i n f o",
			".o r g", ".t k", ".n e t", ".c o m", ".c o . u k", ".b e", ".n l", ".d k", ".c o . c z", ".c o", ".u s",
			".b i z", ".e u", ".d e", ".c c", ".(c)om", "(.)", "kandarin", "o r g", "www", "w w w", "w.w.w", "voidrsps",
			"void-ps", "desolace", "soulsplit", "soulspawn", "atiloc", "<img", "@cr", "<img=", "@cr", ":tradereq:",
			":duelreq:", "<col=", "<shad=", "hostilityps", "hostilityp", "hostility ps" };

	public static boolean blockedWord(String string) {
		for (String s : BLOCKED_WORDS) {
			if (string.contains(s)) {
				return true;
			}
		}
		return false;
	}

	public static String findCommand(String message) {
		if (!message.contains(" ") && !message.contains("-")) {
			return message;
		} else if (!message.contains(" ")) {
			return message.substring(0, message.indexOf("-"));
		} else if (!message.contains("-")) {
			return message.substring(0, message.indexOf(" "));
		}
		int seperatorIndex = message.indexOf(" ") < message.indexOf("-") ? message.indexOf(" ") : message.indexOf("-");
		return message.substring(0, seperatorIndex);
	}

	public static String findInput(String message) {
		if (!message.contains(" ") && !message.contains("-")) {
			return "";
		} else if (!message.contains(" ")) {
			return message.substring(message.indexOf("-") + 1);
		} else if (!message.contains("-")) {
			return message.substring(message.indexOf(" ") + 1);
		}
		int seperatorIndex = message.indexOf(" ") < message.indexOf("-") ? message.indexOf(" ") : message.indexOf("-");
		return message.substring(seperatorIndex + 1);
	}

	public static int secondsToTicks(int i) {
		return((int) (i / .6));
	}

	public static int ticksToSeconds(int i) { return ((int) (i * .6)); }
}
