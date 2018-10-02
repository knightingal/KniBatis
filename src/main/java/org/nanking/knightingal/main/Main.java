package org.nanking.knightingal.main;

import org.nanking.knightingal.bean.Employee;
import org.nanking.knightingal.statement.Statement;

import java.sql.*;
import java.util.List;


public class Main {

    static final String USER = "knightingal";
    static final String PASSWORD = "303606";
    static final String DB_URL = "jdbc:mysql://127.0.0.1/employees";


    public static void main(String[] args) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            String sql =
                    "select " +
                            "emp_no as empNo, " +
                            "birth_date as birthDate, " +
                            "first_name as firstName, " +
                            "last_name as lastName, " +
                            "gender as gender, " +
                            "hire_date as hireDate " +
                    "from " +
                            "employees " +
                    "where " +
                            "emp_no < #{empNo} and gender = #{gender}";
            Employee param = new Employee();
            param.setEmpNo(10010);
            param.setGender("M");
            Statement statement = new Statement(sql
                    ,"org.nanking.knightingal.bean.Employee"
                    ,"org.nanking.knightingal.bean.Employee");
            List<Object> resultList = statement.query(conn, param);
            for (Object ret : resultList) {
                System.out.println(ret);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
