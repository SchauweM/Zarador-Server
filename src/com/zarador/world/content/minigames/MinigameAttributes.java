package com.zarador.world.content.minigames;

/**
 * Holds different minigame attributes for a player
 * 
 * @author Gabriel Hannason
 */
public class MinigameAttributes {

	private final BarrowsMinigameAttributes barrowsMinigameAttributes = new BarrowsMinigameAttributes();
	private final WarriorsGuildAttributes warriorsGuildAttributes = new WarriorsGuildAttributes();
	private final PestControlAttributes pestControlAttributes = new PestControlAttributes();
	private final RecipeForDisasterAttributes rfdAttributes = new RecipeForDisasterAttributes();
	private final CurseQuestAttributes cqAttributes = new CurseQuestAttributes();
	private final NomadAttributes nomadAttributes = new NomadAttributes();
	private final ClawQuestAttributes clawQuestAttributes = new ClawQuestAttributes();
	private final Shrek1Attributes shrek1Attributes = new Shrek1Attributes();
	private final FarmQuestAttributes farmQuestAttributes = new FarmQuestAttributes();
	// private final SoulWarsAttributes soulWarsAttributes = new
	// SoulWarsAttributes();
	private final GodwarsDungeonAttributes godwarsDungeonAttributes = new GodwarsDungeonAttributes();
	private final GraveyardAttributes graveyardAttributes = new GraveyardAttributes();

	public class GraveyardAttributes {

		private int wave;
		private int requiredKills;
		private int level;
		private boolean entered;

		public int getWave() {
			return wave;
		}

		public int getLevel() {
			return level;
		}

		public GraveyardAttributes setWave(int wave) {
			this.wave = wave;
			return this;
		}

		public int incrementAndGetWave() {
			return this.wave++;
		}

		public void setLevel(int level) {
			this.level = level;
		}

		public void incrementLevel() {
			this.level++;
		}

		public int getRequiredKills() {
			return requiredKills;
		}

		public int decrementAndGetRequiredKills() {
			return this.requiredKills--;
		}

		public void setRequiredKills(int requiredKills) {
			this.requiredKills = requiredKills;
		}

		public boolean hasEntered() {
			return entered;
		}

		public GraveyardAttributes setEntered(boolean entered) {
			this.entered = entered;
			return this;
		}
	}

	public class PestControlAttributes {

		public PestControlAttributes() {

		}

		private int damageDealt;

		public int getDamageDealt() {
			return damageDealt;
		}

		public void setDamageDealt(int damageDealt) {
			this.damageDealt = damageDealt;
		}

		public void incrementDamageDealt(int damageDealt) {
			this.damageDealt += damageDealt;
		}
	}

	public class WarriorsGuildAttributes {

		private boolean hasSpawnedArmour;
		private boolean enteredTokenRoom;

		public boolean hasSpawnedArmour() {
			return hasSpawnedArmour;
		}

		public void setSpawnedArmour(boolean hasSpawnedArmour) {
			this.hasSpawnedArmour = hasSpawnedArmour;
		}

		public boolean enteredTokenRoom() {
			return enteredTokenRoom;
		}

		public void setEnteredTokenRoom(boolean enteredTokenRoom) {
			this.enteredTokenRoom = enteredTokenRoom;
		}

	}

	public class BarrowsMinigameAttributes {

		private int killcount, randomCoffin, riddleAnswer = -1;

		public int getKillcount() {
			return killcount;
		}

		public void setKillcount(int killcount) {
			this.killcount = killcount;
		}

		public int getRandomCoffin() {
			return randomCoffin;
		}

		public void setRandomCoffin(int randomCoffin) {
			this.randomCoffin = randomCoffin;
		}

		public int getRiddleAnswer() {
			return riddleAnswer;
		}

		public void setRiddleAnswer(int riddleAnswer) {
			this.riddleAnswer = riddleAnswer;
		}

		private int[][] barrowsData = { // NPCID, state
				{ 2030, 0 }, // verac
				{ 2029, 0 }, // toarg
				{ 2028, 0 }, // karil
				{ 2027, 0 }, // guthan
				{ 2026, 0 }, // dharok
				{ 2025, 0 } // ahrim
		};

		public int[][] getBarrowsData() {
			return barrowsData;
		}

		public void setBarrowsData(int[][] barrowsData) {
			this.barrowsData = barrowsData;
		}

		public void setBarrowsData(int index1, int index2, int barrowsData) {
			this.barrowsData[index1][index2] = barrowsData;
		}
	}

	public class CurseQuestAttributes {
		private int wavesCompleted;

		public int getWavesCompleted() {
			return wavesCompleted;
		}

		public void setWavesCompleted(int wavesCompleted) {
			this.wavesCompleted = wavesCompleted;
		}
	}

