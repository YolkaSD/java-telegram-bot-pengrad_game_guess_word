package org.example.game;
import org.example.questiongenerate.WordGeneratorOldCode;

public class GameOldCode {
    private final String word;
    private String guessedWord;
    private boolean inGame;
    private final String[] letterInWord;
    private final String[] letterInGuessedWord;

    public GameOldCode(String pathToWordsFile) {
        word = new WordGeneratorOldCode(pathToWordsFile).getWord().toUpperCase();
        guessedWord = "*".repeat(word.length());
        letterInWord = word.split("");
        letterInGuessedWord = guessedWord.split("");
        inGame = true;
    }

    public String playGame(String letter){
        letter = letter.toUpperCase();
        if (letter.length() > 1) {
            if (word.equals(letter)) guessedWord = letter;
        } else {
            for (int i = 0; i < letterInWord.length; i++) {
                if (letterInWord[i].equals(letter)) {
                    letterInGuessedWord[i] = letter;
                }
            }
            guessedWord = String.join("", letterInGuessedWord);
        }
        inGame = guessedWord.contains("*");
        return guessedWord;
    }

    public boolean gameStatus() {
        return inGame;
    }

    public String getWord() {
        return word;
    }

    public String getGuessedWord() {
        return guessedWord;
    }
}
