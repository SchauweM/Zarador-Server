package com.zarador;

import java.math.BigInteger;
import java.util.ArrayList;

import com.zarador.model.Position;
import com.zarador.net.security.ConnectionHandler;
import com.zarador.world.entity.impl.npc.NPC;

public class GameSettings {

	public static boolean STARTED_SERVER = false;

	/**
	 * The game port
	 */
	public static final int GAME_PORT = 43594;
	public static final boolean HASH_PASSWORDS = false;

	/**
	 * The game client_version
	 */
	public static String client_version = "none";

	public static int ANTI_FLOOD = 0;
	public static boolean PLAYERS_ONLINE = false;
	public static boolean DATABASE_LOGGING = false;
	public static boolean VOTING_CONNECTIONS = true;
	public static boolean STORE_CONNECTIONS = false;
	public static boolean HIGHSCORE_CONNECTIONS = false;
	public static boolean YELL_STATUS = true;
	public static boolean ITEM_SPAWN_TACTICAL = false;
	public static boolean DOUBLE_VOTE_TOKENS = false;
	public static boolean DOUBLE_EXP = false;
	public static boolean DOUBLE_POINTS = false;
	public static boolean DOUBLE_DROPS = false;
	public static boolean TRIPLE_VOTE_TOKENS = false;
	public static boolean DEBUG_MODE = true;
	public static boolean FORUM_DATABASE_CONNECTIONS = false;
	public static boolean KILL_GRENADE = false;
	public static boolean POS_ENABLED = false;
	public static boolean TOURNAMENT_MODE = false;
	public static boolean JSON_PLAYER_LOADING = false;
	public static boolean JSON_PLAYER_SAVING = false;
	public static boolean MYSQL_PLAYER_LOADING = false;
	public static boolean MYSQL_PLAYER_SAVING = false;
	public static boolean MONGO_PLAYER_SAVING = false;
	public static boolean MONGO_PLAYER_LOADING = false;
	
	public static boolean PLAYER_LOGGING = false;

	public static boolean DEVELOPER_MODE = true;

	public static String mysql_characters_password = "null";
	public static String mysql_forum_password = "null";
	public static String connection_address = "localhost";
	
	public static String mongoURI = "mongodb://zaraps:pass@ds155582.mlab.com:55582/zarador-characters";

	public static int gambler_timer_1 = 0;
	public static int gambler_timer_2 = 0;

	public static boolean gambler_1 = false;
	public static boolean gambler_2 = false;

	public static String clan_name_1 = "";
	public static String clan_name_2 = "";

	public static boolean spawned_1 = false;
	public static boolean spawned_2 = false;

	public static NPC advertiser_1;
	public static NPC advertiser_2;

	public static int AUTH_AMOUNT = 1;
	public static int AUTHS_CLAIMED = 0;
	public static boolean FIGHT_PITS_ACTIVE = false;

	public static int PASSWORD_CHANGE = 0;

	public static int DATABASE_LOGGING_TIME = 60;
	public static int CONFIGURATION_TIME = 0;

	public static ArrayList<String> PROTECTED_MAC_ADDRESS = new ArrayList<>();
	public static ArrayList<String> PROTECTED_COMPUTER_ADDRESS = new ArrayList<>();
	public static ArrayList<String> PROTECTED_IP_ADDRESS = new ArrayList<>();
	public static ArrayList<String> SPECIAL_PLAYERS = new ArrayList<>();

	/**
	 * Default values
	 */

	public static int fakePlayerCount = 0;

	/**
	 * The game version
	 */
	public static final int GAME_VERSION = 1;

	/**
	 * The maximum amount of players that can be logged in on a single game
	 * sequence.
	 */
	public static final int LOGIN_THRESHOLD = 300;

	/**
	 * The maximum amount of players that can be logged in on a single game
	 * sequence.
	 */
	public static final int LOGOUT_THRESHOLD = 300;

	/**
	 * The maximum amount of players who can receive rewards on a single game
	 * sequence.
	 */
	public static final int VOTE_REWARDING_THRESHOLD = 15;

	/**
	 * The maximum amount of connections that can be active at a time, or in
	 * other words how many clients can be logged in at once per connection. (0
	 * is counted too)
	 */
	public static final int CONNECTION_AMOUNT = 2;

	/**
	 * The throttle interval for incoming connections accepted by the
	 * {@link ConnectionHandler}.
	 */
	public static final long CONNECTION_INTERVAL = 1000;

