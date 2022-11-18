package util;

import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static final String DBURL = "jdbc:mysql://localhost:3306/coursejdbc";

    public static java.sql.Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DBURL, USER, PASSWORD);
    }
}