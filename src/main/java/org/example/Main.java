package org.example;
import org.example.configuration.SingletonConfiguration;
import org.example.game.GameImpl;


public class Main {

    public static void main(String[] args) {
        SingletonConfiguration singletonConfiguration = SingletonConfiguration.getInstance();
        new Bot(singletonConfiguration.getBotToken());
        GameImpl gameNew = new GameImpl();
    }
}
