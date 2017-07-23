package com.zarador.util;

import java.util.Random;

public final class RandomGenerator {
	private static final Random RANDOM = new Random();

	public static int random(int n) {
        return RandomGenerator.RANDOM.nextInt(n + 1);
	}

	public static int nextInt() {
        return RandomGenerator.RANDOM.nextInt();
	}

	public static int nextInt(int n) {
        try {
            if(n < 0) {
                throw new Exception("Negative random generator call");
            } else {
                return RandomGenerator.RANDOM.nextInt(n);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return RandomGenerator.RANDOM.nextInt(1);
        }
	}

	public static long nextLong() {
		return RandomGenerator.RANDOM.nextLong();
	}

	public static boolean nextBoolean() {
		return RandomGenerator.RANDOM.nextBoolean();
	}

	public static float nextFloat() {
		return RandomGenerator.RANDOM.nextFloat();
	}

	public static double nextDouble() {
		return RandomGenerator.RANDOM.nextDouble();
	}

	public static boolean hunter(int req, int level) {
		req += 20;
		level += 11;
		req = req * (req - (req / 8));
		level = level * level + 4;
		return RandomGenerator.random(req) <= level;
	}

	public static boolean butterflyHunter(int req, int level, boolean hasNet) {
		req *= hasNet ? 1.75F : 2.0F;
		level += 10;
		req = req * (req - (req / 8));
		level = level * level;
		return RandomGenerator.random(req) < level;
	}
}