	/**
	 * The number of seconds before a connection becomes idle.
	 */
	public static final int IDLE_TIME = 15;

	/**
	 * The keys used for encryption on login
	 */
	public static final BigInteger RSA_MODULUS = new BigInteger(
			"100183080293629537247865568308753024273366818577716625017210873474153748507967056338565570365810542449204325199980394392202732696601035672416034575121617953990803115494430286498197247410362972509753554917095604328717858468247735609128918909527607600420767974268604032223298537923676859979491436292816151123687");
	public static final BigInteger RSA_EXPONENT = new BigInteger(
			"27897847252067367361544572098734191265833718953313828929674816377058850882255806310615708060729700775103818223288252401814240378915252468400943451729092379793916867081525812976498081985684339860892945192827565689187677607607154040537847207997946921214712346167408369996299062648624172819284238618436688719473");

	/**
	 * The maximum amount of messages that can be decoded in one sequence.
	 */
	public static final int DECODE_LIMIT = 30;

	/** GAME **/

	/**
	 * Processing the engine
	 */
	public static final int ENGINE_PROCESSING_CYCLE_RATE = 600;// 200;
	public static final int GAME_PROCESSING_CYCLE_RATE = 600;

	/**
	 * Are the MYSQL services enabled?
	 */
	public static boolean MYSQL_ENABLED = false;

	/**
	 * Is it currently bonus xp?
	 */
	public static final boolean BONUS_EXP = true;// Misc.isWeekend();
	/**
	 * 
	 * The default position
	 */
	public static final Position DEFAULT_POSITION_VARROCK = new Position(3212, 3428);

	public static final Position DEFAULT_POSITION_EDGEVILLE = new Position(3087, 3502);

	public static final int MAX_STARTERS_PER_IP = 1;

	public static final String[] DEVELOPER = { "Michael" };

	/**
	 * Untradeable items Items which cannot be traded or staked
	 */
	public static final int[] UNTRADEABLE_ITEMS = {6542, 15420, 14678, 18689, 13758, 13158, 5733, 10547, 18335, 19669, 18236, 4657, 16389, 13178, 13247, 10586, 4490, 11211, 4142, 5509, 5510, 5511, 5512, 5513, 5514, 5515, 6819, 21776, 11602, 11603, 11604, 11605, 11606, 11607, 11608, 11609, 11610,
			11611, 11612, 11614, 11615, 11618, 11616, 11617, 7887, 21250, 21251, 12487, 21117, 21103, 21102, 22215,
			22216, 22217, 22218, 14300, 14299, 14298, 14297, 14296, 14295, 14294, 14293, 14292, 14291, 21631, 21630,
			21632, 21634, 21635, 21636, 21637, 18359, 18361, 18363, 18337, 6500, 12703, 1843, 13661,
			13262, 6040, 993, 691, 2412, 2413, 2414, 6529, 6950, 1464, 16127, 2677, 2678, 2679, 2680, 2682, 2683,
			2684, 2685, 2686, 2687, 2688, 2689, 2690, 6570, 12158, 12159, 12160, 12163, 12161, 12162, 19143, 19149,
			19146, 19157, 19162, 19152, 4155, 8850, 10551, 8839, 8840, 8842, 11663, 11664, 19712, 11665, 3842, 3844,
			3840, 8844, 8845, 8846, 8847, 8848, 8849, 8850, 10551, 7462, 7461, 7460, 7459, 7458, 7457, 7456, 7455, 7454,
			7453, 11665, 10499, 9748, 9754, 9751, 9769, 9757, 9760, 9763, 9802, 9808, 9784, 9799, 9805, 9781, 9796,
			9793, 9775, 9772, 9778, 9787, 9811, 9766, 9749, 9755, 9752, 9770, 9758, 9761, 9764, 9803, 9809, 9785, 9800,
			9806, 9782, 9797, 9794, 9776, 9773, 9779, 9788, 9812, 9767, 9747, 9753, 9750, 9768, 9756, 9759, 9762, 9801,
			9807, 9783, 9798, 9804, 9780, 9795, 9792, 9774, 9771, 9777, 9786, 9810, 9765, 9948, 9949, 9950, 12169,
			12170, 12171, 20671, 14641, 14642, 6188, 10954, 10956, 10958, 3057, 3058, 3059, 3060, 3061, 7594, 7592,
			7593, 7595, 7596, 14076, 14077, 14081, 10840, 10836, 6858, 6859, 10837, 10838, 10839, 20046, 20044, 20045,
			11789, 19708, 19706, 19707, 4860, 4866, 4872, 4878, 4884, 4896, 4890, 4896,
			4902, 4932, 4938, 4944, 4950, 4908, 4914, 4920, 4926, 4956, 4926, 4968, 4994, 4980, 4986, 4992, 4998, 18778,
			18779, 18780, 18781, 13450, 13444, 13405, 15502, 10548, 10549, 10550, 10551, 10555, 10552, 10553, 2412,
			2413, 2414, 20747, 18365, 18373, 18371, 15246, 12964, 12971, 12978, 14017, 757, 8851, 13855, 13848, 13849,
			13857, 13856, 13854, 13853, 13852, 13851, 13850, 5509, 13653, 14021, 14020, 19111, 14019, 14022, 21097,
			21096, 21093, 21094, 21085, 21086, 21087, 21095, 21099, 21098, 19785, 19786, 18782, 12954, 20072, 11977, 11978,
			11979, 11980, 11981, 11982, 11983, 11984, 11985, 11986, 11987, 11988, 11989, 11990, 11991, 11992, 11993,
			11994, 11995, 11996, 11997, 11998, 11999, 12000, 12001, 12002, 12003, 15103, 15104,
			15106, 15105, 11137, 11139, 11141, 11185, 11186, 11187, 11188, 19711, 19787, 19788, 19789, 19790,
			12954, 12816, 13178, 13247, 12921, 12940, 12939, 12643, 12644, 12645, 12649, 12650, 12651, 12652, 12653, 11995, 12654, 12655, 13181, 13178, 13179, 13177, 13225, 12648, 13322, 13320, 13321, 13247};

