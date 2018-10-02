package org.nanking.knightingal.typeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StringHandler implements TypeHandler {
    public void putValue(PreparedStatement stmt, int index, Object value) throws SQLException {
        stmt.setString(index, (String) value);
    }

    public Object valueFromRs(ResultSet rs, String columnName) throws SQLException {
        return rs.getString(columnName);
    }
}
