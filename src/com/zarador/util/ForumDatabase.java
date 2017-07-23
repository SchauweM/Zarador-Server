package com.zarador.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.zarador.GameServer;
import com.zarador.net.mysql.SQLCallback;
import com.zarador.world.entity.impl.player.Player;

/**
 * Initiates UPDATE and INSERT values towards the forum to update users, and
 * donator ranks based off in-game features.
 * 
 * @author Jonathan Sirens
 */

public class ForumDatabase {

	public static boolean forum_connection_happening = false;

	// Database connection information
	private static final String database_name = "ikov";
	private static final String mysql_address = "149.56.129.176";
	private static final String username = "ikov";
	public static String password = "null";
	static final String mysql_port = "3306";

	// Ranks
	public static int regular_donator = 11;
	public static int super_donator = 31;
	public static int extreme_donator = 30;
	public static int legendary_donator = 32;
	public static int uber_donator = 12;

	public static int members = 3;
	public static int validating = 1;
	public static int banned = 5;

	public static int wiki_editor = 34;

	public static void forumRankUpdate(Player player) {
		GameServer.getStorePool().executeQuery(
				"Select * from `members` WHERE `name` = '" + player.getUsername() + "' LIMIT 1", new SQLCallback() {
					@Override
					public void queryComplete(ResultSet rs) throws SQLException {
						if (rs.next()) {
							int donator_rank = player.getDonatorRights().ordinal();
							if (donator_rank == 1) {
								donator_rank = regular_donator;
							} else if (donator_rank == 2) {
								donator_rank = super_donator;
							} else if (donator_rank == 3) {
								donator_rank = extreme_donator;
							} else if (donator_rank == 4) {
								donator_rank = legendary_donator;
							} else if (donator_rank == 5) {
								donator_rank = uber_donator;
							} else if (donator_rank == 0) {
								donator_rank = members;
							}
							GameServer.getStorePool()
									.executeQuery(
											"UPDATE `members` SET `member_group_id` = '" + donator_rank
													+ "' WHERE `name` = '" + player.getUsername() + "' LIMIT 1",
											new SQLCallback() {
												@Override
												public void queryComplete(ResultSet rs) throws SQLException {

												}

												@Override
												public void queryError(SQLException e) {
													e.printStackTrace();
												}
											});
						} else {
							player.getPacketSender().sendMessage("A forum account has not been found under this name!");
						}
					}

					@Override
					public void queryError(SQLException e) {
						e.printStackTrace();
					}
				});
	}

}
