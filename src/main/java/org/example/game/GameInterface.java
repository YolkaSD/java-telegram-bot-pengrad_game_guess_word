package org.example.game;

import java.util.Random;

public interface GameInterface {
    int getId();

    String getWord();

    String getDescription();

    String playGame(String letter);

    String getGivenWord();

    boolean gameStatus();
    default int setRandomListIndex(int start, int end) {
        Random random = new Random();
        if (start >= end) throw new IllegalArgumentException("Invalid range: start >= end");
        return  random.nextInt((end - start) + start);
    }

}
