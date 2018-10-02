package org.nanking.knightingal.statement;

import org.nanking.knightingal.reflector.BeanWrapper;
import org.nanking.knightingal.reflector.Reflector;
import org.nanking.knightingal.typeHandler.DateHandler;
import org.nanking.knightingal.typeHandler.IntHandler;
import org.nanking.knightingal.typeHandler.StringHandler;
import org.nanking.knightingal.typeHandler.TypeHandler;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class Statement {

    private final static Map<Type, TypeHandler> TYPE_HANDLER_MAP = new HashMap<Type, TypeHandler>();
    static {
        TYPE_HANDLER_MAP.put(int.class, new IntHandler());
        TYPE_HANDLER_MAP.put(String.class, new StringHandler());
        TYPE_HANDLER_MAP.put(Date.class, new DateHandler());
    }

    private String sql;

    private Reflector paramReflector;

    private Reflector resultReflector;

    private Map<String, Type> paramMap = new HashMap<String, Type>();

    public Statement(String sql, String paramClassName, String resultClassName) throws ClassNotFoundException, NoSuchMethodException {
        this.sql = sql;
        this.paramReflector = new Reflector(Thread.currentThread().getContextClassLoader().loadClass(paramClassName));
        this.resultReflector = new Reflector(Thread.currentThread().getContextClassLoader().loadClass(resultClassName));
        parseSql();
    }

    private void parseSql() throws NoSuchMethodException {
        String openToken = "#{";
        String closeToken = "}";
        StringBuffer sb = new StringBuffer();
        int currIndex = 0;
        while (true) {
            int openIndex = sql.indexOf(openToken, currIndex);
            if (openIndex < 0) {
                sb.append(sql.substring(currIndex));
                break;
            }
            int closeIndex = sql.indexOf(closeToken, openIndex);
            if (closeIndex < 0) {
                sb.append(sql.substring(currIndex));
                break;
            }
            String paramName = sql.substring(openIndex + openToken.length(), closeIndex);
            Type paramType = paramReflector.getGetterType().get(paramName);
            paramMap.put(paramName, paramType);
            sb.append(sql.substring(currIndex, openIndex));
            sb.append("?");
            currIndex = closeIndex + closeToken.length();
        }
        this.sql = sb.toString();
    }

    public List<Object> query(Connection conn, Object param) throws Exception {
        PreparedStatement stmt = conn.prepareStatement(sql);
        BeanWrapper bw = new BeanWrapper(param);
        int i = 1;
        for (Map.Entry<String, Type> entry : paramMap.entrySet()) {
            TypeHandler typeHandler = TYPE_HANDLER_MAP.get(entry.getValue());
            Object value = bw.valueByName(entry.getKey());
            typeHandler.putValue(stmt, i, value);
        }
        List<Object> resultList = new ArrayList<Object>();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Object retObject = resultReflector.getConstructor().newInstance();
            Map<String, Method> setters = resultReflector.getSetterMethod();
            Map<String, Type> types = resultReflector.getSetterType();
            int columnCount = rs.getMetaData().getColumnCount();
            for (int j = 0; j < columnCount; j++) {
                String columnName = rs.getMetaData().getColumnLabel(j + 1);
                Method setter = setters.get(columnName);
                Type type = types.get(columnName);
                TypeHandler typeHandler = TYPE_HANDLER_MAP.get(type);
                Object value = typeHandler.valueFromRs(rs, columnName);
                setter.invoke(retObject, value);
            }

            resultList.add(retObject);
        }
        return resultList;
    }
}
