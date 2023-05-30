package org.example;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configuration {
    private String telegramBotToken;

    public Configuration(String filepath) {
        loadConfiguration(filepath);
    }

    private void loadConfiguration(String filepath) {
        File configFile = new File(filepath);
        if (!configFile.exists()) throw new RuntimeException("Configuration file does not exist: " + filepath);

        Properties properties = new Properties();

        try (FileInputStream fileInputStream = new FileInputStream(filepath)){
            properties.load(fileInputStream);
            telegramBotToken = properties.getProperty("telegram.bot.token");
            if (telegramBotToken == null) throw new RuntimeException("Telegram Bot Token is missing in the configuration file.");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration from file: " + filepath, e);
        }
    }

    public String getTelegramBotToken() {
        return telegramBotToken;
    }
}
