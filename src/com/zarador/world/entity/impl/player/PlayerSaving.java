package com.zarador.world.entity.impl.player;

import java.io.File;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.logging.Level;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.zarador.GameServer;
import com.zarador.GameSettings;
import com.zarador.net.login.LoginManager;
import com.zarador.net.mysql.CompletedCallback;
import com.zarador.net.mysql.SQLCallback;
import com.zarador.net.mysql.impl.AccountExistsQuery;
import com.zarador.util.MD5;
import com.zarador.util.Misc;

public class PlayerSaving {

	public static AccountExistsQuery accountExists(String name, CompletedCallback callback) {
		return new AccountExistsQuery(name).setCompletedCallback(callback).execute();
	}

	/*
	 * public static boolean accountExists(Player player, String name,
	 * SQLCallback callback) { GameServer.getServerPool().executeQuery(
	 * "SELECT username FROM `accounts` as acc WHERE LOWER (`username`) = LOWER('"
	 * + name + "') LIMIT 1", new SQLCallback() {
	 * 
	 * @Override public void queryComplete(ResultSet rs) throws SQLException {
	 * if (rs.next()) { player.accountExists = true; } }
	 * 
	 * @Override public void queryError(SQLException e) { e.printStackTrace(); }
	 * }); return player.accountExists; }
	 */

	public static void updatePassword(Player p, String password, String salt) {
		GameServer.getServerPool().executeQuery(
				"UPDATE accounts SET password=" + password + ",salt=" + salt + " WHERE username=" + p.getUsername(),
				new SQLCallback() {
					@Override
					public void queryError(SQLException e) {
						System.err.println("Error updating password for " + p.getUsername() + ": " + password);
						e.printStackTrace();
					}

					@Override
					public void queryComplete(ResultSet result) throws SQLException {
						// password updated!
					}
				});
	}

	public static void createNewAccount(Player p) {
		String query = "INSERT INTO `accounts` (username, password) VALUES ('" + p.getUsername() + "', '"
				+ p.getPassword() + "')";
		if (GameSettings.HASH_PASSWORDS) {
			if (p.getSalt().isEmpty()) {
				p.setSalt(PlayerSaving.generateSalt());
			}
			query = "INSERT INTO `accounts` (username, password, salt) VALUES ('" + p.getUsername() + "', '"
					+ p.getPassword() + ", " + p.getSalt() + "')";
		}
		GameServer.getServerPool().executeQuery(query, new SQLCallback() {
			@Override
			public void queryError(SQLException e) {
				p.setResponse(3);
				e.printStackTrace();
				LoginManager.finalizeLogin(p);
			}

			@Override
			public void queryComplete(ResultSet result) throws SQLException {
				p.setNewPlayer(true);
				p.setResponse(2);
				LoginManager.finalizeLogin(p);
			}
		});
	}

	public static void saveGame(Player player) {
		if (player == null || player.getUsername() == null) {
			return;
		}
		final Player p = player;
		GameServer.getServerPool().executeLogoutQuery(p, new SQLCallback() {
			@Override
			public void queryComplete(ResultSet result) throws SQLException {

			}

			@Override
			public void queryError(SQLException e) {
				e.printStackTrace();
			}

		});
	}

	public static String generateSalt() {
		final Random r = new SecureRandom();
		byte[] salt = new byte[32];
		r.nextBytes(salt);
		return Base64.encode(salt);
	}

