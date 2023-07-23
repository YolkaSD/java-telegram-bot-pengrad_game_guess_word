package org.example.db.dao;

import org.example.statistics.model.PlayerStatsDTO;

public interface PlayerStatsDAO {
    boolean insert(long userId, PlayerStatsDTO playerStatsNode);

    boolean userExists(long userId);

    PlayerStatsDTO getStatisticsFromDB(long userId);

    boolean updateStatistics(long userId, PlayerStatsDTO playerStatsNode);
}
