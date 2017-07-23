package com.zarador.net.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface CompletedCallback {

	void onCompletion(ResultSet rs) throws SQLException;

}
