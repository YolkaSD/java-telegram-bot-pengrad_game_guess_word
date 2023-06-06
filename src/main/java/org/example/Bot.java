package org.example;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.example.game.Game;

public class Bot {
    private final TelegramBot telegramBot;
    Game guessWordGame;

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
        String userText = extractUserText(update);
        if (userText != null) {
            switch (userText) {
                case "/start" :
                    sendResponse(update, "Bot activated");
                    break;
                case "/hangman" :
                    guessWordGame = new Game("src/main/resources/words.json");
                    sendResponse(update, "Игра началась. Я загадал слово - вы должны его отгадать!\n" + guessWordGame.getGuessedWord());
                    break;
                case "/hangman_stop" :
                    if (guessWordGame != null) guessWordGame = null;
                    break;
                default:
                    if (guessWordGame != null) hangman(update, userText);
            }
        }
    }

    private String extractUserText(Update update) {
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

    private void hangman(Update update, String userText) {
        if (userText.equals("/hangman_stop")) {
            guessWordGame = null;
        } else {
            String guessWord = guessWordGame.playGame(userText);
            String word = guessWordGame.getWord();
            userText = userText.toUpperCase();
            if (userText.length() > 1) {
                if (word.equals(userText)) {
                    sendReplyResponse(update, "Вы угадали целое слово:\n" + word);
                } else {
                    sendReplyResponse(update, "Увы, слово не верное");
                }
            } else {
                if (word.contains(userText.toUpperCase())) {
                    sendReplyResponse(update, "Вы угадали, такая буква есть в слове:\n" + guessWord);
                } else {
                    sendReplyResponse(update, "Увы, такой буквы нет");
                }
            }
            if (!guessWordGame.gameStatus()) {
                sendReplyResponse(update, "Вы победили\n");
                guessWordGame = null;
            }
        }
    }

}
