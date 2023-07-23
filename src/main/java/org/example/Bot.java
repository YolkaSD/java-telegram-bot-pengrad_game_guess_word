package org.example;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.example.game.GameImpl;
import java.util.HashMap;
import java.util.Map;

public class Bot {
    private static final String START = "/start";
    private static final String HANGMAN = "/hangman";
    private static final String HANGMAN_STOP = "/hangman_stop";

    private final TelegramBot telegramBot;

    private Map<Long, GameImpl> gameMap = new HashMap<>();

    public Bot(String token) {
        telegramBot = new TelegramBot(token);

        telegramBot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                new Thread(() -> handleUpdate(update)).start();
            }

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void handleUpdate(Update update) {
        String inputMessage = extractInputMessage(update);
        long chatId = update.message().chat().id();
        if (inputMessage != null) {
            switch (inputMessage) {
                case START:
                    sendResponse(update, "Bot activated");
                    break;
                case HANGMAN:
                    gameMap.put(chatId, new GameImpl());
                    sendResponse(update, "Игра началась. Я загадал слово - вы должны его отгадать!\n"
                            + gameMap.get(chatId).getDescription()
                            + "\n" + gameMap.get(chatId).getGivenWord());
                    break;
                case HANGMAN_STOP:
                    if (gameMap.get(chatId) != null) {
                        sendResponse(update, "Игра преждевременно завершена!");
                        gameMap.remove(chatId);
                    }
                    break;
                default:
                    if (gameMap.get(chatId) != null) {
                        if (gameMap.get(chatId).getStatus()) {
                            hangman(update);
                        } else {
                            gameMap.remove(chatId);
                        }
                    }
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
        sendReplyResponse(update, gameMap.get(update.message().chat().id()).tryToGuess(update));
    }

}
