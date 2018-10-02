package org.nanking.knightingal.typeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface TypeHandler {
    void putValue(PreparedStatement stmt, int index, Object value) throws SQLException;

    Object valueFromRs(ResultSet rs, String columnName) throws SQLException;
}
