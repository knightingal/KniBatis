package org.nanking.knightingal.main;

import org.nanking.knightingal.bean.Employee;

import java.sql.*;

public class Main {

    static final String USER = "knightingal";
    static final String PASSWORD = "";
    static final String DB_URL = "jdbc:mysql://127.0.0.1/employees";


    public static void main(String[] args) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            String sql = "select emp_no, birth_date, first_name, last_name, gender, hire_date from employees where emp_no < ?";
            Employee param = new Employee();
            param.setEmpNo(10010);
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, param.getEmpNo());
            rs = stmt.executeQuery();
            while (rs.next()) {
                Employee employee = new Employee(
                        rs.getInt(1),
                        rs.getDate(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getDate(6)
                );
                System.out.println(employee.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
