package com.pigier.pigieretudiant.config;

import com.pigier.pigieretudiant.utils.InfoSensible;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    static InfoSensible infoSensible;
    private static final String URL = "jdbc:mysql://localhost:3306/pigier_db";
    private static final String USER = InfoSensible.userNameServer;
    private static final String PASSWORD = InfoSensible.userPasswordServer;

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
