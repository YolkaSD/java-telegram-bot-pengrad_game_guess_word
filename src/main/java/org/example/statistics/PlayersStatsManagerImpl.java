package org.example.statistics;
import com.pengrad.telegrambot.model.Update;
import org.example.statistics.model.PlayerStatsNode;
import org.example.statistics.model.UserDTO;

import java.util.HashMap;
import java.util.Map;

public class PlayersStatsManagerImpl implements ManagerStatsInterface {
    private final Map<UserDTO, PlayerStatsNode> statsNodeHashMap = new HashMap<>();

    @Override
    public void incrementCountOfGuessedLetters(Update gameInput) {
        UserDTO userDTO = createUserDTO(gameInput);
        PlayerStatsNode playerStatsNode = statsNodeHashMap.computeIfAbsent(userDTO, key -> new PlayerStatsNode());
        playerStatsNode.incrementCountOfGuessedLetters();
    }

    @Override
    public void incrementCountOfUnguessedLetters(Update gameInput) {
        UserDTO userDTO = createUserDTO(gameInput);
        PlayerStatsNode playerStatsNode = statsNodeHashMap.computeIfAbsent(userDTO, key -> new PlayerStatsNode());
        playerStatsNode.incrementCountOfUnguessedLetters();
    }

    @Override
    public void incrementCountOfGuessedWholeWords(Update gameInput) {
        UserDTO userDTO = createUserDTO(gameInput);
        PlayerStatsNode playerStatsNode = statsNodeHashMap.computeIfAbsent(userDTO, key -> new PlayerStatsNode());
        playerStatsNode.incrementCountOfGuessedWholeWords();
    }

    @Override
    public void incrementCountOfUnguessedWholeWords(Update gameInput) {
        UserDTO userDTO = createUserDTO(gameInput);
        PlayerStatsNode playerStatsNode = statsNodeHashMap.computeIfAbsent(userDTO, key -> new PlayerStatsNode());
        playerStatsNode.incrementCountOfUnguessedWholeWords();
    }

    private UserDTO createUserDTO(Update gameInput) {
        long userId = gameInput.message().from().id();
        String username = gameInput.message().from().username();
        return new UserDTO(userId, username);
    }

    @Override
    public Map<UserDTO, PlayerStatsNode> getStatsNodeHashMap() {
        return statsNodeHashMap;
    }

}
