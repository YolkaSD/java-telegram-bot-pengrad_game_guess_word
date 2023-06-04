package org.example;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class SingletonConfiguration {
    private File configFile;
    private final String botToken;
    private SingletonConfiguration() throws IOException {
        this.configFile = getFile();
        ObjectMapper mapper = new ObjectMapper();
        Config config = mapper.readValue(this.configFile, new TypeReference<>() {});
        botToken = config.getBotToken();
    }

    private static final class ConfigHolder{
        private final static SingletonConfiguration instance;

        static {
            try {
                instance = new SingletonConfiguration();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private File getFile() {
        this.configFile = new File("src/main/resources/config.json");
        if (!configFile.exists()) {
            throw new RuntimeException("Config file not found");
        }
        return configFile;
    }

    public String getBotToken() {
        return botToken;
    }

    public static SingletonConfiguration getInstance() {
        return ConfigHolder.instance;
    }
    private static class Config {
        private String botToken;

        public Config() {

        }

        public String getBotToken() {
            return botToken;
        }

        public void setBotToken(String botToken) {
            this.botToken = botToken;
        }
    }
}