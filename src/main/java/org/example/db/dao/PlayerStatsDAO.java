package org.example.db.dao;

import org.example.statistics.model.PlayerStatsNode;
import org.example.statistics.model.UserDTO;

public interface PlayerStatsDAO {
    boolean insert(UserDTO userDTO, PlayerStatsNode playerStatsNode);

    boolean userExists(UserDTO user);

    PlayerStatsNode getStatisticsFromDB(UserDTO userDTO);

    boolean updateStatistics(UserDTO userDTO, PlayerStatsNode playerStatsNode);
}
