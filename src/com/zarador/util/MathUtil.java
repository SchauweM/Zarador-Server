package com.zarador.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * This file manages all math equations that isn't already handled in java's
 * lang package.
 * 
 * @author relex lawl
 */
public final class MathUtil {

    public static String secondsToHours(int totalSecs) {
        int hours = totalSecs / 3600;
        int minutes = (totalSecs % 3600) / 60;
        int seconds = totalSecs % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static String secondsToMinutes(int totalSecs) {
        int minutes = (totalSecs % 3600) / 60;
        int seconds = totalSecs % 60;

        return String.format("%02d.%02d", minutes, seconds);
    }

    public static String secondsToHours(int totalSecs, boolean seconds) {
        if (seconds)
            return secondsToHours(totalSecs);
        int hours = totalSecs / 3600;
        int minutes = (totalSecs % 3600) / 60;

        return String.format("%02d:%02d", hours, minutes);
    }

    public static String secondsToMinutes(int totalSecs, boolean seconds) {
        if (seconds)
            return secondsToMinutes(totalSecs);
        int minutes = (totalSecs % 3600) / 60;

        return String.format("%02d", minutes);
    }

    /**
     * Gets a random number within the {@code range}.
     * 
     * @param range
     *            The max number possible.
     * @return A random number within the {@code range}.
     */
    public static float random(float range) {
        if (range < 0)
            range = 0;
        return ThreadLocalRandom.current().nextFloat() * range;
    }

    /**
     * Gets a random number within the {@code range}.
     * 
     * @param range
     *            The max number possible.
     * @return A random number within the {@code range}.
     */
    public static int random(int range) {
        if (range < 0)
            range = 0;
        return ThreadLocalRandom.current().nextInt(range + 1);
    }

    /**
     * Gets a random number that is higher than {@code startingRange} and less
     * than {@code endRange}.
     * 
     * @param startingRange
     *            The lowest-possible generated random number.
     * @param endRange
     *            The highest-possible generated random number.
     * @return A value between the two specified integers.
     */
    public static int random(int startingRange, int endRange) {
        return startingRange + random(endRange);
    }

    /**
     * Checks if a number is even (can be divided by 2).
     * 
     * @param number
     *            The number to check.
     * @return number % 2 != 0.
     */
    public static boolean even(int number) {
        return (number % 2 == 0);
    }

    /**
     * Checks if a number is odd (cannot be divided by 2).
     * 
     * @param number
     *            The number to check.
     * @return !even({@code number}).
     */
    public static boolean odd(int number) {
        return !even(number);
    }

    /**
     * 
     * @param generalTicks
     * @return
     */
    public static String getTimeForTicks(int ticks) {
        if (ticks < 60) {
            return ticks + " seconds";
        } else {
            final int minutes = ticks / 60;
            if (minutes > 0)
                ticks -= minutes * 60;
            return minutes + " minutes " + ticks + " seconds";
        }
    }

    /**
     * Gets symbol for money
     * 
     * @param quantity
     *            the amount
     * @return the symbol
     */
    public static String currency(final int quantity) {
        if (quantity >= 10000 && quantity < 10000000) {
            return quantity / 1000 + "K";
        } else if (quantity >= 10000000 && quantity <= 2147483647) {
            return quantity / 1000000 + "M";
        } else {
            return "" + quantity + " gp";
        }
    }
}
