package org.example.game;

import com.pengrad.telegrambot.model.Update;

import java.util.Random;

public interface GameInterface {

    String getDescription();

    String getGivenWord();

    String tryToGuess(Update gameInput);

    default int setRandomListIndex(int start, int end) {
        Random random = new Random();
        if (start >= end) throw new IllegalArgumentException("Invalid range: start >= end");
        return  random.nextInt((end - start) + start);
    }

}
