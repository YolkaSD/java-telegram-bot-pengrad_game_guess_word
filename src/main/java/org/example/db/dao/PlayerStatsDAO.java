package org.example.db.dao;

import org.example.statistics.model.PlayerStatsNode;
import org.example.statistics.model.UserDTO;

import java.util.Map;

public interface PlayerStatsDAO {
    boolean insert(UserDTO userDTO, PlayerStatsNode playerStatsNode);
}