	public class RecipeForDisasterAttributes {
		private int wavesCompleted;
		private boolean[] questParts = new boolean[9];

		public int getWavesCompleted() {
			return wavesCompleted;
		}

		public void setWavesCompleted(int wavesCompleted) {
			this.wavesCompleted = wavesCompleted;
		}

		public boolean hasFinishedPart(int index) {
			return questParts[index];
		}

		public void setPartFinished(int index, boolean finished) {
			questParts[index] = finished;
		}

		public boolean[] getQuestParts() {
			return questParts;
		}

		public void setQuestParts(boolean[] questParts) {
			this.questParts = questParts;
		}

		public void reset() {
			questParts = new boolean[9];
			wavesCompleted = 0;
		}
	}

	public class NomadAttributes {
		private boolean[] questParts = new boolean[2];

		public boolean hasFinishedPart(int index) {
			return questParts[index];
		}

		public void setPartFinished(int index, boolean finished) {
			questParts[index] = finished;
		}

		public boolean[] getQuestParts() {
			return questParts;
		}

		public void setQuestParts(boolean[] questParts) {
			this.questParts = questParts;
		}

		public void reset() {
			questParts = new boolean[2];
		}
	}

	public class ClawQuestAttributes {
		private int questParts = 0;
		private int samples = 0;
		public final int SAMPLES_NEEDED = 50;

		public int getSamples() {
			return samples;
		}

		public void setSamples(int samples) {
			this.samples = samples;
		}

		public void addSamples(int samples) {
			this.samples += samples;
		}

		public void minusSamples(int samples) {
			this.samples -= samples;
		}

		public int hasFinishedPart(int index) {
			return questParts;
		}

		public void setPartFinished(int index, int finished) {
			questParts = finished;
		}

		public int getQuestParts() {
			return questParts;
		}

		public void setQuestParts(int questParts) {
			this.questParts = questParts;
		}

	}

	public class Shrek1Attributes {
		private int questParts = 0;

		private int doorKicks = 0;

		public int getDoorKicks() {
			return doorKicks;
		}

		public void setDoorKicks(int doorKicks) {
			this.doorKicks = doorKicks;
		}

		public int getQuestParts() {
			return questParts;
		}

		public void setQuestParts(int questParts) {
			this.questParts = questParts;
		}

	}

	public class FarmQuestAttributes {
		private int questParts = 0;
		private int produce = 0;

		public int getQuestParts() {
			return questParts;
		}

		public void setQuestParts(int questParts) {
			this.questParts = questParts;
		}

		public void addProduce(int produce) {
			this.produce += produce;
		}

		public int getProduce() {
			return produce;
		}
	}

	public class GodwarsDungeonAttributes {
		private int[] killcount = new int[4]; // 0 = armadyl, 1 = bandos, 2 =
												// saradomin, 3 = zamorak
		private boolean enteredRoom;
		private long altarDelay;

		public int[] getKillcount() {
			return killcount;
		}

		public void setKillcount(int[] killcount) {
			this.killcount = killcount;
		}

		public void setKillcount(int killcount, int index) {
			this.killcount[index] = killcount;
		}

		public boolean hasEnteredRoom() {
			return enteredRoom;
		}

		public void setHasEnteredRoom(boolean enteredRoom) {
			this.enteredRoom = enteredRoom;
		}

		public long getAltarDelay() {
			return altarDelay;
		}

		public GodwarsDungeonAttributes setAltarDelay(long altarDelay) {
			this.altarDelay = altarDelay;
			return this;
		}
	}

	public BarrowsMinigameAttributes getBarrowsMinigameAttributes() {
		return barrowsMinigameAttributes;
	}

	public WarriorsGuildAttributes getWarriorsGuildAttributes() {
		return warriorsGuildAttributes;
	}

	public PestControlAttributes getPestControlAttributes() {
		return pestControlAttributes;
	}

	public RecipeForDisasterAttributes getRecipeForDisasterAttributes() {
		return rfdAttributes;
	}

	public FarmQuestAttributes getFarmQuestAttributes() {
		return farmQuestAttributes;
	}

	public ClawQuestAttributes getClawQuestAttributes() {
		return clawQuestAttributes;
	}

	public Shrek1Attributes getShrek1Attributes() {
		return shrek1Attributes;
	}

	public CurseQuestAttributes getCurseQuestAttributes() {
		return cqAttributes;
	}

	public NomadAttributes getNomadAttributes() {
		return nomadAttributes;
	}

	public GraveyardAttributes getGraveyardAttributes() {
		return graveyardAttributes;
	}

	public GodwarsDungeonAttributes getGodwarsDungeonAttributes() {
		return godwarsDungeonAttributes;
	}

	public MinigameAttributes() {
	}

}
