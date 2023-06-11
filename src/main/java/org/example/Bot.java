package org.example;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.example.game.GameImpl;

public class Bot {
    private static final String START = "/start";
    private static final String HANGMAN = "/hangman";
    private static final String HANGMAN_STOP = "/hangman_stop";

    private final TelegramBot telegramBot;
    private GameImpl guessWordGame;

    public Bot(String token) {
        telegramBot = new TelegramBot(token);

        telegramBot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                handleUpdate(update);
            }

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void handleUpdate(Update update) {
        String inputMessage = extractInputMessage(update);
        if (inputMessage != null) {
            switch (inputMessage) {
                case START:
                    sendResponse(update, "Bot activated");
                    break;
                case HANGMAN:
                    guessWordGame = new GameImpl();
                    sendResponse(update, "Игра началась. Я загадал слово - вы должны его отгадать!\n"
                            + guessWordGame.getDescription()
                            + "\n" + guessWordGame.getGivenWord());
                    break;
                case HANGMAN_STOP:
                    if (guessWordGame != null) {
                        sendResponse(update, "Игра преждевременно завершена!");
                        guessWordGame = null;
                    }
                    break;
                default:
                    if (guessWordGame != null) hangman(update);
            }
        }
    }

    private String extractInputMessage(Update update) {
        if (update.message() != null) {
            return update.message().text();
        }
        return null;
    }

    private long extractChatId(Update update) {
        if (update.message() != null && update.message().chat() != null) {
            return update.message().chat().id();
        }
        return 0;
    }

    private long extractMessageId(Update update) {
        if (update.message() != null && update.message().chat() != null) {
            return update.message().messageId();
        }
        return 0;
    }

    private void sendResponse(Update update, String message) {
        long chatId = extractChatId(update);
        telegramBot.execute(new SendMessage(chatId, message));
    }

    private void sendReplyResponse(Update update, String message) {
        long chatId = extractChatId(update);
        long messageId = extractMessageId(update);
        telegramBot.execute(new SendMessage(chatId, message).replyToMessageId((int) messageId));
    }

    private void hangman(Update update) {
        sendReplyResponse(update, guessWordGame.tryToGuess(update));
    }

}
