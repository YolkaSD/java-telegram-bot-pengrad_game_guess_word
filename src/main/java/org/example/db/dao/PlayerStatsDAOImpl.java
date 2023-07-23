package org.example.db.dao;

import org.example.statistics.model.PlayerStatsDTO;
import java.sql.*;
import java.time.LocalDate;

public class PlayerStatsDAOImpl implements PlayerStatsDAO {
    private Connection connection;

    public PlayerStatsDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean insert(long userId, PlayerStatsDTO playerStatsNode) {
        final String insertQuery = "INSERT INTO telegram.userStatistics (id, username, registration_date, count_of_guessed_letters, " +
                "count_of_unguessed_letters, count_of_guessed_whole_words, count_of_unguessed_whole_words) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setString(2, playerStatsNode.getUserName());
            preparedStatement.setDate(3, Date.valueOf(LocalDate.now()));
            preparedStatement.setInt(4, playerStatsNode.getCountOfGuessedLetters());
            preparedStatement.setInt(5, playerStatsNode.getCountOfUnguessedLetters());
            preparedStatement.setInt(6, playerStatsNode.getCountOfGuessedWholeWords());
            preparedStatement.setInt(7, playerStatsNode.getCountOfUnguessedWholeWords());
            return preparedStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override

    public boolean updateStatistics(long userId, PlayerStatsDTO playerStatsNode) {

        String updateQuery = "UPDATE telegram.userStatistics SET count_of_guessed_letters = ?, count_of_unguessed_letters = ?, " +
                "count_of_guessed_whole_words = ?, count_of_unguessed_whole_words = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setInt(1, playerStatsNode.getCountOfGuessedLetters());
            preparedStatement.setInt(2, playerStatsNode.getCountOfUnguessedLetters());
            preparedStatement.setInt(3, playerStatsNode.getCountOfGuessedWholeWords());
            preparedStatement.setInt(4, playerStatsNode.getCountOfUnguessedLetters());
            preparedStatement.setLong(5, userId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public PlayerStatsDTO getStatisticsFromDB(long userId) {
        String selectQuery = "SELECT count_of_guessed_letters, count_of_unguessed_letters, " +
                "count_of_guessed_whole_words, count_of_unguessed_whole_words FROM telegram.userStatistics  WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setLong(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    int countOfGuessedLetters = resultSet.getInt(1);
                    int countOfUnguessedLetters = resultSet.getInt(2);
                    int countOfGuessedWholeWords = resultSet.getInt(3);
                    int countOfUnguessedWholeWords = resultSet.getInt(4);
                    return new PlayerStatsDTO(countOfGuessedLetters, countOfUnguessedLetters, countOfGuessedWholeWords, countOfUnguessedWholeWords);
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public boolean userExists(long userId) {
        final String selectQuery = "SELECT id FROM telegram.userStatistics WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setLong(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
