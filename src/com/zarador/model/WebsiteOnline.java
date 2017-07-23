package com.zarador.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.zarador.GameServer;
import com.zarador.net.mysql.SQLCallback;

public class WebsiteOnline {

	public static void updateOnline(int amountOnline) {
		GameServer.getServerPool().executeQuery("UPDATE `online` SET `amount`=" + amountOnline + " WHERE 1",
				new SQLCallback() {
					@Override
					public void queryComplete(ResultSet rs) throws SQLException {
						// Query is complete
					}

					@Override
					public void queryError(SQLException e) {
						e.printStackTrace();
					}
				});
	}
}