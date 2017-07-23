package com.zarador.world.content;

import com.zarador.model.DonatorRights;
import com.zarador.model.Flag;
import com.zarador.model.player.GameMode;
import com.zarador.util.Misc;
import com.zarador.world.content.achievements.Achievements.AchievementData;
import com.zarador.world.entity.impl.player.Player;

public class Titles {

	public enum TitleData {

		PKER("@dre@", AchievementData.DEFEAT_30_PLAYERS, 15, 4587),
		GODLIKE("@god@", AchievementData.BURY_500_FROST_DRAGON_BONES, 500, 18830),
		CHEF("@or3@", AchievementData.COOK_500_MANTA, 500, 391),
		TERMINATOR("@ter@", AchievementData.DEFEAT_500_BOSSES, 500, 21091),
		FISHERMAN("@fro@", AchievementData.FISH_1500_ROCKTAILS, 1500, 309),
		LUMBERJACK("@lum@", AchievementData.CHOP_2500_MAGIC_LOGS, 2500, 6739),
		DESTROYER("@red@", AchievementData.DEAL_HARD_DAMAGE_USING_MELEE, 500000, 21371),
		PROPKER("@red@", AchievementData.HIT_700_WITH_SPECIAL_ATTACK, 1, 11694),
		ALCHEMIST("@alc@", AchievementData.HIGH_ALCH_ITEMS, 1000, 1387),
		WIZARD("@369@", AchievementData.DEAL_HARD_DAMAGE_USING_MAGIC, 500000, 22211),
		BLACKSMITH("@smt@", AchievementData.SMELT_1000_RUNE_BARS, 1000, 2363),
		THE_REAL("@cmm@", AchievementData.CREATE_CLAN_CHAT, 1, 21140),
		TZTOK("@or4@", AchievementData.DEFEAT_JAD, 1, 6570),
		PREMIUM("@or3@", null, 1, 10943),
		EXTREME("@gre@", null, 1, 10934),
		LEGENDARY("@mag@", null, 1, 10935),
		UBER("@yel@", null, 1, 7629),
		PLATINUM("@pla@", null, 1, 7776),
		KNIGHT("@god@", null, 1, 1052),
		IRONMAN("@iro@", null, 1, 1137),
		REALISM("@rea@", null, 1, 16389);


		TitleData(String color, AchievementData data, int amount, int interfaceItem) {
			this.color = color;
			this.achievement = data;
			this.amount = amount;
			this.interfaceItem = interfaceItem;
		}

		private AchievementData achievement;
		private int amount;
		private int interfaceItem;
		private String color;

		public int getAmount() {
			return amount;
		}

		public AchievementData getAchievement() {
			return achievement;
		}

		/**
		 * Returns the formatted name of the slayer task.
		 * @return
		 */
		public String getName() {
			if(this == TZTOK) {
				return "TzTok";
			}
			return Misc.formatText(this.toString().toLowerCase().replace("_", " "));
		}

		public int getInterfaceItem() {
			return interfaceItem;
		}

		public String getColor() {
			return color;
		}
	}

	/**
	 * Handle a button for a title
	 * @param player
	 * @param buttonId
	 */
	public static boolean handleButtons(Player player, int buttonId) {
		int value = 0;
		for (TitleData titleData : TitleData.values()) {
			if(buttonId == -20126 + value) {
				if(player.getStaffRights().isStaff()) {
					player.getPacketSender().sendMessage("Sorry, the title system is off limits to staff members.");
					return false;
				}
				boolean completed = false;
				if(titleData.getAchievement() == null) {
					switch(titleData) {
						case PREMIUM:
							if(player.getDonatorRights() == DonatorRights.PREMIUM) {
								completed = true;
							}
							break;
						case EXTREME:
							if(player.getDonatorRights() == DonatorRights.EXTREME) {
								completed = true;
							}
							break;
						case LEGENDARY:
							if(player.getDonatorRights() == DonatorRights.LEGENDARY) {
								completed = true;
							}
							break;
						case UBER:
							if(player.getDonatorRights() == DonatorRights.UBER) {
								completed = true;
							}
							break;
						case PLATINUM:
							if(player.getDonatorRights() == DonatorRights.PLATINUM) {
								completed = true;
							}
							break;
						case KNIGHT:
							if(player.getGameModeAssistant().getGameMode() == GameMode.KNIGHT) {
								completed = true;
							}
							break;
						case IRONMAN:
							if(player.getGameModeAssistant().getGameMode() == GameMode.IRONMAN) {
								completed = true;
							}
							break;
						case REALISM:
							if(player.getGameModeAssistant().getGameMode() == GameMode.REALISM) {
								completed = true;
							}
							break;
					}
				} else {
					completed = player.getAchievementAttributes().getCompletion()[titleData.getAchievement().ordinal()];
				}
				if(!completed) {
					player.getPacketSender().sendMessage("You have not unlocked this title yet.");
					return true;
				}
				player.setTitle(titleData.getColor() + titleData.getName());
				player.getPacketSender().sendMessage("<col=ff0000>You have activated the title "+titleData.getName()+".");
				player.getPacketSender().sendMessage("Type ::resettitle to remove your game title.");
				player.getUpdateFlag().flag(Flag.APPEARANCE);
				return true;
			}
			value += 9;
		}
		return false;
	}

