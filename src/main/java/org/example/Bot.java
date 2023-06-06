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
        long chatId = extractChatId(update);
        long messageId = extractMessageId(update);
        if (userText != null) {
            if (userText.equals("/start")) {
                sendResponse(chatId, "Bot activated");
            } else if (userText.equals("/hangman")) {
                guessWordGame = new Game("src/main/resources/words.json");
                sendResponse(chatId, "Игра началась. Я загадал слово - вы должны его отгадать!\n" + guessWordGame.getGuessedWord());
            } else if (guessWordGame != null) {
                String guessWord = guessWordGame.playGame(userText);
                String word = guessWordGame.getWord();

                if (guessWordGame.gameStatus()) {
                    if (!userText.equals("/hangman_stop")) {
                        if (userText.length() > 1) {
                            if (word.equals(userText.toUpperCase())) {
                                sendResponse(chatId, "Вы угадали целое слово:\n" + word, messageId);
                            } else {
                                sendResponse(chatId, "Увы, слово не верное", messageId);
                            }
                        } else {
                            if (word.contains(userText.toUpperCase())) {
                                sendResponse(chatId, "Вы угадали, такая буква есть в слове:\n" + guessWord, messageId);
                            } else {
                                sendResponse(chatId, "Увы, такой буквы нет", messageId);
                            }
                        }
                    } else {
                        guessWordGame = null;
                    }
                } else {
                    sendResponse(chatId, "Вы победили\n" + guessWord);
                    guessWordGame = null;
                }
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

    private void sendResponse(long chatId, String message) {
        telegramBot.execute(new SendMessage(chatId, message));
    }

    private void sendResponse(long chatId, String message, long messageId) {
        telegramBot.execute(new SendMessage(chatId, message).replyToMessageId((int) messageId));
    }

    private void hangman(String userText, long chatId, long messageId) {
        guessWordGame = new Game("src/main/resources/words.json");
        sendResponse(chatId, "Игра началась. Я загадал слово - вы должны его отгадать!\n" + guessWordGame.getGuessedWord());
        if (guessWordGame != null) {
            String guessWord = guessWordGame.playGame(userText);
            String word = guessWordGame.getWord();

            if (guessWordGame.gameStatus()) {
                if (!userText.equals("/hangman_stop")) {
                    if (userText.length() > 1) {
                        if (word.equals(userText.toUpperCase())) {
                            sendResponse(chatId, "Вы угадали целое слово:\n" + word);
                        } else {
                            sendResponse(chatId, "Увы, слово не верное");
                        }
                    } else {
                        if (word.contains(userText.toUpperCase())) {
                            sendResponse(chatId, "Вы угадали, такая буква есть в слове:\n" + guessWord);
                        } else {
                            sendResponse(chatId, "Увы, такой буквы нет");
                        }
                    }
                } else {
                    guessWordGame = null;
                }
            } else {
                sendResponse(chatId, "Вы победили\n" + guessWord, messageId);
                guessWordGame = null;
            }
        }
    }

}