	public static final int[] SMITHABLE_ITEMS = { 2, 1205, 1351, 1422, 1139, 9375, 1277, 4819, 1794, 819, 39, 1321,
			1265, 1291, 9420, 1155, 864, 1173, 1337, 1375, 1103, 1189, 3095, 1307, 1087, 1075, 1117, 1203, 1349, 1420,
			7225, 1137, 9140, 1279, 4820, 820, 40, 1323, 1267, 1293, 1153, 863, 1175, 9423, 1335, 1363, 1101, 4540,
			1191, 3096, 1309, 1081, 1067, 1115, 1207, 1353, 1424, 1141, 9141, 1539, 1281, 821, 41, 1325, 1269, 1295,
			2370, 9425, 1157, 865, 1177, 1339, 1365, 1105, 1193, 3097, 1311, 1084, 1069, 1119, 1209, 1355, 1428, 1143,
			9142, 1285, 4822, 822, 42, 1329, 1273, 1299, 9427, 1159, 866, 1181, 1343, 9416, 1369, 1109, 1197, 3099,
			1315, 1085, 1071, 1121, 1211, 1357, 1430, 1145, 9143, 1287, 4823, 823, 43, 1331, 1271, 1301, 9429, 1161,
			867, 1183, 1345, 1371, 1111, 1199, 3100, 1317, 1091, 1073, 1123, 1213, 1359, 1432, 1147, 9144, 1289, 4824,
			824, 44, 1333, 1275, 1303, 9431, 1163, 868, 1185, 1347, 1373, 1113, 1201, 3101, 1319, 1093, 1079, 1127 };

