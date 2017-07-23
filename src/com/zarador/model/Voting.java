package com.zarador.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.zarador.GameServer;
import com.zarador.GameSettings;
import com.zarador.net.mysql.SQLCallback;
import com.zarador.world.entity.impl.player.Player;

public class Voting {

	public static void useAuth(Player player, String a) {
		final String auth = a.replaceAll("[\"\\\'/]", "");
		if (auth.length() > 10) {
			player.getPacketSender().sendMessage("Your auth code can only be under 10 characters.");
			return;
		}
		GameServer.getVotingPool().executeQuery("Select * from `auth` WHERE `auth` = '" + auth + "'",
				new SQLCallback() {
					@Override
					public void queryComplete(ResultSet rs) throws SQLException {
						if (rs == null) {
							player.getPacketSender().sendMessage("You have entered an invalid auth code.");
							return;
						}
						boolean hasGrabbed = false;
						while (rs.next()) {
							hasGrabbed = true;
							if (GameSettings.DOUBLE_VOTE_TOKENS) {
								player.getInventory().add(10944, 2);
							} else if (GameSettings.TRIPLE_VOTE_TOKENS) {
								player.getInventory().add(10944, 3);
							} else {
								player.getInventory().add(10944, 1);
							}
							player.getPacketSender().sendMessage("Your auth code has been claimed!");
							player.save();
						}
						if (!hasGrabbed) {
							player.getPacketSender().sendMessage("You have entered an invalid auth code.");
							return;
						}
						GameServer.getVotingPool().executeQuery("DELETE FROM `auth` WHERE `auth` = '" + auth + "'",
								new SQLCallback() {
									@Override
									public void queryComplete(ResultSet rs) throws SQLException {
										// Query complete
									}

									@Override
									public void queryError(SQLException e) {
										e.printStackTrace();
									}
								});
					}

					@Override
					public void queryError(SQLException e) {
						e.printStackTrace();
					}
				});
	}
}