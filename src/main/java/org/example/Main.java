package org.example;

public class Main {

    public static void main(String[] args) {
        String botToken = args[0];
        if (botToken == null) {
            throw new IllegalArgumentException("Bot token doesn't exist!");
        }
        new Bot(botToken);
    }
}
