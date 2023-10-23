package com.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.config.Config;

public class Database {

    // init connection object
    private static Connection connection;
    // init properties object
    private Properties properties;

    // connect database
    public Connection connect() {
        if (connection == null) {
            try {
                Class.forName(Config.DATABASE_DRIVER);
                connection = DriverManager.getConnection(Config.DATABASE_URL, getProperties());
            } catch (ClassNotFoundException | SQLException e) {
                // Java 7+
                e.printStackTrace();
            }
        }
        return connection;
    }

    // disconnect database
    public void disconnect() {
        if (connection != null) {
            try {
                Config.statement.close();
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // create properties
    private Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            properties.setProperty("user", Config.USERNAME);
            properties.setProperty("password", Config.PASSWORD);
            properties.setProperty("MaxPooledStatements", Config.MAX_POOL);
        }
        return properties;
    }

}
