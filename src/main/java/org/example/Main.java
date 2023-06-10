package org.example;
import org.example.configuration.SingletonConfiguration;


public class Main {

    public static void main(String[] args) {
        SingletonConfiguration singletonConfiguration = SingletonConfiguration.getInstance();
        new Bot(singletonConfiguration.getBotToken());
    }
}
