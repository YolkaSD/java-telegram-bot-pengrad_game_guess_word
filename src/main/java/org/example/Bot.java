package org.example;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class Bot {
    private final TelegramBot telegramBot;

    public Bot(String token) {
        telegramBot = new TelegramBot(token);

        telegramBot.setUpdatesListener(updates -> {
            for (Update update: updates) {
                handleUpdate(update);
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void handleUpdate(Update update) {
        String userText = extractUserText(update);
        long chatId = extractChatId(update);
        if (userText != null) {
            switch (userText) {
                case "/start":
                    sendResponse(chatId, "Bot activated");
                    break;
                case "hi":
                    sendResponse(chatId, "Hello!");
                    break;
                case "bye":
                    sendResponse(chatId, "Goodbye!");
                    break;
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

    private void sendResponse(long chatId, String message) {
        telegramBot.execute(new SendMessage(chatId, message));
    }

}
