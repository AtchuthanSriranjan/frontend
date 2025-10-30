package com.example.demo.model;

import java.time.LocalDateTime;

public class LeaderboardEntry {

    private Long id;
    private String name;
    private Integer timeSeconds;
    private Integer size;
    private String boardType;
    private LocalDateTime solvedAt;

    public LeaderboardEntry() {}

    public LeaderboardEntry(Long id, String name, Integer timeSeconds, Integer size,
                            String boardType, LocalDateTime solvedAt) {
        this.id = id;
        this.name = name;
        this.timeSeconds = timeSeconds;
        this.size = size;
        this.boardType = boardType;
        this.solvedAt = solvedAt;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getTimeSeconds() { return timeSeconds; }
    public void setTimeSeconds(Integer timeSeconds) { this.timeSeconds = timeSeconds; }

    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }

    public String getBoardType() { return boardType; }
    public void setBoardType(String boardType) { this.boardType = boardType; }

    public LocalDateTime getSolvedAt() { return solvedAt; }
    public void setSolvedAt(LocalDateTime solvedAt) { this.solvedAt = solvedAt; }
}
