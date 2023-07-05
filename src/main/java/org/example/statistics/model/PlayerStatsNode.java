package org.example.statistics.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter
@NoArgsConstructor
public class PlayerStatsNode {
    private int countOfGuessedLetters = 0; // Количество угаданных букв
    private int countOfUnguessedLetters = 0; // Количество неугаданных букв
    private int countOfGuessedWholeWords = 0; // Количество угаданных целых слов
    private int countOfUnguessedWholeWords = 0; // Количество неугаданных целых слов
    private double ratioOfSuccessfulAttemptsToAllAttempts = 0; // Соотношение успешных попыток к общему числу попыток (в процентах)

    public PlayerStatsNode(int countOfGuessedLetters, int countOfUnguessedLetters, int countOfGuessedWholeWords, int countOfUnguessedWholeWords) {
        this.countOfGuessedLetters = countOfGuessedLetters;
        this.countOfUnguessedLetters = countOfUnguessedLetters;
        this.countOfGuessedWholeWords = countOfGuessedWholeWords;
        this.countOfUnguessedWholeWords = countOfUnguessedWholeWords;
        updateRatioOfSuccessfulAttemptsToAllAttempts();
    }

    public void incrementCountOfGuessedLetters(){
        countOfGuessedLetters += 1;
        updateRatioOfSuccessfulAttemptsToAllAttempts();
    }
    public void incrementCountOfUnguessedLetters(){
        countOfUnguessedLetters += 1;
        updateRatioOfSuccessfulAttemptsToAllAttempts();
    }
    public void incrementCountOfGuessedWholeWords(){
        countOfGuessedWholeWords += 1;
        updateRatioOfSuccessfulAttemptsToAllAttempts();
    }
    public void incrementCountOfUnguessedWholeWords(){
        countOfUnguessedWholeWords += 1;
        updateRatioOfSuccessfulAttemptsToAllAttempts();
    }

    private void updateRatioOfSuccessfulAttemptsToAllAttempts() {
        ratioOfSuccessfulAttemptsToAllAttempts = (double)
                (countOfGuessedLetters + countOfGuessedWholeWords) /
                (countOfGuessedLetters + countOfUnguessedLetters + countOfGuessedWholeWords + countOfUnguessedWholeWords);
    }


}
