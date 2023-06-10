package org.example.game;

import org.example.QuestionDTO;
import org.example.questionlist.QuestionDTOListImpl;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GameImpl implements GameInterface{
    private static final List<QuestionDTO> questionsDTOList = QuestionDTOListImpl.getInstance().readLinesFromJson("src/main/resources/wordsAndDescriptions.json");
    private int id;
    private String word;
    private String givenWord;
    private String description;
    private boolean gameStatus;
    private String[] lettersInWord;
    private String[] lettersInGivenWord;

    public GameImpl() {
        gameStatus = true;
        int index = setRandomListIndex(0, questionsDTOList.size());
        setId(index);
        setWord(index);
        setDescription(index);
        setGivenWord(word);
        setLettersInWord();
        setLettersInGivenWords();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getWord() {
        return word;
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
    public boolean gameStatus() {
        return gameStatus;
    }

    @Override
    public String playGame(String letter) {
        letter = letter.toUpperCase();
        if (letter.length() > 1) {
            if (word.equals(letter)) givenWord = letter;
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

    private void setId(int index) {
        this.id = questionsDTOList.get(index).getId();
    }

    private void setWord(int index) {
        this.word = questionsDTOList.get(index).getWord().toUpperCase();
    }

    private void setDescription(int index) {
        this.description = questionsDTOList.get(index).getDescriptions();
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

    private void setLettersInWord() {
        lettersInWord = word.split("");
    }

    private void setLettersInGivenWords() {
        lettersInGivenWord = givenWord.split("");
    }


}
