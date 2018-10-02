package org.nanking.knightingal.typeHandler;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DateHandler implements TypeHandler {
    public void putValue(PreparedStatement stmt, int index, Object value) throws SQLException {
        stmt.setDate(index, (Date) value);
    }

    public Object valueFromRs(ResultSet rs, String columnName) throws SQLException {
        return rs.getDate(columnName);
    }
}