	/**
	 * Unsellable items Items which cannot be sold to shops
	 */
	public static int UNSELLABLE_ITEMS[] = new int[] {6542, 15420, 14678, 18689, 13758, 13158, 5733, 10547, 18361, 18363, 19711, 19787, 19788, 19789, 19790, 18236, 11137, 11139, 11141, 11185, 11186, 11187, 11188, 4657, 16389, 12954, 13178, 13247, 10586, 11602, 11603, 11604, 11605, 11606, 11607, 11608, 11609, 11610,
			11611, 11612, 11614, 11615, 11618, 11616, 11617, 1843, 6040, 993, 691, 2412, 2413, 2414, 13263, 13281,
			14019, 14022, 19785, 19786, 1419, 16127, 4084, 15403, 10887, 6199, 15501, 15441, 15442, 15443, 15444, 14004,
			14005, 14006, 14007, 11848, 11850, 11856, 11854, 11852, 11846, 15018, 15019, 15020, 15220, 14000, 14001,
			14002, 14003, 2577, 19335, 15332, 19336, 19337, 19338, 19339, 19340, 9813, 20084, 8851, 6529, 14641, 14642,
			14017, 10941, 10939, 14938, 10933, 14936, 10940, 18782, 14021, 14020, 13653, 5512, 5509, 5510, 10942,
			10934, 10935, 10943, 10944, 7774, 7775, 7776, 10936, 1038, 1040, 1042, 1044, 1046, 1048, // Phats
			1053, 1055, 1057, // Hween
			12158, 12159, 12160, 12163,
			1050, // Santa
			19780, // Korasi's
			20671, // Brackish
			20135, 20139, 20143, 20147, 20151, 20155, 20159, 20163, 20167, // Nex
			18335, 19669,													// armors
			6570, 15103, 15104, 15106, 15105, // Fire cape
			19143, 19146, 19149, // God bows
			8844, 8845, 8846, 8847, 8848, 8849, 8850, 20072, // Defenders
			20135, 20139, 20143, 20147, 20151, 20155, 20159, 20163, 20167, // Torva,
																			// pernix,
																			// virtus
			13746, 13748, 13750, 13752, 13738, 21104, 13740, 13742, 13744, // Spirit
																			// shields
																			// &
																			// Sigil
			11694, 11696, 11698, 11700, 11702, 11704, 11706, 11708, 11686, 11688, 11690, 11692, 11710, 11712, 11714, // Godswords,
																														// hilts,
																														// pieces
			15486, // sol
			11730, // ss
			11718, 11720, 11722, // armadyl
			11724, 11726, 11728, // bandos
			11286, 11283, 11284, 11613, // dfs & visage
			14472, 14474, 14476, 14479, // dragon pieces and plate
			14484, // dragon claws
			13887, 13888, 13893, 13895, 13899, 13901, 13905, 13907, 13911, 13913, 13917, 13919, 13923, 13925, 13929,
			13931, // Vesta's
			13884, 13886, 13890, 13892, 13896, 13898, 13902, 13904, 13908, 13910, 13914, 13916, 13920, 13922, 13926,
			13928, // Statius's
			13870, 13872, 13873, 13875, 13876, 13878, 13879, 13880, 13881, 13882, 13883, 13944, 13946, 13947, 13949,
			13950, 13952, 13953, 13954, 13955, 13956, 13957, // Morrigan's
			13858, 13860, 13861, 13863, 13864, 13866, 13867, 13869, 13932, 13934, 13935, 13937, 13938, 13940, 13941,
			13943, // Zuriel's
			20147, 20149, 20151, 20153, 20155, 20157, // Pernix
			20159, 20161, 20163, 20165, 20167, 20169, // Virtus
			20135, 20137, 20139, 20141, 20143, 20145, // Torva
			11335, // D full helm
			6731, 6733, 6735, 19111, // warrior ring, seers ring, archer ring
			962, // Christmas Cracker
			21787, 21790, 21793, // Steadfast, glaiven, ragefire
			20674, // Something something..... pvp armor, statuettes
			13958, 13961, 13964, 13967, 13970, 13973, 13976, 13979, 13982, 13985, 13988, 13908, 13914, 13926, 13911,
			13917, 13923, 13929, 13932, 13935, 13938, 13941, 13944, 13947, 13950, 13953, 13957, 13845, 13846, 13847,
			13848, 13849, 13850, 13851, 13852, 13853, 13854, 13855, 13856, 13857, // Le
																					// corrupted
																					// items
			11995, 6500, 19670, 20000, 20001, 20002, 11996, 18782, 18335,
			11997, 19712, 12001, 12002, 12003, 12005, 12006, 11990, 11991, 11992, 11993, 11994, 11989, 11988, 11987,
			11986, 11985, 11984, 11983, 11982, 11981, 11979, 18351, 18351, 18349, 18353, 18357, 18355, 18359, 18335, 19669,
			12954, 12816, 13178, 13247, 12921, 12940, 12939, 12643, 12644, 12645, 12649, 12650, 12651, 12652, 12653, 11995, 12654, 12655, 13181, 13178, 13179, 13177, 13225, 12648, 13322, 13320, 13321, 13247};

	public static final int ATTACK_TAB = 0, SKILLS_TAB = 2, QUESTS_TAB = 15, ACHIEVEMENT_TAB = 1, INVENTORY_TAB = 3,
			EQUIPMENT_TAB = 4, PRAYER_TAB = 5, MAGIC_TAB = 6,

			SUMMONING_TAB = 7, FRIEND_TAB = 8, IGNORE_TAB = 9, CLAN_CHAT_TAB = 10, LOGOUT = 13, OPTIONS_TAB = 11,
			EMOTES_TAB = 12, NOTES_TAB = 14;

}
