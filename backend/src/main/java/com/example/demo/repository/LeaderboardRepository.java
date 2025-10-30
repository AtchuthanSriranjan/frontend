package com.example.demo.repository;

import com.example.demo.model.LeaderboardEntry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class LeaderboardRepository {

    // JdbcTemplate for database operations
    private final JdbcTemplate jdbcTemplate;

    public LeaderboardRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Map a row from the ResultSet to a LeaderboardEntry object
    private LeaderboardEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new LeaderboardEntry(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getInt("time_seconds"),
                rs.getInt("size"),
                rs.getString("board_type"),
                rs.getTimestamp("solved_at").toLocalDateTime()
        );
    }

    // Save a new leaderboard entry
    public void save(LeaderboardEntry entry) {
        jdbcTemplate.update(
                "INSERT INTO leaderboard (name, time_seconds, size, board_type) VALUES (?, ?, ?, ?)",
                entry.getName(), entry.getTimeSeconds(), entry.getSize(), entry.getBoardType()
        );
    }

    // Fetch top 10 entries ordered by timeSeconds ascending and solvedAt ascending
    public List<LeaderboardEntry> findTop10() {
        return jdbcTemplate.query(
                "SELECT * FROM leaderboard ORDER BY time_seconds ASC, solved_at ASC LIMIT 10",
                this::mapRow
        );
    }
}
