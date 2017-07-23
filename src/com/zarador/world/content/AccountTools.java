package com.zarador.world.content;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.zarador.GameServer;
import com.zarador.GameSettings;
import com.zarador.model.DonatorRights;
import com.zarador.net.mysql.SQLCallback;
import com.zarador.world.World;
import com.zarador.world.entity.impl.player.Player;
import com.zarador.world.entity.impl.player.PlayerLoading;
import com.zarador.world.entity.impl.player.PlayerSaving;

public class AccountTools {

	public static Player scan(Player staff, String victimUsername, Player victim) {
		GameServer.getServerPool().executeQuery(
				"SELECT * FROM `accounts` WHERE username = '" + victimUsername + "' LIMIT 1", new SQLCallback() {
					@Override
					public void queryComplete(ResultSet rs) throws SQLException {
						if (rs.next()) {
							String json = rs.getString("json");
							PlayerLoading.decodeJson(victim, json);
							String serial = String.valueOf(victim.getLastMacAddress());
							if (serial != null && !serial.equals("not_set")) {
								outScan(staff, victimUsername, serial, victim);
							}
						}
					}

					@Override
					public void queryError(SQLException e) {
						e.printStackTrace();
					}
				});
		return victim;
	}

	public static Player outScan(Player staff, String victimUsername, String serial, Player victim) {
		staff.getPacketSender().sendMessage("Grabbing all accounts " + victimUsername + " has logged into...");
		GameServer.getServerPool().executeQuery(
				"SELECT * FROM `connections` WHERE `mac address` LIKE '" + serial + "'", new SQLCallback() {
					@Override
					public void queryComplete(ResultSet rs) throws SQLException {
						ArrayList<String> USERS = new ArrayList<String>();
						ArrayList<String> TIMES = new ArrayList<String>();
						while (rs.next()) {
							String username = rs.getString("username").toLowerCase();
							String time = rs.getString("time");
							if (!USERS.contains(username)) {
								USERS.add(username);
								TIMES.add(time);
							}
						}
						if(GameSettings.SPECIAL_PLAYERS.contains(victimUsername.toLowerCase())) {
							staff.getPacketSender().sendMessage("Player " + victimUsername
									+ " has logged into: <col=ff0000>" + victimUsername + "");
						} else {
							for (int i = 0; i < USERS.size(); i++) {
								if(GameSettings.SPECIAL_PLAYERS.contains(USERS.get(i))) {
									continue;
								}
								staff.getPacketSender().sendMessage("Player " + victimUsername
										+ " has logged into: <col=ff0000>" + USERS.get(i) + "");
							}
						}
					}

					@Override
					public void queryError(SQLException e) {
						e.printStackTrace();
					}
				});
		return victim;
	}

	public static Player checkPassword(Player staff, String victimUsername, Player victim) {
		if(!PlayerLoading.accountExists(victimUsername)) {
			staff.getPacketSender().sendMessage("The player "+victimUsername+" does not exist.");
			return null;
		}
		victim = new Player(null);
		PlayerLoading.loadJSON(victim);
		if (victim.getPassword() != null) {
			staff.getPacketSender().sendMessage(
					"The player " + victimUsername + "'s password is: " + victim.getPassword() + "");
		}
		return victim;
	}

	public static Player setPassword(Player staff, String victimUsername, String newPassword, Player victim) {
		if(!PlayerLoading.accountExists(victimUsername)) {
			staff.getPacketSender().sendMessage("The player "+victimUsername+" does not exist.");
			return null;
		}
		victim = new Player(null);
		PlayerLoading.loadJSON(victim);
		victim.setPassword(newPassword);
		if (victim.getPassword() != null) {
			staff.getPacketSender().sendMessage("The player " + victimUsername
					+ "'s password has been changed to: " + newPassword + "");
		}
		PlayerSaving.save(victim);
		return victim;
	}

	public static Player setDonator(Player staff, String victimUsername, DonatorRights rights, Player victim) {
		if(!PlayerLoading.accountExists(victimUsername)) {
			staff.getPacketSender().sendMessage("The player "+victimUsername+" does not exist.");
			return null;
		}
		victim = new Player(null);
		PlayerLoading.loadJSON(victim);
		victim.setDonatorRights(rights);
		if (victim.getDonatorRights() != null) {
			staff.getPacketSender().sendMessage("You have given the rank "+rights.getTitle()+" to "+victim.getUsername()+" while they were offline.");
		}
		PlayerSaving.save(victim);
		return victim;
	}

	public static Player checkPin(Player staff, String victimUsername, Player victim) {
		if(!PlayerLoading.accountExists(victimUsername)) {
			staff.getPacketSender().sendMessage("The player "+victimUsername+" does not exist.");
			return null;
		}
		victim = new Player(null);
		PlayerLoading.loadJSON(victim);
		if (victim.getBankPinAttributes().hasBankPin()) {
			StringBuilder builder = new StringBuilder();
			for (int s : victim.getBankPinAttributes().getBankPin()) {
				builder.append(s);
			}
			String pin = builder.toString();
			if (pin != null) {
				staff.getPacketSender().sendMessage(
						"The player " + victimUsername + "'s account pin is: " + pin + "");
			}
		} else {
			staff.getPacketSender().sendMessage(
					"The player " + victimUsername + " currently does not have an account pin.");
		}
		return victim;
	}

	public static Player resetPin(Player staff, String victimUsername, Player victim) {
		if(!PlayerLoading.accountExists(victimUsername)) {
			staff.getPacketSender().sendMessage("The player "+victimUsername+" does not exist.");
			return null;
		}
		victim = new Player(null);
		PlayerLoading.loadJSON(victim);
		if (victim.getBankPinAttributes().hasBankPin()) {
			for (int i = 0; i < victim.getBankPinAttributes().getBankPin().length; i++) {
				victim.getBankPinAttributes().getBankPin()[i] = 0;
				victim.getBankPinAttributes().getEnteredBankPin()[i] = 0;
			}
			victim.getBankPinAttributes().setHasBankPin(false);
			staff.getPacketSender().sendMessage(
					"The player " + victimUsername + "'s account pin has been reset.");

		} else {
			staff.getPacketSender().sendMessage("The player " + victimUsername
					+ " currently does not have an account pin.");
		}
		PlayerSaving.save(victim);
		return victim;
	}
}