	public static String hashPassword(String salt, String password) {
		try {
			return MD5.MD5(MD5.MD5(salt) + MD5.MD5(password.trim()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.err.println("FAILED TO HASH PASSWORD! CRUCIAL ERROR! [salt: " + salt + ", password: " + password + "]");
		return password;
	}

	public static void save(Player player) {
		if (player.newPlayer())
			return;
		// Create the path and file objects.
		Path path = Paths.get("./characters/", player.getUsername() + ".json");
		File file = path.toFile();
		file.getParentFile().setWritable(true);

		// Attempt to make the player save directory if it doesn't
		// exist.
		if (!file.getParentFile().exists()) {
			try {
				file.getParentFile().mkdirs();
			} catch (SecurityException e) {
				System.out.println("Unable to create directory for player data!");
			}
		}
		try (FileWriter writer = new FileWriter(file)) {
			writer.write(toJson(player));
			writer.close();

			/*
			 * Housing
			 */
			/*FileOutputStream fileOut = new FileOutputStream("./housing/rooms/" + player.getUsername() + ".ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(player.getHouseRooms());
			out.close();
			fileOut.close();

			fileOut = new FileOutputStream("./housing/furniture/" + player.getUsername() + ".ser");
			out = new ObjectOutputStream(fileOut);
			out.writeObject(player.getHouseFurniture());
			out.close();
			fileOut.close();

			fileOut = new FileOutputStream("./housing/portals/" + player.getUsername() + ".ser");
			out = new ObjectOutputStream(fileOut);
			out.writeObject(player.getHousePortals());
			out.close();
			fileOut.close();*/
		} catch (Exception e) {
			// An error happened while saving.
			GameServer.getLogger().log(Level.WARNING, "An error has occured while saving a character file!", e);
		}
	}

	public static String toJson(Player player) {
		Gson builder = new GsonBuilder().setPrettyPrinting().create();
		JsonObject object = new JsonObject();
		object.addProperty("total-play-time-ms", player.getTotalPlayTime());
		object.addProperty("username", player.getUsername().trim());
		if (GameSettings.HASH_PASSWORDS) {
			if (player.getSalt().isEmpty()) {
				player.setSalt(generateSalt());
			}
			object.addProperty("player-salt", player.getSalt());
			object.addProperty("password-hash", hashPassword(player.getSalt(), player.getPassword()));
		} else {
			object.addProperty("password", player.getPassword().trim());
		}
		object.addProperty("password_change", new Integer(player.getPasswordChange()));
		object.addProperty("email", player.getEmailAddress() == null ? "null" : player.getEmailAddress().trim());
		object.addProperty("yell-tag", player.getYellTag());
		object.addProperty("staff-rights", player.getStaffRights().name());
		object.addProperty("donator-rights", player.getDonatorRights().name());
		object.addProperty("game-mode", player.getGameModeAssistant().getGameMode().name());
		object.addProperty("trusted-dicer", new Boolean(player.isTrustedDicer()));
		if(player.getSlayer().getSlayerMaster() != null) {
			object.addProperty("slayer-master", player.getSlayer().getSlayerMaster().name());
		}
		if(player.getSlayer().getSlayerTask() != null) {
			object.addProperty("slayer-task", player.getSlayer().getSlayerTask().name());
		}
		if(player.getSlayer().getAmountLeft() > 0) {
			object.addProperty("slayer-amount", new Integer(player.getSlayer().getAmountLeft()));
		}
		object.addProperty("slayer-streak", new Integer(player.getSlayer().getSlayerStreak()));
		object.addProperty("slayer-partner", player.getSlayer().getDuoSlayerName());
		//object.addProperty("exp-rate", player.getExpRate().name());
		object.addProperty("player-invisibility", player.isInvisible());
		object.addProperty("withdrawx", new Integer(player.getWithdrawX()));
		object.addProperty("last-login", player.getLastLogin());
		object.addProperty("last-ip-address", player.getLastIpAddress());
		object.addProperty("last-serial-address", player.getLastSerialAddress());
		object.addProperty("last-mac-address", player.getLastMacAddress());
		object.addProperty("last-computer-address", player.getLastComputerAddress());
		object.addProperty("last-bank-ip", player.getLastBankIp());
		object.addProperty("last-bank-serial", player.getLastBankSerial());
		object.addProperty("loyalty-title", player.getLoyaltyTitle().name());
		object.addProperty("player-title", player.getTitle());
		object.add("position", builder.toJsonTree(player.getPosition()));
		object.addProperty("melee-max-hit", new Integer(player.getMeleeMaxHit()));
		object.addProperty("range-max-hit", new Integer(player.getRangeMaxHit()));
		object.addProperty("magic-max-hit", new Integer(player.getMagicMaxHit()));
		object.addProperty("loyalty-rank", new Integer(player.getLoyaltyRank()));
		object.addProperty("daily-task-date", new Integer(player.dailyTaskDate));
		object.addProperty("daily-task", new Integer(player.dailyTask));
		object.addProperty("daily-task-progress", new Integer(player.dailyTaskProgress));
		object.addProperty("completed-daily-task", new Boolean(player.completedDailyTask));
		object.addProperty("barrows-chests-looted", new Integer(player.barrowsChestsLooted));
		object.addProperty("barrows-chest-rewards", new Integer(player.barrowsChestRewards));
		object.addProperty("online-status", player.getRelations().getStatus().name());
		object.addProperty("jailed-ticks", new Integer(player.getPlayerTimers().getJailTicks()));
		object.addProperty("xp-rate", new Boolean(player.getXpRate()));
		object.addProperty("given-starter", new Boolean(player.didReceiveStarter()));
		object.addProperty("duradel-only-boss", new Boolean(player.getSlayer().isOnlyBossDuradel()));
		object.addProperty("yell-toggle", new Boolean(player.yellToggle()));
		object.addProperty("tourney-toggle", new Boolean(player.tourneyToggle()));
		object.addProperty("yell-ticks", new Integer(player.getPlayerTimers().getYellTicks()));
		object.addProperty("reset-stats-1", new Integer(player.reset_stats_1));
		object.addProperty("ge-return", new Boolean(player.hasDoneGrandExchangeReturn()));
		object.addProperty("money-pouch", new Long(player.getMoneyInPouch()));
		object.addProperty("tournament-points", new Long(player.getPointsHandler().getTournamentPoints()));
		object.addProperty("donated", new Long(player.getAmountDonated()));
		object.addProperty("credits", new Long(player.getPoints()));
		object.addProperty("quest-points", new Integer(player.getQuestPoints()));
		object.addProperty("warning-points", new Integer(player.getWarningPoints()));
		object.addProperty("minutes-bonus-exp", new Integer(player.getMinutesBonusExp()));
		object.addProperty("total-gained-exp", new Long(player.getSkillManager().getTotalGainedExp()));
		object.addProperty("prestige-points", new Integer(player.getPointsHandler().getPrestigePoints()));
		object.addProperty("achievement-points", new Integer(player.getPointsHandler().getAchievementPoints()));
		object.addProperty("dung-tokens", new Integer(player.getPointsHandler().getDungeoneeringTokens()));
		object.addProperty("commendations", new Integer(player.getPointsHandler().getCommendations()));
		object.addProperty("loyalty-points", new Integer(player.getPointsHandler().getLoyaltyPoints()));
		object.addProperty("total-loyalty-points",
				new Double(player.getAchievementAttributes().getTotalLoyaltyPointsEarned()));
		object.addProperty("dung-items", new Boolean(player.isCanWearDungItems()));
		object.addProperty("Can-Vote", new Boolean(player.isCanVote()));
		object.addProperty("revs-warning", new Boolean(player.getRevsWarning()));
		object.addProperty("equipment-hits", new Integer(player.getEquipmentHits()));
		object.addProperty("votes-claimed", new Integer(player.getVotesClaimed()));
		object.addProperty("voting-points", new Integer(player.getPointsHandler().getVotingPoints()));
		object.addProperty("double-xp", new Integer(player.getDoubleXP()));
		object.addProperty("slayer-points", new Integer(player.getPointsHandler().getSlayerPoints()));
		object.addProperty("pk-points", new Integer(player.getPointsHandler().getPkPoints()));
		object.addProperty("toxic-staff-charges", new Integer(player.getToxicStaffCharges()));
		object.addProperty("forum-connections", new Integer(player.getForumConnections()));
		object.addProperty("boss-points", new Integer(player.getBossPoints()));
		object.addProperty("player-kills", new Integer(player.getPlayerKillingAttributes().getPlayerKills()));
		object.addProperty("player-killstreak", new Integer(player.getPlayerKillingAttributes().getPlayerKillStreak()));
		object.addProperty("player-deaths", new Integer(player.getPlayerKillingAttributes().getPlayerDeaths()));
		object.addProperty("target-percentage", new Integer(player.getPlayerKillingAttributes().getTargetPercentage()));
		object.addProperty("bh-rank", new Integer(player.getAppearance().getBountyHunterSkull()));
		object.addProperty("gender", player.getAppearance().getGender().name());
		object.addProperty("spell-book", player.getSpellbook().name());
		object.addProperty("prayer-book", player.getPrayerbook().name());
		object.addProperty("running", new Boolean(player.getWalkingQueue().isRunning()));
		object.addProperty("run-energy", new Float(player.getRunEnergy()));
		object.addProperty("music", new Boolean(player.musicActive()));
		object.addProperty("potato-timer", new Long(player.getRottenPotato().getTime()));
		object.addProperty("potato-drop-timer", new Long(player.getRottenPotatoDropTimer().getTime()));
		object.addProperty("potato-heal", new Integer(player.rottenPotatoHeal));
		object.addProperty("presents-new", new Integer(player.presentsPickedUp));
		object.addProperty("potato-prayer", new Integer(player.rottenPotatoPrayer));
		object.addProperty("potato-drop", new Integer(player.rottenPotatoDrop));
		object.addProperty("drop-rate-boost", new Double(player.dropRateBoost));
		object.addProperty("after-beta", new Boolean(player.afterBeta));
		object.addProperty("got-beta-items", new Boolean(player.gotBetaItems));
		object.addProperty("sounds", new Boolean(player.soundsActive()));
		object.addProperty("auto-retaliate", new Boolean(player.isAutoRetaliate()));
		object.addProperty("xp-locked", new Boolean(player.experienceLocked()));
		object.addProperty("show-ip", new Boolean(player.showIpAddress()));
		object.addProperty("show-home", new Boolean(player.showHomeOnLogin()));
		object.addProperty("veng-cast", new Boolean(player.hasVengeance()));
		object.addProperty("last-veng", new Long(player.getLastVengeance().elapsed()));
		object.addProperty("last-summon", new Long(player.getSummoningTimer().elapsed()));
		object.addProperty("fight-type", player.getFightType().name());
		object.addProperty("arena-victories", new Integer(player.getDueling().arenaStats[0]));
		object.addProperty("arena-losses", new Integer(player.getDueling().arenaStats[1]));
		object.addProperty("sol-effect", new Integer(player.getStaffOfLightEffect()));
		object.addProperty("skull-timer", new Integer(player.getSkullTimer()));
		object.addProperty("accept-aid", new Boolean(player.isRequestAssistance()));
		object.addProperty("poison-damage", new Integer(player.getPoisonDamage()));
		object.addProperty("poison-immunity", new Integer(player.getPoisonImmunity()));
		object.addProperty("venom-damage", new Integer(player.getVenomDamage()));
		object.addProperty("venom-immunity", new Integer(player.getVenomImmunity()));
		object.addProperty("overload-timer", new Integer(player.getOverloadPotionTimer()));
		object.addProperty("fire-immunity", new Integer(player.getFireImmunity()));
		object.addProperty("fire-damage-mod", new Integer(player.getFireDamageModifier()));
		object.addProperty("prayer-renewal-timer", new Integer(player.getPrayerRenewalPotionTimer()));
		object.addProperty("teleblock-timer", new Integer(player.getTeleblockTimer()));
		object.addProperty("special-amount", new Integer(player.getSpecialPercentage()));
		object.addProperty("entered-gwd-room",
				new Boolean(player.getMinigameAttributes().getGodwarsDungeonAttributes().hasEnteredRoom()));
		object.addProperty("announced-maxed", new Boolean(player.hasAnnouncedMax()));
		object.addProperty("gwd-altar-delay",
				new Long(player.getMinigameAttributes().getGodwarsDungeonAttributes().getAltarDelay()));
		object.add("gwd-killcount",
				builder.toJsonTree(player.getMinigameAttributes().getGodwarsDungeonAttributes().getKillcount()));
		object.addProperty("effigy", new Integer(player.getEffigy()));
		object.addProperty("summon-npc", new Integer(player.getSummoning().getFamiliar() != null
				? player.getSummoning().getFamiliar().getSummonNpc().getId() : -1));
		object.addProperty("summon-death", new Integer(player.getSummoning().getFamiliar() != null
				? player.getSummoning().getFamiliar().getDeathTimer() : -1));
		object.addProperty("process-farming", new Boolean(player.shouldProcessFarming()));
		object.addProperty("clanchat", player.getClanChatName() == null ? "null" : player.getClanChatName().trim());
		object.addProperty("autocast", new Boolean(player.isAutocast()));
		object.addProperty("autocast-spell",
				player.getAutocastSpell() != null ? player.getAutocastSpell().spellId() : -1);
		object.addProperty("dfs-charges", player.getDfsCharges());
		object.addProperty("coins-gambled", new Integer(player.getAchievementAttributes().getCoinsGambled()));
		object.addProperty("duo-times", new Integer(player.getSlayer().getDuoTimes()));
		object.add("charges", builder.toJsonTree(player.getDegrading().getCharges()));
		object.add("killed-players", builder.toJsonTree(player.getPlayerKillingAttributes().getKilledPlayers()));
		object.add("attacked-players", builder.toJsonTree(player.playersAttacked));
		object.add("killed-gods", builder.toJsonTree(player.getAchievementAttributes().getGodsKilled()));
		//object.addProperty("dungeon-stage", player.getDungeoneering().getDungeonStage().name());
		object.add("comp-color",
				builder.toJsonTree(player.compColor));
		object.add("comp-color-rgb",
				builder.toJsonTree(player.compColorsRGB));
		object.add("comp-presets",
				builder.toJsonTree(player.compPreset));
		object.add("barrows-brother",
				builder.toJsonTree(player.getMinigameAttributes().getBarrowsMinigameAttributes().getBarrowsData()));
		object.addProperty("random-coffin",
				new Integer(player.getMinigameAttributes().getBarrowsMinigameAttributes().getRandomCoffin()));
		object.addProperty("barrows-killcount",
				new Integer(player.getMinigameAttributes().getBarrowsMinigameAttributes().getKillcount()));
		object.add("nomad", builder.toJsonTree(player.getMinigameAttributes().getNomadAttributes().getQuestParts()));
		object.add("recipe-for-disaster",
				builder.toJsonTree(player.getMinigameAttributes().getRecipeForDisasterAttributes().getQuestParts()));
		object.add("claw-quest",
				builder.toJsonTree(player.getMinigameAttributes().getClawQuestAttributes().getQuestParts()));
		object.add("shrek1-quest",
				builder.toJsonTree(player.getMinigameAttributes().getShrek1Attributes().getQuestParts()));
		object.add("farm-quest",
				builder.toJsonTree(player.getMinigameAttributes().getFarmQuestAttributes().getQuestParts()));
		object.addProperty("recipe-for-disaster-wave",
				new Integer(player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted()));
		object.addProperty("rune-ess", new Integer(player.getStoredRuneEssence()));
		object.addProperty("pure-ess", new Integer(player.getStoredPureEssence()));
		object.addProperty("has-bank-pin", new Boolean(player.getBankPinAttributes().hasBankPin()));
		object.addProperty("last-pin-attempt", new Long(player.getBankPinAttributes().getLastAttempt()));
		object.addProperty("invalid-pin-attempts", new Integer(player.getBankPinAttributes().getInvalidAttempts()));
		object.add("bank-pin", builder.toJsonTree(player.getBankPinAttributes().getBankPin()));
		object.add("appearance", builder.toJsonTree(player.getAppearance().getLook()));
		object.add("agility-obj", builder.toJsonTree(player.getCrossedObstacles()));
		object.add("skills", builder.toJsonTree(player.getSkillManager().getSkills()));
		object.add("inventory", builder.toJsonTree(player.getInventory().getItems()));
		object.add("equipment", builder.toJsonTree(player.getEquipment().getItems()));
		object.add("dung-bind", builder.toJsonTree(player.getDungeoneering().getBindedItems()));
		object.add("bank-0", builder.toJsonTree(player.getBank(0).getValidItems()));
		object.add("bank-1", builder.toJsonTree(player.getBank(1).getValidItems()));
		object.add("bank-2", builder.toJsonTree(player.getBank(2).getValidItems()));
		object.add("bank-3", builder.toJsonTree(player.getBank(3).getValidItems()));
		object.add("bank-4", builder.toJsonTree(player.getBank(4).getValidItems()));
		object.add("bank-5", builder.toJsonTree(player.getBank(5).getValidItems()));
		object.add("bank-6", builder.toJsonTree(player.getBank(6).getValidItems()));
		object.add("bank-7", builder.toJsonTree(player.getBank(7).getValidItems()));
		object.add("bank-8", builder.toJsonTree(player.getBank(8).getValidItems()));

		/** STORE SUMMON **/
		if (player.getSummoning().getBeastOfBurden() != null) {
			object.add("store", builder.toJsonTree(player.getSummoning().getBeastOfBurden().getValidItems()));
		}
		object.add("charm-imp", builder.toJsonTree(player.getSummoning().getCharmImpConfigs()));

		object.add("friends", builder.toJsonTree(player.getRelations().getFriendList().toArray()));
		object.add("ignores", builder.toJsonTree(player.getRelations().getIgnoreList().toArray()));
		object.add("loyalty-titles", builder.toJsonTree(player.getUnlockedLoyaltyTitles()));
		object.add("kills", builder.toJsonTree(player.getKillsTracker().toArray()));
		object.add("drops", builder.toJsonTree(player.getDropLog().toArray()));
        object.add("buyBacks", builder.toJsonTree(player.itemToBuyBack.toArray()));
		object.add("achievements-completion", builder.toJsonTree(player.getAchievementAttributes().getCompletion()));
		object.add("achievements-progress", builder.toJsonTree(player.getAchievementAttributes().getProgress()));
		object.add("last-duel-rules", builder.toJsonTree(player.lastDuelRules));

		object.add("notes", builder.toJsonTree(player.getNotes()));
		object.add("colours", builder.toJsonTree(player.getNoteColours()));

		return builder.toJson(object);
	}

	public static boolean playerExists(String p) {
		p = Misc.formatPlayerName(p.toLowerCase());
		return new File("./characters/" + p + ".json").exists();
	}
}
