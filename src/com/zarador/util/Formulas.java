package com.zarador.util;

import java.util.Random;

/**
 * Generates a random formula.
 *
 * @Author Jonny
 */
public class Formulas {

	//100
	//.25

	/**
	 * Generates a random formula and spits out the answer.
	 */
	public static void generate() {
		int total = 0;
		for (int i = 0; i < 100000; i++) {
			Random rand = new Random();
			int chance = Misc.inclusiveRandom(1, 4);
			int very_rare = Misc.inclusiveRandom(1, 100);
			if (chance == 3) {
				if (very_rare == 100) {
					total++;
				}
			}
		}
		System.out.println("Total generated in random amount: "+total);
	}
}
