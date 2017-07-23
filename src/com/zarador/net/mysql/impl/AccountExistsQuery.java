package com.zarador.net.mysql.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.zarador.GameServer;
import com.zarador.net.mysql.CompletedCallback;
import com.zarador.net.mysql.SQLCallback;

public class AccountExistsQuery implements SQLCallback {

	private static final String PREPARED_QUERY = "SELECT username FROM `accounts` as acc WHERE LOWER (`username`) = LOWER('?') LIMIT 1";
	private final String query;
	private CompletedCallback completedCallback;

	public AccountExistsQuery(String name) {
		this.query = PREPARED_QUERY.replace("?", name);
	}

	public AccountExistsQuery execute() {
		GameServer.getServerPool().executeQuery(this.query, this);
		return this;
	}

	public AccountExistsQuery setCompletedCallback(CompletedCallback completedCallback) {
		this.completedCallback = completedCallback;
		return this;
	}

	@Override
	public void queryComplete(ResultSet result) throws SQLException {
		this.completedCallback.onCompletion(result);
	}

	@Override
	public void queryError(SQLException e) {
		e.printStackTrace();
	}

}
