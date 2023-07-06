package org.example.db.dao;

import org.example.statistics.model.PlayerStatsNode;
import org.example.statistics.model.UserDTO;

import java.sql.*;
import java.util.Objects;

public class PlayerStatsDAOImpl implements PlayerStatsDAO {
    private Connection connection;

    public PlayerStatsDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean insert(UserDTO userDTO, PlayerStatsNode playerStatsNode) {

        if (Objects.nonNull(userDTO) || Objects.nonNull(playerStatsNode)) {

            final String insertQuery = "INSERT INTO userStatistics (id, username, registration_date, count_of_guessed_letters, " +
                    "count_of_unguessed_letters, count_of_guessed_whole_words, count_of_unguessed_whole_words) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setLong(1, userDTO.getId());
                preparedStatement.setString(2, userDTO.getUsername());
                preparedStatement.setDate(3, Date.valueOf(userDTO.getRegistrationDate()));
                preparedStatement.setInt(4, playerStatsNode.getCountOfGuessedLetters());
                preparedStatement.setInt(5, playerStatsNode.getCountOfUnguessedLetters());
                preparedStatement.setInt(6, playerStatsNode.getCountOfGuessedWholeWords());
                preparedStatement.setInt(7, playerStatsNode.getCountOfUnguessedWholeWords());
                return preparedStatement.execute();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    public boolean updateStatistics(UserDTO userDTO, PlayerStatsNode playerStatsNode) {
        if (Objects.nonNull(userDTO) || Objects.nonNull(playerStatsNode)) {
            String updateQuery = "UPDATE userStatistics SET count_of_guessed_letters = ?, count_of_unguessed_letters = ?, " +
                    "count_of_guessed_whole_words = ?, count_of_unguessed_whole_words = ? WHERE id = ?";
            long userId = userDTO.getId();
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

        return false;
    }

    @Override
    public PlayerStatsNode getStatisticsFromDB(UserDTO userDTO) {

        if (Objects.nonNull(userDTO)) {
            String selectQuery = "SELECT count_of_guessed_letters, count_of_unguessed_letters, " +
                    "count_of_guessed_whole_words, count_of_unguessed_whole_words FROM userStatistics  WHERE id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                long userId = userDTO.getId();
                preparedStatement.setLong(1, userId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {

                    if (resultSet.next()) {
                        int countOfGuessedLetters = resultSet.getInt(1);
                        int countOfUnguessedLetters = resultSet.getInt(2);
                        int countOfGuessedWholeWords = resultSet.getInt(3);
                        int countOfUnguessedWholeWords = resultSet.getInt(4);
                        return new PlayerStatsNode(countOfGuessedLetters, countOfUnguessedLetters, countOfGuessedWholeWords, countOfUnguessedWholeWords);
                    }

                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }

    @Override
    public boolean userExists(UserDTO user) {
        if (Objects.nonNull(user)) {
            final String selectQuery = "SELECT id FROM userStatistics WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setLong(1, user.getId());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return false;
    }
}
