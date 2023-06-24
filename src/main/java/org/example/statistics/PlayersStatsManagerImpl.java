package org.example.statistics;
import com.pengrad.telegrambot.model.Update;
import java.util.HashMap;
import java.util.Map;

public class PlayersStatsManagerImpl implements ManagerStatsInterface {
    private static final Map<String, PlayerStatsNode> statsNodeHashMap = new HashMap<>();

    @Override
    public void incrementCountOfGuessedLetters(Update gameInput) {
        String username = gameInput.message().from().username();
        PlayerStatsNode playerStatsNode = statsNodeHashMap.computeIfAbsent(username, key -> new PlayerStatsNode());
        playerStatsNode.incrementCountOfGuessedLetters();
    }

    @Override
    public void incrementCountOfUnguessedLetters(Update gameInput) {
        String username = gameInput.message().from().username();
        PlayerStatsNode playerStatsNode = statsNodeHashMap.computeIfAbsent(username, key -> new PlayerStatsNode());
        playerStatsNode.incrementCountOfUnguessedLetters();
    }

    @Override
    public void incrementCountOfGuessedWholeWords(Update gameInput) {
        String username = gameInput.message().from().username();
        PlayerStatsNode playerStatsNode = statsNodeHashMap.computeIfAbsent(username, key -> new PlayerStatsNode());
        playerStatsNode.incrementCountOfGuessedWholeWords();
    }

    @Override
    public void incrementCountOfUnguessedWholeWords(Update gameInput) {
        String username = gameInput.message().from().username();
        PlayerStatsNode playerStatsNode = statsNodeHashMap.computeIfAbsent(username, key -> new PlayerStatsNode());
        playerStatsNode.incrementCountOfUnguessedWholeWords();
    }

    @Override
    public Map<String, PlayerStatsNode> getStatsNodeHashMap() {
        return statsNodeHashMap;
    }

}
