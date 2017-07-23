package com.zarador.world.entity.impl.player;

import org.scripts.kotlin.content.dialog.Tutorial.StartTutorial;

import com.zarador.GameServer;
import com.zarador.GameSettings;
import com.zarador.engine.task.TaskManager;
import com.zarador.engine.task.impl.BonusExperienceTask;
import com.zarador.engine.task.impl.CombatSkullEffect;
import com.zarador.engine.task.impl.FireImmunityTask;
import com.zarador.engine.task.impl.OverloadPotionTask;
import com.zarador.engine.task.impl.PlayerSkillsTask;
import com.zarador.engine.task.impl.PlayerSpecialAmountTask;
import com.zarador.engine.task.impl.PrayerRenewalPotionTask;
import com.zarador.engine.task.impl.StaffOfLightSpecialAttackTask;
import com.zarador.model.*;
import com.zarador.model.container.impl.Bank;
import com.zarador.model.container.impl.Equipment;
import com.zarador.model.definitions.WeaponAnimations;
import com.zarador.model.definitions.WeaponInterfaces;
import com.zarador.model.input.impl.ChangePassword;
import com.zarador.model.player.GameMode;
import com.zarador.net.PlayerSession;
import com.zarador.net.SessionState;
import com.zarador.net.login.LoginResponses;
import com.zarador.net.security.ConnectionHandler;
import com.zarador.util.Misc;
import com.zarador.world.World;
import com.zarador.world.content.*;
import com.zarador.world.content.achievements.Achievements;
import com.zarador.world.content.achievements.Achievements.AchievementData;
import com.zarador.world.content.clan.ClanChatManager;
import com.zarador.world.content.combat.effect.CombatPoisonEffect;
import com.zarador.world.content.combat.effect.CombatTeleblockEffect;
import com.zarador.world.content.combat.effect.CombatVenomEffect;
import com.zarador.world.content.combat.magic.Autocasting;
import com.zarador.world.content.combat.prayer.CurseHandler;
import com.zarador.world.content.combat.prayer.PrayerHandler;
import com.zarador.world.content.combat.pvp.BountyHunter;
import com.zarador.world.content.combat.range.DwarfMultiCannon;
import com.zarador.world.content.combat.weapon.CombatSpecial;
import com.zarador.world.content.logs.Logs;
import com.zarador.world.content.minigames.impl.Barrows;
import com.zarador.world.content.pos.PlayerOwnedShops;
import com.zarador.world.content.skill.SkillManager;
import com.zarador.world.content.skill.impl.farming.FarmingManager;
import com.zarador.world.content.skill.impl.hunter.Hunter;
import com.zarador.world.entity.impl.npc.NPC;

public class PlayerHandler {

	public static void displayCombatLevels(Player player) {
		int low_combat = player.getSkillManager().getCombatLevel() - 15;
		int highest_combat = player.getSkillManager().getCombatLevel() + 15;
		if(low_combat >= 3 && highest_combat <= 126) {
			player.getPacketSender().sendString(199, "@or2@" + low_combat + "-" + highest_combat + "");
			return;
		} else if(low_combat < 3) {
			player.getPacketSender().sendString(199, "@or2@3-" + highest_combat + "");
			return;
		} else if(highest_combat >= 127) {
			player.getPacketSender().sendString(199, "@or2@"+low_combat+"-126");
			return;
		}
	}

