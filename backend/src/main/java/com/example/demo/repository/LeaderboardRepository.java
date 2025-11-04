package com.example.demo.repository;

import com.example.demo.model.LeaderboardEntry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class LeaderboardRepository {

    private final JdbcTemplate jdbcTemplate;

    public LeaderboardRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(LeaderboardEntry entry) {
        String sql = "INSERT INTO leaderboard (name, time_seconds, size, board_type, solved_at) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
        jdbcTemplate.update(sql,
                entry.getName(),
                entry.getTimeSeconds(),
                entry.getSize(),
                entry.getBoardType());
    }

    public LeaderboardEntry saveAndReturn(LeaderboardEntry entry) {
        String sql = "INSERT INTO leaderboard (name, time_seconds, size, board_type, solved_at) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
        org.springframework.jdbc.support.GeneratedKeyHolder keyHolder = new org.springframework.jdbc.support.GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            java.sql.PreparedStatement ps = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entry.getName());
            ps.setInt(2, entry.getTimeSeconds() != null ? entry.getTimeSeconds() : 0);
            ps.setInt(3, entry.getSize() != null ? entry.getSize() : 0);
            ps.setString(4, entry.getBoardType());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null) {
            // Fallback: return last inserted row
            return jdbcTemplate.query("SELECT id, name, time_seconds, size, board_type, solved_at FROM leaderboard ORDER BY id DESC LIMIT 1",
                    rs -> rs.next() ? rowMapper().mapRow(rs, 1) : null);
        }

        Long id = key.longValue();
        List<LeaderboardEntry> list = jdbcTemplate.query(
                "SELECT id, name, time_seconds, size, board_type, solved_at FROM leaderboard WHERE id = ?",
                rowMapper(), id);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<LeaderboardEntry> findBySizeOrderByTimeSecondsAsc(Integer size) {
        String sql = "SELECT id, name, time_seconds, size, board_type, solved_at FROM leaderboard WHERE size = ? ORDER BY time_seconds ASC LIMIT 10";
        return jdbcTemplate.query(sql, rowMapper(), size);
    }

    public List<LeaderboardEntry> findTop10() {
        String sql = "SELECT id, name, time_seconds, size, board_type, solved_at FROM leaderboard ORDER BY time_seconds ASC LIMIT 10";
        return jdbcTemplate.query(sql, rowMapper());
    }

    public List<LeaderboardEntry> findTop10BySize(int size) {
        return findBySizeOrderByTimeSecondsAsc(size);
    }

    private RowMapper<LeaderboardEntry> rowMapper() {
        return (rs, rowNum) -> {
            LeaderboardEntry e = new LeaderboardEntry();
            e.setId(rs.getLong("id"));
            e.setName(rs.getString("name"));
            e.setTimeSeconds(rs.getInt("time_seconds"));
            e.setSize(rs.getInt("size"));
            e.setBoardType(rs.getString("board_type"));
            Timestamp ts = rs.getTimestamp("solved_at");
            if (ts != null) {
                e.setSolvedAt(ts.toLocalDateTime());
            }
            return e;
        };
    }
}
