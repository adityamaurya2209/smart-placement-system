package com.smartplacement.util;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();

    private static String getConfig(String key, String defaultValue) {
        String systemValue = System.getenv(key);

        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue;
        }

        String dotenvValue = dotenv.get(key);

        if (dotenvValue != null && !dotenvValue.isBlank()) {
            return dotenvValue;
        }

        return defaultValue;
    }

    private static final String URL = getConfig(
            "DB_URL",
            "jdbc:mysql://localhost:3306/smart_placement"
    );

    private static final String USER = getConfig(
            "DB_USER",
            "root"
    );

    private static final String PASSWORD = getConfig(
            "DB_PASSWORD",
            null
    );

    public static Connection getConnection() throws SQLException {

        if (PASSWORD == null || PASSWORD.isBlank()) {
            throw new SQLException("DB_PASSWORD is not configured.");
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                    "MySQL JDBC Driver not found.",
                    e
            );
        }

        return DriverManager.getConnection(
                URL,
                USER,
                PASSWORD
        );
    }
}
