package org.example;

import org.example.game.Game;
import org.example.game.WordGenerator;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        SingletonConfiguration singletonConfiguration = SingletonConfiguration.getInstance();
        new Bot(singletonConfiguration.getBotToken());
    }
}
