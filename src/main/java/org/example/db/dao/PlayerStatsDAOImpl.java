package org.example.db.dao;

import org.example.statistics.model.PlayerStatsNode;
import org.example.statistics.model.UserDTO;

import java.sql.*;

public class PlayerStatsDAOImpl implements PlayerStatsDAO {
    private Connection connection;

    public PlayerStatsDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean insert(UserDTO userDTO, PlayerStatsNode playerStatsNode) {
        final String insetQuery = "INSERT INTO userStatistics (id, username, registration_date, count_of_guessed_letters, " +
                "count_of_unguessed_letters, count_of_guessed_whole_words, count_of_unguessed_whole_words) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insetQuery)) {
            if (!userExist(userDTO)) {
                preparedStatement.setLong(1, userDTO.getId());
                preparedStatement.setString(2, userDTO.getUsername());
                preparedStatement.setDate(3, Date.valueOf(userDTO.getRegistrationDate()));
                preparedStatement.setInt(4, playerStatsNode.getCountOfGuessedLetters());
                preparedStatement.setInt(5, playerStatsNode.getCountOfUnguessedLetters());
                preparedStatement.setInt(6, playerStatsNode.getCountOfGuessedWholeWords());
                preparedStatement.setInt(7, playerStatsNode.getCountOfUnguessedWholeWords());
                preparedStatement.execute();
            } else {
                long userId = userDTO.getId();
                updateStatistics(userId, playerStatsNode);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    private void updateStatistics(long userId, PlayerStatsNode statsFromLastGame) {
        String updateQuery = "UPDATE userStatistics SET count_of_guessed_letters = ?, count_of_unguessed_letters = ?, " +
                "count_of_guessed_whole_words = ?, count_of_unguessed_whole_words = ? WHERE id = ?";
        // 1 Получить данные от юзера
        PlayerStatsNode statsFromDB = selectStatistics(userId);
        // 2 Суммировать данные
        statsFromDB.setCountOfGuessedLetters(statsFromLastGame.getCountOfGuessedLetters() + statsFromDB.getCountOfGuessedLetters());
        statsFromDB.setCountOfUnguessedLetters(statsFromLastGame.getCountOfUnguessedLetters() + statsFromDB.getCountOfUnguessedLetters());
        statsFromDB.setCountOfGuessedWholeWords(statsFromLastGame.getCountOfGuessedWholeWords() + statsFromDB.getCountOfGuessedWholeWords());
        statsFromDB.setCountOfUnguessedWholeWords(statsFromLastGame.getCountOfUnguessedWholeWords() + statsFromDB.getCountOfUnguessedWholeWords());

        statsFromLastGame.setCountOfGuessedLetters(statsFromDB.getCountOfGuessedLetters());
        statsFromLastGame.setCountOfUnguessedLetters(statsFromDB.getCountOfUnguessedLetters());
        statsFromLastGame.setCountOfGuessedWholeWords(statsFromDB.getCountOfGuessedWholeWords());
        statsFromLastGame.setCountOfUnguessedWholeWords(statsFromDB.getCountOfUnguessedWholeWords());

        // 3 Сделать update c четом новых данных
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setInt(1, statsFromDB.getCountOfGuessedLetters());
            preparedStatement.setInt(2, statsFromDB.getCountOfUnguessedLetters());
            preparedStatement.setInt(3, statsFromDB.getCountOfGuessedWholeWords());
            preparedStatement.setInt(4, statsFromDB.getCountOfUnguessedLetters());
            preparedStatement.setLong(5, userId);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public PlayerStatsNode selectStatistics(long userId) {
        String selectQuery = "SELECT count_of_guessed_letters, count_of_unguessed_letters, " +
                "count_of_guessed_whole_words, count_of_unguessed_whole_words FROM userStatistics  WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setLong(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();

                int countOfGuessedLetters = resultSet.getInt(1);
                int countOfUnguessedLetters = resultSet.getInt(2);
                int countOfGuessedWholeWords = resultSet.getInt(3);
                int countOfUnguessedWholeWords = resultSet.getInt(4);

                return new PlayerStatsNode(countOfGuessedLetters, countOfUnguessedLetters, countOfGuessedWholeWords, countOfUnguessedWholeWords);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean userExist(UserDTO user) {
        final String insetQuery = "SELECT id FROM userStatistics WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insetQuery)) {
            preparedStatement.setLong(1, user.getId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int userID = resultSet.getInt(1);
                    return userID == user.getId();
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
