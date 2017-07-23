package com.zarador.world.content.skill.impl.summoning;

import org.scripts.kotlin.content.dialog.pets.*;

import com.zarador.model.player.dialog.Dialog;
import com.zarador.util.Misc;
import com.zarador.world.entity.impl.npc.NPC;
import com.zarador.world.entity.impl.player.Player;

public class BossPets {

	/**
	 *  Boss Pets
	 *	Effects are given to the player only when used with
	 *	@return
	 */

	public enum BossPet {

		/**
		 *  Chaos Elemental Jr.
		 *	Don't get teleported away in combat
		 *	@Chaos Elemental
		 */
		PET_CHAOS_ELEMENTAL(2054, 5907, 11995),

		/**
		 *  Prince Black Dragon
		 *	Never get damaged by fire breath.
		 *	@All Dragons
		 */
		PRINCE_BLACK_DRAGON(50, 6652, 12653),

		/**
		 *  TzRek-Jad
		 *	Trade a fire cape or tokhaar kal to a player.
		 *	@All Players
		 */
		TZREK_JAD(2745, 5892, 13225),

		/**
		 *  Dark Core
		 *	Receive a 25% defensive boost against Corporeal Beast.
		 *	@Corporeal Beast
		 */
		PET_DARK_CORE(8133, 388, 12816),

		/**
		 *  General Graardor Jr.
		 *	Require no kill count to enter Bandos.
		 *	@Godwars
		 */
		PET_GENERAL_GRAARDOR(3031, 6644, 12650),

		/**
		 *  Kree'arra Jr.
		 *	Require no kill count to enter Armadyl.
		 *	@Godwars
		 */
		PET_KREE_ARRA(6222, 6643, 12649),

		/**
		 *  K'ril Tsutsaroth Jr.
		 *	Require no kill count to enter Zamorak.
		 *	@Godwars
		 */
		PET_KRIL_TSUTSAROTH(6203, 6647, 12652),

		/**
		 *  Zilyana Jr.
		 *	Require no kill count to enter Saradomin.
		 *	@Godwars
		 */
		PET_ZILYANA(6247, 6646, 12651),

		/**
		 *  Dagannoth Supreme Jr.
		 *	Gain a 10% overall damage boost.
		 *	@Dagannoth Supreme
		 */
		PET_DAGANNOTH_SUPREME(2881, 6626, 12643),

		/**
		 *  Dagannoth Prime Jr.
		 *	Gain a 10% overall damage boost.
		 *	@Dagannoth Prime
		 */
		PET_DAGANNOTH_PRIME(2882, 6627, 12644),

		/**
		 *  Dagannoth Rex Jr.
		 *	Gain a 10% overall damage boost.
		 *	@Dagannoth Rex
		 */
		PET_DAGANNOTH_REX(2883, 6641, 12645),

		/**
		 *  Kalphite Princess
		 *	Hit through combat deflection prayers.
		 *	@Kalphite Queen
		 */
		KALPHITE_PRINCESS(6653, 6653, 12654),

		/**
		 *  Hellpuppy
		 *	Gain a 10% increased drop rate.
		 *	@Cerberus
		 */
		HELLPUPPY(1158, 964, 13247),

		/**
		 *  Venenatis spiderling
		 *	Have unlimited prayer points
		 *	@Venenatis
		 */
		VENENATIS_SPIDERLING(2000, 495, 13177),

		/**
		 *  Callisto cub
		 *	Block 25% of all damage.
		 *	@Callisto
		 */
		CALLISTO_CUB(6609, 497, 13178),

		/**
		 *  Kraken
		 *	Find your opponent on the first whirlpool.
		 *	@Kraken
		 */
		//TODO: Kraken isn't added yet
		PET_KRAKEN(6656, 6656, 12655),

		/**
		 *  Scorpia's offspring
		 *	Protect yourself from venom & poison.
		 *	@Scorpia
		 */
		SCORPIAS_OFFSPRING(2001, 5547, 13181),

		/**
		 *  Vet'ion Jr.
		 *	Protect yourself from venom & poison.
		 *	@Scorpia
		 */
		//TODO: Vetion isn't added yet
		PET_VETION(5536, 5536, 13179),

		/**
		 *  Smoke Devil
		 *	-Undecided-
		 *	@return null
		 */
		SMOKE_DEVIL(6655, 6655, 12648),

		/**
		 *  Beaver
		 *	Gain double the logs from woodcutting.
		 *	Stackable with all other benefits.
		 *	@Woodcutting
		 */
		BEAVER(-1, 6717, 13322),

		/**
		 *  Heron
		 *	Catch double hunter entities.
		 *	Stackable with all other benefits.
		 *	@Hunter
		 */
		HERON(6715, 6715, 13320),

		/**
		 *  Rock Golem
		 *	Mine 2 ores at a time.
		 *	Stackable with all other benefits.
		 *	@Mining
		 */
		ROCK_GOLEM(6716, 6716, 13321),

		/**
		 * Abyssal Orphan
		 * -Undecided-
		 * @return AbyssalSire
		*/
		 ABYSSAL_ORPHAN(5886, 5883, 13262),

