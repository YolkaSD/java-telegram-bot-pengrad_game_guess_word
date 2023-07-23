package org.example.statistics;
import com.pengrad.telegrambot.model.Update;
import org.example.statistics.model.PlayerStatsDTO;

import java.util.HashMap;
import java.util.Map;

public class PlayersStatsManagerImpl implements ManagerStatsInterface {
    private final Map<Long, PlayerStatsDTO> statsNodeHashMap = new HashMap<>();

    @Override
    public void incrementCountOfGuessedLetters(Update gameInput) {
        long userId = gameInput.message().from().id();
        PlayerStatsDTO playerStatsNode = statsNodeHashMap.computeIfAbsent(userId, key -> new PlayerStatsDTO(gameInput.message().from().username()));
        playerStatsNode.incrementCountOfGuessedLetters();
    }

    @Override
    public void incrementCountOfUnguessedLetters(Update gameInput) {
        long userId = gameInput.message().from().id();
        PlayerStatsDTO playerStatsNode = statsNodeHashMap.computeIfAbsent(userId, key -> new PlayerStatsDTO(gameInput.message().from().username()));
        playerStatsNode.incrementCountOfUnguessedLetters();
    }

    @Override
    public void incrementCountOfGuessedWholeWords(Update gameInput) {
        long userId = gameInput.message().from().id();
        PlayerStatsDTO playerStatsNode = statsNodeHashMap.computeIfAbsent(userId, key -> new PlayerStatsDTO(gameInput.message().from().username()));
        playerStatsNode.incrementCountOfGuessedWholeWords();
    }

    @Override
    public void incrementCountOfUnguessedWholeWords(Update gameInput) {
        long userId = gameInput.message().from().id();
        PlayerStatsDTO playerStatsNode = statsNodeHashMap.computeIfAbsent(userId, key -> new PlayerStatsDTO(gameInput.message().from().username()));
        playerStatsNode.incrementCountOfUnguessedWholeWords();
    }


    @Override
    public Map<Long, PlayerStatsDTO> getStatsNodeHashMap() {
        return statsNodeHashMap;
    }

}
