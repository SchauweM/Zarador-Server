package com.zarador.world.entity.impl.player.bot.pker;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum PkerBotSets 
{

	BANDOM_PKER(PkerBotTypes.MASTER, 
			new int[] {10828, 3751, 11335},
			new int[] {10551, 11724},
			new int[] {11726},
			new int[] {11732, 11728},
			new int[] {7462},
			new int[] {4151, 4587},
			new int[] {11283, 8850, 4224},
			new int[] {6585},
			new int[] {6570},
			new int[] {11694}),
	
	DHAROCKS_PKER(PkerBotTypes.MASTER, 
			new int[] {4716},
			new int[] {4720},
			new int[] {4722},
			new int[] {11732, 11728},
			new int[] {7462},
			new int[] {4151, 4587},
			new int[] {11283, 8850, 4224},
			new int[] {6585},
			new int[] {6570},
			new int[] {4718}),
	
	PURE_PKER(PkerBotTypes.PURE,
			new int[] {
					7534, 1053, 1055, 1057, 1038,
					1040, 1042, 1044, 1046, 1048,
					1050, 9752, 7003, 1153
					},
			new int[] {1035},
			new int[] {1033},
			new int[] {3105},
			new int[] {7458},
			new int[] {4151, 4587},
			new int[] {3842, 3840, 3844, 6889, 1540},
			new int[] {6585, 1704, 1725, 1731},
			new int[] {7535, 6570, 9751, 9757, 9763, 6568},
			new int[] {11694, 1215}),
	
	PURE_PKER_2(PkerBotTypes.PURE,
			new int[] {
					7534, 1053, 1055, 1057, 1038,
					1040, 1042, 1044, 1046, 1048,
					1050, 9752, 7003, 1153
					},
			new int[] {1115},
			new int[] {1067, 2497},
			new int[] {3105},
			new int[] {7458, 2491},
			new int[] {4151, 4587},
			new int[] {3842, 3840, 3844, 6889, 1540},
			new int[] {6585, 1704, 1725, 1731},
			new int[] {7535, 6570, 9751, 9757, 9763, 6568},
			new int[] {11694, 1215}),
	
	PURE_PKER_3(PkerBotTypes.PURE,
			new int[] {
					7534, 1053, 1055, 1057, 1038,
					1040, 1042, 1044, 1046, 1048,
					1050, 9752, 7003, 1153
					},
			new int[] {6107},
			new int[] {1067},
			new int[] {3105},
			new int[] {7458, 2491},
			new int[] {4151, 4587},
			new int[] {3842, 3840, 3844, 6889, 1540},
			new int[] {6585, 1704, 1725, 1731},
			new int[] {7535, 6570, 9751, 9757, 9763, 6568},
			new int[] {11694, 1215}),
	
	RUNE_PKER(PkerBotTypes.MASTER,
			new int[] {10828, 1149, 3751, 3753, 3755, 1163},
			new int[] {1127, 2615, 2623, 2653, 2661, 2669},
			new int[] {1079, 2617, 2625, 2655, 2663, 2671},
			new int[] {4131, 3105, 11732, 11728},
			new int[] {2922, 2902, 2912, 2932, 2942, 7459, 7460, 7461, 7462},
			new int[] {4587, 4151, 11730},
			new int[] {1201, 2621, 2629, 2659, 2667, 2675, 4224, 1187, 8850, 3840, 3842, 3844},
			new int[] {1725, 1731, 1704, 6585},
			new int[] {1007, 1052, 2412, 2413, 2414, 6568, 6570},
			new int[] {1215, 11694, 10887, 11696, 11698, 11700}),
	
	RUNE_PKER_3(PkerBotTypes.MASTER,
			new int[] {10828, 1149, 3751, 3753, 3755, 1163, 3486},
			new int[] {3481},
			new int[] {3483},
			new int[] {4131, 3105, 11732, 11728},
			new int[] {2922, 2902, 2912, 2932, 2942, 7459, 7460, 7461, 7462},
			new int[] {4587, 4151, 11730},
			new int[] {1201, 2621, 2629, 2659, 2667, 2675, 4224, 1187, 8850, 3840, 3842, 3488},
			new int[] {1725, 1731, 1704, 6585},
			new int[] {1007, 1052, 2412, 2413, 2414, 6568, 6570},
			new int[] {1215, 11694, 10887, 11696, 11698, 11700}),
	
	RUNE_PKER_2(PkerBotTypes.MASTER,
			new int[] {10828, 1149, 3751, 3753, 3755, 1163, 5574},
			new int[] {5575},
			new int[] {5576},
			new int[] {4131, 3105, 11732, 11728},
			new int[] {2922, 2902, 2912, 2932, 2942, 7459, 7460, 7461, 7462},
			new int[] {4587, 4151, 11730},
			new int[] {1201, 2621, 2629, 2659, 2667, 2675, 4224, 1187, 8850, 3840, 3842},
			new int[] {1725, 1731, 1704, 6585},
			new int[] {1007, 1052, 2412, 2413, 2414, 6568, 6570},
			new int[] {1215, 11694, 10887, 11696, 11698, 11700});
	
	
	private PkerBotTypes identifier;
	
	private static Random random = new Random();
	
	public static PkerBotSets getRandomSet(PkerBotTypes type)
	{
		List<PkerBotSets> list = new ArrayList<>();
		
		for(PkerBotSets pkerSet : values())
		{
			if(pkerSet.identifier == type)
			{
				list.add(pkerSet);
			}
		}
		
		return list.get(random.nextInt(list.size()));
	}
	
	public static int getRandomItem(int[] data)
	{
		int index = random.nextInt(data.length);
		return data[index];
	}
	
	
	private int[] 
			head,
			body,
			legs,
			feet,
			hands,
			weapon,
			shield,
			necklace,
			cape,
			specWep;
	
	public int[] getSpecWep() {
		return specWep;
	}

	public void setSpecWep(int[] specWep) {
		this.specWep = specWep;
	}

	public int[] getBody() {
		return body;
	}

	public void setBody(int[] body) {
		this.body = body;
	}

	public int[] getLegs() {
		return legs;
	}

	public void setLegs(int[] legs) {
		this.legs = legs;
	}

	public int[] getFeet() {
		return feet;
	}

	public void setFeet(int[] feet) {
		this.feet = feet;
	}

	public int[] getHands() {
		return hands;
	}

	public void setHands(int[] hands) {
		this.hands = hands;
	}

	public int[] getWeapon() {
		return weapon;
	}

	public void setWeapon(int[] weapon) {
		this.weapon = weapon;
	}

	public int[] getShield() {
		return shield;
	}

	public void setShield(int[] shield) {
		this.shield = shield;
	}

	public int[] getNecklace() {
		return necklace;
	}

	public void setNecklace(int[] necklace) {
		this.necklace = necklace;
	}

	public int[] getCape() {
		return cape;
	}

	public void setCape(int[] cape) {
		this.cape = cape;
	}

	private PkerBotSets(PkerBotTypes identifier,
			int[] head, int[] body,
			int[] legs, int[] feet,
			int[] hands, int[] weapon,
			int[] shield, int[] necklace,
			int[] cape, int[] specWep)
	{
		this.setIdentifier(identifier);
		this.setHead(head);
		this.setBody(body);
		this.setLegs(legs);
		this.setFeet(feet);
		this.setHands(hands);
		this.setWeapon(weapon);
		this.setShield(shield);
		this.setNecklace(necklace);
		this.setCape(cape);
		this.setSpecWep(specWep);
		
	}

	public PkerBotTypes getIdentifier() {
		return identifier;
	}

	public void setIdentifier(PkerBotTypes identifier) {
		this.identifier = identifier;
	}

	public int[] getHead() {
		return head;
	}

	public void setHead(int[] head) {
		this.head = head;
	}
	
	
	
	
}