		/**
		 *  Snakeling
		 *	-Undecided-
		 *	@return null
		 */
		//TODO: Zulrah isn't added yet
		ZULRAH_RANGE(2127, 2127, 12921),
		ZULRAH_MAGE(2129, 2129, 12940),
		ZULRAH_MELEE(2128, 2128, 12939);

		BossPet(int npcId, int spawnNpcId, int itemId) {
			this.npcId = npcId;
			this.spawnNpcId = spawnNpcId;
			this.itemId = itemId;
		}

		public int npcId, spawnNpcId, itemId;

		/**
		 * Grabs the npc id of the pet.
		 * @return
		 */
		public int getSpawnNpcId() {
			return this.spawnNpcId;
		}

		/**
		 * Grabs the npc id of the boss for the pet.
		 * @return
		 */
		public int getBossId() {
			return this.npcId;
		}

		public static BossPet forId(int itemId) {
			for (BossPet p : BossPet.values()) {
				if (p != null && p.itemId == itemId) {
					return p;
				}
			}
			return null;
		}

		public static BossPet forSpawnId(int spawnNpcId) {
			for (BossPet p : BossPet.values()) {
				if (p != null && p.spawnNpcId == spawnNpcId) {
					return p;
				}
			}
			return null;
		}
	}

	public static boolean pickup(Player player, NPC npc) {
		BossPet pet = BossPet.forSpawnId(npc.getId());
		if (player.getInventory().getFreeSlots() >= 1) {
			if (pet != null) {
				if (player.getSummoning().getFamiliar() != null
						&& player.getSummoning().getFamiliar().getSummonNpc() != null
						&& player.getSummoning().getFamiliar().getSummonNpc().isRegistered()) {
					if (player.getSummoning().getFamiliar().getSummonNpc().getId() == pet.spawnNpcId
							&& player.getSummoning().getFamiliar().getSummonNpc().getIndex() == npc.getIndex()) {
						player.getSummoning().unsummon(true, true);
						player.getPacketSender().sendMessage("You pick up your pet.");
						return true;
					} else {
						player.getPacketSender().sendMessage("This is not your pet to pick up.");
					}
				} else {
					player.getPacketSender().sendMessage("This is not your pet to pick up.");
				}
			}
			return false;
		} else {
			player.getPacketSender().sendMessage("You need atleast 1 free inventory space to pickup your pet.");
			return false;
		}
	}

	public static boolean talkTo(Player player, NPC npc) {
		Dialog dialog = getDialog(player, npc.getId());
		if(dialog != null) {
			player.setEntityInteraction(npc);
			player.setNpcClickId(npc.getId());
			player.getDialog().sendDialog(getDialog(player, npc.getId()));
			return true;
		}
		return false;
	}

	public static Dialog getDialog(Player player, int npcId) {
		switch(npcId) {
			case 5907:
				return new ChaosElementalJr(player);
			case 6652:
				return new PrinceBlackDragon(player);
			case 6644:
				return new GeneralGraardorJr(player);
			case 5892:
				int chance = Misc.random(3);
				if(chance >= 2) {
					return new TzRekJad1(player);
				} else {
					return new TzRekJad2(player);
				}
			case 388:
				return new DarkCore(player);
			case 6643:
				return new KreearraJr(player);
			case 6647:
				return new KrilTsutsarothJr(player);
			case 6646:
				return new ZilyanaJr(player);
			case 6626:
				return new DagannothSupremeJr(player);
			case 6627:
				return new DagannothPrimeJr(player);
			case 6641:
				return new DagannothRexJr(player);
			case 6653:
				return new KalphitePrincess(player);
			case 964:
				int chance2 = Misc.random(4);
				switch(chance2) {
					case 1:
						return new Hellpuppy2(player);
					case 2:
						return new Hellpuppy3(player);
					case 3:
						return new Hellpuppy4(player);
					case 4:
						return new Hellpuppy5(player);
					default:
						return new Hellpuppy1(player);
				}
			case 495:
				return new VenenatisSpiderling(player);
			case 497:
				return new CallistoCub(player);
			case 6656:
				return new Kraken(player);
			case 5547:
				return new ScorpiasOffspring(player);
			case 5536:
				return new VetionJr(player);
			case 6655:
				return new SmokeDevil(player);
			case 6717:
				return new Beaver(player);
			case 6715:
				return new Heron(player);
			case 6716:
				return new RockGolem(player);
			case 5883:
				int orphanChance = Misc.inclusiveRandom(1, 2);
				switch(orphanChance) {
					case 1:
						return new AbyssalOrphan(player);
					case 2:
						return new AbyssalOrphan2(player);
					}
				return new AbyssalOrphan(player);
			case 2127:
			case 2129:
			case 2128:
				return new Snakeling(player);
		}
		return null;
	}

	public static boolean hasPet(Player player, BossPet pet) {
		if(player.getSummoning().getFamiliar() != null) {
			if (player.getSummoning().getFamiliar().getSummonNpc() != null) {
				if (pet.getSpawnNpcId() == player.getSummoning().getFamiliar().getSummonNpc().getId()) {
					return true;
				}
			}
		}
		return false;
	}
}