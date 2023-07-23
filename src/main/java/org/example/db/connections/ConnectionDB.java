package org.example.db.connections;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConnectionDB {

    public Connection connect() {
        Properties properties = new Properties();
        try (FileReader fileReader = new FileReader("src/main/resources/db.properties")){
            properties.load(fileReader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final String URL = properties.getProperty("url");
        final String USERNAME = properties.getProperty("username");
        final String PASSWORD = properties.getProperty("password");

        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