	/**
	 * On login set your staff title.
	 * @param player
	 */
	public static void onLogin(Player player) {
		if(player.getStaffRights().isStaff()) {
			switch(player.getStaffRights()) {
				case SUPPORT:
					player.setTitle("@spt@Support");
					break;
				case MODERATOR:
					player.setTitle("@mds@Moderator");
					break;
				case ADMINISTRATOR:
					player.setTitle("@yel@Admin");
					break;
				case OWNER:
					player.setTitle("@red@Owner");
					break;
				case GLOBAL_MOD:
					player.setTitle("@glb@Global Mod");
					break;
				case MANAGER:
					player.setTitle("@red@Manager");
					break;
				case YOUTUBER:
					player.setTitle("@red@YouTuber");
					break;
				case FORUM_MOD:
					player.setTitle("@fmd@Forum Mod");
					break;
				case WIKI_EDITOR:
					player.setTitle("@wke@Wiki Editor");
					break;
			}
		} else if(player.getDonatorRights().isDonator()) {
			if(player.getTitle().toLowerCase().contains("premium")) {
				if(player.getDonatorRights() != DonatorRights.PREMIUM) {
					player.setTitle("null");
				}
			} else if(player.getTitle().toLowerCase().contains("extreme")) {
				if(player.getDonatorRights() != DonatorRights.EXTREME) {
					player.setTitle("null");
				}
			} else if(player.getTitle().toLowerCase().contains("legendary")) {
				if(player.getDonatorRights() != DonatorRights.LEGENDARY) {
					player.setTitle("null");
				}
			} else if(player.getTitle().toLowerCase().contains("uber")) {
				if(player.getDonatorRights() != DonatorRights.UBER) {
					player.setTitle("null");
				}
			} else if(player.getTitle().toLowerCase().contains("platinum")) {
				if(player.getDonatorRights() != DonatorRights.PLATINUM) {
					player.setTitle("null");
				}
			}
		}
		player.getUpdateFlag().flag(Flag.APPEARANCE);
	}

	/**
	 * Open the interface and write the Strings
	 * @param player
	 */
	public static void openInterface(Player player) {
		int value = 0;
		for (TitleData titleData : TitleData.values()) {
			boolean completed = false;
			boolean progress = false;
			String description = "";
			if(titleData.getAchievement() == null) {
				switch(titleData) {
					case PREMIUM:
						if(player.getDonatorRights() == DonatorRights.PREMIUM) {
							completed = true;
							progress = true;
						}
						description = "Become a Premium Donator";
						break;
					case EXTREME:
						if(player.getDonatorRights() == DonatorRights.EXTREME) {
							completed = true;
							progress = true;
						}
						description = "Become a Extreme Donator";
						break;
					case LEGENDARY:
						if(player.getDonatorRights() == DonatorRights.LEGENDARY) {
							completed = true;
							progress = true;
						}
						description = "Become a Legendary Donator";
						break;
					case UBER:
						if(player.getDonatorRights() == DonatorRights.UBER) {
							completed = true;
							progress = true;
						}
						description = "Become a Uber Donator";
						break;
					case PLATINUM:
						if(player.getDonatorRights() == DonatorRights.PLATINUM) {
							completed = true;
							progress = true;
						}
						description = "Become a Platinum Donator";
						break;
					case KNIGHT:
						if(player.getGameModeAssistant().getGameMode() == GameMode.KNIGHT) {
							completed = true;
							progress = true;
						}
						description = "Become a Knight";
						break;
					case IRONMAN:
						if(player.getGameModeAssistant().getGameMode() == GameMode.IRONMAN) {
							completed = true;
							progress = true;
						}
						description = "Become a Ironman";
						break;
					case REALISM:
						if(player.getGameModeAssistant().getGameMode() == GameMode.REALISM) {
							completed = true;
							progress = true;
						}
						description = "Become a Realism";
						break;
				}
			} else {
				completed = player.getAchievementAttributes().getCompletion()[titleData.getAchievement().ordinal()];
				progress = titleData.getAchievement().progressData != null
						&& player.getAchievementAttributes().getProgress()[titleData.getAchievement().progressData[0]] > 0;
				description = titleData.getAchievement().getName();
			}
			if(completed) {
				player.getPacketSender().sendString(45406 + value, titleData.getColor() +titleData.getName());
				player.getPacketSender().sendString(45413 + value, "@gre@Activate");
				player.getPacketSender().sendString(45408 + value, "@gre@"+description);
				player.getPacketSender().sendString(45409 + value, "@gre@("+Misc.formatAmount(titleData.getAmount())+"/"+Misc.formatAmount(titleData.getAmount())+")");
			} else if(progress) {
				player.getPacketSender().sendString(45406 + value, "@yel@"+titleData.getName());
				player.getPacketSender().sendString(45413 + value, "@yel@Locked");
				player.getPacketSender().sendString(45408 + value, "@yel@"+description);
				if(titleData.getAchievement().progressData != null) {
					player.getPacketSender().sendString(45409 + value, "@yel@("+player.getAchievementAttributes().getProgress()[titleData.getAchievement().progressData[0]]+"/"+Misc.formatAmount(titleData.getAmount())+")");
				}
			} else {
				player.getPacketSender().sendString(45406 + value, "@red@"+titleData.getName());
				player.getPacketSender().sendString(45413 + value, "@red@Locked");
				player.getPacketSender().sendString(45408 + value, "@red@" + description);
				player.getPacketSender().sendString(45409 + value, "@red@(0/"+Misc.formatAmount(titleData.getAmount())+")");
			}
			player.getPacketSender().sendItemOnInterface(45414 + value, titleData.getInterfaceItem(), 0, 1);
			value+= 9;
		}
	}
}
