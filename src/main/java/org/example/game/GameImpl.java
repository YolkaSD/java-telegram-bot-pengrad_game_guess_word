package org.example.game;

import com.pengrad.telegrambot.model.Update;
import org.example.QuestionDTO;
import org.example.configuration.Configuration;
import org.example.statistics.ManagerStatsInterface;
import org.example.statistics.PlayerStatsNode;
import org.example.statistics.PlayersStatsManagerImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GameImpl implements GameInterface {

    private static final List<QuestionDTO> questionsDTOList = Configuration.readLinesFromJson();

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

    private String getStatisticsReport(Map<String, PlayerStatsNode> statsNodeHashMap, Update update) {
        StringBuilder statsMessage = new StringBuilder();
        for (String key: statsNodeHashMap.keySet()) {
            PlayerStatsNode node = statsNodeHashMap.get(key);
            statsMessage
                    .append("Gamer ").append(key).append(":\n")
                    .append("- Count of guessed letters: ").append(node.getCountOfGuessedLetters()).append("\n")
                    .append("- Count of unguessed letters: ").append(node.getCountOfUnguessedLetters()).append("\n")
                    .append("- Count of guessed whole words: ").append(node.getCountOfGuessedWholeWords()).append("\n")
                    .append("- Count of unguessed whole words: ").append(node.getCountOfUnguessedWholeWords()).append("\n")
                    .append("- Ratio of successful attempts to all attempts (percentage): ").append(node.getRatioOfSuccessfulAttemptsToAllAttempts()).append("%\n");
        }
        return statsMessage.toString();
    }

    private String getMessageWinner(Update gameInput) {
        String stats = getStatisticsReport(playersStatsManager.getStatsNodeHashMap(), gameInput);
        return "Вы угадали слово:\n" + word + "\n\n" + gameInput.message().from().username() + " победил!\n\n" + stats;
    }
}
