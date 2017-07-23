package com.zarador.world.entity.impl.player.bot.util;


import java.util.Random;

/*
 * 
 * auth @adam.trinity1
 */

public enum NameGenerator 
{

	SEGS("Anto", "a", "b", "c", "deshawn", "nba","perk","backwood","deshawn",
			"adam", "adamisop", "adamprodev", "adamthebest", "Argos" ,
			"gang ", "rlgang", "acc", "ass", "tits", "dicklover", "kanker",
			"kankerop", "moer", "bangbang", "lijpe", "eng", "englijpe",
			"darryl", "is", "a", "dumb", "cutie", "dog", "dino", "drac",
			"darth", "blitz", "bong", "nba", "eggy", "el", "en", "end", "elf",
			"enemy", "fun", "fang", "freeze", "fs", "flop", "fold", "foil", "full",
			"gram", "gonk", "gato", "gloop", "genocide", "happy", "hell", "hippo", "hands",
			"helecopter", "hector", "henry", "hiya", "happy", "hello", "hacker", "hold",
			"hope", "honda", "iglue", "ignite", "ignition", "isafe", "i safe", "i kill", "i live",
			"i veng", "i taught", "i die", "i good", "james", "jelous", "jigsaw", "jan", "jose",
			"jen", "krocodile", "klue", "klux", "klan", "karot", "krap", "kanye", "kyle", "kenny",
			"k", "kapped", "kall", "zante", "zoyle", "zogre", "zee", "z"),
	
	START_ATR("x ", "xx ", " o", ""),
	END_ATR(" x", " xx", " o", "y", "");
	
	private String[] names;
	
	public String[] getNames() {
		return names;
	}

	public void setNames(String[] names) {
		this.names = names;
	}

	private NameGenerator(String...names)
	{
		this.names = names;
	}
	
	private static Random rnd = new Random();
	
	public static String generateRandomName()
	{
		String startSeg = 
				rnd.nextInt(10) == 0 
				? START_ATR.getNames()[rnd.nextInt(START_ATR.getNames().length)] : "";
				
		String endSeg = 
				rnd.nextInt(10) == 0 
				? END_ATR.getNames()[rnd.nextInt(END_ATR.getNames().length)] : "";
				
		String split = rnd.nextInt(4) == 0 ? " " : "";
		
		String randomFirst = SEGS.getNames()[rnd.nextInt(SEGS.getNames().length)];
		String randomSecond = SEGS.getNames()[rnd.nextInt(SEGS.getNames().length)];
		
		String finalString = startSeg + randomFirst + split + randomSecond + endSeg;
		if(finalString.length() > 12)
			return finalString.substring(0, 12);
		else return finalString;
	}
}