	public static void handleLogin(Player player) {
		// player.setLoginQue(false);
		player.setResponse(LoginResponses.LOGIN_SUCCESSFUL);
		// Register the player
		System.out.println("[World] Registering player - [username, host] : [" + player.getUsername() + ", "
				+ player.getHostAddress() + "]");
		ConnectionHandler.add(player.getHostAddress());
		World.getPlayers().add(player);
		CustomObjects.handleRegionChange(player);
		World.updatePlayersOnline();
		PlayersOnlineInterface.add(player);
		player.getSession().setState(SessionState.LOGGED_IN);
		// Packets
		player.getPacketSender().sendMapRegion().sendDetails();

		player.getRecordedLogin().reset();
		boolean maxed_out = true;
		for (int i = 0; i < Skill.values().length; i++) {
			if (i == 21)
				continue;
			if (player.getSkillManager().getMaxLevel(i) < (i == 3 || i == 5 ? 990 : 99)) {
				maxed_out = false;
			}
		}
		if (maxed_out) {
			player.setAnnounceMax(true);
		}
		// Tabs
		player.getPacketSender().sendTabs();
		// Setting up the player's item containers..
		for (int i = 0; i < player.getBanks().length; i++) {
			if (player.getBank(i) == null) {
				player.setBank(i, new Bank(player));
			}
		}
		if (player.isRequestAssistance()) {
			player.getPacketSender().sendConfig(427, 0);
		} else {
			player.getPacketSender().sendConfig(427, 1);
		}

		Titles.onLogin(player);

		player.getInventory().refreshItems();
		player.getEquipment().refreshItems();

		// Weapons and equipment..
		WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
		WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
		CombatSpecial.updateBar(player);
		BonusManager.update(player);

		player.getPacketSender().sendString(1, "[WITHDRAWX]-"+player.getWithdrawX());

		// Skills
		player.getSummoning().login();
		for (Skill skill : Skill.values()) {
			player.getSkillManager().updateSkill(skill);
		}
		player.getPacketSender().sendString(5067,
				"Friends List (" + player.getRelations().getFriendList().size() + "/200)");
		// Relations
		player.getRelations().setPrivateMessageId(1).onLogin(player).updateLists(true, 1);
		// Client configurations
		player.getPacketSender().sendConfig(172, player.isAutoRetaliate() ? 1 : 0)
				.sendTotalXp(player.getSkillManager().getTotalGainedExp())
				.sendConfig(player.getFightType().getParentId(), player.getFightType().getChildId()).sendRunStatus()
				.sendRunEnergy().sendConstitutionOrbPoison(player.isPoisoned())
				.sendConstitutionOrbVenom(player.isVenomed()).sendString(8135, "" + player.getMoneyInPouch())
				.sendInteractionOption("Follow", 3, false).sendInteractionOption("Trade with", 4, false)
				.sendInterfaceRemoval()
				.sendString(39161, "@or2@Server time: @or2@[ @yel@" + Misc.getCurrentServerTime() + "@or2@ ]");

		Autocasting.onLogin(player);
		PrayerHandler.deactivateAll(player);
		CurseHandler.deactivateAll(player);
		BonusManager.sendCurseBonuses(player);
		Achievements.updateInterface(player);
		Barrows.updateInterface(player);

		// Spellbook Teleports
		player.getPacketSender().sendString(13037, "Training Teleports");
		player.getPacketSender().sendString(13038, "Teleport to easy monsters.");
		player.getPacketSender().sendString(13047, "Skilling Areas");
		player.getPacketSender().sendString(13048, "Teleport to skilling locations.");
		player.getPacketSender().sendString(13055, "Boss Teleports");
		player.getPacketSender().sendString(13056, "Teleport to Bosses on Argos.");
		player.getPacketSender().sendString(13063, "Market (G.E.)");
		player.getPacketSender().sendString(13064, "Teleport to Grand Exchange");
		player.getPacketSender().sendString(13071, "Dungeons");
		player.getPacketSender().sendString(13072, "Teleport to dungeons.");
		player.getPacketSender().sendString(13081, "City Teleports");
		player.getPacketSender().sendString(13082, "Teleport to various citys.");
		player.getPacketSender().sendString(13089, "Minigames");
		player.getPacketSender().sendString(13090, "Teleport to minigames.");
		player.getPacketSender().sendString(13097, "Wilderness Areas");
		player.getPacketSender().sendString(13098, "Teleport to the wilderness.");

		player.getPacketSender().sendString(1300, "Training Teleports");
		player.getPacketSender().sendString(1301, "Teleport to easy monsters.");
		player.getPacketSender().sendString(1325, "Minigames");
		player.getPacketSender().sendString(1326, "Teleport to minigames.");
		player.getPacketSender().sendString(1350, "Wilderness Areas");
		player.getPacketSender().sendString(1351, "Teleport to the wilderness.");
		player.getPacketSender().sendString(1382, "City Teleports");
		player.getPacketSender().sendString(1383, "Teleport to various citys.");
		player.getPacketSender().sendString(1415, "Skilling Areas");
		player.getPacketSender().sendString(1416, "Teleport to skilling areas.");
		player.getPacketSender().sendString(1454, "Dungeons");
		player.getPacketSender().sendString(1455, "Teleport to dungeons.");
		player.getPacketSender().sendString(7457, "Boss Teleports");
		player.getPacketSender().sendString(7458, "Teleport to Bosses on Argos.");
		player.getPacketSender().sendString(18472, "Ape Atoll");

		if(player.getSkillManager().getMaxLevel(Skill.DUNGEONEERING) < 120) {
			if(player.getSkillManager().getExperience(Skill.DUNGEONEERING) == SkillManager.MAX_EXPERIENCE) {
				player.getSkillManager().setCurrentLevel(Skill.DUNGEONEERING, 120);
				player.getSkillManager().setMaxLevel(Skill.DUNGEONEERING, 120);
			}
		}

		String g = "";
		for (int i = 0; i < player.compColorsRGB.length; i++) {
			g += ""+player.compColorsRGB[i]+" ";
		}
		player.getPacketSender().sendString(g, 18712);

		// Tasks
		TaskManager.submit(new PlayerSkillsTask(player));
		if (player.isPoisoned()) {
			TaskManager.submit(new CombatPoisonEffect(player));
		}
		if (player.isVenomed()) {
			TaskManager.submit(new CombatVenomEffect(player));
		}
		if (player.getPrayerRenewalPotionTimer() > 0) {
			TaskManager.submit(new PrayerRenewalPotionTask(player));
		}
		if (player.getOverloadPotionTimer() > 0) {
			TaskManager.submit(new OverloadPotionTask(player));
		}
		if (player.getTeleblockTimer() > 0) {
			TaskManager.submit(new CombatTeleblockEffect(player));
		}
		if (player.getSkullTimer() > 0) {
			player.setSkullIcon(1);
			TaskManager.submit(new CombatSkullEffect(player));
		}
		if (player.getFireImmunity() > 0) {
			FireImmunityTask.makeImmune(player, player.getFireImmunity(), player.getFireDamageModifier());
		}
		if (player.getSpecialPercentage() < 100) {
			TaskManager.submit(new PlayerSpecialAmountTask(player));
		}
		if (player.hasStaffOfLightEffect()) {
			TaskManager.submit(new StaffOfLightSpecialAttackTask(player));
		}
		if (player.getMinutesBonusExp() >= 0) {
			TaskManager.submit(new BonusExperienceTask(player));
		}
		if (player.getPointsHandler().getPkPoints() < 0) {
			player.getPointsHandler().setPkPoints(0, false);
			System.out.println(
					"The user " + player.getUsername() + " logged in with negative PK Points, resetting to 0.");
		}
		if (player.getPointsHandler().getSlayerPoints() < 0) {
			player.getPointsHandler().setSlayerPoints(0, false);
			player.getPacketSender()
					.sendMessage("You have logged in with negative Slayer points, they have been set to 0.");
		}
		if (player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() == 6) {
			//Achievements.finishAchievement(player, AchievementData.DEFEAT_NOMAD);
			//Achievements.finishAchievement(player, AchievementData.DEFEAT_THE_CULINAROMANCER);
		}
		if(player.getSkillManager().hasAll99s()) {
			if (!player.hasAnnouncedMax()) {
				player.setAnnounceMax(true);
			}
		}
		// Update appearance
		player.getUpdateFlag().flag(Flag.APPEARANCE);

		// Loads login messages from Kotlin
		// new org.scripts.kotlin.core.login.LoginMessageParser();
		 //LoginMessageParser.LoginMessageParser.sendLogin(player);

		if(player.getDoubleXP() > 0) {
			player.getPacketSender().sendMessage("<col=C70000>You currently have "+player.getDoubleXP() / 60+" minutes of double XP left.");
		}
		// Loads Assetts
		// new org.scripts.kotlin.core.login.LoginLoaderAssetts();
		// LoginLoaderAssetts.LoginLoaderAssetts.loadAssetts(player);
		if (player.getPointsHandler().getAchievementPoints() > AchievementData.values().length) {
			player.getPointsHandler().setAchievementPoints(AchievementData.values().length, false);
		}
		Locations.login(player);
		player.getPacketSender().sendString(1, "[CLEAR]");
		ClanChatManager.handleLogin(player);
		PlayerPanel.refreshPanel(player);

		// New player
		if (player.newPlayer()) {
			player.setPasswordChange(GameSettings.PASSWORD_CHANGE);
			player.afterBeta = true;
			player.save();
			player.setPlayerLocked(true);
			player.moveTo(new Position(3230, 3232, 0));
			player.setNpcClickId(945);
			player.getDialog().sendDialog(new StartTutorial(player));
		} else {
			if (player.getPasswordChange() != GameSettings.PASSWORD_CHANGE) {
				player.setPlayerLocked(true);
				player.setPasswordChanging(true);
				player.setInputHandling(new ChangePassword());
				player.getPacketSender().sendEnterInputPrompt("Please enter a new password to set for your account:");
			} else {
				//TODO: Check if the player has an account pin, if not make him set one
			}
		}

		player.getPacketSender().updateSpecialAttackOrb().sendIronmanMode();

		if (player.getPointsHandler().getAchievementPoints() == 0) {
			Achievements.setPoints(player);
		}
		if (player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) == 0) {
			player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION,
					player.getSkillManager().getMaxLevel(Skill.CONSTITUTION));
		}
		Logs.log(player, "connections",
				new String[] {
						"[LOGIN]",
						"Ip: "+player.getHostAddress()+"",
						"Computer Address: "+player.getComputerAddress()+"",
						"Mac Address: "+player.getMacAddress()+"",
						"Serial Address: "+player.getSerialNumber()+""
				});
		if (player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin()
				&& player.getBankPinAttributes().onDifferent(player)) {
			BankPin.init(player, false);
		}
		player.getSkillManager().updateSkill(Skill.PRAYER);
		player.sendCompCapePresets();
		PlayerOwnedShops.collectCoinsOnLogin(player);
		NoteHandler.login(player);
		player.getPacketSender().sendRights();
		FarmingManager.uponLogin(player);
		player.getDungeoneering().addRingOfKinship();

		/**
		 * Fix double xp negative
		 */

		if(player.getDoubleXP() < 0) {
			player.setDoubleXP(60 * 60);
		}

		/**
		 * Give the beta testers their items
		 */
		if(!player.afterBeta) {
			boolean needsHat = true;
			//Give the hat
			for(Item t : player.getEquipment().getItems()) {
				if(t != null && t.getId() > 0) {
					if(t.getId() == 10547) {
						needsHat = false;
					}
				}
			}
			for(Item t : player.getInventory().getItems()) {
				if(t != null && t.getId() > 0) {
					if(t.getId() == 10547) {
						needsHat = false;
					}
				}
			}
			for (int i = 0; i < player.getBanks().length; i++) {
				if (player.getBank(i) != null) {
					for(Item t : player.getBank(i).getItems()) {
						if(t != null && t.getId() > 0) {
							if(t.getId() == 10547) {
								needsHat = false;
							}
						}
					}
				}
			}
			if(needsHat) {
				player.getBank(0).add(10547, 1);
			}
			//Give the potato
			boolean needsPotato = true;
			for(Item t : player.getEquipment().getItems()) {
				if(t != null && t.getId() > 0) {
					if(t.getId() == 5733) {
						needsPotato = false;
					}
				}
			}
			for(Item t : player.getInventory().getItems()) {
				if(t != null && t.getId() > 0) {
					if(t.getId() == 5733) {
						needsPotato = false;
					}
				}
			}
			for (int i = 0; i < player.getBanks().length; i++) {
				if (player.getBank(i) != null) {
					for(Item t : player.getBank(i).getItems()) {
						if(t != null && t.getId() > 0) {
							if(t.getId() == 5733) {
								needsPotato = false;
							}
						}
					}
				}
			}
			if(needsPotato) {
				player.getBank(0).add(5733, 1);
			}
			if(!player.gotBetaItems) {
				player.gotBetaItems = true;
				if(player.getGameModeAssistant().getGameMode() == GameMode.KNIGHT) {
					player.getBank(0).add(11188, 3);
				} else if(player.getGameModeAssistant().getGameMode() == GameMode.REALISM || player.getGameModeAssistant().getGameMode() == GameMode.IRONMAN) {
					player.getBank(0).add(11188, 1);
				}
				player.getPacketSender().sendMessage("<col=009E44><shad=0><icon=3>Thank you for being an awesome beta player!");
				player.getPacketSender().sendMessage("<col=009E44><shad=0><icon=3>Check your bank for beta rewards.");
			}
		}
	}

	public static boolean handleLogout(Player player) {
		try {
			PlayerSession session = player.getSession();

			if (session.getChannel().isOpen()) {
				session.getChannel().close();
			}

			if (!player.isRegistered()) {
				return true;
			}
			if (player.spawnedCerberus) {
				NPC n = new NPC(5866, new Position(1240, 1253, player.getPosition().getZ())).setSpawnedFor(player);
				World.deregister(n);
			}
			boolean exception = GameServer.isUpdating()
					|| World.getLogoutQueue().contains(player) && player.getLogoutTimer().elapsed(90000);
			if (player.logout() || exception) {
				System.out.println("[World] Deregistering player - [username, host] : [" + player.getUsername() + ", "
						+ player.getHostAddress() + "]");

				player.setLastLogin(System.currentTimeMillis());
				player.setLastIpAddress(player.getHostAddress());
				player.setLastSerialAddress(player.getSerialNumber());
				player.setLastMacAddress(player.getMacAddress());
				player.setLastComputerAddress(player.getComputerAddress());
				player.getSession().setState(SessionState.LOGGING_OUT);
				ConnectionHandler.remove(player.getHostAddress());
				player.setTotalPlayTime(player.getTotalPlayTime() + player.getRecordedLogin().elapsed());
				player.getPacketSender().sendInterfaceRemoval();
				if (player.getCannon() != null) {
					DwarfMultiCannon.pickupCannon(player, player.getCannon(), true);
				}
				if (exception && player.getResetPosition() != null) {
					player.moveTo(player.getResetPosition());
					player.setResetPosition(null);
				}
				if (player.getRegionInstance() != null) {
					player.getRegionInstance().destruct();
				}
				Hunter.handleLogout(player);
				Locations.logout(player);
				player.getSummoning().unsummon(false, false);
				BountyHunter.handleLogout(player);
				ClanChatManager.leave(player, false);
				player.getRelations().updateLists(false, 0);
				PlayersOnlineInterface.remove(player);
				TaskManager.cancelTasks(player.getCombatBuilder());
				TaskManager.cancelTasks(player);
				player.save();
				World.getPlayers().remove(player);
				session.setState(SessionState.LOGGED_OUT);
				World.updatePlayersOnline();
				player.setForumConnections(0);
				Logs.log(player, "connections",
						new String[] {
								"[LOGOUT]",
								"Ip: "+player.getHostAddress()+"",
								"Computer Address: "+player.getComputerAddress()+"",
								"Mac Address: "+player.getMacAddress()+"",
								"Serial Address: "+player.getSerialNumber()+""
						});
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
}
