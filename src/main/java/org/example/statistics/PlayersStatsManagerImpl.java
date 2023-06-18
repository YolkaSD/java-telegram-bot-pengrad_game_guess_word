package org.example.statistics;
import com.pengrad.telegrambot.model.Update;
import java.util.HashMap;
import java.util.Map;

public class PlayersStatsManagerImpl implements ManagerStatsInterface {
    private static final Map<Long, PlayerStatsNode> statsNodeHashMap = new HashMap<>();

    @Override
    public void incrementCountOfGuessedLetters(Update gameInput) {
        long userId = gameInput.message().from().id();
        PlayerStatsNode playerStatsNode = statsNodeHashMap.computeIfAbsent(userId, key -> new PlayerStatsNode());
        playerStatsNode.incrementCountOfGuessedLetters();
    }

    @Override
    public void incrementCountOfUnguessedLetters(Update gameInput) {
        long userId = gameInput.message().from().id();
        PlayerStatsNode playerStatsNode = statsNodeHashMap.computeIfAbsent(userId, key -> new PlayerStatsNode());
        playerStatsNode.incrementCountOfUnguessedLetters();
    }

    @Override
    public void incrementCountOfGuessedWholeWords(Update gameInput) {
        long userId = gameInput.message().from().id();
        PlayerStatsNode playerStatsNode = statsNodeHashMap.computeIfAbsent(userId, key -> new PlayerStatsNode());
        playerStatsNode.incrementCountOfGuessedWholeWords();
    }

    @Override
    public void incrementCountOfUnguessedWholeWords(Update gameInput) {
        long userId = gameInput.message().from().id();
        PlayerStatsNode playerStatsNode = statsNodeHashMap.computeIfAbsent(userId, key -> new PlayerStatsNode());
        playerStatsNode.incrementCountOfUnguessedWholeWords();
    }

    public Map<Long, PlayerStatsNode> getStatsNodeHashMap() {
        return statsNodeHashMap;
    }

}
