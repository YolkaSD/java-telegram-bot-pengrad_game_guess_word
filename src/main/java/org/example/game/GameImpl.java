package org.example.game;

import com.pengrad.telegrambot.model.Update;
import org.example.db.connections.ConnectionDB;
import org.example.db.dao.PlayerStatsDAOImpl;
import org.example.game.model.QuestionDTO;
import org.example.configuration.Configuration;
import org.example.statistics.ManagerStatsInterface;
import org.example.statistics.model.PlayerStatsDTO;
import org.example.statistics.PlayersStatsManagerImpl;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GameImpl implements GameInterface {

    private static final List<QuestionDTO> questionsDTOList = Configuration.readLinesFromJson();
    private static final Connection connectionDB = new ConnectionDB().connect();

    private final ManagerStatsInterface playersStatsManager = new PlayersStatsManagerImpl();

    private String word;
    private String givenWord;
    private String description;
    private boolean gameStatus;
    private String[] lettersInWord;
    private String[] lettersInGivenWord;

    public GameImpl() {
        init();
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getGivenWord() {
        return givenWord;
    }

    @Override
    public String tryToGuess(Update gameInput) {
        String inputMessage = gameInput.message().text();
        String guessWord = this.playGame(inputMessage);
        inputMessage = inputMessage.toUpperCase();


        if (inputMessage.length() > 1) {
            if (word.equals(inputMessage)) {
                gameStatus = false;
                playersStatsManager.incrementCountOfGuessedWholeWords(gameInput);
                return getMessageWinner(gameInput);
            } else {
                playersStatsManager.incrementCountOfUnguessedWholeWords(gameInput);
                return "Увы, слово не верное";
            }
        } else {
            if (word.contains(inputMessage.toUpperCase())) {
                String message = "Вы угадали, такая буква есть в слове:\n" + guessWord;
                playersStatsManager.incrementCountOfGuessedLetters(gameInput);
                if (!gameStatus) {
                    message = "\n\n" + getMessageWinner(gameInput);
                    gameStatus = false;
                }
                return message;
            } else {
                playersStatsManager.incrementCountOfUnguessedLetters(gameInput);
                return "Увы, такой буквы нет";
            }
        }
    }

    public boolean getStatus(){
        return gameStatus;
    }


    private String playGame(String letter) {
        letter = letter.toUpperCase();
        if (letter.length() > 1) {
            if (word.equals(letter)) {
                givenWord = letter;
            }
        } else {
            for (int i = 0; i < lettersInWord.length; i++) {
                if (lettersInWord[i].equals(letter)) {
                    lettersInGivenWord[i] = letter;
                }
            }
            givenWord = String.join("", lettersInGivenWord);
        }
        gameStatus = givenWord.contains("*");
        return givenWord;
    }

    private void init() {
        gameStatus = true;
        int index = setRandomListIndex(0, questionsDTOList.size());
        this.word = questionsDTOList.get(index).getWord().toUpperCase();
        System.out.println(word);
        this.description = questionsDTOList.get(index).getDescriptions();
        setGivenWord(word);
        lettersInWord = word.split("");
        lettersInGivenWord = givenWord.split("");
    }

    private void setGivenWord(String word) {
        givenWord = IntStream.range(0, word.length()).mapToObj(i -> {
            String[] letters = word.split("");
            if (!letters[i].equals(" ")) {
                return "*";
            } else {
                return " ";
            }
        }).collect(Collectors.joining());
    }

    private String getStatisticsReport(Map<Long, PlayerStatsDTO> statsNodeHashMap) {
        StringBuilder statsMessage = new StringBuilder("\nСтатистика игроков \n");
        for (Long key: statsNodeHashMap.keySet()) {
            PlayerStatsDTO node = statsNodeHashMap.get(key);
            statsMessage
                    .append("Игрок ").append(node.getUserName()).append(":\n")
                    .append("- Угаданных букв: ").append(node.getCountOfGuessedLetters()).append("\n")
                    .append("- Не угаданных букв: ").append(node.getCountOfUnguessedLetters()).append("\n")
                    .append("- Угаданных слов: ").append(node.getCountOfGuessedWholeWords()).append("\n")
                    .append("- Не угаданных слов: ").append(node.getCountOfUnguessedWholeWords()).append("\n")
                    .append("- Процент удач/недач: ").append(node.getRatioOfSuccessfulAttemptsToAllAttempts()).append("%\n\n");
        }
        return statsMessage.toString();
    }

    private String getMessageWinner(Update gameInput) {

        PlayerStatsDAOImpl dao = new PlayerStatsDAOImpl(connectionDB);
        playersStatsManager.getStatsNodeHashMap().forEach((userId, playerStatsNode) -> {
            if (dao.userExists(userId)) {
                PlayerStatsDTO oldStat = dao.getStatisticsFromDB(userId);
                playerStatsNode.setCountOfUnguessedLetters(playerStatsNode.getCountOfUnguessedLetters() + oldStat.getCountOfUnguessedLetters());
                playerStatsNode.setCountOfGuessedLetters(playerStatsNode.getCountOfGuessedLetters() + oldStat.getCountOfGuessedLetters());
                playerStatsNode.setCountOfGuessedWholeWords(playerStatsNode.getCountOfGuessedWholeWords() + oldStat.getCountOfGuessedWholeWords());
                playerStatsNode.setCountOfUnguessedWholeWords(playerStatsNode.getCountOfUnguessedWholeWords() + oldStat.getCountOfUnguessedWholeWords());
                playerStatsNode.updateRatioOfSuccessfulAttemptsToAllAttempts();
                dao.updateStatistics(userId, playerStatsNode);

            } else {
                dao.insert(userId, playerStatsNode);
            }
        });
        String stats = getStatisticsReport(playersStatsManager.getStatsNodeHashMap());
        return "Игрок " + gameInput.message().from().username() + " победил!\n" + stats + "\n";
    }

    @Override
    public String toString() {
        return "GameImpl{" +
                "word='" + word + '\'' +
                '}';
    }
}
